/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.paint;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.util.Builder;

@Deprecated
public final class StopBuilder
implements Builder<Stop> {
    private Color color = Color.BLACK;
    private double offset;

    protected StopBuilder() {
    }

    public static StopBuilder create() {
        return new StopBuilder();
    }

    public StopBuilder color(Color color) {
        this.color = color;
        return this;
    }

    public StopBuilder offset(double d2) {
        this.offset = d2;
        return this;
    }

    @Override
    public Stop build() {
        Stop stop = new Stop(this.offset, this.color);
        return stop;
    }
}

