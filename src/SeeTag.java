// SeeTag.java, created Wed Mar 19 12:59:15 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>SeeTag</code> class represents a "see also" documentation
 * tag.  The @see tag can be plain text, or reference a class or
 * member.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see com.sun.javadoc.SeeTag
 */
public interface SeeTag extends Tag {
    /** Return the label of the see tag. */
    public String label();
    /**
     * Return the class referenced by the class name part of @see,
     * or <code>null</code> if the class is not a class specified on the
     * GJDoc command line. */
    public ClassDoc referencedClass();
    /** 
     * Return the class name part of @see.  For example, if the
     * comment is <code>@see String#startsWith(java.lang.String)</code>,
     * then this method returns "String".  Returns <code>null</code> if
     * format is not that of a java reference.  Returns the empty string
     * if the class name was not specified. */
    public String referencedClassName();
    /**
     * Return the member referenced by @see, or <code>null</code> if
     * the member could not be determined or was not in the included set.
     */
    public MemberDoc referencedMember();
    /**
     * Return the name of the member referenced by @see.  For example, if
     * the comment is <code>@see String#startsWith(java.lang.String)</code>,
     * then this method returns "startsWith(java.lang.String)".  Returns
     * <code>null</code> if the format is not that of a java reference.
     * Returns the empty string if the member name was not specified.
     */
    public String referencedMemberName();
    /**
     * Return the package referenced by @see, or <code>null</code> if
     * no known package found. */
    public PackageDoc referencedPackage();
}
