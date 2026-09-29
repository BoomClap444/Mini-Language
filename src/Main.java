import java.util.List;

import ast.Program;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

public class Main {
    public static void main(String[] args) {
        System.out.println("running");


        String source = """
        let i = 0;
        i = i + 1;
        """;
        
        // PRINT TOKENS AFTER LEXING
        Lexer lexer = new Lexer(source);
        for (Token token : lexer.getTokens()) {
            token.printToken();
        }

        // PARSE PROGRAM
        Parser parser = new Parser(lexer.getTokens());
        Program program = parser.parseProgram();
        
    }
}