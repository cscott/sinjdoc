// TemplateWriter.java, created Mon Mar 31 19:14:35 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cscott@cscott.net>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.DocErrorReporter;

import java.io.*;
/**
 * The <code>TemplateWriter</code> class emits chunks of template
 * interspersed with customized chunks.  It performs macro substitution
 * of the template as well.
 * 
 * @author  C. Scott Ananian <cscott@cscott.net>
 * @version $Id$
 */
class TemplateWriter extends PrintWriter  {
    final Reader templateReader;
    final HTMLOptions options;
    final URLContext context;

    /** Creates a <code>TemplateWriter</code>. */
    TemplateWriter(Writer delegate,
		   Reader templateReader, HTMLOptions options,
		   URLContext context) {
        super(delegate);
	this.templateReader = templateReader;
	this.options = options;
	this.context = context;
    }
    public void copyRemainder(DocErrorReporter reporter) {
	try {
	    while (copyToSplit())
		/* repeat */;
	    flush();
	    close();
	    templateReader.close();
	} catch (IOException e) {
	    reporter.printError("Couldn't emit "+context+": "+e);
	}
    }
    /** Read from the template, performing macro substition, until the
     *  occurrence of the string @SPLIT@ or end-of-file, whichever comes
     *  first.
     * @return true if split found, false if EOF found first. */
    public boolean copyToSplit() throws IOException {
	char[] buf = new char[1024];
	int r; boolean eof=false;
	while (!eof) {
	    r = templateReader.read();
	    if (r<0) { eof=true; break; /* end of stream */}
	    if (r!='@') { write(r); continue; }
	    // ooh, ooh, saw a '@'
	    StringBuffer tag = new StringBuffer("@");
	    while (!eof) {
		r = templateReader.read();
		if (r<0) { eof=true; write(tag.toString()); break; }
		tag.append((char)r);
		if (Character.isLetter((char)r) && r!='@') continue;
		// saw closing '@'.  is this a valid tag?
		String tagName = tag.toString();
		if (tagName.equals("@CHARSET@"))
		    write(options.charSet.name());
		else if (tagName.equals("@WINDOWTITLE@")) {
		    if (options.windowTitle!=null)
			write(options.windowTitle);
		} else if (tagName.equals("@TITLESUFFIX@")) {
		    if (options.windowTitle!=null)
			write(" ("+options.windowTitle+")");
		} else if (tagName.equals("@DOCTITLE@")) {
		    if (options.docTitle!=null)
			write(options.docTitle);
		} else if (tagName.equals("@ROOT@")) {
		    write(context.makeRelative(""));
		} else if (tagName.equals("@SPLIT"))
		    return true; // done.
		else // invalid tag.
		    write(tag.toString());
		break;
	    }
	}
	return false; // eof found.
    }
}
