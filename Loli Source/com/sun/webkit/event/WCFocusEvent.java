/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.event;

public final class WCFocusEvent {
    public static final int WINDOW_ACTIVATED = 0;
    public static final int WINDOW_DEACTIVATED = 1;
    public static final int FOCUS_GAINED = 2;
    public static final int FOCUS_LOST = 3;
    public static final int UNKNOWN = -1;
    public static final int FORWARD = 0;
    public static final int BACKWARD = 1;
    private final int id;
    private final int direction;

    public WCFocusEvent(int n2, int n3) {
        this.id = n2;
        this.direction = n3;
    }

    public int getID() {
        return this.id;
    }

    public int getDirection() {
        return this.direction;
    }

    public String toString() {
        return "WCFocusEvent(" + this.id + ", " + this.direction + ")";
    }
}

