import java.util.List;
import java.util.ArrayList;

public class Lexer {
    private final String source;
    private int pos;

    public Lexer(String source) {
        this.source = source;
        this.pos = 0;
    }

    public Token nextToken() {
        while (this.pos < this.source.length() && Character.isWhitespace(this.source.charAt(this.pos))) {
            this.pos++;
        }

        if (this.pos >= this.source.length()) {
            return new Token(TokenType.EOF, "");
        }

        int first = this.pos;
        char current = this.source.charAt(this.pos);
        TokenType type = TokenType.UNDEFINED;

        if (Character.isDigit(current)) {
            type = TokenType.NUMBER;
            while (this.pos < this.source.length() && Character.isDigit(this.source.charAt(this.pos))) {
                this.pos++;
            }
        }
        else if (Character.isLetter(current)) {
            type = TokenType.IDENTIFIER;
            while (this.pos < this.source.length() && Character.isLetterOrDigit(this.source.charAt(this.pos))) {
                this.pos++;
            }
        }

        return new Token(type, this.source.substring(first, this.pos));
    }

    public List<Token> getTokens() {
        List<Token> result = new ArrayList<>();
        Token current = nextToken();
        result.add(current);

        while (current.getType() != TokenType.EOF) {
            current = nextToken();
            result.add(current);
        }

        return result;
    }
}