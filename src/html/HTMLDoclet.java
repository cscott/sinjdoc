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

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
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

    void makeStylesheet(RootDoc root, HTMLUtil hu) {
	// get a reader for the style sheet.
	Reader styleReader = null;
	if (options.stylesheetFile!=null) try {
	    styleReader = new FileReader(options.stylesheetFile);
	} catch (FileNotFoundException e) {
	    root.printError("Couldn't open "+options.stylesheetFile);
	}
	if (styleReader==null) styleReader=hu.resourceReader("stylesheet.css");
	// get a writer for the emitted style sheet.
	TemplateContext context = new TemplateContext
	    (root, options, new URLContext("stylesheet.css"), null, null);
	TemplateWriter styleWriter=new TemplateWriter(styleReader,hu,context);
	// copy from template.
	styleWriter.copyRemainder(root);
    }
    void makeTopIndex(RootDoc root, HTMLUtil hu) {
	// THREE CASES:
	//   zero packages specified: use index-nopackages.html with
	//     first class page in main frame.
	//   one package specified: use index-nopackages.html with
	//     package overview in main frame.
	//   multiple packages specified: use index-packages.html
	//(remember to use <unnamed package> in package list where appropriate)
	TemplateContext context = new TemplateContext
	    (root, options, new URLContext("index.html"), null, null);
	int numPackages = root.specifiedPackages().size();
	TemplateWriter indexWriter;
	String mainURL;

	if (numPackages==0)
	    mainURL=HTMLUtil.toURL((ClassDoc)Collections.min
				   ((List)root.specifiedClasses()));
	else if (numPackages==1 && root.specifiedClasses().size()==0)
	    mainURL=HTMLUtil.toURL(root.specifiedPackages().get(0));
	else
	    mainURL=null;

	if (mainURL==null) {
	    makeOverviewFrame(root, hu);
	    makeOverviewSummary(root, hu);
	    indexWriter=new TemplateWriter("index-packages.html",hu,context);
	} else {
	    mainURL = context.curURL.makeRelative(mainURL);
	    indexWriter=new TemplateWriter("index-nopackages.html",hu,context);
	    indexWriter.copyToSplit(root);
	    indexWriter.print(mainURL);
	    indexWriter.copyToSplit(root);
	    indexWriter.print(mainURL);
	}
	indexWriter.copyRemainder(root);
	// done!
    }
    void makeOverviewFrame(RootDoc root, HTMLUtil hu) {
	// first collect all referenced packages.
	Map<String,PackageDoc> pkgMap = new HashMap<String,PackageDoc>();
	for (Iterator<ClassDoc> it=root.classes().iterator(); it.hasNext(); ) {
	    PackageDoc pd = it.next().containingPackage();
	    pkgMap.put(pd.name(), pd);
	}
	// now sort them.
	List<PackageDoc> pkgList = new ArrayList<PackageDoc>(pkgMap.values());
	Collections.sort((List)pkgList);
	// okay, emit the overview-frame header:
	TemplateContext context = new TemplateContext
	    (root, options, new URLContext("overview-frame.html"), null, null);
	TemplateWriter tw=new TemplateWriter("overview-frame.html",hu,context);
	tw.copyToSplit(root);
	// now emit the sorted package list.
	for (Iterator<PackageDoc> it=pkgList.iterator(); it.hasNext(); )
	    tw.println(hu.toLink(it.next(), "package-frame.html"));
	// emit the footer.
	tw.copyRemainder(root);
	// XXX now emit package-frame, package-tree, and package-summary
	//     for these packages.
    }
    void makeOverviewSummary(RootDoc root, HTMLUtil hu) {
	// XXX do me.
    }

    public boolean start(RootDoc root) {
	// parse options.
	options.parseOptions(root.options());
	// create our HTMLUtil object.
	HTMLUtil hu = new HTMLUtil(root);
	// put the stylesheet where it belongs.
	makeStylesheet(root, hu);
	// top-level index.
	makeTopIndex(root, hu);
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
