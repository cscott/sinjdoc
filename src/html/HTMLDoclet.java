// HTMLDoclet.java, created Tue Mar 18 14:31:10 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.ClassType;
import net.cscott.gjdoc.Doclet;
import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.PackageDoc;
import net.cscott.gjdoc.RootDoc;

import java.util.*;
/**
 * The <code>HTMLDoclet</code> is the standard doclet for GJDoc.  It
 * generates HTML-format documentation.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
public class HTMLDoclet extends Doclet {
    HTMLOptions options = new HTMLOptions();
    public String getName() { return "Standard"; }

    public boolean start(RootDoc root) {
	// parse options.
	options.parseOptions(root.options());
	// look at overview document.
	System.out.println("OVERVIEW TAGS: "+root.tags());
	// look at packages
	for (Iterator<PackageDoc> it=root.specifiedPackages().iterator();
	     it.hasNext(); ) {
	    PackageDoc pd = it.next();
	    System.out.println("PACKAGE: "+pd.name());
	    System.out.println("TAGS: "+pd.tags());
	    System.out.println("FIRST: "+pd.firstSentenceTags());
	    System.out.println("CLASSES: "+pd.allClasses());
	}
	for (Iterator<ClassDoc> it=root.classes().iterator(); it.hasNext(); ){
	    ClassDoc cd = it.next();
	    System.out.println("CLASSDOC: "+cd.qualifiedName()+cd.type().typeParameters()+", super="+cd.superclass()+", interfaces="+cd.interfaces()+", comment="+cd.firstSentenceTags());
	}
	// create main index page
	// for each package listed...
	//   create package page.
	//   for each class in the package...
	//      create class page.
	return true; /* do nothing */
    }

    public int optionLength(String option) {
	return options.optionLength(option);
    }
    public boolean validOptions(List<List<String>> optionList,
				DocErrorReporter reporter) {
	for (Iterator<List<String>> it=optionList.iterator(); it.hasNext(); )
	    if (!options.validOption(it.next(), reporter))
		return false;
	// all options valid.
	return true;
    }
    public void optionHelp(DocErrorReporter reporter) {
	options.printHelp(reporter);
    }
}
