package functionplotter.ast;

import java.text.ParseException;

public record AST(ASTNodeI root) implements ASTNodeI {

    @Override
    public ASTNodeI copy() { return new AST(this.root.copy()); }

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

    @Override
    public String toDotGraph() {
        if (this.root == null) {
            throw new IllegalStateException("AST root is not set.");
        }
//        System.out.println("diagraph AST {" + this.root.toDotGraph() + "}");
        return "digraph AST {" + this.root.toDotGraph() + "}";
    }

    @Override
    public String name() {
        if (this.root == null) {
            throw new IllegalStateException("AST root is not set.");
        }
        return this.root.name();
    }

    @Override
    public String getId() { return "AST_" + System.identityHashCode(this); }
}
