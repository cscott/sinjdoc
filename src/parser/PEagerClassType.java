// PEagerClassType.java, created Wed Mar 26 11:55:02 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

/**
 * The <code>PEagerClassType</code> class represents a fully-resolved
 * non-parameterized class type.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id: PEagerParameterizedType.java,v 1.3 2003/03/24 20:47:36 cananian */
public class PEagerClassType extends PClassType {
    final String packageName;
    final String className; // dots here indicate inner classes.
    PEagerClassType (PRootDoc rootDoc,
			     String packageName, String className,
			     int dimension) {
	super(rootDoc, dimension);
	this.packageName = packageName.intern();
	this.className = className.intern();
    }
    public String typeName() { return className; }
    public String qualifiedTypeName() {
	if (packageName.length()==0) return className;
	return packageName+"."+className;
    }
}// PEagerClassType
