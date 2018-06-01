package compiladores;

public class Token {
    public final int tag; //constante que representa o token
    public String type;
    Object val;
    public Token (int t){
        tag = t;
        type = null;
    }
        
    public String toString(){
        return "" + tag;
    }
}