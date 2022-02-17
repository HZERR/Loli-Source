/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.scene.control.skin.RadioButtonSkin;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Skin;
import javafx.scene.control.ToggleButton;

public class RadioButton
extends ToggleButton {
    private static final String DEFAULT_STYLE_CLASS = "radio-button";

    public RadioButton() {
        this.initialize();
    }

    public RadioButton(String string) {
        this.setText(string);
        this.initialize();
    }

    private void initialize() {
        this.getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.setAccessibleRole(AccessibleRole.RADIO_BUTTON);
        ((StyleableProperty)((Object)this.alignmentProperty())).applyStyle((StyleOrigin)null, Pos.CENTER_LEFT);
    }

    @Override
    public void fire() {
        if (this.getToggleGroup() == null || !this.isSelected()) {
            super.fire();
        }
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new RadioButtonSkin(this);
    }

    @Override
    @Deprecated
    protected Pos impl_cssGetAlignmentInitialValue() {
        return Pos.CENTER_LEFT;
    }

    @Override
    public Object queryAccessibleAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        switch (accessibleAttribute) {
            case SELECTED: {
                return this.isSelected();
            }
        }
        return super.queryAccessibleAttribute(accessibleAttribute, arrobject);
    }
}

