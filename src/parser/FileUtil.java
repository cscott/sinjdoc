// FileUtil.java, created Thu Mar 20 15:06:21 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cananian@alumni.princeton.edu>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.DocErrorReporter;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The <code>FileUtil</code> class contains some useful methods
 * for testing filenames and enumerating source files in packages.
 *
 * @author C. Scott Ananian <cananian@alumni.ptinceton.edu>
 * @version $Id$
 */
public class FileUtil {
    final List<File> sourcePath = new ArrayList<File>();
    final int sourceVersion;
    public FileUtil(String sourcePath, int sourceVersion) {
	assert sourceVersion>=1 && sourceVersion<=5;
	this.sourceVersion = sourceVersion;
	for (Iterator<String> it=splitPath(sourcePath).iterator();
	     it.hasNext(); ) {
	    File f = new File(it.next());
	    if (f.isDirectory())
		this.sourcePath.add(f);
	}
    }

    /** Returns true if the given string is a valid java package name. */
    public boolean isValidPackageName(String str) {
	for (Iterator<String> it=Arrays.asList(DOT.split(str,-1)).iterator();
	     it.hasNext(); )
	    if (!isValidIdentifier(it.next()))
		return false;
	return true;
    }
    /** Returns true if the given string is a valid java source file name. */
    public boolean isValidClassName(String str) {
	if (!str.toLowerCase().endsWith(".java")) return false;
	return isValidIdentifier(str.substring(0,str.length()-5));
    }
    /** Returns true if the given string is a valid java identifier. */
    public boolean isValidIdentifier(String str) {
	if (str.length()<1) return false;
	if (!Character.isJavaIdentifierStart(str.charAt(0))) return false;
	for(int i=1; i<str.length(); i++)
	    if (!Character.isJavaIdentifierPart(str.charAt(i)))
		return false;
	// check that it is not a reserved word.
	if (sourceVersion<4 && str.equals("assert")) return true;
	if (sourceVersion<2 && str.equals("strictfp")) return true;
	if (stoplist.matcher(str).matches()) return false;
	// okay, we've passed.
	return true;
    }
    /** a list of non-identifier words */
    private static final Pattern stoplist = Pattern.compile
	("true|false|null|abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while");
   
