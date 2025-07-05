package functionplotter.utils;

public enum TOKEN_TYPE {
    NUMBER,                                             // Numeric literals (integers, floats)
    IDENTIFIER,                                         // Identifiers (variable names)
    FUNCTION, UNARYMINUS,                               // Native functions like sin, cos, log, sqrt etc.
    PLUS, MINUS, MULTIPLY, DIVIDE, EXPONENT,            // Arithmetic operators
    OPENPARENTHESIS, CLOSEPARENTHESIS,                  // Parentheses
    COMMA,                                              // Comma for function arguments
    AND, OR, NOT,                                       // Boolean Logic
    GT, LT, GTE, LTE, EQ, NEQ,                          // Boolean comparisons
    QUESTION, COLON,                                    // For the ternary operator
    EOF                                                 // End of file token
}