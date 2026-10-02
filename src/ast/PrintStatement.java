package ast;

public class PrintStatement implements ASTNode {
    private final ASTNode value;

    public PrintStatement(ASTNode value) {
        this.value = value;
    }

    public ASTNode getValue() {
        return this.value;
    }
}