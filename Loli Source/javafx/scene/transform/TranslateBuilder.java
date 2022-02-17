/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.transform;

import javafx.scene.transform.Translate;
import javafx.util.Builder;

@Deprecated
public class TranslateBuilder<B extends TranslateBuilder<B>>
implements Builder<Translate> {
    private int __set;
    private double x;
    private double y;
    private double z;

    protected TranslateBuilder() {
    }

    public static TranslateBuilder<?> create() {
        return new TranslateBuilder();
    }

    public void applyTo(Translate translate) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            translate.setX(this.x);
        }
        if ((n2 & 2) != 0) {
            translate.setY(this.y);
        }
        if ((n2 & 4) != 0) {
            translate.setZ(this.z);
        }
    }

    public B x(double d2) {
        this.x = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B z(double d2) {
        this.z = d2;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public Translate build() {
        Translate translate = new Translate();
        this.applyTo(translate);
        return translate;
    }
}

