package functionplotter.ast;

import functionplotter.utils.GlobalContext;


public record VariableNode(String name) implements ASTNodeI {

    @Override
    public ASTNodeI copy() { return new VariableNode(this.name()); }

    @Override
    public double evaluate() {
        return GlobalContext.VARIABLES.getOrDefault(this.name()).ast().evaluate();
    }

    @Override
    public boolean hasVar(String name) { return this.name().equals(name); }

    @Override
    public String toStringInfix() {
        return switch(this.name()) {
            case "x" -> "x";
            case "pi" -> "pi";
            case "e" -> "e";
            default -> GlobalContext.VARIABLES.getOrDefault(this.name()).toStringInfix();
        };
    }

    @Override
    public String toStringRPN() {
        return switch(this.name()) {
            case "x" -> "x";
            case "pi" -> "pi";
            case "e" -> "e";
            default -> GlobalContext.VARIABLES.getOrDefault(this.name()).toStringRPN();
        };
    }

    @Override
    public String toDotGraph() {
        ASTNodeI root = GlobalContext.VARIABLES.getOrDefault(this.name()).ast().root().copy();

        // Eigenen Knoten definieren
        String res = "\"" + this.getId() + "\" [label=\"" + this.name() + "\"];\n";

        if (root instanceof ValueNode) {
            return res +
                    (this.name().equals("x")
                            ? ""
                            :
                            "\"" + this.getId() + "\" -> \"" + root.getId() + "\";\n" +
                                    root.toDotGraph()
                    );
        }

        res += "\"" + this.getId() + "\" -> \"" + root.getId() + "\";\n";

        return res + root.toDotGraph();
    }


    @Override
    public String name() { return name; }

    @Override
    public String getId() { return "VariableNode_" + System.identityHashCode(this); }
}
