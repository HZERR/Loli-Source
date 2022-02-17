/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.FlatteningPathIterator;
import com.sun.javafx.geom.IllegalPathStateException;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Vector;

class ShapeEvaluator {
    private Shape savedv0;
    private Shape savedv1;
    private Geometry geom0;
    private Geometry geom1;

    ShapeEvaluator() {
    }

    public Shape evaluate(Shape shape, Shape shape2, float f2) {
        if (this.savedv0 != shape || this.savedv1 != shape2) {
            if (this.savedv0 == shape2 && this.savedv1 == shape) {
                Geometry geometry = this.geom0;
                this.geom0 = this.geom1;
                this.geom1 = geometry;
            } else {
                this.recalculate(shape, shape2);
            }
            this.savedv0 = shape;
            this.savedv1 = shape2;
        }
        return this.getShape(f2);
    }

    private void recalculate(Shape shape, Shape shape2) {
        this.geom0 = new Geometry(shape);
        this.geom1 = new Geometry(shape2);
        float[] arrf = this.geom0.getTvals();
        float[] arrf2 = this.geom1.getTvals();
        float[] arrf3 = ShapeEvaluator.mergeTvals(arrf, arrf2);
        this.geom0.setTvals(arrf3);
        this.geom1.setTvals(arrf3);
    }

    private Shape getShape(float f2) {
        return new MorphedShape(this.geom0, this.geom1, f2);
    }

    private static float[] mergeTvals(float[] arrf, float[] arrf2) {
        int n2 = ShapeEvaluator.sortTvals(arrf, arrf2, null);
        float[] arrf3 = new float[n2];
        ShapeEvaluator.sortTvals(arrf, arrf2, arrf3);
        return arrf3;
    }

    private static int sortTvals(float[] arrf, float[] arrf2, float[] arrf3) {
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        while (n2 < arrf.length && n3 < arrf2.length) {
            float f2 = arrf[n2];
            float f3 = arrf2[n3];
            if (f2 <= f3) {
                if (arrf3 != null) {
                    arrf3[n4] = f2;
                }
                ++n2;
            }
            if (f3 <= f2) {
                if (arrf3 != null) {
                    arrf3[n4] = f3;
                }
                ++n3;
            }
            ++n4;
        }
        return n4;
    }

    private static float interp(float f2, float f3, float f4) {
        return f2 + (f3 - f2) * f4;
    }

    private static class Iterator
    implements PathIterator {
        BaseTransform at;
        Geometry g0;
        Geometry g1;
        float t;
        int cindex;

        public Iterator(BaseTransform baseTransform, Geometry geometry, Geometry geometry2, float f2) {
            this.at = baseTransform;
            this.g0 = geometry;
            this.g1 = geometry2;
            this.t = f2;
        }

        @Override
        public int getWindingRule() {
            return (double)this.t < 0.5 ? this.g0.getWindingRule() : this.g1.getWindingRule();
        }

        @Override
        public boolean isDone() {
            return this.cindex > this.g0.getNumCoords();
        }

        @Override
        public void next() {
            this.cindex = this.cindex == 0 ? 2 : (this.cindex += 6);
        }

        @Override
        public int currentSegment(float[] arrf) {
            int n2;
            int n3;
            if (this.cindex == 0) {
                n3 = 0;
                n2 = 2;
            } else if (this.cindex >= this.g0.getNumCoords()) {
                n3 = 4;
                n2 = 0;
            } else {
                n3 = 3;
                n2 = 6;
            }
            if (n2 > 0) {
                for (int i2 = 0; i2 < n2; ++i2) {
                    arrf[i2] = ShapeEvaluator.interp(this.g0.getCoord(this.cindex + i2), this.g1.getCoord(this.cindex + i2), this.t);
                }
                if (this.at != null) {
                    this.at.transform(arrf, 0, arrf, 0, n2 / 2);
                }
            }
            return n3;
        }
    }

