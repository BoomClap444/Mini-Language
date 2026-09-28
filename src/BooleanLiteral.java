public class BooleanLiteral implements ASTNode {
    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }
}
