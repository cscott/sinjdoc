// PEagerClassType.java, created Wed Mar 26 11:55:02 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassTypeVariable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/**
 * The <code>PEagerClassType</code> class represents a fully-resolved
 * non-parameterized class type.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PEagerClassType extends PClassType {
    final String packageName;
    final String className; // dots here indicate inner classes.
    final List<ClassTypeVariable> typeParameters;
    PEagerClassType(ParseControl pc,
		    String packageName, String className,
		    List<ClassTypeVariable> typeParameters) {
	super(pc);
	this.packageName = packageName.intern();
	this.className = className.intern();
	this.typeParameters = typeParameters;
    }
    PEagerClassType(ParseControl pc,
		    String packageName, String className) {
	this(pc, packageName, className, NO_VARS);
    }
    private static final List<ClassTypeVariable> NO_VARS =
	Arrays.asList(new ClassTypeVariable[0]);

    public List<ClassTypeVariable> typeParameters() {
	return Collections.unmodifiableList(typeParameters);
    }
    public String typeName() { return className; }
    public String canonicalTypeName() {
	if (packageName.length()==0) return className;
	return packageName+"."+className;
    }
}// PEagerClassType
