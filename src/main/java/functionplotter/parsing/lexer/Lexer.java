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
                    default: throw new RuntimeException("Unbekanntes Zeichen: " + currentChar);
                }
                pos++;
            }
        }
        tokens.add(new Token(TOKEN_TYPE.EOF, ""));
        return tokens;
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

        if (text.matches("sin|cos|tan|log|ln|sqrt")) {
            return new Token(TOKEN_TYPE.FUNCTION, text);
        } else {
            return new Token(TOKEN_TYPE.IDENTIFIER, text);
        }
    }
}