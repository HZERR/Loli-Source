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

public final class RadialGradient
extends Paint {
    private double focusAngle;
    private double focusDistance;
    private double centerX;
    private double centerY;
    private double radius;
    private boolean proportional;
    private CycleMethod cycleMethod;
    private List<Stop> stops;
    private final boolean opaque;
    private Object platformPaint;
    private int hash;

    public final double getFocusAngle() {
        return this.focusAngle;
    }

    public final double getFocusDistance() {
        return this.focusDistance;
    }

    public final double getCenterX() {
        return this.centerX;
    }

    public final double getCenterY() {
        return this.centerY;
    }

    public final double getRadius() {
        return this.radius;
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

    public RadialGradient(@NamedArg(value="focusAngle") double d2, @NamedArg(value="focusDistance") double d3, @NamedArg(value="centerX") double d4, @NamedArg(value="centerY") double d5, @NamedArg(value="radius", defaultValue="1") double d6, @NamedArg(value="proportional", defaultValue="true") boolean bl, @NamedArg(value="cycleMethod") CycleMethod cycleMethod, Stop ... arrstop) {
        this.focusAngle = d2;
        this.focusDistance = d3;
        this.centerX = d4;
        this.centerY = d5;
        this.radius = d6;
        this.proportional = bl;
        this.cycleMethod = cycleMethod == null ? CycleMethod.NO_CYCLE : cycleMethod;
        this.stops = Stop.normalize(arrstop);
        this.opaque = this.determineOpacity();
    }

    public RadialGradient(@NamedArg(value="focusAngle") double d2, @NamedArg(value="focusDistance") double d3, @NamedArg(value="centerX") double d4, @NamedArg(value="centerY") double d5, @NamedArg(value="radius", defaultValue="1") double d6, @NamedArg(value="proportional", defaultValue="true") boolean bl, @NamedArg(value="cycleMethod") CycleMethod cycleMethod, @NamedArg(value="stops") List<Stop> list) {
        this.focusAngle = d2;
        this.focusDistance = d3;
        this.centerX = d4;
        this.centerY = d5;
        this.radius = d6;
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
        if (object == this) {
            return true;
        }
        if (object instanceof RadialGradient) {
            RadialGradient radialGradient = (RadialGradient)object;
            if (this.focusAngle != radialGradient.focusAngle || this.focusDistance != radialGradient.focusDistance || this.centerX != radialGradient.centerX || this.centerY != radialGradient.centerY || this.radius != radialGradient.radius || this.proportional != radialGradient.proportional || this.cycleMethod != radialGradient.cycleMethod) {
                return false;
            }
            return this.stops.equals(radialGradient.stops);
        }
        return false;
    }

    public int hashCode() {
        if (this.hash == 0) {
            long l2 = 17L;
            l2 = 37L * l2 + Double.doubleToLongBits(this.focusAngle);
            l2 = 37L * l2 + Double.doubleToLongBits(this.focusDistance);
            l2 = 37L * l2 + Double.doubleToLongBits(this.centerX);
            l2 = 37L * l2 + Double.doubleToLongBits(this.centerY);
            l2 = 37L * l2 + Double.doubleToLongBits(this.radius);
            l2 = 37L * l2 + (long)(this.proportional ? 1231 : 1237);
            l2 = 37L * l2 + (long)this.cycleMethod.hashCode();
            for (Stop stop : this.stops) {
                l2 = 37L * l2 + (long)stop.hashCode();
            }
            this.hash = (int)(l2 ^ l2 >> 32);
        }
        return this.hash;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("radial-gradient(focus-angle ").append(this.focusAngle).append("deg, focus-distance ").append(this.focusDistance * 100.0).append("% , center ").append(GradientUtils.lengthToString(this.centerX, this.proportional)).append(" ").append(GradientUtils.lengthToString(this.centerY, this.proportional)).append(", radius ").append(GradientUtils.lengthToString(this.radius, this.proportional)).append(", ");
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

    public static RadialGradient valueOf(String string) {
        GradientUtils.Point point;
        GradientUtils.Point point2;
        GradientUtils.Parser parser;
        if (string == null) {
            throw new NullPointerException("gradient must be specified");
        }
        String string2 = "radial-gradient(";
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
        double d2 = 0.0;
        double d3 = 0.0;
        String[] arrstring = parser.splitCurrentToken();
        if ("focus-angle".equals(arrstring[0])) {
            GradientUtils.Parser.checkNumberOfArguments(arrstring, 1);
            d2 = GradientUtils.Parser.parseAngle(arrstring[1]);
            parser.shift();
        }
        if ("focus-distance".equals((arrstring = parser.splitCurrentToken())[0])) {
            GradientUtils.Parser.checkNumberOfArguments(arrstring, 1);
            d3 = GradientUtils.Parser.parsePercentage(arrstring[1]);
            parser.shift();
        }
        if ("center".equals((arrstring = parser.splitCurrentToken())[0])) {
            GradientUtils.Parser.checkNumberOfArguments(arrstring, 2);
            point2 = parser.parsePoint(arrstring[1]);
            point = parser.parsePoint(arrstring[2]);
            parser.shift();
        } else {
            point2 = GradientUtils.Point.MIN;
            point = GradientUtils.Point.MIN;
        }
        arrstring = parser.splitCurrentToken();
        if (!"radius".equals(arrstring[0])) {
            throw new IllegalArgumentException("Invalid gradient specification: radius must be specified");
        }
        GradientUtils.Parser.checkNumberOfArguments(arrstring, 1);
        GradientUtils.Point point3 = parser.parsePoint(arrstring[1]);
        parser.shift();
        CycleMethod cycleMethod = CycleMethod.NO_CYCLE;
        String string4 = parser.getCurrentToken();
        if ("repeat".equals(string4)) {
            cycleMethod = CycleMethod.REPEAT;
            parser.shift();
        } else if ("reflect".equals(string4)) {
            cycleMethod = CycleMethod.REFLECT;
            parser.shift();
        }
        Stop[] arrstop = parser.parseStops(point3.proportional, point3.value);
        return new RadialGradient(d2, d3, point2.value, point.value, point3.value, point3.proportional, cycleMethod, arrstop);
    }
}

