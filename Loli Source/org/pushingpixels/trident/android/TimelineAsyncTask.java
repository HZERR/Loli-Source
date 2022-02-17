/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  android.os.AsyncTask
 */
package org.pushingpixels.trident.android;

import android.os.AsyncTask;
import org.pushingpixels.trident.TimelineScenario;

public abstract class TimelineAsyncTask<T, V, U>
extends AsyncTask<T, V, U>
implements TimelineScenario.TimelineScenarioActor {
    @Override
    public void play() {
        this.execute(new Object[0]);
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

