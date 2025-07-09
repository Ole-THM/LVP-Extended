package functionplotter.ast;

import java.text.ParseException;

public sealed interface ASTNodeI permits AST, BinaryLogicalOPNode, BinaryOpNode, FunctionCallNode, TernaryOpNode, UnaryLogicalOpNode, UnaryOpNode, ValueNode, VariableNode {
    double evaluate();
    boolean hasVar(String name);
    String toStringInfix(boolean printOutVariables);
    String toStringRPN(boolean printOutVariables);
    String toDotGraph();
    String name();
    String getId();
    ASTNodeI copy();
}