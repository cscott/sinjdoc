// MemberDoc.java, created Wed Mar 19 12:18:43 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import java.lang.reflect.Modifier;
/**
 * The <code>PMemberDoc</code> class represents a member of a java class:
 * field, constructor, or method.  This is an abstract class dealing with
 * information common to method, constructor, and field members.  Class
 * members of a class (inner classes) are represented instead by
 * <code>PClassDoc</code>.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PMemberDoc extends PProgramElementDoc
    implements net.cscott.gjdoc.MemberDoc {
    boolean isSynthetic = false;
    PMemberDoc(ParseControl pc, PClassDoc containingClass, int modifiers,
	       PSourcePosition position) {
	super(pc, containingClass.containingPackage(), containingClass,
	      modifiers, position);
    }
    public boolean isSynthetic() { return isSynthetic; }
}
