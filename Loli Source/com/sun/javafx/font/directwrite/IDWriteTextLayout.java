/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.directwrite.IUnknown;
import com.sun.javafx.font.directwrite.JFXTextRenderer;
import com.sun.javafx.font.directwrite.OS;

class IDWriteTextLayout
extends IUnknown {
    IDWriteTextLayout(long l2) {
        super(l2);
    }

    int Draw(long l2, JFXTextRenderer jFXTextRenderer, float f2, float f3) {
        return OS.Draw(this.ptr, l2, jFXTextRenderer.ptr, f2, f3);
    }
}

