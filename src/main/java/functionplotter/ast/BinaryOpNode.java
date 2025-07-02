package functionplotter.ast;

import functionplotter.utils.TOKEN_TYPE;

public record BinaryOpNode(ASTNodeI left, TOKEN_TYPE op, ASTNodeI right) implements ASTNodeI {
    @Override
    public double evaluate() {
        return switch (op) {
            case PLUS -> this.left.evaluate() + this.right.evaluate();
            case MINUS -> this.left.evaluate() - this.right.evaluate();
            case MULTIPLY -> this.left.evaluate() * this.right.evaluate();
            case DIVIDE -> this.left.evaluate() / this.right.evaluate();
            case EXPONENT -> Math.pow(this.left.evaluate(), this.right.evaluate());
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
        return leftStr + " " + opToString(op) + " " + rightStr;
    }

    @Override
    public String toStringRPN() {
        return this.left.toStringRPN() + " " + this.right.toStringRPN() + " " + opToString(op);
    }

    private String opToString(TOKEN_TYPE op) {
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
}
