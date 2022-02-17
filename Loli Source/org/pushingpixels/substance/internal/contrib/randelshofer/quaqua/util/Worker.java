/*
 * Decompiled with CFR 0.150.
 */
package org.pushingpixels.substance.internal.contrib.randelshofer.quaqua.util;

import javax.swing.SwingUtilities;

public abstract class Worker
implements Runnable {
    private Object value;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public final void run() {
        Runnable doFinished = new Runnable(){

            @Override
            public void run() {
                Worker.this.finished(Worker.this.getValue());
            }
        };
        try {
            this.setValue(this.construct());
        }
        catch (Throwable e2) {
            e2.printStackTrace();
        }
        finally {
            SwingUtilities.invokeLater(doFinished);
        }
    }

    public abstract Object construct();

    public abstract void finished(Object var1);

    protected synchronized Object getValue() {
        return this.value;
    }

    private synchronized void setValue(Object x2) {
        this.value = x2;
    }

    public void start() {
        new Thread(this).start();
    }
}

