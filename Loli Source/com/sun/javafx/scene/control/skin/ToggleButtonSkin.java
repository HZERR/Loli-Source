/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.behavior.ToggleButtonBehavior;
import com.sun.javafx.scene.control.skin.LabeledSkinBase;
import javafx.scene.control.ToggleButton;

public class ToggleButtonSkin
extends LabeledSkinBase<ToggleButton, ToggleButtonBehavior<ToggleButton>> {
    public ToggleButtonSkin(ToggleButton toggleButton) {
        super(toggleButton, new ToggleButtonBehavior<ToggleButton>(toggleButton));
    }
}

