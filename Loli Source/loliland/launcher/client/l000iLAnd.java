/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation;

public final class l000iLAnd {
    private l000iLAnd() {
    }

    public static String I1O1I1LaNd(Pointer pointer) {
        return l000iLAnd.I1O1I1LaNd(pointer, true);
    }

    public static String I1O1I1LaNd(Pointer pointer, boolean bl) {
        String string = "";
        if (pointer != null) {
            CoreFoundation.CFStringRef cFStringRef = new CoreFoundation.CFStringRef(pointer);
            string = cFStringRef.stringValue();
        }
        if (bl && string.isEmpty()) {
            return "unknown";
        }
        return string;
    }
}

