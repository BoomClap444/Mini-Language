package ast;

public class LetStatement implements ASTNode {
    private final String varName;
    private final ASTNode value;

    public LetStatement(String varName, ASTNode value) {
        this.varName = varName;
        this.value = value;
    }

    public String getName() {
        return this.varName;
    }

    public ASTNode getValue() {
        return this.value;
    }
}
