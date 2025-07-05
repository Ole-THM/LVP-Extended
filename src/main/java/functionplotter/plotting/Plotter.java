package functionplotter.plotting;

import functionplotter.ast.AST;
import functionplotter.ast.ASTNodeI;
import functionplotter.ast.ValueNode;
import functionplotter.ast.VariableNode;
import functionplotter.plotting.utils.ColoredNode;
import functionplotter.plotting.utils.OutPutDimension;
import functionplotter.plotting.utils.XYRange;
import functionplotter.plotting.utils.XYRangeRecommender;
import functionplotter.utils.GlobalContext;
import functionplotter.utils.SCALINGS;

public class Plotter {

    static XYRange xyRange;
    static double xMin;
    static double xMax;
    static double yMin;
    static double yMax;

    static OutPutDimension outPutDimension;
    static int width;
    static int height;

    static ASTNodeI scalingFunction;
    static SCALINGS scaling;

    static ColoredNode[] coloredNodes;

    /**
     * Handler Function that sets all the relevant class Variables and calls the actual logic to plot which relies on these here set Values
     *
     * @param xyRange User defined X and Y Ranges
     * @param outPutDimension Dimensions of the output Image
     * @param scalingFunction User defined scaling Function for the X Axis
     * @param scaling User selected scaling Function (takes priority over the scalingFunction)
     * @param useSmartRange Tells the Plotter to use the User defined Range or calculate a custom one
     * @param coloredNodes Array of all the ASTs and the colors in which they are going to be plotted
     */

    public static String plot(
            XYRange xyRange,
            OutPutDimension outPutDimension,
            AST scalingFunction,
            SCALINGS scaling,
            Boolean useSmartRange,
            ColoredNode...coloredNodes
    ) {
        Plotter.xyRange = useSmartRange ? XYRangeRecommender.recommendRange(coloredNodes) : xyRange;
        xMin = Plotter.xyRange.xMin();
        xMax = Plotter.xyRange.xMax();
        yMin = Plotter.xyRange.yMin();
        yMax = Plotter.xyRange.yMax();
        Plotter.outPutDimension = outPutDimension;
        width = outPutDimension.width();
        height = outPutDimension.height();
        Plotter.scalingFunction = scalingFunction;
        Plotter.scaling = scaling;
        Plotter.coloredNodes = coloredNodes;
        return plot();
    }

    private static String plot() {

        StringBuilder res = new StringBuilder();
        // Set the viewBox and preserveAspectRatio attributes
        res.append("<svg width=\"").append(width).append("\" height=\"").append(height)
                .append("\" viewBox=\"0 0 ").append(width).append(" ").append(height)
                .append("\" preserveAspectRatio=\"xMidYMid meet\" xmlns=\"http://www.w3.org/2000/svg\">\n");
        res.append(BaseCoordinateSystem.genBase(xyRange, outPutDimension, scalingFunction, scaling));
        for (ColoredNode node : coloredNodes) {
            res.append(plotFunction(node));
        }
        res.append("</svg>\n");
        return res.toString();
    }

    private static String plotFunction(ColoredNode coloredNode) {
        StringBuilder res = new StringBuilder();
        Integer prevXPos = null, prevYPos = null;
        Double prevY = null;
//        System.out.println("Plotter.plotFunction: " + coloredNode.ast().toStringInfix());
        if (coloredNode.ast().toStringInfix().equals("0")) { //! not an elegant solution (Problem: Emtpy expressions should not be plotted)
            return res.toString(); // Skip empty expressions
        }
        for (double x = xMin; x <= xMax; x += getStepSize()) {
            GlobalContext.VARIABLES.set("x", new ValueNode(x));
            // Only apply scaling function if scaling is null
            if (scaling == null) {
                GlobalContext.VARIABLES.set("x", new ValueNode(scalingFunction.evaluate()));
            }
            double rawY = coloredNode.ast().evaluate();
            boolean currInBounds = !Double.isNaN(rawY) && rawY >= yMin && rawY <= yMax;
            boolean prevInBounds = prevY != null && !Double.isNaN(prevY) && prevY >= yMin && prevY <= yMax;

            // Only clip after checking if the point is in bounds
            double y = clipYVal(rawY);
            int currXPos = (int) ((x - xMin) / (xMax - xMin) * width);
            int currYPos = height - (int) ((y - yMin) / (yMax - yMin) * height);

            // Draw line only if both points are not NaN and at least one point is in bounds
            if (prevXPos != null && !Double.isNaN(rawY) && !Double.isNaN(prevY) && (currInBounds || prevInBounds)) {
                res.append("<line x1=\"").append(prevXPos)
                    .append("\" y1=\"").append(prevYPos)
                    .append("\" x2=\"").append(currXPos)
                    .append("\" y2=\"").append(currYPos)
                    .append("\" stroke=\"rgb(")
                    .append(coloredNode.color().r).append(",")
                    .append(coloredNode.color().g).append(",")
                    .append(coloredNode.color().b)
                    .append(")\" stroke-width=\"2\"/>\n");
            }
            prevXPos = currXPos;
            prevYPos = currYPos;
            prevY = rawY;
        }
        return res.toString();
    }

    private static Double clipYVal(Double YPos) {
        if (Double.isNaN(YPos)) return YPos; // Return NaN unchanged
        if (YPos > yMax) return yMax;
        if (YPos < yMin) return yMin;
        return YPos;
    }

    private static double getStepSize() {
        // Adjust step size based on the range and resolution
        double range = xMax - xMin;
        return range / width; // Step size based on width
    }

}
