// PackageDoc.java, created Wed Mar 19 12:23:13 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * The <code>PPackageDoc</code> class represents a java package.  It
 * provides access to information about the package, the package's
 * comment and tags (!), and the classes in the package.  It does *not*
 * necessarily represent a package included in the current GJDoc run.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PPackageDoc extends PDoc
    implements net.cscott.gjdoc.PackageDoc {
    public abstract List<Type> allClasses();
    public List<ClassDoc> includedClasses() {
	List<ClassDoc> list = new ArrayList<ClassDoc>();
	list.addAll(includedInterfaces());
	list.addAll(includedOrdinaryClasses());
	list.addAll(includedErrors());
	list.addAll(includedExceptions());
	return Collections.unmodifiableList(list);
    }
    public List<ClassDoc> includedErrors() {
	List<ClassDoc> list = new ArrayList<ClassDoc>(includedClasses());
	for (Iterator<ClassDoc> it=list.iterator(); it.hasNext(); )
	    if (!it.next().isError()) it.remove();
	return Collections.unmodifiableList(list);
    }
    public List<ClassDoc> includedExceptions() {
	List<ClassDoc> list = new ArrayList<ClassDoc>(includedClasses());
	for (Iterator<ClassDoc> it=list.iterator(); it.hasNext(); )
	    if (!it.next().isException()) it.remove();
	return Collections.unmodifiableList(list);
    }
    public List<ClassDoc> includedInterfaces() {
	List<ClassDoc> list = new ArrayList<ClassDoc>(includedClasses());
	for (Iterator<ClassDoc> it=list.iterator(); it.hasNext(); )
	    if (!it.next().isInterface()) it.remove();
	return Collections.unmodifiableList(list);
    }
    public List<ClassDoc> includedOrdinaryClasses() {
	List<ClassDoc> list = new ArrayList<ClassDoc>(includedClasses());
	for (Iterator<ClassDoc> it=list.iterator(); it.hasNext(); )
	    if (!it.next().isOrdinaryClass()) it.remove();
	return Collections.unmodifiableList(list);
    }
    // xxx fully qualified name, or just partial name?  let's say either.
    public ClassDoc findClass(String className) {
	for (Iterator<ClassDoc> it=includedClasses().iterator();
	     it.hasNext(); ) {
	    ClassDoc cd = it.next();
	    if (cd.name().equals(className) ||
		cd.qualifiedName().equals(className))
		return cd;
	}
	return null; // not found.
    }
}
