// SourcePosition.java, created Tue Mar 18 18:31:42 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import java.io.File;

/**
 * The <code>PSourcePosition</code> interface describes a source position:
 * filename, line number, and column number.
 * 
 * @author  C. Scott Ananian <cananian@alumni.princeton.edu>
 * @version $Id$
 */
abstract class PSourcePosition
    implements net.cscott.gjdoc.SourcePosition {
    /** The source file.  Returns null if no file information is available. */
    public abstract File file();
    /** The line in the source file.  The first line is numbered 1; 0 means
     *  no line number information is available. */
    public abstract int line();
    /** The column in the source file.  The first column is numbered 1; 0 means
     *  no column information is available.  Columns count characters in the
     *  input stream; a tab advances the column number to the next 8-column
     *  tab stop. */
    public abstract int column();
    /** Convert the source position to the form "Filename:line".
     */
    public abstract String toString();
}
