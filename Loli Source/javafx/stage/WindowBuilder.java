/*
 * Decompiled with CFR 0.150.
 */
package javafx.stage;

import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

@Deprecated
public abstract class WindowBuilder<B extends WindowBuilder<B>> {
    private int __set;
    private EventDispatcher eventDispatcher;
    private boolean focused;
    private double height;
    private EventHandler<WindowEvent> onCloseRequest;
    private EventHandler<WindowEvent> onHidden;
    private EventHandler<WindowEvent> onHiding;
    private EventHandler<WindowEvent> onShowing;
    private EventHandler<WindowEvent> onShown;
    private double opacity;
    private double width;
    private double x;
    private double y;

    protected WindowBuilder() {
    }

    private void __set(int n2) {
        this.__set |= 1 << n2;
    }

    public void applyTo(Window window) {
        int n2 = this.__set;
        while (n2 != 0) {
            int n3 = Integer.numberOfTrailingZeros(n2);
            n2 &= ~(1 << n3);
            switch (n3) {
                case 0: {
                    window.setEventDispatcher(this.eventDispatcher);
                    break;
                }
                case 1: {
                    window.setFocused(this.focused);
                    break;
                }
                case 2: {
                    window.setHeight(this.height);
                    break;
                }
                case 3: {
                    window.setOnCloseRequest(this.onCloseRequest);
                    break;
                }
                case 4: {
                    window.setOnHidden(this.onHidden);
                    break;
                }
                case 5: {
                    window.setOnHiding(this.onHiding);
                    break;
                }
                case 6: {
                    window.setOnShowing(this.onShowing);
                    break;
                }
                case 7: {
                    window.setOnShown(this.onShown);
                    break;
                }
                case 8: {
                    window.setOpacity(this.opacity);
                    break;
                }
                case 9: {
                    window.setWidth(this.width);
                    break;
                }
                case 10: {
                    window.setX(this.x);
                    break;
                }
                case 11: {
                    window.setY(this.y);
                }
            }
        }
    }

    public B eventDispatcher(EventDispatcher eventDispatcher) {
        this.eventDispatcher = eventDispatcher;
        this.__set(0);
        return (B)this;
    }

    public B focused(boolean bl) {
        this.focused = bl;
        this.__set(1);
        return (B)this;
    }

    public B height(double d2) {
        this.height = d2;
        this.__set(2);
        return (B)this;
    }

    public B onCloseRequest(EventHandler<WindowEvent> eventHandler) {
        this.onCloseRequest = eventHandler;
        this.__set(3);
        return (B)this;
    }

    public B onHidden(EventHandler<WindowEvent> eventHandler) {
        this.onHidden = eventHandler;
        this.__set(4);
        return (B)this;
    }

    public B onHiding(EventHandler<WindowEvent> eventHandler) {
        this.onHiding = eventHandler;
        this.__set(5);
        return (B)this;
    }

    public B onShowing(EventHandler<WindowEvent> eventHandler) {
        this.onShowing = eventHandler;
        this.__set(6);
        return (B)this;
    }

    public B onShown(EventHandler<WindowEvent> eventHandler) {
        this.onShown = eventHandler;
        this.__set(7);
        return (B)this;
    }

    public B opacity(double d2) {
        this.opacity = d2;
        this.__set(8);
        return (B)this;
    }

    public B width(double d2) {
        this.width = d2;
        this.__set(9);
        return (B)this;
    }

    public B x(double d2) {
        this.x = d2;
        this.__set(10);
        return (B)this;
    }

    public B y(double d2) {
        this.y = d2;
        this.__set(11);
        return (B)this;
    }
}

