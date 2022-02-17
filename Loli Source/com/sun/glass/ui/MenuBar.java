/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MenuBar {
    private final MenuBarDelegate delegate;
    private final List<Menu> menus = new ArrayList<Menu>();

    protected MenuBar() {
        Application.checkEventThread();
        this.delegate = PlatformFactory.getPlatformFactory().createMenuBarDelegate(this);
        if (!this.delegate.createMenuBar()) {
            throw new RuntimeException("MenuBar creation error.");
        }
    }

    long getNativeMenu() {
        return this.delegate.getNativeMenu();
    }

    public void add(Menu menu) {
        Application.checkEventThread();
        this.insert(menu, this.menus.size());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void insert(Menu menu, int n2) {
        Application.checkEventThread();
        List<Menu> list = this.menus;
        synchronized (list) {
            if (this.delegate.insert(menu.getDelegate(), n2)) {
                this.menus.add(n2, menu);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void remove(int n2) {
        Application.checkEventThread();
        List<Menu> list = this.menus;
        synchronized (list) {
            Menu menu = this.menus.get(n2);
            if (this.delegate.remove(menu.getDelegate(), n2)) {
                this.menus.remove(n2);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void remove(Menu menu) {
        Application.checkEventThread();
        List<Menu> list = this.menus;
        synchronized (list) {
            int n2 = this.menus.indexOf(menu);
            if (n2 >= 0 && this.delegate.remove(menu.getDelegate(), n2)) {
                this.menus.remove(n2);
            }
        }
    }

    public List<Menu> getMenus() {
        Application.checkEventThread();
        return Collections.unmodifiableList(this.menus);
    }
}

