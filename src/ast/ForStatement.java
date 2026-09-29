package ast;

public class ForStatement implements ASTNode {
    private final ASTNode initialization;
    private final ASTNode condition;
    private final ASTNode update;
    private final BlockStatement body;

    public ForStatement(
            ASTNode initialization,
            ASTNode condition,
            ASTNode update,
            BlockStatement body) {

        this.initialization = initialization;
        this.condition = condition;
        this.update = update;
        this.body = body;
    }
}