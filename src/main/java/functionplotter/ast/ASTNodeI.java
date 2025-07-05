package functionplotter.ast;

public sealed interface ASTNodeI permits AST, BinaryLogicalOPNode, BinaryOpNode, FunctionCallNode, TernaryOpNode, UnaryLogicalOpNode, UnaryOpNode, ValueNode, VariableNode {
    double evaluate();
    String toStringInfix();
    String toStringRPN();
    String toDotGraph();
    String name();
    String getId();
}