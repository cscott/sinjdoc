// MethodDoc.java, created Wed Mar 19 12:19:34 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.MethodDoc;
import net.cscott.gjdoc.Parameter;
import net.cscott.gjdoc.Type;

import java.lang.reflect.Modifier;
import java.util.List;
/**
 * The <code>PMethodDoc</code> class represents a (non-constructor) member of
 * a java class.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PMethodDoc extends PExecutableMemberDoc
    implements net.cscott.gjdoc.MethodDoc {
    final Type returnType;
    <P extends Parameter, T extends Type>
    PMethodDoc(ParseControl pc, PClassDoc containingClass, int modifiers,
	       Type returnType, String name, PSourcePosition position,
	       List<P> parameters, List<T> thrownExceptions,
	       String commentText, PSourcePosition commentPosition,
	       TypeContext memberContext) {
	super(pc, containingClass, modifiers, name, position,
	      parameters, thrownExceptions,
	      commentText, commentPosition, memberContext);
	this.returnType = returnType;
    }
    public final boolean isAbstract() {
	return Modifier.isAbstract(modifierSpecifier());
    }
    // xxx return null if overridden method is not being documented.
    public MethodDoc overriddenMethod() {
	assert false : "unimplemented";
	return null;
    }
    public Type returnType() { return returnType; }

    // override methods from PDoc
    public boolean isMethod() { return true; }
}
