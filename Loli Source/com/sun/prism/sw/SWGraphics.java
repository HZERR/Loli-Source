/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.pisces.PiscesRenderer;
import com.sun.pisces.Transform6;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.Graphics;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.Paint;
import com.sun.prism.sw.SWArgbPreTexture;
import com.sun.prism.sw.SWContext;
import com.sun.prism.sw.SWPaint;
import com.sun.prism.sw.SWRTTexture;
import com.sun.prism.sw.SWResourceFactory;
import com.sun.prism.sw.SWUtils;

final class SWGraphics
implements ReadbackGraphics {
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, 2, 0, 10.0f);
    private static final Paint DEFAULT_PAINT = Color.WHITE;
    private final PiscesRenderer pr;
    private final SWContext context;
    private final SWRTTexture target;
    private final SWPaint swPaint;
    private final BaseTransform tx = new Affine2D();
    private CompositeMode compositeMode = CompositeMode.SRC_OVER;
    private Rectangle clip;
    private final Rectangle finalClip = new Rectangle();
    private RectBounds nodeBounds;
    private int clipRectIndex;
    private Paint paint = DEFAULT_PAINT;
    private BasicStroke stroke = DEFAULT_STROKE;
    private Ellipse2D ellipse2d;
    private Line2D line2d;
    private RoundRectangle2D rect2d;
    private boolean antialiasedShape = true;
    private boolean hasPreCullingBits = false;
    private float pixelScale = 1.0f;
    private NodePath renderRoot;

    @Override
    public void setRenderRoot(NodePath nodePath) {
        this.renderRoot = nodePath;
    }

    @Override
    public NodePath getRenderRoot() {
        return this.renderRoot;
    }

    public SWGraphics(SWRTTexture sWRTTexture, SWContext sWContext, PiscesRenderer piscesRenderer) {
        this.target = sWRTTexture;
        this.context = sWContext;
        this.pr = piscesRenderer;
        this.swPaint = new SWPaint(sWContext, piscesRenderer);
        this.setClipRect(null);
    }

    @Override
    public RenderTarget getRenderTarget() {
        return this.target;
    }

    @Override
    public SWResourceFactory getResourceFactory() {
        return this.target.getResourceFactory();
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.target.getAssociatedScreen();
    }

    @Override
    public void sync() {
    }

    @Override
    public BaseTransform getTransformNoClone() {
        if (PrismSettings.debug) {
            System.out.println("+ getTransformNoClone " + this + "; tr: " + this.tx);
        }
        return this.tx;
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        if (PrismSettings.debug) {
            System.out.println("+ setTransform " + this + "; tr: " + baseTransform);
        }
        this.tx.setTransform(baseTransform);
    }

    @Override
    public void setTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.tx.restoreTransform(d2, d3, d4, d5, d6, d7);
        if (PrismSettings.debug) {
            System.out.println("+ restoreTransform " + this + "; tr: " + this.tx);
        }
    }

    @Override
    public void setTransform3D(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        if (d4 != 0.0 || d8 != 0.0 || d10 != 0.0 || d11 != 0.0 || d12 != 1.0 || d13 != 0.0) {
            throw new UnsupportedOperationException("3D transforms not supported.");
        }
        this.setTransform(d2, d6, d3, d7, d5, d9);
    }

    @Override
    public void transform(BaseTransform baseTransform) {
        if (PrismSettings.debug) {
            System.out.println("+ concatTransform " + this + "; tr: " + baseTransform);
        }
        this.tx.deriveWithConcatenation(baseTransform);
    }

    @Override
    public void translate(float f2, float f3) {
        if (PrismSettings.debug) {
            System.out.println("+ concat translate " + this + "; tx: " + f2 + "; ty: " + f3);
        }
        this.tx.deriveWithTranslation(f2, f3);
    }

    @Override
    public void translate(float f2, float f3, float f4) {
        throw new UnsupportedOperationException("translate3D: unimp");
    }

    @Override
    public void scale(float f2, float f3) {
        if (PrismSettings.debug) {
            System.out.println("+ concat scale " + this + "; sx: " + f2 + "; sy: " + f3);
        }
        this.tx.deriveWithConcatenation(f2, 0.0, 0.0, f3, 0.0, 0.0);
    }

    @Override
    public void scale(float f2, float f3, float f4) {
        throw new UnsupportedOperationException("scale3D: unimp");
    }

    @Override
    public void setCamera(NGCamera nGCamera) {
    }

    @Override
    public NGCamera getCameraNoClone() {
        throw new UnsupportedOperationException("getCameraNoClone: unimp");
    }

    @Override
    public void setDepthTest(boolean bl) {
    }

    @Override
    public boolean isDepthTest() {
        return false;
    }

    @Override
    public void setDepthBuffer(boolean bl) {
    }

    @Override
    public boolean isDepthBuffer() {
        return false;
    }

    @Override
    public boolean isAlphaTestShader() {
        if (PrismSettings.verbose && PrismSettings.forceAlphaTestShader) {
            System.out.println("SW pipe doesn't support shader with alpha testing");
        }
        return false;
    }

    @Override
    public void setAntialiasedShape(boolean bl) {
        this.antialiasedShape = bl;
    }

    @Override
    public boolean isAntialiasedShape() {
        return this.antialiasedShape;
    }

    @Override
    public Rectangle getClipRect() {
        return this.clip == null ? null : new Rectangle(this.clip);
    }

    @Override
    public Rectangle getClipRectNoClone() {
        return this.clip;
    }

    @Override
    public RectBounds getFinalClipNoClone() {
        return this.finalClip.toRectBounds();
    }

    @Override
    public void setClipRect(Rectangle rectangle) {
        this.finalClip.setBounds(this.target.getDimensions());
        if (rectangle == null) {
            if (PrismSettings.debug) {
                System.out.println("+ PR.resetClip");
            }
            this.clip = null;
        } else {
            if (PrismSettings.debug) {
                System.out.println("+ PR.setClip: " + rectangle);
            }
            this.finalClip.intersectWith(rectangle);
            this.clip = new Rectangle(rectangle);
        }
        this.pr.setClip(this.finalClip.x, this.finalClip.y, this.finalClip.width, this.finalClip.height);
    }

    @Override
    public void setHasPreCullingBits(boolean bl) {
        this.hasPreCullingBits = bl;
    }

    @Override
    public boolean hasPreCullingBits() {
        return this.hasPreCullingBits;
    }

    @Override
    public int getClipRectIndex() {
        return this.clipRectIndex;
    }

    @Override
    public void setClipRectIndex(int n2) {
        if (PrismSettings.debug) {
            System.out.println("+ PR.setClipRectIndex: " + n2);
        }
        this.clipRectIndex = n2;
    }

    @Override
    public float getExtraAlpha() {
        return this.swPaint.getCompositeAlpha();
    }

    @Override
    public void setExtraAlpha(float f2) {
        if (PrismSettings.debug) {
            System.out.println("PR.setCompositeAlpha, value: " + f2);
        }
        this.swPaint.setCompositeAlpha(f2);
    }

    @Override
    public Paint getPaint() {
        return this.paint;
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    @Override
    public BasicStroke getStroke() {
        return this.stroke;
    }

    @Override
    public void setStroke(BasicStroke basicStroke) {
        this.stroke = basicStroke;
    }

    @Override
    public CompositeMode getCompositeMode() {
        return this.compositeMode;
    }

    @Override
    public void setCompositeMode(CompositeMode compositeMode) {
        int n2;
        this.compositeMode = compositeMode;
        switch (compositeMode) {
            case CLEAR: {
                n2 = 0;
                if (!PrismSettings.debug) break;
                System.out.println("PR.setCompositeRule - CLEAR");
                break;
            }
            case SRC: {
                n2 = 1;
                if (!PrismSettings.debug) break;
                System.out.println("PR.setCompositeRule - SRC");
                break;
            }
            case SRC_OVER: {
                n2 = 2;
                if (!PrismSettings.debug) break;
                System.out.println("PR.setCompositeRule - SRC_OVER");
                break;
            }
            default: {
                throw new InternalError("Unrecognized composite mode: " + (Object)((Object)compositeMode));
            }
        }
        this.pr.setCompositeRule(n2);
    }

    @Override
    public void setNodeBounds(RectBounds rectBounds) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.setNodeBounds: " + rectBounds);
        }
        this.nodeBounds = rectBounds;
    }

    @Override
    public void clear() {
        this.clear(Color.TRANSPARENT);
    }

    @Override
    public void clear(Color color) {
        if (PrismSettings.debug) {
            System.out.println("+ PR.clear: " + color);
        }
        this.swPaint.setColor(color, 1.0f);
        this.pr.clearRect(0, 0, this.target.getPhysicalWidth(), this.target.getPhysicalHeight());
        this.getRenderTarget().setOpaque(color.isOpaque());
    }

    @Override
    public void clearQuad(float f2, float f3, float f4, float f5) {
        CompositeMode compositeMode = this.compositeMode;
        Paint paint = this.paint;
        this.setCompositeMode(CompositeMode.SRC);
        this.setPaint(Color.TRANSPARENT);
        this.fillQuad(f2, f3, f4, f5);
        this.setCompositeMode(compositeMode);
        this.setPaint(paint);
    }

    @Override
    public void fill(Shape shape) {
        if (PrismSettings.debug) {
            System.out.println("+ fill(Shape)");
        }
        this.paintShape(shape, null, this.tx);
    }

    @Override
    public void fillQuad(float f2, float f3, float f4, float f5) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.fillQuad");
        }
        this.fillRect(Math.min(f2, f4), Math.min(f3, f5), Math.abs(f4 - f2), Math.abs(f5 - f3));
    }

    @Override
    public void fillRect(float f2, float f3, float f4, float f5) {
        if (PrismSettings.debug) {
            System.out.printf("+ SWG.fillRect, x: %f, y: %f, w: %f, h: %f\n", Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5));
        }
        if (this.tx.getMxy() == 0.0 && this.tx.getMyx() == 0.0) {
            if (PrismSettings.debug) {
                System.out.println("GR: " + this);
                System.out.println("target: " + this.target + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + this.target.getDimensions());
                System.out.println("Tx: " + this.tx);
                System.out.println("Clip: " + this.finalClip);
                System.out.println("Composite rule: " + (Object)((Object)this.compositeMode));
            }
            Point2D point2D = new Point2D(f2, f3);
            Point2D point2D2 = new Point2D(f2 + f4, f3 + f5);
            this.tx.transform(point2D, point2D);
            this.tx.transform(point2D2, point2D2);
            if (this.paint.getType() == Paint.Type.IMAGE_PATTERN) {
                int n2;
                ImagePattern imagePattern = (ImagePattern)this.paint;
                if (imagePattern.getImage().getPixelFormat() == PixelFormat.BYTE_ALPHA) {
                    throw new UnsupportedOperationException("Alpha image is not supported as an image pattern.");
                }
                Transform6 transform6 = this.swPaint.computeSetTexturePaintTransform(this.paint, this.tx, this.nodeBounds, f2, f3, f4, f5);
                SWArgbPreTexture sWArgbPreTexture = this.context.validateImagePaintTexture(imagePattern.getImage().getWidth(), imagePattern.getImage().getHeight());
                sWArgbPreTexture.update(imagePattern.getImage());
                float f6 = this.swPaint.getCompositeAlpha();
                if (f6 == 1.0f) {
                    n2 = 1;
                } else {
                    n2 = 2;
                    this.pr.setColor(255, 255, 255, (int)(255.0f * f6));
                }
                this.pr.drawImage(1, n2, sWArgbPreTexture.getDataNoClone(), sWArgbPreTexture.getContentWidth(), sWArgbPreTexture.getContentHeight(), sWArgbPreTexture.getOffset(), sWArgbPreTexture.getPhysicalWidth(), transform6, sWArgbPreTexture.getWrapMode() == Texture.WrapMode.REPEAT, (int)(Math.min(point2D.x, point2D2.x) * 65536.0f), (int)(Math.min(point2D.y, point2D2.y) * 65536.0f), (int)(Math.abs(point2D2.x - point2D.x) * 65536.0f), (int)(Math.abs(point2D2.y - point2D.y) * 65536.0f), 0, 0, 0, 0, 0, 0, sWArgbPreTexture.getContentWidth() - 1, sWArgbPreTexture.getContentHeight() - 1, sWArgbPreTexture.hasAlpha());
            } else {
                this.swPaint.setPaintFromShape(this.paint, this.tx, null, this.nodeBounds, f2, f3, f4, f5);
                this.pr.fillRect((int)(Math.min(point2D.x, point2D2.x) * 65536.0f), (int)(Math.min(point2D.y, point2D2.y) * 65536.0f), (int)(Math.abs(point2D2.x - point2D.x) * 65536.0f), (int)(Math.abs(point2D2.y - point2D.y) * 65536.0f));
            }
        } else {
            this.fillRoundRect(f2, f3, f4, f5, 0.0f, 0.0f);
        }
    }

    @Override
    public void fillRoundRect(float f2, float f3, float f4, float f5, float f6, float f7) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.fillRoundRect");
        }
        this.paintRoundRect(f2, f3, f4, f5, f6, f7, null);
    }

    @Override
    public void fillEllipse(float f2, float f3, float f4, float f5) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.fillEllipse");
        }
        this.paintEllipse(f2, f3, f4, f5, null);
    }

    @Override
    public void draw(Shape shape) {
        if (PrismSettings.debug) {
            System.out.println("+ draw(Shape)");
        }
        this.paintShape(shape, this.stroke, this.tx);
    }

    private void paintShape(Shape shape, BasicStroke basicStroke, BaseTransform baseTransform) {
        if (this.finalClip.isEmpty()) {
            if (PrismSettings.debug) {
                System.out.println("Final clip is empty: not rendering the shape: " + shape);
            }
            return;
        }
        this.swPaint.setPaintFromShape(this.paint, this.tx, shape, this.nodeBounds, 0.0f, 0.0f, 0.0f, 0.0f);
        this.paintShapePaintAlreadySet(shape, basicStroke, baseTransform);
    }

    private void paintShapePaintAlreadySet(Shape shape, BasicStroke basicStroke, BaseTransform baseTransform) {
        if (this.finalClip.isEmpty()) {
            if (PrismSettings.debug) {
                System.out.println("Final clip is empty: not rendering the shape: " + shape);
            }
            return;
        }
        if (PrismSettings.debug) {
            System.out.println("GR: " + this);
            System.out.println("target: " + this.target + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + this.target.getDimensions());
            System.out.println("Shape: " + shape);
            System.out.println("Stroke: " + basicStroke);
            System.out.println("Tx: " + baseTransform);
            System.out.println("Clip: " + this.finalClip);
            System.out.println("Composite rule: " + (Object)((Object)this.compositeMode));
        }
        this.context.renderShape(this.pr, shape, basicStroke, baseTransform, this.finalClip, this.isAntialiasedShape());
    }

    private void paintRoundRect(float f2, float f3, float f4, float f5, float f6, float f7, BasicStroke basicStroke) {
        if (this.rect2d == null) {
            this.rect2d = new RoundRectangle2D(f2, f3, f4, f5, f6, f7);
        } else {
            this.rect2d.setRoundRect(f2, f3, f4, f5, f6, f7);
        }
        this.paintShape(this.rect2d, basicStroke, this.tx);
    }

    private void paintEllipse(float f2, float f3, float f4, float f5, BasicStroke basicStroke) {
        if (this.ellipse2d == null) {
            this.ellipse2d = new Ellipse2D(f2, f3, f4, f5);
        } else {
            this.ellipse2d.setFrame(f2, f3, f4, f5);
        }
        this.paintShape(this.ellipse2d, basicStroke, this.tx);
    }

    @Override
    public void drawLine(float f2, float f3, float f4, float f5) {
        if (PrismSettings.debug) {
            System.out.println("+ drawLine");
        }
        if (this.line2d == null) {
            this.line2d = new Line2D(f2, f3, f4, f5);
        } else {
            this.line2d.setLine(f2, f3, f4, f5);
        }
        this.paintShape(this.line2d, this.stroke, this.tx);
    }

    @Override
    public void drawRect(float f2, float f3, float f4, float f5) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawRect");
        }
        this.drawRoundRect(f2, f3, f4, f5, 0.0f, 0.0f);
    }

    @Override
    public void drawRoundRect(float f2, float f3, float f4, float f5, float f6, float f7) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawRoundRect");
        }
        this.paintRoundRect(f2, f3, f4, f5, f6, f7, this.stroke);
    }

    @Override
    public void drawEllipse(float f2, float f3, float f4, float f5) {
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawEllipse");
        }
        this.paintEllipse(f2, f3, f4, f5, this.stroke);
    }

    @Override
    public void drawString(GlyphList glyphList, FontStrike fontStrike, float f2, float f3, Color color, int n2, int n3) {
        float f4;
        float f5;
        float f6;
        float f7;
        if (PrismSettings.debug) {
            System.out.println("+ SWG.drawGlyphList, gl.Count: " + glyphList.getGlyphCount() + ", x: " + f2 + ", y: " + f3 + ", selectStart: " + n2 + ", selectEnd: " + n3);
        }
        if (this.paint.isProportional()) {
            if (this.nodeBounds != null) {
                f7 = this.nodeBounds.getMinX();
                f6 = this.nodeBounds.getMinY();
                f5 = this.nodeBounds.getWidth();
                f4 = this.nodeBounds.getHeight();
            } else {
                Metrics metrics = fontStrike.getMetrics();
                f7 = 0.0f;
                f6 = metrics.getAscent();
                f5 = glyphList.getWidth();
                f4 = metrics.getLineHeight();
            }
        } else {
            f4 = 0.0f;
            f5 = 0.0f;
            f6 = 0.0f;
            f7 = 0.0f;
        }
        boolean bl = this.tx.isTranslateOrIdentity() && !fontStrike.drawAsShapes();
        boolean bl2 = bl && fontStrike.getAAMode() == 1 && this.getRenderTarget().isOpaque() && this.paint.getType() == Paint.Type.COLOR && this.tx.is2D();
        Affine2D affine2D = null;
        if (bl2) {
            this.pr.setLCDGammaCorrection(1.0f / PrismFontFactory.getLCDContrast());
        } else if (bl) {
            FontResource fontResource = fontStrike.getFontResource();
            float f8 = fontStrike.getSize();
            BaseTransform baseTransform = fontStrike.getTransform();
            fontStrike = fontResource.getStrike(f8, baseTransform, 0);
        } else {
            affine2D = new Affine2D();
        }
        if (color == null) {
            this.swPaint.setPaintBeforeDraw(this.paint, this.tx, f7, f6, f5, f4);
            for (int i2 = 0; i2 < glyphList.getGlyphCount(); ++i2) {
                this.drawGlyph(fontStrike, glyphList, i2, affine2D, bl, f2, f3);
            }
        } else {
            for (int i3 = 0; i3 < glyphList.getGlyphCount(); ++i3) {
                int n4 = glyphList.getCharOffset(i3);
                boolean bl3 = n2 <= n4 && n4 < n3;
                this.swPaint.setPaintBeforeDraw(bl3 ? color : this.paint, this.tx, f7, f6, f5, f4);
                this.drawGlyph(fontStrike, glyphList, i3, affine2D, bl, f2, f3);
            }
        }
    }

    private void drawGlyph(FontStrike fontStrike, GlyphList glyphList, int n2, BaseTransform baseTransform, boolean bl, float f2, float f3) {
        Glyph glyph = fontStrike.getGlyph(glyphList.getGlyphCode(n2));
        if (bl) {
            Point2D point2D = new Point2D((float)((double)f2 + this.tx.getMxt() + (double)glyphList.getPosX(n2)), (float)((double)f3 + this.tx.getMyt() + (double)glyphList.getPosY(n2)));
            int n3 = fontStrike.getQuantizedPosition(point2D);
            byte[] arrby = glyph.getPixelData(n3);
            if (arrby != null) {
                int n4 = glyph.getOriginX() + (int)point2D.x;
                int n5 = glyph.getOriginY() + (int)point2D.y;
                if (glyph.isLCDGlyph()) {
                    this.pr.fillLCDAlphaMask(arrby, n4, n5, glyph.getWidth(), glyph.getHeight(), 0, glyph.getWidth());
                } else {
                    this.pr.fillAlphaMask(arrby, n4, n5, glyph.getWidth(), glyph.getHeight(), 0, glyph.getWidth());
                }
            }
        } else {
            Shape shape = glyph.getShape();
            if (shape != null) {
                baseTransform.setTransform(this.tx);
                baseTransform.deriveWithTranslation(f2 + glyphList.getPosX(n2), f3 + glyphList.getPosY(n2));
                this.paintShapePaintAlreadySet(shape, null, baseTransform);
            }
        }
    }

    @Override
    public void drawTexture(Texture texture, float f2, float f3, float f4, float f5) {
        if (PrismSettings.debug) {
            System.out.printf("+ drawTexture1, x: %f, y: %f, w: %f, h: %f\n", Float.valueOf(f2), Float.valueOf(f3), Float.valueOf(f4), Float.valueOf(f5));
        }
        this.drawTexture(texture, f2, f3, f2 + f4, f3 + f5, 0.0f, 0.0f, f4, f5);
    }

    @Override
    public void drawTexture(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.drawTexture(texture, f2, f3, f4, f5, f6, f7, f8, f9, 0, 0, 0, 0);
    }

    private void drawTexture(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int n2, int n3, int n4, int n5) {
        int n6;
        float f10 = this.swPaint.getCompositeAlpha();
        if (f10 == 1.0f) {
            n6 = 1;
        } else {
            n6 = 2;
            this.pr.setColor(255, 255, 255, (int)(255.0f * f10));
        }
        this.drawTexture(texture, n6, f2, f3, f4, f5, f6, f7, f8, f9, n2, n3, n4, n5);
    }

    private void drawTexture(Texture texture, int n2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, int n3, int n4, int n5, int n6) {
        if (PrismSettings.debug) {
            System.out.println("+ drawTexture: " + texture + ", imageMode: " + n2 + ", tex.w: " + texture.getPhysicalWidth() + ", tex.h: " + texture.getPhysicalHeight() + ", tex.cw: " + texture.getContentWidth() + ", tex.ch: " + texture.getContentHeight());
            System.out.println("target: " + this.target + " t.w: " + this.target.getPhysicalWidth() + ", t.h: " + this.target.getPhysicalHeight() + ", t.dims: " + this.target.getDimensions());
            System.out.println("GR: " + this);
            System.out.println("dx1:" + f2 + " dy1:" + f3 + " dx2:" + f4 + " dy2:" + f5);
            System.out.println("sx1:" + f6 + " sy1:" + f7 + " sx2:" + f8 + " sy2:" + f9);
            System.out.println("Clip: " + this.finalClip);
            System.out.println("Composite rule: " + (Object)((Object)this.compositeMode));
        }
        SWArgbPreTexture sWArgbPreTexture = (SWArgbPreTexture)texture;
        int[] arrn = sWArgbPreTexture.getDataNoClone();
        RectBounds rectBounds = new RectBounds(Math.min(f2, f4), Math.min(f3, f5), Math.max(f2, f4), Math.max(f3, f5));
        RectBounds rectBounds2 = new RectBounds();
        this.tx.transform(rectBounds, rectBounds2);
        Transform6 transform6 = this.swPaint.computeDrawTexturePaintTransform(this.tx, f2, f3, f4, f5, f6, f7, f8, f9);
        if (PrismSettings.debug) {
            System.out.println("tx: " + this.tx);
            System.out.println("piscesTx: " + transform6);
            System.out.println("srcBBox: " + rectBounds);
            System.out.println("dstBBox: " + rectBounds2);
        }
        int n7 = Math.max(0, SWUtils.fastFloor(Math.min(f6, f8)));
        int n8 = Math.max(0, SWUtils.fastFloor(Math.min(f7, f9)));
        int n9 = Math.min(texture.getContentWidth() - 1, SWUtils.fastCeil(Math.max(f6, f8)) - 1);
        int n10 = Math.min(texture.getContentHeight() - 1, SWUtils.fastCeil(Math.max(f7, f9)) - 1);
        this.pr.drawImage(1, n2, arrn, texture.getContentWidth(), texture.getContentHeight(), sWArgbPreTexture.getOffset(), texture.getPhysicalWidth(), transform6, texture.getWrapMode() == Texture.WrapMode.REPEAT, (int)(65536.0f * rectBounds2.getMinX()), (int)(65536.0f * rectBounds2.getMinY()), (int)(65536.0f * rectBounds2.getWidth()), (int)(65536.0f * rectBounds2.getHeight()), n3, n4, n5, n6, n7, n8, n9, n10, sWArgbPreTexture.hasAlpha());
        if (PrismSettings.debug) {
            System.out.println("* drawTexture, DONE");
        }
    }

    @Override
    public void drawTexture3SliceH(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        this.drawTexture(texture, f2, f3, f10, f5, f6, f7, f12, f9, 0, 1, 0, 0);
        this.drawTexture(texture, f10, f3, f11, f5, f12, f7, f13, f9, 2, 1, 0, 0);
        this.drawTexture(texture, f11, f3, f4, f5, f13, f7, f8, f9, 2, 0, 0, 0);
    }

    @Override
    public void drawTexture3SliceV(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        this.drawTexture(texture, f2, f3, f4, f10, f6, f7, f8, f12, 0, 0, 0, 1);
        this.drawTexture(texture, f2, f10, f4, f11, f6, f12, f8, f13, 0, 0, 2, 1);
        this.drawTexture(texture, f2, f11, f4, f5, f6, f13, f8, f9, 0, 0, 2, 0);
    }

    @Override
    public void drawTexture9Slice(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17) {
        this.drawTexture(texture, f2, f3, f10, f11, f6, f7, f14, f15, 0, 1, 0, 1);
        this.drawTexture(texture, f10, f3, f12, f11, f14, f7, f16, f15, 2, 1, 0, 1);
        this.drawTexture(texture, f12, f3, f4, f11, f16, f7, f8, f15, 2, 0, 0, 1);
        this.drawTexture(texture, f2, f11, f10, f13, f6, f15, f14, f17, 0, 1, 2, 1);
        this.drawTexture(texture, f10, f11, f12, f13, f14, f15, f16, f17, 2, 1, 2, 1);
        this.drawTexture(texture, f12, f11, f4, f13, f16, f15, f8, f17, 2, 0, 2, 1);
        this.drawTexture(texture, f2, f13, f10, f5, f6, f17, f14, f9, 0, 1, 2, 0);
        this.drawTexture(texture, f10, f13, f12, f5, f14, f17, f16, f9, 2, 1, 2, 0);
        this.drawTexture(texture, f12, f13, f4, f5, f16, f17, f8, f9, 2, 0, 2, 0);
    }

    @Override
    public void drawTextureVO(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        if (PrismSettings.debug) {
            System.out.println("* drawTextureVO");
        }
        int[] arrn = new int[]{0, 65536};
        int[] arrn2 = new int[]{0xFFFFFF | (int)(f2 * 255.0f) << 24, 0xFFFFFF | (int)(f3 * 255.0f) << 24};
        Transform6 transform6 = new Transform6();
        SWUtils.convertToPiscesTransform(this.tx, transform6);
        this.pr.setLinearGradient(0, (int)(65536.0f * f5), 0, (int)(65536.0f * f7), arrn, arrn2, 0, transform6);
        this.drawTexture(texture, 2, f4, f5, f6, f7, f8, f9, f10, f11, 0, 0, 0, 0);
    }

    @Override
    public void drawTextureRaw(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        if (PrismSettings.debug) {
            System.out.println("+ drawTextureRaw");
        }
        int n2 = texture.getContentWidth();
        int n3 = texture.getContentHeight();
        this.drawTexture(texture, f2, f3, f4, f5, f6 *= (float)n2, f7 *= (float)n3, f8 *= (float)n2, f9 *= (float)n3);
    }

    @Override
    public void drawMappedTextureRaw(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        if (PrismSettings.debug) {
            System.out.println("+ drawMappedTextureRaw");
        }
        double d2 = this.tx.getMxx();
        double d3 = this.tx.getMyx();
        double d4 = this.tx.getMxy();
        double d5 = this.tx.getMyy();
        double d6 = this.tx.getMxt();
        double d7 = this.tx.getMyt();
        try {
            float f14 = f8 - f6;
            float f15 = f9 - f7;
            float f16 = f10 - f6;
            float f17 = f11 - f7;
            Affine2D affine2D = new Affine2D(f14, f15, f16, f17, f6, f7);
            ((BaseTransform)affine2D).invert();
            this.tx.setToIdentity();
            this.tx.deriveWithTranslation(f2, f3);
            this.tx.deriveWithConcatenation(f4 - f2, 0.0, 0.0, f5 - f5, 0.0, 0.0);
            this.tx.deriveWithConcatenation(affine2D);
            this.drawTexture(texture, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, texture.getContentWidth(), texture.getContentHeight());
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            // empty catch block
        }
        this.tx.restoreTransform(d2, d3, d4, d5, d6, d7);
    }

    @Override
    public boolean canReadBack() {
        return true;
    }

    @Override
    public RTTexture readBack(Rectangle rectangle) {
        if (PrismSettings.debug) {
            System.out.println("+ readBack, rect: " + rectangle + ", target.dims: " + this.target.getDimensions());
        }
        int n2 = Math.max(1, rectangle.width);
        int n3 = Math.max(1, rectangle.height);
        SWRTTexture sWRTTexture = this.context.validateRBBuffer(n2, n3);
        if (rectangle.isEmpty()) {
            return sWRTTexture;
        }
        int[] arrn = sWRTTexture.getDataNoClone();
        this.target.getSurface().getRGB(arrn, 0, sWRTTexture.getPhysicalWidth(), rectangle.x, rectangle.y, n2, n3);
        return sWRTTexture;
    }

    @Override
    public void releaseReadBackBuffer(RTTexture rTTexture) {
    }

    @Override
    public void setState3D(boolean bl) {
    }

    @Override
    public boolean isState3D() {
        return false;
    }

    @Override
    public void setup3DRendering() {
    }

    @Override
    public void setPixelScaleFactor(float f2) {
        this.pixelScale = f2;
    }

    @Override
    public float getPixelScaleFactor() {
        return this.pixelScale;
    }

    @Override
    public void setLights(NGLightBase[] arrnGLightBase) {
    }

    @Override
    public NGLightBase[] getLights() {
        return null;
    }

    @Override
    public void blit(RTTexture rTTexture, RTTexture rTTexture2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        Graphics graphics = rTTexture2.createGraphics();
        graphics.drawTexture(rTTexture, n6, n7, n8, n9, n2, n3, n4, n5);
    }
}

