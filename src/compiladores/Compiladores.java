package compiladores;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class Compiladores {

    /**
     *
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Lexer lexer;
        lexer = new Lexer("/home/lucas/Projects/Compiladores/ex1.comp");
        Tag.set_map();
        
        for(;;){
            try {
                Token token = lexer.scan(); 
                if(Lexer.EOF.equals(token.toString())){
                    break;
                } else {
                    if(token.getClass() == Word.class){
                        System.out.println("Token: " + Tag.get_value(token.tag) + " Lexema: " + token.toString());
                    }else if(token.getClass() == Num.class){
                        System.out.println("Token: " + Tag.get_value(token.tag) + " Valor: " + token.toString());
                    }else {
                        System.out.println("Token: " + Tag.get_value(token.tag));
                    }
                }
            } catch(IOException e) {
               System.out.println("Erro nao esperado");
               break;
            }
        }
    }
    
}
