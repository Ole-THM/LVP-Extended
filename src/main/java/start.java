import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import functionplotter.ast.AST;
import functionplotter.ast.ValueNode;
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

void main() throws ParseException {

    // Variables for user interactions

    // Display range

    String xMin = "-2*pi"; // X-Achse Minimum
    String xMax = "2 * pi"; // X-Achse Maximum
    String yMin = "-5"; // Y-Achse Minimum
    String yMax = "10"; // Y-Achse Maximum

    XYRange xyRange;
    try {
        xyRange = new XYRange(
            Parser.parse(xMin).evaluate(),
            Parser.parse(xMax).evaluate(),
            Parser.parse(yMin).evaluate(),
            Parser.parse(yMax).evaluate()
        );
    } catch(ParseException e) {
        System.out.println("Fehler beim Parsen der XYRange: " + e.getMessage());
        xyRange = XYRange.DEFAULT_RANGE();
    }

    // Expressions

    String complexExpression = "!(x > 1 && a < 10) || (sin(x^2) >= cos(a/2) ? log(x+b+1, 2) * sqrt(abs(a+c)) : tan(x+d) + ln(a+d)) && (b <= 5 || c != 3) ? (¯x + e*2)^3 / (4 - b) : (x >= a ? sin(x)*cos(a) : log(x) + sqrt(a^2 + 1))";
    String complexExpression_2 = "!(x > 1) || (sin(x^2) >= cos((x+b)/2) ? log(x+c+1, 2) * sqrt(abs(x+d)) : tan(x+a) + ln(x+b)) && (x <= 5 || x != 3) ? (x + 2)^3 / (4 - x) : (x >= 2 ? sin(x)*cos(x) : log(x) + sqrt(x^2 + 1))";
    String complexExpression_3 = "!(x > 1 && a < 10) || (sin(x^2) >= cos(a/2) ? log(x+b+1, 2) * sqrt(abs(a+c)) : tan(x+d) + ln(a+d)) && (b <= 5 || c != 3) ? (¯x + e*2)^3 / (4 - b) : (x >= a ? sin(x)*cos(a) : log(x) + sqrt(a^2 + 1))";
    String complexExpression_4 = "!(x > 1) || (sin(x^2) >= cos((x+b)/2) ? log(x+c+1, 2) * sqrt(abs(x+d)) : tan(x+a) + ln(x+b)) && (x <= 5 || x != 3) ? (x + 2)^3 / (4 - x) : (x >= 2 ? sin(x)*cos(x) : log(x) + sqrt(x^2 + 1))";
    String complexExpression_5 = "max(sinh(a+b) + cosh(c-d) - tanh(e*x), min(asinh(a^2) * acosh(b+1), atanh(c-d) + log(abs(e)+2, 3))) + sqrt(abs(a*b-c*d+e*x)) - ln(max(a, b, c, d, e, x))";
    String complexExpression_6 = "sqrt(abs((a+b-c*d)/(e+x))) + log(min(a^2+b^2, c^2+d^2), 5) * sinh(x) - cosh(a-b) + tanh(c+d) + asinh(e-x) - acosh(a+b+c) + atanh(d-e) + ln(abs(x+a+b))";
    String complexExpression_7 = "min(max(a, b) + sinh(c) - cosh(d) + tanh(e), sqrt(abs(a*b-c*d+e*x)) + log(a+b+c+d+e+x, 7)) * (asinh(a) + acosh(b) - atanh(c) + ln(d+e+x))";
    String func_1 = complexExpression; //Funktion 1
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
    String exampleExpr_1 = "((sin(x) + x^2 > 0 ? sqrt(abs(x)) : log(x+2)) + (cosh(x) - E * sinh(x)) * (x > 5 ? max(sin(x), cos(x)) : min(tan(x), 2*x))) / (asinh(abs(x-3)) + 1)";
    String exampleExpr_2 = "((x < 0 || cos(x) >= 0.5 ? tan(x) : abs(x-3)) + (asin(x/10) * acosh(abs(x)+1)) - (x > 2 && x < 8 ? log(x+5, 2) : ln(abs(x)+3)))";
    String exampleExpr_3 = "x == 0 ? 1 : x > 0 ? (x^3 + sin(x) * cos(x) - PI * tanh(x) + sqrt(abs(x))) : (-x + asinh(x) - acosh(abs(x)+2) + atanh(x/10))";
    String exampleExpr_4 = "!(x <= 2) && ((x^2 - 4 > 0 ? x/2 : x*2) + (min(sin(x), cos(x)) * max(log(abs(x)+1, 3), ln(x+4)))) || (abs(x) > 10 ? sqrt(x^2+1) : tan(x) - atan(x))";
    String exampleExpr_5 = "x > 1 && x < 10 ? (sin(x) * log(x) + cosh(x) - sinh(x) + min(x^2, abs(x-5))) : (cos(x) + sqrt(abs(x)) + max(ln(x+2), asinh(x/2)))";

    String var_1 = exampleExpr_1; // Variable a
    String var_2 = exampleExpr_3; // Variable b
    String var_3 = exampleExpr_5; // Variable c
    String var_4 = exampleExpr_4; // Variable d
    String var_5 = exampleExpr_2; // Variable e

    AST var_1AST;
    AST var_2AST;
    AST var_3AST;
    AST var_4AST;
    AST var_5AST;

    try {
        var_1AST = Parser.parse(var_1);
        var_2AST = Parser.parse(var_2);
        var_3AST = Parser.parse(var_3);
        var_4AST = Parser.parse(var_4);
        var_5AST = Parser.parse(var_5);
        if (var_1AST.hasVar("a")) {
            System.out.println("Variablen dürfen sich nicht selbst enthalten: a = \"" + var_1 + "\"");
            var_1AST = new AST(new ValueNode(1));
            var_1 = "1"; // Fallback to a default value if variable contains itself
        }
        if (var_2AST.hasVar("b")) {
            System.out.println("Variablen dürfen sich nicht selbst enthalten: b = \"" + var_2 + "\"");
            var_2AST = new AST(new ValueNode(2));
            var_2 = "2"; // Fallback to a default value if variable contains itself
        }
        if (var_3AST.hasVar("c")) {
            System.out.println("Variablen dürfen sich nicht selbst enthalten: c = \"" + var_3 + "\"");
            var_3AST = new AST(new ValueNode(3));
            var_3 = "3"; // Fallback to a default value if variable contains itself
        }
        if (var_4AST.hasVar("d")) {
            System.out.println("Variablen dürfen sich nicht selbst enthalten: d = \"" + var_4 + "\"");
            var_4AST = new AST(new ValueNode(4));
            var_4 = "4"; // Fallback to a default value if variable contains itself
        }
        if (var_5AST.hasVar("e")) {
            System.out.println("Variablen dürfen sich nicht selbst enthalten: e = \"" + var_5 + "\"");
            var_5AST = new AST(new ValueNode(5));
            var_5 = "5"; // Fallback to a default value if variable contains itself
        }
    } catch (ParseException e) {
        System.out.println("Fehler beim Parsen der Variable a: " + e.getMessage());
        var_1AST = new AST(new ValueNode(1)); // Fallback to a default value if parsing fails
        var_2AST = new AST(new ValueNode(2));
        var_3AST = new AST(new ValueNode(3));
        var_4AST = new AST(new ValueNode(4));
        var_5AST = new AST(new ValueNode(5));
    }
    GlobalContext.VARIABLES.add(
            new Variable(
                    "a",
                    var_1AST
            ),
            new Variable(
                    "b",
                    var_2AST
            ),
            new Variable(
                    "c",
                    var_3AST
            ),
            new Variable(
                    "d",
                    var_4AST
            ),
            new Variable(
                    "e",
                    var_5AST
            )
    );

    boolean logScale = false; // Logarithmisch
    boolean trigScale = false; // Trigonometrisch

    SCALING scale = scaleHandler(logScale, trigScale);

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
            System.out.println("Fehler beim Parsen des Ausdrucks: " + e.getMessage());
            return new ColoredNode(
                    new AST(new ValueNode(0)), // Fallback to a default value if parsing fails
                    ColorPicker.getNextColor()
            );
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

    AST scalingAST = new AST(new VariableNode("x"));

    try {
        Parser.parse(scalingFunction.equals("") ? "x" : scalingFunction);
    } catch (ParseException e) {
        System.out.println("Fehler beim Parsen der Skalierfunktion: " + e.getMessage());
    }

    Clerk.markdown(
        Plotter.plot(
            PlottingConfig.getConfig(
                xyRange,
                new OutPutDimension(1000, 700),
                scalingAST,
                scale,
                useSmartRange,
                expressionsAsColoredNodes
            )
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
        # Anleitung
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
        **ACHTUNG:** Variablen können auch andere Variablen referenzieren, dies kann zu zirkulären Abhängigkeiten führen, was zu einem StackOverFlow führen wird.
        """);

    // Variable Input
    String inputVar = "x cos";
    AST inputVarAST;
    try {
        inputVarAST = Parser.parse(inputVar);
    } catch (ParseException e) {
        System.out.println("Fehler beim Parsen der Variable v: " + e.getMessage());
        inputVarAST = new AST(new ValueNode(1));
    }

    GlobalContext.VARIABLES.add(
            new Variable(
                    "v", inputVarAST
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
    AST inputExprWithVarAST;
    try {
        inputExprWithVarAST = Parser.parse(inputVar);
    } catch (ParseException e) {
        System.out.println("Fehler beim Parsen des Ausdrucks \"Ausdruck mit Variable\": " + e.getMessage());
        inputExprWithVarAST = new AST(new VariableNode("x"));
    }

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
        inputExprWithVarAST.toDotGraph()
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

    XYRange xyRangeTutorial;
    try {
        xyRangeTutorial = new XYRange(
                Parser.parse(xMinTutorial).evaluate(),
                Parser.parse(xMaxTutorial).evaluate(),
                Parser.parse(yMinTutorial).evaluate(),
                Parser.parse(yMaxTutorial).evaluate()
        );
    } catch(ParseException e) {
        System.out.println("Fehler beim Parsen der XYRange im Abschnitt 3.1 Werte- & Definitions-Bereich: " + e.getMessage());
        xyRangeTutorial = XYRange.DEFAULT_RANGE();
    }

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
    AST scalingFunctionTutorialAST;
    try {
        scalingFunctionTutorialAST = Parser.parse(scalingFunctionTutorial);
    } catch (ParseException e) {
        System.out.println("Fehler beim Parsen der Skalierfunktion im Abschnitt 3.2 Skalierung: " + e.getMessage());
        scalingFunctionTutorialAST = new AST(new VariableNode("x"));
    }
    Clerk.write(Interaction.input(
            "./src/main/java/start.java", "// Skalier Funktion Beispiel",
            "String scalingFunctionTutorial = \"$\";",
            scalingFunctionTutorial.equals("") ? "x" : scalingFunctionTutorial));

    boolean logScaleTutorial = false; // Logarithmische Skalierung
    boolean trigScaleTutorial = false; // Trigonometrische Skalierung

    SCALING scaleTutorial = scaleHandler(logScaleTutorial, trigScaleTutorial);
    Clerk.write(Interaction.checkbox(
        "./src/main/java/start.java", "// Logarithmische Skalierung",
        "boolean logScaleTutorial = $;",
        logScaleTutorial
    ));
    Clerk.write(Interaction.checkbox(
        "./src/main/java/start.java", "// Trigonometrische Skalierung",
        "boolean trigScaleTutorial = $;",
        trigScaleTutorial
    ));

    Clerk.markdown("""
        ### 3.3 Automatischer Werte- und Definitions-Bereich
        Mit der Angabe von `Smart Range` lässt sich einstellen, ob der manuel eingegebene Bereich verwendet wird, oder ob das Programm selbst einen Bereich ermitteln soll.
        """);

    boolean useSmartRangeTutorial = true; // Use Smart Range

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
                ? XYRangeRecommender.recommendRange(inputExprWithVarAST).xMin()
                : xMinTutorial,
            useSmartRangeTutorial
                ? XYRangeRecommender.recommendRange(inputExprWithVarAST).xMax()
                : xMaxTutorial
    ));

    // Plotter Output
    Clerk.markdown(
        Plotter.plot(
            PlottingConfig.getConfig(
                xyRangeTutorial,
                new OutPutDimension(1000, 700),
                scalingFunctionTutorialAST,
                scaleTutorial,
                useSmartRangeTutorial,
                new ColoredNode(
                    inputExprWithVarAST,
                    ColorPicker.getNextColor()
                )
            )
        )
    );

    // Logical Expressions

    Clerk.markdown("""
        ## 5. Logische Ausdrücke
        Um logische Ausdrücke mit den vorhandenen arithmetischen Ausdrücken kompatibel zu machen und sinnvoll darstellen zu können, muss zuerst definiert werden wann ein arithmetischer Ausdruck `true` oder `false` ist.
        In diesem Fall habe ich mich dazu entschieden alle `positiven Werte` als `true` und alle `negativen Werte und 0` als `false` zu behandeln.
        So ist sichergestellt, dass alle Ausdrücke sowohl als *arithmetisch*, als auch als *logisch* behandelt werden können.
        Umgekehrt werden logische Vergleiche stets einen `Wahrheitswert ∈ {0, 1}` zurückgeben.
        ### 5.1 Unterstützte Operanden
        Es werden alle grundlegenden logischen Operationen unterstützt, eine vollständige Liste aller unterstützten Operanden und Funktionen ist unter `6. Liste aller unterstützten Operanden und Funktionen` zu finden.
        ### 5.2 Beispiel
        Zur übersichtlicheren Gestaltung und Demonstrationszwecken benutzen wir Variablen
        """);

    String booleanExpressionTutorial = "f ? g : h"; // Logischer Ausdruck
    AST booleanExpressionTutorialAST;
    try {
        booleanExpressionTutorialAST = Parser.parse(booleanExpressionTutorial);
    } catch (ParseException e) {
        System.out.println("Fehler beim Parsen des logischen Ausdrucks: " + e.getMessage());
        booleanExpressionTutorialAST = new AST(new VariableNode("x")); // Fallback to a default value if parsing fails
    }


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

    try {
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
    } catch (ParseException e) {
        System.out.println("Fehler beim Parsen der Variablen im Abschnitt 5.2 Beispiel: " + e.getMessage());
        GlobalContext.VARIABLES.addDefaultVariables();
    }

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
            PlottingConfig.getConfig(
                new XYRange(
                    -3 * Math.PI,3 * Math.PI,
                    -5,5
                ),
                new OutPutDimension(1000, 700),
                new AST(new VariableNode("x")),
                SCALING.TRIGONOMETRIC,
                false,
                new ColoredNode(
                    booleanExpressionTutorialAST,
                    ColorPicker.getNextColor()
                )
            )
        )
    );

    Clerk.markdown("""
        # 6. Liste aller unterstützten Operanden und Funktionen
        ## 6.1 Operanden
        ### 6.1.1 Arithmetisch
        - Exponentiation `a hoch b`
            - Infix: **`a ^ b`**
            - RPN: **`a b ^`**
        - Multiplikation `a mal b`
            - Infix: **`a * b`**
            - RPN: **`a b *`**
        - Division `a geteilt durch b`
            - Infix: **`a / b`**
            - RPN: **`a b /`**
        - Modulo `a modulo b`
            - Infix: **`a % b`**
            - RPN: **`a b %`**
        - Addition `a + b`
            - Infix: **`a + b`**
            - RPN: **`a b +`**
        - Subtraktion `a - b`
            - Infix: **`a - b`**
            - RPN: **`a b -`**
        ### 6.1.2 Logisch
        - Vergleich `a kleiner b`
            - Infix: **`a < b`**
            - RPN: **`a b <`**
        - Vergleich `a größer b`
            - Infix: **`a > b`**
            - RPN: **`a b >`**
        - Vergleich `a kleiner oder gleich b`
            - Infix: **`a <= b`**
            - RPN: **`a b <=`**
        - Vergleich `a größer oder gleich b`
            - Infix: **"a >= b`**
            - RPN: **`a b >=`**
        - Vergleich `a gleich b`
            - Infix: **`a == b`**
            - RPN: **`a b ==`**
        - Vergleich `a ungleich b`
            - Infix: **`a != b`**
            - RPN: **`a b !=`**
        - Logisches Und `a und b`
            - Infix: **`a && b`**
            - RPN: **`a b &&`**
        - Logisches Oder `a oder b`
            - Infix: **`a || b`**
            - RPN: **`a b ||`**
        - Logisches Nicht `nicht a`
            - Infix: **`! a`**
            - RPN: **`a !`**
        - Ternärer Operator `Wenn a dann b sonst c`
            - Infix: **`a ? b : c`**
            - RPN: **`a b c ?:`**
        ## 6.2 Funktionen
        ### 6.2.1 Kreisfunktionen
        - Sinus `sin(x)`
            - Infix: **`sin(x)`**
            - RPN: **`x sin`**
        - Arkussinus `asin(x)`
            - Infix: **`asin(x)`**
            - RPN: **`x asin`**
        - Sinus Hyperbolicus `sinh(x)`
            - Infix: **`sinh(x)`**
            - RPN: **`x sinh`**
        - Arkussinus Hyperbolicus `asinh(x)`
            - Infix: **`asinh(x)`**
            - RPN: **`x asinh`**
        - Kosinus `cos(x)`
            - Infix: **`cos(x)`**
            - RPN: **`x cos`**
        - Arkuskosinus `acos(x)`
            - Infix: **`acos(x)`**
            - RPN: **`x acos`**
        - Kosinus Hyperbolicus `cosh(x)`
            - Infix: **`cosh(x)`**
            - RPN: **`x cosh`**
        - Arkuskosinus Hyperbolicus `acosh(x)`
            - Infix: **`acosh(x)`**
            - RPN: **`x acosh`**
        - Tangens `tan(x)`
            - Infix: **`tan(x)`**
            - RPN: **`x tan`**
        - Arkustangens `atan(x)`
            - Infix: **`atan(x)`**
            - RPN: **`x atan`**
        - Tangens Hyperbolicus `tanh(x)`
            - Infix: **`tanh(x)`**
            - RPN: **`x tanh`**
        - Arkustangens Hyperbolicus `atanh(x)`
            - Infix: **`atanh(x)`**
            - RPN: **`x atanh`**
        - Square `square(x)`
            - Infix: **`square(x)`**
            - RPN: **`x square`**
        - Sawtooth `sawtooth(x)`
            - Infix: **`sawtooth(x)`**
            - RPN: **`x sawtooth`**
        - Triangle `triangle(x)`
            - Infix: **`triangle(x)`**
            - RPN: **`x triangle`**
        ### 6.2.2 Weitere Funktionen
        - Quadratwurzel `sqrt(x)`
            - Infix: **`sqrt(x)`**
            - RPN: **`x sqrt`**
        - Wurzel n-ten Grades `n-te Wurzel von x`
            - Bei Angabe nur einen Arguments wird standardmäßig die Quadratwurzel berechnet
            - Infix: **`root(n, x)`**
            - RPN: **`n x root`**
        - Logarithmus `Logarithmus von x zur Basis b`
            - Bei Angabe nur einen Arguments wird standardmäßig der Logarithmus zur Basis 10 berechnet
            - Infix: **`log(x, b)`**
            - RPN: **`x b log`**
        - Natürlicher Logarithmus `ln(x)`
            - Infix: **`ln(x)`**
            - RPN: **`x ln`**
        - Absolutwert `|x|`
            - Infix: **`abs(x)`**
            - RPN: **`x abs`**
        ### 6.2.3 Spezielle Funktionen
        - Gamma-Funktion `gamma(x)`
            - Infix: **`gamma(x)`**
            - RPN: **`x gamma`**
        - Fakultät `x!`
            - Infix: **`factorial(x)`** 
            - RPN: **`x factorial`**
        - Heaviside-Funktion `heaviside(x)`
            - Infix: **`heaviside(x)`**
            - RPN: **`x heaviside`**
        - Signum-Funktion `signum(x)`
            - Infix: **`signum(x)`**
            - RPN: **`x signum`**
        - Minimum `min(a, b)`
            - Infix: **`min(a, b)`**
            - RPN: **`a b min`**
        - Maximum `max(a, b)`
            - Infix: **`max(a, b)`**
            - RPN: **`a b max`**
        - Gauß-Funktion `gauss(x, mu, sigma)`
            - Infix: **`gauss(x, mu, sigma)`**
            - RPN: **`x mu sigma gauss`**
        - Logistische Funktion `logistic(x, mu, sigma)`
            - Infix: **`logistic(x, mu, sigma)`**
            - RPN: **`x mu sigma logistic`**
        """);

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
