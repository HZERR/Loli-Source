/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.swing;

import java.awt.Component;
import javax.swing.SwingUtilities;
import org.pushingpixels.trident.UIToolkitHandler;

public class SwingToolkitHandler
implements UIToolkitHandler {
    @Override
    public boolean isHandlerFor(Object mainTimelineObject) {
        return mainTimelineObject instanceof Component;
    }

    @Override
    public boolean isInReadyState(Object mainTimelineObject) {
        return ((Component)mainTimelineObject).isDisplayable();
    }

    @Override
    public void runOnUIThread(Object mainTimelineObject, Runnable runnable) {
        if (SwingUtilities.isEventDispatchThread()) {
            runnable.run();
        } else {
            SwingUtilities.invokeLater(runnable);
        }
    }
}

