/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.eclipse.swt.widgets.Widget
 */
package org.pushingpixels.trident.swt;

import org.eclipse.swt.widgets.Widget;
import org.pushingpixels.trident.UIToolkitHandler;

public class SWTToolkitHandler
implements UIToolkitHandler {
    @Override
    public boolean isHandlerFor(Object mainTimelineObject) {
        return mainTimelineObject instanceof Widget;
    }

    @Override
    public boolean isInReadyState(Object mainTimelineObject) {
        return !((Widget)mainTimelineObject).isDisposed();
    }

    @Override
    public void runOnUIThread(Object mainTimelineObject, Runnable runnable) {
        ((Widget)mainTimelineObject).getDisplay().asyncExec(runnable);
    }
}

