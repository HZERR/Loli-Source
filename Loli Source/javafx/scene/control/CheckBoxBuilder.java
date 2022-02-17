/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.control.ButtonBaseBuilder;
import javafx.scene.control.CheckBox;
import javafx.util.Builder;

@Deprecated
public class CheckBoxBuilder<B extends CheckBoxBuilder<B>>
extends ButtonBaseBuilder<B>
implements Builder<CheckBox> {
    private int __set;
    private boolean allowIndeterminate;
    private boolean indeterminate;
    private boolean selected;

    protected CheckBoxBuilder() {
    }

    public static CheckBoxBuilder<?> create() {
        return new CheckBoxBuilder();
    }

    public void applyTo(CheckBox checkBox) {
        super.applyTo(checkBox);
        int n2 = this.__set;
        if ((n2 & 1) != 0) {
            checkBox.setAllowIndeterminate(this.allowIndeterminate);
        }
        if ((n2 & 2) != 0) {
            checkBox.setIndeterminate(this.indeterminate);
        }
        if ((n2 & 4) != 0) {
            checkBox.setSelected(this.selected);
        }
    }

    public B allowIndeterminate(boolean bl) {
        this.allowIndeterminate = bl;
        this.__set |= 1;
        return (B)this;
    }

    public B indeterminate(boolean bl) {
        this.indeterminate = bl;
        this.__set |= 2;
        return (B)this;
    }

    public B selected(boolean bl) {
        this.selected = bl;
        this.__set |= 4;
        return (B)this;
    }

    @Override
    public CheckBox build() {
        CheckBox checkBox = new CheckBox();
        this.applyTo(checkBox);
        return checkBox;
    }
}

