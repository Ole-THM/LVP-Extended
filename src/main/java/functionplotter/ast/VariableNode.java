package functionplotter.ast;

import functionplotter.utils.GlobalContext;

public record VariableNode(String name) implements ASTNodeI {
    @Override
    public double evaluate() {
        return GlobalContext.VARIABLES.getOrDefault(this.name).ast().evaluate();
    }

    @Override
    public String toStringInfix() { return GlobalContext.VARIABLES.getOrDefault(this.name).toStringInfix(); }

    @Override
    public String toStringRPN() { return GlobalContext.VARIABLES.getOrDefault(this.name).toStringRPN(); }

    @Override
public String toDotGraph() {
    ASTNodeI root = GlobalContext.VARIABLES.getOrDefault(this.name).ast().root();
    if (root instanceof ValueNode) {
        return "\"" + this.getId() + "\" [label=\"" + this.name() + "\"];\n";
    }
    return "\"" + this.getId() + "\" [label=\"" + this.name() + "\"];\n"
         + "\"" + this.getId() + "\" -> \"" + root.getId() + "\";\n"
         + root.toDotGraph();
}

    @Override
    public String name() { return name; }

    @Override
    public String getId() { return "VariableNode_" + System.identityHashCode(this); }
}
