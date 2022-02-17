/*
 * Decompiled with CFR 0.150.
 */
package javafx.animation;

import javafx.animation.Transition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public final class StrokeTransition
extends Transition {
    private Color start;
    private Color end;
    private ObjectProperty<Shape> shape;
    private static final Shape DEFAULT_SHAPE = null;
    private Shape cachedShape;
    private ObjectProperty<Duration> duration;
    private static final Duration DEFAULT_DURATION = Duration.millis(400.0);
    private ObjectProperty<Color> fromValue;
    private static final Color DEFAULT_FROM_VALUE = null;
    private ObjectProperty<Color> toValue;
    private static final Color DEFAULT_TO_VALUE = null;

    public final void setShape(Shape shape) {
        if (this.shape != null || shape != null) {
            this.shapeProperty().set(shape);
        }
    }

    public final Shape getShape() {
        return this.shape == null ? DEFAULT_SHAPE : (Shape)this.shape.get();
    }

    public final ObjectProperty<Shape> shapeProperty() {
        if (this.shape == null) {
            this.shape = new SimpleObjectProperty<Shape>(this, "shape", DEFAULT_SHAPE);
        }
        return this.shape;
    }

    public final void setDuration(Duration duration) {
        if (this.duration != null || !DEFAULT_DURATION.equals(duration)) {
            this.durationProperty().set(duration);
        }
    }

    public final Duration getDuration() {
        return this.duration == null ? DEFAULT_DURATION : (Duration)this.duration.get();
    }

    public final ObjectProperty<Duration> durationProperty() {
        if (this.duration == null) {
            this.duration = new ObjectPropertyBase<Duration>(DEFAULT_DURATION){

                @Override
                public void invalidated() {
                    try {
                        StrokeTransition.this.setCycleDuration(StrokeTransition.this.getDuration());
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        if (this.isBound()) {
                            this.unbind();
                        }
                        this.set(StrokeTransition.this.getCycleDuration());
                        throw illegalArgumentException;
                    }
                }

                @Override
                public Object getBean() {
                    return StrokeTransition.this;
                }

                @Override
                public String getName() {
                    return "duration";
                }
            };
        }
        return this.duration;
    }

    public final void setFromValue(Color color) {
        if (this.fromValue != null || color != null) {
            this.fromValueProperty().set(color);
        }
    }

    public final Color getFromValue() {
        return this.fromValue == null ? DEFAULT_FROM_VALUE : (Color)this.fromValue.get();
    }

    public final ObjectProperty<Color> fromValueProperty() {
        if (this.fromValue == null) {
            this.fromValue = new SimpleObjectProperty<Color>(this, "fromValue", DEFAULT_FROM_VALUE);
        }
        return this.fromValue;
    }

    public final void setToValue(Color color) {
        if (this.toValue != null || color != null) {
            this.toValueProperty().set(color);
        }
    }

    public final Color getToValue() {
        return this.toValue == null ? DEFAULT_TO_VALUE : (Color)this.toValue.get();
    }

    public final ObjectProperty<Color> toValueProperty() {
        if (this.toValue == null) {
            this.toValue = new SimpleObjectProperty<Color>(this, "toValue", DEFAULT_TO_VALUE);
        }
        return this.toValue;
    }

    public StrokeTransition(Duration duration, Shape shape, Color color, Color color2) {
        this.setDuration(duration);
        this.setShape(shape);
        this.setFromValue(color);
        this.setToValue(color2);
        this.setCycleDuration(duration);
    }

    public StrokeTransition(Duration duration, Color color, Color color2) {
        this(duration, null, color, color2);
    }

    public StrokeTransition(Duration duration, Shape shape) {
        this(duration, shape, null, null);
    }

    public StrokeTransition(Duration duration) {
        this(duration, null);
    }

    public StrokeTransition() {
        this(DEFAULT_DURATION, null);
    }

    @Override
    protected void interpolate(double d2) {
        Color color = this.start.interpolate(this.end, d2);
        this.cachedShape.setStroke(color);
    }

    private Shape getTargetShape() {
        Node node;
        Shape shape = this.getShape();
        if (shape == null && (node = this.getParentTargetNode()) instanceof Shape) {
            shape = (Shape)node;
        }
        return shape;
    }

    @Override
    boolean impl_startable(boolean bl) {
        if (!super.impl_startable(bl)) {
            return false;
        }
        if (!bl && this.cachedShape != null) {
            return true;
        }
        Shape shape = this.getTargetShape();
        return shape != null && (this.getFromValue() != null || shape.getStroke() instanceof Color) && this.getToValue() != null;
    }

    @Override
    void impl_sync(boolean bl) {
        super.impl_sync(bl);
        if (bl || this.cachedShape == null) {
            this.cachedShape = this.getTargetShape();
            Color color = this.getFromValue();
            this.start = color != null ? color : (Color)this.cachedShape.getStroke();
            this.end = this.getToValue();
        }
    }
}

