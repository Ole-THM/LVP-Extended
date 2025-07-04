package functionplotter.utils;

import functionplotter.plotting.utils.ColoredNode;
import functionplotter.plotting.utils.OutPutDimension;
import functionplotter.plotting.utils.XYRange;

public class GlobalContext {
    public static final Variables VARIABLES = new Variables();
    public static XYRange XY_RANGE = new XYRange(-10, 10, -5, 5); // [xMin, xMax]
    public static final StringBuilder OUTPUT_STRING = new StringBuilder();
    public static final OutPutDimension OUT_PUT_DIMENSION = new OutPutDimension(1000, 700); // [width, height]
    public static ColoredNode[] EXPRESSIONS;

}