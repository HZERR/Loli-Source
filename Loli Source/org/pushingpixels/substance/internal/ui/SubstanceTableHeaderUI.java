/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultButtonModel;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.pushingpixels.lafwidget.LafWidget;
import org.pushingpixels.lafwidget.LafWidgetRepository;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.lafwidget.animation.AnimationConfigurationManager;
import org.pushingpixels.lafwidget.animation.AnimationFacet;
import org.pushingpixels.lafwidget.utils.RenderingUtils;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableHeaderCellRenderer;
import org.pushingpixels.substance.internal.animation.StateTransitionMultiTracker;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.painter.HighlightPainterUtils;
import org.pushingpixels.substance.internal.ui.SubstanceTableUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

public class SubstanceTableHeaderUI
extends BasicTableHeaderUI {
    protected Set lafWidgets;
    protected TableHeaderListener substanceHeaderListener;
    protected TableCellRenderer defaultHeaderRenderer;
    protected Map<Integer, Object> selectedIndices;
    protected ListSelectionListener substanceFadeSelectionListener;
    private StateTransitionMultiTracker<Integer> stateTransitionMultiTracker = new StateTransitionMultiTracker();
    protected PropertyChangeListener substancePropertyChangeListener;

    public void __org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceTableHeaderUI();
    }

    public SubstanceTableHeaderUI() {
        this.selectedIndices = new HashMap<Integer, Object>();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__installListeners() {
        ListSelectionModel lsm;
        super.installListeners();
        TableColumnModel columnModel = this.header.getColumnModel();
        if (columnModel != null && (lsm = columnModel.getSelectionModel()) != null) {
            this.substanceHeaderListener = new TableHeaderListener(this);
            lsm.addListSelectionListener(this.substanceHeaderListener);
        }
        this.substanceFadeSelectionListener = new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e2) {
                if (SubstanceTableHeaderUI.this.header == null) {
                    return;
                }
                if (!SubstanceTableHeaderUI.this.header.getColumnModel().getColumnSelectionAllowed()) {
                    return;
                }
                JTable table = SubstanceTableHeaderUI.this.header.getTable();
                if (table == null) {
                    return;
                }
                TableUI ui = table.getUI();
                if (!(ui instanceof SubstanceTableUI)) {
                    SubstanceTableHeaderUI.this.stateTransitionMultiTracker.clear();
                    return;
                }
                SubstanceTableUI tableUI = (SubstanceTableUI)ui;
                if (!tableUI._hasSelectionAnimations()) {
                    SubstanceTableHeaderUI.this.stateTransitionMultiTracker.clear();
                    return;
                }
                HashSet<StateTransitionTracker> initiatedTrackers = new HashSet<StateTransitionTracker>();
                boolean fadeCanceled = false;
                TableColumnModel columnModel = SubstanceTableHeaderUI.this.header.getColumnModel();
                int size = columnModel.getColumnCount();
                ListSelectionModel lsm = columnModel.getSelectionModel();
                for (int i2 = e2.getFirstIndex(); i2 <= e2.getLastIndex(); ++i2) {
                    StateTransitionTracker tracker;
                    if (i2 >= size) continue;
                    if (lsm.isSelectedIndex(i2)) {
                        if (SubstanceTableHeaderUI.this.selectedIndices.containsKey(i2)) continue;
                        if (!fadeCanceled) {
                            tracker = SubstanceTableHeaderUI.this.getTracker(i2, SubstanceTableHeaderUI.this.getColumnState(i2).isFacetActive(ComponentStateFacet.ROLLOVER), false);
                            tracker.getModel().setSelected(true);
                            initiatedTrackers.add(tracker);
                            if (initiatedTrackers.size() > 15) {
                                SubstanceTableHeaderUI.this.stateTransitionMultiTracker.clear();
                                initiatedTrackers.clear();
                                fadeCanceled = true;
                            }
                        }
                        SubstanceTableHeaderUI.this.selectedIndices.put(i2, columnModel.getColumn(i2));
                        continue;
                    }
                    if (!SubstanceTableHeaderUI.this.selectedIndices.containsKey(i2)) continue;
                    if (SubstanceTableHeaderUI.this.selectedIndices.get(i2) == columnModel.getColumn(i2) && !fadeCanceled) {
                        tracker = SubstanceTableHeaderUI.this.getTracker(i2, SubstanceTableHeaderUI.this.getColumnState(i2).isFacetActive(ComponentStateFacet.ROLLOVER), true);
                        tracker.getModel().setSelected(false);
                        initiatedTrackers.add(tracker);
                        if (initiatedTrackers.size() > 15) {
                            SubstanceTableHeaderUI.this.stateTransitionMultiTracker.clear();
                            initiatedTrackers.clear();
                            fadeCanceled = true;
                        }
                    }
                    SubstanceTableHeaderUI.this.selectedIndices.remove(i2);
                }
            }
        };
        if (columnModel != null && (lsm = columnModel.getSelectionModel()) != null) {
            lsm.addListSelectionListener(this.substanceFadeSelectionListener);
        }
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("table".equals(evt.getPropertyName())) {
                    TableColumnModel oldModel = evt.getOldValue() instanceof JTable ? ((JTable)evt.getOldValue()).getColumnModel() : null;
                    TableColumnModel newModel = evt.getNewValue() instanceof JTable ? ((JTable)evt.getNewValue()).getColumnModel() : null;
                    SubstanceTableHeaderUI.this.processColumnModelChangeEvent(oldModel, newModel);
                }
            }
        };
        this.header.addPropertyChangeListener(this.substancePropertyChangeListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__installDefaults() {
        super.installDefaults();
        this.defaultHeaderRenderer = this.header.getDefaultRenderer();
        if (this.defaultHeaderRenderer instanceof UIResource) {
            this.header.setDefaultRenderer(new SubstanceDefaultTableHeaderCellRenderer());
        }
        for (int i2 = 0; i2 < this.header.getColumnModel().getColumnCount(); ++i2) {
            if (!this.header.getColumnModel().getSelectionModel().isSelectedIndex(i2)) continue;
            this.selectedIndices.put(i2, this.header.getColumnModel().getColumn(i2));
        }
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__uninstallListeners() {
        ListSelectionModel lsm;
        TableColumnModel columnModel = this.header.getColumnModel();
        if (columnModel != null && (lsm = columnModel.getSelectionModel()) != null) {
            lsm.removeListSelectionListener(this.substanceHeaderListener);
            this.substanceHeaderListener = null;
        }
        this.header.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        super.uninstallListeners();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__uninstallDefaults() {
        super.uninstallDefaults();
        this.selectedIndices.clear();
        if (this.header.getDefaultRenderer() instanceof SubstanceDefaultTableHeaderCellRenderer) {
            this.header.setDefaultRenderer(this.defaultHeaderRenderer);
            if (this.defaultHeaderRenderer instanceof Component) {
                SwingUtilities.updateComponentTreeUI((Component)((Object)this.defaultHeaderRenderer));
            }
        }
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        int columnWidth;
        int column;
        if (this.header.getColumnModel().getColumnCount() <= 0) {
            return;
        }
        boolean ltr = this.header.getComponentOrientation().isLeftToRight();
        Rectangle clip = g2.getClipBounds();
        Point left = clip.getLocation();
        Point right = new Point(clip.x + clip.width - 1, clip.y);
        TableColumnModel cm = this.header.getColumnModel();
        int[] selectedColumns = cm.getSelectedColumns();
        HashSet<Integer> selected = new HashSet<Integer>();
        for (int sel : selectedColumns) {
            selected.add(sel);
        }
        int cMin = this.header.columnAtPoint(ltr ? left : right);
        int cMax = this.header.columnAtPoint(ltr ? right : left);
        if (cMin == -1) {
            cMin = 0;
        }
        if (cMax == -1) {
            cMax = cm.getColumnCount() - 1;
        }
        TableColumn draggedColumn = this.header.getDraggedColumn();
        Rectangle cellRect = this.header.getHeaderRect(ltr ? cMin : cMax);
        if (ltr) {
            for (column = cMin; column <= cMax; ++column) {
                TableColumn aColumn = cm.getColumn(column);
                cellRect.width = columnWidth = aColumn.getWidth();
                if (aColumn != draggedColumn) {
                    this.paintCell(g2, cellRect, column, selected.contains(column));
                }
                cellRect.x += columnWidth;
            }
        } else {
            for (column = cMax; column >= cMin; --column) {
                TableColumn aColumn = cm.getColumn(column);
                cellRect.width = columnWidth = aColumn.getWidth();
                if (aColumn != draggedColumn) {
                    this.paintCell(g2, cellRect, column, selected.contains(column));
                }
                cellRect.x += columnWidth;
            }
        }
        this.paintGrid(g2, c2);
        if (draggedColumn != null) {
            int draggedColumnIndex = this.viewIndexForColumn(draggedColumn);
            Rectangle draggedCellRect = this.header.getHeaderRect(draggedColumnIndex);
            g2.setColor(this.header.getParent().getBackground());
            g2.fillRect(draggedCellRect.x, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height);
            draggedCellRect.x += this.header.getDraggedDistance();
            g2.setColor(this.header.getBackground());
            g2.fillRect(draggedCellRect.x, draggedCellRect.y, draggedCellRect.width, draggedCellRect.height);
            this.paintCell(g2, draggedCellRect, draggedColumnIndex, selected.contains(draggedColumnIndex));
        }
        this.rendererPane.removeAll();
    }

    private Component getHeaderRenderer(int columnIndex) {
        TableColumn aColumn = this.header.getColumnModel().getColumn(columnIndex);
        TableCellRenderer renderer = aColumn.getHeaderRenderer();
        if (renderer == null) {
            renderer = this.header.getDefaultRenderer();
        }
        return renderer.getTableCellRendererComponent(this.header.getTable(), aColumn.getHeaderValue(), false, false, -1, columnIndex);
    }

    protected void paintGrid(Graphics g2, JComponent c2) {
        boolean ltr = this.header.getComponentOrientation().isLeftToRight();
        Graphics2D g2d = (Graphics2D)g2.create();
        Rectangle clip = g2.getClipBounds();
        Point left = clip.getLocation();
        int lineWeight = (int)Math.ceil(SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(c2)));
        left = new Point(left.x - 2 * lineWeight, left.y);
        Point right = new Point(clip.x + clip.width + 2 * lineWeight, clip.y);
        TableColumnModel cm = this.header.getColumnModel();
        int cMin = this.header.columnAtPoint(ltr ? left : right);
        int cMax = this.header.columnAtPoint(ltr ? right : left);
        if (cMin == -1) {
            cMin = 0;
        }
        Rectangle cellRect0 = this.header.getHeaderRect(cMin);
        int bottom = cellRect0.y + cellRect0.height;
        Color gridColor = SubstanceTableHeaderUI.getGridColor(this.header);
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.header));
        g2d.setStroke(new BasicStroke(strokeWidth, 1, 2));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(gridColor);
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.header, 0.7f, g2));
        g2d.drawLine((int)left.getX(), (int)((float)bottom - strokeWidth / 2.0f), (int)right.getX(), (int)((float)bottom - strokeWidth / 2.0f));
        if (cMax == -1) {
            cMax = cm.getColumnCount() - 1;
        }
        TableColumn draggedColumn = this.header.getDraggedColumn();
        Rectangle cellRect = this.header.getHeaderRect(ltr ? cMin : cMax);
        if (ltr) {
            for (int column = cMin; column <= cMax; ++column) {
                int columnWidth;
                TableColumn aColumn = cm.getColumn(column);
                cellRect.width = columnWidth = aColumn.getWidth();
                if (aColumn != draggedColumn) {
                    if (this.hasLeadingVerticalGridLine(this.header, cellRect, column)) {
                        g2d.drawLine(cellRect.x, cellRect.y, cellRect.x, bottom);
                    }
                    if (this.hasTrailingVerticalGridLine(this.header, cellRect, column)) {
                        g2d.drawLine(cellRect.x + cellRect.width - 1, cellRect.y, cellRect.x + cellRect.width - 1, bottom);
                    }
                }
                cellRect.x += columnWidth;
            }
        } else {
            for (int column = cMax; column >= cMin; --column) {
                int columnWidth;
                TableColumn aColumn = cm.getColumn(column);
                cellRect.width = columnWidth = aColumn.getWidth();
                if (aColumn != draggedColumn) {
                    if (this.hasLeadingVerticalGridLine(this.header, cellRect, column)) {
                        g2d.drawLine(cellRect.x + cellRect.width - 1, cellRect.y, cellRect.x + cellRect.width - 1, bottom);
                    }
                    if (this.hasTrailingVerticalGridLine(this.header, cellRect, column)) {
                        g2d.drawLine(cellRect.x, cellRect.y, cellRect.x, bottom);
                    }
                }
                cellRect.x += columnWidth;
            }
        }
        g2d.dispose();
    }

    private boolean hasTrailingVerticalGridLine(JTableHeader tableHeader, Rectangle cellRect, int column) {
        boolean toDrawLine;
        boolean bl = toDrawLine = column != tableHeader.getColumnModel().getColumnCount() - 1;
        if (!toDrawLine) {
            Container parent = this.header.getParent();
            toDrawLine = tableHeader.getComponentOrientation().isLeftToRight() ? parent != null && parent.getWidth() > cellRect.x + cellRect.width : parent != null && cellRect.x > 0;
        }
        return toDrawLine;
    }

    private boolean hasLeadingVerticalGridLine(JTableHeader tableHeader, Rectangle cellRect, int column) {
        Container grand;
        if (column != 0) {
            return false;
        }
        Container parent = tableHeader.getParent();
        if (parent instanceof JViewport && (grand = parent.getParent()) instanceof JScrollPane) {
            return ((JScrollPane)grand).getRowHeader() != null;
        }
        return false;
    }

    protected static Color getGridColor(JTableHeader header) {
        boolean isEnabled = header.isEnabled();
        if (header.getTable() != null) {
            isEnabled = isEnabled && header.getTable().isEnabled();
        }
        ComponentState currState = isEnabled ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
        Color gridColor = SubstanceColorSchemeUtilities.getColorScheme(header, ColorSchemeAssociationKind.BORDER, currState).getLineColor();
        return gridColor;
    }

    private void paintCell(Graphics g2, Rectangle cellRect, int columnIndex, boolean isSelected) {
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.header, g2));
        Component component = this.getHeaderRenderer(columnIndex);
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.getModelStateInfo(columnIndex);
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo == null ? null : modelStateInfo.getStateContributionMap();
        ComponentState currState = modelStateInfo == null ? this.getColumnState(columnIndex) : modelStateInfo.getCurrModelState();
        boolean hasHighlights = false;
        if (activeStates != null) {
            Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry;
            Iterator<Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo>> i$ = activeStates.entrySet().iterator();
            while (i$.hasNext() && !(hasHighlights = SubstanceColorSchemeUtilities.getHighlightAlpha(this.header, (stateEntry = i$.next()).getKey()) * stateEntry.getValue().getContribution() > 0.0f)) {
            }
        } else {
            boolean bl = hasHighlights = SubstanceColorSchemeUtilities.getHighlightAlpha(this.header, currState) > 0.0f;
        }
        if (hasHighlights) {
            if (activeStates == null) {
                float alpha = SubstanceColorSchemeUtilities.getHighlightAlpha(this.header, currState);
                if (alpha > 0.0f) {
                    SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.header, ColorSchemeAssociationKind.HIGHLIGHT, currState);
                    SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.header, ColorSchemeAssociationKind.HIGHLIGHT, currState);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.header, alpha, g2));
                    HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, this.rendererPane, cellRect, 0.8f, null, fillScheme, borderScheme);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.header, g2));
                }
            } else {
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry : activeStates.entrySet()) {
                    ComponentState activeState = stateEntry.getKey();
                    float alpha = SubstanceColorSchemeUtilities.getHighlightAlpha(this.header, activeState) * stateEntry.getValue().getContribution();
                    if (alpha == 0.0f) continue;
                    SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.header, ColorSchemeAssociationKind.HIGHLIGHT, activeState);
                    SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.header, ColorSchemeAssociationKind.HIGHLIGHT, activeState);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.header, alpha, g2));
                    HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, this.rendererPane, cellRect, 0.8f, null, fillScheme, borderScheme);
                    g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.header, g2));
                }
            }
        }
        this.rendererPane.paintComponent(g2d, component, this.header, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
        g2d.dispose();
    }

    private int viewIndexForColumn(TableColumn aColumn) {
        TableColumnModel cm = this.header.getColumnModel();
        for (int column = 0; column < cm.getColumnCount(); ++column) {
            if (cm.getColumn(column) != aColumn) continue;
            return column;
        }
        return -1;
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__update(Graphics g2, JComponent c2) {
        boolean isEnabled = this.header.isEnabled();
        if (this.header.getTable() != null) {
            isEnabled = isEnabled && this.header.getTable().isEnabled();
        }
        ComponentState backgroundState = isEnabled ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
        Rectangle clip = g2.getClipBounds();
        if (clip == null) {
            clip = c2.getBounds();
        }
        SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(c2, backgroundState);
        SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(c2, ColorSchemeAssociationKind.BORDER, backgroundState);
        HighlightPainterUtils.paintHighlight(g2, null, c2, clip, 0.0f, null, fillScheme, borderScheme);
        this.paint(g2, c2);
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTableHeaderUI__uninstallUI(JComponent c2) {
        for (int i2 = 0; i2 < this.header.getColumnModel().getColumnCount(); ++i2) {
            TableColumn aColumn = this.header.getColumnModel().getColumn(i2);
            TableCellRenderer renderer = aColumn.getHeaderRenderer();
            if (renderer == null) {
                renderer = this.header.getDefaultRenderer();
            }
            Component rendComp = renderer.getTableCellRendererComponent(this.header.getTable(), aColumn.getHeaderValue(), false, false, -1, i2);
            SwingUtilities.updateComponentTreeUI(rendComp);
        }
        super.uninstallUI(c2);
    }

    public ComponentState getColumnState(int columnIndex) {
        StateTransitionTracker tracker;
        boolean toEnable = this.header.isEnabled();
        JTable table = this.header.getTable();
        if (table != null) {
            boolean bl = toEnable = toEnable && table.isEnabled();
        }
        if ((tracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)columnIndex)) == null) {
            boolean isRollover = false;
            TableColumnModel columnModel = this.header.getColumnModel();
            boolean isSelected = false;
            if (columnModel.getColumnSelectionAllowed()) {
                isSelected = columnModel.getSelectionModel().isSelectedIndex(columnIndex);
                if (table != null && table.getUI() instanceof SubstanceTableUI) {
                    SubstanceTableUI tableUI = (SubstanceTableUI)table.getUI();
                    int rolledOverIndex = tableUI.getRolloverColumnIndex();
                    isRollover = rolledOverIndex >= 0 && rolledOverIndex == columnIndex;
                    boolean hasSelectionAnimations = tableUI.hasSelectionAnimations();
                    if (hasSelectionAnimations && AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.SELECTION, table)) {
                        isSelected = this.selectedIndices.containsKey(columnIndex);
                    }
                }
            }
            return ComponentState.getState(toEnable, isRollover, isSelected);
        }
        ComponentState fromTracker = tracker.getModelStateInfo().getCurrModelState();
        return ComponentState.getState(toEnable, fromTracker.isFacetActive(ComponentStateFacet.ROLLOVER), fromTracker.isFacetActive(ComponentStateFacet.SELECTION));
    }

    public StateTransitionTracker.ModelStateInfo getModelStateInfo(int columnIndex) {
        if (this.stateTransitionMultiTracker.size() == 0) {
            return null;
        }
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)columnIndex);
        if (tracker == null) {
            return null;
        }
        return tracker.getModelStateInfo();
    }

    public StateTransitionTracker getStateTransitionTracker(int columnIndex) {
        return this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)columnIndex);
    }

    public JComponent getScrollPaneCornerFiller() {
        return new ScrollPaneCornerFiller(this.header);
    }

    public void processColumnModelChangeEvent(TableColumnModel oldModel, TableColumnModel newModel) {
        if (oldModel != null) {
            oldModel.getSelectionModel().removeListSelectionListener(this.substanceFadeSelectionListener);
        }
        if (newModel != null) {
            newModel.getSelectionModel().addListSelectionListener(this.substanceFadeSelectionListener);
        }
        this.selectedIndices.clear();
        this.stateTransitionMultiTracker.clear();
    }

    public StateTransitionTracker getTracker(final int columnIndex, boolean initialRollover, boolean initialSelected) {
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker((Comparable<Integer>)columnIndex);
        if (tracker == null) {
            DefaultButtonModel model = new DefaultButtonModel();
            model.setSelected(initialSelected);
            model.setRollover(initialRollover);
            tracker = new StateTransitionTracker(this.header, model);
            tracker.registerModelListeners();
            tracker.setRepaintCallback(new StateTransitionTracker.RepaintCallback(){

                @Override
                public TimelineCallback getRepaintCallback() {
                    return new ColumnHeaderRepaintCallback(SubstanceTableHeaderUI.this.header, columnIndex);
                }
            });
            this.stateTransitionMultiTracker.addTracker((Comparable<Integer>)columnIndex, tracker);
        }
        return tracker;
    }

    protected class ColumnHeaderRepaintCallback
    extends UIThreadTimelineCallbackAdapter {
        protected JTableHeader header;
        protected int columnIndex;

        public ColumnHeaderRepaintCallback(JTableHeader header, int columnIndex) {
            this.header = header;
            this.columnIndex = columnIndex;
        }

        @Override
        public void onTimelinePulse(float durationFraction, float timelinePosition) {
            this.repaintColumnHeader();
        }

        @Override
        public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
            this.repaintColumnHeader();
        }

        private void repaintColumnHeader() {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    if (ColumnHeaderRepaintCallback.this.header == null) {
                        return;
                    }
                    try {
                        int cellCount = ColumnHeaderRepaintCallback.this.header.getColumnModel().getColumnCount();
                        if (cellCount > 0 && ColumnHeaderRepaintCallback.this.columnIndex < cellCount) {
                            Rectangle rect = ColumnHeaderRepaintCallback.this.header.getHeaderRect(ColumnHeaderRepaintCallback.this.columnIndex);
                            Rectangle damaged = new Rectangle(rect.x - 5, rect.y, rect.width + 10, rect.height);
                            ColumnHeaderRepaintCallback.this.header.repaint(damaged);
                        }
                    }
                    catch (RuntimeException re) {
                        return;
                    }
                }
            });
        }
    }

    protected static class ScrollPaneCornerFiller
    extends JComponent
    implements UIResource {
        protected JTableHeader header;

        public ScrollPaneCornerFiller(JTableHeader header) {
            this.header = header;
        }

        @Override
        protected void paintComponent(Graphics g2) {
            Graphics2D g2d = (Graphics2D)g2.create();
            boolean ltr = this.header.getComponentOrientation().isLeftToRight();
            ComponentState backgroundState = this.header.isEnabled() && this.header.getTable().isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
            SubstanceColorScheme fillScheme = SubstanceColorSchemeUtilities.getColorScheme(this.header, ColorSchemeAssociationKind.HIGHLIGHT, backgroundState);
            SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.header, ColorSchemeAssociationKind.HIGHLIGHT_BORDER, backgroundState);
            HighlightPainterUtils.paintHighlight(g2d, null, this.header, new Rectangle(0, 0, this.getWidth(), this.getHeight()), 0.0f, null, fillScheme, borderScheme);
            g2d.setColor(SubstanceTableHeaderUI.getGridColor(this.header));
            float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.header));
            g2d.setStroke(new BasicStroke(strokeWidth, 1, 2));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.header, 0.7f, g2));
            int x2 = ltr ? (int)strokeWidth / 2 : this.getWidth() - 1 - (int)strokeWidth / 2;
            g2d.drawLine(x2, 0, x2, this.getHeight());
            g2d.dispose();
        }
    }

    private static class TableHeaderListener
    implements ListSelectionListener {
        private SubstanceTableHeaderUI ui;

        public TableHeaderListener(SubstanceTableHeaderUI ui) {
            this.ui = ui;
        }

        @Override
        public void valueChanged(ListSelectionEvent e2) {
            if (this.ui.header == null) {
                return;
            }
            if (this.ui.header.isValid()) {
                this.ui.header.repaint();
            }
        }
    }
}

