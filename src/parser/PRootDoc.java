// RootDoc.java, created Wed Mar 19 12:56:45 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.PackageDoc;
import net.cscott.gjdoc.SourcePosition;

import java.util.List;
/**
 * The <code>PRootDoc</code> class holds the information from one run of
 * GJDoc; in particular the packages, classes, and options specified by
 * the user.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
public abstract class PRootDoc extends PDoc
    implements net.cscott.gjdoc.RootDoc {
    PRootDoc(DocErrorReporter reporter) { this.reporter=reporter; }
    final DocErrorReporter reporter;

    public List<List<String>> options() { return options; }
    public void setOptions(List<List<String>> options) {
	this.options = options;
    }
    private List<List<String>> options=null;

    public abstract List<ClassDoc> classes();
    public abstract ClassDoc classNamed(String qualifiedName);
    public abstract PackageDoc packageNamed(String name);
    public abstract List<ClassDoc> specifiedClasses();
    public abstract List<PackageDoc> specifiedPackages();

    public void printError(String msg)
    { reporter.printError(msg); }
    public void printError(SourcePosition pos, String msg)
    { reporter.printError(pos, msg); }
    public void printWarning(String msg)
    { reporter.printWarning(msg); }
    public void printWarning(SourcePosition pos, String msg)
    { reporter.printWarning(pos, msg); }
    public void printNotice(String msg)
    { reporter.printNotice(msg); }
    public void printNotice(SourcePosition pos, String msg)
    { reporter.printNotice(pos, msg); }
}
