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
    public abstract List<Tag> firstSentenceTags();
    public abstract List<Tag> inlineTags();
    public abstract String kind();
    public abstract String name();
    public abstract SourcePosition position();
    public abstract String text();
    public abstract String toString();
}
