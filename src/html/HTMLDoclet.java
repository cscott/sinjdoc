// HTMLDoclet.java, created Tue Mar 18 14:31:10 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.Doclet;
import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.RootDoc;

import java.util.*;
/**
 * <code>HTMLDoclet</code>
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
public class HTMLDoclet extends Doclet {
    public String getName() { return "Standard"; }

    public boolean start(RootDoc root) {
	// create main index page
	// for each package listed...
	//   create package page.
	//   for each class in the package...
	//      create class page.
	return true; /* do nothing */
    }

    public int optionLength(String option) {
	option = option.toLowerCase();
	if (optionMap.containsKey(option))
	    return optionMap.get(option).intValue();
	return 0;
    }
    public boolean validOptions(List<List<String>> options,
				DocErrorReporter reporter) {
	// XXX no checking done yet.
	// XXX note that options must be case-insensitive.
	return true;
    }
    public void optionHelp(DocErrorReporter reporter) {
	// XXX just list options for now.
	for (Iterator<String> it=optionMap.keySet().iterator(); it.hasNext();)
	    reporter.printNotice(it.next()+" \t[no documentation]");
    }
    static Map<String,Integer> optionMap = new HashMap<String,Integer>();
    static {
	optionMap.put("-author", new Integer(1));
	optionMap.put("-bottom", new Integer(2));
	optionMap.put("-charset", new Integer(2));
	optionMap.put("-d", new Integer(2));
	optionMap.put("-docencoding", new Integer(2));
	optionMap.put("-docfilessubdirs", new Integer(1));
	optionMap.put("-doctitle", new Integer(2));
	optionMap.put("-excludedocfilessubdir", new Integer(2));
	optionMap.put("-footer", new Integer(2));
	optionMap.put("-group", new Integer(3));
	optionMap.put("-header", new Integer(2));
	optionMap.put("-helpfile", new Integer(2));
	optionMap.put("-link", new Integer(2));
	optionMap.put("-linkoffline", new Integer(3));
	optionMap.put("-linksource", new Integer(1));
	optionMap.put("-nocomment", new Integer(1));
	optionMap.put("-nodeprecated", new Integer(1));
	optionMap.put("-nodeprecatedlist", new Integer(1));
	optionMap.put("-nohelp", new Integer(1));
	optionMap.put("-noindex", new Integer(1));
	optionMap.put("-nonavbar", new Integer(1));
	optionMap.put("-noqualifier", new Integer(2));
	optionMap.put("-nosince", new Integer(1));
	optionMap.put("-notree", new Integer(1));
	optionMap.put("-serialwarn", new Integer(1));
	optionMap.put("-splitindex", new Integer(1));
	optionMap.put("-stylesheetfile", new Integer(2));
	optionMap.put("-tag", new Integer(2));
	optionMap.put("-taglet", new Integer(2));
	optionMap.put("-tagletpath", new Integer(2));
	optionMap.put("-title", new Integer(2));
	optionMap.put("-use", new Integer(1));
	optionMap.put("-version", new Integer(1));
	optionMap.put("-windowtitle", new Integer(2));
    }
}
