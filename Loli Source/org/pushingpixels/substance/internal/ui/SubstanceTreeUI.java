/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.utils.LookUtils;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTreeCellRenderer;
import org.pushingpixels.substance.internal.animation.StateTransitionMultiTracker;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.painter.HighlightPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceStripingUtils;
import org.pushingpixels.substance.internal.utils.icon.SubstanceIconFactory;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

public class SubstanceTreeUI
extends BasicTreeUI {
    protected Set lafWidgets;
    protected Map<TreePathId, Object> selectedPaths = new HashMap<TreePathId, Object>();
    protected TreePathId currRolloverPathId;
    protected PropertyChangeListener substancePropertyChangeListener;
    protected TreeSelectionListener substanceSelectionFadeListener;
    protected RolloverFadeListener substanceFadeRolloverListener;
    protected MouseListener substanceRowSelectionListener;
    private StateTransitionMultiTracker<TreePathId> stateTransitionMultiTracker = new StateTransitionMultiTracker();
    private SubstanceColorScheme currDefaultColorScheme;
    private Insets cellRendererInsets;

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTreeUI__installComponents() {
        super.installComponents();
    }

    @Override
    protected void installComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTreeUI__installComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installComponents();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTreeUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceTreeUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTreeUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTreeUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTreeUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTreeUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTreeUI__uninstallComponents() {
        super.uninstallComponents();
    }

    @Override
    protected void uninstallComponents() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTreeUI__uninstallComponents();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallComponents();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTreeUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTreeUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceTreeUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceTreeUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTreeUI__installDefaults() {
        super.installDefaults();
        if (SubstanceCoreUtilities.toDrawWatermark(this.tree)) {
            this.tree.setOpaque(false);
        }
        if (this.tree.getSelectionPaths() != null) {
            for (TreePath selectionPath : this.tree.getSelectionPaths()) {
                TreePathId pathId = new TreePathId(selectionPath);
                this.selectedPaths.put(pathId, selectionPath.getLastPathComponent());
            }
        }
        this.setExpandedIcon(new IconUIResource(SubstanceIconFactory.getTreeIcon(this.tree, false)));
        this.setCollapsedIcon(new IconUIResource(SubstanceIconFactory.getTreeIcon(this.tree, true)));
        this.cellRendererInsets = SubstanceSizeUtils.getTreeCellRendererInsets(SubstanceSizeUtils.getComponentFontSize(this.tree));
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTreeUI__uninstallDefaults() {
        this.selectedPaths.clear();
        super.uninstallDefaults();
    }

    @Override
    protected void paintRow(Graphics g2, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        boolean newOpaque;
        int leadIndex;
        if (this.editingComponent != null && this.editingRow == row && this.shouldPaintExpandControl(path, row, isExpanded, hasBeenExpanded, isLeaf)) {
            if (!this.tree.getComponentOrientation().isLeftToRight() && LookUtils.IS_JAVA_5) {
                bounds.x -= 4;
            }
            this.paintExpandControlEnforce(g2, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
        }
        if (this.tree.hasFocus()) {
            TreePath leadPath = this.tree.getLeadSelectionPath();
            leadIndex = this.getRowForPath(this.tree, leadPath);
        } else {
            leadIndex = -1;
        }
        Component renderer = this.currentCellRenderer.getTreeCellRendererComponent(this.tree, path.getLastPathComponent(), this.tree.isRowSelected(row), isExpanded, isLeaf, row, leadIndex == row);
        if (!(renderer instanceof SubstanceDefaultTreeCellRenderer)) {
            super.paintRow(g2, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
            if (this.shouldPaintExpandControl(path, row, isExpanded, hasBeenExpanded, isLeaf)) {
                this.paintExpandControlEnforce(g2, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
            }
            return;
        }
        TreePathId pathId = new TreePathId(path);
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.tree, g2));
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.getModelStateInfo(pathId);
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo == null ? null : modelStateInfo.getStateContributionMap();
        ComponentState currState = modelStateInfo == null ? this.getPathState(pathId) : modelStateInfo.getCurrModelState();
        boolean hasHighlights = false;
        if (renderer.isEnabled()) {
            if (activeStates != null) {
                Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry;
                Iterator<Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo>> i$ = activeStates.entrySet().iterator();
                while (i$.hasNext() && !(hasHighlights = SubstanceColorSchemeUtilities.getHighlightAlpha(this.tree, (stateEntry = i$.next()).getKey()) * stateEntry.getValue().getContribution() > 0.0f)) {
                }
            } else {
                hasHighlights = SubstanceColorSchemeUtilities.getHighlightAlpha(this.tree, currState) > 0.0f;
            }
        }
        JTree.DropLocation dropLocation = this.tree.getDropLocation();
        Rectangle rowRectangle = new Rectangle(this.tree.getInsets().left, bounds.y, this.tree.getWidth() - this.tree.getInsets().right - this.tree.getInsets().left, bounds.height);
        if (dropLocation != null && dropLocation.getChildIndex() == -1 && this.tree.getRowForPath(dropLocation.getPath()) == row) {
            SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.tree, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, currState);
            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tree, ColorSchemeAssociationKind.BORDER, currState);
            HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, renderer, rowRectangle, 0.8f, null, scheme, borderScheme);
        } else if (hasHighlights) {
            if (activeStates == null) {
                float alpha = SubstanceColorSchemeUtilities.getHighlightAlpha(this.tree, currState);
                if (alpha > 0.0f) {
                    SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tree, ColorSchemeAssociationKind.HIGHLIGHT, currState);
                    SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tree, ColorSchemeAssociationKind.HIGHLIGHT, currState);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.tree, alpha, g2));
                    HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, renderer, rowRectangle, 0.8f, null, fillScheme, borderScheme);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.tree, g2));
                }
            } else {
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry : activeStates.entrySet()) {
                    ComponentState activeState = stateEntry.getKey();
                    float alpha = SubstanceColorSchemeUtilities.getHighlightAlpha(this.tree, activeState) * stateEntry.getValue().getContribution();
                    if (alpha == 0.0f) continue;
                    SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tree, ColorSchemeAssociationKind.HIGHLIGHT, activeState);
                    SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tree, ColorSchemeAssociationKind.HIGHLIGHT, activeState);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.tree, alpha, g2));
                    HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, renderer, rowRectangle, 0.8f, null, fillScheme, borderScheme);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.tree, g2));
                }
            }
        }
        JComponent jRenderer = (JComponent)renderer;
        boolean bl = newOpaque = !this.tree.isRowSelected(row);
        if (SubstanceCoreUtilities.toDrawWatermark(this.tree)) {
            newOpaque = false;
        }
        HashMap<Component, Boolean> opacity = new HashMap<Component, Boolean>();
        if (!newOpaque) {
            SubstanceCoreUtilities.makeNonOpaque(jRenderer, opacity);
        }
        this.rendererPane.paintComponent(g2d, renderer, this.tree, bounds.x, bounds.y, Math.max(this.tree.getWidth() - this.tree.getInsets().right - this.tree.getInsets().left - bounds.x, bounds.width), bounds.height, true);
        if (!newOpaque) {
            SubstanceCoreUtilities.restoreOpaque(jRenderer, opacity);
        }
        if (this.shouldPaintExpandControl(path, row, isExpanded, hasBeenExpanded, isLeaf)) {
            this.paintExpandControlEnforce(g2d, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
        }
        g2d.dispose();
    }

    @Override
    protected void paintExpandControl(Graphics g2, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
    }

    protected void paintExpandControlEnforce(Graphics g2, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        float alpha = SubstanceColorSchemeUtilities.getAlpha(this.tree, this.tree.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
        Graphics2D graphics = (Graphics2D)g2.create();
        graphics.setComposite(LafWidgetUtilities.getAlphaComposite(this.tree, alpha, g2));
        super.paintExpandControl(graphics, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
        graphics.dispose();
    }

    @Override
    protected void paintHorizontalPartOfLeg(Graphics g2, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
    }

    @Override
    protected void paintVerticalPartOfLeg(Graphics g2, Rectangle clipBounds, Insets insets, TreePath path) {
    }

    @Override
    protected TreeCellRenderer createDefaultCellRenderer() {
        return new SubstanceDefaultTreeCellRenderer();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTreeUI__installListeners() {
        super.installListeners();
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("substancelaf.watermark.visible".equals(evt.getPropertyName())) {
                    SubstanceTreeUI.this.tree.setOpaque(!SubstanceCoreUtilities.toDrawWatermark(SubstanceTreeUI.this.tree));
                }
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            SubstanceTreeUI.this.tree.updateUI();
                        }
                    });
                }
                if ("dropLocation".equals(evt.getPropertyName())) {
                    TreePath newDrop;
                    JTree.DropLocation currLocation;
                    JTree.DropLocation oldValue = (JTree.DropLocation)evt.getOldValue();
                    if (oldValue != null) {
                        TreePath oldDrop = oldValue.getPath();
                        Rectangle oldBounds = SubstanceTreeUI.this.getPathBounds(SubstanceTreeUI.this.tree, oldDrop);
                        SubstanceTreeUI.this.tree.repaint(0, oldBounds.y, SubstanceTreeUI.this.tree.getWidth(), oldBounds.height);
                    }
                    if ((currLocation = SubstanceTreeUI.this.tree.getDropLocation()) != null && (newDrop = currLocation.getPath()) != null) {
                        Rectangle newBounds = SubstanceTreeUI.this.getPathBounds(SubstanceTreeUI.this.tree, newDrop);
                        SubstanceTreeUI.this.tree.repaint(0, newBounds.y, SubstanceTreeUI.this.tree.getWidth(), newBounds.height);
                    }
                }
            }
        };
        this.tree.addPropertyChangeListener(this.substancePropertyChangeListener);
        this.substanceSelectionFadeListener = new MyTreeSelectionListener();
        this.tree.getSelectionModel().addTreeSelectionListener(this.substanceSelectionFadeListener);
        this.substanceRowSelectionListener = new RowSelectionListener();
        this.tree.addMouseListener(this.substanceRowSelectionListener);
        this.substanceFadeRolloverListener = new RolloverFadeListener();
        this.tree.addMouseMotionListener(this.substanceFadeRolloverListener);
        this.tree.addMouseListener(this.substanceFadeRolloverListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTreeUI__uninstallListeners() {
        this.tree.removeMouseListener(this.substanceRowSelectionListener);
        this.substanceRowSelectionListener = null;
        this.tree.getSelectionModel().removeTreeSelectionListener(this.substanceSelectionFadeListener);
        this.substanceSelectionFadeListener = null;
        this.tree.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        this.tree.removeMouseMotionListener(this.substanceFadeRolloverListener);
        this.tree.removeMouseListener(this.substanceFadeRolloverListener);
        this.substanceFadeRolloverListener = null;
        super.uninstallListeners();
    }

    public int getPivotRendererX(Rectangle paintBounds) {
        TreePath initialPath = this.getClosestPathForLocation(this.tree, 0, paintBounds.y);
        Enumeration<TreePath> paintingEnumerator = this.treeState.getVisiblePathsFrom(initialPath);
        int endY = paintBounds.y + paintBounds.height;
        int totalY = 0;
        int count = 0;
        if (initialPath != null && paintingEnumerator != null) {
            boolean done = false;
            Rectangle boundsBuffer = new Rectangle();
            Insets insets = this.tree.getInsets();
            while (!done && paintingEnumerator.hasMoreElements()) {
                TreePath path = paintingEnumerator.nextElement();
                if (path != null) {
                    Rectangle bounds = this.treeState.getBounds(path, boundsBuffer);
                    bounds.x += insets.left;
                    bounds.y += insets.top;
                    int currMedianX = bounds.x;
                    totalY += currMedianX;
                    ++count;
                    if (bounds.y + bounds.height < endY) continue;
                    done = true;
                    continue;
                }
                done = true;
            }
        }
        if (count == 0) {
            return -1;
        }
        return totalY / count - 2 * SubstanceSizeUtils.getTreeIconSize(SubstanceSizeUtils.getComponentFontSize(this.tree));
    }

    public ComponentState getPathState(TreePathId pathId) {
        boolean isEnabled = this.tree.isEnabled();
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker(pathId);
        if (tracker == null) {
            int rowIndex = this.tree.getRowForPath(pathId.path);
            boolean isRollover = this.currRolloverPathId != null && pathId.equals(this.currRolloverPathId);
            boolean isSelected = this.tree.isRowSelected(rowIndex);
            return ComponentState.getState(isEnabled, isRollover, isSelected);
        }
        ComponentState fromTracker = tracker.getModelStateInfo().getCurrModelState();
        return ComponentState.getState(isEnabled, fromTracker.isFacetActive(ComponentStateFacet.ROLLOVER), fromTracker.isFacetActive(ComponentStateFacet.SELECTION));
    }

    public StateTransitionTracker.ModelStateInfo getModelStateInfo(TreePathId pathId) {
        if (this.stateTransitionMultiTracker.size() == 0) {
            return null;
        }
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker(pathId);
        if (tracker == null) {
            return null;
        }
        return tracker.getModelStateInfo();
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTreeUI__update(Graphics g2, JComponent c2) {
        BackgroundPaintingUtils.updateIfOpaque(g2, c2);
        if (this.treeState == null) {
            return;
        }
        this.currDefaultColorScheme = SubstanceColorSchemeUtilities.getColorScheme(this.tree, ComponentState.ENABLED);
        Rectangle paintBounds = g2.getClipBounds();
        Insets insets = this.tree.getInsets();
        TreePath initialPath = this.getClosestPathForLocation(this.tree, 0, paintBounds.y);
        Enumeration<TreePath> paintingEnumerator = this.treeState.getVisiblePathsFrom(initialPath);
        int row = this.treeState.getRowForPath(initialPath);
        int endY = paintBounds.y + paintBounds.height;
        boolean isWatermarkBleed = SubstanceCoreUtilities.toDrawWatermark(this.tree) || !this.tree.isOpaque();
        Graphics2D g2d = (Graphics2D)g2.create();
        SubstanceStripingUtils.setup(c2);
        if (initialPath != null && paintingEnumerator != null) {
            boolean done = false;
            Rectangle boundsBuffer = new Rectangle();
            while (!done && paintingEnumerator.hasMoreElements()) {
                TreePath path = paintingEnumerator.nextElement();
                if (path != null) {
                    Rectangle bounds;
                    boolean isLeaf = this.treeModel.isLeaf(path.getLastPathComponent());
                    boolean isExpanded = isLeaf ? false : this.treeState.getExpandedState(path);
                    Component renderer = this.currentCellRenderer.getTreeCellRendererComponent(this.tree, path.getLastPathComponent(), this.tree.isRowSelected(row), isExpanded, isLeaf, row, this.tree.hasFocus() ? this.tree.getLeadSelectionRow() == row : false);
                    Color background = renderer.getBackground();
                    if (background == null) {
                        background = this.tree.getBackground();
                    }
                    if ((bounds = this.treeState.getBounds(path, boundsBuffer)) != null) {
                        bounds.x += insets.left;
                        bounds.y += insets.top;
                        if (!isWatermarkBleed) {
                            g2d.setColor(background);
                            g2d.fillRect(paintBounds.x, bounds.y, paintBounds.width, bounds.height);
                        } else if (this.tree.getComponentOrientation().isLeftToRight()) {
                            BackgroundPaintingUtils.fillAndWatermark(g2d, this.tree, background, new Rectangle(paintBounds.x, bounds.y, paintBounds.width, bounds.height));
                        } else {
                            BackgroundPaintingUtils.fillAndWatermark(g2d, this.tree, background, new Rectangle(paintBounds.x, bounds.y, paintBounds.width, bounds.height));
                        }
                        if (bounds.y + bounds.height >= endY) {
                            done = true;
                        }
                    }
                } else {
                    done = true;
                }
                ++row;
            }
        }
        this.paint(g2d, c2);
        SubstanceStripingUtils.tearDown(c2);
        g2d.dispose();
    }

    public SubstanceColorScheme getDefaultColorScheme() {
        return this.currDefaultColorScheme;
    }

    public Insets getCellRendererInsets() {
        return this.cellRendererInsets;
    }

    @Override
    public Rectangle getPathBounds(JTree tree, TreePath path) {
        Rectangle result = super.getPathBounds(tree, path);
        if (result != null && !tree.getComponentOrientation().isLeftToRight()) {
            int delta = result.x - tree.getInsets().left;
            result.x -= delta;
            result.width += delta;
        }
        return result;
    }

    private StateTransitionTracker getTracker(final TreePathId pathId, boolean initialRollover, boolean initialSelected) {
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker(pathId);
        if (tracker == null) {
            DefaultButtonModel model = new DefaultButtonModel();
            model.setSelected(initialSelected);
            model.setRollover(initialRollover);
            tracker = new StateTransitionTracker(this.tree, model);
            tracker.registerModelListeners();
            tracker.setRepaintCallback(new StateTransitionTracker.RepaintCallback(){

                @Override
                public TimelineCallback getRepaintCallback() {
                    return new PathRepaintCallback(SubstanceTreeUI.this.tree, pathId.path);
                }
            });
            this.stateTransitionMultiTracker.addTracker(pathId, tracker);
        }
        return tracker;
    }

    public StateTransitionTracker getStateTransitionTracker(TreePathId pathId) {
        return this.stateTransitionMultiTracker.getTracker(pathId);
    }

    private class RowSelectionListener
    extends MouseAdapter {
        private RowSelectionListener() {
        }

        @Override
        public void mousePressed(MouseEvent e2) {
            if (!SubstanceTreeUI.this.tree.isEnabled()) {
                return;
            }
            TreePath closestPath = SubstanceTreeUI.this.tree.getClosestPathForLocation(e2.getX(), e2.getY());
            if (closestPath == null) {
                return;
            }
            Rectangle bounds = SubstanceTreeUI.this.tree.getPathBounds(closestPath);
            if (e2.getY() >= bounds.y && e2.getY() < bounds.y + bounds.height && (e2.getX() < bounds.x || e2.getX() > bounds.x + bounds.width)) {
                if (SubstanceTreeUI.this.isLocationInExpandControl(closestPath, e2.getX(), e2.getY())) {
                    return;
                }
                SubstanceTreeUI.this.selectPathForEvent(closestPath, e2);
            }
        }
    }

    private class RolloverFadeListener
    implements MouseListener,
    MouseMotionListener {
        private RolloverFadeListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e2) {
        }

        @Override
        public void mouseEntered(MouseEvent e2) {
            if (!SubstanceTreeUI.this.tree.isEnabled()) {
                return;
            }
        }

        @Override
        public void mousePressed(MouseEvent e2) {
        }

        @Override
        public void mouseReleased(MouseEvent e2) {
        }

        @Override
        public void mouseExited(MouseEvent e2) {
            if (!SubstanceTreeUI.this.tree.isEnabled()) {
                return;
            }
            this.fadeOut();
            SubstanceTreeUI.this.currRolloverPathId = null;
        }

        @Override
        public void mouseMoved(MouseEvent e2) {
            if (!SubstanceTreeUI.this.tree.isEnabled()) {
                return;
            }
            this.handleMove(e2);
        }

        @Override
        public void mouseDragged(MouseEvent e2) {
            if (!SubstanceTreeUI.this.tree.isEnabled()) {
                return;
            }
            this.handleMove(e2);
        }

        private void handleMove(MouseEvent e2) {
            TreePath closestPath = SubstanceTreeUI.this.tree.getClosestPathForLocation(e2.getX(), e2.getY());
            Rectangle bounds = SubstanceTreeUI.this.tree.getPathBounds(closestPath);
            if (bounds == null) {
                this.fadeOut();
                SubstanceTreeUI.this.currRolloverPathId = null;
                return;
            }
            if (e2.getY() < bounds.y || e2.getY() > bounds.y + bounds.height) {
                this.fadeOut();
                SubstanceTreeUI.this.currRolloverPathId = null;
                return;
            }
            TreePathId newPathId = new TreePathId(closestPath);
            if (SubstanceTreeUI.this.currRolloverPathId != null && newPathId.equals(SubstanceTreeUI.this.currRolloverPathId)) {
                return;
            }
            this.fadeOut();
            StateTransitionTracker tracker = SubstanceTreeUI.this.getTracker(newPathId, false, SubstanceTreeUI.this.selectedPaths.containsKey(newPathId));
            tracker.getModel().setRollover(true);
            SubstanceTreeUI.this.currRolloverPathId = newPathId;
        }

        private void fadeOut() {
            if (SubstanceTreeUI.this.currRolloverPathId == null) {
                return;
            }
            StateTransitionTracker tracker = SubstanceTreeUI.this.getTracker(SubstanceTreeUI.this.currRolloverPathId, true, SubstanceTreeUI.this.selectedPaths.containsKey(SubstanceTreeUI.this.currRolloverPathId));
            tracker.getModel().setRollover(false);
        }
    }

    protected class PathRepaintCallback
    extends UIThreadTimelineCallbackAdapter {
        protected JTree tree;
        protected TreePath treePath;

        public PathRepaintCallback(JTree tree, TreePath treePath) {
            this.tree = tree;
            this.treePath = treePath;
        }

        @Override
        public void onTimelinePulse(float durationFraction, float timelinePosition) {
            this.repaintPath();
        }

        @Override
        public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
            this.repaintPath();
        }

        private void repaintPath() {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    if (SubstanceTreeUI.this.tree == null) {
                        return;
                    }
                    Rectangle boundsBuffer = new Rectangle();
                    Rectangle bounds = SubstanceTreeUI.this.treeState.getBounds(PathRepaintCallback.this.treePath, boundsBuffer);
                    if (bounds != null) {
                        bounds.x = 0;
                        bounds.width = PathRepaintCallback.this.tree.getWidth();
                        Insets insets = PathRepaintCallback.this.tree.getInsets();
                        bounds.x += insets.left;
                        bounds.y += insets.top;
                        PathRepaintCallback.this.tree.repaint(bounds);
                    }
                }
            });
        }
    }

    protected class MyTreeSelectionListener
    implements TreeSelectionListener {
        protected MyTreeSelectionListener() {
        }

        @Override
        public void valueChanged(TreeSelectionEvent e2) {
            if (SubstanceTreeUI.this.tree.getSelectionPaths() != null) {
                for (TreePath selectionPath : SubstanceTreeUI.this.tree.getSelectionPaths()) {
                    TreePathId pathId = new TreePathId(selectionPath);
                    if (SubstanceTreeUI.this.selectedPaths.containsKey(pathId)) continue;
                    StateTransitionTracker tracker = SubstanceTreeUI.this.getTracker(pathId, SubstanceTreeUI.this.currRolloverPathId != null && pathId.equals(SubstanceTreeUI.this.currRolloverPathId), false);
                    tracker.getModel().setSelected(true);
                    SubstanceTreeUI.this.selectedPaths.put(pathId, selectionPath.getLastPathComponent());
                }
            }
            Iterator<Map.Entry<TreePathId, Object>> it = SubstanceTreeUI.this.selectedPaths.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<TreePathId, Object> entry = it.next();
                if (SubstanceTreeUI.this.tree.getSelectionModel().isPathSelected(entry.getKey().path)) continue;
                TreePathId pathId = entry.getKey();
                StateTransitionTracker tracker = SubstanceTreeUI.this.getTracker(pathId, SubstanceTreeUI.this.currRolloverPathId != null && pathId.equals(SubstanceTreeUI.this.currRolloverPathId), true);
                tracker.getModel().setSelected(false);
                it.remove();
            }
        }
    }

    public static class TreePathId
    implements Comparable {
        protected TreePath path;

        public TreePathId(TreePath path) {
            this.path = path;
        }

        public int compareTo(Object o2) {
            if (o2 instanceof TreePathId) {
                Object[] path2Objs;
                TreePathId otherId = (TreePathId)o2;
                if (this.path == null && otherId.path != null) {
                    return 1;
                }
                if (otherId.path == null && this.path != null) {
                    return -1;
                }
                Object[] path1Objs = this.path.getPath();
                if (path1Objs.length != (path2Objs = otherId.path.getPath()).length) {
                    return 1;
                }
                for (int i2 = 0; i2 < path1Objs.length; ++i2) {
                    if (path1Objs[i2].equals(path2Objs[i2])) continue;
                    return 1;
                }
                return 0;
            }
            return -1;
        }

        public boolean equals(Object obj) {
            return this.compareTo(obj) == 0;
        }

        public int hashCode() {
            if (this.path == null) {
                return 0;
            }
            Object[] pathObjs = this.path.getPath();
            int result = pathObjs[0].hashCode();
            for (int i2 = 1; i2 < pathObjs.length; ++i2) {
                result ^= pathObjs[i2].hashCode();
            }
            return result;
        }
    }
}

