import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import functionplotter.parsing.parser.Parser;
import functionplotter.plotting.Plotter;
import functionplotter.plotting.utils.ColoredNode;
import functionplotter.plotting.utils.OutPutDimension;
import functionplotter.plotting.utils.XYRange;
import functionplotter.plotting.utils.XYRangeRecommender;
import functionplotter.utils.*;
import lvp.Clerk;
import lvp.skills.Text;
import lvp.skills.Interaction;
import lvp.views.Dot;

void main() throws ParseException {

    // Variables for user interactions

    // Display range

    String xMin = "0"; // X-Achse Minimum
    String xMax = "10 * pi"; // X-Achse Maximum
    String yMin = "-5"; // Y-Achse Minimum
    String yMax = "5"; // Y-Achse Maximum

    XYRange xyRange = new XYRange(
        Parser.parse(xMin).evaluate(),
        Parser.parse(xMax).evaluate(),
        Parser.parse(yMin).evaluate(),
        Parser.parse(yMax).evaluate()
    );

    // Expressions

    String complexExpression = "!(x > 0 && a < 10) || (sin(x^2) >= cos(a/2) ? log(x+1, 2) * sqrt(abs(a)) : tan(x) + ln(a)) && (b <= 5 || c != 3) ? (¯x + a*2)^3 / (4 - b) : (x >= a ? sin(x)*cos(a) : log(x) + sqrt(a^2 + 1))";
    String func_1 = "a ? b : c"; // Funktion 1
    String func_2 = ""; // Funktion 2
    String func_3 = ""; // Funktion 3
    String func_4 = ""; // Funktion 4
    String func_5 = ""; // Funktion 5

    String scalingFunction = "x"; // Skalier Funktion

    ArrayList<String> expressions = new ArrayList<>(
            List.of(
                    func_1,
                    func_2,
                    func_3,
                    func_4,
                    func_5
            )
    );

    // Variables

    String var_1 = "x % (2 * pi) < pi"; // Variable a
    String var_2 = "sin(x)"; // Variable b
    String var_3 = "tan(x)"; // Variable c
    String var_4 = ""; // Variable d
    String var_5 = ""; // Variable e

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

    boolean logScale = true; // Logarithmisch
    boolean trigScale = false; // Trigonometrisch

    SCALING scale = scaleHandler(logScale, trigScale);

    boolean useSmartRange = false; // Smart Range

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

    ColoredNode[] expressionsAsColoredNodes = expressions.stream().map(expr -> {
        try {
            return new ColoredNode(
                    Parser.parse(expr),
                    ColorPicker.getNextColor()
            );
        } catch (ParseException e) {
            throw new RuntimeException("Fehler beim Parsen des Ausdrucks: " + expr, e);
        }
    }).toArray(ColoredNode[]::new);

    Clerk.write(Interaction.input("./src/main/java/start.java", "// Funktion 1", "String func_1 = \"$\";", func_1 == null ? "" : func_1));
    Clerk.markdown(Text.fillOut("""
        ${0}""", expressionsAsColoredNodes[0].ast().hasVar("x") || expressionsAsColoredNodes[0].ast().isEmpty() ? "" : expressionsAsColoredNodes[0].ast().evaluate()));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Funktion 2", "String func_2 = \"$\";", func_2 == null ? "" : func_2));
    Clerk.markdown(Text.fillOut("""
        ${0}""", expressionsAsColoredNodes[1].ast().hasVar("x") || expressionsAsColoredNodes[1].ast().isEmpty() ? "" : expressionsAsColoredNodes[1].ast().evaluate()));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Funktion 3", "String func_3 = \"$\";", func_3 == null ? "" : func_3));
    Clerk.markdown(Text.fillOut("""
        ${0}""", expressionsAsColoredNodes[2].ast().hasVar("x") || expressionsAsColoredNodes[2].ast().isEmpty() ? "" : expressionsAsColoredNodes[2].ast().evaluate()));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Funktion 4", "String func_4 = \"$\";", func_4 == null ? "" : func_4));
    Clerk.markdown(Text.fillOut("""
        ${0}""", expressionsAsColoredNodes[3].ast().hasVar("x") || expressionsAsColoredNodes[3].ast().isEmpty() ? "" : expressionsAsColoredNodes[3].ast().evaluate()));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Funktion 5", "String func_5 = \"$\";", func_5 == null ? "" : func_5));
    Clerk.markdown(Text.fillOut("""
        ${0}""", expressionsAsColoredNodes[4].ast().hasVar("x") || expressionsAsColoredNodes[4].ast().isEmpty() ? "" : expressionsAsColoredNodes[4].ast().evaluate()));

    // Variables

    Clerk.markdown("""
        ### Variablen
        """);

    Clerk.write(Interaction.input("./src/main/java/start.java", "// Variable a", "String var_1 = \"$\";", var_1 == null ? "" : var_1));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Variable b", "String var_2 = \"$\";", var_2 == null ? "" : var_2));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Variable c", "String var_3 = \"$\";", var_3 == null ? "" : var_3));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Variable d", "String var_4 = \"$\";", var_4 == null ? "" : var_4));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Variable e", "String var_5 = \"$\";", var_5 == null ? "" : var_5));

    // Plot

    Clerk.markdown("""
        ### Anzeige
        """);

    Clerk.markdown(
        Plotter.plot(
            xyRange,
            new OutPutDimension(1000, 700),
            Parser.parse(scalingFunction),
            scale,
            useSmartRange,
            expressionsAsColoredNodes
        )
    );

    // Settings

    Clerk.markdown("""
        ### Einstellungen
        """);


    Clerk.write(Interaction.input("./src/main/java/start.java", "// X-Achse Minimum", "String xMin = \"$\";", xMin));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// X-Achse Maximum", "String xMax = \"$\";", xMax));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Y-Achse Minimum", "String yMin = \"$\";", yMin));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Y-Achse Maximum", "String yMax = \"$\";", yMax));

    Clerk.markdown("""
        ##
        """);

    Clerk.write(Interaction.input("./src/main/java/start.java", "// Skalier Funktion", "String scalingFunction = \"$\";", "x"));

    Clerk.markdown("""
        ##
        """);

    Clerk.write(Interaction.checkbox("./src/main/java/start.java", "// Logarithmisch", "boolean logScale = $;", logScale));
    Clerk.write(Interaction.checkbox("./src/main/java/start.java", "// Trigonometrisch", "boolean trigScale = $;", trigScale));
    Clerk.write(Interaction.checkbox("./src/main/java/start.java", "// Smart Range", "boolean useSmartRange = $;", useSmartRange));

    // Dot Graph

    Clerk.markdown("""
        ### Dot Graph Darstellungen, Infix- und RPN-Darstellung
        """);

    Clerk.markdown(Text.fillOut("""
        ### 1. Funktion:
        - Infix: `${0}`
        - RPN:   `${1}`
        """, expressionsAsColoredNodes[0].ast().toStringInfix(), expressionsAsColoredNodes[0].ast().toStringRPN()
    ));
    Dot dotGraphFunc_1 = new Dot();
    dotGraphFunc_1.draw(expressionsAsColoredNodes[0].ast().toDotGraph());

    Clerk.markdown(Text.fillOut("""
        ### 2. Funktion:
        - Infix: `${0}`
        - RPN:   `${1}`
        """, expressionsAsColoredNodes[1].ast().toStringInfix(), expressionsAsColoredNodes[1].ast().toStringRPN()
    ));
    Dot dotGraphFunc_2 = new Dot();
    dotGraphFunc_2.draw(expressionsAsColoredNodes[1].ast().toDotGraph());

    Clerk.markdown(Text.fillOut("""
        ### 3. Funktion:
        - Infix: `${0}`
        - RPN:   `${1}`
        """, expressionsAsColoredNodes[2].ast().toStringInfix(), expressionsAsColoredNodes[2].ast().toStringRPN()
    ));
    Dot dotGraphFunc_3 = new Dot();
    dotGraphFunc_3.draw(expressionsAsColoredNodes[2].ast().toDotGraph());

    Clerk.markdown(Text.fillOut("""
        ### 4. Funktion:
        - Infix: `${0}`
        - RPN:   `${1}`
        """, expressionsAsColoredNodes[3].ast().toStringInfix(), expressionsAsColoredNodes[3].ast().toStringRPN()
    ));
    Dot dotGraphFunc_4 = new Dot();
    dotGraphFunc_4.draw(expressionsAsColoredNodes[3].ast().toDotGraph());

    Clerk.markdown(Text.fillOut("""
        ### 5. Funktion:
        - Infix: `${0}`
        - RPN:   `${1}`
        """, expressionsAsColoredNodes[4].ast().toStringInfix(), expressionsAsColoredNodes[4].ast().toStringRPN()
    ));
    Dot dotGraphFunc_5 = new Dot();
    dotGraphFunc_5.draw(expressionsAsColoredNodes[4].ast().toDotGraph());

    // Div: Input
    Clerk.markdown("""
        ## 1. Eingabe
        """);
    Clerk.markdown("""
        ### 1.2 Eingabe eines arithmetischen Ausdrucks
        Geben Sie einen Ausdruck in Infix- oder UPN-Notation ein (z. B. `sin(x) + 2` oder `x sin 2 +`).
        """);

    // Expression Input
    String inputExpr = "sin(x)";
    Clerk.write(
            Interaction.input(
                    "./src/main/java/start.java", "// Ausdruck",
                    "String inputExpr = \"$\";",
                    inputExpr == null ? "Geben Sie einen Ausdruck ein" : inputExpr
            )
    );


    Clerk.markdown("""
        ### 1.2 Eingabe einer Variable
        Geben Sie eine Variable, in Form eines simplen Wertes (z. B. `10, -4.2`), eines arithmetischen Ausdrucks (z. B. `sqrt(x), log(69)`) oder in Form von vordefinierten Konstanten (z. B. `e, pi`) ein.
        """);

    // Variable Input
    String inputVar = "x cos";
    GlobalContext.VARIABLES.add(
            new Variable(
                    "v", Parser.parse(inputVar)
            )
    );

    Clerk.write(
            Interaction.input(
                    "./src/main/java/start.java", "// Variable v",
                    "String inputVar = \"$\";",
                    inputVar.equals("") ? "Geben Sie eine Variable ein" : inputVar
            )
    );

    Clerk.markdown("""
        Diese Variable lässt sich nun in den Ausdrücken verwenden unter ihrem angegebenen Identifier (hier `v`).
        """);

    String inputExprWithVar = "e^x"; // Ausdruck mit Variable
    Clerk.write(
            Interaction.input(
                    "./src/main/java/start.java", "// Ausdruck mit Variable",
                    "String inputExprWithVar = \"$\";",
                    inputExprWithVar.equals("") ? "Geben Sie einen Ausdruck ein" : inputExprWithVar
            )
    );
    // Div: Display AST as Dot-Graph
    Clerk.markdown("""
        ## 2. Abstrakter Syntaxbaum (AST)
        Der eingegebene Ausdruck wird als AST (DOT-Graph) dargestellt.
        """);

    // AST Example
    Dot astDot = new Dot();
    astDot.draw(
        Parser.parse(inputExprWithVar).toDotGraph()
    );

    // Div: Infix & UPN
    Clerk.markdown("""
        ## 3. Anzeigeeinstellungen
        ### 3.1 Werte- & Definitions-Bereich
        Mit den Folgenden Eingabefeldern lassen sich Werte und Definitions bereich der zu plottenden Ausdrücke manuell einstellen
        """);

    String xMinTutorial = "-5"; // X-Achse Min
    String xMaxTutorial = "5"; // X-Achse Max
    String yMinTutorial = "-10"; // Y-Achse Min
    String yMaxTutorial = "10"; // Y-Achse Max

    Clerk.write(Interaction.input(
            "./src/main/java/start.java", "// X-Achse Min",
            "String xMinTutorial = \"$\";",
            "" + xMinTutorial));
    Clerk.write(Interaction.input(
            "./src/main/java/start.java", "// X-Achse Max",
            "String xMaxTutorial = \"$\";",
            "" + xMaxTutorial));
    Clerk.write(Interaction.input(
            "./src/main/java/start.java", "// Y-Achse Min",
            "String yMinTutorial = \"$\";",
            "" + yMinTutorial));
    Clerk.write(Interaction.input(
            "./src/main/java/start.java", "// Y-Achse Max",
            "String yMaxTutorial = \"$\";",
            "" + yMaxTutorial));

    Clerk.markdown("""
        ### 3.2 Skalierung
        Es ist außerdem möglich eine Funktion in Form eines arithmetischen Ausdrucks anzugeben, welche die Skalierung der X-Achse definiert, der eingegebene Ausdruck agiert wie ein mapping (`x -> f(x)`).
        
        Bsp.:
        
        - Um **keine** Skalierung vorzunehmen, so lässt man den Standardwert `x` einfach stehen.
        
        - Um beispielsweise eine logarithmische Skalierung zu bewirken, so würde man die Umkehrfunktion des Logarithmus' angeben: `10^x`.
        
        Hier ist es einem komplett freigestellt wie kreativ man mit der Skalierung sein möchte, 
        grundlegend wird hier jeder Ausdruck funktionieren *solange er ein `x` enthält*, 
        wie nützlich dies im Endeffekt ist, sei jedem selbst überlassen. 
        Beispielsweise ist es möglich Kreisfunktionen als Skalierfunktion anzugeben, was natürlich wenig Sinn macht.
        Dementsprechend wird der Definitionsbereich auch automatisch umgestellt,
        da es in manchen Fällen unmöglich ist Nutzereingaben zu übernehmen. 
        Zum Beispiel lassen sich für eine Skalierungsfunktion wie `sin(x)` keine x Werte plotten, 
        welche größer als `1` oder kleiner als `-1` sind.
        Aus diesem Grund werden vorgefertigte Skalierungen wie `logarithmisch` und Skalierungen für `Kreisfunktionen` bereitgestellt.
        """);
    String scalingFunctionTutorial = "x"; // Skalier Funktion Beispiel
    Clerk.write(Interaction.input(
            "./src/main/java/start.java", "// Skalier Funktion Beispiel",
            "String scalingFunctionTutorial = \"$\";",
            scalingFunctionTutorial.equals("") ? "x" : scalingFunctionTutorial));

    boolean logScaleTutorial = false; // Logarithmische Skalierung
    Boolean trigScaleTutorial = false; // Trigonometrische Skalierung

    SCALING scaleTutorial = scaleHandler(logScaleTutorial, trigScaleTutorial);
    Clerk.write(Interaction.checkbox(
        "./src/main/java/start.java", "// Logarithmische Skalierung",
        "Boolean logScaleTutorial = $;",
        logScaleTutorial
    ));
    Clerk.write(Interaction.checkbox(
        "./src/main/java/start.java", "// Trigonometrische Skalierung",
        "Boolean trigScaleTutorial = $;",
        trigScaleTutorial
    ));

    Clerk.markdown("""
        ### 3.3 Automatischer Werte- und Definitions-Bereich
        Mit der Angabe von `Smart Range` lässt sich einstellen, ob der manuel eingegebene Bereich verwendet wird, oder ob das Programm selbst einen Bereich ermitteln soll.
        """);

    boolean useSmartRangeTutorial = false; // Use Smart Range

    Clerk.write(Interaction.checkbox(
        "./src/main/java/start.java", "// Use Smart Range",
        "boolean useSmartRangeTutorial = $;",
        useSmartRangeTutorial
    ));
    // Div: Display
    Clerk.markdown(Text.fillOut("""
        ## 4. Funktionsplot im Koordinatensystem
        Die Funktion wird im Bereich x = `${0}` bis x = `${1}` geplottet.
        """, useSmartRangeTutorial
                ? XYRangeRecommender.recommendRange(Parser.parse(inputExprWithVar)).xMin()
                : xMinTutorial,
            useSmartRangeTutorial
                ? XYRangeRecommender.recommendRange(Parser.parse(inputExprWithVar)).xMax()
                : xMaxTutorial
    ));

    // Plotter Output
    Clerk.markdown(
            Plotter.plot(
                    new XYRange(
                            Parser.parse(xMinTutorial).evaluate(),
                            Parser.parse(xMaxTutorial).evaluate(),
                            Parser.parse(yMinTutorial).evaluate(),
                            Parser.parse(yMaxTutorial).evaluate()
                    ),
                    new OutPutDimension(1000, 700),
                    Parser.parse(scalingFunctionTutorial),
                    scaleTutorial,
                    useSmartRangeTutorial,
                    new ColoredNode(
                            Parser.parse(inputExprWithVar),
                            ColorPicker.getNextColor()
                    )
            )
    );

    // Logical Expressions

    Clerk.markdown("""
        ## 5. Logische Ausdrücke
        Um logische Ausdrücke mit den vorhandenen arithmetischen Ausdrücken kompatibel zu machen und sinnvoll darstellen zu können, muss zuerst definiert werden wann ein arithmetischer Ausdruck `true` oder `false` ist.
        In diesem Fall habe ich mich dazu entschieden alle `positiven Werte` als `true` und alle `negativen Werte und 0` als `false` zu behandeln.
        So ist sichergestellt, dass alle Ausdrücke sowohl als *arithmetisch*, als auch als *logisch* behandelt werden können.
        UMgekehrt werden logische Vergleiche stets einen `Wahrheitswert ∈ {0, 1}` zurückgeben.
        ### 5.1 Unterstützte Operanden
        Es werden alle grundlegenden logischen Operationen unterstützt:
        - **`>, >=`**
            - `größer, größer oder gleich`
        - **`<, <=`**
            - `kleiner, kleiner oder gleich`
        - **`==, !=`**
            - `gleich, nicht gleich`
        - **`!, ||, &&`**
            - `nicht, oder, und`
        - **`? :`**
            - `ternärer Operator`
        ### 5.2 Beispiel
        Zur übersichtlicheren Gestaltung und Demonstrationszwecken benutzen wir Variablen
        """);

    String booleanExpressionTutorial = "f ? g : h"; // Logischer Ausdruck

    Clerk.write(Interaction.input(
        "./src/main/java/start.java", "// Logischer Ausdruck",
        "String booleanExpressionTutorial = \"$\";",
        booleanExpressionTutorial
    ));


    String booleanVarTutorial_1 = "x < 0"; // Logische Variable f
    String booleanVarTutorial_2 = "tan(x)"; // Logische Variable g
    String booleanVarTutorial_3 = "x sin"; // Logische Variable h

    Clerk.markdown("""
        ##
        """);

    GlobalContext.VARIABLES.add(
            new Variable(
                    "f",
                    Parser.parse(booleanVarTutorial_1)
            ),
            new Variable(
                    "g",
                    Parser.parse(booleanVarTutorial_2)
            ),
            new Variable(
                    "h",
                    Parser.parse(booleanVarTutorial_3)
            )
    );
    Clerk.write(Interaction.input(
            "./src/main/java/start.java", "// Logische Variable f",
            "String booleanVarTutorial_1 = \"$\";",
            booleanVarTutorial_1
    ));
    Clerk.write(Interaction.input(
            "./src/main/java/start.java", "// Logische Variable g",
            "String booleanVarTutorial_2 = \"$\";",
            booleanVarTutorial_2
    ));
    Clerk.write(Interaction.input(
            "./src/main/java/start.java", "// Logische Variable h",
            "String booleanVarTutorial_3 = \"$\";",
            booleanVarTutorial_3
    ));

    Clerk.markdown("""
        Der Ausdruck ist in diesem Beispiel der ternäre Operator, für welchen die Kondition die Variable `f = x < 0` darstellt.<br>
        Wenn diese Kondition erfüllt ist, soll also `g = tan(x)` geplottet werden und für alle anderen Werte `h = sin(x)`
        ##
        """);

    Clerk.markdown(
        Plotter.plot(
            new XYRange(
                Parser.parse("-3*pi").evaluate(),Parser.parse("3*pi").evaluate(),
                -5,5
            ),
            new OutPutDimension(1000, 700),
            Parser.parse("x"),
            SCALING.TRIGONOMETRIC,
            false,
            new ColoredNode(
                Parser.parse(booleanExpressionTutorial),
                ColorPicker.getNextColor()
            )
        )
    );

    // Div: Extensions
    Clerk.markdown("""
        ## 5. Erweiterungen (optional)
        - [x] Mehrere Funktionen gleichzeitig plotten
        - [x] Parameter einstellbar machen
        - [x] Bereichsauswahl, Zoom, etc.
        - [x] Logarithmische Achsen, (Benutzerdefinierte Skalierung)
        - [x] Automatische Bereichsauswahl
        - [x] Logische Ausdrücke (z.B. ternärer Operator)
        """);

}

private SCALING scaleHandler(boolean logScale, boolean trigScale) {
    if (logScale && trigScale) {
        System.out.println("Nur eine Skalierung kann gewählt werden");
        return SCALING.NONE;
    } else if (logScale) return SCALING.LOGARITHMIC;
    else if (trigScale) return SCALING.TRIGONOMETRIC;
    else return SCALING.NONE;
}
