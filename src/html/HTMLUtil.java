// HTMLUtil.java, created Mon Mar 31 13:43:45 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.DocErrorReporter;

import java.nio.charset.*;
import java.io.*;
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
	// make directory for file.
	if (f.getParentFile()!=null)
	    f.getParentFile().mkdirs();
	// now make a proper escaping-writer.
	assert options.charSet!=null;
	CharsetEncoder encoder = options.charSet.newEncoder();
	try {
	    return new PrintWriter
		(new HTMLWriter
		 (new BufferedWriter
		  (new OutputStreamWriter
		   (new FileOutputStream(f), encoder)), encoder));
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