// TemplateWriter.java, created Mon Mar 31 19:14:35 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cscott@cscott.net>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.Doc;
import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.PackageDoc;
import net.cscott.gjdoc.Tag;
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
    final Stack<ExtendedContext> contextStack = new Stack<ExtendedContext>();
    final ReplayReader templateReader;

    /** Creates a <code>TemplateWriter</code> which uses the specified
     *  <code>Reader</code> as a template and writes to the provides
     *  <code>Writer</code>.  Macros are expanded using the specified
     *  <code>TemplateContext</code>. */
    TemplateWriter(Writer delegate, Reader templateReader,
		   TemplateContext context) {
        super(delegate);
	this.templateReader = new ReplayReader(templateReader);
	contextStack.push(new ExtendedContext
			  (Collections.singletonList(context),
			   this.templateReader));
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
	return contextStack.get(0).contexts.get(0);
    }
    /** Returns false if the <code>TemplateWriter</code> is currently
     *  suppressing output. */
    private boolean isEcho() { return contextStack.peek().isEcho(); }
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
	    if (r!='@') {
		if (isEcho()) write(r);
		continue;
	    }
	    // ooh, ooh, saw a '@'
	    StringBuffer tag = new StringBuffer("@");
	    while (!eof) {
		r = templateReader.read();
		if (r<0) {
		    eof=true;
		    if (isEcho()) write(tag.toString());
		    break;
		}
		tag.append((char)r);
		if (Character.isJavaIdentifierPart((char)r) && r!='@')
		    continue; // part of the tag, keep going.
		// saw closing '@'.  is this a valid tag?
		String tagName = tag.toString();
		if (tagName.equals("@SPLIT@")) {
		    if (isEcho()) return true; // done.
		} else if (tagName.equals("@END@")) {
		    // repeat from mark or pop context.
		    if (contextStack.peek().contexts.size()>1) {
			// remove first element from context list.
			ExtendedContext ec = contextStack.peek();
			ec.contexts=ec.contexts.subList(1,ec.contexts.size());
			ec.isFirst=false;
			// reset reader to mark.
			templateReader.reset(ec.replayMark);
		    } else if (contextStack.size()>1) {
			// done with this block. pop context.
			contextStack.pop();
		    } else assert false : "too many @END@ tags";
		} else if (macroMap.containsKey(tagName)) {
		    ExtendedContext ec = contextStack.peek();
		    List<TemplateContext> ltc = macroMap.get(tagName).doMacro
			(this, ec.curContext(), ec.isFirst(), ec.isLast());
		    if (ltc!=null)
			contextStack.push(new ExtendedContext
					  (ltc, templateReader));
		} else // invalid tag.
		    if (isEcho()) write(tag.toString());
		break;
	    }
	}
	return false; // eof found.
    }
    /** An extended context object that allows for template conditionals
     *  and repeats. */
    static final class ExtendedContext {
	boolean isFirst=true;
	List<TemplateContext> contexts;
	final ReplayReader.Mark replayMark;
	ExtendedContext(List<TemplateContext> contexts,
			ReplayReader.Mark replayMark) {
	    this.contexts = contexts;
	    this.replayMark = replayMark;
	}
	ExtendedContext(List<TemplateContext> contexts, ReplayReader r) {
	    // only need to make mark if contexts.size()>1.
	    this(contexts, contexts.size()>1 ? r.getMark() : null);
	}
	public boolean isFirst() { return isFirst; }
	public boolean isLast() { return contexts.size()==1; }
	boolean isEcho() { return contexts.size() > 0; }
	public TemplateContext curContext() {
	    return contexts.size()==0 ? null : contexts.get(0);
	}
	public String toString() {
	    return "EC["+isFirst+","+contexts+","+replayMark+"]";
	}
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
	 *  output is currently suppressed. The <code>isFirst</code>
	 *  parameter will be true if this is the first repetition of
	 *  this block.  The <code>isLast</code> parameter will be true
	 *  if this is the last repetition of this block. */
	abstract List<TemplateContext> doMacro
	    (TemplateWriter tw, TemplateContext context,
	     boolean isFirst, boolean isLast);
    }
    static abstract class TemplateAction extends TemplateMacro {
	final List<TemplateContext> doMacro
	    (TemplateWriter tw, TemplateContext context,
	     boolean isFirst, boolean isLast) {
	    // process the macro...
	    if (context!=null) process(tw, context);
	    // no new contexts added, so return null.
	    return null;
	}
	/** Expand the macro by writing to <code>tw</code>.  This method
	 *  will only be invoked if output is not currently suppressed. */
	abstract void process(TemplateWriter tw, TemplateContext context);
    }
    static abstract class TemplateForAll extends TemplateMacro {
	final List<TemplateContext> doMacro
	    (TemplateWriter tw, TemplateContext context,
	     boolean isFirst, boolean isLast) {
	    // don't really push new contexts if we're not currently echoing.
	    if (context==null) return EMPTY_CONTEXT_LIST;
	    // otherwise, go ahead and process this.
	    return process(tw, context, isFirst, isLast);
	}
	/** Return a list of <code>TemplateContext</code>s; each one will
	 *  be used in turn to process this block.  So if you return a
	 *  list of size two, the block will be repeated twice, etc.  This
	 *  method will only be invoked if output is not currently
	 *  suppressed. */
	abstract List<TemplateContext> process
	    (TemplateWriter tw, TemplateContext context,
	     boolean isFirst, boolean isLast);
	/** An empty list object, static for efficiency. */
	static final List<TemplateContext> EMPTY_CONTEXT_LIST =
	    Arrays.asList(new TemplateContext[0]);
    }
    static abstract class TemplateConditional extends TemplateForAll {
	final List<TemplateContext> process
	    (TemplateWriter tw, TemplateContext context,
	     boolean isFirst, boolean isLast) {
	    assert context!=null;
	    if (!isBlockEmitted(context, isFirst, isLast))
		return EMPTY_CONTEXT_LIST;
	    return Collections.singletonList(context);
	}
	/** Return true if this block following this conditional should be
	 *  emitted, else return false.  This method will only be invoked
	 *  if output is not currently suppressed. */
	abstract boolean isBlockEmitted(TemplateContext context,
					boolean isFirst, boolean isLast);
    }
    static abstract class TemplateSimpleForAll extends TemplateForAll {
	final List<TemplateContext> process
	    (TemplateWriter tw, TemplateContext context,
	     boolean isFirst, boolean isLast) {
	    return process(context);
	}
	/** Return a list of <code>TemplateContext</code>s; each one will
	 *  be used in turn to process this block.  So if you return a
	 *  list of size two, the block will be repeated twice, etc.  This
	 *  method will only be invoked if output is not currently
	 *  suppressed. */
	abstract List<TemplateContext> process(TemplateContext context);
    }
    /** A map from macro names to definitions. */
    private static final Map<String, TemplateMacro> macroMap =
	new HashMap<String,TemplateMacro>();
    /** Convenience method to register macro definitions. */
    private static final void register(String name, TemplateMacro action) {
	assert (!name.startsWith("@")) && (!name.endsWith("@"));
	assert name.startsWith("IF")==(action instanceof TemplateConditional);
	macroMap.put("@"+name+"@", action);
    }
    private static final void registerConditional(String name,
						  final TemplateConditional c){
	register("IF_"+name, (TemplateMacro) c);
	register("IFNOT_"+name, (TemplateMacro) new TemplateConditional() {
		boolean isBlockEmitted(TemplateContext context,
				       boolean isFirst, boolean isLast) {
		    return !c.isBlockEmitted(context, isFirst, isLast);
		}
	    });
    }
    static {
	// macro definitions.  java is so noisy!
	register("CHARSET", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    tw.write(context.options.charSet.name());
		}
	    });
	register("WINDOWTITLE", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.windowTitle==null) return;
		    tw.write(context.options.windowTitle);
		}
	    });
	register("TITLESUFFIX", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.windowTitle==null) return;
		    tw.write(" (");
		    tw.write(context.options.windowTitle);
		    tw.write(")");
		}
	    });
	register("DOCTITLE", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.docTitle==null) return;
		    tw.write(context.options.docTitle);
		}
	    });
	register("ROOT", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    tw.write(context.curURL.makeRelative(""));
		}
	    });
	register("HEADER", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.header==null) return;
		    tw.write(context.options.header);
		}
	    });
	register("FOOTER", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.footer==null) return;
		    tw.write(context.options.footer);
		}
	    });
	register("BOTTOM", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    if (context.options.bottom==null) return;
		    tw.write(context.options.bottom);
		}
	    });
	register("CLASSSHORTNAME", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curClass!=null;
		    // should include containing classes.
		    StringBuffer sb=new StringBuffer(context.curClass.name());
		    for(ClassDoc p=context.curClass.containingClass();
			p!=null; p=p.containingClass())
			sb.insert(0, p.name()+".");
		    tw.write(sb.toString());
		}
	    });
	register("CLASSNAME", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curClass!=null;
		    tw.write(context.curClass.qualifiedName());
		}
	    });
	register("PKGNAME", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curPackage!=null;
		    tw.write(context.curPackage.name());
		}
	    });
	register("PKGFRAMELINK", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curPackage!=null;
		    tw.write(HTMLUtil.toLink(context.curURL,context.curPackage,
					     "package-frame.html"));
		}
	    });
	register("PKGSUMMARYLINK", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curPackage!=null;
		    tw.write(HTMLUtil.toLink(context.curURL,context.curPackage,
					     "package-summary.html"));
		}
	    });
	register("CLASSTYPE", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curClass!=null;
		    if (context.curClass.isOrdinaryClass())
			tw.write("class");
		    else if (context.curClass.isInterface())
			tw.write("interface");
		    else if (context.curClass.isException())
			tw.write("exception");
		    else if (context.curClass.isError())
			tw.write("error");
		    else assert false : "what is it?";
		}
	    });
	register("CLASSLINK_P", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curClass!=null;
		    tw.write(HTMLUtil.toLink(context.curURL, context.curClass,
					     true/*with params*/));
		}
	    });
	register("CLASSLINK_NP", new TemplateAction() {
		void process(TemplateWriter tw, TemplateContext context) {
		    assert context.curClass!=null;
		    tw.write(HTMLUtil.toLink(context.curURL, context.curClass,
					     false/*no params*/));
		}
	    });
	registerConditional("FIRST", new TemplateConditional() {
		boolean isBlockEmitted(TemplateContext c,
				       boolean isFirst, boolean isLast) {
		    return isFirst;
		}
	    });
	registerConditional("LAST", new TemplateConditional() {
		boolean isBlockEmitted(TemplateContext c,
				       boolean isFirst, boolean isLast) {
		    return isLast;
		}
	    });
	registerConditional("TAGS", new TemplateConditional() {
		boolean isBlockEmitted(TemplateContext c,
				       boolean isFirst, boolean isLast) {
		    List<Tag> lt;
		    if (c.curMember!=null) lt=c.curMember.tags();
		    else if (c.curClass!=null) lt=c.curClass.tags();
		    else if (c.curPackage!=null) lt=c.curPackage.tags();
		    else lt = c.root.tags();
		    return lt.size()>1 ||
			(lt.size()==1 && 
			 !(lt.get(0).isText() &&
			   lt.get(0).text().trim().length()==0));
		}
	    });
	registerConditional("INTERFACE", new TemplateConditional() {
		boolean isBlockEmitted(TemplateContext c,
				       boolean isFirst, boolean isLast) {
		    assert c.curClass!=null;
		    return c.curClass.isInterface();
		}
	    });
	// iterator over all packages with documented classes
	register("FORALL_PACKAGES", new TemplateSimpleForAll() {
		List<TemplateContext> process(final TemplateContext c) {
		    return new FilterList<PackageDoc,TemplateContext>
			(sorted(HTMLUtil.allDocumentedPackages(c.root))) {
			public TemplateContext filter(PackageDoc pd) {
			    return new TemplateContext(c.root, c.options,
						       c.curURL, pd);
			}
		    };
		}
	    });
	// iterator over included classes (of the package).
	register("FORALL_CLASSES", new TemplateSimpleForAll() {
		List<TemplateContext> process(final TemplateContext c) {
		    // if no package, then all in root. else all in pkg.
		    Collection<ClassDoc> l = (c.curPackage==null) ?
			c.root.classes() : c.curPackage.includedClasses();
		    return new FilterList<ClassDoc,TemplateContext>(sorted(l)){
			public TemplateContext filter(ClassDoc cd) {
			    return new TemplateContext(c.root, c.options,
						       c.curURL, c.curPackage,
						       cd);
			}
		    };
		}
	    });
	// iterator over included interfaces of the package.
	register("FORALL_INTERFACES", new TemplateSimpleForAll() {
		List<TemplateContext> process(final TemplateContext c) {
		    // xxx in non-package context, all interfaces in root?
		    return new FilterList<ClassDoc,TemplateContext>
			(sorted(c.curPackage.includedInterfaces())) {
			public TemplateContext filter(ClassDoc cd) {
			    return new TemplateContext(c.root, c.options,
						       c.curURL, c.curPackage,
						       cd);
			}
		    };
		}
	    });
	// iterator over included ordinary classes of the package.
	register("FORALL_ORDINARYCLASSES", new TemplateSimpleForAll() {
		List<TemplateContext> process(final TemplateContext c) {
		    // xxx in non-package context, all o-classes in root?
		    return new FilterList<ClassDoc,TemplateContext>
			(sorted(c.curPackage.includedOrdinaryClasses())) {
			public TemplateContext filter(ClassDoc cd) {
			    return new TemplateContext(c.root, c.options,
						       c.curURL, c.curPackage,
						       cd);
			}
		    };
		}
	    });
	// iterator over included exceptions of the package.
	register("FORALL_EXCEPTIONS", new TemplateSimpleForAll() {
		List<TemplateContext> process(final TemplateContext c) {
		    // xxx in non-package context, all exceptions in root?
		    return new FilterList<ClassDoc,TemplateContext>
			(sorted(c.curPackage.includedExceptions())) {
			public TemplateContext filter(ClassDoc cd) {
			    return new TemplateContext(c.root, c.options,
						       c.curURL, c.curPackage,
						       cd);
			}
		    };
		}
	    });
	// iterator over included errors of the package.
	register("FORALL_ERRORS", new TemplateSimpleForAll() {
		List<TemplateContext> process(final TemplateContext c) {
		    // xxx in non-package context, all errors in root?
		    return new FilterList<ClassDoc,TemplateContext>
			(sorted(c.curPackage.includedErrors())) {
			public TemplateContext filter(ClassDoc cd) {
			    return new TemplateContext(c.root, c.options,
						       c.curURL, c.curPackage,
						       cd);
			}
		    };
		}
	    });
    }

    /** Helper function. */
    private static <D extends Doc> List<D> sorted(Collection<D> l) {
	List<D> result = new ArrayList<D>(l);
	Collections.sort(result, new DocComparator<D>());
	return result;
    }
    /** Helper class to turn one type of list into another. */
    private static abstract class FilterList<A,B> extends AbstractList<B> {
	final List<A> source;
	FilterList(List<A> source) { this.source = source; }
	public abstract B filter(A a);
	public int size() { return source.size(); }
	public B get(int i) { return filter(source.get(i)); }
    }
}
