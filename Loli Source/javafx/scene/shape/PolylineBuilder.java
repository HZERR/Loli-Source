/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class PolylineBuilder<B extends PolylineBuilder<B>>
extends ShapeBuilder<B>
implements Builder<Polyline> {
    private boolean __set;
    private Collection<? extends Double> points;

    protected PolylineBuilder() {
    }

    public static PolylineBuilder<?> create() {
        return new PolylineBuilder();
    }

    public void applyTo(Polyline polyline) {
        super.applyTo(polyline);
        if (this.__set) {
            polyline.getPoints().addAll(this.points);
        }
    }

    public B points(Collection<? extends Double> collection) {
        this.points = collection;
        this.__set = true;
        return (B)this;
    }

    public B points(Double ... arrdouble) {
        return this.points(Arrays.asList(arrdouble));
    }

    @Override
    public Polyline build() {
        Polyline polyline = new Polyline();
        this.applyTo(polyline);
        return polyline;
    }
}

