// ParamTag.java, created Wed Mar 19 12:42:42 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;

import java.util.List;
/**
 * The <code>PParamTag</code> class represents a @param documentation tag.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PParamTag extends PTag.Trailing
    implements net.cscott.gjdoc.ParamTag {
    PParamTag(SourcePosition sp, String name, List<Tag> contents) {
	super(sp, name, contents);
	assert name()=="param";
    }
    public abstract List<Tag> parameterComment();
    public abstract String parameterName();
}
