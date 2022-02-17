/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Size;

final class MacCursor
extends Cursor {
    private static boolean isNSCursorVisible;
    private static boolean isCursorNONE;
    private static boolean isVisible;

    private static native void _initIDs();

    protected MacCursor(int n2) {
        super(n2);
    }

    protected MacCursor(int n2, int n3, Pixels pixels) {
        super(n2, n3, pixels);
    }

    @Override
    protected native long _createCursor(int var1, int var2, Pixels var3);

    void set() {
        int n2 = this.getType();
        isCursorNONE = n2 == -1;
        MacCursor.setVisible(isVisible);
        switch (n2) {
            case -1: {
                break;
            }
            case 0: {
                this._setCustom(this.getNativeCursor());
                break;
            }
            default: {
                this._set(n2);
            }
        }
    }

    private native void _set(int var1);

    private native void _setCustom(long var1);

    private static native void _setVisible(boolean var0);

    private static native Size _getBestSize(int var0, int var1);

    static void setVisible_impl(boolean bl) {
        boolean bl2;
        isVisible = bl;
        boolean bl3 = bl2 = bl && !isCursorNONE;
        if (isNSCursorVisible == bl2) {
            return;
        }
        isNSCursorVisible = bl2;
        MacCursor._setVisible(bl2);
    }

    static Size getBestSize_impl(int n2, int n3) {
        return MacCursor._getBestSize(n2, n3);
    }

    static {
        MacCursor._initIDs();
        isNSCursorVisible = true;
        isCursorNONE = false;
        isVisible = true;
    }
}

