// ClassTypeVariable.java, created Wed Mar 19 15:01:41 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.Type;
import net.cscott.gjdoc.TypeVisitor;

import java.util.List;
/**
 * The <code>PClassTypeVariable</code> interface represents a type variable
 * declared as a formal parameter to a generic class or interface.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PClassTypeVariable extends PTypeVariable
    implements net.cscott.gjdoc.ClassTypeVariable {
    final ClassDoc declaringClass;
    PClassTypeVariable(ClassDoc declaringClass,
		       String name, List<Type> bounds) {
	super(name, bounds);
	this.declaringClass = declaringClass;
    }
    public ClassDoc declaringClass() { return declaringClass; }
    public <T> T accept(TypeVisitor<T> visitor) { return visitor.visit(this); }
}
