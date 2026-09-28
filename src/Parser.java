import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    public Program parseProgram() {
        List<ASTNode> statements = new ArrayList<>();
        while (currentToken().getType() != TokenType.EOF) {
            statements.add(parseStatement());
        }

        return new Program(statements);
    }

    private ASTNode parseStatement() {
        if (currentToken().getType() == TokenType.LET) {
            return parseLetStatement();
        }

        throw new IllegalArgumentException("ERROR: UNEXPECTED STATEMENT");
    }

    private Token currentToken() {
        return tokens.get(this.pos);
    }

    private void advance() {
        this.pos++;
    }

    private LetStatement parseLetStatement() {
        advance(); // LET
        
        String variableName = currentToken().getText();
        expect(TokenType.IDENTIFIER);

        expect(TokenType.EQUALS);

        ASTNode value = parseExpression();

        expect(TokenType.SEMICOLON);

        return new LetStatement(variableName, value);
    }

    private ASTNode parseExpression() {
        return parseAddition();
    }

    private ASTNode parseAddition() {
        ASTNode left = parseMultiplication();

        while (currentToken().getType() == TokenType.PLUS ||
            currentToken().getType() == TokenType.MINUS) {

            TokenType operator = currentToken().getType();
            advance();

            ASTNode right = parseMultiplication();

            left = new BinaryExpression(left, operator, right);
        }

        return left;
    }

    private ASTNode parseMultiplication() {
        ASTNode left = parsePrimary();

        while (currentToken().getType() == TokenType.MULTIPLY ||
            currentToken().getType() == TokenType.DIVIDE) {

            TokenType operator = currentToken().getType();
            advance();

            ASTNode right = parsePrimary();

            left = new BinaryExpression(left, operator, right);
        }

        return left;
    }

    private ASTNode parsePrimary() {
        if (currentToken().getType() == TokenType.LEFT_PAREN) {
            advance();
            ASTNode expression = parseExpression();
            expect(TokenType.RIGHT_PAREN);

            return expression;
        }
        if (currentToken().getType() == TokenType.IDENTIFIER) {
            ASTNode expression = new IdentifierExpression(currentToken().getText());
            advance();
            return expression;
        }

        if (currentToken().getType() == TokenType.STRING) {
            ASTNode expression = new StringLiteral(currentToken().getText());
            advance();
            return expression;
        }
        
        if (currentToken().getType() == TokenType.NUMBER) {
            ASTNode expression = new NumberLiteral(Integer.parseInt(currentToken().getText()));
            advance();
            return expression;
        }

        throw new IllegalArgumentException("ERROR: UNEXPECTED TYPE");
    }

    private void expect(TokenType type) {
        if (currentToken().getType() != type) {
            throw new IllegalArgumentException(
                "Expected " + type + ", got " + currentToken().getType()
            );
        }

        advance();
    }
}
