// Doc.java, created Wed Mar 19 12:04:15 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.Doc;
import net.cscott.gjdoc.SeeTag;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * The <code>PDoc</code> class is the abstract base class representing
 * all java language constructs (classes, packages, methods, etc) which
 * have comments and have been processed by this run of GJDoc.  All
 * <code>PDoc</code> items are <code>ReferenceUnique</code>, that is, they
 * are == comparable.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PDoc implements net.cscott.gjdoc.Doc {
    final ParserControl pc;
    PDoc(ParserControl pc) { this.pc = pc; }
    PDoc() { /* temporary kludge */ this.pc=null; }
    public abstract String getRawCommentText();
    public boolean isClass() { return false; }
    public boolean isConstructor() { return false; }
    public boolean isError() { return false; }
    public boolean isException() { return false; }
    public boolean isField() { return false; }
    public abstract boolean isIncluded();
    public boolean isInterface() { return false; }
    public boolean isMethod() { return false; }
    public boolean isOrdinaryClass() { return false; }
    public abstract String name();
    public SourcePosition position() { return PSourcePosition.NO_INFO; }

    // xxx parse raw comment text into tags.  use regexp?
    public abstract List<Tag> tags();

    public List<Tag> firstSentenceTags() {
	List<Tag> itags = inlineTags();
	// create a plain-text version of these tags.
	StringBuffer sb = new StringBuffer();
	for (Iterator<Tag> it=itags.iterator(); it.hasNext(); ) {
	    Tag tag = it.next();
	    if (tag.kind()=="Text")
		sb.append(tag.text());
	}
	// now create a break iterator...
	BreakIterator boundary = BreakIterator.getSentenceInstance(pc.locale);
	// ...and identify the start end of the first sentence.
	boundary.setText(sb.toString());
	int start = boundary.first();
	int end = boundary.next();
	// now translate this into tags.
	List<Tag> result = new ArrayList<Tag>(itags.size());
	Iterator<Tag> it=itags.iterator();
	Tag lastTag = null; int pos=0, lastPos=0;
	//  ...find start position.
	while (it.hasNext() && start < pos) {
	    Tag curTag = it.next();
	    if (curTag.kind()!="Text") continue;// ignore leading non-text tags
	    lastTag=curTag;
	    lastPos=pos;
	    pos += curTag.text().length();
	}
	if (lastTag!=null) // shorten front of tag.
	    lastTag = PTag.newTextTag(lastTag.text().substring(start-lastPos),
				      lastTag.position()); // xxx position?
	// ...now find end position.
	while (it.hasNext() && pos < end) {
	    Tag curTag = it.next();
	    result.add(lastTag);
	    lastTag=curTag;
	    lastPos=pos;
	    if (curTag.kind()=="Text")
		pos += curTag.text().length();
	}
	// shorten end of tag.
	if (lastTag!=null)
	    lastTag = PTag.newTextTag(lastTag.text().substring(0, end-lastPos),
				      lastTag.position()); // xxx position?
	// add last tag to result.
	if (lastTag!=null)
	    result.add(lastTag);
	// and we're done!
	return Collections.unmodifiableList(result);
    }
    public List<Tag> inlineTags() {
	List<Tag> result = new ArrayList<Tag>();
	for (Iterator<Tag> it=tags().iterator(); it.hasNext(); ) {
	    Tag tag = it.next();
	    if (tag.kind()!="Text" && !tag.isInline())
		return result; // done!
	    result.add(tag);
	}
	return Collections.unmodifiableList(result);
    }
    public final List<Tag> tags(String tagname) {
	List<Tag> result = new ArrayList<Tag>();
	for (Iterator<Tag> it=tags().iterator(); it.hasNext(); ) {
	    Tag tag = it.next();
	    if (tag.name().equals(tagname))
		result.add(tag);
	}
	return Collections.unmodifiableList(result);
    }
    public final String commentText() {
	// strip out all tags not of kind 'Text'.  append the rest.
	StringBuffer sb = new StringBuffer();
	for (Iterator<Tag> it=tags().iterator(); it.hasNext(); ) {
	    Tag tag = it.next();
	    if (tag.kind()=="Text")
		sb.append(tag.text());
	}
	return sb.toString();
    }
    /** Compare based on name. */
    public final int compareTo(Object/*Doc*/ d) {
	return name().compareTo(((Doc)d).name());
    }
}
