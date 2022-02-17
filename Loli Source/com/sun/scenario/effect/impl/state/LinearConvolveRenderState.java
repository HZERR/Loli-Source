/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.state;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.Rectangle;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import java.nio.FloatBuffer;
import java.security.AccessController;

public abstract class LinearConvolveRenderState
implements RenderState {
    public static final int MAX_COMPILED_KERNEL_SIZE = 128;
    public static final int MAX_KERNEL_SIZE;
    static final float MIN_EFFECT_RADIUS = 0.00390625f;
    static final float[] BLACK_COMPONENTS;

    public static int getPeerSize(int n2) {
        if (n2 < 32) {
            return n2 + 3 & 0xFFFFFFFC;
        }
        if (n2 <= MAX_KERNEL_SIZE) {
            return n2 + 31 & 0xFFFFFFE0;
        }
        throw new RuntimeException("No peer available for kernel size: " + n2);
    }

    static boolean nearZero(float f2, int n2) {
        return (double)Math.abs(f2 * (float)n2) < 0.001953125;
    }

    static boolean nearOne(float f2, int n2) {
        return (double)Math.abs(f2 * (float)n2 - (float)n2) < 0.001953125;
    }

    public abstract boolean isShadow();

    public abstract Color4f getShadowColor();

    public abstract int getInputKernelSize(int var1);

    public abstract boolean isNop();

    public abstract ImageData validatePassInput(ImageData var1, int var2);

    public abstract boolean isPassNop();

    public EffectPeer<? extends LinearConvolveRenderState> getPassPeer(Renderer renderer, FilterContext filterContext) {
        if (this.isPassNop()) {
            return null;
        }
        int n2 = this.getPassKernelSize();
        int n3 = LinearConvolveRenderState.getPeerSize(n2);
        String string = this.isShadow() ? "LinearConvolveShadow" : "LinearConvolve";
        return renderer.getPeerInstance(filterContext, string, n3);
    }

    public abstract Rectangle getPassResultBounds(Rectangle var1, Rectangle var2);

    public PassType getPassType() {
        return PassType.GENERAL_VECTOR;
    }

    public abstract FloatBuffer getPassWeights();

    public abstract int getPassWeightsArrayLength();

    public abstract float[] getPassVector();

    public abstract float[] getPassShadowColorComponents();

    public abstract int getPassKernelSize();

    static {
        BLACK_COMPONENTS = Color4f.BLACK.getPremultipliedRGBComponents();
        int n2 = PlatformUtil.isEmbedded() ? 64 : 128;
        int n3 = AccessController.doPrivileged(() -> Integer.getInteger("decora.maxLinearConvolveKernelSize", n2));
        if (n3 > 128) {
            System.out.println("Clamping maxLinearConvolveKernelSize to 128");
            n3 = 128;
        }
        MAX_KERNEL_SIZE = n3;
    }

    public static enum PassType {
        HORIZONTAL_CENTERED,
        VERTICAL_CENTERED,
        GENERAL_VECTOR;

    }
}

