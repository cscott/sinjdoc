// TypeVariable.java, created Wed Mar 19 15:03:51 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ArrayType;
import net.cscott.gjdoc.ClassType;
import net.cscott.gjdoc.Type;
import net.cscott.gjdoc.TypeVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * The <code>PTypeVariable</code> interface represents a type
 * variable declared as a formal parameter to a generic class,
 * interface, or method.  Every actual type variable supports
 * exact one of the two subinterfaces <code>MethodTypeVariable</code> or
 * <code>ClassTypeVariable</code>.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see java.lang.reflect.TypeVariable
 */
abstract class PTypeVariable
    implements net.cscott.gjdoc.TypeVariable {
    final String name;
    final List<Type> bounds = new ArrayList<Type>(1);
    PTypeVariable(String name) {
	this.name = name;
    }
    <T extends Type> void addBounds(List<T> bounds) {
	this.bounds.addAll(bounds);
	assert this.bounds.size()>0;
	assert boundsValid();
    }
    /** Check that the bounds specified for the Type Variable are legit. */
    private boolean boundsValid() {
	boolean seenClass=false;
	for (Iterator<Type> it=bounds.iterator(); it.hasNext(); ) {
	    Type t = it.next();
	    assert !(t instanceof ArrayType);
	    if (!(t instanceof TypeVariable || TypeUtil.isInterface(t))) {
		assert !seenClass : "at most one bound can be non-interface";
		seenClass=true;
	    }
	}
	return true;
    }
    public String getName() { return name; }
    public List<Type> getBounds() {
	assert bounds.size()>0;
	assert boundsValid();
	return Collections.unmodifiableList(bounds);
    }
    public String signature() {
	return TypeUtil.erasedType(this).signature();
    }
    public String toString() {
	StringBuffer sb = new StringBuffer(getName());
	List<Type> shortList = new ArrayList<Type>(getBounds());
	// trim out java.lang.Object from 'extends' list.
	for (Iterator<Type> it=shortList.iterator(); it.hasNext(); ) {
	    Type ty = it.next();
	    if ((ty instanceof ClassType) &&
		((ClassType)ty).canonicalTypeName().equals("java.lang.Object"))
		it.remove();
	}
	// now print out remaining part of bounds list.
	if (shortList.size()>0) sb.append(" extends ");
	for (Iterator<Type> it=shortList.iterator(); it.hasNext(); ) {
	    sb.append(it.next().toString());
	    if (it.hasNext())
		sb.append(" & ");
	}
	return sb.toString();
    }
}
