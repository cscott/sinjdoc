// ThrowsTag.java, created Wed Mar 19 13:03:46 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.Type;

/**
 * The <code>PThrowsTag</code> class represents a @throws or @exception
 * documentation tag.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PThrowsTag extends PTag
    implements net.cscott.gjdoc.ThrowsTag {
    public abstract Type exception();
    public abstract String exceptionComment();
    public abstract String exceptionName();
}
