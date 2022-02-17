/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.events.mac.NpapiEvent;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.glass.ui.mac.MacCursor;
import java.util.Map;

final class MacWindow
extends Window {
    private static native void _initIDs();

    protected MacWindow(Window window, Screen screen, int n2) {
        super(window, screen, n2);
        this.setPlatformScale(screen.getUIScale());
        this.setRenderScale(screen.getRenderScale());
    }

    protected MacWindow(long l2) {
        super(l2);
    }

    @Override
    protected void setScreen(Screen screen) {
        this.setRenderScale(screen.getUIScale());
        this.setRenderScale(screen.getRenderScale());
        super.setScreen(screen);
    }

    @Override
    public float getOutputScale() {
        return this.getRenderScale();
    }

    @Override
    protected native long _createWindow(long var1, long var3, int var5);

    @Override
    protected native long _createChildWindow(long var1);

    @Override
    protected native boolean _close(long var1);

    @Override
    protected native boolean _setView(long var1, View var3);

    @Override
    protected native boolean _setMenubar(long var1, long var3);

    @Override
    protected native boolean _minimize(long var1, boolean var3);

    @Override
    protected native boolean _maximize(long var1, boolean var3, boolean var4);

    @Override
    protected native void _setBounds(long var1, int var3, int var4, boolean var5, boolean var6, int var7, int var8, int var9, int var10, float var11, float var12);

    @Override
    protected native boolean _setVisible(long var1, boolean var3);

    @Override
    protected native boolean _setResizable(long var1, boolean var3);

    private native boolean _requestFocus(long var1);

    @Override
    protected boolean _requestFocus(long l2, int n2) {
        if (n2 != 541) {
            return this._requestFocus(l2);
        }
        return false;
    }

    @Override
    protected native void _setFocusable(long var1, boolean var3);

    @Override
    protected native boolean _setTitle(long var1, String var3);

    @Override
    protected native void _setLevel(long var1, int var3);

    @Override
    protected native void _setAlpha(long var1, float var3);

    @Override
    protected native boolean _setBackground(long var1, float var3, float var4, float var5);

    @Override
    protected native void _setEnabled(long var1, boolean var3);

    @Override
    protected native boolean _setMinimumSize(long var1, int var3, int var4);

    @Override
    protected native boolean _setMaximumSize(long var1, int var3, int var4);

    @Override
    protected native void _setIcon(long var1, Pixels var3);

    @Override
    protected native void _toFront(long var1);

    @Override
    protected native void _toBack(long var1);

    @Override
    protected native void _enterModal(long var1);

    @Override
    protected native void _enterModalWithWindow(long var1, long var3);

    @Override
    protected native void _exitModal(long var1);

    @Override
    protected native boolean _grabFocus(long var1);

    @Override
    protected native void _ungrabFocus(long var1);

    @Override
    protected native int _getEmbeddedX(long var1);

    @Override
    protected native int _getEmbeddedY(long var1);

    @Override
    protected void _setCursor(long l2, Cursor cursor) {
        ((MacCursor)cursor).set();
    }

    @Override
    public void dispatchNpapiEvent(Map map) {
        NpapiEvent.dispatchCocoaNpapiEvent(this, map);
    }

    @Override
    protected void _requestInput(long l2, String string, int n2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void _releaseInput(long l2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    static {
        MacWindow._initIDs();
    }
}

