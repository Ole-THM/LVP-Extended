package functionplotter.parsing.parser;

import functionplotter.ast.AST;
import functionplotter.ast.ASTNodeI;

import java.text.ParseException;

public class Parser {

    private static final InfixParser infixParser = new InfixParser();
    private static final RPNParser RPNParser = new RPNParser();

    public static AST parse(String expression) throws ParseException {
//        System.out.println("Parser.parse: Start parsing expression: " + expression);
        if (infixParser.isValid(expression)) {
//            System.out.println("Parser.parse: Parsing as infix expression.");
            return infixParser.parse(expression);
        } else if (RPNParser.isValid(expression)) {
//            System.out.println("Parser.parse: Parsing as RPN expression.");
            return RPNParser.parse(expression);
        } else {
//            System.out.println("Parser.parse: Invalid expression: " + expression);
            throw new ParseException("Invalid expression: " + expression, 0);
        }
    }

    public static ASTNodeI parseInfix(String expression) throws ParseException {
        return infixParser.parse(expression);
    }

    public ASTNodeI parseRPN(String expression) throws ParseException {
        return RPNParser.parse(expression);
    }
}