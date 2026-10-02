import java.util.List;

import ast.Program;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import java.nio.file.Files;
import java.nio.file.Path;

import interpreter.Interpreter;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java Main <file>");
            return;
        }
        String source = Files.readString(Path.of(args[0]));

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
