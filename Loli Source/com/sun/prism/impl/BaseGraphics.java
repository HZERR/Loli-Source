/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.glass.ui.Screen;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NodePath;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.PixelFormat;
import com.sun.prism.RectShadowGraphics;
import com.sun.prism.RenderTarget;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseContext;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Paint;

public abstract class BaseGraphics
implements RectShadowGraphics {
    private static final BasicStroke DEFAULT_STROKE = new BasicStroke(1.0f, 2, 0, 10.0f);
    private static final Paint DEFAULT_PAINT = Color.WHITE;
    protected static final RoundRectangle2D scratchRRect = new RoundRectangle2D();
    protected static final Ellipse2D scratchEllipse = new Ellipse2D();
    protected static final Line2D scratchLine = new Line2D();
    protected static final BaseTransform IDENT = BaseTransform.IDENTITY_TRANSFORM;
    private final Affine3D transform3D = new Affine3D();
    private NGCamera camera = NGCamera.INSTANCE;
    private RectBounds devClipRect;
    private RectBounds finalClipRect;
    protected RectBounds nodeBounds = null;
    private Rectangle clipRect;
    private int clipRectIndex;
    private boolean hasPreCullingBits = false;
    private float extraAlpha = 1.0f;
    private CompositeMode compMode;
    private boolean antialiasedShape = true;
    private boolean depthBuffer = false;
    private boolean depthTest = false;
    protected Paint paint = DEFAULT_PAINT;
    protected BasicStroke stroke = DEFAULT_STROKE;
    protected boolean isSimpleTranslate = true;
    protected float transX;
    protected float transY;
    private final BaseContext context;
    private final RenderTarget renderTarget;
    private boolean state3D = false;
    private float pixelScale = 1.0f;
    private NodePath renderRoot;

    protected BaseGraphics(BaseContext baseContext, RenderTarget renderTarget) {
        this.context = baseContext;
        this.renderTarget = renderTarget;
        this.devClipRect = new RectBounds(0.0f, 0.0f, renderTarget.getContentWidth(), renderTarget.getContentHeight());
        this.finalClipRect = new RectBounds(this.devClipRect);
        this.compMode = CompositeMode.SRC_OVER;
        if (baseContext != null) {
            baseContext.setRenderTarget(this);
        }
    }

    protected NGCamera getCamera() {
        return this.camera;
    }

    @Override
    public RenderTarget getRenderTarget() {
        return this.renderTarget;
    }

    @Override
    public void setState3D(boolean bl) {
        this.state3D = bl;
    }

    @Override
    public boolean isState3D() {
        return this.state3D;
    }

    @Override
    public Screen getAssociatedScreen() {
        return this.context.getAssociatedScreen();
    }

    @Override
    public ResourceFactory getResourceFactory() {
        return this.context.getResourceFactory();
    }

    @Override
    public BaseTransform getTransformNoClone() {
        return this.transform3D;
    }

    @Override
    public void setTransform(BaseTransform baseTransform) {
        if (baseTransform == null) {
            this.transform3D.setToIdentity();
        } else {
            this.transform3D.setTransform(baseTransform);
        }
        this.validateTransformAndPaint();
    }

    @Override
    public void setTransform(double d2, double d3, double d4, double d5, double d6, double d7) {
        this.transform3D.setTransform(d2, d3, d4, d5, d6, d7);
        this.validateTransformAndPaint();
    }

    @Override
    public void setTransform3D(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13) {
        this.transform3D.setTransform(d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13);
        this.validateTransformAndPaint();
    }

    @Override
    public void transform(BaseTransform baseTransform) {
        this.transform3D.concatenate(baseTransform);
        this.validateTransformAndPaint();
    }

    @Override
    public void translate(float f2, float f3) {
        if (f2 != 0.0f || f3 != 0.0f) {
            this.transform3D.translate(f2, f3);
            this.validateTransformAndPaint();
        }
    }

    @Override
    public void translate(float f2, float f3, float f4) {
        if (f2 != 0.0f || f3 != 0.0f || f4 != 0.0f) {
            this.transform3D.translate(f2, f3, f4);
            this.validateTransformAndPaint();
        }
    }

    @Override
    public void scale(float f2, float f3) {
        if (f2 != 1.0f || f3 != 1.0f) {
            this.transform3D.scale(f2, f3);
            this.validateTransformAndPaint();
        }
    }

    @Override
    public void scale(float f2, float f3, float f4) {
        if (f2 != 1.0f || f3 != 1.0f || f4 != 1.0f) {
            this.transform3D.scale(f2, f3, f4);
            this.validateTransformAndPaint();
        }
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
    public final void setRenderRoot(NodePath nodePath) {
        this.renderRoot = nodePath;
    }

    @Override
    public final NodePath getRenderRoot() {
        return this.renderRoot;
    }

    private void validateTransformAndPaint() {
        if (this.transform3D.isTranslateOrIdentity() && this.paint.getType() == Paint.Type.COLOR) {
            this.isSimpleTranslate = true;
            this.transX = (float)this.transform3D.getMxt();
            this.transY = (float)this.transform3D.getMyt();
        } else {
            this.isSimpleTranslate = false;
            this.transX = 0.0f;
            this.transY = 0.0f;
        }
    }

    @Override
    public NGCamera getCameraNoClone() {
        return this.camera;
    }

    @Override
    public void setDepthTest(boolean bl) {
        this.depthTest = bl;
    }

    @Override
    public boolean isDepthTest() {
        return this.depthTest;
    }

    @Override
    public void setDepthBuffer(boolean bl) {
        this.depthBuffer = bl;
    }

    @Override
    public boolean isDepthBuffer() {
        return this.depthBuffer;
    }

    @Override
    public boolean isAlphaTestShader() {
        return PrismSettings.forceAlphaTestShader || this.isDepthTest() && this.isDepthBuffer();
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
    public void setPixelScaleFactor(float f2) {
        this.pixelScale = f2;
    }

    @Override
    public float getPixelScaleFactor() {
        return this.pixelScale;
    }

    @Override
    public void setCamera(NGCamera nGCamera) {
        this.camera = nGCamera;
    }

    @Override
    public Rectangle getClipRect() {
        return this.clipRect != null ? new Rectangle(this.clipRect) : null;
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
        } else {
            this.clipRect = new Rectangle(rectangle);
            this.finalClipRect.intersectWith(rectangle);
        }
    }

    @Override
    public float getExtraAlpha() {
        return this.extraAlpha;
    }

    @Override
    public void setExtraAlpha(float f2) {
        this.extraAlpha = f2;
    }

    @Override
    public CompositeMode getCompositeMode() {
        return this.compMode;
    }

    @Override
    public void setCompositeMode(CompositeMode compositeMode) {
        this.compMode = compositeMode;
    }

    @Override
    public Paint getPaint() {
        return this.paint;
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;
        this.validateTransformAndPaint();
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
    public void clear() {
        this.clear(Color.TRANSPARENT);
    }

    protected abstract void renderShape(Shape var1, BasicStroke var2, float var3, float var4, float var5, float var6);

    @Override
    public void fill(Shape shape) {
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        if (this.paint.isProportional()) {
            if (this.nodeBounds != null) {
                f2 = this.nodeBounds.getMinX();
                f3 = this.nodeBounds.getMinY();
                f4 = this.nodeBounds.getWidth();
                f5 = this.nodeBounds.getHeight();
            } else {
                float[] arrf = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
                Shape.accumulate(arrf, shape, BaseTransform.IDENTITY_TRANSFORM);
                f2 = arrf[0];
                f3 = arrf[1];
                f4 = arrf[2] - f2;
                f5 = arrf[3] - f3;
            }
        }
        this.renderShape(shape, null, f2, f3, f4, f5);
    }

    @Override
    public void draw(Shape shape) {
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        if (this.paint.isProportional()) {
            if (this.nodeBounds != null) {
                f2 = this.nodeBounds.getMinX();
                f3 = this.nodeBounds.getMinY();
                f4 = this.nodeBounds.getWidth();
                f5 = this.nodeBounds.getHeight();
            } else {
                float[] arrf = new float[]{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY};
                Shape.accumulate(arrf, shape, BaseTransform.IDENTITY_TRANSFORM);
                f2 = arrf[0];
                f3 = arrf[1];
                f4 = arrf[2] - f2;
                f5 = arrf[3] - f3;
            }
        }
        this.renderShape(shape, this.stroke, f2, f3, f4, f5);
    }

    @Override
    public void drawTexture(Texture texture, float f2, float f3, float f4, float f5) {
        this.drawTexture(texture, f2, f3, f2 + f4, f3 + f5, 0.0f, 0.0f, f4, f5);
    }

    @Override
    public void drawTexture(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f2, f3, f4 - f2, f5 - f3);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f2 += this.transX;
            f3 += this.transY;
            f4 += this.transX;
            f5 += this.transY;
        }
        float f10 = texture.getPhysicalWidth();
        float f11 = texture.getPhysicalHeight();
        float f12 = texture.getContentX();
        float f13 = texture.getContentY();
        float f14 = (f12 + f6) / f10;
        float f15 = (f13 + f7) / f11;
        float f16 = (f12 + f8) / f10;
        float f17 = (f13 + f9) / f11;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vertexBuffer.addSuperQuad(f2, f3, f4, f5, f14, f15, f16, f17, false);
        } else {
            vertexBuffer.addQuad(f2, f3, f4, f5, f14, f15, f16, f17);
        }
    }

    @Override
    public void drawTexture3SliceH(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f2, f3, f4 - f2, f5 - f3);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f2 += this.transX;
            f3 += this.transY;
            f4 += this.transX;
            f5 += this.transY;
            f10 += this.transX;
            f11 += this.transX;
        }
        float f14 = texture.getPhysicalWidth();
        float f15 = texture.getPhysicalHeight();
        float f16 = texture.getContentX();
        float f17 = texture.getContentY();
        float f18 = (f16 + f6) / f14;
        float f19 = (f17 + f7) / f15;
        float f20 = (f16 + f8) / f14;
        float f21 = (f17 + f9) / f15;
        float f22 = (f16 + f12) / f14;
        float f23 = (f16 + f13) / f14;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vertexBuffer.addSuperQuad(f2, f3, f10, f5, f18, f19, f22, f21, false);
            vertexBuffer.addSuperQuad(f10, f3, f11, f5, f22, f19, f23, f21, false);
            vertexBuffer.addSuperQuad(f11, f3, f4, f5, f23, f19, f20, f21, false);
        } else {
            vertexBuffer.addQuad(f2, f3, f10, f5, f18, f19, f22, f21);
            vertexBuffer.addQuad(f10, f3, f11, f5, f22, f19, f23, f21);
            vertexBuffer.addQuad(f11, f3, f4, f5, f23, f19, f20, f21);
        }
    }

    @Override
    public void drawTexture3SliceV(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f2, f3, f4 - f2, f5 - f3);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f2 += this.transX;
            f3 += this.transY;
            f4 += this.transX;
            f5 += this.transY;
            f10 += this.transY;
            f11 += this.transY;
        }
        float f14 = texture.getPhysicalWidth();
        float f15 = texture.getPhysicalHeight();
        float f16 = texture.getContentX();
        float f17 = texture.getContentY();
        float f18 = (f16 + f6) / f14;
        float f19 = (f17 + f7) / f15;
        float f20 = (f16 + f8) / f14;
        float f21 = (f17 + f9) / f15;
        float f22 = (f17 + f12) / f15;
        float f23 = (f17 + f13) / f15;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vertexBuffer.addSuperQuad(f2, f3, f4, f10, f18, f19, f20, f22, false);
            vertexBuffer.addSuperQuad(f2, f10, f4, f11, f18, f22, f20, f23, false);
            vertexBuffer.addSuperQuad(f2, f11, f4, f5, f18, f23, f20, f21, false);
        } else {
            vertexBuffer.addQuad(f2, f3, f4, f10, f18, f19, f20, f22);
            vertexBuffer.addQuad(f2, f10, f4, f11, f18, f22, f20, f23);
            vertexBuffer.addQuad(f2, f11, f4, f5, f18, f23, f20, f21);
        }
    }

    @Override
    public void drawTexture9Slice(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f2, f3, f4 - f2, f5 - f3);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f2 += this.transX;
            f3 += this.transY;
            f4 += this.transX;
            f5 += this.transY;
            f10 += this.transX;
            f11 += this.transY;
            f12 += this.transX;
            f13 += this.transY;
        }
        float f18 = texture.getPhysicalWidth();
        float f19 = texture.getPhysicalHeight();
        float f20 = texture.getContentX();
        float f21 = texture.getContentY();
        float f22 = (f20 + f6) / f18;
        float f23 = (f21 + f7) / f19;
        float f24 = (f20 + f8) / f18;
        float f25 = (f21 + f9) / f19;
        float f26 = (f20 + f14) / f18;
        float f27 = (f21 + f15) / f19;
        float f28 = (f20 + f16) / f18;
        float f29 = (f21 + f17) / f19;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (this.context.isSuperShaderEnabled()) {
            vertexBuffer.addSuperQuad(f2, f3, f10, f11, f22, f23, f26, f27, false);
            vertexBuffer.addSuperQuad(f10, f3, f12, f11, f26, f23, f28, f27, false);
            vertexBuffer.addSuperQuad(f12, f3, f4, f11, f28, f23, f24, f27, false);
            vertexBuffer.addSuperQuad(f2, f11, f10, f13, f22, f27, f26, f29, false);
            vertexBuffer.addSuperQuad(f10, f11, f12, f13, f26, f27, f28, f29, false);
            vertexBuffer.addSuperQuad(f12, f11, f4, f13, f28, f27, f24, f29, false);
            vertexBuffer.addSuperQuad(f2, f13, f10, f5, f22, f29, f26, f25, false);
            vertexBuffer.addSuperQuad(f10, f13, f12, f5, f26, f29, f28, f25, false);
            vertexBuffer.addSuperQuad(f12, f13, f4, f5, f28, f29, f24, f25, false);
        } else {
            vertexBuffer.addQuad(f2, f3, f10, f11, f22, f23, f26, f27);
            vertexBuffer.addQuad(f10, f3, f12, f11, f26, f23, f28, f27);
            vertexBuffer.addQuad(f12, f3, f4, f11, f28, f23, f24, f27);
            vertexBuffer.addQuad(f2, f11, f10, f13, f22, f27, f26, f29);
            vertexBuffer.addQuad(f10, f11, f12, f13, f26, f27, f28, f29);
            vertexBuffer.addQuad(f12, f11, f4, f13, f28, f27, f24, f29);
            vertexBuffer.addQuad(f2, f13, f10, f5, f22, f29, f26, f25);
            vertexBuffer.addQuad(f10, f13, f12, f5, f26, f29, f28, f25);
            vertexBuffer.addQuad(f12, f13, f4, f5, f28, f29, f24, f25);
        }
    }

    @Override
    public void drawTextureVO(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11) {
        BaseTransform baseTransform = this.isSimpleTranslate ? IDENT : this.getTransformNoClone();
        PixelFormat pixelFormat = texture.getPixelFormat();
        if (pixelFormat == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f4, f5, f6 - f4, f7 - f5);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        if (this.isSimpleTranslate) {
            f4 += this.transX;
            f5 += this.transY;
            f6 += this.transX;
            f7 += this.transY;
        }
        float f12 = texture.getPhysicalWidth();
        float f13 = texture.getPhysicalHeight();
        float f14 = texture.getContentX();
        float f15 = texture.getContentY();
        float f16 = (f14 + f8) / f12;
        float f17 = (f15 + f9) / f13;
        float f18 = (f14 + f10) / f12;
        float f19 = (f15 + f11) / f13;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (f2 == 1.0f && f3 == 1.0f) {
            vertexBuffer.addQuad(f4, f5, f6, f7, f16, f17, f18, f19);
        } else {
            vertexBuffer.addQuadVO(f2 *= this.getExtraAlpha(), f3 *= this.getExtraAlpha(), f4, f5, f6, f7, f16, f17, f18, f19);
        }
    }

    @Override
    public void drawTextureRaw(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        PixelFormat pixelFormat;
        float f10 = f2;
        float f11 = f3;
        float f12 = f4 - f2;
        float f13 = f5 - f3;
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f2 += this.transX;
            f3 += this.transY;
            f4 += this.transX;
            f5 += this.transY;
        }
        if ((pixelFormat = texture.getPixelFormat()) == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f10, f11, f12, f13);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f2, f3, f4, f5, f6, f7, f8, f9);
    }

    @Override
    public void drawMappedTextureRaw(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        PixelFormat pixelFormat;
        float f14 = f2;
        float f15 = f3;
        float f16 = f4 - f2;
        float f17 = f5 - f3;
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f2 += this.transX;
            f3 += this.transY;
            f4 += this.transX;
            f5 += this.transY;
        }
        if ((pixelFormat = texture.getPixelFormat()) == PixelFormat.BYTE_ALPHA) {
            this.context.validatePaintOp(this, baseTransform, texture, f14, f15, f16, f17);
        } else {
            this.context.validateTextureOp(this, baseTransform, texture, pixelFormat);
        }
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addMappedQuad(f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13);
    }
}

