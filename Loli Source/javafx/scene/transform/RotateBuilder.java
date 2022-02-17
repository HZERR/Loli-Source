/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.transform;

import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.util.Builder;

@Deprecated
public class RotateBuilder<B extends RotateBuilder<B>>
implements Builder<Rotate> {
    private int __set;
    private double angle;
    private Point3D axis;
    private double pivotX;
    private double pivotY;
    private double pivotZ;

    protected RotateBuilder() {
    }

    public static RotateBuilder<?> create() {
        return new RotateBuilder();
    }

    public void applyTo(Rotate rotate) {
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            rotate.setAngle(this.angle);
        }
        if ((n2 & 2) != 0) {
            rotate.setAxis(this.axis);
        }
        if ((n2 & 4) != 0) {
            rotate.setPivotX(this.pivotX);
        }
        if ((n2 & 8) != 0) {
            rotate.setPivotY(this.pivotY);
        }
        if ((n2 & 0x10) != 0) {
            rotate.setPivotZ(this.pivotZ);
        }
    }

    public B angle(double d2) {
        this.angle = d2;
        this.__set |= 1;
        return (B)this;
    }

    public B axis(Point3D point3D) {
        this.axis = point3D;
        this.__set |= 2;
        return (B)this;
    }

    public B pivotX(double d2) {
        this.pivotX = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B pivotY(double d2) {
        this.pivotY = d2;
        this.__set |= 8;
        return (B)this;
    }

    public B pivotZ(double d2) {
        this.pivotZ = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    @Override
    public Rotate build() {
        Rotate rotate = new Rotate();
        this.applyTo(rotate);
        return rotate;
    }
}

