/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.WinBase;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.O1iOl0lAND;
import loliland.launcher.client.Olll0lANd;
import loliland.launcher.client.lIIi0Land;
import loliland.launcher.client.lOIiilANd;

public final class O1liliLANd {
    private static final String I1O1I1LaNd = "Thread";

    private O1liliLANd() {
    }

    public static Map I1O1I1LaNd(Collection collection) {
        IIOOOlIiLanD iIOOOlIiLanD = lOIiilANd.I1O1I1LaNd(I1O1I1LaNd, Olll0lANd.class);
        if (iIOOOlIiLanD == null) {
            return null;
        }
        List list = (List)iIOOOlIiLanD.I1O1I1LaNd();
        long l2 = (Long)iIOOOlIiLanD.OOOIilanD();
        long l3 = (Long)iIOOOlIiLanD.lI00OlAND();
        HashMap<Integer, O1iOl0lAND> hashMap = new HashMap<Integer, O1iOl0lAND>();
        for (Map map : list) {
            int n2 = (Integer)map.get(Olll0lANd.lil0liLand);
            if (collection != null && !collection.contains(n2) || n2 <= 0) continue;
            int n3 = (Integer)map.get(Olll0lANd.iilIi1laND);
            String string = (String)map.get(Olll0lANd.I1O1I1LaNd);
            long l4 = (l2 - (Long)map.get(Olll0lANd.lli0OiIlAND)) / 10000L;
            if (l4 < 1L) {
                l4 = 1L;
            }
            long l5 = (Long)map.get(Olll0lANd.OOOIilanD) / 10000L;
            long l6 = (Long)map.get(Olll0lANd.lI00OlAND) / 10000L;
            int n4 = (Integer)map.get(Olll0lANd.li0iOILAND);
            int n5 = (Integer)map.get(Olll0lANd.Oill1LAnD);
            int n6 = (Integer)map.get(Olll0lANd.lIOILand);
            Object v2 = map.get(Olll0lANd.O1il1llOLANd);
            long l7 = v2.getClass().equals(Long.class) ? (Long)v2 : Integer.toUnsignedLong((Integer)v2);
            int n7 = (Integer)map.get(Olll0lANd.lli011lLANd);
            hashMap.put(n3, new O1iOl0lAND(string, n3, n2, l3 - l4, l5, l6, n4, n5, n6, l7, n7));
        }
        return hashMap;
    }

    public static Map OOOIilanD(Collection collection) {
        HashMap<Integer, O1iOl0lAND> hashMap = new HashMap<Integer, O1iOl0lAND>();
        O1IiIiI1LAND o1IiIiI1LAND = lIIi0Land.I1O1I1LaNd();
        long l2 = System.currentTimeMillis();
        List list = (List)o1IiIiI1LAND.I1O1I1LaNd();
        Map map = (Map)o1IiIiI1LAND.OOOIilanD();
        List list2 = (List)map.get(Olll0lANd.iilIi1laND);
        List list3 = (List)map.get(Olll0lANd.lil0liLand);
        List list4 = (List)map.get(Olll0lANd.OOOIilanD);
        List list5 = (List)map.get(Olll0lANd.lI00OlAND);
        List list6 = (List)map.get(Olll0lANd.lli0OiIlAND);
        List list7 = (List)map.get(Olll0lANd.li0iOILAND);
        List list8 = (List)map.get(Olll0lANd.Oill1LAnD);
        List list9 = (List)map.get(Olll0lANd.lIOILand);
        List list10 = (List)map.get(Olll0lANd.O1il1llOLANd);
        List list11 = (List)map.get(Olll0lANd.lli011lLANd);
        int n2 = 0;
        for (int i2 = 0; i2 < list.size(); ++i2) {
            int n3 = ((Long)list3.get(i2)).intValue();
            if (collection != null && !collection.contains(n3)) continue;
            int n4 = ((Long)list2.get(i2)).intValue();
            String string = Integer.toString(n2++);
            long l3 = (Long)list6.get(i2);
            if ((l3 = WinBase.FILETIME.filetimeToDate((int)(l3 >> 32), (int)(l3 & 0xFFFFFFFFL)).getTime()) > l2) {
                l3 = l2 - 1L;
            }
            long l4 = (Long)list4.get(i2) / 10000L;
            long l5 = (Long)list5.get(i2) / 10000L;
            int n5 = ((Long)list7.get(i2)).intValue();
            int n6 = ((Long)list8.get(i2)).intValue();
            int n7 = ((Long)list9.get(i2)).intValue();
            long l6 = (Long)list10.get(i2);
            int n8 = ((Long)list11.get(i2)).intValue();
            hashMap.put(n4, new O1iOl0lAND(string, n4, n3, l3, l4, l5, n5, n6, n7, l6, n8));
        }
        return hashMap;
    }
}

