/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.JTextComponent;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.menu.MenuSearchWidget;
import org.pushingpixels.lafwidget.utils.LafConstants;

public class LafWidgetSupport {
    public JComponent getComponentForHover(JInternalFrame.JDesktopIcon desktopIcon) {
        return desktopIcon;
    }

    public boolean toInstallMenuSearch(JMenuBar menuBar) {
        return MenuSearchWidget.getMenuItemCount(menuBar) > 40;
    }

    public boolean toInstallExtraElements(Component comp) {
        return true;
    }

    public Icon getSearchIcon(int dimension, ComponentOrientation componentOrientation) {
        return LafWidgetUtilities.getSearchIcon(dimension, componentOrientation.isLeftToRight());
    }

    public Icon getNumberIcon(int number) {
        return LafWidgetUtilities.getHexaMarker(number);
    }

    public void markButtonAsFlat(AbstractButton button) {
    }

    public int getRolloverTabIndex(JTabbedPane tabbedPane) {
        TabbedPaneUI ui = tabbedPane.getUI();
        if (ui instanceof BasicTabbedPaneUI) {
            try {
                for (Class<?> clazz = ui.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                    try {
                        Method mtd = clazz.getDeclaredMethod("getRolloverTab", new Class[0]);
                        if (mtd == null) continue;
                        mtd.setAccessible(true);
                        int result = (Integer)mtd.invoke(ui, new Object[0]);
                        return result;
                    }
                    catch (NoSuchMethodException nsme) {
                        // empty catch block
                    }
                }
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        throw new UnsupportedOperationException();
    }

    public void setTabAreaInsets(JTabbedPane tabbedPane, Insets tabAreaInsets) {
        Insets old = this.getTabAreaInsets(tabbedPane);
        TabbedPaneUI ui = tabbedPane.getUI();
        if (ui instanceof BasicTabbedPaneUI) {
            try {
                for (Class<?> clazz = ui.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                    try {
                        Field fld = clazz.getDeclaredField("tabAreaInsets");
                        if (fld == null) continue;
                        fld.setAccessible(true);
                        fld.set(ui, tabAreaInsets);
                        LafWidgetUtilities.firePropertyChangeEvent(tabbedPane, "tabAreaInsets", old, tabAreaInsets);
                        return;
                    }
                    catch (NoSuchFieldException nsfe) {
                        // empty catch block
                    }
                }
            }
            catch (Throwable t2) {
                // empty catch block
            }
        }
        throw new UnsupportedOperationException();
    }

    public Insets getTabAreaInsets(JTabbedPane tabbedPane) {
        Insets result;
        TabbedPaneUI ui = tabbedPane.getUI();
        if (ui instanceof BasicTabbedPaneUI) {
            try {
                for (Class<?> clazz = ui.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                    try {
                        Field fld = clazz.getDeclaredField("tabAreaInsets");
                        if (fld == null) continue;
                        fld.setAccessible(true);
                        Insets result2 = (Insets)fld.get(ui);
                        return result2;
                    }
                    catch (NoSuchFieldException nsfe) {
                        // empty catch block
                    }
                }
            }
            catch (Throwable t2) {
                // empty catch block
            }
        }
        if ((result = UIManager.getInsets("TabbedPane.tabAreaInsets")) == null) {
            result = new Insets(0, 0, 0, 0);
        }
        return result;
    }

    public Rectangle getTabRectangle(JTabbedPane tabbedPane, int tabIndex) {
        TabbedPaneUI ui = tabbedPane.getUI();
        if (ui instanceof BasicTabbedPaneUI) {
            try {
                for (Class<?> clazz = ui.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
                    try {
                        Field fld = clazz.getDeclaredField("rects");
                        if (fld == null) continue;
                        fld.setAccessible(true);
                        Rectangle[] rects = (Rectangle[])fld.get(ui);
                        return rects[tabIndex];
                    }
                    catch (NoSuchFieldException nsfe) {
                        // empty catch block
                    }
                }
            }
            catch (Throwable t2) {
                // empty catch block
            }
        }
        throw new UnsupportedOperationException();
    }

    public void paintPasswordStrengthMarker(Graphics g2, int x2, int y2, int width, int height, LafConstants.PasswordStrength pStrength) {
        Graphics2D g22 = (Graphics2D)g2.create();
        if (pStrength == LafConstants.PasswordStrength.WEAK) {
            g22.setColor(Color.orange);
        }
        if (pStrength == LafConstants.PasswordStrength.MEDIUM) {
            g22.setColor(Color.yellow);
        }
        if (pStrength == LafConstants.PasswordStrength.STRONG) {
            g22.setColor(Color.green);
        }
        g22.fillRect(x2, y2, width, height);
        g22.dispose();
    }

    public boolean hasLockIcon(Component comp) {
        boolean isEditableTextComponent;
        boolean bl = isEditableTextComponent = comp instanceof JTextComponent ? ((JTextComponent)comp).isEditable() : false;
        if (comp instanceof JComponent) {
            if (!isEditableTextComponent && Boolean.TRUE.equals(((JComponent)comp).getClientProperty("lafwidgets.hasLockIcon"))) {
                return true;
            }
            if (Boolean.FALSE.equals(((JComponent)comp).getClientProperty("lafwidgets.hasLockIcon"))) {
                return false;
            }
        }
        return !isEditableTextComponent && Boolean.TRUE.equals(UIManager.get("lafwidgets.hasLockIcon"));
    }

    public Icon getLockIcon(Component c2) {
        return LafWidgetUtilities.getSmallLockIcon();
    }

    public Icon getArrowIcon(int orientation) {
        return null;
    }

    public int getLookupIconSize() {
        return 14;
    }

    public int getLookupButtonSize() {
        return 16;
    }
}

