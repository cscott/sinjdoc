package net.cscott.gjdoc.lexer;

class EndOfLineComment extends Comment {
  EndOfLineComment(String comment) { appendLine(comment); }
}
