/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils.menu;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import javax.swing.text.View;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.menu.SubstanceMenu;
import org.pushingpixels.substance.internal.utils.menu.SubstanceMenuBackgroundDelegate;

public class MenuUtilities {
    private static final String LAYOUT_METRICS = "substancelaf.internal.menus.layoutMetrics";
    private static final String GUTTER_X = "substancelaf.internal.menus.gutterX";
    public static final String LAYOUT_INFO = "substancelaf.internal.menus.layoutInfo";

    public static MenuLayoutInfo getMenuLayoutInfo(boolean forPainting, JMenuItem menuItem, Font acceleratorFont, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
        Insets i2 = menuItem.getInsets();
        Rectangle iconRect = new Rectangle(0, 0, 0, 0);
        Rectangle textRect = new Rectangle(0, 0, 0, 0);
        Rectangle acceleratorRect = new Rectangle(0, 0, 0, 0);
        Rectangle checkIconRect = new Rectangle(0, 0, 0, 0);
        Rectangle arrowIconRect = new Rectangle(0, 0, 0, 0);
        Rectangle viewRect = new Rectangle(32767, 32767);
        if (forPainting) {
            int menuWidth = menuItem.getWidth();
            int menuHeight = menuItem.getHeight();
            if (menuWidth > 0 && menuHeight > 0) {
                viewRect.setBounds(0, 0, menuWidth, menuHeight);
            }
            viewRect.x += i2.left;
            viewRect.y += i2.top;
            viewRect.width -= i2.right + viewRect.x;
            viewRect.height -= i2.bottom + viewRect.y;
        }
        FontMetrics fm = menuItem.getFontMetrics(menuItem.getFont());
        FontMetrics fmAccel = menuItem.getFontMetrics(acceleratorFont);
        KeyStroke accelerator = menuItem.getAccelerator();
        String acceleratorText = "";
        if (accelerator != null) {
            int keyCode;
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                acceleratorText = acceleratorText + UIManager.getString("MenuItem.acceleratorDelimiter");
            }
            acceleratorText = (keyCode = accelerator.getKeyCode()) != 0 ? acceleratorText + KeyEvent.getKeyText(keyCode) : acceleratorText + accelerator.getKeyChar();
        }
        String text = MenuUtilities.layoutMenuItem(menuItem, fm, menuItem.getText(), fmAccel, acceleratorText, menuItem.getIcon(), checkIcon, arrowIcon, menuItem.getVerticalAlignment(), menuItem.getHorizontalAlignment(), menuItem.getVerticalTextPosition(), menuItem.getHorizontalTextPosition(), viewRect, iconRect, textRect, acceleratorRect, checkIconRect, arrowIconRect, menuItem.getText() == null ? 0 : defaultTextIconGap, defaultTextIconGap);
        MenuLayoutInfo mlInfo = new MenuLayoutInfo();
        mlInfo.checkIconRect = checkIconRect;
        mlInfo.iconRect = iconRect;
        mlInfo.textRect = textRect;
        mlInfo.viewRect = viewRect;
        mlInfo.acceleratorRect = acceleratorRect;
        mlInfo.arrowIconRect = arrowIconRect;
        mlInfo.text = text;
        return mlInfo;
    }

    private static String layoutMenuItem(JMenuItem menuItem, FontMetrics fm, String text, FontMetrics fmAccel, String acceleratorText, Icon icon, Icon checkIcon, Icon arrowIcon, int verticalAlignment, int horizontalAlignment, int verticalTextPosition, int horizontalTextPosition, Rectangle viewRect, Rectangle iconRect, Rectangle textRect, Rectangle acceleratorRect, Rectangle checkIconRect, Rectangle arrowIconRect, int textIconGap, int menuItemGap) {
        SwingUtilities.layoutCompoundLabel(menuItem, fm, text, icon, verticalAlignment, horizontalAlignment, verticalTextPosition, horizontalTextPosition, viewRect, iconRect, textRect, textIconGap);
        if (acceleratorText == null || acceleratorText.equals("")) {
            acceleratorRect.height = 0;
            acceleratorRect.width = 0;
            acceleratorText = "";
        } else {
            acceleratorRect.width = fmAccel.stringWidth(acceleratorText);
            acceleratorRect.height = fmAccel.getHeight();
        }
        if (MenuUtilities.useCheckAndArrow(menuItem)) {
            if (checkIcon != null) {
                checkIconRect.width = checkIcon.getIconWidth();
                checkIconRect.height = checkIcon.getIconHeight();
            } else {
                checkIconRect.height = 0;
                checkIconRect.width = 0;
            }
            if (arrowIcon != null) {
                arrowIconRect.width = arrowIcon.getIconWidth();
                arrowIconRect.height = arrowIcon.getIconHeight();
            } else {
                arrowIconRect.height = 0;
                arrowIconRect.width = 0;
            }
        }
        Rectangle labelRect = iconRect.union(textRect);
        if (menuItem.getComponentOrientation().isLeftToRight()) {
            textRect.x += menuItemGap;
            iconRect.x += menuItemGap;
            acceleratorRect.x = viewRect.x + viewRect.width - arrowIconRect.width - menuItemGap - acceleratorRect.width;
            if (MenuUtilities.useCheckAndArrow(menuItem)) {
                checkIconRect.x = viewRect.x + menuItemGap;
                textRect.x += menuItemGap + checkIconRect.width;
                iconRect.x += menuItemGap + checkIconRect.width;
                arrowIconRect.x = viewRect.x + viewRect.width - menuItemGap - arrowIconRect.width;
            }
        } else {
            textRect.x -= menuItemGap;
            iconRect.x -= menuItemGap;
            acceleratorRect.x = viewRect.x + arrowIconRect.width + menuItemGap;
            if (MenuUtilities.useCheckAndArrow(menuItem)) {
                checkIconRect.x = viewRect.x + viewRect.width - menuItemGap - checkIconRect.width;
                textRect.x -= menuItemGap + checkIconRect.width;
                iconRect.x -= menuItemGap + checkIconRect.width;
                arrowIconRect.x = viewRect.x + menuItemGap;
            }
        }
        acceleratorRect.y = labelRect.y + labelRect.height / 2 - acceleratorRect.height / 2;
        if (MenuUtilities.useCheckAndArrow(menuItem)) {
            arrowIconRect.y = labelRect.y + labelRect.height / 2 - arrowIconRect.height / 2;
            checkIconRect.y = labelRect.y + labelRect.height / 2 - checkIconRect.height / 2;
        }
        return text;
    }

    private static boolean useCheckAndArrow(JMenuItem menuItem) {
        boolean b2 = true;
        if (menuItem instanceof JMenu && ((JMenu)menuItem).isTopLevelMenu()) {
            b2 = false;
        }
        return b2;
    }

    public static void paintMenuItem(Graphics g2, JMenuItem menuItem, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
        Component[] popups;
        JRootPane rootPane;
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        Graphics2D g2d = (Graphics2D)g2.create();
        if (Boolean.TRUE.equals(menuItem.getClientProperty("substancelaf.internal.paint.isCoveredByLightweightPopups")) && (rootPane = SwingUtilities.getRootPane(menuItem)) != null && (popups = rootPane.getLayeredPane().getComponentsInLayer(JLayeredPane.POPUP_LAYER)) != null) {
            int popupIndexToStartWith = SubstanceCoreUtilities.getPopupParentIndexOf(menuItem, popups) - 1;
            Area clip = new Area(g2d.getClip());
            for (int i2 = popupIndexToStartWith; i2 >= 0; --i2) {
                Component popup = popups[i2];
                Rectangle popupArea = SwingUtilities.convertRectangle(rootPane.getLayeredPane(), popup.getBounds(), menuItem);
                clip.subtract(new Area(popupArea));
            }
            g2d.setClip(clip);
        }
        ButtonModel model = menuItem.getModel();
        SubstanceMenu menuUi = (SubstanceMenu)((Object)menuItem.getUI());
        Font f2 = menuItem.getFont();
        g2d.setFont(f2);
        KeyStroke accelerator = menuItem.getAccelerator();
        String acceleratorText = "";
        if (accelerator != null) {
            int keyCode;
            int modifiers = accelerator.getModifiers();
            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
                acceleratorText = acceleratorText + UIManager.getString("MenuItem.acceleratorDelimiter");
            }
            acceleratorText = (keyCode = accelerator.getKeyCode()) != 0 ? acceleratorText + KeyEvent.getKeyText(keyCode) : acceleratorText + accelerator.getKeyChar();
        }
        Icon icon = menuItem.getIcon();
        MenuLayoutInfo mli = MenuUtilities.getMenuLayoutInfo(true, menuItem, menuUi.getAcceleratorFont(), menuUi.getCheckIcon(), menuUi.getArrowIcon(), menuUi.getDefaultTextIconGap());
        MenuLayoutMetrics popupMetrics = MenuUtilities.getPopupLayoutMetrics(menuItem, true);
        Insets i3 = menuItem.getInsets();
        if (popupMetrics != null) {
            Rectangle labelRect;
            int currX;
            SubstanceConstants.MenuGutterFillKind gutterFillKind = SubstanceCoreUtilities.getMenuGutterFillKind();
            boolean needExtraIconTextGap = gutterFillKind != null && gutterFillKind != SubstanceConstants.MenuGutterFillKind.NONE;
            int gap = popupMetrics.maxIconTextGap;
            if (menuItem.getComponentOrientation().isLeftToRight()) {
                int bump;
                currX = i3.left + gap / 2;
                if (checkIcon != null) {
                    mli.checkIconRect = new Rectangle(currX, i3.top, checkIcon.getIconWidth(), checkIcon.getIconHeight());
                    bump = (popupMetrics.maxCheckIconWidth - checkIcon.getIconWidth()) / 2;
                    mli.checkIconRect.x += bump;
                }
                if (popupMetrics.maxCheckIconWidth > 0) {
                    currX += popupMetrics.maxCheckIconWidth + gap;
                }
                if (icon != null) {
                    mli.iconRect = new Rectangle(currX, i3.top, icon.getIconWidth(), icon.getIconHeight());
                    bump = (popupMetrics.maxIconWidth - icon.getIconWidth()) / 2;
                    mli.iconRect.x += bump;
                }
                if (popupMetrics.maxIconWidth > 0) {
                    currX += popupMetrics.maxIconWidth + gap;
                }
                menuItem.putClientProperty(GUTTER_X, currX + gap / 2);
                if (needExtraIconTextGap) {
                    currX += gap;
                }
                if (menuItem.getText() != null) {
                    mli.textRect = new Rectangle(currX, mli.textRect.y, popupMetrics.maxTextWidth, mli.textRect.height);
                    mli.text = menuItem.getText();
                }
                currX += popupMetrics.maxTextWidth + gap;
                if (popupMetrics.maxAcceleratorWidth > 0) {
                    mli.acceleratorRect = new Rectangle((currX += 5 * gap) + popupMetrics.maxAcceleratorWidth - mli.acceleratorRect.width, mli.textRect.y, mli.acceleratorRect.width, mli.textRect.height);
                }
                if (popupMetrics.maxAcceleratorWidth > 0) {
                    currX += popupMetrics.maxAcceleratorWidth + gap;
                }
                if (arrowIcon != null) {
                    mli.arrowIconRect = new Rectangle(currX, i3.top, popupMetrics.maxArrowIconWidth, arrowIcon.getIconHeight());
                }
                labelRect = new Rectangle(0, 0, menuItem.getWidth(), menuItem.getHeight());
                if (mli.textRect != null) {
                    labelRect = mli.textRect;
                }
                if (mli.iconRect != null) {
                    mli.iconRect.y = labelRect.y + labelRect.height / 2 - mli.iconRect.height / 2;
                }
                if (mli.arrowIconRect != null) {
                    mli.arrowIconRect.y = labelRect.y + labelRect.height / 2 - mli.arrowIconRect.height / 2;
                }
                if (mli.checkIconRect != null) {
                    mli.checkIconRect.y = labelRect.y + labelRect.height / 2 - mli.checkIconRect.height / 2;
                }
            } else {
                int bump;
                currX = menuItem.getWidth() - i3.right - gap / 2;
                if (checkIcon != null) {
                    mli.checkIconRect = new Rectangle(currX - popupMetrics.maxCheckIconWidth, i3.top, checkIcon.getIconWidth(), checkIcon.getIconHeight());
                    bump = (popupMetrics.maxCheckIconWidth - checkIcon.getIconWidth()) / 2;
                    mli.checkIconRect.x += bump;
                }
                if (popupMetrics.maxCheckIconWidth > 0) {
                    currX -= popupMetrics.maxCheckIconWidth + gap;
                }
                if (icon != null) {
                    mli.iconRect = new Rectangle(currX - popupMetrics.maxIconWidth, i3.top, icon.getIconWidth(), icon.getIconHeight());
                    bump = (popupMetrics.maxIconWidth - icon.getIconWidth()) / 2;
                    mli.iconRect.x += bump;
                }
                if (popupMetrics.maxIconWidth > 0) {
                    currX -= popupMetrics.maxIconWidth + gap;
                }
                menuItem.putClientProperty(GUTTER_X, currX + gap / 2);
                if (needExtraIconTextGap) {
                    currX -= gap;
                }
                if (menuItem.getText() != null) {
                    mli.textRect = new Rectangle(currX - mli.textRect.width, mli.textRect.y, popupMetrics.maxTextWidth, mli.textRect.height);
                    mli.text = menuItem.getText();
                }
                currX -= popupMetrics.maxTextWidth + gap;
                if (popupMetrics.maxAcceleratorWidth > 0) {
                    mli.acceleratorRect = new Rectangle((currX -= 5 * gap) - popupMetrics.maxAcceleratorWidth, mli.textRect.y, mli.acceleratorRect.width, mli.textRect.height);
                }
                if (popupMetrics.maxAcceleratorWidth > 0) {
                    currX -= popupMetrics.maxAcceleratorWidth + gap;
                }
                if (arrowIcon != null) {
                    mli.arrowIconRect = new Rectangle(currX - popupMetrics.maxArrowIconWidth, i3.top, popupMetrics.maxArrowIconWidth, arrowIcon.getIconHeight());
                }
                labelRect = new Rectangle(0, 0, menuItem.getWidth(), menuItem.getHeight());
                if (mli.textRect != null) {
                    labelRect = mli.textRect;
                }
                if (mli.iconRect != null) {
                    mli.iconRect.y = labelRect.y + labelRect.height / 2 - mli.iconRect.height / 2;
                }
                if (mli.arrowIconRect != null) {
                    mli.arrowIconRect.y = labelRect.y + labelRect.height / 2 - mli.arrowIconRect.height / 2;
                }
                if (mli.checkIconRect != null) {
                    mli.checkIconRect.y = labelRect.y + labelRect.height / 2 - mli.checkIconRect.height / 2;
                }
            }
        }
        menuItem.putClientProperty(LAYOUT_INFO, mli);
        Container parent = menuItem.getParent();
        if (parent instanceof JPopupMenu) {
            ((JPopupMenu)parent).putClientProperty(GUTTER_X, menuItem.getClientProperty(GUTTER_X));
        }
        MenuUtilities.paintBackground(g2d, menuItem);
        SubstanceMenuBackgroundDelegate.paintHighlights(g2, menuItem, 0.5f);
        Graphics2D graphics = (Graphics2D)g2d.create();
        if (mli.text != null) {
            View v2 = (View)menuItem.getClientProperty("html");
            if (v2 != null) {
                v2.paint(graphics, mli.textRect);
            } else {
                SubstanceTextUtilities.paintText(graphics, menuItem, mli.textRect, mli.text, menuItem.getDisplayedMnemonicIndex());
            }
        }
        if (acceleratorText != null && !acceleratorText.equals("")) {
            SubstanceTextUtilities.paintText(graphics, menuItem, mli.acceleratorRect, acceleratorText, -1);
        }
        float textAlpha = SubstanceColorSchemeUtilities.getAlpha(menuItem, ComponentState.getState(menuItem.getModel(), menuItem, true));
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(menuItem, textAlpha, g2d));
        if (checkIcon != null && MenuUtilities.useCheckAndArrow(menuItem)) {
            checkIcon.paintIcon(menuItem, graphics, mli.checkIconRect.x, mli.checkIconRect.y);
        }
        if (icon != null) {
            if (!model.isEnabled()) {
                icon = menuItem.getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                icon = menuItem.getPressedIcon();
                if (icon == null) {
                    icon = menuItem.getIcon();
                }
            } else {
                icon = menuItem.getIcon();
            }
            if (icon != null) {
                boolean useThemed = SubstanceCoreUtilities.useThemedDefaultIcon(menuItem);
                if (!useThemed) {
                    icon.paintIcon(menuItem, g2d, mli.iconRect.x, mli.iconRect.y);
                } else {
                    boolean useRegularVersion;
                    Icon themed = SubstanceCoreUtilities.getThemedIcon(menuItem, icon);
                    boolean bl = useRegularVersion = model.isPressed() || model.isSelected();
                    if (useRegularVersion) {
                        icon.paintIcon(menuItem, g2d, mli.iconRect.x, mli.iconRect.y);
                    } else {
                        TransitionAwareUI transitionAwareUI = (TransitionAwareUI)((Object)menuItem.getUI());
                        StateTransitionTracker stateTransitionTracker = transitionAwareUI.getTransitionTracker();
                        float rolloverAmount = Math.max(stateTransitionTracker.getFacetStrength(ComponentStateFacet.ROLLOVER), stateTransitionTracker.getFacetStrength(ComponentStateFacet.ARM));
                        if (rolloverAmount > 0.0f) {
                            themed.paintIcon(menuItem, g2d, mli.iconRect.x, mli.iconRect.y);
                            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(menuItem, rolloverAmount, g2));
                            icon.paintIcon(menuItem, g2d, mli.iconRect.x, mli.iconRect.y);
                            g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)menuItem, g2));
                        } else {
                            themed.paintIcon(menuItem, g2d, mli.iconRect.x, mli.iconRect.y);
                        }
                    }
                }
            }
        }
        if (arrowIcon != null && MenuUtilities.useCheckAndArrow(menuItem)) {
            arrowIcon.paintIcon(menuItem, graphics, mli.arrowIconRect.x, mli.arrowIconRect.y);
        }
        graphics.dispose();
        g2d.dispose();
    }

    private static void paintBackground(Graphics g2, JMenuItem menuItem) {
        int textOffset = MenuUtilities.getTextOffset(menuItem, menuItem.getParent());
        SubstanceMenuBackgroundDelegate.paintBackground(g2, menuItem, textOffset);
    }

    protected static MenuLayoutMetrics getMetrics(JPopupMenu popupMenu, boolean forPainting) {
        MenuLayoutMetrics metrics = new MenuLayoutMetrics();
        for (int i2 = 0; i2 < popupMenu.getComponentCount(); ++i2) {
            JMenuItem childMenuItem;
            ButtonUI bui;
            Component comp = popupMenu.getComponent(i2);
            if (!(comp instanceof JMenuItem) || !((bui = (childMenuItem = (JMenuItem)comp).getUI()) instanceof SubstanceMenu)) continue;
            SubstanceMenu ui = (SubstanceMenu)((Object)bui);
            MenuLayoutInfo mli = MenuUtilities.getMenuLayoutInfo(forPainting, childMenuItem, ui.getAcceleratorFont(), ui.getCheckIcon(), ui.getArrowIcon(), ui.getDefaultTextIconGap());
            metrics.maxIconWidth = Math.max(metrics.maxIconWidth, mli.iconRect.width);
            metrics.maxCheckIconWidth = Math.max(metrics.maxCheckIconWidth, mli.checkIconRect.width);
            metrics.maxTextWidth = Math.max(metrics.maxTextWidth, mli.textRect.width);
            metrics.maxAcceleratorWidth = Math.max(metrics.maxAcceleratorWidth, mli.acceleratorRect.width);
            metrics.maxArrowIconWidth = Math.max(metrics.maxArrowIconWidth, mli.arrowIconRect.width);
            metrics.maxIconTextGap = Math.max(metrics.maxIconTextGap, ui.getDefaultTextIconGap());
        }
        return metrics;
    }

    public static MenuLayoutMetrics getPopupLayoutMetrics(JMenuItem menuItem, boolean forPainting) {
        ButtonUI bui;
        Container comp = menuItem.getParent();
        if (comp instanceof JPopupMenu) {
            JPopupMenu popupMenu = (JPopupMenu)comp;
            return MenuUtilities.getPopupLayoutMetrics(popupMenu, forPainting);
        }
        if (!(comp instanceof JMenu) && (bui = menuItem.getUI()) instanceof SubstanceMenu) {
            SubstanceMenu ui = (SubstanceMenu)((Object)bui);
            MenuLayoutInfo mli = MenuUtilities.getMenuLayoutInfo(forPainting, menuItem, ui.getAcceleratorFont(), ui.getCheckIcon(), ui.getArrowIcon(), ui.getDefaultTextIconGap());
            MenuLayoutMetrics metrics = new MenuLayoutMetrics();
            metrics.maxIconWidth = mli.iconRect.width;
            metrics.maxCheckIconWidth = mli.checkIconRect.width;
            metrics.maxTextWidth = mli.textRect.width;
            metrics.maxAcceleratorWidth = mli.acceleratorRect.width;
            metrics.maxArrowIconWidth = mli.arrowIconRect.width;
            metrics.maxIconTextGap = ui.getDefaultTextIconGap();
            return metrics;
        }
        return null;
    }

    public static MenuLayoutMetrics getPopupLayoutMetrics(JPopupMenu popupMenu, boolean forPainting) {
        Object prop = popupMenu.getClientProperty(LAYOUT_METRICS);
        if (prop instanceof MenuLayoutMetrics) {
            return (MenuLayoutMetrics)prop;
        }
        MenuLayoutMetrics metrics = MenuUtilities.getMetrics(popupMenu, forPainting);
        popupMenu.putClientProperty(LAYOUT_METRICS, metrics);
        return metrics;
    }

    private static void cleanPopupLayoutMetrics(JMenuItem menuItem) {
        Container comp = menuItem.getParent();
        if (comp instanceof JPopupMenu) {
            JPopupMenu popupMenu = (JPopupMenu)comp;
            MenuUtilities.cleanPopupLayoutMetrics(popupMenu);
        }
    }

    public static void cleanPopupLayoutMetrics(JPopupMenu popupMenu) {
        if (popupMenu != null) {
            popupMenu.putClientProperty(LAYOUT_METRICS, null);
        }
    }

    public static int getPreferredWidth(JMenuItem menuItem) {
        SubstanceConstants.MenuGutterFillKind gutterFillKind;
        boolean needExtraIconTextGap;
        Insets ins = menuItem.getInsets();
        MenuLayoutMetrics popupMetrics = MenuUtilities.getPopupLayoutMetrics(menuItem, false);
        int width = popupMetrics.maxCheckIconWidth + popupMetrics.maxIconWidth + popupMetrics.maxTextWidth + popupMetrics.maxAcceleratorWidth + popupMetrics.maxArrowIconWidth + ins.left + ins.right;
        int gapsToAdd = 0;
        if (popupMetrics.maxCheckIconWidth > 0) {
            ++gapsToAdd;
        }
        if (popupMetrics.maxIconWidth > 0) {
            ++gapsToAdd;
        }
        if (popupMetrics.maxAcceleratorWidth > 0) {
            ++gapsToAdd;
        }
        if (popupMetrics.maxArrowIconWidth > 0) {
            ++gapsToAdd;
        }
        int gap = popupMetrics.maxIconTextGap;
        width += (1 + gapsToAdd) * gap;
        if (popupMetrics.maxAcceleratorWidth > 0) {
            width += 5 * gap;
        }
        boolean bl = needExtraIconTextGap = (gutterFillKind = SubstanceCoreUtilities.getMenuGutterFillKind()) != null && gutterFillKind != SubstanceConstants.MenuGutterFillKind.NONE;
        if (needExtraIconTextGap) {
            width += gap;
        }
        return width;
    }

    public static int getTextOffset(JComponent menuItem, Component menuItemParent) {
        if (!(menuItemParent instanceof JPopupMenu)) {
            return 0;
        }
        Object itemProp = menuItem.getClientProperty(GUTTER_X);
        if (itemProp instanceof Integer) {
            return (Integer)itemProp;
        }
        JPopupMenu popupMenu = (JPopupMenu)menuItemParent;
        Object parentProp = popupMenu.getClientProperty(GUTTER_X);
        if (parentProp instanceof Integer) {
            return (Integer)parentProp;
        }
        return 0;
    }

    public static class MenuLayoutMetrics {
        public int maxIconWidth;
        public int maxCheckIconWidth;
        public int maxTextWidth;
        public int maxAcceleratorWidth;
        public int maxArrowIconWidth;
        public int maxIconTextGap;
    }

    public static class MenuLayoutInfo {
        public Rectangle viewRect;
        public Rectangle iconRect;
        public Rectangle checkIconRect;
        public Rectangle textRect;
        public Rectangle acceleratorRect;
        public Rectangle arrowIconRect;
        public String text;
    }

    public static class MenuPropertyListener
    implements PropertyChangeListener {
        private JMenuItem menuItem;
        private Runnable cleanLayoutMetricsRunnable;

        public MenuPropertyListener(final JMenuItem menuItem) {
            this.menuItem = menuItem;
            this.cleanLayoutMetricsRunnable = new Runnable(){

                @Override
                public void run() {
                    MenuUtilities.cleanPopupLayoutMetrics(menuItem);
                }
            };
        }

        public void install() {
            this.menuItem.addPropertyChangeListener(this);
        }

        public void uninstall() {
            this.menuItem.removePropertyChangeListener(this);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (!evt.getPropertyName().equals(MenuUtilities.LAYOUT_METRICS)) {
                SwingUtilities.invokeLater(this.cleanLayoutMetricsRunnable);
            }
        }
    }
}

