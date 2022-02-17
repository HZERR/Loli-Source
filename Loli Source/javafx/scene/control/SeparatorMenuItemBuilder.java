/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.CustomMenuItemBuilder;
import javafx.scene.control.SeparatorMenuItem;

@Deprecated
public class SeparatorMenuItemBuilder<B extends SeparatorMenuItemBuilder<B>>
extends CustomMenuItemBuilder<B> {
    protected SeparatorMenuItemBuilder() {
    }

    public static SeparatorMenuItemBuilder<?> create() {
        return new SeparatorMenuItemBuilder();
    }

    @Override
    public SeparatorMenuItem build() {
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        this.applyTo(separatorMenuItem);
        return separatorMenuItem;
    }
}

