// TemplateContext.java, created Tue Apr  1 11:58:12 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian <cscott@cscott.net>
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.html;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.MemberDoc;
import net.cscott.gjdoc.PackageDoc;
import net.cscott.gjdoc.RootDoc;

/**
 * The <code>TemplateContext</code> class encapsulates all the information
 * required to emit an HTML page using the macro expander.
 * 
 * @author  C. Scott Ananian <cscott@cscott.net>
 * @version $Id$
 */
class TemplateContext {
    public final RootDoc root;
    public final HTMLOptions options;
    public final PackageDoc curPackage;
    public final ClassDoc curClass;
    public final MemberDoc curMember;
    public final URLContext curURL;
    public final boolean echo;
    
    /** Creates a <code>TemplateContext</code>. */
    public TemplateContext(RootDoc root, HTMLOptions options,URLContext curURL,
			   PackageDoc curPackage, ClassDoc curClass) {
	this(root,options,curURL,curPackage,curClass,null, true);
    }
    /** Creates a <code>TemplateContext</code>. */
    private TemplateContext(RootDoc root, HTMLOptions options,
			    URLContext curURL,
			    PackageDoc curPackage, ClassDoc curClass,
			    MemberDoc curMember, boolean echo) {
	this.root = root;
	this.options = options;
	this.curPackage = curPackage;
	this.curClass = curClass;
	this.curMember = curMember;
	this.curURL = curURL;
	this.echo = echo;
	assert root!=null && options!=null && curURL!=null;
    }
    TemplateContext echoOff() {
	return new TemplateContext(root,options,curURL,curPackage,curClass,
				   curMember,false);
    }
}
