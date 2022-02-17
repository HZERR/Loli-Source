/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Shape;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.trait.SubstanceTrait;

public interface SubstanceBorderPainter
extends SubstanceTrait {
    public void paintBorder(Graphics var1, Component var2, int var3, int var4, Shape var5, Shape var6, SubstanceColorScheme var7);

    public boolean isPaintingInnerContour();
}

