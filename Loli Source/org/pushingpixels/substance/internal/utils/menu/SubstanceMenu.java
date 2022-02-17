/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.menu;

import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JMenuItem;

public interface SubstanceMenu {
    public Font getAcceleratorFont();

    public Icon getCheckIcon();

    public Icon getArrowIcon();

    public int getDefaultTextIconGap();

    public JMenuItem getAssociatedMenuItem();
}

