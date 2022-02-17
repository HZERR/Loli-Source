/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.event;

public final class WCKeyEvent {
    public static final int KEY_TYPED = 0;
    public static final int KEY_PRESSED = 1;
    public static final int KEY_RELEASED = 2;
    public static final int VK_BACK = 8;
    public static final int VK_TAB = 9;
    public static final int VK_RETURN = 13;
    public static final int VK_ESCAPE = 27;
    public static final int VK_PRIOR = 33;
    public static final int VK_NEXT = 34;
    public static final int VK_END = 35;
    public static final int VK_HOME = 36;
    public static final int VK_LEFT = 37;
    public static final int VK_UP = 38;
    public static final int VK_RIGHT = 39;
    public static final int VK_DOWN = 40;
    public static final int VK_INSERT = 45;
    public static final int VK_DELETE = 46;
    public static final int VK_OEM_PERIOD = 190;
    private final int type;
    private final long when;
    private final String text;
    private final String keyIdentifier;
    private final int windowsVirtualKeyCode;
    private final boolean shift;
    private final boolean ctrl;
    private final boolean alt;
    private final boolean meta;

    public WCKeyEvent(int n2, String string, String string2, int n3, boolean bl, boolean bl2, boolean bl3, boolean bl4, long l2) {
        this.type = n2;
        this.text = string;
        this.keyIdentifier = string2;
        this.windowsVirtualKeyCode = n3;
        this.shift = bl;
        this.ctrl = bl2;
        this.alt = bl3;
        this.meta = bl4;
        this.when = l2;
    }

    public int getType() {
        return this.type;
    }

    public long getWhen() {
        return this.when;
    }

    public String getText() {
        return this.text;
    }

    public String getKeyIdentifier() {
        return this.keyIdentifier;
    }

    public int getWindowsVirtualKeyCode() {
        return this.windowsVirtualKeyCode;
    }

    public boolean isShiftDown() {
        return this.shift;
    }

    public boolean isCtrlDown() {
        return this.ctrl;
    }

    public boolean isAltDown() {
        return this.alt;
    }

    public boolean isMetaDown() {
        return this.meta;
    }

    public static boolean filterEvent(WCKeyEvent wCKeyEvent) {
        if (wCKeyEvent.getType() == 0) {
            String string = wCKeyEvent.getText();
            if (string == null || string.length() != 1) {
                return true;
            }
            char c2 = string.charAt(0);
            if (c2 == '\b' || c2 == '\n' || c2 == '\t' || c2 == '\uffff' || c2 == '\u0018' || c2 == '\u001b' || c2 == '\u007f') {
                return true;
            }
        }
        return false;
    }
}

