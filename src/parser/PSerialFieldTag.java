// SerialFieldTag.java, created Wed Mar 19 13:01:22 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.SerialFieldTag;

/**
 * The <code>PSerialFieldTag</code> class documents a Serializable field
 * defined by an ObjectStreamField.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PSerialFieldTag extends PTag
    implements net.cscott.gjdoc.SerialFieldTag {
    public abstract int compareTo(SerialFieldTag tag);
    public abstract String description();
    public abstract String fieldName();
    public abstract String fieldType();
    public abstract ClassDoc fieldTypeDoc();
}
