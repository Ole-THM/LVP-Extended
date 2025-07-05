package functionplotter.plotting;

import functionplotter.ast.AST;
import functionplotter.ast.ASTNodeI;
import functionplotter.ast.ValueNode;
import functionplotter.ast.VariableNode;
import functionplotter.plotting.utils.ColoredNode;
import functionplotter.plotting.utils.OutPutDimension;
import functionplotter.plotting.utils.XYRange;
import functionplotter.utils.GlobalContext;

public class Plotter {

    static int width;
    static int height;

    static double xMin;
    static double xMax;
    static double yMin;
    static double yMax;

    static ASTNodeI scalingFunction;

    public static String plot(XYRange xyRange, OutPutDimension outPutDimension, ColoredNode...coloredNodes) {
        return plot(xyRange, outPutDimension, new AST(new VariableNode("x")), coloredNodes);
    }

    public static String plot(XYRange xyRange, OutPutDimension outPutDimension, ASTNodeI scalingFun, ColoredNode...coloredNodes) {

        width = outPutDimension.width();
        height = outPutDimension.height();

        xMin = xyRange.xMin();
        xMax = xyRange.xMax();
        yMin = xyRange.yMin();
        yMax = xyRange.yMax();

        scalingFunction = scalingFun;

        StringBuilder res = new StringBuilder();
        // Set the viewBox and preserveAspectRatio attributes
        res.append("<svg width=\"").append(width).append("\" height=\"").append(height)
                .append("\" viewBox=\"0 0 ").append(width).append(" ").append(height)
                .append("\" preserveAspectRatio=\"xMidYMid meet\" xmlns=\"http://www.w3.org/2000/svg\">\n");
        res.append(BaseCoordinateSystem.genBase(xyRange, outPutDimension, scalingFun));
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
            GlobalContext.VARIABLES.set("x", new ValueNode(scalingFunction.evaluate()));
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
