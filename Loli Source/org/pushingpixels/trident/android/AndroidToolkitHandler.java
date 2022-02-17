/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package org.pushingpixels.trident.android;

import android.view.View;
import org.pushingpixels.trident.UIToolkitHandler;

public class AndroidToolkitHandler
implements UIToolkitHandler {
    @Override
    public boolean isHandlerFor(Object mainTimelineObject) {
        return mainTimelineObject instanceof View;
    }

    @Override
    public boolean isInReadyState(Object mainTimelineObject) {
        return true;
    }

    @Override
    public void runOnUIThread(Object mainTimelineObject, Runnable runnable) {
        ((View)mainTimelineObject).post(runnable);
    }
}

