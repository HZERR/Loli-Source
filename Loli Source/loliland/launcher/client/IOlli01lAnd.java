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
import loliland.launcher.client.l10lO11lanD;
import loliland.launcher.client.l11OOlIIlaND;
import loliland.launcher.client.lOIiilANd;
import loliland.launcher.client.ll10II0LaND;
import loliland.launcher.client.ll10lanD;

public final class IOlli01lAnd {
    private static final String OOOIilanD = "Process";
    public static final String I1O1I1LaNd = "oshi.os.windows.hkeyperfdata";
    private static final boolean lI00OlAND = l10lO11lanD.I1O1I1LaNd("oshi.os.windows.hkeyperfdata", true);

    private IOlli01lAnd() {
    }

    public static Map I1O1I1LaNd(Collection collection) {
        IIOOOlIiLanD iIOOOlIiLanD = null;
        if (lI00OlAND) {
            iIOOOlIiLanD = lOIiilANd.I1O1I1LaNd(OOOIilanD, ll10II0LaND.class);
        }
        if (iIOOOlIiLanD == null) {
            return null;
        }
        List list = (List)iIOOOlIiLanD.I1O1I1LaNd();
        long l2 = (Long)iIOOOlIiLanD.lI00OlAND();
        HashMap<Integer, l11OOlIIlaND> hashMap = new HashMap<Integer, l11OOlIIlaND>();
        for (Map map : list) {
            long l3;
            int n2 = (Integer)map.get(ll10II0LaND.lli0OiIlAND);
            String string = (String)map.get(ll10II0LaND.I1O1I1LaNd);
            if (collection != null && !collection.contains(n2) || "_Total".equals(string)) continue;
            long l4 = (Long)map.get(ll10II0LaND.lI00OlAND);
            if (l4 > l2) {
                l4 = WinBase.FILETIME.filetimeToDate((int)(l4 >> 32), (int)(l4 & 0xFFFFFFFFL)).getTime();
            }
            if ((l3 = l2 - l4) < 1L) {
                l3 = 1L;
            }
            hashMap.put(n2, new l11OOlIIlaND(string, (Integer)map.get(ll10II0LaND.li0iOILAND), (Integer)map.get(ll10II0LaND.OOOIilanD), (Long)map.get(ll10II0LaND.lIOILand), l4, l3, (Long)map.get(ll10II0LaND.O1il1llOLANd), (Long)map.get(ll10II0LaND.Oill1LAnD), (Integer)map.get(ll10II0LaND.lil0liLand)));
        }
        return hashMap;
    }

    public static Map OOOIilanD(Collection collection) {
        HashMap<Integer, l11OOlIIlaND> hashMap = new HashMap<Integer, l11OOlIIlaND>();
        O1IiIiI1LAND o1IiIiI1LAND = ll10lanD.I1O1I1LaNd();
        long l2 = System.currentTimeMillis();
        List list = (List)o1IiIiI1LAND.I1O1I1LaNd();
        Map map = (Map)o1IiIiI1LAND.OOOIilanD();
        List list2 = (List)map.get(ll10II0LaND.lli0OiIlAND);
        List list3 = (List)map.get(ll10II0LaND.li0iOILAND);
        List list4 = (List)map.get(ll10II0LaND.OOOIilanD);
        List list5 = (List)map.get(ll10II0LaND.O1il1llOLANd);
        List list6 = (List)map.get(ll10II0LaND.Oill1LAnD);
        List list7 = (List)map.get(ll10II0LaND.lIOILand);
        List list8 = (List)map.get(ll10II0LaND.lI00OlAND);
        List list9 = (List)map.get(ll10II0LaND.lil0liLand);
        for (int i2 = 0; i2 < list.size(); ++i2) {
            long l3;
            int n2 = ((Long)list2.get(i2)).intValue();
            if (collection != null && !collection.contains(n2)) continue;
            long l4 = (Long)list8.get(i2);
            if (l4 > l2) {
                l4 = WinBase.FILETIME.filetimeToDate((int)(l4 >> 32), (int)(l4 & 0xFFFFFFFFL)).getTime();
            }
            if ((l3 = l2 - l4) < 1L) {
                l3 = 1L;
            }
            hashMap.put(n2, new l11OOlIIlaND((String)list.get(i2), ((Long)list3.get(i2)).intValue(), ((Long)list4.get(i2)).intValue(), (Long)list7.get(i2), l4, l3, (Long)list5.get(i2), (Long)list6.get(i2), ((Long)list9.get(i2)).intValue()));
        }
        return hashMap;
    }
}

