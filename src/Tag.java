// Tag.java, created Wed Mar 19 12:45:35 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.List;
/**
 * The <code>Tag</code> class represents a documentation tag.  The
 * tag name and tag text are available for queries; tags with structure
 * or which require special processing are handled by subclasses.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.Tag
 */
public interface Tag {
    /** Returns true if this is an inline tag. 'Text' tags are not inline. */
    public boolean isInline();
    /** Return the name of this tag. */
    public String name();
    /** Return the kind of this tag.  This is 'Text' for plain text, and
     *  something else (same as <code>name()</code>?) for other tags.
     *  The string returned should always be intern'ed. */
    public String kind();
    /** Return the source position of this tag.
     *  Will never return <code>null</code>. */
    public SourcePosition position();
    /** Return the text of this tag; that is, the portion beyond the tag
     *  name. */
    public String text();
    /** Return a human-readable representation of this tag object. */
    public String toString();
}
