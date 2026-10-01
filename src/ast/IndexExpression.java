package ast;

public class IndexExpression implements ASTNode {
    private final ASTNode array;
    private final ASTNode index;

    public IndexExpression(ASTNode array, ASTNode index) {
        this.array = array;
        this.index = index;
    }

    public ASTNode getArray() {
        return this.array;
    }

    public ASTNode getIndex() {
        return this.index;
    }
}