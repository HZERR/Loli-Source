/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.EventLoop;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.font.PrismFontLoader;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.runtime.async.AbstractRemoteResource;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.text.PrismTextLayoutFactory;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.FileChooserType;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKDragGestureListener;
import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKDropTargetListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKScreenConfigurationListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.TKSystemMenu;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.CursorUtils;
import com.sun.javafx.tk.quantum.EmbeddedStage;
import com.sun.javafx.tk.quantum.GlassAppletWindow;
import com.sun.javafx.tk.quantum.GlassScene;
import com.sun.javafx.tk.quantum.GlassStage;
import com.sun.javafx.tk.quantum.GlassSystemMenu;
import com.sun.javafx.tk.quantum.MasterTimer;
import com.sun.javafx.tk.quantum.PaintCollector;
import com.sun.javafx.tk.quantum.PaintRenderJob;
import com.sun.javafx.tk.quantum.PathIteratorHelper;
import com.sun.javafx.tk.quantum.PerformanceTrackerImpl;
import com.sun.javafx.tk.quantum.PrismImageLoader2;
import com.sun.javafx.tk.quantum.QuantumClipboard;
import com.sun.javafx.tk.quantum.QuantumRenderer;
import com.sun.javafx.tk.quantum.ViewPainter;
import com.sun.javafx.tk.quantum.WindowStage;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrImage;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javafx.application.ConditionalFeature;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public final class QuantumToolkit
extends Toolkit {
    public static final boolean verbose = AccessController.doPrivileged(() -> Boolean.getBoolean("quantum.verbose"));
    public static final boolean pulseDebug = AccessController.doPrivileged(() -> Boolean.getBoolean("quantum.pulse"));
    private static final boolean multithreaded = AccessController.doPrivileged(() -> {
        String string = System.getProperty("quantum.multithreaded");
        if (string == null) {
            return true;
        }
        boolean bl = Boolean.parseBoolean(string);
        if (verbose) {
            System.out.println(bl ? "Multi-Threading Enabled" : "Multi-Threading Disabled");
        }
        return bl;
    });
    private static boolean debug = AccessController.doPrivileged(() -> Boolean.getBoolean("quantum.debug"));
    private static Integer pulseHZ = AccessController.doPrivileged(() -> Integer.getInteger("javafx.animation.pulse"));
    static final boolean liveResize = AccessController.doPrivileged(() -> {
        boolean bl = "swt".equals(System.getProperty("glass.platform"));
        String string = (PlatformUtil.isMac() || PlatformUtil.isWindows()) && !bl ? "true" : "false";
        return "true".equals(System.getProperty("javafx.live.resize", string));
    });
    static final boolean drawInPaint = AccessController.doPrivileged(() -> {
        boolean bl = "swt".equals(System.getProperty("glass.platform"));
        String string = PlatformUtil.isMac() && bl ? "true" : "false";
        return "true".equals(System.getProperty("javafx.draw.in.paint", string));
    });
    private static boolean singleThreaded = AccessController.doPrivileged(() -> {
        Boolean bl = Boolean.getBoolean("quantum.singlethreaded");
        if (bl.booleanValue()) {
            System.out.println("Warning: Single GUI Threadiong is enabled, FPS should be slower");
        }
        return bl;
    });
    private static boolean noRenderJobs = AccessController.doPrivileged(() -> {
        Boolean bl = Boolean.getBoolean("quantum.norenderjobs");
        if (bl.booleanValue()) {
            System.out.println("Warning: Quantum will not submit render jobs, nothing should draw");
        }
        return bl;
    });
    private AtomicBoolean toolkitRunning = new AtomicBoolean(false);
    private AtomicBoolean animationRunning = new AtomicBoolean(false);
    private AtomicBoolean nextPulseRequested = new AtomicBoolean(false);
    private AtomicBoolean pulseRunning = new AtomicBoolean(false);
    private int inPulse = 0;
    private CountDownLatch launchLatch = new CountDownLatch(1);
    final int PULSE_INTERVAL = (int)(TimeUnit.SECONDS.toMillis(1L) / (long)this.getRefreshRate());
    final int FULLSPEED_INTERVAL = 1;
    boolean nativeSystemVsync = false;
    private float _maxPixelScale;
    private Runnable pulseRunnable;
    private Runnable userRunnable;
    private Runnable timerRunnable;
    private Timer pulseTimer = null;
    private Thread shutdownHook = null;
    private PaintCollector collector;
    private QuantumRenderer renderer;
    private GraphicsPipeline pipeline;
    private ClassLoader ccl;
    private HashMap<Object, EventLoop> eventLoopMap = null;
    private final PerformanceTracker perfTracker = new PerformanceTrackerImpl();
    private static ScreenConfigurationAccessor screenAccessor = new ScreenConfigurationAccessor(){

        @Override
        public int getMinX(Object object) {
            return ((com.sun.glass.ui.Screen)object).getX();
        }

        @Override
        public int getMinY(Object object) {
            return ((com.sun.glass.ui.Screen)object).getY();
        }

        @Override
        public int getWidth(Object object) {
            return ((com.sun.glass.ui.Screen)object).getWidth();
        }

        @Override
        public int getHeight(Object object) {
            return ((com.sun.glass.ui.Screen)object).getHeight();
        }

        @Override
        public int getVisualMinX(Object object) {
            return ((com.sun.glass.ui.Screen)object).getVisibleX();
        }

        @Override
        public int getVisualMinY(Object object) {
            return ((com.sun.glass.ui.Screen)object).getVisibleY();
        }

        @Override
        public int getVisualWidth(Object object) {
            return ((com.sun.glass.ui.Screen)object).getVisibleWidth();
        }

        @Override
        public int getVisualHeight(Object object) {
            return ((com.sun.glass.ui.Screen)object).getVisibleHeight();
        }

        @Override
        public float getDPI(Object object) {
            return ((com.sun.glass.ui.Screen)object).getResolutionX();
        }

        @Override
        public float getUIScale(Object object) {
            return ((com.sun.glass.ui.Screen)object).getUIScale();
        }

        @Override
        public float getRenderScale(Object object) {
            return ((com.sun.glass.ui.Screen)object).getRenderScale();
        }
    };
    private Map<Object, Object> contextMap = Collections.synchronizedMap(new HashMap());
    private DelayedRunnable animationRunnable;
    static BasicStroke tmpStroke = new BasicStroke();
    private QuantumClipboard clipboard;
    private GlassSystemMenu systemMenu = new GlassSystemMenu();

    @Override
    public boolean init() {
        this.renderer = QuantumRenderer.getInstance();
        this.collector = PaintCollector.createInstance(this);
        this.pipeline = GraphicsPipeline.getPipeline();
        this.shutdownHook = new Thread("Glass/Prism Shutdown Hook"){

            @Override
            public void run() {
                QuantumToolkit.this.dispose();
            }
        };
        AccessController.doPrivileged(() -> {
            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
            return null;
        });
        return true;
    }

    @Override
    public void startup(Runnable runnable) {
        this.ccl = Thread.currentThread().getContextClassLoader();
        try {
            this.userRunnable = runnable;
            Application.run(() -> this.runToolkit());
        }
        catch (RuntimeException runtimeException) {
            if (verbose) {
                runtimeException.printStackTrace();
            }
            throw runtimeException;
        }
        catch (Throwable throwable) {
            if (verbose) {
                throwable.printStackTrace();
            }
            throw new RuntimeException(throwable);
        }
        try {
            this.launchLatch.await();
        }
        catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    private void assertToolkitRunning() {
    }

    boolean shouldWaitForRenderingToComplete() {
        return !multithreaded;
    }

    private static void initSceneGraph() {
        Screen.getPrimary();
    }

    void runToolkit() {
        Thread thread = Thread.currentThread();
        if (!this.toolkitRunning.getAndSet(true)) {
            thread.setName("JavaFX Application Thread");
            thread.setContextClassLoader(this.ccl);
            QuantumToolkit.setFxUserThread(thread);
            QuantumToolkit.assignScreensAdapters();
            this.renderer.createResourceFactory();
            this.pulseRunnable = () -> this.pulse();
            this.timerRunnable = () -> {
                try {
                    this.postPulse();
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace(System.err);
                }
            };
            this.pulseTimer = Application.GetApplication().createTimer(this.timerRunnable);
            Application.GetApplication().setEventHandler(new Application.EventHandler(){

                @Override
                public void handleQuitAction(Application application, long l2) {
                    GlassStage.requestClosingAllWindows();
                }

                @Override
                public boolean handleThemeChanged(String string) {
                    return PlatformImpl.setAccessibilityTheme(string);
                }
            });
        }
        QuantumToolkit.initSceneGraph();
        this.launchLatch.countDown();
        try {
            Application.invokeAndWait(this.userRunnable);
            if (this.getMasterTimer().isFullspeed()) {
                this.pulseTimer.start(1);
            } else {
                boolean bl = this.nativeSystemVsync = com.sun.glass.ui.Screen.getVideoRefreshPeriod() != 0.0;
                if (this.nativeSystemVsync) {
                    this.pulseTimer.start();
                } else {
                    this.pulseTimer.start(this.PULSE_INTERVAL);
                }
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace(System.err);
        }
        finally {
            if (PrismSettings.verbose) {
                System.err.println(" vsync: " + PrismSettings.isVsyncEnabled + " vpipe: " + this.pipeline.isVsyncSupported());
            }
            PerformanceTracker.logEvent("Toolkit.startup - finished");
        }
    }

    public static <T> T runWithoutRenderLock(Supplier<T> supplier) {
        boolean bl = ViewPainter.renderLock.isHeldByCurrentThread();
        try {
            if (bl) {
                ViewPainter.renderLock.unlock();
            }
            T t2 = supplier.get();
            return t2;
        }
        finally {
            if (bl) {
                ViewPainter.renderLock.lock();
            }
        }
    }

    public static <T> T runWithRenderLock(Supplier<T> supplier) {
        ViewPainter.renderLock.lock();
        try {
            T t2 = supplier.get();
            return t2;
        }
        finally {
            ViewPainter.renderLock.unlock();
        }
    }

    boolean hasNativeSystemVsync() {
        return this.nativeSystemVsync;
    }

    boolean isVsyncEnabled() {
        return PrismSettings.isVsyncEnabled && this.pipeline.isVsyncSupported();
    }

    @Override
    public void checkFxUserThread() {
        super.checkFxUserThread();
        this.renderer.checkRendererIdle();
    }

    protected static Thread getFxUserThread() {
        return Toolkit.getFxUserThread();
    }

    @Override
    public Future addRenderJob(RenderJob renderJob) {
        if (noRenderJobs) {
            CompletionListener completionListener = renderJob.getCompletionListener();
            if (renderJob instanceof PaintRenderJob) {
                ((PaintRenderJob)renderJob).getScene().setPainting(false);
            }
            if (completionListener != null) {
                try {
                    completionListener.done(renderJob);
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
            return null;
        }
        if (singleThreaded) {
            renderJob.run();
            return null;
        }
        return this.renderer.submitRenderJob(renderJob);
    }

    void postPulse() {
        if (this.toolkitRunning.get() && (this.animationRunning.get() || this.nextPulseRequested.get() || this.collector.hasDirty()) && !this.setPulseRunning()) {
            Application.invokeLater(this.pulseRunnable);
            if (debug) {
                System.err.println("QT.postPulse@(" + System.nanoTime() + "): " + this.pulseString());
            }
        } else if (debug) {
            System.err.println("QT.postPulse#(" + System.nanoTime() + ") DROP: " + this.pulseString());
        }
    }

    private String pulseString() {
        return (this.toolkitRunning.get() ? "T" : "t") + (this.animationRunning.get() ? "A" : "a") + (this.pulseRunning.get() ? "P" : "p") + (this.nextPulseRequested.get() ? "N" : "n") + (this.collector.hasDirty() ? "D" : "d");
    }

    private boolean setPulseRunning() {
        return this.pulseRunning.getAndSet(true);
    }

    private void endPulseRunning() {
        this.pulseRunning.set(false);
        if (debug) {
            System.err.println("QT.endPulse: " + System.nanoTime());
        }
    }

    protected void pulse() {
        this.pulse(true);
    }

    void pulse(boolean bl) {
        try {
            ++this.inPulse;
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.pulseStart();
            }
            if (!this.toolkitRunning.get()) {
                return;
            }
            this.nextPulseRequested.set(false);
            if (this.animationRunnable != null) {
                this.animationRunning.set(true);
                this.animationRunnable.run();
            } else {
                this.animationRunning.set(false);
            }
            this.firePulse();
            if (bl) {
                this.collector.renderAll();
            }
        }
        finally {
            --this.inPulse;
            this.endPulseRunning();
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.pulseEnd();
            }
        }
    }

    void vsyncHint() {
        if (this.isVsyncEnabled()) {
            if (debug) {
                System.err.println("QT.vsyncHint: postPulse: " + System.nanoTime());
            }
            this.postPulse();
        }
    }

    @Override
    public AppletWindow createAppletWindow(long l2, String string) {
        GlassAppletWindow glassAppletWindow = new GlassAppletWindow(l2, string);
        WindowStage.setAppletWindow(glassAppletWindow);
        return glassAppletWindow;
    }

    @Override
    public void closeAppletWindow() {
        GlassAppletWindow glassAppletWindow = WindowStage.getAppletWindow();
        if (null != glassAppletWindow) {
            glassAppletWindow.dispose();
            WindowStage.setAppletWindow(null);
        }
    }

    @Override
    public TKStage createTKStage(Window window, boolean bl, StageStyle stageStyle, boolean bl2, Modality modality, TKStage tKStage, boolean bl3, AccessControlContext accessControlContext) {
        this.assertToolkitRunning();
        WindowStage windowStage = new WindowStage(window, bl, stageStyle, modality, tKStage);
        windowStage.setSecurityContext(accessControlContext);
        if (bl2) {
            windowStage.setIsPrimary();
        }
        windowStage.setRTL(bl3);
        windowStage.init(this.systemMenu);
        return windowStage;
    }

    @Override
    public boolean canStartNestedEventLoop() {
        return this.inPulse == 0;
    }

    @Override
    public Object enterNestedEventLoop(Object object) {
        this.checkFxUserThread();
        if (object == null) {
            throw new NullPointerException();
        }
        if (!this.canStartNestedEventLoop()) {
            throw new IllegalStateException("Cannot enter nested loop during animation or layout processing");
        }
        if (this.eventLoopMap == null) {
            this.eventLoopMap = new HashMap();
        }
        if (this.eventLoopMap.containsKey(object)) {
            throw new IllegalArgumentException("Key already associated with a running event loop: " + object);
        }
        EventLoop eventLoop = Application.GetApplication().createEventLoop();
        this.eventLoopMap.put(object, eventLoop);
        Object object2 = eventLoop.enter();
        if (!this.isNestedLoopRunning()) {
            this.notifyLastNestedLoopExited();
        }
        return object2;
    }

    @Override
    public void exitNestedEventLoop(Object object, Object object2) {
        this.checkFxUserThread();
        if (object == null) {
            throw new NullPointerException();
        }
        if (this.eventLoopMap == null || !this.eventLoopMap.containsKey(object)) {
            throw new IllegalArgumentException("Key not associated with a running event loop: " + object);
        }
        EventLoop eventLoop = this.eventLoopMap.get(object);
        this.eventLoopMap.remove(object);
        eventLoop.leave(object2);
    }

    @Override
    public TKStage createTKPopupStage(Window window, StageStyle stageStyle, TKStage tKStage, AccessControlContext accessControlContext) {
        this.assertToolkitRunning();
        boolean bl = tKStage instanceof WindowStage ? ((WindowStage)tKStage).isSecurityDialog() : false;
        WindowStage windowStage = new WindowStage(window, bl, stageStyle, null, tKStage);
        windowStage.setSecurityContext(accessControlContext);
        windowStage.setIsPopup();
        windowStage.init(this.systemMenu);
        return windowStage;
    }

    @Override
    public TKStage createTKEmbeddedStage(HostInterface hostInterface, AccessControlContext accessControlContext) {
        this.assertToolkitRunning();
        EmbeddedStage embeddedStage = new EmbeddedStage(hostInterface);
        embeddedStage.setSecurityContext(accessControlContext);
        return embeddedStage;
    }

    @Override
    public ScreenConfigurationAccessor setScreenConfigurationListener(final TKScreenConfigurationListener tKScreenConfigurationListener) {
        com.sun.glass.ui.Screen.setEventHandler(new Screen.EventHandler(){

            @Override
            public void handleSettingsChanged() {
                QuantumToolkit.notifyScreenListener(tKScreenConfigurationListener);
            }
        });
        return screenAccessor;
    }

    private static void assignScreensAdapters() {
        GraphicsPipeline graphicsPipeline = GraphicsPipeline.getPipeline();
        for (com.sun.glass.ui.Screen screen : com.sun.glass.ui.Screen.getScreens()) {
            screen.setAdapterOrdinal(graphicsPipeline.getAdapterOrdinal(screen));
        }
    }

    private static void notifyScreenListener(TKScreenConfigurationListener tKScreenConfigurationListener) {
        QuantumToolkit.assignScreensAdapters();
        tKScreenConfigurationListener.screenConfigurationChanged();
    }

    @Override
    public Object getPrimaryScreen() {
        return com.sun.glass.ui.Screen.getMainScreen();
    }

    @Override
    public List<?> getScreens() {
        return com.sun.glass.ui.Screen.getScreens();
    }

    @Override
    public ScreenConfigurationAccessor getScreenConfigurationAccessor() {
        return screenAccessor;
    }

    @Override
    public PerformanceTracker getPerformanceTracker() {
        return this.perfTracker;
    }

    @Override
    public PerformanceTracker createPerformanceTracker() {
        return new PerformanceTrackerImpl();
    }

    public float getMaxRenderScale() {
        if (this._maxPixelScale == 0.0f) {
            for (Object obj : this.getScreens()) {
                this._maxPixelScale = Math.max(this._maxPixelScale, ((com.sun.glass.ui.Screen)obj).getRenderScale());
            }
        }
        return this._maxPixelScale;
    }

    @Override
    public ImageLoader loadImage(String string, int n2, int n3, boolean bl, boolean bl2) {
        return new PrismImageLoader2(string, n2, n3, bl, this.getMaxRenderScale(), bl2);
    }

    @Override
    public ImageLoader loadImage(InputStream inputStream, int n2, int n3, boolean bl, boolean bl2) {
        return new PrismImageLoader2(inputStream, n2, n3, bl, bl2);
    }

    public AbstractRemoteResource<? extends ImageLoader> loadImageAsync(AsyncOperationListener asyncOperationListener, String string, int n2, int n3, boolean bl, boolean bl2) {
        return new PrismImageLoader2.AsyncImageLoader(asyncOperationListener, string, n2, n3, bl, bl2);
    }

    @Override
    public void defer(Runnable runnable) {
        if (!this.toolkitRunning.get()) {
            return;
        }
        Application.invokeLater(runnable);
    }

    @Override
    public void exit() {
        this.checkFxUserThread();
        this.pulseTimer.stop();
        PaintCollector.getInstance().waitForRenderingToComplete();
        this.notifyShutdownHooks();
        QuantumToolkit.runWithRenderLock(() -> {
            Application application = Application.GetApplication();
            application.terminate();
            return null;
        });
        this.dispose();
        super.exit();
    }

    public void dispose() {
        if (this.toolkitRunning.compareAndSet(true, false)) {
            this.pulseTimer.stop();
            this.renderer.stopRenderer();
            try {
                AccessController.doPrivileged(() -> {
                    Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
                    return null;
                });
            }
            catch (IllegalStateException illegalStateException) {
                // empty catch block
            }
        }
    }

    @Override
    public boolean isForwardTraversalKey(KeyEvent keyEvent) {
        return keyEvent.getCode() == KeyCode.TAB && keyEvent.getEventType() == KeyEvent.KEY_PRESSED && !keyEvent.isShiftDown();
    }

    @Override
    public boolean isBackwardTraversalKey(KeyEvent keyEvent) {
        return keyEvent.getCode() == KeyCode.TAB && keyEvent.getEventType() == KeyEvent.KEY_PRESSED && keyEvent.isShiftDown();
    }

    @Override
    public Map<Object, Object> getContextMap() {
        return this.contextMap;
    }

    @Override
    public int getRefreshRate() {
        if (pulseHZ == null) {
            return 60;
        }
        return pulseHZ;
    }

    @Override
    public void setAnimationRunnable(DelayedRunnable delayedRunnable) {
        if (delayedRunnable != null) {
            this.animationRunning.set(true);
        }
        this.animationRunnable = delayedRunnable;
    }

    @Override
    public void requestNextPulse() {
        this.nextPulseRequested.set(true);
    }

    @Override
    public void waitFor(Toolkit.Task task) {
        if (task.isFinished()) {
            return;
        }
    }

    @Override
    protected Object createColorPaint(Color color) {
        return new com.sun.prism.paint.Color((float)color.getRed(), (float)color.getGreen(), (float)color.getBlue(), (float)color.getOpacity());
    }

    private com.sun.prism.paint.Color toPrismColor(Color color) {
        return (com.sun.prism.paint.Color)Toolkit.getPaintAccessor().getPlatformPaint(color);
    }

    private List<Stop> convertStops(List<javafx.scene.paint.Stop> list) {
        ArrayList<Stop> arrayList = new ArrayList<Stop>(list.size());
        for (javafx.scene.paint.Stop stop : list) {
            arrayList.add(new Stop(this.toPrismColor(stop.getColor()), (float)stop.getOffset()));
        }
        return arrayList;
    }

    @Override
    protected Object createLinearGradientPaint(javafx.scene.paint.LinearGradient linearGradient) {
        int n2 = 2;
        CycleMethod cycleMethod = linearGradient.getCycleMethod();
        if (cycleMethod == CycleMethod.NO_CYCLE) {
            n2 = 0;
        } else if (cycleMethod == CycleMethod.REFLECT) {
            n2 = 1;
        }
        List<Stop> list = this.convertStops(linearGradient.getStops());
        return new LinearGradient((float)linearGradient.getStartX(), (float)linearGradient.getStartY(), (float)linearGradient.getEndX(), (float)linearGradient.getEndY(), null, linearGradient.isProportional(), n2, list);
    }

    @Override
    protected Object createRadialGradientPaint(javafx.scene.paint.RadialGradient radialGradient) {
        float f2 = (float)radialGradient.getCenterX();
        float f3 = (float)radialGradient.getCenterY();
        float f4 = (float)radialGradient.getFocusAngle();
        float f5 = (float)radialGradient.getFocusDistance();
        int n2 = 0;
        n2 = radialGradient.getCycleMethod() == CycleMethod.NO_CYCLE ? 0 : (radialGradient.getCycleMethod() == CycleMethod.REFLECT ? 1 : 2);
        List<Stop> list = this.convertStops(radialGradient.getStops());
        return new RadialGradient(f2, f3, f4, f5, (float)radialGradient.getRadius(), null, radialGradient.isProportional(), n2, list);
    }

    @Override
    protected Object createImagePatternPaint(ImagePattern imagePattern) {
        if (imagePattern.getImage() == null) {
            return com.sun.prism.paint.Color.TRANSPARENT;
        }
        return new com.sun.prism.paint.ImagePattern((com.sun.prism.Image)imagePattern.getImage().impl_getPlatformImage(), (float)imagePattern.getX(), (float)imagePattern.getY(), (float)imagePattern.getWidth(), (float)imagePattern.getHeight(), imagePattern.isProportional(), Toolkit.getPaintAccessor().isMutable(imagePattern));
    }

    private void initStroke(StrokeType strokeType, double d2, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f2, float[] arrf, float f3) {
        int n2 = strokeType == StrokeType.CENTERED ? 0 : (strokeType == StrokeType.INSIDE ? 1 : 2);
        int n3 = strokeLineCap == StrokeLineCap.BUTT ? 0 : (strokeLineCap == StrokeLineCap.SQUARE ? 2 : 1);
        int n4 = strokeLineJoin == StrokeLineJoin.BEVEL ? 2 : (strokeLineJoin == StrokeLineJoin.MITER ? 0 : 1);
        tmpStroke.set(n2, (float)d2, n3, n4, f2);
        if (arrf != null && arrf.length > 0) {
            tmpStroke.set(arrf, f3);
        } else {
            tmpStroke.set((float[])null, 0.0f);
        }
    }

    @Override
    public void accumulateStrokeBounds(Shape shape, float[] arrf, StrokeType strokeType, double d2, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f2, BaseTransform baseTransform) {
        this.initStroke(strokeType, d2, strokeLineCap, strokeLineJoin, f2, null, 0.0f);
        if (baseTransform.isTranslateOrIdentity()) {
            tmpStroke.accumulateShapeBounds(arrf, shape, baseTransform);
        } else {
            Shape.accumulate(arrf, tmpStroke.createStrokedShape(shape), baseTransform);
        }
    }

    @Override
    public boolean strokeContains(Shape shape, double d2, double d3, StrokeType strokeType, double d4, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f2) {
        this.initStroke(strokeType, d4, strokeLineCap, strokeLineJoin, f2, null, 0.0f);
        return tmpStroke.createStrokedShape(shape).contains((float)d2, (float)d3);
    }

    @Override
    public Shape createStrokedShape(Shape shape, StrokeType strokeType, double d2, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f2, float[] arrf, float f3) {
        this.initStroke(strokeType, d2, strokeLineCap, strokeLineJoin, f2, arrf, f3);
        return tmpStroke.createStrokedShape(shape);
    }

    @Override
    public Dimension2D getBestCursorSize(int n2, int n3) {
        return CursorUtils.getBestCursorSize(n2, n3);
    }

    @Override
    public int getMaximumCursorColors() {
        return 2;
    }

    @Override
    public int getKeyCodeForChar(String string) {
        return string.length() == 1 ? com.sun.glass.events.KeyEvent.getKeyCodeForChar(string.charAt(0)) : 0;
    }

    @Override
    public PathElement[] convertShapeToFXPath(Object object) {
        if (object == null) {
            return new PathElement[0];
        }
        ArrayList<MoveTo> arrayList = new ArrayList<MoveTo>();
        Shape shape = (Shape)object;
        PathIterator pathIterator = shape.getPathIterator(null);
        PathIteratorHelper pathIteratorHelper = new PathIteratorHelper(pathIterator);
        PathIteratorHelper.Struct struct = new PathIteratorHelper.Struct();
        while (!pathIteratorHelper.isDone()) {
            PathElement pathElement;
            boolean bl = pathIteratorHelper.getWindingRule() == 0;
            int n2 = pathIteratorHelper.currentSegment(struct);
            if (n2 == 0) {
                pathElement = new MoveTo(struct.f0, struct.f1);
            } else if (n2 == 1) {
                pathElement = new LineTo(struct.f0, struct.f1);
            } else if (n2 == 2) {
                pathElement = new QuadCurveTo(struct.f0, struct.f1, struct.f2, struct.f3);
            } else if (n2 == 3) {
                pathElement = new CubicCurveTo(struct.f0, struct.f1, struct.f2, struct.f3, struct.f4, struct.f5);
            } else if (n2 == 4) {
                pathElement = new ClosePath();
            } else {
                throw new IllegalStateException("Invalid element type: " + n2);
            }
            pathIteratorHelper.next();
            arrayList.add((MoveTo)pathElement);
        }
        return arrayList.toArray(new PathElement[arrayList.size()]);
    }

    @Override
    public HitInfo convertHitInfoToFX(Object object) {
        Integer n2 = (Integer)object;
        HitInfo hitInfo = new HitInfo();
        hitInfo.setCharIndex(n2);
        hitInfo.setLeading(true);
        return hitInfo;
    }

    @Override
    public Filterable toFilterable(Image image) {
        return PrImage.create((com.sun.prism.Image)image.impl_getPlatformImage());
    }

    @Override
    public FilterContext getFilterContext(Object object) {
        if (object == null || !(object instanceof com.sun.glass.ui.Screen)) {
            return PrFilterContext.getDefaultInstance();
        }
        com.sun.glass.ui.Screen screen = (com.sun.glass.ui.Screen)object;
        return PrFilterContext.getInstance(screen);
    }

    @Override
    public AbstractMasterTimer getMasterTimer() {
        return MasterTimer.getInstance();
    }

    @Override
    public FontLoader getFontLoader() {
        return PrismFontLoader.getInstance();
    }

    @Override
    public TextLayoutFactory getTextLayoutFactory() {
        return PrismTextLayoutFactory.getFactory();
    }

    @Override
    public Object createSVGPathObject(SVGPath sVGPath) {
        int n2 = sVGPath.getFillRule() == FillRule.NON_ZERO ? 1 : 0;
        Path2D path2D = new Path2D(n2);
        path2D.appendSVGPath(sVGPath.getContent());
        return path2D;
    }

    @Override
    public Path2D createSVGPath2D(SVGPath sVGPath) {
        int n2 = sVGPath.getFillRule() == FillRule.NON_ZERO ? 1 : 0;
        Path2D path2D = new Path2D(n2);
        path2D.appendSVGPath(sVGPath.getContent());
        return path2D;
    }

    @Override
    public boolean imageContains(Object object, float f2, float f3) {
        if (object == null) {
            return false;
        }
        com.sun.prism.Image image = (com.sun.prism.Image)object;
        int n2 = (int)f2 + image.getMinX();
        int n3 = (int)f3 + image.getMinY();
        if (image.isOpaque()) {
            return true;
        }
        if (image.getPixelFormat() == PixelFormat.INT_ARGB_PRE) {
            IntBuffer intBuffer = (IntBuffer)image.getPixelBuffer();
            int n4 = n2 + n3 * image.getRowLength();
            if (n4 >= intBuffer.limit()) {
                return false;
            }
            return (intBuffer.get(n4) & 0xFF000000) != 0;
        }
        if (image.getPixelFormat() == PixelFormat.BYTE_BGRA_PRE) {
            ByteBuffer byteBuffer = (ByteBuffer)image.getPixelBuffer();
            int n5 = n2 * image.getBytesPerPixelUnit() + n3 * image.getScanlineStride() + 3;
            if (n5 >= byteBuffer.limit()) {
                return false;
            }
            return (byteBuffer.get(n5) & 0xFF) != 0;
        }
        if (image.getPixelFormat() == PixelFormat.BYTE_ALPHA) {
            ByteBuffer byteBuffer = (ByteBuffer)image.getPixelBuffer();
            int n6 = n2 * image.getBytesPerPixelUnit() + n3 * image.getScanlineStride();
            if (n6 >= byteBuffer.limit()) {
                return false;
            }
            return (byteBuffer.get(n6) & 0xFF) != 0;
        }
        return true;
    }

    @Override
    public boolean isNestedLoopRunning() {
        return Application.isNestedLoopRunning();
    }

    @Override
    public boolean isSupported(ConditionalFeature conditionalFeature) {
        switch (conditionalFeature) {
            case SCENE3D: {
                return GraphicsPipeline.getPipeline().is3DSupported();
            }
            case EFFECT: {
                return GraphicsPipeline.getPipeline().isEffectSupported();
            }
            case SHAPE_CLIP: {
                return true;
            }
            case INPUT_METHOD: {
                return Application.GetApplication().supportsInputMethods();
            }
            case TRANSPARENT_WINDOW: {
                return Application.GetApplication().supportsTransparentWindows();
            }
            case UNIFIED_WINDOW: {
                return Application.GetApplication().supportsUnifiedWindows();
            }
            case TWO_LEVEL_FOCUS: {
                return Application.GetApplication().hasTwoLevelFocus();
            }
            case VIRTUAL_KEYBOARD: {
                return Application.GetApplication().hasVirtualKeyboard();
            }
            case INPUT_TOUCH: {
                return Application.GetApplication().hasTouch();
            }
            case INPUT_MULTITOUCH: {
                return Application.GetApplication().hasMultiTouch();
            }
            case INPUT_POINTER: {
                return Application.GetApplication().hasPointer();
            }
        }
        return false;
    }

    @Override
    public boolean isMSAASupported() {
        return GraphicsPipeline.getPipeline().isMSAASupported();
    }

    static TransferMode clipboardActionToTransferMode(int n2) {
        switch (n2) {
            case 0: {
                return null;
            }
            case 1: 
            case 0x40000001: {
                return TransferMode.COPY;
            }
            case 2: 
            case 0x40000002: {
                return TransferMode.MOVE;
            }
            case 0x40000000: {
                return TransferMode.LINK;
            }
            case 0x4FFFFFFF: {
                return TransferMode.COPY;
            }
        }
        return null;
    }

    @Override
    public TKClipboard getSystemClipboard() {
        if (this.clipboard == null) {
            this.clipboard = QuantumClipboard.getClipboardInstance(new ClipboardAssistance("SYSTEM"));
        }
        return this.clipboard;
    }

    @Override
    public TKSystemMenu getSystemMenu() {
        return this.systemMenu;
    }

    @Override
    public TKClipboard getNamedClipboard(String string) {
        return null;
    }

    @Override
    public void startDrag(TKScene tKScene, Set<TransferMode> set, TKDragSourceListener tKDragSourceListener, Dragboard dragboard) {
        if (dragboard == null) {
            throw new IllegalArgumentException("dragboard should not be null");
        }
        GlassScene glassScene = (GlassScene)tKScene;
        glassScene.setTKDragSourceListener(tKDragSourceListener);
        QuantumClipboard quantumClipboard = (QuantumClipboard)dragboard.impl_getPeer();
        quantumClipboard.setSupportedTransferMode(set);
        quantumClipboard.flush();
        quantumClipboard.close();
    }

    @Override
    public void enableDrop(TKScene tKScene, TKDropTargetListener tKDropTargetListener) {
        assert (tKScene instanceof GlassScene);
        GlassScene glassScene = (GlassScene)tKScene;
        glassScene.setTKDropTargetListener(tKDropTargetListener);
    }

    @Override
    public void registerDragGestureListener(TKScene tKScene, Set<TransferMode> set, TKDragGestureListener tKDragGestureListener) {
        assert (tKScene instanceof GlassScene);
        GlassScene glassScene = (GlassScene)tKScene;
        glassScene.setTKDragGestureListener(tKDragGestureListener);
    }

    @Override
    public void installInputMethodRequests(TKScene tKScene, InputMethodRequests inputMethodRequests) {
        assert (tKScene instanceof GlassScene);
        GlassScene glassScene = (GlassScene)tKScene;
        glassScene.setInputMethodRequests(inputMethodRequests);
    }

    @Override
    public ImageLoader loadPlatformImage(Object object) {
        if (object instanceof QuantumImage) {
            return (QuantumImage)object;
        }
        if (object instanceof com.sun.prism.Image) {
            return new QuantumImage((com.sun.prism.Image)object);
        }
        throw new UnsupportedOperationException("unsupported class for loadPlatformImage");
    }

    @Override
    public PlatformImage createPlatformImage(int n2, int n3) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(n2 * n3 * 4);
        return com.sun.prism.Image.fromByteBgraPreData(byteBuffer, n2, n3);
    }

    @Override
    public Object renderToImage(Toolkit.ImageRenderingContext imageRenderingContext) {
        Object object = imageRenderingContext.platformImage;
        final Toolkit.ImageRenderingContext imageRenderingContext2 = imageRenderingContext;
        final Paint paint = imageRenderingContext.platformPaint instanceof Paint ? (Paint)imageRenderingContext.platformPaint : null;
        RenderJob renderJob2 = new RenderJob(new Runnable(){

            private com.sun.prism.paint.Color getClearColor() {
                if (paint == null) {
                    return com.sun.prism.paint.Color.WHITE;
                }
                if (paint.getType() == Paint.Type.COLOR) {
                    return (com.sun.prism.paint.Color)paint;
                }
                if (paint.isOpaque()) {
                    return com.sun.prism.paint.Color.TRANSPARENT;
                }
                return com.sun.prism.paint.Color.WHITE;
            }

            private void draw(Graphics graphics, int n2, int n3, int n4, int n5) {
                graphics.setLights(imageRenderingContext2.lights);
                graphics.setDepthBuffer(imageRenderingContext2.depthBuffer);
                graphics.clear(this.getClearColor());
                if (paint != null && paint.getType() != Paint.Type.COLOR) {
                    graphics.getRenderTarget().setOpaque(paint.isOpaque());
                    graphics.setPaint(paint);
                    graphics.fillQuad(0.0f, 0.0f, n4, n5);
                }
                if (n2 != 0 || n3 != 0) {
                    graphics.translate(-n2, -n3);
                }
                if (imageRenderingContext2.transform != null) {
                    graphics.transform(imageRenderingContext2.transform);
                }
                if (imageRenderingContext2.root != null) {
                    if (imageRenderingContext2.camera != null) {
                        graphics.setCamera(imageRenderingContext2.camera);
                    }
                    NGNode nGNode = imageRenderingContext2.root;
                    nGNode.render(graphics);
                }
            }

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            @Override
            public void run() {
                ResourceFactory resourceFactory = GraphicsPipeline.getDefaultResourceFactory();
                if (!resourceFactory.isDeviceReady()) {
                    return;
                }
                int n2 = imageRenderingContext2.x;
                int n3 = imageRenderingContext2.y;
                int n4 = imageRenderingContext2.width;
                int n5 = imageRenderingContext2.height;
                if (n4 <= 0 || n5 <= 0) {
                    return;
                }
                boolean bl = false;
                try {
                    QuantumImage quantumImage = imageRenderingContext2.platformImage instanceof QuantumImage ? (QuantumImage)imageRenderingContext2.platformImage : new QuantumImage(null);
                    RTTexture rTTexture = quantumImage.getRT(n4, n5, resourceFactory);
                    if (rTTexture == null) {
                        return;
                    }
                    Graphics graphics = rTTexture.createGraphics();
                    this.draw(graphics, n2, n3, n4, n5);
                    int[] arrn = quantumImage.rt.getPixels();
                    if (arrn != null) {
                        quantumImage.setImage(com.sun.prism.Image.fromIntArgbPreData(arrn, n4, n5));
                    } else {
                        IntBuffer intBuffer = IntBuffer.allocate(n4 * n5);
                        if (quantumImage.rt.readPixels(intBuffer, quantumImage.rt.getContentX(), quantumImage.rt.getContentY(), n4, n5)) {
                            quantumImage.setImage(com.sun.prism.Image.fromIntArgbPreData(intBuffer, n4, n5));
                        } else {
                            quantumImage.dispose();
                            quantumImage = null;
                        }
                    }
                    rTTexture.unlock();
                    imageRenderingContext2.platformImage = quantumImage;
                }
                catch (Throwable throwable) {
                    bl = true;
                    throwable.printStackTrace(System.err);
                }
                finally {
                    Disposer.cleanUp();
                    resourceFactory.getTextureResourcePool().freeDisposalRequestedAndCheckResources(bl);
                }
            }
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);
        renderJob2.setCompletionListener(renderJob -> countDownLatch.countDown());
        this.addRenderJob(renderJob2);
        while (true) {
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
                continue;
            }
            break;
        }
        Object object2 = imageRenderingContext2.platformImage;
        imageRenderingContext2.platformImage = object;
        return object2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public CommonDialogs.FileChooserResult showFileChooser(TKStage tKStage, String string, File file, String string2, FileChooserType fileChooserType, List<FileChooser.ExtensionFilter> list, FileChooser.ExtensionFilter extensionFilter) {
        WindowStage windowStage = null;
        try {
            windowStage = this.blockOwnerStage(tKStage);
            CommonDialogs.FileChooserResult fileChooserResult = CommonDialogs.showFileChooser(tKStage instanceof WindowStage ? ((WindowStage)tKStage).getPlatformWindow() : null, file, string2, string, fileChooserType == FileChooserType.SAVE ? 1 : 0, fileChooserType == FileChooserType.OPEN_MULTIPLE, QuantumToolkit.convertExtensionFilters(list), list.indexOf(extensionFilter));
            return fileChooserResult;
        }
        finally {
            if (windowStage != null) {
                windowStage.setEnabled(true);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public File showDirectoryChooser(TKStage tKStage, String string, File file) {
        WindowStage windowStage = null;
        try {
            windowStage = this.blockOwnerStage(tKStage);
            File file2 = CommonDialogs.showFolderChooser(tKStage instanceof WindowStage ? ((WindowStage)tKStage).getPlatformWindow() : null, file, string);
            return file2;
        }
        finally {
            if (windowStage != null) {
                windowStage.setEnabled(true);
            }
        }
    }

    private WindowStage blockOwnerStage(TKStage tKStage) {
        GlassStage glassStage;
        if (tKStage instanceof WindowStage && (glassStage = ((WindowStage)tKStage).getOwner()) instanceof WindowStage) {
            WindowStage windowStage = (WindowStage)glassStage;
            windowStage.setEnabled(false);
            return windowStage;
        }
        return null;
    }

    private static List<CommonDialogs.ExtensionFilter> convertExtensionFilters(List<FileChooser.ExtensionFilter> list) {
        CommonDialogs.ExtensionFilter[] arrextensionFilter = new CommonDialogs.ExtensionFilter[list.size()];
        int n2 = 0;
        for (FileChooser.ExtensionFilter extensionFilter : list) {
            arrextensionFilter[n2++] = new CommonDialogs.ExtensionFilter(extensionFilter.getDescription(), extensionFilter.getExtensions());
        }
        return Arrays.asList(arrextensionFilter);
    }

    @Override
    public long getMultiClickTime() {
        return View.getMultiClickTime();
    }

    @Override
    public int getMultiClickMaxX() {
        return View.getMultiClickMaxX();
    }

    @Override
    public int getMultiClickMaxY() {
        return View.getMultiClickMaxY();
    }

    @Override
    public String getThemeName() {
        return Application.GetApplication().getHighContrastTheme();
    }

    static class QuantumImage
    implements ImageLoader,
    ResourceFactoryListener {
        private RTTexture rt;
        private com.sun.prism.Image image;
        private ResourceFactory rf;

        QuantumImage(com.sun.prism.Image image) {
            this.image = image;
        }

        RTTexture getRT(int n2, int n3, ResourceFactory resourceFactory) {
            boolean bl;
            boolean bl2 = bl = this.rt != null && this.rf == resourceFactory && this.rt.getContentWidth() == n2 && this.rt.getContentHeight() == n3;
            if (bl) {
                this.rt.lock();
                if (this.rt.isSurfaceLost()) {
                    bl = false;
                }
            }
            if (!bl) {
                if (this.rt != null) {
                    this.rt.dispose();
                }
                if (this.rf != null) {
                    this.rf.removeFactoryListener(this);
                    this.rf = null;
                }
                this.rt = resourceFactory.createRTTexture(n2, n3, Texture.WrapMode.CLAMP_TO_ZERO);
                if (this.rt != null) {
                    this.rf = resourceFactory;
                    this.rf.addFactoryListener(this);
                }
            }
            return this.rt;
        }

        void dispose() {
            if (this.rt != null) {
                this.rt.dispose();
                this.rt = null;
            }
        }

        void setImage(com.sun.prism.Image image) {
            this.image = image;
        }

        @Override
        public Exception getException() {
            return this.image == null ? new IllegalStateException("Unitialized image") : null;
        }

        @Override
        public int getFrameCount() {
            return 1;
        }

        @Override
        public PlatformImage getFrame(int n2) {
            return this.image;
        }

        @Override
        public int getFrameDelay(int n2) {
            return 0;
        }

        @Override
        public int getLoopCount() {
            return 0;
        }

        @Override
        public int getWidth() {
            return this.image.getWidth();
        }

        @Override
        public int getHeight() {
            return this.image.getHeight();
        }

        @Override
        public void factoryReset() {
            this.dispose();
        }

        @Override
        public void factoryReleased() {
            this.dispose();
        }
    }
}

