package net.cscott.gjdoc.lexer;

import net.cscott.gjdoc.parser.Sym;
import java_cup.runtime.Symbol;

class IntegerLiteral extends NumericLiteral {
  IntegerLiteral(int i) { this.val = new Integer(i); }

  Symbol token() { return new Symbol(Sym.INTEGER_LITERAL, val); }
}
