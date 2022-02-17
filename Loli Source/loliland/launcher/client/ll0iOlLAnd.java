/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.O1iOOlllANd;
import loliland.launcher.client.iOO1IiI1lAnD;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.lii1IO0LaNd;

public final class ll0iOlLAnd {
    private static final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(ll0iOlLAnd::OOOIilanD);
    private final Map OOOIilanD = new HashMap();
    private final ReentrantLock lI00OlAND = new ReentrantLock();

    private ll0iOlLAnd() {
    }

    public static ll0iOlLAnd I1O1I1LaNd() {
        return (ll0iOlLAnd)I1O1I1LaNd.get();
    }

    private static ll0iOlLAnd OOOIilanD() {
        return new ll0iOlLAnd();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public String I1O1I1LaNd(int n2, long l2) {
        this.lI00OlAND.lock();
        try {
            O1IiIiI1LAND o1IiIiI1LAND = (O1IiIiI1LAND)this.OOOIilanD.get(n2);
            if (o1IiIiI1LAND != null && l2 < (Long)o1IiIiI1LAND.I1O1I1LaNd()) {
                String string = (String)o1IiIiI1LAND.OOOIilanD();
                return string;
            }
            long l3 = System.currentTimeMillis();
            WbemcliUtil.WmiResult wmiResult = O1iOOlllANd.I1O1I1LaNd(null);
            if (this.OOOIilanD.size() > wmiResult.getResultCount() * 2) {
                this.OOOIilanD.clear();
            }
            String string = "";
            for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
                int n3 = ii00llanD.O1il1llOLANd(wmiResult, iOO1IiI1lAnD.I1O1I1LaNd, i2);
                String string2 = ii00llanD.I1O1I1LaNd(wmiResult, iOO1IiI1lAnD.OOOIilanD, i2);
                this.OOOIilanD.put(n3, new O1IiIiI1LAND(l3, string2));
                if (n3 != n2) continue;
                string = string2;
            }
            String string3 = string;
            return string3;
        }
        finally {
            this.lI00OlAND.unlock();
        }
    }
}

