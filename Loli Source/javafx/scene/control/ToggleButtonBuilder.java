/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.ButtonBaseBuilder;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Builder;

@Deprecated
public class ToggleButtonBuilder<B extends ToggleButtonBuilder<B>>
extends ButtonBaseBuilder<B>
implements Builder<ToggleButton> {
    private int __set;
    private boolean selected;
    private ToggleGroup toggleGroup;

    protected ToggleButtonBuilder() {
    }

    public static ToggleButtonBuilder<?> create() {
        return new ToggleButtonBuilder();
    }

    public void applyTo(ToggleButton toggleButton) {
        super.applyTo(toggleButton);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            toggleButton.setSelected(this.selected);
        }
        if ((n2 & 2) != 0) {
            toggleButton.setToggleGroup(this.toggleGroup);
        }
    }

    public B selected(boolean bl) {
        this.selected = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B toggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup = toggleGroup;
        this.__set |= 2;
        return (B)this;
    }

    @Override
    public ToggleButton build() {
        ToggleButton toggleButton = new ToggleButton();
        this.applyTo(toggleButton);
        return toggleButton;
    }
}

