/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Skinnable;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

public abstract class BehaviorSkinBase<C extends Control, BB extends BehaviorBase<C>>
extends SkinBase<C> {
    protected static final boolean IS_TOUCH_SUPPORTED = Platform.isSupported(ConditionalFeature.INPUT_TOUCH);
    private BB behavior;
    private MultiplePropertyChangeListenerHandler changeListenerHandler;
    private final EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>(){

        @Override
        public void handle(MouseEvent mouseEvent) {
            EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
            if (eventType == MouseEvent.MOUSE_ENTERED) {
                BehaviorSkinBase.this.behavior.mouseEntered(mouseEvent);
            } else if (eventType == MouseEvent.MOUSE_EXITED) {
                BehaviorSkinBase.this.behavior.mouseExited(mouseEvent);
            } else if (eventType == MouseEvent.MOUSE_PRESSED) {
                BehaviorSkinBase.this.behavior.mousePressed(mouseEvent);
            } else if (eventType == MouseEvent.MOUSE_RELEASED) {
                BehaviorSkinBase.this.behavior.mouseReleased(mouseEvent);
            } else if (eventType == MouseEvent.MOUSE_DRAGGED) {
                BehaviorSkinBase.this.behavior.mouseDragged(mouseEvent);
            } else {
                throw new AssertionError((Object)"Unsupported event type received");
            }
        }
    };
    private final EventHandler<ContextMenuEvent> contextMenuHandler = new EventHandler<ContextMenuEvent>(){

        @Override
        public void handle(ContextMenuEvent contextMenuEvent) {
            BehaviorSkinBase.this.behavior.contextMenuRequested(contextMenuEvent);
        }
    };

    protected BehaviorSkinBase(C c2, BB BB) {
        super(c2);
        if (BB == null) {
            throw new IllegalArgumentException("Cannot pass null for behavior");
        }
        this.behavior = BB;
        ((Node)c2).addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseHandler);
        ((Node)c2).addEventHandler(MouseEvent.MOUSE_EXITED, this.mouseHandler);
        ((Node)c2).addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
        ((Node)c2).addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
        ((Node)c2).addEventHandler(MouseEvent.MOUSE_DRAGGED, this.mouseHandler);
        ((Node)c2).addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, this.contextMenuHandler);
    }

    public final BB getBehavior() {
        return this.behavior;
    }

    @Override
    public void dispose() {
        Skinnable skinnable;
        if (this.changeListenerHandler != null) {
            this.changeListenerHandler.dispose();
        }
        if ((skinnable = this.getSkinnable()) != null) {
            ((Node)((Object)skinnable)).removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseHandler);
            ((Node)((Object)skinnable)).removeEventHandler(MouseEvent.MOUSE_EXITED, this.mouseHandler);
            ((Node)((Object)skinnable)).removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
            ((Node)((Object)skinnable)).removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
            ((Node)((Object)skinnable)).removeEventHandler(MouseEvent.MOUSE_DRAGGED, this.mouseHandler);
        }
        if (this.behavior != null) {
            ((BehaviorBase)this.behavior).dispose();
            this.behavior = null;
        }
        super.dispose();
    }

    protected final void registerChangeListener(ObservableValue<?> observableValue, String string2) {
        if (this.changeListenerHandler == null) {
            this.changeListenerHandler = new MultiplePropertyChangeListenerHandler(string -> {
                this.handleControlPropertyChanged((String)string);
                return null;
            });
        }
        this.changeListenerHandler.registerChangeListener(observableValue, string2);
    }

    protected void handleControlPropertyChanged(String string) {
    }
}

