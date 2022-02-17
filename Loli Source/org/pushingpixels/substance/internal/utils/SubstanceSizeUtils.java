/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.utils;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.fonts.FontPolicy;
import org.pushingpixels.substance.api.fonts.FontSet;
import org.pushingpixels.substance.internal.fonts.DefaultGnomeFontPolicy;

public class SubstanceSizeUtils {
    private static int controlFontSize = -1;
    private static double pointsToPixelsRatio = 1.0;

    public static int getControlFontSize() {
        if (controlFontSize > 0) {
            return controlFontSize;
        }
        FontPolicy fPolicy = SubstanceLookAndFeel.getFontPolicy();
        FontSet fSet = fPolicy.getFontSet("Substance", null);
        controlFontSize = fSet.getControlFont().getSize();
        return controlFontSize;
    }

    public static void setControlFontSize(int size) {
        controlFontSize = size;
    }

    public static int getComponentFontSize(Component c2) {
        return c2 == null || c2.getFont() == null ? SubstanceSizeUtils.getControlFontSize() : c2.getFont().getSize();
    }

    public static float getAdjustedSize(int fontSize, float baseSize, int forEachBase, float toAdjustBy) {
        int delta = fontSize - 11;
        if (delta <= 0) {
            return baseSize;
        }
        float result = baseSize + (float)delta * toAdjustBy / (float)forEachBase;
        return result;
    }

    public static int getAdjustedSize(int fontSize, int baseSize, int forEachBase, int toAdjustBy, boolean toRoundAsEven) {
        int delta = fontSize - 11;
        if (delta <= 0) {
            return baseSize;
        }
        int result = baseSize + delta * toAdjustBy / forEachBase;
        if (toRoundAsEven && result % 2 != 0) {
            --result;
        }
        return result;
    }

    public static float getArrowIconHeight(int fontSize) {
        if (fontSize < 12) {
            return 2.5f + (float)fontSize * 0.5f;
        }
        return 3.0f + (float)fontSize * 0.6f;
    }

    public static float getArrowIconWidth(int fontSize) {
        int result = 2 * fontSize / 3;
        if (result % 2 == 0) {
            ++result;
        }
        return result + 4;
    }

    public static float getArrowStrokeWidth(int fontSize) {
        return (float)fontSize / 6.0f;
    }

    public static float getBorderStrokeWidth(int fontSize) {
        return (float)fontSize / 10.0f;
    }

