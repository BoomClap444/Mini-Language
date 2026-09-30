package ast;

public class ReturnStatement implements ASTNode {
    private final ASTNode value;

    public ReturnStatement(ASTNode value) {
        this.value = value;
    }

    public ASTNode getValue() {
        return value;
    }
}