// ExecutableMemberDoc.java, created Wed Mar 19 12:14:29 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

import java.util.List;
/**
 * The <code>ExecutableMemberDoc</code> class represents a method or
 * constructor of a java class.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.ExecutableMemberDoc
 */
public interface ExecutableMemberDoc extends MemberDoc {
    public List<MethodTypeVariable> typeParameters();
    public String flatSignature();
    public boolean isNative();
    public boolean isSynchronized();
    public List<Parameter> parameters();
    public List<ParamTag> paramTags();
    public String signature();
    public List<Type> thrownExceptions();
    public List<ThrowsTag> throwsTags();
}
