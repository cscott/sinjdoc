## Process this file with automake to produce Makefile.in
MAINTAINERCLEANFILES = Makefile.in
SUBDIRS = . parser html

AM_JAVACFLAGS = -g -warnunchecked
# JAVAROOT directory must exist!
JAVAROOT=$(top_builddir)/classes

CLASSPATH_ENV = CLASSPATH=$(JAVAROOT):$(top_srcdir)/lib/jutil.jar:$$CLASSPATH

# missing Main.java
noinst_JAVA = \
ClassDoc.java \
ClassTypeVariable.java \
ConstructorDoc.java \
Doc.java \
DocErrorReporter.java \
Doclet.java \
ExecutableMemberDoc.java \
FieldDoc.java \
MemberDoc.java \
MethodDoc.java \
MethodTypeVariable.java \
PackageDoc.java \
ParamTag.java \
Parameter.java \
ParameterizedType.java \
ProgramElementDoc.java \
RootDoc.java \
SeeTag.java \
SerialFieldTag.java \
SourcePosition.java \
Tag.java \
ThrowsTag.java \
Type.java \
TypeVariable.java

# automake 1.6.3 is broken: it doesn't actually include java sources
# in the distribution.  The following line works around this.
EXTRA_DIST = $(noinst_JAVA)
