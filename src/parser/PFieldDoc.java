// FieldDoc.java, created Wed Mar 19 12:17:04 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.SerialFieldTag;
import net.cscott.gjdoc.Type;

import java.lang.reflect.Modifier;
import java.util.List;
/**
 * The <code>PFieldDoc</code> class represents a field in a java class.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PFieldDoc extends PMemberDoc
    implements net.cscott.gjdoc.FieldDoc {
    final Type type;
    final String name;
    PFieldDoc(ParseControl pc, PClassDoc containingClass, int modifiers,
	      Type type, String name, PSourcePosition position) {
	super(pc, containingClass, modifiers, position);
	this.type = type;
	this.name = name;
    }
    public abstract Object constantValue();
    public abstract String constantValueExpression();
    public final boolean isTransient() {
	return Modifier.isTransient(modifierSpecifier());
    }
    public final boolean isVolatile() {
	return Modifier.isVolatile(modifierSpecifier());
    }
    public abstract List<SerialFieldTag> serialFieldTags();
    public Type type() { return type; }
    // methods abstract in PDoc
    public String name() { return name; }
    // override methods from PDoc
    public boolean isField() { return true; }
}
