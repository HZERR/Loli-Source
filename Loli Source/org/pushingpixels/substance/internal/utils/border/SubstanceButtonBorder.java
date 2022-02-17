/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.border;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;

public abstract class SubstanceButtonBorder
implements Border,
UIResource {
    private Class<?> buttonShaperClass;

    public SubstanceButtonBorder(Class<?> buttonShaperClass) {
        this.buttonShaperClass = buttonShaperClass;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c2, Graphics g2, int x2, int y2, int width, int height) {
    }

    public Class<?> getButtonShaperClass() {
        return this.buttonShaperClass;
    }
}

