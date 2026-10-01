import java.util.List;

import ast.Program;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

import interpreter.Interpreter;

public class Main {
    public static void main(String[] args) {
        System.out.println("running");


        String source = """
        func add(a, b) {
            return a + b;
        }

        let result = add(5, 10);
        """;
        
        // PRINT TOKENS AFTER LEXING
        Lexer lexer = new Lexer(source);
        for (Token token : lexer.getTokens()) {
            token.printToken();
        }

        // PARSE PROGRAM
        Parser parser = new Parser(lexer.getTokens());
        Program program = parser.parseProgram();
        

        // INTERPRET
        Interpreter interpreter = new Interpreter();
        interpreter.execute(program);
    }
}