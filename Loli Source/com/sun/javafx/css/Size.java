/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css;

import com.sun.javafx.css.SizeUnits;
import javafx.scene.text.Font;

public final class Size {
    private final double value;
    private final SizeUnits units;

    public Size(double d2, SizeUnits sizeUnits) {
        this.value = d2;
        this.units = sizeUnits != null ? sizeUnits : SizeUnits.PX;
    }

    public double getValue() {
        return this.value;
    }

    public SizeUnits getUnits() {
        return this.units;
    }

    public boolean isAbsolute() {
        return this.units.isAbsolute();
    }

    public double points(Font font) {
        return this.points(1.0, font);
    }

    public double points(double d2, Font font) {
        return this.units.points(this.value, d2, font);
    }

    public double pixels(double d2, Font font) {
        return this.units.pixels(this.value, d2, font);
    }

    public double pixels(Font font) {
        return this.pixels(1.0, font);
    }

    public double pixels(double d2) {
        return this.pixels(d2, null);
    }

    public double pixels() {
        return this.pixels(1.0, null);
    }

    public String toString() {
        return Double.toString(this.value) + this.units.toString();
    }

    public int hashCode() {
        long l2 = 17L;
        l2 = 37L * l2 + Double.doubleToLongBits(this.value);
        l2 = 37L * l2 + (long)this.units.hashCode();
        return (int)(l2 ^ l2 >> 32);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        Size size = (Size)object;
        if (this.units != size.units) {
            return false;
        }
        if (this.value == size.value) {
            return true;
        }
        if (this.value > 0.0 ? size.value > 0.0 : size.value < 0.0) {
            double d2 = this.value > 0.0 ? this.value : -this.value;
            double d3 = size.value > 0.0 ? size.value : -size.value;
            double d4 = this.value - size.value;
            return !(d4 < -1.0E-6) && !(1.0E-6 < d4);
        }
        return false;
    }
}

