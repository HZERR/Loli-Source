/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.events.mac;

import com.sun.glass.ui.Window;
import java.util.Map;

public class NpapiEvent {
    public static final int NPCocoaEventDrawRect = 1;
    public static final int NPCocoaEventMouseDown = 2;
    public static final int NPCocoaEventMouseUp = 3;
    public static final int NPCocoaEventMouseMoved = 4;
    public static final int NPCocoaEventMouseEntered = 5;
    public static final int NPCocoaEventMouseExited = 6;
    public static final int NPCocoaEventMouseDragged = 7;
    public static final int NPCocoaEventKeyDown = 8;
    public static final int NPCocoaEventKeyUp = 9;
    public static final int NPCocoaEventFlagsChanged = 10;
    public static final int NPCocoaEventFocusChanged = 11;
    public static final int NPCocoaEventWindowFocusChanged = 12;
    public static final int NPCocoaEventScrollWheel = 13;
    public static final int NPCocoaEventTextInput = 14;

    private static native void _dispatchCocoaNpapiDrawEvent(long var0, int var2, long var3, double var5, double var7, double var9, double var11);

    private static native void _dispatchCocoaNpapiMouseEvent(long var0, int var2, int var3, double var4, double var6, int var8, int var9, double var10, double var12, double var14);

    private static native void _dispatchCocoaNpapiKeyEvent(long var0, int var2, int var3, String var4, String var5, boolean var6, int var7, boolean var8);

    private static native void _dispatchCocoaNpapiFocusEvent(long var0, int var2, boolean var3);

    private static native void _dispatchCocoaNpapiTextInputEvent(long var0, int var2, String var3);

    private static final boolean getBoolean(Map map, String string) {
        boolean bl = false;
        if (map.containsKey(string)) {
            try {
                bl = (Boolean)map.get(string);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return bl;
    }

    private static final int getInt(Map map, String string) {
        int n2 = 0;
        if (map.containsKey(string)) {
            try {
                n2 = (Integer)map.get(string);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return n2;
    }

    private static final long getLong(Map map, String string) {
        long l2 = 0L;
        if (map.containsKey(string)) {
            try {
                l2 = (Long)map.get(string);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return l2;
    }

    private static final double getDouble(Map map, String string) {
        double d2 = 0.0;
        if (map.containsKey(string)) {
            try {
                d2 = (Double)map.get(string);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return d2;
    }

    private static final String getString(Map map, String string) {
        String string2 = null;
        if (map.containsKey(string)) {
            try {
                string2 = (String)map.get(string);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return string2;
    }

    public static void dispatchCocoaNpapiEvent(Window window, Map map) {
        long l2 = window.getNativeWindow();
        int n2 = (Integer)map.get("type");
        switch (n2) {
            case 1: {
                long l3 = NpapiEvent.getLong(map, "context");
                double d2 = NpapiEvent.getDouble(map, "x");
                double d3 = NpapiEvent.getDouble(map, "y");
                double d4 = NpapiEvent.getDouble(map, "width");
                double d5 = NpapiEvent.getDouble(map, "height");
                NpapiEvent._dispatchCocoaNpapiDrawEvent(l2, n2, l3, d2, d3, d4, d5);
                break;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 13: {
                int n3 = NpapiEvent.getInt(map, "modifierFlags");
                double d6 = NpapiEvent.getDouble(map, "pluginX");
                double d7 = NpapiEvent.getDouble(map, "pluginY");
                int n4 = NpapiEvent.getInt(map, "buttonNumber");
                int n5 = NpapiEvent.getInt(map, "clickCount");
                double d8 = NpapiEvent.getDouble(map, "deltaX");
                double d9 = NpapiEvent.getDouble(map, "deltaY");
                double d10 = NpapiEvent.getDouble(map, "deltaZ");
                NpapiEvent._dispatchCocoaNpapiMouseEvent(l2, n2, n3, d6, d7, n4, n5, d8, d9, d10);
                break;
            }
            case 8: 
            case 9: 
            case 10: {
                int n6 = NpapiEvent.getInt(map, "modifierFlags");
                String string = NpapiEvent.getString(map, "characters");
                String string2 = NpapiEvent.getString(map, "charactersIgnoringModifiers");
                boolean bl = NpapiEvent.getBoolean(map, "isARepeat");
                int n7 = NpapiEvent.getInt(map, "keyCode");
                boolean bl2 = NpapiEvent.getBoolean(map, "needsKeyTyped");
                NpapiEvent._dispatchCocoaNpapiKeyEvent(l2, n2, n6, string, string2, bl, n7, bl2);
                break;
            }
            case 11: 
            case 12: {
                boolean bl = NpapiEvent.getBoolean(map, "hasFocus");
                NpapiEvent._dispatchCocoaNpapiFocusEvent(l2, n2, bl);
                break;
            }
            case 14: {
                String string = NpapiEvent.getString(map, "text");
                NpapiEvent._dispatchCocoaNpapiTextInputEvent(l2, n2, string);
            }
        }
    }
}

