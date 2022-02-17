/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.painter.highlight;

import java.awt.Component;
import java.awt.Graphics2D;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.trait.SubstanceTrait;

public interface SubstanceHighlightPainter
extends SubstanceTrait {
    public void paintHighlight(Graphics2D var1, Component var2, int var3, int var4, SubstanceColorScheme var5);
}

