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
    // helper functions.
    /** Returns the topmost context for this <code>TemplateWriter</code>. */
    private TemplateContext topContext() {
	return contextStack.get(0).right.get(0);
    }
    /** Returns false if the <code>TemplateWriter</code> is currently
     *  suppressing output. */
    private boolean isEcho() {
	return contextStack.peek().right.size() > 0;
    }
    /** Copy all remaining text from the template and close the files. */
    public void copyRemainder(DocErrorReporter reporter) {
	try {
	    copyRemainder();
	} catch (IOException e) {
	    reporter.printError("Couldn't emit "+topContext().curURL+": "+e);
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
	    reporter.printError("Couldn't emit "+topContext().curURL+": "+e);
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
	    assert contextStack.size()==1 : "unmatched blocks.";
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
		    if (isEcho()) return true; // done.
		} else if (tagName.equals("@END@")) {
		    // repeat from mark or pop context.
		    if (contextStack.peek().right.size()>1) {
			// remove first element from context list.
			Pair<Mark,List<TemplateContext>> pair =
			    contextStack.pop();
			contextStack.push(new Pair<Mark,List<TemplateContext>>
					  (pair.left, pair.right.subList
					   (1,pair.right.size())));
			// reset reader to mark.
			templateReader.reset(pair.left);
		    } else if (contextStack.size()>1) {
			// done with this block. pop context.
			contextStack.pop();
		    } else assert false : "too many @END@ tags";
		} else if (macroMap.containsKey(tagName)) {
		    TemplateContext context = isEcho() ?
			contextStack.peek().right.get(0) : null;
		    List<TemplateContext> ltc =
			macroMap.get(tagName).doMacro(this, context);
		    if (ltc!=null) {
			// only need to make mark if ltc.size()>1.
			Mark m = (ltc.size()>1) ? templateReader.getMark()
			    : null;
			contextStack.push(new Pair<Mark,List<TemplateContext>>
					  (m, ltc));
		    }
		} else // invalid tag.
		    if (isEcho()) write(tag.toString());
		break;
	    }
	}
	return false; // eof found.
    }

    /** Encapsulates a macro definition. */
    static abstract class TemplateMacro {
	/** This is the most general interface to the macro-expansion
	 *  engine.  The method should write to <code>tw</code> whatever
	 *  is necessary for the expansion of the macro.  If the
	 *  return value is non-null, the template text between this
	 *  macro and a corresponding <code>@END@</code> macro will
	 *  be repeated once for every element in the
	 *  <code>TemplateContext</code> list.  Note that returning
	 *  a list of size 0 is allowed, and suppresses output until the
	 *  matching <code>@END@</code> macro is found.  The
	 *  <code>context</code> parameter will be <code>null</code> if
	 *  output is currently suppressed. */
	abstract List<TemplateContext> doMacro
	    (TemplateWriter tw, TemplateContext context);
    }
    static abstract class TemplateAction extends TemplateMacro {
	final List<TemplateContext> doMacro
	    (TemplateWriter tw, TemplateContext context) {
	    // process the macro...
	    if (context!=null) process(tw, context);
	    // no new contexts added, so return null.
	    return null;
	}
	/** Expand the macro by writing to <code>tw</code>.  This method
	 *  will only be invoked if output is not currently suppressed. */
	abstract void process(TemplateWriter tw, TemplateContext context);
    }
    static abstract class TemplateForEach extends TemplateMacro {
	final List<TemplateContext> doMacro
	    (TemplateWriter tw, TemplateContext context) {
	    // don't really push new contexts if we're not currently echoing.
	    if (context==null) return EMPTY_CONTEXT_LIST;
	    // otherwise, go ahead and process this.
	    return process(tw, context);
	}
	/** Return a list of <code>TemplateContext</code>s; each one will
	 *  be used in turn to process this block.  So if you return a
	 *  list of size two, the block will be repeated twice, etc.  This
	 *  method will only be invoked if output is not currently
	 *  suppressed. */
	abstract List<TemplateContext> process
	    (TemplateWriter tw, TemplateContext context);
	/** An empty list object, static for efficiency. */
	static final List<TemplateContext> EMPTY_CONTEXT_LIST =
	    Arrays.asList(new TemplateContext[0]);
    }
    static abstract class TemplateConditional extends TemplateForEach {
	final List<TemplateContext> process
	    (TemplateWriter tw, TemplateContext context) {
	    assert context!=null;
	    if (!isBlockEmitted(context)) return EMPTY_CONTEXT_LIST;
	    else return Collections.singletonList(context);
	}
	/** Return true if this block following this conditional should be
	 *  emitted, else return false.  This method will only be invoked
	 *  if output is not currently suppressed. */
	abstract boolean isBlockEmitted(TemplateContext context);
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
