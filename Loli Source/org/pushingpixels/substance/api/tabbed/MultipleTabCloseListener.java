/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.tabbed;

import java.awt.Component;
import java.util.Set;
import javax.swing.JTabbedPane;
import org.pushingpixels.substance.api.tabbed.BaseTabCloseListener;

public interface MultipleTabCloseListener
extends BaseTabCloseListener {
    public void tabsClosing(JTabbedPane var1, Set<Component> var2);

    public void tabsClosed(JTabbedPane var1, Set<Component> var2);
}

