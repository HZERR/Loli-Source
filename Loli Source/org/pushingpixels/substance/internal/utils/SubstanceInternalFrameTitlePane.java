/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.MenuBarUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.colorscheme.ShiftColorScheme;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.ui.SubstanceMenuBarUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceTitleButton;
import org.pushingpixels.substance.internal.utils.icon.SubstanceIconFactory;
import org.pushingpixels.substance.internal.utils.icon.TransitionAwareIcon;

public class SubstanceInternalFrameTitlePane
extends BasicInternalFrameTitlePane {
    protected PropertyChangeListener substancePropertyListener;
    protected PropertyChangeListener substanceWinModifiedListener;
    protected static final String ICONIFYING = "substance.internal.internalTitleFramePane.iconifying";
    protected static final String UNINSTALLED = "substance.internal.internalTitleFramePane.uninstalled";

    public SubstanceInternalFrameTitlePane(JInternalFrame f2) {
        super(f2);
        this.setToolTipText(f2.getTitle());
        SubstanceLookAndFeel.setDecorationType(this, DecorationAreaType.SECONDARY_TITLE_PANE);
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        if (SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            this.setForeground(SubstanceColorUtilities.getForegroundColor(SubstanceCoreUtilities.getSkin(this.frame).getActiveColorScheme(this.getThisDecorationType())));
        }
    }

    @Override
    protected void installListeners() {
        super.installListeners();
        this.substancePropertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("title".equals(evt.getPropertyName())) {
                    SubstanceInternalFrameTitlePane.this.setToolTipText((String)evt.getNewValue());
                }
                if ("JInternalFrame.messageType".equals(evt.getPropertyName())) {
                    SubstanceInternalFrameTitlePane.this.updateOptionPaneState();
                    SubstanceInternalFrameTitlePane.this.frame.repaint();
                }
                if ("closed".equals(evt.getPropertyName())) {
                    SubstanceInternalFrameTitlePane.this.windowMenu.setPopupMenuVisible(false);
                }
            }
        };
        this.frame.addPropertyChangeListener(this.substancePropertyListener);
        this.substanceWinModifiedListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("windowModified".equals(evt.getPropertyName())) {
                    SubstanceInternalFrameTitlePane.this.syncCloseButtonTooltip();
                }
            }
        };
        this.frame.getRootPane().addPropertyChangeListener(this.substanceWinModifiedListener);
    }

    @Override
    public void uninstallListeners() {
        this.frame.removePropertyChangeListener(this.substancePropertyListener);
        this.substancePropertyListener = null;
        this.frame.getRootPane().removePropertyChangeListener(this.substanceWinModifiedListener);
        this.substanceWinModifiedListener = null;
        super.uninstallListeners();
    }

    public void uninstall() {
        if (this.menuBar != null && this.menuBar.getMenuCount() > 0) {
            SubstanceMenuBarUI ui;
            MenuBarUI menuBarUI = this.menuBar.getUI();
            if (menuBarUI instanceof SubstanceMenuBarUI && (ui = (SubstanceMenuBarUI)menuBarUI).getMenuBar() == this.menuBar) {
                menuBarUI.uninstallUI(this.menuBar);
            }
            SubstanceCoreUtilities.uninstallMenu(this.menuBar.getMenu(0));
            this.remove(this.menuBar);
            this.remove(this.maxButton);
            this.remove(this.closeButton);
            this.remove(this.iconButton);
        }
        this.uninstallListeners();
        this.putClientProperty(UNINSTALLED, Boolean.TRUE);
    }

    public void setActive(boolean isActive) {
        if (this.getRootPane() != null) {
            this.getRootPane().repaint();
        }
    }

    @Override
    protected void enableActions() {
        super.enableActions();
        if (!this.frame.isIcon()) {
            if (this.maxButton != null) {
                this.maxButton.setEnabled(this.maximizeAction.isEnabled() || this.restoreAction.isEnabled());
            }
            if (this.iconButton != null) {
                this.iconButton.setEnabled(this.iconifyAction.isEnabled());
            }
        }
    }

    public DecorationAreaType getThisDecorationType() {
        DecorationAreaType dat = SubstanceLookAndFeel.getDecorationType(this);
        if (dat == DecorationAreaType.PRIMARY_TITLE_PANE) {
            return SubstanceCoreUtilities.isPaintRootPaneActivated(this.frame.getRootPane()) ? DecorationAreaType.PRIMARY_TITLE_PANE : DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE;
        }
        if (dat == DecorationAreaType.SECONDARY_TITLE_PANE) {
            return SubstanceCoreUtilities.isPaintRootPaneActivated(this.frame.getRootPane()) ? DecorationAreaType.SECONDARY_TITLE_PANE : DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE;
        }
        return dat;
    }

    @Override
    public void paintComponent(Graphics g2) {
        FontMetrics fm;
        Rectangle rect;
        Icon icon;
        int xOffset;
        Color backgr;
        JInternalFrame hostFrame;
        DecorationAreaType decorationType = this.getThisDecorationType();
        Graphics2D graphics = (Graphics2D)g2.create();
        float coef = this.getParent() instanceof JInternalFrame.JDesktopIcon ? 0.6f : 1.0f;
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(this.frame, coef, g2));
        boolean leftToRight = this.frame.getComponentOrientation().isLeftToRight();
        int width = this.getWidth();
        int height = this.getHeight() + 2;
        SubstanceColorScheme scheme = SubstanceCoreUtilities.getSkin(this.frame).getEnabledColorScheme(decorationType);
        JComponent hostForColorization = hostFrame = (JInternalFrame)SwingUtilities.getAncestorOfClass(JInternalFrame.class, this);
        if (hostFrame == null) {
            JInternalFrame.JDesktopIcon desktopIcon = (JInternalFrame.JDesktopIcon)SwingUtilities.getAncestorOfClass(JInternalFrame.JDesktopIcon.class, this);
            if (desktopIcon != null) {
                hostFrame = desktopIcon.getInternalFrame();
            }
            hostForColorization = desktopIcon;
        }
        if (!((backgr = hostFrame.getBackground()) instanceof UIResource)) {
            double colorization = SubstanceCoreUtilities.getColorizationFactor(hostForColorization);
            scheme = ShiftColorScheme.getShiftedScheme(scheme, backgr, colorization, null, 0.0);
        }
        String theTitle = this.frame.getTitle();
        if (leftToRight) {
            xOffset = 5;
            icon = this.frame.getFrameIcon();
            if (icon != null) {
                xOffset += icon.getIconWidth() + 5;
            }
            int leftEnd = this.menuBar == null ? 0 : this.menuBar.getWidth() + 5;
            xOffset += leftEnd;
            if (icon != null) {
                leftEnd += icon.getIconWidth() + 5;
            }
            int rightEnd = width - 5;
            JButton leftmostButton = null;
            if (this.frame.isIconifiable()) {
                leftmostButton = this.iconButton;
            } else if (this.frame.isMaximizable()) {
                leftmostButton = this.maxButton;
            } else if (this.frame.isClosable()) {
                leftmostButton = this.closeButton;
            }
            if (leftmostButton != null) {
                rect = leftmostButton.getBounds();
                rightEnd = rect.getBounds().x - 5;
            }
            if (theTitle != null) {
                int titleWidth;
                fm = this.frame.getFontMetrics(graphics.getFont());
                String clippedTitle = SubstanceCoreUtilities.clipString(fm, titleWidth = rightEnd - leftEnd, theTitle);
                if (theTitle.equals(clippedTitle)) {
                    this.setToolTipText(null);
                } else {
                    this.setToolTipText(theTitle);
                }
                theTitle = clippedTitle;
            }
        } else {
            xOffset = width - 5;
            icon = this.frame.getFrameIcon();
            if (icon != null) {
                xOffset -= icon.getIconWidth() + 5;
            }
            int rightEnd = this.menuBar == null ? xOffset : xOffset - this.menuBar.getWidth() - 5;
            JButton rightmostButton = null;
            if (this.frame.isIconifiable()) {
                rightmostButton = this.iconButton;
            } else if (this.frame.isMaximizable()) {
                rightmostButton = this.maxButton;
            } else if (this.frame.isClosable()) {
                rightmostButton = this.closeButton;
            }
            int leftEnd = 5;
            if (rightmostButton != null) {
                rect = rightmostButton.getBounds();
                leftEnd = rect.getBounds().x + 5;
            }
            if (theTitle != null) {
                int titleWidth;
                fm = this.frame.getFontMetrics(graphics.getFont());
                String clippedTitle = SubstanceCoreUtilities.clipString(fm, titleWidth = rightEnd - leftEnd, theTitle);
                if (theTitle.equals(clippedTitle)) {
                    this.setToolTipText(null);
                } else {
                    this.setToolTipText(theTitle);
                }
                theTitle = clippedTitle;
                xOffset = rightEnd - fm.stringWidth(theTitle);
            }
        }
        BackgroundPaintingUtils.update(graphics, this, false, decorationType);
        if (theTitle != null) {
            JRootPane rootPane = this.getRootPane();
            FontMetrics fm2 = rootPane.getFontMetrics(graphics.getFont());
            int yOffset = (height - fm2.getHeight()) / 2 + fm2.getAscent();
            SubstanceTextUtilities.paintTextWithDropShadow(this, graphics, SubstanceColorUtilities.getForegroundColor(scheme), theTitle, width, height, xOffset, yOffset);
        }
        graphics.dispose();
    }

    @Override
    protected void setButtonIcons() {
        super.setButtonIcons();
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return;
        }
        TransitionAwareIcon restoreIcon = new TransitionAwareIcon(this.maxButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                return SubstanceIconFactory.getTitlePaneIcon(SubstanceIconFactory.IconKind.RESTORE, scheme, SubstanceCoreUtilities.getSkin(SubstanceInternalFrameTitlePane.this).getBackgroundColorScheme(SubstanceInternalFrameTitlePane.this.getThisDecorationType()));
            }
        }, "substance.internalFrame.restoreIcon");
        TransitionAwareIcon maximizeIcon = new TransitionAwareIcon(this.maxButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                return SubstanceIconFactory.getTitlePaneIcon(SubstanceIconFactory.IconKind.MAXIMIZE, scheme, SubstanceCoreUtilities.getSkin(SubstanceInternalFrameTitlePane.this).getBackgroundColorScheme(SubstanceInternalFrameTitlePane.this.getThisDecorationType()));
            }
        }, "substance.internalFrame.maxIcon");
        TransitionAwareIcon minimizeIcon = new TransitionAwareIcon(this.iconButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                return SubstanceIconFactory.getTitlePaneIcon(SubstanceIconFactory.IconKind.MINIMIZE, scheme, SubstanceCoreUtilities.getSkin(SubstanceInternalFrameTitlePane.this).getBackgroundColorScheme(SubstanceInternalFrameTitlePane.this.getThisDecorationType()));
            }
        }, "substance.internalFrame.minIcon");
        TransitionAwareIcon closeIcon = new TransitionAwareIcon(this.closeButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                return SubstanceIconFactory.getTitlePaneIcon(SubstanceIconFactory.IconKind.CLOSE, scheme, SubstanceCoreUtilities.getSkin(SubstanceInternalFrameTitlePane.this).getBackgroundColorScheme(SubstanceInternalFrameTitlePane.this.getThisDecorationType()));
            }
        }, "substance.internalFrame.closeIcon");
        if (this.frame.isIcon()) {
            this.iconButton.setIcon(restoreIcon);
            this.iconButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.frame).getString("SystemMenu.restore"));
            this.maxButton.setIcon(maximizeIcon);
            this.maxButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.frame).getString("SystemMenu.maximize"));
        } else {
            this.iconButton.setIcon(minimizeIcon);
            this.iconButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.frame).getString("SystemMenu.iconify"));
            if (this.frame.isMaximum()) {
                this.maxButton.setIcon(restoreIcon);
                this.maxButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.frame).getString("SystemMenu.restore"));
            } else {
                this.maxButton.setIcon(maximizeIcon);
                this.maxButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.frame).getString("SystemMenu.maximize"));
            }
        }
        if (closeIcon != null) {
            this.closeButton.setIcon(closeIcon);
            this.syncCloseButtonTooltip();
        }
    }

    @Override
    protected JMenuBar createSystemMenuBar() {
        this.menuBar = new SubstanceMenuBar();
        this.menuBar.setFocusable(false);
        this.menuBar.setBorderPainted(true);
        this.menuBar.add(this.createSystemMenu());
        this.menuBar.setOpaque(false);
        this.menuBar.applyComponentOrientation(this.getComponentOrientation());
        return this.menuBar;
    }

    @Override
    protected void createActions() {
        super.createActions();
        this.iconifyAction = new SubstanceIconifyAction();
    }

    @Override
    protected JMenu createSystemMenu() {
        JMenu menu = super.createSystemMenu();
        menu.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e2) {
                if (e2.getClickCount() > 1) {
                    SubstanceInternalFrameTitlePane.this.closeAction.actionPerformed(new ActionEvent(e2.getSource(), 1001, null, EventQueue.getMostRecentEventTime(), e2.getModifiers()));
                }
            }
        });
        return menu;
    }

    @Override
    protected void addSystemMenuItems(JMenu menu) {
        menu.add(this.restoreAction);
        menu.add(this.iconifyAction);
        if (Toolkit.getDefaultToolkit().isFrameStateSupported(6)) {
            menu.add(this.maximizeAction);
        }
        menu.addSeparator();
        menu.add(this.closeAction);
    }

    @Override
    protected void createButtons() {
        this.iconButton = new SubstanceTitleButton("InternalFrameTitlePane.iconifyButtonAccessibleName");
        this.iconButton.addActionListener(this.iconifyAction);
        this.maxButton = new SubstanceTitleButton("InternalFrameTitlePane.maximizeButtonAccessibleName");
        this.maxButton.addActionListener(this.maximizeAction);
        this.closeButton = new SubstanceTitleButton("InternalFrameTitlePane.closeButtonAccessibleName");
        this.closeButton.addActionListener(this.closeAction);
        this.setButtonIcons();
        for (ActionListener listener : this.iconButton.getActionListeners()) {
            if (!(listener instanceof ClickListener)) continue;
            return;
        }
        this.iconButton.addActionListener(new ClickListener());
        for (ActionListener listener : this.maxButton.getActionListeners()) {
            if (!(listener instanceof ClickListener)) continue;
            return;
        }
        this.maxButton.addActionListener(new ClickListener());
        this.iconButton.putClientProperty("substancelaf.componentFlat", Boolean.TRUE);
        this.maxButton.putClientProperty("substancelaf.componentFlat", Boolean.TRUE);
        this.closeButton.putClientProperty("substancelaf.internal.isTitleCloseButton", Boolean.TRUE);
        this.closeButton.putClientProperty("substancelaf.componentFlat", Boolean.TRUE);
        this.enableActions();
    }

    @Override
    protected LayoutManager createLayout() {
        return new SubstanceTitlePaneLayout();
    }

    protected void syncCloseButtonTooltip() {
        if (SubstanceCoreUtilities.isInternalFrameModified(this.frame)) {
            this.closeButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.frame).getString("SystemMenu.close") + " [" + SubstanceCoreUtilities.getResourceBundle(this.frame).getString("Tooltip.contentsNotSaved") + "]");
        } else {
            this.closeButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.frame).getString("SystemMenu.close"));
        }
        this.closeButton.repaint();
    }

    @Override
    public void removeNotify() {
        boolean isAlive;
        super.removeNotify();
        boolean bl = isAlive = this.frame.isIcon() && !this.frame.isClosed() || Boolean.TRUE.equals(this.frame.getClientProperty(ICONIFYING));
        if (!isAlive) {
            this.uninstall();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (Boolean.TRUE.equals(this.getClientProperty(UNINSTALLED))) {
            this.installTitlePane();
            this.putClientProperty(UNINSTALLED, null);
        }
    }

    private void updateOptionPaneState() {
        Object obj = this.frame.getClientProperty("JInternalFrame.messageType");
        if (obj == null) {
            return;
        }
        if (this.frame.isClosable()) {
            this.frame.setClosable(false);
        }
    }

    public AbstractButton getCloseButton() {
        return this.closeButton;
    }

    public class SubstanceIconifyAction
    extends BasicInternalFrameTitlePane.IconifyAction {
        public SubstanceIconifyAction() {
            super(SubstanceInternalFrameTitlePane.this);
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            SubstanceInternalFrameTitlePane.this.frame.putClientProperty(SubstanceInternalFrameTitlePane.ICONIFYING, Boolean.TRUE);
            super.actionPerformed(e2);
            SubstanceInternalFrameTitlePane.this.frame.putClientProperty(SubstanceInternalFrameTitlePane.ICONIFYING, null);
        }
    }

    protected class SubstanceTitlePaneLayout
    extends BasicInternalFrameTitlePane.TitlePaneLayout {
        protected SubstanceTitlePaneLayout() {
            super(SubstanceInternalFrameTitlePane.this);
        }

        @Override
        public void addLayoutComponent(String name, Component c2) {
        }

        @Override
        public void removeLayoutComponent(Component c2) {
        }

        @Override
        public Dimension preferredLayoutSize(Container c2) {
            return this.minimumLayoutSize(c2);
        }

        @Override
        public Dimension minimumLayoutSize(Container c2) {
            int subtitle_w;
            int title_length;
            int width = 30;
            if (SubstanceInternalFrameTitlePane.this.frame.isClosable()) {
                width += 21;
            }
            if (SubstanceInternalFrameTitlePane.this.frame.isMaximizable()) {
                width += 16 + (SubstanceInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4);
            }
            if (SubstanceInternalFrameTitlePane.this.frame.isIconifiable()) {
                width += 16 + (SubstanceInternalFrameTitlePane.this.frame.isMaximizable() ? 2 : (SubstanceInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4));
            }
            FontMetrics fm = SubstanceInternalFrameTitlePane.this.frame.getFontMetrics(SubstanceInternalFrameTitlePane.this.getFont());
            String frameTitle = SubstanceInternalFrameTitlePane.this.frame.getTitle();
            int title_w = frameTitle != null ? fm.stringWidth(frameTitle) : 0;
            int n2 = title_length = frameTitle != null ? frameTitle.length() : 0;
            width = title_length > 2 ? (width += title_w < (subtitle_w = fm.stringWidth(SubstanceInternalFrameTitlePane.this.frame.getTitle().substring(0, 2) + "...")) ? title_w : subtitle_w) : (width += title_w);
            int fontHeight = fm.getHeight();
            fontHeight += 7;
            Icon icon = SubstanceInternalFrameTitlePane.this.frame.getFrameIcon();
            int iconHeight = 0;
            if (icon != null) {
                iconHeight = Math.min(icon.getIconHeight(), 16);
            }
            int height = Math.max(fontHeight, iconHeight += 5);
            return new Dimension(width, height);
        }

        @Override
        public void layoutContainer(Container c2) {
            int spacing;
            boolean leftToRight = SubstanceInternalFrameTitlePane.this.frame.getComponentOrientation().isLeftToRight();
            int w2 = SubstanceInternalFrameTitlePane.this.getWidth();
            int x2 = leftToRight ? w2 : 0;
            int buttonHeight = SubstanceInternalFrameTitlePane.this.closeButton.getIcon().getIconHeight();
            int buttonWidth = SubstanceInternalFrameTitlePane.this.closeButton.getIcon().getIconWidth();
            int y2 = (SubstanceInternalFrameTitlePane.this.getHeight() - buttonHeight) / 2;
            Icon icon = SubstanceInternalFrameTitlePane.this.frame.getFrameIcon();
            int iconHeight = 0;
            int iconWidth = 0;
            if (icon != null) {
                iconHeight = icon.getIconHeight();
                iconWidth = icon.getIconWidth();
            }
            int xMenuBar = leftToRight ? 5 : w2 - 16 - 5;
            SubstanceInternalFrameTitlePane.this.menuBar.setBounds(xMenuBar, (SubstanceInternalFrameTitlePane.this.getHeight() - iconHeight) / 2, iconWidth, iconHeight);
            if (SubstanceInternalFrameTitlePane.this.frame.isClosable()) {
                spacing = 4;
                SubstanceInternalFrameTitlePane.this.closeButton.setBounds(x2 += leftToRight ? -spacing - buttonWidth : spacing, y2, buttonWidth, buttonHeight);
                if (!leftToRight) {
                    x2 += buttonWidth;
                }
            }
            if (SubstanceInternalFrameTitlePane.this.frame.isMaximizable()) {
                spacing = SubstanceInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4;
                SubstanceInternalFrameTitlePane.this.maxButton.setBounds(x2 += leftToRight ? -spacing - buttonWidth : spacing, y2, buttonWidth, buttonHeight);
                if (!leftToRight) {
                    x2 += buttonWidth;
                }
            }
            if (SubstanceInternalFrameTitlePane.this.frame.isIconifiable()) {
                spacing = SubstanceInternalFrameTitlePane.this.frame.isMaximizable() ? 2 : (SubstanceInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4);
                SubstanceInternalFrameTitlePane.this.iconButton.setBounds(x2 += leftToRight ? -spacing - buttonWidth : spacing, y2, buttonWidth, buttonHeight);
                if (!leftToRight) {
                    x2 += buttonWidth;
                }
            }
        }
    }

    public class SubstanceMenuBar
    extends JMenuBar {
        @Override
        public void paint(Graphics g2) {
            if (SubstanceInternalFrameTitlePane.this.frame.getFrameIcon() != null) {
                SubstanceInternalFrameTitlePane.this.frame.getFrameIcon().paintIcon(this, g2, 0, 0);
            } else {
                Icon icon = UIManager.getIcon("InternalFrame.icon");
                if (icon != null) {
                    icon.paintIcon(this, g2, 0, 0);
                }
            }
        }

        @Override
        public Dimension getMinimumSize() {
            return this.getPreferredSize();
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            int iSize = SubstanceSizeUtils.getTitlePaneIconSize();
            return new Dimension(Math.max(iSize, size.width), Math.max(size.height, iSize));
        }
    }

    public static class ClickListener
    implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e2) {
            AbstractButton src = (AbstractButton)e2.getSource();
            ButtonModel model = src.getModel();
            model.setArmed(false);
            model.setPressed(false);
            model.setRollover(false);
            model.setSelected(false);
        }
    }
}

