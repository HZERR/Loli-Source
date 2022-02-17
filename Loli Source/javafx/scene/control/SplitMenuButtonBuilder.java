/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.MenuButtonBuilder;
import javafx.scene.control.SplitMenuButton;

@Deprecated
public class SplitMenuButtonBuilder<B extends SplitMenuButtonBuilder<B>>
extends MenuButtonBuilder<B> {
    protected SplitMenuButtonBuilder() {
    }

    public static SplitMenuButtonBuilder<?> create() {
        return new SplitMenuButtonBuilder();
    }

    @Override
    public SplitMenuButton build() {
        SplitMenuButton splitMenuButton = new SplitMenuButton();
        this.applyTo(splitMenuButton);
        return splitMenuButton;
    }
}

