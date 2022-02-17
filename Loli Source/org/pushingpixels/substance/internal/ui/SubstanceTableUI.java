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
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultButtonModel;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
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
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultTableCellRenderer;
import org.pushingpixels.substance.internal.animation.StateTransitionMultiTracker;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.painter.HighlightPainterUtils;
import org.pushingpixels.substance.internal.ui.SubstanceTableHeaderUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorResource;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceStripingUtils;
import org.pushingpixels.substance.internal.utils.UpdateOptimizationAware;
import org.pushingpixels.substance.internal.utils.UpdateOptimizationInfo;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.callback.TimelineCallback;
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter;

public class SubstanceTableUI
extends BasicTableUI
implements UpdateOptimizationAware {
    protected Set lafWidgets;
    protected Map<TableCellId, Object> selectedIndices = new HashMap<TableCellId, Object>();
    protected Set<TableCellId> rolledOverIndices = new HashSet<TableCellId>();
    protected TableCellId focusedCellId;
    protected int rolledOverColumn = -1;
    protected Map<Class<?>, TableCellRenderer> defaultRenderers;
    protected Map<Class<?>, TableCellEditor> defaultEditors;
    protected PropertyChangeListener substancePropertyChangeListener;
    protected TableStateListener substanceTableStateListener;
    protected RolloverFadeListener substanceFadeRolloverListener;
    protected FocusListener substanceFocusListener;
    private StateTransitionMultiTracker<TableCellId> stateTransitionMultiTracker = new StateTransitionMultiTracker();
    protected Boolean drawLeadingVerticalLine;
    protected Boolean drawTrailingVerticalLine;
    private Insets cellRendererInsets;
    TableCellId cellId = new TableCellId(-1, -1);
    private TableUpdateOptimizationInfo updateInfo;

    public void __org__pushingpixels__substance__internal__ui__SubstanceTableUI__installUI(JComponent jComponent) {
        super.installUI(jComponent);
    }

    @Override
    public void installUI(JComponent jComponent) {
        this.lafWidgets = LafWidgetRepository.getRepository().getMatchingWidgets(jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableUI__installUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installUI();
        }
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTableUI__uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
    }

    @Override
    public void uninstallUI(JComponent jComponent) {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableUI__uninstallUI(jComponent);
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallUI();
        }
    }

    @Override
    protected void installListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableUI__installListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installListeners();
        }
    }

    @Override
    protected void installDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableUI__installDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.installDefaults();
        }
    }

    @Override
    protected void uninstallListeners() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableUI__uninstallListeners();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallListeners();
        }
    }

    @Override
    protected void uninstallDefaults() {
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableUI__uninstallDefaults();
        for (LafWidget lafWidget : this.lafWidgets) {
            lafWidget.uninstallDefaults();
        }
    }

    @Override
    public void update(Graphics graphics, JComponent jComponent) {
        Graphics2D graphics2D = (Graphics2D)graphics.create();
        RenderingUtils.installDesktopHints(graphics2D, jComponent);
        this.__org__pushingpixels__substance__internal__ui__SubstanceTableUI__update(graphics2D, jComponent);
        graphics2D.dispose();
    }

    public static ComponentUI createUI(JComponent comp) {
        SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
        return new SubstanceTableUI();
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTableUI__installDefaults() {
        Class[] defEditorClasses;
        Class[] defClasses;
        super.installDefaults();
        if (SubstanceCoreUtilities.toDrawWatermark(this.table)) {
            this.table.setOpaque(false);
        }
        this.defaultRenderers = new HashMap();
        for (Class clazz : defClasses = new Class[]{Object.class, Icon.class, ImageIcon.class, Number.class, Float.class, Double.class, Date.class, Boolean.class}) {
            this.defaultRenderers.put(clazz, this.table.getDefaultRenderer(clazz));
        }
        this.installRendererIfNecessary(Object.class, new SubstanceDefaultTableCellRenderer());
        this.installRendererIfNecessary(Icon.class, new SubstanceDefaultTableCellRenderer.IconRenderer());
        this.installRendererIfNecessary(ImageIcon.class, new SubstanceDefaultTableCellRenderer.IconRenderer());
        this.installRendererIfNecessary(Number.class, new SubstanceDefaultTableCellRenderer.NumberRenderer());
        this.installRendererIfNecessary(Float.class, new SubstanceDefaultTableCellRenderer.DoubleRenderer());
        this.installRendererIfNecessary(Double.class, new SubstanceDefaultTableCellRenderer.DoubleRenderer());
        this.installRendererIfNecessary(Date.class, new SubstanceDefaultTableCellRenderer.DateRenderer());
        this.installRendererIfNecessary(Boolean.class, new SubstanceDefaultTableCellRenderer.BooleanRenderer());
        this.defaultEditors = new HashMap();
        for (Class clazz : defEditorClasses = new Class[]{Boolean.class}) {
            this.defaultEditors.put(clazz, this.table.getDefaultEditor(clazz));
        }
        this.installEditorIfNecessary(Boolean.class, new BooleanEditor());
        int rows = this.table.getRowCount();
        int cols = this.table.getColumnCount();
        for (int i2 = 0; i2 < rows; ++i2) {
            for (int j2 = 0; j2 < cols; ++j2) {
                if (!this.table.isCellSelected(i2, j2)) continue;
                TableCellId cellId = new TableCellId(i2, j2);
                this.selectedIndices.put(cellId, this.table.getValueAt(i2, j2));
            }
        }
        boolean areAllRenderersFromSubstance = true;
        TableColumnModel columnModel = this.table.getColumnModel();
        for (int i3 = 0; i3 < columnModel.getColumnCount(); ++i3) {
            TableColumn column = columnModel.getColumn(i3);
            TableCellRenderer renderer = column.getCellRenderer();
            if (renderer == null) {
                renderer = this.table.getDefaultRenderer(this.table.getColumnClass(i3));
            }
            if (renderer instanceof SubstanceDefaultTableCellRenderer || renderer instanceof SubstanceDefaultTableCellRenderer.BooleanRenderer) continue;
            areAllRenderersFromSubstance = false;
            break;
        }
        if (areAllRenderersFromSubstance) {
            Insets rendererInsets = SubstanceSizeUtils.getTableCellRendererInsets(SubstanceSizeUtils.getComponentFontSize(this.table));
            JLabel dummy = new JLabel("dummy");
            dummy.setFont(this.table.getFont());
            int rowHeight = dummy.getPreferredSize().height + rendererInsets.bottom + rendererInsets.top;
            this.table.setRowHeight(rowHeight);
        }
        this.cellRendererInsets = SubstanceSizeUtils.getTableCellRendererInsets(SubstanceSizeUtils.getComponentFontSize(this.table));
        this.drawLeadingVerticalLine = (Boolean)this.table.getClientProperty("substancelaf.tableLeadingVerticalLine");
        this.drawTrailingVerticalLine = (Boolean)this.table.getClientProperty("substancelaf.tableTrailingVerticalLine");
    }

    protected void installRendererIfNecessary(Class<?> clazz, TableCellRenderer renderer) {
        TableCellRenderer currRenderer = this.table.getDefaultRenderer(clazz);
        if (currRenderer != null) {
            boolean isCore;
            boolean bl = isCore = currRenderer instanceof DefaultTableCellRenderer.UIResource || currRenderer.getClass().getName().startsWith("javax.swing.JTable");
            if (!isCore) {
                return;
            }
        }
        this.table.setDefaultRenderer(clazz, renderer);
    }

    protected void installEditorIfNecessary(Class<?> clazz, TableCellEditor editor) {
        boolean isCore;
        TableCellEditor currEditor = this.table.getDefaultEditor(clazz);
        if (currEditor != null && !(isCore = currEditor.getClass().getName().startsWith("javax.swing.JTable"))) {
            return;
        }
        this.table.setDefaultEditor(clazz, editor);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTableUI__uninstallDefaults() {
        for (Map.Entry<Class<?>, TableCellRenderer> entry : this.defaultRenderers.entrySet()) {
            this.uninstallRendererIfNecessary(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Class<?>, Object> entry : this.defaultEditors.entrySet()) {
            this.uninstallEditorIfNecessary(entry.getKey(), (TableCellEditor)entry.getValue());
        }
        this.selectedIndices.clear();
        super.uninstallDefaults();
    }

    protected void uninstallRendererIfNecessary(Class<?> clazz, TableCellRenderer renderer) {
        boolean isSubstanceRenderer;
        TableCellRenderer currRenderer = this.table.getDefaultRenderer(clazz);
        if (currRenderer != null && !(isSubstanceRenderer = this.isSubstanceDefaultRenderer(currRenderer))) {
            return;
        }
        if (renderer instanceof Component) {
            SwingUtilities.updateComponentTreeUI((Component)((Object)renderer));
        }
        this.table.setDefaultRenderer(clazz, renderer);
    }

    protected void uninstallEditorIfNecessary(Class<?> clazz, TableCellEditor editor) {
        boolean isSubstanceEditor;
        TableCellEditor currEditor = this.table.getDefaultEditor(clazz);
        if (currEditor != null && !(isSubstanceEditor = this.isSubstanceDefaultEditor(currEditor))) {
            return;
        }
        if (editor instanceof Component) {
            SwingUtilities.updateComponentTreeUI((Component)((Object)editor));
        }
        this.table.setDefaultEditor(clazz, editor);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTableUI__installListeners() {
        super.installListeners();
        this.substancePropertyChangeListener = new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                JTableHeader header;
                Object old;
                if ("substancelaf.watermark.visible".equals(evt.getPropertyName())) {
                    SubstanceTableUI.this.table.setOpaque(!SubstanceCoreUtilities.toDrawWatermark(SubstanceTableUI.this.table));
                }
                if ("columnSelectionAllowed".equals(evt.getPropertyName()) || "rowSelectionAllowed".equals(evt.getPropertyName())) {
                    SubstanceTableUI.this.syncSelection(true);
                }
                if ("model".equals(evt.getPropertyName())) {
                    old = (TableModel)evt.getOldValue();
                    if (old != null) {
                        old.removeTableModelListener(SubstanceTableUI.this.substanceTableStateListener);
                    }
                    SubstanceTableUI.this.table.getModel().addTableModelListener(SubstanceTableUI.this.substanceTableStateListener);
                    SubstanceTableUI.this.selectedIndices.clear();
                    SubstanceTableUI.this.stateTransitionMultiTracker.clear();
                    SubstanceTableUI.this.syncSelection(true);
                }
                if ("columnModel".equals(evt.getPropertyName())) {
                    old = (TableColumnModel)evt.getOldValue();
                    if (old != null) {
                        old.getSelectionModel().removeListSelectionListener(SubstanceTableUI.this.substanceTableStateListener);
                    }
                    SubstanceTableUI.this.table.getColumnModel().getSelectionModel().addListSelectionListener(SubstanceTableUI.this.substanceTableStateListener);
                    SubstanceTableUI.this.selectedIndices.clear();
                    SubstanceTableUI.this.stateTransitionMultiTracker.clear();
                    SubstanceTableUI.this.syncSelection(true);
                    JTableHeader tableHeader = SubstanceTableUI.this.table.getTableHeader();
                    if (tableHeader != null) {
                        SubstanceTableHeaderUI headerUI = (SubstanceTableHeaderUI)tableHeader.getUI();
                        headerUI.processColumnModelChangeEvent((TableColumnModel)evt.getOldValue(), (TableColumnModel)evt.getNewValue());
                    }
                }
                if ("selectionModel".equals(evt.getPropertyName())) {
                    old = (ListSelectionModel)evt.getOldValue();
                    if (old != null) {
                        old.removeListSelectionListener(SubstanceTableUI.this.substanceTableStateListener);
                    }
                    SubstanceTableUI.this.table.getSelectionModel().addListSelectionListener(SubstanceTableUI.this.substanceTableStateListener);
                    SubstanceTableUI.this.selectedIndices.clear();
                    SubstanceTableUI.this.stateTransitionMultiTracker.clear();
                    SubstanceTableUI.this.syncSelection(true);
                }
                if ("rowSorter".equals(evt.getPropertyName())) {
                    RowSorter newSorter;
                    old = (RowSorter)evt.getOldValue();
                    if (old != null) {
                        ((RowSorter)old).removeRowSorterListener(SubstanceTableUI.this.substanceTableStateListener);
                    }
                    if ((newSorter = (RowSorter)evt.getNewValue()) != null) {
                        newSorter.addRowSorterListener(SubstanceTableUI.this.substanceTableStateListener);
                    }
                    SubstanceTableUI.this.selectedIndices.clear();
                    SubstanceTableUI.this.stateTransitionMultiTracker.clear();
                    SubstanceTableUI.this.syncSelection(true);
                }
                if ("font".equals(evt.getPropertyName())) {
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            if (SubstanceTableUI.this.table == null) {
                                return;
                            }
                            SubstanceTableUI.this.table.updateUI();
                        }
                    });
                }
                if ("background".equals(evt.getPropertyName())) {
                    Color headerBackground;
                    Color newBackgr = (Color)evt.getNewValue();
                    JTableHeader header2 = SubstanceTableUI.this.table.getTableHeader();
                    if (header2 != null && SubstanceCoreUtilities.canReplaceChildBackgroundColor(headerBackground = header2.getBackground())) {
                        if (!(newBackgr instanceof UIResource)) {
                            if (newBackgr == null) {
                                header2.setBackground(null);
                            } else {
                                header2.setBackground(new SubstanceColorResource(newBackgr));
                            }
                        } else {
                            header2.setBackground(newBackgr);
                        }
                    }
                }
                if ("enabled".equals(evt.getPropertyName()) && (header = SubstanceTableUI.this.table.getTableHeader()) != null) {
                    header.setEnabled(SubstanceTableUI.this.table.isEnabled());
                }
                if ("dropLocation".equals(evt.getPropertyName())) {
                    JTable.DropLocation newValue;
                    JTable.DropLocation oldValue = (JTable.DropLocation)evt.getOldValue();
                    if (oldValue != null) {
                        Rectangle oldRect = SubstanceTableUI.this.getCellRectangleForRepaint(oldValue.getRow(), oldValue.getColumn());
                        SubstanceTableUI.this.table.repaint(oldRect);
                    }
                    if ((newValue = SubstanceTableUI.this.table.getDropLocation()) != null) {
                        Rectangle newRect = SubstanceTableUI.this.getCellRectangleForRepaint(SubstanceTableUI.this.table.getDropLocation().getRow(), SubstanceTableUI.this.table.getDropLocation().getColumn());
                        SubstanceTableUI.this.table.repaint(newRect);
                    }
                }
                if ("tableCellEditor".equals(evt.getPropertyName())) {
                    TableCellEditor newEditor = (TableCellEditor)evt.getNewValue();
                    TableCellEditor oldEditor = (TableCellEditor)evt.getOldValue();
                    if (oldEditor != null && SubstanceTableUI.this.table.getEditorComponent() != null) {
                        SubstanceTableUI.this.table.getEditorComponent().removeMouseListener(SubstanceTableUI.this.substanceFadeRolloverListener);
                    }
                    if (newEditor != null && SubstanceTableUI.this.table.getEditorComponent() != null) {
                        SubstanceTableUI.this.table.getEditorComponent().addMouseListener(SubstanceTableUI.this.substanceFadeRolloverListener);
                    }
                }
                if ("substancelaf.tableLeadingVerticalLine".equals(evt.getPropertyName())) {
                    SubstanceTableUI.this.drawLeadingVerticalLine = (Boolean)evt.getNewValue();
                }
                if ("substancelaf.tableTrailingVerticalLine".equals(evt.getPropertyName())) {
                    SubstanceTableUI.this.drawTrailingVerticalLine = (Boolean)evt.getNewValue();
                }
            }
        };
        this.table.addPropertyChangeListener(this.substancePropertyChangeListener);
        this.substanceTableStateListener = new TableStateListener();
        this.table.getSelectionModel().addListSelectionListener(this.substanceTableStateListener);
        TableColumnModel columnModel = this.table.getColumnModel();
        columnModel.getSelectionModel().addListSelectionListener(this.substanceTableStateListener);
        this.table.getModel().addTableModelListener(this.substanceTableStateListener);
        if (this.table.getRowSorter() != null) {
            this.table.getRowSorter().addRowSorterListener(this.substanceTableStateListener);
        }
        this.substanceFadeRolloverListener = new RolloverFadeListener();
        this.table.addMouseMotionListener(this.substanceFadeRolloverListener);
        this.table.addMouseListener(this.substanceFadeRolloverListener);
        this.substanceFocusListener = new FocusListener(){

            @Override
            public void focusLost(FocusEvent e2) {
                if (SubstanceTableUI.this.focusedCellId == null) {
                    return;
                }
                ComponentState cellState = SubstanceTableUI.this.getCellState(SubstanceTableUI.this.focusedCellId);
                StateTransitionTracker tracker = SubstanceTableUI.this.getTracker(SubstanceTableUI.this.focusedCellId, cellState.isFacetActive(ComponentStateFacet.ROLLOVER), cellState.isFacetActive(ComponentStateFacet.SELECTION));
                tracker.setFocusState(false);
                SubstanceTableUI.this.focusedCellId = null;
            }

            @Override
            public void focusGained(FocusEvent e2) {
                int rowLead = SubstanceTableUI.this.table.getSelectionModel().getLeadSelectionIndex();
                int colLead = SubstanceTableUI.this.table.getColumnModel().getSelectionModel().getLeadSelectionIndex();
                if (rowLead >= 0 && colLead >= 0) {
                    TableCellId toFocus = new TableCellId(rowLead, colLead);
                    if (toFocus.equals(SubstanceTableUI.this.focusedCellId)) {
                        return;
                    }
                    ComponentState cellState = SubstanceTableUI.this.getCellState(toFocus);
                    StateTransitionTracker tracker = SubstanceTableUI.this.getTracker(toFocus, cellState.isFacetActive(ComponentStateFacet.ROLLOVER), cellState.isFacetActive(ComponentStateFacet.SELECTION));
                    tracker.setFocusState(true);
                    SubstanceTableUI.this.focusedCellId = toFocus;
                }
            }
        };
        this.table.addFocusListener(this.substanceFocusListener);
    }

    protected void __org__pushingpixels__substance__internal__ui__SubstanceTableUI__uninstallListeners() {
        this.table.removePropertyChangeListener(this.substancePropertyChangeListener);
        this.substancePropertyChangeListener = null;
        this.table.getSelectionModel().removeListSelectionListener(this.substanceTableStateListener);
        this.table.getColumnModel().getSelectionModel().removeListSelectionListener(this.substanceTableStateListener);
        this.table.getModel().removeTableModelListener(this.substanceTableStateListener);
        if (this.table.getRowSorter() != null) {
            this.table.getRowSorter().removeRowSorterListener(this.substanceTableStateListener);
        }
        this.substanceTableStateListener = null;
        this.table.removeMouseMotionListener(this.substanceFadeRolloverListener);
        this.table.removeMouseListener(this.substanceFadeRolloverListener);
        this.substanceFadeRolloverListener = null;
        this.table.removeFocusListener(this.substanceFocusListener);
        this.substanceFocusListener = null;
        super.uninstallListeners();
    }

    @Override
    public void paint(Graphics g2, JComponent c2) {
        boolean ltr;
        Rectangle clip = g2.getClipBounds();
        Rectangle bounds = this.table.getBounds();
        bounds.y = 0;
        bounds.x = 0;
        if (this.table.getRowCount() <= 0 || this.table.getColumnCount() <= 0 || !bounds.intersects(clip)) {
            return;
        }
        Point upperLeft = clip.getLocation();
        Point lowerRight = new Point(clip.x + clip.width - 1, clip.y + clip.height - 1);
        int rMin = this.table.rowAtPoint(upperLeft);
        int rMax = this.table.rowAtPoint(lowerRight);
        if (rMin == -1) {
            rMin = 0;
        }
        if (rMax == -1) {
            rMax = this.table.getRowCount() - 1;
        }
        int cMin = this.table.columnAtPoint((ltr = this.table.getComponentOrientation().isLeftToRight()) ? upperLeft : lowerRight);
        int cMax = this.table.columnAtPoint(ltr ? lowerRight : upperLeft);
        if (cMin == -1) {
            cMin = 0;
        }
        if (cMax == -1) {
            cMax = this.table.getColumnCount() - 1;
        }
        this.paintCells(g2, rMin, rMax, cMin, cMax);
        this.paintGrid(g2, rMin, rMax, cMin, cMax);
        this.paintDropLines(g2);
    }

    protected void paintGrid(Graphics g2, int rMin, int rMax, int cMin, int cMax) {
        Graphics2D g2d = (Graphics2D)g2.create();
        ComponentState currState = this.table.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED;
        float alpha = SubstanceColorSchemeUtilities.getAlpha(this.table, currState);
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.table, alpha, g2));
        Color gridColor = this.table.getGridColor();
        if (gridColor instanceof UIResource) {
            SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.table, ColorSchemeAssociationKind.BORDER, this.table.isEnabled() ? ComponentState.ENABLED : ComponentState.DISABLED_UNSELECTED);
            gridColor = scheme.getLineColor();
        }
        g2d.setColor(gridColor);
        Rectangle minCell = this.table.getCellRect(rMin, cMin, true);
        Rectangle maxCell = this.table.getCellRect(rMax, cMax, true);
        Rectangle damagedArea = minCell.union(maxCell);
        float strokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.table));
        g2d.setStroke(new BasicStroke(strokeWidth, 1, 2));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (this.table.getShowHorizontalLines()) {
            int tableWidth = damagedArea.x + damagedArea.width;
            int y2 = damagedArea.y;
            for (int row = rMin; row <= rMax; ++row) {
                g2d.drawLine(damagedArea.x, (y2 += this.table.getRowHeight(row)) - 1, tableWidth - 1, y2 - 1);
            }
        }
        if (this.table.getShowVerticalLines()) {
            int x2;
            TableColumnModel cm = this.table.getColumnModel();
            int tableHeight = damagedArea.y + damagedArea.height;
            if (this.table.getComponentOrientation().isLeftToRight()) {
                x2 = damagedArea.x;
                for (int column = cMin; column <= cMax; ++column) {
                    int w2 = cm.getColumn(column).getWidth();
                    if (this.hasLeadingVerticalGridLine(cm, column)) {
                        g2d.drawLine(x2, 0, x2, tableHeight - 1);
                    }
                    x2 += w2;
                    if (!this.hasTrailingVerticalGridLine(cm, column)) continue;
                    g2d.drawLine(x2 - 1, 0, x2 - 1, tableHeight - 1);
                }
            } else {
                x2 = damagedArea.x + damagedArea.width;
                for (int column = cMin; column <= cMax; ++column) {
                    int w3 = cm.getColumn(column).getWidth();
                    if (this.hasLeadingVerticalGridLine(cm, column)) {
                        g2d.drawLine(x2 - 1, 0, x2 - 1, tableHeight - 1);
                    }
                    x2 -= w3;
                    if (!this.hasTrailingVerticalGridLine(cm, column)) continue;
                    g2d.drawLine(x2, 0, x2, tableHeight - 1);
                }
            }
        }
        g2d.dispose();
    }

    private boolean hasTrailingVerticalGridLine(TableColumnModel cm, int column) {
        boolean toDrawLine;
        boolean bl = toDrawLine = column != cm.getColumnCount() - 1;
        if (!toDrawLine) {
            Container parent;
            toDrawLine = this.drawTrailingVerticalLine != null ? this.drawTrailingVerticalLine : (parent = this.table.getParent()) != null && parent.getWidth() >= this.table.getWidth();
        }
        return toDrawLine;
    }

    private boolean hasLeadingVerticalGridLine(TableColumnModel cm, int column) {
        Container grand;
        if (column != 0) {
            return false;
        }
        if (this.drawLeadingVerticalLine != null) {
            return this.drawLeadingVerticalLine;
        }
        Container parent = this.table.getParent();
        if (parent instanceof JViewport && (grand = parent.getParent()) instanceof JScrollPane) {
            return ((JScrollPane)grand).getRowHeader() != null;
        }
        return false;
    }

    private int viewIndexForColumn(TableColumn aColumn) {
        TableColumnModel cm = this.table.getColumnModel();
        for (int column = 0; column < cm.getColumnCount(); ++column) {
            if (cm.getColumn(column) != aColumn) continue;
            return column;
        }
        return -1;
    }

    protected void paintCells(Graphics g2, int rMin, int rMax, int cMin, int cMax) {
        int row;
        JTableHeader header = this.table.getTableHeader();
        TableColumn draggedColumn = header == null ? null : header.getDraggedColumn();
        TableColumnModel cm = this.table.getColumnModel();
        int columnMargin = cm.getColumnMargin();
        int rowMargin = this.table.getRowMargin();
        if (this.table.getComponentOrientation().isLeftToRight()) {
            for (row = rMin; row <= rMax; ++row) {
                Rectangle cellRect = this.table.getCellRect(row, cMin, false);
                Rectangle highlightCellRect = new Rectangle(cellRect);
                highlightCellRect.y -= rowMargin / 2;
                highlightCellRect.height += rowMargin;
                for (int column = cMin; column <= cMax; ++column) {
                    TableColumn aColumn = cm.getColumn(column);
                    int columnWidth = aColumn.getWidth();
                    cellRect.width = columnWidth - columnMargin;
                    highlightCellRect.x = cellRect.x - columnMargin / 2;
                    highlightCellRect.width = columnWidth;
                    if (aColumn != draggedColumn) {
                        this.paintCell(g2, cellRect, highlightCellRect, row, column);
                    }
                    cellRect.x += columnWidth;
                }
            }
        } else {
            for (row = rMin; row <= rMax; ++row) {
                Rectangle cellRect = this.table.getCellRect(row, cMin, false);
                Rectangle highlightCellRect = new Rectangle(cellRect);
                highlightCellRect.y -= rowMargin / 2;
                highlightCellRect.height += rowMargin;
                for (int column = cMin; column <= cMax; ++column) {
                    TableColumn aColumn = cm.getColumn(column);
                    int columnWidth = aColumn.getWidth();
                    cellRect.width = columnWidth - columnMargin;
                    highlightCellRect.x = cellRect.x - columnMargin / 2;
                    highlightCellRect.width = columnWidth;
                    if (aColumn != draggedColumn) {
                        this.paintCell(g2, cellRect, highlightCellRect, row, column);
                    }
                    cellRect.x -= columnWidth;
                }
            }
        }
        if (draggedColumn != null) {
            Graphics2D g2d = (Graphics2D)g2.create();
            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.table, 0.65f, g2));
            this.paintDraggedArea(g2d, rMin, rMax, draggedColumn, header.getDraggedDistance());
            g2d.dispose();
        }
        this.rendererPane.removeAll();
    }

    protected void paintDraggedArea(Graphics g2, int rMin, int rMax, TableColumn draggedColumn, int distance) {
        int draggedColumnIndex = this.viewIndexForColumn(draggedColumn);
        Rectangle minCell = this.table.getCellRect(rMin, draggedColumnIndex, true);
        Rectangle maxCell = this.table.getCellRect(rMax, draggedColumnIndex, true);
        Rectangle vacatedColumnRect = minCell.union(maxCell);
        g2.setColor(this.table.getParent().getBackground());
        g2.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height);
        vacatedColumnRect.x += distance;
        g2.setColor(this.table.getBackground());
        g2.fillRect(vacatedColumnRect.x, vacatedColumnRect.y, vacatedColumnRect.width, vacatedColumnRect.height);
        if (this.table.getShowVerticalLines()) {
            g2.setColor(this.table.getGridColor());
            int x1 = vacatedColumnRect.x;
            int y1 = vacatedColumnRect.y;
            int x2 = x1 + vacatedColumnRect.width - 1;
            int y2 = y1 + vacatedColumnRect.height - 1;
            g2.drawLine(x1 - 1, y1, x1 - 1, y2);
            g2.drawLine(x2, y1, x2, y2);
        }
        for (int row = rMin; row <= rMax; ++row) {
            Rectangle r2 = this.table.getCellRect(row, draggedColumnIndex, false);
            r2.x += distance;
            this.paintCell(g2, r2, r2, row, draggedColumnIndex);
            if (!this.table.getShowHorizontalLines()) continue;
            g2.setColor(this.table.getGridColor());
            Rectangle rcr = this.table.getCellRect(row, draggedColumnIndex, true);
            rcr.x += distance;
            int x1 = rcr.x;
            int y1 = rcr.y;
            int x2 = x1 + rcr.width - 1;
            int y2 = y1 + rcr.height - 1;
            g2.drawLine(x1, y2, x2, y2);
        }
    }

    protected void paintCell(Graphics g2, Rectangle cellRect, Rectangle highlightCellRect, int row, int column) {
        Object highlightProperty;
        boolean hasHighlights;
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates;
        Component rendererComponent = null;
        if (!this.table.isEditing() || this.table.getEditingRow() != row || this.table.getEditingColumn() != column) {
            TableCellRenderer renderer = this.table.getCellRenderer(row, column);
            boolean isSubstanceRenderer = this.isSubstanceDefaultRenderer(renderer);
            rendererComponent = this.table.prepareRenderer(renderer, row, column);
            boolean isSubstanceRendererComponent = this.isSubstanceDefaultRenderer(rendererComponent);
            if (isSubstanceRenderer && !isSubstanceRendererComponent && !Boolean.getBoolean("insubstantial.looseTableCellRenderers")) {
                throw new IllegalArgumentException("Renderer extends the SubstanceDefaultTableCellRenderer but does not return one in its getTableCellRendererComponent() method");
            }
            if (!isSubstanceRenderer) {
                this.rendererPane.paintComponent(g2, rendererComponent, this.table, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
                return;
            }
        }
        Graphics2D g2d = (Graphics2D)g2.create();
        g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.table, g2));
        TableCellId cellId = new TableCellId(row, column);
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.getModelStateInfo(cellId);
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> map = activeStates = modelStateInfo == null ? null : modelStateInfo.getStateContributionMap();
        if (!this.updateInfo.hasRolloverAnimations && !this.updateInfo.hasSelectionAnimations) {
            activeStates = null;
        }
        ComponentState currState = modelStateInfo == null ? this.getCellState(cellId) : modelStateInfo.getCurrModelState();
        boolean bl = hasHighlights = currState != ComponentState.ENABLED || activeStates != null;
        if (activeStates != null) {
            Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry;
            Iterator<Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo>> i$ = activeStates.entrySet().iterator();
            while (i$.hasNext() && !(hasHighlights = this.updateInfo.getHighlightAlpha((stateEntry = i$.next()).getKey()) * stateEntry.getValue().getContribution() > 0.0f)) {
            }
        } else {
            hasHighlights = this.updateInfo.getHighlightAlpha(currState) > 0.0f;
        }
        hasHighlights = (highlightProperty = this.table.getClientProperty("substancelaf.highlightCells")) instanceof Boolean ? ((Boolean)highlightProperty).booleanValue() && hasHighlights : hasHighlights;
        EnumSet<SubstanceConstants.Side> highlightOpenSides = null;
        float highlightBorderAlpha = 0.0f;
        if (hasHighlights) {
            ComponentState leftNeighbourState;
            ComponentState upperNeighbourState;
            highlightOpenSides = EnumSet.noneOf(SubstanceConstants.Side.class);
            float f2 = highlightBorderAlpha = this.table.getShowHorizontalLines() || this.table.getShowVerticalLines() ? 0.0f : 0.8f;
            if (!this.table.getColumnSelectionAllowed() && this.table.getRowSelectionAllowed()) {
                highlightOpenSides.add(SubstanceConstants.Side.LEFT);
                highlightOpenSides.add(SubstanceConstants.Side.RIGHT);
            }
            if (this.table.getColumnSelectionAllowed() && !this.table.getRowSelectionAllowed()) {
                highlightOpenSides.add(SubstanceConstants.Side.TOP);
                highlightOpenSides.add(SubstanceConstants.Side.BOTTOM);
            }
            if (row > 1 && currState == (upperNeighbourState = this.getCellState(new TableCellId(row - 1, column)))) {
                highlightOpenSides.add(SubstanceConstants.Side.TOP);
            }
            if (column > 1 && currState == (leftNeighbourState = this.getCellState(new TableCellId(row, column - 1)))) {
                highlightOpenSides.add(SubstanceConstants.Side.LEFT);
            }
            if (row == 0) {
                highlightOpenSides.add(SubstanceConstants.Side.TOP);
            }
            if (row == this.table.getRowCount() - 1) {
                highlightOpenSides.add(SubstanceConstants.Side.BOTTOM);
            }
            if (column == 0) {
                highlightOpenSides.add(SubstanceConstants.Side.LEFT);
            }
            if (column == this.table.getColumnCount() - 1) {
                highlightOpenSides.add(SubstanceConstants.Side.RIGHT);
            }
        }
        boolean isRollover = this.rolledOverIndices.contains(cellId);
        if (this.table.isEditing() && this.table.getEditingRow() == row && this.table.getEditingColumn() == column) {
            Component component = this.table.getEditorComponent();
            component.applyComponentOrientation(this.table.getComponentOrientation());
            if (hasHighlights) {
                float extra = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.table.getTableHeader()));
                float extraWidth = highlightOpenSides.contains((Object)SubstanceConstants.Side.LEFT) ? 0.0f : extra;
                float extraHeight = highlightOpenSides.contains((Object)SubstanceConstants.Side.TOP) ? 0.0f : extra;
                Rectangle highlightRect = new Rectangle(highlightCellRect.x - (int)extraWidth, highlightCellRect.y - (int)extraHeight, highlightCellRect.width + (int)extraWidth, highlightCellRect.height + (int)extraHeight);
                if (activeStates == null) {
                    float alpha = this.updateInfo.getHighlightAlpha(currState);
                    if (alpha > 0.0f) {
                        SubstanceColorScheme fillScheme = this.updateInfo.getHighlightColorScheme(currState);
                        SubstanceColorScheme borderScheme = this.updateInfo.getHighlightBorderColorScheme(currState);
                        g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.table, alpha, g2));
                        HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, component, highlightRect, highlightBorderAlpha, highlightOpenSides, fillScheme, borderScheme);
                        g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.table, g2));
                    }
                } else {
                    for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry : activeStates.entrySet()) {
                        ComponentState activeState = stateEntry.getKey();
                        float alpha = this.updateInfo.getHighlightAlpha(activeState) * stateEntry.getValue().getContribution();
                        if (alpha == 0.0f) continue;
                        SubstanceColorScheme fillScheme = this.updateInfo.getHighlightColorScheme(activeState);
                        SubstanceColorScheme borderScheme = this.updateInfo.getHighlightBorderColorScheme(activeState);
                        g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.table, alpha, g2));
                        HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, component, highlightRect, highlightBorderAlpha, highlightOpenSides, fillScheme, borderScheme);
                        g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.table, g2));
                    }
                }
            }
            component.setBounds(cellRect);
            component.validate();
        } else {
            boolean isWatermarkBleed = this.updateInfo.toDrawWatermark;
            if (rendererComponent != null) {
                if (!isWatermarkBleed) {
                    Color background = rendererComponent.getBackground();
                    if (background != null && (!this.table.getBackground().equals(background) || this.updateInfo.isInDecorationArea)) {
                        g2d.setColor(background);
                        g2d.fillRect(highlightCellRect.x, highlightCellRect.y, highlightCellRect.width, highlightCellRect.height);
                    }
                } else {
                    BackgroundPaintingUtils.fillAndWatermark(g2d, this.table, rendererComponent.getBackground(), highlightCellRect);
                }
            }
            if (hasHighlights) {
                JTable.DropLocation dropLocation = this.table.getDropLocation();
                if (dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn() && dropLocation.getRow() == row && dropLocation.getColumn() == column) {
                    SubstanceColorScheme scheme = SubstanceColorSchemeUtilities.getColorScheme(this.table, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, currState);
                    SubstanceColorScheme borderScheme = SubstanceColorSchemeUtilities.getColorScheme(this.table, ColorSchemeAssociationKind.BORDER, currState);
                    float extra = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.table.getTableHeader()));
                    HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, rendererComponent, new Rectangle(highlightCellRect.x - (int)extra, highlightCellRect.y - (int)extra, highlightCellRect.width + (int)extra, highlightCellRect.height + (int)extra), 0.8f, null, scheme, borderScheme);
                } else {
                    float extra = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.table.getTableHeader()));
                    float extraWidth = highlightOpenSides.contains((Object)SubstanceConstants.Side.LEFT) ? 0.0f : extra;
                    float extraHeight = highlightOpenSides.contains((Object)SubstanceConstants.Side.TOP) ? 0.0f : extra;
                    Rectangle highlightRect = new Rectangle(highlightCellRect.x - (int)extraWidth, highlightCellRect.y - (int)extraHeight, highlightCellRect.width + (int)extraWidth, highlightCellRect.height + (int)extraHeight);
                    if (activeStates == null) {
                        SubstanceColorScheme fillScheme = this.updateInfo.getHighlightColorScheme(currState);
                        SubstanceColorScheme borderScheme = this.updateInfo.getHighlightBorderColorScheme(currState);
                        float alpha = this.updateInfo.getHighlightAlpha(currState);
                        if (alpha > 0.0f) {
                            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.table, alpha, g2));
                            HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, rendererComponent, highlightRect, highlightBorderAlpha, highlightOpenSides, fillScheme, borderScheme);
                            g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.table, g2));
                        }
                    } else {
                        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> stateEntry : activeStates.entrySet()) {
                            ComponentState activeState = stateEntry.getKey();
                            SubstanceColorScheme fillScheme = this.updateInfo.getHighlightColorScheme(activeState);
                            SubstanceColorScheme borderScheme = this.updateInfo.getHighlightBorderColorScheme(activeState);
                            float alpha = this.updateInfo.getHighlightAlpha(activeState) * stateEntry.getValue().getContribution();
                            if (!(alpha > 0.0f)) continue;
                            g2d.setComposite(LafWidgetUtilities.getAlphaComposite(this.table, alpha, g2));
                            HighlightPainterUtils.paintHighlight(g2d, this.rendererPane, rendererComponent, highlightRect, highlightBorderAlpha, highlightOpenSides, fillScheme, borderScheme);
                            g2d.setComposite(LafWidgetUtilities.getAlphaComposite((Component)this.table, g2));
                        }
                    }
                }
            }
            rendererComponent.applyComponentOrientation(this.table.getComponentOrientation());
            if (rendererComponent instanceof JComponent) {
                boolean newOpaque;
                JComponent jRenderer = (JComponent)rendererComponent;
                boolean isSelected = this.updateInfo.hasSelectionAnimations ? this.selectedIndices.containsKey(cellId) : this.table.isCellSelected(row, column);
                boolean bl2 = newOpaque = !isSelected && !isRollover && !hasHighlights;
                if (this.updateInfo.toDrawWatermark) {
                    newOpaque = false;
                }
                HashMap<Component, Boolean> opacity = new HashMap<Component, Boolean>();
                if (!newOpaque) {
                    SubstanceCoreUtilities.makeNonOpaque(jRenderer, opacity);
                }
                this.rendererPane.paintComponent(g2d, rendererComponent, this.table, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
                if (!newOpaque) {
                    SubstanceCoreUtilities.restoreOpaque(jRenderer, opacity);
                }
            } else {
                this.rendererPane.paintComponent(g2d, rendererComponent, this.table, cellRect.x, cellRect.y, cellRect.width, cellRect.height, true);
            }
        }
        g2d.dispose();
    }

    protected void paintDropLines(Graphics g2) {
        JTable.DropLocation loc = this.table.getDropLocation();
        if (loc == null) {
            return;
        }
        Color color = UIManager.getColor("Table.dropLineColor");
        Color shortColor = UIManager.getColor("Table.dropLineShortColor");
        if (color == null && shortColor == null) {
            return;
        }
        Rectangle rect = this.getHDropLineRect(loc);
        if (rect != null) {
            int x2 = rect.x;
            int w2 = rect.width;
            if (color != null) {
                this.extendRect(rect, true);
                g2.setColor(color);
                g2.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
            if (!loc.isInsertColumn() && shortColor != null) {
                g2.setColor(shortColor);
                g2.fillRect(x2, rect.y, w2, rect.height);
            }
        }
        if ((rect = this.getVDropLineRect(loc)) != null) {
            int y2 = rect.y;
            int h2 = rect.height;
            if (color != null) {
                this.extendRect(rect, false);
                g2.setColor(color);
                g2.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
            if (!loc.isInsertRow() && shortColor != null) {
                g2.setColor(shortColor);
                g2.fillRect(rect.x, y2, rect.width, h2);
            }
        }
    }

    private Rectangle getHDropLineRect(JTable.DropLocation loc) {
        if (!loc.isInsertRow()) {
            return null;
        }
        int row = loc.getRow();
        int col = loc.getColumn();
        if (col >= this.table.getColumnCount()) {
            --col;
        }
        Rectangle rect = this.table.getCellRect(row, col, true);
        if (row >= this.table.getRowCount()) {
            Rectangle prevRect = this.table.getCellRect(--row, col, true);
            rect.y = prevRect.y + prevRect.height;
        }
        rect.y = rect.y == 0 ? -1 : (rect.y -= 2);
        rect.height = 3;
        return rect;
    }

    private Rectangle getVDropLineRect(JTable.DropLocation loc) {
        if (!loc.isInsertColumn()) {
            return null;
        }
        boolean ltr = this.table.getComponentOrientation().isLeftToRight();
        int col = loc.getColumn();
        Rectangle rect = this.table.getCellRect(loc.getRow(), col, true);
        if (col >= this.table.getColumnCount()) {
            rect = this.table.getCellRect(loc.getRow(), --col, true);
            if (ltr) {
                rect.x += rect.width;
            }
        } else if (!ltr) {
            rect.x += rect.width;
        }
        rect.x = rect.x == 0 ? -1 : (rect.x -= 2);
        rect.width = 3;
        return rect;
    }

    private Rectangle extendRect(Rectangle rect, boolean horizontal) {
        if (rect == null) {
            return rect;
        }
        if (horizontal) {
            rect.x = 0;
            rect.width = this.table.getWidth();
        } else {
            rect.y = 0;
            if (this.table.getRowCount() != 0) {
                Rectangle lastRect = this.table.getCellRect(this.table.getRowCount() - 1, 0, true);
                rect.height = lastRect.y + lastRect.height;
            } else {
                rect.height = this.table.getHeight();
            }
        }
        return rect;
    }

    public TableCellId getId(int row, int column) {
        this.cellId.column = column;
        this.cellId.row = row;
        return this.cellId;
    }

    protected void syncSelection(boolean enforceNoAnimations) {
        if (this.table == null) {
            return;
        }
        int rows = this.table.getRowCount();
        int cols = this.table.getColumnCount();
        int rowLeadIndex = this.table.getSelectionModel().getLeadSelectionIndex();
        int colLeadIndex = this.table.getColumnModel().getSelectionModel().getLeadSelectionIndex();
        boolean isFocusOwner = this.table.isFocusOwner();
        if (!this._hasSelectionAnimations()) {
            this.stateTransitionMultiTracker.clear();
            this.table.repaint();
            if (isFocusOwner) {
                this.focusedCellId = new TableCellId(rowLeadIndex, colLeadIndex);
            }
            return;
        }
        HashSet<StateTransitionTracker> initiatedTrackers = new HashSet<StateTransitionTracker>();
        for (int i2 = 0; i2 < rows; ++i2) {
            for (int j2 = 0; j2 < cols; ++j2) {
                ComponentState cellState;
                boolean cellHasFocus;
                TableCellId cellId = new TableCellId(i2, j2);
                if (this.table.isCellSelected(i2, j2)) {
                    if (!this.selectedIndices.containsKey(cellId)) {
                        if (!enforceNoAnimations) {
                            StateTransitionTracker tracker = this.getTracker(cellId, this.getCellState(cellId).isFacetActive(ComponentStateFacet.ROLLOVER), false);
                            tracker.getModel().setSelected(true);
                            initiatedTrackers.add(tracker);
                            if (initiatedTrackers.size() > 20) {
                                this.stateTransitionMultiTracker.clear();
                                initiatedTrackers.clear();
                                enforceNoAnimations = true;
                            }
                        }
                        this.selectedIndices.put(cellId, this.table.getValueAt(i2, j2));
                    }
                } else if (this.selectedIndices.containsKey(cellId)) {
                    Object oldValue = this.selectedIndices.get(cellId);
                    if (i2 >= this.table.getModel().getRowCount() || j2 >= this.table.getModel().getColumnCount()) continue;
                    Object currValue = this.table.getValueAt(i2, j2);
                    boolean isSame = oldValue == null ? currValue == null : oldValue.equals(currValue);
                    if (isSame && !enforceNoAnimations) {
                        StateTransitionTracker tracker = this.getTracker(cellId, this.getCellState(cellId).isFacetActive(ComponentStateFacet.ROLLOVER), true);
                        tracker.getModel().setSelected(false);
                        initiatedTrackers.add(tracker);
                        if (initiatedTrackers.size() > 20) {
                            this.stateTransitionMultiTracker.clear();
                            initiatedTrackers.clear();
                            enforceNoAnimations = true;
                        }
                    }
                    this.selectedIndices.remove(cellId);
                }
                boolean bl = cellHasFocus = isFocusOwner && i2 == rowLeadIndex && j2 == colLeadIndex;
                if (cellHasFocus) {
                    if (this.focusedCellId != null && this.focusedCellId.equals(cellId)) continue;
                    if (!enforceNoAnimations) {
                        if (this.focusedCellId != null) {
                            cellState = this.getCellState(this.focusedCellId);
                            StateTransitionTracker tracker = this.getTracker(this.focusedCellId, cellState.isFacetActive(ComponentStateFacet.ROLLOVER), cellState.isFacetActive(ComponentStateFacet.SELECTION));
                            tracker.setFocusState(false);
                        }
                        cellState = this.getCellState(cellId);
                        StateTransitionTracker tracker = this.getTracker(cellId, cellState.isFacetActive(ComponentStateFacet.ROLLOVER), cellState.isFacetActive(ComponentStateFacet.SELECTION));
                        tracker.setFocusState(true);
                    }
                    if (!AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.FOCUS, this.table)) continue;
                    this.focusedCellId = new TableCellId(i2, j2);
                    continue;
                }
                if (!cellId.equals(this.focusedCellId)) continue;
                if (!enforceNoAnimations) {
                    cellState = this.getCellState(cellId);
                    StateTransitionTracker tracker = this.getTracker(cellId, cellState.isFacetActive(ComponentStateFacet.ROLLOVER), cellState.isFacetActive(ComponentStateFacet.SELECTION));
                    tracker.setFocusState(false);
                }
                this.focusedCellId = null;
            }
        }
    }

    public ComponentState getCellState(TableCellId cellIndex) {
        boolean isEnabled = this.table.isEnabled();
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker(cellIndex);
        if (tracker == null) {
            int row = cellIndex.row;
            int column = cellIndex.column;
            TableCellId cellId = this.getId(row, column);
            boolean isRollover = this.rolledOverIndices.contains(cellId);
            boolean hasSelectionAnimations = this.updateInfo != null ? this.updateInfo.hasSelectionAnimations : this._hasSelectionAnimations();
            boolean isSelected = hasSelectionAnimations && AnimationConfigurationManager.getInstance().isAnimationAllowed(AnimationFacet.SELECTION, this.table) ? this.selectedIndices.containsKey(cellId) : this.table.isCellSelected(row, column);
            return ComponentState.getState(isEnabled, isRollover, isSelected);
        }
        ComponentState fromTracker = tracker.getModelStateInfo().getCurrModelState();
        return ComponentState.getState(isEnabled, fromTracker.isFacetActive(ComponentStateFacet.ROLLOVER), fromTracker.isFacetActive(ComponentStateFacet.SELECTION));
    }

    public StateTransitionTracker.ModelStateInfo getModelStateInfo(TableCellId cellId) {
        if (this.stateTransitionMultiTracker.size() == 0) {
            return null;
        }
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker(cellId);
        if (tracker == null) {
            return null;
        }
        return tracker.getModelStateInfo();
    }

    protected boolean _hasAnimations() {
        int colCount;
        int rowCount = this.table.getRowCount();
        if (rowCount * (colCount = this.table.getColumnCount()) >= 500) {
            return false;
        }
        if (this.table.getColumnSelectionAllowed() && !this.table.getRowSelectionAllowed()) {
            if (!this.table.getShowHorizontalLines() && !this.table.getShowVerticalLines()) {
                return rowCount <= 10;
            }
            return rowCount <= 25;
        }
        if (!this.table.getColumnSelectionAllowed() && this.table.getRowSelectionAllowed()) {
            if (!this.table.getShowHorizontalLines() && !this.table.getShowVerticalLines()) {
                return colCount <= 10;
            }
            return colCount <= 25;
        }
        return true;
    }

    protected boolean _hasSelectionAnimations() {
        return this._hasAnimations() && !LafWidgetUtilities.hasNoAnimations(this.table, AnimationFacet.SELECTION);
    }

    protected boolean _hasRolloverAnimations() {
        return this._hasAnimations() && !LafWidgetUtilities.hasNoAnimations(this.table, AnimationFacet.ROLLOVER);
    }

    public int getRolloverColumnIndex() {
        return this.rolledOverColumn;
    }

    public boolean isFocusedCell(int row, int column) {
        return this.focusedCellId != null && this.focusedCellId.row == row && this.focusedCellId.column == column;
    }

    public void __org__pushingpixels__substance__internal__ui__SubstanceTableUI__update(Graphics g2, JComponent c2) {
        BackgroundPaintingUtils.updateIfOpaque(g2, c2);
        Graphics2D g2d = (Graphics2D)g2.create();
        SubstanceStripingUtils.setup(c2);
        this.updateInfo = new TableUpdateOptimizationInfo();
        this.paint(g2d, c2);
        SubstanceStripingUtils.tearDown(c2);
        g2d.dispose();
        this.updateInfo = null;
    }

    public Insets getCellRendererInsets() {
        return this.cellRendererInsets;
    }

    public boolean hasSelectionAnimations() {
        if (this.updateInfo != null) {
            return this.updateInfo.hasSelectionAnimations;
        }
        return this._hasSelectionAnimations();
    }

    public boolean hasRolloverAnimations() {
        if (this.updateInfo != null) {
            return this.updateInfo.hasRolloverAnimations;
        }
        return this._hasRolloverAnimations();
    }

    @Override
    public UpdateOptimizationInfo getUpdateOptimizationInfo() {
        return this.updateInfo;
    }

    private boolean isSubstanceDefaultRenderer(Object instance) {
        return instance instanceof SubstanceDefaultTableCellRenderer || instance instanceof SubstanceDefaultTableCellRenderer.BooleanRenderer;
    }

    private boolean isSubstanceDefaultEditor(TableCellEditor editor) {
        return editor instanceof BooleanEditor;
    }

    private Rectangle getCellRectangleForRepaint(int row, int column) {
        Rectangle rect = this.table.getCellRect(row, column, true);
        if (!this.table.getShowHorizontalLines() && !this.table.getShowVerticalLines()) {
            float extra = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(this.table.getTableHeader()));
            rect.x -= (int)extra;
            rect.width += 2 * (int)extra;
            rect.y -= (int)extra;
            rect.height += 2 * (int)extra;
        }
        return rect;
    }

    private StateTransitionTracker getTracker(final TableCellId tableCellId, boolean initialRollover, boolean initialSelected) {
        StateTransitionTracker tracker = this.stateTransitionMultiTracker.getTracker(tableCellId);
        if (tracker == null) {
            DefaultButtonModel model = new DefaultButtonModel();
            model.setSelected(initialSelected);
            model.setRollover(initialRollover);
            tracker = new StateTransitionTracker(this.table, model);
            tracker.registerModelListeners();
            tracker.setRepaintCallback(new StateTransitionTracker.RepaintCallback(){

                @Override
                public TimelineCallback getRepaintCallback() {
                    return new CellRepaintCallback(SubstanceTableUI.this.table, tableCellId.row, tableCellId.column);
                }
            });
            tracker.setName("row " + tableCellId.row + ", col " + tableCellId.column);
            this.stateTransitionMultiTracker.addTracker(tableCellId, tracker);
        }
        return tracker;
    }

    public StateTransitionTracker getStateTransitionTracker(TableCellId tableId) {
        return this.stateTransitionMultiTracker.getTracker(tableId);
    }

    private class TableUpdateOptimizationInfo
    extends UpdateOptimizationInfo {
        public boolean hasSelectionAnimations;
        public boolean hasRolloverAnimations;

        public TableUpdateOptimizationInfo() {
            super(SubstanceTableUI.this.table);
            this.hasSelectionAnimations = SubstanceTableUI.this._hasSelectionAnimations();
            this.hasRolloverAnimations = SubstanceTableUI.this._hasRolloverAnimations();
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
            if (SubstanceTableUI.this.table == null) {
                return;
            }
            if (!SubstanceTableUI.this.table.isEnabled()) {
                return;
            }
            PointerInfo pi = MouseInfo.getPointerInfo();
            Point mouseLoc = pi == null ? null : pi.getLocation();
            Window windowAncestor = SwingUtilities.getWindowAncestor(SubstanceTableUI.this.table);
            if (mouseLoc != null && windowAncestor != null) {
                SwingUtilities.convertPointFromScreen(mouseLoc, windowAncestor);
                for (Component deepest = SwingUtilities.getDeepestComponentAt(windowAncestor, mouseLoc.x, mouseLoc.y); deepest != null; deepest = deepest.getParent()) {
                    if (deepest != SubstanceTableUI.this.table) continue;
                    return;
                }
            }
            this.fadeOutAllRollovers();
            this.fadeOutTableHeader();
            SubstanceTableUI.this.rolledOverIndices.clear();
            SubstanceTableUI.this.rolledOverColumn = -1;
        }

        @Override
        public void mouseMoved(MouseEvent e2) {
            if (!SubstanceTableUI.this.table.isEnabled()) {
                return;
            }
            this.handleMouseMove(e2.getPoint());
            this.handleMoveForHeader(e2);
        }

        @Override
        public void mouseDragged(MouseEvent e2) {
            if (!SubstanceTableUI.this.table.isEnabled()) {
                return;
            }
            this.handleMouseMove(e2.getPoint());
            this.handleMoveForHeader(e2);
        }

        private void handleMoveForHeader(MouseEvent e2) {
            if (!SubstanceTableUI.this.table.getColumnSelectionAllowed()) {
                return;
            }
            JTableHeader header = SubstanceTableUI.this.table.getTableHeader();
            if (header == null || !header.isVisible()) {
                return;
            }
            TableHeaderUI ui = header.getUI();
            if (!(ui instanceof SubstanceTableHeaderUI)) {
                return;
            }
            SubstanceTableHeaderUI sthui = (SubstanceTableHeaderUI)ui;
            int row = SubstanceTableUI.this.table.rowAtPoint(e2.getPoint());
            int column = SubstanceTableUI.this.table.columnAtPoint(e2.getPoint());
            if (row < 0 || row >= SubstanceTableUI.this.table.getRowCount() || column < 0 || column >= SubstanceTableUI.this.table.getColumnCount()) {
                this.fadeOutTableHeader();
                SubstanceTableUI.this.rolledOverColumn = -1;
            } else {
                if (SubstanceTableUI.this.rolledOverColumn == column) {
                    return;
                }
                this.fadeOutTableHeader();
                TableColumnModel columnModel = header.getColumnModel();
                StateTransitionTracker columnTransitionTracker = sthui.getTracker(column, false, columnModel.getColumnSelectionAllowed() && columnModel.getSelectionModel().isSelectedIndex(column));
                columnTransitionTracker.getModel().setRollover(true);
                SubstanceTableUI.this.rolledOverColumn = column;
            }
        }

        private void fadeOutTableHeader() {
            if (SubstanceTableUI.this.rolledOverColumn >= 0) {
                JTableHeader header = SubstanceTableUI.this.table.getTableHeader();
                if (header == null || !header.isVisible()) {
                    return;
                }
                SubstanceTableHeaderUI ui = (SubstanceTableHeaderUI)header.getUI();
                TableColumnModel columnModel = header.getColumnModel();
                StateTransitionTracker columnTransitionTracker = ui.getTracker(SubstanceTableUI.this.rolledOverColumn, true, columnModel.getColumnSelectionAllowed() && columnModel.getSelectionModel().isSelectedIndex(SubstanceTableUI.this.rolledOverColumn));
                columnTransitionTracker.getModel().setRollover(false);
            }
        }

        private void handleMouseMove(Point mousePoint) {
            int row = SubstanceTableUI.this.table.rowAtPoint(mousePoint);
            int column = SubstanceTableUI.this.table.columnAtPoint(mousePoint);
            if (row < 0 || row >= SubstanceTableUI.this.table.getRowCount() || column < 0 || column >= SubstanceTableUI.this.table.getColumnCount()) {
                this.fadeOutAllRollovers();
                SubstanceTableUI.this.rolledOverIndices.clear();
            } else {
                boolean hasRowSelection = SubstanceTableUI.this.table.getRowSelectionAllowed();
                boolean hasColumnSelection = SubstanceTableUI.this.table.getColumnSelectionAllowed();
                int startRolloverRow = row;
                int endRolloverRow = row;
                int startRolloverColumn = column;
                int endRolloverColumn = column;
                if (hasRowSelection && !hasColumnSelection) {
                    startRolloverColumn = 0;
                    endRolloverColumn = SubstanceTableUI.this.table.getColumnCount() - 1;
                }
                if (!hasRowSelection && hasColumnSelection) {
                    startRolloverRow = 0;
                    endRolloverRow = SubstanceTableUI.this.table.getRowCount() - 1;
                }
                HashSet<TableCellId> toRemove = new HashSet<TableCellId>();
                for (TableCellId currRolloverId : SubstanceTableUI.this.rolledOverIndices) {
                    if (currRolloverId.row >= startRolloverRow && currRolloverId.row <= endRolloverRow && currRolloverId.column >= startRolloverColumn && currRolloverId.column <= endRolloverColumn) continue;
                    this.fadeOutRollover(currRolloverId);
                    toRemove.add(currRolloverId);
                }
                for (TableCellId id : toRemove) {
                    SubstanceTableUI.this.rolledOverIndices.remove(id);
                }
                int totalRolloverCount = (endRolloverRow - startRolloverRow + 1) * (endRolloverColumn - startRolloverColumn + 1);
                if (totalRolloverCount > 20) {
                    for (int i2 = startRolloverRow; i2 <= endRolloverRow; ++i2) {
                        for (int j2 = startRolloverColumn; j2 <= endRolloverColumn; ++j2) {
                            SubstanceTableUI.this.rolledOverIndices.add(new TableCellId(i2, j2));
                        }
                    }
                    SubstanceTableUI.this.table.repaint();
                } else {
                    for (int i3 = startRolloverRow; i3 <= endRolloverRow; ++i3) {
                        for (int j3 = startRolloverColumn; j3 <= endRolloverColumn; ++j3) {
                            TableCellId currCellId = new TableCellId(i3, j3);
                            if (SubstanceTableUI.this.rolledOverIndices.contains(currCellId)) continue;
                            StateTransitionTracker tracker = SubstanceTableUI.this.getTracker(currCellId, false, SubstanceTableUI.this.getCellState(currCellId).isFacetActive(ComponentStateFacet.SELECTION));
                            tracker.getModel().setRollover(true);
                            SubstanceTableUI.this.rolledOverIndices.add(currCellId);
                        }
                    }
                }
            }
        }

        private void fadeOutRollover(TableCellId tableCellId) {
            if (SubstanceTableUI.this.rolledOverIndices.contains(tableCellId)) {
                StateTransitionTracker tracker = SubstanceTableUI.this.getTracker(tableCellId, true, SubstanceTableUI.this.getCellState(tableCellId).isFacetActive(ComponentStateFacet.SELECTION));
                tracker.getModel().setRollover(false);
            }
        }

        private void fadeOutAllRollovers() {
            if (SubstanceTableUI.this.rolledOverIndices.size() < 20) {
                for (TableCellId tcid : SubstanceTableUI.this.rolledOverIndices) {
                    this.fadeOutRollover(tcid);
                }
            }
        }
    }

    protected class TableStateListener
    implements ListSelectionListener,
    TableModelListener,
    RowSorterListener {
        List<RowSorter.SortKey> oldSortKeys = null;

        protected TableStateListener() {
        }

        private boolean isSameSorter(List<? extends RowSorter.SortKey> sortKeys1, List<? extends RowSorter.SortKey> sortKeys2) {
            int size2;
            int size1 = sortKeys1 == null ? 0 : sortKeys1.size();
            int n2 = size2 = sortKeys2 == null ? 0 : sortKeys2.size();
            if (size1 == 0 && size2 == 0) {
                return true;
            }
            if (sortKeys1 == null && sortKeys2 == null) {
                return true;
            }
            if (sortKeys1 == null || sortKeys2 == null) {
                return false;
            }
            if (size1 != size2) {
                return false;
            }
            for (int i2 = 0; i2 < size1; ++i2) {
                RowSorter.SortKey sortKey1 = sortKeys1.get(i2);
                RowSorter.SortKey sortKey2 = sortKeys2.get(i2);
                if (sortKey1.getColumn() == sortKey2.getColumn() && sortKey1.getSortOrder() == sortKey2.getSortOrder()) continue;
                return false;
            }
            return true;
        }

        @Override
        public void valueChanged(ListSelectionEvent e2) {
            boolean isDifferentSorter;
            List<RowSorter.SortKey> sortKeys = SubstanceTableUI.this.table.getRowSorter() == null ? null : SubstanceTableUI.this.table.getRowSorter().getSortKeys();
            boolean bl = isDifferentSorter = !this.isSameSorter(sortKeys, this.oldSortKeys);
            if (e2.getValueIsAdjusting() && isDifferentSorter) {
                return;
            }
            if (sortKeys == null) {
                this.oldSortKeys = null;
            } else {
                this.oldSortKeys = new ArrayList<RowSorter.SortKey>();
                for (RowSorter.SortKey sortKey : sortKeys) {
                    RowSorter.SortKey copy = new RowSorter.SortKey(sortKey.getColumn(), sortKey.getSortOrder());
                    this.oldSortKeys.add(copy);
                }
            }
            SubstanceTableUI.this.syncSelection(isDifferentSorter);
        }

        @Override
        public void tableChanged(final TableModelEvent e2) {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    if (SubstanceTableUI.this.table == null) {
                        return;
                    }
                    if (e2.getType() != 0) {
                        SubstanceTableUI.this.selectedIndices.clear();
                        SubstanceTableUI.this.stateTransitionMultiTracker.clear();
                        SubstanceTableUI.this.focusedCellId = null;
                    }
                    SubstanceTableUI.this.syncSelection(true);
                    SubstanceTableUI.this.table.repaint();
                }
            });
        }

        @Override
        public void sorterChanged(RowSorterEvent e2) {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    SubstanceTableUI.this.stateTransitionMultiTracker.clear();
                }
            });
        }
    }

    public static class TableCellId
    implements Comparable<TableCellId> {
        protected int row;
        protected int column;

        public TableCellId(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public int compareTo(TableCellId o2) {
            if (this.row == o2.row && this.column == o2.column) {
                return 0;
            }
            return 1;
        }

        public boolean equals(Object obj) {
            if (obj instanceof TableCellId) {
                return this.compareTo((TableCellId)obj) == 0;
            }
            return false;
        }

        public int hashCode() {
            return (this.row ^ this.row >>> 32) & (this.column ^ this.column >>> 32);
        }

        public String toString() {
            return "Row " + this.row + ", Column " + this.column;
        }
    }

    protected class ColumnRepaintCallback
    extends UIThreadTimelineCallbackAdapter {
        protected JTable table;
        protected int columnIndex;

        public ColumnRepaintCallback(JTable table, int columnIndex) {
            this.table = table;
            this.columnIndex = columnIndex;
        }

        @Override
        public void onTimelinePulse(float durationFraction, float timelinePosition) {
            this.repaintColumn();
        }

        @Override
        public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
            this.repaintColumn();
        }

        private void repaintColumn() {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    if (SubstanceTableUI.this.table == null) {
                        return;
                    }
                    int columnCount = ColumnRepaintCallback.this.table.getColumnCount();
                    if (columnCount > 0 && ColumnRepaintCallback.this.columnIndex < columnCount) {
                        Rectangle rect = ColumnRepaintCallback.this.table.getCellRect(0, ColumnRepaintCallback.this.columnIndex, true);
                        for (int i2 = 1; i2 < ColumnRepaintCallback.this.table.getRowCount(); ++i2) {
                            rect = rect.union(ColumnRepaintCallback.this.table.getCellRect(i2, ColumnRepaintCallback.this.columnIndex, true));
                        }
                        if (!ColumnRepaintCallback.this.table.getShowHorizontalLines() && !ColumnRepaintCallback.this.table.getShowVerticalLines()) {
                            float extra = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(ColumnRepaintCallback.this.table.getTableHeader()));
                            rect.x -= (int)extra;
                            rect.width += 2 * (int)extra;
                        }
                        ColumnRepaintCallback.this.table.repaint(rect);
                    }
                }
            });
        }
    }

    protected class RowRepaintCallback
    extends UIThreadTimelineCallbackAdapter {
        protected JTable table;
        protected int rowIndex;

        public RowRepaintCallback(JTable table, int rowIndex) {
            this.table = table;
            this.rowIndex = rowIndex;
        }

        @Override
        public void onTimelinePulse(float durationFraction, float timelinePosition) {
            this.repaintRow();
        }

        @Override
        public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
            this.repaintRow();
        }

        private void repaintRow() {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    if (SubstanceTableUI.this.table == null) {
                        return;
                    }
                    int rowCount = RowRepaintCallback.this.table.getRowCount();
                    if (rowCount > 0 && RowRepaintCallback.this.rowIndex < rowCount) {
                        Rectangle rect = RowRepaintCallback.this.table.getCellRect(RowRepaintCallback.this.rowIndex, 0, true);
                        for (int i2 = 1; i2 < RowRepaintCallback.this.table.getColumnCount(); ++i2) {
                            rect = rect.union(RowRepaintCallback.this.table.getCellRect(RowRepaintCallback.this.rowIndex, i2, true));
                        }
                        if (!RowRepaintCallback.this.table.getShowHorizontalLines() && !RowRepaintCallback.this.table.getShowVerticalLines()) {
                            float extra = SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(RowRepaintCallback.this.table.getTableHeader()));
                            rect.y -= (int)extra;
                            rect.height += 2 * (int)extra;
                        }
                        RowRepaintCallback.this.table.repaint(rect);
                    }
                }
            });
        }
    }

    protected class CellRepaintCallback
    extends UIThreadTimelineCallbackAdapter {
        protected JTable table;
        protected int rowIndex;
        protected int columnIndex;

        public CellRepaintCallback(JTable table, int rowIndex, int columnIndex) {
            this.table = table;
            this.rowIndex = rowIndex;
            this.columnIndex = columnIndex;
        }

        @Override
        public void onTimelinePulse(float durationFraction, float timelinePosition) {
            this.repaintCell();
        }

        @Override
        public void onTimelineStateChanged(Timeline.TimelineState oldState, Timeline.TimelineState newState, float durationFraction, float timelinePosition) {
            this.repaintCell();
        }

        private void repaintCell() {
            SwingUtilities.invokeLater(new Runnable(){

                @Override
                public void run() {
                    if (SubstanceTableUI.this.table == null) {
                        return;
                    }
                    int rowCount = CellRepaintCallback.this.table.getRowCount();
                    int colCount = CellRepaintCallback.this.table.getColumnCount();
                    if (rowCount > 0 && CellRepaintCallback.this.rowIndex < rowCount && colCount > 0 && CellRepaintCallback.this.columnIndex < colCount) {
                        Rectangle rect = SubstanceTableUI.this.getCellRectangleForRepaint(CellRepaintCallback.this.rowIndex, CellRepaintCallback.this.columnIndex);
                        CellRepaintCallback.this.table.repaint(rect);
                    }
                }
            });
        }
    }

    static class BooleanEditor
    extends DefaultCellEditor {
        public BooleanEditor() {
            super(new SubstanceEditorCheckBox());
            JCheckBox checkBox = (JCheckBox)this.getComponent();
            checkBox.setOpaque(false);
            checkBox.setHorizontalAlignment(0);
        }

        private static class SubstanceEditorCheckBox
        extends JCheckBox {
            private SubstanceEditorCheckBox() {
            }

            @Override
            public void setOpaque(boolean isOpaque) {
                if (!isOpaque) {
                    super.setOpaque(isOpaque);
                }
            }

            @Override
            public boolean isOpaque() {
                return false;
            }

            @Override
            public void setBorder(Border border) {
            }
        }
    }
}

