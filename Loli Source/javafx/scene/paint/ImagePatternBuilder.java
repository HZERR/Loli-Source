/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.paint;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.util.Builder;

@Deprecated
public final class ImagePatternBuilder
implements Builder<ImagePattern> {
    private double height;
    private Image image;
    private boolean proportional;
    private double width;
    private double x;
    private double y;

    protected ImagePatternBuilder() {
    }

    public static ImagePatternBuilder create() {
        return new ImagePatternBuilder();
    }

    public ImagePatternBuilder height(double d2) {
        this.height = d2;
        return this;
    }

    public ImagePatternBuilder image(Image image) {
        this.image = image;
        return this;
    }

    public ImagePatternBuilder proportional(boolean bl) {
        this.proportional = bl;
        return this;
    }

    public ImagePatternBuilder width(double d2) {
        this.width = d2;
        return this;
    }

    public ImagePatternBuilder x(double d2) {
        this.x = d2;
        return this;
    }

    public ImagePatternBuilder y(double d2) {
        this.y = d2;
        return this;
    }

    @Override
    public ImagePattern build() {
        ImagePattern imagePattern = new ImagePattern(this.image, this.x, this.y, this.width, this.height, this.proportional);
        return imagePattern;
    }
}

