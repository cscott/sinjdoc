// MethodTypeVariable.java, created Wed Mar 19 15:05:26 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ExecutableMemberDoc;
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
    // allow changes to this field, as method type variables are seen & used
    // before we know the name of the method they are associated with.
    ExecutableMemberDoc declaringMethod;
    PMethodTypeVariable(String name, List<Type> bounds) {
	super(name, bounds);
    }
    public ExecutableMemberDoc declaringMethod() {
	assert declaringMethod!=null; // should be set by now.
	return declaringMethod;
    }
}
