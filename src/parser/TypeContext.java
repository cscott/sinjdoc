// PTypeContext.java, created Wed Mar 26 11:33:08 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.ClassType;
import net.cscott.gjdoc.ClassTypeVariable;
import net.cscott.gjdoc.MethodTypeVariable;
import net.cscott.gjdoc.Type;

import java.util.Iterator;
import java.util.List;
/**
 * The <code>TypeContext</code> class encapsulates all of the information
 * needed to resolve a string specifying a type info a fully-qualified
 * type name.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class TypeContext {
    final PRootDoc rootDoc;
    final PPackageDoc packageScope;
    final PCompilationUnit compilationUnit;
    final PClassDoc classScope; // class type variables available here.
    final PMethodDoc methodScope; // method type variables available here.

    public TypeContext(PRootDoc rootDoc, PPackageDoc packageScope,
		       PCompilationUnit compilationUnit, PClassDoc classScope,
		       PMethodDoc methodScope) {
	this.rootDoc = rootDoc;
	this.packageScope = packageScope;
	this.compilationUnit = compilationUnit;
	this.classScope = classScope;
	this.methodScope = methodScope;
    }
    /** A "no context" constructor. Appropriate for fully-qualified names. */
    public TypeContext(PRootDoc rootDoc) {
	this(rootDoc, null, null, null, null);
    }

    // type resolution methods.
    /** Look up the given type name in this type context. */
    Type lookupTypeName(String typeName,
			List<ClassTypeVariable> typeParameters) {
	int idx = typeName.lastIndexOf('.');
	if (idx<0) return lookupSimpleTypeName(typeName, typeParameters);
	else return lookupQualifiedTypeName(typeName.substring(0,idx),
					    typeName.substring(idx+1),
					    typeParameters);
    }
    // look up a simple type name; that is, one without a '.'
    private Type lookupSimpleTypeName(String id,
				      List<ClassTypeVariable> typeParameters) {
	assert id.indexOf('.')<0;
	// 1. not handling local class declarations.
	// (1b) check method type variables.
	if (methodScope!=null)
	    for (Iterator<MethodTypeVariable> it =
		     methodScope.typeParameters().iterator(); it.hasNext(); ) {
		MethodTypeVariable mtv = it.next();
		if (id.equals(mtv.getName())) return mtv;
	    }
	// 2. if the simple type name occurs within the scope of a visible
	//    member type, it denotes that type (if more than one, error)
	if (classScope!=null) {
	    // class type variables.
	    for (Iterator<ClassTypeVariable> it =
		     classScope.type().typeParameters().iterator();
		 it.hasNext(); ) {
		ClassTypeVariable ctv = it.next();
		if (id.equals(ctv.getName())) return ctv;
	    }
	    // class member types
	    for (Iterator<ClassDoc> it=classScope.innerClasses().iterator();
		 it.hasNext(); ) {
		ClassDoc cd = it.next();
		if (id.equals(cd.name())) return cd.type();
	    }
	}
	// 3. if type is declared in the current compilation unit, either
	//    by simple-type-import or by declaration, then denotes that type
	if (compilationUnit!=null) {
	    for (Iterator<String> it = compilationUnit.singleTypeImport
		     .iterator(); it.hasNext(); ) {
		String qualName = it.next();
		if (qualName.endsWith(id))
		    return new TypeContext(rootDoc).lookupTypeName
			(qualName, typeParameters);
	    }
	    for (Iterator<PClassDoc> it = compilationUnit.classes
		     .iterator(); it.hasNext(); ) {
		PClassDoc cd = it.next();
		if (id.equals(cd.name())) return cd.type();
	    }
	}
	// 4. if type is declared in other compilation unit of the package
	//    containing the identifier, then denotes that type.
	if (packageScope!=null)
	    for (Iterator<ClassType> it = packageScope.allClasses().iterator();
		 it.hasNext(); ) {
		ClassType t = it.next();
		if (id.equals(t.typeName())) return t;
	    }
	// 5. if type is declared by a type-import-on-demand delcaration,
	//    then denotes that type (if more than one, error)
	if (compilationUnit!=null)
	    for (Iterator<PPackageDoc> it = compilationUnit.onDemandImport
		     .iterator(); it.hasNext(); )
		for (Iterator<ClassType> it2=it.next().allClasses().iterator();
		     it2.hasNext(); ) {
		    ClassType t = it2.next();
		    if (id.equals(t.typeName())) return t;
		}
	// 6. Otherwise, undefined; if not error then it could be in
	//    same package or in an opaque type-import-on-demand.
	//    we'll just make it opaque.
	return new PEagerClassType(rootDoc, "<unknown>", id, typeParameters);
    }
    private Type lookupQualifiedTypeName(String Q, String id,
					 List<ClassTypeVariable> typeParameters){
	// recursively determine whether Q is a package or type name.
	// then determine if 'id' is a type within Q
	//   1) try package first.
	PPackageDoc pkg = rootDoc.packageNamed(Q);
	// XXX also try creating an 'unincluded' package for reflection?
	if (pkg!=null)
	    for (Iterator<ClassType> it = pkg.allClasses().iterator();
		 it.hasNext(); ) {
		ClassType t = it.next();
		if (id.equals(t.typeName())) return t;
	    }
	//   2) try class named Q.
	Type t = lookupTypeName(Q, typeParameters);
	ClassDoc cls = (t instanceof ClassType) ?
	    ((ClassType)t).asClassDoc() : null;
	if (cls!=null)
	    for (Iterator<ClassDoc> it=cls.innerClasses().iterator();
		 it.hasNext(); ) {
		ClassDoc cd = it.next();
		if (id.equals(cd.name())) return cd.type();
	    }
	//      XXX try this using reflection.
	// give up; assume is fully qualified.
	// XXX should add to pkg if pkg!=null?  should create package?
	return new PEagerClassType(rootDoc, Q, id, typeParameters);
    }
}// TypeContext
