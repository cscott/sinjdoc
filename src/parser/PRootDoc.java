// RootDoc.java, created Wed Mar 19 12:56:45 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
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
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
public class PRootDoc extends PDoc
    implements net.cscott.gjdoc.RootDoc {
    final String overviewText;
    final PSourcePosition overviewPosition;
    PRootDoc(ParseControl pc) {
	super(pc);
	Pair<String,PSourcePosition> pair =
	    FileUtil.rawFileText(pc.overview, pc.reporter, pc.encoding);
	this.overviewText = pair.left;
	this.overviewPosition = pair.right;
    }

    public List<List<String>> options() { return options; }
    public void setOptions(List<List<String>> options) {
	this.options = options;
    }
    private List<List<String>> options=null;

    public List<ClassDoc> classes() {
	throw new RuntimeException("Unimplemented"); // XXX
    }
    public ClassDoc classNamed(String qualifiedName) {
	throw new RuntimeException("Unimplemented"); // XXX
    }
    public PackageDoc packageNamed(String name) {
	throw new RuntimeException("Unimplemented"); // XXX
    }
    public List<ClassDoc> specifiedClasses() {
	throw new RuntimeException("Unimplemented"); // XXX
    }
    public List<PackageDoc> specifiedPackages() {
	throw new RuntimeException("Unimplemented"); // XXX
    }

    // inherited from PDoc
    public String getRawCommentText() { return overviewText; }
    public PSourcePosition getRawCommentPosition() { return overviewPosition; }
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
