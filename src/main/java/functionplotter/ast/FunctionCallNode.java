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
    public String toStringInfix() {
        return switch (functionName) {
            case "sin" -> "sin(" + this.arguments.getFirst().toStringInfix() + ")";
            case "asin" -> "asin(" + this.arguments.getFirst().toStringInfix() + ")";
            case "sinh" -> "sinh(" + this.arguments.getFirst().toStringInfix() + ")";
            case "asinh" -> "asinh(" + this.arguments.getFirst().toStringInfix() + ")";
            case "cos" -> "cos(" + this.arguments.getFirst().toStringInfix() + ")";
            case "acos" -> "acos(" + this.arguments.getFirst().toStringInfix() + ")";
            case "cosh" -> "cosh(" + this.arguments.getFirst().toStringInfix() + ")";
            case "acosh" -> "acosh(" + this.arguments.getFirst().toStringInfix() + ")";
            case "tan" -> "tan(" + this.arguments.getFirst().toStringInfix() + ")";
            case "atan" -> "atan(" + this.arguments.getFirst().toStringInfix() + ")";
            case "tanh" -> "tanh(" + this.arguments.getFirst().toStringInfix() + ")";
            case "atanh" -> "atanh(" + this.arguments.getFirst().toStringInfix() + ")";
            case "sqrt" -> "sqrt(" + this.arguments.getFirst().toStringInfix() + ")";
            case "root" -> this.arguments.size() == 2
                    ? "root(" + this.arguments.getFirst().toStringInfix() + ", " + this.arguments.get(1).toStringInfix() + ")"
                    : "sqrt(" + this.arguments.getFirst().toStringInfix() + ")";
            case "log" -> "log(" + (this.arguments.size() == 2
                    ? this.arguments.getFirst().toStringInfix() + ", " + this.arguments.get(1).toStringInfix()
                    : this.arguments.getFirst().toStringInfix()) + ")";
            case "ln" -> "ln(" + this.arguments.getFirst().toStringInfix() + ")";
            case "abs" -> "abs(" + this.arguments.getFirst().toStringInfix() + ")";
            case "gamma" -> "gamma(" + this.arguments.getFirst().toStringInfix() + ")";
            case "factorial" -> "factorial(" + this.arguments.getFirst().toStringInfix() + ")";
            case "heaviside" -> "heaviside(" + this.arguments.getFirst().toStringInfix() + ")";
            case "signum" -> "signum(" + this.arguments.getFirst().toStringInfix() + ")";
            case "min" -> "min(" + this.arguments.getFirst().toStringInfix() + ", " + this.arguments.get(1).toStringInfix() + ")";
            case "max" -> "max(" + this.arguments.getFirst().toStringInfix() + ", " + this.arguments.get(1).toStringInfix() + ")";
            case "gauss" -> {
                String x = this.arguments.getFirst().toStringInfix();
                String sigma = this.arguments.size() >= 2 ? this.arguments.get(1).toStringInfix() : "1.0";
                String mu = this.arguments.size() >= 3 ? this.arguments.get(2).toStringInfix() : "0.0";
                yield "gauss(" + x + ", " + sigma + ", " + mu + ")";
            }
            case "logistic" -> {
                String x = this.arguments.getFirst().toStringInfix();
                String L = this.arguments.size() >= 2 ? this.arguments.get(1).toStringInfix() : "1.0";
                String k = this.arguments.size() >= 3 ? this.arguments.get(2).toStringInfix() : "1.0";
                String x0 = this.arguments.size() >= 4 ? this.arguments.get(3).toStringInfix() : "0.0";
                yield "logistic(" + x + ", " + L + ", " + k + ", " + x0 + ")";
            }
            case "square" -> "square(" + this.arguments.getFirst().toStringInfix() + ")";
            case "sawtooth" -> "sawtooth(" + this.arguments.getFirst().toStringInfix() + ")";
            case "triangle" -> "triangle(" + this.arguments.getFirst().toStringInfix() + ")";
            default -> throw new UnsupportedOperationException("Unsupported function: " + functionName);
        };
    }

    @Override
    public String toStringRPN() {
        return switch (functionName) {
            case "sin" -> this.arguments.getFirst().toStringRPN() + " sin";
            case "asin" -> this.arguments.getFirst().toStringRPN() + " asin";
            case "sinh" -> this.arguments.getFirst().toStringRPN() + " sinh";
            case "asinh" -> this.arguments.getFirst().toStringRPN() + " asinh";
            case "cos" -> this.arguments.getFirst().toStringRPN() + " cos";
            case "acos" -> this.arguments.getFirst().toStringRPN() + " acos";
            case "cosh" -> this.arguments.getFirst().toStringRPN() + " cosh";
            case "acosh" -> this.arguments.getFirst().toStringRPN() + " acosh";
            case "tan" -> this.arguments.getFirst().toStringRPN() + " tan";
            case "atan" -> this.arguments.getFirst().toStringRPN() + " atan";
            case "tanh" -> this.arguments.getFirst().toStringRPN() + " tanh";
            case "atanh" -> this.arguments.getFirst().toStringRPN() + " atanh";
            case "sqrt" -> this.arguments.getFirst().toStringRPN() + " sqrt";
            case "root" -> this.arguments.size() == 2
                    ? this.arguments.getFirst().toStringInfix() + " " + this.arguments.get(1).toStringInfix() + " root"
                    : this.arguments.getFirst().toStringInfix() + "sqrt";
            case "log" -> (this.arguments.size() == 2
                    ? this.arguments.getFirst().toStringRPN() + " " + this.arguments.get(1).toStringRPN()
                    : this.arguments.getFirst().toStringRPN()) + " log";
            case "ln" -> this.arguments.getFirst().toStringRPN() + " ln";
            case "abs" -> this.arguments.getFirst().toStringRPN() + " abs";
            case "gamma" -> this.arguments.getFirst().toStringRPN() + " gamma";
            case "factorial" -> this.arguments.getFirst().toStringRPN() + " factorial";
            case "heaviside" -> this.arguments.getFirst().toStringRPN() + " heaviside";
            case "signum" -> this.arguments.getFirst().toStringRPN() + " signum";
            case "min" -> this.arguments.getFirst().toStringRPN() + " " + this.arguments.get(1).toStringRPN() + " min";
            case "max" -> this.arguments.getFirst().toStringRPN() + " " + this.arguments.get(1).toStringRPN() + " max";
            case "gauss" -> {
                String x = this.arguments.getFirst().toStringRPN();
                String sigma = this.arguments.size() >= 2 ? this.arguments.get(1).toStringRPN() : "1.0";
                String mu = this.arguments.size() >= 3 ? this.arguments.get(2).toStringRPN() : "0.0";
                yield x + " " + sigma + " " + mu + " gauss";
            }
            case "logistic" -> {
                String x = this.arguments.getFirst().toStringRPN();
                String L = this.arguments.size() >= 2 ? this.arguments.get(1).toStringRPN() : "1.0";
                String k = this.arguments.size() >= 3 ? this.arguments.get(2).toStringRPN() : "1.0";
                String x0 = this.arguments.size() >= 4 ? this.arguments.get(3).toStringRPN() : "0.0";
                yield x + " " + L + " " + k + " " + x0 + " logistic";
            }
            case "square" -> this.arguments.getFirst().toStringRPN() + " square";
            case "sawtooth" -> this.arguments.getFirst().toStringRPN() + " sawtooth";
            case "triangle" -> this.arguments.getFirst().toStringRPN() + " triangle";
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