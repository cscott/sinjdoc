// RootDoc.java, created Wed Mar 19 12:56:45 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.PackageDoc;
import net.cscott.gjdoc.SourcePosition;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * The <code>PRootDoc</code> class holds the information from one run of
 * GJDoc; in particular the packages, classes, and options specified by
 * the user.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
public class PRootDoc extends PDoc
    implements net.cscott.gjdoc.RootDoc {
    final String overviewText;
    final PSourcePosition overviewPosition;
    final TypeContext overviewContext;
    private final Map<String,PPackageDoc> packageMap =
	new HashMap<String,PPackageDoc>();
    private final Map<File,PCompilationUnit> sourceFileMap =
	new HashMap<File,PCompilationUnit>();
    private final Map<String,PClassDoc> classMap =
	new HashMap<String,PClassDoc>();
    PRootDoc(ParseControl pc) {
	super(pc);
	Pair<String,PSourcePosition> pair =
	    FileUtil.rawFileText(pc.overview, pc.encoding, pc.reporter);
	this.overviewText = pair.left;
	this.overviewPosition = pair.right;
	this.overviewContext = new TypeContext(pc);
    }

    public List<List<String>> options() { return options; }
    public void setOptions(List<List<String>> options) {
	this.options = options;
    }
    private List<List<String>> options=null;

    public Collection<ClassDoc> classes() {
	List<ClassDoc> result = new ArrayList<ClassDoc>
	    (specifiedClasses());
	for (Iterator<PackageDoc> it=specifiedPackages().iterator();
	     it.hasNext(); )
	    result.addAll(it.next().includedClasses());
	return Collections.unmodifiableCollection(result);
    }
    public ClassDoc classNamed(String qualifiedName) {
	if (classMap.containsKey(qualifiedName))
	    return classMap.get(qualifiedName);
	return null; // not found.
    }
    // returns null if the named package is not in the packageMap.
    public PPackageDoc packageNamed(String name) {
	if (packageMap.containsKey(name)) return packageMap.get(name);
	return null;
    }
    /** Look for a package of the given name, creating it if it doesn't
     *  already exist in the packageMap. */
    PPackageDoc findOrCreatePackage(String name, boolean isIncluded) {
	if (!packageMap.containsKey(name)) {
	    assert isIncluded==pc.packages.contains(name);
	    PPackageDoc ppd = new PPackageDoc(pc, name, isIncluded);
	    packageMap.put(name, ppd);
	}
	assert packageMap.containsKey(name);
	return packageMap.get(name);
    }
    PCompilationUnit findOrCreateClasses(File f, PPackageDoc pkg) {
	if (!sourceFileMap.containsKey(f)) {
	    assert f.exists() && f.isFile();
	    PCompilationUnit pcu=new PCompilationUnit(f); // empty
	    try {
		printNotice("Parsing "+f);
		pcu = (PCompilationUnit)
		    new Java15(this, f, pkg).parse().value;
	    } catch (java.io.FileNotFoundException e) {
		assert false : "should never happen.";
		printError("File not found: "+e);
	    } catch (Exception e) {
		// syntax error, etc.
		printError("Syntax error: "+e); // XXX use source pos
	    }
	    sourceFileMap.put(f, pcu);
	    for (Iterator<PClassDoc> it=pcu.classes.iterator();
		 it.hasNext(); ) {
		PClassDoc pcd = it.next();
		classMap.put(pcd.qualifiedName(), pcd);
	    }
	}
	return sourceFileMap.get(f);
    }
    public List<ClassDoc> specifiedClasses() {
	List<ClassDoc> result = new ArrayList<ClassDoc>
	    (pc.sourceFiles.size());
	for (Iterator<File> it=pc.sourceFiles.iterator(); it.hasNext(); )
	    result.addAll(findOrCreateClasses(it.next(),null/*not included*/)
			  .classes);
	return Collections.unmodifiableList(result);
    }
    public List<PackageDoc> specifiedPackages() {
	List<PackageDoc> result = new ArrayList<PackageDoc>
	    (pc.packages.size());
	for (Iterator<String> it=pc.packages.iterator(); it.hasNext(); )
	    result.add(findOrCreatePackage(it.next(), true));
	return Collections.unmodifiableList(result);
    }

    // inherited from PDoc
    public String getRawCommentText() { return overviewText; }
    public PSourcePosition getRawCommentPosition() { return overviewPosition; }
    public TypeContext getCommentContext() { return overviewContext; }
    /** @return false */
    public boolean shouldStripStars() { return false; }
    public boolean isIncluded() { return true; }
    public String name() { return "overview"; }
    
    // DocErrorReporter interface.
    public void printError(String msg)
    { pc.reporter.printError(msg); }
    public void printError(SourcePosition pos, String msg)
    { pc.reporter.printError(pos, msg); }
    public void printWarning(String msg)
    { pc.reporter.printWarning(msg); }
    public void printWarning(SourcePosition pos, String msg)
    { pc.reporter.printWarning(pos, msg); }
    public void printNotice(String msg)
    { pc.reporter.printNotice(msg); }
    public void printNotice(SourcePosition pos, String msg)
    { pc.reporter.printNotice(pos, msg); }
}
