package functionplotter.utils;

import functionplotter.ast.AST;
import functionplotter.ast.ValueNode;
import functionplotter.ast.ASTNodeI;

import java.util.ArrayList;
import java.util.Arrays;

public class Variables {

    private final ArrayList<Variable> variables;

    public Variables() {
        this.variables = new ArrayList<>();
        this.add(
            new Variable("E", new AST(new ValueNode(Math.E))),
            new Variable("PI", new AST(new ValueNode(Math.PI))),
            new Variable("x", new AST(new ValueNode(1)))
        );
    }

    public void add(Variable...variables) {
        this.variables.addAll(Arrays.asList(variables));
    }

    public void addDefaultVariables() {
        this.add(
            new Variable("a", new AST(new ValueNode(1))),
            new Variable("b", new AST(new ValueNode(2))),
            new Variable("c", new AST(new ValueNode(3))),
            new Variable("d", new AST(new ValueNode(4))),
            new Variable("e", new AST(new ValueNode(5)))
        );
    }

    public void set(String name, ASTNodeI node) {
        this.remove(name);
        this.add(new Variable(name, new AST(node)));
    }

    public void remove(String name) {
        this.variables.removeIf(var -> var.name().equals(name));
    }

    public Variable getOrDefault(String name) {
        for (Variable variable : this.variables) {
            if (variable.name().equals(name)) {
                return variable;
            }
        }
        return new Variable("default", new AST(new ValueNode(0))); // Variable not found return default variable
    }
}