// ConstructorDoc.java, created Wed Mar 19 12:00:41 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>ConstructorDoc</code> interface represents a constructor of
 * a java class.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 * @see com.sun.javadoc.ConstructorDoc
 */
public interface ConstructorDoc extends ExecutableMemberDoc {
    // only important member is 'qualifiedName', inherited from
    // ProgramElementDoc.
}
