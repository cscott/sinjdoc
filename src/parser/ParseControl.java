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
    private static final boolean DEBUG=true;
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
    /** The root doc of this parser run. */
    PRootDoc rootDoc;

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
	rootDoc = new PRootDoc(this);
	// parse every source file in specified packages.
	for (Iterator<String> it=packages.iterator(); it.hasNext(); ) {
	    PPackageDoc ppd = rootDoc.findOrCreatePackage(it.next(), true);
	    for (Iterator<File> it2=sourcePath.sourceFilesInPackage(ppd.name())
		     .iterator(); it2.hasNext(); ) {
		File f = it2.next();
		// note that 'package' is non-null here because package is
		// always included.
		rootDoc.findOrCreateClasses(f, ppd);
	    }
	}
	// now parse the stand-alone source files.
	for (Iterator<File> it=sourceFiles.iterator(); it.hasNext(); ) {
	    File f = it.next();
	    // note that 'package' is null here because package may not be
	    // included.
	    rootDoc.findOrCreateClasses(f, null);
	}
	// we're done!
	return rootDoc;
    }

    // convenience fields for primitive types.
    final PClassType BYTE = new PEagerClassType(this, "", "byte");
    final PClassType SHORT = new PEagerClassType(this, "", "short");
    final PClassType INT = new PEagerClassType(this, "", "int");
    final PClassType LONG = new PEagerClassType(this, "", "long");
    final PClassType CHAR = new PEagerClassType(this, "", "char");
    final PClassType FLOAT = new PEagerClassType(this, "", "float");
    final PClassType DOUBLE = new PEagerClassType(this, "", "double");
    final PClassType BOOLEAN = new PEagerClassType(this, "", "boolean");
}
