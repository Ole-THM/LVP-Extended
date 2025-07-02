package functionplotter.plotting;

import functionplotter.ast.ValueNode;
import functionplotter.plotting.utils.ColoredNode;
import functionplotter.utils.GlobalContext;

public class Plotter {

    public static String plot(ColoredNode...coloredNodes) {
        int width = GlobalContext.OUT_PUT_DIMENSION.width();
        int height = GlobalContext.OUT_PUT_DIMENSION.height();

        StringBuilder res = new StringBuilder();
        // Set the viewBox and preserveAspectRatio attributes
        res.append("<svg width=\"").append(width).append("\" height=\"").append(height)
                .append("\" viewBox=\"0 0 ").append(width).append(" ").append(height)
                .append("\" preserveAspectRatio=\"xMidYMid meet\" xmlns=\"http://www.w3.org/2000/svg\">\n");
        res.append(BaseCoordinateSystem.genBase());
        for (ColoredNode node : coloredNodes) {
            res.append(
                    plotFunction(node)
            );
        }
        res.append("</svg>\n");
        return res.toString();
    }

    private static String plotFunction(ColoredNode coloredNode) {
        StringBuilder res = new StringBuilder();
        Double prevX = null, prevY = null;
        for (double x = GlobalContext.XY_RANGE.xMin(); x <= GlobalContext.XY_RANGE.xMax(); x += getStepSize()) {
            GlobalContext.VARIABLES.set("x", new ValueNode(x));
            double y = coloredNode.ast().evaluate();
            int xPos = (int) ((x - GlobalContext.XY_RANGE.xMin()) / (GlobalContext.XY_RANGE.xMax() - GlobalContext.XY_RANGE.xMin()) * GlobalContext.OUT_PUT_DIMENSION.width());
            int yPos = (int) ((y - GlobalContext.XY_RANGE.yMin()) / (GlobalContext.XY_RANGE.yMax() - GlobalContext.XY_RANGE.yMin()) * GlobalContext.OUT_PUT_DIMENSION.height());
            yPos = GlobalContext.OUT_PUT_DIMENSION.height() - yPos;
            if (prevX != null && prevY != null) {
                res.append("<line x1=\"").append(prevX.intValue())
                        .append("\" y1=\"").append(prevY.intValue())
                        .append("\" x2=\"").append(xPos)
                        .append("\" y2=\"").append(yPos)
                        .append("\" stroke=\"rgb(")
                        .append(coloredNode.color().r).append(",")
                        .append(coloredNode.color().g).append(",")
                        .append(coloredNode.color().b)
                        .append(")\" stroke-width=\"2\"/>\n");
            }
            prevX = (double) xPos;
            prevY = (double) yPos;
        }
        return res.toString();
    }

    private static double getStepSize() {
        // Adjust step size based on the range and resolution
        double range = GlobalContext.XY_RANGE.xMax() - GlobalContext.XY_RANGE.xMin();
        return range / GlobalContext.OUT_PUT_DIMENSION.width(); // Step size based on width
    }

}