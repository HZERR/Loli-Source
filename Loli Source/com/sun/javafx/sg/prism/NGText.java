/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PGFont;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.javafx.text.TextRun;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Color;

public class NGText
extends NGShape {
    static final BaseTransform IDENT = BaseTransform.IDENTITY_TRANSFORM;
    private GlyphList[] runs;
    private float layoutX;
    private float layoutY;
    private PGFont font;
    private int fontSmoothingType;
    private boolean underline;
    private boolean strikethrough;
    private Object selectionPaint;
    private int selectionStart;
    private int selectionEnd;
    private static double EPSILON = 0.01;
    private FontStrike fontStrike = null;
    private FontStrike identityStrike = null;
    private double[] strikeMat = new double[4];
    private boolean drawingEffect = false;
    private static int FILL = 2;
    private static int SHAPE_FILL = 4;
    private static int TEXT = 8;
    private static int DECORATION = 16;

    public void setGlyphs(Object[] arrobject) {
        this.runs = (GlyphList[])arrobject;
        this.geometryChanged();
    }

    public void setLayoutLocation(float f2, float f3) {
        this.layoutX = f2;
        this.layoutY = f3;
        this.geometryChanged();
    }

    public void setFont(Object object) {
        if (object != null && object.equals(this.font)) {
            return;
        }
        this.font = (PGFont)object;
        this.fontStrike = null;
        this.identityStrike = null;
        this.geometryChanged();
    }

    public void setFontSmoothingType(int n2) {
        this.fontSmoothingType = n2;
        this.geometryChanged();
    }

    public void setUnderline(boolean bl) {
        this.underline = bl;
        this.geometryChanged();
    }

    public void setStrikethrough(boolean bl) {
        this.strikethrough = bl;
        this.geometryChanged();
    }

    public void setSelection(int n2, int n3, Object object) {
        this.selectionPaint = object;
        this.selectionStart = n2;
        this.selectionEnd = n3;
        this.geometryChanged();
    }

    @Override
    protected BaseBounds computePadding(BaseBounds baseBounds) {
        float f2 = this.fontSmoothingType == 1 ? 2.0f : 1.0f;
        return baseBounds.deriveWithNewBounds(baseBounds.getMinX() - f2, baseBounds.getMinY() - f2, baseBounds.getMinZ(), baseBounds.getMaxX() + f2, baseBounds.getMaxY() + f2, baseBounds.getMaxZ());
    }

    private FontStrike getStrike(BaseTransform baseTransform) {
        int n2 = this.fontSmoothingType;
        if (this.getMode() == NGShape.Mode.STROKE_FILL) {
            n2 = 0;
        }
        if (baseTransform.isIdentity()) {
            if (this.identityStrike == null || n2 != this.identityStrike.getAAMode()) {
                this.identityStrike = this.font.getStrike(IDENT, n2);
            }
            return this.identityStrike;
        }
        if (this.fontStrike == null || this.fontStrike.getSize() != this.font.getSize() || baseTransform.getMxy() == 0.0 && this.strikeMat[1] != 0.0 || baseTransform.getMyx() == 0.0 && this.strikeMat[2] != 0.0 || Math.abs(this.strikeMat[0] - baseTransform.getMxx()) > EPSILON || Math.abs(this.strikeMat[1] - baseTransform.getMxy()) > EPSILON || Math.abs(this.strikeMat[2] - baseTransform.getMyx()) > EPSILON || Math.abs(this.strikeMat[3] - baseTransform.getMyy()) > EPSILON || n2 != this.fontStrike.getAAMode()) {
            this.fontStrike = this.font.getStrike(baseTransform, n2);
            this.strikeMat[0] = baseTransform.getMxx();
            this.strikeMat[1] = baseTransform.getMxy();
            this.strikeMat[2] = baseTransform.getMyx();
            this.strikeMat[3] = baseTransform.getMyy();
        }
        return this.fontStrike;
    }

    @Override
    public Shape getShape() {
        if (this.runs == null) {
            return new Path2D();
        }
        FontStrike fontStrike = this.getStrike(IDENT);
        Path2D path2D = new Path2D();
        for (int i2 = 0; i2 < this.runs.length; ++i2) {
            RoundRectangle2D roundRectangle2D;
            GlyphList glyphList = this.runs[i2];
            Point2D point2D = glyphList.getLocation();
            float f2 = point2D.x - this.layoutX;
            float f3 = point2D.y - this.layoutY;
            BaseTransform baseTransform = BaseTransform.getTranslateInstance(f2, f3);
            path2D.append(fontStrike.getOutline(glyphList, baseTransform), false);
            Metrics metrics = null;
            if (this.underline) {
                metrics = fontStrike.getMetrics();
                roundRectangle2D = new RoundRectangle2D();
                roundRectangle2D.x = f2;
                roundRectangle2D.y = f3 + metrics.getUnderLineOffset();
                roundRectangle2D.width = glyphList.getWidth();
                roundRectangle2D.height = metrics.getUnderLineThickness();
                path2D.append(roundRectangle2D, false);
            }
            if (!this.strikethrough) continue;
            if (metrics == null) {
                metrics = fontStrike.getMetrics();
            }
            roundRectangle2D = new RoundRectangle2D();
            roundRectangle2D.x = f2;
            roundRectangle2D.y = f3 + metrics.getStrikethroughOffset();
            roundRectangle2D.width = glyphList.getWidth();
            roundRectangle2D.height = metrics.getStrikethroughThickness();
            path2D.append(roundRectangle2D, false);
        }
        return path2D;
    }

    @Override
    protected void renderEffect(Graphics graphics) {
        if (!graphics.getTransformNoClone().isTranslateOrIdentity()) {
            this.drawingEffect = true;
        }
        try {
            super.renderEffect(graphics);
        }
        finally {
            this.drawingEffect = false;
        }
    }

    @Override
    protected void renderContent2D(Graphics graphics, boolean bl) {
        int n2;
        Object object;
        if (this.mode == NGShape.Mode.EMPTY) {
            return;
        }
        if (this.runs == null || this.runs.length == 0) {
            return;
        }
        BaseTransform baseTransform = graphics.getTransformNoClone();
        FontStrike fontStrike = this.getStrike(baseTransform);
        if (fontStrike.getAAMode() == 1 || this.fillPaint != null && this.fillPaint.isProportional() || this.drawPaint != null && this.drawPaint.isProportional()) {
            object = this.getContentBounds(new RectBounds(), IDENT);
            graphics.setNodeBounds((RectBounds)object);
        }
        object = null;
        if (this.selectionStart != this.selectionEnd && this.selectionPaint instanceof Color) {
            object = (Color)this.selectionPaint;
        }
        BaseBounds baseBounds = null;
        if (this.getClipNode() != null) {
            baseBounds = this.getClippedBounds(new RectBounds(), IDENT);
        }
        if (this.mode != NGShape.Mode.STROKE) {
            graphics.setPaint(this.fillPaint);
            n2 = TEXT;
            this.renderText(graphics, fontStrike, baseBounds, (Color)object, n2 |= fontStrike.drawAsShapes() || this.drawingEffect ? SHAPE_FILL : FILL);
            if (this.underline || this.strikethrough) {
                n2 = DECORATION | SHAPE_FILL;
                this.renderText(graphics, fontStrike, baseBounds, (Color)object, n2);
            }
        }
        if (this.mode != NGShape.Mode.FILL) {
            graphics.setPaint(this.drawPaint);
            graphics.setStroke(this.drawStroke);
            n2 = TEXT;
            if (this.underline || this.strikethrough) {
                n2 |= DECORATION;
            }
            this.renderText(graphics, fontStrike, baseBounds, (Color)object, n2);
        }
        graphics.setNodeBounds(null);
    }

    private void renderText(Graphics graphics, FontStrike fontStrike, BaseBounds baseBounds, Color color, int n2) {
        for (int i2 = 0; i2 < this.runs.length; ++i2) {
            float f2;
            float f3;
            float f4;
            TextRun textRun = (TextRun)this.runs[i2];
            RectBounds rectBounds = textRun.getLineBounds();
            Point2D point2D = textRun.getLocation();
            float f5 = point2D.x - this.layoutX;
            float f6 = point2D.y - this.layoutY;
            if (baseBounds != null) {
                if (f6 > baseBounds.getMaxY()) break;
                if (f6 + rectBounds.getHeight() < baseBounds.getMinY() || f5 > baseBounds.getMaxX() || f5 + textRun.getWidth() < baseBounds.getMinX()) continue;
            }
            f6 -= rectBounds.getMinY();
            if ((n2 & TEXT) != 0 && textRun.getGlyphCount() > 0) {
                if ((n2 & FILL) != 0) {
                    int n3 = textRun.getStart();
                    graphics.drawString(textRun, fontStrike, f5, f6, color, this.selectionStart - n3, this.selectionEnd - n3);
                } else {
                    BaseTransform baseTransform = BaseTransform.getTranslateInstance(f5, f6);
                    if ((n2 & SHAPE_FILL) != 0) {
                        graphics.fill(fontStrike.getOutline(textRun, baseTransform));
                    } else {
                        graphics.draw(fontStrike.getOutline(textRun, baseTransform));
                    }
                }
            }
            if ((n2 & DECORATION) == 0) continue;
            Metrics metrics = fontStrike.getMetrics();
            if (this.underline) {
                f4 = f6 + metrics.getUnderLineOffset();
                f3 = metrics.getUnderLineThickness();
                if ((n2 & SHAPE_FILL) != 0) {
                    if (f3 <= 1.0f && graphics.getTransformNoClone().isTranslateOrIdentity()) {
                        f2 = (float)graphics.getTransformNoClone().getMyt();
                        f4 = (float)Math.round(f4 + f2) - f2;
                    }
                    graphics.fillRect(f5, f4, textRun.getWidth(), f3);
                } else {
                    graphics.drawRect(f5, f4, textRun.getWidth(), f3);
                }
            }
            if (!this.strikethrough) continue;
            f4 = f6 + metrics.getStrikethroughOffset();
            f3 = metrics.getStrikethroughThickness();
            if ((n2 & SHAPE_FILL) != 0) {
                if (f3 <= 1.0f && graphics.getTransformNoClone().isTranslateOrIdentity()) {
                    f2 = (float)graphics.getTransformNoClone().getMyt();
                    f4 = (float)Math.round(f4 + f2) - f2;
                }
                graphics.fillRect(f5, f4, textRun.getWidth(), f3);
                continue;
            }
            graphics.drawRect(f5, f4, textRun.getWidth(), f3);
        }
    }
}

