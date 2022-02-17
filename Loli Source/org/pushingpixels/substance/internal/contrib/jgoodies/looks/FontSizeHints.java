/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.jgoodies.looks;

import org.pushingpixels.lafwidget.utils.LookUtils;

public final class FontSizeHints {
    public static final FontSizeHints LARGE = new FontSizeHints(12, 12, 14, 14);
    public static final FontSizeHints SYSTEM = new FontSizeHints(11, 11, 14, 14);
    public static final FontSizeHints MIXED2 = new FontSizeHints(11, 11, 14, 13);
    public static final FontSizeHints MIXED = new FontSizeHints(11, 11, 14, 12);
    public static final FontSizeHints SMALL = new FontSizeHints(11, 11, 12, 12);
    public static final FontSizeHints FIXED = new FontSizeHints(12, 12, 12, 12);
    public static final FontSizeHints DEFAULT = SYSTEM;
    private final int loResMenuFontSize;
    private final int loResControlFontSize;
    private final int hiResMenuFontSize;
    private final int hiResControlFontSize;

    public FontSizeHints(int loResMenuFontSize, int loResControlFontSize, int hiResMenuFontSize, int hiResControlFontSize) {
        this.loResMenuFontSize = loResMenuFontSize;
        this.loResControlFontSize = loResControlFontSize;
        this.hiResMenuFontSize = hiResMenuFontSize;
        this.hiResControlFontSize = hiResControlFontSize;
    }

    public int loResMenuFontSize() {
        return this.loResMenuFontSize;
    }

    public int loResControlFontSize() {
        return this.loResControlFontSize;
    }

    public int hiResMenuFontSize() {
        return this.hiResMenuFontSize;
    }

    public int hiResControlFontSize() {
        return this.hiResControlFontSize;
    }

    public int menuFontSize() {
        return LookUtils.IS_LOW_RESOLUTION ? this.loResMenuFontSize : this.hiResMenuFontSize();
    }

    public int controlFontSize() {
        return LookUtils.IS_LOW_RESOLUTION ? this.loResControlFontSize : this.hiResControlFontSize();
    }

    public float menuFontSizeDelta() {
        return this.menuFontSize() - SYSTEM.menuFontSize();
    }

    public float controlFontSizeDelta() {
        return this.controlFontSize() - SYSTEM.controlFontSize();
    }

    public static FontSizeHints valueOf(String name) {
        if (name.equalsIgnoreCase("LARGE")) {
            return LARGE;
        }
        if (name.equalsIgnoreCase("SYSTEM")) {
            return SYSTEM;
        }
        if (name.equalsIgnoreCase("MIXED")) {
            return MIXED;
        }
        if (name.equalsIgnoreCase("SMALL")) {
            return SMALL;
        }
        if (name.equalsIgnoreCase("FIXED")) {
            return FIXED;
        }
        throw new IllegalArgumentException("Unknown font size hints name: " + name);
    }
}

