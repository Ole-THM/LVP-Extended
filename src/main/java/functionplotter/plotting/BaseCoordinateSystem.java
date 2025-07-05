package functionplotter.plotting;

import functionplotter.ast.ASTNodeI;
import functionplotter.ast.ValueNode;
import functionplotter.plotting.utils.OutPutDimension;
import functionplotter.plotting.utils.XYRange;
import functionplotter.utils.GlobalContext;
import functionplotter.utils.SCALINGS;

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
    static SCALINGS scalingType;

    // Overloaded method for backward compatibility
    public static String genBase(XYRange xyRange, OutPutDimension outPutDimension, ASTNodeI scalingFun) {
        return genBase(xyRange, outPutDimension, scalingFun, null);
    }

    public static String genBase(XYRange xyRange, OutPutDimension outPutDimension, ASTNodeI scalingFun, SCALINGS scaling) {
        StringBuilder res = new StringBuilder();
        width = outPutDimension.width();
        height = outPutDimension.height();

        xMin = xyRange.xMin();
        xMax = xyRange.xMax();
        yMin = xyRange.yMin();
        yMax = xyRange.yMax();

        scalingFunction = scalingFun;
        scalingType = scaling;

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

    /**
     * Calculates the appropriate number of target lines based on scaling type and x/y ranges.
     * The number of lines is limited to a maximum of 20 to prevent the display from becoming too busy.
     * 
     * @return The calculated number of target lines, never more than 20
     */
    private static int calculateTargetLines() {
        // Base value depends on the range
        double xRangeValue = xMax - xMin;
        double yRangeValue = yMax - yMin;

        // Start with a base value proportional to the range
        int baseValue;

        if (scalingType == SCALINGS.LOGARITHMIC) {
            // For logarithmic scaling, use the log of the range
            double logRange = Math.log10(xMax) - Math.log10(Math.max(xMin, 0.0001));
            baseValue = (int) Math.ceil(logRange * 3);
        } else if (scalingType == SCALINGS.TRIGONOMETRIC) {
            // For trigonometric scaling, consider multiples of PI
            double piRange = (xMax - xMin) / Math.PI;
            baseValue = (int) Math.ceil(piRange * 2);
        } else {
            // For linear scaling, base on the actual range
            baseValue = (int) Math.ceil(Math.sqrt(xRangeValue));

            // Adjust based on the magnitude of the range
            if (xRangeValue > 100) {
                baseValue = (int) Math.ceil(baseValue * 0.8);
            } else if (xRangeValue < 10) {
                baseValue = (int) Math.ceil(baseValue * 1.5);
            }
        }

        // Ensure the value is reasonable (not too small or too large)
        baseValue = Math.max(5, baseValue);
        baseValue = Math.min(20, baseValue);

        return baseValue;
    }

    private static String genGrid() {
        StringBuilder res = new StringBuilder();


        int targetLines = calculateTargetLines(); // Calculate appropriate number of grid lines

        double yStep = calculateGridStep((yMax - yMin) / targetLines);

        // vertical lines (x) - handle different scaling types
        if (scalingType == SCALINGS.LOGARITHMIC) {
            // Use logarithmic scaling for x-axis
            double[] xPositions = calculateLogarithmicGridSteps(xMin, xMax, targetLines);
            for (double x : xPositions) {
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
        } else if (scalingType == SCALINGS.TRIGONOMETRIC) {
            // Use trigonometric scaling for x-axis
            double[] xPositions = calculateTrigonometricGridSteps(xMin, xMax, targetLines);
            for (double x : xPositions) {
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
        } else {
            // Use default linear scaling for x-axis (for null or any other value)
            double xStep = calculateGridStep((xMax - xMin) / targetLines);
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

    private static double[] calculateLogarithmicGridSteps(double min, double max, int targetLines) {
        // Ensure min and max are positive for logarithmic scale
        if (min <= 0) {
            min = 0.0001; // Small positive value
        }

        double logMin = Math.log10(min);
        double logMax = Math.log10(max);
        double logRange = logMax - logMin;

        // Calculate step in log space
        double logStep = logRange / targetLines;

        // Create array of grid positions in original space
        double[] gridPositions = new double[targetLines + 1];
        for (int i = 0; i <= targetLines; i++) {
            double logPos = logMin + i * logStep;
            gridPositions[i] = Math.pow(10, logPos);
        }

        return gridPositions;
    }

    private static double[] calculateTrigonometricGridSteps(double min, double max, int targetLines) {
        // Convert min and max to multiples of PI
        double minPi = min / Math.PI;
        double maxPi = max / Math.PI;

        // Round to nearest fractions of PI
        double piStep = calculateGridStep((maxPi - minPi) / targetLines);

        // Start at a nice multiple of PI
        double startPi = Math.floor(minPi / piStep) * piStep;

        // Calculate number of steps needed
        int steps = (int) Math.ceil((maxPi - startPi) / piStep) + 1;

        // Create array of grid positions in original space
        double[] gridPositions = new double[steps];
        for (int i = 0; i < steps; i++) {
            gridPositions[i] = (startPi + i * piStep) * Math.PI;
            // Ensure we don't exceed max due to floating point errors
            if (gridPositions[i] > max) {
                gridPositions[i] = max;
            }
        }

        return gridPositions;
    }

    private static String drawVerticalLineGridLabel(double value, int xPos) {
        GlobalContext.VARIABLES.set("x", new ValueNode(value));
        value = scalingFunction == null ? value : scalingFunction.evaluate();

        String label;
        if (scalingType != SCALINGS.TRIGONOMETRIC) {
            // Default formatting for other scaling types
            label = String.valueOf(value)
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
        } else if (scalingType == SCALINGS.TRIGONOMETRIC) {
            // Format as multiples of PI
            double piMultiple = value / Math.PI;
            if (Math.abs(piMultiple) < 0.001) {
                label = "0";
            } else if (Math.abs(piMultiple - 1) < 0.001) {
                label = "π";
            } else if (Math.abs(piMultiple + 1) < 0.001) {
                label = "-π";
            } else if (Math.abs(piMultiple - 0.5) < 0.001) {
                label = "π/2";
            } else if (Math.abs(piMultiple + 0.5) < 0.001) {
                label = "-π/2";
            } else if (Math.abs(Math.round(piMultiple) - piMultiple) < 0.001) {
                int multiple = (int) Math.round(piMultiple);
                if (multiple == 0) {
                    label = "0";
                } else if (multiple == 1) {
                    label = "π";
                } else if (multiple == -1) {
                    label = "-π";
                } else {
                    label = multiple + "π";
                }
            } else {
                // Try to represent as a fraction of PI
                int denominator = 0;
                for (int d = 2; d <= 12; d++) {
                    double numerator = piMultiple * d;
                    if (Math.abs(Math.round(numerator) - numerator) < 0.001) {
                        denominator = d;
                        break;
                    }
                }

                if (denominator > 0) {
                    int numerator = (int) Math.round(piMultiple * denominator);
                    if (numerator == 0) {
                        label = "0";
                    } else if (numerator == 1 && denominator == 1) {
                        label = "π";
                    } else if (numerator == -1 && denominator == 1) {
                        label = "-π";
                    } else if (numerator == 1) {
                        label = "π/" + denominator;
                    } else if (numerator == -1) {
                        label = "-π/" + denominator;
                    } else {
                        label = numerator + "π/" + denominator;
                    }
                } else {
                    // Fall back to decimal representation
                    label = String.format("%.2fπ", piMultiple);
                }
            }
        } else {
            // Default formatting for other scaling types
            label = String.valueOf(value)
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
        if (label.contains(".")) {
            int idx = label.indexOf(".");
            if (label.length() - idx - 1 > 2) {
                label = label.substring(0, idx + 3);
                label = label.replaceAll("(\\.\\d*?)0+$", "$1").replaceAll("\\.$", "");
            }
        }
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
