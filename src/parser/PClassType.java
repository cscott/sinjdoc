// PClassType.java, created Mon Mar 24 14:08:22 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.Type;
import java.io.File;
import java.util.Iterator;
/**
 * The <code>PClassType</code> class represents an abstract type
 * name that can possibly be converted into a <code>ClassDoc</code>
 * object.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
// XXX SHOULD BE SPLIT into subclasses -- PEagerClassType for Types where the
// split between package and class part is known exactly (typeName
// always returns just the class part), and a PLazyClassType class
// where the split has to be determined later.  Corresponding
// eager and lazy PParameterizedType implementations.
public class PClassType implements Type {
    final PRootDoc rootDoc;
    final PPackageDoc packageScope;
    final PCompilationUnit compilationUnit;
    final PClassDoc classScope;
    String name;
    int dimension;
    PClassType(PRootDoc rootDoc,
	       PPackageDoc packageScope,
	       PCompilationUnit compilationUnit,
	       PClassDoc classScope,
	       String name) {
	this.rootDoc = rootDoc;
	this.packageScope = packageScope;
	this.compilationUnit = compilationUnit;
	this.classScope = classScope;
	this.name = name;
    }
    public ClassDoc asClassDoc() {
	return rootDoc.classNamed(qualifiedTypeName());
    }
    public String dimension() {
	StringBuffer sb=new StringBuffer();
	for (int i=0; i<dimension; i++)
	    sb.append("[]");
	return sb.toString();
    }
    public String qualifiedTypeName() {
	// cache value, because lookup is expensive.
	if (qualNameCache==null)
	    qualNameCache=lookupTypeName(typeName());
	return qualNameCache;
    }
    transient String qualNameCache=null;
    private String lookupTypeName(String typeName) {
	int idx = typeName.lastIndexOf('.');
	if (idx<0) return lookupSimpleTypeName(typeName);
	else return lookupQualifiedTypeName(typeName.substring(0,idx),
					    typeName.substring(idx+1));
    }
    private String lookupSimpleTypeName(String id) {
	assert id.indexOf('.')<0;
	// 1. not handling local class declarations.
	// 2. if the simple type name occurs within the scope of a visible
	//    member type, it denotes that type (if more than one, error)
	if (classScope!=null)
	    for (Iterator<ClassDoc> it=classScope.innerClasses().iterator();
		 it.hasNext(); ) {
		ClassDoc cd = it.next();
		if (id.equals(cd.name())) return cd.qualifiedName();
	    }
	// 3. if type is declared in the current compilation unit, either
	//    by simple-type-import or by declaration, then denotes that type
	if (compilationUnit!=null) {
	    for (Iterator<String> it = compilationUnit.singleTypeImport
		     .iterator(); it.hasNext(); ) {
		String qualName = it.next();
		if (qualName.endsWith(id)) return qualName;
	    }
	    for (Iterator<PClassDoc> it = compilationUnit.classes
		     .iterator(); it.hasNext(); ) {
		PClassDoc cd = it.next();
		if (id.equals(cd.name())) return cd.qualifiedName();
	    }
	}
	// 4. if type is declared in other compilation unit of the package
	//    containing the identifier, then denotes that type.
	if (packageScope!=null)
	    for (Iterator<Type> it = packageScope.allClasses().iterator();
		 it.hasNext(); ) {
		Type t = it.next();
		if (id.equals(t.typeName())) return t.qualifiedTypeName();
	    }
	// 5. if type is declared by a type-import-on-demand delcaration,
	//    then denotes that type (if more than one, error)
	if (compilationUnit!=null)
	    for (Iterator<PPackageDoc> it = compilationUnit.onDemandImport
		     .iterator(); it.hasNext(); )
		for (Iterator<Type> it2 = it.next().allClasses().iterator();
		     it2.hasNext(); ) {
		    Type t = it2.next();
		    if (id.equals(t.typeName())) return t.qualifiedTypeName();
		}
	// 6. Otherwise, undefined; if not error then it could be in
	//    same package or in an opaque type-import-on-demand.
	//    we'll just make it opaque.
	return "<unknown>."+id;
    }
    private String lookupQualifiedTypeName(String Q, String id) {
	// recursively determine whether Q is a package or type name.
	// then determine if 'id' is a type within Q
	//   1) try package first.
	PPackageDoc pkg = rootDoc.packageNamed(Q);
	if (pkg!=null)
	    for (Iterator<Type> it = pkg.allClasses().iterator();
		 it.hasNext(); ) {
		Type t = it.next();
		if (id.equals(t.typeName())) return t.qualifiedTypeName();
	    }
	//   2) try class named Q.
	ClassDoc cls = rootDoc.classNamed(lookupTypeName(Q));
	if (cls!=null)
	    for (Iterator<ClassDoc> it=cls.innerClasses().iterator();
		 it.hasNext(); ) {
		ClassDoc cd = it.next();
		if (id.equals(cd.name())) return cd.qualifiedName();
	    }
	// give up; assume is fully qualified.
	return Q+"."+id;
    }
    public String typeName() { return name; }
    public String toString() {
	return typeName()+dimension();
    }
}// PClassType
