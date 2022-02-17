/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.control;

import com.sun.javafx.scene.NodeHelper;
import com.sun.javafx.scene.control.skin.LabelSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.Skin;

public class Label
extends Labeled {
    private ChangeListener<Boolean> mnemonicStateListener = (observableValue, bl, bl2) -> this.impl_showMnemonicsProperty().setValue((Boolean)bl2);
    private ObjectProperty<Node> labelFor;

    public Label() {
        this.initialize();
    }

    public Label(String string) {
        super(string);
        this.initialize();
    }

    public Label(String string, Node node) {
        super(string, node);
        this.initialize();
    }

    private void initialize() {
        this.getStyleClass().setAll("label");
        this.setAccessibleRole(AccessibleRole.TEXT);
        ((StyleableProperty)((Object)this.focusTraversableProperty())).applyStyle(null, Boolean.FALSE);
    }

    public ObjectProperty<Node> labelForProperty() {
        if (this.labelFor == null) {
            this.labelFor = new ObjectPropertyBase<Node>(){
                Node oldValue = null;

                @Override
                protected void invalidated() {
                    Node node;
                    if (this.oldValue != null) {
                        NodeHelper.getNodeAccessor().setLabeledBy(this.oldValue, null);
                        this.oldValue.impl_showMnemonicsProperty().removeListener(Label.this.mnemonicStateListener);
                    }
                    if ((node = (Node)this.get()) != null) {
                        NodeHelper.getNodeAccessor().setLabeledBy(node, Label.this);
                        node.impl_showMnemonicsProperty().addListener(Label.this.mnemonicStateListener);
                        Label.this.impl_setShowMnemonics(node.impl_isShowMnemonics());
                    } else {
                        Label.this.impl_setShowMnemonics(false);
                    }
                    this.oldValue = node;
                }

                @Override
                public Object getBean() {
                    return Label.this;
                }

                @Override
                public String getName() {
                    return "labelFor";
                }
            };
        }
        return this.labelFor;
    }

    public final void setLabelFor(Node node) {
        this.labelForProperty().setValue(node);
    }

    public final Node getLabelFor() {
        return this.labelFor == null ? null : (Node)this.labelFor.getValue();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new LabelSkin(this);
    }

    @Override
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }
}

