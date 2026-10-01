import java.util.List;

import ast.Program;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

import interpreter.Interpreter;

public class Main {
    public static void main(String[] args) {
        String source = """
        let numbers = [10, 20, 30];

        let x = numbers[1];

        numbers[1] = 50;
        """;
        
        // LEXER
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.getTokens();

        // PARSE PROGRAM
        Parser parser = new Parser(tokens);
        Program program = parser.parseProgram();
        
        // INTERPRET
        Interpreter interpreter = new Interpreter();
        interpreter.execute(program);
    }
}