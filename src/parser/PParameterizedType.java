// PParameterizedType.java, created Wed Mar 19 15:06:55 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassType;
import net.cscott.gjdoc.Type;

import java.util.Collections;
import java.util.List;
/**
 * The <code>PParameterizedType</code> interface represents a parameterized
 * type such as <code>Collection&lt;Integer&gt;</code> or
 * <code>HashMap&lt;String,Double&gt;</code>.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PParameterizedType
    implements net.cscott.gjdoc.ParameterizedType {
    final ClassType baseType;
    final List<Type> actualTypeArguments;
    PParameterizedType(ClassType baseType, List<Type> actualTypeArguments) {
	this.baseType = baseType;
	this.actualTypeArguments = actualTypeArguments;
    }
    public ClassType getBaseType() { return baseType; }
    public List<Type> getActualTypeArguments() {
	return Collections.unmodifiableList(actualTypeArguments);
    }
}
