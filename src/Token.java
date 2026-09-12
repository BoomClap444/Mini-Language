public class Token {
    TokenType type;
    String text;

    public Token(TokenType type, String text) {
        this.text = text;
        this.type = type;
    }

    public TokenType getType() {
        return type;
    }

    public void printToken() {
        System.out.println(this.type);
        System.out.println(this.text);
    }
}