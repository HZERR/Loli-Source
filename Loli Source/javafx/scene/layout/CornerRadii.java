/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.beans.NamedArg;

public class CornerRadii {
    public static final CornerRadii EMPTY = new CornerRadii(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false, false, false, false, false, false, false, false);
    private double topLeftHorizontalRadius;
    private double topLeftVerticalRadius;
    private double topRightVerticalRadius;
    private double topRightHorizontalRadius;
    private double bottomRightHorizontalRadius;
    private double bottomRightVerticalRadius;
    private double bottomLeftVerticalRadius;
    private double bottomLeftHorizontalRadius;
    private final boolean topLeftHorizontalRadiusAsPercentage;
    private final boolean topLeftVerticalRadiusAsPercentage;
    private final boolean topRightVerticalRadiusAsPercentage;
    private final boolean topRightHorizontalRadiusAsPercentage;
    private final boolean bottomRightHorizontalRadiusAsPercentage;
    private final boolean bottomRightVerticalRadiusAsPercentage;
    private final boolean bottomLeftVerticalRadiusAsPercentage;
    private final boolean bottomLeftHorizontalRadiusAsPercentage;
    final boolean hasPercentBasedRadii;
    final boolean uniform;
    private final int hash;

    public final double getTopLeftHorizontalRadius() {
        return this.topLeftHorizontalRadius;
    }

    public final double getTopLeftVerticalRadius() {
        return this.topLeftVerticalRadius;
    }

    public final double getTopRightVerticalRadius() {
        return this.topRightVerticalRadius;
    }

    public final double getTopRightHorizontalRadius() {
        return this.topRightHorizontalRadius;
    }

    public final double getBottomRightHorizontalRadius() {
        return this.bottomRightHorizontalRadius;
    }

    public final double getBottomRightVerticalRadius() {
        return this.bottomRightVerticalRadius;
    }

    public final double getBottomLeftVerticalRadius() {
        return this.bottomLeftVerticalRadius;
    }

    public final double getBottomLeftHorizontalRadius() {
        return this.bottomLeftHorizontalRadius;
    }

    public final boolean isTopLeftHorizontalRadiusAsPercentage() {
        return this.topLeftHorizontalRadiusAsPercentage;
    }

    public final boolean isTopLeftVerticalRadiusAsPercentage() {
        return this.topLeftVerticalRadiusAsPercentage;
    }

    public final boolean isTopRightVerticalRadiusAsPercentage() {
        return this.topRightVerticalRadiusAsPercentage;
    }

    public final boolean isTopRightHorizontalRadiusAsPercentage() {
        return this.topRightHorizontalRadiusAsPercentage;
    }

    public final boolean isBottomRightHorizontalRadiusAsPercentage() {
        return this.bottomRightHorizontalRadiusAsPercentage;
    }

    public final boolean isBottomRightVerticalRadiusAsPercentage() {
        return this.bottomRightVerticalRadiusAsPercentage;
    }

    public final boolean isBottomLeftVerticalRadiusAsPercentage() {
        return this.bottomLeftVerticalRadiusAsPercentage;
    }

    public final boolean isBottomLeftHorizontalRadiusAsPercentage() {
        return this.bottomLeftHorizontalRadiusAsPercentage;
    }

    public final boolean isUniform() {
        return this.uniform;
    }

    public CornerRadii(@NamedArg(value="radius") double d2) {
        if (d2 < 0.0) {
            throw new IllegalArgumentException("The radii value may not be < 0");
        }
        this.bottomLeftVerticalRadius = this.bottomLeftHorizontalRadius = d2;
        this.bottomRightVerticalRadius = this.bottomLeftHorizontalRadius;
        this.bottomRightHorizontalRadius = this.bottomLeftHorizontalRadius;
        this.topRightHorizontalRadius = this.bottomLeftHorizontalRadius;
        this.topRightVerticalRadius = this.bottomLeftHorizontalRadius;
        this.topLeftVerticalRadius = this.bottomLeftHorizontalRadius;
        this.topLeftHorizontalRadius = this.bottomLeftHorizontalRadius;
        this.bottomLeftHorizontalRadiusAsPercentage = false;
        this.bottomLeftVerticalRadiusAsPercentage = false;
        this.bottomRightVerticalRadiusAsPercentage = false;
        this.bottomRightHorizontalRadiusAsPercentage = false;
        this.topRightHorizontalRadiusAsPercentage = false;
        this.topRightVerticalRadiusAsPercentage = false;
        this.topLeftVerticalRadiusAsPercentage = false;
        this.topLeftHorizontalRadiusAsPercentage = false;
        this.hasPercentBasedRadii = false;
        this.uniform = true;
        this.hash = this.preComputeHash();
    }

