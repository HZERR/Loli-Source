/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.behavior.BehaviorBase;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class SpinnerBehavior<T>
extends BehaviorBase<Spinner<T>> {
    private static final double INITIAL_DURATION_MS = 750.0;
    private final int STEP_AMOUNT = 1;
    private boolean isIncrementing = false;
    private Timeline timeline;
    final EventHandler<ActionEvent> spinningKeyFrameEventHandler = actionEvent -> {
        SpinnerValueFactory spinnerValueFactory = ((Spinner)this.getControl()).getValueFactory();
        if (spinnerValueFactory == null) {
            return;
        }
        if (this.isIncrementing) {
            this.increment(1);
        } else {
            this.decrement(1);
        }
    };
    private boolean keyDown;
    protected static final List<KeyBinding> SPINNER_BINDINGS = new ArrayList<KeyBinding>();

    public SpinnerBehavior(Spinner<T> spinner) {
        super(spinner, SPINNER_BINDINGS);
    }

    @Override
    protected void callAction(String string) {
        boolean bl = this.arrowsAreVertical();
        switch (string) {
            case "increment-up": {
                if (bl) {
                    this.increment(1);
                    break;
                }
                this.traverseUp();
                break;
            }
            case "increment-right": {
                if (!bl) {
                    this.increment(1);
                    break;
                }
                this.traverseRight();
                break;
            }
            case "decrement-down": {
                if (bl) {
                    this.decrement(1);
                    break;
                }
                this.traverseDown();
                break;
            }
            case "decrement-left": {
                if (!bl) {
                    this.decrement(1);
                    break;
                }
                this.traverseLeft();
                break;
            }
            default: {
                super.callAction(string);
            }
        }
    }

    public void increment(int n2) {
        ((Spinner)this.getControl()).increment(n2);
    }

    public void decrement(int n2) {
        ((Spinner)this.getControl()).decrement(n2);
    }

    public void startSpinning(boolean bl) {
        this.isIncrementing = bl;
        if (this.timeline != null) {
            this.timeline.stop();
        }
        this.timeline = new Timeline();
        this.timeline.setCycleCount(-1);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(750.0), this.spinningKeyFrameEventHandler, new KeyValue[0]);
        this.timeline.getKeyFrames().setAll(keyFrame);
        this.timeline.playFromStart();
        this.timeline.play();
        this.spinningKeyFrameEventHandler.handle(null);
    }

    public void stopSpinning() {
        if (this.timeline != null) {
            this.timeline.stop();
        }
    }

    private boolean arrowsAreVertical() {
        ObservableList<String> observableList = ((Spinner)this.getControl()).getStyleClass();
        return !observableList.contains("arrows-on-left-horizontal") && !observableList.contains("arrows-on-right-horizontal") && !observableList.contains("split-arrows-horizontal");
    }

    static {
        SPINNER_BINDINGS.add(new KeyBinding(KeyCode.UP, "increment-up"));
        SPINNER_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "increment-right"));
        SPINNER_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "decrement-left"));
        SPINNER_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "decrement-down"));
    }
}

