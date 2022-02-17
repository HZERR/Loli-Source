/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.beans.NamedArg;

public final class BorderWidths {
    public static final double AUTO = -1.0;
    public static final BorderWidths DEFAULT = new BorderWidths(1.0, 1.0, 1.0, 1.0, false, false, false, false);
    public static final BorderWidths EMPTY = new BorderWidths(0.0, 0.0, 0.0, 0.0, false, false, false, false);
    public static final BorderWidths FULL = new BorderWidths(1.0, 1.0, 1.0, 1.0, true, true, true, true);
    final double top;
    final double right;
    final double bottom;
    final double left;
    final boolean topAsPercentage;
    final boolean rightAsPercentage;
    final boolean bottomAsPercentage;
    final boolean leftAsPercentage;
    private final int hash;

    public final double getTop() {
        return this.top;
    }

    public final double getRight() {
        return this.right;
    }

    public final double getBottom() {
        return this.bottom;
    }

    public final double getLeft() {
        return this.left;
    }

    public final boolean isTopAsPercentage() {
        return this.topAsPercentage;
    }

    public final boolean isRightAsPercentage() {
        return this.rightAsPercentage;
    }

    public final boolean isBottomAsPercentage() {
        return this.bottomAsPercentage;
    }

    public final boolean isLeftAsPercentage() {
        return this.leftAsPercentage;
    }

    public BorderWidths(@NamedArg(value="width") double d2) {
        this(d2, d2, d2, d2, false, false, false, false);
    }

    public BorderWidths(@NamedArg(value="top") double d2, @NamedArg(value="right") double d3, @NamedArg(value="bottom") double d4, @NamedArg(value="left") double d5) {
        this(d2, d3, d4, d5, false, false, false, false);
    }

    public BorderWidths(@NamedArg(value="top") double d2, @NamedArg(value="right") double d3, @NamedArg(value="bottom") double d4, @NamedArg(value="left") double d5, @NamedArg(value="topAsPercentage") boolean bl, @NamedArg(value="rightAsPercentage") boolean bl2, @NamedArg(value="bottomAsPercentage") boolean bl3, @NamedArg(value="leftAsPercentage") boolean bl4) {
        if (d2 != -1.0 && d2 < 0.0 || d3 != -1.0 && d3 < 0.0 || d4 != -1.0 && d4 < 0.0 || d5 != -1.0 && d5 < 0.0) {
            throw new IllegalArgumentException("None of the widths can be < 0");
        }
        this.top = d2;
        this.right = d3;
        this.bottom = d4;
        this.left = d5;
        this.topAsPercentage = bl;
        this.rightAsPercentage = bl2;
        this.bottomAsPercentage = bl3;
        this.leftAsPercentage = bl4;
        long l2 = this.top != 0.0 ? Double.doubleToLongBits(this.top) : 0L;
        int n2 = (int)(l2 ^ l2 >>> 32);
        l2 = this.right != 0.0 ? Double.doubleToLongBits(this.right) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = this.bottom != 0.0 ? Double.doubleToLongBits(this.bottom) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        l2 = this.left != 0.0 ? Double.doubleToLongBits(this.left) : 0L;
        n2 = 31 * n2 + (int)(l2 ^ l2 >>> 32);
        n2 = 31 * n2 + (this.topAsPercentage ? 1 : 0);
        n2 = 31 * n2 + (this.rightAsPercentage ? 1 : 0);
        n2 = 31 * n2 + (this.bottomAsPercentage ? 1 : 0);
        this.hash = n2 = 31 * n2 + (this.leftAsPercentage ? 1 : 0);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        BorderWidths borderWidths = (BorderWidths)object;
        if (this.hash != borderWidths.hash) {
            return false;
        }
        if (Double.compare(borderWidths.bottom, this.bottom) != 0) {
            return false;
        }
        if (this.bottomAsPercentage != borderWidths.bottomAsPercentage) {
            return false;
        }
        if (Double.compare(borderWidths.left, this.left) != 0) {
            return false;
        }
        if (this.leftAsPercentage != borderWidths.leftAsPercentage) {
            return false;
        }
        if (Double.compare(borderWidths.right, this.right) != 0) {
            return false;
        }
        if (this.rightAsPercentage != borderWidths.rightAsPercentage) {
            return false;
        }
        if (Double.compare(borderWidths.top, this.top) != 0) {
            return false;
        }
        return this.topAsPercentage == borderWidths.topAsPercentage;
    }

    public int hashCode() {
        return this.hash;
    }
}

