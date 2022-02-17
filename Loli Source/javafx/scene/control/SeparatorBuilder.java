/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;
import javafx.scene.control.ControlBuilder;
import javafx.scene.control.Separator;
import javafx.util.Builder;

@Deprecated
public class SeparatorBuilder<B extends SeparatorBuilder<B>>
extends ControlBuilder<B>
implements Builder<Separator> {
    private int __set;
    private HPos halignment;
    private Orientation orientation;
    private VPos valignment;

    protected SeparatorBuilder() {
    }

    public static SeparatorBuilder<?> create() {
        return new SeparatorBuilder();
    }

    public void applyTo(Separator separator) {
        super.applyTo(separator);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            separator.setHalignment(this.halignment);
        }
        if ((n2 & 2) != 0) {
            separator.setOrientation(this.orientation);
        }
        if ((n2 & 4) != 0) {
            separator.setValignment(this.valignment);
        }
    }

    public B halignment(HPos hPos) {
        this.halignment = hPos;
        this.__set |= 1;
        return (B)this;
    }

    public B orientation(Orientation orientation) {
        this.orientation = orientation;
        this.__set |= 2;
        return (B)this;
    }

    public B valignment(VPos vPos) {
        this.valignment = vPos;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public Separator build() {
        Separator separator = new Separator();
        this.applyTo(separator);
        return separator;
    }
}

