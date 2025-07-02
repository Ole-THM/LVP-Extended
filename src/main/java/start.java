import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import functionplotter.parsing.parser.Parser;
import functionplotter.plotting.Plotter;
import functionplotter.plotting.utils.ColoredNode;
import functionplotter.utils.COLOR;
import functionplotter.utils.ColorPicker;
import functionplotter.utils.Variable;
import lvp.Clerk;
import lvp.skills.Text;
import lvp.skills.Interaction;
import lvp.views.Dot;
import lvp.views.Turtle;

import functionplotter.utils.GlobalContext;

void main() throws ParseException {

    // Titel und Einleitung
    Clerk.markdown("""
        # Funktionsplotter – Demo
        **Prüfungsleistung Entwicklungsprojekt SoSe 2025**
        """);

    // Div: Application

    Clerk.markdown("""
        ## Applikation
        """);

    // Functions

    Clerk.markdown("""
        ### Funktionen
        """);

    String func_1 = "x  2 + sin"; // function 1
    String func_2 = "sin(x) + 2"; // function 2
    String func_3 = "log(10,x)"; // function 3
    String func_4 = ""; // function 4
    String func_5 = ""; // function 5

    ArrayList<String> expressions = new ArrayList<>(
            List.of(
                    func_1,
                    func_2,
                    func_3,
                    func_4,
                    func_5
            )
    );

    GlobalContext.EXPRESSIONS = expressions.stream().map(expr -> {
        try {
            return new ColoredNode(
                    Parser.parse(expr),
                    ColorPicker.getNextColor()
            );
        } catch (ParseException e) {
            throw new RuntimeException("Fehler beim Parsen des Ausdrucks: " + expr, e);
        }
    }).toArray(ColoredNode[]::new);

    Clerk.write(Interaction.input("./start.java", "// function 1", "String func_1 = \"$\";", func_1 == null ? "" : func_1));
    Clerk.write(Interaction.input("./start.java", "// function 2", "String func_2 = \"$\";", func_2 == null ? "" : func_2));
    Clerk.write(Interaction.input("./start.java", "// function 3", "String func_3 = \"$\";", func_3 == null ? "" : func_3));
    Clerk.write(Interaction.input("./start.java", "// function 4", "String func_4 = \"$\";", func_4 == null ? "" : func_4));
    Clerk.write(Interaction.input("./start.java", "// function 5", "String func_5 = \"$\";", func_5 == null ? "" : func_5));

    // Variables

    Clerk.markdown("""
        ### Variablen
        """);

    String var_1 = "x log"; // Variable a
    String var_2 = "2.0"; // Variable b
    String var_3 = ""; // Variable c
    String var_4 = ""; // Variable d
    String var_5 = ""; // Variable e

    Clerk.write(Interaction.input("./start.java", "// Variable a", "String var_1 = \"$\";", var_1 == null ? "" : var_1));
    Clerk.write(Interaction.input("./start.java", "// Variable b", "String var_2 = \"$\";", var_2 == null ? "" : var_2));
    Clerk.write(Interaction.input("./start.java", "// Variable c", "String var_3 = \"$\";", var_3 == null ? "" : var_3));
    Clerk.write(Interaction.input("./start.java", "// Variable d", "String var_4 = \"$\";", var_4 == null ? "" : var_4));
    Clerk.write(Interaction.input("./start.java", "// Variable e", "String var_5 = \"$\";", var_5 == null ? "" : var_5));

    GlobalContext.VARIABLES.add(
            new Variable(
                    "a",
                    Parser.parse(var_1)
            ),
            new Variable(
                    "b",
                    Parser.parse(var_2)
            ),
            new Variable(
                    "c",
                    Parser.parse(var_3)
            ),
            new Variable(
                    "d",
                    Parser.parse(var_4)
            ),
            new Variable(
                    "e",
                    Parser.parse(var_5)
            )
    );


    // Plot

    GlobalContext.OUTPUT_STRING.append(
            Plotter.plot(
                    GlobalContext.EXPRESSIONS
            )
    );

    Clerk.markdown("""
        ### Anzeige
        """);

    Clerk.markdown(
            GlobalContext.OUTPUT_STRING.toString()
    );



    // Div: Input
    Clerk.markdown("""
        ## 1. Eingabe eines arithmetischen Ausdrucks
        Geben Sie einen Ausdruck in Infix- oder UPN-Notation ein (z. B. `sin(x) + 2` oder `x sin 2 +`).
        """);

    // Expression Input
    String inputExpr = "sin(x) + 2";
    Clerk.write(
            Interaction.input(
                    "./start.java", "// UserExpression",
                    "String inputExpr = \"$\";",
                    inputExpr == null ? "Geben Sie den Ausdruck ein" : inputExpr
            )
    );

    // Abschnitt: Anzeige AST als DOT-Graph
    Clerk.markdown("""
        ## 2. Abstrakter Syntaxbaum (AST)
        Der eingegebene Ausdruck wird als AST (DOT-Graph) dargestellt.
        """);

    // Platzhalter für AST-Graph (hier ein Beispiel, später durch echten Parser ersetzen)
    Dot astDot = new Dot();
    astDot.draw("""
        digraph AST {
            "+" [label="+"];
            "sin" [label="sin"];
            "x" [label="x"];
            "2" [label="2"];
            "+" -> "sin";
            "+" -> "2";
            "sin" -> "x";
        }
        """);

    // Abschnitt: Anzeige Infix & UPN
    Clerk.markdown("""
        ## 3. Ausdruck in Infix- und UPN-Notation
        """);
    String infix = "sin(x) + 2"; // Platzhalter
    String upn   = "x sin 2 +";  // Platzhalter
    Clerk.markdown(Text.fillOut("""
        **Infix:** `${0}`
        **UPN:** `${1}`
        """, infix, upn));

    // Abschnitt: Funktionsplot SVG
    Clerk.markdown("""
        ## 4. Funktionsplot im Koordinatensystem
        Die Funktion wird im Bereich x = -5 bis x = 5 geplottet.
        """
    );

    // Platzhalter für Plot (hier ein Beispiel mit Turtle, später durch echten Plot ersetzen)
    // Du kannst später einen eigenen SVG-Plotter oder die LVP-Plotter-Klasse verwenden
    Clerk.markdown(
            Plotter.plot(
                    new ColoredNode(
                            Parser.parse(inputExpr),
                            ColorPicker.getNextColor()
                    )
            )
    );

    // Abschnitt: Erweiterungen (Platzhalter)
    Clerk.markdown("""
        ## 5. Erweiterungen (optional)
        - [ ] Mehrere Funktionen gleichzeitig plotten
        - [ ] Parameter einstellbar machen
        - [ ] Bereichsauswahl, Zoom, etc.
        - [ ] Logarithmische Achsen
        - [ ] Logische Ausdrücke (z.B. ternärer Operator)
        """);

    // Abschnitt: Hinweise zur Umsetzung
    Clerk.markdown("""
        ---
        **Hinweis:**  
        Diese Datei ist als Outline gedacht.  
        Ersetzen Sie die Platzhalter durch Ihre eigene Logik (Tokenizer, Parser, AST, Plotter, etc.).
        """);
}

// Turtle triangle
void triangle(Turtle turtle, double size) {
    turtle.forward(size).right(60).backward(size).right(60).forward(size).right(60 + 180);
}

void drawing(Turtle turtle, double size) {
    for (int i = 1; i <= 18; i++) {
        turtle.color(255, i * 256 / 37, i * 256 / 37, 1); // turtle color
        turtle.width(1.0 - 1.0 / 36.0 * i);
        triangle(turtle, size + 1 - 2 * i);
        turtle.left(20).forward(5);
    }
}
// Turtle triangle