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
class PSourcePosition
    implements net.cscott.gjdoc.SourcePosition {
    final File file; final int line; final int column;
    PSourcePosition(File file, int line, int column) {
	this.file = file; this.line = line; this.column = column;
    }
    /** The source file.  Returns null if no file information is available. */
    public File file() { return file; }
    /** The line in the source file.  The first line is numbered 1; 0 means
     *  no line number information is available. */
    public int line() { return line; }
    /** The column in the source file.  The first column is numbered 1; 0 means
     *  no column information is available.  Columns count characters in the
     *  input stream; a tab advances the column number to the next 8-column
     *  tab stop. */
    public int column() { return column; }
    /** Convert the source position to the form "Filename:line".
     */
    public final String toString() {
	String filename = file()!=null?file().getPath():"<unknown>";
	return filename+":"+line();
    }

    public static final net.cscott.gjdoc.SourcePosition NO_INFO =
	new PSourcePosition(null, 0, 0);
}
