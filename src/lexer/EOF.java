package net.cscott.gjdoc.lexer;

import net.cscott.gjdoc.parser.Sym;
import java_cup.runtime.Symbol;

class EOF extends Token {
  EOF() {}
  Symbol token() { return new Symbol(Sym.EOF); }
  public String toString() { return "EOF"; }
}
