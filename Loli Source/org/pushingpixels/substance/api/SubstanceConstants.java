/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.internal.animation.TransitionAwareUI;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

public class SubstanceConstants {

    public static enum SubstanceWidgetType {
        MENU_SEARCH,
        TITLE_PANE_HEAP_STATUS;

    }

    public static enum TabContentPaneBorderKind {
        DOUBLE_FULL,
        SINGLE_FULL,
        DOUBLE_PLACEMENT,
        SINGLE_PLACEMENT;

    }

    public static enum MenuGutterFillKind {
        NONE,
        SOFT_FILL,
        HARD_FILL,
        SOFT,
        HARD;

    }

    public static enum ScrollPaneButtonPolicyKind {
        NONE,
        OPPOSITE,
        ADJACENT,
        MULTIPLE,
        MULTIPLE_BOTH;

    }

    public static enum TabCloseKind {
        NONE,
        THIS,
        ALL,
        ALL_BUT_THIS;

    }

    public static enum ImageWatermarkKind {
        SCREEN_CENTER_SCALE,
        SCREEN_TILE,
        APP_ANCHOR,
        APP_CENTER,
        APP_TILE;

    }

    public static enum FocusKind {
        NONE{

            @Override
            public void paintFocus(Component mainComp, Component focusedComp, TransitionAwareUI transitionAwareUI, Graphics2D graphics, Shape focusShape, Rectangle textRect, int extraPadding) {
            }
        }
        ,
        TEXT{

            @Override
            public void paintFocus(Component mainComp, Component focusedComp, TransitionAwareUI transitionAwareUI, Graphics2D graphics, Shape focusShape, Rectangle textRect, int extraPadding) {
                if (textRect == null) {
                    return;
                }
                if (textRect.width == 0 || textRect.height == 0) {
                    return;
                }
                int fontSize = SubstanceSizeUtils.getComponentFontSize(mainComp);
                float dashLength = 2.getDashLength(fontSize);
                float dashGap = 2.getDashGap(fontSize);
                float dashPhase = (dashLength + dashGap) * (1.0f - transitionAwareUI.getTransitionTracker().getFocusLoopPosition());
                graphics.setStroke(new BasicStroke(SubstanceSizeUtils.getFocusStrokeWidth(fontSize), 0, 1, 0.0f, new float[]{dashLength, dashGap}, dashPhase));
                int delta = mainComp instanceof JComboBox || mainComp instanceof JSpinner ? 0 : 1;
                GeneralPath contour = SubstanceOutlineUtilities.getBaseOutline(textRect.width + 2 * delta, textRect.height, SubstanceSizeUtils.getClassicButtonCornerRadius(fontSize), null);
                graphics.translate(textRect.x - delta, textRect.y);
                graphics.draw(contour);
            }

            @Override
            public boolean isAnimated() {
                return true;
            }
        }
        ,
        ALL{

            @Override
            public void paintFocus(Component mainComp, Component focusedComp, TransitionAwareUI transitionAwareUI, Graphics2D graphics, Shape focusShape, Rectangle textRect, int extraPadding) {
                if (focusShape == null && mainComp instanceof AbstractButton && !(mainComp instanceof JCheckBox) && !(mainComp instanceof JRadioButton)) {
                    SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(mainComp);
                    if (shaper == null) {
                        return;
                    }
                    int fontSize = SubstanceSizeUtils.getComponentFontSize(mainComp);
                    float dashLength = 3.getDashLength(fontSize);
                    float dashGap = 3.getDashGap(fontSize);
                    float dashPhase = (dashLength + dashGap) * (1.0f - transitionAwareUI.getTransitionTracker().getFocusLoopPosition());
                    graphics.setStroke(new BasicStroke(SubstanceSizeUtils.getFocusStrokeWidth(fontSize), 0, 1, 0.0f, new float[]{dashLength, dashGap}, dashPhase));
                    Shape contour = shaper.getButtonOutline((AbstractButton)mainComp, null, mainComp.getWidth(), mainComp.getHeight(), false);
                    graphics.draw(contour);
                } else {
                    graphics.translate(1, 1);
                    Shape contour = focusShape != null ? focusShape : SubstanceOutlineUtilities.getBaseOutline(mainComp.getWidth() - 2, mainComp.getHeight() - 2, SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(mainComp)), null);
                    int fontSize = SubstanceSizeUtils.getComponentFontSize(mainComp);
                    float dashLength = 3.getDashLength(fontSize);
                    float dashGap = 3.getDashGap(fontSize);
                    float dashPhase = (dashLength + dashGap) * (1.0f - transitionAwareUI.getTransitionTracker().getFocusLoopPosition());
                    graphics.setStroke(new BasicStroke(SubstanceSizeUtils.getFocusStrokeWidth(fontSize), 0, 1, 0.0f, new float[]{dashLength, dashGap}, dashPhase));
                    graphics.draw(contour);
                }
            }

