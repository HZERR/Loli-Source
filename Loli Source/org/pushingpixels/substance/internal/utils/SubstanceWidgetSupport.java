/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.utils.LafConstants;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.ui.SubstanceDesktopIconUI;
import org.pushingpixels.substance.internal.ui.SubstanceTabbedPaneUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTitlePane;
import org.pushingpixels.substance.internal.utils.SubstanceWidgetManager;

public class SubstanceWidgetSupport
extends LafWidgetSupport {
    @Override
    public JComponent getComponentForHover(JInternalFrame.JDesktopIcon desktopIcon) {
        SubstanceDesktopIconUI ui = (SubstanceDesktopIconUI)desktopIcon.getUI();
        return ui.getComponentForHover();
    }

    @Override
    public boolean toInstallMenuSearch(JMenuBar menuBar) {
        if (!SubstanceWidgetManager.getInstance().isAllowed(SwingUtilities.getRootPane(menuBar), SubstanceConstants.SubstanceWidgetType.MENU_SEARCH)) {
            return false;
        }
        if (menuBar instanceof SubstanceTitlePane.SubstanceMenuBar) {
            return false;
        }
        return super.toInstallMenuSearch(menuBar);
    }

    @Override
    public Icon getSearchIcon(int dimension, ComponentOrientation componentOrientation) {
        return SubstanceImageCreator.getSearchIcon(dimension, SubstanceColorSchemeUtilities.getColorScheme(null, ComponentState.DEFAULT), componentOrientation.isLeftToRight());
    }

    @Override
    public Icon getArrowIcon(int orientation) {
        return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getControlFontSize(), orientation, SubstanceColorSchemeUtilities.getColorScheme(null, ComponentState.DEFAULT));
    }

    @Override
    public Icon getNumberIcon(int number) {
        SubstanceColorScheme colorScheme = SubstanceLookAndFeel.getCurrentSkin(null).getActiveColorScheme(DecorationAreaType.HEADER);
        return SubstanceImageCreator.getHexaMarker(number, colorScheme);
    }

    @Override
    public void markButtonAsFlat(AbstractButton button) {
        button.putClientProperty("substancelaf.componentFlat", Boolean.TRUE);
        button.setOpaque(false);
    }

    @Override
    public int getRolloverTabIndex(JTabbedPane tabbedPane) {
        SubstanceTabbedPaneUI ui = (SubstanceTabbedPaneUI)tabbedPane.getUI();
        return ui.getRolloverTabIndex();
    }

    @Override
    public void setTabAreaInsets(JTabbedPane tabbedPane, Insets tabAreaInsets) {
        SubstanceTabbedPaneUI ui = (SubstanceTabbedPaneUI)tabbedPane.getUI();
        ui.setTabAreaInsets(tabAreaInsets);
    }

    @Override
    public Insets getTabAreaInsets(JTabbedPane tabbedPane) {
        SubstanceTabbedPaneUI ui = (SubstanceTabbedPaneUI)tabbedPane.getUI();
        return ui.getTabAreaInsets();
    }

    @Override
    public Rectangle getTabRectangle(JTabbedPane tabbedPane, int tabIndex) {
        SubstanceTabbedPaneUI ui = (SubstanceTabbedPaneUI)tabbedPane.getUI();
        return ui.getTabRectangle(tabIndex);
    }

    @Override
    public void paintPasswordStrengthMarker(Graphics g2, int x2, int y2, int width, int height, LafConstants.PasswordStrength pStrength) {
        Graphics2D g22 = (Graphics2D)g2.create();
        SubstanceColorScheme colorScheme = null;
        if (pStrength == LafConstants.PasswordStrength.WEAK) {
            colorScheme = SubstanceColorSchemeUtilities.ORANGE;
        }
        if (pStrength == LafConstants.PasswordStrength.MEDIUM) {
            colorScheme = SubstanceColorSchemeUtilities.YELLOW;
        }
        if (pStrength == LafConstants.PasswordStrength.STRONG) {
            colorScheme = SubstanceColorSchemeUtilities.GREEN;
        }
        if (colorScheme != null) {
            SubstanceImageCreator.paintRectangularBackground(null, g2, x2, y2, width, height, colorScheme, 0.5f, false);
        }
        g22.dispose();
    }

    @Override
    public boolean hasLockIcon(Component comp) {
        if (!SubstanceCoreUtilities.toShowExtraWidgets(comp)) {
            return false;
        }
        return super.hasLockIcon(comp);
    }

    @Override
    public Icon getLockIcon(Component c2) {
        return SubstanceImageCreator.makeTransparent(null, SubstanceImageCreator.getSmallLockIcon(SubstanceColorSchemeUtilities.getColorScheme(null, ComponentState.ENABLED), c2), 0.3);
    }

    @Override
    public boolean toInstallExtraElements(Component comp) {
        return SubstanceCoreUtilities.toShowExtraWidgets(comp);
    }

    @Override
    public int getLookupIconSize() {
        int result = 2 + SubstanceSizeUtils.getControlFontSize();
        if (result % 2 != 0) {
            ++result;
        }
        return result;
    }

    @Override
    public int getLookupButtonSize() {
        return 4 + SubstanceSizeUtils.getControlFontSize();
    }
}

