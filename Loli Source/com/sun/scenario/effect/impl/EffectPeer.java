/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class EffectPeer<T extends RenderState> {
    private final FilterContext fctx;
    private final Renderer renderer;
    private final String uniqueName;
    private Effect effect;
    private T renderState;
    private int pass;
    private final Rectangle[] inputBounds = new Rectangle[2];
    private final BaseTransform[] inputTransforms = new BaseTransform[2];
    private final Rectangle[] inputNativeBounds = new Rectangle[2];
    private Rectangle destBounds;
    private final Rectangle destNativeBounds = new Rectangle();

    protected EffectPeer(FilterContext filterContext, Renderer renderer, String string) {
        if (filterContext == null) {
            throw new IllegalArgumentException("FilterContext must be non-null");
        }
        this.fctx = filterContext;
        this.renderer = renderer;
        this.uniqueName = string;
    }

    public boolean isImageDataCompatible(ImageData imageData) {
        return this.getRenderer().isImageDataCompatible(imageData);
    }

    public abstract ImageData filter(Effect var1, T var2, BaseTransform var3, Rectangle var4, ImageData ... var5);

    public void dispose() {
    }

    public Effect.AccelType getAccelType() {
        return this.renderer.getAccelType();
    }

    protected final FilterContext getFilterContext() {
        return this.fctx;
    }

    protected Renderer getRenderer() {
        return this.renderer;
    }

    public String getUniqueName() {
        return this.uniqueName;
    }

    protected Effect getEffect() {
        return this.effect;
    }

    protected void setEffect(Effect effect) {
        this.effect = effect;
    }

    protected T getRenderState() {
        return this.renderState;
    }

    protected void setRenderState(T t2) {
        this.renderState = t2;
    }

    public final int getPass() {
        return this.pass;
    }

    public void setPass(int n2) {
        this.pass = n2;
    }

    protected final Rectangle getInputBounds(int n2) {
        return this.inputBounds[n2];
    }

    protected final void setInputBounds(int n2, Rectangle rectangle) {
        this.inputBounds[n2] = rectangle;
    }

    protected final BaseTransform getInputTransform(int n2) {
        return this.inputTransforms[n2];
    }

    protected final void setInputTransform(int n2, BaseTransform baseTransform) {
        this.inputTransforms[n2] = baseTransform;
    }

    protected final Rectangle getInputNativeBounds(int n2) {
        return this.inputNativeBounds[n2];
    }

    protected final void setInputNativeBounds(int n2, Rectangle rectangle) {
        this.inputNativeBounds[n2] = rectangle;
    }

    public Rectangle getResultBounds(BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        return this.getEffect().getResultBounds(baseTransform, rectangle, arrimageData);
    }

    protected float[] getSourceRegion(int n2) {
        return EffectPeer.getSourceRegion(this.getInputBounds(n2), this.getInputNativeBounds(n2), this.getDestBounds());
    }

    static float[] getSourceRegion(Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3) {
        float f2 = rectangle3.x - rectangle.x;
        float f3 = rectangle3.y - rectangle.y;
        float f4 = f2 + (float)rectangle3.width;
        float f5 = f3 + (float)rectangle3.height;
        float f6 = rectangle2.width;
        float f7 = rectangle2.height;
        return new float[]{f2 / f6, f3 / f7, f4 / f6, f5 / f7};
    }

    public int getTextureCoordinates(int n2, float[] arrf, float f2, float f3, float f4, float f5, Rectangle rectangle, BaseTransform baseTransform) {
        return EffectPeer.getTextureCoordinates(arrf, f2, f3, f4, f5, rectangle, baseTransform);
    }

    public static int getTextureCoordinates(float[] arrf, float f2, float f3, float f4, float f5, Rectangle rectangle, BaseTransform baseTransform) {
        int n2;
        arrf[0] = rectangle.x;
        arrf[1] = rectangle.y;
        arrf[2] = arrf[0] + (float)rectangle.width;
        arrf[3] = arrf[1] + (float)rectangle.height;
        if (baseTransform.isTranslateOrIdentity()) {
            f2 += (float)baseTransform.getMxt();
            f3 += (float)baseTransform.getMyt();
            n2 = 4;
        } else {
            arrf[4] = arrf[2];
            arrf[5] = arrf[1];
            arrf[6] = arrf[0];
            arrf[7] = arrf[3];
            n2 = 8;
            try {
                baseTransform.inverseTransform(arrf, 0, arrf, 0, 4);
            }
            catch (NoninvertibleTransformException noninvertibleTransformException) {
                arrf[4] = 0.0f;
                arrf[2] = 0.0f;
                arrf[1] = 0.0f;
                arrf[0] = 0.0f;
                return 4;
            }
        }
        for (int i2 = 0; i2 < n2; i2 += 2) {
            arrf[i2] = (arrf[i2] - f2) / f4;
            arrf[i2 + 1] = (arrf[i2 + 1] - f3) / f5;
        }
        return n2;
    }

    protected final void setDestBounds(Rectangle rectangle) {
        this.destBounds = rectangle;
    }

    protected final Rectangle getDestBounds() {
        return this.destBounds;
    }

    protected final Rectangle getDestNativeBounds() {
        return this.destNativeBounds;
    }

    protected final void setDestNativeBounds(int n2, int n3) {
        this.destNativeBounds.width = n2;
        this.destNativeBounds.height = n3;
    }

    protected Object getSamplerData(int n2) {
        return null;
    }

    protected boolean isOriginUpperLeft() {
        return this.getAccelType() != Effect.AccelType.OPENGL;
    }
}

