/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.effect;

import javafx.scene.effect.Effect;
import javafx.scene.effect.PerspectiveTransform;
import javafx.util.Builder;

@Deprecated
public class PerspectiveTransformBuilder<B extends PerspectiveTransformBuilder<B>>
implements Builder<PerspectiveTransform> {
    private int __set;
    private Effect input;
    private double llx;
    private double lly;
    private double lrx;
    private double lry;
    private double ulx;
    private double uly;
    private double urx;
    private double ury;

    protected PerspectiveTransformBuilder() {
    }

    public static PerspectiveTransformBuilder<?> create() {
        return new PerspectiveTransformBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(PerspectiveTransform perspectiveTransform) {
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    perspectiveTransform.setInput(this.input);
                    break;
                }
                case 1: {
                    perspectiveTransform.setLlx(this.llx);
                    break;
                }
                case 2: {
                    perspectiveTransform.setLly(this.lly);
                    break;
                }
                case 3: {
                    perspectiveTransform.setLrx(this.lrx);
                    break;
                }
                case 4: {
                    perspectiveTransform.setLry(this.lry);
                    break;
                }
                case 5: {
                    perspectiveTransform.setUlx(this.ulx);
                    break;
                }
                case 6: {
                    perspectiveTransform.setUly(this.uly);
                    break;
                }
                case 7: {
                    perspectiveTransform.setUrx(this.urx);
                    break;
                }
                case 8: {
                    perspectiveTransform.setUry(this.ury);
                }
            }
        }
    }

    public B input(Effect effect) {
        this.input = effect;
        this.__set(0);
        return (B)this;
    }

    public B llx(double d2) {
        this.llx = d2;
        this.__set(1);
        return (B)this;
    }

    public B lly(double d2) {
        this.lly = d2;
        this.__set(2);
        return (B)this;
    }

    public B lrx(double d2) {
        this.lrx = d2;
        this.__set(3);
        return (B)this;
    }

    public B lry(double d2) {
        this.lry = d2;
        this.__set(4);
        return (B)this;
    }

    public B ulx(double d2) {
        this.ulx = d2;
        this.__set(5);
        return (B)this;
    }

    public B uly(double d2) {
        this.uly = d2;
        this.__set(6);
        return (B)this;
    }

    public B urx(double d2) {
        this.urx = d2;
        this.__set(7);
        return (B)this;
    }

    public B ury(double d2) {
        this.ury = d2;
        this.__set(8);
        return (B)this;
    }

    @Override
    public PerspectiveTransform build() {
        PerspectiveTransform perspectiveTransform = new PerspectiveTransform();
        this.applyTo(perspectiveTransform);
        return perspectiveTransform;
    }
}

