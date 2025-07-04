package functionplotter.ast;

public sealed interface ASTNodeI permits AST, BinaryOpNode, FunctionCallNode, UnaryOpNode, ValueNode, VariableNode {
    double evaluate();
    String toStringInfix();
    String toStringRPN();
    String toDotGraph();
    String name();
    String getId();
}