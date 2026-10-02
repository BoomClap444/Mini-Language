package interpreter;

import java.util.ArrayList;
import java.util.List;

import ast.ASTNode;
import ast.ArrayLiteral;
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
import ast.IndexAssignmentStatement;
import ast.IndexExpression;
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
            environment.assign(assignment.getVariableName(), value);
            return;
        }

        if (statement instanceof IfStatement ifStatement) {
            Object condition = evaluate(ifStatement.getCondition());

            if (!(condition instanceof Boolean)) {
                throw new IllegalArgumentException("ERROR: IF CONDITION MUST BE BOOLEAN");
            }

            if ((Boolean) condition) {
                executeBlock(ifStatement.getThenBranch());
            } 
            else if (ifStatement.getElseBranch() != null) {
                executeBlock(ifStatement.getElseBranch());
            }

            return;
        }

        if (statement instanceof WhileStatement whileStatement) {
            Object condition = evaluate(whileStatement.getCondition());

            if (!(condition instanceof Boolean)) {
                throw new IllegalArgumentException("ERROR: WHILE CONDITION MUST BE BOOLEAN");
            }

            while ((Boolean) condition) {
                executeBlock(whileStatement.getBody());

                condition = evaluate(whileStatement.getCondition());

                if (!(condition instanceof Boolean)) {
                    throw new IllegalArgumentException("ERROR: WHILE CONDITION MUST BE BOOLEAN");
                }
            }

            return;
        }

        if (statement instanceof ForStatement forStatement) {
            executeStatement(forStatement.getInitialization());

            Object condition = evaluate(forStatement.getCondition());

            if (!(condition instanceof Boolean)) {
                throw new IllegalArgumentException("ERROR: FOR CONDITION MUST BE BOOLEAN");
            }

            while ((Boolean) condition) {
                executeBlock(forStatement.getBody());
                executeStatement(forStatement.getUpdate());

                condition = evaluate(forStatement.getCondition());

                if (!(condition instanceof Boolean)) {
                    throw new IllegalArgumentException("ERROR: FOR CONDITION MUST BE BOOLEAN");
                }
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

        if (statement instanceof IndexAssignmentStatement indexAssignment) {
            Object arrayValue = evaluate(indexAssignment.getArray());
            Object indexValue = evaluate(indexAssignment.getIndex());
            Object value = evaluate(indexAssignment.getValue());

            if (!(arrayValue instanceof List<?>)) {
                throw new IllegalArgumentException("ERROR: VALUE IS NOT AN ARRAY");
            }

            if (!(indexValue instanceof Integer)) {
                throw new IllegalArgumentException("ERROR: ARRAY INDEX MUST BE AN INTEGER");
            }

            List<Object> array = (List<Object>) arrayValue;
            int index = (Integer) indexValue;

            if (index < 0 || index >= array.size()) {
                throw new IllegalArgumentException("ERROR: ARRAY INDEX OUT OF BOUNDS");
            }

            array.set(index, value);
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
                case LESS_THAN:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left < (Integer) right;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPES FOR <");

                case GREATER_THAN:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left > (Integer) right;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPES FOR >");

                case LESS_EQUALS:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left <= (Integer) right;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPES FOR <=");

                case GREATER_EQUALS:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left >= (Integer) right;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPES FOR >=");

                case EQUALS_EQUALS:
                    return left.equals(right);

                case NOT_EQUALS:
                    return !left.equals(right);
                
                case AND:
                    if (left instanceof Boolean && right instanceof Boolean) {
                        return (Boolean) left && (Boolean) right;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPES FOR &&");

                case OR:
                    if (left instanceof Boolean && right instanceof Boolean) {
                        return (Boolean) left || (Boolean) right;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPES FOR ||");
                    
                case PLUS:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left + (Integer) right;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPES FOR +");

                case MINUS:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left - (Integer) right;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPES FOR -");

                case MULTIPLY:
                    if (left instanceof Integer && right instanceof Integer) {
                        return (Integer) left * (Integer) right;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPES FOR *");

                case DIVIDE:
                    if (!(left instanceof Integer) || !(right instanceof Integer)) {
                        throw new IllegalArgumentException(
                            "ERROR: INVALID TYPES FOR /"
                        );
                    }

                    if ((Integer) right == 0) {
                        throw new IllegalArgumentException("ERROR: DIVISION BY ZERO");
                    }

                    return (Integer) left / (Integer) right;
                default:
                    throw new IllegalArgumentException("ERROR: UNKNOWN BINARY OPERATOR");
            }
            
        }

        if (expression instanceof UnaryExpression unary) {
            Object operand = evaluate(unary.getOperand());

            switch (unary.getOperator()) {
                case NOT:
                    if (operand instanceof Boolean) {
                        return !(Boolean) operand;
                    }

                    throw new IllegalArgumentException("ERROR: INVALID TYPE FOR !");

                default:
                    throw new IllegalArgumentException("ERROR: UNKNOWN UNARY OPERATOR");
            }
        }

        if (expression instanceof FunctionCall functionCall) {
            Object functionValue = environment.get(functionCall.getName());

            if (!(functionValue instanceof FunctionDeclaration)) {
                throw new IllegalArgumentException("ERROR: VALUE IS NOT A FUNCTION");
            }

            FunctionDeclaration function = (FunctionDeclaration) functionValue;

            List<ASTNode> arguments = functionCall.getArguments();
            List<String> parameters = function.getParameters();

            if (arguments.size() != parameters.size()) {
                throw new IllegalArgumentException("ERROR: WRONG NUMBER OF ARGUMENTS");
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
        
        if (expression instanceof ArrayLiteral array) {
            List<Object> elements = new ArrayList<>();

            for (ASTNode element : array.getElements()) {
                elements.add(evaluate(element));
            }

            return elements;
        }

        if (expression instanceof IndexExpression indexExpression) {
            Object arrayValue = evaluate(indexExpression.getArray());
            Object indexValue = evaluate(indexExpression.getIndex());

            if (!(arrayValue instanceof List<?>)) {
                throw new IllegalArgumentException("ERROR: VALUE IS NOT AN ARRAY");
            }

            if (!(indexValue instanceof Integer)) {
                throw new IllegalArgumentException(
                    "ERROR: ARRAY INDEX MUST BE AN INTEGER"
                );
            }

            List<?> array = (List<?>) arrayValue;
            int index = (Integer) indexValue;

            if (index < 0 || index >= array.size()) {
                throw new IllegalArgumentException("ERROR: ARRAY INDEX OUT OF BOUNDS");
            }

            return array.get(index);
        }

        throw new IllegalArgumentException("ERROR: UNKNOWN EXPRESSION");
    }

    private void executeBlock(BlockStatement block) {
        for (ASTNode statement : block.getStatements()) {
            executeStatement(statement);
        }
    }

}
