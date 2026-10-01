package ast;

public class IndexAssignmentStatement implements ASTNode {
    private final ASTNode array;
    private final ASTNode index;
    private final ASTNode value;

    public IndexAssignmentStatement(ASTNode array, ASTNode index, ASTNode value) {
        this.array = array;
        this.index = index;
        this.value = value;
    }

    public ASTNode getArray() {
        return this.array;
    }

    public ASTNode getIndex() {
        return this.index;
    }

    public ASTNode getValue() {
        return this.value;
    }
}