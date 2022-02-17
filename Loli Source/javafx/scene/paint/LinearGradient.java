/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.paint;

import com.sun.javafx.scene.paint.GradientUtils;
import com.sun.javafx.tk.Toolkit;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;

public final class LinearGradient
extends Paint {
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private boolean proportional;
    private CycleMethod cycleMethod;
    private List<Stop> stops;
    private final boolean opaque;
    private Object platformPaint;
    private int hash;

    public final double getStartX() {
        return this.startX;
    }

    public final double getStartY() {
        return this.startY;
    }

    public final double getEndX() {
        return this.endX;
    }

    public final double getEndY() {
        return this.endY;
    }

    public final boolean isProportional() {
        return this.proportional;
    }

    public final CycleMethod getCycleMethod() {
        return this.cycleMethod;
    }

    public final List<Stop> getStops() {
        return this.stops;
    }

    @Override
    public final boolean isOpaque() {
        return this.opaque;
    }

    public LinearGradient(@NamedArg(value="startX") double d2, @NamedArg(value="startY") double d3, @NamedArg(value="endX", defaultValue="1") double d4, @NamedArg(value="endY", defaultValue="1") double d5, @NamedArg(value="proportional", defaultValue="true") boolean bl, @NamedArg(value="cycleMethod") CycleMethod cycleMethod, Stop ... arrstop) {
        this.startX = d2;
        this.startY = d3;
        this.endX = d4;
        this.endY = d5;
        this.proportional = bl;
        this.cycleMethod = cycleMethod == null ? CycleMethod.NO_CYCLE : cycleMethod;
        this.stops = Stop.normalize(arrstop);
        this.opaque = this.determineOpacity();
    }

    public LinearGradient(@NamedArg(value="startX") double d2, @NamedArg(value="startY") double d3, @NamedArg(value="endX", defaultValue="1") double d4, @NamedArg(value="endY", defaultValue="1") double d5, @NamedArg(value="proportional", defaultValue="true") boolean bl, @NamedArg(value="cycleMethod") CycleMethod cycleMethod, @NamedArg(value="stops") List<Stop> list) {
        this.startX = d2;
        this.startY = d3;
        this.endX = d4;
        this.endY = d5;
        this.proportional = bl;
        this.cycleMethod = cycleMethod == null ? CycleMethod.NO_CYCLE : cycleMethod;
        this.stops = Stop.normalize(list);
        this.opaque = this.determineOpacity();
    }

    private boolean determineOpacity() {
        int n2 = this.stops.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (this.stops.get(i2).getColor().isOpaque()) continue;
            return false;
        }
        return true;
    }

    @Override
    Object acc_getPlatformPaint() {
        if (this.platformPaint == null) {
            this.platformPaint = Toolkit.getToolkit().getPaint((Paint)this);
        }
        return this.platformPaint;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object instanceof LinearGradient) {
            LinearGradient linearGradient = (LinearGradient)object;
            if (this.startX != linearGradient.startX || this.startY != linearGradient.startY || this.endX != linearGradient.endX || this.endY != linearGradient.endY || this.proportional != linearGradient.proportional || this.cycleMethod != linearGradient.cycleMethod) {
                return false;
            }
            return this.stops.equals(linearGradient.stops);
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 17L;
            l2 = 37L * l2 + Double.doubleToLongBits(this.startX);
            l2 = 37L * l2 + Double.doubleToLongBits(this.startY);
            l2 = 37L * l2 + Double.doubleToLongBits(this.endX);
            l2 = 37L * l2 + Double.doubleToLongBits(this.endY);
            l2 = 37L * l2 + (this.proportional ? 1231L : 1237L);
            l2 = 37L * l2 + (long)this.cycleMethod.hashCode();
            for (Stop stop : this.stops) {
                l2 = 37L * l2 + (long)stop.hashCode();
            }
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("linear-gradient(from ").append(GradientUtils.lengthToString(this.startX, this.proportional)).append(" ").append(GradientUtils.lengthToString(this.startY, this.proportional)).append(" to ").append(GradientUtils.lengthToString(this.endX, this.proportional)).append(" ").append(GradientUtils.lengthToString(this.endY, this.proportional)).append(", ");
        switch (this.cycleMethod) {
            case REFLECT: {
                stringBuilder.append("reflect").append(", ");
                break;
            }
            case REPEAT: {
                stringBuilder.append("repeat").append(", ");
            }
        }
        for (Stop stop : this.stops) {
            stringBuilder.append(stop).append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static LinearGradient valueOf(String string) {
        GradientUtils.Parser parser;
        if (string == null) {
            throw new NullPointerException("gradient must be specified");
        }
        String string2 = "linear-gradient(";
        String string3 = ")";
        if (string.startsWith(string2)) {
            if (!string.endsWith(string3)) {
                throw new IllegalArgumentException("Invalid gradient specification, must end with \"" + string3 + '\"');
            }
            string = string.substring(string2.length(), string.length() - string3.length());
        }
        if ((parser = new GradientUtils.Parser(string)).getSize() < 2) {
            throw new IllegalArgumentException("Invalid gradient specification");
        }
        GradientUtils.Point point = GradientUtils.Point.MIN;
        GradientUtils.Point point2 = GradientUtils.Point.MIN;
        GradientUtils.Point point3 = GradientUtils.Point.MIN;
        GradientUtils.Point point4 = GradientUtils.Point.MIN;
        String[] arrstring = parser.splitCurrentToken();
        if ("from".equals(arrstring[0])) {
            GradientUtils.Parser.checkNumberOfArguments(arrstring, 5);
            point = parser.parsePoint(arrstring[1]);
            point2 = parser.parsePoint(arrstring[2]);
            if (!"to".equals(arrstring[3])) {
                throw new IllegalArgumentException("Invalid gradient specification, \"to\" expected");
            }
            point3 = parser.parsePoint(arrstring[4]);
            point4 = parser.parsePoint(arrstring[5]);
            parser.shift();
        } else if ("to".equals(arrstring[0])) {
            int n2 = 0;
            int n3 = 0;
            for (int i2 = 1; i2 < 3 && i2 < arrstring.length; ++i2) {
                if ("left".equals(arrstring[i2])) {
                    point = GradientUtils.Point.MAX;
                    point3 = GradientUtils.Point.MIN;
                    ++n2;
                    continue;
                }
                if ("right".equals(arrstring[i2])) {
                    point = GradientUtils.Point.MIN;
                    point3 = GradientUtils.Point.MAX;
                    ++n2;
                    continue;
                }
                if ("top".equals(arrstring[i2])) {
                    point2 = GradientUtils.Point.MAX;
                    point4 = GradientUtils.Point.MIN;
                    ++n3;
                    continue;
                }
                if ("bottom".equals(arrstring[i2])) {
                    point2 = GradientUtils.Point.MIN;
                    point4 = GradientUtils.Point.MAX;
                    ++n3;
                    continue;
                }
                throw new IllegalArgumentException("Invalid gradient specification, unknown value after 'to'");
            }
            if (n3 > 1) {
                throw new IllegalArgumentException("Invalid gradient specification, vertical direction set twice after 'to'");
            }
            if (n2 > 1) {
                throw new IllegalArgumentException("Invalid gradient specification, horizontal direction set twice after 'to'");
            }
            parser.shift();
        } else {
            point2 = GradientUtils.Point.MIN;
            point4 = GradientUtils.Point.MAX;
        }
        CycleMethod cycleMethod = CycleMethod.NO_CYCLE;
        String string4 = parser.getCurrentToken();
        if ("repeat".equals(string4)) {
            cycleMethod = CycleMethod.REPEAT;
            parser.shift();
        } else if ("reflect".equals(string4)) {
            cycleMethod = CycleMethod.REFLECT;
            parser.shift();
        }
        double d2 = 0.0;
        if (!point.proportional) {
            double d3 = point3.value - point.value;
            double d4 = point4.value - point2.value;
            d2 = Math.sqrt(d3 * d3 + d4 * d4);
        }
        Stop[] arrstop = parser.parseStops(point.proportional, d2);
        return new LinearGradient(point.value, point2.value, point3.value, point4.value, point.proportional, cycleMethod, arrstop);
    }
}

