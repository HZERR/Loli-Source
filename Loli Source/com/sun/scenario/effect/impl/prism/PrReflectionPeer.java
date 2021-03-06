/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Reflection;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import com.sun.scenario.effect.impl.state.RenderState;

public class PrReflectionPeer
extends EffectPeer {
    public PrReflectionPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        FilterContext filterContext = this.getFilterContext();
        Reflection reflection = (Reflection)effect;
        Rectangle rectangle2 = arrimageData[0].getUntransformedBounds();
        int n2 = rectangle2.width;
        int n3 = rectangle2.height;
        float f2 = (float)n3 + reflection.getTopOffset();
        float f3 = reflection.getFraction() * (float)n3;
        int n4 = (int)Math.floor(f2);
        int n5 = (int)Math.ceil(f2 + f3);
        int n6 = n5 - n4;
        int n7 = n5 > n3 ? n5 : n3;
        PrDrawable prDrawable = (PrDrawable)this.getRenderer().getCompatibleImage(n2, n7);
        if (!arrimageData[0].validate(filterContext) || prDrawable == null) {
            return new ImageData(filterContext, null, rectangle2);
        }
        PrDrawable prDrawable2 = (PrDrawable)arrimageData[0].getUntransformedImage();
        Object t2 = prDrawable2.getTextureObject();
        Graphics graphics = prDrawable.createGraphics();
        graphics.transform(arrimageData[0].getTransform());
        float f4 = 0.0f;
        float f5 = n3 - n6;
        float f6 = n2;
        float f7 = n3;
        graphics.drawTextureVO((Texture)t2, reflection.getBottomOpacity(), reflection.getTopOpacity(), 0.0f, n5, n2, n4, f4, f5, f6, f7);
        graphics.drawTexture((Texture)t2, 0.0f, 0.0f, n2, n3);
        Rectangle rectangle3 = new Rectangle(rectangle2.x, rectangle2.y, n2, n7);
        return new ImageData(filterContext, prDrawable, rectangle3);
    }
}

