import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("running");


        String source = """
        for let i = 0; i < 10; i {
            let x = i;
        }
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