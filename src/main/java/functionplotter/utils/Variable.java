package functionplotter.utils;

import functionplotter.ast.ASTNodeI;

public record Variable(String name, ASTNodeI astNode) {

    public double evaluate() {
        if (astNode == null) {
            throw new IllegalStateException("Variable astNode is not set.");
        }
        return astNode.evaluate();
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", astNode=" + astNode +
                '}';
    }

    public String toStringInfix() {
        return this.astNode.toStringInfix();
    }

    public String toStringRPN() { return this.astNode.toStringRPN(); }
}