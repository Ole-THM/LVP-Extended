package functionplotter.ast;

import java.text.ParseException;

public record TernaryOpNode(ASTNodeI condition, ASTNodeI trueValue, ASTNodeI falseValue) implements ASTNodeI {

    @Override
    public ASTNodeI copy() { return new TernaryOpNode(this.condition.copy(), this.trueValue.copy(), this.falseValue.copy()); }

    @Override
    public double evaluate() { return condition.evaluate() > 0 ? trueValue.evaluate() : falseValue.evaluate(); }

    @Override
    public String toStringInfix() { return condition.toStringInfix() + " ? " + trueValue.toStringInfix() + " : " + falseValue.toStringInfix(); }

    @Override
    public String toStringRPN() {
        return condition.toStringRPN() + " " +
                trueValue.toStringRPN() + " " +
                falseValue.toStringRPN() + " ?:";
    }


    @Override
    public String toDotGraph() {
        return "\"" + this.getId() + "\" [label=\"" + name() + "\"];\n" +
                "\"" + this.getId() + "\" -> \"" + condition.getId() + "\";\n" +
                condition.toDotGraph() +
                "\"" + this.getId() + "\" -> \"" + trueValue.getId() + "\";\n" +
                trueValue.toDotGraph() +
                "\"" + this.getId() + "\" -> \"" + falseValue.getId() + "\";\n" +
                falseValue.toDotGraph();
    }

    @Override
    public String name() { return "? :"; }


    @Override
    public String getId() { return "TernaryOpNode_" + System.identityHashCode(this); }
}
