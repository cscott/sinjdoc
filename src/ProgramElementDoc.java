// ProgramElementDoc.java, created Wed Mar 19 12:49:27 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>ProgramElementDoc</code> class represents a java program
 * element: class, interface, field, constructor, or method.  This is
 * an abstract class dealing with information common to these elements.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.ProgramElementDoc
 */
public interface ProgramElementDoc extends Doc {
    public ClassDoc containingClass();
    public PackageDoc containingPackage();
    public int modifierSpecifier();
    public String qualifiedName();
    public boolean isFinal();
    public boolean isPackagePrivate();
    public boolean isPrivate();
    public boolean isProtected();
    public boolean isPublic();
    public boolean isStatic();
    public String modifiers();
}
