// FieldDoc.java, created Wed Mar 19 12:17:04 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.SerialFieldTag;
import net.cscott.gjdoc.Type;

import java.util.List;
/**
 * The <code>PFieldDoc</code> class represents a field in a java class.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PFieldDoc extends PMemberDoc
    implements net.cscott.gjdoc.FieldDoc {
    public abstract Object constantValue();
    public abstract String constantValueExpression();
    public abstract boolean isTransient();
    public abstract boolean isVolatiile();
    public abstract List<SerialFieldTag> serialFieldTags();
    public abstract Type type();
}
