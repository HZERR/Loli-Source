/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.google.common.base.Predicate;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimerTask;
import java.util.TreeMap;
import loliland.launcher.client.Ililll0land;
import loliland.launcher.client.O1l0i01lLAND;

public class IO0I1lilaNd
extends TimerTask {
    private final int I1O1I1LaNd;
    private final Predicate OOOIilanD;
    private final long lI00OlAND;
    private final SortedMap lli0OiIlAND = new TreeMap();
    private final ThreadMXBean li0iOILAND = ManagementFactory.getThreadMXBean();
    private final SettableFuture O1il1llOLANd = SettableFuture.create();

    private IO0I1lilaNd(int n2, Predicate predicate, long l2) {
        this.I1O1I1LaNd = n2;
        this.OOOIilanD = predicate;
        this.lI00OlAND = l2;
    }

    public ListenableFuture I1O1I1LaNd() {
        return this.O1il1llOLANd;
    }

    private Map OOOIilanD() {
        return this.lli0OiIlAND;
    }

    private O1l0i01lLAND I1O1I1LaNd(String string) {
        O1l0i01lLAND o1l0i01lLAND = (O1l0i01lLAND)this.lli0OiIlAND.get(string);
        if (o1l0i01lLAND == null) {
            o1l0i01lLAND = new O1l0i01lLAND(string);
            this.lli0OiIlAND.put(string, o1l0i01lLAND);
        }
        return o1l0i01lLAND;
    }

    @Override
    public synchronized void run() {
        try {
            ThreadInfo[] arrthreadInfo;
            if (this.lI00OlAND <= System.currentTimeMillis()) {
                this.O1il1llOLANd.set(this);
                this.cancel();
                return;
            }
            for (ThreadInfo threadInfo : arrthreadInfo = this.li0iOILAND.dumpAllThreads(false, false)) {
                String string = threadInfo.getThreadName();
                StackTraceElement[] arrstackTraceElement = threadInfo.getStackTrace();
                if (string == null || arrstackTraceElement == null || !this.OOOIilanD.apply(threadInfo)) continue;
                O1l0i01lLAND o1l0i01lLAND = this.I1O1I1LaNd(string);
                o1l0i01lLAND.I1O1I1LaNd(arrstackTraceElement, (long)this.I1O1I1LaNd);
            }
        }
        catch (Throwable throwable) {
            this.O1il1llOLANd.setException(throwable);
            this.cancel();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry entry : this.OOOIilanD().entrySet()) {
            stringBuilder.append((String)entry.getKey());
            stringBuilder.append(" ");
            stringBuilder.append(((O1l0i01lLAND)entry.getValue()).lI00OlAND()).append("ms");
            stringBuilder.append("\n");
            ((O1l0i01lLAND)entry.getValue()).I1O1I1LaNd(stringBuilder, 1);
        }
        return stringBuilder.toString();
    }

    /* synthetic */ IO0I1lilaNd(int n2, Predicate predicate, long l2, Ililll0land ililll0land) {
        this(n2, predicate, l2);
    }
}

