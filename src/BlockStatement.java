import java.util.List;

public class BlockStatement implements ASTNode {
    private final List<ASTNode> statements;

    public BlockStatement(List<ASTNode> statements) {
        this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }
}