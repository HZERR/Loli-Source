/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.shaper;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Shape;
import javax.swing.AbstractButton;
import javax.swing.border.Border;
import org.pushingpixels.substance.api.trait.SubstanceTrait;

public interface SubstanceButtonShaper
extends SubstanceTrait {
    @Override
    public String getDisplayName();

    public Shape getButtonOutline(AbstractButton var1, Insets var2, int var3, int var4, boolean var5);

    public Border getButtonBorder(AbstractButton var1);

    public Dimension getPreferredSize(AbstractButton var1, Dimension var2);

    public boolean isProportionate();
}

