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
        Sintatic sintatic;
        lexer = new Lexer("/home/lucas/Projects/CEFET/compiladores/src/compiladores/testes/ex6.comp");
        sintatic = new Sintatic(lexer);
        sintatic.program();
        System.out.println("Compilation sucessful");
    }
    
}
