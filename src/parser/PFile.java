package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.SourcePosition;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
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
    static PFile get(File f) {
	File canon;
	try { // canonicalize if possible.
	    canon = f.getCanonicalFile();
	} catch (java.io.IOException e) { // okay, then don't.
	    canon = f;
	}
	// lock because this is a static object; hence potentially shared
	// between multiple threads running the tool concurrently.
	synchronized(cache) {
	    if (!cache.containsKey(canon))
		cache.put(canon, new PFile(f/* preserve original name */));
	    return cache.get(canon);
	}
    }
    private static final Map<File,PFile> cache = new HashMap<File,PFile>();
}
