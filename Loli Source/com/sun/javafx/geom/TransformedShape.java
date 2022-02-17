/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;

public abstract class TransformedShape
extends Shape {
    protected final Shape delegate;
    private Shape cachedTransformedShape;

    public static TransformedShape transformedShape(Shape shape, BaseTransform baseTransform) {
        if (baseTransform.isTranslateOrIdentity()) {
            return TransformedShape.translatedShape(shape, baseTransform.getMxt(), baseTransform.getMyt());
        }
        return new General(shape, baseTransform.copy());
    }

    public static TransformedShape translatedShape(Shape shape, double d2, double d3) {
        return new Translate(shape, (float)d2, (float)d3);
    }

    protected TransformedShape(Shape shape) {
        this.delegate = shape;
    }

    public Shape getDelegateNoClone() {
        return this.delegate;
    }

    public abstract BaseTransform getTransformNoClone();

    public abstract BaseTransform adjust(BaseTransform var1);

    protected Point2D untransform(float f2, float f3) {
        Point2D point2D = new Point2D(f2, f3);
        try {
            point2D = this.getTransformNoClone().inverseTransform(point2D, point2D);
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            // empty catch block
        }
        return point2D;
    }

    protected BaseBounds untransformedBounds(float f2, float f3, float f4, float f5) {
        RectBounds rectBounds = new RectBounds(f2, f3, f2 + f4, f3 + f5);
        try {
            return this.getTransformNoClone().inverseTransform(rectBounds, rectBounds);
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            return rectBounds.makeEmpty();
        }
    }

    @Override
    public RectBounds getBounds() {
        float[] arrf = new float[4];
        Shape.accumulate(arrf, this.delegate, this.getTransformNoClone());
        return new RectBounds(arrf[0], arrf[1], arrf[2], arrf[3]);
    }

    @Override
    public boolean contains(float f2, float f3) {
        return this.delegate.contains(this.untransform(f2, f3));
    }

    private Shape getCachedTransformedShape() {
        if (this.cachedTransformedShape == null) {
            this.cachedTransformedShape = this.copy();
        }
        return this.cachedTransformedShape;
    }

    @Override
    public boolean intersects(float f2, float f3, float f4, float f5) {
        return this.getCachedTransformedShape().intersects(f2, f3, f4, f5);
    }

    @Override
    public boolean contains(float f2, float f3, float f4, float f5) {
        return this.getCachedTransformedShape().contains(f2, f3, f4, f5);
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform) {
        return this.delegate.getPathIterator(this.adjust(baseTransform));
    }

    @Override
    public PathIterator getPathIterator(BaseTransform baseTransform, float f2) {
        return this.delegate.getPathIterator(this.adjust(baseTransform), f2);
    }

    @Override
    public Shape copy() {
        return this.getTransformNoClone().createTransformedShape(this.delegate);
    }

    static final class Translate
    extends TransformedShape {
        private final float tx;
        private final float ty;
        private BaseTransform cachedTx;

        public Translate(Shape shape, float f2, float f3) {
            super(shape);
            this.tx = f2;
            this.ty = f3;
        }

        @Override
        public BaseTransform getTransformNoClone() {
            if (this.cachedTx == null) {
                this.cachedTx = BaseTransform.getTranslateInstance(this.tx, this.ty);
            }
            return this.cachedTx;
        }

        @Override
        public BaseTransform adjust(BaseTransform baseTransform) {
            if (baseTransform == null || baseTransform.isIdentity()) {
                return BaseTransform.getTranslateInstance(this.tx, this.ty);
            }
            return baseTransform.copy().deriveWithTranslation(this.tx, this.ty);
        }

        @Override
        public RectBounds getBounds() {
            RectBounds rectBounds = this.delegate.getBounds();
            rectBounds.setBounds(rectBounds.getMinX() + this.tx, rectBounds.getMinY() + this.ty, rectBounds.getMaxX() + this.tx, rectBounds.getMaxY() + this.ty);
            return rectBounds;
        }

        @Override
        public boolean contains(float f2, float f3) {
            return this.delegate.contains(f2 - this.tx, f3 - this.ty);
        }

        @Override
        public boolean intersects(float f2, float f3, float f4, float f5) {
            return this.delegate.intersects(f2 - this.tx, f3 - this.ty, f4, f5);
        }

        @Override
        public boolean contains(float f2, float f3, float f4, float f5) {
            return this.delegate.contains(f2 - this.tx, f3 - this.ty, f4, f5);
        }
    }

    static final class General
    extends TransformedShape {
        BaseTransform transform;

        General(Shape shape, BaseTransform baseTransform) {
            super(shape);
            this.transform = baseTransform;
        }

        @Override
        public BaseTransform getTransformNoClone() {
            return this.transform;
        }

        @Override
        public BaseTransform adjust(BaseTransform baseTransform) {
            if (baseTransform == null || baseTransform.isIdentity()) {
                return this.transform.copy();
            }
            return baseTransform.copy().deriveWithConcatenation(this.transform);
        }
    }
}

