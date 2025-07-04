package functionplotter.plotting;

import functionplotter.ast.ASTNodeI;
import functionplotter.ast.ValueNode;
import functionplotter.plotting.utils.OutPutDimension;
import functionplotter.plotting.utils.XYRange;
import functionplotter.utils.GlobalContext;

import javax.management.ValueExp;

public class BaseCoordinateSystem {

    private static int axisXPos = -1; // position of the X-axis in the SVG output
    private static int axisYPos = -1; // position of the Y-axis in the SVG output

    static int width;
    static int height;

    static double xMin;
    static double xMax;
    static double yMin;
    static double yMax;

    static ASTNodeI scalingFunction;
    public static String genBase(XYRange xyRange, OutPutDimension outPutDimension, ASTNodeI scalingFun) {
        StringBuilder res = new StringBuilder();
        width = outPutDimension.width();
        height = outPutDimension.height();

        xMin = xyRange.xMin();
        xMax = xyRange.xMax();
        yMin = xyRange.yMin();
        yMax = xyRange.yMax();

        scalingFunction = scalingFun;

        axisXPos = (int) ((-xMin / (xMax - xMin)) * width);
        axisYPos = (height - (int) ((-yMin / (yMax - yMin) * height)));
        res.append(genGrid());
        res.append(genYAxis());
        res.append(genXAxis());
        return res.toString();
    }

    private static String genXAxis() {
//        System.out.println("Drawing X-axis from " + yMin + " to " + yMax);
        if (yMax < 0 || yMin > 0) return ""; // return if the axis is not in the range
        return "<line x1=\"0\" y1=\"" +
                axisYPos +
                "\" x2=\"" +
                width +
                "\" y2=\"" +
                axisYPos +
                "\" style=\"stroke:rgb(0,0,0);stroke-width:2\"/>\n";
    }

    private static String genYAxis() {
//        System.out.println("Drawing Y-axis from " + xMin + " to " + xMax);
        if (xMax < 0 || xMin > 0) return "";
        return "<line x1=\"" +
                axisXPos +
                "\" y1=\"0\" x2=\"" +
                axisXPos +
                "\" y2=\"" +
                height +
                "\" style=\"stroke:rgb(0,0,0);stroke-width:3\"/>\n";
    }

    private static String genGrid() {
//        System.out.println("Drawing grid lines...");
        StringBuilder res = new StringBuilder();


        int targetLines = 20; // Number of grid lines to be drawn ideally

        double xStep = calculateGridStep((xMax - xMin) / targetLines);
        double yStep = calculateGridStep((yMax - yMin) / targetLines);

        // vertical lines (x)
        double xStart = xMin / xStep * xStep;
        for (double x = xStart; x <= xMax; x += xStep) {
            int xPos = (int) ((x - xMin) / (xMax - xMin) * width);
            res.append("<line x1=\"")
                    .append(xPos)
                    .append("\" y1=\"0\" x2=\"")
                    .append(xPos)
                    .append("\" y2=\"")
                    .append(height)
                    .append("\" style=\"stroke:rgb(200,200,200);stroke-width:2\"/>\n")
                    .append(drawVerticalLineGridLabel(x, xPos));
        }

        // horizontal lines (y)
        double yStart = yMin / yStep * yStep;
//        System.out.println(yStart);
        for (double y = yStart; y <= yMax; y += yStep) {
            int yPos = height - (int) ((y - yMin) / (yMax - yMin) * height);
            res.append("<line x1=\"0\" y1=\"")
                    .append(yPos)
                    .append("\" x2=\"")
                    .append(width)
                    .append("\" y2=\"")
                    .append(yPos)
                    .append("\" style=\"stroke:rgb(200,200,200);stroke-width:2\"/>\n")
                    .append(drawHorizontalLineGridLabel(y, yPos));
        }
        return res.toString();
    }

    private static double calculateGridStep(double num) throws IllegalArgumentException {
        if (num <= 0) {
            throw new IllegalArgumentException("Grid step must be greater than zero, got: " + num);
        }
        double exponent = Math.floor(Math.log10(num));
        double base = Math.pow(10, exponent);
        double mantissa = num / base; // Normalizes to [1, 10)

        double factor;
        if (mantissa >= 5) {
            factor = 5;
        } else if (mantissa >= 2) {
            factor = 2;
        } else {
            factor = 1;
        }

        return factor * base;
    }

    private static String drawVerticalLineGridLabel(double value, int xPos) {
        GlobalContext.VARIABLES.set("x", new ValueNode(value));
        value = scalingFunction == null ? value : scalingFunction.evaluate();
        String label = String.valueOf(value)
            .replaceAll("\\.0+$", "")
            .replaceAll("(\\.\\d*?)0+$", "$1")
            .replaceAll("\\.$", "");
        if (label.contains(".")) {
            int idx = label.indexOf(".");
            if (label.length() - idx - 1 > 2) {
                label = label.substring(0, idx + 3);
                label = label.replaceAll("(\\.\\d*?)0+$", "$1").replaceAll("\\.$", "");
            }
        }
        int yPos = axisYPos + 15; // Position below the X-axis
        return "<text x=\"" +
                (xPos - 10) + // Adjust x position for better visibility
                "\" y=\"" +
                yPos +
                "\" font-size=\"12\" text-anchor=\"middle\">" +
                label +
                "</text>\n";
    }

    private static String drawHorizontalLineGridLabel(double value, int yPos) {
        String label = String.valueOf(value)
            .replaceAll("\\.0+$", "")
            .replaceAll("(\\.\\d*?)0+$", "$1")
            .replaceAll("\\.$", ""); // Remove trailing zeros
        int xPos = axisXPos + 10; // Position to the right of the Y-axis
        return "<text x=\"" +
                xPos +
                "\" y=\"" +
                (yPos + 15) + // Adjust y position for better visibility
                "\" font-size=\"12\" text-anchor=\"start\">" +
                label +
                "</text>\n";
    }
}