/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.font.CompositeStrike;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.j2d.J2DFontFactory;
import com.sun.prism.j2d.J2DPresentable;
import com.sun.prism.j2d.J2DRTTexture;
import com.sun.prism.j2d.J2DTexture;
import com.sun.prism.j2d.paint.MultipleGradientPaint;
import com.sun.prism.j2d.paint.RadialGradientPaint;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class J2DPrismGraphics
implements ReadbackGraphics,
MaskTextureGraphics {
    static final MultipleGradientPaint.CycleMethod[] LGP_CYCLE_METHODS = new MultipleGradientPaint.CycleMethod[]{MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.CycleMethod.REFLECT, MultipleGradientPaint.CycleMethod.REPEAT};
    static final MultipleGradientPaint.CycleMethod[] RGP_CYCLE_METHODS = new MultipleGradientPaint.CycleMethod[]{MultipleGradientPaint.CycleMethod.NO_CYCLE, MultipleGradientPaint.CycleMethod.REFLECT, MultipleGradientPaint.CycleMethod.REPEAT};
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, 2, 0, 10.0f);
    private static final Paint DEFAULT_PAINT = com.sun.prism.paint.Color.WHITE;
    static AffineTransform J2D_IDENTITY = new AffineTransform();
    private int clipRectIndex;
    private boolean hasPreCullingBits = false;
    private float pixelScale = 1.0f;
    private static ConcurrentHashMap<Font, WeakReference<Font>> fontMap = new ConcurrentHashMap();
    private static volatile int cleared = 0;
    private static AffineTransform tmpAT = new AffineTransform();
    private static Path2D tmpQuadShape = new Path2D.Float();
    private static Rectangle2D.Float tmpRect = new Rectangle2D.Float();
    private static Ellipse2D tmpEllipse = new Ellipse2D.Float();
    private static RoundRectangle2D tmpRRect = new RoundRectangle2D.Float();
    private static Line2D tmpLine = new Line2D.Float();
    private static AdaptorShape tmpAdaptor = new AdaptorShape();
    private boolean antialiasedShape = true;
    J2DPresentable target;
    Graphics2D g2d;
    Affine2D transform;
    Rectangle clipRect;
    RectBounds devClipRect;
    RectBounds finalClipRect;
    Paint paint;
    boolean paintWasProportional;
    BasicStroke stroke;
    boolean cull;
    Rectangle2D nodeBounds = null;
    private NodePath renderRoot;

    static Color toJ2DColor(com.sun.prism.paint.Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    static int fixFractions(float[] arrf, Color[] arrcolor) {
        float f2 = arrf[0];
        int n2 = 1;
        int n3 = 1;
        while (n2 < arrf.length) {
            float f3 = arrf[n2];
            Color color = arrcolor[n2++];
            if (f3 <= f2) {
                if (f3 >= 1.0f) break;
                f3 = f2 + Math.ulp(f2);
                while (n2 < arrf.length && !(arrf[n2] > f3)) {
                    color = arrcolor[n2++];
                }
            }
            arrf[n3] = f2 = f3;
            arrcolor[n3++] = color;
        }
        return n3;
    }

    java.awt.Paint toJ2DPaint(Paint paint, Rectangle2D rectangle2D) {
        if (paint instanceof com.sun.prism.paint.Color) {
            return J2DPrismGraphics.toJ2DColor((com.sun.prism.paint.Color)paint);
        }
        if (paint instanceof Gradient) {
            float f2;
            Color[] arrcolor;
            Gradient gradient = (Gradient)paint;
            if (gradient.isProportional() && rectangle2D == null) {
                return null;
            }
            List<Stop> list = gradient.getStops();
            int n2 = list.size();
            float[] arrf = new float[n2];
            Color[] arrcolor2 = new Color[n2];
            float f3 = -1.0f;
            boolean bl = false;
            for (int i2 = 0; i2 < n2; ++i2) {
                arrcolor = list.get(i2);
                f2 = arrcolor.getOffset();
                bl = bl || f2 <= f3;
                arrf[i2] = f3 = f2;
                arrcolor2[i2] = J2DPrismGraphics.toJ2DColor(arrcolor.getColor());
            }
            if (bl && (n2 = J2DPrismGraphics.fixFractions(arrf, arrcolor2)) < arrf.length) {
                float[] arrf2 = new float[n2];
                System.arraycopy(arrf, 0, arrf2, 0, n2);
                arrf = arrf2;
                arrcolor = new Color[n2];
                System.arraycopy(arrcolor2, 0, arrcolor, 0, n2);
                arrcolor2 = arrcolor;
            }
            if (gradient instanceof LinearGradient) {
                LinearGradient linearGradient = (LinearGradient)paint;
                float f4 = linearGradient.getX1();
                f2 = linearGradient.getY1();
                float f5 = linearGradient.getX2();
                float f6 = linearGradient.getY2();
                if (gradient.isProportional()) {
                    float f7 = (float)rectangle2D.getX();
                    float f8 = (float)rectangle2D.getY();
                    float f9 = (float)rectangle2D.getWidth();
                    float f10 = (float)rectangle2D.getHeight();
                    f4 = f7 + f9 * f4;
                    f2 = f8 + f10 * f2;
                    f5 = f7 + f9 * f5;
                    f6 = f8 + f10 * f6;
                }
                if (f4 == f5 && f2 == f2) {
                    return arrcolor2[0];
                }
                Point2D.Float float_ = new Point2D.Float(f4, f2);
                Point2D.Float float_2 = new Point2D.Float(f5, f6);
                MultipleGradientPaint.CycleMethod cycleMethod = LGP_CYCLE_METHODS[gradient.getSpreadMethod()];
                return new LinearGradientPaint(float_, float_2, arrf, arrcolor2, cycleMethod);
            }
            if (gradient instanceof RadialGradient) {
                float f11;
                float f12;
                RadialGradient radialGradient = (RadialGradient)gradient;
                float f13 = radialGradient.getCenterX();
                f2 = radialGradient.getCenterY();
                float f14 = radialGradient.getRadius();
                double d2 = Math.toRadians(radialGradient.getFocusAngle());
                float f15 = radialGradient.getFocusDistance();
                AffineTransform affineTransform = J2D_IDENTITY;
                if (gradient.isProportional()) {
                    float f16 = (float)rectangle2D.getX();
                    f12 = (float)rectangle2D.getY();
                    f11 = (float)rectangle2D.getWidth();
                    float f17 = (float)rectangle2D.getHeight();
                    float f18 = Math.min(f11, f17);
                    float f19 = f16 + f11 * 0.5f;
                    float f20 = f12 + f17 * 0.5f;
                    f13 = f19 + (f13 - 0.5f) * f18;
                    f2 = f20 + (f2 - 0.5f) * f18;
                    f14 *= f18;
                    if (f11 != f17 && (double)f11 != 0.0 && (double)f17 != 0.0) {
                        affineTransform = AffineTransform.getTranslateInstance(f19, f20);
                        affineTransform.scale(f11 / f18, f17 / f18);
                        affineTransform.translate(-f19, -f20);
                    }
                }
                Point2D.Float float_ = new Point2D.Float(f13, f2);
                f12 = (float)((double)f13 + (double)(f15 * f14) * Math.cos(d2));
                f11 = (float)((double)f2 + (double)(f15 * f14) * Math.sin(d2));
                Point2D.Float float_3 = new Point2D.Float(f12, f11);
                MultipleGradientPaint.CycleMethod cycleMethod = RGP_CYCLE_METHODS[gradient.getSpreadMethod()];
                return new RadialGradientPaint(float_, f14, float_3, arrf, arrcolor2, cycleMethod, MultipleGradientPaint.ColorSpaceType.SRGB, affineTransform);
            }
        } else if (paint instanceof ImagePattern) {
            ImagePattern imagePattern = (ImagePattern)paint;
            float f21 = imagePattern.getX();
            float f22 = imagePattern.getY();
            float f23 = imagePattern.getWidth();
            float f24 = imagePattern.getHeight();
            if (paint.isProportional()) {
                if (rectangle2D == null) {
                    return null;
                }
                float f25 = (float)rectangle2D.getX();
                float f26 = (float)rectangle2D.getY();
                float f27 = (float)rectangle2D.getWidth();
                float f28 = (float)rectangle2D.getHeight();
                f23 += f21;
                f24 += f22;
                f21 = f25 + f21 * f27;
                f22 = f26 + f22 * f28;
                f23 = f25 + f23 * f27;
                f24 = f26 + f24 * f28;
                f23 -= f21;
                f24 -= f22;
            }
            Texture texture = this.getResourceFactory().getCachedTexture(imagePattern.getImage(), Texture.WrapMode.REPEAT);
            BufferedImage bufferedImage = ((J2DTexture)texture).getBufferedImage();
            texture.unlock();
            return new TexturePaint(bufferedImage, J2DPrismGraphics.tmpRect(f21, f22, f23, f24));
        }
        throw new UnsupportedOperationException("Paint " + paint + " not supported yet.");
    }

    static Stroke toJ2DStroke(BasicStroke basicStroke) {
        float f2 = basicStroke.getLineWidth();
        int n2 = basicStroke.getType();
        if (n2 != 0) {
            f2 *= 2.0f;
        }
        java.awt.BasicStroke basicStroke2 = new java.awt.BasicStroke(f2, basicStroke.getEndCap(), basicStroke.getLineJoin(), basicStroke.getMiterLimit(), basicStroke.getDashArray(), basicStroke.getDashPhase());
        if (n2 == 1) {
            return new InnerStroke(basicStroke2);
        }
        if (n2 == 2) {
            return new OuterStroke(basicStroke2);
        }
        return basicStroke2;
    }

    private static Font toJ2DFont(FontStrike fontStrike) {
        Object object;
        Font font;
        FontResource fontResource = fontStrike.getFontResource();
        Object object2 = fontResource.getPeer();
        if (object2 == null && fontResource.isEmbeddedFont()) {
            J2DFontFactory.registerFont(fontResource);
            object2 = fontResource.getPeer();
        }
        if (object2 != null && object2 instanceof Font) {
            font = (Font)object2;
        } else {
            if (PlatformUtil.isMac()) {
                object = fontResource.getPSName();
                font = new Font((String)object, 0, 12);
                if (!font.getPSName().equals(object)) {
                    int n2 = fontResource.isBold() ? 1 : 0;
                    font = new Font(fontResource.getFamilyName(), n2 |= fontResource.isItalic() ? 2 : 0, 12);
                    if (!font.getPSName().equals(object)) {
                        Font[] arrfont;
                        for (Font font2 : arrfont = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
                            if (!font2.getPSName().equals(object)) continue;
                            font = font2;
                            break;
                        }
                    }
                }
            } else {
                font = new Font(fontResource.getFullName(), 0, 12);
            }
            fontResource.setPeer(font);
        }
        font = font.deriveFont(fontStrike.getSize());
        object = null;
        WeakReference<Object> weakReference = fontMap.get(font);
        if (weakReference != null && (object = (Font)weakReference.get()) == null) {
            ++cleared;
        }
        if (object == null) {
            if (fontMap.size() > 100 && cleared > 10) {
                for (Font[] arrfont : fontMap.keySet()) {
                    weakReference = fontMap.get(arrfont);
                    if (weakReference != null && weakReference.get() != null) continue;
                    fontMap.remove(arrfont);
                }
                cleared = 0;
            }
            object = J2DFontFactory.getCompositeFont(font);
            weakReference = new WeakReference<Object>(object);
            fontMap.put(font, weakReference);
        }
        return object;
    }

    public static AffineTransform toJ2DTransform(BaseTransform baseTransform) {
        return new AffineTransform(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMxy(), baseTransform.getMyy(), baseTransform.getMxt(), baseTransform.getMyt());
    }

    static AffineTransform tmpJ2DTransform(BaseTransform baseTransform) {
        tmpAT.setTransform(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMxy(), baseTransform.getMyy(), baseTransform.getMxt(), baseTransform.getMyt());
        return tmpAT;
    }

    static BaseTransform toPrTransform(AffineTransform affineTransform) {
        return BaseTransform.getInstance(affineTransform.getScaleX(), affineTransform.getShearY(), affineTransform.getShearX(), affineTransform.getScaleY(), affineTransform.getTranslateX(), affineTransform.getTranslateY());
    }

    static Rectangle toPrRect(java.awt.Rectangle rectangle) {
        return new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    private static Shape tmpQuad(float f2, float f3, float f4, float f5) {
        tmpQuadShape.reset();
        tmpQuadShape.moveTo(f2, f3);
        tmpQuadShape.lineTo(f4, f3);
        tmpQuadShape.lineTo(f4, f5);
        tmpQuadShape.lineTo(f2, f5);
        tmpQuadShape.closePath();
        return tmpQuadShape;
    }

    private static Rectangle2D tmpRect(float f2, float f3, float f4, float f5) {
        tmpRect.setRect(f2, f3, f4, f5);
        return tmpRect;
    }

    private static Shape tmpEllipse(float f2, float f3, float f4, float f5) {
        tmpEllipse.setFrame(f2, f3, f4, f5);
        return tmpEllipse;
    }

    private static Shape tmpRRect(float f2, float f3, float f4, float f5, float f6, float f7) {
        tmpRRect.setRoundRect(f2, f3, f4, f5, f6, f7);
        return tmpRRect;
    }

    private static Shape tmpLine(float f2, float f3, float f4, float f5) {
        tmpLine.setLine(f2, f3, f4, f5);
        return tmpLine;
    }

    private static Shape tmpShape(com.sun.javafx.geom.Shape shape) {
        tmpAdaptor.setShape(shape);
        return tmpAdaptor;
    }

    J2DPrismGraphics(J2DPresentable j2DPresentable, Graphics2D graphics2D) {
        this(graphics2D, j2DPresentable.getContentWidth(), j2DPresentable.getContentHeight());
        this.target = j2DPresentable;
    }

    J2DPrismGraphics(Graphics2D graphics2D, int n2, int n3) {
        this.g2d = graphics2D;
        this.captureTransform(graphics2D);
        this.transform = new Affine2D();
        this.devClipRect = new RectBounds(0.0f, 0.0f, n2, n3);
        this.finalClipRect = new RectBounds(0.0f, 0.0f, n2, n3);
        this.cull = true;
        graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.setTransform(BaseTransform.IDENTITY_TRANSFORM);
        this.setPaint(DEFAULT_PAINT);
        this.setStroke(DEFAULT_STROKE);
    }

    @Override
    public RenderTarget getRenderTarget() {
        return this.target;
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.target.getAssociatedScreen();
    }

    @Override
    public ResourceFactory getResourceFactory() {
        return this.target.getResourceFactory();
    }

    public void reset() {
    }

    @Override
    public Rectangle getClipRect() {
        return this.clipRect == null ? null : new Rectangle(this.clipRect);
    }

    @Override
    public Rectangle getClipRectNoClone() {
        return this.clipRect;
    }

    @Override
    public RectBounds getFinalClipNoClone() {
        return this.finalClipRect;
    }

    @Override
    public void setClipRect(Rectangle rectangle) {
        this.finalClipRect.setBounds(this.devClipRect);
        if (rectangle == null) {
            this.clipRect = null;
            this.g2d.setClip(null);
        } else {
            this.clipRect = new Rectangle(rectangle);
            this.finalClipRect.intersectWith(rectangle);
            this.setTransformG2D(J2D_IDENTITY);
            this.g2d.setClip(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
            this.setTransformG2D(J2DPrismGraphics.tmpJ2DTransform(this.transform));
        }
    }

    private AlphaComposite getAWTComposite() {
        return (AlphaComposite)this.g2d.getComposite();
    }

    @Override
    public float getExtraAlpha() {
        return this.getAWTComposite().getAlpha();
    }

    @Override
    public void setExtraAlpha(float f2) {
        this.g2d.setComposite(this.getAWTComposite().derive(f2));
    }

    @Override
    public CompositeMode getCompositeMode() {
        int n2 = this.getAWTComposite().getRule();
        switch (n2) {
            case 1: {
                return CompositeMode.CLEAR;
            }
            case 2: {
                return CompositeMode.SRC;
            }
            case 3: {
                return CompositeMode.SRC_OVER;
            }
        }
        throw new InternalError("Unrecognized AlphaCompsite rule: " + n2);
    }

    @Override
    public void setCompositeMode(CompositeMode compositeMode) {
        AlphaComposite alphaComposite = this.getAWTComposite();
        switch (compositeMode) {
            case CLEAR: {
                alphaComposite = alphaComposite.derive(1);
                break;
            }
            case SRC: {
                alphaComposite = alphaComposite.derive(2);
                break;
            }
            case SRC_OVER: {
                alphaComposite = alphaComposite.derive(3);
                break;
            }
            default: {
                throw new InternalError("Unrecognized composite mode: " + (Object)((Object)compositeMode));
            }
        }
        this.g2d.setComposite(alphaComposite);
    }

    @Override
    public Paint getPaint() {
        return this.paint;
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
        java.awt.Paint paint2 = this.toJ2DPaint(paint, null);
        if (paint2 == null) {
            this.paintWasProportional = true;
        } else {
            this.paintWasProportional = false;
            this.g2d.setPaint(paint2);
        }
    }

    @Override
    public BasicStroke getStroke() {
        return this.stroke;
    }

    @Override
    public void setStroke(BasicStroke basicStroke) {
        this.stroke = basicStroke;
        this.g2d.setStroke(J2DPrismGraphics.toJ2DStroke(basicStroke));
    }

    @Override
    public BaseTransform getTransformNoClone() {
        return this.transform;
    }

    @Override
    public void translate(float f2, float f3) {
        this.transform.translate(f2, f3);
        this.g2d.translate(f2, f3);
    }

    @Override
    public void scale(float f2, float f3) {
        this.transform.scale(f2, f3);
        this.g2d.scale(f2, f3);
    }

    @Override
    public void transform(BaseTransform baseTransform) {
        if (!baseTransform.is2D()) {
            return;
        }
        this.transform.concatenate(baseTransform);
        this.setTransformG2D(J2DPrismGraphics.tmpJ2DTransform(this.transform));
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        this.transform.setTransform(baseTransform);
        this.setTransformG2D(J2DPrismGraphics.tmpJ2DTransform(this.transform));
    }

    @Override
    public void setTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.transform.setTransform(d2, d3, d4, d5, d6, d7);
        this.setTransformG2D(J2DPrismGraphics.tmpJ2DTransform(this.transform));
    }

    @Override
    public void clear() {
        this.clear(com.sun.prism.paint.Color.TRANSPARENT);
    }

    @Override
    public void clear(com.sun.prism.paint.Color color) {
        this.getRenderTarget().setOpaque(color.isOpaque());
        this.clear(J2DPrismGraphics.toJ2DColor(color));
    }

    void clear(Color color) {
        Graphics2D graphics2D = (Graphics2D)this.g2d.create();
        graphics2D.setTransform(J2D_IDENTITY);
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setColor(color);
        graphics2D.fillRect(0, 0, this.target.getContentWidth(), this.target.getContentHeight());
        graphics2D.dispose();
    }

    @Override
    public void clearQuad(float f2, float f3, float f4, float f5) {
        this.g2d.setComposite(AlphaComposite.Clear);
        this.g2d.fill(J2DPrismGraphics.tmpQuad(f2, f3, f4, f5));
    }

    void fill(Shape shape) {
        if (this.paintWasProportional) {
            if (this.nodeBounds != null) {
                this.g2d.setPaint(this.toJ2DPaint(this.paint, this.nodeBounds));
            } else {
                this.g2d.setPaint(this.toJ2DPaint(this.paint, shape.getBounds2D()));
            }
        }
        this.g2d.fill(shape);
    }

    @Override
    public void fill(com.sun.javafx.geom.Shape shape) {
        this.fill(J2DPrismGraphics.tmpShape(shape));
    }

    @Override
    public void fillRect(float f2, float f3, float f4, float f5) {
        this.fill(J2DPrismGraphics.tmpRect(f2, f3, f4, f5));
    }

    @Override
    public void fillRoundRect(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.fill(J2DPrismGraphics.tmpRRect(f2, f3, f4, f5, f6, f7));
    }

    @Override
    public void fillEllipse(float f2, float f3, float f4, float f5) {
        this.fill(J2DPrismGraphics.tmpEllipse(f2, f3, f4, f5));
    }

    @Override
    public void fillQuad(float f2, float f3, float f4, float f5) {
        this.fill(J2DPrismGraphics.tmpQuad(f2, f3, f4, f5));
    }

    void draw(Shape shape) {
        if (this.paintWasProportional) {
            if (this.nodeBounds != null) {
                this.g2d.setPaint(this.toJ2DPaint(this.paint, this.nodeBounds));
            } else {
                this.g2d.setPaint(this.toJ2DPaint(this.paint, shape.getBounds2D()));
            }
        }
        try {
            this.g2d.draw(shape);
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Override
    public void draw(com.sun.javafx.geom.Shape shape) {
        this.draw(J2DPrismGraphics.tmpShape(shape));
    }

    @Override
    public void drawLine(float f2, float f3, float f4, float f5) {
        this.draw(J2DPrismGraphics.tmpLine(f2, f3, f4, f5));
    }

    @Override
    public void drawRect(float f2, float f3, float f4, float f5) {
        this.draw(J2DPrismGraphics.tmpRect(f2, f3, f4, f5));
    }

    @Override
    public void drawRoundRect(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.draw(J2DPrismGraphics.tmpRRect(f2, f3, f4, f5, f6, f7));
    }

    @Override
    public void drawEllipse(float f2, float f3, float f4, float f5) {
        this.draw(J2DPrismGraphics.tmpEllipse(f2, f3, f4, f5));
    }

    @Override
    public void setNodeBounds(RectBounds rectBounds) {
        this.nodeBounds = rectBounds != null ? new Rectangle2D.Float(rectBounds.getMinX(), rectBounds.getMinY(), rectBounds.getWidth(), rectBounds.getHeight()) : null;
    }

    private void drawString(GlyphList glyphList, int n2, int n3, FontStrike fontStrike, float f2, float f3) {
        if (n2 == n3) {
            return;
        }
        int n4 = n3 - n2;
        int[] arrn = new int[n4];
        for (int i2 = 0; i2 < n4; ++i2) {
            arrn[i2] = glyphList.getGlyphCode(n2 + i2) & 0xFFFFFF;
        }
        Font font = J2DPrismGraphics.toJ2DFont(fontStrike);
        GlyphVector glyphVector = font.createGlyphVector(this.g2d.getFontRenderContext(), arrn);
        Point2D.Float float_ = new Point2D.Float();
        for (int i3 = 0; i3 < n4; ++i3) {
            ((Point2D)float_).setLocation(glyphList.getPosX(n2 + i3), glyphList.getPosY(n2 + i3));
            glyphVector.setGlyphPosition(i3, float_);
        }
        this.g2d.drawGlyphVector(glyphVector, f2, f3);
    }

    @Override
    public void drawString(GlyphList glyphList, FontStrike fontStrike, float f2, float f3, com.sun.prism.paint.Color color, int n2, int n3) {
        int n4;
        Object object;
        int n5 = glyphList.getGlyphCount();
        if (n5 == 0) {
            return;
        }
        this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (fontStrike.getAAMode() == 1) {
            this.g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        }
        if (this.paintWasProportional) {
            object = this.nodeBounds;
            if (object == null) {
                Metrics metrics = fontStrike.getMetrics();
                object = new Rectangle2D.Float(0.0f, metrics.getAscent(), glyphList.getWidth(), metrics.getLineHeight());
            }
            this.g2d.setPaint(this.toJ2DPaint(this.paint, (Rectangle2D)object));
        }
        object = null;
        int n6 = 0;
        if (fontStrike instanceof CompositeStrike) {
            object = (CompositeStrike)fontStrike;
            int n7 = glyphList.getGlyphCode(0);
            n6 = ((CompositeStrike)object).getStrikeSlotForGlyph(n7);
        }
        Color color2 = null;
        Color color3 = null;
        int n8 = 0;
        if (color != null) {
            color2 = J2DPrismGraphics.toJ2DColor(color);
            color3 = this.g2d.getColor();
            n4 = glyphList.getCharOffset(0);
            n8 = n2 <= n4 && n4 < n3 ? 1 : 0;
        }
        n4 = 0;
        if (color2 != null || object != null) {
            for (int i2 = 1; i2 < n5; ++i2) {
                int n9;
                int n10;
                if (color2 != null) {
                    n10 = glyphList.getCharOffset(i2);
                    int n11 = n9 = n2 <= n10 && n10 < n3 ? 1 : 0;
                    if (n8 != n9) {
                        if (object != null) {
                            fontStrike = ((CompositeStrike)object).getStrikeSlot(n6);
                        }
                        this.g2d.setColor(n8 != 0 ? color2 : color3);
                        this.drawString(glyphList, n4, i2, fontStrike, f2, f3);
                        n4 = i2;
                        n8 = n9;
                    }
                }
                if (object == null || n6 == (n9 = ((CompositeStrike)object).getStrikeSlotForGlyph(n10 = glyphList.getGlyphCode(i2)))) continue;
                fontStrike = ((CompositeStrike)object).getStrikeSlot(n6);
                if (color2 != null) {
                    this.g2d.setColor(n8 != 0 ? color2 : color3);
                }
                this.drawString(glyphList, n4, i2, fontStrike, f2, f3);
                n4 = i2;
                n6 = n9;
            }
            if (object != null) {
                fontStrike = ((CompositeStrike)object).getStrikeSlot(n6);
            }
            if (color2 != null) {
                this.g2d.setColor(n8 != 0 ? color2 : color3);
            }
        }
        this.drawString(glyphList, n4, n5, fontStrike, f2, f3);
        if (color != null) {
            this.g2d.setColor(color3);
        }
        this.g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    protected void setTransformG2D(AffineTransform affineTransform) {
        this.g2d.setTransform(affineTransform);
    }

    protected void captureTransform(Graphics2D graphics2D) {
    }

    @Override
    public void drawMappedTextureRaw(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        BufferedImage bufferedImage = ((J2DTexture)texture).getBufferedImage();
        float f14 = f8 - f6;
        float f15 = f9 - f7;
        float f16 = f10 - f6;
        float f17 = f11 - f7;
        this.setTransformG2D(J2D_IDENTITY);
        tmpAT.setTransform(f14, f15, f16, f17, f6, f7);
        try {
            tmpAT.invert();
            this.g2d.translate(f2, f3);
            this.g2d.scale(f4 - f2, f5 - f3);
            this.g2d.transform(tmpAT);
            this.g2d.drawImage(bufferedImage, 0, 0, 1, 1, null);
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            // empty catch block
        }
        this.setTransform(this.transform);
    }

    @Override
    public void drawTexture(Texture texture, float f2, float f3, float f4, float f5) {
        BufferedImage bufferedImage = ((J2DTexture)texture).getBufferedImage();
        this.g2d.drawImage(bufferedImage, (int)f2, (int)f3, (int)(f2 + f4), (int)(f3 + f5), 0, 0, (int)f4, (int)f5, null);
    }

    @Override
    public void drawTexture(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        BufferedImage bufferedImage = ((J2DTexture)texture).getBufferedImage();
        this.g2d.drawImage(bufferedImage, (int)f2, (int)f3, (int)f4, (int)f5, (int)f6, (int)f7, (int)f8, (int)f9, null);
    }

    @Override
    public void drawTexture3SliceH(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        if (f12 + 0.1f > f13) {
            f13 += 1.0f;
        }
        this.drawTexture(texture, f2, f3, f10, f5, f6, f7, f12, f9);
        this.drawTexture(texture, f10, f3, f11, f5, f12, f7, f13, f9);
        this.drawTexture(texture, f11, f3, f4, f5, f13, f7, f8, f9);
    }

    @Override
    public void drawTexture3SliceV(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        if (f12 + 0.1f > f13) {
            f13 += 1.0f;
        }
        this.drawTexture(texture, f2, f3, f4, f10, f6, f7, f8, f12);
        this.drawTexture(texture, f2, f10, f4, f11, f6, f12, f8, f13);
        this.drawTexture(texture, f2, f11, f4, f5, f6, f13, f8, f9);
    }

    @Override
    public void drawTexture9Slice(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17) {
        if (f14 + 0.1f > f16) {
            f16 += 1.0f;
        }
        if (f15 + 0.1f > f17) {
            f17 += 1.0f;
        }
        this.drawTexture(texture, f2, f3, f10, f11, f6, f7, f14, f15);
        this.drawTexture(texture, f10, f3, f12, f11, f14, f7, f16, f15);
        this.drawTexture(texture, f12, f3, f4, f11, f16, f7, f8, f15);
        this.drawTexture(texture, f2, f11, f10, f13, f6, f15, f14, f17);
        this.drawTexture(texture, f10, f11, f12, f13, f14, f15, f16, f17);
        this.drawTexture(texture, f12, f11, f4, f13, f16, f15, f8, f17);
        this.drawTexture(texture, f2, f13, f10, f5, f6, f17, f14, f9);
        this.drawTexture(texture, f10, f13, f12, f5, f14, f17, f16, f9);
        this.drawTexture(texture, f12, f13, f4, f5, f16, f17, f8, f9);
    }

    @Override
    public void drawTextureRaw(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        int n2 = texture.getContentWidth();
        int n3 = texture.getContentHeight();
        this.drawTexture(texture, f2, f3, f4, f5, f6 *= (float)n2, f7 *= (float)n3, f8 *= (float)n2, f9 *= (float)n3);
    }

    @Override
    public void drawTextureVO(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        java.awt.Paint paint = this.g2d.getPaint();
        Composite composite = this.g2d.getComposite();
        Color color = new Color(1.0f, 1.0f, 1.0f, f2);
        Color color2 = new Color(1.0f, 1.0f, 1.0f, f3);
        this.g2d.setPaint(new GradientPaint(0.0f, f5, color, 0.0f, f7, color2, true));
        this.g2d.setComposite(AlphaComposite.Src);
        int n2 = (int)Math.floor(Math.min(f4, f6));
        int n3 = (int)Math.floor(Math.min(f5, f7));
        int n4 = (int)Math.ceil(Math.max(f4, f6)) - n2;
        int n5 = (int)Math.ceil(Math.max(f5, f7)) - n3;
        this.g2d.fillRect(n2, n3, n4, n5);
        this.g2d.setComposite(AlphaComposite.SrcIn);
        this.drawTexture(texture, f4, f5, f6, f7, f8, f9, f10, f11);
        this.g2d.setComposite(composite);
        this.g2d.setPaint(paint);
    }

    @Override
    public void drawPixelsMasked(RTTexture rTTexture, RTTexture rTTexture2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        this.doDrawMaskTexture((J2DRTTexture)rTTexture, (J2DRTTexture)rTTexture2, n2, n3, n4, n5, n6, n7, n8, n9, true);
    }

    @Override
    public void maskInterpolatePixels(RTTexture rTTexture, RTTexture rTTexture2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        this.doDrawMaskTexture((J2DRTTexture)rTTexture, (J2DRTTexture)rTTexture2, n2, n3, n4, n5, n6, n7, n8, n9, false);
    }

    private void doDrawMaskTexture(J2DRTTexture j2DRTTexture, J2DRTTexture j2DRTTexture2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, boolean bl) {
        int n10;
        int n11 = this.clipRect.x;
        int n12 = this.clipRect.y;
        int n13 = n11 + this.clipRect.width;
        int n14 = n12 + this.clipRect.height;
        if (n4 <= 0 || n5 <= 0) {
            return;
        }
        if (n2 < n11) {
            n10 = n11 - n2;
            if ((n4 -= n10) <= 0) {
                return;
            }
            n6 += n10;
            n8 += n10;
            n2 = n11;
        }
        if (n3 < n12) {
            n10 = n12 - n3;
            if ((n5 -= n10) <= 0) {
                return;
            }
            n7 += n10;
            n9 += n10;
            n3 = n12;
        }
        if (n2 + n4 > n13 && (n4 = n13 - n2) <= 0) {
            return;
        }
        if (n3 + n5 > n14 && (n5 = n14 - n3) <= 0) {
            return;
        }
        n10 = j2DRTTexture.getContentWidth();
        int n15 = j2DRTTexture.getContentHeight();
        if (n6 < 0) {
            if ((n4 += n6) <= 0) {
                return;
            }
            n2 -= n6;
            n8 -= n6;
            n6 = 0;
        }
        if (n7 < 0) {
            if ((n5 += n7) <= 0) {
                return;
            }
            n3 -= n7;
            n9 -= n7;
            n7 = 0;
        }
        if (n6 + n4 > n10 && (n4 = n10 - n6) <= 0) {
            return;
        }
        if (n7 + n5 > n15 && (n5 = n15 - n7) <= 0) {
            return;
        }
        int n16 = j2DRTTexture2.getContentWidth();
        int n17 = j2DRTTexture2.getContentHeight();
        if (n8 < 0) {
            if ((n4 += n8) <= 0) {
                return;
            }
            n2 -= n8;
            n6 -= n8;
            n8 = 0;
        }
        if (n9 < 0) {
            if ((n5 += n9) <= 0) {
                return;
            }
            n3 -= n9;
            n7 -= n9;
            n9 = 0;
        }
        if (n8 + n4 > n16 && (n4 = n16 - n8) <= 0) {
            return;
        }
        if (n9 + n5 > n17 && (n5 = n17 - n9) <= 0) {
            return;
        }
        int[] arrn = j2DRTTexture.getPixels();
        int[] arrn2 = j2DRTTexture2.getPixels();
        DataBuffer dataBuffer = this.target.getBackBuffer().getRaster().getDataBuffer();
        int[] arrn3 = ((DataBufferInt)dataBuffer).getData();
        int n18 = j2DRTTexture.getBufferedImage().getWidth();
        int n19 = j2DRTTexture2.getBufferedImage().getWidth();
        int n20 = this.target.getBackBuffer().getWidth();
        int n21 = n7 * n18 + n6;
        int n22 = n9 * n19 + n8;
        int n23 = n3 * n20 + n2;
        if (bl) {
            for (int i2 = 0; i2 < n5; ++i2) {
                for (int i3 = 0; i3 < n4; ++i3) {
                    int n24;
                    int n25;
                    int n26;
                    int n27;
                    int n28;
                    int n29 = arrn2[n22 + i3] >>> 24;
                    if (n29 == 0 || (n28 = (n27 = arrn[n21 + i3]) >>> 24) == 0) continue;
                    if (n29 < 255) {
                        n29 += n29 >> 7;
                        n28 *= n29;
                        n26 = (n27 >> 16 & 0xFF) * n29;
                        n25 = (n27 >> 8 & 0xFF) * n29;
                        n24 = (n27 & 0xFF) * n29;
                    } else if (n28 < 255) {
                        n28 <<= 8;
                        n26 = (n27 >> 16 & 0xFF) << 8;
                        n25 = (n27 >> 8 & 0xFF) << 8;
                        n24 = (n27 & 0xFF) << 8;
                    } else {
                        arrn3[n23 + i3] = n27;
                        continue;
                    }
                    n29 = n28 + 128 >> 8;
                    n29 += n29 >> 7;
                    n29 = 256 - n29;
                    n27 = arrn3[n23 + i3];
                    n28 += (n27 >>> 24) * n29 + 128;
                    n26 += (n27 >> 16 & 0xFF) * n29 + 128;
                    n25 += (n27 >> 8 & 0xFF) * n29 + 128;
                    n24 += (n27 & 0xFF) * n29 + 128;
                    arrn3[n23 + i3] = n27 = (n28 >> 8 << 24) + (n26 >> 8 << 16) + (n25 >> 8 << 8) + (n24 >> 8);
                }
                n21 += n18;
                n22 += n19;
                n23 += n20;
            }
        } else {
            for (int i4 = 0; i4 < n5; ++i4) {
                for (int i5 = 0; i5 < n4; ++i5) {
                    int n30 = arrn2[n22 + i5] >>> 24;
                    if (n30 == 0) continue;
                    int n31 = arrn[n21 + i5];
                    if (n30 < 255) {
                        n30 += n30 >> 7;
                        int n32 = (n31 >>> 24) * n30;
                        int n33 = (n31 >> 16 & 0xFF) * n30;
                        int n34 = (n31 >> 8 & 0xFF) * n30;
                        int n35 = (n31 & 0xFF) * n30;
                        n30 = 256 - n30;
                        n31 = arrn3[n23 + i5];
                        n32 += (n31 >>> 24) * n30 + 128;
                        n33 += (n31 >> 16 & 0xFF) * n30 + 128;
                        n34 += (n31 >> 8 & 0xFF) * n30 + 128;
                        n35 += (n31 & 0xFF) * n30 + 128;
                        n31 = (n32 >> 8 << 24) + (n33 >> 8 << 16) + (n34 >> 8 << 8) + (n35 >> 8);
                    }
                    arrn3[n23 + i5] = n31;
                }
                n21 += n18;
                n22 += n19;
                n23 += n20;
            }
        }
    }

    @Override
    public boolean canReadBack() {
        return true;
    }

    @Override
    public RTTexture readBack(Rectangle rectangle) {
        J2DRTTexture j2DRTTexture = this.target.getReadbackBuffer();
        Graphics2D graphics2D = j2DRTTexture.createAWTGraphics2D();
        graphics2D.setComposite(AlphaComposite.Src);
        int n2 = rectangle.x;
        int n3 = rectangle.y;
        int n4 = rectangle.width;
        int n5 = rectangle.height;
        int n6 = n2 + n4;
        int n7 = n3 + n5;
        graphics2D.drawImage(this.target.getBackBuffer(), 0, 0, n4, n5, n2, n3, n6, n7, null);
        graphics2D.dispose();
        return j2DRTTexture;
    }

    @Override
    public void releaseReadBackBuffer(RTTexture rTTexture) {
    }

    @Override
    public NGCamera getCameraNoClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isDepthBuffer() {
        return false;
    }

    @Override
    public boolean isDepthTest() {
        return false;
    }

    @Override
    public boolean isAlphaTestShader() {
        if (PrismSettings.verbose && PrismSettings.forceAlphaTestShader) {
            System.out.println("J2D pipe doesn't support shader with alpha testing");
        }
        return false;
    }

    @Override
    public void setAntialiasedShape(boolean bl) {
        this.antialiasedShape = bl;
        this.g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antialiasedShape ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
    }

    @Override
    public boolean isAntialiasedShape() {
        return this.antialiasedShape;
    }

    @Override
    public void scale(float f2, float f3, float f4) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTransform3D(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        if (d4 != 0.0 || d8 != 0.0 || d10 != 0.0 || d11 != 0.0 || d12 != 1.0 || d13 != 0.0) {
            throw new UnsupportedOperationException("3D transforms not supported.");
        }
        this.setTransform(d2, d6, d3, d7, d5, d9);
    }

    @Override
    public void setCamera(NGCamera nGCamera) {
    }

    @Override
    public void setDepthBuffer(boolean bl) {
    }

    @Override
    public void setDepthTest(boolean bl) {
    }

    @Override
    public void sync() {
    }

    @Override
    public void translate(float f2, float f3, float f4) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCulling(boolean bl) {
        this.cull = bl;
    }

    public boolean isCulling() {
        return this.cull;
    }

    @Override
    public void setClipRectIndex(int n2) {
        this.clipRectIndex = n2;
    }

    @Override
    public int getClipRectIndex() {
        return this.clipRectIndex;
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
    public void setRenderRoot(NodePath nodePath) {
        this.renderRoot = nodePath;
    }

    @Override
    public NodePath getRenderRoot() {
        return this.renderRoot;
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
    public void blit(RTTexture rTTexture, RTTexture rTTexture2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLights(NGLightBase[] arrnGLightBase) {
    }

    @Override
    public NGLightBase[] getLights() {
        return null;
    }

    static class OuterStroke
    extends FilterStroke {
        static double SQRT_2 = Math.sqrt(2.0);

        OuterStroke(java.awt.BasicStroke basicStroke) {
            super(basicStroke);
        }

        @Override
        protected Shape makeStrokedRect(Rectangle2D rectangle2D) {
            if (this.stroke.getDashArray() != null) {
                return null;
            }
            float f2 = this.stroke.getLineWidth() / 2.0f;
            float f3 = (float)rectangle2D.getX();
            float f4 = (float)rectangle2D.getY();
            float f5 = f3 + (float)rectangle2D.getWidth();
            float f6 = f4 + (float)rectangle2D.getHeight();
            GeneralPath generalPath = new GeneralPath();
            generalPath.moveTo(f3, f4);
            generalPath.lineTo(f5, f4);
            generalPath.lineTo(f5, f6);
            generalPath.lineTo(f3, f6);
            generalPath.closePath();
            float f7 = f3 - f2;
            float f8 = f4 - f2;
            float f9 = f5 + f2;
            float f10 = f6 + f2;
            switch (this.stroke.getLineJoin()) {
                case 0: {
                    if ((double)this.stroke.getMiterLimit() >= SQRT_2) {
                        generalPath.moveTo(f7, f8);
                        generalPath.lineTo(f7, f10);
                        generalPath.lineTo(f9, f10);
                        generalPath.lineTo(f9, f8);
                        generalPath.closePath();
                        break;
                    }
                }
                case 2: {
                    generalPath.moveTo(f7, f4);
                    generalPath.lineTo(f7, f6);
                    generalPath.lineTo(f3, f10);
                    generalPath.lineTo(f5, f10);
                    generalPath.lineTo(f9, f6);
                    generalPath.lineTo(f9, f4);
                    generalPath.lineTo(f5, f8);
                    generalPath.lineTo(f3, f8);
                    generalPath.closePath();
                    break;
                }
                case 1: {
                    generalPath.moveTo(f7, f4);
                    generalPath.lineTo(f7, f6);
                    OuterStroke.cornerArc(generalPath, f7, f6, f7, f10, f3, f10);
                    generalPath.lineTo(f5, f10);
                    OuterStroke.cornerArc(generalPath, f5, f10, f9, f10, f9, f6);
                    generalPath.lineTo(f9, f4);
                    OuterStroke.cornerArc(generalPath, f9, f4, f9, f8, f5, f8);
                    generalPath.lineTo(f3, f8);
                    OuterStroke.cornerArc(generalPath, f3, f8, f7, f8, f7, f4);
                    generalPath.closePath();
                    break;
                }
                default: {
                    throw new InternalError("Unrecognized line join style");
                }
            }
            return generalPath;
        }

        protected Shape makeStrokedEllipse(Ellipse2D ellipse2D) {
            float f2;
            if (this.stroke.getDashArray() != null) {
                return null;
            }
            float f3 = this.stroke.getLineWidth() / 2.0f;
            float f4 = (float)ellipse2D.getWidth();
            if (f4 > (f2 = (float)ellipse2D.getHeight()) * 2.0f || f2 > f4 * 2.0f) {
                return null;
            }
            float f5 = (float)ellipse2D.getX();
            float f6 = (float)ellipse2D.getY();
            float f7 = f5 + f4 / 2.0f;
            float f8 = f6 + f2 / 2.0f;
            float f9 = f5 + f4;
            float f10 = f6 + f2;
            GeneralPath generalPath = new GeneralPath();
            generalPath.moveTo(f7, f6);
            OuterStroke.cornerArc(generalPath, f7, f6, f9, f6, f9, f8);
            OuterStroke.cornerArc(generalPath, f9, f8, f9, f10, f7, f10);
            OuterStroke.cornerArc(generalPath, f7, f10, f5, f10, f5, f8);
            OuterStroke.cornerArc(generalPath, f5, f8, f5, f6, f7, f6);
            generalPath.closePath();
            generalPath.moveTo(f7, f6 -= f3);
            OuterStroke.cornerArc(generalPath, f7, f6, f5 -= f3, f6, f5, f8);
            OuterStroke.cornerArc(generalPath, f5, f8, f5, f10 += f3, f7, f10);
            OuterStroke.cornerArc(generalPath, f7, f10, f9 += f3, f10, f9, f8);
            OuterStroke.cornerArc(generalPath, f9, f8, f9, f6, f7, f6);
            generalPath.closePath();
            return generalPath;
        }

        @Override
        protected Shape makeStrokedShape(Shape shape) {
            Shape shape2 = this.stroke.createStrokedShape(shape);
            Area area = new Area(shape2);
            area.subtract(new Area(shape));
            return area;
        }
    }

    static class InnerStroke
    extends FilterStroke {
        InnerStroke(java.awt.BasicStroke basicStroke) {
            super(basicStroke);
        }

        @Override
        protected Shape makeStrokedRect(Rectangle2D rectangle2D) {
            if (this.stroke.getDashArray() != null) {
                return null;
            }
            float f2 = this.stroke.getLineWidth() / 2.0f;
            if ((double)f2 >= rectangle2D.getWidth() || (double)f2 >= rectangle2D.getHeight()) {
                return rectangle2D;
            }
            float f3 = (float)rectangle2D.getX();
            float f4 = (float)rectangle2D.getY();
            float f5 = f3 + (float)rectangle2D.getWidth();
            float f6 = f4 + (float)rectangle2D.getHeight();
            GeneralPath generalPath = new GeneralPath();
            generalPath.moveTo(f3, f4);
            generalPath.lineTo(f5, f4);
            generalPath.lineTo(f5, f6);
            generalPath.lineTo(f3, f6);
            generalPath.closePath();
            generalPath.moveTo(f3 += f2, f4 += f2);
            generalPath.lineTo(f3, f6 -= f2);
            generalPath.lineTo(f5 -= f2, f6);
            generalPath.lineTo(f5, f4);
            generalPath.closePath();
            return generalPath;
        }

        protected Shape makeStrokedEllipse(Ellipse2D ellipse2D) {
            float f2;
            if (this.stroke.getDashArray() != null) {
                return null;
            }
            float f3 = this.stroke.getLineWidth() / 2.0f;
            float f4 = (float)ellipse2D.getWidth();
            if (f4 - 2.0f * f3 > (f2 = (float)ellipse2D.getHeight()) * 2.0f || f2 - 2.0f * f3 > f4 * 2.0f) {
                return null;
            }
            if (f3 >= f4 || f3 >= f2) {
                return ellipse2D;
            }
            float f5 = (float)ellipse2D.getX();
            float f6 = (float)ellipse2D.getY();
            float f7 = f5 + f4 / 2.0f;
            float f8 = f6 + f2 / 2.0f;
            float f9 = f5 + f4;
            float f10 = f6 + f2;
            GeneralPath generalPath = new GeneralPath();
            generalPath.moveTo(f7, f6);
            InnerStroke.cornerArc(generalPath, f7, f6, f9, f6, f9, f8);
            InnerStroke.cornerArc(generalPath, f9, f8, f9, f10, f7, f10);
            InnerStroke.cornerArc(generalPath, f7, f10, f5, f10, f5, f8);
            InnerStroke.cornerArc(generalPath, f5, f8, f5, f6, f7, f6);
            generalPath.closePath();
            generalPath.moveTo(f7, f6 += f3);
            InnerStroke.cornerArc(generalPath, f7, f6, f5 += f3, f6, f5, f8);
            InnerStroke.cornerArc(generalPath, f5, f8, f5, f10 -= f3, f7, f10);
            InnerStroke.cornerArc(generalPath, f7, f10, f9 -= f3, f10, f9, f8);
            InnerStroke.cornerArc(generalPath, f9, f8, f9, f6, f7, f6);
            generalPath.closePath();
            return generalPath;
        }

        @Override
        protected Shape makeStrokedShape(Shape shape) {
            Shape shape2 = this.stroke.createStrokedShape(shape);
            Area area = new Area(shape2);
            area.intersect(new Area(shape));
            return area;
        }
    }

    static abstract class FilterStroke
    implements Stroke {
        protected java.awt.BasicStroke stroke;
        static final double CtrlVal = 0.5522847498307933;

        FilterStroke(java.awt.BasicStroke basicStroke) {
            this.stroke = basicStroke;
        }

        protected abstract Shape makeStrokedRect(Rectangle2D var1);

        protected abstract Shape makeStrokedShape(Shape var1);

        @Override
        public Shape createStrokedShape(Shape shape) {
            Shape shape2;
            if (shape instanceof Rectangle2D && (shape2 = this.makeStrokedRect((Rectangle2D)shape)) != null) {
                return shape2;
            }
            return this.makeStrokedShape(shape);
        }

        static Point2D cornerArc(GeneralPath generalPath, float f2, float f3, float f4, float f5, float f6, float f7) {
            return FilterStroke.cornerArc(generalPath, f2, f3, f4, f5, f6, f7, 0.5f);
        }

        static Point2D cornerArc(GeneralPath generalPath, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
            float f9 = (float)((double)f2 + 0.5522847498307933 * (double)(f4 - f2));
            float f10 = (float)((double)f3 + 0.5522847498307933 * (double)(f5 - f3));
            float f11 = (float)((double)f6 + 0.5522847498307933 * (double)(f4 - f6));
            float f12 = (float)((double)f7 + 0.5522847498307933 * (double)(f5 - f7));
            generalPath.curveTo(f9, f10, f11, f12, f6, f7);
            return new Point2D.Float(FilterStroke.eval(f2, f9, f11, f6, f8), FilterStroke.eval(f3, f10, f12, f7, f8));
        }

        static float eval(float f2, float f3, float f4, float f5, float f6) {
            f2 += (f3 - f2) * f6;
            f3 += (f4 - f3) * f6;
            f4 += (f5 - f4) * f6;
            f2 += (f3 - f2) * f6;
            f3 += (f4 - f3) * f6;
            return f2 + (f3 - f2) * f6;
        }
    }

    private static class AdaptorPathIterator
    implements PathIterator {
        private static int[] NUM_COORDS = new int[]{2, 2, 4, 6, 0};
        com.sun.javafx.geom.PathIterator priterator;
        float[] tmpcoords;

        private AdaptorPathIterator() {
        }

        public void setIterator(com.sun.javafx.geom.PathIterator pathIterator) {
            this.priterator = pathIterator;
        }

        @Override
        public int currentSegment(float[] arrf) {
            return this.priterator.currentSegment(arrf);
        }

        @Override
        public int currentSegment(double[] arrd) {
            if (this.tmpcoords == null) {
                this.tmpcoords = new float[6];
            }
            int n2 = this.priterator.currentSegment(this.tmpcoords);
            for (int i2 = 0; i2 < NUM_COORDS[n2]; ++i2) {
                arrd[i2] = this.tmpcoords[i2];
            }
            return n2;
        }

        @Override
        public int getWindingRule() {
            return this.priterator.getWindingRule();
        }

        @Override
        public boolean isDone() {
            return this.priterator.isDone();
        }

        @Override
        public void next() {
            this.priterator.next();
        }
    }

    private static class AdaptorShape
    implements Shape {
        private com.sun.javafx.geom.Shape prshape;
        private static AdaptorPathIterator tmpAdaptor = new AdaptorPathIterator();

        private AdaptorShape() {
        }

        public void setShape(com.sun.javafx.geom.Shape shape) {
            this.prshape = shape;
        }

        @Override
        public boolean contains(double d2, double d3) {
            return this.prshape.contains((float)d2, (float)d3);
        }

        @Override
        public boolean contains(Point2D point2D) {
            return this.contains(point2D.getX(), point2D.getY());
        }

        @Override
        public boolean contains(double d2, double d3, double d4, double d5) {
            return this.prshape.contains((float)d2, (float)d3, (float)d4, (float)d5);
        }

        @Override
        public boolean contains(Rectangle2D rectangle2D) {
            return this.contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
        }

        @Override
        public boolean intersects(double d2, double d3, double d4, double d5) {
            return this.prshape.intersects((float)d2, (float)d3, (float)d4, (float)d5);
        }

        @Override
        public boolean intersects(Rectangle2D rectangle2D) {
            return this.intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
        }

        @Override
        public java.awt.Rectangle getBounds() {
            return this.getBounds2D().getBounds();
        }

        @Override
        public Rectangle2D getBounds2D() {
            RectBounds rectBounds = this.prshape.getBounds();
            Rectangle2D.Float float_ = new Rectangle2D.Float();
            float_.setFrameFromDiagonal(rectBounds.getMinX(), rectBounds.getMinY(), rectBounds.getMaxX(), rectBounds.getMaxY());
            return float_;
        }

        private static PathIterator tmpAdaptor(com.sun.javafx.geom.PathIterator pathIterator) {
            tmpAdaptor.setIterator(pathIterator);
            return tmpAdaptor;
        }

        @Override
        public PathIterator getPathIterator(AffineTransform affineTransform) {
            BaseTransform baseTransform = affineTransform == null ? null : J2DPrismGraphics.toPrTransform(affineTransform);
            return AdaptorShape.tmpAdaptor(this.prshape.getPathIterator(baseTransform));
        }

        @Override
        public PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
            BaseTransform baseTransform = affineTransform == null ? null : J2DPrismGraphics.toPrTransform(affineTransform);
            return AdaptorShape.tmpAdaptor(this.prshape.getPathIterator(baseTransform, (float)d2));
        }
    }
}

