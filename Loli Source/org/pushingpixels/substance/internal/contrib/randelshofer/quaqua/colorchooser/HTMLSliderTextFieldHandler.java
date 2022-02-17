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
import org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.colorchooser.HTMLColorSliderModel;

public class HTMLSliderTextFieldHandler
implements DocumentListener,
ChangeListener {
    private JTextField textField;
    private HTMLColorSliderModel ccModel;
    private int component;

    public HTMLSliderTextFieldHandler(JTextField textField, HTMLColorSliderModel ccModel, int component) {
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
                int value = Integer.decode("#" + this.textField.getText());
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
            String hex;
            int value = this.ccModel.getBoundedRangeModel(2).getValue();
            if (this.ccModel.isWebSaveOnly()) {
                value = Math.round((float)value / 51.0f) * 51;
            }
            this.textField.setText((hex = Integer.toHexString(value).toUpperCase()).length() == 1 ? "0" + hex : hex);
        }
    }
}

