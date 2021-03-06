/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.stage;

import com.sun.javafx.event.BasicEventDispatcher;
import javafx.event.Event;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public final class WindowCloseRequestHandler
extends BasicEventDispatcher {
    private final Window window;

    public WindowCloseRequestHandler(Window window) {
        this.window = window;
    }

    @Override
    public Event dispatchBubblingEvent(Event event) {
        if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
            this.window.hide();
            event.consume();
        }
        return event;
    }
}

