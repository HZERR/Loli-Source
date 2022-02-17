/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.InvokeLaterDispatcher;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Robot;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Size;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.glass.ui.mac.MacAccessible;
import com.sun.glass.ui.mac.MacCommonDialogs;
import com.sun.glass.ui.mac.MacCursor;
import com.sun.glass.ui.mac.MacPixels;
import com.sun.glass.ui.mac.MacRobot;
import com.sun.glass.ui.mac.MacTimer;
import com.sun.glass.ui.mac.MacView;
import com.sun.glass.ui.mac.MacWindow;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;

final class MacApplication
extends Application
implements InvokeLaterDispatcher.InvokeLaterSubmitter {
    private boolean isTaskbarApplication = false;
    private final InvokeLaterDispatcher invokeLaterDispatcher;
    private Menu appleMenu;
    static final long BROWSER_PARENT_ID = -1L;

    private static native void _initIDs(boolean var0);

    static native int _getMacKey(int var0);

    MacApplication() {
        boolean bl = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.embed.isEventThread"));
        if (!bl) {
            this.invokeLaterDispatcher = new InvokeLaterDispatcher(this);
            this.invokeLaterDispatcher.start();
        } else {
            this.invokeLaterDispatcher = null;
        }
    }

    native void _runLoop(ClassLoader var1, Runnable var2, boolean var3);

    @Override
    protected void runLoop(Runnable runnable) {
        this.isTaskbarApplication = AccessController.doPrivileged(() -> {
            String string = System.getProperty("glass.taskbarApplication");
            return !"false".equalsIgnoreCase(string);
        });
        ClassLoader classLoader = MacApplication.class.getClassLoader();
        this._runLoop(classLoader, runnable, this.isTaskbarApplication);
    }

    private native void _finishTerminating();

    @Override
    protected void finishTerminating() {
        this._finishTerminating();
        super.finishTerminating();
    }

    private void notifyApplicationDidTerminate() {
        MacApplication.setEventThread(null);
    }

    private void setEventThread() {
        MacApplication.setEventThread(Thread.currentThread());
    }

    private native Object _enterNestedEventLoopImpl();

    @Override
    protected Object _enterNestedEventLoop() {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
        }
        try {
            Object object = this._enterNestedEventLoopImpl();
            return object;
        }
        finally {
            if (this.invokeLaterDispatcher != null) {
                this.invokeLaterDispatcher.notifyLeftNestedEventLoop();
            }
        }
    }

    private native void _leaveNestedEventLoopImpl(Object var1);

    @Override
    protected void _leaveNestedEventLoop(Object object) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyLeavingNestedEventLoop();
        }
        this._leaveNestedEventLoopImpl(object);
    }

    private native void _hide();

    private native void _hideOtherApplications();

    private native void _unhideAllApplications();

    public void installAppleMenu(MenuBar menuBar) {
        this.appleMenu = this.createMenu("Apple");
        MenuItem menuItem = this.createMenuItem("Hide " + this.getName(), new MenuItem.Callback(){

            @Override
            public void action() {
                MacApplication.this._hide();
            }

            @Override
            public void validate() {
            }
        }, 104, 16);
        this.appleMenu.add(menuItem);
        MenuItem menuItem2 = this.createMenuItem("Hide Others", new MenuItem.Callback(){

            @Override
            public void action() {
                MacApplication.this._hideOtherApplications();
            }

            @Override
            public void validate() {
            }
        }, 104, 24);
        this.appleMenu.add(menuItem2);
        MenuItem menuItem3 = this.createMenuItem("Show All", new MenuItem.Callback(){

            @Override
            public void action() {
                MacApplication.this._unhideAllApplications();
            }

            @Override
            public void validate() {
            }
        });
        this.appleMenu.add(menuItem3);
        this.appleMenu.add(MenuItem.Separator);
        MenuItem menuItem4 = this.createMenuItem("Quit " + this.getName(), new MenuItem.Callback(){

            @Override
            public void action() {
                Application.EventHandler eventHandler = MacApplication.this.getEventHandler();
                if (eventHandler != null) {
                    eventHandler.handleQuitAction(Application.GetApplication(), System.nanoTime());
                }
            }

            @Override
            public void validate() {
            }
        }, 113, 16);
        this.appleMenu.add(menuItem4);
        menuBar.add(this.appleMenu);
    }

    public Menu getAppleMenu() {
        return this.appleMenu;
    }

    @Override
    public void installDefaultMenus(MenuBar menuBar) {
        this.installAppleMenu(menuBar);
    }

    @Override
    public Window createWindow(Window window, Screen screen, int n2) {
        return new MacWindow(window, screen, n2);
    }

    @Override
    public Window createWindow(long l2) {
        MacWindow macWindow = new MacWindow(l2);
        if (l2 == -1L) {
            macWindow.setView(this.createView());
        }
        return macWindow;
    }

    @Override
    public View createView() {
        return new MacView();
    }

    @Override
    public Cursor createCursor(int n2) {
        return new MacCursor(n2);
    }

    @Override
    public Cursor createCursor(int n2, int n3, Pixels pixels) {
        return new MacCursor(n2, n3, pixels);
    }

    @Override
    protected void staticCursor_setVisible(boolean bl) {
        MacCursor.setVisible_impl(bl);
    }

    @Override
    protected Size staticCursor_getBestSize(int n2, int n3) {
        return MacCursor.getBestSize_impl(n2, n3);
    }

    @Override
    public Pixels createPixels(int n2, int n3, ByteBuffer byteBuffer) {
        return new MacPixels(n2, n3, byteBuffer);
    }

    @Override
    public Pixels createPixels(int n2, int n3, IntBuffer intBuffer) {
        return new MacPixels(n2, n3, intBuffer);
    }

    @Override
    public Pixels createPixels(int n2, int n3, IntBuffer intBuffer, float f2) {
        return new MacPixels(n2, n3, intBuffer, f2);
    }

    @Override
    protected int staticPixels_getNativeFormat() {
        return MacPixels.getNativeFormat_impl();
    }

    @Override
    public Robot createRobot() {
        return new MacRobot();
    }

    @Override
    protected native double staticScreen_getVideoRefreshPeriod();

    @Override
    protected native Screen[] staticScreen_getScreens();

    @Override
    public Timer createTimer(Runnable runnable) {
        return new MacTimer(runnable);
    }

    @Override
    protected int staticTimer_getMinPeriod() {
        return MacTimer.getMinPeriod_impl();
    }

    @Override
    protected int staticTimer_getMaxPeriod() {
        return MacTimer.getMaxPeriod_impl();
    }

    @Override
    public Accessible createAccessible() {
        return new MacAccessible();
    }

    @Override
    protected CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window window, String string, String string2, String string3, int n2, boolean bl, CommonDialogs.ExtensionFilter[] arrextensionFilter, int n3) {
        return MacCommonDialogs.showFileChooser_impl(window, string, string2, string3, n2, bl, arrextensionFilter, n3);
    }

    @Override
    protected File staticCommonDialogs_showFolderChooser(Window window, String string, String string2) {
        return MacCommonDialogs.showFolderChooser_impl(window, string, string2);
    }

    @Override
    protected long staticView_getMultiClickTime() {
        return MacView.getMultiClickTime_impl();
    }

    @Override
    protected int staticView_getMultiClickMaxX() {
        return MacView.getMultiClickMaxX_impl();
    }

    @Override
    protected int staticView_getMultiClickMaxY() {
        return MacView.getMultiClickMaxY_impl();
    }

    @Override
    protected native void _invokeAndWait(Runnable var1);

    private native void _submitForLaterInvocation(Runnable var1);

    @Override
    public void submitForLaterInvocation(Runnable runnable) {
        this._submitForLaterInvocation(runnable);
    }

    @Override
    protected void _invokeLater(Runnable runnable) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.invokeLater(runnable);
        } else {
            this.submitForLaterInvocation(runnable);
        }
    }

    @Override
    protected boolean _supportsInputMethods() {
        return true;
    }

    @Override
    protected boolean _supportsTransparentWindows() {
        return true;
    }

    @Override
    protected boolean _supportsUnifiedWindows() {
        return true;
    }

    @Override
    protected native boolean _supportsSystemMenu();

    protected native String _getRemoteLayerServerName();

    public String getRemoteLayerServerName() {
        return this._getRemoteLayerServerName();
    }

    private native String _getDataDirectory();

    @Override
    public String getDataDirectory() {
        MacApplication.checkEventThread();
        String string = this._getDataDirectory();
        if (string == null || string.length() == 0) {
            return super.getDataDirectory();
        }
        return string + File.separator + this.name + File.separator;
    }

    @Override
    protected native int _getKeyCodeForChar(char var1);

    static {
        AccessController.doPrivileged(() -> {
            Application.loadNativeLibrary();
            return null;
        });
        boolean bl = AccessController.doPrivileged(() -> Boolean.getBoolean("glass.disableSyncRendering"));
        MacApplication._initIDs(bl);
    }
}

