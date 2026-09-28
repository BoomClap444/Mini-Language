public class WhileStatement implements ASTNode {
    private final ASTNode condition;
    private final BlockStatement body;

    public WhileStatement(ASTNode condition, BlockStatement body) {
        this.condition = condition;
        this.body = body;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public BlockStatement getBody() {
        return body;
    }
}