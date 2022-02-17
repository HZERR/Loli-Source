/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.tk.quantum;

import com.sun.glass.events.ViewEvent;
import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.scene.input.KeyCodeMap;
import com.sun.javafx.tk.quantum.GestureRecognizers;
import com.sun.javafx.tk.quantum.GlassSceneDnDEventHandler;
import com.sun.javafx.tk.quantum.PaintCollector;
import com.sun.javafx.tk.quantum.QuantumToolkit;
import com.sun.javafx.tk.quantum.RotateGestureRecognizer;
import com.sun.javafx.tk.quantum.ScrollGestureRecognizer;
import com.sun.javafx.tk.quantum.SwipeGestureRecognizer;
import com.sun.javafx.tk.quantum.ViewScene;
import com.sun.javafx.tk.quantum.WindowStage;
import com.sun.javafx.tk.quantum.ZoomGestureRecognizer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.InputMethodHighlight;
import javafx.scene.input.InputMethodTextRun;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchPoint;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ZoomEvent;

class GlassViewEventHandler
extends View.EventHandler {
    static boolean zoomGestureEnabled;
    static boolean rotateGestureEnabled;
    static boolean scrollGestureEnabled;
    private ViewScene scene;
    private final GlassSceneDnDEventHandler dndHandler;
    private final GestureRecognizers gestures;
    private final PaintCollector collector = PaintCollector.getInstance();
    private final KeyEventNotification keyNotification = new KeyEventNotification();
    private int mouseButtonPressedMask = 0;
    private final MouseEventNotification mouseNotification = new MouseEventNotification();
    private ClipboardAssistance dropSourceAssistant;
    private final ViewEventNotification viewNotification = new ViewEventNotification();

    public GlassViewEventHandler(ViewScene viewScene) {
        this.scene = viewScene;
        this.dndHandler = new GlassSceneDnDEventHandler(viewScene);
        this.gestures = new GestureRecognizers();
        if (PlatformUtil.isWindows() || PlatformUtil.isIOS() || PlatformUtil.isEmbedded()) {
            this.gestures.add(new SwipeGestureRecognizer(viewScene));
        }
        if (zoomGestureEnabled) {
            this.gestures.add(new ZoomGestureRecognizer(viewScene));
        }
        if (rotateGestureEnabled) {
            this.gestures.add(new RotateGestureRecognizer(viewScene));
        }
        if (scrollGestureEnabled) {
            this.gestures.add(new ScrollGestureRecognizer(viewScene));
        }
    }

    private static boolean allowableFullScreenKeys(int n2) {
        switch (n2) {
            case 9: 
            case 10: 
            case 32: 
            case 33: 
            case 34: 
            case 35: 
            case 36: 
            case 37: 
            case 38: 
            case 39: 
            case 40: {
                return true;
            }
        }
        return false;
    }

    private boolean checkFullScreenKeyEvent(int n2, int n3, char[] arrc, int n4) {
        return this.scene.getWindowStage().isTrustedFullScreen() || GlassViewEventHandler.allowableFullScreenKeys(n3);
    }

    private static EventType<KeyEvent> keyEventType(int n2) {
        switch (n2) {
            case 111: {
                return KeyEvent.KEY_PRESSED;
            }
            case 112: {
                return KeyEvent.KEY_RELEASED;
            }
            case 113: {
                return KeyEvent.KEY_TYPED;
            }
        }
        if (QuantumToolkit.verbose) {
            System.err.println("Unknown Glass key event type: " + n2);
        }
        return null;
    }

    @Override
    public void handleKeyEvent(View view, long l2, int n2, int n3, char[] arrc, int n4) {
        this.keyNotification.view = view;
        this.keyNotification.time = l2;
        this.keyNotification.type = n2;
        this.keyNotification.key = n3;
        this.keyNotification.chars = arrc;
        this.keyNotification.modifiers = n4;
        QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(this.keyNotification, this.scene.getAccessControlContext()));
    }

    private static EventType<MouseEvent> mouseEventType(int n2) {
        switch (n2) {
            case 221: {
                return MouseEvent.MOUSE_PRESSED;
            }
            case 222: {
                return MouseEvent.MOUSE_RELEASED;
            }
            case 225: {
                return MouseEvent.MOUSE_ENTERED;
            }
            case 226: {
                return MouseEvent.MOUSE_EXITED;
            }
            case 224: {
                return MouseEvent.MOUSE_MOVED;
            }
            case 223: {
                return MouseEvent.MOUSE_DRAGGED;
            }
            case 228: {
                throw new IllegalArgumentException("WHEEL event cannot be translated to MouseEvent, must be translated to ScrollEvent");
            }
        }
        if (QuantumToolkit.verbose) {
            System.err.println("Unknown Glass mouse event type: " + n2);
        }
        return null;
    }

    private static MouseButton mouseEventButton(int n2) {
        switch (n2) {
            case 212: {
                return MouseButton.PRIMARY;
            }
            case 213: {
                return MouseButton.SECONDARY;
            }
            case 214: {
                return MouseButton.MIDDLE;
            }
        }
        return MouseButton.NONE;
    }

    @Override
    public void handleMouseEvent(View view, long l2, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl, boolean bl2) {
        this.mouseNotification.view = view;
        this.mouseNotification.time = l2;
        this.mouseNotification.type = n2;
        this.mouseNotification.button = n3;
        this.mouseNotification.x = n4;
        this.mouseNotification.y = n5;
        this.mouseNotification.xAbs = n6;
        this.mouseNotification.yAbs = n7;
        this.mouseNotification.modifiers = n8;
        this.mouseNotification.isPopupTrigger = bl;
        this.mouseNotification.isSynthesized = bl2;
        QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(this.mouseNotification, this.scene.getAccessControlContext()));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleMenuEvent(View view, int n2, int n3, int n4, int n5, boolean bl) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("MENU_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    double d2;
                    double d3;
                    double d4;
                    Window window = view.getWindow();
                    if (window != null) {
                        d4 = window.getPlatformScale();
                        Screen screen = window.getScreen();
                        if (screen != null) {
                            d3 = screen.getX();
                            d2 = screen.getY();
                        } else {
                            d2 = 0.0;
                            d3 = 0.0;
                        }
                    } else {
                        d4 = 1.0;
                        d2 = 0.0;
                        d3 = 0.0;
                    }
                    this.scene.sceneListener.menuEvent((double)n2 / d4, (double)n3 / d4, d3 + ((double)n4 - d3) / d4, d2 + ((double)n5 - d2) / d4, bl);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleScrollEvent(View view, long l2, int n2, int n3, int n4, int n5, double d2, double d3, int n6, int n7, int n8, int n9, int n10, double d4, double d5) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("SCROLL_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    Window window = view.getWindow();
                    double d6 = window == null ? 1.0 : (double)window.getPlatformScale();
                    this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL, d2 / d6, d3 / d6, 0.0, 0.0, d4, d5, 0, n8, n7, n10, n9, (double)n2 / d6, (double)n3 / d6, (double)n4 / d6, (double)n5 / d6, (n6 & 1) != 0, (n6 & 4) != 0, (n6 & 8) != 0, (n6 & 0x10) != 0, false, false);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    private static byte inputMethodEventAttrValue(int n2, int[] arrn, byte[] arrby) {
        if (arrn != null) {
            for (int i2 = 0; i2 < arrn.length - 1; ++i2) {
                if (n2 < arrn[i2] || n2 >= arrn[i2 + 1]) continue;
                return arrby[i2];
            }
        }
        return 4;
    }

    private static ObservableList<InputMethodTextRun> inputMethodEventComposed(String string, int n2, int[] arrn, int[] arrn2, byte[] arrby) {
        TrackableObservableList<InputMethodTextRun> trackableObservableList = new TrackableObservableList<InputMethodTextRun>(){

            @Override
            protected void onChanged(ListChangeListener.Change<InputMethodTextRun> change) {
            }
        };
        if (n2 < string.length()) {
            if (arrn == null) {
                trackableObservableList.add(new InputMethodTextRun(string.substring(n2), InputMethodHighlight.UNSELECTED_RAW));
            } else {
                for (int i2 = 0; i2 < arrn.length - 1; ++i2) {
                    InputMethodHighlight inputMethodHighlight;
                    if (arrn[i2] < n2) continue;
                    switch (GlassViewEventHandler.inputMethodEventAttrValue(arrn[i2], arrn2, arrby)) {
                        case 1: {
                            inputMethodHighlight = InputMethodHighlight.SELECTED_CONVERTED;
                            break;
                        }
                        case 2: {
                            inputMethodHighlight = InputMethodHighlight.UNSELECTED_CONVERTED;
                            break;
                        }
                        case 3: {
                            inputMethodHighlight = InputMethodHighlight.SELECTED_RAW;
                            break;
                        }
                        default: {
                            inputMethodHighlight = InputMethodHighlight.UNSELECTED_RAW;
                        }
                    }
                    trackableObservableList.add(new InputMethodTextRun(string.substring(arrn[i2], arrn[i2 + 1]), inputMethodHighlight));
                }
            }
        }
        return trackableObservableList;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleInputMethodEvent(long l2, String string, int[] arrn, int[] arrn2, byte[] arrby, int n2, int n3) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("INPUT_METHOD_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    String string2 = string != null ? string : "";
                    EventType<InputMethodEvent> eventType = InputMethodEvent.INPUT_METHOD_TEXT_CHANGED;
                    ObservableList<InputMethodTextRun> observableList = GlassViewEventHandler.inputMethodEventComposed(string2, n2, arrn, arrn2, arrby);
                    String string3 = string2.substring(0, n2);
                    this.scene.sceneListener.inputMethodEvent(eventType, observableList, string3, n3);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    @Override
    public double[] getInputMethodCandidatePos(int n2) {
        Point2D point2D = this.scene.inputMethodRequests.getTextLocation(n2);
        double[] arrd = new double[]{point2D.getX(), point2D.getY()};
        return arrd;
    }

    private static TransferMode actionToTransferMode(int n2) {
        if (n2 == 0) {
            return null;
        }
        if (n2 == 1 || n2 == 0x40000001) {
            return TransferMode.COPY;
        }
        if (n2 == 2 || n2 == 0x40000002) {
            return TransferMode.MOVE;
        }
        if (n2 == 0x40000000) {
            return TransferMode.LINK;
        }
        if (n2 == 3) {
            if (QuantumToolkit.verbose) {
                System.err.println("Ambiguous drop action: " + Integer.toHexString(n2));
            }
        } else if (QuantumToolkit.verbose) {
            System.err.println("Unknown drop action: " + Integer.toHexString(n2));
        }
        return null;
    }

    private static int transferModeToAction(TransferMode transferMode) {
        if (transferMode == null) {
            return 0;
        }
        switch (transferMode) {
            case COPY: {
                return 1;
            }
            case MOVE: {
                return 2;
            }
            case LINK: {
                return 0x40000000;
            }
        }
        return 0;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int handleDragEnter(View view, int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
        TransferMode transferMode;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_ENTER");
        }
        try {
            transferMode = QuantumToolkit.runWithoutRenderLock(() -> this.dndHandler.handleDragEnter(n2, n3, n4, n5, GlassViewEventHandler.actionToTransferMode(n6), clipboardAssistance));
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        return GlassViewEventHandler.transferModeToAction(transferMode);
    }

    @Override
    public void handleDragLeave(View view, ClipboardAssistance clipboardAssistance) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_LEAVE");
        }
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                this.dndHandler.handleDragLeave(clipboardAssistance);
                return null;
            });
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int handleDragDrop(View view, int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
        TransferMode transferMode;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_DROP");
        }
        try {
            transferMode = QuantumToolkit.runWithoutRenderLock(() -> this.dndHandler.handleDragDrop(n2, n3, n4, n5, GlassViewEventHandler.actionToTransferMode(n6), clipboardAssistance));
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        return GlassViewEventHandler.transferModeToAction(transferMode);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public int handleDragOver(View view, int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
        TransferMode transferMode;
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_OVER");
        }
        try {
            transferMode = QuantumToolkit.runWithoutRenderLock(() -> this.dndHandler.handleDragOver(n2, n3, n4, n5, GlassViewEventHandler.actionToTransferMode(n6), clipboardAssistance));
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        return GlassViewEventHandler.transferModeToAction(transferMode);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleDragStart(View view, int n2, int n3, int n4, int n5, int n6, ClipboardAssistance clipboardAssistance) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_START");
        }
        this.dropSourceAssistant = clipboardAssistance;
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                this.dndHandler.handleDragStart(n2, n3, n4, n5, n6, clipboardAssistance);
                return null;
            });
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    @Override
    public void handleDragEnd(View view, int n2) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("DRAG_END");
        }
        try {
            QuantumToolkit.runWithoutRenderLock(() -> {
                this.dndHandler.handleDragEnd(GlassViewEventHandler.actionToTransferMode(n2), this.dropSourceAssistant);
                return null;
            });
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleViewEvent(View view, long l2, int n2) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("VIEW_EVENT: " + ViewEvent.getTypeString(n2));
        }
        this.viewNotification.view = view;
        this.viewNotification.time = l2;
        this.viewNotification.type = n2;
        try {
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(this.viewNotification, this.scene.getAccessControlContext()));
        }
        finally {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleScrollGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, int n8, double d2, double d3, double d4, double d5, double d6, double d7) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("SCROLL_GESTURE_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    EventType<ScrollEvent> eventType;
                    switch (n2) {
                        case 1: {
                            eventType = ScrollEvent.SCROLL_STARTED;
                            break;
                        }
                        case 2: {
                            eventType = ScrollEvent.SCROLL;
                            break;
                        }
                        case 3: {
                            eventType = ScrollEvent.SCROLL_FINISHED;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown scroll event type: " + n2);
                        }
                    }
                    Window window = view.getWindow();
                    double d8 = window == null ? 1.0 : (double)window.getPlatformScale();
                    this.scene.sceneListener.scrollEvent(eventType, d2 / d8, d3 / d8, d4 / d8, d5 / d8, d6, d7, n4, 0, 0, 0, 0, n5 == Integer.MAX_VALUE ? Double.NaN : (double)n5 / d8, n6 == Integer.MAX_VALUE ? Double.NaN : (double)n6 / d8, n7 == Integer.MAX_VALUE ? Double.NaN : (double)n7 / d8, n8 == Integer.MAX_VALUE ? Double.NaN : (double)n8 / d8, (n3 & 1) != 0, (n3 & 4) != 0, (n3 & 8) != 0, (n3 & 0x10) != 0, bl, bl2);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleZoomGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, double d2, double d3, double d4, double d5) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("ZOOM_GESTURE_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    EventType<ZoomEvent> eventType;
                    switch (n2) {
                        case 1: {
                            eventType = ZoomEvent.ZOOM_STARTED;
                            break;
                        }
                        case 2: {
                            eventType = ZoomEvent.ZOOM;
                            break;
                        }
                        case 3: {
                            eventType = ZoomEvent.ZOOM_FINISHED;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown scroll event type: " + n2);
                        }
                    }
                    Window window = view.getWindow();
                    double d4 = window == null ? 1.0 : (double)window.getPlatformScale();
                    this.scene.sceneListener.zoomEvent(eventType, d2, d4, n4 == Integer.MAX_VALUE ? Double.NaN : (double)n4 / d4, n5 == Integer.MAX_VALUE ? Double.NaN : (double)n5 / d4, n6 == Integer.MAX_VALUE ? Double.NaN : (double)n6 / d4, n7 == Integer.MAX_VALUE ? Double.NaN : (double)n7 / d4, (n3 & 1) != 0, (n3 & 4) != 0, (n3 & 8) != 0, (n3 & 0x10) != 0, bl, bl2);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleRotateGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, double d2, double d3) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("ROTATE_GESTURE_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    EventType<RotateEvent> eventType;
                    switch (n2) {
                        case 1: {
                            eventType = RotateEvent.ROTATION_STARTED;
                            break;
                        }
                        case 2: {
                            eventType = RotateEvent.ROTATE;
                            break;
                        }
                        case 3: {
                            eventType = RotateEvent.ROTATION_FINISHED;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown scroll event type: " + n2);
                        }
                    }
                    Window window = view.getWindow();
                    double d4 = window == null ? 1.0 : (double)window.getPlatformScale();
                    this.scene.sceneListener.rotateEvent(eventType, d2, d3, n4 == Integer.MAX_VALUE ? Double.NaN : (double)n4 / d4, n5 == Integer.MAX_VALUE ? Double.NaN : (double)n5 / d4, n6 == Integer.MAX_VALUE ? Double.NaN : (double)n6 / d4, n7 == Integer.MAX_VALUE ? Double.NaN : (double)n7 / d4, (n3 & 1) != 0, (n3 & 4) != 0, (n3 & 8) != 0, (n3 & 0x10) != 0, bl, bl2);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleSwipeGestureEvent(View view, long l2, int n2, int n3, boolean bl, boolean bl2, int n4, int n5, int n6, int n7, int n8, int n9) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("SWIPE_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    EventType<SwipeEvent> eventType;
                    switch (n5) {
                        case 1: {
                            eventType = SwipeEvent.SWIPE_UP;
                            break;
                        }
                        case 2: {
                            eventType = SwipeEvent.SWIPE_DOWN;
                            break;
                        }
                        case 3: {
                            eventType = SwipeEvent.SWIPE_LEFT;
                            break;
                        }
                        case 4: {
                            eventType = SwipeEvent.SWIPE_RIGHT;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown swipe event direction: " + n5);
                        }
                    }
                    Window window = view.getWindow();
                    double d2 = window == null ? 1.0 : (double)window.getPlatformScale();
                    this.scene.sceneListener.swipeEvent(eventType, n4, n6 == Integer.MAX_VALUE ? Double.NaN : (double)n6 / d2, n7 == Integer.MAX_VALUE ? Double.NaN : (double)n7 / d2, n8 == Integer.MAX_VALUE ? Double.NaN : (double)n8 / d2, n9 == Integer.MAX_VALUE ? Double.NaN : (double)n9 / d2, (n3 & 1) != 0, (n3 & 4) != 0, (n3 & 8) != 0, (n3 & 0x10) != 0, bl);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleBeginTouchEvent(View view, long l2, int n2, boolean bl, int n3) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("BEGIN_TOUCH_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    this.scene.sceneListener.touchEventBegin(l2, n3, bl, (n2 & 1) != 0, (n2 & 4) != 0, (n2 & 8) != 0, (n2 & 0x10) != 0);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        this.gestures.notifyBeginTouchEvent(l2, n2, bl, n3);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleNextTouchEvent(View view, long l2, int n2, long l3, int n3, int n4, int n5, int n6) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("NEXT_TOUCH_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    TouchPoint.State state;
                    switch (n2) {
                        case 811: {
                            state = TouchPoint.State.PRESSED;
                            break;
                        }
                        case 812: {
                            state = TouchPoint.State.MOVED;
                            break;
                        }
                        case 814: {
                            state = TouchPoint.State.STATIONARY;
                            break;
                        }
                        case 813: {
                            state = TouchPoint.State.RELEASED;
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown touch state: " + n2);
                        }
                    }
                    Window window = view.getWindow();
                    double d2 = window == null ? 1.0 : (double)window.getPlatformScale();
                    this.scene.sceneListener.touchEventNext(state, l3, (double)n3 / d2, (double)n4 / d2, (double)n5 / d2, (double)n6 / d2);
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        this.gestures.notifyNextTouchEvent(l2, n2, l3, n3, n4, n5, n6);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void handleEndTouchEvent(View view, long l2) {
        if (PulseLogger.PULSE_LOGGING_ENABLED) {
            PulseLogger.newInput("END_TOUCH_EVENT");
        }
        WindowStage windowStage = this.scene.getWindowStage();
        try {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(true);
            }
            QuantumToolkit.runWithoutRenderLock(() -> AccessController.doPrivileged(() -> {
                if (this.scene.sceneListener != null) {
                    this.scene.sceneListener.touchEventEnd();
                }
                return null;
            }, this.scene.getAccessControlContext()));
        }
        finally {
            if (windowStage != null) {
                windowStage.setInAllowedEventHandler(false);
            }
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(null);
            }
        }
        this.gestures.notifyEndTouchEvent(l2);
    }

    @Override
    public Accessible getSceneAccessible() {
        if (this.scene != null && this.scene.sceneListener != null) {
            return this.scene.sceneListener.getSceneAccessible();
        }
        return null;
    }

    static {
        AccessController.doPrivileged(() -> {
            zoomGestureEnabled = Boolean.valueOf(System.getProperty("com.sun.javafx.gestures.zoom", "false"));
            rotateGestureEnabled = Boolean.valueOf(System.getProperty("com.sun.javafx.gestures.rotate", "false"));
            scrollGestureEnabled = Boolean.valueOf(System.getProperty("com.sun.javafx.gestures.scroll", "false"));
            return null;
        });
    }

    private class ViewEventNotification
    implements PrivilegedAction<Void> {
        View view;
        long time;
        int type;

        private ViewEventNotification() {
        }

        @Override
        public Void run() {
            if (((GlassViewEventHandler)GlassViewEventHandler.this).scene.sceneListener == null) {
                return null;
            }
            switch (this.type) {
                case 421: {
                    WindowStage windowStage;
                    Window window = this.view.getWindow();
                    if (window != null && window.getMinimumWidth() == this.view.getWidth() && !window.isVisible()) break;
                    if (QuantumToolkit.drawInPaint && window != null && window.isVisible() && (windowStage = GlassViewEventHandler.this.scene.getWindowStage()) != null && !windowStage.isApplet()) {
                        GlassViewEventHandler.this.collector.liveRepaintRenderJob(GlassViewEventHandler.this.scene);
                    }
                    GlassViewEventHandler.this.scene.entireSceneNeedsRepaint();
                    break;
                }
                case 422: {
                    WindowStage windowStage;
                    Window window = this.view.getWindow();
                    float f2 = window == null ? 1.0f : window.getPlatformScale();
                    ((GlassViewEventHandler)GlassViewEventHandler.this).scene.sceneListener.changedSize((float)this.view.getWidth() / f2, (float)this.view.getHeight() / f2);
                    GlassViewEventHandler.this.scene.entireSceneNeedsRepaint();
                    QuantumToolkit.runWithRenderLock(() -> {
                        GlassViewEventHandler.this.scene.updateSceneState();
                        return null;
                    });
                    if (!QuantumToolkit.liveResize || window == null || !window.isVisible() || (windowStage = GlassViewEventHandler.this.scene.getWindowStage()) == null || windowStage.isApplet()) break;
                    GlassViewEventHandler.this.collector.liveRepaintRenderJob(GlassViewEventHandler.this.scene);
                    break;
                }
                case 423: {
                    Window window = this.view.getWindow();
                    float f3 = window == null ? 1.0f : window.getPlatformScale();
                    ((GlassViewEventHandler)GlassViewEventHandler.this).scene.sceneListener.changedLocation((float)this.view.getX() / f3, (float)this.view.getY() / f3);
                    break;
                }
                case 431: 
                case 432: {
                    if (GlassViewEventHandler.this.scene.getWindowStage() == null) break;
                    GlassViewEventHandler.this.scene.getWindowStage().fullscreenChanged(this.type == 431);
                    break;
                }
                case 411: 
                case 412: {
                    break;
                }
                default: {
                    throw new RuntimeException("handleViewEvent: unhandled type: " + this.type);
                }
            }
            return null;
        }
    }

    private class MouseEventNotification
    implements PrivilegedAction<Void> {
        View view;
        long time;
        int type;
        int button;
        int x;
        int y;
        int xAbs;
        int yAbs;
        int modifiers;
        boolean isPopupTrigger;
        boolean isSynthesized;

        private MouseEventNotification() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Void run() {
            int n2;
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(GlassViewEventHandler.mouseEventType(this.type).toString());
            }
            switch (this.button) {
                case 212: {
                    n2 = 32;
                    break;
                }
                case 214: {
                    n2 = 128;
                    break;
                }
                case 213: {
                    n2 = 64;
                    break;
                }
                default: {
                    n2 = 0;
                }
            }
            switch (this.type) {
                case 224: {
                    if (this.button == 211) break;
                    return null;
                }
                case 222: {
                    if ((GlassViewEventHandler.this.mouseButtonPressedMask & n2) == 0) {
                        return null;
                    }
                    GlassViewEventHandler.this.mouseButtonPressedMask = GlassViewEventHandler.this.mouseButtonPressedMask & ~n2;
                    break;
                }
                case 221: {
                    GlassViewEventHandler.this.mouseButtonPressedMask = GlassViewEventHandler.this.mouseButtonPressedMask | n2;
                    break;
                }
                case 225: 
                case 226: {
                    break;
                }
                case 227: {
                    return null;
                }
                default: {
                    if (!QuantumToolkit.verbose) break;
                    System.out.println("handleMouseEvent: unhandled type: " + this.type);
                }
            }
            WindowStage windowStage = GlassViewEventHandler.this.scene.getWindowStage();
            try {
                if (windowStage != null) {
                    switch (this.type) {
                        case 221: 
                        case 222: {
                            windowStage.setInAllowedEventHandler(true);
                            break;
                        }
                        default: {
                            windowStage.setInAllowedEventHandler(false);
                        }
                    }
                }
                if (((GlassViewEventHandler)GlassViewEventHandler.this).scene.sceneListener != null) {
                    boolean bl = (this.modifiers & 1) != 0;
                    boolean bl2 = (this.modifiers & 4) != 0;
                    boolean bl3 = (this.modifiers & 8) != 0;
                    boolean bl4 = (this.modifiers & 0x10) != 0;
                    boolean bl5 = (this.modifiers & 0x20) != 0;
                    boolean bl6 = (this.modifiers & 0x80) != 0;
                    boolean bl7 = (this.modifiers & 0x40) != 0;
                    Window window = this.view.getWindow();
                    double d2 = window == null ? 1.0 : (double)window.getPlatformScale();
                    ((GlassViewEventHandler)GlassViewEventHandler.this).scene.sceneListener.mouseEvent(GlassViewEventHandler.mouseEventType(this.type), (double)this.x / d2, (double)this.y / d2, (double)this.xAbs / d2, (double)this.yAbs / d2, GlassViewEventHandler.mouseEventButton(this.button), this.isPopupTrigger, this.isSynthesized, bl, bl2, bl3, bl4, bl5, bl6, bl7);
                }
            }
            finally {
                if (windowStage != null) {
                    windowStage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
            }
            return null;
        }
    }

    private class KeyEventNotification
    implements PrivilegedAction<Void> {
        View view;
        long time;
        int type;
        int key;
        char[] chars;
        int modifiers;
        private KeyCode lastKeyCode;

        private KeyEventNotification() {
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public Void run() {
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.newInput(GlassViewEventHandler.keyEventType(this.type).toString());
            }
            WindowStage windowStage = GlassViewEventHandler.this.scene.getWindowStage();
            try {
                String string;
                boolean bl = (this.modifiers & 1) != 0;
                boolean bl2 = (this.modifiers & 4) != 0;
                boolean bl3 = (this.modifiers & 8) != 0;
                boolean bl4 = (this.modifiers & 0x10) != 0;
                String string2 = string = new String(this.chars);
                KeyEvent keyEvent = new KeyEvent(GlassViewEventHandler.keyEventType(this.type), string, string2, KeyCodeMap.valueOf(this.key), bl, bl2, bl3, bl4);
                KeyCode keyCode = KeyCodeMap.valueOf(this.key);
                switch (this.type) {
                    case 111: 
                    case 112: {
                        this.lastKeyCode = keyCode;
                        break;
                    }
                    case 113: {
                        keyCode = this.lastKeyCode;
                    }
                }
                if (windowStage != null) {
                    if (keyCode == KeyCode.ESCAPE) {
                        windowStage.setInAllowedEventHandler(false);
                    } else {
                        windowStage.setInAllowedEventHandler(true);
                    }
                }
                switch (this.type) {
                    case 111: {
                        if (this.view.isInFullscreen() && windowStage != null && windowStage.getSavedFullScreenExitKey() != null && windowStage.getSavedFullScreenExitKey().match(keyEvent)) {
                            windowStage.exitFullScreen();
                        }
                    }
                    case 112: 
                    case 113: {
                        if (this.view.isInFullscreen() && !GlassViewEventHandler.this.checkFullScreenKeyEvent(this.type, this.key, this.chars, this.modifiers)) {
                            return null;
                        } else {
                            if (((GlassViewEventHandler)GlassViewEventHandler.this).scene.sceneListener == null) return null;
                            ((GlassViewEventHandler)GlassViewEventHandler.this).scene.sceneListener.keyEvent(keyEvent);
                            return null;
                        }
                    }
                    default: {
                        if (!QuantumToolkit.verbose) return null;
                        System.out.println("handleKeyEvent: unhandled type: " + this.type);
                        return null;
                    }
                }
            }
            finally {
                if (windowStage != null) {
                    windowStage.setInAllowedEventHandler(false);
                }
                if (PulseLogger.PULSE_LOGGING_ENABLED) {
                    PulseLogger.newInput(null);
                }
            }
        }
    }
}

