/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.D2D1_RENDER_TARGET_PROPERTIES;
import com.sun.javafx.font.directwrite.ID2D1RenderTarget;
import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.IWICBitmap;
import com.sun.javafx.font.directwrite.OS;

class ID2D1Factory
extends IUnknown {
    ID2D1Factory(long l2) {
        super(l2);
    }

    ID2D1RenderTarget CreateWicBitmapRenderTarget(IWICBitmap iWICBitmap, D2D1_RENDER_TARGET_PROPERTIES d2D1_RENDER_TARGET_PROPERTIES) {
        long l2 = OS.CreateWicBitmapRenderTarget(this.ptr, iWICBitmap.ptr, d2D1_RENDER_TARGET_PROPERTIES);
        return l2 != 0L ? new ID2D1RenderTarget(l2) : null;
    }
}

