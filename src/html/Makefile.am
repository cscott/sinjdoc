## Process this file with automake to produce Makefile.in
MAINTAINERCLEANFILES = Makefile.in
AM_JAVACFLAGS = -g -warnunchecked

JAVAROOT=$(top_builddir)/classes

noinst_JAVA = \
HTMLDoclet.java \
Version.java

# automake 1.6.3 is broken: it doesn't actually include java sources
# in the distribution.  The following line works around this.
EXTRA_DIST := $(noinst_JAVA)
