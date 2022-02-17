/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.mac;

import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.delegate.ClipboardDelegate;
import com.sun.glass.ui.mac.MacDnDClipboard;
import com.sun.glass.ui.mac.MacSystemClipboard;

final class MacClipboardDelegate
implements ClipboardDelegate {
    MacClipboardDelegate() {
    }

    @Override
    public Clipboard createClipboard(String string) {
        if ("SYSTEM".equals(string)) {
            return new MacSystemClipboard(string);
        }
        if ("DND".equals(string)) {
            return new MacDnDClipboard(string);
        }
        return null;
    }
}

