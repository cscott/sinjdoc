// Doc.java, created Wed Mar 19 12:04:15 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.Doc;
import net.cscott.gjdoc.SeeTag;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;

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
    public abstract String commentText();
    public abstract int compareTo(Object/*Doc*/ d);
    public abstract List<Tag> firstSentenceTags();
    public abstract String getRawCommentText();
    public abstract List<Tag> inlineTags();
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
    public abstract SourcePosition position();
    public abstract List<SeeTag> seeTags();
    public abstract void setRawCommentText(String rawDocumentation);
    public abstract List<Tag> tags();
    public abstract List<Tag> tags(String tagname);
}
