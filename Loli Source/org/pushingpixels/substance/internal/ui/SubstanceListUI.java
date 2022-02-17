/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicListUI;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;
import org.pushingpixels.substance.internal.animation.StateTransitionMultiTracker;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.painter.HighlightPainterUtils;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceStripingUtils;
import org.pushingpixels.substance.internal.utils.UpdateOptimizationAware;
import org.pushingpixels.substance.internal.utils.UpdateOptimizationInfo;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

public class SubstanceListUI
extends BasicListUI
implements UpdateOptimizationAware {
    protected Set lafWidgets;
    protected Map<Integer, Object> selectedIndices = new HashMap<Integer, Object>();
    protected int rolledOverIndex = -1;
    protected PropertyChangeListener substancePropertyChangeListener;
    protected ListSelectionListener substanceListSelectionListener;
    protected RolloverFadeListener substanceFadeRolloverListener;
    private ComponentListener substanceComponentListener;
    private StateTransitionMultiTracker<Integer> stateTransitionMultiTracker = new StateTransitionMultiTracker();
    private ListDataListener substanceListDataListener;
    private UpdateOptimizationInfo updateInfo;

    public void __org__pushingpixels__substance__internal__ui__SubstanceListUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceListUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceListUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceListUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceListUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceListUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceListUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceListUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceListUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceListUI__installDefaults() {
        super.installDefaults();
        if (SubstanceCoreUtilities.toDrawWatermark(this.list)) {
            this.list.setOpaque(false);
        }
        this.syncModelContents();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceListUI__uninstallDefaults() {
        this.selectedIndices.clear();
        super.uninstallDefaults();
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceListUI__uninstallUI(JComponent c2) {
        this.stateTransitionMultiTracker.clear();
        super.uninstallUI(c2);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceListUI__installListeners() {
        super.installListeners();
        this.substanceListSelectionListener = new SubstanceListSelectionListener();
        this.list.getSelectionModel().addListSelectionListener(this.substanceListSelectionListener);
        this.substanceFadeRolloverListener = new RolloverFadeListener();
        this.list.addMouseMotionListener(this.substanceFadeRolloverListener);
        this.list.addMouseListener(this.substanceFadeRolloverListener);
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if ("substancelaf.watermark.visible".equals(evt.getPropertyName())) {
                    SubstanceListUI.this.list.setOpaque(!SubstanceCoreUtilities.toDrawWatermark(SubstanceListUI.this.list));
                }
                if ("model".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            ListModel oldModel = (ListModel)evt.getOldValue();
                            if (oldModel != null) {
                                oldModel.removeListDataListener(SubstanceListUI.this.substanceListDataListener);
                            }
                            ListModel newModel = (ListModel)evt.getNewValue();
                            SubstanceListUI.this.substanceListDataListener = new SubstanceListDataListener();
                            newModel.addListDataListener(SubstanceListUI.this.substanceListDataListener);
                            SubstanceListUI.this.syncModelContents();
                        }
                    });
                }
                if ("selectionModel".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            ListSelectionModel oldModel = (ListSelectionModel)evt.getOldValue();
                            if (oldModel != null) {
                                oldModel.removeListSelectionListener(SubstanceListUI.this.substanceListSelectionListener);
                            }
                            ListSelectionModel newModel = (ListSelectionModel)evt.getNewValue();
                            SubstanceListUI.this.substanceListSelectionListener = new SubstanceListSelectionListener();
                            newModel.addListSelectionListener(SubstanceListUI.this.substanceListSelectionListener);
                            SubstanceListUI.this.syncModelContents();
                        }
                    });
                }
            }
        };
        this.list.addPropertyChangeListener(this.substancePropertyChangeListener);
        this.substanceComponentListener = new ComponentAdapter(){

            @Override
            public void componentMoved(ComponentEvent e2) {
                SubstanceListUI.this.fadeOutRolloverIndication();
                SubstanceListUI.this.resetRolloverIndex();
            }
        };
        this.list.addComponentListener(this.substanceComponentListener);
        this.substanceListDataListener = new SubstanceListDataListener();
        this.list.getModel().addListDataListener(this.substanceListDataListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceListUI__uninstallListeners() {
        this.list.getModel().removeListDataListener(this.substanceListDataListener);
        this.substanceListDataListener = null;
        this.list.getSelectionModel().removeListSelectionListener(this.substanceListSelectionListener);
        this.substanceListSelectionListener = null;
        this.list.removeMouseMotionListener(this.substanceFadeRolloverListener);
        this.list.removeMouseListener(this.substanceFadeRolloverListener);
        this.substanceFadeRolloverListener = null;
        this.list.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        this.list.removeComponentListener(this.substanceComponentListener);
        this.substanceComponentListener = null;
        super.uninstallListeners();
    }

    @Override
    protected void paintCell(Graphics g2, int row, Rectangle rowBounds, ListCellRenderer cellRenderer, ListModel dataModel, ListSelectionModel selModel, int leadIndex) {
        JList.DropLocation dropLocation;
        Object value = dataModel.getElementAt(row);
        boolean cellHasFocus = this.list.hasFocus() && row == leadIndex;
        boolean isSelected = selModel.isSelectedIndex(row);
        Component rendererComponent = cellRenderer.getListCellRendererComponent(this.list, value, row, isSelected, cellHasFocus);
        if (!(rendererComponent instanceof SubstanceDefaultListCellRenderer)) {
            super.paintCell(g2, row, rowBounds, cellRenderer, dataModel, selModel, leadIndex);
            return;
        }
        boolean isWatermarkBleed = this.updateInfo.toDrawWatermark;
        int cx = rowBounds.x;
        int cy = rowBounds.y;
        int cw = rowBounds.width;
        int ch = rowBounds.height;
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.list, g2));
        if (!isWatermarkBleed) {
            Color background = rendererComponent.getBackground();
            if (background != null && (!this.list.getBackground().equals(background) || this.updateInfo.isInDecorationArea)) {
                g2d.setColor(background);
                g2d.fillRect(cx, cy, cw, ch);
            }
        } else {
            BackgroundPaintingUtils.fillAndWatermark(g2d, this.list, rendererComponent.getBackground(), new Rectangle(cx, cy, cw, ch));
        }
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.getModelStateInfo(row, rendererComponent);
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo == null ? null : modelStateInfo.getStateContributionMap();
        ComponentState currState = modelStateInfo == null ? this.getCellState(row, rendererComponent) : modelStateInfo.getCurrModelState();
        boolean hasHighlights = false;
        if (rendererComponent.isEnabled()) {
            if (activeStates != null) {
                Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry;
                Iterator<Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo>> i$ = activeStates.entrySet().iterator();
                while (i$.hasNext() && !(hasHighlights = this.updateInfo.getHighlightAlpha((stateEntry = i$.next()).getKey()) * stateEntry.getValue().getContribution() > 0.0f)) {
                }
            } else {
                boolean bl = hasHighlights = this.updateInfo.getHighlightAlpha(currState) > 0.0f;
            }
        }
        if ((dropLocation = this.list.getDropLocation()) != null && !dropLocation.isInsert() && dropLocation.getIndex() == row) {
            SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.list, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, currState);
            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.list, ColorSchemeAssociationKind.BORDER, currState);
            Rectangle cellRect = new Rectangle(cx, cy, cw, ch);
            HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, rendererComponent, cellRect, 0.8f, null, fillScheme, borderScheme);
        } else if (hasHighlights) {
            Rectangle cellRect = new Rectangle(cx, cy, cw, ch);
            if (activeStates == null) {
                float alpha = this.updateInfo.getHighlightAlpha(currState);
                if (alpha > 0.0f) {
                    SubstanceColorScheme fillScheme = this.updateInfo.getHighlightColorScheme(currState);
                    SubstanceColorScheme borderScheme = this.updateInfo.getHighlightBorderColorScheme(currState);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.list, alpha, g2));
                    HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, rendererComponent, cellRect, 0.8f, null, fillScheme, borderScheme);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.list, g2));
                }
            } else {
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry : activeStates.entrySet()) {
                    ComponentState activeState = stateEntry.getKey();
                    float alpha = this.updateInfo.getHighlightAlpha(activeState) * stateEntry.getValue().getContribution();
                    if (alpha == 0.0f) continue;
                    SubstanceColorScheme fillScheme = this.updateInfo.getHighlightColorScheme(activeState);
                    SubstanceColorScheme borderScheme = this.updateInfo.getHighlightBorderColorScheme(activeState);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.list, alpha, g2));
                    HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, rendererComponent, cellRect, 0.8f, null, fillScheme, borderScheme);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.list, g2));
                }
            }
        }
        this.rendererPane.paintComponent(g2d, rendererComponent, this.list, cx, cy, cw, ch, true);
        g2d.dispose();
    }

    public StateTransitionTracker getStateTransitionTracker(int row) {
        return this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)row);
    }

    public ComponentState getCellState(int cellIndex, Component rendererComponent) {
        StateTransitionTracker tracker;
        boolean isEnabled = this.list.isEnabled();
        if (rendererComponent != null) {
            boolean bl = isEnabled = isEnabled && rendererComponent.isEnabled();
        }
        if ((tracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)cellIndex)) == null) {
            boolean isRollover = this.rolledOverIndex >= 0 && this.rolledOverIndex == cellIndex;
            boolean isSelected = this.selectedIndices.containsKey(cellIndex);
            return ComponentState.getState(isEnabled, isRollover, isSelected);
        }
        ComponentState fromTracker = tracker.getModelStateInfo().getCurrModelState();
        return ComponentState.getState(isEnabled, fromTracker.isFacetActive(ComponentStateFacet.ROLLOVER), fromTracker.isFacetActive(ComponentStateFacet.SELECTION));
    }

    public StateTransitionTracker.ModelStateInfo getModelStateInfo(int row, Component rendererComponent) {
        if (this.stateTransitionMultiTracker.size() == 0) {
            return null;
        }
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)row);
        if (tracker == null) {
            return null;
        }
        return tracker.getModelStateInfo();
    }

    public void resetRolloverIndex() {
        this.rolledOverIndex = -1;
    }

    private void fadeOutRolloverIndication() {
        if (this.rolledOverIndex < 0) {
            return;
        }
        StateTransitionTracker tracker = this.getTracker(this.rolledOverIndex, true, this.list.isSelectedIndex(this.rolledOverIndex));
        tracker.getModel().setRollover(false);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceListUI__update(Graphics g2, JComponent c2) {
        BackgroundPaintingUtils.updateIfOpaque(g2, c2);
        Graphics2D g2d = (Graphics2D)g2.create();
        SubstanceStripingUtils.setup(c2);
        this.updateInfo = new UpdateOptimizationInfo(c2);
        this.paint(g2d, c2);
        SubstanceStripingUtils.tearDown(c2);
        g2d.dispose();
        this.updateInfo = null;
    }

    private void syncModelContents() {
        if (this.list == null) {
            return;
        }
        this.stateTransitionMultiTracker.clear();
        this.selectedIndices.clear();
        for (int i2 = 0; i2 < this.list.getModel().getSize(); ++i2) {
            if (!this.list.isSelectedIndex(i2)) continue;
            this.selectedIndices.put(i2, this.list.getModel().getElementAt(i2));
        }
        this.list.repaint();
    }

    private StateTransitionTracker getTracker(final int row, boolean initialRollover, boolean initialSelected) {
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)row);
        if (tracker == null) {
            DefaultButtonModel model = new DefaultButtonModel();
            model.setSelected(initialSelected);
            model.setRollover(initialRollover);
            tracker = new StateTransitionTracker(this.list, model);
            tracker.registerModelListeners();
            tracker.setRepaintCallback(new StateTransitionTracker.RepaintCallback(){

                @Override
                public TimelineCallback getRepaintCallback() {
                    return new CellRepaintCallback(SubstanceListUI.this.list, row);
                }
            });
            tracker.setName("row " + row);
            this.stateTransitionMultiTracker.addTracker((Comparable<Integer>)row, tracker);
        }
        return tracker;
    }

    @Override
    public UpdateOptimizationInfo getUpdateOptimizationInfo() {
        return this.updateInfo;
    }

    protected class CellRepaintCallback
    extends UIThreadTimelineCallbackAdapter {
        protected JList list;
        protected int cellIndex;

        public CellRepaintCallback(JList list, int cellIndex) {
            this.list = list;
            this.cellIndex = cellIndex;
        }

        @Override
        public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
            this.repaintCell();
        }

        @Override
        public void onTimelinePulse(float durationFraction, float timelinePosition) {
            this.repaintCell();
        }

        private void repaintCell() {
            if (SubstanceListUI.this.list == null) {
                return;
            }
            try {
                SubstanceListUI.this.maybeUpdateLayoutState();
                int cellCount = this.list.getModel().getSize();
                if (cellCount > 0 && this.cellIndex < cellCount) {
                    Rectangle rect = SubstanceListUI.this.getCellBounds(this.list, this.cellIndex, this.cellIndex);
                    this.list.repaint(rect);
                }
            }
            catch (RuntimeException re) {
                return;
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
        }

        @Override
        public void mousePressed(MouseEvent e2) {
        }

        @Override
        public void mouseReleased(MouseEvent e2) {
        }

        @Override
        public void mouseExited(MouseEvent e2) {
            SubstanceListUI.this.fadeOutRolloverIndication();
            SubstanceListUI.this.resetRolloverIndex();
        }

        @Override
        public void mouseMoved(MouseEvent e2) {
            if (!SubstanceListUI.this.list.isEnabled()) {
                return;
            }
            this.handleMove(e2);
        }

        @Override
        public void mouseDragged(MouseEvent e2) {
            if (!SubstanceListUI.this.list.isEnabled()) {
                return;
            }
            this.handleMove(e2);
        }

        private void handleMove(MouseEvent e2) {
            if (!(SubstanceListUI.this.list.getCellRenderer() instanceof SubstanceDefaultListCellRenderer)) {
                SubstanceListUI.this.fadeOutRolloverIndication();
                SubstanceListUI.this.resetRolloverIndex();
                return;
            }
            int roIndex = SubstanceListUI.this.list.locationToIndex(e2.getPoint());
            if (roIndex >= 0 && roIndex < SubstanceListUI.this.list.getModel().getSize() && !SubstanceListUI.this.list.getCellBounds(roIndex, roIndex).contains(e2.getPoint())) {
                roIndex = -1;
            }
            if (roIndex < 0 || roIndex >= SubstanceListUI.this.list.getModel().getSize()) {
                SubstanceListUI.this.fadeOutRolloverIndication();
                SubstanceListUI.this.resetRolloverIndex();
            } else {
                if (SubstanceListUI.this.rolledOverIndex >= 0 && SubstanceListUI.this.rolledOverIndex == roIndex) {
                    return;
                }
                SubstanceListUI.this.fadeOutRolloverIndication();
                StateTransitionTracker tracker = SubstanceListUI.this.getTracker(roIndex, false, SubstanceListUI.this.list.isSelectedIndex(roIndex));
                tracker.getModel().setRollover(true);
                SubstanceListUI.this.rolledOverIndex = roIndex;
            }
        }
    }

    private final class SubstanceListDataListener
    implements ListDataListener {
        private SubstanceListDataListener() {
        }

        private void _syncModelContents() {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    SubstanceListUI.this.syncModelContents();
                }
            });
        }

        @Override
        public void intervalRemoved(ListDataEvent e2) {
            this._syncModelContents();
        }

        @Override
        public void intervalAdded(ListDataEvent e2) {
            this._syncModelContents();
        }

        @Override
        public void contentsChanged(ListDataEvent e2) {
            this._syncModelContents();
        }
    }

    private class SubstanceListSelectionListener
    implements ListSelectionListener {
        private SubstanceListSelectionListener() {
        }

        @Override
        public void valueChanged(final ListSelectionEvent e2) {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    SubstanceListSelectionListener.this.handleListSelectionChange(e2);
                    if (SubstanceListUI.this.list != null) {
                        SubstanceListUI.this.list.repaint();
                    }
                }
            });
        }

        private void handleListSelectionChange(ListSelectionEvent e2) {
            if (SubstanceListUI.this.list == null) {
                return;
            }
            if (LafWidgetUtilities.hasNoAnimations(SubstanceListUI.this.list, AnimationFacet.SELECTION)) {
                return;
            }
            if (!(SubstanceListUI.this.list.getCellRenderer() instanceof SubstanceDefaultListCellRenderer)) {
                SubstanceListUI.this.syncModelContents();
                return;
            }
            HashSet<StateTransitionTracker> initiatedTrackers = new HashSet<StateTransitionTracker>();
            boolean fadeCanceled = false;
            for (int i2 = e2.getFirstIndex(); i2 <= e2.getLastIndex(); ++i2) {
                StateTransitionTracker tracker;
                if (i2 >= SubstanceListUI.this.list.getModel().getSize()) continue;
                if (SubstanceListUI.this.list.isSelectedIndex(i2)) {
                    if (SubstanceListUI.this.selectedIndices.containsKey(i2)) continue;
                    SubstanceListUI.this.selectedIndices.put(i2, SubstanceListUI.this.list.getModel().getElementAt(i2));
                    if (fadeCanceled) continue;
                    tracker = SubstanceListUI.this.getTracker(i2, i2 == SubstanceListUI.this.rolledOverIndex, false);
                    tracker.getModel().setSelected(true);
                    initiatedTrackers.add(tracker);
                    if (initiatedTrackers.size() <= 25) continue;
                    SubstanceListUI.this.stateTransitionMultiTracker.clear();
                    initiatedTrackers.clear();
                    fadeCanceled = true;
                    continue;
                }
                if (!SubstanceListUI.this.selectedIndices.containsKey(i2)) continue;
                if (SubstanceListUI.this.selectedIndices.get(i2) == SubstanceListUI.this.list.getModel().getElementAt(i2) && !fadeCanceled) {
                    tracker = SubstanceListUI.this.getTracker(i2, i2 == SubstanceListUI.this.rolledOverIndex, true);
                    tracker.getModel().setSelected(false);
                    initiatedTrackers.add(tracker);
                    if (initiatedTrackers.size() > 25) {
                        SubstanceListUI.this.stateTransitionMultiTracker.clear();
                        initiatedTrackers.clear();
                        fadeCanceled = true;
                    }
                }
                SubstanceListUI.this.selectedIndices.remove(i2);
            }
        }
    }
}

