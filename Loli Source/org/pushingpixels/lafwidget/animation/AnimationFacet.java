/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.animation;

import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;

public final class AnimationFacet {
    protected String displayName;
    public static final AnimationFacet ARM = new AnimationFacet("lafwidgets.core.arm", true);
    public static final AnimationFacet PRESS = new AnimationFacet("lafwidgets.core.press", true);
    public static final AnimationFacet FOCUS = new AnimationFacet("lafwidgets.core.focus", true);
    public static final AnimationFacet FOCUS_LOOP_ANIMATION = new AnimationFacet("lafwidgets.core.focusLoopAnimation", false);
    public static final AnimationFacet ROLLOVER = new AnimationFacet("lafwidgets.core.rollover", true);
    public static final AnimationFacet SELECTION = new AnimationFacet("lafwidgets.core.selection", true);
    public static final AnimationFacet GHOSTING_ICON_ROLLOVER = new AnimationFacet("lafwidgets.core.ghosting.iconRollover", false);
    public static final AnimationFacet GHOSTING_BUTTON_PRESS = new AnimationFacet("lafwidgets.core.ghosting.buttonPress", false);
    public static final AnimationFacet ICON_GLOW = new AnimationFacet("lafwidgets.core.iconGlow", false);

    public AnimationFacet(String displayName, boolean isDefaultAllowed) {
        this.displayName = displayName;
        if (isDefaultAllowed) {
            AnimationConfigurationManager.getInstance().allowAnimations(this);
        }
    }

    public String toString() {
        return this.displayName;
    }
}

