package functionplotter.ast;

public record ValueNode(double value) implements ASTNodeI {
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
}
