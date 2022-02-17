/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.tabbed;

import java.awt.Component;
import javax.swing.JTabbedPane;
import org.pushingpixels.substance.api.tabbed.BaseTabCloseListener;

public interface TabCloseListener
extends BaseTabCloseListener {
    public void tabClosing(JTabbedPane var1, Component var2);

    public void tabClosed(JTabbedPane var1, Component var2);
}

