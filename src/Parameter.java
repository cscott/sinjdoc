// Parameter.java, created Wed Mar 19 12:38:54 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc;

/**
 * The <code>Parameter</code> interface represents parameter information,
 * including parameter type and parameter name.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 * @see com.sun.javadoc.Parameter
 */
public interface Parameter {
    public abstract String name();
    public abstract Type type();
    public abstract String toString();
}
