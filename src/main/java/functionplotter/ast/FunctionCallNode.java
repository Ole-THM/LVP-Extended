package functionplotter.ast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record FunctionCallNode(String functionName, List<ASTNodeI> arguments) implements ASTNodeI {

    @Override
    public ASTNodeI copy() { return new FunctionCallNode(this.functionName, copyOfArguments()); }

    private List<ASTNodeI> copyOfArguments() {
        ArrayList<ASTNodeI> newArguments = new ArrayList<>();
        for (ASTNodeI astNodeI : this.arguments) {
            if (astNodeI != null) newArguments.add(astNodeI.copy());
        }
        return newArguments;
    }

    @Override
    public double evaluate() {
        return switch (functionName) {
            case "sin" -> Math.sin(this.arguments.getFirst().evaluate());
            case "cos" -> Math.cos(this.arguments.getFirst().evaluate());
            case "tan" -> Math.tan(this.arguments.getFirst().evaluate());
            case "sqrt" -> Math.sqrt(this.arguments.getFirst().evaluate());
            case "abs" -> Math.abs(this.arguments.getFirst().evaluate());
            case "log" -> this.arguments.get(1) != null
                    ? log_n(this.arguments.getFirst().evaluate(), this.arguments.get(1).evaluate())
                    : log_n(10, this.arguments.get(0).evaluate()); // defaults to base 10 if no base is given
            case "ln" -> log_n(Math.E, this.arguments.get(0).evaluate());
            default -> throw new UnsupportedOperationException("Unsupported function: " + functionName);
        };
    }

    private double log_n(double base, double value) {
        return Math.log(value) / Math.log(base);
    }

    @Override
    public String toStringInfix() {
        return switch (functionName) {
            case "sin" -> "sin(" + this.arguments.getFirst().toStringInfix() + ")";
            case "cos" -> "cos(" + this.arguments.getFirst().toStringInfix() + ")";
            case "tan" -> "tan(" + this.arguments.getFirst().toStringInfix() + ")";
            case "sqrt" -> "sqrt(" + this.arguments.getFirst().toStringInfix() + ")";
            case "abs" -> "abs(" + this.arguments.getFirst().toStringInfix() + ")";
            case "log" -> "log(" + (this.arguments.get(1) != null
                    ? this.arguments.getFirst().toStringInfix() + ", " + this.arguments.get(1).toStringInfix()
                    : String.format("%.4f", Math.E) + ", " + this.arguments.getFirst().toStringInfix()) + ")";
            case "ln" -> "ln(" + this.arguments.getFirst().toStringInfix() + ")";
            default -> throw new UnsupportedOperationException("Unsupported function: " + functionName);
        };
    }

    @Override
    public String toStringRPN() {
        return switch (functionName) {
            case "sin" -> this.arguments.getFirst().toStringRPN() + " sin";
            case "cos" -> this.arguments.getFirst().toStringRPN() + " cos";
            case "tan" -> this.arguments.getFirst().toStringRPN() + " tan";
            case "sqrt" -> this.arguments.getFirst().toStringRPN() + " sqrt";
            case "abs" -> this.arguments.getFirst().toStringRPN() + " abs";
            case "log" -> (this.arguments.get(1) != null
                    ? this.arguments.getFirst().toStringRPN() + " " + this.arguments.get(1).toStringRPN()
                    : String.format("%.4f", Math.E) + ", " + this.arguments.getFirst().toStringRPN()) + " log";
            case "ln" -> this.arguments.getFirst().toStringRPN() + " ln";
            default -> throw new UnsupportedOperationException("Unsupported function: " + functionName);
        };
    }

    @Override
    public String toDotGraph() {
        return "\"" + this.getId() + "\" [label=\"" + this.name() + "\"];\n" +
                this.arguments.stream()
                        .filter(Objects::nonNull)
                        .map(arg -> "\"" + this.getId() + "\" -> \"" + arg.getId() + "\";\n")
                        .reduce("", String::concat)
                + this.arguments.stream()
                .filter(Objects::nonNull)
                .map(ASTNodeI::toDotGraph)
                .reduce("", String::concat);
    }

    @Override
    public String name() {
        return this.functionName;
    }

    @Override
    public String getId() { return "FunctionCallNode_" + System.identityHashCode(this); }
}
