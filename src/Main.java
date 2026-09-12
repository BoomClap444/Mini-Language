public class Main {
    public static void main(String[] args) {
        System.out.println("running");
        Lexer test = new Lexer("123 + abc / 42 let lett if else ifelse return function (){} @@@@");
        for (Token token : test.getTokens()) {
            token.printToken();
        }
    }
}