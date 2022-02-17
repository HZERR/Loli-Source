/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.prism;

import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Paint;
import com.sun.webkit.graphics.WCStroke;

final class WCStrokeImpl
extends WCStroke<Paint, BasicStroke> {
    private BasicStroke stroke;

    public WCStrokeImpl() {
    }

    public WCStrokeImpl(float f2, int n2, int n3, float f3, float[] arrf, float f4) {
        this.setThickness(f2);
        this.setLineCap(n2);
        this.setLineJoin(n3);
        this.setMiterLimit(f3);
        this.setDashSizes(arrf);
        this.setDashOffset(f4);
    }

    @Override
    protected void invalidate() {
        this.stroke = null;
    }

    @Override
    public BasicStroke getPlatformStroke() {
        int n2;
        if (this.stroke == null && (n2 = this.getStyle()) != 0) {
            float f2 = this.getThickness();
            float[] arrf = this.getDashSizes();
            if (arrf == null) {
                switch (n2) {
                    case 2: {
                        arrf = new float[]{f2, f2};
                        break;
                    }
                    case 3: {
                        arrf = new float[]{3.0f * f2, 3.0f * f2};
                    }
                }
            }
            this.stroke = new BasicStroke(f2, this.getLineCap(), this.getLineJoin(), this.getMiterLimit(), arrf, this.getDashOffset());
        }
        return this.stroke;
    }

    boolean isApplicable() {
        return this.getPaint() != null && this.getPlatformStroke() != null;
    }

    boolean apply(Graphics graphics) {
        if (this.isApplicable()) {
            Paint paint = (Paint)this.getPaint();
            BasicStroke basicStroke = this.getPlatformStroke();
            graphics.setPaint(paint);
            graphics.setStroke(basicStroke);
            return true;
        }
        return false;
    }
}

