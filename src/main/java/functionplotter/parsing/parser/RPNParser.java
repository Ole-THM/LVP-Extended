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
                    ASTNodeI right = null;
                    ASTNodeI left = null;
                    if (token.text().equals("log")) {
                        if (this.stack.size() == 1) {
                            left = this.safePop();
                        } else if (this.stack.size() >= 2) {
                            right = this.safePop();
                            left = this.safePop();
                        }

                    } else { // Extension to support two args for the root function maybe added later
                        // For other functions, we assume they take one argument
                        left = this.safePop();
//                        System.out.println(left);

                    }
                    List<ASTNodeI> args = new ArrayList<>(Arrays.asList(left, right));
                    this.stack.push(
                            new FunctionCallNode(token.text(), args)
                    );
                }
                case MINUS, PLUS, MULTIPLY, DIVIDE, EXPONENT -> {
                    ASTNodeI right = this.safePop();
                    ASTNodeI left = this.safePop();
                    this.stack.push(
                            new BinaryOpNode(left, token.type(), right)
                    );
                }
                case UNARYMINUS -> this.stack.push(
                        new UnaryOpNode(this.safePop(), token.type())
                );
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
}