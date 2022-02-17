/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RectangularShape;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.EffectUtil;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.RectShadowGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.shape.ShapeRep;
import com.sun.scenario.effect.Effect;

public class NGRectangle
extends NGShape {
    private RoundRectangle2D rrect = new RoundRectangle2D();
    static final float HALF_MINUS_HALF_SQRT_HALF = 0.14700001f;
    private static final double SQRT_2 = Math.sqrt(2.0);

    public void updateRectangle(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.rrect.x = f2;
        this.rrect.y = f3;
        this.rrect.width = f4;
        this.rrect.height = f5;
        this.rrect.arcWidth = f6;
        this.rrect.arcHeight = f7;
        this.geometryChanged();
    }

    @Override
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override
    protected boolean hasOpaqueRegion() {
        return super.hasOpaqueRegion() && this.rrect.width > 1.0f && this.rrect.height > 1.0f;
    }

    @Override
    protected RectBounds computeOpaqueRegion(RectBounds rectBounds) {
        float f2 = this.rrect.x;
        float f3 = this.rrect.y;
        float f4 = this.rrect.width;
        float f5 = this.rrect.height;
        float f6 = this.rrect.arcWidth;
        float f7 = this.rrect.arcHeight;
        if (f6 <= 0.0f || f7 <= 0.0f) {
            return (RectBounds)rectBounds.deriveWithNewBounds(f2, f3, 0.0f, f2 + f4, f3 + f5, 0.0f);
        }
        float f8 = Math.min(f4, f6) * 0.14700001f;
        float f9 = Math.min(f5, f7) * 0.14700001f;
        return (RectBounds)rectBounds.deriveWithNewBounds(f2 + f8, f3 + f9, 0.0f, f2 + f4 - f8, f3 + f5 - f9, 0.0f);
    }

    boolean isRounded() {
        return this.rrect.arcWidth > 0.0f && this.rrect.arcHeight > 0.0f;
    }

    @Override
    protected void renderEffect(Graphics graphics) {
        if (!(graphics instanceof RectShadowGraphics) || !this.renderEffectDirectly(graphics)) {
            super.renderEffect(graphics);
        }
    }

    private boolean renderEffectDirectly(Graphics graphics) {
        if (this.mode != NGShape.Mode.FILL || this.isRounded()) {
            return false;
        }
        float f2 = graphics.getExtraAlpha();
        if (!(this.fillPaint instanceof Color)) {
            return false;
        }
        Effect effect = this.getEffect();
        return EffectUtil.renderEffectForRectangularNode(this, graphics, effect, f2 *= ((Color)this.fillPaint).getAlpha(), true, this.rrect.x, this.rrect.y, this.rrect.width, this.rrect.height);
    }

    @Override
    public final Shape getShape() {
        return this.rrect;
    }

    @Override
    protected ShapeRep createShapeRep(Graphics graphics) {
        return graphics.getResourceFactory().createRoundRectRep();
    }

    private static boolean hasRightAngleMiterAndNoDashes(BasicStroke basicStroke) {
        return basicStroke.getLineJoin() == 0 && (double)basicStroke.getMiterLimit() >= SQRT_2 && basicStroke.getDashArray() == null;
    }

    static boolean rectContains(float f2, float f3, NGShape nGShape, RectangularShape rectangularShape) {
        double d2 = rectangularShape.getWidth();
        double d3 = rectangularShape.getHeight();
        if (d2 < 0.0 || d3 < 0.0) {
            return false;
        }
        NGShape.Mode mode = nGShape.mode;
        if (mode == NGShape.Mode.EMPTY) {
            return false;
        }
        double d4 = rectangularShape.getX();
        double d5 = rectangularShape.getY();
        if (mode == NGShape.Mode.FILL) {
            return (double)f2 >= d4 && (double)f3 >= d5 && (double)f2 < d4 + d2 && (double)f3 < d5 + d3;
        }
        float f4 = -1.0f;
        float f5 = -1.0f;
        boolean bl = false;
        BasicStroke basicStroke = nGShape.drawStroke;
        int n2 = basicStroke.getType();
        if (n2 == 1) {
            if (mode == NGShape.Mode.STROKE_FILL) {
                f4 = 0.0f;
            } else if (basicStroke.getDashArray() == null) {
                f4 = 0.0f;
                f5 = basicStroke.getLineWidth();
            } else {
                bl = true;
            }
        } else if (n2 == 2) {
            if (NGRectangle.hasRightAngleMiterAndNoDashes(basicStroke)) {
                f4 = basicStroke.getLineWidth();
                if (mode == NGShape.Mode.STROKE) {
                    f5 = 0.0f;
                }
            } else {
                if (mode == NGShape.Mode.STROKE_FILL) {
                    f4 = 0.0f;
                }
                bl = true;
            }
        } else if (n2 == 0) {
            if (NGRectangle.hasRightAngleMiterAndNoDashes(basicStroke)) {
                f4 = basicStroke.getLineWidth() / 2.0f;
                if (mode == NGShape.Mode.STROKE) {
                    f5 = f4;
                }
            } else {
                if (mode == NGShape.Mode.STROKE_FILL) {
                    f4 = 0.0f;
                }
                bl = true;
            }
        } else {
            if (mode == NGShape.Mode.STROKE_FILL) {
                f4 = 0.0f;
            }
            bl = true;
        }
        if (f4 >= 0.0f && (double)f2 >= d4 - (double)f4 && (double)f3 >= d5 - (double)f4 && (double)f2 < d4 + d2 + (double)f4 && (double)f3 < d5 + d3 + (double)f4) {
            return !(f5 >= 0.0f) || !((double)f5 < d2 / 2.0) || !((double)f5 < d3 / 2.0) || !((double)f2 >= d4 + (double)f5) || !((double)f3 >= d5 + (double)f5) || !((double)f2 < d4 + d2 - (double)f5) || !((double)f3 < d5 + d3 - (double)f5);
        }
        if (bl) {
            return nGShape.getStrokeShape().contains(f2, f3);
        }
        return false;
    }

    @Override
    protected final boolean isRectClip(BaseTransform baseTransform, boolean bl) {
        long l2;
        if (this.mode != NGShape.Mode.FILL || this.getClipNode() != null || this.getEffect() != null && this.getEffect().reducesOpaquePixels() || this.getOpacity() < 1.0f || !bl && this.isRounded() || !this.fillPaint.isOpaque()) {
            return false;
        }
        BaseTransform baseTransform2 = this.getTransform();
        if (!baseTransform2.isIdentity()) {
            if (!baseTransform.isIdentity()) {
                TEMP_TRANSFORM.setTransform(baseTransform);
                TEMP_TRANSFORM.concatenate(baseTransform2);
                baseTransform = TEMP_TRANSFORM;
            } else {
                baseTransform = baseTransform2;
            }
        }
        return ((l2 = (long)baseTransform.getType()) & 0xFFFFFFFFFFFFFFF0L) == 0L;
    }
}

