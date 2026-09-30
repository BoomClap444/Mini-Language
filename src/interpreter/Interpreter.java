package interpreter;

import ast.ASTNode;
import ast.LetStatement;
import ast.NumberLiteral;
import ast.IdentifierExpression;
import ast.BooleanLiteral;
import ast.StringLiteral;
import ast.BinaryExpression;

import ast.Program;

public class Interpreter {
    private final Environment environment;

    public Interpreter() {
        this.environment = new Environment();
    }

    public void execute(Program program) {
        for (ASTNode statement : program.getStatements()) {
            executeStatement(statement);
        }
    }

    private void executeStatement(ASTNode statement) {
        if (statement instanceof LetStatement letStatement) {
            Object value = evaluate(letStatement.getValue());
            environment.set(letStatement.getName(), value);
            return;
        }

        throw new IllegalArgumentException("ERROR: UNKNOWN STATEMENT");
    }

    private Object evaluate(ASTNode expression) {
        if (expression instanceof NumberLiteral number) {
            return number.getValue();
        }

        if (expression instanceof IdentifierExpression identifier) {
            return environment.get(identifier.getName());
        }

        if (expression instanceof BooleanLiteral booleanLiteral) {
            return booleanLiteral.getValue();
        }

        if (expression instanceof StringLiteral stringLiteral) {
            return stringLiteral.getValue();
        }

        if (expression instanceof BinaryExpression binary) {
            Object left = evaluate(binary.getLeft());
            Object right = evaluate(binary.getRight());

            switch (binary.getOperator()) {
                case PLUS:
                    return (Integer) left + (Integer) right;

                case MINUS:
                    return (Integer) left - (Integer) right;

                case MULTIPLY:
                    return (Integer) left * (Integer) right;

                case DIVIDE:
                    return (Integer) left / (Integer) right;

                default:
                    throw new IllegalArgumentException("ERROR: UNKNOWN BINARY OPERATOR");
            }
        }

        

        return null;
    }
}
