// MethodTypeVariable.java, created Wed Mar 19 15:05:26 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.MethodDoc;
import net.cscott.gjdoc.Type;

import java.util.List;
/**
 * The <code>PMethodTypeVariable</code> interface represents a type
 * variable declared as a formal parameter to a generic method.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PMethodTypeVariable extends PTypeVariable
    implements net.cscott.gjdoc.MethodTypeVariable {
    final MethodDoc declaringMethod;
    PMethodTypeVariable(MethodDoc declaringMethod,
			String name, List<Type> bounds) {
	super(name, bounds);
	this.declaringMethod = declaringMethod;
    }
    public MethodDoc declaringMethod() { return declaringMethod; }
}
