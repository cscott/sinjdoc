// ClassDoc.java, created Wed Mar 19 11:39:43 2003 by cananian
// Copyright (C) 2003 C. Scott Ananian (cscott@cscott.net)
// Licensed under the terms of the GNU GPL; see COPYING for details.
package net.cscott.gjdoc.parser;

import net.cscott.gjdoc.ClassDoc;
import net.cscott.gjdoc.ClassTypeVariable;
import net.cscott.gjdoc.ConstructorDoc;
import net.cscott.gjdoc.FieldDoc;
import net.cscott.gjdoc.MethodDoc;
import net.cscott.gjdoc.PackageDoc;
import net.cscott.gjdoc.Type;

import java.util.List;
/**
 * The <code>PClassDoc</code> interface represents a java class and
 * raw type and provides access to information about the class, the
 * class' comment and tags, and the members of the class.  A
 * <code>PClassDoc</code> only exists if it was processed in this
 * run of javadoc.  References to classes which may or may not have
 * been processed in this run and parameterized types are referred to
 * using <code>Type</code> (components of which can be converted to
 * <code>ClassDoc</code>, if possible).
 * 
 * @author  C. Scott Ananian (cscott@cscott.net)
 * @version $Id$
 */
abstract class PClassDoc extends PProgramElementDoc
    implements net.cscott.gjdoc.ClassDoc {
    PClassDoc(ParserControl pc) { super(pc); }
    public abstract List<ClassTypeVariable> typeParameters();
    public abstract List<ConstructorDoc> constructors();
    public abstract boolean definesSerializableFields();
    public abstract List<FieldDoc> fields();
    public abstract ClassDoc findClass(String className);
    public abstract List<Type> importedClasses();
    public abstract List<PackageDoc> importedPackages();
    public abstract List<ClassDoc> innerClasses();
    public abstract List<Type> interfaces();
    public abstract boolean isAbstract();
    public abstract boolean isExternalizable();
    public abstract boolean isSerializable();
    public abstract List<MethodDoc> methods();
    public abstract List<FieldDoc> serializableFields();
    public abstract List<MethodDoc> serializationMethods();
    public abstract boolean subclassOf(ClassDoc cd);
    public abstract Type superclass();
}
