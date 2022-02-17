/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  android.graphics.Color
 *  android.graphics.Point
 *  android.graphics.Rect
 *  android.graphics.RectF
 */
package org.pushingpixels.trident.android;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.pushingpixels.trident.interpolator.PropertyInterpolator;
import org.pushingpixels.trident.interpolator.PropertyInterpolatorSource;

public class AndroidPropertyInterpolators
implements PropertyInterpolatorSource {
    private Set<PropertyInterpolator> interpolators = new HashSet<PropertyInterpolator>();
    public static final PropertyInterpolator<Integer> COLOR_INTERPOLATOR = new ColorInterpolator();

    public AndroidPropertyInterpolators() {
        this.interpolators.add(COLOR_INTERPOLATOR);
        this.interpolators.add(new PointInterpolator());
        this.interpolators.add(new RectInterpolator());
        this.interpolators.add(new RectFInterpolator());
    }

    @Override
    public Set<PropertyInterpolator> getPropertyInterpolators() {
        return Collections.unmodifiableSet(this.interpolators);
    }

    static class RectFInterpolator
    implements PropertyInterpolator<RectF> {
        RectFInterpolator() {
        }

        @Override
        public RectF interpolate(RectF from, RectF to, float timelinePosition) {
            float left = from.left + (float)((int)(timelinePosition * (to.left - from.left)));
            float top = from.top + (float)((int)(timelinePosition * (to.top - from.top)));
            float right = from.right + (float)((int)(timelinePosition * (to.right - from.right)));
            float bottom = from.bottom + (float)((int)(timelinePosition * (to.bottom - from.bottom)));
            return new RectF(left, top, right, bottom);
        }

        @Override
        public Class getBasePropertyClass() {
            return RectF.class;
        }
    }

    static class RectInterpolator
    implements PropertyInterpolator<Rect> {
        RectInterpolator() {
        }

        @Override
        public Rect interpolate(Rect from, Rect to, float timelinePosition) {
            int left = from.left + (int)(timelinePosition * (float)(to.left - from.left));
            int top = from.top + (int)(timelinePosition * (float)(to.top - from.top));
            int right = from.right + (int)(timelinePosition * (float)(to.right - from.right));
            int bottom = from.bottom + (int)(timelinePosition * (float)(to.bottom - from.bottom));
            return new Rect(left, top, right, bottom);
        }

        @Override
        public Class getBasePropertyClass() {
            return Rect.class;
        }
    }

    static class PointInterpolator
    implements PropertyInterpolator<Point> {
        PointInterpolator() {
        }

        @Override
        public Point interpolate(Point from, Point to, float timelinePosition) {
            int x2 = from.x + (int)(timelinePosition * (float)(to.x - from.x));
            int y2 = from.y + (int)(timelinePosition * (float)(to.y - from.y));
            return new Point(x2, y2);
        }

        @Override
        public Class getBasePropertyClass() {
            return Point.class;
        }
    }

    static class ColorInterpolator
    implements PropertyInterpolator<Integer> {
        ColorInterpolator() {
        }

        @Override
        public Class getBasePropertyClass() {
            return Color.class;
        }

        @Override
        public Integer interpolate(Integer from, Integer to, float timelinePosition) {
            return this.getInterpolatedRGB(from, to, 1.0f - timelinePosition);
        }

        int getInterpolatedRGB(Integer color1, Integer color2, float color1Likeness) {
            if ((double)color1Likeness < 0.0 || (double)color1Likeness > 1.0) {
                throw new IllegalArgumentException("Color likeness should be in 0.0-1.0 range [is " + color1Likeness + "]");
            }
            if (color1.equals(color2)) {
                return color1;
            }
            if ((double)color1Likeness == 1.0) {
                return color1;
            }
            if ((double)color1Likeness == 0.0) {
                return color2;
            }
            int lr = Color.red((int)color1);
            int lg = Color.green((int)color1);
            int lb = Color.blue((int)color1);
            int la = Color.alpha((int)color1);
            int dr = Color.red((int)color2);
            int dg = Color.green((int)color2);
            int db = Color.blue((int)color2);
            int da = Color.alpha((int)color2);
            int r2 = lr == dr ? lr : (int)Math.round((double)(color1Likeness * (float)lr) + (1.0 - (double)color1Likeness) * (double)dr);
            int g2 = lg == dg ? lg : (int)Math.round((double)(color1Likeness * (float)lg) + (1.0 - (double)color1Likeness) * (double)dg);
            int b2 = lb == db ? lb : (int)Math.round((double)(color1Likeness * (float)lb) + (1.0 - (double)color1Likeness) * (double)db);
            int a2 = la == da ? la : (int)Math.round((double)(color1Likeness * (float)la) + (1.0 - (double)color1Likeness) * (double)da);
            return Color.argb((int)a2, (int)r2, (int)g2, (int)b2);
        }
    }
}

