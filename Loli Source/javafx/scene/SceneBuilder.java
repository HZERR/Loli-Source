/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

@Deprecated
public class SceneBuilder<B extends SceneBuilder<B>>
implements Builder<Scene> {
    private long __set;
    private Camera camera;
    private Cursor cursor;
    private boolean depthBuffer;
    private EventDispatcher eventDispatcher;
    private Paint fill;
    private double height = -1.0;
    private EventHandler<? super ContextMenuEvent> onContextMenuRequested;
    private EventHandler<? super MouseEvent> onDragDetected;
    private EventHandler<? super DragEvent> onDragDone;
    private EventHandler<? super DragEvent> onDragDropped;
    private EventHandler<? super DragEvent> onDragEntered;
    private EventHandler<? super DragEvent> onDragExited;
    private EventHandler<? super DragEvent> onDragOver;
    private EventHandler<? super InputMethodEvent> onInputMethodTextChanged;
    private EventHandler<? super KeyEvent> onKeyPressed;
    private EventHandler<? super KeyEvent> onKeyReleased;
    private EventHandler<? super KeyEvent> onKeyTyped;
    private EventHandler<? super MouseEvent> onMouseClicked;
    private EventHandler<? super MouseDragEvent> onMouseDragEntered;
    private EventHandler<? super MouseDragEvent> onMouseDragExited;
    private EventHandler<? super MouseEvent> onMouseDragged;
    private EventHandler<? super MouseDragEvent> onMouseDragOver;
    private EventHandler<? super MouseDragEvent> onMouseDragReleased;
    private EventHandler<? super MouseEvent> onMouseEntered;
    private EventHandler<? super MouseEvent> onMouseExited;
    private EventHandler<? super MouseEvent> onMouseMoved;
    private EventHandler<? super MouseEvent> onMousePressed;
    private EventHandler<? super MouseEvent> onMouseReleased;
    private EventHandler<? super RotateEvent> onRotate;
    private EventHandler<? super RotateEvent> onRotationFinished;
    private EventHandler<? super RotateEvent> onRotationStarted;
    private EventHandler<? super ScrollEvent> onScroll;
    private EventHandler<? super ScrollEvent> onScrollFinished;
    private EventHandler<? super ScrollEvent> onScrollStarted;
    private EventHandler<? super SwipeEvent> onSwipeDown;
    private EventHandler<? super SwipeEvent> onSwipeLeft;
    private EventHandler<? super SwipeEvent> onSwipeRight;
    private EventHandler<? super SwipeEvent> onSwipeUp;
    private EventHandler<? super TouchEvent> onTouchMoved;
    private EventHandler<? super TouchEvent> onTouchPressed;
    private EventHandler<? super TouchEvent> onTouchReleased;
    private EventHandler<? super TouchEvent> onTouchStationary;
    private EventHandler<? super ZoomEvent> onZoom;
    private EventHandler<? super ZoomEvent> onZoomFinished;
    private EventHandler<? super ZoomEvent> onZoomStarted;
    private Parent root;
    private Collection<? extends String> stylesheets;
    private double width = -1.0;

    protected SceneBuilder() {
    }

    public static SceneBuilder<?> create() {
        return new SceneBuilder();
    }

    private void __set(int n2) {
        this.__set |= 1L << n2;
    }

    public void applyTo(Scene scene) {
        long l2 = this.__set;
        while (l2 != 0L) {
            int n2 = Long.numberOfTrailingZeros(l2);
            l2 &= 1L << n2 ^ 0xFFFFFFFFFFFFFFFFL;
            switch (n2) {
                case 0: {
                    scene.setCamera(this.camera);
                    break;
                }
                case 1: {
                    scene.setCursor(this.cursor);
                    break;
                }
                case 2: {
                    scene.setEventDispatcher(this.eventDispatcher);
                    break;
                }
                case 3: {
                    scene.setFill(this.fill);
                    break;
                }
                case 4: {
                    scene.setOnContextMenuRequested(this.onContextMenuRequested);
                    break;
                }
                case 5: {
                    scene.setOnDragDetected(this.onDragDetected);
                    break;
                }
                case 6: {
                    scene.setOnDragDone(this.onDragDone);
                    break;
                }
                case 7: {
                    scene.setOnDragDropped(this.onDragDropped);
                    break;
                }
                case 8: {
                    scene.setOnDragEntered(this.onDragEntered);
                    break;
                }
                case 9: {
                    scene.setOnDragExited(this.onDragExited);
                    break;
                }
                case 10: {
                    scene.setOnDragOver(this.onDragOver);
                    break;
                }
                case 11: {
                    scene.setOnInputMethodTextChanged(this.onInputMethodTextChanged);
                    break;
                }
                case 12: {
                    scene.setOnKeyPressed(this.onKeyPressed);
                    break;
                }
                case 13: {
                    scene.setOnKeyReleased(this.onKeyReleased);
                    break;
                }
                case 14: {
                    scene.setOnKeyTyped(this.onKeyTyped);
                    break;
                }
                case 15: {
                    scene.setOnMouseClicked(this.onMouseClicked);
                    break;
                }
                case 16: {
                    scene.setOnMouseDragEntered(this.onMouseDragEntered);
                    break;
                }
                case 17: {
                    scene.setOnMouseDragExited(this.onMouseDragExited);
                    break;
                }
                case 18: {
                    scene.setOnMouseDragged(this.onMouseDragged);
                    break;
                }
                case 19: {
                    scene.setOnMouseDragOver(this.onMouseDragOver);
                    break;
                }
                case 20: {
                    scene.setOnMouseDragReleased(this.onMouseDragReleased);
                    break;
                }
                case 21: {
                    scene.setOnMouseEntered(this.onMouseEntered);
                    break;
                }
                case 22: {
                    scene.setOnMouseExited(this.onMouseExited);
                    break;
                }
                case 23: {
                    scene.setOnMouseMoved(this.onMouseMoved);
                    break;
                }
                case 24: {
                    scene.setOnMousePressed(this.onMousePressed);
                    break;
                }
                case 25: {
                    scene.setOnMouseReleased(this.onMouseReleased);
                    break;
                }
                case 26: {
                    scene.setOnRotate(this.onRotate);
                    break;
                }
                case 27: {
                    scene.setOnRotationFinished(this.onRotationFinished);
                    break;
                }
                case 28: {
                    scene.setOnRotationStarted(this.onRotationStarted);
                    break;
                }
                case 29: {
                    scene.setOnScroll(this.onScroll);
                    break;
                }
                case 30: {
                    scene.setOnScrollFinished(this.onScrollFinished);
                    break;
                }
                case 31: {
                    scene.setOnScrollStarted(this.onScrollStarted);
                    break;
                }
                case 32: {
                    scene.setOnSwipeDown(this.onSwipeDown);
                    break;
                }
                case 33: {
                    scene.setOnSwipeLeft(this.onSwipeLeft);
                    break;
                }
                case 34: {
                    scene.setOnSwipeRight(this.onSwipeRight);
                    break;
                }
                case 35: {
                    scene.setOnSwipeUp(this.onSwipeUp);
                    break;
                }
                case 36: {
                    scene.setOnTouchMoved(this.onTouchMoved);
                    break;
                }
                case 37: {
                    scene.setOnTouchPressed(this.onTouchPressed);
                    break;
                }
                case 38: {
                    scene.setOnTouchReleased(this.onTouchReleased);
                    break;
                }
                case 39: {
                    scene.setOnTouchStationary(this.onTouchStationary);
                    break;
                }
                case 40: {
                    scene.setOnZoom(this.onZoom);
                    break;
                }
                case 41: {
                    scene.setOnZoomFinished(this.onZoomFinished);
                    break;
                }
                case 42: {
                    scene.setOnZoomStarted(this.onZoomStarted);
                    break;
                }
                case 43: {
                    scene.getStylesheets().addAll(this.stylesheets);
                }
            }
        }
    }

    public B camera(Camera camera) {
        this.camera = camera;
        this.__set(0);
        return (B)this;
    }

    public B cursor(Cursor cursor) {
        this.cursor = cursor;
        this.__set(1);
        return (B)this;
    }

    public B depthBuffer(boolean bl) {
        this.depthBuffer = bl;
        return (B)this;
    }

    public B eventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        this.__set(2);
        return (B)this;
    }

    public B fill(Paint paint) {
        this.fill = paint;
        this.__set(3);
        return (B)this;
    }

    public B height(double d2) {
        this.height = d2;
        return (B)this;
    }

    public B onContextMenuRequested(EventHandler<? super ContextMenuEvent> eventHandler) {
        this.onContextMenuRequested = eventHandler;
        this.__set(4);
        return (B)this;
    }

    public B onDragDetected(EventHandler<? super MouseEvent> eventHandler) {
        this.onDragDetected = eventHandler;
        this.__set(5);
        return (B)this;
    }

    public B onDragDone(EventHandler<? super DragEvent> eventHandler) {
        this.onDragDone = eventHandler;
        this.__set(6);
        return (B)this;
    }

    public B onDragDropped(EventHandler<? super DragEvent> eventHandler) {
        this.onDragDropped = eventHandler;
        this.__set(7);
        return (B)this;
    }

    public B onDragEntered(EventHandler<? super DragEvent> eventHandler) {
        this.onDragEntered = eventHandler;
        this.__set(8);
        return (B)this;
    }

    public B onDragExited(EventHandler<? super DragEvent> eventHandler) {
        this.onDragExited = eventHandler;
        this.__set(9);
        return (B)this;
    }

    public B onDragOver(EventHandler<? super DragEvent> eventHandler) {
        this.onDragOver = eventHandler;
        this.__set(10);
        return (B)this;
    }

    public B onInputMethodTextChanged(EventHandler<? super InputMethodEvent> eventHandler) {
        this.onInputMethodTextChanged = eventHandler;
        this.__set(11);
        return (B)this;
    }

    public B onKeyPressed(EventHandler<? super KeyEvent> eventHandler) {
        this.onKeyPressed = eventHandler;
        this.__set(12);
        return (B)this;
    }

    public B onKeyReleased(EventHandler<? super KeyEvent> eventHandler) {
        this.onKeyReleased = eventHandler;
        this.__set(13);
        return (B)this;
    }

    public B onKeyTyped(EventHandler<? super KeyEvent> eventHandler) {
        this.onKeyTyped = eventHandler;
        this.__set(14);
        return (B)this;
    }

    public B onMouseClicked(EventHandler<? super MouseEvent> eventHandler) {
        this.onMouseClicked = eventHandler;
        this.__set(15);
        return (B)this;
    }

    public B onMouseDragEntered(EventHandler<? super MouseDragEvent> eventHandler) {
        this.onMouseDragEntered = eventHandler;
        this.__set(16);
        return (B)this;
    }

    public B onMouseDragExited(EventHandler<? super MouseDragEvent> eventHandler) {
        this.onMouseDragExited = eventHandler;
        this.__set(17);
        return (B)this;
    }

    public B onMouseDragged(EventHandler<? super MouseEvent> eventHandler) {
        this.onMouseDragged = eventHandler;
        this.__set(18);
        return (B)this;
    }

    public B onMouseDragOver(EventHandler<? super MouseDragEvent> eventHandler) {
        this.onMouseDragOver = eventHandler;
        this.__set(19);
        return (B)this;
    }

    public B onMouseDragReleased(EventHandler<? super MouseDragEvent> eventHandler) {
        this.onMouseDragReleased = eventHandler;
        this.__set(20);
        return (B)this;
    }

    public B onMouseEntered(EventHandler<? super MouseEvent> eventHandler) {
        this.onMouseEntered = eventHandler;
        this.__set(21);
        return (B)this;
    }

    public B onMouseExited(EventHandler<? super MouseEvent> eventHandler) {
        this.onMouseExited = eventHandler;
        this.__set(22);
        return (B)this;
    }

    public B onMouseMoved(EventHandler<? super MouseEvent> eventHandler) {
        this.onMouseMoved = eventHandler;
        this.__set(23);
        return (B)this;
    }

    public B onMousePressed(EventHandler<? super MouseEvent> eventHandler) {
        this.onMousePressed = eventHandler;
        this.__set(24);
        return (B)this;
    }

    public B onMouseReleased(EventHandler<? super MouseEvent> eventHandler) {
        this.onMouseReleased = eventHandler;
        this.__set(25);
        return (B)this;
    }

    public B onRotate(EventHandler<? super RotateEvent> eventHandler) {
        this.onRotate = eventHandler;
        this.__set(26);
        return (B)this;
    }

    public B onRotationFinished(EventHandler<? super RotateEvent> eventHandler) {
        this.onRotationFinished = eventHandler;
        this.__set(27);
        return (B)this;
    }

    public B onRotationStarted(EventHandler<? super RotateEvent> eventHandler) {
        this.onRotationStarted = eventHandler;
        this.__set(28);
        return (B)this;
    }

    public B onScroll(EventHandler<? super ScrollEvent> eventHandler) {
        this.onScroll = eventHandler;
        this.__set(29);
        return (B)this;
    }

    public B onScrollFinished(EventHandler<? super ScrollEvent> eventHandler) {
        this.onScrollFinished = eventHandler;
        this.__set(30);
        return (B)this;
    }

    public B onScrollStarted(EventHandler<? super ScrollEvent> eventHandler) {
        this.onScrollStarted = eventHandler;
        this.__set(31);
        return (B)this;
    }

    public B onSwipeDown(EventHandler<? super SwipeEvent> eventHandler) {
        this.onSwipeDown = eventHandler;
        this.__set(32);
        return (B)this;
    }

    public B onSwipeLeft(EventHandler<? super SwipeEvent> eventHandler) {
        this.onSwipeLeft = eventHandler;
        this.__set(33);
        return (B)this;
    }

    public B onSwipeRight(EventHandler<? super SwipeEvent> eventHandler) {
        this.onSwipeRight = eventHandler;
        this.__set(34);
        return (B)this;
    }

    public B onSwipeUp(EventHandler<? super SwipeEvent> eventHandler) {
        this.onSwipeUp = eventHandler;
        this.__set(35);
        return (B)this;
    }

    public B onTouchMoved(EventHandler<? super TouchEvent> eventHandler) {
        this.onTouchMoved = eventHandler;
        this.__set(36);
        return (B)this;
    }

    public B onTouchPressed(EventHandler<? super TouchEvent> eventHandler) {
        this.onTouchPressed = eventHandler;
        this.__set(37);
        return (B)this;
    }

    public B onTouchReleased(EventHandler<? super TouchEvent> eventHandler) {
        this.onTouchReleased = eventHandler;
        this.__set(38);
        return (B)this;
    }

    public B onTouchStationary(EventHandler<? super TouchEvent> eventHandler) {
        this.onTouchStationary = eventHandler;
        this.__set(39);
        return (B)this;
    }

    public B onZoom(EventHandler<? super ZoomEvent> eventHandler) {
        this.onZoom = eventHandler;
        this.__set(40);
        return (B)this;
    }

    public B onZoomFinished(EventHandler<? super ZoomEvent> eventHandler) {
        this.onZoomFinished = eventHandler;
        this.__set(41);
        return (B)this;
    }

    public B onZoomStarted(EventHandler<? super ZoomEvent> eventHandler) {
        this.onZoomStarted = eventHandler;
        this.__set(42);
        return (B)this;
    }

    public B root(Parent parent) {
        this.root = parent;
        return (B)this;
    }

    public B stylesheets(Collection<? extends String> collection) {
        this.stylesheets = collection;
        this.__set(43);
        return (B)this;
    }

    public B stylesheets(String ... arrstring) {
        return this.stylesheets(Arrays.asList(arrstring));
    }

    public B width(double d2) {
        this.width = d2;
        return (B)this;
    }

    @Override
    public Scene build() {
        Scene scene = new Scene(this.root, this.width, this.height, this.depthBuffer);
        this.applyTo(scene);
        return scene;
    }
}

