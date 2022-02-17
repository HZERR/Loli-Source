/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.PaneBuilder;

@Deprecated
public class HBoxBuilder<B extends HBoxBuilder<B>>
extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private boolean fillHeight;
    private double spacing;

    protected HBoxBuilder() {
    }

    public static HBoxBuilder<?> create() {
        return new HBoxBuilder();
    }

    public void applyTo(HBox hBox) {
        super.applyTo(hBox);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            hBox.setAlignment(this.alignment);
        }
        if ((n2 & 2) != 0) {
            hBox.setFillHeight(this.fillHeight);
        }
        if ((n2 & 4) != 0) {
            hBox.setSpacing(this.spacing);
        }
    }

    public B alignment(Pos pos) {
        this.alignment = pos;
        this.__set |= 1;
        return (B)this;
    }

    public B fillHeight(boolean bl) {
        this.fillHeight = bl;
        this.__set |= 2;
        return (B)this;
    }

    public B spacing(double d2) {
        this.spacing = d2;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public HBox build() {
        HBox hBox = new HBox();
        this.applyTo(hBox);
        return hBox;
    }
}

