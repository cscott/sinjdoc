package net.cscott.sinjdoc.lexer;

import java_cup.runtime.Symbol;

class EOF extends Token {
  EOF() {}
  Symbol token() { return new Symbol(Sym.EOF); }
  public String toString() { return "EOF"; }
}
