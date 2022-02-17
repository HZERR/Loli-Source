/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.webkit.drt;

import com.sun.javafx.webkit.KeyCodeMap;
import com.sun.webkit.WebPage;
import com.sun.webkit.event.WCKeyEvent;
import com.sun.webkit.event.WCMouseEvent;
import com.sun.webkit.event.WCMouseWheelEvent;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.input.KeyCode;

final class EventSender {
    private static final int ALT = 1;
    private static final int CTRL = 2;
    private static final int META = 4;
    private static final int SHIFT = 8;
    private static final int PRESSED = 16;
    private static final float ZOOM = 1.2f;
    private static final float SCROLL = 40.0f;
    private static final Map<Object, KeyCode> MAP = new HashMap<Object, KeyCode>();
    private final WebPage webPage;
    private boolean dragMode = true;
    private int mousePositionX;
    private int mousePositionY;
    private boolean mousePressed;
    private int mouseButton = 0;
    private long timeOffset;
    private int modifiers;

    EventSender(WebPage webPage) {
        this.webPage = webPage;
    }

    private void keyDown(String string, int n2) {
        String string2 = null;
        KeyCode keyCode = MAP.get(string);
        if (1 == string.length()) {
            if (keyCode == null) {
                keyCode = MAP.get(Character.toUpperCase(string.charAt(0)));
            }
            string2 = string;
        }
        if (keyCode == null) {
            System.err.println("unexpected key = " + string);
        } else {
            KeyCodeMap.Entry entry = KeyCodeMap.lookup(keyCode);
            String string3 = entry.getKeyIdentifier();
            int n3 = entry.getWindowsVirtualKeyCode();
            this.dispatchKeyEvent(1, null, string3, n3, n2);
            this.dispatchKeyEvent(0, string2, null, 0, n2);
            this.dispatchKeyEvent(2, null, string3, n3, n2);
        }
    }

    private void mouseUpDown(int n2, int n3) {
        this.mouseButton = n2;
        this.mousePressed = EventSender.isSet(n3, 16);
        this.dispatchMouseEvent(this.mousePressed ? 0 : 1, n2, 1, n3);
    }

    private void mouseMoveTo(int n2, int n3) {
        this.mousePositionX = n2;
        this.mousePositionY = n3;
        this.dispatchMouseEvent(this.mousePressed ? 3 : 2, this.mousePressed ? this.mouseButton : 0, 0, 0);
    }

    private void mouseScroll(float f2, float f3, boolean bl) {
        if (bl) {
            f2 /= 40.0f;
            f3 /= 40.0f;
        }
        this.webPage.dispatchMouseWheelEvent(new WCMouseWheelEvent(this.mousePositionX, this.mousePositionY, this.mousePositionX, this.mousePositionY, this.getEventTime(), false, false, false, false, f2, f3));
    }

    private void leapForward(int n2) {
        this.timeOffset += (long)n2;
    }

    private void contextClick() {
        this.dispatchMouseEvent(0, 2, 1, 0);
        this.dispatchMouseEvent(1, 2, 1, 0);
    }

    private void scheduleAsynchronousClick() {
        this.dispatchMouseEvent(0, 1, 1, 0);
        this.dispatchMouseEvent(1, 1, 1, 0);
    }

    private void touchStart() {
        throw new UnsupportedOperationException("touchStart");
    }

    private void touchCancel() {
        throw new UnsupportedOperationException("touchCancel");
    }

    private void touchMove() {
        throw new UnsupportedOperationException("touchMove");
    }

    private void touchEnd() {
        throw new UnsupportedOperationException("touchEnd");
    }

    private void addTouchPoint(int n2, int n3) {
        throw new UnsupportedOperationException("addTouchPoint");
    }

    private void updateTouchPoint(int n2, int n3, int n4) {
        throw new UnsupportedOperationException("updateTouchPoint");
    }

    private void cancelTouchPoint(int n2) {
        throw new UnsupportedOperationException("cancelTouchPoint");
    }

    private void releaseTouchPoint(int n2) {
        throw new UnsupportedOperationException("releaseTouchPoint");
    }

    private void clearTouchPoints() {
        throw new UnsupportedOperationException("clearTouchPoints");
    }

    private void setTouchModifier(int n2, boolean bl) {
        this.modifiers = bl ? this.modifiers | n2 : this.modifiers & ~n2;
    }

    private void scalePageBy(float f2, int n2, int n3) {
        throw new UnsupportedOperationException("scalePageBy(" + f2 + "); x=" + n2 + "; y=" + n3);
    }

    private void zoom(boolean bl, boolean bl2) {
        float f2 = this.webPage.getZoomFactor(bl2);
        this.webPage.setZoomFactor(bl ? f2 * 1.2f : f2 / 1.2f, bl2);
    }

    private void beginDragWithFiles(String[] arrstring) {
        StringBuilder stringBuilder = new StringBuilder("beginDragWithFiles");
        for (String string : arrstring) {
            stringBuilder.append(", ").append(string);
        }
        throw new UnsupportedOperationException(stringBuilder.append('.').toString());
    }

    private boolean getDragMode() {
        return this.dragMode;
    }

    private void setDragMode(boolean bl) {
        this.dragMode = bl;
    }

    private long getEventTime() {
        return this.timeOffset + System.currentTimeMillis();
    }

    private void dispatchKeyEvent(int n2, String string, String string2, int n3, int n4) {
        this.webPage.dispatchKeyEvent(new WCKeyEvent(n2, string, string2, n3, EventSender.isSet(n4, 8), EventSender.isSet(n4, 2), EventSender.isSet(n4, 1), EventSender.isSet(n4, 4), this.getEventTime()));
    }

    private void dispatchMouseEvent(int n2, int n3, int n4, int n5) {
        this.webPage.dispatchMouseEvent(new WCMouseEvent(n2, n3, n4, this.mousePositionX, this.mousePositionY, this.mousePositionX, this.mousePositionY, this.getEventTime(), EventSender.isSet(n5, 8), EventSender.isSet(n5, 2), EventSender.isSet(n5, 1), EventSender.isSet(n5, 4), false));
    }

    private static boolean isSet(int n2, int n3) {
        return n3 == (n3 & n2);
    }

    static {
        MAP.put("\r", KeyCode.ENTER);
        MAP.put("pageUp", KeyCode.PAGE_UP);
        MAP.put("pageDown", KeyCode.PAGE_DOWN);
        MAP.put("leftArrow", KeyCode.LEFT);
        MAP.put("upArrow", KeyCode.UP);
        MAP.put("rightArrow", KeyCode.RIGHT);
        MAP.put("downArrow", KeyCode.DOWN);
        MAP.put("printScreen", KeyCode.PRINTSCREEN);
        MAP.put("menu", KeyCode.CONTEXT_MENU);
        for (KeyCode keyCode : KeyCode.values()) {
            MAP.put(keyCode.impl_getCode(), keyCode);
            MAP.put(keyCode.getName().toLowerCase(), keyCode);
            MAP.put(keyCode.getName(), keyCode);
        }
    }
}

