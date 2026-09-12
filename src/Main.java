public class Main {
    public static void main(String[] args) {
        System.out.println("running");
        Lexer test = new Lexer("let x = 42 + 5");
        for (Token token : test.getTokens()) {
            token.printToken();
        }
    }
}