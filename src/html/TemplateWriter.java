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
    final TemplateContext context;

    /** Creates a <code>TemplateWriter</code> which uses the specified
     *  <code>Reader</code> as a template and writes to the provides
     *  <code>Writer</code>.  Macros are expanded using the specified
     *  <code>TemplateContext</code>. */
    TemplateWriter(Writer delegate, Reader templateReader,
		   TemplateContext context) {
        super(delegate);
	this.templateReader = templateReader;
	this.context = context;
    }
    /** Creates a <code>TemplateWriter</code> which uses the reader provided
     *  as a template and writes to the URL specified by the template
     *  context. */
    TemplateWriter(Reader templateReader, HTMLUtil hu,TemplateContext context){
	this(hu.fileWriter(context.curURL, context.options), templateReader,
	     context);
    }
    /** Creates a <code>TemplateWriter</code> which uses the resource with
     *  the specified name as a template, and writes to the URL specified
     *  by the template context. */
    TemplateWriter(String resourceName, HTMLUtil hu, TemplateContext context) {
	this(hu.resourceReader(resourceName), hu, context);
    }
    /** Copy all remaining text from the template and close the files. */
    public void copyRemainder(DocErrorReporter reporter) {
	try {
	    copyRemainder();
	} catch (IOException e) {
	    reporter.printError("Couldn't emit "+context.curURL+": "+e);
	}
    }
    /** Read from the template, performing macro substition, until the
     *  occurrence of the string @SPLIT@ or end-of-file, whichever comes
     *  first.
     * @return true if split found, false if EOF found first. */
    public boolean copyToSplit(DocErrorReporter reporter) {
	try {
	    return copyToSplit();
	} catch (IOException e) {
	    reporter.printError("Couldn't emit "+context.curURL+": "+e);
	    return false;
	}
    }
    /** Copy all remaining text from the template and close the files. */
    void copyRemainder() throws IOException {
	try {
	    while (copyToSplit())
		/* repeat */;
	} finally {
	    close();
	    templateReader.close();
	}
    }
    /** Read from the template, performing macro substition, until the
     *  occurrence of the string @SPLIT@ or end-of-file, whichever comes
     *  first.
     * @return true if split found, false if EOF found first. */
    boolean copyToSplit() throws IOException {
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
		    write(context.options.charSet.name());
		else if (tagName.equals("@WINDOWTITLE@")) {
		    if (context.options.windowTitle!=null)
			write(context.options.windowTitle);
		} else if (tagName.equals("@TITLESUFFIX@")) {
		    if (context.options.windowTitle!=null)
			write(" ("+context.options.windowTitle+")");
		} else if (tagName.equals("@DOCTITLE@")) {
		    if (context.options.docTitle!=null)
			write(context.options.docTitle);
		} else if (tagName.equals("@ROOT@")) {
		    write(context.curURL.makeRelative(""));
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
