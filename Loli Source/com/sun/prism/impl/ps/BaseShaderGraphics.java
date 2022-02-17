/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.ps;

import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrike;
import com.sun.javafx.font.Metrics;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.scene.text.GlyphList;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.prism.BasicStroke;
import com.sun.prism.CompositeMode;
import com.sun.prism.MaskTextureGraphics;
import com.sun.prism.MultiTexture;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ReadbackGraphics;
import com.sun.prism.ReadbackRenderTarget;
import com.sun.prism.RenderTarget;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseGraphics;
import com.sun.prism.impl.GlyphCache;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.VertexBuffer;
import com.sun.prism.impl.ps.BaseShaderContext;
import com.sun.prism.impl.ps.PaintHelper;
import com.sun.prism.impl.shape.MaskData;
import com.sun.prism.impl.shape.ShapeUtil;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import java.security.AccessController;

public abstract class BaseShaderGraphics
extends BaseGraphics
implements ShaderGraphics,
ReadbackGraphics,
MaskTextureGraphics {
    private static Affine2D TEMP_TX2D = new Affine2D();
    private static Affine3D TEMP_TX3D = new Affine3D();
    private final BaseShaderContext context;
    private Shader externalShader;
    private boolean isComplexPaint;
    private NGLightBase[] lights = null;
    private static RectBounds TMP_BOUNDS = new RectBounds();
    private static final float FRINGE_FACTOR;
    private static final double SQRT_2;
    private boolean lcdSampleInvalid = false;

    protected BaseShaderGraphics(BaseShaderContext baseShaderContext, RenderTarget renderTarget) {
        super(baseShaderContext, renderTarget);
        this.context = baseShaderContext;
    }

    BaseShaderContext getContext() {
        return this.context;
    }

    boolean isComplexPaint() {
        return this.isComplexPaint;
    }

    @Override
    public void getPaintShaderTransform(Affine3D affine3D) {
        affine3D.setTransform(this.getTransformNoClone());
    }

    public Shader getExternalShader() {
        return this.externalShader;
    }

    @Override
    public void setExternalShader(Shader shader) {
        this.externalShader = shader;
        this.context.setExternalShader(this, shader);
    }

    @Override
    public void setPaint(Paint paint) {
        Gradient gradient;
        this.isComplexPaint = paint.getType().isGradient() ? (gradient = (Gradient)paint).getNumStops() > 12 : false;
        super.setPaint(paint);
    }

    @Override
    public void setLights(NGLightBase[] arrnGLightBase) {
        this.lights = arrnGLightBase;
    }

    @Override
    public final NGLightBase[] getLights() {
        return this.lights;
    }

    @Override
    public void drawTexture(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        if (texture instanceof MultiTexture) {
            this.drawMultiTexture((MultiTexture)texture, f2, f3, f4, f5, f6, f7, f8, f9);
        } else {
            super.drawTexture(texture, f2, f3, f4, f5, f6, f7, f8, f9);
        }
    }

    @Override
    public void drawTexture3SliceH(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        if (!(texture instanceof MultiTexture)) {
            super.drawTexture3SliceH(texture, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13);
            return;
        }
        MultiTexture multiTexture = (MultiTexture)texture;
        this.drawMultiTexture(multiTexture, f2, f3, f10, f5, f6, f7, f12, f9);
        this.drawMultiTexture(multiTexture, f10, f3, f11, f5, f12, f7, f13, f9);
        this.drawMultiTexture(multiTexture, f11, f3, f4, f5, f13, f7, f8, f9);
    }

    @Override
    public void drawTexture3SliceV(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        if (!(texture instanceof MultiTexture)) {
            super.drawTexture3SliceV(texture, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13);
            return;
        }
        MultiTexture multiTexture = (MultiTexture)texture;
        this.drawMultiTexture(multiTexture, f2, f3, f4, f10, f6, f7, f8, f12);
        this.drawMultiTexture(multiTexture, f2, f10, f4, f11, f6, f12, f8, f13);
        this.drawMultiTexture(multiTexture, f2, f11, f4, f5, f6, f13, f8, f9);
    }

    @Override
    public void drawTexture9Slice(Texture texture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17) {
        if (!(texture instanceof MultiTexture)) {
            super.drawTexture9Slice(texture, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17);
            return;
        }
        MultiTexture multiTexture = (MultiTexture)texture;
        this.drawMultiTexture(multiTexture, f2, f3, f10, f11, f6, f7, f14, f15);
        this.drawMultiTexture(multiTexture, f10, f3, f12, f11, f14, f7, f16, f15);
        this.drawMultiTexture(multiTexture, f12, f3, f4, f11, f16, f7, f8, f15);
        this.drawMultiTexture(multiTexture, f2, f11, f10, f13, f6, f15, f14, f17);
        this.drawMultiTexture(multiTexture, f10, f11, f12, f13, f14, f15, f16, f17);
        this.drawMultiTexture(multiTexture, f12, f11, f4, f13, f16, f15, f8, f17);
        this.drawMultiTexture(multiTexture, f2, f13, f10, f5, f6, f17, f14, f9);
        this.drawMultiTexture(multiTexture, f10, f13, f12, f5, f14, f17, f16, f9);
        this.drawMultiTexture(multiTexture, f12, f13, f4, f5, f16, f17, f8, f9);
    }

    private static float calculateScaleFactor(float f2, float f3) {
        if (f2 == f3) {
            return 1.0f;
        }
        return (f2 - 1.0f) / f3;
    }

    protected void drawMultiTexture(MultiTexture multiTexture, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        Texture texture;
        Texture texture2;
        Texture[] arrtexture;
        Shader shader;
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f2 += this.transX;
            f3 += this.transY;
            f4 += this.transX;
            f5 += this.transY;
        }
        if (null == (shader = this.context.validateTextureOp(this, baseTransform, arrtexture = multiTexture.getTextures(), multiTexture.getPixelFormat()))) {
            return;
        }
        if (multiTexture.getPixelFormat() == PixelFormat.MULTI_YCbCr_420) {
            Texture texture3 = arrtexture[0];
            texture2 = arrtexture[2];
            texture = arrtexture[1];
            f15 = multiTexture.getContentWidth();
            f14 = multiTexture.getContentHeight();
            f13 = BaseShaderGraphics.calculateScaleFactor(f15, texture3.getPhysicalWidth());
            f12 = BaseShaderGraphics.calculateScaleFactor(f14, texture3.getPhysicalHeight());
            if (arrtexture.length > 3) {
                Texture texture4 = arrtexture[3];
                f11 = BaseShaderGraphics.calculateScaleFactor(f15, texture4.getPhysicalWidth());
                f10 = BaseShaderGraphics.calculateScaleFactor(f14, texture4.getPhysicalHeight());
            } else {
                f10 = 0.0f;
                f11 = 0.0f;
            }
        } else {
            throw new UnsupportedOperationException("Unsupported multitexture format " + (Object)((Object)multiTexture.getPixelFormat()));
        }
        float f16 = (float)Math.floor((double)f15 / 2.0);
        float f17 = (float)Math.floor((double)f14 / 2.0);
        float f18 = BaseShaderGraphics.calculateScaleFactor(f16, texture2.getPhysicalWidth());
        float f19 = BaseShaderGraphics.calculateScaleFactor(f17, texture2.getPhysicalHeight());
        float f20 = BaseShaderGraphics.calculateScaleFactor(f16, texture.getPhysicalWidth());
        float f21 = BaseShaderGraphics.calculateScaleFactor(f17, texture.getPhysicalHeight());
        shader.setConstant("lumaAlphaScale", f13, f12, f11, f10);
        shader.setConstant("cbCrScale", f18, f19, f20, f21);
        float f22 = f6 / f15;
        float f23 = f7 / f14;
        float f24 = f8 / f15;
        float f25 = f9 / f14;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f2, f3, f4, f5, f22, f23, f24, f25);
    }

    @Override
    public void drawTextureRaw2(Texture texture, Texture texture2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f2 += this.transX;
            f3 += this.transY;
            f4 += this.transX;
            f5 += this.transY;
        }
        this.context.validateTextureOp(this, baseTransform, texture, texture2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13);
    }

    @Override
    public void drawMappedTextureRaw2(Texture texture, Texture texture2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, float f21) {
        BaseTransform baseTransform = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f2 += this.transX;
            f3 += this.transY;
            f4 += this.transX;
            f5 += this.transY;
        }
        this.context.validateTextureOp(this, baseTransform, texture, texture2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addMappedQuad(f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20, f21);
    }

    @Override
    public void drawPixelsMasked(RTTexture rTTexture, RTTexture rTTexture2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        if (n4 <= 0 || n5 <= 0) {
            return;
        }
        float f2 = rTTexture.getPhysicalWidth();
        float f3 = rTTexture.getPhysicalHeight();
        float f4 = rTTexture2.getPhysicalWidth();
        float f5 = rTTexture2.getPhysicalHeight();
        float f6 = n2;
        float f7 = n3;
        float f8 = n2 + n4;
        float f9 = n3 + n5;
        float f10 = (float)n6 / f2;
        float f11 = (float)n7 / f3;
        float f12 = (float)(n6 + n4) / f2;
        float f13 = (float)(n7 + n5) / f3;
        float f14 = (float)n8 / f4;
        float f15 = (float)n9 / f5;
        float f16 = (float)(n8 + n4) / f4;
        float f17 = (float)(n9 + n5) / f5;
        this.context.validateMaskTextureOp(this, IDENT, rTTexture, rTTexture2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17);
    }

    @Override
    public void maskInterpolatePixels(RTTexture rTTexture, RTTexture rTTexture2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        if (n4 <= 0 || n5 <= 0) {
            return;
        }
        float f2 = rTTexture.getPhysicalWidth();
        float f3 = rTTexture.getPhysicalHeight();
        float f4 = rTTexture2.getPhysicalWidth();
        float f5 = rTTexture2.getPhysicalHeight();
        float f6 = n2;
        float f7 = n3;
        float f8 = n2 + n4;
        float f9 = n3 + n5;
        float f10 = (float)n6 / f2;
        float f11 = (float)n7 / f3;
        float f12 = (float)(n6 + n4) / f2;
        float f13 = (float)(n7 + n5) / f3;
        float f14 = (float)n8 / f4;
        float f15 = (float)n9 / f5;
        float f16 = (float)(n8 + n4) / f4;
        float f17 = (float)(n9 + n5) / f5;
        CompositeMode compositeMode = this.getCompositeMode();
        this.setCompositeMode(CompositeMode.DST_OUT);
        this.context.validateTextureOp((BaseGraphics)this, IDENT, rTTexture2, PixelFormat.INT_ARGB_PRE);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f6, f7, f8, f9, f14, f15, f16, f17);
        this.setCompositeMode(CompositeMode.ADD);
        this.context.validateMaskTextureOp(this, IDENT, rTTexture, rTTexture2, PixelFormat.INT_ARGB_PRE);
        vertexBuffer.addQuad(f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17);
        this.setCompositeMode(compositeMode);
    }

    private void renderWithComplexPaint(Shape shape, BasicStroke basicStroke, float f2, float f3, float f4, float f5) {
        this.context.flushVertexBuffer();
        BaseTransform baseTransform = this.getTransformNoClone();
        MaskData maskData = ShapeUtil.rasterizeShape(shape, basicStroke, this.getFinalClipNoClone(), baseTransform, true, this.isAntialiasedShape());
        int n2 = maskData.getWidth();
        int n3 = maskData.getHeight();
        float f6 = maskData.getOriginX();
        float f7 = maskData.getOriginY();
        float f8 = f6 + (float)n2;
        float f9 = f7 + (float)n3;
        Gradient gradient = (Gradient)this.paint;
        TEMP_TX2D.setToTranslation(-f6, -f7);
        TEMP_TX2D.concatenate(baseTransform);
        Texture texture = this.context.getGradientTexture(gradient, TEMP_TX2D, n2, n3, maskData, f2, f3, f4, f5);
        float f10 = 0.0f;
        float f11 = 0.0f;
        float f12 = f10 + (float)n2 / (float)texture.getPhysicalWidth();
        float f13 = f11 + (float)n3 / (float)texture.getPhysicalHeight();
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        this.context.validateTextureOp(this, IDENT, texture, null, texture.getPixelFormat());
        vertexBuffer.addQuad(f6, f7, f8, f9, f10, f11, f12, f13);
        texture.unlock();
    }

    @Override
    protected void renderShape(Shape shape, BasicStroke basicStroke, float f2, float f3, float f4, float f5) {
        AffineBase affineBase;
        if (this.isComplexPaint) {
            this.renderWithComplexPaint(shape, basicStroke, f2, f3, f4, f5);
            return;
        }
        BaseTransform baseTransform = this.getTransformNoClone();
        MaskData maskData = ShapeUtil.rasterizeShape(shape, basicStroke, this.getFinalClipNoClone(), baseTransform, true, this.isAntialiasedShape());
        Texture texture = this.context.validateMaskTexture(maskData, false);
        if (PrismSettings.primTextureSize != 0) {
            Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f2, f3, f4, f5);
            affineBase = this.getPaintTextureTx(baseTransform, shader, f2, f3, f4, f5);
        } else {
            this.context.validatePaintOp(this, IDENT, texture, f2, f3, f4, f5);
            affineBase = null;
        }
        this.context.updateMaskTexture(maskData, TMP_BOUNDS, false);
        float f6 = maskData.getOriginX();
        float f7 = maskData.getOriginY();
        float f8 = f6 + (float)maskData.getWidth();
        float f9 = f7 + (float)maskData.getHeight();
        float f10 = TMP_BOUNDS.getMinX();
        float f11 = TMP_BOUNDS.getMinY();
        float f12 = TMP_BOUNDS.getMaxX();
        float f13 = TMP_BOUNDS.getMaxY();
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f6, f7, f8, f9, f10, f11, f12, f13, affineBase);
        texture.unlock();
    }

    private static float getStrokeExpansionFactor(BasicStroke basicStroke) {
        if (basicStroke.getType() == 2) {
            return 1.0f;
        }
        if (basicStroke.getType() == 0) {
            return 0.5f;
        }
        return 0.0f;
    }

    private BaseTransform extract3Dremainder(BaseTransform baseTransform) {
        if (baseTransform.is2D()) {
            return IDENT;
        }
        TEMP_TX3D.setTransform(baseTransform);
        TEMP_TX2D.setTransform(baseTransform.getMxx(), baseTransform.getMyx(), baseTransform.getMxy(), baseTransform.getMyy(), baseTransform.getMxt(), baseTransform.getMyt());
        try {
            TEMP_TX2D.invert();
            TEMP_TX3D.concatenate(TEMP_TX2D);
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            // empty catch block
        }
        return TEMP_TX3D;
    }

    private void renderGeneralRoundedRect(float f2, float f3, float f4, float f5, float f6, float f7, BaseShaderContext.MaskType maskType, BasicStroke basicStroke) {
        BaseTransform baseTransform;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18;
        float f19;
        if (basicStroke == null) {
            f19 = f2;
            f18 = f3;
            f17 = f4;
            f16 = f5;
            f15 = 0.0f;
            f14 = 0.0f;
        } else {
            float f20 = basicStroke.getLineWidth();
            float f21 = BaseShaderGraphics.getStrokeExpansionFactor(basicStroke) * f20;
            f19 = f2 - f21;
            f18 = f3 - f21;
            f17 = f4 + (f21 *= 2.0f);
            f16 = f5 + f21;
            if (f6 > 0.0f && f7 > 0.0f) {
                f6 += f21;
                f7 += f21;
            } else if (basicStroke.getLineJoin() == 1) {
                f6 = f7 = f21;
                maskType = BaseShaderContext.MaskType.DRAW_ROUNDRECT;
            } else {
                f7 = 0.0f;
                f6 = 0.0f;
            }
            f14 = (f17 - f20 * 2.0f) / f17;
            f15 = (f16 - f20 * 2.0f) / f16;
            if (f14 <= 0.0f || f15 <= 0.0f) {
                maskType = maskType.getFillType();
            }
        }
        BaseTransform baseTransform2 = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            f13 = 1.0f;
            f12 = 1.0f;
            f11 = 0.0f;
            f10 = 0.0f;
            f9 = f19 + this.transX;
            f8 = f18 + this.transY;
            baseTransform = IDENT;
        } else {
            baseTransform = this.extract3Dremainder(baseTransform2);
            f12 = (float)baseTransform2.getMxx();
            f11 = (float)baseTransform2.getMxy();
            f10 = (float)baseTransform2.getMyx();
            f13 = (float)baseTransform2.getMyy();
            f9 = f19 * f12 + f18 * f11 + (float)baseTransform2.getMxt();
            f8 = f19 * f10 + f18 * f13 + (float)baseTransform2.getMyt();
        }
        float f22 = f6 / f17;
        float f23 = f7 / f16;
        this.renderGeneralRoundedPgram(f9, f8, f12 *= f17, f10 *= f17, f11 *= f16, f13 *= f16, f22, f23, f14, f15, baseTransform, maskType, f2, f3, f4, f5);
    }

    private void renderGeneralRoundedPgram(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, BaseTransform baseTransform, BaseShaderContext.MaskType maskType, float f12, float f13, float f14, float f15) {
        float f16;
        float f17;
        float f18 = BaseShaderGraphics.len(f4, f5);
        float f19 = BaseShaderGraphics.len(f6, f7);
        if (f18 == 0.0f || f19 == 0.0f) {
            return;
        }
        float f20 = f2;
        float f21 = f3;
        float f22 = f2 + f4;
        float f23 = f3 + f5;
        float f24 = f2 + f6;
        float f25 = f3 + f7;
        float f26 = f22 + f6;
        float f27 = f23 + f7;
        float f28 = (f4 * f7 - f5 * f6) * 0.5f;
        float f29 = f28 / f19;
        float f30 = f28 / f18;
        if (f29 < 0.0f) {
            f29 = -f29;
        }
        if (f30 < 0.0f) {
            f30 = -f30;
        }
        float f31 = f4 / f18;
        float f32 = f5 / f18;
        float f33 = f6 / f19;
        float f34 = f7 / f19;
        float f35 = -f6 * (f31 + f33) - f7 * (f32 + f34);
        float f36 = f7 * f4 - f6 * f5;
        float f37 = f35 / f36;
        float f38 = FRINGE_FACTOR * Math.signum(f37);
        float f39 = (f37 * f4 + f32) * f38;
        float f40 = (f37 * f5 - f31) * f38;
        f20 += f39;
        f21 += f40;
        f26 -= f39;
        f27 -= f40;
        f35 = f5 * (f34 - f32) - f4 * (f31 - f33);
        f37 = f35 / f36;
        f38 = FRINGE_FACTOR * Math.signum(f37);
        f39 = (f37 * f6 + f34) * f38;
        f40 = (f37 * f7 - f33) * f38;
        float f41 = (f20 + f26) * 0.5f;
        float f42 = (f21 + f27) * 0.5f;
        float f43 = f41 * f34 - f42 * f33;
        float f44 = f41 * f32 - f42 * f31;
        float f45 = f20 * f34 - f21 * f33 - f43;
        float f46 = f20 * f32 - f21 * f31 - f44;
        float f47 = (f22 += f39) * f34 - (f23 += f40) * f33 - f43;
        float f48 = f22 * f32 - f23 * f31 - f44;
        float f49 = (f24 -= f39) * f34 - (f25 -= f40) * f33 - f43;
        float f50 = f24 * f32 - f25 * f31 - f44;
        float f51 = f26 * f34 - f27 * f33 - f43;
        float f52 = f26 * f32 - f27 * f31 - f44;
        if (maskType == BaseShaderContext.MaskType.DRAW_ROUNDRECT || maskType == BaseShaderContext.MaskType.FILL_ROUNDRECT) {
            f17 = f29 * f8;
            f16 = f30 * f9;
            if ((double)f17 < 0.5 || (double)f16 < 0.5) {
                maskType = maskType == BaseShaderContext.MaskType.DRAW_ROUNDRECT ? BaseShaderContext.MaskType.DRAW_PGRAM : BaseShaderContext.MaskType.FILL_PGRAM;
            } else {
                float f53;
                float f54;
                float f55 = f29 - f17;
                float f56 = f30 - f16;
                if (maskType == BaseShaderContext.MaskType.DRAW_ROUNDRECT) {
                    float f57 = f29 * f10;
                    float f58 = f30 * f11;
                    f54 = f57 - f55;
                    f53 = f58 - f56;
                    if (f54 < 0.5f || f53 < 0.5f) {
                        f54 = f57;
                        f53 = f58;
                        maskType = BaseShaderContext.MaskType.DRAW_SEMIROUNDRECT;
                    } else {
                        f54 = 1.0f / f54;
                        f53 = 1.0f / f53;
                    }
                } else {
                    f53 = 0.0f;
                    f54 = 0.0f;
                }
                f17 = 1.0f / f17;
                f16 = 1.0f / f16;
                Shader shader = this.context.validatePaintOp(this, baseTransform, maskType, f12, f13, f14, f15, f17, f16, f54, f53, 0.0f, 0.0f);
                shader.setConstant("oinvarcradii", f17, f16);
                if (maskType == BaseShaderContext.MaskType.DRAW_ROUNDRECT) {
                    shader.setConstant("iinvarcradii", f54, f53);
                } else if (maskType == BaseShaderContext.MaskType.DRAW_SEMIROUNDRECT) {
                    shader.setConstant("idim", f54, f53);
                }
                f29 = f55;
                f30 = f56;
            }
        }
        if (maskType == BaseShaderContext.MaskType.DRAW_PGRAM || maskType == BaseShaderContext.MaskType.DRAW_ELLIPSE) {
            f17 = f29 * f10;
            f16 = f30 * f11;
            if (maskType == BaseShaderContext.MaskType.DRAW_ELLIPSE) {
                if ((double)Math.abs(f29 - f30) < 0.01) {
                    maskType = BaseShaderContext.MaskType.DRAW_CIRCLE;
                    f30 = (float)Math.min(1.0, (double)(f30 * f30) * Math.PI);
                    f16 = (float)Math.min(1.0, (double)(f16 * f16) * Math.PI);
                } else {
                    f29 = 1.0f / f29;
                    f30 = 1.0f / f30;
                    f17 = 1.0f / f17;
                    f16 = 1.0f / f16;
                }
            }
            Shader shader = this.context.validatePaintOp(this, baseTransform, maskType, f12, f13, f14, f15, f17, f16, 0.0f, 0.0f, 0.0f, 0.0f);
            shader.setConstant("idim", f17, f16);
        } else if (maskType == BaseShaderContext.MaskType.FILL_ELLIPSE) {
            if ((double)Math.abs(f29 - f30) < 0.01) {
                maskType = BaseShaderContext.MaskType.FILL_CIRCLE;
                f30 = (float)Math.min(1.0, (double)(f30 * f30) * Math.PI);
            } else {
                f29 = 1.0f / f29;
                f30 = 1.0f / f30;
                f45 *= f29;
                f46 *= f30;
                f47 *= f29;
                f48 *= f30;
                f49 *= f29;
                f50 *= f30;
                f51 *= f29;
                f52 *= f30;
            }
            this.context.validatePaintOp(this, baseTransform, maskType, f12, f13, f14, f15);
        } else if (maskType == BaseShaderContext.MaskType.FILL_PGRAM) {
            this.context.validatePaintOp(this, baseTransform, maskType, f12, f13, f14, f15);
        }
        this.context.getVertexBuffer().addMappedPgram(f20, f21, f22, f23, f24, f25, f26, f27, f45, f46, f47, f48, f49, f50, f51, f52, f29, f30);
    }

    AffineBase getPaintTextureTx(BaseTransform baseTransform, Shader shader, float f2, float f3, float f4, float f5) {
        switch (this.paint.getType()) {
            case COLOR: {
                return null;
            }
            case LINEAR_GRADIENT: {
                return PaintHelper.getLinearGradientTx((LinearGradient)this.paint, shader, baseTransform, f2, f3, f4, f5);
            }
            case RADIAL_GRADIENT: {
                return PaintHelper.getRadialGradientTx((RadialGradient)this.paint, shader, baseTransform, f2, f3, f4, f5);
            }
            case IMAGE_PATTERN: {
                return PaintHelper.getImagePatternTx(this, (ImagePattern)this.paint, shader, baseTransform, f2, f3, f4, f5);
            }
        }
        throw new InternalError("Unrecogized paint type: " + this.paint);
    }

    boolean fillPrimRect(float f2, float f3, float f4, float f5, Texture texture, Texture texture2, float f6, float f7, float f8, float f9) {
        BaseTransform baseTransform = this.getTransformNoClone();
        float f10 = (float)baseTransform.getMxx();
        float f11 = (float)baseTransform.getMxy();
        float f12 = (float)baseTransform.getMxt();
        float f13 = (float)baseTransform.getMyx();
        float f14 = (float)baseTransform.getMyy();
        float f15 = (float)baseTransform.getMyt();
        float f16 = BaseShaderGraphics.len(f10, f13);
        float f17 = BaseShaderGraphics.len(f11, f14);
        if (f16 == 0.0f || f17 == 0.0f) {
            return true;
        }
        float f18 = 1.0f / f16;
        float f19 = 1.0f / f17;
        float f20 = f2 - f18 * 0.5f;
        float f21 = f3 - f19 * 0.5f;
        float f22 = f2 + f4 + f18 * 0.5f;
        float f23 = f3 + f5 + f19 * 0.5f;
        int n2 = (int)Math.ceil(f4 * f16 - 0.001953125f);
        int n3 = (int)Math.ceil(f5 * f17 - 0.001953125f);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        int n4 = this.context.getRectTextureMaxSize();
        if (n2 <= n4 && n3 <= n4) {
            float f24 = (float)(n2 * (n2 + 1) / 2) - 0.5f;
            float f25 = (float)(n3 * (n3 + 1) / 2) - 0.5f;
            float f26 = f24 + (float)n2 + 1.0f;
            float f27 = f25 + (float)n3 + 1.0f;
            f24 /= (float)texture.getPhysicalWidth();
            f25 /= (float)texture.getPhysicalHeight();
            f26 /= (float)texture.getPhysicalWidth();
            f27 /= (float)texture.getPhysicalHeight();
            if (baseTransform.isTranslateOrIdentity()) {
                f20 += f12;
                f21 += f15;
                f22 += f12;
                f23 += f15;
                baseTransform = IDENT;
            } else {
                if (baseTransform.is2D()) {
                    Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f6, f7, f8, f9);
                    AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f6, f7, f8, f9);
                    if (affineBase == null) {
                        vertexBuffer.addMappedPgram(f20 * f10 + f21 * f11 + f12, f20 * f13 + f21 * f14 + f15, f22 * f10 + f21 * f11 + f12, f22 * f13 + f21 * f14 + f15, f20 * f10 + f23 * f11 + f12, f20 * f13 + f23 * f14 + f15, f22 * f10 + f23 * f11 + f12, f22 * f13 + f23 * f14 + f15, f24, f25, f26, f25, f24, f27, f26, f27, 0.0f, 0.0f);
                    } else {
                        vertexBuffer.addMappedPgram(f20 * f10 + f21 * f11 + f12, f20 * f13 + f21 * f14 + f15, f22 * f10 + f21 * f11 + f12, f22 * f13 + f21 * f14 + f15, f20 * f10 + f23 * f11 + f12, f20 * f13 + f23 * f14 + f15, f22 * f10 + f23 * f11 + f12, f22 * f13 + f23 * f14 + f15, f24, f25, f26, f25, f24, f27, f26, f27, f20, f21, f22, f23, affineBase);
                    }
                    return true;
                }
                System.out.println("Not a 2d transform!");
                f15 = 0.0f;
                f12 = 0.0f;
            }
            Shader shader = this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f6, f7, f8, f9);
            AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f6, f7, f8, f9);
            if (affineBase == null) {
                vertexBuffer.addQuad(f20, f21, f22, f23, f24, f25, f26, f27);
            } else {
                affineBase.translate(-f12, -f15);
                vertexBuffer.addQuad(f20, f21, f22, f23, f24, f25, f26, f27, affineBase);
            }
            return true;
        }
        if (texture2 == null) {
            return false;
        }
        float f28 = 0.5f / (float)texture2.getPhysicalWidth();
        float f29 = 0.5f / (float)texture2.getPhysicalHeight();
        float f30 = ((float)n2 * 0.5f + 1.0f) / (float)texture2.getPhysicalWidth();
        float f31 = ((float)n3 * 0.5f + 1.0f) / (float)texture2.getPhysicalHeight();
        float f32 = f2 + f4 * 0.5f;
        float f33 = f3 + f5 * 0.5f;
        if (baseTransform.isTranslateOrIdentity()) {
            f20 += f12;
            f21 += f15;
            f32 += f12;
            f33 += f15;
            f22 += f12;
            f23 += f15;
            baseTransform = IDENT;
        } else {
            if (baseTransform.is2D()) {
                Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture2, f6, f7, f8, f9);
                AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f6, f7, f8, f9);
                float f34 = f10 * f20;
                float f35 = f13 * f20;
                float f36 = f11 * f21;
                float f37 = f14 * f21;
                float f38 = f10 * f32;
                float f39 = f13 * f32;
                float f40 = f11 * f33;
                float f41 = f14 * f33;
                float f42 = f10 * f22;
                float f43 = f13 * f22;
                float f44 = f11 * f23;
                float f45 = f14 * f23;
                float f46 = f38 + f40 + f12;
                float f47 = f39 + f41 + f15;
                float f48 = f38 + f36 + f12;
                float f49 = f39 + f37 + f15;
                float f50 = f34 + f40 + f12;
                float f51 = f35 + f41 + f15;
                float f52 = f38 + f44 + f12;
                float f53 = f39 + f45 + f15;
                float f54 = f42 + f40 + f12;
                float f55 = f43 + f41 + f15;
                if (affineBase == null) {
                    vertexBuffer.addMappedPgram(f20 * f10 + f21 * f11 + f12, f20 * f13 + f21 * f14 + f15, f48, f49, f50, f51, f46, f47, f28, f29, f30, f29, f28, f31, f30, f31, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f22 * f10 + f21 * f11 + f12, f22 * f13 + f21 * f14 + f15, f48, f49, f54, f55, f46, f47, f28, f29, f30, f29, f28, f31, f30, f31, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f20 * f10 + f23 * f11 + f12, f20 * f13 + f23 * f14 + f15, f52, f53, f50, f51, f46, f47, f28, f29, f30, f29, f28, f31, f30, f31, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f22 * f10 + f23 * f11 + f12, f22 * f13 + f23 * f14 + f15, f52, f53, f54, f55, f46, f47, f28, f29, f30, f29, f28, f31, f30, f31, 0.0f, 0.0f);
                } else {
                    vertexBuffer.addMappedPgram(f20 * f10 + f21 * f11 + f12, f20 * f13 + f21 * f14 + f15, f48, f49, f50, f51, f46, f47, f28, f29, f30, f29, f28, f31, f30, f31, f20, f21, f32, f33, affineBase);
                    vertexBuffer.addMappedPgram(f22 * f10 + f21 * f11 + f12, f22 * f13 + f21 * f14 + f15, f48, f49, f54, f55, f46, f47, f28, f29, f30, f29, f28, f31, f30, f31, f22, f21, f32, f33, affineBase);
                    vertexBuffer.addMappedPgram(f20 * f10 + f23 * f11 + f12, f20 * f13 + f23 * f14 + f15, f52, f53, f50, f51, f46, f47, f28, f29, f30, f29, f28, f31, f30, f31, f20, f23, f32, f33, affineBase);
                    vertexBuffer.addMappedPgram(f22 * f10 + f23 * f11 + f12, f22 * f13 + f23 * f14 + f15, f52, f53, f54, f55, f46, f47, f28, f29, f30, f29, f28, f31, f30, f31, f22, f23, f32, f33, affineBase);
                }
                return true;
            }
            System.out.println("Not a 2d transform!");
            f15 = 0.0f;
            f12 = 0.0f;
        }
        Shader shader = this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture2, f6, f7, f8, f9);
        AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f6, f7, f8, f9);
        if (affineBase != null) {
            affineBase.translate(-f12, -f15);
        }
        vertexBuffer.addQuad(f20, f21, f32, f33, f28, f29, f30, f31, affineBase);
        vertexBuffer.addQuad(f22, f21, f32, f33, f28, f29, f30, f31, affineBase);
        vertexBuffer.addQuad(f20, f23, f32, f33, f28, f29, f30, f31, affineBase);
        vertexBuffer.addQuad(f22, f23, f32, f33, f28, f29, f30, f31, affineBase);
        return true;
    }

    boolean drawPrimRect(float f2, float f3, float f4, float f5) {
        float f6 = this.stroke.getLineWidth();
        float f7 = BaseShaderGraphics.getStrokeExpansionFactor(this.stroke) * f6;
        BaseTransform baseTransform = this.getTransformNoClone();
        float f8 = (float)baseTransform.getMxx();
        float f9 = (float)baseTransform.getMxy();
        float f10 = (float)baseTransform.getMxt();
        float f11 = (float)baseTransform.getMyx();
        float f12 = (float)baseTransform.getMyy();
        float f13 = (float)baseTransform.getMyt();
        float f14 = BaseShaderGraphics.len(f8, f11);
        float f15 = BaseShaderGraphics.len(f9, f12);
        if (f14 == 0.0f || f15 == 0.0f) {
            return true;
        }
        float f16 = 1.0f / f14;
        float f17 = 1.0f / f15;
        float f18 = f2 - f7 - f16 * 0.5f;
        float f19 = f3 - f7 - f17 * 0.5f;
        float f20 = f2 + f4 * 0.5f;
        float f21 = f3 + f5 * 0.5f;
        float f22 = f2 + f4 + f7 + f16 * 0.5f;
        float f23 = f3 + f5 + f7 + f17 * 0.5f;
        Texture texture = this.context.getWrapRectTexture();
        float f24 = 1.0f / (float)texture.getPhysicalWidth();
        float f25 = 1.0f / (float)texture.getPhysicalHeight();
        float f26 = 0.5f * f24;
        float f27 = 0.5f * f25;
        float f28 = ((f4 * 0.5f + f7) * f14 + 1.0f) * f24;
        float f29 = ((f5 * 0.5f + f7) * f15 + 1.0f) * f25;
        float f30 = f6 * f14 * f24;
        float f31 = f6 * f15 * f25;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        if (baseTransform.isTranslateOrIdentity()) {
            f18 += f10;
            f19 += f13;
            f20 += f10;
            f21 += f13;
            f22 += f10;
            f23 += f13;
            baseTransform = IDENT;
        } else {
            if (baseTransform.is2D()) {
                Shader shader = this.context.validatePaintOp(this, IDENT, BaseShaderContext.MaskType.ALPHA_TEXTURE_DIFF, texture, f2, f3, f4, f5, f30, f31, 0.0f, 0.0f, 0.0f, 0.0f);
                shader.setConstant("innerOffset", f30, f31);
                AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f2, f3, f4, f5);
                float f32 = f8 * f18;
                float f33 = f11 * f18;
                float f34 = f9 * f19;
                float f35 = f12 * f19;
                float f36 = f8 * f20;
                float f37 = f11 * f20;
                float f38 = f9 * f21;
                float f39 = f12 * f21;
                float f40 = f8 * f22;
                float f41 = f11 * f22;
                float f42 = f9 * f23;
                float f43 = f12 * f23;
                float f44 = f36 + f38 + f10;
                float f45 = f37 + f39 + f13;
                float f46 = f36 + f34 + f10;
                float f47 = f37 + f35 + f13;
                float f48 = f32 + f38 + f10;
                float f49 = f33 + f39 + f13;
                float f50 = f36 + f42 + f10;
                float f51 = f37 + f43 + f13;
                float f52 = f40 + f38 + f10;
                float f53 = f41 + f39 + f13;
                if (affineBase == null) {
                    vertexBuffer.addMappedPgram(f32 + f34 + f10, f33 + f35 + f13, f46, f47, f48, f49, f44, f45, f26, f27, f28, f27, f26, f29, f28, f29, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f40 + f34 + f10, f41 + f35 + f13, f46, f47, f52, f53, f44, f45, f26, f27, f28, f27, f26, f29, f28, f29, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f32 + f42 + f10, f33 + f43 + f13, f50, f51, f48, f49, f44, f45, f26, f27, f28, f27, f26, f29, f28, f29, 0.0f, 0.0f);
                    vertexBuffer.addMappedPgram(f40 + f42 + f10, f41 + f43 + f13, f50, f51, f52, f53, f44, f45, f26, f27, f28, f27, f26, f29, f28, f29, 0.0f, 0.0f);
                } else {
                    vertexBuffer.addMappedPgram(f32 + f34 + f10, f33 + f35 + f13, f46, f47, f48, f49, f44, f45, f26, f27, f28, f27, f26, f29, f28, f29, f18, f19, f20, f21, affineBase);
                    vertexBuffer.addMappedPgram(f40 + f34 + f10, f41 + f35 + f13, f46, f47, f52, f53, f44, f45, f26, f27, f28, f27, f26, f29, f28, f29, f22, f19, f20, f21, affineBase);
                    vertexBuffer.addMappedPgram(f32 + f42 + f10, f33 + f43 + f13, f50, f51, f48, f49, f44, f45, f26, f27, f28, f27, f26, f29, f28, f29, f18, f23, f20, f21, affineBase);
                    vertexBuffer.addMappedPgram(f40 + f42 + f10, f41 + f43 + f13, f50, f51, f52, f53, f44, f45, f26, f27, f28, f27, f26, f29, f28, f29, f22, f23, f20, f21, affineBase);
                }
                texture.unlock();
                return true;
            }
            System.out.println("Not a 2d transform!");
            f13 = 0.0f;
            f10 = 0.0f;
        }
        Shader shader = this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE_DIFF, texture, f2, f3, f4, f5, f30, f31, 0.0f, 0.0f, 0.0f, 0.0f);
        shader.setConstant("innerOffset", f30, f31);
        AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f2, f3, f4, f5);
        if (affineBase != null) {
            affineBase.translate(-f10, -f13);
        }
        vertexBuffer.addQuad(f18, f19, f20, f21, f26, f27, f28, f29, affineBase);
        vertexBuffer.addQuad(f22, f19, f20, f21, f26, f27, f28, f29, affineBase);
        vertexBuffer.addQuad(f18, f23, f20, f21, f26, f27, f28, f29, affineBase);
        vertexBuffer.addQuad(f22, f23, f20, f21, f26, f27, f28, f29, affineBase);
        texture.unlock();
        return true;
    }

    boolean drawPrimDiagonal(float f2, float f3, float f4, float f5, float f6, int n2, float f7, float f8, float f9, float f10) {
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        int n3;
        int n4;
        float f16;
        float f17;
        float f18;
        float f19;
        if (this.stroke.getType() == 0) {
            f6 *= 0.5f;
        }
        float f20 = f4 - f2;
        float f21 = f5 - f3;
        float f22 = BaseShaderGraphics.len(f20, f21);
        float f23 = (f20 /= f22) * f6;
        float f24 = (f21 /= f22) * f6;
        float f25 = f2 + f24;
        float f26 = f3 - f23;
        float f27 = f4 + f24;
        float f28 = f5 - f23;
        float f29 = f2 - f24;
        float f30 = f3 + f23;
        float f31 = f4 - f24;
        float f32 = f5 + f23;
        if (n2 == 2) {
            f25 -= f23;
            f26 -= f24;
            f29 -= f23;
            f30 -= f24;
            f27 += f23;
            f28 += f24;
            f31 += f23;
            f32 += f24;
        }
        BaseTransform baseTransform = this.getTransformNoClone();
        float f33 = (float)baseTransform.getMxt();
        float f34 = (float)baseTransform.getMyt();
        if (baseTransform.isTranslateOrIdentity()) {
            f19 = f20;
            f18 = f21;
            f17 = f21;
            f16 = -f20;
            n4 = (int)Math.ceil(BaseShaderGraphics.len(f27 - f25, f28 - f26));
            n3 = (int)Math.ceil(BaseShaderGraphics.len(f29 - f25, f30 - f26));
            baseTransform = IDENT;
        } else if (baseTransform.is2D()) {
            float f35 = (float)baseTransform.getMxx();
            float f36 = (float)baseTransform.getMxy();
            f15 = (float)baseTransform.getMyx();
            f14 = (float)baseTransform.getMyy();
            f13 = f35 * f25 + f36 * f26;
            f12 = f15 * f25 + f14 * f26;
            f25 = f13;
            f26 = f12;
            f13 = f35 * f27 + f36 * f28;
            f12 = f15 * f27 + f14 * f28;
            f27 = f13;
            f28 = f12;
            f13 = f35 * f29 + f36 * f30;
            f12 = f15 * f29 + f14 * f30;
            f29 = f13;
            f30 = f12;
            f13 = f35 * f31 + f36 * f32;
            f12 = f15 * f31 + f14 * f32;
            f31 = f13;
            f32 = f12;
            f19 = f35 * f20 + f36 * f21;
            f18 = f15 * f20 + f14 * f21;
            f11 = BaseShaderGraphics.len(f19, f18);
            if (f11 == 0.0f) {
                return true;
            }
            f19 /= f11;
            f18 /= f11;
            f17 = f35 * f21 - f36 * f20;
            f16 = f15 * f21 - f14 * f20;
            f11 = BaseShaderGraphics.len(f17, f16);
            if (f11 == 0.0f) {
                return true;
            }
            n4 = (int)Math.ceil(Math.abs((f27 - f25) * f19 + (f28 - f26) * f18));
            n3 = (int)Math.ceil(Math.abs((f29 - f25) * (f17 /= f11) + (f30 - f26) * (f16 /= f11)));
            baseTransform = IDENT;
        } else {
            System.out.println("Not a 2d transform!");
            return false;
        }
        f25 = f25 + f33 + (f17 *= 0.5f) - (f19 *= 0.5f);
        f26 = f26 + f34 + (f16 *= 0.5f) - (f18 *= 0.5f);
        f27 = f27 + f33 + f17 + f19;
        f28 = f28 + f34 + f16 + f18;
        f29 = f29 + f33 - f17 - f19;
        f30 = f30 + f34 - f16 - f18;
        f31 = f31 + f33 - f17 + f19;
        f32 = f32 + f34 - f16 + f18;
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        int n5 = this.context.getRectTextureMaxSize();
        if (n3 <= n5) {
            f15 = (float)(n3 * (n3 + 1) / 2) - 0.5f;
            f14 = f15 + (float)n3 + 1.0f;
            Texture texture = this.context.getRectTexture();
            f15 /= (float)texture.getPhysicalHeight();
            f14 /= (float)texture.getPhysicalHeight();
            if (n4 <= n5) {
                f12 = (float)(n4 * (n4 + 1) / 2) - 0.5f;
                f11 = f12 + (float)n4 + 1.0f;
                this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f7, f8, f9, f10);
                vertexBuffer.addMappedPgram(f25, f26, f27, f28, f29, f30, f31, f32, f12 /= (float)texture.getPhysicalWidth(), f15, f11 /= (float)texture.getPhysicalWidth(), f15, f12, f14, f11, f14, 0.0f, 0.0f);
                texture.unlock();
                return true;
            }
            if (n4 <= n5 * 2 - 1) {
                f12 = (f25 + f27) * 0.5f;
                f11 = (f26 + f28) * 0.5f;
                float f37 = (f29 + f31) * 0.5f;
                float f38 = (f30 + f32) * 0.5f;
                float f39 = (float)(n5 * (n5 + 1) / 2) - 0.5f;
                float f40 = f39 + 0.5f + (float)n4 * 0.5f;
                this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f7, f8, f9, f10);
                vertexBuffer.addMappedPgram(f25, f26, f12, f11, f29, f30, f37, f38, f39 /= (float)texture.getPhysicalWidth(), f15, f40 /= (float)texture.getPhysicalWidth(), f15, f39, f14, f40, f14, 0.0f, 0.0f);
                vertexBuffer.addMappedPgram(f27, f28, f12, f11, f31, f32, f37, f38, f39, f15, f40, f15, f39, f14, f40, f14, 0.0f, 0.0f);
                texture.unlock();
                return true;
            }
            f12 = 0.5f / (float)texture.getPhysicalWidth();
            f11 = 1.5f / (float)texture.getPhysicalWidth();
            float f41 = f25 + (f19 *= 2.0f);
            float f42 = f26 + (f18 *= 2.0f);
            float f43 = f27 - f19;
            float f44 = f28 - f18;
            float f45 = f29 + f19;
            float f46 = f30 + f18;
            float f47 = f31 - f19;
            float f48 = f32 - f18;
            this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f7, f8, f9, f10);
            vertexBuffer.addMappedPgram(f25, f26, f41, f42, f29, f30, f45, f46, f12, f15, f11, f15, f12, f14, f11, f14, 0.0f, 0.0f);
            vertexBuffer.addMappedPgram(f41, f42, f43, f44, f45, f46, f47, f48, f11, f15, f11, f15, f11, f14, f11, f14, 0.0f, 0.0f);
            vertexBuffer.addMappedPgram(f43, f44, f27, f28, f47, f48, f31, f32, f11, f15, f12, f15, f11, f14, f12, f14, 0.0f, 0.0f);
            texture.unlock();
            return true;
        }
        f15 = (f25 + f27) * 0.5f;
        f14 = (f26 + f28) * 0.5f;
        f13 = (f29 + f31) * 0.5f;
        f12 = (f30 + f32) * 0.5f;
        f11 = (f25 + f29) * 0.5f;
        float f49 = (f26 + f30) * 0.5f;
        float f50 = (f27 + f31) * 0.5f;
        float f51 = (f28 + f32) * 0.5f;
        float f52 = (f15 + f13) * 0.5f;
        float f53 = (f14 + f12) * 0.5f;
        Texture texture = this.context.getWrapRectTexture();
        float f54 = 0.5f / (float)texture.getPhysicalWidth();
        float f55 = 0.5f / (float)texture.getPhysicalHeight();
        float f56 = ((float)n4 * 0.5f + 1.0f) / (float)texture.getPhysicalWidth();
        float f57 = ((float)n3 * 0.5f + 1.0f) / (float)texture.getPhysicalHeight();
        this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_TEXTURE, texture, f7, f8, f9, f10);
        vertexBuffer.addMappedPgram(f25, f26, f15, f14, f11, f49, f52, f53, f54, f55, f56, f55, f54, f57, f56, f57, 0.0f, 0.0f);
        vertexBuffer.addMappedPgram(f27, f28, f15, f14, f50, f51, f52, f53, f54, f55, f56, f55, f54, f57, f56, f57, 0.0f, 0.0f);
        vertexBuffer.addMappedPgram(f29, f30, f13, f12, f11, f49, f52, f53, f54, f55, f56, f55, f54, f57, f56, f57, 0.0f, 0.0f);
        vertexBuffer.addMappedPgram(f31, f32, f13, f12, f50, f51, f52, f53, f54, f55, f56, f55, f54, f57, f56, f57, 0.0f, 0.0f);
        texture.unlock();
        return true;
    }

    @Override
    public void fillRect(float f2, float f3, float f4, float f5) {
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return;
        }
        if (!this.isAntialiasedShape()) {
            this.fillQuad(f2, f3, f2 + f4, f3 + f5);
            return;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(f2, f3, f4, f5, 0.0f, 0.0f);
            this.renderWithComplexPaint(scratchRRect, null, f2, f3, f4, f5);
            return;
        }
        if (PrismSettings.primTextureSize != 0) {
            Texture texture = this.context.getRectTexture();
            Texture texture2 = this.context.getWrapRectTexture();
            boolean bl = this.fillPrimRect(f2, f3, f4, f5, texture, texture2, f2, f3, f4, f5);
            texture.unlock();
            texture2.unlock();
            if (bl) {
                return;
            }
        }
        this.renderGeneralRoundedRect(f2, f3, f4, f5, 0.0f, 0.0f, BaseShaderContext.MaskType.FILL_PGRAM, null);
    }

    @Override
    public void fillEllipse(float f2, float f3, float f4, float f5) {
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return;
        }
        if (this.isComplexPaint) {
            scratchEllipse.setFrame(f2, f3, f4, f5);
            this.renderWithComplexPaint(scratchEllipse, null, f2, f3, f4, f5);
            return;
        }
        if (!this.isAntialiasedShape()) {
            scratchEllipse.setFrame(f2, f3, f4, f5);
            this.renderShape(scratchEllipse, null, f2, f3, f4, f5);
            return;
        }
        if (PrismSettings.primTextureSize != 0 && this.fillPrimRect(f2, f3, f4, f5, this.context.getOvalTexture(), null, f2, f3, f4, f5)) {
            return;
        }
        this.renderGeneralRoundedRect(f2, f3, f4, f5, f4, f5, BaseShaderContext.MaskType.FILL_ELLIPSE, null);
    }

    @Override
    public void fillRoundRect(float f2, float f3, float f4, float f5, float f6, float f7) {
        f6 = Math.min(Math.abs(f6), f4);
        f7 = Math.min(Math.abs(f7), f5);
        if (f4 <= 0.0f || f5 <= 0.0f) {
            return;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(f2, f3, f4, f5, f6, f7);
            this.renderWithComplexPaint(scratchRRect, null, f2, f3, f4, f5);
            return;
        }
        if (!this.isAntialiasedShape()) {
            scratchRRect.setRoundRect(f2, f3, f4, f5, f6, f7);
            this.renderShape(scratchRRect, null, f2, f3, f4, f5);
            return;
        }
        this.renderGeneralRoundedRect(f2, f3, f4, f5, f6, f7, BaseShaderContext.MaskType.FILL_ROUNDRECT, null);
    }

    @Override
    public void fillQuad(float f2, float f3, float f4, float f5) {
        float f6;
        float f7;
        float f8;
        float f9;
        if (f2 <= f4) {
            f9 = f2;
            f8 = f4 - f2;
        } else {
            f9 = f4;
            f8 = f2 - f4;
        }
        if (f3 <= f5) {
            f7 = f3;
            f6 = f5 - f3;
        } else {
            f7 = f5;
            f6 = f3 - f5;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(f9, f7, f8, f6, 0.0f, 0.0f);
            this.renderWithComplexPaint(scratchRRect, null, f9, f7, f8, f6);
            return;
        }
        BaseTransform baseTransform = this.getTransformNoClone();
        if (PrismSettings.primTextureSize != 0) {
            float f10;
            float f11;
            if (baseTransform.isTranslateOrIdentity()) {
                f11 = (float)baseTransform.getMxt();
                f10 = (float)baseTransform.getMyt();
                baseTransform = IDENT;
                f2 += f11;
                f3 += f10;
                f4 += f11;
                f5 += f10;
            } else {
                f10 = 0.0f;
                f11 = 0.0f;
            }
            Shader shader = this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.ALPHA_ONE, null, f9, f7, f8, f6);
            AffineBase affineBase = this.getPaintTextureTx(IDENT, shader, f9, f7, f8, f6);
            if (affineBase != null) {
                affineBase.translate(-f11, -f10);
            }
            this.context.getVertexBuffer().addQuad(f2, f3, f4, f5, 0.0f, 0.0f, 0.0f, 0.0f, affineBase);
            return;
        }
        if (this.isSimpleTranslate) {
            baseTransform = IDENT;
            f9 += this.transX;
            f7 += this.transY;
        }
        this.context.validatePaintOp(this, baseTransform, BaseShaderContext.MaskType.SOLID, f9, f7, f8, f6);
        VertexBuffer vertexBuffer = this.context.getVertexBuffer();
        vertexBuffer.addQuad(f9, f7, f9 + f8, f7 + f6);
    }

    private static boolean canUseStrokeShader(BasicStroke basicStroke) {
        return !basicStroke.isDashed() && (basicStroke.getType() == 1 || basicStroke.getLineJoin() == 1 || basicStroke.getLineJoin() == 0 && (double)basicStroke.getMiterLimit() >= SQRT_2);
    }

    @Override
    public void blit(RTTexture rTTexture, RTTexture rTTexture2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9) {
        if (rTTexture2 == null) {
            this.context.setRenderTarget(this);
        } else {
            this.context.setRenderTarget((BaseGraphics)rTTexture2.createGraphics());
        }
        this.context.blit(rTTexture, rTTexture2, n2, n3, n4, n5, n6, n7, n8, n9);
    }

    @Override
    public void drawRect(float f2, float f3, float f4, float f5) {
        if (f4 < 0.0f || f5 < 0.0f) {
            return;
        }
        if (f4 == 0.0f || f5 == 0.0f) {
            this.drawLine(f2, f3, f2 + f4, f3 + f5);
            return;
        }
        if (this.isComplexPaint) {
            scratchRRect.setRoundRect(f2, f3, f4, f5, 0.0f, 0.0f);
            this.renderWithComplexPaint(scratchRRect, this.stroke, f2, f3, f4, f5);
            return;
        }
        if (!this.isAntialiasedShape()) {
            scratchRRect.setRoundRect(f2, f3, f4, f5, 0.0f, 0.0f);
            this.renderShape(scratchRRect, this.stroke, f2, f3, f4, f5);
            return;
        }
        if (BaseShaderGraphics.canUseStrokeShader(this.stroke)) {
            if (PrismSettings.primTextureSize != 0 && this.stroke.getLineJoin() != 1 && this.drawPrimRect(f2, f3, f4, f5)) {
                return;
            }
            this.renderGeneralRoundedRect(f2, f3, f4, f5, 0.0f, 0.0f, BaseShaderContext.MaskType.DRAW_PGRAM, this.stroke);
            return;
        }
        scratchRRect.setRoundRect(f2, f3, f4, f5, 0.0f, 0.0f);
        this.renderShape(scratchRRect, this.stroke, f2, f3, f4, f5);
    }

    private boolean checkInnerCurvature(float f2, float f3) {
        float f4 = this.stroke.getLineWidth() * (1.0f - BaseShaderGraphics.getStrokeExpansionFactor(this.stroke));
        return (f2 -= f4) <= 0.0f || (f3 -= f4) <= 0.0f || f2 * 2.0f > f3 && f3 * 2.0f > f2;
    }

    @Override
    public void drawEllipse(float f2, float f3, float f4, float f5) {
        if (f4 < 0.0f || f5 < 0.0f) {
            return;
        }
        if (!this.isComplexPaint && !this.stroke.isDashed() && this.checkInnerCurvature(f4, f5) && this.isAntialiasedShape()) {
            this.renderGeneralRoundedRect(f2, f3, f4, f5, f4, f5, BaseShaderContext.MaskType.DRAW_ELLIPSE, this.stroke);
            return;
        }
        scratchEllipse.setFrame(f2, f3, f4, f5);
        this.renderShape(scratchEllipse, this.stroke, f2, f3, f4, f5);
    }

    @Override
    public void drawRoundRect(float f2, float f3, float f4, float f5, float f6, float f7) {
        f6 = Math.min(Math.abs(f6), f4);
        f7 = Math.min(Math.abs(f7), f5);
        if (f4 < 0.0f || f5 < 0.0f) {
            return;
        }
        if (!this.isComplexPaint && !this.stroke.isDashed() && this.checkInnerCurvature(f6, f7) && this.isAntialiasedShape()) {
            this.renderGeneralRoundedRect(f2, f3, f4, f5, f6, f7, BaseShaderContext.MaskType.DRAW_ROUNDRECT, this.stroke);
            return;
        }
        scratchRRect.setRoundRect(f2, f3, f4, f5, f6, f7);
        this.renderShape(scratchRRect, this.stroke, f2, f3, f4, f5);
    }

    @Override
    public void drawLine(float f2, float f3, float f4, float f5) {
        BaseShaderContext.MaskType maskType;
        float f6;
        float f7;
        BaseTransform baseTransform;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float f13;
        float f14;
        float f15;
        float f16;
        float f17;
        float f18;
        if (f2 <= f4) {
            f18 = f2;
            f17 = f4 - f2;
        } else {
            f18 = f4;
            f17 = f2 - f4;
        }
        if (f3 <= f5) {
            f16 = f3;
            f15 = f5 - f3;
        } else {
            f16 = f5;
            f15 = f3 - f5;
        }
        if (this.stroke.getType() == 1) {
            return;
        }
        if (this.isComplexPaint) {
            scratchLine.setLine(f2, f3, f4, f5);
            this.renderWithComplexPaint(scratchLine, this.stroke, f18, f16, f17, f15);
            return;
        }
        if (!this.isAntialiasedShape()) {
            scratchLine.setLine(f2, f3, f4, f5);
            this.renderShape(scratchLine, this.stroke, f18, f16, f17, f15);
            return;
        }
        int n2 = this.stroke.getEndCap();
        if (this.stroke.isDashed()) {
            scratchLine.setLine(f2, f3, f4, f5);
            this.renderShape(scratchLine, this.stroke, f18, f16, f17, f15);
            return;
        }
        float f19 = this.stroke.getLineWidth();
        if (PrismSettings.primTextureSize != 0 && n2 != 1) {
            f14 = f19;
            if (this.stroke.getType() == 0) {
                f14 *= 0.5f;
            }
            if (f17 == 0.0f || f15 == 0.0f) {
                if (n2 == 2) {
                    f12 = f13 = f14;
                } else if (f17 != 0.0f) {
                    f12 = 0.0f;
                    f13 = f14;
                } else if (f15 != 0.0f) {
                    f12 = f14;
                    f13 = 0.0f;
                } else {
                    return;
                }
                Texture texture = this.context.getRectTexture();
                Texture texture2 = this.context.getWrapRectTexture();
                boolean bl = this.fillPrimRect(f18 - f12, f16 - f13, f17 + f12 + f12, f15 + f13 + f13, texture, texture2, f18, f16, f17, f15);
                texture.unlock();
                texture2.unlock();
                if (bl) {
                    return;
                }
            } else if (this.drawPrimDiagonal(f2, f3, f4, f5, f19, n2, f18, f16, f17, f15)) {
                return;
            }
        }
        if (this.stroke.getType() == 2) {
            f19 *= 2.0f;
        }
        if ((f13 = BaseShaderGraphics.len(f14 = f4 - f2, f12 = f5 - f3)) == 0.0f) {
            if (n2 == 0) {
                return;
            }
            f11 = f19;
            f10 = 0.0f;
        } else {
            f11 = f19 * f14 / f13;
            f10 = f19 * f12 / f13;
        }
        BaseTransform baseTransform2 = this.getTransformNoClone();
        if (this.isSimpleTranslate) {
            double d2 = baseTransform2.getMxt();
            double d3 = baseTransform2.getMyt();
            f2 = (float)((double)f2 + d2);
            f3 = (float)((double)f3 + d3);
            f4 = (float)((double)f4 + d2);
            f5 = (float)((double)f5 + d3);
            f9 = f10;
            f8 = -f11;
            baseTransform = IDENT;
        } else {
            baseTransform = this.extract3Dremainder(baseTransform2);
            double[] arrd = new double[]{f2, f3, f4, f5};
            baseTransform2.transform(arrd, 0, arrd, 0, 2);
            f2 = (float)arrd[0];
            f3 = (float)arrd[1];
            f4 = (float)arrd[2];
            f5 = (float)arrd[3];
            f14 = f4 - f2;
            f12 = f5 - f3;
            arrd[0] = f11;
            arrd[1] = f10;
            arrd[2] = f10;
            arrd[3] = -f11;
            baseTransform2.deltaTransform(arrd, 0, arrd, 0, 2);
            f11 = (float)arrd[0];
            f10 = (float)arrd[1];
            f9 = (float)arrd[2];
            f8 = (float)arrd[3];
        }
        float f20 = f2 - f9 / 2.0f;
        float f21 = f3 - f8 / 2.0f;
        if (n2 != 0) {
            f20 -= f11 / 2.0f;
            f21 -= f10 / 2.0f;
            f14 += f11;
            f12 += f10;
            if (n2 == 1) {
                f7 = BaseShaderGraphics.len(f11, f10) / BaseShaderGraphics.len(f14, f12);
                f6 = 1.0f;
                maskType = BaseShaderContext.MaskType.FILL_ROUNDRECT;
            } else {
                f6 = 0.0f;
                f7 = 0.0f;
                maskType = BaseShaderContext.MaskType.FILL_PGRAM;
            }
        } else {
            f6 = 0.0f;
            f7 = 0.0f;
            maskType = BaseShaderContext.MaskType.FILL_PGRAM;
        }
        this.renderGeneralRoundedPgram(f20, f21, f14, f12, f9, f8, f7, f6, 0.0f, 0.0f, baseTransform, maskType, f18, f16, f17, f15);
    }

    private static float len(float f2, float f3) {
        return f2 == 0.0f ? Math.abs(f3) : (f3 == 0.0f ? Math.abs(f2) : (float)Math.sqrt(f2 * f2 + f3 * f3));
    }

    @Override
    public void setNodeBounds(RectBounds rectBounds) {
        this.nodeBounds = rectBounds;
        this.lcdSampleInvalid = rectBounds != null;
    }

    private void initLCDSampleRT() {
        if (this.lcdSampleInvalid) {
            RectBounds rectBounds = new RectBounds();
            this.getTransformNoClone().transform(this.nodeBounds, rectBounds);
            Rectangle rectangle = this.getClipRectNoClone();
            if (rectangle != null && !rectangle.isEmpty()) {
                rectBounds.intersectWith(rectangle);
            }
            float f2 = rectBounds.getMinX() - 1.0f;
            float f3 = rectBounds.getMinY();
            float f4 = rectBounds.getWidth() + 2.0f;
            float f5 = rectBounds.getHeight() + 1.0f;
            this.context.validateLCDBuffer(this.getRenderTarget());
            BaseShaderGraphics baseShaderGraphics = (BaseShaderGraphics)this.context.getLCDBuffer().createGraphics();
            baseShaderGraphics.setCompositeMode(CompositeMode.SRC);
            this.context.validateLCDOp(baseShaderGraphics, IDENT, (Texture)((Object)this.getRenderTarget()), null, true, null);
            int n2 = this.getRenderTarget().getPhysicalHeight();
            int n3 = this.getRenderTarget().getPhysicalWidth();
            float f6 = f2 / (float)n3;
            float f7 = f3 / (float)n2;
            float f8 = (f2 + f4) / (float)n3;
            float f9 = (f3 + f5) / (float)n2;
            baseShaderGraphics.drawLCDBuffer(f2, f3, f4, f5, f6, f7, f8, f9);
            this.context.setRenderTarget(this);
        }
        this.lcdSampleInvalid = false;
    }

    @Override
    public void drawString(GlyphList glyphList, FontStrike fontStrike, float f2, float f3, Color color, int n2, int n3) {
        Object object;
        RectBounds rectBounds;
        float f4;
        boolean bl;
        if (this.isComplexPaint || this.paint.getType().isImagePattern() || fontStrike.drawAsShapes()) {
            BaseTransform baseTransform = BaseTransform.getTranslateInstance(f2, f3);
            Shape shape = fontStrike.getOutline(glyphList, baseTransform);
            this.fill(shape);
            return;
        }
        BaseTransform baseTransform = this.getTransformNoClone();
        Paint paint = this.getPaint();
        Color color2 = paint.getType() == Paint.Type.COLOR ? (Color)paint : null;
        CompositeMode compositeMode = this.getCompositeMode();
        boolean bl2 = bl = compositeMode == CompositeMode.SRC_OVER && color2 != null && baseTransform.is2D() && !this.getRenderTarget().isMSAA();
        if (fontStrike.getAAMode() == 1 && !bl) {
            FontResource fontResource = fontStrike.getFontResource();
            f4 = fontStrike.getSize();
            BaseTransform baseTransform2 = fontStrike.getTransform();
            fontStrike = fontResource.getStrike(f4, baseTransform2, 0);
        }
        float f5 = 0.0f;
        f4 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        if (this.paint.getType().isGradient() && ((Gradient)this.paint).isProportional()) {
            rectBounds = this.nodeBounds;
            if (rectBounds == null) {
                object = fontStrike.getMetrics();
                float f8 = -object.getAscent() * 0.4f;
                rectBounds = new RectBounds(-f8, object.getAscent(), glyphList.getWidth() + 2.0f * f8, object.getDescent() + object.getLineGap());
                f5 = f2;
                f4 = f3;
            }
            f5 += rectBounds.getMinX();
            f4 += rectBounds.getMinY();
            f6 = rectBounds.getWidth();
            f7 = rectBounds.getHeight();
        }
        rectBounds = null;
        object = new Point2D(f2, f3);
        if (this.isSimpleTranslate) {
            rectBounds = this.getFinalClipNoClone();
            baseTransform = IDENT;
            ((Point2D)object).x += this.transX;
            ((Point2D)object).y += this.transY;
        }
        GlyphCache glyphCache = this.context.getGlyphCache(fontStrike);
        Texture texture = glyphCache.getBackingStore();
        if (fontStrike.getAAMode() == 1) {
            if (this.nodeBounds == null) {
                Metrics metrics = fontStrike.getMetrics();
                RectBounds rectBounds2 = new RectBounds(f2 - 2.0f, f3 + metrics.getAscent(), f2 + 2.0f + glyphList.getWidth(), f3 + 1.0f + metrics.getDescent() + metrics.getLineGap());
                this.setNodeBounds(rectBounds2);
                this.initLCDSampleRT();
                this.setNodeBounds(null);
            } else {
                this.initLCDSampleRT();
            }
            float f9 = PrismFontFactory.getLCDContrast();
            float f10 = 1.0f / f9;
            color2 = new Color((float)Math.pow(color2.getRed(), f9), (float)Math.pow(color2.getGreen(), f9), (float)Math.pow(color2.getBlue(), f9), (float)Math.pow(color2.getAlpha(), f9));
            if (color != null) {
                color = new Color((float)Math.pow(color.getRed(), f9), (float)Math.pow(color.getGreen(), f9), (float)Math.pow(color.getBlue(), f9), (float)Math.pow(color.getAlpha(), f9));
            }
            this.setCompositeMode(CompositeMode.SRC);
            Shader shader = this.context.validateLCDOp(this, IDENT, this.context.getLCDBuffer(), texture, false, color2);
            float f11 = 1.0f / (float)texture.getPhysicalWidth();
            shader.setConstant("gamma", f10, f9, f11);
            this.setCompositeMode(compositeMode);
        } else {
            this.context.validatePaintOp(this, IDENT, texture, f5, f4, f6, f7);
        }
        if (this.isSimpleTranslate) {
            ((Point2D)object).y = Math.round(((Point2D)object).y);
            ((Point2D)object).x = Math.round(((Point2D)object).x);
        }
        glyphCache.render(this.context, glyphList, ((Point2D)object).x, ((Point2D)object).y, n2, n3, color, color2, baseTransform, rectBounds);
    }

    private void drawLCDBuffer(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.context.setRenderTarget(this);
        this.context.getVertexBuffer().addQuad(f2, f3, f2 + f4, f3 + f5, f6, f7, f8, f9);
    }

    @Override
    public boolean canReadBack() {
        RenderTarget renderTarget = this.getRenderTarget();
        return renderTarget instanceof ReadbackRenderTarget && ((ReadbackRenderTarget)renderTarget).getBackBuffer() != null;
    }

    @Override
    public RTTexture readBack(Rectangle rectangle) {
        RenderTarget renderTarget = this.getRenderTarget();
        this.context.flushVertexBuffer();
        this.context.validateLCDBuffer(renderTarget);
        RTTexture rTTexture = this.context.getLCDBuffer();
        Texture texture = ((ReadbackRenderTarget)renderTarget).getBackBuffer();
        float f2 = rectangle.x;
        float f3 = rectangle.y;
        float f4 = f2 + (float)rectangle.width;
        float f5 = f3 + (float)rectangle.height;
        BaseShaderGraphics baseShaderGraphics = (BaseShaderGraphics)rTTexture.createGraphics();
        baseShaderGraphics.setCompositeMode(CompositeMode.SRC);
        this.context.validateTextureOp((BaseGraphics)baseShaderGraphics, IDENT, texture, texture.getPixelFormat());
        baseShaderGraphics.drawTexture(texture, 0.0f, 0.0f, rectangle.width, rectangle.height, f2, f3, f4, f5);
        this.context.flushVertexBuffer();
        this.context.setRenderTarget(this);
        return rTTexture;
    }

    @Override
    public void releaseReadBackBuffer(RTTexture rTTexture) {
    }

    @Override
    public void setup3DRendering() {
        this.context.setRenderTarget(this);
    }

    static {
        String string = (String)AccessController.doPrivileged(() -> System.getProperty("prism.primshaderpad"));
        if (string == null) {
            FRINGE_FACTOR = -0.5f;
        } else {
            FRINGE_FACTOR = -Float.valueOf(string).floatValue();
            System.out.println("Prism ShaderGraphics primitive shader pad = " + FRINGE_FACTOR);
        }
        SQRT_2 = Math.sqrt(2.0);
    }
}

