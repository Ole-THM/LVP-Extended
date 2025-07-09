package functionplotter.utils;

import functionplotter.ast.AST;
import functionplotter.ast.BinaryOpNode;
import functionplotter.ast.ValueNode;
import functionplotter.ast.VariableNode;
import functionplotter.parsing.parser.Parser;
import functionplotter.plotting.utils.ColoredNode;
import functionplotter.plotting.utils.OutPutDimension;
import functionplotter.plotting.utils.XYRange;
import functionplotter.plotting.utils.XYRangeRecommender;

import java.text.ParseException;

public record PlottingConfig(
    XYRange xyRange,
    OutPutDimension outPutDimension,
    AST scalingFunction,
    SCALING scaling,
    ColoredNode[] coloredNodes
) {
    /**
     * Factory method to create a PlottingConfig instance with the provided parameters.
     * It also handles the case for logarithmic scaling by adjusting the xMin value.
     *
     * @param xyRange User defined X and Y Ranges
     * @param outPutDimension Dimensions of the output Image
     * @param scalingFunction User defined scaling Function for the X Axis
     * @param scaling User selected scaling Function (takes priority over the scalingFunction)
     * @param useSmartRange Tells the Plotter to use the User defined Range or calculate a custom one
     * @param coloredNodes Array of all the ASTs and the colors in which they are going to be plotted
     * @return A new PlottingConfig instance
     * @throws ParseException If there is an error during parsing of the scaling function
     */

    public static PlottingConfig getConfig(
        XYRange xyRange,
        OutPutDimension outPutDimension,
        AST scalingFunction,
        SCALING scaling,
        boolean useSmartRange,
        ColoredNode... coloredNodes
    ) {
        XYRange newXYRange = useSmartRange ? XYRangeRecommender.recommendRange(coloredNodes) : xyRange;
        AST newScalingFunction = scalingFunction;
        if (scaling == SCALING.LOGARITHMIC) { // Logarithmic scaling requires xMin to be positive
            newXYRange = new XYRange(
                Math.max(0, newXYRange.xMin()),
                newXYRange.xMax(),
                newXYRange.yMin(),
                newXYRange.yMax()
            );
            newScalingFunction = new AST(
                    new BinaryOpNode(
                            new ValueNode(10),
                            TOKEN_TYPE.EXPONENT,
                            new VariableNode("x")
                    )
            ); // default scaling function for logarithmic scaling
        }
        return new PlottingConfig(
            newXYRange,
            outPutDimension,
            newScalingFunction,
            scaling,
            coloredNodes
        );

    }

}
