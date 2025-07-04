package functionplotter.utils;

import functionplotter.ast.AST;
public record Variable(String name, AST ast) {

    public double evaluate() {
        if (ast == null) {
            throw new IllegalStateException("Variable ast is not set.");
        }
        return ast.evaluate();
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", ast=" + ast +
                '}';
    }

    public String toStringInfix() {
        return this.ast.toStringInfix();
    }

    public String toStringRPN() { return this.ast.toStringRPN(); }
}