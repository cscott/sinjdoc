// Tag.java, created Wed Mar 19 12:45:35 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;

import java.util.List;
/**
 * The <code>PTag</code> class represents a documentation tag.  The
 * tag name and tag text are available for queries; tags with structure
 * or which require special processing are handled by subclasses.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PTag 
    implements net.cscott.gjdoc.Tag {
    public abstract boolean isInline();
    public abstract String kind();
    public abstract String name();
    public abstract SourcePosition position();
    public abstract String text();
    public String toString() {
	String str = "@"+name()+" "+text();
	if (isInline()) str = "{"+str+"}";
	return str;
    }
    /** Convenience method to create a new 'Tag' representing plain-text;
     *  i.e. it has kind()=="Text". */
    static Tag newTextTag(final String text, final SourcePosition pos) {
	return new Tag() {
		public String toString() { return text; }
		public String text() { return text; }
		public String kind() { return "Text"; }
		public String name() { return ""; }
		public boolean isInline() { return false; }
		public SourcePosition position() { return pos; }
	    };
    }
    /** Select a new non-inline Tag object to create based on the tagname. */
    static Tag newTag(String tagname, List<Tag> contents, SourcePosition pos) {
	assert false : "unimplemented";
	return null;
    }
    /** Select a new inline Tag object to create based on the tagname. */
    // sp is position of first char of 'tagname'
    // we're not allowing nested inline tags.
    static Tag newInlineTag(String tagname, String text, SourcePosition pos) {
	assert false : "unimplemented";
	return null;
    }
}
