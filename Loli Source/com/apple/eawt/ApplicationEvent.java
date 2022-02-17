/*
 * Decompiled with CFR 0.150.
 */
package com.apple.eawt;

import java.util.EventObject;

public class ApplicationEvent
extends EventObject {
    ApplicationEvent(Object object) {
        super(object);
    }

    ApplicationEvent(Object object, String string) {
        super(object);
    }

    public boolean isHandled() {
        return false;
    }

    public void setHandled(boolean bl) {
    }

    public String getFilename() {
        return null;
    }
}

