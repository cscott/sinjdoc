// ParameterizedType.java, created Wed Mar 19 15:06:55 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.List;
/**
 * The <code>ParameterizedType</code> interface represents a parameterized
 * type such as <code>Collection&lt;Integer&gt;</code> or
 * <code>HashMap&lt;String,Double&gt;</code>.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see java.lang.reflect.ParameterizedType
 */
public interface ParameterizedType extends Type {
    /** Return the type arguments that this parameterized type has been
     *  instantiated with. */
    public List<Type> getActualTypeArguments();
    /** Return the <code>Type</code> corresponding to the "raw class"
     *  of this parameterized type; that is, the type without parameters. */
    public Type getRawClass();
}
