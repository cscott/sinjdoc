// TemplateWriter.java, created Mon Mar 31 19:14:35 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cscott@cscott.net>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.DocErrorReporter;

import java.io.*;
import java.util.*;
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
		if (tagName.equals("@SPLIT@"))
		    return true; // done.
		else if (macroMap.containsKey(tagName))
		    macroMap.get(tagName).process(this, this.context);
		else // invalid tag.
		    write(tag.toString());
		break;
	    }
	}
	return false; // eof found.
    }

    /** Encapsulates a macro definition. */
    static abstract class TemplateAction {
	abstract void process(TemplateWriter tw, TemplateContext context);
    }
    /** A map from macro names to definitions. */
    private static final Map<String, TemplateAction> macroMap =
	new HashMap<String,TemplateAction>();
    /** Convenience method to register macro definitions. */
    private static final void register(String name, TemplateAction action) {
	assert name.startsWith("@") && name.endsWith("@");
	macroMap.put(name, action);
    }
    static {
	// macro definitions.  java is so noisy!
	register("@CHARSET@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    tw.write(context.options.charSet.name());
		}
	    });
	register("@WINDOWTITLE@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.windowTitle==null) return;
		    tw.write(context.options.windowTitle);
		}
	    });
	register("@TITLESUFFIX@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.windowTitle==null) return;
		    tw.write(" (");
		    tw.write(context.options.windowTitle);
		    tw.write(")");
		}
	    });
	register("@DOCTITLE@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.docTitle==null) return;
		    tw.write(context.options.docTitle);
		}
	    });
	register("@ROOT@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    tw.write(context.curURL.makeRelative(""));
		}
	    });
	register("@PKGNAME@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curPackage!=null;
		    tw.write(context.curPackage.name());
		}
	    });
	register("@PKGSUMMARYLINK@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curPackage!=null;
		    tw.write(HTMLUtil.toLink(context.curURL,context.curPackage,
					     "package-summary.html"));
		}
	    });
    }
}
