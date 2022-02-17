/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.transform;

import javafx.scene.transform.Scale;
import javafx.util.Builder;

@Deprecated
public class ScaleBuilder<B extends ScaleBuilder<B>>
implements Builder<Scale> {
    private int __set;
    private double pivotX;
    private double pivotY;
    private double pivotZ;
    private double x;
    private double y;
    private double z;

    protected ScaleBuilder() {
    }

    public static ScaleBuilder<?> create() {
        return new ScaleBuilder();
    }

    public void applyTo(Scale scale) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            scale.setPivotX(this.pivotX);
        }
        if ((n2 & 2) != 0) {
            scale.setPivotY(this.pivotY);
        }
        if ((n2 & 4) != 0) {
            scale.setPivotZ(this.pivotZ);
        }
        if ((n2 & 8) != 0) {
            scale.setX(this.x);
        }
        if ((n2 & 0x10) != 0) {
            scale.setY(this.y);
        }
        if ((n2 & 0x20) != 0) {
            scale.setZ(this.z);
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

    public B pivotZ(double d2) {
        this.pivotZ = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B z(double d2) {
        this.z = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    @Override
    public Scale build() {
        Scale scale = new Scale();
        this.applyTo(scale);
        return scale;
    }
}

