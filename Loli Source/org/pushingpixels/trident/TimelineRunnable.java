/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.trident;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.pushingpixels.trident.TimelineScenario;

public abstract class TimelineRunnable
implements Runnable,
TimelineScenario.TimelineScenarioActor {
    private static ExecutorService service = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    private Future<?> future;

    @Override
    public void play() {
        this.future = service.submit(this);
    }

    @Override
    public boolean isDone() {
        if (this.future == null) {
            return false;
        }
        return this.future.isDone();
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

