/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.PopupControl;
import javafx.scene.control.Skin;
import javafx.stage.PopupWindowBuilder;
import javafx.util.Builder;

@Deprecated
public class PopupControlBuilder<B extends PopupControlBuilder<B>>
extends PopupWindowBuilder<B>
implements Builder<PopupControl> {
    private int __set;
    private String id;
    private double maxHeight;
    private double maxWidth;
    private double minHeight;
    private double minWidth;
    private double prefHeight;
    private double prefWidth;
    private Skin<?> skin;
    private String style;
    private Collection<? extends String> styleClass;

    protected PopupControlBuilder() {
    }

    public static PopupControlBuilder<?> create() {
        return new PopupControlBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(PopupControl popupControl) {
        super.applyTo(popupControl);
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    popupControl.setId(this.id);
                    break;
                }
                case 1: {
                    popupControl.setMaxHeight(this.maxHeight);
                    break;
                }
                case 2: {
                    popupControl.setMaxWidth(this.maxWidth);
                    break;
                }
                case 3: {
                    popupControl.setMinHeight(this.minHeight);
                    break;
                }
                case 4: {
                    popupControl.setMinWidth(this.minWidth);
                    break;
                }
                case 5: {
                    popupControl.setPrefHeight(this.prefHeight);
                    break;
                }
                case 6: {
                    popupControl.setPrefWidth(this.prefWidth);
                    break;
                }
                case 7: {
                    popupControl.setSkin(this.skin);
                    break;
                }
                case 8: {
                    popupControl.setStyle(this.style);
                    break;
                }
                case 9: {
                    popupControl.getStyleClass().addAll(this.styleClass);
                }
            }
        }
    }

    public B id(String string) {
        this.id = string;
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

    public B style(String string) {
        this.style = string;
        this.__set(8);
        return (B)this;
    }

    public B styleClass(Collection<? extends String> collection) {
        this.styleClass = collection;
        this.__set(9);
        return (B)this;
    }

    public B styleClass(String ... arrstring) {
        return this.styleClass(Arrays.asList(arrstring));
    }

    @Override
    public PopupControl build() {
        PopupControl popupControl = new PopupControl();
        this.applyTo(popupControl);
        return popupControl;
    }
}

