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
	    mainURL=HTMLUtil.toURL(Collections.min
				   (root.specifiedClasses(),
				    new DocComparator<ClassDoc>()));
	else if (numPackages==1 && root.specifiedClasses().size()==0)
	    mainURL=HTMLUtil.toURL(root.specifiedPackages().get(0),
				   "package-summary.html");
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
	// sort all referenced packages.
	List<PackageDoc> pkgList = new ArrayList<PackageDoc>
	    (allDocumentedPackages(root));
	Collections.sort(pkgList, new DocComparator<PackageDoc>());
	// okay, emit the overview-frame header:
	TemplateContext context = new TemplateContext
	    (root, options, new URLContext("overview-frame.html"), null, null);
	TemplateWriter tw=new TemplateWriter("overview-frame.html",hu,context);
	tw.copyToSplit(root);
	// now emit the sorted package list.
	for (Iterator<PackageDoc> it=pkgList.iterator(); it.hasNext(); )
	    tw.println(hu.toLink(context.curURL, it.next(),
				 "package-frame.html"));
	// emit the footer.
	tw.copyRemainder(root);
	// now emit package-frame.html for these packages.
	//  (not referenced except by this file)
	for (Iterator<PackageDoc> it=pkgList.iterator(); it.hasNext(); )
	    makePackageFrame(root, hu, it.next());
    }
    void makeOverviewSummary(RootDoc root, HTMLUtil hu) {
	TemplateContext context = new TemplateContext
	    (root, options, new URLContext("overview-summary.html"),null,null);
	// XXX do me.
    }
    void makeAllClassesFrame(RootDoc root, HTMLUtil hu) {
	// create sorted list of all documented classes.
	List<ClassDoc> clsList = new ArrayList<ClassDoc>(root.classes());
	Collections.sort(clsList, new DocComparator<ClassDoc>());
	// now emit!
	TemplateContext context = new TemplateContext
	    (root, options, new URLContext("allclasses-frame.html"),null,null);
	TemplateWriter tw = new TemplateWriter
	    ("allclasses-frame.html", hu, context);
	tw.copyToSplit(root);
	for (Iterator<ClassDoc> it=clsList.iterator(); it.hasNext(); )
	    tw.println(hu.toLink(context.curURL, it.next(), true));
	tw.copyRemainder(root);
    }
    void makePackageFrame(RootDoc root, HTMLUtil hu, PackageDoc pd) {
	TemplateContext context = new TemplateContext
	    (root, options, new URLContext(hu.toURL(pd, "package-frame.html")),
	     pd, null);
	TemplateWriter tw = new TemplateWriter
	    ("package-frame.html"/*resource*/, hu, context);
	tw.copyToSplit(root);
	// dump interfaces
	for (Iterator<ClassDoc> it=pd.includedInterfaces().iterator();
	     it.hasNext(); )
	    tw.println(hu.toLink(context.curURL, it.next(), true));
	tw.copyToSplit(root);
	// dump ordinary classes
	for (Iterator<ClassDoc> it=pd.includedOrdinaryClasses().iterator();
	     it.hasNext(); )
	    tw.println(hu.toLink(context.curURL, it.next(), true));
	tw.copyToSplit(root);
	// dump exceptions
	for (Iterator<ClassDoc> it=pd.includedErrors().iterator();
	     it.hasNext(); )
	    tw.println(hu.toLink(context.curURL, it.next(), true));
	tw.copyToSplit(root);
	// dump errors
	for (Iterator<ClassDoc> it=pd.includedExceptions().iterator();
	     it.hasNext(); )
	    tw.println(hu.toLink(context.curURL, it.next(), true));
	// done!
	tw.copyRemainder(root);
    }
    void makePackageSummary(RootDoc root, HTMLUtil hu, PackageDoc pd) {
	TemplateContext context = new TemplateContext
	    (root,options, new URLContext(hu.toURL(pd,"package-summary.html")),
	     pd, null);
	// xxx do me.
    }
    void makePackageTree(RootDoc root, HTMLUtil hu, PackageDoc pd) {
	TemplateContext context = new TemplateContext
	    (root, options, new URLContext(hu.toURL(pd, "package-tree.html")),
	     pd, null);
	// xxx do me.
    }
    void makeClassPage(RootDoc root, HTMLUtil hu, ClassDoc cd) {
	TemplateContext context = new TemplateContext
	    (root, options, new URLContext(hu.toURL(cd)),
	     cd.containingPackage(), cd);
	// xxx do me.
    }

    private static List<PackageDoc> allDocumentedPackages(RootDoc root) {
	// first collect all referenced packages.
	Map<String,PackageDoc> pkgMap = new LinkedHashMap<String,PackageDoc>();
	for (Iterator<ClassDoc> it=root.classes().iterator(); it.hasNext(); ) {
	    PackageDoc pd = it.next().containingPackage();
	    pkgMap.put(pd.name(), pd);
	}
	Collection<PackageDoc> c = pkgMap.values();
	return Arrays.asList(c.toArray(new PackageDoc[c.size()]));
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
	// list all documented classes.
	makeAllClassesFrame(root, hu);
	// top-level class hierarchy.
	if (options.emitTreePage) {
	    // XXX create overview-tree.html
	}
	// for each package to be documented...
	for (Iterator<PackageDoc> it=allDocumentedPackages(root).iterator();
	     it.hasNext(); ) {
	    PackageDoc pd = it.next();
	    // create package pages.
	    makePackageSummary(root, hu, pd);
	    if (options.emitTreePage)
		makePackageTree(root, hu, pd);
	    // XXX copy doc-files.
	}
	// for each class to be documented...
	for (Iterator<ClassDoc> it=root.classes().iterator(); it.hasNext(); ){
	    ClassDoc cd = it.next();
	    // create class page.
	    makeClassPage(root, hu, cd);
	}
	// XXX create package-list
	if (options.emitUsePage) {
	    // XXX create class-use
	}
	if (options.emitIndexPage) {
	    // XXX create index pages
	}
	if (options.emitHelpPage) {
	    // XXX create help-doc.html
	}
	if (options.emitDeprecatedPage) {
	    // XXX create deprecated-list.html
	}
	// XXX create constant-values.html
	// XXX create serialized-form.html
	// XXX create annotated source code.
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
