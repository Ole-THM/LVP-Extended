package functionplotter.ast;

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
            case "asin" -> Math.asin(this.arguments.getFirst().evaluate());
            case "sinh" -> Math.sinh(this.arguments.getFirst().evaluate());
            case "asinh" -> Math.log(this.arguments.getFirst().evaluate() + Math.sqrt(Math.pow(this.arguments.getFirst().evaluate(), 2) + 1));
            case "cos" -> Math.cos(this.arguments.getFirst().evaluate());
            case "acos" -> Math.acos(this.arguments.getFirst().evaluate());
            case "cosh" -> Math.cosh(this.arguments.getFirst().evaluate());
            case "acosh" -> Math.log(this.arguments.getFirst().evaluate() + Math.sqrt(Math.pow(this.arguments.getFirst().evaluate(), 2) - 1));
            case "tan" -> Math.tan(this.arguments.getFirst().evaluate());
            case "atan" -> Math.atan(this.arguments.getFirst().evaluate());
            case "tanh" -> Math.tanh(this.arguments.getFirst().evaluate());
            case "atanh" -> 0.5 * Math.log((1 + this.arguments.getFirst().evaluate()) / (1 - this.arguments.getFirst().evaluate()));
            case "sqrt" -> Math.sqrt(this.arguments.getFirst().evaluate());
            case "root" -> this.arguments.size() == 2
                    ? this.root(this.arguments.getFirst().evaluate(), this.arguments.get(1).evaluate())
                    : Math.sqrt(this.arguments.get(0).evaluate()); // defaults to sqrt if no base is given
            case "log" -> this.arguments.size() == 2
                    ? this.log_n(this.arguments.getFirst().evaluate(), this.arguments.get(1).evaluate())
                    : this.log_n(10, this.arguments.getFirst().evaluate()); // defaults to base 10 if no base is given
            case "ln" -> this.log_n(Math.E, this.arguments.getFirst().evaluate());
            case "abs" -> Math.abs(this.arguments.getFirst().evaluate());
            case "gamma" -> this.gamma(this.arguments.getFirst().evaluate());
            case "factorial" -> this.factorial(this.arguments.getFirst().evaluate());
            case "heaviside" -> this.arguments.getFirst().evaluate() >= 0 ? 1.0 : 0.0;
            case "signum" -> Math.signum(this.arguments.getFirst().evaluate());
            case "min" -> Math.min(this.arguments.getFirst().evaluate(), this.arguments.get(1).evaluate());
            case "max" -> Math.max(this.arguments.getFirst().evaluate(), this.arguments.get(1).evaluate());
            case "gauss" -> {
                double x = this.arguments.getFirst().evaluate();
                double sigma = this.arguments.size() >= 2 ? this.arguments.get(1).evaluate() : 1.0;
                double mu = this.arguments.size() >= 3 ? this.arguments.get(2).evaluate() : 0.0;
                yield (1 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp(-0.5 * Math.pow((x - mu) / sigma, 2));
            }
            case "logistic" -> {
                double x = this.arguments.getFirst().evaluate();
                double L = this.arguments.size() >= 2 ? this.arguments.get(1).evaluate() : 1.0;
                double k = this.arguments.size() >= 3 ? this.arguments.get(2).evaluate() : 1.0;
                double x0 = this.arguments.size() >= 4 ? this.arguments.get(3).evaluate() : 0.0;
                yield L / (1 + Math.exp(-k * (x - x0)));
            }
            case "square" -> Math.sin(this.arguments.getFirst().evaluate()) >= 0 ? 1.0 : -1.0;
            case "sawtooth" -> {
                double x = this.arguments.getFirst().evaluate();
                yield 2 * (x / (2 * Math.PI) - Math.floor(x / (2 * Math.PI) + 0.5));
            }
            case "triangle" -> {
                double x = this.arguments.getFirst().evaluate();
                yield 2 * Math.abs(2 * (x / (2 * Math.PI) - Math.floor(x / (2 * Math.PI) + 0.5))) - 1;
            }
            default -> throw new UnsupportedOperationException("Unsupported function: " + functionName);
        };
    }

    @Override
    public boolean hasVar(String name) {
        for (ASTNodeI arg : this.arguments) {
            if (arg != null && arg.hasVar(name)) {
                return true;
            }
        }
        return false;
    }

    private double log_n(double base, double value) {
        return Math.log(value) / Math.log(base);
    }

    @Override
    public String toStringInfix(boolean printOutVariables) {
        return switch (functionName) {
            case "sin" -> "sin(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "asin" -> "asin(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "sinh" -> "sinh(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "asinh" -> "asinh(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "cos" -> "cos(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "acos" -> "acos(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "cosh" -> "cosh(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "acosh" -> "acosh(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "tan" -> "tan(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "atan" -> "atan(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "tanh" -> "tanh(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "atanh" -> "atanh(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "sqrt" -> "sqrt(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "root" -> this.arguments.size() == 2
                    ? "root(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ", " + this.arguments.get(1).toStringInfix(printOutVariables) + ")"
                    : "sqrt(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "log" -> "log(" + (this.arguments.size() == 2
                    ? this.arguments.getFirst().toStringInfix(printOutVariables) + ", " + this.arguments.get(1).toStringInfix(printOutVariables)
                    : this.arguments.getFirst().toStringInfix(printOutVariables)) + ")";
            case "ln" -> "ln(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "abs" -> "abs(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "gamma" -> "gamma(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "factorial" -> "factorial(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "heaviside" -> "heaviside(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "signum" -> "signum(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "min" -> "min(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ", " + this.arguments.get(1).toStringInfix(printOutVariables) + ")";
            case "max" -> "max(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ", " + this.arguments.get(1).toStringInfix(printOutVariables) + ")";
            case "gauss" -> {
                String x = this.arguments.getFirst().toStringInfix(printOutVariables);
                String sigma = this.arguments.size() >= 2 ? this.arguments.get(1).toStringInfix(printOutVariables) : "1.0";
                String mu = this.arguments.size() >= 3 ? this.arguments.get(2).toStringInfix(printOutVariables) : "0.0";
                yield "gauss(" + x + ", " + sigma + ", " + mu + ")";
            }
            case "logistic" -> {
                String x = this.arguments.getFirst().toStringInfix(printOutVariables);
                String L = this.arguments.size() >= 2 ? this.arguments.get(1).toStringInfix(printOutVariables) : "1.0";
                String k = this.arguments.size() >= 3 ? this.arguments.get(2).toStringInfix(printOutVariables) : "1.0";
                String x0 = this.arguments.size() >= 4 ? this.arguments.get(3).toStringInfix(printOutVariables) : "0.0";
                yield "logistic(" + x + ", " + L + ", " + k + ", " + x0 + ")";
            }
            case "square" -> "square(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "sawtooth" -> "sawtooth(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            case "triangle" -> "triangle(" + this.arguments.getFirst().toStringInfix(printOutVariables) + ")";
            default -> throw new UnsupportedOperationException("Unsupported function: " + functionName);
        };
    }

    @Override
    public String toStringRPN(boolean printOutVariables) {
        return switch (functionName) {
            case "sin" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " sin";
            case "asin" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " asin";
            case "sinh" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " sinh";
            case "asinh" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " asinh";
            case "cos" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " cos";
            case "acos" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " acos";
            case "cosh" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " cosh";
            case "acosh" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " acosh";
            case "tan" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " tan";
            case "atan" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " atan";
            case "tanh" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " tanh";
            case "atanh" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " atanh";
            case "sqrt" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " sqrt";
            case "root" -> this.arguments.size() == 2
                    ? this.arguments.getFirst().toStringRPN(printOutVariables) + " " + this.arguments.get(1).toStringRPN(printOutVariables) + " root"
                    : this.arguments.getFirst().toStringRPN(printOutVariables) + "sqrt";
            case "log" -> (this.arguments.size() == 2
                    ? this.arguments.getFirst().toStringRPN(printOutVariables) + " " + this.arguments.get(1).toStringRPN(printOutVariables)
                    : this.arguments.getFirst().toStringRPN(printOutVariables)) + " log";
            case "ln" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " ln";
            case "abs" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " abs";
            case "gamma" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " gamma";
            case "factorial" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " factorial";
            case "heaviside" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " heaviside";
            case "signum" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " signum";
            case "min" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " " + this.arguments.get(1).toStringRPN(printOutVariables) + " min";
            case "max" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " " + this.arguments.get(1).toStringRPN(printOutVariables) + " max";
            case "gauss" -> {
                String x = this.arguments.getFirst().toStringRPN(printOutVariables);
                String sigma = this.arguments.size() >= 2 ? this.arguments.get(1).toStringRPN(printOutVariables) : "1.0";
                String mu = this.arguments.size() >= 3 ? this.arguments.get(2).toStringRPN(printOutVariables) : "0.0";
                yield x + " " + sigma + " " + mu + " gauss";
            }
            case "logistic" -> {
                String x = this.arguments.getFirst().toStringRPN(printOutVariables);
                String L = this.arguments.size() >= 2 ? this.arguments.get(1).toStringRPN(printOutVariables) : "1.0";
                String k = this.arguments.size() >= 3 ? this.arguments.get(2).toStringRPN(printOutVariables) : "1.0";
                String x0 = this.arguments.size() >= 4 ? this.arguments.get(3).toStringRPN(printOutVariables) : "0.0";
                yield x + " " + L + " " + k + " " + x0 + " logistic";
            }
            case "square" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " square";
            case "sawtooth" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " sawtooth";
            case "triangle" -> this.arguments.getFirst().toStringRPN(printOutVariables) + " triangle";
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

    private double root(double base, double index) { return Math.pow(index, 1.0 / base); }

    private double gamma(double x) {
        // Stirling's Approximation für Gamma-Funktion
        if (x < 0.5) {
            return Math.PI / (Math.sin(Math.PI * x) * gamma(1 - x));
        }
        x -= 1;
        double p = 0.99999999999980993;
        double[] g = {676.5203681218851, -1259.1392167224028, 771.32342877765313,
                      -176.61502916214059, 12.507343278686905, -0.13857109526572012,
                      9.9843695780195716e-6, 1.5056327351493116e-7};
        
        for (int i = 0; i < g.length; i++) {
            p += g[i] / (x + i + 1);
        }
        
        double t = x + g.length - 1.5;
        return Math.sqrt(2 * Math.PI) * Math.pow(t, x + 0.5) * Math.exp(-t) * p;
    }

    private double factorial(double n) {
        if (n == Math.floor(n)) {
            if (n >= 0) {
                return gamma(n + 1);
            } else {
                return Double.NaN;
            }
        }
        return gamma(n + 1);
    }

}