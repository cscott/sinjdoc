// PArrayType.java, created Wed Mar 26 11:55:02 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ArrayType;
import net.cscott.gjdoc.Type;
/**
 * The <code>PArrayType</code> class represents a java array type.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id: PEagerParameterizedType.java,v 1.3 2003/03/24 20:47:36 cananian */
public class PArrayType implements ArrayType {
    final Type baseType;
    final int dimension;
    PArrayType (Type baseType, int dimension) {
	this.baseType = baseType;
	this.dimension = dimension;
    }
    public Type baseType() { return baseType; }
    public int dimension() { return dimension; }
    public String toString() {
	StringBuffer sb=new StringBuffer(baseType.toString());
	for (int i=0; i<dimension; i++)
	    sb.append("[]");
	return sb.toString();
    }
}// PArrayType
