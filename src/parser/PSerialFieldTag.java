// SerialFieldTag.java, created Wed Mar 19 13:01:22 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.SerialFieldTag;
import net.cscott.gjdoc.SourcePosition;
import net.cscott.gjdoc.Tag;

import java.util.List;

/**
 * The <code>PSerialFieldTag</code> class documents a Serializable field
 * defined by an ObjectStreamField.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PSerialFieldTag extends PTag.Trailing
    implements net.cscott.gjdoc.SerialFieldTag {
    PSerialFieldTag(SourcePosition sp, String name, List<Tag> contents) {
	super(sp, name, contents);
	assert name()=="serialField";
    }
    public abstract int compareTo(SerialFieldTag tag);
    public abstract List<Tag> description();
    public abstract String fieldName();
    public abstract String fieldType();
    public abstract ClassDoc fieldTypeDoc();
}
