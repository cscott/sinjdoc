// MethodDoc.java, created Wed Mar 19 12:19:34 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.MethodDoc;
import net.cscott.gjdoc.Type;

/**
 * The <code>PMethodDoc</code> class represents a (non-constructor) member of
 * a java class.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PMethodDoc extends PExecutableMemberDoc
    implements net.cscott.gjdoc.MethodDoc {
    public abstract boolean isAbstract();
    public abstract Type overriddenClass();
    // xxx what if overridden method is not in this javadoc set?
    public abstract MethodDoc overriddenMethod();
    public abstract Type returnType();
}
