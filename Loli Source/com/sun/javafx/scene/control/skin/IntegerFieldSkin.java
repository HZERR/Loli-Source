/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.skin.InputFieldSkin;
import com.sun.javafx.scene.control.skin.IntegerField;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.Node;

public class IntegerFieldSkin
extends InputFieldSkin {
    private InvalidationListener integerFieldValueListener = observable -> this.updateText();

    public IntegerFieldSkin(IntegerField integerField) {
        super(integerField);
        integerField.valueProperty().addListener(this.integerFieldValueListener);
    }

    @Override
    public IntegerField getSkinnable() {
        return (IntegerField)this.control;
    }

    @Override
    public Node getNode() {
        return this.getTextField();
    }

    @Override
    public void dispose() {
        ((IntegerField)this.control).valueProperty().removeListener(this.integerFieldValueListener);
        super.dispose();
    }

    @Override
    protected boolean accept(String string) {
        if (string.length() == 0) {
            return true;
        }
        if (string.matches("[0-9]*")) {
            try {
                Integer.parseInt(string);
                int n2 = Integer.parseInt(string);
                int n3 = ((IntegerField)this.control).getMaxValue();
                return n3 != -1 ? n2 <= n3 : true;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
        return false;
    }

    @Override
    protected void updateText() {
        this.getTextField().setText("" + ((IntegerField)this.control).getValue());
    }

    @Override
    protected void updateValue() {
        int n2 = ((IntegerField)this.control).getValue();
        String string = this.getTextField().getText() == null ? "" : this.getTextField().getText().trim();
        try {
            int n3 = Integer.parseInt(string);
            if (n3 != n2) {
                ((IntegerField)this.control).setValue(n3);
            }
        }
        catch (NumberFormatException numberFormatException) {
            ((IntegerField)this.control).setValue(0);
            Platform.runLater(() -> this.getTextField().positionCaret(1));
        }
    }
}

