/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import com.sun.glass.ui.mac.MacApplication;
import com.sun.glass.ui.mac.MacClipboardDelegate;
import com.sun.glass.ui.mac.MacMenuBarDelegate;
import com.sun.glass.ui.mac.MacMenuDelegate;

public final class MacPlatformFactory
extends PlatformFactory {
    @Override
    public Application createApplication() {
        return new MacApplication();
    }

    @Override
    public MenuBarDelegate createMenuBarDelegate(MenuBar menuBar) {
        return new MacMenuBarDelegate();
    }

    @Override
    public MenuDelegate createMenuDelegate(Menu menu) {
        return new MacMenuDelegate(menu);
    }

    @Override
    public MenuItemDelegate createMenuItemDelegate(MenuItem menuItem) {
        return new MacMenuDelegate();
    }

    @Override
    public ClipboardDelegate createClipboardDelegate() {
        return new MacClipboardDelegate();
    }
}

