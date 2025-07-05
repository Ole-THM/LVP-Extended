package functionplotter.ast;

import java.text.ParseException;

public sealed interface ASTNodeI permits AST, BinaryLogicalOPNode, BinaryOpNode, FunctionCallNode, TernaryOpNode, UnaryLogicalOpNode, UnaryOpNode, ValueNode, VariableNode {
    double evaluate();
    String toStringInfix();
    String toStringRPN();
    String toDotGraph();
    String name();
    String getId();
    ASTNodeI copy();
}