/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.table.JTableHeader;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.ui.SubstanceTableHeaderUI;
import org.pushingpixels.substance.internal.ui.SubstanceTreeUI;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.scroll.SubstanceScrollPaneBorder;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;
import org.pushingpixels.trident.ease.TimelineEase;

public class SubstanceScrollPaneUI
extends BasicScrollPaneUI {
    protected Set lafWidgets;
    protected PropertyChangeListener substancePropertyChangeListener;
    protected ChangeListener substanceVerticalScrollbarChangeListener;
    protected Timeline horizontalScrollTimeline;

    public void __org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners(JScrollPane jScrollPane) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__installListeners(jScrollPane);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults(JScrollPane jScrollPane) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__installDefaults(jScrollPane);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__uninstallListeners(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    @Override
    protected void uninstallDefaults(JScrollPane jScrollPane) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__uninstallDefaults(jScrollPane);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceScrollPaneUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__installDefaults(final JScrollPane scrollpane) {
        super.installDefaults(scrollpane);
        if (SubstanceCoreUtilities.toDrawWatermark(scrollpane) && SubstanceLookAndFeel.getCurrentSkin(scrollpane).getWatermark() != null) {
            scrollpane.setOpaque(false);
            scrollpane.getViewport().setOpaque(false);
        }
        scrollpane.setLayout(new AdjustedLayout((ScrollPaneLayout)scrollpane.getLayout()));
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                SubstanceScrollPaneUI.installTableHeaderCornerFiller(scrollpane);
            }
        });
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__uninstallDefaults(JScrollPane c2) {
        LayoutManager lm;
        Component upperLeft;
        Component upperRight = c2.getCorner("UPPER_RIGHT_CORNER");
        if (upperRight instanceof UIResource) {
            c2.setCorner("UPPER_RIGHT_CORNER", null);
        }
        if ((upperLeft = c2.getCorner("UPPER_LEFT_CORNER")) instanceof UIResource) {
            c2.setCorner("UPPER_LEFT_CORNER", null);
        }
        if ((lm = this.scrollpane.getLayout()) instanceof AdjustedLayout) {
            c2.setLayout(((AdjustedLayout)lm).delegate);
        }
        super.uninstallDefaults(c2);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__installListeners(final JScrollPane c2) {
        super.installListeners(c2);
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Color newBackgr;
                ScrollPaneLayout currLayout;
                if ("substancelaf.scrollPaneButtonsPolicy".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            c2.getHorizontalScrollBar().doLayout();
                            c2.getVerticalScrollBar().doLayout();
                        }
                    });
                }
                if ("substancelaf.watermark.visible".equals(evt.getPropertyName())) {
                    boolean toBleed = SubstanceCoreUtilities.toDrawWatermark(c2);
                    c2.setOpaque(!toBleed);
                    c2.getViewport().setOpaque(!toBleed);
                    Component view = c2.getViewport().getView();
                    if (view instanceof JComponent) {
                        ((JComponent)view).setOpaque(!toBleed);
                    }
                }
                if ("layoutManager".equals(evt.getPropertyName()) && ((Boolean)evt.getNewValue()).booleanValue() && !((currLayout = (ScrollPaneLayout)c2.getLayout()) instanceof AdjustedLayout)) {
                    c2.setLayout(new AdjustedLayout((ScrollPaneLayout)c2.getLayout()));
                }
                if ("background".equals(evt.getPropertyName()) && !((newBackgr = (Color)evt.getNewValue()) instanceof UIResource)) {
                    JScrollBar horizontal;
                    JScrollBar vertical = SubstanceScrollPaneUI.this.scrollpane.getVerticalScrollBar();
                    if (vertical != null && vertical.getBackground() instanceof UIResource) {
                        vertical.setBackground(newBackgr);
                    }
                    if ((horizontal = SubstanceScrollPaneUI.this.scrollpane.getHorizontalScrollBar()) != null && horizontal.getBackground() instanceof UIResource) {
                        horizontal.setBackground(newBackgr);
                    }
                }
                if ("columnHeader".equals(evt.getPropertyName()) || "componentOrientation".equals(evt.getPropertyName()) || "ancestor".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (SubstanceScrollPaneUI.this.scrollpane != null) {
                                SubstanceScrollPaneUI.installTableHeaderCornerFiller(SubstanceScrollPaneUI.this.scrollpane);
                            }
                        }
                    });
                }
            }
        };
        c2.addPropertyChangeListener(this.substancePropertyChangeListener);
        this.substanceVerticalScrollbarChangeListener = new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e2) {
                if (c2.getHorizontalScrollBar() != null && c2.getHorizontalScrollBar().isVisible() && c2.getViewport().getView() instanceof JTree) {
                    JTree tree = (JTree)c2.getViewport().getView();
                    if (AnimationConfigurationManager.getInstance().isAnimationAllowed(SubstanceLookAndFeel.TREE_SMART_SCROLL_ANIMATION_KIND, tree)) {
                        int finalDelta;
                        int currPivotX;
                        Rectangle viewportRect;
                        SubstanceTreeUI treeUI = (SubstanceTreeUI)tree.getUI();
                        int pivotX = treeUI.getPivotRendererX(viewportRect = c2.getViewport().getViewRect());
                        int delta = pivotX - (currPivotX = viewportRect.x);
                        int finalX = viewportRect.x + delta;
                        if (finalX < 0) {
                            delta -= finalX;
                        }
                        if (Math.abs(finalDelta = delta) > viewportRect.width / 6) {
                            if (SubstanceScrollPaneUI.this.horizontalScrollTimeline != null) {
                                SubstanceScrollPaneUI.this.horizontalScrollTimeline.abort();
                            }
                            SubstanceScrollPaneUI.this.horizontalScrollTimeline = new Timeline(tree);
                            SubstanceScrollPaneUI.this.horizontalScrollTimeline.addCallback(new UIThreadTimelineCallbackAdapter(){

                                @Override
                                public void onTimelinePulse(float durationFraction, float timelinePosition) {
                                    if ((double)timelinePosition >= 0.5) {
                                        int nudge = (int)((double)finalDelta * ((double)timelinePosition - 0.5));
                                        c2.getViewport().setViewPosition(new Point(viewportRect.x + nudge, viewportRect.y));
                                    }
                                }
                            });
                            SubstanceScrollPaneUI.this.horizontalScrollTimeline.setEase(new TimelineEase(){

                                @Override
                                public float map(float durationFraction) {
                                    if ((double)durationFraction < 0.5) {
                                        return 0.5f * durationFraction;
                                    }
                                    return 0.25f + (durationFraction - 0.5f) * 0.75f / 0.5f;
                                }
                            });
                            AnimationConfigurationManager.getInstance().configureTimeline(SubstanceScrollPaneUI.this.horizontalScrollTimeline);
                            SubstanceScrollPaneUI.this.horizontalScrollTimeline.setDuration(2L * SubstanceScrollPaneUI.this.horizontalScrollTimeline.getDuration());
                            SubstanceScrollPaneUI.this.horizontalScrollTimeline.play();
                        }
                    }
                }
            }
        };
        c2.getVerticalScrollBar().getModel().addChangeListener(this.substanceVerticalScrollbarChangeListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__uninstallListeners(JComponent c2) {
        c2.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        JScrollPane jsp = (JScrollPane)c2;
        jsp.getVerticalScrollBar().getModel().removeChangeListener(this.substanceVerticalScrollbarChangeListener);
        this.substanceVerticalScrollbarChangeListener = null;
        super.uninstallListeners(c2);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceScrollPaneUI__update(Graphics g2, JComponent c2) {
        BackgroundPaintingUtils.updateIfOpaque(g2, c2);
        JScrollPane jsp = (JScrollPane)c2;
        LayoutManager lm = jsp.getLayout();
        ScrollPaneLayout scrollLm = null;
        if (lm instanceof ScrollPaneLayout) {
            scrollLm = (ScrollPaneLayout)lm;
        }
        if (scrollLm != null) {
            HashSet<Component> corners = new HashSet<Component>();
            if (scrollLm.getCorner("LOWER_LEFT_CORNER") != null) {
                corners.add(scrollLm.getCorner("LOWER_LEFT_CORNER"));
            }
            if (scrollLm.getCorner("LOWER_RIGHT_CORNER") != null) {
                corners.add(scrollLm.getCorner("LOWER_RIGHT_CORNER"));
            }
            if (scrollLm.getCorner("UPPER_LEFT_CORNER") != null) {
                corners.add(scrollLm.getCorner("UPPER_LEFT_CORNER"));
            }
            if (scrollLm.getCorner("UPPER_RIGHT_CORNER") != null) {
                corners.add(scrollLm.getCorner("UPPER_RIGHT_CORNER"));
            }
            if (SubstanceCoreUtilities.isOpaque(c2)) {
                for (Component corner : corners) {
                    BackgroundPaintingUtils.fillAndWatermark(g2, c2, c2.getBackground(), corner.getBounds());
                }
            }
        }
        super.paint(g2, c2);
    }

    protected static void installTableHeaderCornerFiller(JScrollPane scrollpane) {
        boolean canReplace;
        JViewport columnHeader = scrollpane.getColumnHeader();
        if (columnHeader == null) {
            return;
        }
        Component columnHeaderComp = columnHeader.getView();
        if (!(columnHeaderComp instanceof JTableHeader)) {
            return;
        }
        JTableHeader tableHeader = (JTableHeader)columnHeaderComp;
        TableHeaderUI tableHeaderUI = tableHeader.getUI();
        if (!(tableHeaderUI instanceof SubstanceTableHeaderUI)) {
            return;
        }
        SubstanceTableHeaderUI ui = (SubstanceTableHeaderUI)tableHeaderUI;
        JComponent scrollPaneCornerFiller = ui.getScrollPaneCornerFiller();
        String cornerKey = scrollpane.getComponentOrientation().isLeftToRight() ? "UPPER_RIGHT_CORNER" : "UPPER_LEFT_CORNER";
        Component cornerComp = scrollpane.getCorner(cornerKey);
        boolean bl = canReplace = cornerComp == null || cornerComp instanceof UIResource;
        if (canReplace) {
            scrollpane.setCorner(cornerKey, scrollPaneCornerFiller);
        }
    }

    protected static class AdjustedLayout
    extends ScrollPaneLayout
    implements UIResource {
        protected ScrollPaneLayout delegate;

        public AdjustedLayout(ScrollPaneLayout delegate) {
            this.delegate = delegate;
        }

        @Override
        public void addLayoutComponent(String s2, Component c2) {
            this.delegate.addLayoutComponent(s2, c2);
        }

        public boolean equals(Object obj) {
            return this.delegate.equals(obj);
        }

        @Override
        public JViewport getColumnHeader() {
            return this.delegate.getColumnHeader();
        }

        @Override
        public Component getCorner(String key) {
            return this.delegate.getCorner(key);
        }

        @Override
        public JScrollBar getHorizontalScrollBar() {
            return this.delegate.getHorizontalScrollBar();
        }

        @Override
        public int getHorizontalScrollBarPolicy() {
            return this.delegate.getHorizontalScrollBarPolicy();
        }

        @Override
        public JViewport getRowHeader() {
            return this.delegate.getRowHeader();
        }

        @Override
        public JScrollBar getVerticalScrollBar() {
            return this.delegate.getVerticalScrollBar();
        }

        @Override
        public int getVerticalScrollBarPolicy() {
            return this.delegate.getVerticalScrollBarPolicy();
        }

        @Override
        public JViewport getViewport() {
            return this.delegate.getViewport();
        }

        @Override
        public Rectangle getViewportBorderBounds(JScrollPane scrollpane) {
            return this.delegate.getViewportBorderBounds(scrollpane);
        }

        public int hashCode() {
            return this.delegate.hashCode();
        }

        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return this.delegate.minimumLayoutSize(parent);
        }

        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return this.delegate.preferredLayoutSize(parent);
        }

        @Override
        public void removeLayoutComponent(Component c2) {
            this.delegate.removeLayoutComponent(c2);
        }

        @Override
        public void setHorizontalScrollBarPolicy(int x2) {
            this.delegate.setHorizontalScrollBarPolicy(x2);
        }

        @Override
        public void setVerticalScrollBarPolicy(int x2) {
            this.delegate.setVerticalScrollBarPolicy(x2);
        }

        @Override
        public void syncWithScrollPane(JScrollPane sp) {
            this.delegate.syncWithScrollPane(sp);
        }

        public String toString() {
            return this.delegate.toString();
        }

        @Override
        public void layoutContainer(Container parent) {
            this.delegate.layoutContainer(parent);
            JScrollPane scrollPane = (JScrollPane)parent;
            Border border = scrollPane.getBorder();
            boolean toAdjust = border instanceof SubstanceScrollPaneBorder;
            if (toAdjust) {
                Rectangle hBounds;
                Rectangle vBounds;
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                JScrollBar horizontal = scrollPane.getHorizontalScrollBar();
                int borderDelta = (int)Math.floor((double)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(scrollPane)) / 2.0);
                int borderWidth = (int)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(scrollPane));
                int dx = 0;
                int dy = 0;
                int dw = 0;
                int dh = 0;
                if (scrollPane.getComponentOrientation().isLeftToRight()) {
                    if (vertical != null && vertical.isVisible()) {
                        vBounds = vertical.getBounds();
                        dw += 1 + borderDelta;
                        vertical.setBounds(vBounds.x + 1 + borderDelta, vBounds.y + 1 - 2 * borderWidth, vBounds.width, vBounds.height + 2 * borderWidth);
                    }
                    if (horizontal != null && horizontal.isVisible()) {
                        dh += 1 + borderDelta;
                        hBounds = horizontal.getBounds();
                        horizontal.setBounds(hBounds.x + (scrollPane.getRowHeader() == null ? 1 : 2) - 2 * borderWidth, hBounds.y + 1, hBounds.width + 2 * borderWidth, hBounds.height);
                    }
                    if (this.delegate.getCorner("LOWER_RIGHT_CORNER") != null) {
                        Rectangle lrBounds = this.delegate.getCorner("LOWER_RIGHT_CORNER").getBounds();
                        this.delegate.getCorner("LOWER_RIGHT_CORNER").setBounds(lrBounds.x + 1 + borderDelta, lrBounds.y + 1 + borderDelta, lrBounds.width, lrBounds.height);
                    }
                    if (this.delegate.getCorner("UPPER_RIGHT_CORNER") != null) {
                        Rectangle urBounds = this.delegate.getCorner("UPPER_RIGHT_CORNER").getBounds();
                        this.delegate.getCorner("UPPER_RIGHT_CORNER").setBounds(urBounds.x + 1 + borderDelta, urBounds.y + borderDelta, urBounds.width - 1, urBounds.height);
                    }
                } else {
                    if (vertical != null && vertical.isVisible()) {
                        dx -= 1 + borderDelta;
                        dw += 1 + borderDelta;
                        vBounds = vertical.getBounds();
                        vertical.setBounds(vBounds.x - 1 - borderDelta, vBounds.y - 1 - borderDelta, vBounds.width, vBounds.height + 2 * borderWidth);
                    }
                    if (horizontal != null && horizontal.isVisible()) {
                        dh += 1 + borderDelta;
                        hBounds = horizontal.getBounds();
                        horizontal.setBounds(hBounds.x - (scrollPane.getRowHeader() == null ? 1 : 2) - borderDelta, hBounds.y + 1 + borderDelta, hBounds.width + 2 * borderWidth, hBounds.height);
                    }
                    if (this.delegate.getCorner("LOWER_LEFT_CORNER") != null) {
                        Rectangle llBounds = this.delegate.getCorner("LOWER_LEFT_CORNER").getBounds();
                        this.delegate.getCorner("LOWER_LEFT_CORNER").setBounds(llBounds.x - 1 - borderDelta, llBounds.y - 1 - borderDelta, llBounds.width, llBounds.height);
                    }
                    if (this.delegate.getCorner("UPPER_LEFT_CORNER") != null) {
                        Rectangle ulBounds = this.delegate.getCorner("UPPER_LEFT_CORNER").getBounds();
                        this.delegate.getCorner("UPPER_LEFT_CORNER").setBounds(ulBounds.x - borderDelta, ulBounds.y - borderDelta, ulBounds.width - 1, ulBounds.height);
                    }
                }
                if (this.delegate.getViewport() != null) {
                    Rectangle vpBounds = this.delegate.getViewport().getBounds();
                    this.delegate.getViewport().setBounds(new Rectangle(vpBounds.x + dx, vpBounds.y + dy, vpBounds.width + dw, vpBounds.height + dh));
                }
                if (this.delegate.getColumnHeader() != null) {
                    Rectangle columnHeaderBounds = this.delegate.getColumnHeader().getBounds();
                    this.delegate.getColumnHeader().setBounds(new Rectangle(columnHeaderBounds.x + dx, columnHeaderBounds.y + dy, columnHeaderBounds.width + dw, columnHeaderBounds.height));
                }
            }
        }
    }
}

