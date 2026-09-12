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
        TokenType type = TokenType.UNKNOWN;

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
        else if (isOperator(current)) {
            switch (current) {
                case '+':
                    type = TokenType.PLUS;
                    break;
                case '-':
                    type = TokenType.MINUS;
                    break;
                case '*':
                    type = TokenType.MULTIPLY;
                    break;
                case '/':
                    type = TokenType.DIVIDE;
                    break;
                case '=':
                    type = TokenType.EQUALS;
                    break;
                default:
                    break;
            }
            this.pos++;
        }
        else if (isPunc(current)) {
            switch (current) {
                case '(':
                    type = TokenType.LEFT_PAREN;
                    break;
                case ')':
                    type = TokenType.RIGHT_PAREN;
                    break;
                case '}':
                    type = TokenType.RIGHT_BRACE;
                    break;
                case '{':
                    type = TokenType.LEFT_BRACE;
                    break;
                default:
                    break;
            }
            this.pos++;
        }
        else{
            this.pos++;
        }

        String text = this.source.substring(first, this.pos);
        if (isKeyword(text)) {
            switch (text) {
                case "let":
                    type = TokenType.LET;
                    break;
                case "if":
                    type = TokenType.IF;
                    break;
                case "else":
                    type = TokenType.ELSE;
                    break;
                case "function":
                    type = TokenType.FUNC;
                    break;
                case "return":
                    type = TokenType.RETURN;
                    break;
                default:
                    break;
            }
        }
        return new Token(type, text);
    }

    static boolean isKeyword(String s) {
        return s.equals("let") || s.equals("if") ||
        s.equals("else") || s.equals("function") ||
        s.equals("return");
    }

    static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '=';
    }

    static boolean isPunc(char c) {
        return c == '{' || c == '(' || c == ')' || c == '}';
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