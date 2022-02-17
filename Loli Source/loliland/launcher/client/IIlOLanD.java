/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OI1i0ilanD;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.l00OlIlAnd;
import loliland.launcher.client.l0Oil0IILAnd;
import loliland.launcher.client.liIIllIland;

public final class IIlOLanD
extends OI1i0ilanD {
    private final Supplier I1O1I1LaNd;
    private long OOOIilanD = 0L;
    private long lI00OlAND = 0L;
    private long lli0OiIlAND = 0L;
    private long li0iOILAND = 0L;
    private long O1il1llOLANd = 0L;
    private long Oill1LAnD = 0L;
    private long lIOILand = 0L;
    private List lil0liLand;

    private IIlOLanD(String string, String string2, String string3, long l2, Supplier supplier) {
        super(string, string2, string3, l2);
        this.I1O1I1LaNd = supplier;
    }

    @Override
    public long li0iOILAND() {
        return this.OOOIilanD;
    }

    @Override
    public long O1il1llOLANd() {
        return this.lI00OlAND;
    }

    @Override
    public long Oill1LAnD() {
        return this.lli0OiIlAND;
    }

    @Override
    public long lIOILand() {
        return this.li0iOILAND;
    }

    @Override
    public long lil0liLand() {
        return this.O1il1llOLANd;
    }

    @Override
    public long iilIi1laND() {
        return this.Oill1LAnD;
    }

    @Override
    public long l0illAND() {
        return this.lIOILand;
    }

    @Override
    public List lli011lLANd() {
        return this.lil0liLand;
    }

    @Override
    public boolean IO11O0LANd() {
        for (Perfstat.perfstat_disk_t perfstat_disk_t2 : (Perfstat.perfstat_disk_t[])this.I1O1I1LaNd.get()) {
            String string = Native.toString(perfstat_disk_t2.name);
            if (!string.equals(this.I1O1I1LaNd())) continue;
            long l2 = perfstat_disk_t2.rblks + perfstat_disk_t2.wblks;
            this.OOOIilanD = perfstat_disk_t2.xfers;
            if (l2 > 0L) {
                this.lli0OiIlAND = perfstat_disk_t2.xfers * perfstat_disk_t2.wblks / l2;
                this.OOOIilanD -= this.lli0OiIlAND;
            }
            this.lI00OlAND = perfstat_disk_t2.rblks * perfstat_disk_t2.bsize;
            this.li0iOILAND = perfstat_disk_t2.wblks * perfstat_disk_t2.bsize;
            this.O1il1llOLANd = perfstat_disk_t2.qdepth;
            this.Oill1LAnD = perfstat_disk_t2.time;
            return true;
        }
        return false;
    }

    public static List I1O1I1LaNd(Supplier supplier) {
        Map map = liIIllIland.I1O1I1LaNd();
        ArrayList<IIlOLanD> arrayList = new ArrayList<IIlOLanD>();
        for (Perfstat.perfstat_disk_t perfstat_disk_t2 : (Perfstat.perfstat_disk_t[])supplier.get()) {
            String string = Native.toString(perfstat_disk_t2.name);
            O1IiIiI1LAND o1IiIiI1LAND = l0Oil0IILAnd.I1O1I1LaNd(string);
            String string2 = o1IiIiI1LAND.I1O1I1LaNd() == null ? Native.toString(perfstat_disk_t2.description) : (String)o1IiIiI1LAND.I1O1I1LaNd();
            String string3 = o1IiIiI1LAND.OOOIilanD() == null ? "unknown" : (String)o1IiIiI1LAND.OOOIilanD();
            arrayList.add(IIlOLanD.I1O1I1LaNd(string, string2, string3, perfstat_disk_t2.size << 20, supplier, map));
        }
        return arrayList.stream().sorted(Comparator.comparingInt(iIlOLanD -> iIlOLanD.lli011lLANd().isEmpty() ? Integer.MAX_VALUE : ((i0Oi1LANd)iIlOLanD.lli011lLANd().get(0)).O1il1llOLANd())).collect(Collectors.toList());
    }

    private static IIlOLanD I1O1I1LaNd(String string, String string2, String string3, long l2, Supplier supplier, Map map) {
        IIlOLanD iIlOLanD = new IIlOLanD(string, string2.isEmpty() ? "unknown" : string2, string3, l2, supplier);
        iIlOLanD.lil0liLand = Collections.unmodifiableList(l00OlIlAnd.I1O1I1LaNd(string, map).stream().sorted(Comparator.comparing(i0Oi1LANd::Oill1LAnD).thenComparing(i0Oi1LANd::OOOIilanD)).collect(Collectors.toList()));
        iIlOLanD.IO11O0LANd();
        return iIlOLanD;
    }
}

