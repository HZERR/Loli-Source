/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.ScalerFactory;
import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.quantum.GlassAppletWindow;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.GlassStage;
import com.sun.javafx.tk.quantum.GlassSystemMenu;
import com.sun.javafx.tk.quantum.GlassWindowEventHandler;
import com.sun.javafx.tk.quantum.OverlayWarning;
import com.sun.javafx.tk.quantum.PixelUtils;
import com.sun.javafx.tk.quantum.QuantumRenderer;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.ViewPainter;
import com.sun.javafx.tk.quantum.ViewScene;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.Permission;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

class WindowStage
extends GlassStage {
    protected com.sun.glass.ui.Window platformWindow;
    protected Stage fxStage;
    private StageStyle style;
    private GlassStage owner = null;
    private Modality modality = Modality.NONE;
    private final boolean securityDialog;
    private OverlayWarning warning = null;
    private boolean rtl = false;
    private boolean transparent = false;
    private boolean isPrimaryStage = false;
    private boolean isAppletStage = false;
    private boolean isPopupStage = false;
    private boolean isInFullScreen = false;
    private boolean inAllowedEventHandler = false;
    private static List<WindowStage> activeWindows = new LinkedList<WindowStage>();
    private static Map<com.sun.glass.ui.Window, WindowStage> platformWindows = new HashMap<com.sun.glass.ui.Window, WindowStage>();
    private static GlassAppletWindow appletWindow = null;
    private static final Locale LOCALE = Locale.getDefault();
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(WindowStage.class.getPackage().getName() + ".QuantumMessagesBundle", LOCALE);
    private static final Permission fullScreenPermission = new AllPermission();
    private static final Permission alwaysOnTopPermission = new AllPermission();
    private boolean fullScreenFromUserEvent = false;
    private KeyCombination savedFullScreenExitKey = null;

    static void setAppletWindow(GlassAppletWindow glassAppletWindow) {
        appletWindow = glassAppletWindow;
    }

    static GlassAppletWindow getAppletWindow() {
        return appletWindow;
    }

    public WindowStage(Window window, boolean bl, StageStyle stageStyle, Modality modality, TKStage tKStage) {
        this.style = stageStyle;
        this.owner = (GlassStage)tKStage;
        this.modality = modality;
        this.securityDialog = bl;
        this.fxStage = window instanceof Stage ? (Stage)window : null;
        boolean bl2 = this.transparent = stageStyle == StageStyle.TRANSPARENT;
        if (tKStage == null && this.modality == Modality.WINDOW_MODAL) {
            this.modality = Modality.NONE;
        }
    }

    final void setIsPrimary() {
        this.isPrimaryStage = true;
        if (appletWindow != null) {
            this.isAppletStage = true;
        }
    }

    final void setIsPopup() {
        this.isPopupStage = true;
    }

    final boolean isSecurityDialog() {
        return this.securityDialog;
    }

    public final WindowStage init(GlassSystemMenu glassSystemMenu) {
        this.initPlatformWindow();
        this.platformWindow.setEventHandler(new GlassWindowEventHandler(this));
        if (glassSystemMenu.isSupported()) {
            glassSystemMenu.createMenuBar();
            this.platformWindow.setMenuBar(glassSystemMenu.getMenuBar());
        }
        return this;
    }

    private void initPlatformWindow() {
        if (this.platformWindow == null) {
            Application application = Application.GetApplication();
            if (this.isPrimaryStage && null != appletWindow) {
                this.platformWindow = application.createWindow(appletWindow.getGlassWindow().getNativeWindow());
            } else {
                int n2;
                com.sun.glass.ui.Window window = null;
                if (this.owner instanceof WindowStage) {
                    window = ((WindowStage)this.owner).platformWindow;
                }
                boolean bl = false;
                boolean bl2 = true;
                int n3 = n2 = this.rtl ? 128 : 0;
                if (this.isPopupStage) {
                    n2 |= 8;
                    if (this.style == StageStyle.TRANSPARENT) {
                        n2 |= 2;
                    }
                    bl2 = false;
                } else {
                    switch (this.style) {
                        case UNIFIED: {
                            if (application.supportsUnifiedWindows()) {
                                n2 |= 0x100;
                            }
                        }
                        case DECORATED: {
                            n2 |= 0x71;
                            if (window != null || this.modality != Modality.NONE) {
                                n2 &= 0xFFFFFF9F;
                            }
                            bl = true;
                            break;
                        }
                        case UTILITY: {
                            n2 |= 0x15;
                            break;
                        }
                        default: {
                            n2 |= (this.transparent ? 2 : 0) | 0x10;
                        }
                    }
                }
                this.platformWindow = application.createWindow(window, Screen.getMainScreen(), n2);
                this.platformWindow.setResizable(bl);
                this.platformWindow.setFocusable(bl2);
                if (this.securityDialog) {
                    this.platformWindow.setLevel(2);
                }
            }
        }
        platformWindows.put(this.platformWindow, this);
    }

    final com.sun.glass.ui.Window getPlatformWindow() {
        return this.platformWindow;
    }

    static WindowStage findWindowStage(com.sun.glass.ui.Window window) {
        return platformWindows.get(window);
    }

    protected GlassStage getOwner() {
        return this.owner;
    }

    protected ViewScene getViewScene() {
        return (ViewScene)this.getScene();
    }

    StageStyle getStyle() {
        return this.style;
    }

    @Override
    public TKScene createTKScene(boolean bl, boolean bl2, AccessControlContext accessControlContext) {
        ViewScene viewScene = new ViewScene(bl, bl2);
        viewScene.setSecurityContext(accessControlContext);
        return viewScene;
    }

    @Override
    public void setScene(TKScene tKScene) {
        Object object;
        GlassScene glassScene = this.getScene();
        if (glassScene == tKScene) {
            return;
        }
        this.exitFullScreen();
        super.setScene(tKScene);
        if (tKScene != null) {
            object = this.getViewScene();
            View view = ((GlassScene)object).getPlatformView();
            QuantumToolkit.runWithRenderLock(() -> this.lambda$setScene$388(view, glassScene, (GlassScene)object));
            this.requestFocus();
        } else {
            QuantumToolkit.runWithRenderLock(() -> {
                if (this.platformWindow != null) {
                    this.platformWindow.setView(null);
                }
                if (glassScene != null) {
                    glassScene.updateSceneState();
                }
                return null;
            });
        }
        if (glassScene != null) {
            object = ((ViewScene)glassScene).getPainter();
            QuantumRenderer.getInstance().disposePresentable(((ViewPainter)object).presentable);
        }
    }

    @Override
    public void setBounds(float f2, float f3, boolean bl, boolean bl2, float f4, float f5, float f6, float f7, float f8, float f9) {
        int n2;
        int n3;
        if (this.isAppletStage) {
            bl2 = false;
            bl = false;
        }
        float f10 = this.platformWindow.getPlatformScale();
        if (bl || bl2) {
            float f11;
            float f12;
            Screen screen = this.platformWindow.getScreen();
            List<Screen> list = Screen.getScreens();
            if (list.size() > 1) {
                f12 = bl ? f2 : (float)this.platformWindow.getX();
                f11 = bl2 ? f3 : (float)this.platformWindow.getY();
                float f13 = (float)screen.getX() + (float)screen.getWidth() / 2.0f - f12;
                float f14 = (float)screen.getY() + (float)screen.getHeight() / 2.0f - f11;
                float f15 = f13 * f13 + f14 * f14;
                for (Screen screen2 : Screen.getScreens()) {
                    f13 = (float)screen2.getX() + (float)screen2.getWidth() / 2.0f - f12;
                    float f16 = f13 * f13 + (f14 = (float)screen2.getY() + (float)screen2.getHeight() / 2.0f - f11) * f14;
                    if (!(f16 < f15)) continue;
                    screen = screen2;
                    f15 = f16;
                }
            }
            f12 = screen == null ? 0.0f : (float)screen.getX();
            f11 = screen == null ? 0.0f : (float)screen.getY();
            n3 = bl ? Math.round(f12 + (f2 - f12) * f10) : 0;
            n2 = bl2 ? Math.round(f11 + (f3 - f11) * f10) : 0;
        } else {
            n2 = 0;
            n3 = 0;
        }
        int n4 = (int)(f4 > 0.0f ? Math.ceil(f4 * f10) : (double)f4);
        int n5 = (int)(f5 > 0.0f ? Math.ceil(f5 * f10) : (double)f5);
        int n6 = (int)(f6 > 0.0f ? Math.ceil(f6 * f10) : (double)f6);
        int n7 = (int)(f7 > 0.0f ? Math.ceil(f7 * f10) : (double)f7);
        this.platformWindow.setBounds(n3, n2, bl, bl2, n4, n5, n6, n7, f8, f9);
    }

    @Override
    public float getUIScale() {
        return this.platformWindow.getPlatformScale();
    }

    @Override
    public float getRenderScale() {
        return this.platformWindow.getRenderScale();
    }

    @Override
    public void setMinimumSize(int n2, int n3) {
        float f2 = this.platformWindow.getPlatformScale();
        n2 = (int)Math.ceil((float)n2 * f2);
        n3 = (int)Math.ceil((float)n3 * f2);
        this.platformWindow.setMinimumSize(n2, n3);
    }

    @Override
    public void setMaximumSize(int n2, int n3) {
        float f2 = this.platformWindow.getPlatformScale();
        n2 = (int)Math.ceil((float)n2 * f2);
        n3 = (int)Math.ceil((float)n3 * f2);
        this.platformWindow.setMaximumSize(n2, n3);
    }

    static Image findBestImage(List list, int n2, int n3) {
        Image image = null;
        double d2 = 3.0;
        for (Object e2 : list) {
            double d3;
            int n4;
            int n5;
            Image image2 = (Image)e2;
            if (image2 == null || image2.getPixelFormat() != PixelFormat.BYTE_RGB && image2.getPixelFormat() != PixelFormat.BYTE_BGRA_PRE && image2.getPixelFormat() != PixelFormat.BYTE_GRAY) continue;
            int n6 = image2.getWidth();
            int n7 = image2.getHeight();
            if (n6 <= 0 || n7 <= 0) continue;
            double d4 = Math.min((double)n2 / (double)n6, (double)n3 / (double)n7);
            double d5 = 1.0;
            if (d4 >= 2.0) {
                d4 = Math.floor(d4);
                n5 = n6 * (int)d4;
                n4 = n7 * (int)d4;
                d5 = 1.0 - 0.5 / d4;
            } else if (d4 >= 1.0) {
                d4 = 1.0;
                n5 = n6;
                n4 = n7;
                d5 = 0.0;
            } else if (d4 >= 0.75) {
                d4 = 0.75;
                n5 = n6 * 3 / 4;
                n4 = n7 * 3 / 4;
                d5 = 0.3;
            } else if (d4 >= 0.6666) {
                d4 = 0.6666;
                n5 = n6 * 2 / 3;
                n4 = n7 * 2 / 3;
                d5 = 0.33;
            } else {
                d3 = Math.ceil(1.0 / d4);
                d4 = 1.0 / d3;
                n5 = (int)Math.round((double)n6 / d3);
                n4 = (int)Math.round((double)n7 / d3);
                d5 = 1.0 - 1.0 / d3;
            }
            d3 = ((double)n2 - (double)n5) / (double)n2 + ((double)n3 - (double)n4) / (double)n3 + d5;
            if (d3 < d2) {
                d2 = d3;
                image = image2;
            }
            if (d3 != 0.0) continue;
            break;
        }
        return image;
    }

    @Override
    public void setIcons(List list) {
        int n2 = 32;
        int n3 = 32;
        if (PlatformUtil.isMac()) {
            n2 = 128;
            n3 = 128;
        } else if (PlatformUtil.isWindows()) {
            n2 = 32;
            n3 = 32;
        } else if (PlatformUtil.isLinux()) {
            n2 = 128;
            n3 = 128;
        }
        if (list == null || list.size() < 1) {
            this.platformWindow.setIcon(null);
            return;
        }
        Image image = WindowStage.findBestImage(list, n3, n2);
        if (image == null) {
            return;
        }
        PushbroomScaler pushbroomScaler = ScalerFactory.createScaler(image.getWidth(), image.getHeight(), image.getBytesPerPixelUnit(), n3, n2, true);
        ByteBuffer byteBuffer = (ByteBuffer)image.getPixelBuffer();
        byte[] arrby = new byte[byteBuffer.limit()];
        int n4 = image.getHeight();
        for (int i2 = 0; i2 < n4; ++i2) {
            byteBuffer.position(i2 * image.getScanlineStride());
            byteBuffer.get(arrby, 0, image.getScanlineStride());
            pushbroomScaler.putSourceScanline(arrby, 0);
        }
        byteBuffer.rewind();
        Image image2 = image.iconify(pushbroomScaler.getDestination(), n3, n2);
        this.platformWindow.setIcon(PixelUtils.imageToPixels(image2));
    }

    @Override
    public void setTitle(String string) {
        this.platformWindow.setTitle(string);
    }

    @Override
    public void setVisible(boolean bl) {
        if (!bl) {
            WindowStage.removeActiveWindow(this);
            if (this.modality == Modality.WINDOW_MODAL) {
                if (this.owner != null && this.owner instanceof WindowStage) {
                    ((WindowStage)this.owner).setEnabled(true);
                }
            } else if (this.modality == Modality.APPLICATION_MODAL) {
                this.windowsSetEnabled(true);
            } else if (!this.isPopupStage && this.owner != null && this.owner instanceof WindowStage) {
                WindowStage windowStage = (WindowStage)this.owner;
                windowStage.requestToFront();
            }
        }
        QuantumToolkit.runWithRenderLock(() -> {
            if (this.platformWindow != null) {
                this.platformWindow.setVisible(bl);
            }
            super.setVisible(bl);
            return null;
        });
        if (bl) {
            if (this.modality == Modality.WINDOW_MODAL) {
                if (this.owner != null && this.owner instanceof WindowStage) {
                    ((WindowStage)this.owner).setEnabled(false);
                }
            } else if (this.modality == Modality.APPLICATION_MODAL) {
                this.windowsSetEnabled(false);
            }
            if (this.isAppletStage && null != appletWindow) {
                appletWindow.assertStageOrder();
            }
        }
        this.applyFullScreen();
    }

    @Override
    boolean isVisible() {
        return this.platformWindow.isVisible();
    }

    @Override
    public void setOpacity(float f2) {
        this.platformWindow.setAlpha(f2);
        GlassScene glassScene = this.getScene();
        if (glassScene != null) {
            glassScene.entireSceneNeedsRepaint();
        }
    }

    public boolean needsUpdateWindow() {
        return this.transparent && Application.GetApplication().shouldUpdateWindow();
    }

    @Override
    public void setIconified(boolean bl) {
        if (this.platformWindow.isMinimized() == bl) {
            return;
        }
        this.platformWindow.minimize(bl);
    }

    @Override
    public void setMaximized(boolean bl) {
        if (this.platformWindow.isMaximized() == bl) {
            return;
        }
        this.platformWindow.maximize(bl);
    }

    @Override
    public void setAlwaysOnTop(boolean bl) {
        if (this.securityDialog) {
            return;
        }
        if (bl) {
            if (this.hasPermission(alwaysOnTopPermission)) {
                this.platformWindow.setLevel(2);
            }
        } else {
            this.platformWindow.setLevel(1);
        }
    }

    @Override
    public void setResizable(boolean bl) {
        this.platformWindow.setResizable(bl);
    }

    boolean isTrustedFullScreen() {
        return this.hasPermission(fullScreenPermission);
    }

    void exitFullScreen() {
        this.setFullScreen(false);
    }

    boolean isApplet() {
        return this.isPrimaryStage && null != appletWindow;
    }

    private boolean hasPermission(Permission permission) {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(permission, this.getAccessControlContext());
            }
            return true;
        }
        catch (SecurityException securityException) {
            return false;
        }
    }

    public final KeyCombination getSavedFullScreenExitKey() {
        return this.savedFullScreenExitKey;
    }

    private void applyFullScreen() {
        if (this.platformWindow == null) {
            return;
        }
        View view = this.platformWindow.getView();
        if (this.isVisible() && view != null && view.isInFullscreen() != this.isInFullScreen) {
            if (this.isInFullScreen) {
                boolean bl = this.isTrustedFullScreen();
                if (!bl && !this.fullScreenFromUserEvent) {
                    this.exitFullScreen();
                    this.fullscreenChanged(false);
                } else {
                    view.enterFullscreen(false, false, false);
                    if (this.warning != null && this.warning.inWarningTransition()) {
                        this.warning.setView(this.getViewScene());
                    } else {
                        boolean bl2 = true;
                        KeyCombination keyCombination = null;
                        String string = null;
                        if (bl && this.fxStage != null) {
                            keyCombination = this.fxStage.getFullScreenExitKeyCombination();
                            string = this.fxStage.getFullScreenExitHint();
                        }
                        KeyCombination keyCombination2 = this.savedFullScreenExitKey = keyCombination == null ? defaultFullScreenExitKeycombo : keyCombination;
                        if ("".equals(string) || this.savedFullScreenExitKey.equals(KeyCombination.NO_MATCH)) {
                            bl2 = false;
                        }
                        if (bl2 && string == null) {
                            if (keyCombination == null) {
                                string = RESOURCES.getString("OverlayWarningESC");
                            } else {
                                String string2 = RESOURCES.getString("OverlayWarningKey");
                                string = String.format(string2, this.savedFullScreenExitKey.toString());
                            }
                        }
                        if (bl2 && this.warning == null) {
                            this.setWarning(new OverlayWarning(this.getViewScene()));
                        }
                        if (bl2 && this.warning != null) {
                            this.warning.warn(string);
                        }
                    }
                }
            } else {
                if (this.warning != null) {
                    this.warning.cancel();
                    this.setWarning(null);
                }
                view.exitFullscreen(false);
            }
            this.fullScreenFromUserEvent = false;
        } else if (!this.isVisible() && this.warning != null) {
            this.warning.cancel();
            this.setWarning(null);
        }
    }

    void setWarning(OverlayWarning overlayWarning) {
        this.warning = overlayWarning;
        this.getViewScene().synchroniseOverlayWarning();
    }

    OverlayWarning getWarning() {
        return this.warning;
    }

    @Override
    public void setFullScreen(boolean bl) {
        if (this.isInFullScreen == bl) {
            return;
        }
        if (this.isInAllowedEventHandler()) {
            this.fullScreenFromUserEvent = true;
        }
        GlassStage glassStage = (GlassStage)activeFSWindow.get();
        if (bl && glassStage != null) {
            glassStage.setFullScreen(false);
        }
        this.isInFullScreen = bl;
        this.applyFullScreen();
        if (bl) {
            activeFSWindow.set(this);
        }
    }

    void fullscreenChanged(boolean bl) {
        if (!bl) {
            if (activeFSWindow.compareAndSet(this, null)) {
                this.isInFullScreen = false;
            }
        } else {
            this.isInFullScreen = true;
            activeFSWindow.set(this);
        }
        AccessController.doPrivileged(() -> {
            if (this.stageListener != null) {
                this.stageListener.changedFullscreen(bl);
            }
            return null;
        }, this.getAccessControlContext());
    }

    @Override
    public void toBack() {
        this.platformWindow.toBack();
        if (this.isAppletStage && null != appletWindow) {
            appletWindow.assertStageOrder();
        }
    }

    @Override
    public void toFront() {
        this.platformWindow.requestFocus();
        this.platformWindow.toFront();
        if (this.isAppletStage && null != appletWindow) {
            appletWindow.assertStageOrder();
        }
    }

    @Override
    public void close() {
        super.close();
        QuantumToolkit.runWithRenderLock(() -> {
            ViewScene viewScene;
            if (this.platformWindow != null) {
                platformWindows.remove(this.platformWindow);
                this.platformWindow.close();
                this.platformWindow = null;
            }
            if ((viewScene = this.getViewScene()) != null) {
                viewScene.updateSceneState();
            }
            return null;
        });
    }

    void setPlatformWindowClosed() {
        this.platformWindow = null;
    }

    static void addActiveWindow(WindowStage windowStage) {
        activeWindows.remove(windowStage);
        activeWindows.add(windowStage);
    }

    static void removeActiveWindow(WindowStage windowStage) {
        activeWindows.remove(windowStage);
    }

    final void handleFocusDisabled() {
        if (activeWindows.isEmpty()) {
            return;
        }
        WindowStage windowStage = activeWindows.get(activeWindows.size() - 1);
        windowStage.setIconified(false);
        windowStage.requestToFront();
        windowStage.requestFocus();
    }

    @Override
    public boolean grabFocus() {
        return this.platformWindow.grabFocus();
    }

    @Override
    public void ungrabFocus() {
        this.platformWindow.ungrabFocus();
    }

    @Override
    public void requestFocus() {
        this.platformWindow.requestFocus();
    }

    @Override
    public void requestFocus(FocusCause focusCause) {
        switch (focusCause) {
            case TRAVERSED_FORWARD: {
                this.platformWindow.requestFocus(543);
                break;
            }
            case TRAVERSED_BACKWARD: {
                this.platformWindow.requestFocus(544);
                break;
            }
            case ACTIVATED: {
                this.platformWindow.requestFocus(542);
                break;
            }
            case DEACTIVATED: {
                this.platformWindow.requestFocus(541);
            }
        }
    }

    @Override
    protected void setPlatformEnabled(boolean bl) {
        super.setPlatformEnabled(bl);
        this.platformWindow.setEnabled(bl);
        if (bl) {
            if (this.platformWindow.isEnabled()) {
                this.requestToFront();
            }
        } else {
            WindowStage.removeActiveWindow(this);
        }
    }

    void setEnabled(boolean bl) {
        if (this.owner != null && this.owner instanceof WindowStage) {
            ((WindowStage)this.owner).setEnabled(bl);
        }
        if (bl && (this.platformWindow == null || this.platformWindow.isClosed())) {
            return;
        }
        this.setPlatformEnabled(bl);
        if (bl && this.isAppletStage && null != appletWindow) {
            appletWindow.assertStageOrder();
        }
    }

    protected void requestToFront() {
        if (this.platformWindow != null) {
            this.platformWindow.toFront();
            this.platformWindow.requestFocus();
        }
    }

    public void setInAllowedEventHandler(boolean bl) {
        this.inAllowedEventHandler = bl;
    }

    private boolean isInAllowedEventHandler() {
        return this.inAllowedEventHandler;
    }

    @Override
    public void requestInput(String string, int n2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14, double d15) {
        this.platformWindow.requestInput(string, n2, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14, d15);
    }

    @Override
    public void releaseInput() {
        this.platformWindow.releaseInput();
    }

    @Override
    public void setRTL(boolean bl) {
        this.rtl = bl;
    }

    private /* synthetic */ Object lambda$setScene$388(View view, GlassScene glassScene, GlassScene glassScene2) {
        this.platformWindow.setView(view);
        if (glassScene != null) {
            glassScene.updateSceneState();
        }
        glassScene2.updateSceneState();
        return null;
    }
}

