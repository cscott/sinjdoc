// SeeTag.java, created Wed Mar 19 12:59:15 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.MemberDoc;
import net.cscott.gjdoc.PackageDoc;

/**
 * The <code>PSeeTag</code> class represents a "see also" documentation
 * tag.  The @see tag can be plain text, or reference a class or
 * member.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PSeeTag extends PTag
    implements net.cscott.gjdoc.SeeTag {
    public abstract String label();
    public abstract ClassDoc referencedClass();
    public abstract String referencedClassName();
    public abstract MemberDoc referencedMember();
    public abstract String referencedMemberName();
    public abstract PackageDoc referencedPackage();
}
