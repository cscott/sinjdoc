// PLazyInnerClassType.java, created Mon Mar 24 14:08:22 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.ClassType;
import net.cscott.gjdoc.ClassTypeVariable;
import net.cscott.gjdoc.Type;

import java.io.File;
import java.util.Iterator;
import java.util.List;
/**
 * The <code>PLazyInnerClassType</code> class represents an unresolved
 * inner class type.  Resolution of the exact type specified is deferred
 * until its methods are invoked, at which time the given outer class
 * is resolved, and the inner class name appended to its name.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PLazyInnerClassType extends PClassType {
    final ClassType outerClass;
    final String innerName;
    PLazyInnerClassType(ParseControl pc,
			ClassType outerClass, String innerName) {
	super(pc);
	this.outerClass = outerClass;
	this.innerName = innerName;
    }
    public String canonicalTypeName() {
	return outerClass.canonicalTypeName()+"."+innerName;
    }
    public String typeName() {
	return outerClass.typeName()+"."+innerName;
    }
}// PLazyInnerClassType
