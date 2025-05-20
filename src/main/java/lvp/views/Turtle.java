package lvp.views;

import lvp.Clerk;
import lvp.views.turtle.Font;

public class Turtle implements Clerk {
    public final String ID;
    final int width, height;
    Font textFont = Font.SANSSERIF;
    double textSize = 10;
    Font.Align textAlign = Font.Align.CENTER;

    public Turtle(int width, int height) {
        this.width  = Math.max(1, Math.abs(width));  // width is at least of size 1
        this.height = Math.max(1, Math.abs(height)); // height is at least of size 1
        ID = Clerk.getHashID(this);
        Clerk.load("views/turtle/turtle.js");
        Clerk.write("<canvas id='turtleCanvas" + ID + "' width='" + this.width + "' height='" + this.height + "' style='border:1px solid #000;'></canvas>");
        Clerk.script("let turtle" + ID + " = new Turtle(document.getElementById('turtleCanvas" + ID + "'));");
    }

    public Turtle() { this(500, 500); }

    public Turtle penDown() {
        Clerk.call("turtle" + ID + ".penDown();");
        return this;
    }

    public Turtle penUp() {
        Clerk.call("turtle" + ID + ".penUp();");
        return this;
    }

    public Turtle forward(double distance) {
        Clerk.call("turtle" + ID + ".forward(" + distance + ");");
        return this;
    }

    public Turtle backward(double distance) {
        Clerk.call("turtle" + ID + ".backward(" + distance + ");");
        return this;
    }

    public Turtle left(double degrees) {
        Clerk.call("turtle" + ID + ".left(" + degrees + ");");
        return this;
    }

    public Turtle right(double degrees) {
        Clerk.call("turtle" + ID + ".right(" + degrees + ");");
        return this;
    }

    public Turtle color(int red, int green, int blue) {
        Clerk.call("turtle" + ID + ".color('rgb(" + (red & 0xFF) + ", " + (green & 0xFF) + ", " + (blue & 0xFF) + ")');");
        return this;
    }

    public Turtle color(int rgb) {
        color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        return this;
    }

    public Turtle lineWidth(double width) {
        Clerk.call("turtle" + ID + ".lineWidth('" + width + "');");
        return this;
    }

    public Turtle reset() {
        Clerk.call("turtle" + ID + ".reset();");
        return this;
    }

    public Turtle text(String text, Font font, double size, Font.Align align) {
        textFont = font;
        textSize = size;
        textAlign = align;
        Clerk.call("turtle" + ID + ".text('" + text + "', '" + "" + size + "px " + font + "', '" + align + "')");
        return this;
    }

    public Turtle text(String text) { return text(text, textFont, textSize, textAlign); }

    public Turtle moveTo(double x, double y) {
        Clerk.call("turtle" + ID + ".moveTo(" + x + ", " + y + ");");
        return this;
    }

    public Turtle lineTo(double x, double y) {
        Clerk.call("turtle" + ID + ".lineTo(" + x + ", " + y + ");");
        return this;
    }
}