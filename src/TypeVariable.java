// TypeVariable.java, created Wed Mar 19 15:03:51 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.List;
/**
 * The <code>TypeVariable</code> interface represents a type
 * variable declared as a formal parameter to a generic class,
 * interface, or method.  Every actual type variable supports
 * exact one of the two subinterfaces <code>MethodTypeVariable</code> or
 * <code>ClassTypeVariable</code>.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see java.lang.reflect.TypeVariable
 */
public interface TypeVariable {
    /**
     * Return the bounds on this type variable.  If there are no bounds
     * specified, returns an array of length one containing the
     * <code>Type</code> for <code>java.lang.Object</code>. */
    public List<Type> getBounds();
    /** Return the name of this type variable. */
    public String getName();
}
