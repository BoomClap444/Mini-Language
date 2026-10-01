package ast;

import java.util.List;

public class ArrayLiteral implements ASTNode {
    private final List<ASTNode> elements;

    public ArrayLiteral(List<ASTNode> elements) {
        this.elements = elements;
    }

    public List<ASTNode> getElements() {
        return this.elements;
    }
}