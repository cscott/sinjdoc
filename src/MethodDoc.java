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
    /** Return true if this method is abstract. */
    public boolean isAbstract();
    /** Return the method that this method overrides, or <code>null</code>
     *  if there is no such method. */
    public MethodDoc overriddenMethod();
    /** Return the return type of this method. */
    public Type returnType();
}
