package net.cscott.gjdoc.lexer;

import net.cscott.gjdoc.parser.Sym;
import java_cup.runtime.Symbol;

class CharacterLiteral extends Literal {
  Character val;
  CharacterLiteral(char c) { this.val = new Character(c); }

  Symbol token() { return new Symbol(Sym.CHARACTER_LITERAL, val); }

  public String toString() { 
    return "CharacterLiteral <"+Token.escape(val.toString())+">"; 
  }
}
