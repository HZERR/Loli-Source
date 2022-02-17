/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.ParentBuilder;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.control.Tooltip;

@Deprecated
public abstract class ControlBuilder<B extends ControlBuilder<B>>
extends ParentBuilder<B> {
    private int __set;
    private ContextMenu contextMenu;
    private double maxHeight;
    private double maxWidth;
    private double minHeight;
    private double minWidth;
    private double prefHeight;
    private double prefWidth;
    private Skin<?> skin;
    private Tooltip tooltip;

    protected ControlBuilder() {
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Control control) {
        super.applyTo(control);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    control.setContextMenu(this.contextMenu);
                    break;
                }
                case 1: {
                    control.setMaxHeight(this.maxHeight);
                    break;
                }
                case 2: {
                    control.setMaxWidth(this.maxWidth);
                    break;
                }
                case 3: {
                    control.setMinHeight(this.minHeight);
                    break;
                }
                case 4: {
                    control.setMinWidth(this.minWidth);
                    break;
                }
                case 5: {
                    control.setPrefHeight(this.prefHeight);
                    break;
                }
                case 6: {
                    control.setPrefWidth(this.prefWidth);
                    break;
                }
                case 7: {
                    control.setSkin(this.skin);
                    break;
                }
                case 8: {
                    control.setTooltip(this.tooltip);
                }
            }
        }
    }

    public B contextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
        this.__set(0);
        return (B)this;
    }

    public B maxHeight(double d2) {
        this.maxHeight = d2;
        this.__set(1);
        return (B)this;
    }

    public B maxWidth(double d2) {
        this.maxWidth = d2;
        this.__set(2);
        return (B)this;
    }

    public B minHeight(double d2) {
        this.minHeight = d2;
        this.__set(3);
        return (B)this;
    }

    public B minWidth(double d2) {
        this.minWidth = d2;
        this.__set(4);
        return (B)this;
    }

    public B prefHeight(double d2) {
        this.prefHeight = d2;
        this.__set(5);
        return (B)this;
    }

    public B prefWidth(double d2) {
        this.prefWidth = d2;
        this.__set(6);
        return (B)this;
    }

    public B skin(Skin<?> skin) {
        this.skin = skin;
        this.__set(7);
        return (B)this;
    }

    public B tooltip(Tooltip tooltip) {
        this.tooltip = tooltip;
        this.__set(8);
        return (B)this;
    }
}

