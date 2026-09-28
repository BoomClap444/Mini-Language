public class UnaryExpression implements ASTNode {
    private final TokenType operator;
    private final ASTNode operand;

    public UnaryExpression(TokenType operator, ASTNode operand) {
        this.operator = operator;
        this.operand = operand;
    }

    public TokenType getOperator() {
        return operator;
    }

    public ASTNode getOperand() {
        return operand;
    }
}