    private static class MorphedShape
    extends Shape {
        Geometry geom0;
        Geometry geom1;
        float t;

        MorphedShape(Geometry geometry, Geometry geometry2, float f2) {
            this.geom0 = geometry;
            this.geom1 = geometry2;
            this.t = f2;
        }

        public Rectangle getRectangle() {
            return new Rectangle(this.getBounds());
        }

        @Override
        public RectBounds getBounds() {
            float f2;
            float f3;
            int n2 = this.geom0.getNumCoords();
            float f4 = f3 = ShapeEvaluator.interp(this.geom0.getCoord(0), this.geom1.getCoord(0), this.t);
            float f5 = f2 = ShapeEvaluator.interp(this.geom0.getCoord(1), this.geom1.getCoord(1), this.t);
            for (int i2 = 2; i2 < n2; i2 += 2) {
                float f6 = ShapeEvaluator.interp(this.geom0.getCoord(i2), this.geom1.getCoord(i2), this.t);
                float f7 = ShapeEvaluator.interp(this.geom0.getCoord(i2 + 1), this.geom1.getCoord(i2 + 1), this.t);
                if (f4 > f6) {
                    f4 = f6;
                }
                if (f5 > f7) {
                    f5 = f7;
                }
                if (f3 < f6) {
                    f3 = f6;
                }
                if (!(f2 < f7)) continue;
                f2 = f7;
            }
            return new RectBounds(f4, f5, f3, f2);
        }

        @Override
        public boolean contains(float f2, float f3) {
            return Path2D.contains(this.getPathIterator(null), f2, f3);
        }

        @Override
        public boolean intersects(float f2, float f3, float f4, float f5) {
            return Path2D.intersects(this.getPathIterator(null), f2, f3, f4, f5);
        }

        @Override
        public boolean contains(float f2, float f3, float f4, float f5) {
            return Path2D.contains(this.getPathIterator(null), f2, f3, f4, f5);
        }

        @Override
        public PathIterator getPathIterator(BaseTransform baseTransform) {
            return new Iterator(baseTransform, this.geom0, this.geom1, this.t);
        }

        @Override
        public PathIterator getPathIterator(BaseTransform baseTransform, float f2) {
            return new FlatteningPathIterator(this.getPathIterator(baseTransform), f2);
        }

        @Override
        public Shape copy() {
            return new Path2D(this);
        }
    }

    private static class Geometry {
        static final float THIRD = 0.33333334f;
        static final float MIN_LEN = 0.001f;
        float[] bezierCoords = new float[20];
        int numCoords;
        int windingrule;
        float[] myTvals;

