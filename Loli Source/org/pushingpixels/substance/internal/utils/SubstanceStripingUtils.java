/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Color;
import javax.swing.JComponent;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;

public class SubstanceStripingUtils {
    private static final String ODD_COLOR = "substancelaf.internal.stripingColor.odd";
    private static final String EVEN_COLOR = "substancelaf.internal.stripingColor.even";

    public static void setup(JComponent comp) {
        comp.putClientProperty(EVEN_COLOR, SubstanceColorUtilities.getStripedBackground(comp, 0));
        comp.putClientProperty(ODD_COLOR, SubstanceColorUtilities.getStripedBackground(comp, 1));
    }

    public static void tearDown(JComponent comp) {
        comp.putClientProperty(EVEN_COLOR, null);
        comp.putClientProperty(ODD_COLOR, null);
    }

    public static void applyStripedBackground(JComponent component, int rowIndex, JComponent renderer) {
        Color backgr = (Color)component.getClientProperty(rowIndex % 2 == 0 ? EVEN_COLOR : ODD_COLOR);
        if (backgr == null) {
            return;
        }
        renderer.setBackground(backgr);
    }
}

