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
/**
 * The <code>PSeeTag</code> class represents a "see also" documentation
 * tag.  The @see tag can be plain text, or reference a class or
 * member.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PSeeTag extends PTag.NonText
    implements net.cscott.gjdoc.SeeTag {
    PSeeTag(SourcePosition sp, String name, List<Tag> contents,
	    TypeContext tagContext) {
	super(sp, name, contents);
	assert name()=="see" || name()=="link" || name()=="linkplain";
    }
    public boolean isTrailing() { return name=="see"; }
    public boolean isInline() { return name=="link" || name=="linkplain"; }
    // parse!
    public abstract String label();
    public abstract ClassDoc referencedClass();
    public abstract String referencedClassName();
    public abstract MemberDoc referencedMember();
    public abstract String referencedMemberName();
    public abstract PackageDoc referencedPackage();
}
