package ast;

public class AssignmentStatement implements ASTNode {
    private final String variableName;
    private final ASTNode value;

    public AssignmentStatement(String variableName, ASTNode value) {
        this.variableName = variableName;
        this.value = value;
    }

    public String getVariableName() {
        return variableName;
    }

    public ASTNode getValue() {
        return value;
    }
}