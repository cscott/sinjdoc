// Doc.java, created Wed Mar 19 12:04:15 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import net.cscott.jutil.ReferenceUnique;

import java.util.List;

/**
 * The <code>Doc</code> class is the abstract base class representing
 * all java language constructs (classes, packages, methods, etc) which
 * have comments and have been processed by this run of GJDoc.  All
 * <code>Doc</code> items are <code>ReferenceUnique</code>, that is, they
 * are == comparable.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.Doc
 */
public interface Doc extends ReferenceUnique, Comparable<Doc> {
    public String commentText();
    public int compareTo(Doc d);
    public List<Tag> firstSentenceTags();
    public String getRawCommentText();
    public List<Tag> inlineTags();
    public boolean isClass();
    public boolean isConstructor();
    public boolean isError();
    public boolean isException();
    public boolean isField();
    public boolean isIncluded();
    public boolean isInterface();
    public boolean isMethod();
    public boolean isOrdinaryClass();
    public String name();
    public SourcePosition position();
    public List<SeeTag> seeTags();
    public void setRawCommentText(String rawDocumentation);
    public List<Tag> tags();
    public List<Tag> tags(String tagname);
}
