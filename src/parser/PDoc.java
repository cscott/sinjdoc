// Doc.java, created Wed Mar 19 12:04:15 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The <code>PDoc</code> class is the abstract base class representing
 * all java language constructs (classes, packages, methods, etc) which
 * have comments and have been processed by this run of GJDoc.  All
 * <code>PDoc</code> items are <code>ReferenceUnique</code>, that is, they
 * are == comparable.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PDoc implements net.cscott.gjdoc.Doc {
    final ParseControl pc;
    PDoc(ParseControl pc) { this.pc = pc; }
    public abstract String getRawCommentText();
    /** Return a <code>PSourcePosition</code> object corresponding to the
     *  position of the raw comment text. */
    public abstract PSourcePosition getRawCommentPosition();
    /** Return true if leading stars should be stripped from the raw
     *  comment text for this doc item.
     * @return true
     */
    public boolean shouldStripStars() { return true; }
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

    // parse raw comment text into tags using regexp.
    public List<Tag> tags() {
	List<Tag> result = new ArrayList<Tag>();
	String raw = getRawCommentText();
	PSourcePosition sp = getRawCommentPosition();
	boolean stripStars = shouldStripStars();
	// pull out all the non-inline tags.
	Pattern tagPattern = (stripStars?TAGPATSS:TAGPAT);
	Matcher tagMatcher = tagPattern.matcher(raw);
	int start = tagMatcher.find() ? tagMatcher.start() : raw.length();
	String firstPart = raw.substring(0,start); // doc comment.
	String lastPart = raw.substring(start); // tag portion.
	// create the initial 'text' section.
	result.addAll(parseInline(firstPart, sp, stripStars));
	// now the tags.
	sp = sp.add(start);
	int lastTagStart=0, lastTagEnd=0;  String lastTagName=null;
	tagMatcher = tagPattern.matcher(lastPart);
	while (tagMatcher.find()) {
	    // last tag went from lastTagStart to tagMatcher.start()
	    if (lastTagName!=null) {
		List<Tag> contents =
		    parseInline(lastPart.substring
				(lastTagEnd, tagMatcher.start()),
				sp.add(lastTagEnd), stripStars);;
		result.add(PTag.newTag(lastTagName, contents,
					  sp.add(lastTagStart)));
	    }
	    lastTagStart= tagMatcher.start();
	    lastTagEnd  = tagMatcher.end();
	    lastTagName = tagMatcher.group(1);
	}
	// last tag went from lastTagStart to lastPart.length()
	if (lastTagName!=null) {
	    List<Tag> contents =
		parseInline(lastPart.substring(lastTagEnd),
			    sp.add(lastTagEnd), stripStars);;
	    result.add(PTag.newTag(lastTagName, contents,
				      sp.add(lastTagStart)));
	}
	// done!
	return Collections.unmodifiableList(result);
    }
    private static final Pattern TAGPAT = Pattern.compile
	("^\\p{Blank}*@(\\S+)", Pattern.MULTILINE);
    private static final Pattern TAGPATSS = Pattern.compile
	("^(?:\\p{Blank}*[*]+)?\\p{Blank}*@(\\S+)", Pattern.MULTILINE);
    /** Parse the raw text into a series of 'Text' and 'inline' tags. */
    private static List<Tag> parseInline(String rawText, PSourcePosition sp,
					 boolean stripStars) {
	List<Tag> result = new ArrayList<Tag>();
	Matcher tagMatcher = INLINE.matcher(rawText);
	int pos=0;
	while (tagMatcher.find()) {
	    int start = tagMatcher.start();
	    // section between pos and start becomes a text tag.
	    if (pos<start) {
		String text = rawText.substring(pos, start);
		if (stripStars) text = removeLeadingStars(text);
		result.add(PTag.newTextTag(text, sp.add(pos)));
	    }
	    // now this section becomes an inline tag.
	    // XXX regexp doesn't handle nested inline tags properly.
	    List<Tag> contents = parseInline(tagMatcher.group(2),
					     sp.add(tagMatcher.start(2)),
					     stripStars);
	    result.add(PTag.newInlineTag(tagMatcher.group(1), contents,
					 sp.add(tagMatcher.start(1))));
	    pos = tagMatcher.end();
	}
	// any trailing text becomes a text tag.
	if (pos < rawText.length()) {
	    String text = rawText.substring(pos);
	    if (stripStars) text = removeLeadingStars(text);
	    result.add(PTag.newTextTag(text, sp.add(pos)));
	}
	// done!
	return Collections.unmodifiableList(result);
    }
    private static final Pattern INLINE = Pattern.compile
	("[{]@(\\S+)(?:\\s+([^}]*))?[}]");

    // parse inlineTags() list into first sentence tags using breakiterator.
    // note that we look for the sentence boundary by throwing away all
    // inline tag text, which means that the boundary can never fall in
    // the middle of an inline tag.  This is probably not an issue.
    public List<Tag> firstSentenceTags() {
	List<Tag> itags = inlineTags();
	// create a plain-text version of these tags.
	StringBuffer sb = new StringBuffer();
	for (Iterator<Tag> it=itags.iterator(); it.hasNext(); ) {
	    Tag tag = it.next();
	    if (tag.isText())
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
	    if (!curTag.isText()) continue;// ignore leading non-text tags
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
	    if (lastTag!=null) result.add(lastTag);
	    lastTag=curTag;
	    lastPos=pos;
	    if (curTag.isText())
		pos += curTag.text().length();
	}
	// shorten end of tag.
	if (lastTag!=null)
	    lastTag = PTag.newTextTag(lastTag.text().substring(0, end-lastPos),
				      lastTag.position()); // xxx position?
	// add last tag to result.
	if (lastTag!=null) result.add(lastTag);
	// and we're done!
	return Collections.unmodifiableList(result);
    }
    public List<Tag> inlineTags() {
	List<Tag> result = new ArrayList<Tag>();
	for (Iterator<Tag> it=tags().iterator(); it.hasNext(); ) {
	    Tag tag = it.next();
	    if (tag.isTrailing())
		return result; // done!
	    result.add(tag);
	}
	return Collections.unmodifiableList(result);
    }
    public final List<Tag> tags(String tagname) {
	List<Tag> result = new ArrayList<Tag>();
	for (Iterator<Tag> it=tags().iterator(); it.hasNext(); ) {
	    Tag tag = it.next();
	    if ((!tag.isText()) && tag.name().equals(tagname))
		result.add(tag);
	}
	return Collections.unmodifiableList(result);
    }
    public final String commentText() {
	// strip out all tags not of kind 'Text'.  append the rest.
	StringBuffer sb = new StringBuffer();
	for (Iterator<Tag> it=tags().iterator(); it.hasNext(); ) {
	    Tag tag = it.next();
	    if (tag.isText())
		sb.append(tag.text());
	}
	return sb.toString();
    }
    /** Compare based on name. */
    public final int compareTo(Object/*Doc*/ d) {
	return name().compareTo(((Doc)d).name());
    }
    /** Convenience method: remove leading stars, as from comment text. */
    static String removeLeadingStars(String str) {
	return LEADSTAR.matcher(str).replaceAll("");
    }
    /** Pattern used by <code>removeLeadingStars()</code> method. */
    private static final Pattern LEADSTAR = Pattern.compile
	("^[\\p{Blank}]*[*]+", Pattern.MULTILINE);
}
