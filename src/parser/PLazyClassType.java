// PLazyClassType.java, created Mon Mar 24 14:08:22 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.Type;
import java.io.File;
import java.util.Iterator;
/**
 * The <code>PLazyClassType</code> class represents an unresolved class
 * type.  Resolution of the exact type specified is deferred until
 * its methods are invoked, at which time the given <code>TypeContext</code>
 * is used to resolve the name.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PLazyClassType extends PClassType {
    Type cache;
    TypeContext typeContext;
    String typeName;
    PLazyClassType(TypeContext typeContext, String typeName, int dimension) {
	super(typeContext.rootDoc, dimension);
	this.cache = null;
	this.typeContext = typeContext;
	this.typeName = typeName;
	assert isValid();
    }
    public String qualifiedTypeName() {
	if (cache==null) lookup();
	return cache.qualifiedTypeName();
    }
    public String typeName() {
	if (cache==null) lookup();
	return cache.typeName();
    }
    private void lookup() {
	assert cache==null && isValid();
	cache = typeContext.lookupTypeName(typeName, dimension);
	typeContext=null;
	typeName=null;
	assert cache!=null && isValid();
    }
    private boolean isValid() {
	assert cache==null ?
	    (typeContext!=null && typeName!=null) :
	    (typeContext==null && typeName==null);
	return true;
    }
}// PLazyClassType