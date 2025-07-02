package functionplotter.utils;

public record Token(TOKEN_TYPE type, String text) {

    @Override
    public String toString() {
        return type + "('" + text + "')";
    }

}