    public CornerRadii(@NamedArg(value="radius") double d2, @NamedArg(value="asPercent") boolean bl) {
        if (d2 < 0.0) {
            throw new IllegalArgumentException("The radii value may not be < 0");
        }
        this.bottomLeftVerticalRadius = this.bottomLeftHorizontalRadius = d2;
        this.bottomRightVerticalRadius = this.bottomLeftHorizontalRadius;
        this.bottomRightHorizontalRadius = this.bottomLeftHorizontalRadius;
        this.topRightHorizontalRadius = this.bottomLeftHorizontalRadius;
        this.topRightVerticalRadius = this.bottomLeftHorizontalRadius;
        this.topLeftVerticalRadius = this.bottomLeftHorizontalRadius;
        this.topLeftHorizontalRadius = this.bottomLeftHorizontalRadius;
        this.bottomLeftVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage = bl;
        this.bottomRightVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.bottomRightHorizontalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.topRightHorizontalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.topRightVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.topLeftVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.topLeftHorizontalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.uniform = true;
        this.hasPercentBasedRadii = bl;
        this.hash = this.preComputeHash();
    }

    public CornerRadii(@NamedArg(value="topLeft") double d2, @NamedArg(value="topRight") double d3, @NamedArg(value="bottomRight") double d4, @NamedArg(value="bottomLeft") double d5, @NamedArg(value="asPercent") boolean bl) {
        if (d2 < 0.0 || d3 < 0.0 || d4 < 0.0 || d5 < 0.0) {
            throw new IllegalArgumentException("No radii value may be < 0");
        }
        this.topLeftHorizontalRadius = this.topLeftVerticalRadius = d2;
        this.topRightVerticalRadius = this.topRightHorizontalRadius = d3;
        this.bottomRightHorizontalRadius = this.bottomRightVerticalRadius = d4;
        this.bottomLeftVerticalRadius = this.bottomLeftHorizontalRadius = d5;
        this.bottomLeftVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage = bl;
        this.bottomRightVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.bottomRightHorizontalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.topRightHorizontalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.topRightVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.topLeftVerticalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.topLeftHorizontalRadiusAsPercentage = this.bottomLeftHorizontalRadiusAsPercentage;
        this.uniform = d2 == d3 && d2 == d5 && d2 == d4;
        this.hasPercentBasedRadii = bl;
        this.hash = this.preComputeHash();
    }

