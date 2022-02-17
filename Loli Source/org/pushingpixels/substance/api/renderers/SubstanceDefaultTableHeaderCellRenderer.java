/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.renderers.SubstanceRenderer;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.ui.SubstanceTableHeaderUI;
import org.pushingpixels.substance.internal.ui.SubstanceTableUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

@SubstanceRenderer
public class SubstanceDefaultTableHeaderCellRenderer
extends DefaultTableCellRenderer
implements UIResource {
    public SubstanceDefaultTableHeaderCellRenderer() {
        this.setHorizontalAlignment(0);
        this.putClientProperty("substancelaf.colorizationFactor", 1.0);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (table == null) {
            this.setBorder(DefaultTableCellRenderer.noFocusBorder);
            this.setValue(value);
            this.setOpaque(false);
            return this;
        }
        if (table.getTableHeader() == null) {
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
        JTableHeader tableHeader = table.getTableHeader();
        TableHeaderUI tableHeaderUI = tableHeader.getUI();
        if (SubstanceLookAndFeel.isCurrentLookAndFeel() && tableHeaderUI instanceof SubstanceTableHeaderUI) {
            SubstanceTableHeaderUI ui = (SubstanceTableHeaderUI)tableHeaderUI;
            StateTransitionTracker.ModelStateInfo modelStateInfo = ui.getModelStateInfo(column);
            ComponentState currState = ui.getColumnState(column);
            if (modelStateInfo != null) {
                Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
                SubstanceColorScheme colorScheme = this.getColorSchemeForState(tableHeader, currState);
                if (currState.isDisabled() || activeStates == null || activeStates.size() == 1) {
                    super.setForeground(new ColorUIResource(colorScheme.getForegroundColor()));
                } else {
                    float aggrRed = 0.0f;
                    float aggrGreen = 0.0f;
                    float aggrBlue = 0.0f;
                    for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : modelStateInfo.getStateContributionMap().entrySet()) {
                        ComponentState activeState = activeEntry.getKey();
                        SubstanceColorScheme scheme = this.getColorSchemeForState(tableHeader, activeState);
                        Color schemeFg = scheme.getForegroundColor();
                        float contribution = activeEntry.getValue().getContribution();
                        aggrRed += (float)schemeFg.getRed() * contribution;
                        aggrGreen += (float)schemeFg.getGreen() * contribution;
                        aggrBlue += (float)schemeFg.getBlue() * contribution;
                    }
                    super.setForeground(new ColorUIResource(new Color((int)aggrRed, (int)aggrGreen, (int)aggrBlue)));
                }
            } else {
                SubstanceColorScheme scheme = this.getColorSchemeForState(tableHeader, currState);
                super.setForeground(new ColorUIResource(scheme.getForegroundColor()));
            }
        } else {
            super.setForeground(table.getForeground());
        }
        this.setBackground(tableHeader.getBackground());
        if (tableHeader.getFont() != null) {
            this.setFont(tableHeader.getFont());
        } else {
            this.setFont(table.getFont());
        }
        TableUI tableUI = table.getUI();
        if (SubstanceLookAndFeel.isCurrentLookAndFeel() && tableUI instanceof SubstanceTableUI) {
            this.setBorder(new EmptyBorder(((SubstanceTableUI)tableUI).getCellRendererInsets()));
        }
        this.setValue(value);
        this.setOpaque(false);
        this.setEnabled(tableHeader.isEnabled() && table.isEnabled());
        if (SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            this.setIcon(null);
            RowSorter<? extends TableModel> rowSorter = table.getRowSorter();
            if (rowSorter != null) {
                this.setHorizontalTextPosition(10);
                List<RowSorter.SortKey> sortKeys = rowSorter.getSortKeys();
                Icon sortIcon = null;
                SubstanceColorScheme scheme = null;
                if (tableHeaderUI instanceof SubstanceTableHeaderUI) {
                    SubstanceTableHeaderUI ui = (SubstanceTableHeaderUI)tableHeaderUI;
                    ComponentState state = ui.getColumnState(column);
                    ColorSchemeAssociationKind colorSchemeAssociationKind = state == ComponentState.ENABLED ? ColorSchemeAssociationKind.MARK : ColorSchemeAssociationKind.HIGHLIGHT_MARK;
                    scheme = SubstanceColorSchemeUtilities.getColorScheme(tableHeader, colorSchemeAssociationKind, state);
                } else {
                    scheme = SubstanceColorSchemeUtilities.getColorScheme(tableHeader, ComponentState.ENABLED);
                }
                if (sortKeys.size() > 0 && sortKeys.get(0).getColumn() == table.convertColumnIndexToModel(column)) {
                    switch (sortKeys.get(0).getSortOrder()) {
                        case ASCENDING: {
                            sortIcon = SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getComponentFontSize(tableHeader), 1, scheme);
                            break;
                        }
                        case DESCENDING: {
                            sortIcon = SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getComponentFontSize(tableHeader), 5, scheme);
                            break;
                        }
                        case UNSORTED: {
                            sortIcon = null;
                        }
                    }
                    this.setIcon(sortIcon);
                }
            }
        }
        return this;
    }

    private SubstanceColorScheme getColorSchemeForState(JTableHeader tableHeader, ComponentState activeState) {
        SubstanceColorScheme scheme = activeState == ComponentState.ENABLED ? SubstanceColorSchemeUtilities.getColorScheme(tableHeader, activeState) : SubstanceColorSchemeUtilities.getColorScheme(tableHeader, ColorSchemeAssociationKind.HIGHLIGHT, activeState);
        return scheme;
    }

    @Override
    public final void paint(Graphics g2) {
        super.paint(g2);
    }

    @Override
    protected final void paintComponent(Graphics g2) {
        super.paintComponent(g2);
    }
}

