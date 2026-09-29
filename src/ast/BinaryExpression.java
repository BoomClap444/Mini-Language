package ast;
import lexer.TokenType;

public class BinaryExpression implements ASTNode {
    private final ASTNode left;
    private final TokenType op;
    private final ASTNode right;

    public BinaryExpression(ASTNode left, TokenType op, ASTNode right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    public ASTNode getLeft() {
        return left;
    }

    public TokenType getOperator() {
        return op;
    }

    public ASTNode getRight() {
        return right;
    }
    
}
