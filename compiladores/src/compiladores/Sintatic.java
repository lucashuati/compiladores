package compiladores;

import com.sun.corba.se.impl.orb.ParserTable;
import java.io.IOException;
import java.util.*;

public class Sintatic {
    Token tok;
    Lexer lex;
    Hashtable symbolTable = new Hashtable(); 
	
    void advance() throws IOException {
        try {
            tok = lex.scan();
            if(tok.tag == Tag.COMMENT){
                advance();
            }
        }catch(IOException e){
            
        }
        
    }
    
    void updateSymbolTable(Token token){
        String s = token.toString();
        Hashtable words = lex.getWords();
        Word w = (Word)words.get(s);
        if (w == null){ 
            semanticError("undeclared");
        }else {
            w.type = token.type;
            words.put(s, w);
        }
    }
    void addSymbolTable(Token token){
        String s = token.toString();
        Hashtable words = lex.getWords();
        Word w = (Word)words.get(s);
        if (w != null){ 
            semanticError("oneness");
        }else {
            w = new Word (s, Tag.ID);
            w.type = token.type;
            words.put(s, w);
        }
    }

    Token getToken(Token token){
        String s = token.toString();
        Hashtable words = lex.getWords();
        Word w = (Word)words.get(s);
        if (w == null){ 
            semanticError("undeclared");
            return new Token(-1);
        }else {
            return w;
        }
    }
    
    void sintaticError() {
        System.out.println("Unexpected Token " + tok.toString() + " on line " + Lexer.line);
        System.exit(0);
    }
    
    void semanticError(String type) {
        switch(type){
            case "type":
                System.out.println("Type error on line " + Lexer.line);
                break;
            case "oneness":
                System.out.println(tok.toString() + " already declared on line " + Lexer.line);
                break;
            case "undeclared":
                System.out.println(tok.toString() + " not declared yet on line " + Lexer.line);
                break;
        }
        System.exit(0);
    }
	
    void eat(int tag) throws IOException {
        if(tag == tok.tag) advance();
        else sintaticError();
    }
	
    boolean identifier(String type, boolean declaration) throws IOException{ // true = more | false = not more
        switch(tok.tag){
          case Tag.ID:
              if(declaration){
                tok.type = type;
                addSymbolTable(tok);
              }else {
                tok = getToken(tok);
                updateSymbolTable(tok);
              }
              eat(Tag.ID);
              break;  
          default:
              sintaticError();
      }
      if(tok.tag == Tag.COM){ // verify if has more id
          eat(Tag.COM);
          return true;
      }else {
          return false;
      }
  }

    void identList(String type) throws IOException {
        while(identifier(type, true));     
    }
	
    boolean decl() throws IOException {
        boolean valid = false;
    
        switch(tok.tag) {
            case Tag.INT: 
                eat(Tag.INT);
                identList("Int");
                eat(Tag.DOTCOM);
                valid = true;
                break;
            case Tag.STRING:
                eat(Tag.STRING);
                identList("String");
                eat(Tag.DOTCOM);
                valid = true;
                break;
        }

        return valid;
    }
	
    void declList() throws IOException { // optional 
        while(decl()){};
    }

  
    Token factor() throws IOException {
        Token aux = tok;
        switch(tok.tag) {
            case Tag.NUM:
                tok.type = "Int";
                eat(Tag.NUM);
                break;  
            case Tag.ID:
                tok.type = getToken(tok).type;
                eat(Tag.ID);
                break;
            case Tag.LITERAL:
                tok.type = "String";
                eat(Tag.LITERAL);
                break;
            case Tag.OPENPARENTESES:
                eat(Tag.OPENPARENTESES);
                aux = expression();
                eat(Tag.CLOSEPARENTESES);
                break;
            default:
                sintaticError();
        }
        return aux;
  }

    Token factorA() throws IOException {
        switch(tok.tag) {
            case Tag.NUM:
            case Tag.ID:
            case Tag.LITERAL:
            case Tag.OPENPARENTESES:
                return factor();
            case Tag.NOT: 
                eat(Tag.NOT);
                return factor();
            case Tag.SUB:
                eat(Tag.SUB);
                return factor();
            default:
                sintaticError();
        }
        return null;
    }

    void condition() throws IOException {		
        expression();
    }
  
    Token expression() throws IOException {
      Token first = null;
      Token seccond = null;
      first = simpleExpr();
      switch(tok.tag) {
        case Tag.GE:
        case Tag.GT:
        case Tag.LE:
        case Tag.LT:
        case Tag.EE:
        case Tag.NE:
            relOp();
            seccond = simpleExpr();
      }
      if(seccond != null){
            if(!first.type.equals(seccond.type)){
                semanticError("type");
            }
        }
        Token aux = new Token(400);
        aux.type = "Bool";
        return aux;
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
            default: sintaticError();
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
                sintaticError();
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
                sintaticError();
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
                sintaticError();
        }
    }
    Token term() throws IOException { // factor-a [ mulop factor-a ]
        Token first = null;
        Token seccond = null;
        first = factorA();
        switch(tok.tag){
            case Tag.MUL:
            case Tag.DIV:
            case Tag.AND:
                mulOp();
                seccond = simpleExpr();
        }
        if(seccond != null){
            if(first.type != null &&  seccond.type != null){ // for ( and ) type verifications
                if(!first.type.equals(seccond.type)){
                    semanticError("type");
                }else {
                    if(first.type == "String"){
                        semanticError("type");
                    }
                }
            }
        }
        if(first != null && first.type != null){
            return first; 
        }else if(seccond != null && seccond.type != null){
            return seccond;
        }else {
            semanticError("type");
        }
        return null;
    }
    Token simpleExpr() throws IOException { // term [ addop term ]
        Token first = null;
        Token seccond = null;
        first = term();
        boolean add = false;
        switch(tok.tag) {
            case Tag.ADD:
                add = true;
            case Tag.SUB:
            case Tag.OR:
                addOp();
                seccond = simpleExpr();
               
        }
    
        if(seccond != null){
           
            if(first.type != null &&  seccond.type != null){ // for ( and ) type verifications
                if(!first.type.equals(seccond.type)){
                    semanticError("type");
                }else {
                    if(first.type == "String" && !add){
                        semanticError("type");
                    }
                }
            }
        }
        
        if(first != null && first.type != null){
            return first; 
        }else if(seccond != null && seccond.type != null){
            return seccond;
        }else {
            semanticError("type");
        }
        return null;
    }

    void assignStmt() throws IOException {
        switch(tok.tag){
            case Tag.ID:
                Token dest = tok;
                identifier(tok.type, false);
                eat(Tag.EQ);
                Token expr = simpleExpr();
                if(!dest.type.equals(expr.type)){
                    semanticError("type");
                }
                break;
            default:
                sintaticError();
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
                identifier(tok.type, false);
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
                sintaticError();
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
            default: sintaticError();		
        }
        
    }
	
    public Sintatic(Lexer lexer) throws IOException {
        this.lex = lexer;
        this.tok = this.lex.scan(); // le o primeiro token
    }
}
