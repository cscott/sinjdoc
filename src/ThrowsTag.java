// ThrowsTag.java, created Wed Mar 19 13:03:46 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.List;
/**
 * The <code>ThrowsTag</code> class represents a @throws or @exception
 * documentation tag.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see com.sun.javadoc.ThrowsTag
 */
public interface ThrowsTag extends Tag {
    /** Return a <code>Type</code> object representing the exception. */
    public Type exception();
    /** Return the exception comment associated with this
     *  <code>ThrowsTag</code>>. */
    public List<Tag> exceptionComment();
    /** Return the name of the exception associated with this
     *  <code>ThrowsTag</code>. */
    public String exceptionName();
}
