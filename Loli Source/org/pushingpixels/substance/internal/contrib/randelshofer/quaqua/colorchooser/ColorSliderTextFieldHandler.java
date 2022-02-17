/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.ColorSliderModel;

public class ColorSliderTextFieldHandler
implements DocumentListener,
ChangeListener {
    private JTextField textField;
    private ColorSliderModel ccModel;
    private int component;

    public ColorSliderTextFieldHandler(JTextField textField, ColorSliderModel ccModel, int component) {
        this.textField = textField;
        this.ccModel = ccModel;
        this.component = component;
        textField.getDocument().addDocumentListener(this);
        ccModel.getBoundedRangeModel(component).addChangeListener(this);
    }

    @Override
    public void changedUpdate(DocumentEvent evt) {
        this.docChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent evt) {
        this.docChanged();
    }

    @Override
    public void insertUpdate(DocumentEvent evt) {
        this.docChanged();
    }

    private void docChanged() {
        if (this.textField.hasFocus()) {
            DefaultBoundedRangeModel brm = this.ccModel.getBoundedRangeModel(this.component);
            try {
                int value = Integer.decode(this.textField.getText());
                if (brm.getMinimum() <= value && value <= brm.getMaximum()) {
                    brm.setValue(value);
                }
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e2) {
        if (!this.textField.hasFocus()) {
            this.textField.setText(Integer.toString(this.ccModel.getBoundedRangeModel(this.component).getValue()));
        }
    }
}

