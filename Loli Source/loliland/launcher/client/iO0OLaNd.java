/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.solaris.LibKstat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import loliland.launcher.client.IIOlO0ilAnD;
import loliland.launcher.client.OI1i0ilanD;
import loliland.launcher.client.OI1l11LaNd;
import loliland.launcher.client.OIl0IilanD;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.ilIlllanD;
import loliland.launcher.client.lIiIlOi0lanD;
import loliland.launcher.client.lO0li10llaND;

public final class iO0OLaNd
extends OI1i0ilanD {
    private long I1O1I1LaNd = 0L;
    private long OOOIilanD = 0L;
    private long lI00OlAND = 0L;
    private long lli0OiIlAND = 0L;
    private long li0iOILAND = 0L;
    private long O1il1llOLANd = 0L;
    private long Oill1LAnD = 0L;
    private List lIOILand;

    private iO0OLaNd(String string, String string2, String string3, long l2) {
        super(string, string2, string3, l2);
    }

    @Override
    public long li0iOILAND() {
        return this.I1O1I1LaNd;
    }

    @Override
    public long O1il1llOLANd() {
        return this.OOOIilanD;
    }

    @Override
    public long Oill1LAnD() {
        return this.lI00OlAND;
    }

    @Override
    public long lIOILand() {
        return this.lli0OiIlAND;
    }

    @Override
    public long lil0liLand() {
        return this.li0iOILAND;
    }

    @Override
    public long iilIi1laND() {
        return this.O1il1llOLANd;
    }

    @Override
    public long l0illAND() {
        return this.Oill1LAnD;
    }

    @Override
    public List lli011lLANd() {
        return this.lIOILand;
    }

    @Override
    public boolean IO11O0LANd() {
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            LibKstat.Kstat kstat = lIiIlOi0lanD.I1O1I1LaNd(null, 0, this.I1O1I1LaNd());
            if (kstat != null && lIiIlOi0lanD.I1O1I1LaNd(kstat)) {
                LibKstat.KstatIO kstatIO = new LibKstat.KstatIO(kstat.ks_data);
                this.I1O1I1LaNd = kstatIO.reads;
                this.lI00OlAND = kstatIO.writes;
                this.OOOIilanD = kstatIO.nread;
                this.lli0OiIlAND = kstatIO.nwritten;
                this.li0iOILAND = (long)kstatIO.wcnt + (long)kstatIO.rcnt;
                this.O1il1llOLANd = kstatIO.rtime / 1000000L;
                this.Oill1LAnD = kstat.ks_snaptime / 1000000L;
                boolean bl = true;
                return bl;
            }
        }
        return false;
    }

    public static List l11lLANd() {
        Map map = OIl0IilanD.I1O1I1LaNd();
        Map map2 = ilIlllanD.I1O1I1LaNd();
        Map map3 = OIl0IilanD.I1O1I1LaNd(map.keySet());
        ArrayList<iO0OLaNd> arrayList = new ArrayList<iO0OLaNd>();
        for (Map.Entry entry : map3.entrySet()) {
            String string = (String)entry.getKey();
            OI1l11LaNd oI1l11LaNd = (OI1l11LaNd)entry.getValue();
            arrayList.add(iO0OLaNd.I1O1I1LaNd(string, (String)oI1l11LaNd.I1O1I1LaNd(), (String)oI1l11LaNd.OOOIilanD(), (String)oI1l11LaNd.lI00OlAND(), (String)oI1l11LaNd.lli0OiIlAND(), (Long)oI1l11LaNd.li0iOILAND(), map.getOrDefault(string, ""), map2.getOrDefault(string, 0)));
        }
        return arrayList;
    }

    private static iO0OLaNd I1O1I1LaNd(String string, String string2, String string3, String string4, String string5, long l2, String string6, int n2) {
        iO0OLaNd iO0OLaNd2 = new iO0OLaNd(string, string2.isEmpty() ? (string3 + " " + string4).trim() : string2, string5, l2);
        iO0OLaNd2.lIOILand = Collections.unmodifiableList(IIOlO0ilAnD.I1O1I1LaNd(string6, n2).stream().sorted(Comparator.comparing(i0Oi1LANd::OOOIilanD)).collect(Collectors.toList()));
        iO0OLaNd2.IO11O0LANd();
        return iO0OLaNd2;
    }
}

