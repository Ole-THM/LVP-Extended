package functionplotter.parsing.parser;

import functionplotter.ast.*;
import functionplotter.parsing.lexer.Lexer;
import functionplotter.utils.Token;
import functionplotter.utils.TOKEN_TYPE;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfixParser implements ParserI {
    private List<Token> tokens;
    private int pos = 0;
    private final Lexer lexer = new Lexer();

    public ASTNodeI parse(String input) throws ParseException {
        this.tokens = this.lexer.tokenize(input);
        System.out.println("InfixParser.parse: Tokens: " + this.tokens);
        this.pos = 0;
        if (this.tokens.get(this.pos).type() == TOKEN_TYPE.EOF) {
            return new AST(new ValueNode(0)); // Leerer Ausdruck gibt 0 zurück
        }
        ASTNodeI result = new AST(parseExpression());
        if (this.tokens.get(pos).type() != TOKEN_TYPE.EOF) {
            throw new ParseException("Unerwartete Tokens am Ende: " + peek(), pos);
        }
        System.out.println("InfixParser.parse: Finished parsing expression. Result: " + result);
        return result;
    }

    private ASTNodeI parseExpression() throws ParseException {
        System.out.println("InfixParser.parseExpression: Parsing expression at position: " + pos);
        ASTNodeI node = parseTerm();
        while (match(TOKEN_TYPE.PLUS, TOKEN_TYPE.MINUS)) {
            TOKEN_TYPE op = previous().type();
            ASTNodeI right = parseTerm();
            node = new BinaryOpNode(node, op, right);
        }
        return node;
    }

    private ASTNodeI parseTerm() throws ParseException {
        System.out.println("InfixParser.parseTerm: Parsing term at position: " + pos);
        ASTNodeI node = parseFactor();
        while (match(TOKEN_TYPE.MULTIPLY, TOKEN_TYPE.DIVIDE)) {
            TOKEN_TYPE op = previous().type();
            ASTNodeI right = parseFactor();
            node = new BinaryOpNode(node, op, right);
        }
        return node;
    }

    private ASTNodeI parseFactor() throws ParseException {
        System.out.println("InfixParser.parseFactor: Parsing factor at position: " + pos);
        ASTNodeI node = parsePrimary();
        if (match(TOKEN_TYPE.EXPONENT)) {
            TOKEN_TYPE op = previous().type();
            ASTNodeI right = parsePrimary();
            node = new BinaryOpNode(node, op, right);
        }
        return node;
    }
    private ASTNodeI parsePrimary() throws ParseException {
        System.out.println("InfixParser.parsePrimary: Parsing primary at position: " + pos);
        if (match(TOKEN_TYPE.UNARYMINUS, TOKEN_TYPE.MINUS)) {
            // -x wird als UnaryOpNode gespeichert
            return new UnaryOpNode(parseFactor(), TOKEN_TYPE.UNARYMINUS);
        }
        if (match(TOKEN_TYPE.NUMBER)) {
            return new ValueNode(Double.parseDouble(previous().text()));
        }
        if (match(TOKEN_TYPE.IDENTIFIER)) {
            return new VariableNode(previous().text());
        }
        if (match(TOKEN_TYPE.OPENPARENTHESIS)) {
            ASTNodeI expr = parseExpression();
            expect(TOKEN_TYPE.CLOSEPARENTHESIS);
            return expr;
        }
        if (match(TOKEN_TYPE.FUNCTION)) {
            return parseFunctionCall();
        }
        throw new ParseException("Unerwartetes Token: " + peek(), this.pos);
    }

    private ASTNodeI parseFunctionCall() throws ParseException {
        System.out.println("InfixParser.parseFunctionCall: Parsing function call at position: " + pos);
        String funcName = previous().text();
        expect(TOKEN_TYPE.OPENPARENTHESIS);
        ASTNodeI firstArg = parseExpression();
        ASTNodeI secondArg = null;
        if (match(TOKEN_TYPE.COMMA)) {
            secondArg = parseExpression();
        }
        List<ASTNodeI> args = new ArrayList<>(Arrays.asList(firstArg, secondArg));
        expect(TOKEN_TYPE.CLOSEPARENTHESIS);
        return new FunctionCallNode(funcName, args);
    }

    // Hilfsmethoden
    private boolean match(TOKEN_TYPE... types) {
        for (TOKEN_TYPE type : types) {
            if (check(type)) {
                pos++;
                return true;
            }
        }
        return false;
    }
    private boolean check(TOKEN_TYPE type) { return peek().type() == type; }
    private Token previous() { return this.tokens.get(pos - 1); }
    private Token peek() { return this.tokens.get(pos); }
    private void expect(TOKEN_TYPE type) throws ParseException {
        if (!match(type)) throw new ParseException("Erwartet: " + type, this.pos);
    }

    @Override
    public boolean isValid(String input) {
        try {
            System.out.println("InfixParser.isvalid: Validating infix expression: " + input);
            this.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}