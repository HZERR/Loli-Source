/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.transform;

import javafx.scene.transform.Shear;
import javafx.util.Builder;

@Deprecated
public class ShearBuilder<B extends ShearBuilder<B>>
implements Builder<Shear> {
    private int __set;
    private double pivotX;
    private double pivotY;
    private double x;
    private double y;

    protected ShearBuilder() {
    }

    public static ShearBuilder<?> create() {
        return new ShearBuilder();
    }

    public void applyTo(Shear shear) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            shear.setPivotX(this.pivotX);
        }
        if ((n2 & 2) != 0) {
            shear.setPivotY(this.pivotY);
        }
        if ((n2 & 4) != 0) {
            shear.setX(this.x);
        }
        if ((n2 & 8) != 0) {
            shear.setY(this.y);
        }
    }

    public B pivotX(double d2) {
        this.pivotX = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B pivotY(double d2) {
        this.pivotY = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set |= 8;
        return (B)this;
    }

    @Override
    public Shear build() {
        Shear shear = new Shear();
        this.applyTo(shear);
        return shear;
    }
}

