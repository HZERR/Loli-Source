/*
 * Decompiled with CFR 0.150.
 */
package javafx.scene.input;

import com.sun.javafx.robot.impl.FXRobotHelper;
import com.sun.javafx.scene.input.KeyCodeMap;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.NamedArg;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public final class KeyEvent
extends InputEvent {
    private static final long serialVersionUID = 20121107L;
    public static final EventType<KeyEvent> ANY = new EventType<InputEvent>(InputEvent.ANY, "KEY");
    public static final EventType<KeyEvent> KEY_PRESSED = new EventType<KeyEvent>(ANY, "KEY_PRESSED");
    public static final EventType<KeyEvent> KEY_RELEASED = new EventType<KeyEvent>(ANY, "KEY_RELEASED");
    public static final EventType<KeyEvent> KEY_TYPED = new EventType<KeyEvent>(ANY, "KEY_TYPED");
    public static final String CHAR_UNDEFINED;
    private final String character;
    private final String text;
    private final KeyCode code;
    private final boolean shiftDown;
    private final boolean controlDown;
    private final boolean altDown;
    private final boolean metaDown;

    public KeyEvent(@NamedArg(value="source") Object object, @NamedArg(value="target") EventTarget eventTarget, @NamedArg(value="eventType") EventType<KeyEvent> eventType, @NamedArg(value="character") String string, @NamedArg(value="text") String string2, @NamedArg(value="code") KeyCode keyCode, @NamedArg(value="shiftDown") boolean bl, @NamedArg(value="controlDown") boolean bl2, @NamedArg(value="altDown") boolean bl3, @NamedArg(value="metaDown") boolean bl4) {
        super(object, eventTarget, (EventType<? extends InputEvent>)eventType);
        boolean bl5 = eventType == KEY_TYPED;
        this.character = bl5 ? string : CHAR_UNDEFINED;
        this.text = bl5 ? "" : string2;
        this.code = bl5 ? KeyCode.UNDEFINED : keyCode;
        this.shiftDown = bl;
        this.controlDown = bl2;
        this.altDown = bl3;
        this.metaDown = bl4;
    }

    public KeyEvent(@NamedArg(value="eventType") EventType<KeyEvent> eventType, @NamedArg(value="character") String string, @NamedArg(value="text") String string2, @NamedArg(value="code") KeyCode keyCode, @NamedArg(value="shiftDown") boolean bl, @NamedArg(value="controlDown") boolean bl2, @NamedArg(value="altDown") boolean bl3, @NamedArg(value="metaDown") boolean bl4) {
        super((EventType<? extends InputEvent>)eventType);
        boolean bl5 = eventType == KEY_TYPED;
        this.character = bl5 ? string : CHAR_UNDEFINED;
        this.text = bl5 ? "" : string2;
        this.code = bl5 ? KeyCode.UNDEFINED : keyCode;
        this.shiftDown = bl;
        this.controlDown = bl2;
        this.altDown = bl3;
        this.metaDown = bl4;
    }

    public final String getCharacter() {
        return this.character;
    }

    public final String getText() {
        return this.text;
    }

    public final KeyCode getCode() {
        return this.code;
    }

    public final boolean isShiftDown() {
        return this.shiftDown;
    }

    public final boolean isControlDown() {
        return this.controlDown;
    }

    public final boolean isAltDown() {
        return this.altDown;
    }

    public final boolean isMetaDown() {
        return this.metaDown;
    }

    public final boolean isShortcutDown() {
        switch (Toolkit.getToolkit().getPlatformShortcutKey()) {
            case SHIFT: {
                return this.shiftDown;
            }
            case CONTROL: {
                return this.controlDown;
            }
            case ALT: {
                return this.altDown;
            }
            case META: {
                return this.metaDown;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("KeyEvent [");
        stringBuilder.append("source = ").append(this.getSource());
        stringBuilder.append(", target = ").append(this.getTarget());
        stringBuilder.append(", eventType = ").append(this.getEventType());
        stringBuilder.append(", consumed = ").append(this.isConsumed());
        stringBuilder.append(", character = ").append(this.getCharacter());
        stringBuilder.append(", text = ").append(this.getText());
        stringBuilder.append(", code = ").append((Object)this.getCode());
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
        return stringBuilder.append("]").toString();
    }

    @Override
    public KeyEvent copyFor(Object object, EventTarget eventTarget) {
        return (KeyEvent)super.copyFor(object, eventTarget);
    }

    public KeyEvent copyFor(Object object, EventTarget eventTarget, EventType<KeyEvent> eventType) {
        KeyEvent keyEvent = this.copyFor(object, eventTarget);
        keyEvent.eventType = eventType;
        return keyEvent;
    }

    public EventType<KeyEvent> getEventType() {
        return super.getEventType();
    }

    static {
        FXRobotHelper.FXRobotInputAccessor fXRobotInputAccessor = new FXRobotHelper.FXRobotInputAccessor(){

            @Override
            public int getCodeForKeyCode(KeyCode keyCode) {
                return keyCode.code;
            }

            @Override
            public KeyCode getKeyCodeForCode(int n2) {
                return KeyCodeMap.valueOf(n2);
            }

            @Override
            public KeyEvent createKeyEvent(EventType<? extends KeyEvent> eventType, KeyCode keyCode, String string, String string2, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
                return new KeyEvent(eventType, string, string2, keyCode, bl, bl2, bl3, bl4);
            }

            @Override
            public MouseEvent createMouseEvent(EventType<? extends MouseEvent> eventType, int n2, int n3, int n4, int n5, MouseButton mouseButton, int n6, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6, boolean bl7, boolean bl8) {
                return new MouseEvent(eventType, n2, n3, n4, n5, mouseButton, n6, bl, bl2, bl3, bl4, bl6, bl7, bl8, false, bl5, false, null);
            }

            @Override
            public ScrollEvent createScrollEvent(EventType<? extends ScrollEvent> eventType, int n2, int n3, ScrollEvent.HorizontalTextScrollUnits horizontalTextScrollUnits, int n4, ScrollEvent.VerticalTextScrollUnits verticalTextScrollUnits, int n5, int n6, int n7, int n8, int n9, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
                return new ScrollEvent(ScrollEvent.SCROLL, n6, n7, n8, n9, bl, bl2, bl3, bl4, false, false, n2, n3, 0.0, 0.0, horizontalTextScrollUnits, n4, verticalTextScrollUnits, n5, 0, null);
            }
        };
        FXRobotHelper.setInputAccessor(fXRobotInputAccessor);
        CHAR_UNDEFINED = KeyCode.UNDEFINED.ch;
    }
}

