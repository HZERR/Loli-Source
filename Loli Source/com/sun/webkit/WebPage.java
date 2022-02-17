/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.webkit.BackForwardList;
import com.sun.webkit.Disposer;
import com.sun.webkit.InputMethodClient;
import com.sun.webkit.InspectorClient;
import com.sun.webkit.Invoker;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.PolicyClient;
import com.sun.webkit.ThemeClient;
import com.sun.webkit.UIClient;
import com.sun.webkit.WCFrameView;
import com.sun.webkit.WCWidget;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.event.WCFocusEvent;
import com.sun.webkit.event.WCInputMethodEvent;
import com.sun.webkit.event.WCKeyEvent;
import com.sun.webkit.event.WCMouseEvent;
import com.sun.webkit.event.WCMouseWheelEvent;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;
import com.sun.webkit.graphics.WCSize;
import com.sun.webkit.network.CookieManager;
import com.sun.webkit.network.URLs;
import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import netscape.javascript.JSException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class WebPage {
    private static final Logger log = Logger.getLogger(WebPage.class.getName());
    private static final Logger paintLog = Logger.getLogger(WebPage.class.getName() + ".paint");
    private static final int MAX_FRAME_QUEUE_SIZE = 10;
    private long pPage = 0L;
    private boolean isDisposed = false;
    private int width;
    private int height;
    private int fontSmoothingType;
    private final WCFrameView hostWindow;
    private final Set<Long> frames = new HashSet<Long>();
    private final AccessControlContext accessControlContext;
    private final Map<Integer, String> requestURLs = new HashMap<Integer, String>();
    private final Set<Integer> requestStarted = new HashSet<Integer>();
    private static final ReentrantLock PAGE_LOCK = new ReentrantLock();
    private final Queue<RenderFrame> frameQueue = new LinkedList<RenderFrame>();
    private RenderFrame currentFrame = new RenderFrame();
    private int updateContentCycleID;
    private static boolean firstWebPageCreated;
    private WCPageBackBuffer backbuffer;
    private List<WCRectangle> dirtyRects = new LinkedList<WCRectangle>();
    private final WebPageClient pageClient;
    private final UIClient uiClient;
    private final PolicyClient policyClient;
    private InputMethodClient imClient;
    private final List<LoadListenerClient> loadListenerClients = new LinkedList<LoadListenerClient>();
    private final InspectorClient inspectorClient;
    private final RenderTheme renderTheme;
    private final ScrollBarTheme scrollbarTheme;
    public static final int DND_DST_ENTER = 0;
    public static final int DND_DST_OVER = 1;
    public static final int DND_DST_CHANGE = 2;
    public static final int DND_DST_EXIT = 3;
    public static final int DND_DST_DROP = 4;
    public static final int DND_SRC_ENTER = 100;
    public static final int DND_SRC_OVER = 101;
    public static final int DND_SRC_CHANGE = 102;
    public static final int DND_SRC_EXIT = 103;
    public static final int DND_SRC_DROP = 104;

    private static void collectJSCGarbages() {
        Invoker.getInvoker().checkEventThread();
        Disposer.addRecord(new Object(), WebPage::collectJSCGarbages);
        WebPage.twkDoJSCGarbageCollection();
    }

    public WebPage(WebPageClient webPageClient, UIClient uIClient, PolicyClient policyClient, InspectorClient inspectorClient, ThemeClient themeClient, boolean bl) {
        Invoker.getInvoker().checkEventThread();
        this.pageClient = webPageClient;
        this.uiClient = uIClient;
        this.policyClient = policyClient;
        this.inspectorClient = inspectorClient;
        if (themeClient != null) {
            this.renderTheme = themeClient.createRenderTheme();
            this.scrollbarTheme = themeClient.createScrollBarTheme();
        } else {
            this.renderTheme = null;
            this.scrollbarTheme = null;
        }
        this.accessControlContext = AccessController.getContext();
        this.hostWindow = new WCFrameView(this);
        this.pPage = this.twkCreatePage(bl);
        this.twkInit(this.pPage, false, WCGraphicsManager.getGraphicsManager().getDevicePixelScale());
        if (webPageClient != null && webPageClient.isBackBufferSupported()) {
            this.backbuffer = webPageClient.createBackBuffer();
            this.backbuffer.ref();
        }
        if (!firstWebPageCreated) {
            Disposer.addRecord(new Object(), WebPage::collectJSCGarbages);
            firstWebPageCreated = true;
        }
    }

    long getPage() {
        return this.pPage;
    }

    private WCWidget getHostWindow() {
        return this.hostWindow;
    }

    public AccessControlContext getAccessControlContext() {
        return this.accessControlContext;
    }

    static boolean lockPage() {
        return Invoker.getInvoker().lock(PAGE_LOCK);
    }

    static boolean unlockPage() {
        return Invoker.getInvoker().unlock(PAGE_LOCK);
    }

    private void addDirtyRect(WCRectangle wCRectangle) {
        if (wCRectangle.getWidth() <= 0.0f || wCRectangle.getHeight() <= 0.0f) {
            return;
        }
        Iterator<WCRectangle> iterator = this.dirtyRects.iterator();
        while (iterator.hasNext()) {
            WCRectangle wCRectangle2 = iterator.next();
            if (wCRectangle2.contains(wCRectangle)) {
                return;
            }
            if (wCRectangle.contains(wCRectangle2)) {
                iterator.remove();
                continue;
            }
            WCRectangle wCRectangle3 = wCRectangle2.createUnion(wCRectangle);
            if (wCRectangle3.getIntWidth() * wCRectangle3.getIntHeight() >= wCRectangle2.getIntWidth() * wCRectangle2.getIntHeight() + wCRectangle.getIntWidth() * wCRectangle.getIntHeight()) continue;
            iterator.remove();
            wCRectangle = wCRectangle3;
        }
        this.dirtyRects.add(wCRectangle);
    }

    public boolean isDirty() {
        WebPage.lockPage();
        try {
            boolean bl = !this.dirtyRects.isEmpty();
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void updateDirty(WCRectangle wCRectangle) {
        Object object;
        Object object2;
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.log(Level.FINEST, "Entering, dirtyRects: {0}, currentFrame: {1}", new Object[]{this.dirtyRects, this.currentFrame});
        }
        if (this.isDisposed || this.width <= 0 || this.height <= 0) {
            this.dirtyRects.clear();
            return;
        }
        if (wCRectangle == null) {
            wCRectangle = new WCRectangle(0.0f, 0.0f, this.width, this.height);
        }
        List<WCRectangle> list = this.dirtyRects;
        this.dirtyRects = new LinkedList<WCRectangle>();
        this.twkPrePaint(this.getPage());
        while (!list.isEmpty()) {
            object2 = list.remove(0).intersection(wCRectangle);
            if (((WCRectangle)object2).getWidth() <= 0.0f || ((WCRectangle)object2).getHeight() <= 0.0f) continue;
            paintLog.log(Level.FINEST, "Updating: {0}", object2);
            object = WCGraphicsManager.getGraphicsManager().createRenderQueue((WCRectangle)object2, true);
            this.twkUpdateContent(this.getPage(), (WCRenderQueue)object, ((WCRectangle)object2).getIntX() - 1, ((WCRectangle)object2).getIntY() - 1, ((WCRectangle)object2).getIntWidth() + 2, ((WCRectangle)object2).getIntHeight() + 2);
            this.currentFrame.addRenderQueue((WCRenderQueue)object);
        }
        object2 = WCGraphicsManager.getGraphicsManager().createRenderQueue(wCRectangle, false);
        this.twkPostPaint(this.getPage(), (WCRenderQueue)object2, wCRectangle.getIntX(), wCRectangle.getIntY(), wCRectangle.getIntWidth(), wCRectangle.getIntHeight());
        this.currentFrame.addRenderQueue((WCRenderQueue)object2);
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.log(Level.FINEST, "Dirty rects processed, dirtyRects: {0}, currentFrame: {1}", new Object[]{this.dirtyRects, this.currentFrame});
        }
        if (this.currentFrame.getRQList().size() > 0) {
            object2 = this.frameQueue;
            synchronized (object2) {
                paintLog.log(Level.FINEST, "About to update frame queue, frameQueue: {0}", this.frameQueue);
                object = this.frameQueue.iterator();
                block4: while (object.hasNext()) {
                    RenderFrame renderFrame = (RenderFrame)object.next();
                    for (WCRenderQueue wCRenderQueue : this.currentFrame.getRQList()) {
                        WCRectangle wCRectangle2 = wCRenderQueue.getClip();
                        if (!wCRenderQueue.isOpaque() || !wCRectangle2.contains(renderFrame.getEnclosingRect())) continue;
                        paintLog.log(Level.FINEST, "Dropping: {0}", renderFrame);
                        renderFrame.drop();
                        object.remove();
                        continue block4;
                    }
                }
                this.frameQueue.add(this.currentFrame);
                this.currentFrame = new RenderFrame();
                if (this.frameQueue.size() > 10) {
                    paintLog.log(Level.FINEST, "Frame queue exceeded maximum size, clearing and requesting full repaint");
                    this.dropRenderFrames();
                    this.repaintAll();
                }
                paintLog.log(Level.FINEST, "Frame queue updated, frameQueue: {0}", this.frameQueue);
            }
        }
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.log(Level.FINEST, "Exiting, dirtyRects: {0}, currentFrame: {1}", new Object[]{this.dirtyRects, this.currentFrame});
        }
    }

    private void scroll(int n2, int n3, int n4, int n5, int n6, int n7) {
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.finest("rect=[" + n2 + ", " + n3 + " " + n4 + "x" + n5 + "] delta=[" + n6 + ", " + n7 + "]");
        }
        if (Math.abs(n6 += this.currentFrame.scrollDx) < n4 && Math.abs(n7 += this.currentFrame.scrollDy) < n5) {
            int n8 = n6 >= 0 ? n2 : n2 - n6;
            int n9 = n7 >= 0 ? n3 : n3 - n7;
            int n10 = n6 == 0 ? n4 : n4 - Math.abs(n6);
            int n11 = n7 == 0 ? n5 : n5 - Math.abs(n7);
            WCRenderQueue wCRenderQueue = WCGraphicsManager.getGraphicsManager().createRenderQueue(new WCRectangle(0.0f, 0.0f, this.width, this.height), false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(32).order(ByteOrder.nativeOrder()).putInt(40).putInt(this.backbuffer.getID()).putInt(n8).putInt(n9).putInt(n10).putInt(n11).putInt(n6).putInt(n7);
            byteBuffer.flip();
            wCRenderQueue.addBuffer(byteBuffer);
            this.currentFrame.drop();
            this.currentFrame.addRenderQueue(wCRenderQueue);
            this.currentFrame.scrollDx = n6;
            this.currentFrame.scrollDy = n7;
            if (!this.dirtyRects.isEmpty()) {
                WCRectangle wCRectangle = new WCRectangle(n2, n3, n4, n5);
                for (WCRectangle wCRectangle2 : this.dirtyRects) {
                    if (!wCRectangle.contains(wCRectangle2)) continue;
                    if (paintLog.isLoggable(Level.FINEST)) {
                        paintLog.log(Level.FINEST, "translating old dirty rect by the delta: " + wCRectangle2);
                    }
                    wCRectangle2.translate(n6, n7);
                }
            }
        }
        this.addDirtyRect(new WCRectangle(n2, n7 >= 0 ? (float)n3 : (float)(n3 + n5 + n7), n4, Math.abs(n7)));
        this.addDirtyRect(new WCRectangle(n6 >= 0 ? (float)n2 : (float)(n2 + n4 + n6), n3, Math.abs(n6), n5 - Math.abs(n7)));
    }

    public WebPageClient getPageClient() {
        return this.pageClient;
    }

    public void setInputMethodClient(InputMethodClient inputMethodClient) {
        this.imClient = inputMethodClient;
    }

    public void setInputMethodState(boolean bl) {
        if (this.imClient != null) {
            this.imClient.activateInputMethods(bl);
        }
    }

    public void addLoadListenerClient(LoadListenerClient loadListenerClient) {
        if (!this.loadListenerClients.contains(loadListenerClient)) {
            this.loadListenerClients.add(loadListenerClient);
        }
    }

    private RenderTheme getRenderTheme() {
        return this.renderTheme;
    }

    private static RenderTheme fwkGetDefaultRenderTheme() {
        return ThemeClient.getDefaultRenderTheme();
    }

    private ScrollBarTheme getScrollBarTheme() {
        return this.scrollbarTheme;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setBounds(int n2, int n3, int n4, int n5) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "setBounds: " + n2 + " " + n3 + " " + n4 + " " + n5);
            if (this.isDisposed) {
                log.log(Level.FINE, "setBounds() request for a disposed web page.");
                return;
            }
            this.width = n4;
            this.height = n5;
            this.twkSetBounds(this.getPage(), 0, 0, n4, n5);
            this.repaintAll();
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setOpaque(long l2, boolean bl) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "setOpaque: " + bl);
            if (this.isDisposed) {
                log.log(Level.FINE, "setOpaque() request for a disposed web page.");
                return;
            }
            if (!this.frames.contains(l2)) {
                return;
            }
            this.twkSetTransparent(l2, !bl);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setBackgroundColor(long l2, int n2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "setBackgroundColor: " + n2);
            if (this.isDisposed) {
                log.log(Level.FINE, "setBackgroundColor() request for a disposed web page.");
                return;
            }
            if (!this.frames.contains(l2)) {
                return;
            }
            this.twkSetBackgroundColor(l2, n2);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setBackgroundColor(int n2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "setBackgroundColor: " + n2 + " for all frames");
            if (this.isDisposed) {
                log.log(Level.FINE, "setBackgroundColor() request for a disposed web page.");
                return;
            }
            for (long l2 : this.frames) {
                this.twkSetBackgroundColor(l2, n2);
            }
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void updateContent(WCRectangle wCRectangle) {
        WebPage.lockPage();
        try {
            ++this.updateContentCycleID;
            paintLog.log(Level.FINEST, "toPaint: {0}", wCRectangle);
            if (this.isDisposed) {
                paintLog.fine("updateContent() request for a disposed web page.");
                return;
            }
            this.updateDirty(wCRectangle);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public int getUpdateContentCycleID() {
        return this.updateContentCycleID;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isRepaintPending() {
        WebPage.lockPage();
        try {
            Queue<RenderFrame> queue = this.frameQueue;
            synchronized (queue) {
                boolean bl = !this.frameQueue.isEmpty();
                return bl;
            }
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void print(WCGraphicsContext wCGraphicsContext, int n2, int n3, int n4, int n5) {
        WebPage.lockPage();
        try {
            WCRenderQueue wCRenderQueue = WCGraphicsManager.getGraphicsManager().createRenderQueue(new WCRectangle(n2, n3, n4, n5), true);
            FutureTask<Object> futureTask = new FutureTask<Object>(() -> this.twkUpdateContent(this.getPage(), wCRenderQueue, n2, n3, n4, n5), null);
            Invoker.getInvoker().invokeOnEventThread(futureTask);
            try {
                futureTask.get();
            }
            catch (ExecutionException executionException) {
                throw new AssertionError((Object)executionException);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
            wCRenderQueue.decode(wCGraphicsContext);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void paint(WCGraphicsContext wCGraphicsContext, int n2, int n3, int n4, int n5) {
        block10: {
            WebPage.lockPage();
            try {
                if (this.pageClient != null && this.pageClient.isBackBufferSupported()) {
                    if (!this.backbuffer.validate(this.width, this.height)) {
                        Invoker.getInvoker().invokeOnEventThread(() -> this.repaintAll());
                        return;
                    }
                    WCGraphicsContext wCGraphicsContext2 = this.backbuffer.createGraphics();
                    try {
                        this.paint2GC(wCGraphicsContext2);
                        wCGraphicsContext2.flush();
                    }
                    finally {
                        this.backbuffer.disposeGraphics(wCGraphicsContext2);
                    }
                    this.backbuffer.flush(wCGraphicsContext, n2, n3, n4, n5);
                    break block10;
                }
                this.paint2GC(wCGraphicsContext);
            }
            finally {
                WebPage.unlockPage();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void paint2GC(WCGraphicsContext wCGraphicsContext) {
        ArrayList<RenderFrame> arrayList;
        paintLog.finest("Entering");
        wCGraphicsContext.setFontSmoothingType(this.fontSmoothingType);
        Queue<RenderFrame> queue = this.frameQueue;
        synchronized (queue) {
            arrayList = new ArrayList<RenderFrame>(this.frameQueue);
            this.frameQueue.clear();
        }
        paintLog.log(Level.FINEST, "Frames to render: {0}", arrayList);
        for (RenderFrame renderFrame : arrayList) {
            paintLog.log(Level.FINEST, "Rendering: {0}", renderFrame);
            for (WCRenderQueue wCRenderQueue : renderFrame.getRQList()) {
                wCGraphicsContext.saveState();
                if (wCRenderQueue.getClip() != null) {
                    wCGraphicsContext.setClip(wCRenderQueue.getClip());
                }
                wCRenderQueue.decode(wCGraphicsContext);
                wCGraphicsContext.restoreState();
            }
        }
        paintLog.finest("Exiting");
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void dropRenderFrames() {
        WebPage.lockPage();
        try {
            this.currentFrame.drop();
            Queue<RenderFrame> queue = this.frameQueue;
            synchronized (queue) {
                RenderFrame renderFrame = this.frameQueue.poll();
                while (renderFrame != null) {
                    renderFrame.drop();
                    renderFrame = this.frameQueue.poll();
                }
            }
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void dispatchFocusEvent(WCFocusEvent wCFocusEvent) {
        WebPage.lockPage();
        try {
            log.log(Level.FINEST, "dispatchFocusEvent: " + wCFocusEvent);
            if (this.isDisposed) {
                log.log(Level.FINE, "Focus event for a disposed web page.");
                return;
            }
            this.twkProcessFocusEvent(this.getPage(), wCFocusEvent.getID(), wCFocusEvent.getDirection());
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean dispatchKeyEvent(WCKeyEvent wCKeyEvent) {
        WebPage.lockPage();
        try {
            log.log(Level.FINEST, "dispatchKeyEvent: " + wCKeyEvent);
            if (this.isDisposed) {
                log.log(Level.FINE, "Key event for a disposed web page.");
                boolean bl = false;
                return bl;
            }
            if (WCKeyEvent.filterEvent(wCKeyEvent)) {
                log.log(Level.FINEST, "filtered");
                boolean bl = false;
                return bl;
            }
            boolean bl = this.twkProcessKeyEvent(this.getPage(), wCKeyEvent.getType(), wCKeyEvent.getText(), wCKeyEvent.getKeyIdentifier(), wCKeyEvent.getWindowsVirtualKeyCode(), wCKeyEvent.isShiftDown(), wCKeyEvent.isCtrlDown(), wCKeyEvent.isAltDown(), wCKeyEvent.isMetaDown(), (double)wCKeyEvent.getWhen() / 1000.0);
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean dispatchMouseEvent(WCMouseEvent wCMouseEvent) {
        WebPage.lockPage();
        try {
            log.log(Level.FINEST, "dispatchMouseEvent: " + wCMouseEvent.getX() + "," + wCMouseEvent.getY());
            if (this.isDisposed) {
                log.log(Level.FINE, "Mouse event for a disposed web page.");
                boolean bl = false;
                return bl;
            }
            boolean bl = !this.isDragConfirmed() && this.twkProcessMouseEvent(this.getPage(), wCMouseEvent.getID(), wCMouseEvent.getButton(), wCMouseEvent.getClickCount(), wCMouseEvent.getX(), wCMouseEvent.getY(), wCMouseEvent.getScreenX(), wCMouseEvent.getScreenY(), wCMouseEvent.isShiftDown(), wCMouseEvent.isControlDown(), wCMouseEvent.isAltDown(), wCMouseEvent.isMetaDown(), wCMouseEvent.isPopupTrigger(), (double)wCMouseEvent.getWhen() / 1000.0);
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean dispatchMouseWheelEvent(WCMouseWheelEvent wCMouseWheelEvent) {
        WebPage.lockPage();
        try {
            log.log(Level.FINEST, "dispatchMouseWheelEvent: " + wCMouseWheelEvent);
            if (this.isDisposed) {
                log.log(Level.FINE, "MouseWheel event for a disposed web page.");
                boolean bl = false;
                return bl;
            }
            boolean bl = this.twkProcessMouseWheelEvent(this.getPage(), wCMouseWheelEvent.getX(), wCMouseWheelEvent.getY(), wCMouseWheelEvent.getScreenX(), wCMouseWheelEvent.getScreenY(), wCMouseWheelEvent.getDeltaX(), wCMouseWheelEvent.getDeltaY(), wCMouseWheelEvent.isShiftDown(), wCMouseWheelEvent.isControlDown(), wCMouseWheelEvent.isAltDown(), wCMouseWheelEvent.isMetaDown(), (double)wCMouseWheelEvent.getWhen() / 1000.0);
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean dispatchInputMethodEvent(WCInputMethodEvent wCInputMethodEvent) {
        WebPage.lockPage();
        try {
            log.log(Level.FINEST, "dispatchInputMethodEvent: " + wCInputMethodEvent);
            if (this.isDisposed) {
                log.log(Level.FINE, "InputMethod event for a disposed web page.");
                boolean bl = false;
                return bl;
            }
            switch (wCInputMethodEvent.getID()) {
                case 0: {
                    boolean bl = this.twkProcessInputTextChange(this.getPage(), wCInputMethodEvent.getComposed(), wCInputMethodEvent.getCommitted(), wCInputMethodEvent.getAttributes(), wCInputMethodEvent.getCaretPosition());
                    return bl;
                }
                case 1: {
                    boolean bl = this.twkProcessCaretPositionChange(this.getPage(), wCInputMethodEvent.getCaretPosition());
                    return bl;
                }
            }
            boolean bl = false;
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int dispatchDragOperation(int n2, String[] arrstring, String[] arrstring2, int n3, int n4, int n5, int n6, int n7) {
        WebPage.lockPage();
        try {
            log.log(Level.FINEST, "dispatchDragOperation: " + n3 + "," + n4 + " dndCommand:" + n2 + " dndAction" + n7);
            if (this.isDisposed) {
                log.log(Level.FINE, "DnD event for a disposed web page.");
                int n8 = 0;
                return n8;
            }
            int n9 = this.twkProcessDrag(this.getPage(), n2, arrstring, arrstring2, n3, n4, n5, n6, n7);
            return n9;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void confirmStartDrag() {
        if (this.uiClient != null) {
            this.uiClient.confirmStartDrag();
        }
    }

    public boolean isDragConfirmed() {
        return this.uiClient != null ? this.uiClient.isDragConfirmed() : false;
    }

    public int[] getClientTextLocation(int n2) {
        WebPage.lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientTextLocation() request for a disposed web page.");
                int[] arrn = new int[]{0, 0, 0, 0};
                return arrn;
            }
            int[] arrn = this.twkGetTextLocation(this.getPage(), n2);
            return arrn;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getClientLocationOffset(int n2, int n3) {
        WebPage.lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientLocationOffset() request for a disposed web page.");
                int n4 = 0;
                return n4;
            }
            int n5 = this.twkGetInsertPositionOffset(this.getPage());
            return n5;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public int getClientInsertPositionOffset() {
        WebPage.lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientInsertPositionOffset() request for a disposed web page.");
                int n2 = 0;
                return n2;
            }
            int n3 = this.twkGetInsertPositionOffset(this.getPage());
            return n3;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public int getClientCommittedTextLength() {
        WebPage.lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientCommittedTextOffset() request for a disposed web page.");
                int n2 = 0;
                return n2;
            }
            int n3 = this.twkGetCommittedTextLength(this.getPage());
            return n3;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public String getClientCommittedText() {
        WebPage.lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientCommittedText() request for a disposed web page.");
                String string = "";
                return string;
            }
            String string = this.twkGetCommittedText(this.getPage());
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public String getClientSelectedText() {
        WebPage.lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientSelectedText() request for a disposed web page.");
                String string = "";
                return string;
            }
            String string = this.twkGetSelectedText(this.getPage());
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void dispose() {
        WebPage.lockPage();
        try {
            log.log(Level.FINER, "dispose");
            this.stop();
            this.dropRenderFrames();
            this.isDisposed = true;
            this.twkDestroyPage(this.pPage);
            this.pPage = 0L;
            for (long l2 : this.frames) {
                log.log(Level.FINE, "Undestroyed frame view: " + l2);
            }
            this.frames.clear();
            if (this.backbuffer != null) {
                this.backbuffer.deref();
                this.backbuffer = null;
            }
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getName(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get Name: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "getName() request for a disposed web page.");
                String string = null;
                return string;
            }
            if (!this.frames.contains(l2)) {
                String string = null;
                return string;
            }
            String string = this.twkGetName(l2);
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getURL(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get URL: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "getURL() request for a disposed web page.");
                String string = null;
                return string;
            }
            if (!this.frames.contains(l2)) {
                String string = null;
                return string;
            }
            String string = this.twkGetURL(l2);
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public String getEncoding() {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get encoding");
            if (this.isDisposed) {
                log.log(Level.FINE, "getEncoding() request for a disposed web page.");
                String string = null;
                return string;
            }
            String string = this.twkGetEncoding(this.getPage());
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setEncoding(String string) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Set encoding: encoding = " + string);
            if (this.isDisposed) {
                log.log(Level.FINE, "setEncoding() request for a disposed web page.");
                return;
            }
            if (string != null && !string.isEmpty()) {
                this.twkSetEncoding(this.getPage(), string);
            }
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getInnerText(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get inner text: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "getInnerText() request for a disposed web page.");
                String string = null;
                return string;
            }
            if (!this.frames.contains(l2)) {
                String string = null;
                return string;
            }
            String string = this.twkGetInnerText(l2);
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getRenderTree(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get render tree: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "getRenderTree() request for a disposed web page.");
                String string = null;
                return string;
            }
            if (!this.frames.contains(l2)) {
                String string = null;
                return string;
            }
            String string = this.twkGetRenderTree(l2);
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getUnloadEventListenersCount(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "frame: " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "request for a disposed web page.");
                int n2 = 0;
                return n2;
            }
            if (!this.frames.contains(l2)) {
                int n3 = 0;
                return n3;
            }
            int n4 = this.twkGetUnloadEventListenersCount(l2);
            return n4;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getContentType(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get content type: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "getContentType() request for a disposed web page.");
                String string = null;
                return string;
            }
            if (!this.frames.contains(l2)) {
                String string = null;
                return string;
            }
            String string = this.twkGetContentType(l2);
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getTitle(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get title: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "getTitle() request for a disposed web page.");
                String string = null;
                return string;
            }
            if (!this.frames.contains(l2)) {
                String string = null;
                return string;
            }
            String string = this.twkGetTitle(l2);
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public WCImage getIcon(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get icon: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "getIcon() request for a disposed web page.");
                WCImage wCImage = null;
                return wCImage;
            }
            if (!this.frames.contains(l2)) {
                WCImage wCImage = null;
                return wCImage;
            }
            String string = this.twkGetIconURL(l2);
            if (string != null && !string.isEmpty()) {
                WCImage wCImage = WCGraphicsManager.getGraphicsManager().getIconImage(string);
                return wCImage;
            }
            WCImage wCImage = null;
            return wCImage;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void open(long l2, String string) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Open URL: " + string);
            if (this.isDisposed) {
                log.log(Level.FINE, "open() request for a disposed web page.");
                return;
            }
            if (!this.frames.contains(l2)) {
                return;
            }
            if (this.twkIsLoading(l2)) {
                Invoker.getInvoker().postOnEventThread(() -> this.twkOpen(l2, string));
            } else {
                this.twkOpen(l2, string);
            }
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void load(long l2, String string, String string2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Load text: " + string);
            if (string == null) {
                return;
            }
            if (this.isDisposed) {
                log.log(Level.FINE, "load() request for a disposed web page.");
                return;
            }
            if (!this.frames.contains(l2)) {
                return;
            }
            if (this.twkIsLoading(l2)) {
                Invoker.getInvoker().postOnEventThread(() -> this.twkLoad(l2, string, string2));
            } else {
                this.twkLoad(l2, string, string2);
            }
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stop(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Stop loading: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "cancel() request for a disposed web page.");
                return;
            }
            if (!this.frames.contains(l2)) {
                return;
            }
            String string = this.twkGetURL(l2);
            String string2 = this.twkGetContentType(l2);
            this.twkStop(l2);
            this.fireLoadEvent(l2, 6, string, string2, 1.0, 0);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void stop() {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Stop loading sync");
            if (this.isDisposed) {
                log.log(Level.FINE, "stopAll() request for a disposed web page.");
                return;
            }
            this.twkStopAll(this.getPage());
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void refresh(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Refresh: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "refresh() request for a disposed web page.");
                return;
            }
            if (!this.frames.contains(l2)) {
                return;
            }
            this.twkRefresh(l2);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public BackForwardList createBackForwardList() {
        return new BackForwardList(this);
    }

    public boolean goBack() {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Go back");
            if (this.isDisposed) {
                log.log(Level.FINE, "goBack() request for a disposed web page.");
                boolean bl = false;
                return bl;
            }
            boolean bl = this.twkGoBackForward(this.getPage(), -1);
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean goForward() {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Go forward");
            if (this.isDisposed) {
                log.log(Level.FINE, "goForward() request for a disposed web page.");
                boolean bl = false;
                return bl;
            }
            boolean bl = this.twkGoBackForward(this.getPage(), 1);
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean copy() {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Copy");
            if (this.isDisposed) {
                log.log(Level.FINE, "copy() request for a disposed web page.");
                boolean bl = false;
                return bl;
            }
            long l2 = this.getMainFrame();
            if (!this.frames.contains(l2)) {
                boolean bl = false;
                return bl;
            }
            boolean bl = this.twkCopy(l2);
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean find(String string, boolean bl, boolean bl2, boolean bl3) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Find in page: stringToFind = " + string + ", " + (bl ? "forward" : "backward") + (bl2 ? ", wrap" : "") + (bl3 ? ", matchCase" : ""));
            if (this.isDisposed) {
                log.log(Level.FINE, "find() request for a disposed web page.");
                boolean bl4 = false;
                return bl4;
            }
            boolean bl5 = this.twkFindInPage(this.getPage(), string, bl, bl2, bl3);
            return bl5;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean find(long l2, String string, boolean bl, boolean bl2, boolean bl3) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Find in frame: stringToFind = " + string + ", " + (bl ? "forward" : "backward") + (bl2 ? ", wrap" : "") + (bl3 ? ", matchCase" : ""));
            if (this.isDisposed) {
                log.log(Level.FINE, "find() request for a disposed web page.");
                boolean bl4 = false;
                return bl4;
            }
            if (!this.frames.contains(l2)) {
                boolean bl5 = false;
                return bl5;
            }
            boolean bl6 = this.twkFindInFrame(l2, string, bl, bl2, bl3);
            return bl6;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void overridePreference(String string, String string2) {
        WebPage.lockPage();
        try {
            this.twkOverridePreference(this.getPage(), string, string2);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void resetToConsistentStateBeforeTesting() {
        WebPage.lockPage();
        try {
            this.twkResetToConsistentStateBeforeTesting(this.getPage());
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public float getZoomFactor(boolean bl) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get zoom factor, textOnly=" + bl);
            if (this.isDisposed) {
                log.log(Level.FINE, "getZoomFactor() request for a disposed web page.");
                float f2 = 1.0f;
                return f2;
            }
            long l2 = this.getMainFrame();
            if (!this.frames.contains(l2)) {
                float f3 = 1.0f;
                return f3;
            }
            float f4 = this.twkGetZoomFactor(l2, bl);
            return f4;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setZoomFactor(float f2, boolean bl) {
        WebPage.lockPage();
        try {
            log.fine(String.format("Set zoom factor %.2f, textOnly=%b", Float.valueOf(f2), bl));
            if (this.isDisposed) {
                log.log(Level.FINE, "setZoomFactor() request for a disposed web page.");
                return;
            }
            long l2 = this.getMainFrame();
            if (l2 == 0L || !this.frames.contains(l2)) {
                return;
            }
            this.twkSetZoomFactor(l2, f2, bl);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setFontSmoothingType(int n2) {
        this.fontSmoothingType = n2;
        this.repaintAll();
    }

    public void reset(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Reset: frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "reset() request for a disposed web page.");
                return;
            }
            if (l2 == 0L || !this.frames.contains(l2)) {
                return;
            }
            this.twkReset(l2);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Object executeScript(long l2, String string) throws JSException {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "execute script: \"" + string + "\" in frame = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "executeScript() request for a disposed web page.");
                Object var4_3 = null;
                return var4_3;
            }
            if (l2 == 0L || !this.frames.contains(l2)) {
                Object var4_4 = null;
                return var4_4;
            }
            Object object = this.twkExecuteScript(l2, string);
            return object;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public long getMainFrame() {
        WebPage.lockPage();
        try {
            log.log(Level.FINER, "getMainFrame: page = " + this.pPage);
            if (this.isDisposed) {
                log.log(Level.FINE, "getMainFrame() request for a disposed web page.");
                long l2 = 0L;
                return l2;
            }
            long l3 = this.twkGetMainFrame(this.getPage());
            log.log(Level.FINER, "Main frame = " + l3);
            this.frames.add(l3);
            long l4 = l3;
            return l4;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public long getParentFrame(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "getParentFrame: child = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "getParentFrame() request for a disposed web page.");
                long l3 = 0L;
                return l3;
            }
            if (!this.frames.contains(l2)) {
                long l4 = 0L;
                return l4;
            }
            long l5 = this.twkGetParentFrame(l2);
            return l5;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public List<Long> getChildFrames(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "getChildFrames: parent = " + l2);
            if (this.isDisposed) {
                log.log(Level.FINE, "getChildFrames() request for a disposed web page.");
                List<Long> list = null;
                return list;
            }
            if (!this.frames.contains(l2)) {
                List<Long> list = null;
                return list;
            }
            long[] arrl = this.twkGetChildFrames(l2);
            LinkedList<Long> linkedList = new LinkedList<Long>();
            for (long l3 : arrl) {
                linkedList.add(l3);
            }
            LinkedList<Long> linkedList2 = linkedList;
            return linkedList2;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public WCRectangle getVisibleRect(long l2) {
        WebPage.lockPage();
        try {
            if (!this.frames.contains(l2)) {
                WCRectangle wCRectangle = null;
                return wCRectangle;
            }
            int[] arrn = this.twkGetVisibleRect(l2);
            if (arrn != null) {
                WCRectangle wCRectangle = new WCRectangle(arrn[0], arrn[1], arrn[2], arrn[3]);
                return wCRectangle;
            }
            WCRectangle wCRectangle = null;
            return wCRectangle;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void scrollToPosition(long l2, WCPoint wCPoint) {
        WebPage.lockPage();
        try {
            if (!this.frames.contains(l2)) {
                return;
            }
            this.twkScrollToPosition(l2, wCPoint.getIntX(), wCPoint.getIntY());
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public WCSize getContentSize(long l2) {
        WebPage.lockPage();
        try {
            if (!this.frames.contains(l2)) {
                WCSize wCSize = null;
                return wCSize;
            }
            int[] arrn = this.twkGetContentSize(l2);
            if (arrn != null) {
                WCSize wCSize = new WCSize(arrn[0], arrn[1]);
                return wCSize;
            }
            WCSize wCSize = null;
            return wCSize;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Document getDocument(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "getDocument");
            if (this.isDisposed) {
                log.log(Level.FINE, "getDocument() request for a disposed web page.");
                Document document = null;
                return document;
            }
            if (!this.frames.contains(l2)) {
                Document document = null;
                return document;
            }
            Document document = WebPage.twkGetDocument(l2);
            return document;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Element getOwnerElement(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "getOwnerElement");
            if (this.isDisposed) {
                log.log(Level.FINE, "getOwnerElement() request for a disposed web page.");
                Element element = null;
                return element;
            }
            if (!this.frames.contains(l2)) {
                Element element = null;
                return element;
            }
            Element element = WebPage.twkGetOwnerElement(l2);
            return element;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean executeCommand(String string, String string2) {
        WebPage.lockPage();
        try {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "command: [{0}], value: [{1}]", new Object[]{string, string2});
            }
            if (this.isDisposed) {
                log.log(Level.FINE, "Web page is already disposed");
                boolean bl = false;
                return bl;
            }
            boolean bl = this.twkExecuteCommand(this.getPage(), string, string2);
            log.log(Level.FINE, "result: [{0}]", bl);
            boolean bl2 = bl;
            return bl2;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean queryCommandEnabled(String string) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "command: [{0}]", string);
            if (this.isDisposed) {
                log.log(Level.FINE, "Web page is already disposed");
                boolean bl = false;
                return bl;
            }
            boolean bl = this.twkQueryCommandEnabled(this.getPage(), string);
            log.log(Level.FINE, "result: [{0}]", bl);
            boolean bl2 = bl;
            return bl2;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean queryCommandState(String string) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "command: [{0}]", string);
            if (this.isDisposed) {
                log.log(Level.FINE, "Web page is already disposed");
                boolean bl = false;
                return bl;
            }
            boolean bl = this.twkQueryCommandState(this.getPage(), string);
            log.log(Level.FINE, "result: [{0}]", bl);
            boolean bl2 = bl;
            return bl2;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String queryCommandValue(String string) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "command: [{0}]", string);
            if (this.isDisposed) {
                log.log(Level.FINE, "Web page is already disposed");
                String string2 = null;
                return string2;
            }
            String string3 = this.twkQueryCommandValue(this.getPage(), string);
            log.log(Level.FINE, "result: [{0}]", string3);
            String string4 = string3;
            return string4;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean isEditable() {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "isEditable");
            if (this.isDisposed) {
                log.log(Level.FINE, "isEditable() request for a disposed web page.");
                boolean bl = false;
                return bl;
            }
            boolean bl = this.twkIsEditable(this.getPage());
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setEditable(boolean bl) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "setEditable");
            if (this.isDisposed) {
                log.log(Level.FINE, "setEditable() request for a disposed web page.");
                return;
            }
            this.twkSetEditable(this.getPage(), bl);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String getHtml(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "getHtml");
            if (this.isDisposed) {
                log.log(Level.FINE, "getHtml() request for a disposed web page.");
                String string = null;
                return string;
            }
            if (!this.frames.contains(l2)) {
                String string = null;
                return string;
            }
            String string = this.twkGetHtml(l2);
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int beginPrinting(float f2, float f3) {
        WebPage.lockPage();
        try {
            if (this.isDisposed) {
                log.warning("beginPrinting() called for a disposed web page.");
                int n2 = 0;
                return n2;
            }
            AtomicReference<Integer> atomicReference = new AtomicReference<Integer>(0);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Invoker.getInvoker().invokeOnEventThread(() -> {
                try {
                    int n2 = this.twkBeginPrinting(this.getPage(), f2, f3);
                    atomicReference.set(n2);
                }
                finally {
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                throw new RuntimeException(interruptedException);
            }
            int n3 = atomicReference.get();
            return n3;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void endPrinting() {
        WebPage.lockPage();
        try {
            if (this.isDisposed) {
                log.warning("endPrinting() called for a disposed web page.");
                return;
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Invoker.getInvoker().invokeOnEventThread(() -> {
                try {
                    this.twkEndPrinting(this.getPage());
                }
                finally {
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                throw new RuntimeException(interruptedException);
            }
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void print(WCGraphicsContext wCGraphicsContext, int n2, float f2) {
        WebPage.lockPage();
        try {
            if (this.isDisposed) {
                log.warning("print() called for a disposed web page.");
                return;
            }
            WCRenderQueue wCRenderQueue = WCGraphicsManager.getGraphicsManager().createRenderQueue(null, true);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Invoker.getInvoker().invokeOnEventThread(() -> {
                try {
                    this.twkPrint(this.getPage(), wCRenderQueue, n2, f2);
                }
                finally {
                    countDownLatch.countDown();
                }
            });
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                wCRenderQueue.dispose();
                WebPage.unlockPage();
                return;
            }
            wCRenderQueue.decode(wCGraphicsContext);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public int getPageHeight() {
        return this.getFrameHeight(this.getMainFrame());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getFrameHeight(long l2) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Get page height");
            if (this.isDisposed) {
                log.log(Level.FINE, "getFrameHeight() request for a disposed web page.");
                int n2 = 0;
                return n2;
            }
            if (!this.frames.contains(l2)) {
                int n3 = 0;
                return n3;
            }
            int n4 = this.twkGetFrameHeight(l2);
            log.log(Level.FINE, "Height = " + n4);
            int n5 = n4;
            return n5;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public float adjustFrameHeight(long l2, float f2, float f3, float f4) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Adjust page height");
            if (this.isDisposed) {
                log.log(Level.FINE, "adjustFrameHeight() request for a disposed web page.");
                float f5 = 0.0f;
                return f5;
            }
            if (!this.frames.contains(l2)) {
                float f6 = 0.0f;
                return f6;
            }
            float f7 = this.twkAdjustFrameHeight(l2, f2, f3, f4);
            return f7;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean getUsePageCache() {
        WebPage.lockPage();
        try {
            boolean bl = this.twkGetUsePageCache(this.getPage());
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setUsePageCache(boolean bl) {
        WebPage.lockPage();
        try {
            this.twkSetUsePageCache(this.getPage(), bl);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean getDeveloperExtrasEnabled() {
        WebPage.lockPage();
        try {
            boolean bl = this.twkGetDeveloperExtrasEnabled(this.getPage());
            log.log(Level.FINE, "Getting developerExtrasEnabled, result: [{0}]", bl);
            boolean bl2 = bl;
            return bl2;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setDeveloperExtrasEnabled(boolean bl) {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Setting developerExtrasEnabled, value: [{0}]", bl);
            this.twkSetDeveloperExtrasEnabled(this.getPage(), bl);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean isJavaScriptEnabled() {
        WebPage.lockPage();
        try {
            boolean bl = this.twkIsJavaScriptEnabled(this.getPage());
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setJavaScriptEnabled(boolean bl) {
        WebPage.lockPage();
        try {
            this.twkSetJavaScriptEnabled(this.getPage(), bl);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public boolean isContextMenuEnabled() {
        WebPage.lockPage();
        try {
            boolean bl = this.twkIsContextMenuEnabled(this.getPage());
            return bl;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setContextMenuEnabled(boolean bl) {
        WebPage.lockPage();
        try {
            this.twkSetContextMenuEnabled(this.getPage(), bl);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setUserStyleSheetLocation(String string) {
        WebPage.lockPage();
        try {
            this.twkSetUserStyleSheetLocation(this.getPage(), string);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public String getUserAgent() {
        WebPage.lockPage();
        try {
            String string = this.twkGetUserAgent(this.getPage());
            return string;
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setUserAgent(String string) {
        WebPage.lockPage();
        try {
            this.twkSetUserAgent(this.getPage(), string);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setLocalStorageDatabasePath(String string) {
        WebPage.lockPage();
        try {
            this.twkSetLocalStorageDatabasePath(this.getPage(), string);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void setLocalStorageEnabled(boolean bl) {
        WebPage.lockPage();
        try {
            this.twkSetLocalStorageEnabled(this.getPage(), bl);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void connectInspectorFrontend() {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Connecting inspector frontend");
            this.twkConnectInspectorFrontend(this.getPage());
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void disconnectInspectorFrontend() {
        WebPage.lockPage();
        try {
            log.log(Level.FINE, "Disconnecting inspector frontend");
            this.twkDisconnectInspectorFrontend(this.getPage());
        }
        finally {
            WebPage.unlockPage();
        }
    }

    public void dispatchInspectorMessageFromFrontend(String string) {
        WebPage.lockPage();
        try {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Dispatching inspector message from frontend, message: [{0}]", string);
            }
            this.twkDispatchInspectorMessageFromFrontend(this.getPage(), string);
        }
        finally {
            WebPage.unlockPage();
        }
    }

    private void fwkFrameCreated(long l2) {
        log.log(Level.FINE, "Frame created: frame = " + l2);
        if (this.frames.contains(l2)) {
            log.log(Level.FINE, "Error in fwkFrameCreated: frame is already in frames");
            return;
        }
        this.frames.add(l2);
    }

    private void fwkFrameDestroyed(long l2) {
        log.log(Level.FINE, "Frame destroyed: frame = " + l2);
        if (!this.frames.contains(l2)) {
            log.log(Level.FINE, "Error in fwkFrameDestroyed: frame is not found in frames");
            return;
        }
        this.frames.remove(l2);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void fwkRepaint(int n2, int n3, int n4, int n5) {
        WebPage.lockPage();
        try {
            if (paintLog.isLoggable(Level.FINEST)) {
                paintLog.log(Level.FINEST, "x: {0}, y: {1}, w: {2}, h: {3}", new Object[]{n2, n3, n4, n5});
            }
            this.addDirtyRect(new WCRectangle(n2, n3, n4, n5));
        }
        finally {
            WebPage.unlockPage();
        }
    }

    private void fwkScroll(int n2, int n3, int n4, int n5, int n6, int n7) {
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.finest("Scroll: " + n2 + " " + n3 + " " + n4 + " " + n5 + "  " + n6 + " " + n7);
        }
        if (this.pageClient == null || !this.pageClient.isBackBufferSupported()) {
            paintLog.finest("blit scrolling is switched off");
            return;
        }
        this.scroll(n2, n3, n4, n5, n6, n7);
    }

    private void fwkTransferFocus(boolean bl) {
        log.log(Level.FINER, "Transfer focus " + (bl ? "forward" : "backward"));
        if (this.pageClient != null) {
            this.pageClient.transferFocus(bl);
        }
    }

    private void fwkSetCursor(long l2) {
        log.log(Level.FINER, "Set cursor: " + l2);
        if (this.pageClient != null) {
            this.pageClient.setCursor(l2);
        }
    }

    private void fwkSetFocus(boolean bl) {
        log.log(Level.FINER, "Set focus: " + (bl ? "true" : "false"));
        if (this.pageClient != null) {
            this.pageClient.setFocus(bl);
        }
    }

    private void fwkSetTooltip(String string) {
        log.log(Level.FINER, "Set tooltip: " + string);
        if (this.pageClient != null) {
            this.pageClient.setTooltip(string);
        }
    }

    private void fwkPrint() {
        log.log(Level.FINER, "Print");
        if (this.uiClient != null) {
            this.uiClient.print();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void fwkSetRequestURL(long l2, int n2, String string) {
        log.log(Level.FINER, "Set request URL: id = " + n2 + ", url = " + string);
        Map<Integer, String> map = this.requestURLs;
        synchronized (map) {
            this.requestURLs.put(n2, string);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void fwkRemoveRequestURL(long l2, int n2) {
        log.log(Level.FINER, "Set request URL: id = " + n2);
        Map<Integer, String> map = this.requestURLs;
        synchronized (map) {
            this.requestURLs.remove(n2);
            this.requestStarted.remove(n2);
        }
    }

    private WebPage fwkCreateWindow(boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        log.log(Level.FINER, "Create window");
        if (this.uiClient != null) {
            return this.uiClient.createPage(bl, bl2, bl3, bl4);
        }
        return null;
    }

    private void fwkShowWindow() {
        log.log(Level.FINER, "Show window");
        if (this.uiClient != null) {
            this.uiClient.showView();
        }
    }

    private void fwkCloseWindow() {
        log.log(Level.FINER, "Close window");
        if (this.permitCloseWindowAction() && this.uiClient != null) {
            this.uiClient.closePage();
        }
    }

    private WCRectangle fwkGetWindowBounds() {
        WCRectangle wCRectangle;
        log.log(Level.FINE, "Get window bounds");
        if (this.uiClient != null && (wCRectangle = this.uiClient.getViewBounds()) != null) {
            return wCRectangle;
        }
        return this.fwkGetPageBounds();
    }

    private void fwkSetWindowBounds(int n2, int n3, int n4, int n5) {
        log.log(Level.FINER, "Set window bounds: " + n2 + " " + n3 + " " + n4 + " " + n5);
        if (this.uiClient != null) {
            this.uiClient.setViewBounds(new WCRectangle(n2, n3, n4, n5));
        }
    }

    private WCRectangle fwkGetPageBounds() {
        log.log(Level.FINER, "Get page bounds");
        return new WCRectangle(0.0f, 0.0f, this.width, this.height);
    }

    private void fwkSetScrollbarsVisible(boolean bl) {
    }

    private void fwkSetStatusbarText(String string) {
        log.log(Level.FINER, "Set statusbar text: " + string);
        if (this.uiClient != null) {
            this.uiClient.setStatusbarText(string);
        }
    }

    private String[] fwkChooseFile(String string, boolean bl, String string2) {
        log.log(Level.FINER, "Choose file, initial=" + string);
        return this.uiClient != null ? this.uiClient.chooseFile(string, bl, string2) : null;
    }

    private void fwkStartDrag(Object object, int n2, int n3, int n4, int n5, String[] arrstring, Object[] arrobject) {
        log.log(Level.FINER, "Start drag: ");
        if (this.uiClient != null) {
            this.uiClient.startDrag(WCImage.getImage(object), n2, n3, n4, n5, arrstring, arrobject);
        }
    }

    private WCPoint fwkScreenToWindow(WCPoint wCPoint) {
        log.log(Level.FINER, "fwkScreenToWindow");
        if (this.pageClient != null) {
            return this.pageClient.screenToWindow(wCPoint);
        }
        return wCPoint;
    }

    private WCPoint fwkWindowToScreen(WCPoint wCPoint) {
        log.log(Level.FINER, "fwkWindowToScreen");
        if (this.pageClient != null) {
            return this.pageClient.windowToScreen(wCPoint);
        }
        return wCPoint;
    }

    private void fwkAlert(String string) {
        log.log(Level.FINE, "JavaScript alert(): text = " + string);
        if (this.uiClient != null) {
            this.uiClient.alert(string);
        }
    }

    private boolean fwkConfirm(String string) {
        log.log(Level.FINE, "JavaScript confirm(): text = " + string);
        if (this.uiClient != null) {
            return this.uiClient.confirm(string);
        }
        return false;
    }

    private String fwkPrompt(String string, String string2) {
        log.log(Level.FINE, "JavaScript prompt(): text = " + string + ", default = " + string2);
        if (this.uiClient != null) {
            return this.uiClient.prompt(string, string2);
        }
        return null;
    }

    private void fwkAddMessageToConsole(String string, int n2, String string2) {
        log.log(Level.FINE, "fwkAddMessageToConsole(): message = " + string + ", lineNumber = " + n2 + ", sourceId = " + string2);
        if (this.pageClient != null) {
            this.pageClient.addMessageToConsole(string, n2, string2);
        }
    }

    private void fwkFireLoadEvent(long l2, int n2, String string, String string2, double d2, int n3) {
        log.log(Level.FINER, "Load event: pFrame = " + l2 + ", state = " + n2 + ", url = " + string + ", contenttype=" + string2 + ", progress = " + d2 + ", error = " + n3);
        this.fireLoadEvent(l2, n2, string, string2, d2, n3);
    }

    private void fwkFireResourceLoadEvent(long l2, int n2, int n3, String string, double d2, int n4) {
        log.log(Level.FINER, "Resource load event: pFrame = " + l2 + ", state = " + n2 + ", id = " + n3 + ", contenttype=" + string + ", progress = " + d2 + ", error = " + n4);
        String string2 = this.requestURLs.get(n3);
        if (string2 == null) {
            log.log(Level.FINE, "Error in fwkFireResourceLoadEvent: unknown request id " + n3);
            return;
        }
        int n5 = n2;
        if (n2 == 20) {
            if (this.requestStarted.contains(n3)) {
                n5 = 21;
            } else {
                this.requestStarted.add(n3);
            }
        }
        this.fireResourceLoadEvent(l2, n5, string2, string, d2, n4);
    }

    private boolean fwkPermitNavigateAction(long l2, String string) {
        log.log(Level.FINE, "Policy: permit NAVIGATE: pFrame = " + l2 + ", url = " + string);
        if (this.policyClient != null) {
            return this.policyClient.permitNavigateAction(l2, this.str2url(string));
        }
        return true;
    }

    private boolean fwkPermitRedirectAction(long l2, String string) {
        log.log(Level.FINE, "Policy: permit REDIRECT: pFrame = " + l2 + ", url = " + string);
        if (this.policyClient != null) {
            return this.policyClient.permitRedirectAction(l2, this.str2url(string));
        }
        return true;
    }

    private boolean fwkPermitAcceptResourceAction(long l2, String string) {
        log.log(Level.FINE, "Policy: permit ACCEPT_RESOURCE: pFrame + " + l2 + ", url = " + string);
        if (this.policyClient != null) {
            return this.policyClient.permitAcceptResourceAction(l2, this.str2url(string));
        }
        return true;
    }

    private boolean fwkPermitSubmitDataAction(long l2, String string, String string2, boolean bl) {
        log.log(Level.FINE, "Policy: permit " + (bl ? "" : "RE") + "SUBMIT_DATA: pFrame = " + l2 + ", url = " + string + ", httpMethod = " + string2);
        if (this.policyClient != null) {
            if (bl) {
                return this.policyClient.permitSubmitDataAction(l2, this.str2url(string), string2);
            }
            return this.policyClient.permitResubmitDataAction(l2, this.str2url(string), string2);
        }
        return true;
    }

    private boolean fwkPermitEnableScriptsAction(long l2, String string) {
        log.log(Level.FINE, "Policy: permit ENABLE_SCRIPTS: pFrame + " + l2 + ", url = " + string);
        if (this.policyClient != null) {
            return this.policyClient.permitEnableScriptsAction(l2, this.str2url(string));
        }
        return true;
    }

    private boolean fwkPermitNewWindowAction(long l2, String string) {
        log.log(Level.FINE, "Policy: permit NEW_PAGE: pFrame = " + l2 + ", url = " + string);
        if (this.policyClient != null) {
            return this.policyClient.permitNewPageAction(l2, this.str2url(string));
        }
        return true;
    }

    private boolean permitCloseWindowAction() {
        log.log(Level.FINE, "Policy: permit CLOSE_PAGE");
        if (this.policyClient != null) {
            return this.policyClient.permitClosePageAction(this.getMainFrame());
        }
        return true;
    }

    private void fwkRepaintAll() {
        log.log(Level.FINE, "Repainting the entire page");
        this.repaintAll();
    }

    private boolean fwkSendInspectorMessageToFrontend(String string) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Sending inspector message to frontend, message: [{0}]", string);
        }
        boolean bl = false;
        if (this.inspectorClient != null) {
            log.log(Level.FINE, "Invoking inspector client");
            bl = this.inspectorClient.sendMessageToFrontend(string);
        }
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Result: [{0}]", bl);
        }
        return bl;
    }

    public static int getWorkerThreadCount() {
        return WebPage.twkWorkerThreadCount();
    }

    private static native int twkWorkerThreadCount();

    private void fwkDidClearWindowObject(long l2, long l3) {
        if (this.pageClient != null) {
            this.pageClient.didClearWindowObject(l2, l3);
        }
    }

    private URL str2url(String string) {
        try {
            return URLs.newURL(string);
        }
        catch (MalformedURLException malformedURLException) {
            log.log(Level.FINE, "Exception while converting \"" + string + "\" to URL", malformedURLException);
            return null;
        }
    }

    private void fireLoadEvent(long l2, int n2, String string, String string2, double d2, int n3) {
        for (LoadListenerClient loadListenerClient : this.loadListenerClients) {
            loadListenerClient.dispatchLoadEvent(l2, n2, string, string2, d2, n3);
        }
    }

    private void fireResourceLoadEvent(long l2, int n2, String string, String string2, double d2, int n3) {
        for (LoadListenerClient loadListenerClient : this.loadListenerClients) {
            loadListenerClient.dispatchResourceLoadEvent(l2, n2, string, string2, d2, n3);
        }
    }

    private void repaintAll() {
        this.dirtyRects.clear();
        this.addDirtyRect(new WCRectangle(0.0f, 0.0f, this.width, this.height));
    }

    private static native void twkInitWebCore(boolean var0, boolean var1);

    private native long twkCreatePage(boolean var1);

    private native void twkInit(long var1, boolean var3, float var4);

    private native void twkDestroyPage(long var1);

    private native long twkGetMainFrame(long var1);

    private native long twkGetParentFrame(long var1);

    private native long[] twkGetChildFrames(long var1);

    private native String twkGetName(long var1);

    private native String twkGetURL(long var1);

    private native String twkGetInnerText(long var1);

    private native String twkGetRenderTree(long var1);

    private native String twkGetContentType(long var1);

    private native String twkGetTitle(long var1);

    private native String twkGetIconURL(long var1);

    private static native Document twkGetDocument(long var0);

    private static native Element twkGetOwnerElement(long var0);

    private native void twkOpen(long var1, String var3);

    private native void twkOverridePreference(long var1, String var3, String var4);

    private native void twkResetToConsistentStateBeforeTesting(long var1);

    private native void twkLoad(long var1, String var3, String var4);

    private native boolean twkIsLoading(long var1);

    private native void twkStop(long var1);

    private native void twkStopAll(long var1);

    private native void twkRefresh(long var1);

    private native boolean twkGoBackForward(long var1, int var3);

    private native boolean twkCopy(long var1);

    private native boolean twkFindInPage(long var1, String var3, boolean var4, boolean var5, boolean var6);

    private native boolean twkFindInFrame(long var1, String var3, boolean var4, boolean var5, boolean var6);

    private native float twkGetZoomFactor(long var1, boolean var3);

    private native void twkSetZoomFactor(long var1, float var3, boolean var4);

    private native Object twkExecuteScript(long var1, String var3);

    private native void twkReset(long var1);

    private native int twkGetFrameHeight(long var1);

    private native int twkBeginPrinting(long var1, float var3, float var4);

    private native void twkEndPrinting(long var1);

    private native void twkPrint(long var1, WCRenderQueue var3, int var4, float var5);

    private native float twkAdjustFrameHeight(long var1, float var3, float var4, float var5);

    private native int[] twkGetVisibleRect(long var1);

    private native void twkScrollToPosition(long var1, int var3, int var4);

    private native int[] twkGetContentSize(long var1);

    private native void twkSetTransparent(long var1, boolean var3);

    private native void twkSetBackgroundColor(long var1, int var3);

    private native void twkSetBounds(long var1, int var3, int var4, int var5, int var6);

    private native void twkPrePaint(long var1);

    private native void twkUpdateContent(long var1, WCRenderQueue var3, int var4, int var5, int var6, int var7);

    private native void twkPostPaint(long var1, WCRenderQueue var3, int var4, int var5, int var6, int var7);

    private native String twkGetEncoding(long var1);

    private native void twkSetEncoding(long var1, String var3);

    private native void twkProcessFocusEvent(long var1, int var3, int var4);

    private native boolean twkProcessKeyEvent(long var1, int var3, String var4, String var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10, double var11);

    private native boolean twkProcessMouseEvent(long var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10, boolean var11, boolean var12, boolean var13, boolean var14, double var15);

    private native boolean twkProcessMouseWheelEvent(long var1, int var3, int var4, int var5, int var6, float var7, float var8, boolean var9, boolean var10, boolean var11, boolean var12, double var13);

    private native boolean twkProcessInputTextChange(long var1, String var3, String var4, int[] var5, int var6);

    private native boolean twkProcessCaretPositionChange(long var1, int var3);

    private native int[] twkGetTextLocation(long var1, int var3);

    private native int twkGetInsertPositionOffset(long var1);

    private native int twkGetCommittedTextLength(long var1);

    private native String twkGetCommittedText(long var1);

    private native String twkGetSelectedText(long var1);

    private native int twkProcessDrag(long var1, int var3, String[] var4, String[] var5, int var6, int var7, int var8, int var9, int var10);

    private native boolean twkExecuteCommand(long var1, String var3, String var4);

    private native boolean twkQueryCommandEnabled(long var1, String var3);

    private native boolean twkQueryCommandState(long var1, String var3);

    private native String twkQueryCommandValue(long var1, String var3);

    private native boolean twkIsEditable(long var1);

    private native void twkSetEditable(long var1, boolean var3);

    private native String twkGetHtml(long var1);

    private native boolean twkGetUsePageCache(long var1);

    private native void twkSetUsePageCache(long var1, boolean var3);

    private native boolean twkGetDeveloperExtrasEnabled(long var1);

    private native void twkSetDeveloperExtrasEnabled(long var1, boolean var3);

    private native boolean twkIsJavaScriptEnabled(long var1);

    private native void twkSetJavaScriptEnabled(long var1, boolean var3);

    private native boolean twkIsContextMenuEnabled(long var1);

    private native void twkSetContextMenuEnabled(long var1, boolean var3);

    private native void twkSetUserStyleSheetLocation(long var1, String var3);

    private native String twkGetUserAgent(long var1);

    private native void twkSetUserAgent(long var1, String var3);

    private native void twkSetLocalStorageDatabasePath(long var1, String var3);

    private native void twkSetLocalStorageEnabled(long var1, boolean var3);

    private native int twkGetUnloadEventListenersCount(long var1);

    private native void twkConnectInspectorFrontend(long var1);

    private native void twkDisconnectInspectorFrontend(long var1);

    private native void twkDispatchInspectorMessageFromFrontend(long var1, String var3);

    private static native void twkDoJSCGarbageCollection();

    static {
        AccessController.doPrivileged(() -> {
            boolean bl;
            NativeLibLoader.loadLibrary("jfxwebkit");
            log.finer("jfxwebkit loaded");
            if (CookieHandler.getDefault() == null && (bl = Boolean.valueOf(System.getProperty("com.sun.webkit.setDefaultCookieHandler", "true")).booleanValue())) {
                CookieHandler.setDefault(new CookieManager());
            }
            bl = Boolean.valueOf(System.getProperty("com.sun.webkit.useJIT", "true"));
            boolean bl2 = Boolean.valueOf(System.getProperty("com.sun.webkit.useDFGJIT", "true"));
            WebPage.twkInitWebCore(bl, bl2);
            return null;
        });
        firstWebPageCreated = false;
    }

    private static final class RenderFrame {
        private final List<WCRenderQueue> rqList = new LinkedList<WCRenderQueue>();
        private int scrollDx;
        private int scrollDy;
        private final WCRectangle enclosingRect = new WCRectangle();

        private RenderFrame() {
        }

        private void addRenderQueue(WCRenderQueue wCRenderQueue) {
            if (wCRenderQueue.isEmpty()) {
                return;
            }
            this.rqList.add(wCRenderQueue);
            WCRectangle wCRectangle = wCRenderQueue.getClip();
            if (this.enclosingRect.isEmpty()) {
                this.enclosingRect.setFrame(wCRectangle.getX(), wCRectangle.getY(), wCRectangle.getWidth(), wCRectangle.getHeight());
            } else if (!wCRectangle.isEmpty()) {
                WCRectangle.union(this.enclosingRect, wCRectangle, this.enclosingRect);
            }
        }

        private List<WCRenderQueue> getRQList() {
            return this.rqList;
        }

        private WCRectangle getEnclosingRect() {
            return this.enclosingRect;
        }

        private void drop() {
            for (WCRenderQueue wCRenderQueue : this.rqList) {
                wCRenderQueue.dispose();
            }
            this.rqList.clear();
            this.enclosingRect.setFrame(0.0f, 0.0f, 0.0f, 0.0f);
            this.scrollDx = 0;
            this.scrollDy = 0;
        }

        public String toString() {
            return "RenderFrame{rqList=" + this.rqList + ", " + "enclosingRect=" + this.enclosingRect + "}";
        }
    }
}

