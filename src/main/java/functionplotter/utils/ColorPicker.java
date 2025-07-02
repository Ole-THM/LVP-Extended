package functionplotter.utils;

import java.util.*;

public class ColorPicker {
    private static final List<COLOR> AVAILABLE_COLORS = new ArrayList<>(Arrays.asList(COLOR.values()));
    private static Iterator<COLOR> iterator = AVAILABLE_COLORS.iterator();

    public static COLOR getNextColor() {
        if (!iterator.hasNext()) {
            Collections.shuffle(AVAILABLE_COLORS);
            iterator = AVAILABLE_COLORS.iterator();
        }
        return iterator.next();
    }
}