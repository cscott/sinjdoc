// PLazyParameterizedType.java, created Wed Mar 19 15:06:55 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.Type;

import java.util.List;
/**
 * The <code>PLazyParameterizedType</code> class represents an unresolved
 * parameterized type.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PLazyParameterizedType extends PLazyClassType
    implements net.cscott.gjdoc.ParameterizedType {
    final List<Type> actualTypeArguments;
    PLazyParameterizedType(TypeContext typeContext, String typeName,
			   int dimension, List<Type> actualTypeArguments) {
	super(typeContext, typeName, dimension);
	this.actualTypeArguments = actualTypeArguments;
    }
    public List<Type> getActualTypeArguments() {
	return actualTypeArguments;
    }
    public Type getRawClass() {
	assert false : "unimplemented";
	return null;
    }
}