        public Geometry(Shape shape) {
            int n2;
            int n3;
            float f2;
            float f3;
            float f4;
            float f5;
            PathIterator pathIterator = shape.getPathIterator(null);
            this.windingrule = pathIterator.getWindingRule();
            if (pathIterator.isDone()) {
                this.numCoords = 8;
            }
            float[] arrf = new float[6];
            int n4 = pathIterator.currentSegment(arrf);
            pathIterator.next();
            if (n4 != 0) {
                throw new IllegalPathStateException("missing initial moveto");
            }
            float f6 = f5 = arrf[0];
            this.bezierCoords[0] = f5;
            float f7 = f4 = arrf[1];
            this.bezierCoords[1] = f4;
            Vector<Point2D> vector = new Vector<Point2D>();
            this.numCoords = 2;
            while (!pathIterator.isDone()) {
                switch (pathIterator.currentSegment(arrf)) {
                    case 0: {
                        if (f6 != f5 || f7 != f4) {
                            this.appendLineTo(f6, f7, f5, f4);
                            f6 = f5;
                            f7 = f4;
                        }
                        f3 = arrf[0];
                        f2 = arrf[1];
                        if (f6 == f3 && f7 == f2) break;
                        vector.add(new Point2D(f5, f4));
                        this.appendLineTo(f6, f7, f3, f2);
                        f6 = f5 = f3;
                        f7 = f4 = f2;
                        break;
                    }
                    case 4: {
                        if (f6 == f5 && f7 == f4) break;
                        this.appendLineTo(f6, f7, f5, f4);
                        f6 = f5;
                        f7 = f4;
                        break;
                    }
                    case 1: {
                        f3 = arrf[0];
                        f2 = arrf[1];
                        this.appendLineTo(f6, f7, f3, f2);
                        f6 = f3;
                        f7 = f2;
                        break;
                    }
                    case 2: {
                        float f8 = arrf[0];
                        float f9 = arrf[1];
                        f3 = arrf[2];
                        f2 = arrf[3];
                        this.appendQuadTo(f6, f7, f8, f9, f3, f2);
                        f6 = f3;
                        f7 = f2;
                        break;
                    }
                    case 3: {
                        f6 = arrf[4];
                        f7 = arrf[5];
                        this.appendCubicTo(arrf[0], arrf[1], arrf[2], arrf[3], f6, f7);
                    }
                }
                pathIterator.next();
            }
            if (this.numCoords < 8 || f6 != f5 || f7 != f4) {
                this.appendLineTo(f6, f7, f5, f4);
                f6 = f5;
                f7 = f4;
            }
            for (n3 = vector.size() - 1; n3 >= 0; --n3) {
                Point2D point2D = (Point2D)vector.get(n3);
                f3 = point2D.x;
                f2 = point2D.y;
                if (f6 == f3 && f7 == f2) continue;
                this.appendLineTo(f6, f7, f3, f2);
                f6 = f3;
                f7 = f2;
            }
            n3 = 0;
            float f10 = this.bezierCoords[0];
            float f11 = this.bezierCoords[1];
            for (int i2 = 6; i2 < this.numCoords; i2 += 6) {
                float f12 = this.bezierCoords[i2];
                float f13 = this.bezierCoords[i2 + 1];
                if (!(f13 < f11) && (f13 != f11 || !(f12 < f10))) continue;
                n3 = i2;
                f10 = f12;
                f11 = f13;
            }
            if (n3 > 0) {
                float[] arrf2 = new float[this.numCoords];
                System.arraycopy(this.bezierCoords, n3, arrf2, 0, this.numCoords - n3);
                System.arraycopy(this.bezierCoords, 2, arrf2, this.numCoords - n3, n3);
                this.bezierCoords = arrf2;
            }
            float f14 = 0.0f;
            f6 = this.bezierCoords[0];
            f7 = this.bezierCoords[1];
            for (n2 = 2; n2 < this.numCoords; n2 += 2) {
                f3 = this.bezierCoords[n2];
                f2 = this.bezierCoords[n2 + 1];
                f14 += f6 * f2 - f3 * f7;
                f6 = f3;
                f7 = f2;
            }
            if (f14 < 0.0f) {
                n2 = 2;
                for (int i3 = this.numCoords - 4; n2 < i3; n2 += 2, i3 -= 2) {
                    f6 = this.bezierCoords[n2];
                    f7 = this.bezierCoords[n2 + 1];
                    this.bezierCoords[n2] = this.bezierCoords[i3];
                    this.bezierCoords[n2 + 1] = this.bezierCoords[i3 + 1];
                    this.bezierCoords[i3] = f6;
                    this.bezierCoords[i3 + 1] = f7;
                }
            }
        }

        private void appendLineTo(float f2, float f3, float f4, float f5) {
            this.appendCubicTo(ShapeEvaluator.interp(f2, f4, 0.33333334f), ShapeEvaluator.interp(f3, f5, 0.33333334f), ShapeEvaluator.interp(f4, f2, 0.33333334f), ShapeEvaluator.interp(f5, f3, 0.33333334f), f4, f5);
        }

        private void appendQuadTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.appendCubicTo(ShapeEvaluator.interp(f4, f2, 0.33333334f), ShapeEvaluator.interp(f5, f3, 0.33333334f), ShapeEvaluator.interp(f4, f6, 0.33333334f), ShapeEvaluator.interp(f5, f7, 0.33333334f), f6, f7);
        }

