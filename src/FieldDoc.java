// FieldDoc.java, created Wed Mar 19 12:17:04 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.List;
/**
 * The <code>FieldDoc</code> class represents a field in a java class.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.FieldDoc
 */
public interface FieldDoc extends MemberDoc {
    public Object constantValue();
    public String constantValueExpression();
    public boolean isTransient();
    public boolean isVolatiile();
    public List<SerialFieldTag> serialFieldTags();
    public Type type();
}
