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

        if (currentToken().getType() == TokenType.IF) {
            return parseIfStatement();
        }

        throw new IllegalArgumentException("ERROR: UNEXPECTED STATEMENT");
    }

    private BlockStatement parseBlockStatement() {
        advance(); // {

        List<ASTNode> statements = new ArrayList<>();

        while (currentToken().getType() != TokenType.RIGHT_BRACE) {
            if (currentToken().getType() == TokenType.EOF) {
                throw new IllegalArgumentException("ERROR: EXPECTED '}'");
            }
            statements.add(parseStatement());
        }

        advance(); // }

        return new BlockStatement(statements);
    }

    private IfStatement parseIfStatement() {
        advance(); // IF

        ASTNode condition = parseExpression();

        BlockStatement thenBranch = parseBlockStatement();

        if (currentToken().getType() == TokenType.ELSE) {
            advance(); // ELSE
            BlockStatement elseBranch = parseBlockStatement();

            return new IfStatement(condition, thenBranch, elseBranch);
        }

        return new IfStatement(condition, thenBranch, null);
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
        return parseLogicalOr();
    }

    private ASTNode parseLogicalOr() {
        ASTNode left = parseLogicalAnd();

        while (currentToken().getType() == TokenType.OR) {
            TokenType operator = currentToken().getType();
            advance();

            ASTNode right = parseLogicalAnd();

            left = new BinaryExpression(left, operator, right);
        }

        return left;
    }

    private ASTNode parseLogicalAnd() {
        ASTNode left = parseComparison();

        while (currentToken().getType() == TokenType.AND) {
            TokenType operator = currentToken().getType();
            advance();

            ASTNode right = parseComparison();

            left = new BinaryExpression(left, operator, right);
        }

        return left;
    }

    private ASTNode parseComparison() {
        ASTNode left = parseAddition();

        while (currentToken().getType() == TokenType.LESS_THAN ||
            currentToken().getType() == TokenType.GREATER_THAN ||
            currentToken().getType() == TokenType.LESS_EQUALS ||
            currentToken().getType() == TokenType.GREATER_EQUALS ||
            currentToken().getType() == TokenType.EQUALS_EQUALS ||
            currentToken().getType() == TokenType.NOT_EQUALS) {

            TokenType operator = currentToken().getType();
            advance();

            ASTNode right = parseAddition();

            left = new BinaryExpression(left, operator, right);
        }

        return left;
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

        if (currentToken().getType() == TokenType.TRUE) {
            ASTNode expression = new BooleanLiteral(true);
            advance();
            return expression;
        }

        if (currentToken().getType() == TokenType.FALSE) {
            ASTNode expression = new BooleanLiteral(false);
            advance();
            return expression;
        }

        if (currentToken().getType() == TokenType.NOT) {
            TokenType operator = currentToken().getType();
            advance();

            ASTNode operand = parsePrimary();

            return new UnaryExpression(operator, operand);
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
