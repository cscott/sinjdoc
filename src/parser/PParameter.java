// Parameter.java, created Wed Mar 19 12:38:54 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.sinjdoc.parser;

import net.cscott.sinjdoc.Type;

/**
 * The <code>PParameter</code> interface represents parameter information,
 * including parameter type and parameter name.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PParameter implements net.cscott.sinjdoc.Parameter {
    final Type type;
    final String name;
    PParameter(Type type, String name) {
	this.type = type; this.name = name;
    }
    public Type type() { return type; }
    public String name() { return name; }

    public final String toString() {
	return type().toString()+" "+name();
    }
}
