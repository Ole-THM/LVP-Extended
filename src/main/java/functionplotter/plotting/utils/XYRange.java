package functionplotter.plotting.utils;

public record XYRange(
        double xMin,
        double xMax,
        double yMin,
        double yMax,
        double xRange,
        double yRange
) {

    public XYRange(double xMin, double xMax, double yMin, double yMax) {
        this(xMin, xMax, yMin, yMax, xMax - xMin, yMax - yMin);
    }
}