/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.ListUI;
import javax.swing.plaf.UIResource;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.ComponentStateFacet;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.renderers.SubstanceDefaultListCellRenderer;
import org.pushingpixels.substance.api.renderers.SubstanceRenderer;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.ui.SubstanceComboBoxUI;
import org.pushingpixels.substance.internal.ui.SubstanceListUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.SubstanceStripingUtils;
import org.pushingpixels.substance.internal.utils.SubstanceTextUtilities;
import org.pushingpixels.substance.internal.utils.UpdateOptimizationInfo;

@SubstanceRenderer
public class SubstanceDefaultComboBoxRenderer
extends SubstanceDefaultListCellRenderer {
    private JComboBox combo;

    public SubstanceDefaultComboBoxRenderer(JComboBox combo) {
        this.combo = combo;
        Insets ins = SubstanceSizeUtils.getListCellRendererInsets(SubstanceSizeUtils.getComponentFontSize(combo));
        this.setBorder(new EmptyBorder(ins.top, ins.left, ins.bottom, ins.right));
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JComponent result = (JComponent)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        ListUI baseListUI = list.getUI();
        ComboBoxUI baseComboUI = this.combo.getUI();
        if (baseListUI instanceof SubstanceListUI && baseComboUI instanceof SubstanceComboBoxUI) {
            SubstanceListUI listUI = (SubstanceListUI)baseListUI;
            SubstanceComboBoxUI comboUI = (SubstanceComboBoxUI)baseComboUI;
            if (index == -1) {
                StateTransitionTracker stateTransitionTracker = comboUI.getTransitionTracker();
                StateTransitionTracker.ModelStateInfo modelStateInfo = stateTransitionTracker.getModelStateInfo();
                ComponentState currState = modelStateInfo.getCurrModelState();
                float comboAlpha = SubstanceColorSchemeUtilities.getAlpha(this.combo, currState);
                Color fg = SubstanceTextUtilities.getForegroundColor((JComponent)this.combo, ((JLabel)result).getText(), modelStateInfo, comboAlpha);
                result.setForeground(fg);
            } else {
                StateTransitionTracker.ModelStateInfo modelStateInfo = listUI.getModelStateInfo(index, result);
                ComponentState currState = listUI.getCellState(index, result);
                if (modelStateInfo == null) {
                    SubstanceColorScheme scheme = this.getColorSchemeForState(list, index, listUI, currState);
                    result.setForeground(new ColorUIResource(scheme.getForegroundColor()));
                } else {
                    Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
                    SubstanceColorScheme colorScheme = this.getColorSchemeForState(list, index, listUI, currState);
                    if (currState.isDisabled() || activeStates == null || activeStates.size() == 1) {
                        super.setForeground(new ColorUIResource(colorScheme.getForegroundColor()));
                    } else {
                        float aggrRed = 0.0f;
                        float aggrGreen = 0.0f;
                        float aggrBlue = 0.0f;
                        for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : modelStateInfo.getStateContributionMap().entrySet()) {
                            ComponentState activeState = activeEntry.getKey();
                            float activeContribution = activeEntry.getValue().getContribution();
                            if (activeContribution == 0.0f) continue;
                            SubstanceColorScheme scheme = this.getColorSchemeForState(list, index, listUI, activeState);
                            Color schemeFg = scheme.getForegroundColor();
                            aggrRed += (float)schemeFg.getRed() * activeContribution;
                            aggrGreen += (float)schemeFg.getGreen() * activeContribution;
                            aggrBlue += (float)schemeFg.getBlue() * activeContribution;
                        }
                        result.setForeground(new ColorUIResource(new Color((int)aggrRed, (int)aggrGreen, (int)aggrBlue)));
                    }
                }
            }
            SubstanceStripingUtils.applyStripedBackground(list, index, this);
        }
        result.setEnabled(this.combo.isEnabled());
        return result;
    }

    private SubstanceColorScheme getColorSchemeForState(JList list, int index, SubstanceListUI listUI, ComponentState state) {
        boolean toUseHighlightKindForCurrState = index >= 0 && (state.isFacetActive(ComponentStateFacet.ROLLOVER) || state.isFacetActive(ComponentStateFacet.SELECTION));
        UpdateOptimizationInfo updateOptimizationInfo = listUI.getUpdateOptimizationInfo();
        if (toUseHighlightKindForCurrState) {
            if (updateOptimizationInfo == null) {
                return SubstanceColorSchemeUtilities.getColorScheme(list, ColorSchemeAssociationKind.HIGHLIGHT, state);
            }
            return updateOptimizationInfo.getHighlightColorScheme(state);
        }
        if (updateOptimizationInfo == null) {
            return SubstanceColorSchemeUtilities.getColorScheme(list, state);
        }
        return updateOptimizationInfo.getDefaultScheme();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size;
        if (this.getText() == null || this.getText().equals("")) {
            this.setText(" ");
            size = super.getPreferredSize();
            this.setText("");
        } else {
            size = super.getPreferredSize();
        }
        return size;
    }

    public static class SubstanceUIResource
    extends SubstanceDefaultComboBoxRenderer
    implements UIResource {
        public SubstanceUIResource(JComboBox combo) {
            super(combo);
        }
    }
}

