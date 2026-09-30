package ast;

import java.util.List;

public class FunctionCall implements ASTNode {
    private final String name;
    private final List<ASTNode> arguments;

    public FunctionCall(String name, List<ASTNode> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public List<ASTNode> getArguments() {
        return arguments;
    }
}
