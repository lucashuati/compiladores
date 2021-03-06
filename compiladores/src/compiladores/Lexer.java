package compiladores;
import java.io.*;
import java.util.*;

public class Lexer {
    public static final String EOF = "65535";
    public static boolean last_token = false;
    public static int line = 1; //contador de linhas
    private char ch = ' '; //caractere lido do arquivo
    private FileReader file;
    private Hashtable words = new Hashtable();
    
    public Hashtable getWords(){
        return this.words;
    }
    
    public void setWords(Hashtable words){
        this.words = words;
    }
    /* Método para inserir palavras reservadas na HashTable */
    private void reserve(Word w){
        words.put(w.getLexeme(), w); // lexema é a chave para entrada na
    
    //HashTable
    }
    public Lexer(String fileName) throws FileNotFoundException{
        try{
            file = new FileReader (fileName);
        }
        catch(FileNotFoundException e){
            System.out.println("Arquivo não encontrado");
        throw e;
        }
        //Insere palavras reservadas na HashTable
        reserve(new Word ("if", Tag.IF));
        reserve(new Word ("then", Tag.THEN));
        reserve(new Word ("else", Tag.ELSE));
        reserve(new Word ("program", Tag.PRG));
        reserve(new Word ("end", Tag.END));
        reserve(new Word ("int", Tag.INT));
        reserve(new Word ("string", Tag.STRING));
        reserve(new Word ("true", Tag.TRUE));
        reserve(new Word ("false", Tag.FALSE));
        reserve(new Word ("print", Tag.PRINT));
        reserve(new Word ("scan", Tag.SCAN));
        reserve(new Word ("while", Tag.WHILE));
        reserve(new Word ("do", Tag.DO));
    }
    
    /*Lê o próximo caractere do arquivo*/
    private void readch() throws IOException{
        int valid_char = file.read();
        if (valid_char == -1){
            if(last_token){
                throw new IOException();
            }
            last_token = true;
        }  
        ch = (char) valid_char;
    }
    
    /* Lê o próximo caractere do arquivo e verifica se é igual a c*/
    private boolean readch(char c) throws IOException{
        readch();
        if (ch != c) return false;
        ch = ' ';
        return true;
    }
    
    public Token scan() throws IOException{
        //Desconsidera delimitadores na entrada
        for (;; readch()) {
            if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\b')
                continue;
            else if (ch == '\n')
                line++; // line = line + 1
            else
                break;
        }
        switch(ch){
            //Operadores
            case '&':
                if (readch('&'))
                    return Word.and;
            case '|':
                if (readch('|')) 
                    return Word.or;
            case '=':
                if (readch('='))
                    return Word.ee;
                return Word.eq;
            case '<':
                if (readch('=')) 
                    return Word.le;
                return Word.lt;
            case '>':
                if (readch('=')) 
                    return Word.ge;
                return Word.gt;
            case '!':
                if (readch('='))
                    return Word.ne;
                return Word.not;
            case '*':
                readch();
                return Word.mul;
            case '/':
                readch();
                if(ch == '/'){
                    for(;;){
                        readch();
                        if(ch == '\n'){
                            line++;
                            ch = ' ';
                            break;
                        }
                    }
                }else if(ch == '*'){
                    for(;;){
                     readch();
                     if(ch == '*'){
                         readch();
                         if(ch == '\n'){
                             line++;
                         }
                         if(ch == '/'){
                             ch = ' ';
                             break;
                         }
                     }
                    }
                    
                }else {
                    return Word.div;
                }
                return new Token(Tag.COMMENT);
            case '+':
                readch();
                return Word.add;
            case '-':
                readch();
                return Word.sub;
            case '(':
                readch();
                return Word.openParenteses;
            case ')':
                readch();
                return Word.closeParenteses;
            case ',':
                readch();
                return Word.com;
            case ';':
                readch();
                return Word.dotCom;
        }
    
        //Números
        if (Character.isDigit(ch)){
            int value=0;
            do{
                value = 10*value + Character.digit(ch,10);
                readch();
            }while(Character.isDigit(ch));
            return new Num(value);
        }
    
        // Identificadores
        if (Character.isLetter(ch)){
            StringBuffer sb = new StringBuffer();
            do{
                sb.append(ch);
                readch();
            }while(Character.isLetterOrDigit(ch));
            String s = sb.toString();
            Word w = (Word)words.get(s);
            if (w != null) 
                return w; //palavra já existe na HashTable
            w = new Word (s, Tag.ID);
            return w;
        }
        
        // Literal
        if(ch == '"'){
            StringBuffer sb = new StringBuffer();
            do{
                sb.append(ch);
                readch();
            }while(ch != '"');
            sb.append(ch);
            ch = ' ';
            String s = sb.toString();
            Word w;
            w = new Word (s, Tag.LITERAL);
            words.put(s, w);
            return w;
        }

        //Caracteres não especificados
        Token t = new Token(ch);
        ch = ' ';
        if(!EOF.equals(t.toString())){
            System.out.println("Token nao esperado: linha: " + line);
            return new Token(Tag.ERRO);
        }
        return t;
    }
}
    


    
