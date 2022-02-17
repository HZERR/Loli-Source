/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.paint;

import java.util.Arrays;
import java.util.List;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Builder;

@Deprecated
public final class LinearGradientBuilder
implements Builder<LinearGradient> {
    private CycleMethod cycleMethod;
    private double endX = 1.0;
    private double endY = 1.0;
    private boolean proportional = true;
    private double startX;
    private double startY;
    private List<Stop> stops;

    protected LinearGradientBuilder() {
    }

    public static LinearGradientBuilder create() {
        return new LinearGradientBuilder();
    }

    public LinearGradientBuilder cycleMethod(CycleMethod cycleMethod) {
        this.cycleMethod = cycleMethod;
        return this;
    }

    public LinearGradientBuilder endX(double d2) {
        this.endX = d2;
        return this;
    }

    public LinearGradientBuilder endY(double d2) {
        this.endY = d2;
        return this;
    }

    public LinearGradientBuilder proportional(boolean bl) {
        this.proportional = bl;
        return this;
    }

    public LinearGradientBuilder startX(double d2) {
        this.startX = d2;
        return this;
    }

    public LinearGradientBuilder startY(double d2) {
        this.startY = d2;
        return this;
    }

    public LinearGradientBuilder stops(List<Stop> list) {
        this.stops = list;
        return this;
    }

    public LinearGradientBuilder stops(Stop ... arrstop) {
        return this.stops(Arrays.asList(arrstop));
    }

    @Override
    public LinearGradient build() {
        LinearGradient linearGradient = new LinearGradient(this.startX, this.startY, this.endX, this.endY, this.proportional, this.cycleMethod, this.stops);
        return linearGradient;
    }
}

