// ParserControl.java, created Wed Mar 19 17:35:54 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.RootDoc;

import net.cscott.jutil.UniqueVector;

import java.lang.reflect.Modifier;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
/**
 * <code>ParserControl</code> runs the parser to generate a
 * <code>PRootDoc</code>.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
public class ParserControl {
    /** A means to report errors and warnings. */
    final DocErrorReporter reporter;
    /** Determines what classes and members to show. */
    int access = Modifier.PROTECTED;
    /** Determines parse verbosity. */
    boolean verbose=false;
    /** Source file encoding name. */
    String encoding=null;
    /** Source version to use. */
    int sourceVersion=5;
    /** Source files to document. */
    List<File> sourceFiles = Arrays.asList(new File[0]);
    /** Packages to document. */
    List<String> packages = Arrays.asList(new String[0]);
    /** Overview file. */
    File overview=null;

    public ParserControl(DocErrorReporter reporter) { this.reporter=reporter; }

    public void setVerbose(boolean verbose) { this.verbose=verbose; }
    public boolean getVerbose() { return verbose; }

    public String getEncoding() { return encoding; }
    public void setEncoding(String encoding) { this.encoding=encoding; }

    public void setAccess(int access) { this.access = access; }
    public void setSourceVersion(int v) {
	assert v>=1 && v<=5;
	this.sourceVersion=v;
    }
    public int getSourceVersion() { return this.sourceVersion; }

    public void setOverviewFile(File f) { this.overview=f; }

    public void setSourceFiles(List<File> sp) {
	// eliminate duplicates.
	this.sourceFiles = new UniqueVector<File>(sp);
    }
    public void setPackages(List<String> packages) {
	// eliminate duplicates
	this.packages = new UniqueVector<String>(packages);
    }

    public boolean showPublic() { return true; }
    public boolean showProtected() { return access!=Modifier.PUBLIC; }
    public boolean showPackage() { return access!=Modifier.PUBLIC &&
				       access!=Modifier.PROTECTED; }
    public boolean showPrivate() { return access==Modifier.PRIVATE; }


    public PRootDoc parse(FileUtil fu) {
	reporter.printNotice("PACKAGE NAMES: "+packages);
	reporter.printNotice("SOURCE FILE NAMES: "+sourceFiles);
	return null;
    }
}
