// TagEmitter.java, created Fri Apr  4 18:52:33 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cscott@cscott.net>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.ParamTag;
import net.cscott.gjdoc.SeeTag;
import net.cscott.gjdoc.SerialFieldTag;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.ThrowsTag;
import net.cscott.gjdoc.Tag;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * The <code>TagEmitter</code> class encapsulates the code required to
 * emit a list of <code>Tag</code> objects.
 * 
 * @author  C. Scott Ananian <cscott@cscott.net>
 * @version $Id$
 */
abstract class TagEmitter {
    /** The <code>TagInfo</code> class represents a particular tag type, and
     *  stores information about how to process it, including its order in
     *  the output and a <code>BlockTagAction</code> which can be used to
     *  emit it. */
    private static class TagInfo {
	final String kind;
	final Class tagClass;
	final BlockTagAction action;
	final int order;
	TagInfo(String kind, Class tagClass, BlockTagAction action) {
	    this.kind = kind;
	    this.tagClass = tagClass;
	    this.action = action;
	    this.order = counter++;
	}
	private static int counter=0;
    }
    /** This field enumerates all the tag types we know about.  Because
     *  of how the <code>TagInfo.order</code> field is assigned, the
     *  order of this array should be the same as the order in which
     *  the tags should be emitted.  (Explicitly specifying order
     *  integers would eliminate this restriction.) */
    private final static TagInfo[] tagInfo = new TagInfo[] {
	new TagInfo("deprecated", null, new SimpleBlockAction("Deprecated")),
	new TagInfo(".text", null, new InlineAction()),
	new TagInfo("serialData", null, new SimpleBlockAction("Serial Data")),
	new TagInfo("serialField", SerialFieldTag.class,
		    new SimpleBlockAction("Serialized Fields")/*XXX*/),
	new TagInfo("serial", null, new SimpleBlockAction("Serial")/*XXX*/),
	new TagInfo(".specified", SpecifiedTag.class,
		    new SimpleBlockAction("Specified by")/*XXX*/),
	new TagInfo(".overrides", OverrideTag.class,
		    new SimpleBlockAction("Overrides")/*XXX*/),
	new TagInfo("param", ParamTag.class,
		    new SimpleBlockAction("Parameters")/*XXX*/),
	new TagInfo("return", null, new SimpleBlockAction("Returns")),
	new TagInfo("throws", ThrowsTag.class,
		    new SimpleBlockAction("Throws")/*XXX*/),
	new TagInfo("since", null, new SimpleBlockAction("Since")),
	new TagInfo("version", null, new SimpleBlockAction("Version")),
	new TagInfo("author", null, new SimpleBlockAction("Author")),
	new TagInfo(".unknown", null, new SimpleBlockAction("UNKNOWN")),
	new TagInfo("see", SeeTag.class, new SimpleBlockAction("See Also")),
    };

    /** A <code>BlockTagAction</code> allows you to emit a specific type
     *  of tag.  All tags of that type are grouped together and passed as
     *  a list (in order by source position) to the emit method. */
    static abstract class BlockTagAction {
	/** Emit the specified homogeneous group of tags to the specified
	 *  <code>PrintWriter</code>.  Errors are directed to the specified
	 *  <code>DocErrorReporter/code>. */
	abstract void emit(PrintWriter pw, String kind, List<Tag> tags,
			   DocErrorReporter reporter);
    }
    /** A <code>SimpleBlockAction</code> is a common
     *  <code>BlockTagAction</code> for tags with no additional structure
     *  to their contents. */
    static class SimpleBlockAction extends BlockTagAction {
	/** The human-readable description used for this tag. */
	final String tagDescription;
	SimpleBlockAction(String desc) { this.tagDescription = desc; }
	void emit(PrintWriter pw, String kind, List<Tag> tags,
		  DocErrorReporter reporter) {
	    pw.print("<p class=\"tag tag_"+kind+"\">");
	    String desc = tagDescription; boolean first=true;
	    for (Iterator<Tag> it=tags.iterator(); it.hasNext(); ) {
		Tag t = it.next();
		assert t.isTrailing();
		if (desc==null) desc = t.name();
		if (first)
		    pw.print("<span class=\"tagName\">"+desc+":</span> ");
		pw.print("<span class=\"tagContents\">");
		emitInline(pw, t.contents(), reporter);
		pw.print("</span> ");
	    }
	    pw.println("</p>");
	}
    }
    /** An <code>InlineAction</code> handles text and inline tags by
     *  invoking <code>emitInline()</code>. */
    static class InlineAction extends BlockTagAction {
	void emit(PrintWriter pw, String kind, List<Tag> tags,
		  DocErrorReporter reporter) {
	    emitInline(pw, tags, reporter);
	}
    }
    /** The <code>SpecifiedTag</code> is a synthetic tag used for
     *  "Specified By" notations. */
    static interface SpecifiedTag extends Tag { }
    /** The <code>OverrideTag</code> is a synthetic tag used for
     *  "Overrides" notations. */
    static interface OverrideTag extends Tag { }

