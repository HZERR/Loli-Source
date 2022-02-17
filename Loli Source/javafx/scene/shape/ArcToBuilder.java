/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import javafx.scene.shape.ArcTo;
import javafx.scene.shape.PathElementBuilder;
import javafx.util.Builder;

@Deprecated
public class ArcToBuilder<B extends ArcToBuilder<B>>
extends PathElementBuilder<B>
implements Builder<ArcTo> {
    private int __set;
    private boolean largeArcFlag;
    private double radiusX;
    private double radiusY;
    private boolean sweepFlag;
    private double x;
    private double XAxisRotation;
    private double y;

    protected ArcToBuilder() {
    }

    public static ArcToBuilder<?> create() {
        return new ArcToBuilder();
    }

    public void applyTo(ArcTo arcTo) {
        super.applyTo(arcTo);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            arcTo.setLargeArcFlag(this.largeArcFlag);
        }
        if ((n2 & 2) != 0) {
            arcTo.setRadiusX(this.radiusX);
        }
        if ((n2 & 4) != 0) {
            arcTo.setRadiusY(this.radiusY);
        }
        if ((n2 & 8) != 0) {
            arcTo.setSweepFlag(this.sweepFlag);
        }
        if ((n2 & 0x10) != 0) {
            arcTo.setX(this.x);
        }
        if ((n2 & 0x20) != 0) {
            arcTo.setXAxisRotation(this.XAxisRotation);
        }
        if ((n2 & 0x40) != 0) {
            arcTo.setY(this.y);
        }
    }

    public B largeArcFlag(boolean bl) {
        this.largeArcFlag = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B radiusX(double d2) {
        this.radiusX = d2;
        this.__set |= 2;
        return (B)this;
    }

    public B radiusY(double d2) {
        this.radiusY = d2;
        this.__set |= 4;
        return (B)this;
    }

    public B sweepFlag(boolean bl) {
        this.sweepFlag = bl;
        this.__set |= 8;
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set |= 0x10;
        return (B)this;
    }

    public B XAxisRotation(double d2) {
        this.XAxisRotation = d2;
        this.__set |= 0x20;
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set |= 0x40;
        return (B)this;
    }

    @Override
    public ArcTo build() {
        ArcTo arcTo = new ArcTo();
        this.applyTo(arcTo);
        return arcTo;
    }
}

