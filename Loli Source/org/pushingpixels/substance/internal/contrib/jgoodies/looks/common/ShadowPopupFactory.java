/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.jgoodies.looks.common;

import java.awt.Component;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import org.pushingpixels.lafwidget.utils.LookUtils;
import org.pushingpixels.substance.internal.contrib.jgoodies.looks.Options;
import org.pushingpixels.substance.internal.contrib.jgoodies.looks.common.ShadowPopup;

public final class ShadowPopupFactory
extends PopupFactory {
    static final String PROP_HORIZONTAL_BACKGROUND = "jgoodies.hShadowBg";
    static final String PROP_VERTICAL_BACKGROUND = "jgoodies.vShadowBg";
    private final PopupFactory storedFactory;

    private ShadowPopupFactory(PopupFactory storedFactory) {
        this.storedFactory = storedFactory;
    }

    public static void install() {
        if (LookUtils.IS_OS_MAC) {
            return;
        }
        PopupFactory factory = PopupFactory.getSharedInstance();
        if (factory instanceof ShadowPopupFactory) {
            return;
        }
        PopupFactory.setSharedInstance(new ShadowPopupFactory(factory));
    }

    public static void uninstall() {
        PopupFactory factory = PopupFactory.getSharedInstance();
        if (!(factory instanceof ShadowPopupFactory)) {
            return;
        }
        PopupFactory stored = ((ShadowPopupFactory)factory).storedFactory;
        PopupFactory.setSharedInstance(stored);
    }

    @Override
    public Popup getPopup(Component owner, Component contents, int x2, int y2) throws IllegalArgumentException {
        Popup popup = super.getPopup(owner, contents, x2, y2);
        return Options.isPopupDropShadowActive() ? ShadowPopup.getInstance(owner, contents, x2, y2, popup) : popup;
    }
}

