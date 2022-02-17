/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import loliland.launcher.client.II0101Iland;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.l1i0ILaND;
import loliland.launcher.client.lOilLanD;

public class O0IllLAnD
extends l1i0ILaND {
    private int I1O1I1LaNd;
    private OOOOO10iLAND OOOIilanD = OOOOO10iLAND.lIOILand;
    private long lI00OlAND;
    private long lli0OiIlAND;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;
    private int lil0liLand;

    public O0IllLAnD(int n2, String[] arrstring) {
        super(n2);
        this.I1O1I1LaNd(arrstring);
    }

    @Override
    public int lI00OlAND() {
        return this.I1O1I1LaNd;
    }

    @Override
    public OOOOO10iLAND li0iOILAND() {
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
    public long iilIi1laND() {
        return this.li0iOILAND;
    }

    @Override
    public long lli011lLANd() {
        return this.O1il1llOLANd;
    }

    @Override
    public long l0illAND() {
        return this.lIOILand;
    }

    @Override
    public long IO11O0LANd() {
        return this.Oill1LAnD;
    }

    @Override
    public int l11lLANd() {
        return this.lil0liLand;
    }

    @Override
    public boolean lO110l1LANd() {
        Optional<String[]> optional;
        List list;
        List list2 = Iill1lanD.I1O1I1LaNd("ps -o lwp,s,etime,stime,time,addr,pri -p " + this.I1O1I1LaNd());
        Map map = II0101Iland.I1O1I1LaNd(list2, 0, 7, list = Iill1lanD.I1O1I1LaNd("prstat -L -v -p " + this.I1O1I1LaNd() + " 1 1"), true);
        if (map.keySet().size() > 1 && (optional = map.entrySet().stream().filter(entry -> ((Integer)entry.getKey()).intValue() == this.lI00OlAND()).map(Map.Entry::getValue).findFirst()).isPresent()) {
            return this.I1O1I1LaNd(optional.get());
        }
        this.OOOIilanD = OOOOO10iLAND.lIOILand;
        return false;
    }

    private boolean I1O1I1LaNd(String[] arrstring) {
        this.I1O1I1LaNd = lOilLanD.lli0OiIlAND(arrstring[0], 0);
        this.OOOIilanD = II0101Iland.I1O1I1LaNd(arrstring[1].charAt(0));
        long l2 = lOilLanD.lli0OiIlAND(arrstring[2], 0L);
        this.lIOILand = l2 < 1L ? 1L : l2;
        long l3 = System.currentTimeMillis();
        this.Oill1LAnD = l3 - this.lIOILand;
        this.li0iOILAND = lOilLanD.lli0OiIlAND(arrstring[3], 0L);
        this.O1il1llOLANd = lOilLanD.lli0OiIlAND(arrstring[4], 0L) - this.li0iOILAND;
        this.lI00OlAND = lOilLanD.li0iOILAND(arrstring[5], 0L);
        this.lil0liLand = lOilLanD.lli0OiIlAND(arrstring[6], 0);
        long l4 = lOilLanD.OOOIilanD(arrstring[7], 0L);
        long l5 = lOilLanD.OOOIilanD(arrstring[8], 0L);
        this.lli0OiIlAND = l5 + l4;
        return true;
    }
}

