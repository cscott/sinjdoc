// HTMLUtil.java, created Mon Mar 31 13:43:45 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.ClassTypeVariable;
import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.PackageDoc;
import net.cscott.gjdoc.RootDoc;

import java.nio.charset.CharsetEncoder;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
/**
 * The <code>HTMLUtil</code> class encapsulates several generally-used
 * functions used by the <code>HTMLDoclet</code>.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */

class HTMLUtil {
    DocErrorReporter reporter;
    HTMLUtil(DocErrorReporter reporter) { this.reporter = reporter; }

    /** Collect all documented packages. */
    public static List<PackageDoc> allDocumentedPackages(RootDoc root) {
	// first collect all referenced packages.
	Map<String,PackageDoc> pkgMap = new LinkedHashMap<String,PackageDoc>();
	for (Iterator<ClassDoc> it=root.classes().iterator(); it.hasNext(); ) {
	    PackageDoc pd = it.next().containingPackage();
	    pkgMap.put(pd.name(), pd);
	}
	Collection<PackageDoc> c = pkgMap.values();
	return Arrays.asList(c.toArray(new PackageDoc[c.size()]));
    }

    /** Construct the URL for a page corresponding to the specified class. */
    public static String toURL(ClassDoc c) {
	StringBuffer sb = new StringBuffer();
	for (ClassDoc p=c; p!=null; ) {
	    sb.insert(0, p.name());
	    p = p.containingClass();
	    if (p!=null) sb.insert(0, '.');
	}
	sb.insert(0, toBaseURL(c.containingPackage()));
	sb.append(".html");
	return sb.toString();
    }
    /** Construct the URL for the package-summary page for the given package.
     *  The pageName parameter should be one of "package-summary.html",
     *  "package-frame.html", or "package-tree.html". */
    public static String toURL(PackageDoc p, String pageName) {
	assert Pattern.matches("package-(summary|frame|tree)\\.html",pageName);
	return toBaseURL(p)+pageName;
    }
    /** Construct the URL representing the *directory* information
     *  regarding the specified package (including pages for classes
     *  in that package) should be stored in. */
    public static String toBaseURL(PackageDoc p) {
	String name = p.name();
	if (name.length()==0) return name;
	return name.replace('.','/')+"/";
    }
    /** Return a link to the specified package-related page in the package.
     *  The pageName parameter should be one of "package-summary.html",
     *  "package-frame.html", or "package-tree.html". */
    public static String toLink(URLContext context, PackageDoc p,
				String pageName) {
	StringBuffer sb = new StringBuffer("<a href=\"");
	sb.append(context.makeRelative(toURL(p, pageName)));
	sb.append("\" class=\"packageRef\">");
	if (p.name().length()==0)
	    sb.append("&lt;unnamed package&gt;");
	else
	    sb.append(p.name());
	sb.append("</a>");
	return sb.toString();
    }
    public static String toLink(URLContext context, ClassDoc c,
				boolean withParam) {
	StringBuffer sb = new StringBuffer("<a href=\"");
	sb.append(context.makeRelative(toURL(c)));
	sb.append("\" class=\"");
	if (c.isInterface()) sb.append("interfaceRef");
	else if (c.isError()) sb.append("errorRef");
	else if (c.isException()) sb.append("exceptionRef");
	else sb.append("classRef");
	sb.append("\">");
	int idx = sb.length();
	sb.append(c.name());
	for (ClassDoc p=c.containingClass(); p!=null; p=p.containingClass())
	    sb.insert(idx, p.name()+"."); // parent class also in link text
	if (withParam && c.typeParameters().size()>0) {
	    sb.append("&lt;");
	    for (Iterator<ClassTypeVariable> it=c.typeParameters().iterator();
		 it.hasNext(); ) {
		sb.append(it.next().getName());
		if (it.hasNext()) sb.append(",");
	    }
	    sb.append("&gt;");
	}
	sb.append("</a>");
	return sb.toString();
    }
    /** Copy the contents of a <code>Reader</code> to a <code>Writer</code>.
     */
    void copy(Reader r, Writer w) {
	try {
	    char[] buf = new char[1024];
	    while (true) {
		int st = r.read(buf, 0, buf.length);
		if (st<0) break; // end of stream.
		w.write(buf, 0, st);
	    }
	    r.close();
	    w.close();
	} catch (IOException e) {
	    reporter.printError(e.toString());
	}
    }

    /** Return a <code>Reader</code> for a resource stored at
     *  <code>net.cscott.gjdoc.html.templates</code>. */
    static Reader resourceReader(String resource) {
	InputStream is = HTMLUtil.class.getResourceAsStream
	    ("templates/"+resource);
	if (is==null) return null;
	try {
	    return new InputStreamReader(is, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    assert false : "UTF-8 should always be supported";
	    return null;
	}
    }

    /** Return a <code>Writer</code> for the document at the given URL. */
    PrintWriter fileWriter(String url, HTMLOptions options) {
	return fileWriter(new URLContext(url), options);
    }
    /** Return a <code>Writer</code> for the document with the given
     *  <code>URLContext</code>. */
    PrintWriter fileWriter(URLContext url, HTMLOptions options) {
	File f = new File(options.docRoot, url.toFile().toString());
	reporter.printNotice("Generating "+f+"...");
	// make directory for file.
	if (f.getParentFile()!=null)
	    f.getParentFile().mkdirs();
	// now make a proper escaping-writer.
	assert options.charSet!=null;
	CharsetEncoder encoder1 = options.charSet.newEncoder();
	CharsetEncoder encoder2 = options.charSet.newEncoder();
	// can't share these encoders or else we'll eventually get an
	// IllegalStateException from the canEncode() method when we
	// (in HTMLWriter.write) interrupt a coding operation in
	// OutputStreamWriter.write().
	try {
	    return new PrintWriter
		(new HTMLWriter
		 (new BufferedWriter
		  (new OutputStreamWriter
		   (new FileOutputStream(f), encoder1)), encoder2));
	} catch (FileNotFoundException e) {
	    reporter.printError("Couldn't open file "+f+": "+e.toString());
	    return new PrintWriter(new NullWriter());
	}
    }

    /** Escapes any un-encodable characters using HTML entity escapes. */
    private static class HTMLWriter extends Writer {
	final Writer delegate;
	final CharsetEncoder encoder;
	HTMLWriter(Writer delegate, CharsetEncoder encoder) {
	    this.delegate = delegate;
	    this.encoder = encoder;
	}
	public void close() throws IOException { delegate.close(); }
	public void flush() throws IOException { delegate.flush(); }
	public void write(char[] cbuf, int off, int len)
	    throws IOException {
	    if (len==0) return; // quickly done.
	    CharSequence cs = new SimpleCharSequence(cbuf, off, len);
	    if (encoder.canEncode(cs))
		delegate.write(cbuf, off, len); // everything okie-dokie.
	    else if (len>1) { // divide and conquer.
		int half = len/2;
		this.write(cbuf, off, half);
		this.write(cbuf, off+half, len-half);
	    } else { // HTML-escape exactly one character.
		delegate.write("&#"+Integer.toString(cbuf[off], 10)+";");
	    }
	}
    }
    /** Throws away all output. */
    private static class NullWriter extends Writer {
	NullWriter() { }
	public void close() { }
	public void flush() { }
	public void write(char[] cbuf, int off, int len) { }
    }
}// HTMLUtil
