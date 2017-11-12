package compiladores;

import java.io.IOException;

public class Sintatic {
	Token tok;
	Lexer lex;
	void advance() throws IOException {
            tok = lex.scan();
        }
        
	void error() {
            System.out.println("Unexpected Token");
	}
	
	boolean eat(int tag) {
            return tag == tok.tag;
	}
	
	void identList() {
		
	}
	
	void decl() {
		switch(tok.tag) {
			case Tag.INT: 
				eat(Tag.INT);
				identList();
				eat(Tag.DOTCOM);
				break;
			case Tag.STRING:
				decl(); 
				break;
			default: error();
		}
	}
	
	
	void declList() { // optional
            switch(tok.tag) {
                case Tag.INT: 
                    decl(); 
                    break;
		case Tag.STRING:
                    decl(); 
                    break;	
            }
	}
	
	void condition() {
		
	}
	
	void stmtSufix() {
            switch(tok.tag) {
                case Tag.WHILE:
                    eat(Tag.WHILE);
                    condition();
                    eat(Tag.END);
		default: error();
            }
	}
	
	void ifStmt() {
            switch(tok.tag) {
		case Tag.IF:
                    eat(Tag.IF);
			break;
		default: error();
            }
	}
	
	void whileStmt() {
            switch(tok.tag) {
		case Tag.DO: 
                    eat(Tag.DO);
                    stmtList();
                    stmtSufix();
                    break;
		default: error();
            }
	}
	
	
	void stmtList() {
            switch(tok.tag) {
		case Tag.IF: 
                    ifStmt();
                    break;
		case Tag.DO:
                    whileStmt(); 
                    break;
                default: error();
		}
	}
	
	void program() {
            //  program [decl-list] stmt-list end
            switch(tok.tag) {
                case Tag.PRG: 
                    eat(Tag.PRG); 
                    declList(); // optional
                    stmtList(); 
                    eat(Tag.END); 
                    break;
		default: error();		
            }
	}
	
	public Sintatic(Lexer lexer) throws IOException {
            this.lex = lexer;
            this.tok = this.lex.scan(); // le o primeiro token
	}
}
