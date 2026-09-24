public class NumberLiteral implements ASTNode {
    private final int value;

    public NumberLiteral(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
