/*
 * Decompiled with CFR 0.150.
 */
package javafx.embed.swing;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.embed.EmbeddedSceneInterface;
import com.sun.javafx.embed.EmbeddedStageInterface;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.stage.EmbeddedWindow;
import com.sun.javafx.tk.Toolkit;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.HierarchyEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.im.InputMethodRequests;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Platform;
import javafx.embed.swing.InputMethodSupport;
import javafx.embed.swing.SwingCursors;
import javafx.embed.swing.SwingDnD;
import javafx.embed.swing.SwingEvents;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import sun.awt.AppContext;
import sun.awt.CausedFocusEvent;
import sun.awt.SunToolkit;
import sun.awt.UngrabEvent;
import sun.java2d.SunGraphics2D;
import sun.util.logging.PlatformLogger;

public class JFXPanel
extends JComponent {
    private static final PlatformLogger log = PlatformLogger.getLogger(JFXPanel.class.getName());
    private static AtomicInteger instanceCount = new AtomicInteger(0);
    private static PlatformImpl.FinishListener finishListener;
    private HostContainer hostContainer;
    private volatile EmbeddedWindow stage;
    private volatile Scene scene;
    private SwingDnD dnd;
    private EmbeddedStageInterface stagePeer;
    private EmbeddedSceneInterface scenePeer;
    private int pWidth;
    private int pHeight;
    private int scaleFactor = 1;
    private volatile int pPreferredWidth = -1;
    private volatile int pPreferredHeight = -1;
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private BufferedImage pixelsIm;
    private volatile float opacity = 1.0f;
    private AtomicInteger disableCount = new AtomicInteger(0);
    private boolean isCapturingMouse = false;
    private final AWTEventListener ungrabListener = aWTEvent -> {
        if (aWTEvent instanceof UngrabEvent) {
            SwingFXUtils.runOnFxThread(() -> {
                if (this.stagePeer != null) {
                    this.stagePeer.focusUngrab();
                }
            });
        }
        if (aWTEvent instanceof MouseEvent && aWTEvent.getID() == 501 && aWTEvent.getSource() instanceof Component) {
            Window window;
            Window window2 = SwingUtilities.getWindowAncestor(this);
            Component component = (Component)aWTEvent.getSource();
            Window window3 = window = component instanceof Window ? (Window)component : SwingUtilities.getWindowAncestor(component);
            if (window2 == window) {
                SwingFXUtils.runOnFxThread(() -> {
                    if (this.stagePeer != null) {
                        this.stagePeer.focusUngrab();
                    }
                });
            }
        }
    };

    private synchronized void registerFinishListener() {
        if (instanceCount.getAndIncrement() > 0) {
            return;
        }
        finishListener = new PlatformImpl.FinishListener(){

            @Override
            public void idle(boolean bl) {
            }

            @Override
            public void exitCalled() {
            }
        };
        PlatformImpl.addListener(finishListener);
    }

    private synchronized void deregisterFinishListener() {
        if (instanceCount.decrementAndGet() > 0) {
            return;
        }
        PlatformImpl.removeListener(finishListener);
        finishListener = null;
    }

    private static synchronized void initFx() {
        AccessController.doPrivileged(() -> {
            System.setProperty("glass.win.uiScale", "100%");
            System.setProperty("glass.win.renderScale", "100%");
            return null;
        });
        PlatformImpl.startup(() -> {});
    }

    public JFXPanel() {
        JFXPanel.initFx();
        this.hostContainer = new HostContainer();
        this.enableEvents(231485L);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setScene(Scene scene) {
        if (Toolkit.getToolkit().isFxUserThread()) {
            this.setSceneImpl(scene);
        } else {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            Platform.runLater(() -> {
                this.setSceneImpl(scene);
                countDownLatch.countDown();
            });
            try {
                countDownLatch.await();
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace(System.err);
            }
        }
    }

    private void setSceneImpl(Scene scene) {
        if (this.stage != null && scene == null) {
            this.stage.hide();
            this.stage = null;
        }
        this.scene = scene;
        if (this.stage == null && scene != null) {
            this.stage = new EmbeddedWindow(this.hostContainer);
        }
        if (this.stage != null) {
            this.stage.setScene(scene);
            if (!this.stage.isShowing()) {
                this.stage.show();
            }
        }
    }

    @Override
    public final void setOpaque(boolean bl) {
        if (!bl) {
            super.setOpaque(bl);
        }
    }

    @Override
    public final boolean isOpaque() {
        return false;
    }

    private void sendMouseEventToFX(MouseEvent mouseEvent) {
        boolean bl;
        if (this.scenePeer == null || !this.isFxEnabled()) {
            return;
        }
        switch (mouseEvent.getID()) {
            case 501: 
            case 502: 
            case 506: {
                if (mouseEvent.getButton() <= 3) break;
                return;
            }
        }
        int n2 = mouseEvent.getModifiersEx();
        boolean bl2 = (n2 & 0x400) != 0;
        boolean bl3 = (n2 & 0x800) != 0;
        boolean bl4 = bl = (n2 & 0x1000) != 0;
        if (mouseEvent.getID() == 506) {
            if (!this.isCapturingMouse) {
                return;
            }
        } else if (mouseEvent.getID() == 501) {
            this.isCapturingMouse = true;
        } else if (mouseEvent.getID() == 502) {
            if (!this.isCapturingMouse) {
                return;
            }
            this.isCapturingMouse = bl2 || bl3 || bl;
        } else if (mouseEvent.getID() == 500) {
            return;
        }
        boolean bl5 = false;
        if (mouseEvent.getID() == 501 || mouseEvent.getID() == 502) {
            bl5 = mouseEvent.isPopupTrigger();
        }
        this.scenePeer.mouseEvent(SwingEvents.mouseIDToEmbedMouseType(mouseEvent.getID()), SwingEvents.mouseButtonToEmbedMouseButton(mouseEvent.getButton(), n2), bl2, bl3, bl, mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), (n2 & 0x40) != 0, (n2 & 0x80) != 0, (n2 & 0x200) != 0, (n2 & 0x100) != 0, SwingEvents.getWheelRotation(mouseEvent), bl5);
        if (mouseEvent.isPopupTrigger()) {
            this.scenePeer.menuEvent(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), false);
        }
    }

    @Override
    protected void processMouseEvent(MouseEvent mouseEvent) {
        if (mouseEvent.getID() == 501 && mouseEvent.getButton() == 1 && !this.hasFocus()) {
            this.requestFocus();
        }
        this.sendMouseEventToFX(mouseEvent);
        super.processMouseEvent(mouseEvent);
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent mouseEvent) {
        this.sendMouseEventToFX(mouseEvent);
        super.processMouseMotionEvent(mouseEvent);
    }

    @Override
    protected void processMouseWheelEvent(MouseWheelEvent mouseWheelEvent) {
        this.sendMouseEventToFX(mouseWheelEvent);
        super.processMouseWheelEvent(mouseWheelEvent);
    }

    private void sendKeyEventToFX(KeyEvent keyEvent) {
        char[] arrc;
        if (this.scenePeer == null || !this.isFxEnabled()) {
            return;
        }
        if (keyEvent.getKeyChar() == '\uffff') {
            arrc = new char[]{};
        } else {
            char[] arrc2 = new char[1];
            arrc = arrc2;
            arrc2[0] = SwingEvents.keyCharToEmbedKeyChar(keyEvent.getKeyChar());
        }
        char[] arrc3 = arrc;
        this.scenePeer.keyEvent(SwingEvents.keyIDToEmbedKeyType(keyEvent.getID()), keyEvent.getKeyCode(), arrc3, SwingEvents.keyModifiersToEmbedKeyModifiers(keyEvent.getModifiersEx()));
    }

    @Override
    protected void processKeyEvent(KeyEvent keyEvent) {
        this.sendKeyEventToFX(keyEvent);
        super.processKeyEvent(keyEvent);
    }

    private void sendResizeEventToFX() {
        if (this.stagePeer != null) {
            this.stagePeer.setSize(this.pWidth, this.pHeight);
        }
        if (this.scenePeer != null) {
            this.scenePeer.setSize(this.pWidth, this.pHeight);
        }
    }

    @Override
    protected void processComponentEvent(ComponentEvent componentEvent) {
        switch (componentEvent.getID()) {
            case 101: {
                this.updateComponentSize();
                break;
            }
            case 100: {
                if (!this.updateScreenLocation()) break;
                this.sendMoveEventToFX();
                break;
            }
        }
        super.processComponentEvent(componentEvent);
    }

    private void updateComponentSize() {
        int n2 = this.pWidth;
        int n3 = this.pHeight;
        this.pWidth = Math.max(0, this.getWidth());
        this.pHeight = Math.max(0, this.getHeight());
        if (this.getBorder() != null) {
            Insets insets = this.getBorder().getBorderInsets(this);
            this.pWidth -= insets.left + insets.right;
            this.pHeight -= insets.top + insets.bottom;
        }
        if (n2 != this.pWidth || n3 != this.pHeight) {
            this.resizePixelBuffer(this.scaleFactor);
            this.sendResizeEventToFX();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean updateScreenLocation() {
        Object object = this.getTreeLock();
        synchronized (object) {
            if (this.isShowing()) {
                Point point = this.getLocationOnScreen();
                this.screenX = point.x;
                this.screenY = point.y;
                return true;
            }
        }
        return false;
    }

    private void sendMoveEventToFX() {
        if (this.stagePeer == null) {
            return;
        }
        this.stagePeer.setLocation(this.screenX, this.screenY);
    }

    @Override
    protected void processHierarchyBoundsEvent(HierarchyEvent hierarchyEvent) {
        if (hierarchyEvent.getID() == 1401 && this.updateScreenLocation()) {
            this.sendMoveEventToFX();
        }
        super.processHierarchyBoundsEvent(hierarchyEvent);
    }

    @Override
    protected void processHierarchyEvent(HierarchyEvent hierarchyEvent) {
        if ((hierarchyEvent.getChangeFlags() & 4L) != 0L && this.updateScreenLocation()) {
            this.sendMoveEventToFX();
        }
        super.processHierarchyEvent(hierarchyEvent);
    }

    private void sendFocusEventToFX(FocusEvent focusEvent) {
        int n2;
        if (this.stage == null || this.stagePeer == null || !this.isFxEnabled()) {
            return;
        }
        boolean bl = focusEvent.getID() == 1004;
        int n3 = n2 = bl ? 0 : 3;
        if (bl && focusEvent instanceof CausedFocusEvent) {
            CausedFocusEvent causedFocusEvent = (CausedFocusEvent)focusEvent;
            if (causedFocusEvent.getCause() == CausedFocusEvent.Cause.TRAVERSAL_FORWARD) {
                n2 = 1;
            } else if (causedFocusEvent.getCause() == CausedFocusEvent.Cause.TRAVERSAL_BACKWARD) {
                n2 = 2;
            }
        }
        this.stagePeer.setFocused(bl, n2);
    }

    @Override
    protected void processFocusEvent(FocusEvent focusEvent) {
        this.sendFocusEventToFX(focusEvent);
        super.processFocusEvent(focusEvent);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void resizePixelBuffer(int n2) {
        if (this.pWidth <= 0 || this.pHeight <= 0) {
            this.pixelsIm = null;
        } else {
            BufferedImage bufferedImage = this.pixelsIm;
            this.pixelsIm = new BufferedImage(this.pWidth * n2, this.pHeight * n2, 2);
            if (bufferedImage != null) {
                double d2 = n2 / this.scaleFactor;
                int n3 = (int)Math.round((double)bufferedImage.getWidth() * d2);
                int n4 = (int)Math.round((double)bufferedImage.getHeight() * d2);
                Graphics graphics = this.pixelsIm.getGraphics();
                try {
                    graphics.drawImage(bufferedImage, 0, 0, n3, n4, null);
                }
                finally {
                    graphics.dispose();
                }
            }
        }
    }

    @Override
    protected void processInputMethodEvent(InputMethodEvent inputMethodEvent) {
        if (inputMethodEvent.getID() == 1100) {
            this.sendInputMethodEventToFX(inputMethodEvent);
        }
        super.processInputMethodEvent(inputMethodEvent);
    }

    private void sendInputMethodEventToFX(InputMethodEvent inputMethodEvent) {
        String string = InputMethodSupport.getTextForEvent(inputMethodEvent);
        this.scenePeer.inputMethodEvent(javafx.scene.input.InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, InputMethodSupport.inputMethodEventComposed(string, inputMethodEvent.getCommittedCharacterCount()), string.substring(0, inputMethodEvent.getCommittedCharacterCount()), inputMethodEvent.getCaret().getInsertionIndex());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        if (this.scenePeer == null || this.pixelsIm == null) {
            return;
        }
        DataBufferInt dataBufferInt = (DataBufferInt)this.pixelsIm.getRaster().getDataBuffer();
        int[] arrn = dataBufferInt.getData();
        IntBuffer intBuffer = IntBuffer.wrap(arrn);
        if (!this.scenePeer.getPixels(intBuffer, this.pWidth, this.pHeight)) {
            // empty if block
        }
        Graphics graphics2 = null;
        try {
            Object object;
            graphics2 = graphics.create();
            if (this.opacity < 1.0f && graphics2 instanceof Graphics2D) {
                object = (Graphics2D)graphics2;
                AlphaComposite alphaComposite = AlphaComposite.getInstance(3, this.opacity);
                ((Graphics2D)object).setComposite(alphaComposite);
            }
            if (this.getBorder() != null) {
                object = this.getBorder().getBorderInsets(this);
                graphics2.translate(((Insets)object).left, ((Insets)object).top);
            }
            graphics2.drawImage(this.pixelsIm, 0, 0, this.pWidth, this.pHeight, null);
            int n2 = this.scaleFactor;
            if (graphics instanceof SunGraphics2D) {
                n2 = ((SunGraphics2D)graphics).surfaceData.getDefaultScale();
            }
            if (this.scaleFactor != n2) {
                this.resizePixelBuffer(n2);
                this.scenePeer.setPixelScaleFactor(n2);
                this.scaleFactor = n2;
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        finally {
            if (graphics2 != null) {
                graphics2.dispose();
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (this.isPreferredSizeSet() || this.scenePeer == null) {
            return super.getPreferredSize();
        }
        return new Dimension(this.pPreferredWidth, this.pPreferredHeight);
    }

    private boolean isFxEnabled() {
        return this.disableCount.get() == 0;
    }

    private void setFxEnabled(boolean bl) {
        if (!bl) {
            if (this.disableCount.incrementAndGet() == 1 && this.dnd != null) {
                this.dnd.removeNotify();
            }
        } else {
            if (this.disableCount.get() == 0) {
                return;
            }
            if (this.disableCount.decrementAndGet() == 0 && this.dnd != null) {
                this.dnd.addNotify();
            }
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        this.registerFinishListener();
        AccessController.doPrivileged(() -> {
            this.getToolkit().addAWTEventListener(this.ungrabListener, -2147483632L);
            return null;
        });
        this.updateComponentSize();
        SwingFXUtils.runOnFxThread(() -> {
            if (this.stage != null && !this.stage.isShowing()) {
                this.stage.show();
                this.sendMoveEventToFX();
            }
        });
    }

    @Override
    public InputMethodRequests getInputMethodRequests() {
        if (this.scenePeer == null) {
            return null;
        }
        return new InputMethodSupport.InputMethodRequestsAdapter(this.scenePeer.getInputMethodRequests());
    }

    @Override
    public void removeNotify() {
        SwingFXUtils.runOnFxThread(() -> {
            if (this.stage != null && this.stage.isShowing()) {
                this.stage.hide();
            }
        });
        this.pixelsIm = null;
        this.pWidth = 0;
        this.pHeight = 0;
        super.removeNotify();
        AccessController.doPrivileged(() -> {
            this.getToolkit().removeAWTEventListener(this.ungrabListener);
            return null;
        });
        this.getInputContext().removeNotify(this);
        this.deregisterFinishListener();
    }

    private void invokeOnClientEDT(Runnable runnable) {
        AppContext appContext = SunToolkit.targetToAppContext(this);
        if (appContext == null) {
            if (log.isLoggable(PlatformLogger.Level.FINE)) {
                log.fine("null AppContext encountered!");
            }
            return;
        }
        SunToolkit.postEvent(appContext, new InvocationEvent((Object)this, runnable));
    }

    private class HostContainer
    implements HostInterface {
        private HostContainer() {
        }

        @Override
        public void setEmbeddedStage(EmbeddedStageInterface embeddedStageInterface) {
            JFXPanel.this.stagePeer = embeddedStageInterface;
            if (JFXPanel.this.stagePeer == null) {
                return;
            }
            if (JFXPanel.this.pWidth > 0 && JFXPanel.this.pHeight > 0) {
                JFXPanel.this.stagePeer.setSize(JFXPanel.this.pWidth, JFXPanel.this.pHeight);
            }
            JFXPanel.this.invokeOnClientEDT(() -> {
                if (JFXPanel.this.isFocusOwner()) {
                    JFXPanel.this.stagePeer.setFocused(true, 0);
                }
            });
            JFXPanel.this.sendMoveEventToFX();
        }

        @Override
        public void setEmbeddedScene(EmbeddedSceneInterface embeddedSceneInterface) {
            if (JFXPanel.this.scenePeer == embeddedSceneInterface) {
                return;
            }
            JFXPanel.this.scenePeer = embeddedSceneInterface;
            if (JFXPanel.this.scenePeer == null) {
                JFXPanel.this.invokeOnClientEDT(() -> {
                    if (JFXPanel.this.dnd != null) {
                        JFXPanel.this.dnd.removeNotify();
                        JFXPanel.this.dnd = null;
                    }
                });
                return;
            }
            if (JFXPanel.this.pWidth > 0 && JFXPanel.this.pHeight > 0) {
                JFXPanel.this.scenePeer.setSize(JFXPanel.this.pWidth, JFXPanel.this.pHeight);
            }
            JFXPanel.this.scenePeer.setPixelScaleFactor(JFXPanel.this.scaleFactor);
            JFXPanel.this.invokeOnClientEDT(() -> {
                JFXPanel.this.dnd = new SwingDnD(JFXPanel.this, JFXPanel.this.scenePeer);
                JFXPanel.this.dnd.addNotify();
                if (JFXPanel.this.scenePeer != null) {
                    JFXPanel.this.scenePeer.setDragStartListener(JFXPanel.this.dnd.getDragStartListener());
                }
            });
        }

        @Override
        public boolean requestFocus() {
            return JFXPanel.this.requestFocusInWindow();
        }

        @Override
        public boolean traverseFocusOut(boolean bl) {
            KeyboardFocusManager keyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            if (bl) {
                keyboardFocusManager.focusNextComponent(JFXPanel.this);
            } else {
                keyboardFocusManager.focusPreviousComponent(JFXPanel.this);
            }
            return true;
        }

        @Override
        public void setPreferredSize(int n2, int n3) {
            JFXPanel.this.invokeOnClientEDT(() -> {
                JFXPanel.this.pPreferredWidth = n2;
                JFXPanel.this.pPreferredHeight = n3;
                JFXPanel.this.revalidate();
            });
        }

        @Override
        public void repaint() {
            JFXPanel.this.invokeOnClientEDT(() -> JFXPanel.this.repaint());
        }

        @Override
        public void setEnabled(boolean bl) {
            JFXPanel.this.setFxEnabled(bl);
        }

        @Override
        public void setCursor(CursorFrame cursorFrame) {
            Cursor cursor = this.getPlatformCursor(cursorFrame);
            JFXPanel.this.invokeOnClientEDT(() -> JFXPanel.this.setCursor(cursor));
        }

        private Cursor getPlatformCursor(CursorFrame cursorFrame) {
            Cursor cursor = cursorFrame.getPlatformCursor(Cursor.class);
            if (cursor != null) {
                return cursor;
            }
            Cursor cursor2 = SwingCursors.embedCursorToCursor(cursorFrame);
            cursorFrame.setPlatforCursor(Cursor.class, cursor2);
            return cursor2;
        }

        @Override
        public boolean grabFocus() {
            if (PlatformUtil.isLinux()) {
                return true;
            }
            JFXPanel.this.invokeOnClientEDT(() -> {
                Window window = SwingUtilities.getWindowAncestor(JFXPanel.this);
                if (window != null && JFXPanel.this.getToolkit() instanceof SunToolkit) {
                    ((SunToolkit)JFXPanel.this.getToolkit()).grab(window);
                }
            });
            return true;
        }

        @Override
        public void ungrabFocus() {
            if (PlatformUtil.isLinux()) {
                return;
            }
            JFXPanel.this.invokeOnClientEDT(() -> {
                Window window = SwingUtilities.getWindowAncestor(JFXPanel.this);
                if (window != null && JFXPanel.this.getToolkit() instanceof SunToolkit) {
                    ((SunToolkit)JFXPanel.this.getToolkit()).ungrab(window);
                }
            });
        }
    }
}

