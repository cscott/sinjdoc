// HTMLUtil.java, created Mon Mar 31 13:43:45 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import java.nio.charset.*;
import java.io.*;
/**
 * The <code>HTMLUtil</code> class encapsulates several generally-used
 * functions used by the <code>HTMLDoclet</code>.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */

abstract class HTMLUtil {
    /** Copy the contents of a <code>Reader</code> to a <code>Writer</code>.
     */
    static void copy(Reader r, Writer w) throws IOException {
	char[] buf = new char[1024];
	while (true) {
	    int st = r.read(buf, 0, buf.length);
	    if (st<0) break; // end of stream.
	    w.write(buf, 0, st);
	}
	r.close();
	w.close();
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
    static PrintWriter fileWriter(String url, HTMLOptions options)
	throws FileNotFoundException {
	return fileWriter(new URLContext(url), options);
    }
    /** Return a <code>Writer</code> for the document with the given
     *  <code>URLContext</code>. */
    static PrintWriter fileWriter(URLContext url, HTMLOptions options)
	throws FileNotFoundException {
	File f = new File(options.docRoot, url.toFile().toString());
	// make directory for file.
	if (f.getParentFile()!=null)
	    f.getParentFile().mkdirs();
	// now make a proper escaping-writer.
	assert options.charSet!=null;
	CharsetEncoder encoder = options.charSet.newEncoder();
	return new PrintWriter
	    (new HTMLWriter
	     (new BufferedWriter
	      (new OutputStreamWriter
	       (new FileOutputStream(f), encoder)), encoder));
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
	    CharSequence cs = new ImmutableCharSequence(cbuf, off, len);
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
	private static class ImmutableCharSequence implements CharSequence {
	    private final char[] buf; final int off, len;
	    ImmutableCharSequence(char[] buf, int off, int len) {
		this.buf = buf; this.off = off; this.len = len;
	    }
	    public char charAt(int index) { return buf[off+index]; }
	    public int length() { return len; }
	    public String toString() { return new String(buf, off, len); }
	    public CharSequence subSequence(int start, int end) {
		return new ImmutableCharSequence(buf, off+start, end-start);
	    }
	}
    }
}// HTMLUtil
