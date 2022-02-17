/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.api.shaper;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.geom.GeneralPath;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.border.Border;
import org.pushingpixels.substance.api.SubstanceConstants;
import org.pushingpixels.substance.api.shaper.RectangularButtonShaper;
import org.pushingpixels.substance.api.shaper.SubstanceButtonShaper;
import org.pushingpixels.substance.internal.utils.HashMapKey;
import org.pushingpixels.substance.internal.utils.LazyResettableHashMap;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceInternalArrowButton;
import org.pushingpixels.substance.internal.utils.SubstanceOutlineUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;
import org.pushingpixels.substance.internal.utils.border.SubstanceBorder;
import org.pushingpixels.substance.internal.utils.border.SubstanceButtonBorder;

public class ClassicButtonShaper
implements SubstanceButtonShaper,
RectangularButtonShaper {
    private static final LazyResettableHashMap<GeneralPath> contours = new LazyResettableHashMap("ClassicButtonShaper");
    public static final ClassicButtonShaper INSTANCE = new ClassicButtonShaper();

    @Override
    public String getDisplayName() {
        return "Classic";
    }

    @Override
    public GeneralPath getButtonOutline(AbstractButton button, Insets insets, int width, int height, boolean isInner) {
        HashMapKey key;
        GeneralPath result;
        Set<SubstanceConstants.Side> straightSides = SubstanceCoreUtilities.getSides(button, "substancelaf.buttonside");
        float radius = this.getCornerRadius(button, insets);
        if (isInner && (radius -= (float)((int)SubstanceSizeUtils.getBorderStrokeWidth(SubstanceSizeUtils.getComponentFontSize(button)))) < 0.0f) {
            radius = 0.0f;
        }
        if ((result = contours.get(key = SubstanceCoreUtilities.getHashKey(width, height, straightSides, Float.valueOf(radius), insets))) != null) {
            return result;
        }
        result = SubstanceOutlineUtilities.getBaseOutline(width, height, radius, straightSides, insets);
        contours.put(key, result);
        return result;
    }

    @Override
    public Border getButtonBorder(final AbstractButton button) {
        return new SubstanceButtonBorder(ClassicButtonShaper.class){

            @Override
            public Insets getBorderInsets(Component c2) {
                int fontSize = SubstanceSizeUtils.getComponentFontSize(button);
                Insets buttonInsets = SubstanceSizeUtils.getButtonInsets(fontSize);
                int focusPadding = SubstanceSizeUtils.getFocusRingPadding(fontSize);
                int lrPadding = SubstanceCoreUtilities.hasText(button) ? SubstanceSizeUtils.getTextButtonLRPadding(fontSize) : 0;
                Set<SubstanceConstants.Side> openSides = SubstanceCoreUtilities.getSides(button, "substancelaf.buttonopenSide");
                int left = lrPadding + buttonInsets.left + focusPadding + (openSides != null && openSides.contains((Object)SubstanceConstants.Side.LEFT) ? -1 : 0);
                int right = lrPadding + buttonInsets.right + focusPadding + (openSides != null && openSides.contains((Object)SubstanceConstants.Side.RIGHT) ? -1 : 0);
                int top = buttonInsets.top + (openSides != null && openSides.contains((Object)SubstanceConstants.Side.TOP) ? -1 : 0);
                int bottom = buttonInsets.bottom + (openSides != null && openSides.contains((Object)SubstanceConstants.Side.BOTTOM) ? -1 : 0);
                return new Insets(top, left, bottom, right);
            }
        };
    }

    @Override
    public Dimension getPreferredSize(AbstractButton button, Dimension uiPreferredSize) {
        boolean toTweakWidth = false;
        boolean toTweakHeight = false;
        Icon icon = button.getIcon();
        boolean hasIcon = SubstanceCoreUtilities.hasIcon(button);
        boolean hasText = SubstanceCoreUtilities.hasText(button);
        Insets margin = button.getMargin();
        Dimension result = uiPreferredSize;
        boolean hasNoMinSizeProperty = SubstanceCoreUtilities.hasNoMinSizeProperty(button);
        if (!hasNoMinSizeProperty && hasText) {
            int baseWidth = uiPreferredSize.width;
            baseWidth = Math.max(baseWidth, SubstanceSizeUtils.getMinButtonWidth(SubstanceSizeUtils.getComponentFontSize(button)));
            result = new Dimension(baseWidth, uiPreferredSize.height);
            int baseHeight = result.height;
            result = new Dimension(result.width, baseHeight);
        } else if (hasNoMinSizeProperty && margin != null) {
            result = new Dimension(result.width + margin.left + margin.right, result.height + margin.top + margin.bottom);
        }
        int fontSize = SubstanceSizeUtils.getComponentFontSize(button);
        int extraPadding = SubstanceSizeUtils.getExtraPadding(fontSize);
        int focusPadding = SubstanceSizeUtils.getFocusRingPadding(fontSize);
        int iconPaddingWidth = 6 + 2 * extraPadding + 2 * focusPadding;
        int iconPaddingHeight = 6 + 2 * extraPadding;
        if (margin != null) {
            iconPaddingWidth = Math.max(iconPaddingWidth, margin.left + margin.right);
            iconPaddingHeight = Math.max(iconPaddingHeight, margin.top + margin.bottom);
        }
        if (hasIcon) {
            int iconWidth;
            int iconHeight = icon.getIconHeight();
            if ((double)iconHeight > result.getHeight() - (double)iconPaddingHeight) {
                result = new Dimension(result.width, iconHeight);
                toTweakHeight = true;
            }
            if ((double)(iconWidth = icon.getIconWidth()) > result.getWidth() - (double)iconPaddingWidth) {
                result = new Dimension(iconWidth, result.height);
                toTweakWidth = true;
            }
        }
        if (SubstanceCoreUtilities.isScrollBarButton(button)) {
            toTweakWidth = false;
            toTweakHeight = false;
        }
        if (toTweakWidth) {
            result = new Dimension(result.width + iconPaddingWidth, result.height);
        }
        if (toTweakHeight) {
            result = new Dimension(result.width, result.height + iconPaddingHeight);
        }
        return result;
    }

    @Override
    public boolean isProportionate() {
        return true;
    }

    @Override
    public float getCornerRadius(AbstractButton button, Insets insets) {
        Border parentBorder;
        float radius = SubstanceSizeUtils.getClassicButtonCornerRadius(SubstanceSizeUtils.getComponentFontSize(button));
        if (button instanceof SubstanceInternalArrowButton && (parentBorder = ((JComponent)button.getParent()).getBorder()) instanceof SubstanceBorder) {
            radius *= ((SubstanceBorder)parentBorder).getRadiusScaleFactor();
        }
        if (SubstanceCoreUtilities.isToolBarButton(button)) {
            radius = SubstanceCoreUtilities.getToolbarButtonCornerRadius(button, insets);
        }
        return radius;
    }
}

