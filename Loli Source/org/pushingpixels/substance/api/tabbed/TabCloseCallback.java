/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.tabbed;

import java.awt.event.MouseEvent;
import javax.swing.JTabbedPane;
import org.pushingpixels.substance.api.SubstanceConstants;

public interface TabCloseCallback {
    public SubstanceConstants.TabCloseKind onAreaClick(JTabbedPane var1, int var2, MouseEvent var3);

    public SubstanceConstants.TabCloseKind onCloseButtonClick(JTabbedPane var1, int var2, MouseEvent var3);

    public String getAreaTooltip(JTabbedPane var1, int var2);

    public String getCloseButtonTooltip(JTabbedPane var1, int var2);
}

