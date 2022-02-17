/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Map;
import loliland.launcher.client.IOl1l0LANd;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.iIiO00OLaNd;
import loliland.launcher.client.l11IlanD;
import loliland.launcher.client.l1i0ILaND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;

public class i1i1l11ilanD
extends l1i0ILaND {
    private static final int[] I1O1I1LaNd = new int[IOl1l0LANd.values().length];
    private final int OOOIilanD;
    private String lI00OlAND;
    private OOOOO10iLAND lli0OiIlAND = OOOOO10iLAND.lIOILand;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;
    private long lil0liLand;
    private long iilIi1laND;
    private long lli011lLANd;
    private long l0illAND;
    private int IO11O0LANd;

    public i1i1l11ilanD(int n2, int n3) {
        super(n2);
        this.OOOIilanD = n3;
        this.lO110l1LANd();
    }

    @Override
    public int lI00OlAND() {
        return this.OOOIilanD;
    }

    @Override
    public String lli0OiIlAND() {
        return this.lI00OlAND;
    }

    @Override
    public OOOOO10iLAND li0iOILAND() {
        return this.lli0OiIlAND;
    }

    @Override
    public long IO11O0LANd() {
        return this.lli011lLANd;
    }

    @Override
    public long O1il1llOLANd() {
        return this.Oill1LAnD;
    }

    @Override
    public long Oill1LAnD() {
        return this.lIOILand;
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
        return this.lil0liLand;
    }

    @Override
    public long lli011lLANd() {
        return this.iilIi1laND;
    }

    @Override
    public long l0illAND() {
        return this.l0illAND;
    }

    @Override
    public int l11lLANd() {
        return this.IO11O0LANd;
    }

    @Override
    public boolean lO110l1LANd() {
        this.lI00OlAND = liOIOOlLAnD.li0iOILAND(String.format(iI11I1lllaNd.IOI1LaNd, this.I1O1I1LaNd(), this.OOOIilanD));
        Map map = liOIOOlLAnD.I1O1I1LaNd(String.format(iI11I1lllaNd.lI00ilAND, this.I1O1I1LaNd(), this.OOOIilanD), ":");
        String string = liOIOOlLAnD.li0iOILAND(String.format(iI11I1lllaNd.l0l00lAND, this.I1O1I1LaNd(), this.OOOIilanD));
        if (string.isEmpty()) {
            this.lli0OiIlAND = OOOOO10iLAND.lIOILand;
            return false;
        }
        long l2 = System.currentTimeMillis();
        long[] arrl = lOilLanD.I1O1I1LaNd(string, I1O1I1LaNd, iIiO00OLaNd.I1O1I1LaNd, ' ');
        this.lli011lLANd = (l11IlanD.lI00OlAND * l11IlanD.lI00ilAND() + arrl[IOl1l0LANd.lIOILand.ordinal()]) * 1000L / l11IlanD.lI00ilAND();
        if (this.lli011lLANd >= l2) {
            this.lli011lLANd = l2 - 1L;
        }
        this.li0iOILAND = arrl[IOl1l0LANd.OOOIilanD.ordinal()];
        this.O1il1llOLANd = arrl[IOl1l0LANd.lI00OlAND.ordinal()];
        this.Oill1LAnD = arrl[IOl1l0LANd.lli011lLANd.ordinal()];
        long l3 = lOilLanD.OOOIilanD((String)map.get("voluntary_ctxt_switches"), 0L);
        long l4 = lOilLanD.OOOIilanD((String)map.get("nonvoluntary_ctxt_switches"), 0L);
        this.lIOILand = l3 + l4;
        this.lli0OiIlAND = iIiO00OLaNd.I1O1I1LaNd(map.getOrDefault("State", "U").charAt(0));
        this.lil0liLand = arrl[IOl1l0LANd.li0iOILAND.ordinal()] * 1000L / l11IlanD.lI00ilAND();
        this.iilIi1laND = arrl[IOl1l0LANd.lli0OiIlAND.ordinal()] * 1000L / l11IlanD.lI00ilAND();
        this.l0illAND = l2 - this.lli011lLANd;
        this.IO11O0LANd = (int)arrl[IOl1l0LANd.O1il1llOLANd.ordinal()];
        return true;
    }

    static {
        for (IOl1l0LANd iOl1l0LANd : IOl1l0LANd.values()) {
            i1i1l11ilanD.I1O1I1LaNd[iOl1l0LANd.ordinal()] = iOl1l0LANd.I1O1I1LaNd() - 1;
        }
    }
}

