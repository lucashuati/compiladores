
package compiladores;

public class Word extends Token{
    private String lexeme = "";
    // Operadores Logicos
    public static final Word and = new Word ("&&", Tag.AND);
    public static final Word or = new Word ("||", Tag.OR);
    public static final Word eq = new Word ("=", Tag.EQ);
    public static final Word ee = new Word ("==", Tag.EE);
    public static final Word ne = new Word ("!=", Tag.NE);
    public static final Word le = new Word ("<=", Tag.LE);
    public static final Word ge = new Word (">=", Tag.GE);
    public static final Word lt = new Word ("<", Tag.LT);
    public static final Word gt = new Word (">", Tag.GT);
    public static final Word not = new Word ("!", Tag.NOT);
    
    
    //Operadores Aritimeticos
    public static final Word mul = new Word ("*", Tag.MUL);
    public static final Word sub = new Word ("-", Tag.SUB);
    public static final Word add = new Word ("+", Tag.ADD);
    public static final Word div = new Word ("/", Tag.DIV);
    
    //Caracters
    public static final Word openParenteses = new Word ("(", Tag.OPENPARENTESES);
    public static final Word closeParenteses = new Word (")", Tag.CLOSEPARENTESES);
    public static final Word com = new Word (",", Tag.COM);
    public static final Word dotCom = new Word (";", Tag.DOTCOM);
    

    public Word (String s, int tag){
        super (tag);
        lexeme = s;
    }
    
public String toString(){
        return "" + lexeme;
    }

    public String getLexeme() {
        return this.lexeme;
    }
}
