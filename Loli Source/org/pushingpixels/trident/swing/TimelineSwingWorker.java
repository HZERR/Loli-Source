/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident.swing;

import javax.swing.SwingWorker;
import org.pushingpixels.trident.TimelineScenario;

public abstract class TimelineSwingWorker<T, V>
extends SwingWorker<T, V>
implements TimelineScenario.TimelineScenarioActor {
    @Override
    public void play() {
        this.execute();
    }

    @Override
    public boolean supportsReplay() {
        return false;
    }

    @Override
    public void resetDoneFlag() {
        throw new UnsupportedOperationException();
    }
}

