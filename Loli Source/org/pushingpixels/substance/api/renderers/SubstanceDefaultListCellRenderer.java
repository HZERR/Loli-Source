/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ListUI;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.renderers.SubstanceRenderer;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.ui.SubstanceListUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceStripingUtils;
import org.pushingpixels.substance.internal.utils.UpdateOptimizationInfo;

@SubstanceRenderer
public class SubstanceDefaultListCellRenderer
extends DefaultListCellRenderer {
    public SubstanceDefaultListCellRenderer() {
        this.putClientProperty("substancelaf.colorizationFactor", 1.0);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        this.setComponentOrientation(list.getComponentOrientation());
        ListUI listUI = list.getUI();
        if (listUI instanceof SubstanceListUI) {
            boolean isDropLocation;
            SubstanceListUI ui = (SubstanceListUI)listUI;
            StateTransitionTracker.ModelStateInfo modelStateInfo = ui.getModelStateInfo(index, this);
            ComponentState currState = ui.getCellState(index, this);
            JList.DropLocation dropLocation = list.getDropLocation();
            boolean bl = isDropLocation = dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == index;
            if (!isDropLocation && modelStateInfo != null) {
                Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
                SubstanceColorScheme colorScheme = this.getColorSchemeForState(list, ui, currState);
                if (currState.isDisabled() || activeStates == null || activeStates.size() == 1) {
                    super.setForeground(new ColorUIResource(colorScheme.getForegroundColor()));
                } else {
                    float aggrRed = 0.0f;
                    float aggrGreen = 0.0f;
                    float aggrBlue = 0.0f;
                    for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : modelStateInfo.getStateContributionMap().entrySet()) {
                        ComponentState activeState = activeEntry.getKey();
                        SubstanceColorScheme scheme = this.getColorSchemeForState(list, ui, activeState);
                        Color schemeFg = scheme.getForegroundColor();
                        float contribution = activeEntry.getValue().getContribution();
                        aggrRed += (float)schemeFg.getRed() * contribution;
                        aggrGreen += (float)schemeFg.getGreen() * contribution;
                        aggrBlue += (float)schemeFg.getBlue() * contribution;
                    }
                    super.setForeground(new ColorUIResource(new Color((int)aggrRed, (int)aggrGreen, (int)aggrBlue)));
                }
            } else {
                SubstanceColorScheme scheme = this.getColorSchemeForState(list, ui, currState);
                if (isDropLocation) {
                    scheme = SubstanceColorSchemeUtilities.getColorScheme(list, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, currState);
                }
                super.setForeground(new ColorUIResource(scheme.getForegroundColor()));
            }
        } else if (isSelected) {
            this.setForeground(list.getSelectionForeground());
        } else {
            this.setForeground(list.getForeground());
        }
        if (SubstanceLookAndFeel.isCurrentLookAndFeel() && list.getLayoutOrientation() == 0) {
            SubstanceStripingUtils.applyStripedBackground(list, index, this);
        }
        if (value instanceof Icon) {
            this.setIcon((Icon)value);
            this.setText("");
        } else {
            this.setIcon(null);
            this.setText(value == null ? "" : value.toString());
        }
        this.setEnabled(list.isEnabled());
        this.setFont(list.getFont());
        Insets ins = SubstanceSizeUtils.getListCellRendererInsets(SubstanceSizeUtils.getComponentFontSize(list));
        this.setBorder(new EmptyBorder(ins.top, ins.left, ins.bottom, ins.right));
        this.setOpaque(false);
        return this;
    }

    private SubstanceColorScheme getColorSchemeForState(JList list, SubstanceListUI ui, ComponentState state) {
        UpdateOptimizationInfo updateOptimizationInfo = ui.getUpdateOptimizationInfo();
        if (state == ComponentState.ENABLED) {
            if (updateOptimizationInfo == null) {
                return SubstanceColorSchemeUtilities.getColorScheme(list, state);
            }
            return updateOptimizationInfo.getDefaultScheme();
        }
        if (updateOptimizationInfo == null) {
            return SubstanceColorSchemeUtilities.getColorScheme(list, ColorSchemeAssociationKind.HIGHLIGHT, state);
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
    public final void paintComponents(Graphics g2) {
        super.paintComponents(g2);
    }

    @Override
    protected final void paintBorder(Graphics g2) {
        super.paintBorder(g2);
    }

    public static class SubstanceUIResource
    extends SubstanceDefaultListCellRenderer
    implements UIResource {
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }
}

