// Tag.java, created Wed Mar 19 12:45:35 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * The <code>PTag</code> class represents a documentation tag.  The
 * tag name and tag text are available for queries; tags with structure
 * or which require special processing are handled by subclasses.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PTag 
    implements net.cscott.gjdoc.Tag {
    final SourcePosition sp;
    PTag(SourcePosition sp) { this.sp = sp; }
    public boolean isInline() { return false; }
    public boolean isText() { return false; }
    public boolean isTrailing() { return false; }

    public String name() { return null; }
    public String text() { return null; }
    public SourcePosition position() { return sp; }
    public List<Tag> contents() { return null; }

    public String toString() {
	if (isText()) return text();
	StringBuffer sb=new StringBuffer();
	if (isInline()) sb.append("{");
	sb.append("@"); sb.append(name()); sb.append(" ");
	for (Iterator<Tag> it=contents().iterator(); it.hasNext(); )
	    sb.append(it.next().toString());
	if (isInline()) sb.append("}");
	return sb.toString();
    }

    static class Text extends PTag {
	final String text;
	public boolean isText() { return true; }
	public String text() { return text; }
	Text(SourcePosition sp, String text) {
	    super(sp);
	    this.text = text;
	}
    }
    static class Trailing extends PTag {
	final String name;
	List<Tag> contents;
	public boolean isTrailing() { return true; }
	public String name() { return name; }
	public List<Tag> contents() { return contents; }
	Trailing(SourcePosition sp, String name, List<Tag> contents) {
	    super(sp);
	    this.name = name.intern();
	    this.contents = Collections.unmodifiableList(contents);
	}
    }
    static class Inline extends PTag {
	public boolean isInline() { return true; }
	final String name;
	List<Tag> contents;
	public String name() { return name; }
	public List<Tag> contents() { return contents; }
	Inline(SourcePosition sp, String name, List<Tag> contents) {
	    super(sp);
	    this.name = name.intern();
	    this.contents = Collections.unmodifiableList(contents);
	}
    }
    /** Convenience method to create a new 'Tag' representing plain-text;
     *  i.e. it has kind()=="Text". */
    static Tag newTextTag(final String text, final SourcePosition pos) {
	return new Text(pos, text);
    }
    /** Select a new non-inline Tag object to create based on the tagname. */
    static Tag newTag(String tagname, List<Tag> contents, SourcePosition pos) {
	tagname=tagname.intern();
	/* XXX uncomment when tag subtypes are not abstract.
	if (tagname=="param") return new PParamTag(pos, tagname, contents);
	if (tagname=="see") return new PSeeTag(pos, tagname, contents);
	if (tagname=="serialField") return new PSerialFieldTag(pos, tagname, contents);
	if (tagname=="throws" || tagname=="exception") return new PThrowsTag(pos, tagname, contents);
	*/
	return new Trailing(pos, tagname, contents);
    }
    /** Select a new inline Tag object to create based on the tagname. */
    // sp is position of first char of 'tagname'
    static Tag newInlineTag(String tagname, List<Tag> contents, SourcePosition pos) {
	tagname=tagname.intern();
	/* XXX uncomment when PSeeTag is not abstract.
	if (tagname=="link" || tagname=="linkplain")
	    return new PSeeTag(pos, tagname, contents);
	*/
	return new Inline(pos, tagname, contents);
    }
}