    /** Split the given string at the path separator character. */
    public static List<String> splitPath(String str) {
	String pathSep = System.getProperty("path.separator");
	if (pathSep==null) pathSep=":"; // safe default.
	return Arrays.asList(Pattern.compile(pathSep).split(str));
    }
    /** Split the given string at colons. */
    public static List<String> splitColon(String str) {
	return Arrays.asList(Pattern.compile(":").split(str));
    }
    /** Expand a fileglob with the '*' character to a list of matching
     *  files. */
    public static List<File> expandFileStar(String str) {
	// xxx note this doesn't work for strings like: asd/*/asdas.java
	int idx = str.indexOf('*');
	if (idx<0) return Collections.singletonList(new File(str));
	String lhs = str.substring(0, idx);
	final String rhs = str.substring(idx+1);
	File lhsF = new File(lhs);
	final String namePrefix = lhsF.isDirectory()?"":lhsF.getName();
	File dir = lhsF.isDirectory()?lhsF:lhsF.getParentFile();
	if (dir==null) dir=new File(".");
	return Arrays.asList(dir.listFiles(new FilenameFilter() {
		public boolean accept(File dir, String name) {
		    return
			name.length()>=(namePrefix.length()+rhs.length()) &&
			name.startsWith(namePrefix) &&
			name.endsWith(rhs);
		}
	    }));
    }
    public List<String> expandPackages(List<String> subpackages,
				       List<String> exclude) {
	List<String> result = new ArrayList<String>();
	for (Iterator<String> it=subpackages.iterator(); it.hasNext(); )
	    // expand the packages one at a time.
	    _expandOnePackage_(result, it.next(), exclude);
	return result;
    }
    private void _expandOnePackage_(List<String> result,
				    String packageName,
				    List<String> exclude) {
	// if this package is on the exclude list, we're done.
	for (Iterator<String> it=exclude.iterator(); it.hasNext(); ) {
	    String ex = it.next();
	    if (packageName.equals(ex) || packageName.startsWith(ex+"."))
		return; // done already!  quick finish.
	}
	// okay, it's not to be excluded.  Does it exist on the source path?
	File pkgDir = findPackage(packageName);
	if (pkgDir==null) return; // doesn't exist; we're done.
	// add this package to the result if it's got at least one valid
	// java source file in it.
	if (sourceFilesInPackage(pkgDir).size()>0)
	    result.add(packageName);
	// now look for subdirectories containing java source files.
	for (Iterator<File> it=Arrays.asList(pkgDir.listFiles(new FileFilter(){
		public boolean accept(File f) { return f.isDirectory(); }
	    })).iterator(); it.hasNext(); ) {
	    File subDir = it.next();
	    String newPkg = packageName+"."+subDir.getName();
	    // must be a valid name for a java package.
	    if (!isValidPackageName(newPkg)) continue;
	    // okay, then!
	    _expandOnePackage_(result, newPkg, exclude);
	}
    }
    public List<File> sourceFilesInPackage(String packageName) {
	File pkgDir = findPackage(packageName);
	if (pkgDir==null) return Arrays.asList(new File[0]);
	return sourceFilesInPackage(pkgDir);
    }
    private List<File> sourceFilesInPackage(File packageDir) {
	return Arrays.asList(packageDir.listFiles(new FileFilter() {
		public boolean accept(File f) {
		    if (!f.isFile()) return false;
		    if (!f.exists()) return false;
		    return isValidClassName(f.getName());
		}
	    }));
    }
    // find a directory in the source path corresponding to the given
    // package.
    private File findPackage(String packageName) {
	// convert package name to path.
	String path = DOT.matcher(packageName).replaceAll
	    (System.getProperty("file.separator"));
	for (Iterator<File> it=sourcePath.iterator(); it.hasNext(); ) {
	    File candidate = new File(it.next(), path);
	    if (candidate.isDirectory() && candidate.exists())
		return candidate;
	}
	return null; // couldn't find it.
    }
    private static final Pattern DOT = Pattern.compile("[.]");

    /** Extract the text between &lt;body&gt; and &lt;/body&gt; tags
     *  in the given file. */
    static Pair<String,PSourcePosition> rawFileText
	(File f, DocErrorReporter reporter, String encoding) {
	String contents = snarf(f, reporter, encoding);
	Matcher matcher = BODY_PATTERN.matcher(contents);
	if (matcher.find())
	    return new Pair<String,PSourcePosition>
		(matcher.group(1), new PSourcePosition
		 (new PFile(f), matcher.start(1)));
	// okay, just copy it all then.
	return new Pair<String,PSourcePosition>
	    (contents, new PSourcePosition(new PFile(f), 0));
    }
    private final static Pattern BODY_PATTERN = Pattern.compile
	("<\\s*body[^>]*>(.*)</\\s*body",
	 Pattern.CASE_INSENSITIVE|Pattern.DOTALL);

    /** Snarf up the contents of a file as a string. */
    static String snarf(File f, DocErrorReporter reporter, String encoding) {
	if (f==null) return "";
	if (!(f.exists() && f.isFile())) {
	    reporter.printError("Can't open file: "+f);
	    return "";
	}
	StringBuffer sb=new StringBuffer();
	try {
	    Reader reader = (encoding==null) ? new FileReader(f) :
		new InputStreamReader(new FileInputStream(f), encoding);
	    char[] buf=new char[8192];
	    int len;
	    while (-1!=(len=reader.read(buf)))
		sb.append(buf, 0, len);
	    reader.close();
	} catch (IOException e) {
	    reporter.printError("Trouble reading "+f+": "+e);
	}
	return sb.toString();
    }
}// FileUtil
