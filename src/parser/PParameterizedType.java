// PParameterizedType.java, created Wed Mar 19 15:06:55 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassType;
import net.cscott.gjdoc.Type;
import net.cscott.gjdoc.TypeVisitor;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * The <code>PParameterizedType</code> interface represents a parameterized
 * type such as <code>Collection&lt;Integer&gt;</code> or
 * <code>HashMap&lt;String,Double&gt;</code>.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PParameterizedType
    implements net.cscott.gjdoc.ParameterizedType {
    final ClassType baseType;
    final List<Type> actualTypeArguments;
    PParameterizedType(ClassType baseType, List<Type> actualTypeArguments) {
	this.baseType = baseType;
	this.actualTypeArguments = actualTypeArguments;
	assert actualTypeArguments.size()>0;
    }
    public ClassType getBaseType() { return baseType; }
    public List<Type> getActualTypeArguments() {
	return Collections.unmodifiableList(actualTypeArguments);
    }
    // we can't overload based on parameterization due to type erasure, so
    // I think going with the signature of the base type is the right thing
    // to do here.
    public String signature() { return baseType.signature(); }
    public String toString() {
	StringBuffer sb = new StringBuffer(getBaseType().toString());
	sb.append('<');
	for (Iterator<Type> it=getActualTypeArguments().iterator();
	     it.hasNext(); ) {
	    sb.append(it.next().toString());
	    if (it.hasNext()) sb.append(',');
	}
	sb.append('>');
	return sb.toString();
    }
    public <T> T accept(TypeVisitor<T> visitor) { return visitor.visit(this); }
}
