/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.tabbed;

import java.awt.Component;
import java.util.Set;
import javax.swing.JTabbedPane;
import org.pushingpixels.substance.api.tabbed.MultipleTabCloseListener;

public interface VetoableMultipleTabCloseListener
extends MultipleTabCloseListener {
    public boolean vetoTabsClosing(JTabbedPane var1, Set<Component> var2);
}

