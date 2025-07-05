package functionplotter.parsing.lexer;

import functionplotter.utils.Token;
import functionplotter.utils.TOKEN_TYPE;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private int pos = 0;

    public Lexer() {}

    public List<Token> tokenize(String input) {
        this.pos = 0;
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char currentChar = input.charAt(pos);
            if (Character.isWhitespace(currentChar)) {
                pos++;
            } else if (Character.isDigit(currentChar) || currentChar == '.') {
                tokens.add(readNumber(input));
            } else if (Character.isLetter(currentChar)) {
                tokens.add(readIdentifier(input));
            } else {
                // Mehrdeutige Zeichen behandeln
                Token token = readOperator(input);
                if (token != null) {
                    tokens.add(token);
                } else {
                    switch (currentChar) {
                        case '+': tokens.add(new Token(TOKEN_TYPE.PLUS, "+")); break;
                        case '-': tokens.add(new Token(TOKEN_TYPE.MINUS, "-")); break;
                        case '¯': tokens.add(new Token(TOKEN_TYPE.UNARYMINUS, "¯")); break;
                        case '*': tokens.add(new Token(TOKEN_TYPE.MULTIPLY, "*")); break;
                        case '/': tokens.add(new Token(TOKEN_TYPE.DIVIDE, "/")); break;
                        case '(': tokens.add(new Token(TOKEN_TYPE.OPENPARENTHESIS, "(")); break;
                        case ')': tokens.add(new Token(TOKEN_TYPE.CLOSEPARENTHESIS, ")")); break;
                        case '^': tokens.add(new Token(TOKEN_TYPE.EXPONENT, "^")); break;
                        case ',': tokens.add(new Token(TOKEN_TYPE.COMMA, ",")); break;
                        case '?': tokens.add(new Token(TOKEN_TYPE.QUESTION, "?")); break;
                        case ':': tokens.add(new Token(TOKEN_TYPE.COLON, ":")); break;
                        default: throw new RuntimeException("Unbekanntes Zeichen: " + currentChar);
                    }
                    pos++;
                }
            }
        }
        tokens.add(new Token(TOKEN_TYPE.EOF, ""));
        return tokens;
    }

    private Token readOperator(String input) {
        char currentChar = input.charAt(pos);
        char nextChar = pos + 1 < input.length() ? input.charAt(pos + 1) : '\0';

        switch (currentChar) {
            case '>':
                if (nextChar == '=') {
                    pos += 2;
                    return new Token(TOKEN_TYPE.GTE, ">=");
                } else {
                    pos++;
                    return new Token(TOKEN_TYPE.GT, ">");
                }
            case '<':
                if (nextChar == '=') {
                    pos += 2;
                    return new Token(TOKEN_TYPE.LTE, "<=");
                } else {
                    pos++;
                    return new Token(TOKEN_TYPE.LT, "<");
                }
            case '=':
                if (nextChar == '=') {
                    pos += 2;
                    return new Token(TOKEN_TYPE.EQ, "==");
                } else {
                    throw new RuntimeException("Einzelnes '=' ist nicht erlaubt, verwenden Sie '=='");
                }
            case '!':
                if (nextChar == '=') {
                    pos += 2;
                    return new Token(TOKEN_TYPE.NEQ, "!=");
                } else {
                    pos++;
                    return new Token(TOKEN_TYPE.NOT, "!");
                }
            case '&':
                if (nextChar == '&') {
                    pos += 2;
                    return new Token(TOKEN_TYPE.AND, "&&");
                } else {
                    throw new RuntimeException("Einzelnes '&' ist nicht erlaubt, verwenden Sie '&&'");
                }
            case '|':
                if (nextChar == '|') {
                    pos += 2;
                    return new Token(TOKEN_TYPE.OR, "||");
                } else {
                    throw new RuntimeException("Einzelnes '|' ist nicht erlaubt, verwenden Sie '||'");
                }
            default:
                return null; // Kein mehrdeutiger Operator
        }
    }

    private Token readNumber(String input) {
        int start = pos;
        while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) pos++;
        return new Token(TOKEN_TYPE.NUMBER, input.substring(start, pos));
    }

    private Token readIdentifier(String input) {
        int start = pos;
        while (pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) pos++;
        String text = input.substring(start, pos);

        if (text.matches("sin|cos|tan|log|ln|sqrt|abs")) {
            return new Token(TOKEN_TYPE.FUNCTION, text);
        } else {
            return new Token(TOKEN_TYPE.IDENTIFIER, text);
        }
    }
}