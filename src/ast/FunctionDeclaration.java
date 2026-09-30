package ast;

import java.util.List;

public class FunctionDeclaration implements ASTNode {
    private final String name;
    private final List<String> parameters;
    private final BlockStatement body;

    public FunctionDeclaration(
            String name,
            List<String> parameters,
            BlockStatement body) {

        this.name = name;
        this.parameters = parameters;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public BlockStatement getBody() {
        return body;
    }
}