// SeeTag.java, created Wed Mar 19 12:59:15 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>SeeTag</code> class represents a "see also" documentation
 * tag.  The @see tag can be plain text, or reference a class or
 * member.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.SeeTag
 */
public interface SeeTag extends Tag {
    public String label();
    public ClassDoc referencedClass();
    public String referencedClassName();
    public MemberDoc referencedMember();
    public String referencedMemberName();
    public PackageDoc referencedPackage();
}
