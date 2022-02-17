/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.lafwidget.utils;

import java.util.HashSet;
import java.util.Set;

public abstract class TrackableThread
extends Thread {
    private static Set<TrackableThread> threads = new HashSet<TrackableThread>();

    protected TrackableThread() {
        threads.add(this);
        this.setDaemon(true);
    }

    protected abstract void requestStop();

    public static void requestStopAllThreads() {
        for (TrackableThread tt : threads) {
            tt.requestStop();
        }
    }
}