        private void appendCubicTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            if (this.numCoords + 6 > this.bezierCoords.length) {
                int n2 = (this.numCoords - 2) * 2 + 2;
                float[] arrf = new float[n2];
                System.arraycopy(this.bezierCoords, 0, arrf, 0, this.numCoords);
                this.bezierCoords = arrf;
            }
            this.bezierCoords[this.numCoords++] = f2;
            this.bezierCoords[this.numCoords++] = f3;
            this.bezierCoords[this.numCoords++] = f4;
            this.bezierCoords[this.numCoords++] = f5;
            this.bezierCoords[this.numCoords++] = f6;
            this.bezierCoords[this.numCoords++] = f7;
        }

        public int getWindingRule() {
            return this.windingrule;
        }

        public int getNumCoords() {
            return this.numCoords;
        }

        public float getCoord(int n2) {
            return this.bezierCoords[n2];
        }

        public float[] getTvals() {
            float f2;
            float f3;
            if (this.myTvals != null) {
                return this.myTvals;
            }
            float[] arrf = new float[(this.numCoords - 2) / 6 + 1];
            float f4 = this.bezierCoords[0];
            float f5 = this.bezierCoords[1];
            float f6 = 0.0f;
            int n2 = 2;
            int n3 = 0;
            while (n2 < this.numCoords) {
                f3 = f4;
                f2 = f5;
                float f7 = this.bezierCoords[n2++];
                float f8 = this.bezierCoords[n2++];
                float f9 = (float)Math.sqrt((f3 -= f7) * f3 + (f2 -= f8) * f2);
                f3 = f7;
                f2 = f8;
                f7 = this.bezierCoords[n2++];
                f8 = this.bezierCoords[n2++];
                f9 += (float)Math.sqrt((f3 -= f7) * f3 + (f2 -= f8) * f2);
                f3 = f7;
                f2 = f8;
                f7 = this.bezierCoords[n2++];
                f8 = this.bezierCoords[n2++];
                f9 += (float)Math.sqrt((f3 -= f7) * f3 + (f2 -= f8) * f2);
                f9 += (float)Math.sqrt((f4 -= f7) * f4 + (f5 -= f8) * f5);
                if ((f9 /= 2.0f) < 0.001f) {
                    f9 = 0.001f;
                }
                arrf[n3++] = f6 += f9;
                f4 = f7;
                f5 = f8;
            }
            f3 = arrf[0];
            arrf[0] = 0.0f;
            for (n3 = 1; n3 < arrf.length - 1; ++n3) {
                f2 = arrf[n3];
                arrf[n3] = f3 / f6;
                f3 = f2;
            }
            arrf[n3] = 1.0f;
            this.myTvals = arrf;
            return arrf;
        }

        public void setTvals(float[] arrf) {
            float f2;
            float f3;
            float[] arrf2 = this.bezierCoords;
            float[] arrf3 = new float[2 + (arrf.length - 1) * 6];
            float[] arrf4 = this.getTvals();
            int n2 = 0;
            float f4 = f3 = arrf2[n2++];
            float f5 = f3;
            float f6 = f3;
            float f7 = f2 = arrf2[n2++];
            float f8 = f2;
            float f9 = f2;
            int n3 = 0;
            arrf3[n3++] = f6;
            arrf3[n3++] = f9;
            float f10 = 0.0f;
            float f11 = 0.0f;
            int n4 = 1;
            int n5 = 1;
            while (n5 < arrf.length) {
                if (f10 >= f11) {
                    f6 = f3;
                    f9 = f2;
                    f5 = arrf2[n2++];
                    f8 = arrf2[n2++];
                    f4 = arrf2[n2++];
                    f7 = arrf2[n2++];
                    f3 = arrf2[n2++];
                    f2 = arrf2[n2++];
                    f11 = arrf4[n4++];
                }
                int n6 = n5++;
                float f12 = arrf[n6];
                if (f12 < f11) {
                    float f13 = (f12 - f10) / (f11 - f10);
                    arrf3[n3++] = f6 = ShapeEvaluator.interp(f6, f5, f13);
                    arrf3[n3++] = f9 = ShapeEvaluator.interp(f9, f8, f13);
                    f5 = ShapeEvaluator.interp(f5, f4, f13);
                    f8 = ShapeEvaluator.interp(f8, f7, f13);
                    f4 = ShapeEvaluator.interp(f4, f3, f13);
                    f7 = ShapeEvaluator.interp(f7, f2, f13);
                    arrf3[n3++] = f6 = ShapeEvaluator.interp(f6, f5, f13);
                    arrf3[n3++] = f9 = ShapeEvaluator.interp(f9, f8, f13);
                    f5 = ShapeEvaluator.interp(f5, f4, f13);
                    f8 = ShapeEvaluator.interp(f8, f7, f13);
                    arrf3[n3++] = f6 = ShapeEvaluator.interp(f6, f5, f13);
                    arrf3[n3++] = f9 = ShapeEvaluator.interp(f9, f8, f13);
                } else {
                    arrf3[n3++] = f5;
                    arrf3[n3++] = f8;
                    arrf3[n3++] = f4;
                    arrf3[n3++] = f7;
                    arrf3[n3++] = f3;
                    arrf3[n3++] = f2;
                }
                f10 = f12;
            }
            this.bezierCoords = arrf3;
            this.numCoords = arrf3.length;
            this.myTvals = arrf;
        }
    }
}

