package functionplotter.plotting.utils;

import functionplotter.ast.AST;
import functionplotter.ast.ValueNode;
import functionplotter.utils.GlobalContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for recommending an appropriate XYRange for plotting functions.
 * Analyzes ASTs to determine a range that includes all interesting features.
 */
public class XYRangeRecommender {

    // Default initial range for sampling
    private static final double DEFAULT_X_MIN = -10.0;
    private static final double DEFAULT_X_MAX = 10.0;
    private static final int SAMPLE_POINTS = 200;
    private static final double PADDING_FACTOR = 0.1; // 10% padding

    /**
     * Recommends an XYRange for plotting the given ASTs.
     * 
     * @param asts Array of ASTs to analyze
     * @return Recommended XYRange for plotting
     */
    public static XYRange recommendRange(AST...asts) {
        if (asts == null || asts.length == 0) {
            // Return default range if no ASTs provided
            return new XYRange(DEFAULT_X_MIN, DEFAULT_X_MAX, DEFAULT_X_MIN / 2, DEFAULT_X_MAX / 2);
        }

        // Initial x range for sampling
        double xMin = DEFAULT_X_MIN;
        double xMax = DEFAULT_X_MAX;

        // Sample points and find min/max y values
        List<Double> allYValues = new ArrayList<>();
        double step = (xMax - xMin) / SAMPLE_POINTS;

        for (double x = xMin; x <= xMax; x += step) {
            GlobalContext.VARIABLES.set("x", new ValueNode(x));
            
            for (AST ast : asts) {
                try {
                    double y = ast.evaluate();
                    if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                        allYValues.add(y);
                    }
                } catch (Exception e) {
                    // Skip evaluation errors
                }
            }
        }

        // If no valid y values found, return default range
        if (allYValues.isEmpty()) {
            return new XYRange(DEFAULT_X_MIN, DEFAULT_X_MAX, DEFAULT_X_MIN / 2, DEFAULT_X_MAX / 2);
        }

        // Find min and max y values
        double yMin = Double.MAX_VALUE;
        double yMax = -Double.MAX_VALUE;
        
        for (Double y : allYValues) {
            if (y < yMin) yMin = y;
            if (y > yMax) yMax = y;
        }

        // Ensure yMin and yMax are different
        if (Math.abs(yMax - yMin) < 1e-10) {
            yMin -= 1.0;
            yMax += 1.0;
        }

        // Add padding to ensure features are visible
        double yRange = yMax - yMin;
        double padding = yRange * PADDING_FACTOR;
        yMin -= padding;
        yMax += padding;

        // Analyze for interesting features (zeros, extrema)
        List<Double> interestingXPoints = findInterestingXPoints(asts, xMin, xMax);
        
        // If interesting points found, adjust x range to include them
        if (!interestingXPoints.isEmpty()) {
            double minInterestingX = Double.MAX_VALUE;
            double maxInterestingX = -Double.MAX_VALUE;
            
            for (Double x : interestingXPoints) {
                if (x < minInterestingX) minInterestingX = x;
                if (x > maxInterestingX) maxInterestingX = x;
            }
            
            // Add padding to x range
            double xRange = maxInterestingX - minInterestingX;
            double xPadding = Math.max(xRange * PADDING_FACTOR, 1.0);
            xMin = minInterestingX - xPadding;
            xMax = maxInterestingX + xPadding;
        }

        return new XYRange(xMin, xMax, yMin, yMax);
    }

    /**
     * Finds interesting x points (zeros, extrema) for the given ASTs.
     * 
     * @param asts Array of ASTs to analyze
     * @param xMin Minimum x value to consider
     * @param xMax Maximum x value to consider
     * @return List of interesting x points
     */
    private static List<Double> findInterestingXPoints(AST[] asts, double xMin, double xMax) {
        List<Double> interestingPoints = new ArrayList<>();
        double step = (xMax - xMin) / SAMPLE_POINTS;

        for (AST ast : asts) {
            // Find zeros (where function crosses x-axis)
            Double prevY = null;
            for (double x = xMin; x <= xMax; x += step) {
                GlobalContext.VARIABLES.set("x", new ValueNode(x));
                try {
                    double y = ast.evaluate();
                    if (!Double.isNaN(y) && !Double.isInfinite(y)) {
                        if (prevY != null && ((prevY <= 0 && y >= 0) || (prevY >= 0 && y <= 0))) {
                            // Zero crossing detected
                            interestingPoints.add(x - step/2); // Approximate zero point
                        }
                        prevY = y;
                    }
                } catch (Exception e) {
                    // Skip evaluation errors
                }
            }

            // Find local extrema (where derivative changes sign)
            Double prevDy = null;
            for (double x = xMin; x <= xMax; x += step) {
                GlobalContext.VARIABLES.set("x", new ValueNode(x));
                try {
                    // Calculate approximate derivative
                    double y1 = ast.evaluate();
                    GlobalContext.VARIABLES.set("x", new ValueNode(x + step * 0.01));
                    double y2 = ast.evaluate();
                    double dy = (y2 - y1) / (step * 0.01);

                    if (!Double.isNaN(dy) && !Double.isInfinite(dy)) {
                        if (prevDy != null && ((prevDy <= 0 && dy >= 0) || (prevDy >= 0 && dy <= 0))) {
                            // Extremum detected
                            interestingPoints.add(x - step/2); // Approximate extremum point
                        }
                        prevDy = dy;
                    }
                } catch (Exception e) {
                    // Skip evaluation errors
                }
            }
        }

        return interestingPoints;
    }

    /**
     * Convenience method that takes an array of ColoredNodes and extracts their ASTs.
     * 
     * @param coloredNodes Array of ColoredNodes to analyze
     * @return Recommended XYRange for plotting
     */
    public static XYRange recommendRange(ColoredNode[] coloredNodes) {
        if (coloredNodes == null || coloredNodes.length == 0) {
            return new XYRange(DEFAULT_X_MIN, DEFAULT_X_MAX, DEFAULT_X_MIN / 2, DEFAULT_X_MAX / 2);
        }
        
        AST[] asts = Arrays.stream(coloredNodes)
                .map(ColoredNode::ast)
                .toArray(AST[]::new);
        
        return recommendRange(asts);
    }
}