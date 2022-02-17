/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.util.Builder;

@Deprecated
public class ImageInputBuilder<B extends ImageInputBuilder<B>>
implements Builder<ImageInput> {
    private int __set;
    private Image source;
    private double x;
    private double y;

    protected ImageInputBuilder() {
    }

    public static ImageInputBuilder<?> create() {
        return new ImageInputBuilder();
    }

    public void applyTo(ImageInput imageInput) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            imageInput.setSource(this.source);
        }
        if ((n2 & 2) != 0) {
            imageInput.setX(this.x);
        }
        if ((n2 & 4) != 0) {
            imageInput.setY(this.y);
        }
    }

    public B source(Image image) {
        this.source = image;
        this.__set |= 1;
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public ImageInput build() {
        ImageInput imageInput = new ImageInput();
        this.applyTo(imageInput);
        return imageInput;
    }
}

