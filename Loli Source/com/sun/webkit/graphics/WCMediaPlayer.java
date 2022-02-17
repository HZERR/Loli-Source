/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import com.sun.webkit.Invoker;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.WCGraphicsContext;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WCMediaPlayer
extends Ref {
    protected static final Logger log = Logger.getLogger("webkit.mediaplayer");
    protected static final boolean verbose;
    private long nPtr;
    protected static final int NETWORK_STATE_EMPTY = 0;
    protected static final int NETWORK_STATE_IDLE = 1;
    protected static final int NETWORK_STATE_LOADING = 2;
    protected static final int NETWORK_STATE_LOADED = 3;
    protected static final int NETWORK_STATE_FORMAT_ERROR = 4;
    protected static final int NETWORK_STATE_NETWORK_ERROR = 5;
    protected static final int NETWORK_STATE_DECODE_ERROR = 6;
    protected static final int READY_STATE_HAVE_NOTHING = 0;
    protected static final int READY_STATE_HAVE_METADATA = 1;
    protected static final int READY_STATE_HAVE_CURRENT_DATA = 2;
    protected static final int READY_STATE_HAVE_FUTURE_DATA = 3;
    protected static final int READY_STATE_HAVE_ENOUGH_DATA = 4;
    protected static final int PRELOAD_NONE = 0;
    protected static final int PRELOAD_METADATA = 1;
    protected static final int PRELOAD_AUTO = 2;
    private int networkState = 0;
    private int readyState = 0;
    private int preload = 2;
    private boolean paused = true;
    private boolean seeking = false;
    private Runnable newFrameNotifier = () -> {
        if (this.nPtr != 0L) {
            this.notifyNewFrame(this.nPtr);
        }
    };
    private boolean preserve = true;

    protected WCMediaPlayer() {
    }

    void setNativePointer(long l2) {
        if (l2 == 0L) {
            throw new IllegalArgumentException("nativePointer is 0");
        }
        if (this.nPtr != 0L) {
            throw new IllegalStateException("nPtr is not 0");
        }
        this.nPtr = l2;
    }

    protected abstract void load(String var1, String var2);

    protected abstract void cancelLoad();

    protected abstract void disposePlayer();

    protected abstract void prepareToPlay();

    protected abstract void play();

    protected abstract void pause();

    protected abstract float getCurrentTime();

    protected abstract void seek(float var1);

    protected abstract void setRate(float var1);

    protected abstract void setVolume(float var1);

    protected abstract void setMute(boolean var1);

    protected abstract void setSize(int var1, int var2);

    protected abstract void setPreservesPitch(boolean var1);

    protected abstract void renderCurrentFrame(WCGraphicsContext var1, int var2, int var3, int var4, int var5);

    protected boolean getPreservesPitch() {
        return this.preserve;
    }

    protected int getNetworkState() {
        return this.networkState;
    }

    protected int getReadyState() {
        return this.readyState;
    }

    protected int getPreload() {
        return this.preload;
    }

    protected boolean isPaused() {
        return this.paused;
    }

    protected boolean isSeeking() {
        return this.seeking;
    }

    protected void notifyNetworkStateChanged(int n2) {
        if (this.networkState != n2) {
            this.networkState = n2;
            int n3 = n2;
            Invoker.getInvoker().invokeOnEventThread(() -> {
                if (this.nPtr != 0L) {
                    this.notifyNetworkStateChanged(this.nPtr, n3);
                }
            });
        }
    }

    protected void notifyReadyStateChanged(int n2) {
        if (this.readyState != n2) {
            this.readyState = n2;
            int n3 = n2;
            Invoker.getInvoker().invokeOnEventThread(() -> {
                if (this.nPtr != 0L) {
                    this.notifyReadyStateChanged(this.nPtr, n3);
                }
            });
        }
    }

    protected void notifyPaused(boolean bl) {
        if (verbose) {
            log.log(Level.FINE, "notifyPaused, {0} => {1}", new Object[]{this.paused, bl});
        }
        if (this.paused != bl) {
            this.paused = bl;
            boolean bl2 = bl;
            Invoker.getInvoker().invokeOnEventThread(() -> {
                if (this.nPtr != 0L) {
                    this.notifyPaused(this.nPtr, bl2);
                }
            });
        }
    }

    protected void notifySeeking(boolean bl, int n2) {
        if (verbose) {
            log.log(Level.FINE, "notifySeeking, {0} => {1}", new Object[]{this.seeking, bl});
        }
        if (this.seeking != bl || this.readyState != n2) {
            this.seeking = bl;
            this.readyState = n2;
            boolean bl2 = bl;
            int n3 = n2;
            Invoker.getInvoker().invokeOnEventThread(() -> {
                if (this.nPtr != 0L) {
                    this.notifySeeking(this.nPtr, bl2, n3);
                }
            });
        }
    }

    protected void notifyFinished() {
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0L) {
                this.notifyFinished(this.nPtr);
            }
        });
    }

    protected void notifyReady(boolean bl, boolean bl2, float f2) {
        boolean bl3 = bl;
        boolean bl4 = bl2;
        float f3 = f2;
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0L) {
                this.notifyReady(this.nPtr, bl3, bl4, f3);
            }
        });
    }

    protected void notifyDurationChanged(float f2) {
        float f3 = f2;
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0L) {
                this.notifyDurationChanged(this.nPtr, f3);
            }
        });
    }

    protected void notifySizeChanged(int n2, int n3) {
        int n4 = n2;
        int n5 = n3;
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0L) {
                this.notifySizeChanged(this.nPtr, n4, n5);
            }
        });
    }

    protected void notifyNewFrame() {
        Invoker.getInvoker().invokeOnEventThread(this.newFrameNotifier);
    }

    protected void notifyBufferChanged(float[] arrf, int n2) {
        float[] arrf2 = arrf;
        int n3 = n2;
        Invoker.getInvoker().invokeOnEventThread(() -> {
            if (this.nPtr != 0L) {
                this.notifyBufferChanged(this.nPtr, arrf2, n3);
            }
        });
    }

    private void fwkLoad(String string, String string2) {
        if (verbose) {
            log.log(Level.FINE, "fwkLoad, url={0}, userAgent={1}", new Object[]{string, string2});
        }
        this.load(string, string2);
    }

    private void fwkCancelLoad() {
        if (verbose) {
            log.log(Level.FINE, "fwkCancelLoad");
        }
        this.cancelLoad();
    }

    private void fwkPrepareToPlay() {
        if (verbose) {
            log.log(Level.FINE, "fwkPrepareToPlay");
        }
        this.prepareToPlay();
    }

    private void fwkDispose() {
        if (verbose) {
            log.log(Level.FINE, "fwkDispose");
        }
        this.nPtr = 0L;
        this.cancelLoad();
        this.disposePlayer();
    }

    private void fwkPlay() {
        if (verbose) {
            log.log(Level.FINE, "fwkPlay");
        }
        this.play();
    }

    private void fwkPause() {
        if (verbose) {
            log.log(Level.FINE, "fwkPause");
        }
        this.pause();
    }

    private float fwkGetCurrentTime() {
        float f2 = this.getCurrentTime();
        if (verbose) {
            log.log(Level.FINER, "fwkGetCurrentTime(), return {0}", Float.valueOf(f2));
        }
        return f2;
    }

    private void fwkSeek(float f2) {
        if (verbose) {
            log.log(Level.FINE, "fwkSeek({0})", Float.valueOf(f2));
        }
        this.seek(f2);
    }

    private void fwkSetRate(float f2) {
        if (verbose) {
            log.log(Level.FINE, "fwkSetRate({0})", Float.valueOf(f2));
        }
        this.setRate(f2);
    }

    private void fwkSetVolume(float f2) {
        if (verbose) {
            log.log(Level.FINE, "fwkSetVolume({0})", Float.valueOf(f2));
        }
        this.setVolume(f2);
    }

    private void fwkSetMute(boolean bl) {
        if (verbose) {
            log.log(Level.FINE, "fwkSetMute({0})", bl);
        }
        this.setMute(bl);
    }

    private void fwkSetSize(int n2, int n3) {
        this.setSize(n2, n3);
    }

    private void fwkSetPreservesPitch(boolean bl) {
        if (verbose) {
            log.log(Level.FINE, "setPreservesPitch({0})", bl);
        }
        this.preserve = bl;
        this.setPreservesPitch(bl);
    }

    private void fwkSetPreload(int n2) {
        if (verbose) {
            log.log(Level.FINE, "fwkSetPreload({0})", n2 == 0 ? "PRELOAD_NONE" : (n2 == 1 ? "PRELOAD_METADATA" : (n2 == 2 ? "PRELOAD_AUTO" : "INVALID VALUE: " + n2)));
        }
        this.preload = n2;
    }

    void render(WCGraphicsContext wCGraphicsContext, int n2, int n3, int n4, int n5) {
        if (verbose) {
            log.log(Level.FINER, "render(x={0}, y={1}, w={2}, h={3}", new Object[]{n2, n3, n4, n5});
        }
        this.renderCurrentFrame(wCGraphicsContext, n2, n3, n4, n5);
    }

    private native void notifyNetworkStateChanged(long var1, int var3);

    private native void notifyReadyStateChanged(long var1, int var3);

    private native void notifyPaused(long var1, boolean var3);

    private native void notifySeeking(long var1, boolean var3, int var4);

    private native void notifyFinished(long var1);

    private native void notifyReady(long var1, boolean var3, boolean var4, float var5);

    private native void notifyDurationChanged(long var1, float var3);

    private native void notifySizeChanged(long var1, int var3, int var4);

    private native void notifyNewFrame(long var1);

    private native void notifyBufferChanged(long var1, float[] var3, int var4);

    static {
        if (log.getLevel() == null) {
            verbose = false;
            log.setLevel(Level.OFF);
        } else {
            verbose = true;
            log.log(Level.CONFIG, "webkit.mediaplayer logging is ON, level: {0}", log.getLevel());
        }
    }
}

