/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.combo;

import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;

public class SubstanceComboBoxEditor
extends BasicComboBoxEditor {
    public SubstanceComboBoxEditor() {
        this.editor = new JTextField("", 9);
        this.editor.setBorder(null);
        this.editor.setOpaque(false);
    }

    public static class UIResource
    extends SubstanceComboBoxEditor
    implements javax.swing.plaf.UIResource {
    }
}

