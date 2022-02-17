/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.fill;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Shape;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.trait.SubstanceTrait;

public interface SubstanceFillPainter
extends SubstanceTrait {
    public void paintContourBackground(Graphics var1, Component var2, int var3, int var4, Shape var5, boolean var6, SubstanceColorScheme var7, boolean var8);
}

