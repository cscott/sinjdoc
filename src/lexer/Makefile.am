## Process this file with automake to produce Makefile.in
MAINTAINERCLEANFILES = Makefile.in
AM_JAVACFLAGS = -g -warnunchecked
EXTRA_DIST := package.html

JAVAROOT=$(top_builddir)/classes

CLASSPATH_ENV = CLASSPATH=$(JAVAROOT):$(top_srcdir)/lib/cup.jar:$$CLASSPATH

noinst_JAVA = \
BooleanLiteral.java \
CharacterLiteral.java \
Comment.java \
DocumentationComment.java \
DoubleLiteral.java \
EOF.java \
EndOfLineComment.java \
EscapedUnicodeReader.java \
FIFO.java \
FloatLiteral.java \
Identifier.java \
InputElement.java \
IntegerLiteral.java \
Keyword.java \
Lexer.java \
Literal.java \
LongLiteral.java \
NullLiteral.java \
NumericLiteral.java \
Operator.java \
Separator.java \
StringLiteral.java \
Token.java \
TraditionalComment.java \
WhiteSpace.java

# automake 1.6.3 is broken: it doesn't actually include java sources
# in the distribution.  The following line works around this.
EXTRA_DIST += $(noinst_JAVA)
