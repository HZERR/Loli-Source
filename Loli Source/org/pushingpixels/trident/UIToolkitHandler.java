/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident;

public interface UIToolkitHandler {
    public boolean isHandlerFor(Object var1);

    public boolean isInReadyState(Object var1);

    public void runOnUIThread(Object var1, Runnable var2);
}

