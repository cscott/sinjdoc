package net.cscott.gjdoc.lexer;

import net.cscott.gjdoc.parser.Sym;
import java_cup.runtime.Symbol;

class StringLiteral extends Literal {
  String val;
  StringLiteral(String s) { this.val = s; }

  Symbol token() { return new Symbol(Sym.STRING_LITERAL, val); }

  public String toString() { 
    return "StringLiteral <"+Token.escape(val)+">"; 
  }
}
