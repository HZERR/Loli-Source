/*
 * Decompiled with CFR 0.150.
 */
package javafx.embed.swing;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGExternalNode;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.stage.FocusUngrabEvent;
import com.sun.javafx.stage.WindowHelper;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.lang.reflect.Method;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.FXDnD;
import javafx.embed.swing.SwingCursors;
import javafx.embed.swing.SwingEvents;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;
import javafx.stage.Window;
import javax.swing.JComponent;
import sun.awt.UngrabEvent;
import sun.swing.JLightweightFrame;
import sun.swing.LightweightContent;

public class SwingNode
extends Node {
    private double fxWidth;
    private double fxHeight;
    private int swingPrefWidth;
    private int swingPrefHeight;
    private int swingMaxWidth;
    private int swingMaxHeight;
    private int swingMinWidth;
    private int swingMinHeight;
    private volatile JComponent content;
    private volatile JLightweightFrame lwFrame;
    private NGExternalNode peer;
    private final ReentrantLock paintLock = new ReentrantLock();
    private boolean skipBackwardUnrgabNotification;
    private boolean grabbed;
    private volatile int scale = 1;
    private static final OptionalMethod<JLightweightFrame> jlfNotifyDisplayChanged = new OptionalMethod<JLightweightFrame>(JLightweightFrame.class, "notifyDisplayChanged", Integer.TYPE);
    private List<Runnable> peerRequests = new ArrayList<Runnable>();
    private final InvalidationListener locationListener = observable -> this.locateLwFrame();
    private final EventHandler<FocusUngrabEvent> ungrabHandler = focusUngrabEvent -> {
        if (!this.skipBackwardUnrgabNotification && this.lwFrame != null) {
            AccessController.doPrivileged(new PostEventAction(new UngrabEvent(this.lwFrame)));
        }
    };
    private final ChangeListener<Boolean> windowVisibleListener = (observableValue, bl, bl2) -> {
        if (!bl2.booleanValue()) {
            this.disposeLwFrame();
        } else {
            this.setContent(this.content);
        }
    };
    private final ChangeListener<Window> sceneWindowListener = (observableValue, window, window2) -> {
        if (window != null) {
            this.removeWindowListeners((Window)window);
        }
        if (window2 != null) {
            this.addWindowListeners((Window)window2);
        }
    };
    private static final OptionalMethod<JLightweightFrame> jlfSetHostBounds = new OptionalMethod<JLightweightFrame>(JLightweightFrame.class, "setHostBounds", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);

    final JLightweightFrame getLightweightFrame() {
        return this.lwFrame;
    }

    public SwingNode() {
        this.setFocusTraversable(true);
        this.setEventHandler(javafx.scene.input.MouseEvent.ANY, new SwingMouseEventHandler());
        this.setEventHandler(KeyEvent.ANY, new SwingKeyEventHandler());
        this.setEventHandler(ScrollEvent.SCROLL, new SwingScrollEventHandler());
        this.focusedProperty().addListener((observableValue, bl, bl2) -> this.activateLwFrame((boolean)bl2));
        Font.getFamilies();
    }

    public void setContent(JComponent jComponent) {
        this.content = jComponent;
        SwingFXUtils.runOnEDT(() -> this.setContentImpl(jComponent));
    }

    public JComponent getContent() {
        return this.content;
    }

    private void setContentImpl(JComponent jComponent) {
        if (this.lwFrame != null) {
            this.lwFrame.dispose();
            this.lwFrame = null;
        }
        if (jComponent != null) {
            this.lwFrame = new JLightweightFrame();
            this.lwFrame.addWindowFocusListener(new WindowFocusListener(){

                @Override
                public void windowGainedFocus(WindowEvent windowEvent) {
                    SwingFXUtils.runOnFxThread(() -> SwingNode.this.requestFocus());
                }

                @Override
                public void windowLostFocus(WindowEvent windowEvent) {
                    SwingFXUtils.runOnFxThread(() -> SwingNode.this.ungrabFocus(true));
                }
            });
            jlfNotifyDisplayChanged.invoke(this.lwFrame, this.scale);
            this.lwFrame.setContent(new SwingNodeContent(jComponent));
            this.lwFrame.setVisible(true);
            SwingFXUtils.runOnFxThread(() -> {
                this.locateLwFrame();
                if (this.focusedProperty().get()) {
                    this.activateLwFrame(true);
                }
            });
        }
    }

    void setImageBuffer(int[] arrn, int n2, int n3, int n4, int n5, int n6, int n7) {
        Runnable runnable = () -> {
            Window window = this.getScene().getWindow();
            float f2 = WindowHelper.getWindowAccessor().getUIScale(window);
            this.peer.setImageBuffer(IntBuffer.wrap(arrn), n2, n3, n4, n5, (float)n4 / f2, (float)n5 / f2, n6, n7);
        };
        SwingFXUtils.runOnFxThread(() -> {
            if (this.peer != null) {
                runnable.run();
            } else {
                this.peerRequests.clear();
                this.peerRequests.add(runnable);
            }
        });
    }

    void setImageBounds(int n2, int n3, int n4, int n5) {
        Runnable runnable = () -> {
            Window window = this.getScene().getWindow();
            float f2 = WindowHelper.getWindowAccessor().getUIScale(window);
            this.peer.setImageBounds(n2, n3, n4, n5, (float)n4 / f2, (float)n5 / f2);
        };
        SwingFXUtils.runOnFxThread(() -> {
            if (this.peer != null) {
                runnable.run();
            } else {
                this.peerRequests.add(runnable);
            }
        });
    }

    void repaintDirtyRegion(int n2, int n3, int n4, int n5) {
        Runnable runnable = () -> {
            this.peer.repaintDirtyRegion(n2, n3, n4, n5);
            this.impl_markDirty(DirtyBits.NODE_CONTENTS);
        };
        SwingFXUtils.runOnFxThread(() -> {
            if (this.peer != null) {
                runnable.run();
            } else {
                this.peerRequests.add(runnable);
            }
        });
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public void resize(double d2, double d3) {
        super.resize(d2, d3);
        if (d2 != this.fxWidth || d3 != this.fxHeight) {
            this.fxWidth = d2;
            this.fxHeight = d3;
            this.impl_geomChanged();
            this.impl_markDirty(DirtyBits.NODE_GEOMETRY);
            SwingFXUtils.runOnEDT(() -> {
                if (this.lwFrame != null) {
                    this.locateLwFrame();
                }
            });
        }
    }

    @Override
    public double prefWidth(double d2) {
        float f2 = WindowHelper.getWindowAccessor().getUIScale(this.getScene().getWindow());
        return (float)this.swingPrefWidth / f2;
    }

    @Override
    public double prefHeight(double d2) {
        float f2 = WindowHelper.getWindowAccessor().getUIScale(this.getScene().getWindow());
        return (float)this.swingPrefHeight / f2;
    }

    @Override
    public double maxWidth(double d2) {
        float f2 = WindowHelper.getWindowAccessor().getUIScale(this.getScene().getWindow());
        return (float)this.swingMaxWidth / f2;
    }

    @Override
    public double maxHeight(double d2) {
        float f2 = WindowHelper.getWindowAccessor().getUIScale(this.getScene().getWindow());
        return (float)this.swingMaxHeight / f2;
    }

    @Override
    public double minWidth(double d2) {
        float f2 = WindowHelper.getWindowAccessor().getUIScale(this.getScene().getWindow());
        return (float)this.swingMinWidth / f2;
    }

    @Override
    public double minHeight(double d2) {
        float f2 = WindowHelper.getWindowAccessor().getUIScale(this.getScene().getWindow());
        return (float)this.swingMinHeight / f2;
    }

    @Override
    @Deprecated
    protected boolean impl_computeContains(double d2, double d3) {
        return true;
    }

    private void removeSceneListeners(Scene scene) {
        Window window = scene.getWindow();
        if (window != null) {
            this.removeWindowListeners(window);
        }
        scene.windowProperty().removeListener(this.sceneWindowListener);
    }

    private void addSceneListeners(Scene scene) {
        Window window = scene.getWindow();
        if (window != null) {
            this.addWindowListeners(window);
        }
        scene.windowProperty().addListener(this.sceneWindowListener);
    }

    private void addWindowListeners(Window window) {
        window.xProperty().addListener(this.locationListener);
        window.yProperty().addListener(this.locationListener);
        window.addEventHandler(FocusUngrabEvent.FOCUS_UNGRAB, this.ungrabHandler);
        window.showingProperty().addListener(this.windowVisibleListener);
        this.scale = Math.round(WindowHelper.getWindowAccessor().getRenderScale(window));
        this.setLwFrameScale(this.scale);
    }

    private void removeWindowListeners(Window window) {
        window.xProperty().removeListener(this.locationListener);
        window.yProperty().removeListener(this.locationListener);
        window.removeEventHandler(FocusUngrabEvent.FOCUS_UNGRAB, this.ungrabHandler);
        window.showingProperty().removeListener(this.windowVisibleListener);
    }

    @Override
    @Deprecated
    protected NGNode impl_createPeer() {
        this.peer = new NGExternalNode();
        this.peer.setLock(this.paintLock);
        for (Runnable runnable : this.peerRequests) {
            runnable.run();
        }
        this.peerRequests = null;
        if (this.getScene() != null) {
            this.addSceneListeners(this.getScene());
        }
        this.sceneProperty().addListener((observableValue, scene, scene2) -> {
            if (scene != null) {
                this.removeSceneListeners((Scene)scene);
                this.disposeLwFrame();
            }
            if (scene2 != null) {
                if (this.content != null && this.lwFrame == null) {
                    this.setContent(this.content);
                }
                this.addSceneListeners((Scene)scene2);
            }
        });
        this.impl_treeVisibleProperty().addListener((observableValue, bl, bl2) -> this.setLwFrameVisible((boolean)bl2));
        return this.peer;
    }

    @Override
    @Deprecated
    public void impl_updatePeer() {
        super.impl_updatePeer();
        if (this.impl_isDirty(DirtyBits.NODE_VISIBLE) || this.impl_isDirty(DirtyBits.NODE_BOUNDS)) {
            this.locateLwFrame();
        }
        if (this.impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            this.peer.markContentDirty();
        }
    }

    private void locateLwFrame() {
        if (this.getScene() == null || this.lwFrame == null || this.getScene().getWindow() == null || !this.getScene().getWindow().isShowing()) {
            return;
        }
        Window window = this.getScene().getWindow();
        float f2 = WindowHelper.getWindowAccessor().getRenderScale(window);
        float f3 = WindowHelper.getWindowAccessor().getUIScale(window);
        int n2 = Math.round(f2);
        boolean bl = this.scale != n2;
        this.scale = n2;
        Point2D point2D = this.localToScene(0.0, 0.0);
        int n3 = (int)(window.getX() * (double)f3);
        int n4 = (int)(window.getY() * (double)f3);
        int n5 = (int)(window.getWidth() * (double)f3);
        int n6 = (int)(window.getHeight() * (double)f3);
        int n7 = (int)Math.round((window.getX() + this.getScene().getX() + point2D.getX()) * (double)f3);
        int n8 = (int)Math.round((window.getY() + this.getScene().getY() + point2D.getY()) * (double)f3);
        int n9 = (int)(this.fxWidth * (double)f3);
        int n10 = (int)(this.fxHeight * (double)f3);
        SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
                if (bl) {
                    jlfNotifyDisplayChanged.invoke(this.lwFrame, this.scale);
                }
                this.lwFrame.setSize(n9, n10);
                this.lwFrame.setLocation(n7, n8);
                jlfSetHostBounds.invoke(this.lwFrame, n3, n4, n5, n6);
            }
        });
    }

    private void activateLwFrame(boolean bl) {
        if (this.lwFrame == null) {
            return;
        }
        SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
                this.lwFrame.emulateActivation(bl);
            }
        });
    }

    private void disposeLwFrame() {
        if (this.lwFrame == null) {
            return;
        }
        SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
                this.lwFrame.dispose();
                this.lwFrame = null;
            }
        });
    }

    private void setLwFrameVisible(boolean bl) {
        if (this.lwFrame == null) {
            return;
        }
        SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
                this.lwFrame.setVisible(bl);
            }
        });
    }

    private void setLwFrameScale(final int n2) {
        if (this.lwFrame == null) {
            return;
        }
        SwingFXUtils.runOnEDT(new Runnable(){

            @Override
            public void run() {
                if (SwingNode.this.lwFrame != null) {
                    jlfNotifyDisplayChanged.invoke(SwingNode.this.lwFrame, n2);
                }
            }
        });
    }

    @Override
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform) {
        baseBounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, (float)this.fxWidth, (float)this.fxHeight, 0.0f);
        baseTransform.transform(baseBounds, baseBounds);
        return baseBounds;
    }

    @Override
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm mXNodeAlgorithm, MXNodeAlgorithmContext mXNodeAlgorithmContext) {
        return mXNodeAlgorithm.processLeafNode(this, mXNodeAlgorithmContext);
    }

    private void ungrabFocus(boolean bl) {
        if (PlatformUtil.isLinux()) {
            return;
        }
        if (this.grabbed && this.getScene() != null && this.getScene().getWindow() != null && this.getScene().getWindow().impl_getPeer() != null) {
            this.skipBackwardUnrgabNotification = !bl;
            this.getScene().getWindow().impl_getPeer().ungrabFocus();
            this.skipBackwardUnrgabNotification = false;
            this.grabbed = false;
        }
    }

    private class SwingKeyEventHandler
    implements EventHandler<KeyEvent> {
        private SwingKeyEventHandler() {
        }

        @Override
        public void handle(KeyEvent keyEvent) {
            String string;
            int n2;
            JLightweightFrame jLightweightFrame = SwingNode.this.lwFrame;
            if (jLightweightFrame == null) {
                return;
            }
            if (keyEvent.getCharacter().isEmpty()) {
                return;
            }
            if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.TAB) {
                keyEvent.consume();
            }
            if ((n2 = SwingEvents.fxKeyEventTypeToKeyID(keyEvent)) < 0) {
                return;
            }
            int n3 = SwingEvents.fxKeyModsToKeyMods(keyEvent);
            int n4 = keyEvent.getCode().impl_getCode();
            char c2 = keyEvent.getCharacter().charAt(0);
            if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED && (string = keyEvent.getText()).length() == 1) {
                c2 = string.charAt(0);
            }
            long l2 = System.currentTimeMillis();
            java.awt.event.KeyEvent keyEvent2 = new java.awt.event.KeyEvent(jLightweightFrame, n2, l2, n3, n4, c2);
            AccessController.doPrivileged(new PostEventAction(keyEvent2));
        }
    }

    private class SwingScrollEventHandler
    implements EventHandler<ScrollEvent> {
        private SwingScrollEventHandler() {
        }

        @Override
        public void handle(ScrollEvent scrollEvent) {
            double d2;
            boolean bl;
            JLightweightFrame jLightweightFrame = SwingNode.this.lwFrame;
            if (jLightweightFrame == null) {
                return;
            }
            int n2 = SwingEvents.fxScrollModsToMouseWheelMods(scrollEvent);
            boolean bl2 = bl = (n2 & 0x40) != 0;
            if (!bl && scrollEvent.getDeltaY() != 0.0) {
                this.sendMouseWheelEvent(jLightweightFrame, scrollEvent.getX(), scrollEvent.getY(), n2, scrollEvent.getDeltaY() / scrollEvent.getMultiplierY());
            }
            double d3 = d2 = bl && scrollEvent.getDeltaY() != 0.0 ? scrollEvent.getDeltaY() / scrollEvent.getMultiplierY() : scrollEvent.getDeltaX() / scrollEvent.getMultiplierX();
            if (d2 != 0.0) {
                this.sendMouseWheelEvent(jLightweightFrame, scrollEvent.getX(), scrollEvent.getY(), n2 |= 0x40, d2);
            }
        }

        private void sendMouseWheelEvent(Component component, double d2, double d3, int n2, double d4) {
            int n3 = (int)d4;
            int n4 = (int)Math.signum(d4);
            if ((double)n4 * d4 < 1.0) {
                n3 = n4;
            }
            Window window = SwingNode.this.getScene().getWindow();
            float f2 = WindowHelper.getWindowAccessor().getUIScale(window);
            int n5 = (int)Math.round(d2 * (double)f2);
            int n6 = (int)Math.round(d3 * (double)f2);
            MouseWheelEvent mouseWheelEvent = new MouseWheelEvent(component, 507, System.currentTimeMillis(), n2, n5, n6, 0, 0, 0, false, 0, 1, -n3);
            AccessController.doPrivileged(new PostEventAction(mouseWheelEvent));
        }
    }

    private class SwingMouseEventHandler
    implements EventHandler<javafx.scene.input.MouseEvent> {
        private final Set<MouseButton> mouseClickedAllowed = new HashSet<MouseButton>();

        private SwingMouseEventHandler() {
        }

        @Override
        public void handle(javafx.scene.input.MouseEvent mouseEvent) {
            JLightweightFrame jLightweightFrame = SwingNode.this.lwFrame;
            if (jLightweightFrame == null) {
                return;
            }
            int n2 = SwingEvents.fxMouseEventTypeToMouseID(mouseEvent);
            if (n2 < 0) {
                return;
            }
            mouseEvent.consume();
            EventType<? extends javafx.scene.input.MouseEvent> eventType = mouseEvent.getEventType();
            if (eventType == javafx.scene.input.MouseEvent.MOUSE_PRESSED) {
                this.mouseClickedAllowed.add(mouseEvent.getButton());
            } else if (eventType != javafx.scene.input.MouseEvent.MOUSE_RELEASED) {
                if (eventType == javafx.scene.input.MouseEvent.MOUSE_DRAGGED) {
                    this.mouseClickedAllowed.clear();
                } else if (eventType == javafx.scene.input.MouseEvent.MOUSE_CLICKED) {
                    if (mouseEvent.getClickCount() == 1 && !this.mouseClickedAllowed.contains((Object)mouseEvent.getButton())) {
                        return;
                    }
                    this.mouseClickedAllowed.remove((Object)mouseEvent.getButton());
                }
            }
            int n3 = SwingEvents.fxMouseModsToMouseMods(mouseEvent);
            boolean bl = mouseEvent.isPopupTrigger();
            int n4 = SwingEvents.fxMouseButtonToMouseButton(mouseEvent);
            long l2 = System.currentTimeMillis();
            Window window = SwingNode.this.getScene().getWindow();
            float f2 = WindowHelper.getWindowAccessor().getUIScale(window);
            int n5 = (int)Math.round(mouseEvent.getX() * (double)f2);
            int n6 = (int)Math.round(mouseEvent.getY() * (double)f2);
            int n7 = (int)Math.round(mouseEvent.getScreenX() * (double)f2);
            int n8 = (int)Math.round(mouseEvent.getScreenY() * (double)f2);
            MouseEvent mouseEvent2 = new MouseEvent(jLightweightFrame, n2, l2, n3, n5, n6, n7, n8, mouseEvent.getClickCount(), bl, n4);
            AccessController.doPrivileged(new PostEventAction(mouseEvent2));
        }
    }

    private class PostEventAction
    implements PrivilegedAction<Void> {
        private AWTEvent event;

        public PostEventAction(AWTEvent aWTEvent) {
            this.event = aWTEvent;
        }

        @Override
        public Void run() {
            EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
            eventQueue.postEvent(this.event);
            return null;
        }
    }

    private class SwingNodeContent
    implements LightweightContent {
        private JComponent comp;
        private volatile FXDnD dnd;

        public SwingNodeContent(JComponent jComponent) {
            this.comp = jComponent;
        }

        @Override
        public JComponent getComponent() {
            return this.comp;
        }

        @Override
        public void paintLock() {
            SwingNode.this.paintLock.lock();
        }

        @Override
        public void paintUnlock() {
            SwingNode.this.paintLock.unlock();
        }

        @Override
        public void imageBufferReset(int[] arrn, int n2, int n3, int n4, int n5, int n6) {
            this.imageBufferReset(arrn, n2, n3, n4, n5, n6, 1);
        }

        @Override
        public void imageBufferReset(int[] arrn, int n2, int n3, int n4, int n5, int n6, int n7) {
            SwingNode.this.setImageBuffer(arrn, n2, n3, n4, n5, n6, n7);
        }

        @Override
        public void imageReshaped(int n2, int n3, int n4, int n5) {
            SwingNode.this.setImageBounds(n2, n3, n4, n5);
        }

        @Override
        public void imageUpdated(int n2, int n3, int n4, int n5) {
            SwingNode.this.repaintDirtyRegion(n2, n3, n4, n5);
        }

        @Override
        public void focusGrabbed() {
            SwingFXUtils.runOnFxThread(() -> {
                if (PlatformUtil.isLinux()) {
                    return;
                }
                if (SwingNode.this.getScene() != null && SwingNode.this.getScene().getWindow() != null && SwingNode.this.getScene().getWindow().impl_getPeer() != null) {
                    SwingNode.this.getScene().getWindow().impl_getPeer().grabFocus();
                    SwingNode.this.grabbed = true;
                }
            });
        }

        @Override
        public void focusUngrabbed() {
            SwingFXUtils.runOnFxThread(() -> SwingNode.this.ungrabFocus(false));
        }

        @Override
        public void preferredSizeChanged(int n2, int n3) {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode.this.swingPrefWidth = n2;
                SwingNode.this.swingPrefHeight = n3;
                SwingNode.this.impl_notifyLayoutBoundsChanged();
            });
        }

        @Override
        public void maximumSizeChanged(int n2, int n3) {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode.this.swingMaxWidth = n2;
                SwingNode.this.swingMaxHeight = n3;
                SwingNode.this.impl_notifyLayoutBoundsChanged();
            });
        }

        @Override
        public void minimumSizeChanged(int n2, int n3) {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode.this.swingMinWidth = n2;
                SwingNode.this.swingMinHeight = n3;
                SwingNode.this.impl_notifyLayoutBoundsChanged();
            });
        }

        @Override
        public void setCursor(Cursor cursor) {
            SwingFXUtils.runOnFxThread(() -> SwingNode.this.setCursor(SwingCursors.embedCursorToCursor(cursor)));
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void initDnD() {
            SwingNodeContent swingNodeContent = this;
            synchronized (swingNodeContent) {
                if (this.dnd == null) {
                    this.dnd = new FXDnD(SwingNode.this);
                }
            }
        }

        @Override
        public synchronized <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> class_, DragSource dragSource, Component component, int n2, DragGestureListener dragGestureListener) {
            this.initDnD();
            return this.dnd.createDragGestureRecognizer(class_, dragSource, component, n2, dragGestureListener);
        }

        @Override
        public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException {
            this.initDnD();
            return this.dnd.createDragSourceContextPeer(dragGestureEvent);
        }

        @Override
        public void addDropTarget(DropTarget dropTarget) {
            this.initDnD();
            this.dnd.addDropTarget(dropTarget);
        }

        @Override
        public void removeDropTarget(DropTarget dropTarget) {
            this.initDnD();
            this.dnd.removeDropTarget(dropTarget);
        }
    }

    private static final class OptionalMethod<T> {
        private final Method method;

        public OptionalMethod(Class<T> class_, String string, Class<?> ... arrclass) {
            Method method;
            try {
                method = class_.getMethod(string, arrclass);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                method = null;
            }
            catch (Throwable throwable) {
                throw new RuntimeException("Error when calling " + class_.getName() + ".getMethod('" + string + "').", throwable);
            }
            this.method = method;
        }

        public boolean isSupported() {
            return this.method != null;
        }

        public Object invoke(T t2, Object ... arrobject) {
            if (this.method != null) {
                try {
                    return this.method.invoke(t2, arrobject);
                }
                catch (Throwable throwable) {
                    throw new RuntimeException("Error when calling " + t2.getClass().getName() + "." + this.method.getName() + "().", throwable);
                }
            }
            return null;
        }
    }
}

