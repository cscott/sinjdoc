// SourcePosition.java, created Tue Mar 18 18:31:42 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import java.io.File;

/**
 * The <code>PSourcePosition</code> interface describes a source position:
 * filename, line number, and column number.  The implementation represents
 * the position as a zero-based character index which it lazily converts
 * to line and column number when needed.
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
class PSourcePosition
    implements net.cscott.gjdoc.SourcePosition {
    // Represent source position as a File and a raw character index.
    // calls to the interface accessors lazily convert the index to a
    // line and column number.
    final PFile pfile; final int charIndex;
    PSourcePosition(PFile pfile, int charIndex) {
	this.pfile = pfile; this.charIndex = charIndex;
	assert charIndex >= 0;
    }
    public File file() { return pfile.file; }
    public int line() { return sp().line(); }
    public int column() { return sp().column(); }
    /** mathematical operations. */
    PSourcePosition add(int i) {
	return new PSourcePosition(pfile, charIndex+i);
    }
    PSourcePosition subtract(int i) {
	return new PSourcePosition(pfile, charIndex-i);
    }

    /** Convert the source position to the form "Filename:line".
     */
    public final String toString() {
	String filename = file()!=null?file().getPath():"<unknown>";
	return filename+":"+line();
    }
    // lookup line and column and cache.
    private transient net.cscott.gjdoc.SourcePosition sp=null;
    private net.cscott.gjdoc.SourcePosition sp() {
	if (sp==null) sp=pfile.convert(charIndex);
	assert sp!=null;
	return sp;
    }
    public static final net.cscott.gjdoc.SourcePosition NO_INFO =
	new net.cscott.gjdoc.SourcePosition() {
	    public File file() { return null; }
	    public int line() { return 0; }
	    public int column() { return 0; }
	};
}
