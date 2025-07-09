package functionplotter.ast;

import functionplotter.utils.TOKEN_TYPE;

import java.text.ParseException;

public record UnaryOpNode(ASTNodeI node, TOKEN_TYPE op) implements ASTNodeI {

    @Override
    public ASTNodeI copy() { return new UnaryOpNode(this.node.copy(), this.op); }

    @Override
    public double evaluate() {
        return switch (op) {
            case PLUS -> this.node.evaluate(); // Unary plus, no change
            case UNARYMINUS -> -this.node.evaluate(); // Unary minus, negate the astNode
            default -> throw new UnsupportedOperationException("Unsupported operation: " + op);
        };
    }

    @Override
    public boolean hasVar(String name) { return this.node.hasVar(name); }

    @Override
    public String toStringInfix(boolean printOutVariables) {
        return switch (op) {
            case PLUS -> this.node.toStringInfix(printOutVariables); // Unary plus, no change
            case UNARYMINUS -> "¯" + this.node.toStringInfix(printOutVariables); // Unary minus, negate the astNode
            default -> throw new UnsupportedOperationException("Unsupported operation: " + op);
        };

    }

    @Override
    public String toStringRPN(boolean printOutVariables) {
        return switch (op) {
            case PLUS -> this.node.toStringRPN(printOutVariables); // Unary plus, no change
            case UNARYMINUS -> this.node.toStringRPN(printOutVariables) + "¯"; // Unary minus, negate the astNode
            default -> throw new UnsupportedOperationException("Unsupported operation: " + op);
        };
    }

    @Override
    public String toDotGraph() {
        return "\"" + this.getId() + "\" [label=\"" + name() + "\"];\n" +
               "\"" + this.getId() + "\" -> \"" + node.getId() + "\";\n" +
                node.toDotGraph();
    }

    @Override
    public String name() {
        return switch (op) {
            case PLUS -> "+";
            case UNARYMINUS -> "¯";
            default -> throw new UnsupportedOperationException("Unsupported operation: " + op);
        };
    }

    @Override
    public String getId() { return "UnaryOpNode_" + System.identityHashCode(this); }
}