// ThrowsTag.java, created Wed Mar 19 13:03:46 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;
import net.cscott.gjdoc.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * The <code>PThrowsTag</code> class represents a @throws or @exception
 * documentation tag.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @param
 */
class PThrowsTag extends PTag.Trailing
    implements net.cscott.gjdoc.ThrowsTag {
    final Type exceptionType;
    final String exceptionName;
    final List<Tag> exceptionComment;
    PThrowsTag(SourcePosition sp, String name, List<Tag> contents,
	       TypeContext tagContext) throws TagParseException {
	super(sp, name, contents);
	assert name()=="throws" || name()=="exception";
	//  now parse the tag.
	// exception name should be first word in tag.
	if (contents.size()==0)
	    throw new TagParseException(position(), "Empty @"+name()+" tag");
	Tag first = contents.get(0);
	if (!first.isText())
	    throw new TagParseException(first.position(),
					"Exception name must not be a tag");
	Matcher matcher = NAME.matcher(first.text());
	if (!matcher.find())
	    throw new TagParseException(first.position(),
					"Can't find exception name for "+
					"@"+name()+" tag");
	String exName = matcher.group();
	ArrayList<Tag> nContents = new ArrayList<Tag>(contents.size());
	if (matcher.end()!=first.text().length())
	    nContents.add
		(PTag.newTextTag
		 (first.text().substring(matcher.end()),
		  ((PSourcePosition)first.position()).add(matcher.end())));
	nContents.addAll(contents.subList(1, contents.size()));
	nContents.trimToSize();
	// okay, assign to the fields of the object.
	this.exceptionType = tagContext.lookupTypeName(exName);
	this.exceptionName = exName;
	this.exceptionComment = Collections.unmodifiableList(nContents);
    }
    private static final Pattern NAME = Pattern.compile("\\S+");

    public Type exception() { return exceptionType; }
    public List<Tag> exceptionComment() { return exceptionComment; }
    public String exceptionName() { return exceptionName; }
}
