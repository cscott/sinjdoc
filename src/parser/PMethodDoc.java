// MethodDoc.java, created Wed Mar 19 12:19:34 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.MethodDoc;
import net.cscott.gjdoc.Type;

/**
 * The <code>PMethodDoc</code> class represents a (non-constructor) member of
 * a java class.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PMethodDoc extends PExecutableMemberDoc
    implements net.cscott.gjdoc.MethodDoc {
    PMethodDoc(ParserControl pc) { super(pc); }
    public abstract boolean isAbstract();
    // xxx return null if overridden method is not being documented.
    public abstract MethodDoc overriddenMethod();
    public abstract Type returnType();
}
