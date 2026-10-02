package parser;
import java.util.ArrayList;
import java.util.List;

import ast.ASTNode;
import ast.ArrayLiteral;
import ast.AssignmentStatement;
import ast.BinaryExpression;
import ast.BlockStatement;
import ast.BooleanLiteral;
import ast.ForStatement;
import ast.FunctionCall;
import ast.FunctionDeclaration;
import ast.IdentifierExpression;
import ast.IfStatement;
import ast.IndexAssignmentStatement;
import ast.IndexExpression;
import ast.LetStatement;
import ast.NumberLiteral;
import ast.Program;
import ast.ReturnStatement;
import ast.StringLiteral;
import ast.UnaryExpression;
import ast.WhileStatement;
import lexer.Token;
import lexer.TokenType;

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

    private FunctionDeclaration parseFunctionDeclaration() {
        advance(); // FUNC

        String name = currentToken().getText();
        expect(TokenType.IDENTIFIER);

        expect(TokenType.LEFT_PAREN);

        List<String> parameters = new ArrayList<>();

        if (currentToken().getType() != TokenType.RIGHT_PAREN) {
            while (true) {
                parameters.add(currentToken().getText());
                expect(TokenType.IDENTIFIER);

                if (currentToken().getType() != TokenType.COMMA) {
                    break;
                }

                advance(); // COMMA
            }
        }

        expect(TokenType.RIGHT_PAREN);

        BlockStatement body = parseBlockStatement();

        return new FunctionDeclaration(name, parameters, body);
    }

    private ReturnStatement parseReturnStatement() {
        advance(); // RETURN

        ASTNode value = parseExpression();

        expect(TokenType.SEMICOLON);

        return new ReturnStatement(value);
    }

    private FunctionCall parseFunctionCall() {
        String name = currentToken().getText();
        expect(TokenType.IDENTIFIER);

        expect(TokenType.LEFT_PAREN);

        List<ASTNode> arguments = new ArrayList<>();

        if (currentToken().getType() != TokenType.RIGHT_PAREN) {
            arguments.add(parseExpression());

            while (currentToken().getType() == TokenType.COMMA) {
                advance();
                arguments.add(parseExpression());
            }
        }

        expect(TokenType.RIGHT_PAREN);

        return new FunctionCall(name, arguments);
    }
    
    private ASTNode parseStatement() {
        if (currentToken().getType() == TokenType.LET) {
            return parseLetStatement();
        }

        if (currentToken().getType() == TokenType.IF) {
            return parseIfStatement();
        }

        if (currentToken().getType() == TokenType.WHILE) {
            return parseWhileStatement();
        }

        if (currentToken().getType() == TokenType.FOR) {
            return parseForStatement();
        }

        if (currentToken().getType() == TokenType.FUNC) {
            return parseFunctionDeclaration();
        }

        if (currentToken().getType() == TokenType.RETURN) {
            return parseReturnStatement();
        }

        if (currentToken().getType() == TokenType.IDENTIFIER) {
            if (tokens.get(pos + 1).getType() == TokenType.LEFT_BRACKET) {
                return parseIndexAssignmentStatement();
            }

            if (tokens.get(pos + 1).getType() == TokenType.EQUALS) {
                return parseAssignmentStatement();
            }
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

    private WhileStatement parseWhileStatement() {
        advance(); // WHILE

        ASTNode condition = parseExpression();

        BlockStatement body = parseBlockStatement();

        return new WhileStatement(condition, body);
    }

    private ForStatement parseForStatement() {
        advance(); // FOR

        ASTNode initialization = parseLetStatement();
        ASTNode condition = parseExpression();
        expect(TokenType.SEMICOLON);

        ASTNode update = parseAssignment();

        BlockStatement body = parseBlockStatement();

        return new ForStatement(initialization, condition, update, body);
    }

    private AssignmentStatement parseAssignmentStatement() {
        AssignmentStatement assignment = parseAssignment();
        expect(TokenType.SEMICOLON);
        return assignment;
    }

    private AssignmentStatement parseAssignment() {
        String variableName = currentToken().getText();
        advance(); // IDENTIFIER

        expect(TokenType.EQUALS);

        ASTNode value = parseExpression();

        return new AssignmentStatement(variableName, value);
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
            ASTNode expression;

            if (tokens.get(pos + 1).getType() == TokenType.LEFT_PAREN) {
                expression = parseFunctionCall();
            }
            
            else {
                expression = new IdentifierExpression(currentToken().getText());
                advance();
            }

            while (currentToken().getType() == TokenType.LEFT_BRACKET) {
                expression = parseIndexExpression(expression);
            }

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

        if (currentToken().getType() == TokenType.LEFT_BRACKET) {
            return parseArrayLiteral();
        }
        
        throw new IllegalArgumentException("ERROR: UNEXPECTED TYPE");
    }

    private IndexExpression parseIndexExpression(ASTNode array) {
        advance(); // [

        ASTNode index = parseExpression();

        expect(TokenType.RIGHT_BRACKET);

        return new IndexExpression(array, index);
    }

    private ArrayLiteral parseArrayLiteral() {
        advance(); // [

        List<ASTNode> elements = new ArrayList<>();

        if (currentToken().getType() != TokenType.RIGHT_BRACKET) {
            elements.add(parseExpression());

            while (currentToken().getType() == TokenType.COMMA) {
                advance(); // ,
                elements.add(parseExpression());
            }
        }

        expect(TokenType.RIGHT_BRACKET);

        return new ArrayLiteral(elements);
    }

    private IndexAssignmentStatement parseIndexAssignmentStatement() {
        ASTNode array = new IdentifierExpression(currentToken().getText());
        advance();

        expect(TokenType.LEFT_BRACKET);

        ASTNode index = parseExpression();

        expect(TokenType.RIGHT_BRACKET);
        expect(TokenType.EQUALS);

        ASTNode value = parseExpression();

        expect(TokenType.SEMICOLON);

        return new IndexAssignmentStatement(array, index, value);
    }

    private void expect(TokenType expected) {
    if (currentToken().getType() != expected) {
        throw new IllegalArgumentException(
            "ERROR: EXPECTED " + expected +
            " BUT FOUND " + currentToken().getType()
            );
        }

        advance();
    }
}
