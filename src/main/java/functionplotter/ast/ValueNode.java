package functionplotter.ast;

import javax.management.ValueExp;

public record ValueNode(double value) implements ASTNodeI {

    @Override
    public ASTNodeI copy() { return new ValueNode(this.value); }

    @Override
    public double evaluate() {
        return this.value;
    }

    @Override
    public String toStringInfix() {
        return (int) this.value == this.value ? String.format("%d", (int) this.value) : String.format("%.4f", this.value);
    }

    @Override
    public String toStringRPN() {
        return this.toStringInfix();
    }

    @Override
    public String toDotGraph() {
        return "\"" + this.getId() + "\" [label=\"" + this.toStringInfix() + "\"];\n";
    }

    @Override
    public String name() {
        return "" + this.value;
    }

    @Override
    public String getId() { return "ValueNode_" + System.identityHashCode(this); }
}
