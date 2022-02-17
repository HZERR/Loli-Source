/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.TreeCellRenderer;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.renderers.SubstanceRenderer;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.ui.SubstanceTreeUI;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceStripingUtils;

@SubstanceRenderer
public class SubstanceDefaultTreeCellRenderer
extends JLabel
implements TreeCellRenderer {
    private JTree tree;
    protected boolean selected;
    protected boolean hasFocus;

    public SubstanceDefaultTreeCellRenderer() {
        this.setHorizontalAlignment(2);
        this.putClientProperty("substancelaf.colorizationFactor", 1.0);
    }

    public Icon getDefaultOpenIcon() {
        return UIManager.getIcon("Tree.openIcon");
    }

    public Icon getDefaultClosedIcon() {
        return UIManager.getIcon("Tree.closedIcon");
    }

    public Icon getDefaultLeafIcon() {
        return UIManager.getIcon("Tree.leafIcon");
    }

    @Override
    public void setFont(Font font) {
        if (font instanceof FontUIResource) {
            font = null;
        }
        super.setFont(font);
    }

    @Override
    public Font getFont() {
        Font font = super.getFont();
        if (font == null && this.tree != null) {
            font = this.tree.getFont();
        }
        return font;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        SubstanceTreeUI ui;
        String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);
        this.tree = tree;
        this.hasFocus = hasFocus;
        this.setText(stringValue);
        TreeUI treeUI = tree.getUI();
        if (treeUI instanceof SubstanceTreeUI) {
            boolean isDropLocation;
            ui = (SubstanceTreeUI)treeUI;
            SubstanceTreeUI.TreePathId pathId = new SubstanceTreeUI.TreePathId(tree.getPathForRow(row));
            StateTransitionTracker.ModelStateInfo modelStateInfo = ui.getModelStateInfo(pathId);
            ComponentState currState = ui.getPathState(pathId);
            JTree.DropLocation dropLocation = tree.getDropLocation();
            boolean bl = isDropLocation = dropLocation != null && dropLocation.getChildIndex() == -1 && tree.getRowForPath(dropLocation.getPath()) == row;
            if (!isDropLocation && modelStateInfo != null) {
                Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
                SubstanceColorScheme colorScheme = this.getColorSchemeForState(tree, ui, currState);
                if (currState.isDisabled() || activeStates == null || activeStates.size() == 1) {
                    super.setForeground(new ColorUIResource(colorScheme.getForegroundColor()));
                } else {
                    float aggrRed = 0.0f;
                    float aggrGreen = 0.0f;
                    float aggrBlue = 0.0f;
                    for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : modelStateInfo.getStateContributionMap().entrySet()) {
                        ComponentState activeState = activeEntry.getKey();
                        SubstanceColorScheme scheme = this.getColorSchemeForState(tree, ui, activeState);
                        Color schemeFg = scheme.getForegroundColor();
                        float contribution = activeEntry.getValue().getContribution();
                        aggrRed += (float)schemeFg.getRed() * contribution;
                        aggrGreen += (float)schemeFg.getGreen() * contribution;
                        aggrBlue += (float)schemeFg.getBlue() * contribution;
                    }
                    super.setForeground(new ColorUIResource(new Color((int)aggrRed, (int)aggrGreen, (int)aggrBlue)));
                }
            } else {
                SubstanceColorScheme scheme = this.getColorSchemeForState(tree, ui, currState);
                if (isDropLocation) {
                    scheme = SubstanceColorSchemeUtilities.getColorScheme(tree, ColorSchemeAssociationKind.TEXT_HIGHLIGHT, currState);
                }
                super.setForeground(new ColorUIResource(scheme.getForegroundColor()));
            }
        } else if (sel) {
            this.setForeground(UIManager.getColor("Tree.selectionForeground"));
        } else {
            this.setForeground(UIManager.getColor("Tree.textForeground"));
        }
        if (SubstanceLookAndFeel.isCurrentLookAndFeel()) {
            SubstanceStripingUtils.applyStripedBackground(tree, row, this);
        }
        if (!tree.isEnabled()) {
            this.setEnabled(false);
            if (leaf) {
                this.setDisabledIcon(SubstanceImageCreator.toGreyscale(SubstanceImageCreator.makeTransparent(tree, this.getDefaultLeafIcon(), 0.5)));
            } else if (expanded) {
                this.setDisabledIcon(SubstanceImageCreator.toGreyscale(SubstanceImageCreator.makeTransparent(tree, this.getDefaultOpenIcon(), 0.5)));
            } else {
                this.setDisabledIcon(SubstanceImageCreator.toGreyscale(SubstanceImageCreator.makeTransparent(tree, this.getDefaultClosedIcon(), 0.5)));
            }
        } else {
            this.setEnabled(true);
            if (leaf) {
                this.setIcon(this.getDefaultLeafIcon());
            } else if (expanded) {
                this.setIcon(this.getDefaultOpenIcon());
            } else {
                this.setIcon(this.getDefaultClosedIcon());
            }
        }
        this.setComponentOrientation(tree.getComponentOrientation());
        this.setOpaque(false);
        this.selected = sel;
        if (treeUI instanceof SubstanceTreeUI) {
            ui = (SubstanceTreeUI)treeUI;
            Insets regInsets = ui.getCellRendererInsets();
            this.setBorder(new BorderUIResource.EmptyBorderUIResource(regInsets));
        }
        return this;
    }

    private SubstanceColorScheme getColorSchemeForState(JTree tree, SubstanceTreeUI ui, ComponentState activeState) {
        SubstanceColorScheme scheme;
        SubstanceColorScheme substanceColorScheme = scheme = activeState == ComponentState.ENABLED ? ui.getDefaultColorScheme() : SubstanceColorSchemeUtilities.getColorScheme(tree, ColorSchemeAssociationKind.HIGHLIGHT, activeState);
        if (scheme == null) {
            scheme = SubstanceColorSchemeUtilities.getColorScheme(tree, ColorSchemeAssociationKind.HIGHLIGHT, activeState);
        }
        return scheme;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension retDimension = super.getPreferredSize();
        if (retDimension != null) {
            retDimension = new Dimension(retDimension.width + 3, retDimension.height);
        }
        return retDimension;
    }

    @Override
    public void validate() {
    }

    @Override
    public void invalidate() {
    }

    @Override
    public void revalidate() {
    }

    @Override
    public void repaint(long tm, int x2, int y2, int width, int height) {
    }

    @Override
    public void repaint(Rectangle r2) {
    }

    @Override
    public void repaint() {
    }

    @Override
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if ("text".equals(propertyName)) {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    @Override
    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    }

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
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

