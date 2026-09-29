package ast;

public class IdentifierExpression implements ASTNode {
    private final String name;

    public IdentifierExpression(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
