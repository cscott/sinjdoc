package net.cscott.gjdoc.lexer;

abstract class NumericLiteral extends Literal {
  Number val;

  public String toString() { return "NumericLiteral <"+val.toString()+">"; }
}
