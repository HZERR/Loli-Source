/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.combo;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import javax.swing.JComboBox;
import javax.swing.JScrollBar;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboPopup;
import org.pushingpixels.substance.internal.ui.SubstanceListUI;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.border.SubstanceBorder;

public class SubstanceComboPopup
extends BasicComboPopup {
    public SubstanceComboPopup(JComboBox combo) {
        super(combo);
        this.setOpaque(true);
        this.list.setBackground(combo.getBackground());
    }

    @Override
    protected void configurePopup() {
        super.configurePopup();
        this.setBorder(new SubstanceBorder(new Insets(0, 2, 2, 2)));
    }

    private void setListSelection(int selectedIndex) {
        if (selectedIndex == -1) {
            this.list.clearSelection();
        } else {
            this.list.setSelectedIndex(selectedIndex);
            this.list.ensureIndexIsVisible(selectedIndex);
        }
    }

    private Point getPopupLocation() {
        Dimension popupSize = this.comboBox.getSize();
        Insets insets = this.getInsets();
        popupSize.setSize(popupSize.width - (insets.right + insets.left), this.getPopupHeightForRowCount(this.comboBox.getMaximumRowCount()));
        Rectangle popupBounds = this.computePopupBounds(0, this.comboBox.getBounds().height, popupSize.width, popupSize.height);
        Dimension scrollSize = popupBounds.getSize();
        Point popupLocation = popupBounds.getLocation();
        this.scroller.setMaximumSize(scrollSize);
        this.scroller.setPreferredSize(scrollSize);
        this.scroller.setMinimumSize(scrollSize);
        this.list.revalidate();
        return new Point(popupLocation.x, popupLocation.y);
    }

    @Override
    protected Rectangle computePopupBounds(int px, int py, int pw, int ph) {
        Object popupPrototypeDisplayValue;
        Rectangle screenBounds;
        int popupFlyoutOrientation = SubstanceCoreUtilities.getPopupFlyoutOrientation(this.comboBox);
        Insets insets = this.getInsets();
        int dx = 0;
        int dy = 0;
        switch (popupFlyoutOrientation) {
            case 1: {
                dy = -ph - (int)this.comboBox.getSize().getHeight() - insets.top - insets.bottom;
                break;
            }
            case 0: {
                dy = -ph / 2 - (int)this.comboBox.getSize().getHeight() / 2 - insets.top / 2 - insets.bottom / 2;
                break;
            }
            case 3: {
                dx = pw + insets.left + insets.right;
                dy = -((int)this.comboBox.getSize().getHeight());
                break;
            }
            case 7: {
                dx = -pw - insets.left - insets.right;
                dy = -((int)this.comboBox.getSize().getHeight());
            }
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        GraphicsConfiguration gc = this.comboBox.getGraphicsConfiguration();
        Point p2 = new Point();
        SwingUtilities.convertPointFromScreen(p2, this.comboBox);
        if (gc != null) {
            Insets screenInsets = toolkit.getScreenInsets(gc);
            screenBounds = gc.getBounds();
            screenBounds.width -= screenInsets.left + screenInsets.right;
            screenBounds.height -= screenInsets.top + screenInsets.bottom;
            screenBounds.x += p2.x + screenInsets.left;
            screenBounds.y += p2.y + screenInsets.top;
        } else {
            screenBounds = new Rectangle(p2, toolkit.getScreenSize());
        }
        Rectangle rect = new Rectangle(px + dx, py + dy, pw, ph);
        if (py + ph > screenBounds.y + screenBounds.height && ph < screenBounds.height) {
            rect.y = -rect.height - insets.top - insets.bottom;
        }
        if ((popupPrototypeDisplayValue = SubstanceCoreUtilities.getComboPopupPrototypeDisplayValue(this.comboBox)) != null) {
            boolean hasVerticalScrollBar;
            ListCellRenderer<Object> renderer = this.list.getCellRenderer();
            Component c2 = renderer.getListCellRendererComponent(this.list, popupPrototypeDisplayValue, -1, true, true);
            int npw = c2.getPreferredSize().width;
            boolean bl = hasVerticalScrollBar = this.comboBox.getItemCount() > this.comboBox.getMaximumRowCount();
            if (hasVerticalScrollBar) {
                JScrollBar verticalBar = this.scroller.getVerticalScrollBar();
                npw += verticalBar.getPreferredSize().width;
            }
            rect.width = pw = Math.max(pw, npw);
        }
        return rect;
    }

    @Override
    public void hide() {
        super.hide();
        SubstanceListUI ui = (SubstanceListUI)this.list.getUI();
        ui.resetRolloverIndex();
    }

    public JComboBox getCombobox() {
        return this.comboBox;
    }
}

