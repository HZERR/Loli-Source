/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.tabbed;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetSupport;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.LafWidgetUtilities2;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.JCarosel;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.JCarouselMenu;
import org.pushingpixels.lafwidget.contrib.blogofbug.swing.components.ReflectedImageLabel;
import org.pushingpixels.lafwidget.tabbed.TabPreviewControl;
import org.pushingpixels.lafwidget.tabbed.TabPreviewPainter;
import org.pushingpixels.lafwidget.tabbed.TabPreviewThread;
import org.pushingpixels.lafwidget.utils.LafConstants;
import org.pushingpixels.lafwidget.utils.ShadowPopupBorder;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

public class TabOverviewDialog
extends JDialog {
    protected JTabbedPane tabPane;
    protected TabPreviewThread.TabPreviewCallback previewCallback;
    protected PropertyChangeListener lafSwitchListener;

    private TabOverviewDialog(JTabbedPane tabPane, LafConstants.TabOverviewKind overviewKind, Frame owner, boolean modal, int dialogWidth, int dialogHeight) throws HeadlessException {
        super(owner, modal);
        this.tabPane = tabPane;
        this.setLayout(new BorderLayout());
        if (overviewKind == LafConstants.TabOverviewKind.GRID) {
            TabGridOverviewPanel gridOverviewPanel = new TabGridOverviewPanel(dialogWidth, dialogHeight);
            this.add((Component)gridOverviewPanel, "Center");
        }
        if (overviewKind == LafConstants.TabOverviewKind.ROUND_CAROUSEL) {
            this.add((Component)new TabRoundCarouselOverviewPanel(dialogWidth, dialogHeight), "Center");
        }
        if (overviewKind == LafConstants.TabOverviewKind.MENU_CAROUSEL) {
            this.add((Component)new TabMenuCarouselOverviewPanel(dialogWidth, dialogHeight), "Center");
        }
        this.setDefaultCloseOperation(2);
        this.setResizable(false);
        this.lafSwitchListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lookAndFeel".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            SwingUtilities.updateComponentTreeUI(TabOverviewDialog.this);
                        }
                    });
                }
            }
        };
        UIManager.addPropertyChangeListener(this.lafSwitchListener);
        this.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosing(WindowEvent e2) {
                this.cancelRequests();
                UIManager.removePropertyChangeListener(TabOverviewDialog.this.lafSwitchListener);
            }

            @Override
            public void windowClosed(WindowEvent e2) {
                this.cancelRequests();
                UIManager.removePropertyChangeListener(TabOverviewDialog.this.lafSwitchListener);
            }

            private void cancelRequests() {
                if (TabPreviewThread.instanceRunning()) {
                    TabPreviewThread.getInstance().cancelTabPreviewRequests(TabOverviewDialog.this);
                }
            }
        });
    }

    public static TabOverviewDialog getOverviewDialog(JTabbedPane tabPane) {
        final TabPreviewPainter previewPainter = LafWidgetUtilities2.getTabPreviewPainter(tabPane);
        String title = previewPainter.toUpdatePeriodically(tabPane) ? MessageFormat.format(LafWidgetUtilities.getResourceBundle(tabPane).getString("TabbedPane.overviewDialogTitleRefresh"), previewPainter.getUpdateCycle(tabPane) / 1000) : LafWidgetUtilities.getResourceBundle(tabPane).getString("TabbedPane.overviewDialogTitle");
        JFrame frameForModality = previewPainter.getModalOwner(tabPane);
        boolean isModal = frameForModality != null;
        Rectangle dialogScreenBounds = previewPainter.getPreviewDialogScreenBounds(tabPane);
        LafConstants.TabOverviewKind overviewKind = previewPainter.getOverviewKind(tabPane);
        final TabOverviewDialog overviewDialog = new TabOverviewDialog(tabPane, overviewKind, frameForModality, isModal, dialogScreenBounds.width, dialogScreenBounds.height);
        overviewDialog.setTitle(title);
        overviewDialog.setLocation(dialogScreenBounds.x, dialogScreenBounds.y);
        overviewDialog.setSize(dialogScreenBounds.width, dialogScreenBounds.height);
        final PropertyChangeListener activeWindowListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("activeWindow".equals(evt.getPropertyName()) && overviewDialog == evt.getOldValue() && previewPainter.toDisposeOverviewOnFocusLoss()) {
                    overviewDialog.dispose();
                }
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(activeWindowListener);
        final Window tabWindow = SwingUtilities.getWindowAncestor(tabPane);
        final WindowAdapter tabWindowListener = new WindowAdapter(){

            @Override
            public void windowClosed(WindowEvent e2) {
                overviewDialog.dispose();
            }
        };
        tabWindow.addWindowListener(tabWindowListener);
        overviewDialog.addWindowListener(new WindowAdapter(){

            @Override
            public void windowClosed(WindowEvent e2) {
                tabWindow.removeWindowListener(tabWindowListener);
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener(activeWindowListener);
            }
        });
        return overviewDialog;
    }

    public class TabGridOverviewGlassPane
    extends JPanel {
        private int currHoverIndex;
        private MouseListener[] mouseListeners;
        private TabGridOverviewPanel overviewPanel;

        public TabGridOverviewGlassPane(TabGridOverviewPanel overviewPanel) {
            this.setOpaque(false);
            this.overviewPanel = overviewPanel;
            int size = this.overviewPanel.previewControls.length;
            this.mouseListeners = new MouseListener[size];
            this.currHoverIndex = -1;
            for (int i2 = 0; i2 < size; ++i2) {
                int index = i2;
                this.mouseListeners[i2] = new RolloverMouseListener(index, overviewPanel);
                this.overviewPanel.previewControls[i2].addMouseListener(this.mouseListeners[i2]);
            }
        }

        @Override
        protected void paintComponent(Graphics g2) {
            Graphics2D graphics = (Graphics2D)g2.create();
            for (int i2 = 0; i2 < TabOverviewDialog.this.tabPane.getTabCount(); ++i2) {
                TabPreviewControl child = this.overviewPanel.previewControls[i2];
                if (!(child.getZoom() > 1.0f)) continue;
                this.paintSingleTabComponent(graphics, i2);
            }
            if (this.currHoverIndex >= 0) {
                this.paintSingleTabComponent(graphics, this.currHoverIndex);
            }
            graphics.dispose();
        }

        private void paintSingleTabComponent(Graphics2D graphics, int index) {
            int iy;
            TabPreviewControl child = this.overviewPanel.previewControls[index];
            Rectangle cBounds = child.getBounds();
            int dx = child.getLocationOnScreen().x - this.getLocationOnScreen().x;
            int dy = child.getLocationOnScreen().y - this.getLocationOnScreen().y;
            double factor = child.getZoom();
            int bw = (int)((double)cBounds.width * factor);
            int bh = (int)((double)cBounds.height * factor);
            BufferedImage bi = new BufferedImage(bw, bh, 2);
            Graphics2D bGraphics = (Graphics2D)bi.getGraphics().create();
            bGraphics.scale(factor, factor);
            TabPreviewControl tChild = child;
            bGraphics.setColor(tChild.getBackground());
            bGraphics.fillRect(0, 0, tChild.getWidth(), tChild.getHeight());
            Icon icon = TabOverviewDialog.this.tabPane.getIconAt(index);
            int n2 = iy = icon == null ? 16 : icon.getIconHeight();
            if (icon != null) {
                icon.paintIcon(this, bGraphics, 1, 1);
            }
            String title = TabOverviewDialog.this.tabPane.getTitleAt(index);
            JLabel tempLabel = new JLabel(title);
            tempLabel.setBounds(tChild.titleLabel.getBounds());
            tempLabel.setFont(tChild.titleLabel.getFont());
            int bdx = tempLabel.getX();
            int bdy = tempLabel.getY();
            bGraphics.translate(bdx, bdy);
            tempLabel.paint(bGraphics);
            bGraphics.translate(-bdx, -bdy);
            bdx = 1;
            bdy = iy + 3;
            bGraphics.translate(bdx, bdy);
            child.paintTabThumbnail(bGraphics);
            bGraphics.translate(-bdx, -bdy);
            bGraphics.setColor(Color.black);
            bGraphics.drawRect(0, 0, child.getWidth() - 1, child.getHeight() - 1);
            bGraphics.dispose();
            dx -= (bw - cBounds.width) / 2;
            dy -= (bh - cBounds.height) / 2;
            dx = Math.max(dx, 0);
            dy = Math.max(dy, 0);
            if (dx + bi.getWidth() > this.getWidth()) {
                dx -= dx + bi.getWidth() - this.getWidth();
            }
            if (dy + bi.getHeight() > this.getHeight()) {
                dy -= dy + bi.getHeight() - this.getHeight();
            }
            graphics.drawImage((Image)bi, dx, dy, null);
        }

        private final class RolloverMouseListener
        extends MouseAdapter {
            private final int index;
            private final TabGridOverviewPanel overviewPanel;
            private Timeline rolloverTimeline;

            private RolloverMouseListener(final int index, final TabGridOverviewPanel overviewPanel) {
                this.index = index;
                this.overviewPanel = overviewPanel;
                this.rolloverTimeline = new Timeline(overviewPanel.previewControls[index]);
                AnimationConfigurationManager.getInstance().configureTimeline(this.rolloverTimeline);
                this.rolloverTimeline.addPropertyToInterpolate("zoom", Float.valueOf(1.0f), Float.valueOf(1.2f));
                this.rolloverTimeline.addCallback(new SwingRepaintCallback(SwingUtilities.getRootPane(overviewPanel)));
                this.rolloverTimeline.addCallback(new UIThreadTimelineCallbackAdapter(){

                    @Override
                    public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
                        if (oldState == Timeline.TimelineState.DONE && newState == Timeline.TimelineState.IDLE) {
                            overviewPanel.previewControls[index].setToolTipText(LafWidgetUtilities.getResourceBundle(TabOverviewDialog.this.tabPane).getString("TabbedPane.overviewWidgetTooltip"));
                        }
                    }
                });
            }

            @Override
            public void mouseEntered(MouseEvent e2) {
                TabGridOverviewGlassPane.this.currHoverIndex = this.index;
                this.overviewPanel.previewControls[this.index].setToolTipText(null);
                this.rolloverTimeline.play();
            }

            @Override
            public void mouseExited(MouseEvent e2) {
                if (TabGridOverviewGlassPane.this.currHoverIndex == this.index) {
                    TabGridOverviewGlassPane.this.currHoverIndex = -1;
                }
                this.overviewPanel.previewControls[this.index].setToolTipText(null);
                this.rolloverTimeline.playReverse();
            }
        }
    }

    protected class TabGridOverviewPanel
    extends JPanel {
        protected TabPreviewControl[] previewControls;
        protected int pWidth;
        protected int pHeight;
        protected int colCount;
        protected TabGridOverviewGlassPane glassPane;

        public TabGridOverviewPanel(final int dialogWidth, final int dialogHeight) {
            TabPreviewThread.TabPreviewCallback previewCallback = new TabPreviewThread.TabPreviewCallback(){

                @Override
                public void start(JTabbedPane tabPane, int tabCount, TabPreviewThread.TabPreviewInfo tabPreviewInfo) {
                    int i2;
                    boolean isSame;
                    TabGridOverviewPanel.this.colCount = (int)Math.sqrt(tabCount);
                    if (TabGridOverviewPanel.this.colCount * TabGridOverviewPanel.this.colCount < tabCount) {
                        ++TabGridOverviewPanel.this.colCount;
                    }
                    TabGridOverviewPanel.this.pWidth = (dialogWidth - 8) / TabGridOverviewPanel.this.colCount;
                    TabGridOverviewPanel.this.pHeight = (dialogHeight - 32) / TabGridOverviewPanel.this.colCount;
                    tabPreviewInfo.setPreviewWidth(TabGridOverviewPanel.this.pWidth - 4);
                    tabPreviewInfo.setPreviewHeight(TabGridOverviewPanel.this.pHeight - 20);
                    boolean bl = isSame = TabGridOverviewPanel.this.previewControls != null && TabGridOverviewPanel.this.previewControls.length == tabCount;
                    if (isSame) {
                        return;
                    }
                    if (TabGridOverviewPanel.this.previewControls != null) {
                        for (int i3 = 0; i3 < TabGridOverviewPanel.this.previewControls.length; ++i3) {
                            TabGridOverviewPanel.this.remove(TabGridOverviewPanel.this.previewControls[i3]);
                        }
                    }
                    TabGridOverviewPanel.this.previewControls = new TabPreviewControl[tabCount];
                    TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter(TabOverviewDialog.this.tabPane);
                    for (i2 = 0; i2 < tabCount; ++i2) {
                        TabPreviewControl previewControl = new TabPreviewControl(TabOverviewDialog.this.tabPane, i2);
                        if (tpp.isSensitiveToEvents(TabOverviewDialog.this.tabPane, i2)) {
                            previewControl.addMouseListener(new TabPreviewMouseHandler(i2, previewControl, true, false));
                        }
                        TabGridOverviewPanel.this.previewControls[i2] = previewControl;
                        TabGridOverviewPanel.this.add(previewControl);
                    }
                    TabGridOverviewPanel.this.doLayout();
                    for (i2 = 0; i2 < tabCount; ++i2) {
                        TabGridOverviewPanel.this.previewControls[i2].revalidate();
                    }
                    TabGridOverviewPanel.this.repaint();
                    JRootPane rp = SwingUtilities.getRootPane(TabGridOverviewPanel.this);
                    TabGridOverviewPanel.this.glassPane = new TabGridOverviewGlassPane(TabGridOverviewPanel.this);
                    rp.setGlassPane(TabGridOverviewPanel.this.glassPane);
                    TabGridOverviewPanel.this.glassPane.setVisible(true);
                }

                @Override
                public void offer(JTabbedPane tabPane, int tabIndex, BufferedImage componentSnap) {
                    TabGridOverviewPanel.this.previewControls[tabIndex].setPreviewImage(componentSnap, true);
                }
            };
            this.setLayout(new TabGridOverviewPanelLayout());
            TabPreviewThread.TabPreviewInfo previewInfo = new TabPreviewThread.TabPreviewInfo();
            previewInfo.tabPane = TabOverviewDialog.this.tabPane;
            previewInfo.previewCallback = previewCallback;
            previewInfo.toPreviewAllTabs = true;
            previewInfo.initiator = TabOverviewDialog.this;
            TabPreviewThread.getInstance().queueTabPreviewRequest(previewInfo);
        }

        private class TabGridOverviewPanelLayout
        implements LayoutManager {
            private TabGridOverviewPanelLayout() {
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
            }

            @Override
            public void removeLayoutComponent(Component comp) {
            }

            @Override
            public void layoutContainer(Container parent) {
                if (TabGridOverviewPanel.this.previewControls == null) {
                    return;
                }
                for (int i2 = 0; i2 < TabGridOverviewPanel.this.previewControls.length; ++i2) {
                    TabPreviewControl previewControl = TabGridOverviewPanel.this.previewControls[i2];
                    if (previewControl == null) continue;
                    int rowIndex = i2 / TabGridOverviewPanel.this.colCount;
                    int colIndex = i2 % TabGridOverviewPanel.this.colCount;
                    previewControl.setBounds(colIndex * TabGridOverviewPanel.this.pWidth, rowIndex * TabGridOverviewPanel.this.pHeight, TabGridOverviewPanel.this.pWidth, TabGridOverviewPanel.this.pHeight);
                }
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return parent.getSize();
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return this.minimumLayoutSize(parent);
            }
        }
    }

    protected class TabMenuCarouselOverviewPanel
    extends JPanel {
        protected ReflectedImageLabel[] previewControls;
        protected int pWidth;
        protected int pHeight;
        protected JCarouselMenu caroselMenu;

        public TabMenuCarouselOverviewPanel(final int dialogWidth, final int dialogHeight) {
            TabPreviewThread.TabPreviewCallback previewCallback = new TabPreviewThread.TabPreviewCallback(){

                @Override
                public void start(JTabbedPane tabPane, int tabCount, TabPreviewThread.TabPreviewInfo tabPreviewInfo) {
                    boolean isSame;
                    boolean bl = isSame = TabMenuCarouselOverviewPanel.this.previewControls != null && TabMenuCarouselOverviewPanel.this.previewControls.length == tabCount;
                    if (isSame) {
                        return;
                    }
                    if (TabMenuCarouselOverviewPanel.this.previewControls != null) {
                        for (int i2 = 0; i2 < TabMenuCarouselOverviewPanel.this.previewControls.length; ++i2) {
                            TabMenuCarouselOverviewPanel.this.caroselMenu.remove(TabMenuCarouselOverviewPanel.this.previewControls[i2]);
                        }
                    }
                    double coef = Math.min(2.8, (double)tabCount / 1.8);
                    coef = Math.max(2.5, coef);
                    TabMenuCarouselOverviewPanel.this.pWidth = (int)((double)dialogWidth / coef);
                    TabMenuCarouselOverviewPanel.this.pHeight = (int)((double)dialogHeight / coef);
                    tabPreviewInfo.setPreviewWidth(TabMenuCarouselOverviewPanel.this.pWidth - 4);
                    tabPreviewInfo.setPreviewHeight(TabMenuCarouselOverviewPanel.this.pHeight - 4);
                    TabMenuCarouselOverviewPanel.this.previewControls = new ReflectedImageLabel[tabCount];
                    TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter(TabOverviewDialog.this.tabPane);
                    for (int i3 = 0; i3 < tabCount; ++i3) {
                        BufferedImage placeHolder = new BufferedImage(TabMenuCarouselOverviewPanel.this.pWidth, TabMenuCarouselOverviewPanel.this.pHeight, 2);
                        Graphics2D g2d = (Graphics2D)placeHolder.getGraphics();
                        g2d.setColor(UIManager.getColor("Label.background"));
                        g2d.fillRect(0, 0, TabMenuCarouselOverviewPanel.this.pWidth, TabMenuCarouselOverviewPanel.this.pHeight);
                        ReflectedImageLabel ril = (ReflectedImageLabel)TabMenuCarouselOverviewPanel.this.caroselMenu.add(placeHolder, tabPane.getTitleAt(i3));
                        ril.setForeground(UIManager.getColor("Label.foreground"));
                        ril.setBackground(UIManager.getColor("Label.background"));
                        ril.setPreferredSize(new Dimension(TabMenuCarouselOverviewPanel.this.pWidth, TabMenuCarouselOverviewPanel.this.pHeight));
                        if (tpp.isSensitiveToEvents(TabOverviewDialog.this.tabPane, i3)) {
                            ril.addMouseListener(new TabPreviewMouseHandler(i3, ril, false, true));
                            ril.setToolTipText(LafWidgetUtilities.getResourceBundle(tabPane).getString("TabbedPane.overviewWidgetTooltip"));
                        }
                        TabMenuCarouselOverviewPanel.this.previewControls[i3] = ril;
                    }
                    TabMenuCarouselOverviewPanel.this.caroselMenu.setSelectedIndex(tabPane.getSelectedIndex());
                }

                @Override
                public void offer(JTabbedPane tabPane, int tabIndex, BufferedImage componentSnap) {
                    int width = componentSnap.getWidth() + 4;
                    int height = componentSnap.getHeight() + 4;
                    BufferedImage result = new BufferedImage(width, height, 2);
                    Graphics2D g2d = (Graphics2D)result.getGraphics();
                    g2d.setColor(UIManager.getColor("Label.background"));
                    g2d.fillRect(0, 0, width, height);
                    g2d.setColor(UIManager.getColor("Label.foreground"));
                    g2d.drawRect(0, 0, width - 1, height - 1);
                    g2d.drawImage((Image)componentSnap, 2, 2, null);
                    Icon tabIcon = tabPane.getIconAt(tabIndex);
                    if (tabIcon != null) {
                        tabIcon.paintIcon(tabPane, g2d, 2, 2);
                    }
                    TabMenuCarouselOverviewPanel.this.previewControls[tabIndex].setRichImage(result);
                    TabMenuCarouselOverviewPanel.this.previewControls[tabIndex].repaint();
                }
            };
            this.caroselMenu = new JCarouselMenu(null);
            JList dummyList = new JList();
            ListCellRenderer lcr = dummyList.getCellRenderer();
            this.caroselMenu.setCellRenderer(new MenuCarouselListCellRenderer(lcr));
            this.caroselMenu.setMenuScrollColor(UIManager.getColor("Panel.background"));
            this.caroselMenu.setUpDownColor(UIManager.getColor("Label.foreground"));
            LafWidgetSupport support = LafWidgetRepository.getRepository().getLafSupport();
            if (support != null) {
                this.caroselMenu.setUpDownIcons(support.getArrowIcon(1), support.getArrowIcon(5));
            }
            this.setLayout(new BorderLayout());
            this.add((Component)this.caroselMenu, "Center");
            TabPreviewThread.TabPreviewInfo previewInfo = new TabPreviewThread.TabPreviewInfo();
            previewInfo.tabPane = TabOverviewDialog.this.tabPane;
            previewInfo.previewCallback = previewCallback;
            previewInfo.setPreviewWidth(this.pWidth - 4);
            previewInfo.setPreviewHeight(this.pHeight - 4);
            previewInfo.toPreviewAllTabs = true;
            previewInfo.initiator = TabOverviewDialog.this;
            TabPreviewThread.getInstance().queueTabPreviewRequest(previewInfo);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            if (this.caroselMenu != null) {
                JList dummyList = new JList();
                ListCellRenderer lcr = dummyList.getCellRenderer();
                this.caroselMenu.setCellRenderer(new MenuCarouselListCellRenderer(lcr));
                this.caroselMenu.setMenuScrollColor(UIManager.getColor("Panel.background"));
                this.caroselMenu.setUpDownColor(UIManager.getColor("Label.foreground"));
                this.caroselMenu.setBackground(UIManager.getColor("Panel.background"));
                LafWidgetSupport support = LafWidgetRepository.getRepository().getLafSupport();
                if (support != null) {
                    this.caroselMenu.setUpDownIcons(support.getArrowIcon(1), support.getArrowIcon(5));
                }
            }
        }

        protected class MenuCarouselListCellRenderer
        extends JLabel
        implements ListCellRenderer {
            protected ListCellRenderer lafDefaultCellRenderer;

            public MenuCarouselListCellRenderer(ListCellRenderer lafDefaultCellRenderer) {
                this.lafDefaultCellRenderer = lafDefaultCellRenderer;
            }

            public Component getListCellRendererComponent(JList jList, Object object, int i2, boolean isSelected, boolean cellHasFocus) {
                JCarouselMenu.MenuItem item = (JCarouselMenu.MenuItem)object;
                Component result = this.lafDefaultCellRenderer.getListCellRendererComponent(jList, item.getLabel(), i2, isSelected, cellHasFocus);
                if (result instanceof Component) {
                    JComponent jc = (JComponent)result;
                    jc.setBorder(new EmptyBorder(5, 5, 5, 5));
                    jc.setFont(super.getFont().deriveFont(1, 14.0f));
                }
                return result;
            }
        }
    }

    protected class TabRoundCarouselOverviewPanel
    extends JPanel {
        protected ReflectedImageLabel[] previewControls;
        protected int pWidth;
        protected int pHeight;
        protected JCarosel carosel;

        public TabRoundCarouselOverviewPanel(final int dialogWidth, final int dialogHeight) {
            TabPreviewThread.TabPreviewCallback previewCallback = new TabPreviewThread.TabPreviewCallback(){

                @Override
                public void start(JTabbedPane tabPane, int tabCount, TabPreviewThread.TabPreviewInfo tabPreviewInfo) {
                    boolean isSame;
                    boolean bl = isSame = TabRoundCarouselOverviewPanel.this.previewControls != null && TabRoundCarouselOverviewPanel.this.previewControls.length == tabCount;
                    if (isSame) {
                        return;
                    }
                    if (TabRoundCarouselOverviewPanel.this.previewControls != null) {
                        for (int i2 = 0; i2 < TabRoundCarouselOverviewPanel.this.previewControls.length; ++i2) {
                            TabRoundCarouselOverviewPanel.this.carosel.remove(TabRoundCarouselOverviewPanel.this.previewControls[i2]);
                        }
                    }
                    double coef = Math.min(3.5, (double)tabCount / 1.5);
                    coef = Math.max(coef, 4.5);
                    TabRoundCarouselOverviewPanel.this.pWidth = (int)((double)dialogWidth / coef);
                    TabRoundCarouselOverviewPanel.this.pHeight = (int)((double)dialogHeight / coef);
                    tabPreviewInfo.setPreviewWidth(TabRoundCarouselOverviewPanel.this.pWidth - 4);
                    tabPreviewInfo.setPreviewHeight(TabRoundCarouselOverviewPanel.this.pHeight - 4);
                    TabRoundCarouselOverviewPanel.this.previewControls = new ReflectedImageLabel[tabCount];
                    TabPreviewPainter tpp = LafWidgetUtilities2.getTabPreviewPainter(TabOverviewDialog.this.tabPane);
                    for (int i3 = 0; i3 < tabCount; ++i3) {
                        BufferedImage placeHolder = new BufferedImage(TabRoundCarouselOverviewPanel.this.pWidth, TabRoundCarouselOverviewPanel.this.pHeight, 2);
                        Graphics2D g2d = (Graphics2D)placeHolder.getGraphics();
                        g2d.setColor(UIManager.getColor("Label.background"));
                        g2d.fillRect(0, 0, TabRoundCarouselOverviewPanel.this.pWidth, TabRoundCarouselOverviewPanel.this.pHeight);
                        ReflectedImageLabel ril = (ReflectedImageLabel)TabRoundCarouselOverviewPanel.this.carosel.add(placeHolder, tabPane.getTitleAt(i3));
                        ril.setForeground(UIManager.getColor("Label.foreground"));
                        ril.setBackground(UIManager.getColor("Label.background"));
                        ril.setPreferredSize(new Dimension(TabRoundCarouselOverviewPanel.this.pWidth, TabRoundCarouselOverviewPanel.this.pHeight));
                        if (tpp.isSensitiveToEvents(TabOverviewDialog.this.tabPane, i3)) {
                            ril.addMouseListener(new TabPreviewMouseHandler(i3, ril, false, true));
                            ril.setToolTipText(LafWidgetUtilities.getResourceBundle(tabPane).getString("TabbedPane.overviewWidgetTooltip"));
                        }
                        TabRoundCarouselOverviewPanel.this.previewControls[i3] = ril;
                    }
                    TabRoundCarouselOverviewPanel.this.carosel.bringToFront(TabRoundCarouselOverviewPanel.this.previewControls[tabPane.getSelectedIndex()]);
                }

                @Override
                public void offer(JTabbedPane tabPane, int tabIndex, BufferedImage componentSnap) {
                    int width = componentSnap.getWidth() + 4;
                    int height = componentSnap.getHeight() + 4;
                    BufferedImage result = new BufferedImage(width, height, 2);
                    Graphics2D g2d = (Graphics2D)result.getGraphics();
                    g2d.setColor(UIManager.getColor("Label.background"));
                    g2d.fillRect(0, 0, width, height);
                    g2d.setColor(UIManager.getColor("Label.foreground"));
                    g2d.drawRect(0, 0, width - 1, height - 1);
                    g2d.drawImage((Image)componentSnap, 2, 2, null);
                    Icon tabIcon = tabPane.getIconAt(tabIndex);
                    if (tabIcon != null) {
                        tabIcon.paintIcon(tabPane, g2d, 2, 2);
                    }
                    TabRoundCarouselOverviewPanel.this.previewControls[tabIndex].setRichImage(result);
                    TabRoundCarouselOverviewPanel.this.previewControls[tabIndex].repaint();
                }
            };
            this.carosel = new JCarosel();
            this.carosel.setDepthBasedAlpha(true);
            this.setLayout(new BorderLayout());
            this.add((Component)this.carosel, "Center");
            TabPreviewThread.TabPreviewInfo previewInfo = new TabPreviewThread.TabPreviewInfo();
            previewInfo.tabPane = TabOverviewDialog.this.tabPane;
            previewInfo.previewCallback = previewCallback;
            previewInfo.toPreviewAllTabs = true;
            previewInfo.initiator = TabOverviewDialog.this;
            TabPreviewThread.getInstance().queueTabPreviewRequest(previewInfo);
        }
    }

    protected class TabPreviewMouseHandler
    extends MouseAdapter {
        private int index;
        private JComponent previewControl;
        private boolean useDoubleClick;
        private boolean hasRolloverBorderEffect;

        public TabPreviewMouseHandler(int index, JComponent previewControl, boolean hasRolloverBorderEffect, boolean useDoubleClick) {
            this.index = index;
            this.previewControl = previewControl;
            this.useDoubleClick = useDoubleClick;
            this.hasRolloverBorderEffect = hasRolloverBorderEffect;
        }

        @Override
        public void mouseClicked(MouseEvent e2) {
            if (this.useDoubleClick && e2.getClickCount() < 2) {
                return;
            }
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    TabOverviewDialog.this.dispose();
                    TabOverviewDialog.this.tabPane.setSelectedIndex(TabPreviewMouseHandler.this.index);
                }
            });
        }

        @Override
        public void mouseEntered(MouseEvent e2) {
            if (!this.hasRolloverBorderEffect) {
                return;
            }
            boolean isSelected = TabOverviewDialog.this.tabPane.getSelectedIndex() == this.index;
            LineBorder innerBorder = isSelected ? new LineBorder(Color.blue, 2) : new LineBorder(Color.black, 1);
            this.previewControl.setBorder(new CompoundBorder(new ShadowPopupBorder(), innerBorder));
        }

        @Override
        public void mouseExited(MouseEvent e2) {
            if (!this.hasRolloverBorderEffect) {
                return;
            }
            boolean isSelected = TabOverviewDialog.this.tabPane.getSelectedIndex() == this.index;
            LineBorder innerBorder = isSelected ? new LineBorder(Color.black, 2) : new LineBorder(Color.black, 1);
            this.previewControl.setBorder(new CompoundBorder(new ShadowPopupBorder(), innerBorder));
        }
    }
}

