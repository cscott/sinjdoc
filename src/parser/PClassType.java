// PClassType.java, created Mon Mar 24 14:08:22 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.Type;
import java.io.File;
import java.util.Iterator;
/**
 * The <code>PClassType</code> class represents an abstract type
 * name that can possibly be converted into a <code>ClassDoc</code>
 * object.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PClassType implements Type {
    final PRootDoc rootDoc;
    final int dimension;
    PClassType(PRootDoc rootDoc, int dimension) {
	this.rootDoc = rootDoc;
	this.dimension = dimension;
    }
    public final ClassDoc asClassDoc() {
	return rootDoc.classNamed(qualifiedTypeName());
    }
    public final String dimension() {
	StringBuffer sb=new StringBuffer();
	for (int i=0; i<dimension; i++)
	    sb.append("[]");
	return sb.toString();
    }
    public final String toString() {
	return typeName()+dimension();
    }
}// PClassType
