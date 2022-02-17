/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.decoration;

import java.awt.Component;
import java.awt.Graphics2D;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.trait.SubstanceTrait;

public interface SubstanceDecorationPainter
extends SubstanceTrait {
    public void paintDecorationArea(Graphics2D var1, Component var2, DecorationAreaType var3, int var4, int var5, SubstanceSkin var6);
}

