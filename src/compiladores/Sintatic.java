package compiladores;

import java.io.IOException;

public class Sintatic {
    Token tok;
    Lexer lex;
	
    void advance() throws IOException {
        try {
            tok = lex.scan();
            if(tok.tag == Tag.COMMENT){
                advance();
            }
        }catch(IOException e){
            
        }
        
    }
        
    void error() {
        System.out.println("Unexpected Token on line " + lex.line);
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
      if(tok.tag == Tag.COM){ // verify if has more id
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
                eat(Tag.STRING);
                identList();
                eat(Tag.DOTCOM);
                valid = true;
                break;
        }

        return valid;
    }
	
    void declList() throws IOException { // optional 
        while(decl()){};
    }

  
    void factor() throws IOException {
        switch(tok.tag) {
            case Tag.NUM:
                eat(Tag.NUM);
                break;
            case Tag.ID:
                eat(Tag.ID);
                break;
            case Tag.LITERAL:
                eat(Tag.LITERAL);
                break;
            case Tag.OPENPARENTESES:
                eat(Tag.OPENPARENTESES);
                expression();
                eat(Tag.CLOSEPARENTESES);
                break;
            default:
                error();
        }
  }

    void factorA() throws IOException {
        switch(tok.tag) {
            case Tag.NUM:
                factor();
                break;
            case Tag.ID:
                factor();
                break;
            case Tag.LITERAL:
                factor();
                break;
            case Tag.OPENPARENTESES:
                factor();
                break;
            case Tag.NOT: 
                eat(Tag.NOT);
                factor();
                break;
            case Tag.SUB:
                eat(Tag.SUB);
                factor();
                break;
            default:
                error();
        }
    }

    void condition() throws IOException {		
        expression();
    }
  
    void expression() throws IOException {
      simpleExpr();
      switch(tok.tag) {
        case Tag.GE:
        case Tag.GT:
        case Tag.LE:
        case Tag.LT:
        case Tag.EE:
        case Tag.NE:
            relOp();
            simpleExpr();
      }
    }
    
    void stmtSuffix() throws IOException {
        switch(tok.tag) {
            case Tag.WHILE:
                eat(Tag.WHILE);
                condition();
                eat(Tag.END);
        }
    }
    void whileStmt() throws IOException {
        switch(tok.tag) {
            case Tag.DO: 
                eat(Tag.DO);
                stmtList();
                stmtSuffix();
                break;
            default: error();
        }
    }
    
    void mulOp() throws IOException  {
        switch(tok.tag) {
            case Tag.MUL:
                eat(Tag.MUL);
                break;
            case Tag.DIV:
                eat(Tag.DIV);
                break;
            case Tag.AND:
                eat(Tag.AND);
                break;
            default: 
                error();
        }
    }
    
    void relOp() throws IOException {
        switch (tok.tag) {
            case Tag.GE:
                eat(Tag.GE);
                break;
            case Tag.GT:
                eat(Tag.GT);
                break;
            case Tag.LE:
                eat(Tag.LE);
                break;
            case Tag.LT:
                eat(Tag.LT);
                break;
            case Tag.EE:
                eat(Tag.EE);
                break;
            case Tag.NE:
                eat(Tag.NE);
                break;
            default:
                error();
        }
    }
    
    void addOp() throws IOException{
        switch(tok.tag){
            case Tag.ADD:
                eat(Tag.ADD);
                break;
            case Tag.SUB:
                eat(Tag.SUB);
                break;
            case Tag.OR:
                eat(Tag.OR);
                break;
            default:
                error();
        }
    }
    void term() throws IOException { // factor-a [ mulop factor-a ]
        factorA();
        switch(tok.tag){
            case Tag.MUL:
            case Tag.DIV:
            case Tag.AND:
                mulOp();
                simpleExpr();
        }
    }
    void simpleExpr() throws IOException { // term [ addop term ]
        term();
        switch(tok.tag) {
            case Tag.ADD:
            case Tag.SUB:
            case Tag.OR:
                addOp();
                simpleExpr();
            
        }
    }

    void assignStmt() throws IOException {
        switch(tok.tag){
            case Tag.ID:
                identifier();
                eat(Tag.EQ);
                simpleExpr();
                break;
            default:
                error();
        }
    }

    void writable() throws IOException {
        switch(tok.tag) {
            case Tag.LITERAL:
                eat(Tag.LITERAL);
                break;
            default:
                simpleExpr();
        }
    }
 
    void readStmt() throws IOException {
        switch(tok.tag) {
            case Tag.SCAN:
                eat(Tag.SCAN);
                eat(Tag.OPENPARENTESES);
                identifier();
                eat(Tag.CLOSEPARENTESES);
                break;
        }
    }    
    void writeStmt() throws IOException {
        switch(tok.tag) {
            case Tag.PRINT:
                eat(Tag.PRINT);
                eat(Tag.OPENPARENTESES);
                writable();
                eat(Tag.CLOSEPARENTESES);
                break;
        }
    }
    
    void ifTail() throws IOException {
        switch(tok.tag){
            case Tag.ELSE:
                eat(Tag.ELSE);
                stmtList();
            case Tag.END:
                eat(Tag.END);
                break;
            default:
                error();
        }
    }
    void ifStmt() throws IOException{
        switch(tok.tag){
            case Tag.IF:
                eat(Tag.IF);
                condition();
                eat(Tag.THEN);
                stmtList();
                ifTail();
                break;
        }
    }
    boolean stmt() throws IOException { // assign-stmt ";" | if-stmt | read-stmt ";" | while-stmt | write-stmt ";"
        boolean valid = false;
        switch(tok.tag){
            case Tag.ID:
                assignStmt();
                eat(Tag.DOTCOM);
                valid = true;
                break;
            case Tag.IF: 
                ifStmt();
                valid = true;
                break;
            case Tag.PRINT:
                writeStmt();
                eat(Tag.DOTCOM);
                valid = true;
                break;
            case Tag.SCAN:
                readStmt();
                eat(Tag.DOTCOM);
                valid = true;
                break;
            case Tag.DO:
                whileStmt(); 
                valid = true;
                break;
            default:
                error();
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
