// Type.java, created Wed Mar 19 13:05:01 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>Type</code> interface represents a java type.  Type can be
 * (possibly-parameterized) class type or a primitive data type like
 * int and char.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.Type
 */
public interface Type {
    public ClassDoc asClassDoc();
    public String dimension();
    public String qualifiedTypeName();
    public String toString();
    public String typeName();
}