            @Override
            public boolean isAnimated() {
                return true;
            }
        }
        ,
        ALL_INNER{

            @Override
            public void paintFocus(Component mainComp, Component focusedComp, TransitionAwareUI transitionAwareUI, Graphics2D graphics, Shape focusShape, Rectangle textRect, int extraPadding) {
                if (focusShape == null && mainComp instanceof AbstractButton && !(mainComp instanceof JCheckBox) && !(mainComp instanceof JRadioButton)) {
                    SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(mainComp);
                    if (shaper == null) {
                        return;
                    }
                    if (shaper.isProportionate()) {
                        int fontSize = SubstanceSizeUtils.getComponentFontSize(mainComp);
                        float dashLength = 4.getDashLength(fontSize);
                        float dashGap = 4.getDashGap(fontSize);
                        float dashPhase = (dashLength + dashGap) * (1.0f - transitionAwareUI.getTransitionTracker().getFocusLoopPosition());
                        float focusStrokeWidth = SubstanceSizeUtils.getFocusStrokeWidth(fontSize);
                        graphics.setStroke(new BasicStroke(focusStrokeWidth, 0, 1, 0.0f, new float[]{dashLength, dashGap}, dashPhase));
                        int insetsPix = extraPadding;
                        Insets insets = new Insets(insetsPix, insetsPix, insetsPix, insetsPix);
                        Shape contour = shaper.getButtonOutline((AbstractButton)mainComp, insets, mainComp.getWidth(), mainComp.getHeight(), false);
                        graphics.draw(contour);
                    }
                } else {
                    graphics.translate(extraPadding / 2, extraPadding / 2);
                    int fontSize = SubstanceSizeUtils.getComponentFontSize(mainComp);
                    Shape contour = focusShape != null ? focusShape : SubstanceOutlineUtilities.getBaseOutline(mainComp.getWidth() - extraPadding, mainComp.getHeight() - extraPadding, SubstanceSizeUtils.getClassicButtonCornerRadius(fontSize), null);
                    float dashLength = 4.getDashLength(fontSize);
                    float dashGap = 4.getDashGap(fontSize);
                    float dashPhase = (dashLength + dashGap) * (1.0f - transitionAwareUI.getTransitionTracker().getFocusLoopPosition());
                    graphics.setStroke(new BasicStroke(SubstanceSizeUtils.getFocusStrokeWidth(fontSize), 0, 1, 0.0f, new float[]{dashLength, dashGap}, dashPhase));
                    graphics.draw(contour);
                }
            }

            @Override
            public boolean isAnimated() {
                return true;
            }
        }
        ,
        ALL_STRONG_INNER{

            @Override
            public void paintFocus(Component mainComp, Component focusedComp, TransitionAwareUI transitionAwareUI, Graphics2D graphics, Shape focusShape, Rectangle textRect, int extraPadding) {
                if (focusShape == null && mainComp instanceof AbstractButton && !(mainComp instanceof JCheckBox) && !(mainComp instanceof JRadioButton)) {
                    SubstanceButtonShaper shaper = SubstanceCoreUtilities.getButtonShaper(mainComp);
                    if (shaper == null) {
                        return;
                    }
                    if (shaper.isProportionate()) {
                        Insets insets = new Insets(extraPadding, extraPadding, extraPadding, extraPadding);
                        Shape contour = shaper.getButtonOutline((AbstractButton)mainComp, insets, mainComp.getWidth(), mainComp.getHeight(), false);
                        graphics.draw(contour);
                    }
                } else {
                    graphics.translate(extraPadding / 2, extraPadding / 2);
                    Shape contour = focusShape != null ? focusShape : SubstanceOutlineUtilities.getBaseOutline(mainComp.getWidth() - extraPadding, mainComp.getHeight() - extraPadding, SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(mainComp)), null);
                    graphics.draw(contour);
                }
            }
        }
        ,
        UNDERLINE{

            @Override
            public void paintFocus(Component mainComp, Component focusedComp, TransitionAwareUI transitionAwareUI, Graphics2D graphics, Shape focusShape, Rectangle textRect, int extraPadding) {
                if (textRect == null) {
                    return;
                }
                int fontSize = SubstanceSizeUtils.getComponentFontSize(mainComp);
                float dashLength = 6.getDashLength(fontSize);
                float dashGap = 6.getDashGap(fontSize);
                float dashPhase = (dashLength + dashGap) * (1.0f - transitionAwareUI.getTransitionTracker().getFocusLoopPosition());
                graphics.setStroke(new BasicStroke(SubstanceSizeUtils.getFocusStrokeWidth(fontSize), 0, 1, 0.0f, new float[]{dashLength, dashGap}, dashPhase));
                graphics.translate(textRect.x - 1, textRect.y);
                graphics.drawLine(0, textRect.height - 1, textRect.width, textRect.height - 1);
                graphics.dispose();
            }

            @Override
            public boolean isAnimated() {
                return true;
            }
        }
        ,
        STRONG_UNDERLINE{

            @Override
            public void paintFocus(Component mainComp, Component focusedComp, TransitionAwareUI transitionAwareUI, Graphics2D graphics, Shape focusShape, Rectangle textRect, int extraPadding) {
                if (textRect == null) {
                    return;
                }
                graphics.translate(textRect.x - 1, textRect.y);
                graphics.drawLine(0, textRect.height - 1, textRect.width, textRect.height - 1);
            }
        };


        public abstract void paintFocus(Component var1, Component var2, TransitionAwareUI var3, Graphics2D var4, Shape var5, Rectangle var6, int var7);

        protected static float getDashLength(int fontSize) {
            return 2.0f + (float)SubstanceSizeUtils.getExtraPadding(fontSize);
        }

        protected static float getDashGap(int fontSize) {
            return FocusKind.getDashLength(fontSize) / 2.0f;
        }

        public boolean isAnimated() {
            return false;
        }
    }

    public static enum Side {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM;

    }
}

