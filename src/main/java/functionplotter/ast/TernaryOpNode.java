package functionplotter.ast;

import java.text.ParseException;

public record TernaryOpNode(ASTNodeI condition, ASTNodeI trueValue, ASTNodeI falseValue) implements ASTNodeI {

    @Override
    public ASTNodeI copy() { return new TernaryOpNode(this.condition.copy(), this.trueValue.copy(), this.falseValue.copy()); }

    @Override
    public double evaluate() { return condition.evaluate() > 0 ? trueValue.evaluate() : falseValue.evaluate(); }

    @Override
    public boolean hasVar(String name) { return condition.hasVar(name) || trueValue.hasVar(name) || falseValue.hasVar(name); }

    @Override
    public String toStringInfix(boolean printOutVariables) { return condition.toStringInfix(printOutVariables) + " ? " + trueValue.toStringInfix(printOutVariables) + " : " + falseValue.toStringInfix(printOutVariables); }

    @Override
    public String toStringRPN(boolean printOutVariables) {
        return condition.toStringRPN(printOutVariables) + " " +
                trueValue.toStringRPN(printOutVariables) + " " +
                falseValue.toStringRPN(printOutVariables) + " ?:";
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
