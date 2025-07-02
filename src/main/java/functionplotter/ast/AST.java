package functionplotter.ast;

public record AST(ASTNodeI root) implements ASTNodeI {

    @Override
    public double evaluate() {
        if (this.root == null) {
            throw new IllegalStateException("AST root is not set.");
        }
        return this.root.evaluate();
    }

    public String toStringInfix() {
        if (this.root == null) {
            throw new IllegalStateException("AST root is not set.");
        }
        return this.root.toStringInfix();
    }

    @Override
    public String toStringRPN() {
        if (this.root == null) {
            throw new IllegalStateException("AST root is not set.");
        }
        return this.root.toStringRPN();
    }
}
