package functionplotter.ast;

import functionplotter.utils.TOKEN_TYPE;

public record UnaryOpNode(ASTNodeI node, TOKEN_TYPE op) implements ASTNodeI {

    @Override
    public double evaluate() {
        return switch (op) {
            case PLUS -> this.node.evaluate(); // Unary plus, no change
            case UNARYMINUS -> -this.node.evaluate(); // Unary minus, negate the astNode
            default -> throw new UnsupportedOperationException("Unsupported operation: " + op);
        };
    }

    @Override
    public String toStringInfix() {
        return switch (op) {
            case PLUS -> this.node.toStringInfix(); // Unary plus, no change
            case UNARYMINUS -> "¯" + this.node.toStringInfix(); // Unary minus, negate the astNode
            default -> throw new UnsupportedOperationException("Unsupported operation: " + op);
        };

    }

    @Override
    public String toStringRPN() {
        return switch (op) {
            case PLUS -> this.node.toStringInfix(); // Unary plus, no change
            case UNARYMINUS -> this.node.toStringInfix() + "¯"; // Unary minus, negate the astNode
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