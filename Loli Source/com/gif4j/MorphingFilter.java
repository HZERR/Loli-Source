/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;

public abstract class MorphingFilter {
    public static final int DEFAULT_DELAY_BETWEEN_FRAMES = 10;

    abstract GifFrame[] a(GifImage var1, GifFrame var2);
}

