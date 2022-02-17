/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.shape;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.ShapeBuilder;
import javafx.util.Builder;

@Deprecated
public class PolygonBuilder<B extends PolygonBuilder<B>>
extends ShapeBuilder<B>
implements Builder<Polygon> {
    private boolean __set;
    private Collection<? extends Double> points;

    protected PolygonBuilder() {
    }

    public static PolygonBuilder<?> create() {
        return new PolygonBuilder();
    }

    public void applyTo(Polygon polygon) {
        super.applyTo(polygon);
        if (this.__set) {
            polygon.getPoints().addAll(this.points);
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
    public Polygon build() {
        Polygon polygon = new Polygon();
        this.applyTo(polygon);
        return polygon;
    }
}

