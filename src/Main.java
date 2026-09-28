import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("running");


        String source = """
        let x = 10;

        if x > 5 {
            let y = 20;
        } else {
            let y = 0;
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