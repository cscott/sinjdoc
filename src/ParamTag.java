// ParamTag.java, created Wed Mar 19 12:42:42 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.List;
/**
 * The <code>ParamTag</code> class represents a @param documentation tag.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see com.sun.javadoc.ParamTag
 */
public interface ParamTag extends Tag {
    /** Return the parameter comment associated with this
     *  <code>ParamTag</code>. */
    public List<Tag> parameterComment();
    /** Return the name of the parameter associated with this
     *  <code>ParamTag</code>. */
    public String parameterName();
}
