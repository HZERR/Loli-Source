/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import org.pushingpixels.lafwidget.LafWidgetUtilities;
import org.pushingpixels.substance.api.ColorSchemeAssociationKind;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.internal.animation.StateTransitionTracker;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.painter.BackgroundPaintingUtils;
import org.pushingpixels.substance.internal.ui.SubstanceSplitPaneUI;
import org.pushingpixels.substance.internal.utils.RolloverControlListener;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.icon.TransitionAwareIcon;

public class SubstanceSplitPaneDivider
extends BasicSplitPaneDivider
implements TransitionAwareUI {
    private RolloverControlListener substanceRolloverListener;
    protected StateTransitionTracker stateTransitionTracker;
    private PropertyChangeListener substancePropertyChangeListener;
    private ButtonModel gripModel;

    public SubstanceSplitPaneDivider(SubstanceSplitPaneUI ui) {
        super(ui);
        this.setLayout(new SubstanceDividerLayout());
    }

    @Override
    public void setBasicSplitPaneUI(BasicSplitPaneUI newUI) {
        if (this.splitPane != null) {
            this.uninstall();
        }
        if (newUI != null) {
            this.splitPane = newUI.getSplitPane();
            this.gripModel = new DefaultButtonModel();
            this.gripModel.setArmed(false);
            this.gripModel.setSelected(false);
            this.gripModel.setPressed(false);
            this.gripModel.setRollover(false);
            this.gripModel.setEnabled(this.splitPane.isEnabled());
            this.stateTransitionTracker = new StateTransitionTracker(this.splitPane, this.gripModel);
            this.substanceRolloverListener = new RolloverControlListener(this, this.gripModel);
            this.addMouseListener(this.substanceRolloverListener);
            this.addMouseMotionListener(this.substanceRolloverListener);
            this.substancePropertyChangeListener = new PropertyChangeListener(){

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("enabled".equals(evt.getPropertyName())) {
                        boolean isEnabled = SubstanceSplitPaneDivider.this.splitPane.isEnabled();
                        SubstanceSplitPaneDivider.this.gripModel.setEnabled(isEnabled);
                        if (SubstanceSplitPaneDivider.this.leftButton != null) {
                            SubstanceSplitPaneDivider.this.leftButton.setEnabled(isEnabled);
                        }
                        if (SubstanceSplitPaneDivider.this.rightButton != null) {
                            SubstanceSplitPaneDivider.this.rightButton.setEnabled(isEnabled);
                        }
                        SubstanceSplitPaneDivider.this.setEnabled(isEnabled);
                    }
                }
            };
            this.splitPane.addPropertyChangeListener(this.substancePropertyChangeListener);
            this.stateTransitionTracker.registerModelListeners();
        } else {
            this.uninstall();
        }
        super.setBasicSplitPaneUI(newUI);
    }

    private void uninstall() {
        this.removeMouseListener(this.substanceRolloverListener);
        this.removeMouseMotionListener(this.substanceRolloverListener);
        this.substanceRolloverListener = null;
        if (this.substancePropertyChangeListener != null) {
            this.splitPane.removePropertyChangeListener(this.substancePropertyChangeListener);
            this.substancePropertyChangeListener = null;
        }
        this.stateTransitionTracker.unregisterModelListeners();
    }

    @Override
    public void paint(Graphics g2) {
        if (SubstanceCoreUtilities.hasFlatAppearance(this.splitPane, true)) {
            BackgroundPaintingUtils.updateIfOpaque(g2, this.splitPane);
        }
        Graphics2D graphics = (Graphics2D)g2.create();
        StateTransitionTracker.ModelStateInfo modelStateInfo = this.stateTransitionTracker.getModelStateInfo();
        ComponentState currState = modelStateInfo.getCurrModelState();
        Map<ComponentState, StateTransitionTracker.StateContributionInfo> activeStates = modelStateInfo.getStateContributionMap();
        float alpha = SubstanceColorSchemeUtilities.getAlpha(this.splitPane, currState);
        int minSizeForGripPresence = SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils.getComponentFontSize(this), 30, 1, 2, false);
        int maxGripSize = SubstanceSizeUtils.getAdjustedSize(SubstanceSizeUtils.getComponentFontSize(this), 40, 1, 3, false);
        if (this.splitPane.getOrientation() == 1) {
            int thumbHeight = this.getHeight();
            if (thumbHeight >= minSizeForGripPresence) {
                int gripHeight = thumbHeight / 4;
                if (gripHeight > maxGripSize) {
                    gripHeight = maxGripSize;
                }
                int thumbWidth = this.getWidth();
                int gripX = 0;
                int gripY = (thumbHeight - gripHeight) / 2;
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                    float contribution = activeEntry.getValue().getContribution();
                    if (contribution == 0.0f) continue;
                    ComponentState activeState = activeEntry.getKey();
                    graphics.setComposite(LafWidgetUtilities.getAlphaComposite(this.splitPane, alpha * contribution, g2));
                    SubstanceImageCreator.paintSplitDividerBumpImage(graphics, this, gripX, gripY, thumbWidth, gripHeight, false, SubstanceColorSchemeUtilities.getColorScheme(this, ColorSchemeAssociationKind.MARK, activeState));
                }
            }
        } else {
            int thumbWidth = this.getWidth();
            if (thumbWidth >= minSizeForGripPresence) {
                int gripWidth = thumbWidth / 4;
                if (gripWidth > maxGripSize) {
                    gripWidth = maxGripSize;
                }
                int thumbHeight = this.getHeight();
                int gripX = (thumbWidth - gripWidth) / 2;
                int gripY = 1;
                for (Map.Entry<ComponentState, StateTransitionTracker.StateContributionInfo> activeEntry : activeStates.entrySet()) {
                    float contribution = activeEntry.getValue().getContribution();
                    if (contribution == 0.0f) continue;
                    ComponentState activeState = activeEntry.getKey();
                    graphics.setComposite(LafWidgetUtilities.getAlphaComposite(this.splitPane, alpha * contribution, g2));
                    SubstanceImageCreator.paintSplitDividerBumpImage(graphics, this, gripX, gripY, gripWidth, thumbHeight, true, SubstanceColorSchemeUtilities.getColorScheme(this, ColorSchemeAssociationKind.MARK, activeState));
                }
            }
        }
        graphics.dispose();
        super.paint(g2);
    }

    @Override
    protected JButton createLeftOneTouchButton() {
        JButton oneTouchButton = new JButton(){

            @Override
            public boolean isFocusable() {
                return false;
            }
        };
        TransitionAwareIcon verticalSplit = new TransitionAwareIcon(oneTouchButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane);
                return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSplitPaneArrowIconWidth(fontSize), SubstanceSizeUtils.getSplitPaneArrowIconHeight(fontSize), SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 1, scheme);
            }
        }, "substance.splitPane.left.vertical");
        TransitionAwareIcon horizontalSplit = new TransitionAwareIcon(oneTouchButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane);
                return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSplitPaneArrowIconWidth(fontSize), SubstanceSizeUtils.getSplitPaneArrowIconHeight(fontSize), SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 7, scheme);
            }
        }, "substance.splitPane.left.horizontal");
        oneTouchButton.setIcon(this.splitPane.getOrientation() == 0 ? verticalSplit : horizontalSplit);
        oneTouchButton.putClientProperty("substancelaf.buttonpaintnever", Boolean.TRUE);
        oneTouchButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        oneTouchButton.setRequestFocusEnabled(false);
        oneTouchButton.setCursor(Cursor.getPredefinedCursor(12));
        oneTouchButton.setFocusPainted(false);
        oneTouchButton.setBorderPainted(false);
        return oneTouchButton;
    }

    @Override
    protected JButton createRightOneTouchButton() {
        JButton oneTouchButton = new JButton(){

            @Override
            public boolean isFocusable() {
                return false;
            }
        };
        TransitionAwareIcon verticalSplit = new TransitionAwareIcon(oneTouchButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane);
                return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSplitPaneArrowIconWidth(fontSize), SubstanceSizeUtils.getSplitPaneArrowIconHeight(fontSize), SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 5, scheme);
            }
        }, "substance.splitPane.right.vertical");
        TransitionAwareIcon horizontalSplit = new TransitionAwareIcon(oneTouchButton, new TransitionAwareIcon.Delegate(){

            @Override
            public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane);
                return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSplitPaneArrowIconWidth(fontSize), SubstanceSizeUtils.getSplitPaneArrowIconHeight(fontSize), SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 3, scheme);
            }
        }, "substance.splitPane.right.horizontal");
        oneTouchButton.setIcon(this.splitPane.getOrientation() == 0 ? verticalSplit : horizontalSplit);
        oneTouchButton.putClientProperty("substancelaf.buttonpaintnever", Boolean.TRUE);
        oneTouchButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        oneTouchButton.setCursor(Cursor.getPredefinedCursor(12));
        oneTouchButton.setFocusPainted(false);
        oneTouchButton.setBorderPainted(false);
        oneTouchButton.setRequestFocusEnabled(false);
        return oneTouchButton;
    }

    public void updateOneTouchButtons(int orientation) {
        if (orientation == 0) {
            if (this.leftButton != null) {
                this.leftButton.setIcon(new TransitionAwareIcon(this.leftButton, new TransitionAwareIcon.Delegate(){

                    @Override
                    public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                        int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane);
                        return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSplitPaneArrowIconWidth(fontSize), SubstanceSizeUtils.getSplitPaneArrowIconHeight(fontSize), SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 1, scheme);
                    }
                }, "substance.splitPane.left.vertical"));
            }
            if (this.rightButton != null) {
                this.rightButton.setIcon(new TransitionAwareIcon(this.rightButton, new TransitionAwareIcon.Delegate(){

                    @Override
                    public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                        int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane);
                        return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSplitPaneArrowIconWidth(fontSize), SubstanceSizeUtils.getSplitPaneArrowIconHeight(fontSize), SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 5, scheme);
                    }
                }, "substance.splitPane.right.vertical"));
            }
        } else {
            if (this.leftButton != null) {
                this.leftButton.setIcon(new TransitionAwareIcon(this.leftButton, new TransitionAwareIcon.Delegate(){

                    @Override
                    public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                        int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane);
                        return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSplitPaneArrowIconWidth(fontSize), SubstanceSizeUtils.getSplitPaneArrowIconHeight(fontSize), SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 7, scheme);
                    }
                }, "substance.splitPane.left.horizontal"));
            }
            if (this.rightButton != null) {
                this.rightButton.setIcon(new TransitionAwareIcon(this.rightButton, new TransitionAwareIcon.Delegate(){

                    @Override
                    public Icon getColorSchemeIcon(SubstanceColorScheme scheme) {
                        int fontSize = SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane);
                        return SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getSplitPaneArrowIconWidth(fontSize), SubstanceSizeUtils.getSplitPaneArrowIconHeight(fontSize), SubstanceSizeUtils.getArrowStrokeWidth(fontSize), 3, scheme);
                    }
                }, "substance.splitPane.right.horizontal"));
            }
        }
    }

    @Override
    public boolean isInside(MouseEvent me) {
        return true;
    }

    @Override
    public StateTransitionTracker getTransitionTracker() {
        return this.stateTransitionTracker;
    }

    protected class SubstanceDividerLayout
    extends BasicSplitPaneDivider.DividerLayout {
        protected SubstanceDividerLayout() {
            super(SubstanceSplitPaneDivider.this);
        }

        @Override
        public void layoutContainer(Container c2) {
            if (SubstanceSplitPaneDivider.this.leftButton != null && SubstanceSplitPaneDivider.this.rightButton != null && c2 == SubstanceSplitPaneDivider.this) {
                if (SubstanceSplitPaneDivider.this.splitPane.isOneTouchExpandable()) {
                    Insets insets = SubstanceSplitPaneDivider.this.getInsets();
                    if (SubstanceSplitPaneDivider.this.orientation == 0) {
                        int extraX = insets != null ? insets.left : 0;
                        int blockSize = SubstanceSplitPaneDivider.this.getHeight();
                        if (insets != null) {
                            blockSize -= insets.top + insets.bottom;
                            blockSize = Math.max(blockSize, 0);
                        }
                        int y2 = (c2.getSize().height - blockSize) / 2;
                        int offset = SubstanceSizeUtils.getSplitPaneButtonOffset(SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane));
                        SubstanceSplitPaneDivider.this.leftButton.setBounds(extraX + offset, y2, ((SubstanceSplitPaneDivider)SubstanceSplitPaneDivider.this).leftButton.getPreferredSize().width * 2 / 3, blockSize);
                        SubstanceSplitPaneDivider.this.rightButton.setBounds(SubstanceSplitPaneDivider.this.leftButton.getX() + SubstanceSplitPaneDivider.this.leftButton.getWidth(), y2, ((SubstanceSplitPaneDivider)SubstanceSplitPaneDivider.this).rightButton.getPreferredSize().width * 2 / 3, blockSize);
                    } else {
                        int extraY = insets != null ? insets.top : 0;
                        int blockSize = SubstanceSplitPaneDivider.this.getWidth();
                        if (insets != null) {
                            blockSize -= insets.left + insets.right;
                            blockSize = Math.max(blockSize, 0);
                        }
                        int x2 = (c2.getSize().width - blockSize) / 2;
                        int offset = SubstanceSizeUtils.getSplitPaneButtonOffset(SubstanceSizeUtils.getComponentFontSize(SubstanceSplitPaneDivider.this.splitPane));
                        SubstanceSplitPaneDivider.this.leftButton.setBounds(x2, extraY + offset, blockSize, ((SubstanceSplitPaneDivider)SubstanceSplitPaneDivider.this).leftButton.getPreferredSize().height * 2 / 3);
                        SubstanceSplitPaneDivider.this.rightButton.setBounds(x2, SubstanceSplitPaneDivider.this.leftButton.getY() + SubstanceSplitPaneDivider.this.leftButton.getHeight(), blockSize, ((SubstanceSplitPaneDivider)SubstanceSplitPaneDivider.this).leftButton.getPreferredSize().height * 2 / 3);
                    }
                } else {
                    SubstanceSplitPaneDivider.this.leftButton.setBounds(-5, -5, 1, 1);
                    SubstanceSplitPaneDivider.this.rightButton.setBounds(-5, -5, 1, 1);
                }
            }
        }
    }
}

