// ClassType.java, created Wed Mar 19 13:05:01 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.List;
/**
 * The <code>ClassType</code> interface represents a concrete java class or
 * primitive data type.  It does not represent type variables.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see com.sun.javadoc.Type
 */
public interface ClassType extends Type {
    /**
     * Returns a list of <code>ClassTypeVariable</code> objects that
     * represent the type variables declared by this class or interface
     * represented by this <code>ClassDoc</code> object, in declaration
     * order.  Returns a zero-length list if the underlying class or
     * interface declares no type variables.
     */
    public List<ClassTypeVariable> typeParameters();
    /**
     * Return the <code>ClassDoc</code> corresponding to this type,
     * ignoring any array dimensions or parameters.  Returns
     * <code>null</code> if it is a primitive type, or if the
     * type is not included. */
    public ClassDoc asClassDoc();
    /**
     * Return the qualified name of the type excluding any dimension
     * information.  For example, a two-dimensional array of
     * <code>String</code> returns "java.lang.String". */
    public String qualifiedTypeName();
    /**
     * Return the unqualified name of this type excluding any dimension
     * or package information.  For example, a two-dimensional array of
     * <code>java.lang.String</code> returns "String".  Note that inner class
     * specifications <i>are</i> included in this name; i.e. the returned
     * string may contain dots. */
    public String typeName();
}
