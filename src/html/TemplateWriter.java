// TemplateWriter.java, created Mon Mar 31 19:14:35 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cscott@cscott.net>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.html.ReplayReader.Mark;

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
    final Stack<Pair<Mark,List<TemplateContext>>> contextStack =
	new Stack<Pair<Mark,List<TemplateContext>>>();
    final ReplayReader templateReader;

    /** Creates a <code>TemplateWriter</code> which uses the specified
     *  <code>Reader</code> as a template and writes to the provides
     *  <code>Writer</code>.  Macros are expanded using the specified
     *  <code>TemplateContext</code>. */
    TemplateWriter(Writer delegate, Reader templateReader,
		   TemplateContext context) {
        super(delegate);
	this.templateReader = new ReplayReader(templateReader);
	contextStack.push(new Pair<Mark,List<TemplateContext>>
			  (null, Collections.singletonList(context)));
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
    private TemplateContext context() {
	return contextStack.peek().right.get(0);
    }
    /** Copy all remaining text from the template and close the files. */
    public void copyRemainder(DocErrorReporter reporter) {
	try {
	    copyRemainder();
	} catch (IOException e) {
	    reporter.printError("Couldn't emit "+context().curURL+": "+e);
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
	    reporter.printError("Couldn't emit "+context().curURL+": "+e);
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
		if (Character.isJavaIdentifierPart((char)r) && r!='@')
		    continue; // part of the tag, keep going.
		// saw closing '@'.  is this a valid tag?
		String tagName = tag.toString();
		if (tagName.equals("@SPLIT@")) {
		    if (context().echo) return true; // done.
		} else if (tagName.equals("@END@")) {
		    // pop context. & mark.
		    if (contextStack.peek().right.size()>1) {
			Pair<Mark,List<TemplateContext>> opair =
			    contextStack.pop(), npair =
			    new Pair<Mark,List<TemplateContext>>
			    (opair.left,
			     opair.right.subList(1,opair.right.size()));
			contextStack.push(npair);
			// reset to mark.
			templateReader.reset(opair.left);
		    } else if (contextStack.size()>1) {
			contextStack.pop();
		    } else assert false : "too many @END@ tags";
		} else if (macroMap.containsKey(tagName)) {
		    List<TemplateContext> ltc =
			macroMap.get(tagName).doMacro(this, context());
		    if (ltc!=null) {
			// only need to make mark if ltc.size()>1.
			Mark m = (ltc.size()>1) ? templateReader.getMark()
			    : null;
			contextStack.push(new Pair<Mark,List<TemplateContext>>
					  (m, ltc));
		    }
		} else // invalid tag.
		    if (context().echo) write(tag.toString());
		break;
	    }
	}
	return false; // eof found.
    }

    /** Encapsulates a macro definition. */
    static abstract class TemplateMacro {
	abstract List<TemplateContext> doMacro
	    (TemplateWriter tw, TemplateContext context);
    }
    static abstract class TemplateAction extends TemplateMacro {
	final List<TemplateContext> doMacro
	    (TemplateWriter tw, TemplateContext context) {
	    // process the macro...
	    process(tw, context);
	    // no new contexts added, so return null.
	    return null;
	}
	abstract void process(TemplateWriter tw, TemplateContext context);
    }
    static abstract class TemplateConditional extends TemplateMacro {
	final List<TemplateContext> doMacro
	    (TemplateWriter tw, TemplateContext context) {
	    // don't really push new contexts if we're not currently echoing.
	    if (!context.echo) return Collections.singletonList(context);
	    // otherwise, go ahead and process this.
	    return process(tw, context);
	}
	abstract List<TemplateContext> process
	    (TemplateWriter tw, TemplateContext context);
    }
    /** A map from macro names to definitions. */
    private static final Map<String, TemplateMacro> macroMap =
	new HashMap<String,TemplateMacro>();
    /** Convenience method to register macro definitions. */
    private static final void register(String name, TemplateMacro action) {
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
	register("@HEADER@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.header==null) return;
		    tw.write(context.options.header);
		}
	    });
	register("@FOOTER@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.footer==null) return;
		    tw.write(context.options.footer);
		}
	    });
	register("@BOTTOM@", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.bottom==null) return;
		    tw.write(context.options.bottom);
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
