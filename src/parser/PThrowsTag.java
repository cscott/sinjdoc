// ThrowsTag.java, created Wed Mar 19 13:03:46 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;
import net.cscott.gjdoc.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * The <code>PThrowsTag</code> class represents a @throws or @exception
 * documentation tag.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PThrowsTag extends PTag.Trailing<Pair<Type,List<Tag>>>
    implements net.cscott.gjdoc.ThrowsTag {
    PThrowsTag(SourcePosition sp, String name, List<Tag> contents,
	       TypeContext tagContext, DocErrorReporter reporter) {
	super(sp, name, contents, tagContext, reporter);
	assert name()=="throws" || name()=="exception";
    }
    public Type exception() { return parsed.left; }
    public List<Tag> exceptionComment() { return parsed.right; }
    public String exceptionName() { return parsed.toString(); }
    // parse.
    protected Pair<Type,List<Tag>> parse(List<Tag> contents,
					 TypeContext tagContext,
					 DocErrorReporter reporter) {
	Pair<Type,List<Tag>> ERROR = new Pair<Type,List<Tag>>
	    (new PEagerClassType(tagContext.pc,"java.lang","Object"),contents);
	// parameter name should be first word in tag.
	if (contents.size()==0) {
	    reporter.printError(position(), "Empty @"+name()+" tag");
	    return ERROR;
	}
	Tag first = contents.get(0);
	if (!first.isText()) {
	    reporter.printError(first.position(), "Exception name must not "+
				"be a tag");
	    return ERROR;
	}
	Matcher matcher = NAME.matcher(first.text());
	if (!matcher.find()) {
	    reporter.printError(first.position(), "Can't find exception "+
				"name for @"+name()+" tag");
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
	return new Pair<Type,List<Tag>>(tagContext.lookupTypeName(name),
					nContents);
    }
    private static final Pattern NAME = Pattern.compile("\\S+");
}
