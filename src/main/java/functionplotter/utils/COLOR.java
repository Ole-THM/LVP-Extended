package functionplotter.utils;

public enum COLOR {
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255),
    YELLOW(255, 255, 0),
    CYAN(0, 255, 255),
    MAGENTA(255, 0, 255),
    ORANGE(255, 165, 0),
    PURPLE(128, 0, 128),
    LIME(191, 255, 0),
    TEAL(0, 128, 128),
    PINK(255, 105, 180),
    BROWN(139, 69, 19),
    NAVY(0, 0, 128),
    OLIVE(128, 128, 0),
    MAROON(128, 0, 0),
    GOLD(255, 215, 0),
    TURQUOISE(64, 224, 208),
    VIOLET(238, 130, 238),
    INDIGO(75, 0, 130),
    GRAY(128, 128, 128);

    public final int r, g, b;

    COLOR(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
}