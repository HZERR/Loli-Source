/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;

final class MacRobot
extends Robot {
    private long ptr;

    MacRobot() {
    }

    private native long _init();

    @Override
    protected void _create() {
        this.ptr = this._init();
    }

    private native void _destroy(long var1);

    @Override
    protected void _destroy() {
        if (this.ptr == 0L) {
            return;
        }
        this._destroy(this.ptr);
    }

    @Override
    protected native void _keyPress(int var1);

    @Override
    protected native void _keyRelease(int var1);

    private native void _mouseMove(long var1, int var3, int var4);

    @Override
    protected void _mouseMove(int n2, int n3) {
        if (this.ptr == 0L) {
            return;
        }
        this._mouseMove(this.ptr, n2, n3);
    }

    private native void _mousePress(long var1, int var3);

    @Override
    protected void _mousePress(int n2) {
        if (this.ptr == 0L) {
            return;
        }
        this._mousePress(this.ptr, n2);
    }

    private native void _mouseRelease(long var1, int var3);

    @Override
    protected void _mouseRelease(int n2) {
        if (this.ptr == 0L) {
            return;
        }
        this._mouseRelease(this.ptr, n2);
    }

    @Override
    protected native void _mouseWheel(int var1);

    private native int _getMouseX(long var1);

    @Override
    protected int _getMouseX() {
        if (this.ptr == 0L) {
            return 0;
        }
        return this._getMouseX(this.ptr);
    }

    private native int _getMouseY(long var1);

    @Override
    protected int _getMouseY() {
        if (this.ptr == 0L) {
            return 0;
        }
        return this._getMouseY(this.ptr);
    }

    @Override
    protected native int _getPixelColor(int var1, int var2);

    @Override
    protected native Pixels _getScreenCapture(int var1, int var2, int var3, int var4, boolean var5);
}

