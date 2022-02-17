/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.combo;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JList;
import org.pushingpixels.substance.api.combo.ComboPopupPrototypeCallback;
import org.pushingpixels.substance.internal.ui.SubstanceComboBoxUI;

public class WidestComboPopupPrototype
implements ComboPopupPrototypeCallback {
    @Override
    public Object getPopupPrototypeDisplayValue(JComboBox jc) {
        int maxWidth = -1;
        Object prototype = null;
        JList list = ((SubstanceComboBoxUI)jc.getUI()).getPopup().getList();
        for (int i2 = 0; i2 < jc.getModel().getSize(); ++i2) {
            int rWidth;
            Object elem = jc.getModel().getElementAt(i2);
            Component renderer = jc.getRenderer().getListCellRendererComponent(list, elem, i2, false, false);
            if (renderer == null || (rWidth = renderer.getPreferredSize().width) <= maxWidth) continue;
            maxWidth = rWidth;
            prototype = elem;
        }
        return prototype;
    }
}

