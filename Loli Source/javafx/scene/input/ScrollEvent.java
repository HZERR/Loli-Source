/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.input;

import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.input.GestureEvent;
import javafx.scene.input.PickResult;

public final class ScrollEvent
extends GestureEvent {
    private static final long serialVersionUID = 20121107L;
    public static final EventType<ScrollEvent> ANY = new EventType<GestureEvent>(GestureEvent.ANY, "ANY_SCROLL");
    public static final EventType<ScrollEvent> SCROLL = new EventType<ScrollEvent>(ANY, "SCROLL");
    public static final EventType<ScrollEvent> SCROLL_STARTED = new EventType<ScrollEvent>(ANY, "SCROLL_STARTED");
    public static final EventType<ScrollEvent> SCROLL_FINISHED = new EventType<ScrollEvent>(ANY, "SCROLL_FINISHED");
    private final double deltaX;
    private final double deltaY;
    private double totalDeltaX;
    private final double totalDeltaY;
    private final HorizontalTextScrollUnits textDeltaXUnits;
    private final VerticalTextScrollUnits textDeltaYUnits;
    private final double textDeltaX;
    private final double textDeltaY;
    private final int touchCount;
    private final double multiplierX;
    private final double multiplierY;

    private ScrollEvent(Object object, EventTarget eventTarget, EventType<ScrollEvent> eventType, double d2, double d3, double d4, double d5, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6, double d6, double d7, double d8, double d9, double d10, double d11, HorizontalTextScrollUnits horizontalTextScrollUnits, double d12, VerticalTextScrollUnits verticalTextScrollUnits, double d13, int n2, PickResult pickResult) {
        super(object, eventTarget, eventType, d2, d3, d4, d5, bl, bl2, bl3, bl4, bl5, bl6, pickResult);
        this.deltaX = d6;
        this.deltaY = d7;
        this.totalDeltaX = d8;
        this.totalDeltaY = d9;
        this.textDeltaXUnits = horizontalTextScrollUnits;
        this.textDeltaX = d12;
        this.textDeltaYUnits = verticalTextScrollUnits;
        this.textDeltaY = d13;
        this.touchCount = n2;
        this.multiplierX = d10;
        this.multiplierY = d11;
    }

    public ScrollEvent(@NamedArg(value="source") Object object, @NamedArg(value="target") EventTarget eventTarget, @NamedArg(value="eventType") EventType<ScrollEvent> eventType, @NamedArg(value="x") double d2, @NamedArg(value="y") double d3, @NamedArg(value="screenX") double d4, @NamedArg(value="screenY") double d5, @NamedArg(value="shiftDown") boolean bl, @NamedArg(value="controlDown") boolean bl2, @NamedArg(value="altDown") boolean bl3, @NamedArg(value="metaDown") boolean bl4, @NamedArg(value="direct") boolean bl5, @NamedArg(value="inertia") boolean bl6, @NamedArg(value="deltaX") double d6, @NamedArg(value="deltaY") double d7, @NamedArg(value="totalDeltaX") double d8, @NamedArg(value="totalDeltaY") double d9, @NamedArg(value="textDeltaXUnits") HorizontalTextScrollUnits horizontalTextScrollUnits, @NamedArg(value="textDeltaX") double d10, @NamedArg(value="textDeltaYUnits") VerticalTextScrollUnits verticalTextScrollUnits, @NamedArg(value="textDeltaY") double d11, @NamedArg(value="touchCount") int n2, @NamedArg(value="pickResult") PickResult pickResult) {
        this(object, eventTarget, eventType, d2, d3, d4, d5, bl, bl2, bl3, bl4, bl5, bl6, d6, d7, d8, d9, 1.0, 1.0, horizontalTextScrollUnits, d10, verticalTextScrollUnits, d11, n2, pickResult);
    }

    public ScrollEvent(@NamedArg(value="eventType") EventType<ScrollEvent> eventType, @NamedArg(value="x") double d2, @NamedArg(value="y") double d3, @NamedArg(value="screenX") double d4, @NamedArg(value="screenY") double d5, @NamedArg(value="shiftDown") boolean bl, @NamedArg(value="controlDown") boolean bl2, @NamedArg(value="altDown") boolean bl3, @NamedArg(value="metaDown") boolean bl4, @NamedArg(value="direct") boolean bl5, @NamedArg(value="inertia") boolean bl6, @NamedArg(value="deltaX") double d6, @NamedArg(value="deltaY") double d7, @NamedArg(value="totalDeltaX") double d8, @NamedArg(value="totalDeltaY") double d9, @NamedArg(value="textDeltaXUnits") HorizontalTextScrollUnits horizontalTextScrollUnits, @NamedArg(value="textDeltaX") double d10, @NamedArg(value="textDeltaYUnits") VerticalTextScrollUnits verticalTextScrollUnits, @NamedArg(value="textDeltaY") double d11, @NamedArg(value="touchCount") int n2, @NamedArg(value="pickResult") PickResult pickResult) {
        this(null, null, eventType, d2, d3, d4, d5, bl, bl2, bl3, bl4, bl5, bl6, d6, d7, d8, d9, 1.0, 1.0, horizontalTextScrollUnits, d10, verticalTextScrollUnits, d11, n2, pickResult);
    }

    public ScrollEvent(@NamedArg(value="eventType") EventType<ScrollEvent> eventType, @NamedArg(value="x") double d2, @NamedArg(value="y") double d3, @NamedArg(value="screenX") double d4, @NamedArg(value="screenY") double d5, @NamedArg(value="shiftDown") boolean bl, @NamedArg(value="controlDown") boolean bl2, @NamedArg(value="altDown") boolean bl3, @NamedArg(value="metaDown") boolean bl4, @NamedArg(value="direct") boolean bl5, @NamedArg(value="inertia") boolean bl6, @NamedArg(value="deltaX") double d6, @NamedArg(value="deltaY") double d7, @NamedArg(value="totalDeltaX") double d8, @NamedArg(value="totalDeltaY") double d9, @NamedArg(value="multiplierX") double d10, @NamedArg(value="multiplierY") double d11, @NamedArg(value="textDeltaXUnits") HorizontalTextScrollUnits horizontalTextScrollUnits, @NamedArg(value="textDeltaX") double d12, @NamedArg(value="textDeltaYUnits") VerticalTextScrollUnits verticalTextScrollUnits, @NamedArg(value="textDeltaY") double d13, @NamedArg(value="touchCount") int n2, @NamedArg(value="pickResult") PickResult pickResult) {
        this(null, null, eventType, d2, d3, d4, d5, bl, bl2, bl3, bl4, bl5, bl6, d6, d7, d8, d9, d10, d11, horizontalTextScrollUnits, d12, verticalTextScrollUnits, d13, n2, pickResult);
    }

    public double getDeltaX() {
        return this.deltaX;
    }

    public double getDeltaY() {
        return this.deltaY;
    }

    public double getTotalDeltaX() {
        return this.totalDeltaX;
    }

    public double getTotalDeltaY() {
        return this.totalDeltaY;
    }

    public HorizontalTextScrollUnits getTextDeltaXUnits() {
        return this.textDeltaXUnits;
    }

    public VerticalTextScrollUnits getTextDeltaYUnits() {
        return this.textDeltaYUnits;
    }

    public double getTextDeltaX() {
        return this.textDeltaX;
    }

    public double getTextDeltaY() {
        return this.textDeltaY;
    }

    public int getTouchCount() {
        return this.touchCount;
    }

    public double getMultiplierX() {
        return this.multiplierX;
    }

    public double getMultiplierY() {
        return this.multiplierY;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("ScrollEvent [");
        stringBuilder.append("source = ").append(this.getSource());
        stringBuilder.append(", target = ").append(this.getTarget());
        stringBuilder.append(", eventType = ").append(this.getEventType());
        stringBuilder.append(", consumed = ").append(this.isConsumed());
        stringBuilder.append(", deltaX = ").append(this.getDeltaX()).append(", deltaY = ").append(this.getDeltaY());
        stringBuilder.append(", totalDeltaX = ").append(this.getTotalDeltaX()).append(", totalDeltaY = ").append(this.getTotalDeltaY());
        stringBuilder.append(", textDeltaXUnits = ").append((Object)this.getTextDeltaXUnits()).append(", textDeltaX = ").append(this.getTextDeltaX());
        stringBuilder.append(", textDeltaYUnits = ").append((Object)this.getTextDeltaYUnits()).append(", textDeltaY = ").append(this.getTextDeltaY());
        stringBuilder.append(", touchCount = ").append(this.getTouchCount());
        stringBuilder.append(", x = ").append(this.getX()).append(", y = ").append(this.getY()).append(", z = ").append(this.getZ());
        stringBuilder.append(this.isDirect() ? ", direct" : ", indirect");
        if (this.isInertia()) {
            stringBuilder.append(", inertia");
        }
        if (this.isShiftDown()) {
            stringBuilder.append(", shiftDown");
        }
        if (this.isControlDown()) {
            stringBuilder.append(", controlDown");
        }
        if (this.isAltDown()) {
            stringBuilder.append(", altDown");
        }
        if (this.isMetaDown()) {
            stringBuilder.append(", metaDown");
        }
        if (this.isShortcutDown()) {
            stringBuilder.append(", shortcutDown");
        }
        stringBuilder.append(", pickResult = ").append(this.getPickResult());
        return stringBuilder.append("]").toString();
    }

    @Override
    public ScrollEvent copyFor(Object object, EventTarget eventTarget) {
        return (ScrollEvent)super.copyFor(object, eventTarget);
    }

    public ScrollEvent copyFor(Object object, EventTarget eventTarget, EventType<ScrollEvent> eventType) {
        ScrollEvent scrollEvent = this.copyFor(object, eventTarget);
        scrollEvent.eventType = eventType;
        return scrollEvent;
    }

    public EventType<ScrollEvent> getEventType() {
        return super.getEventType();
    }

    public static enum VerticalTextScrollUnits {
        NONE,
        LINES,
        PAGES;

    }

    public static enum HorizontalTextScrollUnits {
        NONE,
        CHARACTERS;

    }
}

