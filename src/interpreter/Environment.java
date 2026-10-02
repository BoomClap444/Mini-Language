package interpreter;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> variables = new HashMap<>();
    private final Environment parent;

    public Environment() {
        this.parent = null;
    }

    public Environment(Environment parent) {
        this.parent = parent;
    }

    public void set(String name, Object value) {
        variables.put(name, value);
    }

    public void assign(String name, Object value) {
        if (variables.containsKey(name)) {
            variables.put(name, value);
            return;
        }

        if (parent != null) {
            parent.assign(name, value);
            return;
        }

        throw new IllegalArgumentException("ERROR: UNDEFINED VARIABLE " + name);
    }
    
    public Object get(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        }

        if (parent != null) {
            return parent.get(name);
        }

        throw new IllegalArgumentException("ERROR: UNDEFINED VARIABLE " + name);
    }
}
