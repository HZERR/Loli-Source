/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.util.Builder;

@Deprecated
public class ImageCursorBuilder<B extends ImageCursorBuilder<B>>
implements Builder<ImageCursor> {
    private double hotspotX;
    private double hotspotY;
    private Image image;

    protected ImageCursorBuilder() {
    }

    public static ImageCursorBuilder<?> create() {
        return new ImageCursorBuilder();
    }

    public B hotspotX(double d2) {
        this.hotspotX = d2;
        return (B)this;
    }

    public B hotspotY(double d2) {
        this.hotspotY = d2;
        return (B)this;
    }

    public B image(Image image) {
        this.image = image;
        return (B)this;
    }

    @Override
    public ImageCursor build() {
        ImageCursor imageCursor = new ImageCursor(this.image, this.hotspotX, this.hotspotY);
        return imageCursor;
    }
}

