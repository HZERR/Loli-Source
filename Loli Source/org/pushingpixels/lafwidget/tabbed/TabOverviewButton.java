/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.plaf.UIResource;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.tabbed.TabOverviewDialog;

public class TabOverviewButton
extends JButton
implements UIResource {
    private static final String OWN_BOUNDS = "lafwidget.ownBounds";

    public TabOverviewButton(final JTabbedPane tabPane) {
        this.setFocusable(false);
        LafWidgetSupport support = LafWidgetRepository.getRepository().getLafSupport();
        if (support != null) {
            Icon searchIcon = support.getSearchIcon(LafWidgetRepository.getRepository().getLafSupport().getLookupIconSize(), tabPane.getComponentOrientation());
            this.setIcon(searchIcon);
            support.markButtonAsFlat(this);
        }
        this.setToolTipText(LafWidgetUtilities.getResourceBundle(tabPane).getString("TabbedPane.overviewButtonTooltip"));
        this.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e2) {
                TabOverviewDialog.getOverviewDialog(tabPane).setVisible(true);
            }
        });
    }

    @Override
    public void setBounds(int x2, int y2, int width, int height) {
        if (Boolean.TRUE.equals(this.getClientProperty(OWN_BOUNDS))) {
            super.setBounds(x2, y2, width, height);
        }
    }

    public void updateLocation(JTabbedPane tabbedPane, Insets tabAreaInsets) {
        if (tabbedPane == null) {
            return;
        }
        this.putClientProperty(OWN_BOUNDS, Boolean.TRUE);
        int buttonSize = LafWidgetRepository.getRepository().getLafSupport().getLookupButtonSize();
        switch (tabbedPane.getTabPlacement()) {
            case 1: {
                if (tabbedPane.getComponentOrientation().isLeftToRight()) {
                    this.setBounds(2, tabAreaInsets.top, buttonSize, buttonSize);
                    break;
                }
                this.setBounds(tabbedPane.getBounds().width - tabAreaInsets.right - buttonSize - 2, tabAreaInsets.top, buttonSize, buttonSize);
                break;
            }
            case 3: {
                if (tabbedPane.getComponentOrientation().isLeftToRight()) {
                    this.setBounds(2, tabbedPane.getBounds().height - tabAreaInsets.bottom - buttonSize - 4, buttonSize, buttonSize);
                    break;
                }
                this.setBounds(tabbedPane.getBounds().width - tabAreaInsets.right - buttonSize - 2, tabbedPane.getBounds().height - tabAreaInsets.bottom - buttonSize - 4, buttonSize, buttonSize);
                break;
            }
            case 2: {
                this.setBounds(2, tabAreaInsets.top - 1, buttonSize, buttonSize);
                break;
            }
            case 4: {
                this.setBounds(tabbedPane.getBounds().width - tabAreaInsets.right - buttonSize - 2, tabAreaInsets.top - 1, buttonSize, buttonSize);
            }
        }
        this.putClientProperty(OWN_BOUNDS, null);
    }
}

