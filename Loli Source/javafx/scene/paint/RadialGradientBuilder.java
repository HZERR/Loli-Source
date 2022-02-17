/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.paint;

import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.util.Builder;

@Deprecated
public final class RadialGradientBuilder
implements Builder<RadialGradient> {
    private double centerX;
    private double centerY;
    private CycleMethod cycleMethod;
    private double focusAngle;
    private double focusDistance;
    private boolean proportional = true;
    private double radius = 1.0;
    private List<Stop> stops;

    protected RadialGradientBuilder() {
    }

    public static RadialGradientBuilder create() {
        return new RadialGradientBuilder();
    }

    public RadialGradientBuilder centerX(double d2) {
        this.centerX = d2;
        return this;
    }

    public RadialGradientBuilder centerY(double d2) {
        this.centerY = d2;
        return this;
    }

    public RadialGradientBuilder cycleMethod(CycleMethod cycleMethod) {
        this.cycleMethod = cycleMethod;
        return this;
    }

    public RadialGradientBuilder focusAngle(double d2) {
        this.focusAngle = d2;
        return this;
    }

    public RadialGradientBuilder focusDistance(double d2) {
        this.focusDistance = d2;
        return this;
    }

    public RadialGradientBuilder proportional(boolean bl) {
        this.proportional = bl;
        return this;
    }

    public RadialGradientBuilder radius(double d2) {
        this.radius = d2;
        return this;
    }

    public RadialGradientBuilder stops(List<Stop> list) {
        this.stops = list;
        return this;
    }

    public RadialGradientBuilder stops(Stop ... arrstop) {
        return this.stops(Arrays.asList(arrstop));
    }

    @Override
    public RadialGradient build() {
        RadialGradient radialGradient = new RadialGradient(this.focusAngle, this.focusDistance, this.centerX, this.centerY, this.radius, this.proportional, this.cycleMethod, this.stops);
        return radialGradient;
    }
}

