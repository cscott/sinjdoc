// TypeVariable.java, created Wed Mar 19 15:03:51 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.Type;

import java.util.List;
/**
 * The <code>PTypeVariable</code> interface represents a type
 * variable declared as a formal parameter to a generic class,
 * interface, or method.  Every actual type variable supports
 * exact one of the two subinterfaces <code>MethodTypeVariable</code> or
 * <code>ClassTypeVariable</code>.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see java.lang.reflect.TypeVariable
 */
abstract class PTypeVariable
    implements net.cscott.gjdoc.TypeVariable {
    public abstract List<Type> getBounds();
    public abstract String getName();
}
