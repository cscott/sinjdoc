// RootDoc.java, created Wed Mar 19 12:56:45 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.List;
/**
 * The <code>RootDoc</code> class holds the information from one run of
 * GJDoc; in particular the packages, classes, and options specified by
 * the user.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.RootDoc
 */
public interface RootDoc extends Doc, DocErrorReporter {
    public List<ClassDoc> classes();
    public ClassDoc classNamed(String qualifiedName);
    public List<List<String>> options();
    public PackageDoc packageNamed(String name);
    public List<ClassDoc> specifiedClasses();
    public List<PackageDoc> specifiedPackages();
}
