/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import javax.accessibility.AccessibleContext;
import javax.swing.JButton;
import javax.swing.UIManager;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.substance.internal.utils.SubstanceInternalButton;

public class SubstanceTitleButton
extends JButton
implements SubstanceInternalButton {
    private String uiKey;

    public SubstanceTitleButton(String uiKey) {
        this.uiKey = uiKey;
        this.setOpaque(false);
    }

    @Override
    public boolean isOpaque() {
        return false;
    }

    public SubstanceTitleButton() {
        this("");
    }

    @Override
    public boolean isFocusTraversable() {
        return false;
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public AccessibleContext getAccessibleContext() {
        AccessibleContext ac = super.getAccessibleContext();
        if (this.uiKey != null) {
            ac.setAccessibleName(UIManager.getString(this.uiKey));
            this.uiKey = null;
        }
        return ac;
    }

    static {
        AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.GHOSTING_BUTTON_PRESS, SubstanceTitleButton.class);
        AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.GHOSTING_ICON_ROLLOVER, SubstanceTitleButton.class);
    }
}

