// PackageGroup.java, created Fri Apr  4 22:12:25 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cscott@cscott.net>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.PackageDoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * A <code>PackageGroup</code> represents a named group of packages
 * (specified by a pattern string) which will appear on the
 * documentation overview page.  <code>PackageGroup</code>s are
 * specified on the command-line with the <code>-group</code> option.
 * 
 * @author  C. Scott Ananian <cscott@cscott.net>
 * @version $Id$
 */
class PackageGroup {
    /** This text is placed in the table heading for the group. */
    public final String heading;
    /** The package patterns that this group matches. */
    public final List<String> patterns;
    /** The <code>PackageDoc</code> objects corresponding to this group. */
    private final List<PackageDoc> packageList = new ArrayList<PackageDoc>();
    /** Creates a <code>PackageGroup</code>. */
    public PackageGroup(String heading, String patternList) {
	this.heading=heading;
	this.patterns=Collections.unmodifiableList
	    (Arrays.asList(COLON.split(patternList)));
    }
    private static final Pattern COLON = Pattern.compile(";");
    public List<PackageDoc> packages() {
	return Collections.unmodifiableList(packageList);
    }

    static void groupPackages(List<PackageGroup> groups,
			      List<PackageDoc> allPackages) {
	List<PackageDoc> pkgs = new ArrayList<PackageDoc>(allPackages);
	for (Iterator<PackageGroup> it=groups.iterator(); it.hasNext(); ) {
	    PackageGroup pg = it.next();
	    pg.packageList.clear();
	    for (Iterator<String> it2=pg.patterns.iterator(); it2.hasNext(); ){
		String pattern = it2.next();
		for (Iterator<PackageDoc> it3=pkgs.iterator(); it3.hasNext();){
		    PackageDoc pd = it3.next();
		    if (matches(pattern, pd.name())) {
			pg.packageList.add(pd);
			it3.remove();
		    }
		}
	    }
	}
	assert pkgs.size()==0 : "catch-all rule not actually catching all";
    }
    private static boolean matches(String pattern, String packageName) {
	int idx = pattern.indexOf('*');
	if (idx < 0) return pattern.equals(packageName);
	else return packageName.startsWith(pattern.substring(0,idx));
    }
}
