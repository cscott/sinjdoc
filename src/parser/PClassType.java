// PClassType.java, created Mon Mar 24 14:08:22 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.ClassType;
import net.cscott.gjdoc.ClassTypeVariable;
import net.cscott.gjdoc.Type;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/**
 * The <code>PClassType</code> class represents an abstract type
 * name that can possibly be converted into a <code>ClassDoc</code>
 * object.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PClassType implements ClassType {
    final ParseControl pc;
    PClassType(ParseControl pc) {
	this.pc = pc;
    }
    public final ClassDoc asClassDoc() {
	return pc.rootDoc.classNamed(qualifiedTypeName());
    }
    public final String toString() {
	return typeName();
    }
}// PClassType
