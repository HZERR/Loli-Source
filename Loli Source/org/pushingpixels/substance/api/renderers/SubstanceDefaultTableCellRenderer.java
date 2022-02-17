/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.TableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.renderers.SubstanceRenderer;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.ui.SubstanceTableUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceStripingUtils;
import org.pushingpixels.substance.internal.utils.UpdateOptimizationInfo;
import org.pushingpixels.substance.internal.utils.border.SubstanceTableCellBorder;

@SubstanceRenderer
public class SubstanceDefaultTableCellRenderer
extends DefaultTableCellRenderer {
    public SubstanceDefaultTableCellRenderer() {
        this.putClientProperty("substancelaf.colorizationFactor", 1.0);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        SubstanceColorScheme scheme;
        boolean isDropLocation;
        if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        TableUI tableUI = table.getUI();
        SubstanceTableUI ui = (SubstanceTableUI)tableUI;
        hasFocus = ui.isFocusedCell(row, column);
        SubstanceTableUI.TableCellId cellId = new SubstanceTableUI.TableCellId(row, column);
        StateTransitionTracker.ModelStateInfo modelStateInfo = ui.getModelStateInfo(cellId);
        ComponentState currState = ui.getCellState(cellId);
        JTable.DropLocation dropLocation = table.getDropLocation();
        boolean bl = isDropLocation = dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn() && dropLocation.getRow() == row && dropLocation.getColumn() == column;
        if (!isDropLocation && modelStateInfo != null) {
            if (ui.hasRolloverAnimations() || ui.hasSelectionAnimations()) {
                Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
                SubstanceColorScheme colorScheme = this.getColorSchemeForState(table, ui, currState);
                if (currState.isDisabled() || activeStates == null || activeStates.size() == 1) {
                    super.setForeground(new ColorUIResource(colorScheme.getForegroundColor()));
                } else {
                    float aggrRed = 0.0f;
                    float aggrGreen = 0.0f;
                    float aggrBlue = 0.0f;
                    for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : modelStateInfo.getStateContributionMap().entrySet()) {
                        ComponentState activeState = activeEntry.getKey();
                        SubstanceColorScheme scheme2 = this.getColorSchemeForState(table, ui, activeState);
                        Color schemeFg = scheme2.getForegroundColor();
                        float contribution = activeEntry.getValue().getContribution();
                        aggrRed += (float)schemeFg.getRed() * contribution;
                        aggrGreen += (float)schemeFg.getGreen() * contribution;
                        aggrBlue += (float)schemeFg.getBlue() * contribution;
                    }
                    super.setForeground(new ColorUIResource(new Color((int)aggrRed, (int)aggrGreen, (int)aggrBlue)));
                }
            } else {
                scheme = this.getColorSchemeForState(table, ui, currState);
                super.setForeground(new ColorUIResource(scheme.getForegroundColor()));
            }
        } else {
            scheme = this.getColorSchemeForState(table, ui, currState);
            if (isDropLocation) {
                scheme = SubstanceColorSchemeUtilities.getColorScheme(table, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, currState);
            }
            super.setForeground(new ColorUIResource(scheme.getForegroundColor()));
        }
        SubstanceStripingUtils.applyStripedBackground(table, row, this);
        this.setFont(table.getFont());
        SubstanceTableUI.TableCellId cellFocusId = new SubstanceTableUI.TableCellId(row, column);
        StateTransitionTracker focusStateTransitionTracker = ui.getStateTransitionTracker(cellFocusId);
        Insets regInsets = ui.getCellRendererInsets();
        if (hasFocus || focusStateTransitionTracker != null) {
            SubstanceTableCellBorder border = new SubstanceTableCellBorder(regInsets, ui, cellFocusId);
            if (focusStateTransitionTracker != null) {
                border.setAlpha(focusStateTransitionTracker.getFocusStrength(hasFocus));
            }
            if (!table.getShowHorizontalLines() && !table.getShowVerticalLines()) {
                this.setBorder(new CompoundBorder(new EmptyBorder(table.getRowMargin() / 2, 0, table.getRowMargin() / 2, 0), border));
            } else {
                this.setBorder(border);
            }
        } else {
            this.setBorder(new EmptyBorder(regInsets.top, regInsets.left, regInsets.bottom, regInsets.right));
        }
        this.setValue(value);
        this.setOpaque(false);
        this.setEnabled(table.isEnabled());
        return this;
    }

    private SubstanceColorScheme getColorSchemeForState(JTable table, SubstanceTableUI ui, ComponentState state) {
        UpdateOptimizationInfo updateOptimizationInfo = ui.getUpdateOptimizationInfo();
        if (state == ComponentState.ENABLED) {
            if (updateOptimizationInfo == null) {
                return SubstanceColorSchemeUtilities.getColorScheme(table, state);
            }
            return updateOptimizationInfo.getDefaultScheme();
        }
        if (updateOptimizationInfo == null) {
            return SubstanceColorSchemeUtilities.getColorScheme(table, ColorSchemeAssociationKind.HIGHLIGHT, state);
        }
        return updateOptimizationInfo.getHighlightColorScheme(state);
    }

    @Override
    public final void paint(Graphics g2) {
        super.paint(g2);
    }

    @Override
    protected final void paintComponent(Graphics g2) {
        super.paintComponent(g2);
    }

    @Override
    protected final void paintBorder(Graphics g2) {
        super.paintBorder(g2);
    }

    @Override
    public final void paintComponents(Graphics g2) {
    }

    public static class DateRenderer
    extends SubstanceDefaultTableCellRenderer {
        DateFormat formatter;

        @Override
        public void setValue(Object value) {
            if (this.formatter == null) {
                this.formatter = DateFormat.getDateInstance();
            }
            this.setText(value == null ? "" : this.formatter.format(value));
        }
    }

    public static class DoubleRenderer
    extends NumberRenderer {
        NumberFormat formatter;

        @Override
        public void setValue(Object value) {
            if (this.formatter == null) {
                this.formatter = NumberFormat.getInstance();
            }
            this.setText(value == null ? "" : this.formatter.format(value));
        }
    }

    public static class NumberRenderer
    extends SubstanceDefaultTableCellRenderer {
        public NumberRenderer() {
            this.setHorizontalAlignment(4);
        }
    }

    public static class IconRenderer
    extends SubstanceDefaultTableCellRenderer {
        public IconRenderer() {
            this.setHorizontalAlignment(0);
        }

        @Override
        public void setValue(Object value) {
            this.setIcon(value instanceof Icon ? (Icon)value : null);
            this.setText(null);
        }
    }

    @SubstanceRenderer
    public static class BooleanRenderer
    extends JCheckBox
    implements TableCellRenderer {
        private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        public BooleanRenderer() {
            this.setHorizontalAlignment(0);
            this.setBorderPainted(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                this.setForeground(table.getSelectionForeground());
            } else {
                this.setForeground(table.getForeground());
            }
            SubstanceStripingUtils.applyStripedBackground(table, row, this);
            this.setSelected(value != null && (Boolean)value != false);
            this.setEnabled(table.isEnabled());
            TableUI tableUI = table.getUI();
            if (tableUI instanceof SubstanceTableUI) {
                SubstanceTableUI ui = (SubstanceTableUI)tableUI;
                hasFocus = ui.isFocusedCell(row, column);
                SubstanceTableUI.TableCellId cellFocusId = new SubstanceTableUI.TableCellId(row, column);
                StateTransitionTracker stateTransitionTracker = ui.getStateTransitionTracker(cellFocusId);
                if (hasFocus || stateTransitionTracker != null) {
                    SubstanceTableCellBorder border = new SubstanceTableCellBorder(new Insets(0, 0, 0, 0), ui, cellFocusId);
                    if (stateTransitionTracker != null) {
                        border.setAlpha(stateTransitionTracker.getFocusStrength(hasFocus));
                    }
                    this.setBorder(border);
                } else {
                    this.setBorder(noFocusBorder);
                }
            } else if (hasFocus) {
                this.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                this.setBorder(noFocusBorder);
            }
            this.setOpaque(false);
            return this;
        }

        @Override
        public final void paint(Graphics g2) {
            super.paint(g2);
        }

        @Override
        protected final void paintComponent(Graphics g2) {
            super.paintComponent(g2);
        }

        @Override
        protected final void paintBorder(Graphics g2) {
            super.paintBorder(g2);
        }

        @Override
        public final void paintComponents(Graphics g2) {
        }
    }
}

