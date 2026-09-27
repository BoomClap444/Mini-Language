public class StringLiteral implements ASTNode {
    private final String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
