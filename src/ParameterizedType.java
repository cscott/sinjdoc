// ParameterizedType.java, created Wed Mar 19 15:06:55 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.sinjdoc;

import java.util.List;
/**
 * The <code>ParameterizedType</code> interface represents a parameterized
 * type such as <code>Collection&lt;Integer&gt;</code> or
 * <code>HashMap&lt;String,Double&gt;</code>.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see java.lang.reflect.ParameterizedType
 */
public interface ParameterizedType extends Type {
    /** Return the <code>Type</code> corresponding to the "raw class"
     *  of this parameterized type; that is, the type without parameters. */
    public ClassType getBaseType();
    /** Return the type arguments that this parameterized type has been
     *  instantiated with.  Note that for nested parameterized types,
     *  the size of the returned list may well be larger than the size
     *  of <code>getBaseType().typeParameters()</code>.  The returned
     *  actual types are ordered from outermost class to innermost
     *  class.  For the type <code>A<Integer>.B<String>.C</code>,
     *  this method will return the list <code>[ Integer, String ]</code>
     *  even those <code>getBaseType().typeParameters().size()</code> will
     *  be zero. */
    public List<Type> getActualTypeArguments();
}
