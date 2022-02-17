/*
 * Decompiled with CFR 0.150.
 */
package javafx.stage;

import com.sun.javafx.css.StyleManager;
import com.sun.javafx.stage.WindowEventDispatcher;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.stage.WindowPeerListener;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import com.sun.javafx.util.WeakReferenceQueue;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AllPermission;
import java.util.HashMap;
import java.util.Iterator;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;

public class Window
implements EventTarget {
    private static WeakReferenceQueue<Window> windowQueue = new WeakReferenceQueue();
    final AccessControlContext acc = AccessController.getContext();
    @Deprecated
    protected WindowPeerListener peerListener;
    @Deprecated
    protected volatile TKStage impl_peer;
    private TKBoundsConfigurator peerBoundsConfigurator = new TKBoundsConfigurator();
    private boolean sizeToScene = false;
    private static final float CENTER_ON_SCREEN_X_FRACTION = 0.5f;
    private static final float CENTER_ON_SCREEN_Y_FRACTION = 0.33333334f;
    private boolean xExplicit = false;
    private ReadOnlyDoubleWrapper x = new ReadOnlyDoubleWrapper(this, "x", Double.NaN);
    private boolean yExplicit = false;
    private ReadOnlyDoubleWrapper y = new ReadOnlyDoubleWrapper(this, "y", Double.NaN);
    private boolean widthExplicit = false;
    private ReadOnlyDoubleWrapper width = new ReadOnlyDoubleWrapper(this, "width", Double.NaN);
    private boolean heightExplicit = false;
    private ReadOnlyDoubleWrapper height = new ReadOnlyDoubleWrapper(this, "height", Double.NaN);
    private ReadOnlyBooleanWrapper focused = new ReadOnlyBooleanWrapper(){

        @Override
        protected void invalidated() {
            Window.this.focusChanged(this.get());
        }

        @Override
        public Object getBean() {
            return Window.this;
        }

        @Override
        public String getName() {
            return "focused";
        }
    };
    private static final Object USER_DATA_KEY;
    private ObservableMap<Object, Object> properties;
    private SceneModel scene = new SceneModel();
    private DoubleProperty opacity;
    private ObjectProperty<EventHandler<WindowEvent>> onCloseRequest;
    private ObjectProperty<EventHandler<WindowEvent>> onShowing;
    private ObjectProperty<EventHandler<WindowEvent>> onShown;
    private ObjectProperty<EventHandler<WindowEvent>> onHiding;
    private ObjectProperty<EventHandler<WindowEvent>> onHidden;
    private ReadOnlyBooleanWrapper showing = new ReadOnlyBooleanWrapper(){
        private boolean oldVisible;

        @Override
        protected void invalidated() {
            boolean bl = this.get();
            if (this.oldVisible == bl) {
                return;
            }
            if (!this.oldVisible && bl) {
                Window.this.fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_SHOWING));
            } else {
                Window.this.fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_HIDING));
            }
            this.oldVisible = bl;
            Window.this.impl_visibleChanging(bl);
            if (bl) {
                Window.this.hasBeenVisible = true;
                windowQueue.add(Window.this);
            } else {
                windowQueue.remove(Window.this);
            }
            Toolkit toolkit = Toolkit.getToolkit();
            if (Window.this.impl_peer != null) {
                if (bl) {
                    if (Window.this.peerListener == null) {
                        Window.this.peerListener = new WindowPeerListener(Window.this);
                    }
                    Window.this.impl_peer.setTKStageListener(Window.this.peerListener);
                    toolkit.addStageTkPulseListener(Window.this.peerBoundsConfigurator);
                    if (Window.this.getScene() != null) {
                        Window.this.getScene().impl_initPeer();
                        Window.this.impl_peer.setScene(Window.this.getScene().impl_getPeer());
                        Window.this.getScene().impl_preferredSize();
                    }
                    if (!(Window.this.getScene() == null || Window.this.widthExplicit && Window.this.heightExplicit)) {
                        Window.this.adjustSize(true);
                    } else {
                        Window.this.peerBoundsConfigurator.setSize(Window.this.getWidth(), Window.this.getHeight(), -1.0, -1.0);
                    }
                    if (!Window.this.xExplicit && !Window.this.yExplicit) {
                        Window.this.centerOnScreen();
                    } else {
                        Window.this.peerBoundsConfigurator.setLocation(Window.this.getX(), Window.this.getY(), 0.0f, 0.0f);
                    }
                    Window.this.applyBounds();
                    Window.this.impl_peer.setOpacity((float)Window.this.getOpacity());
                    Window.this.impl_peer.setVisible(true);
                    Window.this.fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_SHOWN));
                } else {
                    Window.this.impl_peer.setVisible(false);
                    Window.this.fireEvent(new WindowEvent(Window.this, WindowEvent.WINDOW_HIDDEN));
                    if (Window.this.getScene() != null) {
                        Window.this.impl_peer.setScene(null);
                        Window.this.getScene().impl_disposePeer();
                        StyleManager.getInstance().forget(Window.this.getScene());
                    }
                    toolkit.removeStageTkPulseListener(Window.this.peerBoundsConfigurator);
                    Window.this.impl_peer.setTKStageListener(null);
                    Window.this.impl_peer.close();
                }
            }
            if (bl) {
                toolkit.requestNextPulse();
            }
            Window.this.impl_visibleChanged(bl);
            if (Window.this.sizeToScene) {
                if (bl) {
                    Window.this.sizeToScene();
                }
                Window.this.sizeToScene = false;
            }
        }

        @Override
        public Object getBean() {
            return Window.this;
        }

        @Override
        public String getName() {
            return "showing";
        }
    };
    boolean hasBeenVisible = false;
    private ObjectProperty<EventDispatcher> eventDispatcher;
    private WindowEventDispatcher internalEventDispatcher;
    private int focusGrabCounter;
    private final ReadOnlyObjectWrapper<Screen> screen = new ReadOnlyObjectWrapper<Screen>(Screen.getPrimary());

    @Deprecated
    public static Iterator<Window> impl_getWindows() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new AllPermission());
        }
        return windowQueue.iterator();
    }

    protected Window() {
        this.initializeInternalEventDispatcher();
    }

    @Deprecated
    public TKStage impl_getPeer() {
        return this.impl_peer;
    }

    @Deprecated
    public String impl_getMXWindowType() {
        return this.getClass().getSimpleName();
    }

    public void sizeToScene() {
        if (this.getScene() != null && this.impl_peer != null) {
            this.getScene().impl_preferredSize();
            this.adjustSize(false);
        } else {
            this.sizeToScene = true;
        }
    }

    private void adjustSize(boolean bl) {
        if (this.getScene() == null) {
            return;
        }
        if (this.impl_peer != null) {
            double d2 = this.getScene().getWidth();
            double d3 = d2 > 0.0 ? d2 : -1.0;
            double d4 = -1.0;
            if (bl && this.widthExplicit) {
                d4 = this.getWidth();
            } else if (d3 <= 0.0) {
                d4 = this.widthExplicit ? this.getWidth() : -1.0;
            } else {
                this.widthExplicit = false;
            }
            double d5 = this.getScene().getHeight();
            double d6 = d5 > 0.0 ? d5 : -1.0;
            double d7 = -1.0;
            if (bl && this.heightExplicit) {
                d7 = this.getHeight();
            } else if (d6 <= 0.0) {
                d7 = this.heightExplicit ? this.getHeight() : -1.0;
            } else {
                this.heightExplicit = false;
            }
            this.peerBoundsConfigurator.setSize(d4, d7, d3, d6);
            this.applyBounds();
        }
    }

    public void centerOnScreen() {
        this.xExplicit = false;
        this.yExplicit = false;
        if (this.impl_peer != null) {
            Rectangle2D rectangle2D = this.getWindowScreen().getVisualBounds();
            double d2 = rectangle2D.getMinX() + (rectangle2D.getWidth() - this.getWidth()) * 0.5;
            double d3 = rectangle2D.getMinY() + (rectangle2D.getHeight() - this.getHeight()) * 0.3333333432674408;
            this.x.set(d2);
            this.y.set(d3);
            this.peerBoundsConfigurator.setLocation(d2, d3, 0.5f, 0.33333334f);
            this.applyBounds();
        }
    }

    public final void setX(double d2) {
        this.setXInternal(d2);
    }

    public final double getX() {
        return this.x.get();
    }

    public final ReadOnlyDoubleProperty xProperty() {
        return this.x.getReadOnlyProperty();
    }

    void setXInternal(double d2) {
        this.x.set(d2);
        this.peerBoundsConfigurator.setX(d2, 0.0f);
        this.xExplicit = true;
    }

    public final void setY(double d2) {
        this.setYInternal(d2);
    }

    public final double getY() {
        return this.y.get();
    }

    public final ReadOnlyDoubleProperty yProperty() {
        return this.y.getReadOnlyProperty();
    }

    void setYInternal(double d2) {
        this.y.set(d2);
        this.peerBoundsConfigurator.setY(d2, 0.0f);
        this.yExplicit = true;
    }

    void notifyLocationChanged(double d2, double d3) {
        this.x.set(d2);
        this.y.set(d3);
    }

    public final void setWidth(double d2) {
        this.width.set(d2);
        this.peerBoundsConfigurator.setWindowWidth(d2);
        this.widthExplicit = true;
    }

    public final double getWidth() {
        return this.width.get();
    }

    public final ReadOnlyDoubleProperty widthProperty() {
        return this.width.getReadOnlyProperty();
    }

    public final void setHeight(double d2) {
        this.height.set(d2);
        this.peerBoundsConfigurator.setWindowHeight(d2);
        this.heightExplicit = true;
    }

    public final double getHeight() {
        return this.height.get();
    }

    public final ReadOnlyDoubleProperty heightProperty() {
        return this.height.getReadOnlyProperty();
    }

    void notifySizeChanged(double d2, double d3) {
        this.width.set(d2);
        this.height.set(d3);
    }

    @Deprecated
    public final void setFocused(boolean bl) {
        this.focused.set(bl);
    }

    public final void requestFocus() {
        if (this.impl_peer != null) {
            this.impl_peer.requestFocus();
        }
    }

    public final boolean isFocused() {
        return this.focused.get();
    }

    public final ReadOnlyBooleanProperty focusedProperty() {
        return this.focused.getReadOnlyProperty();
    }

    public final ObservableMap<Object, Object> getProperties() {
        if (this.properties == null) {
            this.properties = FXCollections.observableMap(new HashMap());
        }
        return this.properties;
    }

    public boolean hasProperties() {
        return this.properties != null && !this.properties.isEmpty();
    }

    public void setUserData(Object object) {
        this.getProperties().put(USER_DATA_KEY, object);
    }

    public Object getUserData() {
        return this.getProperties().get(USER_DATA_KEY);
    }

    protected void setScene(Scene scene) {
        this.scene.set(scene);
    }

    public final Scene getScene() {
        return (Scene)this.scene.get();
    }

    public final ReadOnlyObjectProperty<Scene> sceneProperty() {
        return this.scene.getReadOnlyProperty();
    }

    public final void setOpacity(double d2) {
        this.opacityProperty().set(d2);
    }

    public final double getOpacity() {
        return this.opacity == null ? 1.0 : this.opacity.get();
    }

    public final DoubleProperty opacityProperty() {
        if (this.opacity == null) {
            this.opacity = new DoublePropertyBase(1.0){

                @Override
                protected void invalidated() {
                    if (Window.this.impl_peer != null) {
                        Window.this.impl_peer.setOpacity((float)this.get());
                    }
                }

                @Override
                public Object getBean() {
                    return Window.this;
                }

                @Override
                public String getName() {
                    return "opacity";
                }
            };
        }
        return this.opacity;
    }

    public final void setOnCloseRequest(EventHandler<WindowEvent> eventHandler) {
        this.onCloseRequestProperty().set(eventHandler);
    }

    public final EventHandler<WindowEvent> getOnCloseRequest() {
        return this.onCloseRequest != null ? (EventHandler)this.onCloseRequest.get() : null;
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onCloseRequestProperty() {
        if (this.onCloseRequest == null) {
            this.onCloseRequest = new ObjectPropertyBase<EventHandler<WindowEvent>>(){

                @Override
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return Window.this;
                }

                @Override
                public String getName() {
                    return "onCloseRequest";
                }
            };
        }
        return this.onCloseRequest;
    }

    public final void setOnShowing(EventHandler<WindowEvent> eventHandler) {
        this.onShowingProperty().set(eventHandler);
    }

    public final EventHandler<WindowEvent> getOnShowing() {
        return this.onShowing == null ? null : (EventHandler)this.onShowing.get();
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onShowingProperty() {
        if (this.onShowing == null) {
            this.onShowing = new ObjectPropertyBase<EventHandler<WindowEvent>>(){

                @Override
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_SHOWING, (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return Window.this;
                }

                @Override
                public String getName() {
                    return "onShowing";
                }
            };
        }
        return this.onShowing;
    }

    public final void setOnShown(EventHandler<WindowEvent> eventHandler) {
        this.onShownProperty().set(eventHandler);
    }

    public final EventHandler<WindowEvent> getOnShown() {
        return this.onShown == null ? null : (EventHandler)this.onShown.get();
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onShownProperty() {
        if (this.onShown == null) {
            this.onShown = new ObjectPropertyBase<EventHandler<WindowEvent>>(){

                @Override
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_SHOWN, (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return Window.this;
                }

                @Override
                public String getName() {
                    return "onShown";
                }
            };
        }
        return this.onShown;
    }

    public final void setOnHiding(EventHandler<WindowEvent> eventHandler) {
        this.onHidingProperty().set(eventHandler);
    }

    public final EventHandler<WindowEvent> getOnHiding() {
        return this.onHiding == null ? null : (EventHandler)this.onHiding.get();
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onHidingProperty() {
        if (this.onHiding == null) {
            this.onHiding = new ObjectPropertyBase<EventHandler<WindowEvent>>(){

                @Override
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_HIDING, (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return Window.this;
                }

                @Override
                public String getName() {
                    return "onHiding";
                }
            };
        }
        return this.onHiding;
    }

    public final void setOnHidden(EventHandler<WindowEvent> eventHandler) {
        this.onHiddenProperty().set(eventHandler);
    }

    public final EventHandler<WindowEvent> getOnHidden() {
        return this.onHidden == null ? null : (EventHandler)this.onHidden.get();
    }

    public final ObjectProperty<EventHandler<WindowEvent>> onHiddenProperty() {
        if (this.onHidden == null) {
            this.onHidden = new ObjectPropertyBase<EventHandler<WindowEvent>>(){

                @Override
                protected void invalidated() {
                    Window.this.setEventHandler(WindowEvent.WINDOW_HIDDEN, (EventHandler)this.get());
                }

                @Override
                public Object getBean() {
                    return Window.this;
                }

                @Override
                public String getName() {
                    return "onHidden";
                }
            };
        }
        return this.onHidden;
    }

    private void setShowing(boolean bl) {
        Toolkit.getToolkit().checkFxUserThread();
        this.showing.set(bl);
    }

    public final boolean isShowing() {
        return this.showing.get();
    }

    public final ReadOnlyBooleanProperty showingProperty() {
        return this.showing.getReadOnlyProperty();
    }

    protected void show() {
        this.setShowing(true);
    }

    public void hide() {
        this.setShowing(false);
    }

    @Deprecated
    protected void impl_visibleChanging(boolean bl) {
        if (bl && this.getScene() != null) {
            this.getScene().getRoot().impl_reapplyCSS();
        }
    }

    @Deprecated
    protected void impl_visibleChanged(boolean bl) {
        assert (this.impl_peer != null);
        if (!bl) {
            this.peerListener = null;
            this.impl_peer = null;
        }
    }

    public final void setEventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcherProperty().set(eventDispatcher);
    }

    public final EventDispatcher getEventDispatcher() {
        return (EventDispatcher)this.eventDispatcherProperty().get();
    }

    public final ObjectProperty<EventDispatcher> eventDispatcherProperty() {
        this.initializeInternalEventDispatcher();
        return this.eventDispatcher;
    }

    public final <T extends Event> void addEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        this.getInternalEventDispatcher().getEventHandlerManager().addEventHandler(eventType, eventHandler);
    }

    public final <T extends Event> void removeEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        this.getInternalEventDispatcher().getEventHandlerManager().removeEventHandler(eventType, eventHandler);
    }

    public final <T extends Event> void addEventFilter(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        this.getInternalEventDispatcher().getEventHandlerManager().addEventFilter(eventType, eventHandler);
    }

    public final <T extends Event> void removeEventFilter(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        this.getInternalEventDispatcher().getEventHandlerManager().removeEventFilter(eventType, eventHandler);
    }

    protected final <T extends Event> void setEventHandler(EventType<T> eventType, EventHandler<? super T> eventHandler) {
        this.getInternalEventDispatcher().getEventHandlerManager().setEventHandler(eventType, eventHandler);
    }

    WindowEventDispatcher getInternalEventDispatcher() {
        this.initializeInternalEventDispatcher();
        return this.internalEventDispatcher;
    }

    private void initializeInternalEventDispatcher() {
        if (this.internalEventDispatcher == null) {
            this.internalEventDispatcher = this.createInternalEventDispatcher();
            this.eventDispatcher = new SimpleObjectProperty<WindowEventDispatcher>(this, "eventDispatcher", this.internalEventDispatcher);
        }
    }

    WindowEventDispatcher createInternalEventDispatcher() {
        return new WindowEventDispatcher(this);
    }

    public final void fireEvent(Event event) {
        Event.fireEvent(this, event);
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain eventDispatchChain) {
        EventDispatcher eventDispatcher;
        if (this.eventDispatcher != null && (eventDispatcher = (EventDispatcher)this.eventDispatcher.get()) != null) {
            eventDispatchChain = eventDispatchChain.prepend(eventDispatcher);
        }
        return eventDispatchChain;
    }

    void increaseFocusGrabCounter() {
        if (++this.focusGrabCounter == 1 && this.impl_peer != null && this.isFocused()) {
            this.impl_peer.grabFocus();
        }
    }

    void decreaseFocusGrabCounter() {
        if (--this.focusGrabCounter == 0 && this.impl_peer != null) {
            this.impl_peer.ungrabFocus();
        }
    }

    private void focusChanged(boolean bl) {
        if (this.focusGrabCounter > 0 && this.impl_peer != null && bl) {
            this.impl_peer.grabFocus();
        }
    }

    final void applyBounds() {
        this.peerBoundsConfigurator.apply();
    }

    Window getWindowOwner() {
        return null;
    }

    private Screen getWindowScreen() {
        Window window = this;
        do {
            if (Double.isNaN(window.getX()) || Double.isNaN(window.getY()) || Double.isNaN(window.getWidth()) || Double.isNaN(window.getHeight())) continue;
            return Utils.getScreenForRectangle(new Rectangle2D(window.getX(), window.getY(), window.getWidth(), window.getHeight()));
        } while ((window = window.getWindowOwner()) != null);
        return Screen.getPrimary();
    }

    private ReadOnlyObjectProperty<Screen> screenProperty() {
        return this.screen.getReadOnlyProperty();
    }

    private void notifyScreenChanged(Object object, Object object2) {
        this.screen.set(Screen.getScreenForNative(object2));
    }

    static {
        WindowHelper.setWindowAccessor(new WindowHelper.WindowAccessor(){

            @Override
            public void notifyLocationChanged(Window window, double d2, double d3) {
                window.notifyLocationChanged(d2, d3);
            }

            @Override
            public void notifySizeChanged(Window window, double d2, double d3) {
                window.notifySizeChanged(d2, d3);
            }

            @Override
            public void notifyScreenChanged(Window window, Object object, Object object2) {
                window.notifyScreenChanged(object, object2);
            }

            @Override
            public float getUIScale(Window window) {
                TKStage tKStage = window.impl_peer;
                return tKStage == null ? 1.0f : tKStage.getUIScale();
            }

            @Override
            public float getRenderScale(Window window) {
                TKStage tKStage = window.impl_peer;
                return tKStage == null ? 1.0f : tKStage.getRenderScale();
            }

            @Override
            public ReadOnlyObjectProperty<Screen> screenProperty(Window window) {
                return window.screenProperty();
            }

            @Override
            public AccessControlContext getAccessControlContext(Window window) {
                return window.acc;
            }
        });
        USER_DATA_KEY = new Object();
    }

    private final class TKBoundsConfigurator
    implements TKPulseListener {
        private double x;
        private double y;
        private float xGravity;
        private float yGravity;
        private double windowWidth;
        private double windowHeight;
        private double clientWidth;
        private double clientHeight;
        private boolean dirty;

        public TKBoundsConfigurator() {
            this.reset();
        }

        public void setX(double d2, float f2) {
            this.x = d2;
            this.xGravity = f2;
            this.setDirty();
        }

        public void setY(double d2, float f2) {
            this.y = d2;
            this.yGravity = f2;
            this.setDirty();
        }

        public void setWindowWidth(double d2) {
            this.windowWidth = d2;
            this.setDirty();
        }

        public void setWindowHeight(double d2) {
            this.windowHeight = d2;
            this.setDirty();
        }

        public void setClientWidth(double d2) {
            this.clientWidth = d2;
            this.setDirty();
        }

        public void setClientHeight(double d2) {
            this.clientHeight = d2;
            this.setDirty();
        }

        public void setLocation(double d2, double d3, float f2, float f3) {
            this.x = d2;
            this.y = d3;
            this.xGravity = f2;
            this.yGravity = f3;
            this.setDirty();
        }

        public void setSize(double d2, double d3, double d4, double d5) {
            this.windowWidth = d2;
            this.windowHeight = d3;
            this.clientWidth = d4;
            this.clientHeight = d5;
            this.setDirty();
        }

        public void apply() {
            if (this.dirty) {
                Window.this.impl_peer.setBounds((float)(Double.isNaN(this.x) ? 0.0 : this.x), (float)(Double.isNaN(this.y) ? 0.0 : this.y), !Double.isNaN(this.x), !Double.isNaN(this.y), (float)this.windowWidth, (float)this.windowHeight, (float)this.clientWidth, (float)this.clientHeight, this.xGravity, this.yGravity);
                this.reset();
            }
        }

        @Override
        public void pulse() {
            this.apply();
        }

        private void reset() {
            this.x = Double.NaN;
            this.y = Double.NaN;
            this.xGravity = 0.0f;
            this.yGravity = 0.0f;
            this.windowWidth = -1.0;
            this.windowHeight = -1.0;
            this.clientWidth = -1.0;
            this.clientHeight = -1.0;
            this.dirty = false;
        }

        private void setDirty() {
            if (!this.dirty) {
                Toolkit.getToolkit().requestNextPulse();
                this.dirty = true;
            }
        }
    }

    private final class SceneModel
    extends ReadOnlyObjectWrapper<Scene> {
        private Scene oldScene;

        private SceneModel() {
        }

        @Override
        protected void invalidated() {
            Scene scene = (Scene)this.get();
            if (this.oldScene == scene) {
                return;
            }
            if (Window.this.impl_peer != null) {
                Toolkit.getToolkit().checkFxUserThread();
            }
            this.updatePeerScene(null);
            if (this.oldScene != null) {
                this.oldScene.impl_setWindow(null);
                StyleManager.getInstance().forget(this.oldScene);
            }
            if (scene != null) {
                Window window = scene.getWindow();
                if (window != null) {
                    window.setScene(null);
                }
                scene.impl_setWindow(Window.this);
                this.updatePeerScene(scene.impl_getPeer());
                if (Window.this.isShowing()) {
                    scene.getRoot().impl_reapplyCSS();
                    if (!Window.this.widthExplicit || !Window.this.heightExplicit) {
                        Window.this.getScene().impl_preferredSize();
                        Window.this.adjustSize(true);
                    }
                }
            }
            this.oldScene = scene;
        }

        @Override
        public Object getBean() {
            return Window.this;
        }

        @Override
        public String getName() {
            return "scene";
        }

        private void updatePeerScene(TKScene tKScene) {
            if (Window.this.impl_peer != null) {
                Window.this.impl_peer.setScene(tKScene);
            }
        }
    }
}

