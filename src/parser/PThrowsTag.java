// ThrowsTag.java, created Wed Mar 19 13:03:46 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;
import net.cscott.gjdoc.TagVisitor;
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
	// parse the tag
	Pair<Matcher,List<Tag>> pair =
	    extractRegexpFromHead(contents, NAME, "exception name");
	// okay, assign to the fields of the object.
	this.exceptionName = pair.left.group();
	// XXX what if exception type is parameterized?
	this.exceptionType = tagContext.lookupTypeName(exceptionName);
	this.exceptionComment = pair.right;
    }
    private static final Pattern NAME = Pattern.compile("\\S+");

    public Type exception() { return exceptionType; }
    public List<Tag> exceptionComment() { return exceptionComment; }
    public String exceptionName() { return exceptionName; }

    public <T> T accept(TagVisitor<T> visitor) { return visitor.visit(this); }
}
