// ParserControl.java, created Wed Mar 19 17:35:54 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.RootDoc;

import java.lang.reflect.Modifier;
import java.io.File;
import java.util.ArrayList;
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
    /** Sourcepath to use. */
    List<File> sourcePath = new ArrayList<File>();
    /** Source version to use. */
    int sourceVersion=5;

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
    public void setSourcePath(List<String> sp) {
	sourcePath.clear();
	for (Iterator<String> it=sp.iterator(); it.hasNext(); ) {
	    File f = new File(it.next());
	    if (f.isDirectory())
		sourcePath.add(f);
	    else
		reporter.printWarning("Ignoring non-directory source path "+f);
	}
    }

    public boolean showPublic() { return true; }
    public boolean showProtected() { return access!=Modifier.PUBLIC; }
    public boolean showPackage() { return access!=Modifier.PUBLIC &&
				       access!=Modifier.PROTECTED; }
    public boolean showPrivate() { return access==Modifier.PRIVATE; }


    public RootDoc parse(List<String> packageNames,
			 List<String> sourcefileNames,
			 List<String> subpackages,
			 List<String> excludePackages,
			 List<List<String>> docletOptions) {
	reporter.printNotice("PACKAGE NAMES: "+packageNames);
	reporter.printNotice("SOURCE FILE NAMES: "+sourcefileNames);
	reporter.printNotice("SUBPACKAGES: "+subpackages);
	reporter.printNotice("EXCLUDE PACKAGES: "+excludePackages);
	reporter.printNotice("DOCLET OPTIONS: "+docletOptions);
	return null;
    }
}
