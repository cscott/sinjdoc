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
    public List<Tag> firstSentenceTags();
    public List<Tag> inlineTags();
    public String kind();
    public String name();
    public SourcePosition position();
    public String text();
    public String toString();
}
