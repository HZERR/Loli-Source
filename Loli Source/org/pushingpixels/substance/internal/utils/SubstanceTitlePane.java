/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.UIResource;
import org.pushingpixels.lafwidget.animation.effects.GhostPaintingUtils;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.lafwidget.utils.TrackableThread;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.skin.SkinInfo;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.ui.SubstanceRootPaneUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceTitleButton;
import org.pushingpixels.substance.internal.utils.SubstanceWidgetManager;
import org.pushingpixels.substance.internal.utils.icon.SubstanceIconFactory;
import org.pushingpixels.substance.internal.utils.icon.TransitionAwareIcon;

public class SubstanceTitlePane
extends JComponent {
    private PropertyChangeListener propertyChangeListener;
    protected JMenuBar menuBar;
    private Action closeAction;
    private Action iconifyAction;
    private Action restoreAction;
    private Action maximizeAction;
    protected JButton toggleButton;
    protected JButton minimizeButton;
    protected JButton closeButton;
    private WindowListener windowListener;
    protected Window window;
    protected JRootPane rootPane;
    private int state;
    private SubstanceRootPaneUI rootPaneUI;
    private static String heapStatusLogfileName;
    protected HeapStatusPanel heapStatusPanel;
    protected JCheckBoxMenuItem heapStatusMenuItem;
    protected PropertyChangeListener propertyListener;
    protected static final String EXTRA_COMPONENT_KIND = "substancelaf.internal.titlePane.extraComponentKind";
    protected Image appIcon;

    public SubstanceTitlePane(JRootPane root, SubstanceRootPaneUI ui) {
        this.rootPane = root;
        this.rootPaneUI = ui;
        this.state = -1;
        this.installSubcomponents();
        this.installDefaults();
        this.setLayout(this.createLayout());
        this.setToolTipText(this.getTitle());
        SubstanceLookAndFeel.setDecorationType(this, DecorationAreaType.PRIMARY_TITLE_PANE);
        this.setForeground(SubstanceColorUtilities.getForegroundColor(SubstanceCoreUtilities.getSkin(this).getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE)));
    }

    public void uninstall() {
        this.uninstallListeners();
        this.window = null;
        HeapStatusThread.unregisterPanel(this.heapStatusPanel);
        if (this.menuBar != null && this.menuBar.getMenuCount() > 0) {
            this.menuBar.getUI().uninstallUI(this.menuBar);
            SubstanceCoreUtilities.uninstallMenu(this.menuBar.getMenu(0));
        }
        if (this.heapStatusPanel != null) {
            for (MouseListener listener : this.heapStatusPanel.getMouseListeners()) {
                this.heapStatusPanel.removeMouseListener(listener);
            }
            HeapStatusThread.unregisterPanel(this.heapStatusPanel);
            this.remove(this.heapStatusPanel);
        }
        if (this.menuBar != null) {
            this.menuBar.removeAll();
        }
        this.removeAll();
    }

    private void installListeners() {
        if (this.window != null) {
            this.windowListener = new WindowHandler();
            this.window.addWindowListener(this.windowListener);
            this.propertyChangeListener = new PropertyChangeHandler();
            this.window.addPropertyChangeListener(this.propertyChangeListener);
        }
        this.propertyListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if ("windowModified".equals(evt.getPropertyName())) {
                    SubstanceTitlePane.this.syncCloseButtonTooltip();
                }
                if ("componentOrientation".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (SubstanceTitlePane.this.menuBar != null) {
                                SubstanceTitlePane.this.menuBar.applyComponentOrientation((ComponentOrientation)evt.getNewValue());
                            }
                        }
                    });
                }
            }
        };
        this.rootPane.addPropertyChangeListener(this.propertyListener);
        if (this.getFrame() != null) {
            this.getFrame().addPropertyChangeListener(this.propertyListener);
        }
    }

    private void uninstallListeners() {
        if (this.window != null) {
            this.window.removeWindowListener(this.windowListener);
            this.windowListener = null;
            this.window.removePropertyChangeListener(this.propertyChangeListener);
            this.propertyChangeListener = null;
        }
        this.rootPane.removePropertyChangeListener(this.propertyListener);
        if (this.getFrame() != null) {
            this.getFrame().removePropertyChangeListener(this.propertyListener);
        }
        this.propertyListener = null;
    }

    @Override
    public JRootPane getRootPane() {
        return this.rootPane;
    }

    protected int getWindowDecorationStyle() {
        return this.getRootPane().getWindowDecorationStyle();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.uninstallListeners();
        this.window = SwingUtilities.getWindowAncestor(this);
        if (this.window != null) {
            this.setActive(this.window.isActive());
            if (this.window instanceof Frame) {
                this.setState(((Frame)this.window).getExtendedState());
            } else {
                this.setState(0);
            }
            if (this.getComponentCount() == 0) {
                this.installSubcomponents();
            }
            this.installListeners();
        }
        this.setToolTipText(this.getTitle());
        this.updateAppIcon();
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        this.uninstall();
        this.window = null;
    }

    private void installSubcomponents() {
        int decorationStyle = this.getWindowDecorationStyle();
        if (decorationStyle == 1) {
            this.createActions();
            this.menuBar = this.createMenuBar();
            if (this.menuBar != null) {
                this.add(this.menuBar);
            }
            this.createButtons();
            this.add(this.minimizeButton);
            this.add(this.toggleButton);
            this.add(this.closeButton);
            this.heapStatusPanel = new HeapStatusPanel();
            this.markExtraComponent(this.heapStatusPanel, ExtraComponentKind.TRAILING);
            this.add(this.heapStatusPanel);
            boolean isHeapStatusPanelShowing = SubstanceWidgetManager.getInstance().isAllowed(this.rootPane, SubstanceConstants.SubstanceWidgetType.TITLE_PANE_HEAP_STATUS);
            this.heapStatusPanel.setVisible(isHeapStatusPanelShowing);
            this.heapStatusPanel.setPreferredSize(new Dimension(80, this.getPreferredSize().height));
            this.heapStatusPanel.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.rootPane).getString("Tooltip.heapStatusPanel"));
            this.heapStatusPanel.addMouseListener(new MouseAdapter(){

                @Override
                public void mouseClicked(MouseEvent e2) {
                    System.gc();
                }
            });
            HeapStatusThread.registerPanel(this.heapStatusPanel);
        } else if (decorationStyle == 2 || decorationStyle == 3 || decorationStyle == 4 || decorationStyle == 5 || decorationStyle == 6 || decorationStyle == 7 || decorationStyle == 8) {
            this.createActions();
            this.menuBar = this.createMenuBar();
            if (this.menuBar != null) {
                this.add(this.menuBar);
            }
            this.createButtons();
            this.add(this.closeButton);
        }
    }

    private void installDefaults() {
        this.setFont(UIManager.getFont("InternalFrame.titleFont", this.getLocale()));
    }

    protected JMenuBar createMenuBar() {
        this.menuBar = new SubstanceMenuBar();
        this.menuBar.setFocusable(false);
        this.menuBar.setBorderPainted(true);
        this.menuBar.add(this.createMenu());
        this.menuBar.setOpaque(false);
        this.menuBar.applyComponentOrientation(this.rootPane.getComponentOrientation());
        this.markExtraComponent(this.menuBar, ExtraComponentKind.LEADING);
        return this.menuBar;
    }

    private void createActions() {
        this.closeAction = new CloseAction();
        if (this.getWindowDecorationStyle() == 1) {
            this.iconifyAction = new IconifyAction();
            this.restoreAction = new RestoreAction();
            this.maximizeAction = new MaximizeAction();
        }
    }

    private JMenu createMenu() {
        JMenu menu = new JMenu("");
        menu.setOpaque(false);
        menu.setBackground(null);
        this.addMenuItems(menu);
        menu.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e2) {
                if (e2.getClickCount() > 1) {
                    SubstanceTitlePane.this.closeAction.actionPerformed(new ActionEvent(e2.getSource(), 1001, null, EventQueue.getMostRecentEventTime(), e2.getModifiers()));
                }
            }
        });
        return menu;
    }

    private void addMenuItems(JMenu menu) {
        if (this.getWindowDecorationStyle() == 1) {
            menu.add(this.restoreAction);
            menu.add(this.iconifyAction);
            if (Toolkit.getDefaultToolkit().isFrameStateSupported(6)) {
                menu.add(this.maximizeAction);
            }
            if (SubstanceCoreUtilities.toShowExtraWidgets(this.rootPane)) {
                menu.addSeparator();
                JMenu skinMenu = new JMenu(SubstanceCoreUtilities.getResourceBundle(this.rootPane).getString("SystemMenu.skins"));
                Map<String, SkinInfo> allSkins = SubstanceLookAndFeel.getAllSkins();
                for (Map.Entry<String, SkinInfo> skinEntry : allSkins.entrySet()) {
                    final String skinClassName = skinEntry.getValue().getClassName();
                    JMenuItem jmiSkin = new JMenuItem(skinEntry.getKey());
                    jmiSkin.addActionListener(new ActionListener(){

                        @Override
                        public void actionPerformed(ActionEvent e2) {
                            SwingUtilities.invokeLater(new Runnable(){

                                @Override
                                public void run() {
                                    SubstanceLookAndFeel.setSkin(skinClassName);
                                }
                            });
                        }
                    });
                    skinMenu.add(jmiSkin);
                }
                menu.add(skinMenu);
            }
            menu.addSeparator();
        }
        menu.add(this.closeAction);
    }

    private JButton createTitleButton() {
        SubstanceTitleButton button = new SubstanceTitleButton();
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setOpaque(true);
        this.markExtraComponent(button, ExtraComponentKind.TRAILING);
        return button;
    }

    private void createButtons() {
        this.closeButton = this.createTitleButton();
        this.closeButton.setAction(this.closeAction);
        this.closeButton.setText(null);
        this.closeButton.setBorder(null);
        TransitionAwareIcon closeIcon = new TransitionAwareIcon(this.closeButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                return SubstanceIconFactory.getTitlePaneIcon(SubstanceIconFactory.IconKind.CLOSE, scheme, SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE));
            }
        }, "substance.titlePane.closeIcon");
        this.closeButton.setIcon(closeIcon);
        this.closeButton.setFocusable(false);
        this.closeButton.putClientProperty("substancelaf.componentFlat", Boolean.TRUE);
        this.closeButton.putClientProperty("substancelaf.internal.isTitleCloseButton", Boolean.TRUE);
        if (this.getWindowDecorationStyle() == 1) {
            this.minimizeButton = this.createTitleButton();
            this.minimizeButton.setAction(this.iconifyAction);
            this.minimizeButton.setText(null);
            this.minimizeButton.setBorder(null);
            TransitionAwareIcon minIcon = new TransitionAwareIcon(this.minimizeButton, new TransitionAwareIcon.Delegate(){

                @Override
                public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                    return SubstanceIconFactory.getTitlePaneIcon(SubstanceIconFactory.IconKind.MINIMIZE, scheme, SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE));
                }
            }, "substance.titlePane.minIcon");
            this.minimizeButton.setIcon(minIcon);
            this.minimizeButton.setFocusable(false);
            this.minimizeButton.putClientProperty("substancelaf.componentFlat", Boolean.TRUE);
            this.minimizeButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.rootPane).getString("SystemMenu.iconify"));
            this.toggleButton = this.createTitleButton();
            this.toggleButton.setAction(this.restoreAction);
            this.toggleButton.setBorder(null);
            this.toggleButton.setText(null);
            TransitionAwareIcon maxIcon = new TransitionAwareIcon(this.toggleButton, new TransitionAwareIcon.Delegate(){

                @Override
                public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                    return SubstanceIconFactory.getTitlePaneIcon(SubstanceIconFactory.IconKind.MAXIMIZE, scheme, SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE));
                }
            }, "substance.titlePane.maxIcon");
            this.toggleButton.setIcon(maxIcon);
            this.toggleButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.rootPane).getString("SystemMenu.maximize"));
            this.toggleButton.setFocusable(false);
            this.toggleButton.putClientProperty("substancelaf.componentFlat", Boolean.TRUE);
        }
        this.syncCloseButtonTooltip();
    }

    protected LayoutManager createLayout() {
        return new TitlePaneLayout();
    }

    void setActive(boolean isActive) {
        if (this.getRootPane() != null) {
            this.getRootPane().repaint();
        }
    }

    private void setState(int state) {
        this.setState(state, false);
    }

    private void setState(int state, boolean updateRegardless) {
        Window w2 = this.getWindow();
        if (w2 != null && this.getWindowDecorationStyle() == 1) {
            if (this.state == state && !updateRegardless) {
                return;
            }
            Frame frame = this.getFrame();
            if (frame != null) {
                final JRootPane rootPane = this.getRootPane();
                if ((state & 6) != 0 && (rootPane.getBorder() == null || rootPane.getBorder() instanceof UIResource) && frame.isShowing()) {
                    rootPane.setBorder(null);
                } else if ((state & 6) == 0) {
                    this.rootPaneUI.installBorder(rootPane);
                }
                if (frame.isResizable()) {
                    if ((state & 6) != 0) {
                        TransitionAwareIcon restoreIcon = new TransitionAwareIcon(this.toggleButton, new TransitionAwareIcon.Delegate(){

                            @Override
                            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                                return SubstanceIconFactory.getTitlePaneIcon(SubstanceIconFactory.IconKind.RESTORE, scheme, SubstanceCoreUtilities.getSkin(rootPane).getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE));
                            }
                        }, "substance.titlePane.restoreIcon");
                        this.updateToggleButton(this.restoreAction, restoreIcon);
                        this.toggleButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(rootPane).getString("SystemMenu.restore"));
                        this.maximizeAction.setEnabled(false);
                        this.restoreAction.setEnabled(true);
                    } else {
                        TransitionAwareIcon maxIcon = new TransitionAwareIcon(this.toggleButton, new TransitionAwareIcon.Delegate(){

                            @Override
                            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                                return SubstanceIconFactory.getTitlePaneIcon(SubstanceIconFactory.IconKind.MAXIMIZE, scheme, SubstanceCoreUtilities.getSkin(rootPane).getBackgroundColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE));
                            }
                        }, "substance.titlePane.maxIcon");
                        this.updateToggleButton(this.maximizeAction, maxIcon);
                        this.toggleButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(rootPane).getString("SystemMenu.maximize"));
                        this.maximizeAction.setEnabled(true);
                        this.restoreAction.setEnabled(false);
                    }
                    if (this.toggleButton.getParent() == null || this.minimizeButton.getParent() == null) {
                        this.add(this.toggleButton);
                        this.add(this.minimizeButton);
                        this.revalidate();
                        this.repaint();
                    }
                    this.toggleButton.setText(null);
                } else {
                    this.maximizeAction.setEnabled(false);
                    this.restoreAction.setEnabled(false);
                    if (this.toggleButton.getParent() != null) {
                        this.remove(this.toggleButton);
                        this.revalidate();
                        this.repaint();
                    }
                }
            } else {
                this.maximizeAction.setEnabled(false);
                this.restoreAction.setEnabled(false);
                this.iconifyAction.setEnabled(false);
                this.remove(this.toggleButton);
                this.remove(this.minimizeButton);
                this.revalidate();
                this.repaint();
            }
            this.closeAction.setEnabled(true);
            this.state = state;
        }
    }

    private void updateToggleButton(Action action, Icon icon) {
        this.toggleButton.setAction(action);
        this.toggleButton.setIcon(icon);
        this.toggleButton.setText(null);
    }

    private Frame getFrame() {
        Window window = this.getWindow();
        if (window instanceof Frame) {
            return (Frame)window;
        }
        return null;
    }

    private Window getWindow() {
        return this.window;
    }

    private String getTitle() {
        Window w2 = this.getWindow();
        if (w2 instanceof Frame) {
            return ((Frame)w2).getTitle();
        }
        if (w2 instanceof Dialog) {
            return ((Dialog)w2).getTitle();
        }
        return null;
    }

    public DecorationAreaType getThisDecorationType() {
        DecorationAreaType dat = SubstanceLookAndFeel.getDecorationType(this);
        if (dat == DecorationAreaType.PRIMARY_TITLE_PANE) {
            return SubstanceCoreUtilities.isPaintRootPaneActivated(this.getRootPane()) ? DecorationAreaType.PRIMARY_TITLE_PANE : DecorationAreaType.PRIMARY_TITLE_PANE_INACTIVE;
        }
        if (dat == DecorationAreaType.SECONDARY_TITLE_PANE) {
            return SubstanceCoreUtilities.isPaintRootPaneActivated(this.getRootPane()) ? DecorationAreaType.SECONDARY_TITLE_PANE : DecorationAreaType.SECONDARY_TITLE_PANE_INACTIVE;
        }
        return dat;
    }

    @Override
    public void paintComponent(Graphics g2) {
        if (this.getFrame() != null) {
            this.setState(this.getFrame().getExtendedState());
        }
        JRootPane rootPane = this.getRootPane();
        Window window = this.getWindow();
        boolean leftToRight = window == null ? rootPane.getComponentOrientation().isLeftToRight() : window.getComponentOrientation().isLeftToRight();
        int width = this.getWidth();
        int height = this.getHeight();
        SubstanceSkin skin = SubstanceCoreUtilities.getSkin(rootPane);
        if (skin == null) {
            SubstanceCoreUtilities.traceSubstanceApiUsage(this, "Substance delegate used when Substance is not the current LAF");
        }
        DecorationAreaType decorationType = this.getThisDecorationType();
        SubstanceColorScheme scheme = skin.getEnabledColorScheme(decorationType);
        int xOffset = 0;
        String theTitle = this.getTitle();
        if (theTitle != null) {
            FontMetrics fm = rootPane.getFontMetrics(g2.getFont());
            Rectangle titleTextRect = this.getTitleTextRectangle(fm.stringWidth(theTitle));
            int titleWidth = titleTextRect.width;
            String clippedTitle = SubstanceCoreUtilities.clipString(fm, titleWidth, theTitle);
            if (theTitle.equals(clippedTitle)) {
                this.setToolTipText(null);
            } else {
                this.setToolTipText(theTitle);
            }
            theTitle = clippedTitle;
            xOffset = leftToRight ? titleTextRect.x : titleTextRect.x + titleTextRect.width - fm.stringWidth(theTitle);
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        FontUIResource font = SubstanceLookAndFeel.getFontPolicy().getFontSet("Substance", null).getWindowTitleFont();
        graphics.setFont(font);
        BackgroundPaintingUtils.update(graphics, this, false, decorationType);
        if (theTitle != null) {
            FontMetrics fm = rootPane.getFontMetrics(graphics.getFont());
            int yOffset = (height - fm.getHeight()) / 2 + fm.getAscent();
            SubstanceTextUtilities.paintTextWithDropShadow(this, graphics, SubstanceColorUtilities.getForegroundColor(scheme), theTitle, width, height, xOffset, yOffset);
        }
        GhostPaintingUtils.paintGhostImages(this, graphics);
        graphics.dispose();
    }

    protected Rectangle getTitleTextRectangle(int preferredWidth) {
        int end;
        int start;
        int maxTrailingX;
        boolean leftToRight;
        JRootPane rootPane = this.getRootPane();
        Window window = this.getWindow();
        boolean bl = leftToRight = window == null ? rootPane.getComponentOrientation().isLeftToRight() : window.getComponentOrientation().isLeftToRight();
        if (leftToRight) {
            int end2;
            int start2;
            int maxLeadingX = 0;
            int minTrailingX = this.getWidth();
            int maxMiddlingX = maxLeadingX;
            int minMiddlingX = minTrailingX;
            block10: for (int i2 = 0; i2 < this.getComponentCount(); ++i2) {
                Component child = this.getComponent(i2);
                if (!child.isVisible() || !(child instanceof JComponent)) continue;
                ExtraComponentKind kind = (ExtraComponentKind)((Object)((JComponent)child).getClientProperty(EXTRA_COMPONENT_KIND));
                if (kind == null) {
                    throw new IllegalStateException("Title pane child " + child.getClass().getName() + " is not marked as leading or trailing");
                }
                switch (kind) {
                    case LEADING: {
                        int cx = child.getX() + child.getWidth();
                        if (cx <= maxLeadingX) continue block10;
                        maxLeadingX = cx;
                        continue block10;
                    }
                    case MIDDLING: {
                        int cx = child.getX();
                        if (cx < minMiddlingX) {
                            minMiddlingX = cx;
                        }
                        if ((cx = child.getX() + child.getWidth()) <= maxMiddlingX) continue block10;
                        maxMiddlingX = cx;
                        continue block10;
                    }
                    case TRAILING: {
                        int cx = child.getX();
                        if (cx >= minTrailingX) continue block10;
                        minTrailingX = cx;
                    }
                }
            }
            minMiddlingX = Math.max(minMiddlingX, maxLeadingX);
            maxMiddlingX = Math.min(maxMiddlingX, minTrailingX);
            int leadWidth = minMiddlingX - maxLeadingX - 15;
            int trailWidth = minTrailingX - maxMiddlingX - 15;
            if (maxMiddlingX <= minMiddlingX) {
                start2 = maxLeadingX + 10;
                end2 = minTrailingX - 5;
            } else if (preferredWidth > leadWidth && (trailWidth > leadWidth || preferredWidth <= trailWidth)) {
                start2 = maxMiddlingX + 10;
                end2 = minTrailingX - 5;
            } else {
                start2 = maxLeadingX + 10;
                end2 = minMiddlingX - 5;
            }
            return new Rectangle(start2, 0, end2 - start2, this.getHeight());
        }
        int minLeadingX = this.getWidth();
        int maxMiddlingX = maxTrailingX = 0;
        int minMiddlingX = minLeadingX;
        block11: for (int i3 = 0; i3 < this.getComponentCount(); ++i3) {
            Component child = this.getComponent(i3);
            if (!child.isVisible() || !(child instanceof JComponent)) continue;
            ExtraComponentKind kind = (ExtraComponentKind)((Object)((JComponent)child).getClientProperty(EXTRA_COMPONENT_KIND));
            if (kind == null) {
                throw new IllegalStateException("Title pane child " + child.getClass().getName() + " is not marked as leading or trailing");
            }
            switch (kind) {
                case LEADING: {
                    int cx = child.getX();
                    if (cx >= minLeadingX) continue block11;
                    minLeadingX = cx;
                    continue block11;
                }
                case MIDDLING: {
                    int cx = child.getX();
                    if (cx < minMiddlingX) {
                        minMiddlingX = cx;
                    }
                    if ((cx = child.getX() + child.getWidth()) <= maxMiddlingX) continue block11;
                    maxMiddlingX = cx;
                    continue block11;
                }
                case TRAILING: {
                    int cx = child.getX() + child.getWidth();
                    if (cx <= maxTrailingX) continue block11;
                    maxTrailingX = cx;
                }
            }
        }
        minMiddlingX = Math.max(minMiddlingX, maxTrailingX);
        maxMiddlingX = Math.min(maxMiddlingX, minLeadingX);
        int leadWidth = minLeadingX - maxMiddlingX - 15;
        int trailWidth = minMiddlingX - maxTrailingX - 15;
        if (maxMiddlingX <= minMiddlingX) {
            start = maxTrailingX + 5;
            end = minLeadingX - 10;
        } else if (preferredWidth > leadWidth && (trailWidth > leadWidth || preferredWidth <= trailWidth)) {
            start = maxTrailingX + 10;
            end = minMiddlingX - 5;
        } else {
            start = maxMiddlingX + 5;
            end = minLeadingX - 10;
        }
        return new Rectangle(start, 0, end - start, this.getHeight());
    }

    public static void setHeapStatusLogfileName(String heapStatusLogfileName) {
        SubstanceTitlePane.heapStatusLogfileName = heapStatusLogfileName;
    }

    protected void syncCloseButtonTooltip() {
        if (SubstanceCoreUtilities.isRootPaneModified(this.getRootPane())) {
            this.closeButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.rootPane).getString("SystemMenu.close") + " [" + SubstanceCoreUtilities.getResourceBundle(this.rootPane).getString("Tooltip.contentsNotSaved") + "]");
        } else {
            this.closeButton.setToolTipText(SubstanceCoreUtilities.getResourceBundle(this.rootPane).getString("SystemMenu.close"));
        }
        this.closeButton.repaint();
    }

    protected void markExtraComponent(JComponent comp, ExtraComponentKind kind) {
        comp.putClientProperty(EXTRA_COMPONENT_KIND, (Object)kind);
    }

    private void updateAppIcon() {
        Window window = this.getWindow();
        if (window == null) {
            this.appIcon = null;
            return;
        }
        this.appIcon = null;
        while (window != null && this.appIcon == null) {
            List<Image> icons = window.getIconImages();
            if (icons.size() == 0) {
                window = window.getOwner();
                continue;
            }
            int prefSize = SubstanceSizeUtils.getTitlePaneIconSize();
            this.appIcon = SubstanceCoreUtilities.getScaledIconImage(icons, prefSize, prefSize);
        }
    }

    public AbstractButton getCloseButton() {
        return this.closeButton;
    }

    private class WindowHandler
    extends WindowAdapter {
        private WindowHandler() {
        }

        @Override
        public void windowActivated(WindowEvent ev) {
            SubstanceTitlePane.this.setActive(true);
        }

        @Override
        public void windowDeactivated(WindowEvent ev) {
            SubstanceTitlePane.this.setActive(false);
        }
    }

    private class PropertyChangeHandler
    implements PropertyChangeListener {
        private PropertyChangeHandler() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            String name = pce.getPropertyName();
            if ("resizable".equals(name) || "state".equals(name)) {
                Frame frame = SubstanceTitlePane.this.getFrame();
                if (frame != null) {
                    SubstanceTitlePane.this.setState(frame.getExtendedState(), true);
                }
                if ("resizable".equals(name)) {
                    SubstanceTitlePane.this.getRootPane().repaint();
                }
            } else if ("title".equals(name)) {
                SubstanceTitlePane.this.repaint();
                SubstanceTitlePane.this.setToolTipText((String)pce.getNewValue());
            } else if ("componentOrientation".equals(name)) {
                SubstanceTitlePane.this.revalidate();
                SubstanceTitlePane.this.repaint();
            } else if ("iconImage".equals(name)) {
                SubstanceTitlePane.this.updateAppIcon();
                SubstanceTitlePane.this.revalidate();
                SubstanceTitlePane.this.repaint();
            }
        }
    }

    protected class TitlePaneLayout
    implements LayoutManager {
        protected TitlePaneLayout() {
        }

        @Override
        public void addLayoutComponent(String name, Component c2) {
        }

        @Override
        public void removeLayoutComponent(Component c2) {
        }

        @Override
        public Dimension preferredLayoutSize(Container c2) {
            int height = this.computeHeight();
            return new Dimension(height, height);
        }

        private int computeHeight() {
            FontMetrics fm = SubstanceTitlePane.this.rootPane.getFontMetrics(SubstanceTitlePane.this.getFont());
            int fontHeight = fm.getHeight();
            int iconHeight = SubstanceSizeUtils.getTitlePaneIconSize();
            return Math.max(fontHeight += 7, iconHeight);
        }

        @Override
        public Dimension minimumLayoutSize(Container c2) {
            return this.preferredLayoutSize(c2);
        }

        @Override
        public void layoutContainer(Container c2) {
            int x2;
            int buttonWidth;
            int buttonHeight;
            boolean leftToRight = SubstanceTitlePane.this.window == null ? SubstanceTitlePane.this.getRootPane().getComponentOrientation().isLeftToRight() : SubstanceTitlePane.this.window.getComponentOrientation().isLeftToRight();
            int w2 = SubstanceTitlePane.this.getWidth();
            if (SubstanceTitlePane.this.closeButton != null && SubstanceTitlePane.this.closeButton.getIcon() != null) {
                buttonHeight = SubstanceTitlePane.this.closeButton.getIcon().getIconHeight();
                buttonWidth = SubstanceTitlePane.this.closeButton.getIcon().getIconWidth();
            } else {
                buttonHeight = SubstanceSizeUtils.getTitlePaneIconSize();
                buttonWidth = SubstanceSizeUtils.getTitlePaneIconSize();
            }
            int y2 = (SubstanceTitlePane.this.getHeight() - buttonHeight) / 2;
            int spacing = 5;
            int n2 = x2 = leftToRight ? spacing : w2 - buttonWidth - spacing;
            if (SubstanceTitlePane.this.menuBar != null) {
                SubstanceTitlePane.this.menuBar.setBounds(x2, y2, buttonWidth, buttonHeight);
            }
            x2 = leftToRight ? w2 : 0;
            spacing = 3;
            x2 += leftToRight ? -spacing - buttonWidth : spacing;
            if (SubstanceTitlePane.this.closeButton != null) {
                SubstanceTitlePane.this.closeButton.setBounds(x2, y2, buttonWidth, buttonHeight);
            }
            if (!leftToRight) {
                x2 += buttonWidth;
            }
            if (SubstanceTitlePane.this.getWindowDecorationStyle() == 1) {
                if (Toolkit.getDefaultToolkit().isFrameStateSupported(6) && SubstanceTitlePane.this.toggleButton.getParent() != null) {
                    spacing = 10;
                    SubstanceTitlePane.this.toggleButton.setBounds(x2 += leftToRight ? -spacing - buttonWidth : spacing, y2, buttonWidth, buttonHeight);
                    if (!leftToRight) {
                        x2 += buttonWidth;
                    }
                }
                if (SubstanceTitlePane.this.minimizeButton != null && SubstanceTitlePane.this.minimizeButton.getParent() != null) {
                    spacing = 2;
                    SubstanceTitlePane.this.minimizeButton.setBounds(x2 += leftToRight ? -spacing - buttonWidth : spacing, y2, buttonWidth, buttonHeight);
                    if (!leftToRight) {
                        x2 += buttonWidth;
                    }
                }
                if (SubstanceTitlePane.this.heapStatusPanel != null && SubstanceTitlePane.this.heapStatusPanel.isVisible()) {
                    spacing = 5;
                    SubstanceTitlePane.this.heapStatusPanel.setBounds(x2 += leftToRight ? -spacing - SubstanceTitlePane.this.heapStatusPanel.getPreferredWidth() : spacing, 1, SubstanceTitlePane.this.heapStatusPanel.getPreferredWidth(), SubstanceTitlePane.this.getHeight() - 3);
                }
            }
        }
    }

    public class SubstanceMenuBar
    extends JMenuBar {
        @Override
        public void paint(Graphics g2) {
            if (SubstanceTitlePane.this.appIcon != null) {
                g2.drawImage(SubstanceTitlePane.this.appIcon, 0, 0, null);
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

    private class MaximizeAction
    extends AbstractAction {
        public MaximizeAction() {
            super(SubstanceCoreUtilities.getResourceBundle(SubstanceTitlePane.this.rootPane).getString("SystemMenu.maximize"), SubstanceImageCreator.getMaximizeIcon(SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getActiveColorScheme(SubstanceTitlePane.this.getThisDecorationType()), SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getEnabledColorScheme(SubstanceTitlePane.this.getThisDecorationType())));
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            Frame frame = SubstanceTitlePane.this.getFrame();
            if (frame != null) {
                if (frame instanceof JFrame) {
                    SubstanceRootPaneUI rpUI = (SubstanceRootPaneUI)((JFrame)frame).getRootPane().getUI();
                    rpUI.setMaximized();
                }
                frame.setExtendedState(SubstanceTitlePane.this.state | 6);
            }
        }
    }

    private class RestoreAction
    extends AbstractAction {
        public RestoreAction() {
            super(SubstanceCoreUtilities.getResourceBundle(SubstanceTitlePane.this.rootPane).getString("SystemMenu.restore"), SubstanceImageCreator.getRestoreIcon(SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getActiveColorScheme(SubstanceTitlePane.this.getThisDecorationType()), SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getBackgroundColorScheme(SubstanceTitlePane.this.getThisDecorationType())));
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            Frame frame = SubstanceTitlePane.this.getFrame();
            if (frame == null) {
                return;
            }
            if ((SubstanceTitlePane.this.state & 1) != 0) {
                frame.setExtendedState(SubstanceTitlePane.this.state & 0xFFFFFFFE);
            } else {
                frame.setExtendedState(SubstanceTitlePane.this.state & 0xFFFFFFF9);
            }
        }
    }

    private class IconifyAction
    extends AbstractAction {
        public IconifyAction() {
            super(SubstanceCoreUtilities.getResourceBundle(SubstanceTitlePane.this.rootPane).getString("SystemMenu.iconify"), SubstanceImageCreator.getMinimizeIcon(SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getActiveColorScheme(SubstanceTitlePane.this.getThisDecorationType()), SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getBackgroundColorScheme(SubstanceTitlePane.this.getThisDecorationType())));
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            Frame frame = SubstanceTitlePane.this.getFrame();
            if (frame != null) {
                frame.setExtendedState(SubstanceTitlePane.this.state | 1);
            }
        }
    }

    private class CloseAction
    extends AbstractAction {
        public CloseAction() {
            super(SubstanceCoreUtilities.getResourceBundle(SubstanceTitlePane.this.rootPane).getString("SystemMenu.close"), SubstanceImageCreator.getCloseIcon(SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getActiveColorScheme(SubstanceTitlePane.this.getThisDecorationType()), SubstanceCoreUtilities.getSkin(SubstanceTitlePane.this.rootPane).getBackgroundColorScheme(SubstanceTitlePane.this.getThisDecorationType())));
        }

        @Override
        public void actionPerformed(ActionEvent e2) {
            Window window = SubstanceTitlePane.this.getWindow();
            if (window != null) {
                window.dispatchEvent(new WindowEvent(window, 201));
            }
        }
    }

    public static class HeapStatusThread
    extends TrackableThread {
        private int heapSizeKB;
        private int takenHeapSizeKB;
        private static Set<WeakReference<HeapStatusPanel>> panels = new HashSet<WeakReference<HeapStatusPanel>>();
        private static HeapStatusThread instance;
        private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        private boolean isStopRequested = false;

        private HeapStatusThread() {
            this.setName("Substance heap status");
        }

        public static synchronized HeapStatusThread getInstance() {
            if (instance == null) {
                instance = new HeapStatusThread();
                instance.start();
            }
            return instance;
        }

        public static synchronized void registerPanel(HeapStatusPanel panel) {
            panels.add(new WeakReference<HeapStatusPanel>(panel));
        }

        public static synchronized void unregisterPanel(HeapStatusPanel panel) {
            Iterator<WeakReference<HeapStatusPanel>> it = panels.iterator();
            while (it.hasNext()) {
                WeakReference<HeapStatusPanel> ref = it.next();
                HeapStatusPanel currPanel = (HeapStatusPanel)ref.get();
                if (panel != currPanel) continue;
                it.remove();
                return;
            }
        }

        private synchronized void updateHeapCounts() {
            long heapSize = Runtime.getRuntime().totalMemory();
            long heapFreeSize = Runtime.getRuntime().freeMemory();
            this.heapSizeKB = (int)(heapSize / 1024L);
            this.takenHeapSizeKB = (int)((heapSize - heapFreeSize) / 1024L);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void run() {
            while (!this.isStopRequested) {
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException ignore) {
                    // empty catch block
                }
                if (!SubstanceWidgetManager.getInstance().isAllowedAnywhere(SubstanceConstants.SubstanceWidgetType.TITLE_PANE_HEAP_STATUS)) continue;
                this.updateHeapCounts();
                Iterator<WeakReference<HeapStatusPanel>> it = panels.iterator();
                while (it.hasNext()) {
                    WeakReference<HeapStatusPanel> refPanel = it.next();
                    HeapStatusPanel panel = (HeapStatusPanel)refPanel.get();
                    if (panel == null) {
                        it.remove();
                        continue;
                    }
                    panel.updateStatus(this.heapSizeKB, this.takenHeapSizeKB);
                }
                if (heapStatusLogfileName == null) continue;
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(new FileWriter(heapStatusLogfileName, true));
                    pw.println(this.format.format(new Date()) + " " + this.takenHeapSizeKB + "KB / " + this.heapSizeKB + "KB");
                }
                catch (IOException iOException) {}
                continue;
                finally {
                    if (pw == null) continue;
                    pw.close();
                }
            }
        }

        @Override
        protected void requestStop() {
            this.isStopRequested = true;
            instance = null;
        }
    }

    public static class HeapStatusPanel
    extends JPanel {
        private int currHeapSizeKB;
        private int currTakenHeapSizeKB;
        private LinkedList<Double> graphValues = new LinkedList();

        public HeapStatusPanel() {
            HeapStatusThread.getInstance();
        }

        public synchronized void updateStatus(int currHeapSizeKB, int currTakenHeapSizeKB) {
            this.currHeapSizeKB = currHeapSizeKB;
            this.currTakenHeapSizeKB = currTakenHeapSizeKB;
            double newGraphValue = (double)currTakenHeapSizeKB / (double)currHeapSizeKB;
            this.graphValues.addLast(newGraphValue);
            this.repaint();
        }

        @Override
        public synchronized void paint(Graphics g2) {
            Graphics2D graphics = (Graphics2D)g2.create();
            SubstanceColorScheme scheme = SubstanceCoreUtilities.getSkin(this).getActiveColorScheme(DecorationAreaType.PRIMARY_TITLE_PANE);
            graphics.setColor(scheme.getDarkColor());
            int w2 = this.getWidth();
            int h2 = this.getHeight();
            graphics.drawRect(0, 0, w2 - 1, h2 - 1);
            graphics.setColor(scheme.getExtraLightColor());
            graphics.fillRect(1, 1, w2 - 2, h2 - 2);
            while (this.graphValues.size() > w2 - 2) {
                this.graphValues.removeFirst();
            }
            int xOff = w2 - this.graphValues.size() - 1;
            graphics.setColor(scheme.getMidColor());
            int count = 0;
            Iterator i$ = this.graphValues.iterator();
            while (i$.hasNext()) {
                double value = (Double)i$.next();
                int valueH = (int)(value * (double)(h2 - 2));
                graphics.drawLine(xOff + count, h2 - 1 - valueH, xOff + count, h2 - 2);
                ++count;
            }
            graphics.setFont(UIManager.getFont("Panel.font"));
            FontMetrics fm = graphics.getFontMetrics();
            StringBuffer longFormat = new StringBuffer();
            Formatter longFormatter = new Formatter(longFormat);
            longFormatter.format("%.1fMB / %.1fMB", Float.valueOf((float)this.currTakenHeapSizeKB / 1024.0f), Float.valueOf((float)this.currHeapSizeKB / 1024.0f));
            int strW = fm.stringWidth(longFormat.toString());
            int strH = fm.getAscent() + fm.getDescent();
            graphics.setColor(scheme.getForegroundColor());
            RenderingUtils.installDesktopHints(graphics, this);
            if (strW < w2 - 5) {
                graphics.drawString(longFormat.toString(), (w2 - strW) / 2, (h2 + strH) / 2 - 2);
            } else {
                String shortFormat = this.currTakenHeapSizeKB / 1024 + "MB / " + this.currHeapSizeKB / 1024 + "MB";
                strW = fm.stringWidth(shortFormat);
                graphics.drawString(shortFormat, (w2 - strW) / 2, (h2 + strH) / 2 - 2);
            }
            graphics.dispose();
        }

        public int getPreferredWidth() {
            BufferedImage dummy = new BufferedImage(1, 1, 2);
            Graphics2D g2d = dummy.createGraphics();
            RenderingUtils.installDesktopHints(g2d, this);
            g2d.setFont(UIManager.getFont("Panel.font"));
            FontMetrics fm = g2d.getFontMetrics();
            int result = fm.stringWidth("100.9MB / 200.9MB");
            g2d.dispose();
            return result;
        }
    }

    protected static enum ExtraComponentKind {
        LEADING,
        TRAILING,
        MIDDLING;

    }
}

