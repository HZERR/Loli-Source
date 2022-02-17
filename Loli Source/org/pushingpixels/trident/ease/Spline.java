/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.ease;

import java.util.ArrayList;
import org.pushingpixels.trident.ease.LengthItem;
import org.pushingpixels.trident.ease.TimelineEase;

public class Spline
implements TimelineEase {
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private ArrayList lengths = new ArrayList();

    public Spline(float easeAmount) {
        this(easeAmount, 0.0f, 1.0f - easeAmount, 1.0f);
    }

    public Spline(float x1, float y1, float x2, float y2) {
        if (x1 < 0.0f || x1 > 1.0f || y1 < 0.0f || y1 > 1.0f || x2 < 0.0f || x2 > 1.0f || y2 < 0.0f || y2 > 1.0f) {
            throw new IllegalArgumentException("Control points must be in the range [0, 1]:");
        }
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        float prevX = 0.0f;
        float prevY = 0.0f;
        float prevLength = 0.0f;
        for (float t2 = 0.01f; t2 <= 1.0f; t2 += 0.01f) {
            FloatPoint xy = this.getXY(t2);
            float length = prevLength + (float)Math.sqrt((xy.x - prevX) * (xy.x - prevX) + (xy.y - prevY) * (xy.y - prevY));
            LengthItem lengthItem = new LengthItem(length, t2);
            this.lengths.add(lengthItem);
            prevLength = length;
            prevX = xy.x;
            prevY = xy.y;
        }
        for (int i2 = 0; i2 < this.lengths.size(); ++i2) {
            LengthItem lengthItem = (LengthItem)this.lengths.get(i2);
            lengthItem.setFraction(prevLength);
        }
    }

    private FloatPoint getXY(float t2) {
        float invT = 1.0f - t2;
        float b1 = 3.0f * t2 * (invT * invT);
        float b2 = 3.0f * (t2 * t2) * invT;
        float b3 = t2 * t2 * t2;
        FloatPoint xy = new FloatPoint(b1 * this.x1 + b2 * this.x2 + b3, b1 * this.y1 + b2 * this.y2 + b3);
        return xy;
    }

    private float getY(float t2) {
        float invT = 1.0f - t2;
        float b1 = 3.0f * t2 * (invT * invT);
        float b2 = 3.0f * (t2 * t2) * invT;
        float b3 = t2 * t2 * t2;
        return b1 * this.y1 + b2 * this.y2 + b3;
    }

    @Override
    public float map(float lengthFraction) {
        float interpolatedT = 1.0f;
        float prevT = 0.0f;
        float prevLength = 0.0f;
        for (int i2 = 0; i2 < this.lengths.size(); ++i2) {
            LengthItem lengthItem = (LengthItem)this.lengths.get(i2);
            float fraction = lengthItem.getFraction();
            float t2 = lengthItem.getT();
            if (lengthFraction <= fraction) {
                float proportion = (lengthFraction - prevLength) / (fraction - prevLength);
                interpolatedT = prevT + proportion * (t2 - prevT);
                return this.getY(interpolatedT);
            }
            prevLength = fraction;
            prevT = t2;
        }
        return this.getY(interpolatedT);
    }

    private static class FloatPoint {
        public float x;
        public float y;

        public FloatPoint(float x2, float y2) {
            this.x = x2;
            this.y = y2;
        }
    }
}

