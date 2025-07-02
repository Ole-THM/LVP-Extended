package functionplotter.parsing.parser;

import functionplotter.ast.ASTNodeI;

import java.text.ParseException;

public interface ParserI {
    /**
     * Parses the input string and returns the root of the Abstract Syntax Tree (AST).
     *
     * @param input The input string to parse.
     * @return The root astNode of the AST.
     * @throws ParseException If there is an error during parsing.
     */
    ASTNodeI parse(String input) throws ParseException;

    boolean isValid(String input);
}