// ExecutableMemberDoc.java, created Wed Mar 19 12:14:29 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.MethodTypeVariable;
import net.cscott.gjdoc.Parameter;
import net.cscott.gjdoc.ParamTag;
import net.cscott.gjdoc.ThrowsTag;
import net.cscott.gjdoc.Type;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * The <code>PExecutableMemberDoc</code> class represents a method or
 * constructor of a java class.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PExecutableMemberDoc extends PMemberDoc 
    implements net.cscott.gjdoc.ExecutableMemberDoc {
    final List<MethodTypeVariable> typeParameters =
	new ArrayList<MethodTypeVariable>(2);
    PExecutableMemberDoc(ParseControl pc, PClassDoc containingClass,
			 int modifiers, String name, PSourcePosition position,
			 String commentText, PSourcePosition commentPosition,
			 TypeContext commentContext) {
	super(pc, containingClass, modifiers, name, position,
	      commentText, commentPosition, commentContext);
    }
    public List<MethodTypeVariable> typeParameters() {
	return Collections.unmodifiableList(typeParameters);
    }
    public final boolean isNative() {
	return Modifier.isNative(modifierSpecifier());
    }
    public final boolean isSynchronized() {
	return Modifier.isSynchronized(modifierSpecifier());
    }
    public abstract List<Parameter> parameters();
    public abstract List<ParamTag> paramTags();
    public abstract String signature();
    public abstract List<Type> thrownExceptions();
    public abstract List<ThrowsTag> throwsTags();
    public String qualifiedName() {
	return containingClass().qualifiedName()+"."+name()+signature();
    }
    // methods abstract in PDoc
    public String name() { return name; }
}
