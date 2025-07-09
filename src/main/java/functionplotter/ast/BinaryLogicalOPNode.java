package functionplotter.ast;


import functionplotter.utils.TOKEN_TYPE;

import java.text.ParseException;

public record BinaryLogicalOPNode(ASTNodeI left, TOKEN_TYPE op, ASTNodeI right) implements ASTNodeI {

    @Override
    public ASTNodeI copy() { return new BinaryLogicalOPNode(this.left.copy(), this.op, this.right.copy()); }

    @Override
    public double evaluate() {
        double leftVal = this.left.evaluate();
        double rightVal = this.right.evaluate();

        boolean result = switch (op) {
            case AND -> leftVal > 0 && rightVal > 0;
            case OR -> leftVal > 0 || rightVal > 0;
            case GT -> leftVal > rightVal;
            case LT -> leftVal < rightVal;
            case GTE -> leftVal >= rightVal;
            case LTE -> leftVal <= rightVal;
            case EQ -> Math.abs(leftVal - rightVal) < 1e-10; // compare against small values instead of zero to mitigate floating point rounding errors
            case NEQ -> Math.abs(leftVal - rightVal) >= 1e-10;
            default -> false;
        };
        return result ? 1 : 0;
    }

    @Override
    public boolean hasVar(String name) {
        return this.left.hasVar(name) || this.right.hasVar(name);
    }

    @Override
    public String toStringInfix(boolean printOutVariables) {
        String leftStr = this.left instanceof BinaryOpNode lNode && precedence(lNode.op()) < precedence(this.op)
                ? "(" + this.left.toStringInfix(printOutVariables) + ")"
                : this.left.toStringInfix(printOutVariables);
        String rightStr = this.right instanceof BinaryOpNode rNode && precedence(rNode.op()) <= precedence(this.op)
                ? "(" + this.right.toStringInfix(printOutVariables) + ")"
                : this.right.toStringInfix(printOutVariables);
        return leftStr + " " + name() + " " + rightStr;
    }

    private int precedence(TOKEN_TYPE op) {
        return switch (op) {
            case OR -> 1;
            case AND -> 2;
            default -> 0;
        };
    }

    @Override
    public String toStringRPN(boolean printOutVariables) {
        return this.left.toStringRPN(printOutVariables) + " " + this.right.toStringRPN(printOutVariables) + " " + this.name();
    }

    @Override
    public String toDotGraph() {
        return "\"" + this.getId() + "\" [label=\"" + this.name() + "\"];\n" +
                "\"" + this.getId() + "\" -> \"" + this.left.getId() + "\";\n" +
                "\"" + this.getId() + "\" -> \"" + this.right.getId() + "\";\n" +
                this.left.toDotGraph() +
                this.right.toDotGraph();

    }

    @Override
    public String name() {
        return switch (op) {
            case GT -> ">";
            case LT -> "<";
            case GTE -> ">=";
            case LTE -> "<=";
            case EQ -> "==";
            case NEQ -> "!=";
            case AND -> "&&";
            case OR -> "||";
            default -> "default";
        };
    }

    @Override
    public String getId() { return "BinaryLogicalOpNode_" + System.identityHashCode(this); }
}
