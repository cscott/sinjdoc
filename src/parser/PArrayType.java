// PArrayType.java, created Wed Mar 26 11:55:02 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ArrayType;
import net.cscott.gjdoc.Type;
import net.cscott.gjdoc.TypeVisitor;
/**
 * The <code>PArrayType</code> class represents a java array type.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id: PEagerParameterizedType.java,v 1.3 2003/03/24 20:47:36 cananian */
class PArrayType implements ArrayType {
    final Type baseType;
    final int dimension;
    PArrayType (Type baseType, int dimension) {
	this.baseType = baseType;
	this.dimension = dimension;
	assert dimension > 0;
    }
    public Type baseType() { return baseType; }
    public int dimension() { return dimension; }
    public String signature() { return baseType.signature()+dimString(); }
    public String toString() { return baseType.toString()+dimString(); }
    private String dimString() {
	StringBuffer sb=new StringBuffer();
	for (int i=0; i<dimension; i++)
	    sb.append("[]");
	return sb.toString();
    }
    public <T> T accept(TypeVisitor<T> visitor) { return visitor.visit(this); }
}// PArrayType
