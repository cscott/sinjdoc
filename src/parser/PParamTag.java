// ParamTag.java, created Wed Mar 19 12:42:42 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * The <code>PParamTag</code> class represents a @param documentation tag.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PParamTag extends PTag.Trailing<Pair<String,List<Tag>>>
    implements net.cscott.gjdoc.ParamTag {
    PParamTag(SourcePosition sp, String name, List<Tag> contents,
	      TypeContext tagContext, DocErrorReporter reporter) {
	super(sp, name, contents, tagContext, reporter);
	assert name()=="param";
    }
    public List<Tag> parameterComment() { return parsed.right; }
    public String parameterName() { return parsed.left; }

    protected Pair<String,List<Tag>> parse(List<Tag> contents,
					   TypeContext tagContext,
					   DocErrorReporter reporter) {
	Pair<String,List<Tag>> ERROR = new Pair<String,List<Tag>>("",contents);
	// parameter name should be first word in tag.
	if (contents.size()==0) {
	    reporter.printError(position(), "Empty @param tag");
	    return ERROR;
	}
	Tag first = contents.get(0);
	if (!first.isText()) {
	    reporter.printError(first.position(), "Parameter name must not "+
				"be a tag");
	    return ERROR;
	}
	Matcher matcher = NAME.matcher(first.text());
	if (!matcher.find()) {
	    reporter.printError(first.position(), "Can't find parameter "+
				"name for @param tag");
	    return ERROR;
	}
	String name = matcher.group();
	List<Tag> nContents = new ArrayList<Tag>(contents.size());
	if (matcher.end()!=first.text().length())
	    nContents.add
		(PTag.newTextTag
		 (first.text().substring(matcher.end()),
		  ((PSourcePosition)first.position()).add(matcher.end())));
	nContents.addAll(contents.subList(1, contents.size()));
	return new Pair<String,List<Tag>>(name, nContents);
    }
    private static final Pattern NAME = Pattern.compile("\\S+");
}
