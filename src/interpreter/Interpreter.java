package interpreter;

import java.util.ArrayList;
import java.util.List;

import ast.ASTNode;
import ast.LetStatement;
import ast.NumberLiteral;
import ast.IdentifierExpression;
import ast.BooleanLiteral;
import ast.StringLiteral;
import ast.BinaryExpression;
import ast.UnaryExpression;
import ast.AssignmentStatement;
import ast.BlockStatement;
import ast.IfStatement;
import ast.WhileStatement;
import ast.ForStatement;
import ast.FunctionDeclaration;
import ast.ReturnStatement;
import ast.FunctionCall;

import ast.Program;

public class Interpreter {
    private Environment environment;

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

        if (statement instanceof AssignmentStatement assignment) {
            Object value = evaluate(assignment.getValue());
            environment.set(assignment.getVariableName(), value);
            return;
        }

        if (statement instanceof IfStatement ifStatement) {
            Object condition = evaluate(ifStatement.getCondition());

            if ((Boolean) condition) {
                executeBlock(ifStatement.getThenBranch());
            } else if (ifStatement.getElseBranch() != null) {
                executeBlock(ifStatement.getElseBranch());
            }

            return;
        }

        if (statement instanceof WhileStatement whileStatement) {
            while ((Boolean) evaluate(whileStatement.getCondition())) {
                executeBlock(whileStatement.getBody());
            }

            return;
        }

        if (statement instanceof ForStatement forStatement) {
            executeStatement(forStatement.getInitialization());

            while ((Boolean) evaluate(forStatement.getCondition())) {
                executeBlock(forStatement.getBody());
                executeStatement(forStatement.getUpdate());
            }

            return;
        }

        if (statement instanceof FunctionDeclaration function) {
            environment.set(function.getName(), function);
            return;
        }

        if (statement instanceof ReturnStatement returnStatement) {
            Object value = evaluate(returnStatement.getValue());
            throw new ReturnSignal(value);
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
                case LESS_THAN:
                    return (Integer) left < (Integer) right;

                case GREATER_THAN:
                    return (Integer) left > (Integer) right;

                case LESS_EQUALS:
                    return (Integer) left <= (Integer) right;

                case GREATER_EQUALS:
                    return (Integer) left >= (Integer) right;

                case EQUALS_EQUALS:
                    return left.equals(right);

                case NOT_EQUALS:
                    return !left.equals(right);
                
                case AND:
                    return (Boolean) left && (Boolean) right;

                case OR:
                    return (Boolean) left || (Boolean) right;
                    
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

        if (expression instanceof UnaryExpression unary) {
            Object operand = evaluate(unary.getOperand());

            switch (unary.getOperator()) {
                case NOT:
                    return !(Boolean) operand;

                default:
                    throw new IllegalArgumentException("ERROR: UNKNOWN UNARY OPERATOR");
            }
        }

        if (expression instanceof FunctionCall functionCall) {
            FunctionDeclaration function = (FunctionDeclaration) environment.get(functionCall.getName());

            if (function == null) {
                throw new IllegalArgumentException("ERROR: UNDEFINED FUNCTION " + functionCall.getName());
            }

            List<ASTNode> arguments = functionCall.getArguments();
            List<String> parameters = function.getParameters();

            if (arguments.size() != parameters.size()) {
                throw new IllegalArgumentException(
                    "ERROR: WRONG NUMBER OF ARGUMENTS"
                );
            }

            List<Object> argumentValues = new ArrayList<>();

            for (ASTNode argument : arguments) {
                argumentValues.add(evaluate(argument));
            }

            Environment previousEnvironment = environment;
            environment = new Environment(previousEnvironment);

            for (int i = 0; i < parameters.size(); i++) {
                environment.set(parameters.get(i), argumentValues.get(i));
            }

            try {
                executeBlock(function.getBody());
            }
            catch (ReturnSignal signal) {
                return signal.getValue();
            }
            finally {
                environment = previousEnvironment;
            }

            return null;
        }
        return null;
    }

    private void executeBlock(BlockStatement block) {
        for (ASTNode statement : block.getStatements()) {
            executeStatement(statement);
        }
    }

}
