public class Main {
    public static void main(String[] args) {
        Lexer test = new Lexer(" 1 f  f  ff1234  test    ");
        for (Token token : test.getTokens()) {
            token.printToken();
        }
    }
}