    /** Returns canonical string naming this tag.  Text and inline tags
     *  return ".text" and the "exception" tag is canonicalized to
     *  "throws". */
    static String tagKind(Tag t) {
	if (t.isText() || t.isInline()) return ".text";
	if (t.name().equals("exception")) return "throws";
	return t.name();
    }
    /** Lookup the <code>TagInfo</code> corresponding to the type of
     *  this <code>Tag</code>. */
    static TagInfo lookup(Tag t) {
	String kind = tagKind(t);
	// brain-dead method for now.
	while (true) {
	    for (int i=0; i<tagInfo.length; i++) {
		if (tagInfo[i].kind.equals(kind) &&
		    (tagInfo[i].tagClass==null ||
		     tagInfo[i].tagClass.isInstance(t)))
		    return tagInfo[i];
	    }
	    // not found; do again with kind=".unknown"
	    assert !kind.equals(".unknown"); // protect against infinite loop
	    kind=".unknown";
	}
    }
    
    /** Compares two SourcePositions. */
    private static final Comparator<SourcePosition> POSITION_COMPARATOR =
	new Comparator<SourcePosition>() {
	    public int compare(SourcePosition sp1, SourcePosition sp2) {
		int c = sp1.file().toString().compareTo(sp2.file().toString());
		if (c!=0) return c;
		c = sp1.line() - sp2.line();
		if (c!=0) return c;
		c = sp1.column() - sp2.column();
		return c;
	    }
	};
    /** Compares two Tags for canonical order. */
    private static final Comparator<Tag> TAG_COMPARATOR =
	new Comparator<Tag>() {
	    public int compare(Tag t1, Tag t2) {
		// use tagSort as primary key.
		TagInfo ti1 = lookup(t1), ti2 = lookup(t2);
		int c = ti1.order - ti2.order;
		if (c!=0) return c;
		// use source location as secondary key.
		return POSITION_COMPARATOR.compare
		(t1.position(), t2.position());
	    }
	};
    
    /** An <code>InlineTagAction</code> processes a text or inline tag. */
    static abstract class InlineTagAction {
	abstract void emit(PrintWriter pw, Tag t, DocErrorReporter reporter);
    }
    /** A map relating inline tag kinds to the proper actions to emit them. */
    static final Map<String,InlineTagAction> inlineActions =
	new HashMap<String,InlineTagAction>();
    static { // initialize the map.
	inlineActions.put(".text", new InlineTagAction() {
		void emit(PrintWriter pw, Tag t, DocErrorReporter reporter) {
		    assert t.isText();
		    pw.print(t.text());
		}
	    });
	inlineActions.put(".unknown", new InlineTagAction() {
		void emit(PrintWriter pw, Tag t, DocErrorReporter reporter) {
		    assert t.isInline();
		    reporter.printWarning(t.position(),
					  "Unknown inline tag: "+t.name());
		    // emit as unprocessed.
		    pw.print("{@"); pw.print(t.name()); pw.print(" ");
		    // (recurse on contents)
		    emitInline(pw, t.contents(), reporter);
		    // close tag.
		    pw.print("}");
		}
	    });
    }
    /** Canonicalize the kind of this inline tag. */
    static String inlineTagKind(Tag t) {
	assert !t.isTrailing();
	if (t.isText()) return ".text";
	if (inlineActions.containsKey(t.name())) return t.name();
	return ".unknown";
    }
    /** Emit the given list of inline and text tags to the specified
     *  <code>PrintWriter</code>. */
    private static void emitInline(PrintWriter pw, List<Tag> tags,
				   DocErrorReporter reporter) {
	// no tag in this list should be trailing.
	for (Iterator<Tag> it=tags.iterator(); it.hasNext(); ) {
	    Tag t = it.next();
	    assert !t.isTrailing();
	    inlineActions.get(inlineTagKind(t)).emit(pw, t, reporter);
	}
    }
    /** Emit a top-level list of tags. */
    public static void emit(PrintWriter pw, List<Tag> tags,
			    TemplateContext context) {
	DocErrorReporter reporter = context.root;
	// XXX add synthetic 'overrides' tags using the TemplateContext.
	// sort tags.
	List<Tag> sorted = new ArrayList<Tag>(tags);
	Collections.sort(sorted, TAG_COMPARATOR);
	// now group trailing tags & invoke actions.
	List<Tag> group = new ArrayList<Tag>();
	TagInfo groupType=null;
	for (Iterator<Tag> it=sorted.iterator(); it.hasNext(); ) {
	    Tag t = it.next();
	    TagInfo ti = lookup(t);
	    if (groupType!=ti) { // this is a new group!
		if (groupType!=null) // emit old group
		    groupType.action.emit(pw, groupType.kind, group, reporter);
		group.clear(); // start new group.
	    }
	    // add tag to group.
	    group.add(t);
	    groupType=ti;
	}
	// emit any unfinished group.
	if (groupType!=null)
	    groupType.action.emit(pw, groupType.kind, group, reporter);
	// done!
    }
}