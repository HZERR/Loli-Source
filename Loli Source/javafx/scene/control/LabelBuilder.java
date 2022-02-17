/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.LabeledBuilder;
import javafx.util.Builder;

@Deprecated
public class LabelBuilder<B extends LabelBuilder<B>>
extends LabeledBuilder<B>
implements Builder<Label> {
    private boolean __set;
    private Node labelFor;

    protected LabelBuilder() {
    }

    public static LabelBuilder<?> create() {
        return new LabelBuilder();
    }

    public void applyTo(Label label) {
        super.applyTo(label);
        if (this.__set) {
            label.setLabelFor(this.labelFor);
        }
    }

    public B labelFor(Node node) {
        this.labelFor = node;
        this.__set = true;
        return (B)this;
    }

    @Override
    public Label build() {
        Label label = new Label();
        this.applyTo(label);
        return label;
    }
}

