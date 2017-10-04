package compiladores;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiladores {

    /**
     *
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Lexer lexer;
        lexer = new Lexer("/home/lucas/Projects/Compiladores/ex2.comp");
        Tag.set_map();
        
        for(;;){
            try {
                Token token = lexer.scan(); 
                if (token.getClass() == Word.class) {
                    System.out.println("Token: " + Tag.get_value(token.tag) + " Lexema: " + token.toString());
                } else if (token.getClass() == Num.class) {
                    System.out.println("Token: " + Tag.get_value(token.tag) + " Valor: " + token.toString());
                }
            } catch(IOException e) {
               break;
            }
        }
    }
    
}
