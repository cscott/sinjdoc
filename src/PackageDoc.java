// PackageDoc.java, created Wed Mar 19 12:23:13 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * The <code>PackageDoc</code> class represents a java package.  It
 * provides access to information about the package, the package's
 * comment and tags (!), and the classes in the package.  It does *not*
 * necessarily represent a package included in the current GJDoc run.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.PackageDoc
 */
public interface PackageDoc extends Doc {
    public List<Type> allClasses();
    public List<ClassDoc> includedClasses();
    public List<ClassDoc> includedErrors();
    public List<ClassDoc> includedExceptions();
    public List<ClassDoc> includedInterfaces();
    public List<ClassDoc> includedOrdinaryClasses();
    // xxx fully qualified name, or just partial name?  let's say either.
    public ClassDoc findClass(String className);
}
