// ParamTag.java, created Wed Mar 19 12:42:42 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

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
class PParamTag extends PTag.Trailing
    implements net.cscott.gjdoc.ParamTag {
    PParamTag(SourcePosition sp, String name, List<Tag> contents) {
	super(sp, name, contents);
	assert name()=="param";
    }
    public List<Tag> parameterComment() { return parse().right; }
    public String parameterName() { return parse().left; }

    private Pair<String,List<Tag>> parse() {
	// parameter name should be first word in tag.
	List<Tag> contents = contents();
	// xxx how to handle errors?  just throwing exceptions for now.
	if (contents.size()==0) throw new RuntimeException("bad tag");
	Tag first = contents.get(0);
	if (!first.isText()) throw new RuntimeException("bad tag");
	Matcher matcher = NAME.matcher(first.text());
	if (!matcher.find()) throw new RuntimeException("bad tag");
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
