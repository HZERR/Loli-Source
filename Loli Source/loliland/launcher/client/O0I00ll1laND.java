/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OI1i0ilanD;
import loliland.launcher.client.i0IIlOLANd;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.li0ii01laND;
import loliland.launcher.client.lli10iliLaND;

public final class O0I00ll1laND
extends OI1i0ilanD {
    private long I1O1I1LaNd = 0L;
    private long OOOIilanD = 0L;
    private long lI00OlAND = 0L;
    private long lli0OiIlAND = 0L;
    private long li0iOILAND = 0L;
    private long O1il1llOLANd = 0L;
    private long Oill1LAnD = 0L;
    private List lIOILand;

    private O0I00ll1laND(String string, String string2, String string3, long l2) {
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
        List list = Iill1lanD.I1O1I1LaNd("iostat -Ix " + this.I1O1I1LaNd());
        long l2 = System.currentTimeMillis();
        boolean bl = false;
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string);
            if (arrstring.length < 7 || !arrstring[0].equals(this.I1O1I1LaNd())) continue;
            bl = true;
            this.I1O1I1LaNd = (long)lOilLanD.OOOIilanD(arrstring[1], 0.0);
            this.lI00OlAND = (long)lOilLanD.OOOIilanD(arrstring[2], 0.0);
            this.OOOIilanD = (long)(lOilLanD.OOOIilanD(arrstring[3], 0.0) * 1024.0);
            this.lli0OiIlAND = (long)(lOilLanD.OOOIilanD(arrstring[4], 0.0) * 1024.0);
            this.li0iOILAND = lOilLanD.OOOIilanD(arrstring[5], 0L);
            this.O1il1llOLANd = (long)(lOilLanD.OOOIilanD(arrstring[6], 0.0) * 1000.0);
            this.Oill1LAnD = l2;
        }
        return bl;
    }

    public static List l11lLANd() {
        ArrayList<O0I00ll1laND> arrayList = new ArrayList<O0I00ll1laND>();
        Map map = li0ii01laND.I1O1I1LaNd();
        Map map2 = i0IIlOLANd.I1O1I1LaNd();
        List<String> list = Arrays.asList(lOilLanD.OOOIilanD.split(lli10iliLaND.I1O1I1LaNd("kern.disks", "")));
        List list2 = Iill1lanD.I1O1I1LaNd("iostat -Ix");
        long l2 = System.currentTimeMillis();
        for (String string : list2) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string);
            if (arrstring.length <= 6 || !list.contains(arrstring[0])) continue;
            IIOOOlIiLanD iIOOOlIiLanD = (IIOOOlIiLanD)map2.get(arrstring[0]);
            O0I00ll1laND o0I00ll1laND = iIOOOlIiLanD == null ? new O0I00ll1laND(arrstring[0], "unknown", "unknown", 0L) : new O0I00ll1laND(arrstring[0], (String)iIOOOlIiLanD.I1O1I1LaNd(), (String)iIOOOlIiLanD.OOOIilanD(), (Long)iIOOOlIiLanD.lI00OlAND());
            o0I00ll1laND.I1O1I1LaNd = (long)lOilLanD.OOOIilanD(arrstring[1], 0.0);
            o0I00ll1laND.lI00OlAND = (long)lOilLanD.OOOIilanD(arrstring[2], 0.0);
            o0I00ll1laND.OOOIilanD = (long)(lOilLanD.OOOIilanD(arrstring[3], 0.0) * 1024.0);
            o0I00ll1laND.lli0OiIlAND = (long)(lOilLanD.OOOIilanD(arrstring[4], 0.0) * 1024.0);
            o0I00ll1laND.li0iOILAND = lOilLanD.OOOIilanD(arrstring[5], 0L);
            o0I00ll1laND.O1il1llOLANd = (long)(lOilLanD.OOOIilanD(arrstring[6], 0.0) * 1000.0);
            o0I00ll1laND.lIOILand = Collections.unmodifiableList(map.getOrDefault(arrstring[0], Collections.emptyList()).stream().sorted(Comparator.comparing(i0Oi1LANd::OOOIilanD)).collect(Collectors.toList()));
            o0I00ll1laND.Oill1LAnD = l2;
            arrayList.add(o0I00ll1laND);
        }
        return arrayList;
    }
}