    public CornerRadii(@NamedArg(value="topLeftHorizontalRadius") double d2, @NamedArg(value="topLeftVerticalRadius") double d3, @NamedArg(value="topRightVerticalRadius") double d4, @NamedArg(value="topRightHorizontalRadius") double d5, @NamedArg(value="bottomRightHorizontalRadius") double d6, @NamedArg(value="bottomRightVerticalRadius") double d7, @NamedArg(value="bottomLeftVerticalRadius") double d8, @NamedArg(value="bottomLeftHorizontalRadius") double d9, @NamedArg(value="topLeftHorizontalRadiusAsPercent") boolean bl, @NamedArg(value="topLeftVerticalRadiusAsPercent") boolean bl2, @NamedArg(value="topRightVerticalRadiusAsPercent") boolean bl3, @NamedArg(value="topRightHorizontalRadiusAsPercent") boolean bl4, @NamedArg(value="bottomRightHorizontalRadiusAsPercent") boolean bl5, @NamedArg(value="bottomRightVerticalRadiusAsPercent") boolean bl6, @NamedArg(value="bottomLeftVerticalRadiusAsPercent") boolean bl7, @NamedArg(value="bottomLeftHorizontalRadiusAsPercent") boolean bl8) {
        if (d2 < 0.0 || d3 < 0.0 || d4 < 0.0 || d5 < 0.0 || d6 < 0.0 || d7 < 0.0 || d8 < 0.0 || d9 < 0.0) {
            throw new IllegalArgumentException("No radii value may be < 0");
        }
        this.topLeftHorizontalRadius = d2;
        this.topLeftVerticalRadius = d3;
        this.topRightVerticalRadius = d4;
        this.topRightHorizontalRadius = d5;
        this.bottomRightHorizontalRadius = d6;
        this.bottomRightVerticalRadius = d7;
        this.bottomLeftVerticalRadius = d8;
        this.bottomLeftHorizontalRadius = d9;
        this.topLeftHorizontalRadiusAsPercentage = bl;
        this.topLeftVerticalRadiusAsPercentage = bl2;
        this.topRightVerticalRadiusAsPercentage = bl3;
        this.topRightHorizontalRadiusAsPercentage = bl4;
        this.bottomRightHorizontalRadiusAsPercentage = bl5;
        this.bottomRightVerticalRadiusAsPercentage = bl6;
        this.bottomLeftVerticalRadiusAsPercentage = bl7;
        this.bottomLeftHorizontalRadiusAsPercentage = bl8;
        this.hash = this.preComputeHash();
        this.hasPercentBasedRadii = bl || bl2 || bl3 || bl4 || bl5 || bl6 || bl7 || bl8;
        this.uniform = d2 == d5 && d3 == d4 && d2 == d6 && d3 == d7 && d2 == d9 && d3 == d8 && bl == bl4 && bl2 == bl3 && bl == bl5 && bl2 == bl6 && bl == bl8 && bl2 == bl7;
    }

