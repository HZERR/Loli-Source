/*
 * Decompiled with CFR 0.150.
 */
package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.transform.BaseTransform;

public abstract class TransformingPathConsumer2D
implements PathConsumer2D {
    protected PathConsumer2D out;

    public TransformingPathConsumer2D(PathConsumer2D pathConsumer2D) {
        this.out = pathConsumer2D;
    }

    public void setConsumer(PathConsumer2D pathConsumer2D) {
        this.out = pathConsumer2D;
    }

    static final class DeltaTransformFilter
    extends TransformingPathConsumer2D {
        private float Mxx;
        private float Mxy;
        private float Myx;
        private float Myy;

        DeltaTransformFilter(PathConsumer2D pathConsumer2D, float f2, float f3, float f4, float f5) {
            super(pathConsumer2D);
            this.set(f2, f3, f4, f5);
        }

        public void set(float f2, float f3, float f4, float f5) {
            this.Mxx = f2;
            this.Mxy = f3;
            this.Myx = f4;
            this.Myy = f5;
        }

        @Override
        public void moveTo(float f2, float f3) {
            this.out.moveTo(f2 * this.Mxx + f3 * this.Mxy, f2 * this.Myx + f3 * this.Myy);
        }

        @Override
        public void lineTo(float f2, float f3) {
            this.out.lineTo(f2 * this.Mxx + f3 * this.Mxy, f2 * this.Myx + f3 * this.Myy);
        }

        @Override
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo(f2 * this.Mxx + f3 * this.Mxy, f2 * this.Myx + f3 * this.Myy, f4 * this.Mxx + f5 * this.Mxy, f4 * this.Myx + f5 * this.Myy);
        }

        @Override
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo(f2 * this.Mxx + f3 * this.Mxy, f2 * this.Myx + f3 * this.Myy, f4 * this.Mxx + f5 * this.Mxy, f4 * this.Myx + f5 * this.Myy, f6 * this.Mxx + f7 * this.Mxy, f6 * this.Myx + f7 * this.Myy);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    static final class DeltaScaleFilter
    extends TransformingPathConsumer2D {
        private float sx;
        private float sy;

        public DeltaScaleFilter(PathConsumer2D pathConsumer2D, float f2, float f3) {
            super(pathConsumer2D);
            this.set(f2, f3);
        }

        public void set(float f2, float f3) {
            this.sx = f2;
            this.sy = f3;
        }

        @Override
        public void moveTo(float f2, float f3) {
            this.out.moveTo(f2 * this.sx, f3 * this.sy);
        }

        @Override
        public void lineTo(float f2, float f3) {
            this.out.lineTo(f2 * this.sx, f3 * this.sy);
        }

        @Override
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo(f2 * this.sx, f3 * this.sy, f4 * this.sx, f5 * this.sy);
        }

        @Override
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo(f2 * this.sx, f3 * this.sy, f4 * this.sx, f5 * this.sy, f6 * this.sx, f7 * this.sy);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    static final class TransformFilter
    extends TransformingPathConsumer2D {
        private float Mxx;
        private float Mxy;
        private float Mxt;
        private float Myx;
        private float Myy;
        private float Myt;

        TransformFilter(PathConsumer2D pathConsumer2D, float f2, float f3, float f4, float f5, float f6, float f7) {
            super(pathConsumer2D);
            this.set(f2, f3, f4, f5, f6, f7);
        }

        public void set(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.Mxx = f2;
            this.Mxy = f3;
            this.Mxt = f4;
            this.Myx = f5;
            this.Myy = f6;
            this.Myt = f7;
        }

        @Override
        public void moveTo(float f2, float f3) {
            this.out.moveTo(f2 * this.Mxx + f3 * this.Mxy + this.Mxt, f2 * this.Myx + f3 * this.Myy + this.Myt);
        }

        @Override
        public void lineTo(float f2, float f3) {
            this.out.lineTo(f2 * this.Mxx + f3 * this.Mxy + this.Mxt, f2 * this.Myx + f3 * this.Myy + this.Myt);
        }

        @Override
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo(f2 * this.Mxx + f3 * this.Mxy + this.Mxt, f2 * this.Myx + f3 * this.Myy + this.Myt, f4 * this.Mxx + f5 * this.Mxy + this.Mxt, f4 * this.Myx + f5 * this.Myy + this.Myt);
        }

        @Override
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo(f2 * this.Mxx + f3 * this.Mxy + this.Mxt, f2 * this.Myx + f3 * this.Myy + this.Myt, f4 * this.Mxx + f5 * this.Mxy + this.Mxt, f4 * this.Myx + f5 * this.Myy + this.Myt, f6 * this.Mxx + f7 * this.Mxy + this.Mxt, f6 * this.Myx + f7 * this.Myy + this.Myt);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    static final class ScaleTranslateFilter
    extends TransformingPathConsumer2D {
        private float sx;
        private float sy;
        private float tx;
        private float ty;

        ScaleTranslateFilter(PathConsumer2D pathConsumer2D, float f2, float f3, float f4, float f5) {
            super(pathConsumer2D);
            this.set(f2, f3, f4, f5);
        }

        public void set(float f2, float f3, float f4, float f5) {
            this.sx = f2;
            this.sy = f3;
            this.tx = f4;
            this.ty = f5;
        }

        @Override
        public void moveTo(float f2, float f3) {
            this.out.moveTo(f2 * this.sx + this.tx, f3 * this.sy + this.ty);
        }

        @Override
        public void lineTo(float f2, float f3) {
            this.out.lineTo(f2 * this.sx + this.tx, f3 * this.sy + this.ty);
        }

        @Override
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo(f2 * this.sx + this.tx, f3 * this.sy + this.ty, f4 * this.sx + this.tx, f5 * this.sy + this.ty);
        }

        @Override
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo(f2 * this.sx + this.tx, f3 * this.sy + this.ty, f4 * this.sx + this.tx, f5 * this.sy + this.ty, f6 * this.sx + this.tx, f7 * this.sy + this.ty);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    static final class TranslateFilter
    extends TransformingPathConsumer2D {
        private float tx;
        private float ty;

        TranslateFilter(PathConsumer2D pathConsumer2D, float f2, float f3) {
            super(pathConsumer2D);
            this.set(f2, f3);
        }

        public void set(float f2, float f3) {
            this.tx = f2;
            this.ty = f3;
        }

        @Override
        public void moveTo(float f2, float f3) {
            this.out.moveTo(f2 + this.tx, f3 + this.ty);
        }

        @Override
        public void lineTo(float f2, float f3) {
            this.out.lineTo(f2 + this.tx, f3 + this.ty);
        }

        @Override
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo(f2 + this.tx, f3 + this.ty, f4 + this.tx, f5 + this.ty);
        }

        @Override
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo(f2 + this.tx, f3 + this.ty, f4 + this.tx, f5 + this.ty, f6 + this.tx, f7 + this.ty);
        }

        @Override
        public void closePath() {
            this.out.closePath();
        }

        @Override
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    public static final class FilterSet {
        private TranslateFilter translater;
        private DeltaScaleFilter deltascaler;
        private ScaleTranslateFilter scaletranslater;
        private DeltaTransformFilter deltatransformer;
        private TransformFilter transformer;

        public PathConsumer2D getConsumer(PathConsumer2D pathConsumer2D, BaseTransform baseTransform) {
            if (baseTransform == null) {
                return pathConsumer2D;
            }
            float f2 = (float)baseTransform.getMxx();
            float f3 = (float)baseTransform.getMxy();
            float f4 = (float)baseTransform.getMxt();
            float f5 = (float)baseTransform.getMyx();
            float f6 = (float)baseTransform.getMyy();
            float f7 = (float)baseTransform.getMyt();
            if (f3 == 0.0f && f5 == 0.0f) {
                if (f2 == 1.0f && f6 == 1.0f) {
                    if (f4 == 0.0f && f7 == 0.0f) {
                        return pathConsumer2D;
                    }
                    if (this.translater == null) {
                        this.translater = new TranslateFilter(pathConsumer2D, f4, f7);
                    } else {
                        this.translater.set(f4, f7);
                    }
                    return this.translater;
                }
                if (f4 == 0.0f && f7 == 0.0f) {
                    if (this.deltascaler == null) {
                        this.deltascaler = new DeltaScaleFilter(pathConsumer2D, f2, f6);
                    } else {
                        this.deltascaler.set(f2, f6);
                    }
                    return this.deltascaler;
                }
                if (this.scaletranslater == null) {
                    this.scaletranslater = new ScaleTranslateFilter(pathConsumer2D, f2, f6, f4, f7);
                } else {
                    this.scaletranslater.set(f2, f6, f4, f7);
                }
                return this.scaletranslater;
            }
            if (f4 == 0.0f && f7 == 0.0f) {
                if (this.deltatransformer == null) {
                    this.deltatransformer = new DeltaTransformFilter(pathConsumer2D, f2, f3, f5, f6);
                } else {
                    this.deltatransformer.set(f2, f3, f5, f6);
                }
                return this.deltatransformer;
            }
            if (this.transformer == null) {
                this.transformer = new TransformFilter(pathConsumer2D, f2, f3, f4, f5, f6, f7);
            } else {
                this.transformer.set(f2, f3, f4, f5, f6, f7);
            }
            return this.transformer;
        }
    }
}

