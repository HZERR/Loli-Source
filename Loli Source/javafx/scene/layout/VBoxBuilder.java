/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.layout;

import javafx.geometry.Pos;
import javafx.scene.layout.PaneBuilder;
import javafx.scene.layout.VBox;

@Deprecated
public class VBoxBuilder<B extends VBoxBuilder<B>>
extends PaneBuilder<B> {
    private int __set;
    private Pos alignment;
    private boolean fillWidth;
    private double spacing;

    protected VBoxBuilder() {
    }

    public static VBoxBuilder<?> create() {
        return new VBoxBuilder();
    }

    public void applyTo(VBox vBox) {
        super.applyTo(vBox);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            vBox.setAlignment(this.alignment);
        }
        if ((n2 & 2) != 0) {
            vBox.setFillWidth(this.fillWidth);
        }
        if ((n2 & 4) != 0) {
            vBox.setSpacing(this.spacing);
        }
    }

    public B alignment(Pos pos) {
        this.alignment = pos;
        this.__set |= 1;
        return (B)this;
    }

    public B fillWidth(boolean bl) {
        this.fillWidth = bl;
        this.__set |= 2;
        return (B)this;
    }

    public B spacing(double d2) {
        this.spacing = d2;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public VBox build() {
        VBox vBox = new VBox();
        this.applyTo(vBox);
        return vBox;
    }
}

