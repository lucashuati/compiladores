package compiladores;

import java.util.HashMap;
import java.util.Map;


public class Tag {
    public static Map<Integer,String> map;
    
    public static void set_map(){
        map = new HashMap<>();
        map.put(256, "PRG");
        map.put(257, "END");
        map.put(258, "STRING");
        map.put(259, "INT");
        map.put(260, "IF");
        map.put(261, "THEN");
        map.put(262, "ELSE");
        map.put(263, "DO");
        map.put(264, "WHILE");
        map.put(265, "SCAN");
        map.put(266, "PRINT");
        map.put(289,"GE");
        map.put(290,"LE");
        map.put(291,"NE");
        map.put(246,"EE");
        map.put(292,"AND");
        map.put(293,"OR");
        map.put(294,"EQ");
        map.put(295,"GT");
        map.put(296,"LT");
        map.put(297,"NOT");
        map.put(312,"FALSE");
        map.put(313,"TRUE");
        map.put(320,"NUM");
        map.put(321,"ID");
        map.put(322,"LITERAL");
        map.put(323,"OPENPARENTESES");
        map.put(324,"CLOSEPARENTESES");
        map.put(325,"DOTCOM");
        map.put(326,"COM");
        map.put(340,"ADD");
        map.put(341,"SUB");
        map.put(342,"DIV");
        map.put(343,"MUL");
        map.put(400,"ERROR");
    }
    
    public static String get_value(int key){
        return map.get(key);
    }
    public final static int
        //Palavras Reservadas
        PRG = 256,
        END = 257,
        STRING = 258,
        INT = 259,
        IF = 260,
        THEN = 261,
        ELSE = 262,
        DO = 263,
        WHILE = 264,
        SCAN = 265,
        PRINT = 266,    
        
        // Operadores    
        GE = 289,
        LE = 290,
        NE = 291,
        EE = 246,
        AND = 292,
        OR = 293,
        EQ = 294,
        GT = 295,
        LT = 296,
        NOT = 297,
        
        // Booleanos
        FALSE = 312,
        TRUE = 313,
     
        //Tipos
        NUM = 320,
        ID = 321,
        LITERAL = 322,
        
        //Simbolos
        OPENPARENTESES = 323,
        CLOSEPARENTESES = 324,
        DOTCOM = 325,
        COM = 326,
        
        // Operadores Aritimeticos
        ADD = 340,
        SUB = 341,
        DIV = 342,
        MUL = 343,
        
        COMMENT = 399,      
        ERRO = 400;
    
    
    
}

