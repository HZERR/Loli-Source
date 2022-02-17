/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.behavior.TextBinding;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayout;
import com.sun.javafx.tk.Toolkit;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.function.Consumer;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.OverrunStyle;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.Mnemonic;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class Utils {
    static final Text helper = new Text();
    static final double DEFAULT_WRAPPING_WIDTH = helper.getWrappingWidth();
    static final double DEFAULT_LINE_SPACING = helper.getLineSpacing();
    static final String DEFAULT_TEXT = helper.getText();
    static final TextBoundsType DEFAULT_BOUNDS_TYPE = helper.getBoundsType();
    static final TextLayout layout = Toolkit.getToolkit().getTextLayoutFactory().createLayout();
    private static BreakIterator charIterator = null;

    static double getAscent(Font font, TextBoundsType textBoundsType) {
        layout.setContent("", font.impl_getNativeFont());
        layout.setWrapWidth(0.0f);
        layout.setLineSpacing(0.0f);
        if (textBoundsType == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
            layout.setBoundsType(16384);
        } else {
            layout.setBoundsType(0);
        }
        return -layout.getBounds().getMinY();
    }

    static double getLineHeight(Font font, TextBoundsType textBoundsType) {
        layout.setContent("", font.impl_getNativeFont());
        layout.setWrapWidth(0.0f);
        layout.setLineSpacing(0.0f);
        if (textBoundsType == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
            layout.setBoundsType(16384);
        } else {
            layout.setBoundsType(0);
        }
        return layout.getLines()[0].getBounds().getHeight();
    }

    static double computeTextWidth(Font font, String string, double d2) {
        layout.setContent(string != null ? string : "", font.impl_getNativeFont());
        layout.setWrapWidth((float)d2);
        return layout.getBounds().getWidth();
    }

    static double computeTextHeight(Font font, String string, double d2, TextBoundsType textBoundsType) {
        return Utils.computeTextHeight(font, string, d2, 0.0, textBoundsType);
    }

    static double computeTextHeight(Font font, String string, double d2, double d3, TextBoundsType textBoundsType) {
        layout.setContent(string != null ? string : "", font.impl_getNativeFont());
        layout.setWrapWidth((float)d2);
        layout.setLineSpacing((float)d3);
        if (textBoundsType == TextBoundsType.LOGICAL_VERTICAL_CENTER) {
            layout.setBoundsType(16384);
        } else {
            layout.setBoundsType(0);
        }
        return layout.getBounds().getHeight();
    }

    static int computeTruncationIndex(Font font, String string, double d2) {
        helper.setText(string);
        helper.setFont(font);
        helper.setWrappingWidth(0.0);
        helper.setLineSpacing(0.0);
        Bounds bounds = helper.getLayoutBounds();
        Point2D point2D = new Point2D(d2 - 2.0, bounds.getMinY() + bounds.getHeight() / 2.0);
        int n2 = helper.impl_hitTestChar(point2D).getCharIndex();
        helper.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
        helper.setLineSpacing(DEFAULT_LINE_SPACING);
        helper.setText(DEFAULT_TEXT);
        return n2;
    }

    static String computeClippedText(Font font, String string, double d2, OverrunStyle overrunStyle, String string2) {
        char c2;
        String string3;
        if (font == null) {
            throw new IllegalArgumentException("Must specify a font");
        }
        OverrunStyle overrunStyle2 = overrunStyle == null || overrunStyle == OverrunStyle.CLIP ? OverrunStyle.ELLIPSIS : overrunStyle;
        String string4 = string3 = overrunStyle == OverrunStyle.CLIP ? "" : string2;
        if (string == null || "".equals(string)) {
            return string;
        }
        double d3 = Utils.computeTextWidth(font, string, 0.0);
        if (d3 - d2 < (double)0.001f) {
            return string;
        }
        double d4 = Utils.computeTextWidth(font, string3, 0.0);
        double d5 = d2 - d4;
        if (d2 < d4) {
            return "";
        }
        if (overrunStyle2 == OverrunStyle.ELLIPSIS || overrunStyle2 == OverrunStyle.WORD_ELLIPSIS || overrunStyle2 == OverrunStyle.LEADING_ELLIPSIS || overrunStyle2 == OverrunStyle.LEADING_WORD_ELLIPSIS) {
            String string5;
            int n2;
            boolean bl;
            boolean bl2 = bl = overrunStyle2 == OverrunStyle.WORD_ELLIPSIS || overrunStyle2 == OverrunStyle.LEADING_WORD_ELLIPSIS;
            if (overrunStyle2 == OverrunStyle.ELLIPSIS && !new Bidi(string, 0).isMixed()) {
                int n3 = Utils.computeTruncationIndex(font, string, d2 - d4);
                if (n3 < 0 || n3 >= string.length()) {
                    return string;
                }
                return string.substring(0, n3) + string3;
            }
            double d6 = 0.0;
            int n4 = -1;
            int n5 = 0;
            int n6 = overrunStyle2 == OverrunStyle.LEADING_ELLIPSIS || overrunStyle2 == OverrunStyle.LEADING_WORD_ELLIPSIS ? string.length() - 1 : 0;
            int n7 = n6 == 0 ? string.length() - 1 : 0;
            int n8 = n2 = n6 == 0 ? 1 : -1;
            boolean bl3 = n6 == 0 ? n6 > n7 : n6 < n7;
            int n9 = n6;
            while (!bl3) {
                n5 = n9;
                char c3 = string.charAt(n5);
                d6 = Utils.computeTextWidth(font, n6 == 0 ? string.substring(0, n9 + 1) : string.substring(n9, n6 + 1), 0.0);
                if (Character.isWhitespace(c3)) {
                    n4 = n5;
                }
                if (d6 > d5) break;
                bl3 = n6 == 0 ? n9 >= n7 : n9 <= n7;
                n9 += n2;
            }
            int n10 = n9 = !bl || n4 == -1 ? 1 : 0;
            String string6 = n6 == 0 ? string.substring(0, n9 != 0 ? n5 : n4) : (string5 = string.substring((n9 != 0 ? n5 : n4) + 1));
            assert (!string.equals(string5));
            if (overrunStyle2 == OverrunStyle.ELLIPSIS || overrunStyle2 == OverrunStyle.WORD_ELLIPSIS) {
                return string5 + string3;
            }
            return string3 + string5;
        }
        int n11 = 0;
        int n12 = 0;
        int n13 = -1;
        int n14 = -1;
        n11 = -1;
        n12 = -1;
        double d7 = 0.0;
        for (int i2 = 0; i2 <= string.length() - 1; ++i2) {
            c2 = string.charAt(i2);
            if ((d7 += Utils.computeTextWidth(font, "" + c2, 0.0)) > d5) break;
            n11 = i2;
            if (Character.isWhitespace(c2)) {
                n13 = n11;
            }
            int n15 = string.length() - 1 - i2;
            c2 = string.charAt(n15);
            if ((d7 += Utils.computeTextWidth(font, "" + c2, 0.0)) > d5) break;
            n12 = n15;
            if (!Character.isWhitespace(c2)) continue;
            n14 = n12;
        }
        if (n11 < 0) {
            return string3;
        }
        if (overrunStyle2 == OverrunStyle.CENTER_ELLIPSIS) {
            if (n12 < 0) {
                return string.substring(0, n11 + 1) + string3;
            }
            return string.substring(0, n11 + 1) + string3 + string.substring(n12);
        }
        boolean bl = Character.isWhitespace(string.charAt(n11 + 1));
        int n16 = n13 == -1 || bl ? n11 + 1 : n13;
        String string7 = string.substring(0, n16);
        if (n12 < 0) {
            return string7 + string3;
        }
        c2 = (char)(Character.isWhitespace(string.charAt(n12 - 1)) ? 1 : 0);
        n16 = n14 == -1 || c2 != '\u0000' ? n12 : n14 + 1;
        String string8 = string.substring(n16);
        return string7 + string3 + string8;
    }

    static String computeClippedWrappedText(Font font, String string, double d2, double d3, OverrunStyle overrunStyle, String string2, TextBoundsType textBoundsType) {
        Point2D point2D;
        int n2;
        if (font == null) {
            throw new IllegalArgumentException("Must specify a font");
        }
        String string3 = overrunStyle == OverrunStyle.CLIP ? "" : string2;
        int n3 = string3.length();
        double d4 = Utils.computeTextWidth(font, string3, 0.0);
        double d5 = Utils.computeTextHeight(font, string3, 0.0, textBoundsType);
        if (d2 < d4 || d3 < d5) {
            return string;
        }
        helper.setText(string);
        helper.setFont(font);
        helper.setWrappingWidth((int)Math.ceil(d2));
        helper.setBoundsType(textBoundsType);
        helper.setLineSpacing(0.0);
        boolean bl = overrunStyle == OverrunStyle.LEADING_ELLIPSIS || overrunStyle == OverrunStyle.LEADING_WORD_ELLIPSIS;
        boolean bl2 = overrunStyle == OverrunStyle.CENTER_ELLIPSIS || overrunStyle == OverrunStyle.CENTER_WORD_ELLIPSIS;
        boolean bl3 = !bl && !bl2;
        boolean bl4 = overrunStyle == OverrunStyle.WORD_ELLIPSIS || overrunStyle == OverrunStyle.LEADING_WORD_ELLIPSIS || overrunStyle == OverrunStyle.CENTER_WORD_ELLIPSIS;
        String string4 = string;
        int n4 = string4 != null ? string4.length() : 0;
        int n5 = -1;
        Point2D point2D2 = null;
        if (bl2) {
            point2D2 = new Point2D((d2 - d4) / 2.0, d3 / 2.0 - helper.getBaselineOffset());
        }
        if ((n2 = helper.impl_hitTestChar(point2D = new Point2D(0.0, d3 - helper.getBaselineOffset())).getCharIndex()) >= n4) {
            helper.setBoundsType(TextBoundsType.LOGICAL);
            return string;
        }
        if (bl2) {
            n2 = helper.impl_hitTestChar(point2D2).getCharIndex();
        }
        if (n2 > 0 && n2 < n4) {
            int n6;
            int n7;
            if (bl2 || bl3) {
                n7 = n2;
                if (bl2) {
                    if (bl4) {
                        n6 = Utils.lastBreakCharIndex(string, n7 + 1);
                        if (n6 >= 0) {
                            n7 = n6 + 1;
                        } else {
                            n6 = Utils.firstBreakCharIndex(string, n7);
                            if (n6 >= 0) {
                                n7 = n6 + 1;
                            }
                        }
                    }
                    n5 = n7 + n3;
                }
                string4 = string4.substring(0, n7) + string3;
            }
            if (bl || bl2) {
                n7 = Math.max(0, n4 - n2 - 10);
                if (n7 > 0 && bl4) {
                    n6 = Utils.lastBreakCharIndex(string, n7 + 1);
                    if (n6 >= 0) {
                        n7 = n6 + 1;
                    } else {
                        n6 = Utils.firstBreakCharIndex(string, n7);
                        if (n6 >= 0) {
                            n7 = n6 + 1;
                        }
                    }
                }
                string4 = bl2 ? string4 + string.substring(n7) : string3 + string.substring(n7);
            }
            while (true) {
                int n8;
                helper.setText(string4);
                n7 = helper.impl_hitTestChar(point2D).getCharIndex();
                if (bl2 && n7 < n5) {
                    if (n7 > 0 && string4.charAt(n7 - 1) == '\n') {
                        --n7;
                    }
                    string4 = string.substring(0, n7) + string3;
                    break;
                }
                if (n7 <= 0 || n7 >= string4.length()) break;
                if (bl) {
                    n6 = n3 + 1;
                    if (bl4 && (n8 = Utils.firstBreakCharIndex(string4, n6)) >= 0) {
                        n6 = n8 + 1;
                    }
                    string4 = string3 + string4.substring(n6);
                    continue;
                }
                if (bl2) {
                    n6 = n5 + 1;
                    if (bl4 && (n8 = Utils.firstBreakCharIndex(string4, n6)) >= 0) {
                        n6 = n8 + 1;
                    }
                    string4 = string4.substring(0, n5) + string4.substring(n6);
                    continue;
                }
                n6 = string4.length() - n3 - 1;
                if (bl4 && (n8 = Utils.lastBreakCharIndex(string4, n6)) >= 0) {
                    n6 = n8;
                }
                string4 = string4.substring(0, n6) + string3;
            }
        }
        helper.setWrappingWidth(DEFAULT_WRAPPING_WIDTH);
        helper.setLineSpacing(DEFAULT_LINE_SPACING);
        helper.setText(DEFAULT_TEXT);
        helper.setBoundsType(DEFAULT_BOUNDS_TYPE);
        return string4;
    }

    private static int firstBreakCharIndex(String string, int n2) {
        char[] arrc = string.toCharArray();
        for (int i2 = n2; i2 < arrc.length; ++i2) {
            if (!Utils.isPreferredBreakCharacter(arrc[i2])) continue;
            return i2;
        }
        return -1;
    }

    private static int lastBreakCharIndex(String string, int n2) {
        char[] arrc = string.toCharArray();
        for (int i2 = n2; i2 >= 0; --i2) {
            if (!Utils.isPreferredBreakCharacter(arrc[i2])) continue;
            return i2;
        }
        return -1;
    }

    private static boolean isPreferredBreakCharacter(char c2) {
        if (Character.isWhitespace(c2)) {
            return true;
        }
        switch (c2) {
            case '.': 
            case ':': 
            case ';': {
                return true;
            }
        }
        return false;
    }

    private static boolean requiresComplexLayout(Font font, String string) {
        return false;
    }

    static int computeStartOfWord(Font font, String string, int n2) {
        if ("".equals(string) || n2 < 0) {
            return 0;
        }
        if (string.length() <= n2) {
            return string.length();
        }
        if (Character.isWhitespace(string.charAt(n2))) {
            return n2;
        }
        boolean bl = Utils.requiresComplexLayout(font, string);
        if (bl) {
            return 0;
        }
        int n3 = n2;
        while (--n3 >= 0) {
            if (!Character.isWhitespace(string.charAt(n3))) continue;
            return n3 + 1;
        }
        return 0;
    }

    static int computeEndOfWord(Font font, String string, int n2) {
        if (string.equals("") || n2 < 0) {
            return 0;
        }
        if (string.length() <= n2) {
            return string.length();
        }
        if (Character.isWhitespace(string.charAt(n2))) {
            return n2;
        }
        boolean bl = Utils.requiresComplexLayout(font, string);
        if (bl) {
            return string.length();
        }
        int n3 = n2;
        while (++n3 < string.length()) {
            if (!Character.isWhitespace(string.charAt(n3))) continue;
            return n3;
        }
        return string.length();
    }

    public static double boundedSize(double d2, double d3, double d4) {
        return Math.min(Math.max(d2, d3), Math.max(d3, d4));
    }

    static void addMnemonics(ContextMenu contextMenu, Scene scene) {
        Utils.addMnemonics(contextMenu, scene, false);
    }

    static void addMnemonics(ContextMenu contextMenu, Scene scene, boolean bl) {
        if (!PlatformUtil.isMac()) {
            ContextMenuContent contextMenuContent = (ContextMenuContent)contextMenu.getSkin().getNode();
            for (int i2 = 0; i2 < contextMenu.getItems().size(); ++i2) {
                TextBinding textBinding;
                int n2;
                MenuItem menuItem = (MenuItem)contextMenu.getItems().get(i2);
                if (!menuItem.isMnemonicParsing() || (n2 = (textBinding = new TextBinding(menuItem.getText())).getMnemonicIndex()) < 0) continue;
                KeyCombination keyCombination = textBinding.getMnemonicKeyCombination();
                Mnemonic mnemonic = new Mnemonic(contextMenuContent.getLabelAt(i2), keyCombination);
                scene.addMnemonic(mnemonic);
                contextMenuContent.getLabelAt(i2).impl_setShowMnemonics(bl);
            }
        }
    }

    static void removeMnemonics(ContextMenu contextMenu, Scene scene) {
        if (!PlatformUtil.isMac()) {
            ContextMenuContent contextMenuContent = (ContextMenuContent)contextMenu.getSkin().getNode();
            for (int i2 = 0; i2 < contextMenu.getItems().size(); ++i2) {
                TextBinding textBinding;
                int n2;
                MenuItem menuItem = (MenuItem)contextMenu.getItems().get(i2);
                if (!menuItem.isMnemonicParsing() || (n2 = (textBinding = new TextBinding(menuItem.getText())).getMnemonicIndex()) < 0) continue;
                KeyCombination keyCombination = textBinding.getMnemonicKeyCombination();
                ObservableList observableList = (ObservableList)scene.getMnemonics().get(keyCombination);
                if (observableList == null) continue;
                for (int i3 = 0; i3 < observableList.size(); ++i3) {
                    if (((Mnemonic)observableList.get(i3)).getNode() != contextMenuContent.getLabelAt(i2)) continue;
                    observableList.remove(i3);
                }
            }
        }
    }

    static double computeXOffset(double d2, double d3, HPos hPos) {
        if (hPos == null) {
            return 0.0;
        }
        switch (hPos) {
            case LEFT: {
                return 0.0;
            }
            case CENTER: {
                return (d2 - d3) / 2.0;
            }
            case RIGHT: {
                return d2 - d3;
            }
        }
        return 0.0;
    }

    static double computeYOffset(double d2, double d3, VPos vPos) {
        if (vPos == null) {
            return 0.0;
        }
        switch (vPos) {
            case TOP: {
                return 0.0;
            }
            case CENTER: {
                return (d2 - d3) / 2.0;
            }
            case BOTTOM: {
                return d2 - d3;
            }
        }
        return 0.0;
    }

    public static boolean isTwoLevelFocus() {
        return Platform.isSupported(ConditionalFeature.TWO_LEVEL_FOCUS);
    }

    public static int getHitInsertionIndex(HitInfo hitInfo, String string) {
        int n2 = hitInfo.getCharIndex();
        if (string != null && !hitInfo.isLeading()) {
            if (charIterator == null) {
                charIterator = BreakIterator.getCharacterInstance();
            }
            charIterator.setText(string);
            int n3 = charIterator.following(n2);
            n2 = n3 == -1 ? hitInfo.getInsertionIndex() : n3;
        }
        return n2;
    }

    public static <T> void executeOnceWhenPropertyIsNonNull(final ObservableValue<T> observableValue, final Consumer<T> consumer) {
        if (observableValue == null) {
            return;
        }
        T t2 = observableValue.getValue();
        if (t2 != null) {
            consumer.accept(t2);
        } else {
            InvalidationListener invalidationListener = new InvalidationListener(){

                @Override
                public void invalidated(Observable observable) {
                    Object t2 = observableValue.getValue();
                    if (t2 != null) {
                        observableValue.removeListener(this);
                        consumer.accept(t2);
                    }
                }
            };
            observableValue.addListener(invalidationListener);
        }
    }
}

