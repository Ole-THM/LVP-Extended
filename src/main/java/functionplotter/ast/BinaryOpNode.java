package functionplotter.ast;

import functionplotter.utils.TOKEN_TYPE;

public record BinaryOpNode(ASTNodeI left, TOKEN_TYPE op, ASTNodeI right) implements ASTNodeI {
    @Override
    public double evaluate() {
        return switch (op) {
            case PLUS -> left.evaluate() + right.evaluate();
            case MINUS -> left.evaluate() - right.evaluate();
            case MULTIPLY -> left.evaluate() * right.evaluate();
            case DIVIDE -> left.evaluate() / right.evaluate();
            case EXPONENT -> Math.pow(left.evaluate(), right.evaluate());
            default -> throw new UnsupportedOperationException("Unsupported operation: " + op);
        };
    }

    @Override
    public String toStringInfix() {
        String leftStr = left instanceof BinaryOpNode lNode && precedence(lNode.op()) < precedence(this.op)
                ? "(" + left.toStringInfix() + ")"
                : left.toStringInfix();
        String rightStr = right instanceof BinaryOpNode rNode && precedence(rNode.op()) <= precedence(this.op)
                ? "(" + right.toStringInfix() + ")"
                : right.toStringInfix();
        return leftStr + " " + name() + " " + rightStr;
    }

    @Override
    public String toStringRPN() {
        return toStringRPN() + " " + toStringRPN() + " " + name();
    }

    @Override
    public String name() {
        return switch (op) {
            case PLUS -> "+";
            case MINUS -> "-";
            case MULTIPLY -> "*";
            case DIVIDE -> "/";
            case EXPONENT -> "^";
            default -> "?";
        };
    }

    private int precedence(TOKEN_TYPE op) {
        return switch (op) {
            case PLUS, MINUS -> 1;
            case MULTIPLY, DIVIDE -> 2;
            case EXPONENT -> 3;
            default -> 0;
        };
    }

    @Override
    public String toDotGraph() {
        return "\"" + this.getId() + "\" [label=\"" + name() + "\"];\n" +
                "\"" + this.getId() + "\" -> \"" + left.getId() + "\";\n" +
                "\"" + this.getId() + "\" -> \"" + right.getId() + "\";\n" +
                left.toDotGraph() +
                right.toDotGraph();

    }

    @Override
    public String getId() { return "ValueNode_" + System.identityHashCode(this); }
}
