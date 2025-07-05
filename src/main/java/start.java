import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import functionplotter.ast.AST;
import functionplotter.ast.ASTNodeI;
import functionplotter.ast.VariableNode;
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
import lvp.views.Turtle;

void main() throws ParseException {

    // Variables for user interactions

    // Display range

    String xMin = "0"; // X-Achse Minimum
    String xMax = "100"; // X-Achse Maximum
    String yMin = "-5"; // Y-Achse Minimum
    String yMax = "5"; // Y-Achse Maximum


    XYRange xyRange = new XYRange(
        Parser.parse(xMin).evaluate(),
        Parser.parse(xMax).evaluate(),
        Parser.parse(yMin).evaluate(),
        Parser.parse(yMax).evaluate()
    );

    // Expressions

    String func_1 = ""; // Funktion 1
    String func_2 = "log (x)"; // Funktion 2
    String func_3 = ""; // Funktion 3
    String func_4 = "sin(x)"; // Funktion 4
    String func_5 = ""; // Funktion 5

    String scalingFunction = "x"; // Skalier Funktion

// tan(sin(x^2)+cos(x))*log(¯10*x)+sin(cos(x^3))
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

    String var_1 = "tan (x)"; // Variable a
    String var_2 = ""; // Variable b
    String var_3 = ""; // Variable c
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

    boolean logScale = false; // Logarithmisch
    boolean trigScale = true; // Trigonometrisch

    SCALINGS scale = scaleHandler(logScale, trigScale);

    boolean useSmartRange = true; // Smart Range

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
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Funktion 2", "String func_2 = \"$\";", func_2 == null ? "" : func_2));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Funktion 3", "String func_3 = \"$\";", func_3 == null ? "" : func_3));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Funktion 4", "String func_4 = \"$\";", func_4 == null ? "" : func_4));
    Clerk.write(Interaction.input("./src/main/java/start.java", "// Funktion 5", "String func_5 = \"$\";", func_5 == null ? "" : func_5));

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

    StringBuilder outPutString = new StringBuilder();
    outPutString.append(
            Plotter.plot(
                    xyRange,
                    new OutPutDimension(1000,700),
                    Parser.parse(scalingFunction),
                    scale,
                    useSmartRange,
                    expressionsAsColoredNodes
            )
    );

    Clerk.markdown("""
        ### Anzeige
        """);

    Clerk.markdown(
            outPutString.toString()
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
        ### Dot Graph Darstellungen
        """);

    Clerk.markdown("""
        ### 1. Funktion:
        """);
    Dot dotGraphFunc_1 = new Dot();
    dotGraphFunc_1.draw(expressionsAsColoredNodes[0].ast().toDotGraph());

    Clerk.markdown("""
        ### 2. Funktion:
        """);
    Dot dotGraphFunc_2 = new Dot();
    dotGraphFunc_2.draw(expressionsAsColoredNodes[1].ast().toDotGraph());

    Clerk.markdown("""
        ### 3. Funktion:
        """);
    Dot dotGraphFunc_3 = new Dot();
    dotGraphFunc_3.draw(expressionsAsColoredNodes[2].ast().toDotGraph());

    Clerk.markdown("""
        ### 4. Funktion:
        """);
    Dot dotGraphFunc_4 = new Dot();
    dotGraphFunc_4.draw(expressionsAsColoredNodes[3].ast().toDotGraph());

    Clerk.markdown("""
        ### 5. Funktion:
        """);
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
        Geben Sie eine Variable ein, die kann in Form eines simplen Wertes (z. B. `10, -4.2`), eines arithmetischen Ausdrucks (z. B. `sqrt(x), log(69)`) oder in Form von vordefinierten Konstanten (z. B. `e, pi`) sein.
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

    String inputExprWithVar = "x sin"; // Ausdruck mit Variable
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

    String xMinTutorial = "- 4 * pi"; // X-Achse Min
    String xMaxTutorial = "4 * pi"; // X-Achse Max
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
    Boolean trigScaleTutorial = true; // Trigonometrische Skalierung

    SCALINGS scaleTutorial = scaleHandler(logScaleTutorial, trigScaleTutorial);
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
        Mit der Angabe von `Smart Range` lässt sich einstellen, ob der manuel eingegebene Bereich verwendet wird, oder ob das Programm selbst einen Bereich ermitteln soll
        """);

    Boolean useSmartRangeTutorial = true; // Use Smart Range

    Clerk.write(Interaction.checkbox(
        "./src/main/java/start.java", "// Use Smart Range",
        "Boolean useSmartRangeTutorial = $;",
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

    // Div: Extensions
    Clerk.markdown("""
        ## 5. Erweiterungen (optional)
        - [x] Mehrere Funktionen gleichzeitig plotten
        - [x] Parameter einstellbar machen
        - [x] Bereichsauswahl, Zoom, etc.
        - [x] Logarithmische Achsen, (Benutzerdefinierte Skalierung)
        - [x] Automatische Bereichsauswahl
        - [ ] Logische Ausdrücke (z.B. ternärer Operator)
        """);
}

private SCALINGS scaleHandler(boolean logScale, boolean trigScale) {
    if (logScale && trigScale) {
        System.out.println("Nur eine Skalierung kann gewählt werden");
        return SCALINGS.NONE;
    } else if (logScale) return SCALINGS.LOGARITHMIC;
    else if (trigScale) return SCALINGS.TRIGONOMETRIC;
    else return SCALINGS.NONE;
}
