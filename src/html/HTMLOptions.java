// HTMLOptions.java, created Mon Mar 31 14:05:16 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.DocErrorReporter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The <code>HTMLOptions</code> class encapsulates and parses all the
 * options given to the <code>HTMLDoclet</code>.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class HTMLOptions {
    boolean emitAuthor=false;


    public boolean validOption(List<String> optionWithArgs,
			       DocErrorReporter reporter) {
	return optionMap.get(optionWithArgs.get(0)).validate
	    (optionWithArgs, reporter);
    }
    public int optionLength(String option) {
	option = option.toLowerCase();
	if (optionMap.containsKey(option))
	    return optionMap.get(option).len;
	return 0;
    }
    public void printHelp(DocErrorReporter reporter) {
	List<Option> options = new ArrayList<Option>(optionMap.values());
	Collections.sort(options);
	for (Iterator<Option> it=options.iterator(); it.hasNext(); ) {
	    Option opt = it.next();
	    StringBuffer sb=new StringBuffer();
	    sb.append(opt.optionName);
	    sb.append(' ');
	    sb.append(opt.argSummary);
	    sb.append(' ');
	    while (sb.length()<34) sb.append(' ');
	    sb.append(opt.optionHelp);
	    reporter.printNotice(sb.toString());
	}
    }

    Map<String,Option> optionMap=new HashMap<String,Option>();
    void addOption(Option o) { optionMap.put(o.optionName, o); }
    {
	// options will be printed for help in the order in which they
	// are created here.
	addOption(new IgnoreOption("-d", "<directory>", 2,
				   "Destination directory for output files"));
	addOption(new IgnoreOption("-use", "", 1,
				   "Create class and package usage pages"));
	addOption(new IgnoreOption("-version", "", 1,
				   "Include @version paragraphs"));
	addOption(new IgnoreOption("-author", "", 1,
				   "Include @author paragraphs"));
	addOption(new IgnoreOption("-docfilessubdirs", "", 1,
				   "Recursively copy doc-file "+
				   "subdirectories"));
	addOption(new IgnoreOption("-splitindex", "", 1,
				   "Split index into one page per letter"));
	addOption(new IgnoreOption("-windowtitle", "<text>", 2,
				   "Browser window title for the "+
				   "documentation"));
	addOption(new IgnoreOption("-doctitle", "<html-code>", 2,
				   "Title for the overview page"));
	addOption(new IgnoreOption("-header", "<html-code>", 2,
				   "Header text to include on each page"));
	addOption(new IgnoreOption("-footer", "<html-code>", 2,
				   "Footer text to include on each page"));
	addOption(new IgnoreOption("-bottom", "<html-code>", 2,
				   "Bottom text to include on each page"));
	addOption(new IgnoreOption("-link", "<url>", 2,
				   "Create links to javadoc output at <url>"));
	addOption(new IgnoreOption("-linkoffline", "<url> <url2>", 3,
				   "Link to docs at <url> using package list "+
				   "at <url2>"));
	addOption(new IgnoreOption("-excludedocfilessubdir", "<name1>:..", 2,
				   "Exclude any doc-files subdirectories "+
				   "with given name"));
	addOption(new IgnoreOption("-group", "<name> <p1>:<p2>..", 3,
				   "Group specified packages together on "+
				   "overview page"));
	addOption(new IgnoreOption("-nocomment", "", 1,
				   "Suppress description and tags, generate "+
				   "only declarations"));
	addOption(new IgnoreOption("-nodeprecated", "", 1,
				   "Do not include @deprecated information"));
	addOption(new IgnoreOption("-noqualifier", "<name1>:<name2>:...", 2,
				   "Exclude the list of qualifiers from the "+
				   "output"));
	addOption(new IgnoreOption("-nosince", "", 1,
				   "Do not include @since information"));
	addOption(new IgnoreOption("-nodeprecatedlist", "", 1,
				   "Do not generate page listing deprecated "+
				   "API"));
	addOption(new IgnoreOption("-notree", "", 1,
				   "Do not generate class hierarchy"));
	addOption(new IgnoreOption("-noindex", "", 1,
				   "Do not generate index"));
	addOption(new IgnoreOption("-nohelp", "", 1,
				   "Do not generate help link"));
	addOption(new IgnoreOption("-nonavbar", "", 1,
				   "Do not generate navigation bar"));
	addOption(new IgnoreOption("-serialwarn", "", 1,
				   "Generate warning about @serial tag"));
	addOption(new IgnoreOption("-tag", "<name>:<locations>:<header>", 2,
				   "Specify single argument custom tags"));
	addOption(new IgnoreOption("-taglet", "", 2,
				   "Fully-qualified name of a Taglet class"));
	addOption(new IgnoreOption("-tagletpath", "<pathlist>", 2,
				   "Classpath for taglets"));
	addOption(new IgnoreOption("-charset", "<charset>", 2,
				   "Charset for cross-platform viewing of "+
				   "generated documentation"));
	addOption(new IgnoreOption("-helpfile", "<file>", 2,
				   "Source document for help page"));
	addOption(new IgnoreOption("-linksource", "", 1,
				   "Generate HTML for annotated java source"));
	addOption(new IgnoreOption("-stylesheetfile", "<path>", 2,
				   "Alternate style sheet for generated "+
				   "documentation"));
	addOption(new IgnoreOption("-docencoding", "<name>", 2,
				   "Output encoding name"));
	addOption(new IgnoreOption("-title", "<name>", 2,
				   "Deprecated synonym for -doctitle"));
    }

    private abstract class Option implements Comparable<Option> {
	private final int order;
	public final String optionName;
	public final int len;
	public final String argSummary;
	public final String optionHelp;
	Option(String optionName, String argSummary, int len,
	       String optionHelp) {
	    this.order = optionCounter++;
	    this.optionName = optionName;
	    this.argSummary = argSummary;
	    this.len = len;
	    this.optionHelp = optionHelp;
	}
	public int compareTo(Option o) { return this.order-o.order; }

	// intended to be overridden
	abstract void process(List<String> optionWithArgs);
	/** Returns true if the specified option & arguments are valid. */
	boolean validate(List<String> optionWithArgs,
			 DocErrorReporter reporter) {
	    /* do no validation by default. */
	    return true;
	}
    }
    private int optionCounter=0;
    private class IgnoreOption extends Option {
	IgnoreOption(String optionName, String argSummary, int len,
		     String optionHelp) {
	    super(optionName, argSummary, len, "[ignored]");
	}
	boolean validate(List<String> optionWithArgs,
			 DocErrorReporter reporter){
	    reporter.printWarning("IGNORING OPTION: "+optionWithArgs);
	    return true;
	}
	void process(List<String> optionWithArgs) { /* ignore */ }
    }
}// HTMLOptions
