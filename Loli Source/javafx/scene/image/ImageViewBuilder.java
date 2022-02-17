/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.image;

import javafx.geometry.Rectangle2D;
import javafx.scene.NodeBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Builder;

@Deprecated
public class ImageViewBuilder<B extends ImageViewBuilder<B>>
extends NodeBuilder<B>
implements Builder<ImageView> {
    private int __set;
    private double fitHeight;
    private double fitWidth;
    private Image image;
    private boolean preserveRatio;
    private boolean smooth;
    private Rectangle2D viewport;
    private double x;
    private double y;

    protected ImageViewBuilder() {
    }

    public static ImageViewBuilder<?> create() {
        return new ImageViewBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(ImageView imageView) {
        super.applyTo(imageView);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    imageView.setFitHeight(this.fitHeight);
                    break;
                }
                case 1: {
                    imageView.setFitWidth(this.fitWidth);
                    break;
                }
                case 2: {
                    imageView.setImage(this.image);
                    break;
                }
                case 3: {
                    imageView.setPreserveRatio(this.preserveRatio);
                    break;
                }
                case 4: {
                    imageView.setSmooth(this.smooth);
                    break;
                }
                case 5: {
                    imageView.setViewport(this.viewport);
                    break;
                }
                case 6: {
                    imageView.setX(this.x);
                    break;
                }
                case 7: {
                    imageView.setY(this.y);
                }
            }
        }
    }

    public B fitHeight(double d2) {
        this.fitHeight = d2;
        this.__set(0);
        return (B)this;
    }

    public B fitWidth(double d2) {
        this.fitWidth = d2;
        this.__set(1);
        return (B)this;
    }

    public B image(Image image) {
        this.image = image;
        this.__set(2);
        return (B)this;
    }

    public B preserveRatio(boolean bl) {
        this.preserveRatio = bl;
        this.__set(3);
        return (B)this;
    }

    public B smooth(boolean bl) {
        this.smooth = bl;
        this.__set(4);
        return (B)this;
    }

    public B viewport(Rectangle2D rectangle2D) {
        this.viewport = rectangle2D;
        this.__set(5);
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set(6);
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set(7);
        return (B)this;
    }

    @Override
    public ImageView build() {
        ImageView imageView = new ImageView();
        this.applyTo(imageView);
        return imageView;
    }
}

