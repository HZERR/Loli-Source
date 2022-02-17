/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.watermark;

import java.awt.Component;
import java.awt.Graphics;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.trait.SubstanceTrait;

public interface SubstanceWatermark
extends SubstanceTrait {
    public void drawWatermarkImage(Graphics var1, Component var2, int var3, int var4, int var5, int var6);

    public boolean updateWatermarkImage(SubstanceSkin var1);

    public void previewWatermark(Graphics var1, SubstanceSkin var2, int var3, int var4, int var5, int var6);

    public void dispose();
}

