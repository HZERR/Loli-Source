/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.state.RenderState;
import java.nio.FloatBuffer;

public class BoxRenderState
extends LinearConvolveRenderState {
    private static final int[] MAX_BOX_SIZES = new int[]{BoxRenderState.getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 0), BoxRenderState.getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 1), BoxRenderState.getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 2), BoxRenderState.getMaxSizeForKernelSize(MAX_KERNEL_SIZE, 3)};
    private final boolean isShadow;
    private final int blurPasses;
    private final float spread;
    private Color4f shadowColor;
    private RenderState.EffectCoordinateSpace space;
    private BaseTransform inputtx;
    private BaseTransform resulttx;
    private final float inputSizeH;
    private final float inputSizeV;
    private final int spreadPass;
    private float[] samplevectors;
    private int validatedPass;
    private float passSize;
    private FloatBuffer weights;
    private float weightsValidSize;
    private float weightsValidSpread;
    private boolean swCompatible;

    public static int getMaxSizeForKernelSize(int n2, int n3) {
        if (n3 == 0) {
            return Integer.MAX_VALUE;
        }
        int n4 = n2 - 1 | 1;
        n4 = (n4 - 1) / n3 | 1;
        assert (BoxRenderState.getKernelSize(n4, n3) <= n2);
        return n4;
    }

    public static int getKernelSize(int n2, int n3) {
        int n4 = n2 < 1 ? 1 : n2;
        n4 = (n4 - 1) * n3 + 1;
        return n4 |= 1;
    }

    public BoxRenderState(float f2, float f3, int n2, float f4, boolean bl, Color4f color4f, BaseTransform baseTransform) {
        boolean bl2;
        this.isShadow = bl;
        this.shadowColor = color4f;
        this.spread = f4;
        this.blurPasses = n2;
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        double d2 = Math.hypot(baseTransform.getMxx(), baseTransform.getMyx());
        double d3 = Math.hypot(baseTransform.getMxy(), baseTransform.getMyy());
        float f5 = (float)((double)f2 * d2);
        float f6 = (float)((double)f3 * d3);
        int n3 = MAX_BOX_SIZES[n2];
        if (f5 > (float)n3) {
            d2 = (float)n3 / f2;
            f5 = n3;
        }
        if (f6 > (float)n3) {
            d3 = (float)n3 / f3;
            f6 = n3;
        }
        this.inputSizeH = f5;
        this.inputSizeV = f6;
        this.spreadPass = f6 > 1.0f ? 1 : 0;
        boolean bl3 = bl2 = d2 != baseTransform.getMxx() || 0.0 != baseTransform.getMyx() || d3 != baseTransform.getMyy() || 0.0 != baseTransform.getMxy();
        if (bl2) {
            this.space = RenderState.EffectCoordinateSpace.CustomSpace;
            this.inputtx = BaseTransform.getScaleInstance(d2, d3);
            this.resulttx = baseTransform.copy().deriveWithScale(1.0 / d2, 1.0 / d3, 1.0);
        } else {
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = baseTransform;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
        }
    }

    public int getBoxPixelSize(int n2) {
        float f2 = this.passSize;
        if (f2 < 1.0f) {
            f2 = 1.0f;
        }
        int n3 = (int)Math.ceil(f2) | 1;
        return n3;
    }

    public int getBlurPasses() {
        return this.blurPasses;
    }

    public float getSpread() {
        return this.spread;
    }

    @Override
    public boolean isShadow() {
        return this.isShadow;
    }

    @Override
    public Color4f getShadowColor() {
        return this.shadowColor;
    }

    @Override
    public float[] getPassShadowColorComponents() {
        return this.validatedPass == 0 ? BLACK_COMPONENTS : this.shadowColor.getPremultipliedRGBComponents();
    }

    @Override
    public RenderState.EffectCoordinateSpace getEffectTransformSpace() {
        return this.space;
    }

    @Override
    public BaseTransform getInputTransform(BaseTransform baseTransform) {
        return this.inputtx;
    }

    @Override
    public BaseTransform getResultTransform(BaseTransform baseTransform) {
        return this.resulttx;
    }

    public EffectPeer<BoxRenderState> getPassPeer(Renderer renderer, FilterContext filterContext) {
        String string;
        if (this.isPassNop()) {
            return null;
        }
        int n2 = this.getPassKernelSize();
        int n3 = BoxRenderState.getPeerSize(n2);
        Effect.AccelType accelType = renderer.getAccelType();
        switch (accelType) {
            case NONE: 
            case SIMD: {
                if (this.swCompatible && this.spread == 0.0f) {
                    string = this.isShadow() ? "BoxShadow" : "BoxBlur";
                    break;
                }
            }
            default: {
                string = this.isShadow() ? "LinearConvolveShadow" : "LinearConvolve";
            }
        }
        EffectPeer effectPeer = renderer.getPeerInstance(filterContext, string, n3);
        return effectPeer;
    }

    @Override
    public Rectangle getInputClip(int n2, Rectangle rectangle) {
        int n3;
        int n4;
        if (rectangle != null && ((n4 = this.getInputKernelSize(0)) | (n3 = this.getInputKernelSize(1))) > 1) {
            rectangle = new Rectangle(rectangle);
            rectangle.grow(n4 / 2, n3 / 2);
        }
        return rectangle;
    }

    @Override
    public ImageData validatePassInput(ImageData imageData, int n2) {
        float f2;
        this.validatedPass = n2;
        BaseTransform baseTransform = imageData.getTransform();
        this.samplevectors = new float[2];
        this.samplevectors[n2] = 1.0f;
        float f3 = f2 = n2 == 0 ? this.inputSizeH : this.inputSizeV;
        if (baseTransform.isTranslateOrIdentity()) {
            this.swCompatible = true;
            this.passSize = f2;
        } else {
            try {
                baseTransform.inverseDeltaTransform(this.samplevectors, 0, this.samplevectors, 0, 1);
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                this.passSize = 0.0f;
                this.samplevectors[1] = 0.0f;
                this.samplevectors[0] = 0.0f;
                this.swCompatible = true;
                return imageData;
            }
            double d2 = Math.hypot(this.samplevectors[0], this.samplevectors[1]);
            float f4 = (float)((double)f2 * d2);
            f4 = (float)((double)f4 * d2);
            int n3 = MAX_BOX_SIZES[this.blurPasses];
            if (f4 > (float)n3) {
                f4 = n3;
                d2 = (float)n3 / f2;
            }
            this.passSize = f4;
            this.samplevectors[0] = (float)((double)this.samplevectors[0] / d2);
            this.samplevectors[1] = (float)((double)this.samplevectors[1] / d2);
            Rectangle rectangle = imageData.getUntransformedBounds();
            this.swCompatible = n2 == 0 ? BoxRenderState.nearOne(this.samplevectors[0], rectangle.width) && BoxRenderState.nearZero(this.samplevectors[1], rectangle.width) : BoxRenderState.nearZero(this.samplevectors[0], rectangle.height) && BoxRenderState.nearOne(this.samplevectors[1], rectangle.height);
        }
        Filterable filterable = imageData.getUntransformedImage();
        this.samplevectors[0] = this.samplevectors[0] / (float)filterable.getPhysicalWidth();
        this.samplevectors[1] = this.samplevectors[1] / (float)filterable.getPhysicalHeight();
        return imageData;
    }

    @Override
    public Rectangle getPassResultBounds(Rectangle rectangle, Rectangle rectangle2) {
        Rectangle rectangle3 = new Rectangle(rectangle);
        if (this.validatedPass == 0) {
            rectangle3.grow(this.getInputKernelSize(0) / 2, 0);
        } else {
            rectangle3.grow(0, this.getInputKernelSize(1) / 2);
        }
        if (rectangle2 != null) {
            if (this.validatedPass == 0) {
                rectangle2 = new Rectangle(rectangle2);
                rectangle2.grow(0, this.getInputKernelSize(1) / 2);
            }
            rectangle3.intersectWith(rectangle2);
        }
        return rectangle3;
    }

    @Override
    public float[] getPassVector() {
        float f2 = this.samplevectors[0];
        float f3 = this.samplevectors[1];
        int n2 = this.getPassKernelSize();
        int n3 = n2 / 2;
        float[] arrf = new float[]{f2, f3, (float)(-n3) * f2, (float)(-n3) * f3};
        return arrf;
    }

    @Override
    public int getPassWeightsArrayLength() {
        this.validateWeights();
        return this.weights.limit() / 4;
    }

    @Override
    public FloatBuffer getPassWeights() {
        this.validateWeights();
        this.weights.rewind();
        return this.weights;
    }

    private void validateWeights() {
        int n2;
        int n3;
        float f2;
        float f3;
        if (this.blurPasses == 0) {
            f3 = 1.0f;
        } else {
            f3 = this.passSize;
            if (f3 < 1.0f) {
                f3 = 1.0f;
            }
        }
        float f4 = f2 = this.validatedPass == this.spreadPass ? this.spread : 0.0f;
        if (this.weights != null && this.weightsValidSize == f3 && this.weightsValidSpread == f2) {
            return;
        }
        int n4 = n3 = (int)Math.ceil(f3) | 1;
        for (int i2 = 1; i2 < this.blurPasses; ++i2) {
            n4 += n3 - 1;
        }
        double[] arrd = new double[n4];
        for (int i3 = 0; i3 < n3; ++i3) {
            arrd[i3] = 1.0;
        }
        double d2 = (float)n3 - f3;
        if (d2 > 0.0) {
            double d3 = 1.0 - d2 * 0.5;
            arrd[n3 - 1] = d3;
            arrd[0] = d3;
        }
        int n5 = n3;
        for (int i4 = 1; i4 < this.blurPasses; ++i4) {
            int n6;
            double d4;
            int n7 = (n5 += n3 - 1) - 1;
            while (n7 > n3) {
                d4 = arrd[n7];
                for (n6 = 1; n6 < n3; ++n6) {
                    d4 += arrd[n7 - n6];
                }
                arrd[n7--] = d4;
            }
            while (n7 > 0) {
                d4 = arrd[n7];
                for (n6 = 0; n6 < n7; ++n6) {
                    d4 += arrd[n6];
                }
                arrd[n7--] = d4;
            }
        }
        double d5 = 0.0;
        for (n2 = 0; n2 < arrd.length; ++n2) {
            d5 += arrd[n2];
        }
        d5 += (1.0 - d5) * (double)f2;
        if (this.weights == null) {
            n2 = BoxRenderState.getPeerSize(MAX_KERNEL_SIZE);
            n2 = n2 + 3 & 0xFFFFFFFC;
            this.weights = BufferUtil.newFloatBuffer(n2);
        }
        this.weights.clear();
        for (n2 = 0; n2 < arrd.length; ++n2) {
            this.weights.put((float)(arrd[n2] / d5));
        }
        n2 = BoxRenderState.getPeerSize(arrd.length);
        while (this.weights.position() < n2) {
            this.weights.put(0.0f);
        }
        this.weights.limit(n2);
        this.weights.rewind();
    }

    @Override
    public int getInputKernelSize(int n2) {
        float f2;
        float f3 = f2 = n2 == 0 ? this.inputSizeH : this.inputSizeV;
        if (f2 < 1.0f) {
            f2 = 1.0f;
        }
        int n3 = (int)Math.ceil(f2) | 1;
        int n4 = 1;
        for (int i2 = 0; i2 < this.blurPasses; ++i2) {
            n4 += n3 - 1;
        }
        return n4;
    }

    @Override
    public int getPassKernelSize() {
        float f2 = this.passSize;
        if (f2 < 1.0f) {
            f2 = 1.0f;
        }
        int n2 = (int)Math.ceil(f2) | 1;
        int n3 = 1;
        for (int i2 = 0; i2 < this.blurPasses; ++i2) {
            n3 += n2 - 1;
        }
        return n3;
    }

    @Override
    public boolean isNop() {
        if (this.isShadow) {
            return false;
        }
        return this.blurPasses == 0 || this.inputSizeH <= 1.0f && this.inputSizeV <= 1.0f;
    }

    @Override
    public boolean isPassNop() {
        if (this.isShadow && this.validatedPass == 1) {
            return false;
        }
        return this.blurPasses == 0 || this.passSize <= 1.0f;
    }
}

