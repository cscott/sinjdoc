// Type.java, created Wed Mar 19 13:05:01 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>Type</code> interface represents a java type.  Type can be
 * (possibly-parameterized) class type or a primitive data type like
 * int and char.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see com.sun.javadoc.Type
 */
public interface Type {
    /**
     * Return the <code>ClassDoc</code> corresponding to this type,
     * ignoring any array dimensions or parameters.  Returns
     * <code>null</code> if it is a primitive type, or if the
     * type is not included. */
    public ClassDoc asClassDoc();
    /**
     * Return the type's dimension information, as a string.
     * For example, a two-dimensional array of <code>String</code>
     * returns "[][]". */
    public String dimension();
    /**
     * Return the qualified name of the type excluding any dimension
     * information.  For example, a two-dimensional array of
     * <code>String</code> returns "java.lang.String". */
    public String qualifiedTypeName();
    /**
     * Returns a string representation of the type.  Returns the
     * name of the type followed by dimension information, if
     * applicable.  For example,  a two-dimensional array of
     * <code>String</code> returns "String[][]". */
    public String toString();
    /**
     * Return the unqualified name of this type excluding any dimension
     * information.  For example, a two-dimensional array of
     * <code>String</code> returns "String". */
    public String typeName();
}
