package functionplotter.parsing.parser;

import functionplotter.ast.*;
import functionplotter.parsing.lexer.Lexer;
import functionplotter.utils.Token;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class RPNParser implements ParserI {
    private final Stack<ASTNodeI> stack = new Stack<>();
    private final Lexer lexer = new Lexer();

    public AST parse(String input) throws ParseException {
        this.stack.clear();
        List<Token> tokens = this.lexer.tokenize(input);
        for (Token token : tokens) {
            switch (token.type()) {
                case NUMBER -> this.stack.push(
                        new ValueNode(Double.parseDouble(token.text()))
                );
                case IDENTIFIER -> this.stack.push(
                        new VariableNode(token.text())
                );
                case FUNCTION -> {
                    int argCount = getArgumentCount(token.text());
                    List<ASTNodeI> args = new ArrayList<>();

                    for (int i = 0; i < argCount; i++) {
                        if (!this.stack.isEmpty()) {
                            args.addFirst(this.safePop());
                        } else {
                            args.addFirst(null);
                        }
                    }

                    this.stack.push(new FunctionCallNode(token.text(), args));
                }
                case MINUS, PLUS, MULTIPLY, DIVIDE, EXPONENT, MODULO -> {
                    ASTNodeI right = this.safePop();
                    ASTNodeI left = this.safePop();
                    this.stack.push(
                            new BinaryOpNode(left, token.type(), right)
                    );
                }
                case GT, LT, GTE, LTE, EQ, NEQ, AND, OR -> {
                    ASTNodeI right = this.safePop();
                    ASTNodeI left = this.safePop();
                    this.stack.push(
                            new BinaryLogicalOPNode(left, token.type(), right)
                    );
                }
                case UNARYMINUS -> this.stack.push(
                        new UnaryOpNode(this.safePop(), token.type())
                );
                case NOT -> this.stack.push(
                        new UnaryLogicalOpNode(this.safePop(), token.type())
                );
                case QUESTION -> {
                    ASTNodeI falseValue = this.safePop();
                    ASTNodeI trueValue = this.safePop();
                    ASTNodeI condition = this.safePop();
                    this.stack.push(
                            new TernaryOpNode(condition, trueValue, falseValue)
                    );
                }
                case EOF -> {}
                default -> throw new ParseException("Unexpected Token: " + token.text(), 0);
            }
        }
        return new AST(this.stack.pop());
    }

    private ASTNodeI safePop() throws ParseException {
        if (this.stack.isEmpty()) {
            throw new ParseException("Stack underflow: not enough operands for operation", 0);
        }
        return this.stack.pop();
    }

    @Override
    public boolean isValid(String input) {
        try {
            parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private int getArgumentCount(String functionName) {
        return switch (functionName) {
            case "logistic" -> 4;
            case "gauss" -> 3;
            case "log", "root", "min", "max" -> 2;
            case "sin", "cos", "tan", "sqrt", "ln", "abs", "factorial", "gamma" -> 1;
            default -> 1; // Standard: 1 Argument
        };
    }

}