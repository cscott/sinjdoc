// ConstructorDoc.java, created Wed Mar 19 12:00:41 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.Parameter;
import net.cscott.gjdoc.Type;

import java.util.List;
/**
 * The <code>PConstructorDoc</code> interface represents a constructor of
 * a java class.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PConstructorDoc extends PExecutableMemberDoc 
    implements net.cscott.gjdoc.ConstructorDoc {
    <P extends Parameter, T extends Type>
    PConstructorDoc(ParseControl pc, PClassDoc containingClass, int modifiers,
		    PSourcePosition position,
		    List<P> parameters, List<T> thrownExceptions,
		    String commentText, PSourcePosition commentPosition,
		    TypeContext commentContext) {
	super(pc, containingClass, modifiers, containingClass.name(), position,
	      parameters, thrownExceptions,
	      commentText, commentPosition, commentContext);
    }
    // only important member is 'qualifiedName', inherited from
    // ProgramElementDoc.

    // override methods from PDoc
    public boolean isConstructor() { return true; }
}
