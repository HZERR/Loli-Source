/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Color
 *  org.eclipse.swt.graphics.Device
 *  org.eclipse.swt.graphics.Point
 *  org.eclipse.swt.graphics.RGB
 *  org.eclipse.swt.graphics.Rectangle
 *  org.eclipse.swt.widgets.Display
 */
package org.pushingpixels.trident.swt;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.pushingpixels.trident.interpolator.PropertyInterpolator;
import org.pushingpixels.trident.interpolator.PropertyInterpolatorSource;

public class SWTPropertyInterpolators
implements PropertyInterpolatorSource {
    private Set<PropertyInterpolator> interpolators = new HashSet<PropertyInterpolator>();

    public SWTPropertyInterpolators() {
        this.interpolators.add(new ColorInterpolator());
        this.interpolators.add(new PointInterpolator());
        this.interpolators.add(new RectangleInterpolator());
    }

    @Override
    public Set<PropertyInterpolator> getPropertyInterpolators() {
        return Collections.unmodifiableSet(this.interpolators);
    }

    static class RectangleInterpolator
    implements PropertyInterpolator<Rectangle> {
        RectangleInterpolator() {
        }

        @Override
        public Rectangle interpolate(Rectangle from, Rectangle to, float timelinePosition) {
            int x2 = from.x + (int)(timelinePosition * (float)(to.x - from.x));
            int y2 = from.y + (int)(timelinePosition * (float)(to.y - from.y));
            int w2 = from.width + (int)(timelinePosition * (float)(to.width - from.width));
            int h2 = from.height + (int)(timelinePosition * (float)(to.height - from.height));
            return new Rectangle(x2, y2, w2, h2);
        }

        @Override
        public Class getBasePropertyClass() {
            return Rectangle.class;
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
    implements PropertyInterpolator<Color> {
        ColorInterpolator() {
        }

        @Override
        public Class getBasePropertyClass() {
            return Color.class;
        }

        @Override
        public Color interpolate(Color from, Color to, float timelinePosition) {
            return this.getInterpolatedColor(from, to, 1.0f - timelinePosition);
        }

        RGB getInterpolatedRGB(Color color1, Color color2, float color1Likeness) {
            if ((double)color1Likeness < 0.0 || (double)color1Likeness > 1.0) {
                throw new IllegalArgumentException("Color likeness should be in 0.0-1.0 range [is " + color1Likeness + "]");
            }
            int lr = color1.getRed();
            int lg = color1.getGreen();
            int lb = color1.getBlue();
            int dr = color2.getRed();
            int dg = color2.getGreen();
            int db = color2.getBlue();
            int r2 = lr == dr ? lr : (int)Math.round((double)(color1Likeness * (float)lr) + (1.0 - (double)color1Likeness) * (double)dr);
            int g2 = lg == dg ? lg : (int)Math.round((double)(color1Likeness * (float)lg) + (1.0 - (double)color1Likeness) * (double)dg);
            int b2 = lb == db ? lb : (int)Math.round((double)(color1Likeness * (float)lb) + (1.0 - (double)color1Likeness) * (double)db);
            return new RGB(r2, g2, b2);
        }

        Color getInterpolatedColor(Color color1, Color color2, float color1Likeness) {
            if (color1.equals((Object)color2)) {
                return color1;
            }
            if ((double)color1Likeness == 1.0) {
                return color1;
            }
            if ((double)color1Likeness == 0.0) {
                return color2;
            }
            return new Color((Device)Display.getDefault(), this.getInterpolatedRGB(color1, color2, color1Likeness));
        }
    }
}

