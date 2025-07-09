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

    @Override
    public boolean hasVar(String name) {
        if (this.root == null) {
            throw new IllegalStateException("AST root is not set.");
        }
        return this.root.hasVar(name);
    }
    @Override
    public String toStringInfix(boolean printOutVariables) {
        if (this.root == null) {
            throw new IllegalStateException("AST root is not set.");
        }
        return this.root.toStringInfix(printOutVariables);
    }

    @Override
    public String toStringRPN(boolean printOutVariables) {
        if (this.root == null) {
            throw new IllegalStateException("AST root is not set.");
        }
        return this.root.toStringRPN(printOutVariables);
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

    public boolean isEmpty() { return this.root == null || (this.root instanceof ValueNode && ((ValueNode) this.root).value() == 0); }
}
