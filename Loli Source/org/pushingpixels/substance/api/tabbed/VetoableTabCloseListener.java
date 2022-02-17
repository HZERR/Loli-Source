/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.tabbed;

import java.awt.Component;
import javax.swing.JTabbedPane;
import org.pushingpixels.substance.api.tabbed.TabCloseListener;

public interface VetoableTabCloseListener
extends TabCloseListener {
    public boolean vetoTabClosing(JTabbedPane var1, Component var2);
}

