/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.graphics.Cursor
 *  org.eclipse.swt.widgets.Display
 */
package javafx.embed.swt;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

class SWTCursors {
    SWTCursors() {
    }

    private static Cursor createCustomCursor(ImageCursorFrame imageCursorFrame) {
        return null;
    }

    static Cursor embedCursorToCursor(CursorFrame cursorFrame) {
        int n2 = 0;
        switch (cursorFrame.getCursorType()) {
            case DEFAULT: {
                n2 = 0;
                break;
            }
            case CROSSHAIR: {
                n2 = 2;
                break;
            }
            case TEXT: {
                n2 = 19;
                break;
            }
            case WAIT: {
                n2 = 1;
                break;
            }
            case SW_RESIZE: {
                n2 = 16;
                break;
            }
            case SE_RESIZE: {
                n2 = 15;
                break;
            }
            case NW_RESIZE: {
                n2 = 17;
                break;
            }
            case NE_RESIZE: {
                n2 = 14;
                break;
            }
            case N_RESIZE: {
                n2 = 10;
                break;
            }
            case S_RESIZE: {
                n2 = 11;
                break;
            }
            case W_RESIZE: {
                n2 = 13;
                break;
            }
            case E_RESIZE: {
                n2 = 12;
                break;
            }
            case OPEN_HAND: 
            case CLOSED_HAND: 
            case HAND: {
                n2 = 21;
                break;
            }
            case MOVE: {
                n2 = 5;
                break;
            }
            case DISAPPEAR: {
                break;
            }
            case H_RESIZE: {
                n2 = 9;
                break;
            }
            case V_RESIZE: {
                n2 = 7;
                break;
            }
            case NONE: {
                return null;
            }
        }
        Display display = Display.getCurrent();
        return display != null ? display.getSystemCursor(n2) : null;
    }
}

