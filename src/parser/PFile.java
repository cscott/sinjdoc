package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.SourcePosition;

import java.io.File;
/**
 * The <code>PFile</code> class wraps a <code>java.io.File</code> object
 * and a lazily-created table which allows translation of raw character
 * indices to line numbers.
 *
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
public class PFile {
    public final File file;
    public PFile (File file) { this.file = file; }

    SourcePosition convert(int charIndex) {
	assert false : "not implemented";
	return new SourcePosition() {
		public File file() { return file; }
		public int line() { return 0; }
		public int column() { return 0; }
	    };
    }
}