    public static Insets getButtonInsets(int fontSize) {
        Insets textInsets = SubstanceSizeUtils.getTextBorderInsets(fontSize);
        int borderStroke = (int)SubstanceSizeUtils.getBorderStrokeWidth(fontSize);
        int topDelta = textInsets.top - borderStroke;
        int bottomDelta = textInsets.bottom - borderStroke;
        int lrInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 4, 4, 1, false);
        return new Insets(topDelta, lrInset, bottomDelta, lrInset);
    }

    public static Border getCheckBoxBorder(int fontSize, boolean ltr) {
        int tInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 3, 1, false);
        int bInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 3, 3, 1, false);
        if (fontSize == 11) {
            tInset = 2;
            bInset = 2;
        }
        int leadingInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 3, 3, 1, false);
        int trailingInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 5, 3, 1, false);
        return new BorderUIResource.EmptyBorderUIResource(tInset, ltr ? leadingInset : trailingInset, bInset, ltr ? trailingInset : leadingInset);
    }

    public static int getCheckBoxMarkSize(int fontSize) {
        return 5 + fontSize;
    }

    public static float getClassicButtonCornerRadius(int fontSize) {
        return SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 6, 1, false);
    }

    public static Insets getComboBorderInsets(int fontSize) {
        int tbInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 1, 3, 1, false);
        int lrInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 3, 1, false);
        return new Insets(tbInset, lrInset, tbInset, lrInset);
    }

    public static Insets getComboLayoutInsets(int fontSize) {
        int tbInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 4, 1, false);
        int lrInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 4, 1, false);
        return new Insets(tbInset, lrInset, tbInset, lrInset);
    }

    public static Insets getComboTextBorderInsets(int fontSize) {
        Insets textInsets = SubstanceSizeUtils.getTextBorderInsets(fontSize);
        Insets comboInsets = SubstanceSizeUtils.getComboBorderInsets(fontSize);
        int topDelta = textInsets.top - comboInsets.top;
        int bottomDelta = textInsets.bottom - comboInsets.bottom;
        int lrInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 3, 4, 1, false);
        return new Insets(topDelta, lrInset, bottomDelta, lrInset);
    }

    public static Insets getDefaultBorderInsets(int fontSize) {
        int inset = SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 3, 1, false);
        return new Insets(inset, inset, inset, inset);
    }

    public static float getDoubleArrowStrokeWidth(int fontSize) {
        return (float)fontSize / 8.0f;
    }

    public static int getDragBumpDiameter(int fontSize) {
        return SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 4, 1, false);
    }

    public static int getBigDragBumpDiameter(int fontSize) {
        int result = SubstanceSizeUtils.getAdjustedSize(fontSize, 3, 3, 1, false);
        if (result % 2 != 0) {
            ++result;
        }
        return result;
    }

    public static int getExtraPadding(int fontSize) {
        if (fontSize < 14) {
            return 0;
        }
        return (int)SubstanceSizeUtils.getAdjustedSize(fontSize, 0.0f, 3, 1.2f);
    }

    public static int getFocusRingPadding(int fontSize) {
        if (fontSize < 14) {
            return 2;
        }
        return 3 + (int)SubstanceSizeUtils.getAdjustedSize(fontSize, 0.0f, 3, 0.8f);
    }

    public static float getFocusStrokeWidth(int fontSize) {
        return Math.max(1.0f, (float)fontSize / 10.0f);
    }

    public static Insets getListCellRendererInsets(int fontSize) {
        Insets textInsets = SubstanceSizeUtils.getTextBorderInsets(fontSize);
        Insets comboInsets = SubstanceSizeUtils.getComboBorderInsets(fontSize);
        int borderStroke = (int)SubstanceSizeUtils.getBorderStrokeWidth(fontSize);
        int topDelta = textInsets.top - comboInsets.top - borderStroke;
        int bottomDelta = textInsets.bottom - comboInsets.bottom - borderStroke;
        int lrInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 4, 4, 1, false);
        return new Insets(topDelta, lrInset, bottomDelta, lrInset);
    }

    public static int getMenuCheckMarkSize(int fontSize) {
        int result = fontSize - 2;
        if (result % 2 == 0) {
            --result;
        }
        return result;
    }

    public static int getMenuItemMargin(int fontSize) {
        return SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 4, 1, false);
    }

    public static int getTextIconGap(int fontSize) {
        return SubstanceSizeUtils.getAdjustedSize(fontSize, 4, 3, 1, false);
    }

    public static int getMinButtonWidth(int fontSize) {
        return 5 * fontSize + 12;
    }

    public static int getPasswordDotDiameter(int fontSize) {
        return SubstanceSizeUtils.getAdjustedSize(fontSize, 7, 2, 1, false);
    }

    public static int getPasswordDotGap(int fontSize) {
        return (fontSize - 6) / 3;
    }

    public static Border getRadioButtonBorder(int fontSize, boolean ltr) {
        Border checkBoxBorder = SubstanceSizeUtils.getCheckBoxBorder(fontSize, ltr);
        Insets checkBoxInsets = checkBoxBorder.getBorderInsets(null);
        return new BorderUIResource.EmptyBorderUIResource(checkBoxInsets.top, checkBoxInsets.left - (ltr ? 0 : 2), checkBoxInsets.bottom, checkBoxInsets.right - (ltr ? 2 : 0));
    }

    public static int getRadioButtonMarkSize(int fontSize) {
        int result = fontSize;
        if (result % 2 == 0) {
            --result;
        }
        return result;
    }

    public static int getScrollBarWidth(int fontSize) {
        int result = (int)(SubstanceSizeUtils.getArrowIconWidth(fontSize) * 3.0f / 2.0f);
        if (result % 2 == 0) {
            ++result;
        }
        return result;
    }

    public static int getSliderIconSize(int fontSize) {
        int result = fontSize + 5;
        if (result % 2 != 0) {
            --result;
        }
        return result;
    }

    public static int getSliderTickSize(int fontSize) {
        return Math.max(7, fontSize - 3);
    }

    public static int getSliderTrackSize(int fontSize) {
        return SubstanceSizeUtils.getAdjustedSize(fontSize, 5, 4, 1, false);
    }

    public static float getSmallArrowIconHeight(int fontSize) {
        return SubstanceSizeUtils.getArrowIconHeight(fontSize) - 1.0f;
    }

    public static float getSmallArrowIconWidth(int fontSize) {
        return SubstanceSizeUtils.getArrowIconWidth(fontSize) - 2.0f;
    }

    public static float getSpinnerArrowIconHeight(int fontSize) {
        float result = SubstanceSizeUtils.getArrowIconHeight(fontSize) + SubstanceSizeUtils.getAdjustedSize(fontSize + 1, 0.0f, 1, -0.25f);
        return result;
    }

    public static float getSpinnerArrowIconWidth(int fontSize) {
        int result = (int)(SubstanceSizeUtils.getArrowIconWidth(fontSize) + SubstanceSizeUtils.getAdjustedSize(fontSize, 1.0f, 1, -0.15f));
        if (result % 2 == 0) {
            --result;
        }
        return result;
    }

    public static Insets getSpinnerBorderInsets(int fontSize) {
        Insets comboInsets = SubstanceSizeUtils.getComboBorderInsets(fontSize);
        return new Insets(comboInsets.top + 1, comboInsets.left, comboInsets.bottom + 1, comboInsets.right);
    }

    public static Insets getSpinnerArrowButtonInsets(int fontSize) {
        int borderStrokeWidth = (int)Math.floor(SubstanceSizeUtils.getBorderStrokeWidth(fontSize));
        return new Insets(borderStrokeWidth, borderStrokeWidth, borderStrokeWidth, borderStrokeWidth);
    }

    public static Insets getSpinnerTextBorderInsets(int fontSize) {
        Insets textInsets = SubstanceSizeUtils.getComboTextBorderInsets(fontSize);
        return new Insets(textInsets.top - 1, textInsets.left, textInsets.bottom - 1, textInsets.right);
    }

    public static float getSplitPaneArrowIconHeight(int fontSize) {
        float result = SubstanceSizeUtils.getArrowIconHeight(fontSize) + SubstanceSizeUtils.getAdjustedSize(fontSize, -1.0f, 1, -0.3f);
        return result;
    }

    public static float getSplitPaneArrowIconWidth(int fontSize) {
        float result = SubstanceSizeUtils.getArrowIconWidth(fontSize) + SubstanceSizeUtils.getAdjustedSize(fontSize, -2.0f, 1, -0.25f);
        return result;
    }

    public static int getSplitPaneButtonOffset(int fontSize) {
        return SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 3, 1, false);
    }

    public static Insets getTabbedPaneContentInsets(int fontSize) {
        float borderStrokeWidth = SubstanceSizeUtils.getBorderStrokeWidth(fontSize);
        int tbIns = (int)Math.ceil(2.5 * (double)borderStrokeWidth);
        int lrIns = (int)Math.ceil(3.0 * (double)borderStrokeWidth);
        return new Insets(tbIns, lrIns, tbIns, lrIns);
    }

    public static float getTabCloseButtonStrokeWidth(int fontSize) {
        return (float)fontSize / 10.0f;
    }

    public static int getTabCloseIconSize(int fontSize) {
        return fontSize - 2;
    }

    public static Insets getTableCellRendererInsets(int fontSize) {
        Insets textInsets = SubstanceSizeUtils.getTextBorderInsets(fontSize);
        Insets comboInsets = SubstanceSizeUtils.getComboBorderInsets(fontSize);
        int topDelta = textInsets.top - comboInsets.top - 1;
        int bottomDelta = textInsets.bottom - comboInsets.bottom - 2;
        if (fontSize == 11) {
            ++bottomDelta;
        }
        int lrInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 4, 1, false);
        return new Insets(topDelta, lrInset, bottomDelta, lrInset);
    }

    public static Insets getTextBorderInsets(int fontSize) {
        int tInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 3, 3, 1, false);
        int bInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 4, 3, 1, false);
        if (fontSize == 11) {
            tInset = 3;
            bInset = 3;
        }
        int lrInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 5, 3, 1, false);
        return new Insets(tInset, lrInset, bInset, lrInset);
    }

    public static int getTextButtonLRPadding(int fontSize) {
        return SubstanceSizeUtils.getAdjustedSize(fontSize, 3, 2, 1, false);
    }

    public static int getTitlePaneIconSize() {
        return 5 + SubstanceSizeUtils.getControlFontSize();
    }

    public static int getToolBarDragInset(int fontSize) {
        return fontSize + 5;
    }

    public static Insets getToolBarInsets(int fontSize) {
        int lbrInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 2, 3, 1, false);
        int tInset = SubstanceSizeUtils.getAdjustedSize(fontSize, 1, 3, 1, false);
        return new Insets(tInset, lbrInset, lbrInset, lbrInset);
    }

    public static Insets getToolTipBorderInsets(int fontSize) {
        int inset = SubstanceSizeUtils.getAdjustedSize(fontSize, 1, 3, 1, false);
        return new Insets(inset, inset, inset, inset);
    }

    public static Insets getTreeCellRendererInsets(int fontSize) {
        Insets listCellInsets = SubstanceSizeUtils.getListCellRendererInsets(fontSize);
        return new Insets(listCellInsets.top - 1, listCellInsets.left - 2, listCellInsets.bottom - 1, listCellInsets.right - 2);
    }

    public static int getTreeIconSize(int fontSize) {
        int extraPadding = SubstanceSizeUtils.getExtraPadding(fontSize);
        int extraPadding2 = 2 * extraPadding;
        return 10 + extraPadding2;
    }

    public static double getPointsToPixelsRatio() {
        return pointsToPixelsRatio;
    }

    public static void resetPointsToPixelsRatio(FontPolicy fontPolicy) {
        pointsToPixelsRatio = fontPolicy instanceof DefaultGnomeFontPolicy ? DefaultGnomeFontPolicy.getPointsToPixelsRatio() : (double)Toolkit.getDefaultToolkit().getScreenResolution() / 72.0;
    }
}

