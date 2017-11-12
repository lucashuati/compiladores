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
	
	void eat(int tag) throws IOException {
            if(tag == tok.tag) advance();
            else error();
	}
	
        boolean identifier() throws IOException{ // true = more | false = not more
            switch(tok.tag){
                case Tag.ID:
                    eat(Tag.ID);
                    break;
                default:
                    error();
            }
            if(tok.tag == Tag.COM){
                eat(Tag.COM);
                return true;
            }else {
                return false;
            }
        }
	void identList() throws IOException {
            while(identifier());     
	}
	
	boolean decl() throws IOException {
            boolean valid = false;
            
            switch(tok.tag) {
                case Tag.INT: 
                    eat(Tag.INT);
                    identList();
                    eat(Tag.DOTCOM);
                    valid = true;
                    break;
		case Tag.STRING:
                    decl();
                    valid = true;
                    break;
		}
            return valid;
	}
	
	void declList() throws IOException { // optional 
            while(decl()){};
	}
	
//	void condition() {
//		
//	}   
	
//	void stmtSufix() {
//            switch(tok.tag) {
//                case Tag.WHILE:
//                    eat(Tag.WHILE);
//                    condition();
//                    eat(Tag.END);
//		default: error();
//            }
//	}
	
//	void ifStmt() {
//            switch(tok.tag) {
//		case Tag.IF:
//                    eat(Tag.IF);
//			break;
//		default: error();
//            }
//	}
	
	void whileStmt() throws IOException {
            switch(tok.tag) {
		case Tag.DO: 
                    eat(Tag.DO);
                    stmtList();
//                    stmtSufix();
                    break;
		default: error();
            }
	}
	
	boolean stmt() throws IOException {
          boolean valid = false;
          switch(tok.tag){
            case Tag.IF: 
//                ifStmt();
                valid = true;
                break;
            case Tag.DO:
                whileStmt(); 
                valid = true;
                break;
          } 
          return valid;
        }
	void stmtList() throws IOException { //stmt {stmt}
            while(stmt());
	}
	
	void program() throws IOException {
            //  program [decl-list] stmt-list end
            switch(tok.tag) {
                case Tag.PRG: 
                    eat(Tag.PRG); 
                    System.out.println("PRG OK");
                    declList(); // optional
                     System.out.println("Decl OK");
                    //stmtList(); 
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
