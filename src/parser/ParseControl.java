// ParseControl.java, created Wed Mar 19 17:35:54 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
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
import java.util.Locale;
/**
 * <code>ParseControl</code> runs the parser to generate a
 * <code>PRootDoc</code>.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
public class ParseControl {
    /** A means to report errors and warnings. */
    final DocErrorReporter reporter;
    /** Determines what classes and members to show. */
    int access = Modifier.PROTECTED;
    /** Determines parse verbosity. */
    boolean verbose=false;
    /** Source file encoding name. */
    String encoding=null; // null means the default encoding.
    /** Source version to use. */
    int sourceVersion=5;
    /** Source files to document. */
    List<File> sourceFiles = Arrays.asList(new File[0]);
    /** Packages to document. */
    List<String> packages = Arrays.asList(new String[0]);
    /** Overview file. */
    File overview=null;
    /** Locale to use. */
    Locale locale = Locale.getDefault();
    /** Encapsulation of sourcePath-related file utilities. */
    FileUtil sourcePath;

    public ParseControl(DocErrorReporter reporter) { this.reporter=reporter; }

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
    
    public void setLocale(Locale l) { this.locale = l; }

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

    public void setSourcePath(String sourcePath) {
	assert this.sourcePath==null : "multiple calls to setSourcePath()";
	this.sourcePath = new FileUtil(sourcePath, sourceVersion);
    }
    public FileUtil getSourcePath() { return sourcePath; }

    public PRootDoc parse() {
	PRootDoc prd = new PRootDoc(this);
	// now parse every specified source file and every source file
	// contained in specified packages, adding the resulting
	// PClassDoc objects to the appropriate PPackageDoc reported
	// by the PRootDoc.
	// XXX UNIMPLEMENTED
	return prd;
    }
}