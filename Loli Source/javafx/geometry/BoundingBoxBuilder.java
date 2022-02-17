/*
 * Decompiled with CFR 0.150.
 */
package javafx.geometry;

import javafx.geometry.BoundingBox;
import javafx.util.Builder;

@Deprecated
public class BoundingBoxBuilder<B extends BoundingBoxBuilder<B>>
implements Builder<BoundingBox> {
    private double depth;
    private double height;
    private double minX;
    private double minY;
    private double minZ;
    private double width;

    protected BoundingBoxBuilder() {
    }

    public static BoundingBoxBuilder<?> create() {
        return new BoundingBoxBuilder();
    }

    public B depth(double d2) {
        this.depth = d2;
        return (B)this;
    }

    public B height(double d2) {
        this.height = d2;
        return (B)this;
    }

    public B minX(double d2) {
        this.minX = d2;
        return (B)this;
    }

    public B minY(double d2) {
        this.minY = d2;
        return (B)this;
    }

    public B minZ(double d2) {
        this.minZ = d2;
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        return (B)this;
    }

    @Override
    public BoundingBox build() {
        BoundingBox boundingBox = new BoundingBox(this.minX, this.minY, this.minZ, this.width, this.height, this.depth);
        return boundingBox;
    }
}

