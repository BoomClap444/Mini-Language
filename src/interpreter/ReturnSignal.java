package interpreter;

public class ReturnSignal extends RuntimeException {
    private final Object value;

    public ReturnSignal(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }
}
