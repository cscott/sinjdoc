// MethodDoc.java, created Wed Mar 19 12:19:34 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>MethodDoc</code> class represents a (non-constructor) member of
 * a java class.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.MethodDoc
 */
public interface MethodDoc extends ExecutableMemberDoc {
    public boolean isAbstract();
    public Type overriddenClass();
    // xxx what if overridden method is not in this javadoc set?
    public MethodDoc overriddenMethod();
    public Type returnType();
}
