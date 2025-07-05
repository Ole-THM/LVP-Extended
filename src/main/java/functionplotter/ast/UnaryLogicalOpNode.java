package functionplotter.ast;

import functionplotter.utils.TOKEN_TYPE;

import java.text.ParseException;

public record UnaryLogicalOpNode(ASTNodeI node, TOKEN_TYPE op) implements ASTNodeI {

    @Override
    public ASTNodeI copy() { return new UnaryLogicalOpNode(this.node.copy(), this.op); }

    @Override
    public double evaluate() { return node.evaluate() > 0 ? 0 : 1; }

    @Override
    public String toStringInfix() {
        return "!" + this.node.toStringInfix();
    }

    @Override
    public String toStringRPN() {
        return this.node.toStringRPN() + " !";
    }

    @Override
    public String toDotGraph() {
        return "\"" + this.getId() + "\" [label=\"" + name() + "\"];\n" +
                "\"" + this.getId() + "\" -> \"" + node.getId() + "\";\n" +
                node.toDotGraph();
    }

    @Override
    public String name() { return "!"; }


    @Override
    public String getId() { return "UnaryLogicalOpNode_" + System.identityHashCode(this); }
}
