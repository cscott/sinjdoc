// SeeTag.java, created Wed Mar 19 12:59:15 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.MemberDoc;
import net.cscott.gjdoc.PackageDoc;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * The <code>PSeeTag</code> class represents a "see also" documentation
 * tag.  The @see tag can be plain text, or reference a class or
 * member.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see java.lang.Object#hash(int,float) a great looking class
 *     {@underline foo} yo {@link java.lang} blah {@link #hashCode()} foo
 */
class PSeeTag extends PTag.NonText
    implements net.cscott.gjdoc.SeeTag {
    final List<Tag> label;
    final String classPart;
    final String memberNamePart;
    final String memberArgsPart;
    PSeeTag(SourcePosition sp, String name, List<Tag> contents,
	    TypeContext tagContext) throws TagParseException {
	super(sp, name, contents);
	assert name()=="see" || name()=="link" || name()=="linkplain";
	char firstChar = extractRegexp
	    (contents, FIRSTCHAR, "non-space character")
	    .left.group().charAt(0);
	if (firstChar=='\"') { // string.
	    // xxx should strip the " characters from the start and end of
	    // the tags list.
	    throw new RuntimeException("unimplemented");
	} else if (firstChar=='<') { // href
	    // xxx should strip the <a href="..."> and </a> from start and
	    // end of tags list.
	    throw new RuntimeException("unimplemented");
	} else { // java member reference.
	    Pair<Matcher,List<Tag>> pair =
		extractRegexp(contents, JREF,
			      "java package, class, or member reference");
	    this.label = pair.right;
	    this.classPart = pair.left.group(1);
	    this.memberNamePart = pair.left.group(2);
	    this.memberArgsPart = pair.left.group(3);
	}
    }
    private static final Pattern FIRSTCHAR = Pattern.compile("^\\s*(\\S)");
    private static final Pattern STRING = Pattern.compile("\"([^\"]*)\"");
    private static final Pattern HREF = Pattern.compile("<[^>]*>([^<]*)<");
    // insanely complicated regex.  We anchor at the start and allow leading
    // spaces, to keep the regex from skipping malformed bits at the beginning.
    // Then we say there is *either* a class/pattern specifier *or* the
    // first character is '#'.  Then we have an optional "#member" and an
    // optional "(args)" section.  Lastly, we say there either must be trailing
    // space or it must be the end of the string.  This keeps us from skipping
    // malformed trailing bits.
    private static final Pattern JREF = Pattern.compile
	("^\\s*(?:([^#\\s\\(,\\)]+)|(?=#))(?:#([^\\(,\\)\\s\\.]+)(\\([^\\(\\)]*\\))?)?(?:\\z|\\s+)");
	
    
    public boolean isTrailing() { return name=="see"; }
    public boolean isInline() { return name=="link" || name=="linkplain"; }
    // parsed values
    public List<Tag> label() { return label; }
    public String referencedClassName() { return classPart; }
    public String referencedMemberName() {
	if (memberNamePart==null) return null;
	StringBuffer sb = new StringBuffer(memberNamePart);
	if (memberArgsPart!=null) sb.append(memberArgsPart);
	return sb.toString();
    }
    public ClassDoc referencedClass() { assert false; return null; }
    public MemberDoc referencedMember() { assert false; return null; }
    public PackageDoc referencedPackage() { assert false; return null; }
}
