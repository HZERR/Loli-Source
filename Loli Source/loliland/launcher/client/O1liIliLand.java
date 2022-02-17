/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import loliland.launcher.client.IO0I1lilaNd;

public class O1liIliLand {
    private static final Timer I1O1I1LaNd = new Timer("Profiler Sampler", true);
    private int OOOIilanD = 100;
    private long lI00OlAND = TimeUnit.MINUTES.toMillis(5L);
    private Predicate lli0OiIlAND = threadInfo -> true;

    public int I1O1I1LaNd() {
        return this.OOOIilanD;
    }

    public void I1O1I1LaNd(int n2) {
        Preconditions.checkArgument(n2 >= 10, "interval >= 10");
        this.OOOIilanD = n2;
    }

    public Predicate OOOIilanD() {
        return this.lli0OiIlAND;
    }

    public void I1O1I1LaNd(Predicate predicate) {
        Preconditions.checkNotNull(predicate, "threadFilter");
        this.lli0OiIlAND = predicate;
    }

    public long I1O1I1LaNd(TimeUnit timeUnit) {
        return timeUnit.convert(this.lI00OlAND, TimeUnit.MILLISECONDS);
    }

    public void I1O1I1LaNd(long l2, TimeUnit timeUnit) {
        Preconditions.checkArgument(l2 > 0L, "time > 0");
        this.lI00OlAND = timeUnit.toMillis(l2);
    }

    public IO0I1lilaNd lI00OlAND() {
        IO0I1lilaNd iO0I1lilaNd = new IO0I1lilaNd(this.OOOIilanD, this.lli0OiIlAND, System.currentTimeMillis() + this.lI00OlAND, null);
        I1O1I1LaNd.scheduleAtFixedRate((TimerTask)iO0I1lilaNd, 0L, (long)this.OOOIilanD);
        return iO0I1lilaNd;
    }
}

