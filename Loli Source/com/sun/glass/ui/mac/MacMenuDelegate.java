/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;

class MacMenuDelegate
implements MenuDelegate,
MenuItemDelegate {
    long ptr;
    private Menu menu;

    private static native void _initIDs();

    public MacMenuDelegate(Menu menu) {
        this.menu = menu;
    }

    public MacMenuDelegate() {
    }

    private native long _createMenu(String var1, boolean var2);

    @Override
    public boolean createMenu(String string, boolean bl) {
        this.ptr = this._createMenu(string, bl);
        return this.ptr != 0L;
    }

    private native long _createMenuItem(String var1, char var2, int var3, Pixels var4, boolean var5, boolean var6, MenuItem.Callback var7);

    @Override
    public boolean createMenuItem(String string, MenuItem.Callback callback, int n2, int n3, Pixels pixels, boolean bl, boolean bl2) {
        this.ptr = this._createMenuItem(string, (char)n2, n3, pixels, bl, bl2, callback);
        return this.ptr != 0L;
    }

    private native void _insert(long var1, long var3, int var5);

    @Override
    public boolean insert(MenuDelegate menuDelegate, int n2) {
        MacMenuDelegate macMenuDelegate = (MacMenuDelegate)menuDelegate;
        this._insert(this.ptr, macMenuDelegate.ptr, n2);
        return true;
    }

    @Override
    public boolean insert(MenuItemDelegate menuItemDelegate, int n2) {
        MacMenuDelegate macMenuDelegate = (MacMenuDelegate)menuItemDelegate;
        this._insert(this.ptr, macMenuDelegate != null ? macMenuDelegate.ptr : 0L, n2);
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
    public boolean remove(MenuItemDelegate menuItemDelegate, int n2) {
        MacMenuDelegate macMenuDelegate = (MacMenuDelegate)menuItemDelegate;
        this._remove(this.ptr, macMenuDelegate == null ? 0L : macMenuDelegate.ptr, n2);
        return true;
    }

    private native void _setTitle(long var1, String var3);

    @Override
    public boolean setTitle(String string) {
        this._setTitle(this.ptr, string);
        return true;
    }

    private native void _setShortcut(long var1, char var3, int var4);

    @Override
    public boolean setShortcut(int n2, int n3) {
        this._setShortcut(this.ptr, (char)n2, n3);
        return true;
    }

    private native void _setPixels(long var1, Pixels var3);

    @Override
    public boolean setPixels(Pixels pixels) {
        this._setPixels(this.ptr, pixels);
        return true;
    }

    private native void _setEnabled(long var1, boolean var3);

    @Override
    public boolean setEnabled(boolean bl) {
        this._setEnabled(this.ptr, bl);
        return true;
    }

    private native void _setChecked(long var1, boolean var3);

    @Override
    public boolean setChecked(boolean bl) {
        this._setChecked(this.ptr, bl);
        return true;
    }

    private native void _setCallback(long var1, MenuItem.Callback var3);

    @Override
    public boolean setCallback(MenuItem.Callback callback) {
        this._setCallback(this.ptr, callback);
        return true;
    }

    static {
        MacMenuDelegate._initIDs();
    }
}

