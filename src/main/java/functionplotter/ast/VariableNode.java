package functionplotter.ast;

import functionplotter.utils.GlobalContext;

public record VariableNode(String name) implements ASTNodeI {
    @Override
    public double evaluate() {
        return GlobalContext.VARIABLES.getOrDefault(this.name).evaluate();
    }

    @Override
    public String toStringInfix() {
        return GlobalContext.VARIABLES.getOrDefault(name).name();
    }

    public String toStringRPN() { return GlobalContext.VARIABLES.getOrDefault(name).toStringRPN(); }
}
