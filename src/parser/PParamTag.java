// ParamTag.java, created Wed Mar 19 12:42:42 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

/**
 * The <code>PParamTag</code> class represents a @param documentation tag.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PParamTag extends PTag
    implements net.cscott.gjdoc.ParamTag {
    public abstract String parameterComment();
    public abstract String parameterName();
}
