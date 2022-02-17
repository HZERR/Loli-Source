/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.mac.MacMenuDelegate;

class MacMenuBarDelegate
implements MenuBarDelegate {
    long ptr;

    MacMenuBarDelegate() {
    }

    private native long _createMenuBar();

    @Override
    public boolean createMenuBar() {
        this.ptr = this._createMenuBar();
        return this.ptr != 0L;
    }

    private native void _insert(long var1, long var3, int var5);

    @Override
    public boolean insert(MenuDelegate menuDelegate, int n2) {
        MacMenuDelegate macMenuDelegate = (MacMenuDelegate)menuDelegate;
        this._insert(this.ptr, macMenuDelegate.ptr, n2);
        return true;
    }

    private native void _remove(long var1, long var3, int var5);

    @Override
    public boolean remove(MenuDelegate menuDelegate, int n2) {
        MacMenuDelegate macMenuDelegate = (MacMenuDelegate)menuDelegate;
        this._remove(this.ptr, macMenuDelegate.ptr, n2);
        return true;
    }

    @Override
    public long getNativeMenu() {
        return this.ptr;
    }
}