    private int preComputeHash() {
        long l2 = this.topLeftHorizontalRadius != 0.0 ? Double.doubleToLongBits(this.topLeftHorizontalRadius) : 0L;
        int n2 = (int)(l2 ^ l2 >>> 32);
        l2 = this.topLeftVerticalRadius != 0.0 ? Double.doubleToLongBits(this.topLeftVerticalRadius) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = this.topRightVerticalRadius != 0.0 ? Double.doubleToLongBits(this.topRightVerticalRadius) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = this.topRightHorizontalRadius != 0.0 ? Double.doubleToLongBits(this.topRightHorizontalRadius) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = this.bottomRightHorizontalRadius != 0.0 ? Double.doubleToLongBits(this.bottomRightHorizontalRadius) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = this.bottomRightVerticalRadius != 0.0 ? Double.doubleToLongBits(this.bottomRightVerticalRadius) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = this.bottomLeftVerticalRadius != 0.0 ? Double.doubleToLongBits(this.bottomLeftVerticalRadius) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = this.bottomLeftHorizontalRadius != 0.0 ? Double.doubleToLongBits(this.bottomLeftHorizontalRadius) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        n2 = 31 * n2 + (this.topLeftHorizontalRadiusAsPercentage ? 1 : 0);
        n2 = 31 * n2 + (this.topLeftVerticalRadiusAsPercentage ? 1 : 0);
        n2 = 31 * n2 + (this.topRightVerticalRadiusAsPercentage ? 1 : 0);
        n2 = 31 * n2 + (this.topRightHorizontalRadiusAsPercentage ? 1 : 0);
        n2 = 31 * n2 + (this.bottomRightHorizontalRadiusAsPercentage ? 1 : 0);
        n2 = 31 * n2 + (this.bottomRightVerticalRadiusAsPercentage ? 1 : 0);
        n2 = 31 * n2 + (this.bottomLeftVerticalRadiusAsPercentage ? 1 : 0);
        n2 = 31 * n2 + (this.bottomLeftHorizontalRadiusAsPercentage ? 1 : 0);
        n2 = 31 * n2 + n2;
        return n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        CornerRadii cornerRadii = (CornerRadii)object;
        if (this.hash != cornerRadii.hash) {
            return false;
        }
        if (Double.compare(cornerRadii.bottomLeftHorizontalRadius, this.bottomLeftHorizontalRadius) != 0) {
            return false;
        }
        if (this.bottomLeftHorizontalRadiusAsPercentage != cornerRadii.bottomLeftHorizontalRadiusAsPercentage) {
            return false;
        }
        if (Double.compare(cornerRadii.bottomLeftVerticalRadius, this.bottomLeftVerticalRadius) != 0) {
            return false;
        }
        if (this.bottomLeftVerticalRadiusAsPercentage != cornerRadii.bottomLeftVerticalRadiusAsPercentage) {
            return false;
        }
        if (Double.compare(cornerRadii.bottomRightVerticalRadius, this.bottomRightVerticalRadius) != 0) {
            return false;
        }
        if (this.bottomRightVerticalRadiusAsPercentage != cornerRadii.bottomRightVerticalRadiusAsPercentage) {
            return false;
        }
        if (Double.compare(cornerRadii.bottomRightHorizontalRadius, this.bottomRightHorizontalRadius) != 0) {
            return false;
        }
        if (this.bottomRightHorizontalRadiusAsPercentage != cornerRadii.bottomRightHorizontalRadiusAsPercentage) {
            return false;
        }
        if (Double.compare(cornerRadii.topLeftVerticalRadius, this.topLeftVerticalRadius) != 0) {
            return false;
        }
        if (this.topLeftVerticalRadiusAsPercentage != cornerRadii.topLeftVerticalRadiusAsPercentage) {
            return false;
        }
        if (Double.compare(cornerRadii.topLeftHorizontalRadius, this.topLeftHorizontalRadius) != 0) {
            return false;
        }
        if (this.topLeftHorizontalRadiusAsPercentage != cornerRadii.topLeftHorizontalRadiusAsPercentage) {
            return false;
        }
        if (Double.compare(cornerRadii.topRightHorizontalRadius, this.topRightHorizontalRadius) != 0) {
            return false;
        }
        if (this.topRightHorizontalRadiusAsPercentage != cornerRadii.topRightHorizontalRadiusAsPercentage) {
            return false;
        }
        if (Double.compare(cornerRadii.topRightVerticalRadius, this.topRightVerticalRadius) != 0) {
            return false;
        }
        return this.topRightVerticalRadiusAsPercentage == cornerRadii.topRightVerticalRadiusAsPercentage;
    }

    public int hashCode() {
        return this.hash;
    }

    public String toString() {
        if (this.isUniform()) {
            return "CornerRadii [uniform radius = " + this.topLeftHorizontalRadius + "]";
        }
        return "CornerRadii [" + (this.topLeftHorizontalRadius == this.topLeftVerticalRadius ? "topLeft=" + this.topLeftHorizontalRadius : "topLeftHorizontalRadius=" + this.topLeftHorizontalRadius + ", topLeftVerticalRadius=" + this.topLeftVerticalRadius) + (this.topRightHorizontalRadius == this.topRightVerticalRadius ? ", topRight=" + this.topRightHorizontalRadius : ", topRightVerticalRadius=" + this.topRightVerticalRadius + ", topRightHorizontalRadius=" + this.topRightHorizontalRadius) + (this.bottomRightHorizontalRadius == this.bottomRightVerticalRadius ? ", bottomRight=" + this.bottomRightHorizontalRadius : ", bottomRightHorizontalRadius=" + this.bottomRightHorizontalRadius + ", bottomRightVerticalRadius=" + this.bottomRightVerticalRadius) + (this.bottomLeftHorizontalRadius == this.bottomLeftVerticalRadius ? ", bottomLeft=" + this.bottomLeftHorizontalRadius : ", bottomLeftVerticalRadius=" + this.bottomLeftVerticalRadius + ", bottomLeftHorizontalRadius=" + this.bottomLeftHorizontalRadius) + ']';
    }
}

