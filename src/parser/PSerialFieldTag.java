// SerialFieldTag.java, created Wed Mar 19 13:01:22 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.ClassType;
import net.cscott.gjdoc.SerialFieldTag;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;
import net.cscott.gjdoc.TagVisitor;
import net.cscott.gjdoc.Type;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The <code>PSerialFieldTag</code> class documents a Serializable field
 * defined by an ObjectStreamField.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PSerialFieldTag extends PTag.Trailing
    implements net.cscott.gjdoc.SerialFieldTag {
    final String fieldName;
    final Type   fieldType;
    final String fieldTypeString;
    final List<Tag> fieldDescription;
    PSerialFieldTag(SourcePosition sp, String name, List<Tag> contents,
		    TypeContext tagContext)
	throws TagParseException {
	super(sp, name, contents);
	assert name()=="serialField";
	//  parse the tag.
	Pair<Matcher,List<Tag>> pair =
	    extractRegexpFromHead(contents, NAME_AND_TYPE,
				  "field name and type");
	// okay, assign to the fields of the object.
	this.fieldName = pair.left.group(1);
	this.fieldTypeString = pair.left.group(2);
	// XXX what if field type is parameterized?
	this.fieldType = tagContext.lookupTypeName(fieldTypeString,true);
	this.fieldDescription = pair.right;
    }
    private static final Pattern NAME_AND_TYPE = Pattern.compile
	("(\\S+)\\s+(\\S+)");

    /** Compare based on source position. */
    public int compareTo(SerialFieldTag tag) {
	if (position().line() != tag.position().line())
	    return position().line() - tag.position().line();
	return position().column() - tag.position().column();
    }
    public List<Tag> description() { return fieldDescription; }
    public String fieldName() { return fieldName; }
    public String fieldType() { return fieldTypeString; }
    public ClassDoc fieldTypeDoc() {
	if (fieldType instanceof ClassType)
	    return ((ClassType)fieldType).asClassDoc();
	// XXX also link in the base type's doc for array and parameterized
	//     types?
	return null;
    }

    public <T> T accept(TagVisitor<T> visitor) { return visitor.visit(this); }
}
