// Type.java, created Wed Mar 19 13:05:01 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>Type</code> interface represents a java type.  A
 * <code>Type</code> can be a (possibly-parameterized) class type,
 * a primitive data type like int and char, or a type variable
 * declared in a generic class or method.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see com.sun.javadoc.Type
 * @see java.lang.reflect.Type
 */
public interface Type {
    /** Return a string naming the type that is appropriate in a method
     *  signature.
     *  @see ExecutableMemberDoc#signature()
     */
    public String signature();

    /** Accept a visitor. */
    public <T> T accept(TypeVisitor<T> visitor);
}
