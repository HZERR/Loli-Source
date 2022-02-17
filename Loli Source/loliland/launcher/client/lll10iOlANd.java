/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O11i1I1lAnd;
import loliland.launcher.client.OI1i0ilanD;
import loliland.launcher.client.l0lOiOlAnd;
import loliland.launcher.client.lIl0iilland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;

public final class lll10iOlANd
extends OI1i0ilanD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(lll10iOlANd::lO110l1LANd, lii1IO0LaNd.I1O1I1LaNd());
    private long OOOIilanD = 0L;
    private long lI00OlAND = 0L;
    private long lli0OiIlAND = 0L;
    private long li0iOILAND = 0L;
    private long O1il1llOLANd = 0L;
    private long Oill1LAnD = 0L;
    private long lIOILand = 0L;
    private List lil0liLand;

    private lll10iOlANd(String string, String string2, String string3, long l2) {
        super(string, string2, string3, l2);
    }

    public static List l11lLANd() {
        String[] arrstring;
        ArrayList<lll10iOlANd> arrayList = new ArrayList<lll10iOlANd>();
        List list = null;
        for (String string : arrstring = lIl0iilland.I1O1I1LaNd("hw.disknames", "").split(",")) {
            String string2 = string.split(":")[0];
            O11i1I1lAnd o11i1I1lAnd = l0lOiOlAnd.I1O1I1LaNd(string2);
            String string3 = (String)o11i1I1lAnd.I1O1I1LaNd();
            long l2 = (Long)o11i1I1lAnd.lI00OlAND();
            if (l2 <= 1L) {
                if (list == null) {
                    list = Iill1lanD.I1O1I1LaNd("dmesg");
                }
                Pattern pattern = Pattern.compile(string2 + " at .*<(.+)>.*");
                Pattern pattern2 = Pattern.compile(string2 + ":.* (\\d+)MB, (?:(\\d+) bytes\\/sector, )?(?:(\\d+) sectors).*");
                for (String string4 : list) {
                    Matcher matcher = pattern.matcher(string4);
                    if (matcher.matches()) {
                        string3 = matcher.group(1);
                    }
                    if (!(matcher = pattern2.matcher(string4)).matches()) continue;
                    long l3 = lOilLanD.OOOIilanD(matcher.group(3), 0L);
                    long l4 = lOilLanD.OOOIilanD(matcher.group(2), 0L);
                    if (l4 == 0L && l3 > 0L) {
                        l2 = lOilLanD.OOOIilanD(matcher.group(1), 0L) << 20;
                        l4 = l2 / l3;
                        l4 = Long.highestOneBit(l4 + l4 >> 1);
                    }
                    l2 = l4 * l3;
                    break;
                }
            }
            lll10iOlANd lll10iOlANd2 = new lll10iOlANd(string2, string3, (String)o11i1I1lAnd.OOOIilanD(), l2);
            lll10iOlANd2.lil0liLand = (List)o11i1I1lAnd.lli0OiIlAND();
            lll10iOlANd2.IO11O0LANd();
            arrayList.add(lll10iOlANd2);
        }
        return arrayList;
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
        long l2 = System.currentTimeMillis();
        boolean bl = false;
        for (String string : (List)this.I1O1I1LaNd.get()) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string);
            if (arrstring.length >= 7 || !arrstring[0].equals(this.I1O1I1LaNd())) continue;
            bl = true;
            this.lI00OlAND = lOilLanD.iilIi1laND(arrstring[1]);
            this.li0iOILAND = lOilLanD.iilIi1laND(arrstring[2]);
            this.OOOIilanD = (long)lOilLanD.OOOIilanD(arrstring[3], 0.0);
            this.lli0OiIlAND = (long)lOilLanD.OOOIilanD(arrstring[4], 0.0);
            this.Oill1LAnD = (long)(lOilLanD.OOOIilanD(arrstring[5], 0.0) * 1000.0);
            this.lIOILand = l2;
        }
        return bl;
    }

    private static List lO110l1LANd() {
        return Iill1lanD.I1O1I1LaNd("systat -ab iostat");
    }
}

