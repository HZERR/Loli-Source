/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import java.util.function.Supplier;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.ilI1OllOLANd;
import loliland.launcher.client.lI01110LaNd;
import loliland.launcher.client.lI1iO0IllAND;
import loliland.launcher.client.lIl0iilland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lilO00LANd;

final class liO0O1O1lAND
extends lI01110LaNd {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(liO0O1O1lAND::O1il1llOLANd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(liO0O1O1lAND::Oill1LAnD);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(liO0O1O1lAND::lIOILand);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(this::lil0liLand);

    liO0O1O1lAND() {
    }

    @Override
    public long OOOIilanD() {
        return (Long)this.I1O1I1LaNd.get() * this.lI00OlAND();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)this.OOOIilanD.get();
    }

    @Override
    public long lI00OlAND() {
        return (Long)this.lI00OlAND.get();
    }

    @Override
    public lilO00LANd lli0OiIlAND() {
        return (lilO00LANd)this.lli0OiIlAND.get();
    }

    private static long O1il1llOLANd() {
        Object object2;
        long l2 = 0L;
        long l3 = 0L;
        for (Object object2 : Iill1lanD.I1O1I1LaNd("vmstat -s")) {
            if (((String)object2).endsWith("pages free")) {
                l2 = lOilLanD.Oill1LAnD((String)object2);
                continue;
            }
            if (!((String)object2).endsWith("pages inactive")) continue;
            l3 = lOilLanD.Oill1LAnD((String)object2);
        }
        Object object3 = new int[3];
        object3[0] = 10;
        object3[1] = false;
        object3[2] = 3;
        object2 = lIl0iilland.I1O1I1LaNd((int[])object3);
        ilI1OllOLANd ilI1OllOLANd2 = new ilI1OllOLANd((Pointer)object2);
        return ilI1OllOLANd2.numbufpages + l2 + l3;
    }

    private static long Oill1LAnD() {
        return lIl0iilland.I1O1I1LaNd("hw.physmem", 0L);
    }

    private static long lIOILand() {
        return lIl0iilland.I1O1I1LaNd("hw.pagesize", 4096L);
    }

    private lilO00LANd lil0liLand() {
        return new lI1iO0IllAND(this);
    }
}

