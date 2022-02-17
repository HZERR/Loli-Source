/*
 * Decompiled with CFR 0.150.
 */
package javafx.embed.swing;

import java.awt.event.MouseWheelEvent;
import javafx.event.EventType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

class SwingEvents {
    SwingEvents() {
    }

    static int mouseIDToEmbedMouseType(int n2) {
        switch (n2) {
            case 501: {
                return 0;
            }
            case 502: {
                return 1;
            }
            case 500: {
                return 2;
            }
            case 503: {
                return 5;
            }
            case 506: {
                return 6;
            }
            case 504: {
                return 3;
            }
            case 505: {
                return 4;
            }
            case 507: {
                return 7;
            }
        }
        return 0;
    }

    static int mouseButtonToEmbedMouseButton(int n2, int n3) {
        int n4 = 0;
        switch (n2) {
            case 1: {
                n4 = 1;
                break;
            }
            case 2: {
                n4 = 4;
                break;
            }
            case 3: {
                n4 = 2;
                break;
            }
        }
        if ((n3 & 0x400) != 0) {
            n4 = 1;
        } else if ((n3 & 0x800) != 0) {
            n4 = 4;
        } else if ((n3 & 0x1000) != 0) {
            n4 = 2;
        }
        return n4;
    }

    static int getWheelRotation(java.awt.event.MouseEvent mouseEvent) {
        if (mouseEvent instanceof MouseWheelEvent) {
            return ((MouseWheelEvent)mouseEvent).getWheelRotation();
        }
        return 0;
    }

    static int keyIDToEmbedKeyType(int n2) {
        switch (n2) {
            case 401: {
                return 0;
            }
            case 402: {
                return 1;
            }
            case 400: {
                return 2;
            }
        }
        return 0;
    }

    static int keyModifiersToEmbedKeyModifiers(int n2) {
        int n3 = 0;
        if ((n2 & 0x40) != 0) {
            n3 |= 1;
        }
        if ((n2 & 0x80) != 0) {
            n3 |= 2;
        }
        if ((n2 & 0x200) != 0) {
            n3 |= 4;
        }
        if ((n2 & 0x100) != 0) {
            n3 |= 8;
        }
        return n3;
    }

    static char keyCharToEmbedKeyChar(char c2) {
        return c2 == '\n' ? (char)'\r' : (char)c2;
    }

    static int fxMouseEventTypeToMouseID(MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> eventType = mouseEvent.getEventType();
        if (eventType == MouseEvent.MOUSE_MOVED) {
            return 503;
        }
        if (eventType == MouseEvent.MOUSE_PRESSED) {
            return 501;
        }
        if (eventType == MouseEvent.MOUSE_RELEASED) {
            return 502;
        }
        if (eventType == MouseEvent.MOUSE_CLICKED) {
            return 500;
        }
        if (eventType == MouseEvent.MOUSE_ENTERED) {
            return 504;
        }
        if (eventType == MouseEvent.MOUSE_EXITED) {
            return 505;
        }
        if (eventType == MouseEvent.MOUSE_DRAGGED) {
            return 506;
        }
        if (eventType == MouseEvent.DRAG_DETECTED) {
            return -1;
        }
        throw new RuntimeException("Unknown MouseEvent type: " + eventType);
    }

    static int fxMouseModsToMouseMods(MouseEvent mouseEvent) {
        int n2 = 0;
        if (mouseEvent.isAltDown()) {
            n2 |= 0x200;
        }
        if (mouseEvent.isControlDown()) {
            n2 |= 0x80;
        }
        if (mouseEvent.isMetaDown()) {
            n2 |= 0x100;
        }
        if (mouseEvent.isShiftDown()) {
            n2 |= 0x40;
        }
        if (mouseEvent.isPrimaryButtonDown()) {
            n2 |= 0x400;
        }
        if (mouseEvent.isSecondaryButtonDown()) {
            n2 |= 0x1000;
        }
        if (mouseEvent.isMiddleButtonDown()) {
            n2 |= 0x800;
        }
        return n2;
    }

    static int fxMouseButtonToMouseButton(MouseEvent mouseEvent) {
        switch (mouseEvent.getButton()) {
            case PRIMARY: {
                return 1;
            }
            case SECONDARY: {
                return 3;
            }
            case MIDDLE: {
                return 2;
            }
        }
        return 0;
    }

    static int fxKeyEventTypeToKeyID(KeyEvent keyEvent) {
        EventType<KeyEvent> eventType = keyEvent.getEventType();
        if (eventType == KeyEvent.KEY_PRESSED) {
            return 401;
        }
        if (eventType == KeyEvent.KEY_RELEASED) {
            return 402;
        }
        if (eventType == KeyEvent.KEY_TYPED) {
            return 400;
        }
        throw new RuntimeException("Unknown KeyEvent type: " + eventType);
    }

    static int fxKeyModsToKeyMods(KeyEvent keyEvent) {
        int n2 = 0;
        if (keyEvent.isAltDown()) {
            n2 |= 0x200;
        }
        if (keyEvent.isControlDown()) {
            n2 |= 0x80;
        }
        if (keyEvent.isMetaDown()) {
            n2 |= 0x100;
        }
        if (keyEvent.isShiftDown()) {
            n2 |= 0x40;
        }
        return n2;
    }

    static int fxScrollModsToMouseWheelMods(ScrollEvent scrollEvent) {
        int n2 = 0;
        if (scrollEvent.isAltDown()) {
            n2 |= 0x200;
        }
        if (scrollEvent.isControlDown()) {
            n2 |= 0x80;
        }
        if (scrollEvent.isMetaDown()) {
            n2 |= 0x100;
        }
        if (scrollEvent.isShiftDown()) {
            n2 |= 0x40;
        }
        return n2;
    }
}

