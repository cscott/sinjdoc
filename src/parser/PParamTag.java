// ParamTag.java, created Wed Mar 19 12:42:42 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * The <code>PParamTag</code> class represents a @param documentation tag.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PParamTag extends PTag.Trailing
    implements net.cscott.gjdoc.ParamTag {
    final String parameterName;
    final List<Tag> parameterComment;
    PParamTag(SourcePosition sp, String name, List<Tag> contents)
	throws TagParseException {
	super(sp, name, contents);
	assert name()=="param";
	// now parse the tag.
	// parameter name should be first word in tag.
	if (contents.size()==0)
	    throw new TagParseException(position(), "Empty @param tag");
	Tag first = contents.get(0);
	if (!first.isText())
	    throw new TagParseException(first.position(),
					"Parameter name must not be a tag");
	Matcher matcher = NAME.matcher(first.text());
	if (!matcher.find())
	    throw new TagParseException(first.position(),
					"Can't find parameter name for "+
					"@param tag");
	String paramName = matcher.group();
	ArrayList<Tag> nContents = new ArrayList<Tag>(contents.size());
	if (matcher.end()!=first.text().length())
	    nContents.add
		(PTag.newTextTag
		 (first.text().substring(matcher.end()),
		  ((PSourcePosition)first.position()).add(matcher.end())));
	nContents.addAll(contents.subList(1, contents.size()));
	nContents.trimToSize();
	// okay, assign to the fields of the object.
	this.parameterName = paramName;
	this.parameterComment = Collections.unmodifiableList(nContents);
    }
    private static final Pattern NAME = Pattern.compile("\\S+");

    public List<Tag> parameterComment() { return parameterComment; }
    public String parameterName() { return parameterName; }
}
