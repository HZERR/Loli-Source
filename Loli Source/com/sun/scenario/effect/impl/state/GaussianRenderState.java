/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.BufferUtil;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.state.RenderState;
import java.nio.FloatBuffer;

public class GaussianRenderState
extends LinearConvolveRenderState {
    public static final float MAX_RADIUS = (MAX_KERNEL_SIZE - 1) / 2;
    private boolean isShadow;
    private Color4f shadowColor;
    private float spread;
    private RenderState.EffectCoordinateSpace space;
    private BaseTransform inputtx;
    private BaseTransform resulttx;
    private float inputRadiusX;
    private float inputRadiusY;
    private float spreadPass;
    private int validatedPass;
    private LinearConvolveRenderState.PassType passType;
    private float passRadius;
    private FloatBuffer weights;
    private float[] samplevectors;
    private float weightsValidRadius;
    private float weightsValidSpread;

    static FloatBuffer getGaussianWeights(FloatBuffer floatBuffer, int n2, float f2, float f3) {
        int n3;
        int n4 = n2;
        int n5 = n4 * 2 + 1;
        if (floatBuffer == null) {
            floatBuffer = BufferUtil.newFloatBuffer(128);
        }
        floatBuffer.clear();
        float f4 = f2 / 3.0f;
        float f5 = 2.0f * f4 * f4;
        if (f5 < Float.MIN_VALUE) {
            f5 = Float.MIN_VALUE;
        }
        float f6 = 0.0f;
        for (n3 = -n4; n3 <= n4; ++n3) {
            float f7 = (float)Math.exp((float)(-(n3 * n3)) / f5);
            floatBuffer.put(f7);
            f6 += f7;
        }
        f6 += (floatBuffer.get(0) - f6) * f3;
        for (n3 = 0; n3 < n5; ++n3) {
            floatBuffer.put(n3, floatBuffer.get(n3) / f6);
        }
        n3 = GaussianRenderState.getPeerSize(n5);
        while (floatBuffer.position() < n3) {
            floatBuffer.put(0.0f);
        }
        floatBuffer.limit(n3);
        floatBuffer.rewind();
        return floatBuffer;
    }

    public GaussianRenderState(float f2, float f3, float f4, boolean bl, Color4f color4f, BaseTransform baseTransform) {
        this.isShadow = bl;
        this.shadowColor = color4f;
        this.spread = f4;
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        double d2 = baseTransform.getMxx();
        double d3 = baseTransform.getMxy();
        double d4 = baseTransform.getMyx();
        double d5 = baseTransform.getMyy();
        double d6 = Math.hypot(d2, d4);
        double d7 = Math.hypot(d3, d5);
        boolean bl2 = false;
        float f5 = (float)((double)f2 * d6);
        float f6 = (float)((double)f3 * d7);
        if (f5 < 0.00390625f && f6 < 0.00390625f) {
            this.inputRadiusX = 0.0f;
            this.inputRadiusY = 0.0f;
            this.spreadPass = 0.0f;
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = baseTransform;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
            this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
        } else {
            if (f5 > MAX_RADIUS) {
                f5 = MAX_RADIUS;
                d6 = MAX_RADIUS / f2;
                bl2 = true;
            }
            if (f6 > MAX_RADIUS) {
                f6 = MAX_RADIUS;
                d7 = MAX_RADIUS / f3;
                bl2 = true;
            }
            this.inputRadiusX = f5;
            this.inputRadiusY = f6;
            float f7 = this.spreadPass = this.inputRadiusY > 1.0f || this.inputRadiusY >= this.inputRadiusX ? 1.0f : 0.0f;
            if (bl2) {
                this.space = RenderState.EffectCoordinateSpace.CustomSpace;
                this.inputtx = BaseTransform.getScaleInstance(d6, d7);
                this.resulttx = baseTransform.copy().deriveWithScale(1.0 / d6, 1.0 / d7, 1.0);
                this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f};
            } else {
                this.space = RenderState.EffectCoordinateSpace.RenderSpace;
                this.inputtx = baseTransform;
                this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
                this.samplevectors = new float[]{(float)(d2 / d6), (float)(d4 / d6), (float)(d3 / d7), (float)(d5 / d7), 0.0f, 0.0f};
            }
        }
    }

    public GaussianRenderState(float f2, float f3, float f4, BaseTransform baseTransform) {
        this.isShadow = false;
        this.spread = 0.0f;
        if (baseTransform == null) {
            baseTransform = BaseTransform.IDENTITY_TRANSFORM;
        }
        double d2 = baseTransform.getMxx();
        double d3 = baseTransform.getMxy();
        double d4 = baseTransform.getMyx();
        double d5 = baseTransform.getMyy();
        double d6 = d2 * (double)f3 + d3 * (double)f4;
        double d7 = d4 * (double)f3 + d5 * (double)f4;
        double d8 = Math.hypot(d6, d7);
        boolean bl = false;
        float f5 = (float)((double)f2 * d8);
        if (f5 < 0.00390625f) {
            this.inputRadiusX = 0.0f;
            this.inputRadiusY = 0.0f;
            this.spreadPass = 0.0f;
            this.space = RenderState.EffectCoordinateSpace.RenderSpace;
            this.inputtx = baseTransform;
            this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
            this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        } else {
            if (f5 > MAX_RADIUS) {
                f5 = MAX_RADIUS;
                d8 = MAX_RADIUS / f2;
                bl = true;
            }
            this.inputRadiusX = f5;
            this.inputRadiusY = 0.0f;
            this.spreadPass = 0.0f;
            if (bl) {
                BaseTransform baseTransform2;
                double d9 = d3 * (double)f3 - d2 * (double)f4;
                double d10 = d5 * (double)f3 - d4 * (double)f4;
                double d11 = Math.hypot(d9, d10);
                this.space = RenderState.EffectCoordinateSpace.CustomSpace;
                Affine2D affine2D = new Affine2D();
                affine2D.scale(d8, d11);
                affine2D.rotate(f3, -f4);
                try {
                    baseTransform2 = affine2D.createInverse();
                }
                catch (NoninvertibleTransformException noninvertibleTransformException) {
                    baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
                }
                this.inputtx = affine2D;
                this.resulttx = baseTransform.copy().deriveWithConcatenation(baseTransform2);
                this.samplevectors = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
            } else {
                this.space = RenderState.EffectCoordinateSpace.RenderSpace;
                this.inputtx = baseTransform;
                this.resulttx = BaseTransform.IDENTITY_TRANSFORM;
                this.samplevectors = new float[]{(float)(d6 / d8), (float)(d7 / d8), 0.0f, 0.0f, 0.0f, 0.0f};
            }
        }
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

    @Override
    public Rectangle getInputClip(int n2, Rectangle rectangle) {
        if (rectangle != null) {
            int n3;
            double d2 = this.samplevectors[0] * this.inputRadiusX;
            double d3 = this.samplevectors[1] * this.inputRadiusX;
            double d4 = this.samplevectors[2] * this.inputRadiusY;
            double d5 = this.samplevectors[3] * this.inputRadiusY;
            int n4 = (int)Math.ceil(d2 + d4);
            if ((n4 | (n3 = (int)Math.ceil(d3 + d5))) != 0) {
                rectangle = new Rectangle(rectangle);
                rectangle.grow(n4, n3);
            }
        }
        return rectangle;
    }

    @Override
    public ImageData validatePassInput(ImageData imageData, int n2) {
        this.validatedPass = n2;
        Filterable filterable = imageData.getUntransformedImage();
        BaseTransform baseTransform = imageData.getTransform();
        float f2 = n2 == 0 ? this.inputRadiusX : this.inputRadiusY;
        int n3 = n2 * 2;
        if (baseTransform.isTranslateOrIdentity()) {
            this.passRadius = f2;
            this.samplevectors[4] = this.samplevectors[n3];
            this.samplevectors[5] = this.samplevectors[n3 + 1];
            this.passType = this.validatedPass == 0 ? (GaussianRenderState.nearOne(this.samplevectors[4], filterable.getPhysicalWidth()) && GaussianRenderState.nearZero(this.samplevectors[5], filterable.getPhysicalWidth()) ? LinearConvolveRenderState.PassType.HORIZONTAL_CENTERED : LinearConvolveRenderState.PassType.GENERAL_VECTOR) : (GaussianRenderState.nearZero(this.samplevectors[4], filterable.getPhysicalHeight()) && GaussianRenderState.nearOne(this.samplevectors[5], filterable.getPhysicalHeight()) ? LinearConvolveRenderState.PassType.VERTICAL_CENTERED : LinearConvolveRenderState.PassType.GENERAL_VECTOR);
        } else {
            this.passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
            try {
                baseTransform.inverseDeltaTransform(this.samplevectors, n3, this.samplevectors, 4, 1);
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                this.passRadius = 0.0f;
                this.samplevectors[5] = 0.0f;
                this.samplevectors[4] = 0.0f;
                return imageData;
            }
            double d2 = Math.hypot(this.samplevectors[4], this.samplevectors[5]);
            float f3 = (float)((double)f2 * d2);
            if (f3 > MAX_RADIUS) {
                f3 = MAX_RADIUS;
                d2 = MAX_RADIUS / f2;
            }
            this.passRadius = f3;
            this.samplevectors[4] = (float)((double)this.samplevectors[4] / d2);
            this.samplevectors[5] = (float)((double)this.samplevectors[5] / d2);
        }
        this.samplevectors[4] = this.samplevectors[4] / (float)filterable.getPhysicalWidth();
        this.samplevectors[5] = this.samplevectors[5] / (float)filterable.getPhysicalHeight();
        return imageData;
    }

    @Override
    public Rectangle getPassResultBounds(Rectangle rectangle, Rectangle rectangle2) {
        double d2 = this.validatedPass == 0 ? (double)this.inputRadiusX : (double)this.inputRadiusY;
        int n2 = this.validatedPass * 2;
        double d3 = (double)this.samplevectors[n2 + 0] * d2;
        double d4 = (double)this.samplevectors[n2 + 1] * d2;
        int n3 = (int)Math.ceil(Math.abs(d3));
        int n4 = (int)Math.ceil(Math.abs(d4));
        Rectangle rectangle3 = new Rectangle(rectangle);
        rectangle3.grow(n3, n4);
        if (rectangle2 != null) {
            if (this.validatedPass == 0) {
                d3 = (double)this.samplevectors[2] * d2;
                d4 = (double)this.samplevectors[3] * d2;
                n3 = (int)Math.ceil(Math.abs(d3));
                if ((n3 | (n4 = (int)Math.ceil(Math.abs(d4)))) != 0) {
                    rectangle2 = new Rectangle(rectangle2);
                    rectangle2.grow(n3, n4);
                }
            }
            rectangle3.intersectWith(rectangle2);
        }
        return rectangle3;
    }

    @Override
    public LinearConvolveRenderState.PassType getPassType() {
        return this.passType;
    }

    @Override
    public float[] getPassVector() {
        float f2 = this.samplevectors[4];
        float f3 = this.samplevectors[5];
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

    @Override
    public int getInputKernelSize(int n2) {
        return 1 + 2 * (int)Math.ceil(n2 == 0 ? (double)this.inputRadiusX : (double)this.inputRadiusY);
    }

    @Override
    public int getPassKernelSize() {
        return 1 + 2 * (int)Math.ceil(this.passRadius);
    }

    @Override
    public boolean isNop() {
        if (this.isShadow) {
            return false;
        }
        return this.inputRadiusX < 0.00390625f && this.inputRadiusY < 0.00390625f;
    }

    @Override
    public boolean isPassNop() {
        if (this.isShadow && this.validatedPass == 1) {
            return false;
        }
        return this.passRadius < 0.00390625f;
    }

    private void validateWeights() {
        float f2;
        float f3 = this.passRadius;
        float f4 = f2 = (float)this.validatedPass == this.spreadPass ? this.spread : 0.0f;
        if (this.weights == null || this.weightsValidRadius != f3 || this.weightsValidSpread != f2) {
            this.weights = GaussianRenderState.getGaussianWeights(this.weights, (int)Math.ceil(f3), f3, f2);
            this.weightsValidRadius = f3;
            this.weightsValidSpread = f2;
        }
    }
}

