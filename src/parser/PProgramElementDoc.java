// ProgramElementDoc.java, created Wed Mar 19 12:49:27 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.PackageDoc;

import java.lang.reflect.Modifier;
/**
 * The <code>PProgramElementDoc</code> class represents a java program
 * element: class, interface, field, constructor, or method.  This is
 * an abstract class dealing with information common to these elements.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PProgramElementDoc extends PDoc
    implements net.cscott.gjdoc.ProgramElementDoc {
    PProgramElementDoc(ParserControl pc) { super(pc); }
    public abstract ClassDoc containingClass();
    public abstract PackageDoc containingPackage();
    public abstract int modifierSpecifier();
    public abstract String qualifiedName();
    public final boolean isFinal() {
	return Modifier.isFinal(modifierSpecifier());
    }
    public final boolean isPackage() {
	return !(isPublic() || isProtected() || isPrivate());
    }
    public final boolean isPrivate() {
	return Modifier.isPrivate(modifierSpecifier());
    }
    public final boolean isProtected() {
	return Modifier.isProtected(modifierSpecifier());
    }
    public final boolean isPublic() {
	return Modifier.isPublic(modifierSpecifier());
    }
    public final boolean isStatic() {
	return Modifier.isStatic(modifierSpecifier());
    }
    // XXX do we spell out synchronized and native?
    public final String modifiers() {
	return Modifier.toString(modifierSpecifier());
    }
}
