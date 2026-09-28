public class IfStatement implements ASTNode {
    private final ASTNode condition;
    private final BlockStatement thenBranch;
    private final BlockStatement elseBranch;

    public IfStatement(ASTNode condition, BlockStatement thenBranch, BlockStatement elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public BlockStatement getThenBranch() {
        return thenBranch;
    }

    public BlockStatement getElseBranch() {
        return elseBranch;
    }
}