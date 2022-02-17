/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.scroll;

import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.plaf.UIResource;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.internal.utils.Sideable;
import org.pushingpixels.substance.internal.utils.SubstanceInternalArrowButton;

public class SubstanceScrollButton
extends JButton
implements UIResource,
Sideable,
SubstanceInternalArrowButton {
    private int orientation;

    public SubstanceScrollButton(int orientation) {
        this.setRequestFocusEnabled(false);
        this.setMargin(new Insets(0, 0, 0, 2));
        this.orientation = orientation;
    }

    @Override
    public boolean isFocusable() {
        return false;
    }

    @Override
    public SubstanceConstants.Side getSide() {
        switch (this.orientation) {
            case 1: {
                return SubstanceConstants.Side.BOTTOM;
            }
            case 7: {
                return SubstanceConstants.Side.RIGHT;
            }
            case 5: {
                return SubstanceConstants.Side.TOP;
            }
            case 3: {
                return SubstanceConstants.Side.LEFT;
            }
        }
        return null;
    }

    static {
        AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.GHOSTING_BUTTON_PRESS, SubstanceScrollButton.class);
        AnimationConfigurationManager.getInstance().disallowAnimations(AnimationFacet.GHOSTING_ICON_ROLLOVER, SubstanceScrollButton.class);
    }
}

