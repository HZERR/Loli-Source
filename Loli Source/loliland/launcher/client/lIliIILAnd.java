/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.COMException;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Kernel32;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import loliland.launcher.client.I0Oi1O0OlaND;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.O1l1OILAnD;
import loliland.launcher.client.OI1i0ilanD;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.i1OilAnD;
import loliland.launcher.client.iI0OilLANd;
import loliland.launcher.client.iIllllOlANd;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.l0l1lLaNd;
import loliland.launcher.client.lI0OIILand;
import loliland.launcher.client.lOOliLANd;
import loliland.launcher.client.lOi1OOI1lanD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lOililLAnd;
import loliland.launcher.client.li0i01LanD;
import loliland.launcher.client.lii1IlAND;
import loliland.launcher.client.llii0LAnD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class lIliIILAnd
extends OI1i0ilanD {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lIliIILAnd.class);
    private static final String OOOIilanD = "\\\\.\\PHYSICALDRIVE";
    private static final Pattern lI00OlAND = Pattern.compile(".*\\.DeviceID=\"(.*)\"");
    private static final int lli0OiIlAND = 100;
    private long li0iOILAND = 0L;
    private long O1il1llOLANd = 0L;
    private long Oill1LAnD = 0L;
    private long lIOILand = 0L;
    private long lil0liLand = 0L;
    private long iilIi1laND = 0L;
    private long lli011lLANd = 0L;
    private List l0illAND;

    private lIliIILAnd(String string, String string2, String string3, long l2) {
        super(string, string2, string3, l2);
    }

    @Override
    public long li0iOILAND() {
        return this.li0iOILAND;
    }

    @Override
    public long O1il1llOLANd() {
        return this.O1il1llOLANd;
    }

    @Override
    public long Oill1LAnD() {
        return this.Oill1LAnD;
    }

    @Override
    public long lIOILand() {
        return this.lIOILand;
    }

    @Override
    public long lil0liLand() {
        return this.lil0liLand;
    }

    @Override
    public long iilIi1laND() {
        return this.iilIi1laND;
    }

    @Override
    public long l0illAND() {
        return this.lli011lLANd;
    }

    @Override
    public List lli011lLANd() {
        return this.l0illAND;
    }

    @Override
    public boolean IO11O0LANd() {
        String string = null;
        List list = this.lli011lLANd();
        if (!list.isEmpty()) {
            string = Integer.toString(((i0Oi1LANd)list.get(0)).O1il1llOLANd());
        } else if (this.I1O1I1LaNd().startsWith(OOOIilanD)) {
            string = this.I1O1I1LaNd().substring(OOOIilanD.length(), this.I1O1I1LaNd().length());
        } else {
            I1O1I1LaNd.warn("Couldn't match index for {}", (Object)this.I1O1I1LaNd());
            return false;
        }
        l0l1lLaNd l0l1lLaNd2 = lIliIILAnd.I1O1I1LaNd(string);
        if (l0l1lLaNd.I1O1I1LaNd(l0l1lLaNd2).containsKey(string)) {
            this.li0iOILAND = l0l1lLaNd.I1O1I1LaNd(l0l1lLaNd2).getOrDefault(string, 0L);
            this.O1il1llOLANd = l0l1lLaNd.OOOIilanD(l0l1lLaNd2).getOrDefault(string, 0L);
            this.Oill1LAnD = l0l1lLaNd.lI00OlAND(l0l1lLaNd2).getOrDefault(string, 0L);
            this.lIOILand = l0l1lLaNd.lli0OiIlAND(l0l1lLaNd2).getOrDefault(string, 0L);
            this.lil0liLand = l0l1lLaNd.li0iOILAND(l0l1lLaNd2).getOrDefault(string, 0L);
            this.iilIi1laND = l0l1lLaNd.O1il1llOLANd(l0l1lLaNd2).getOrDefault(string, 0L);
            this.lli011lLANd = l0l1lLaNd.Oill1LAnD(l0l1lLaNd2);
            return true;
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List l11lLANd() {
        li0i01LanD li0i01LanD2 = li0i01LanD.I1O1I1LaNd();
        boolean bl = false;
        try {
            bl = li0i01LanD2.OOOIilanD();
            ArrayList<lIliIILAnd> arrayList = new ArrayList<lIliIILAnd>();
            l0l1lLaNd l0l1lLaNd2 = lIliIILAnd.I1O1I1LaNd(null);
            iI0OilLANd iI0OilLANd2 = lIliIILAnd.I1O1I1LaNd(li0i01LanD2);
            WbemcliUtil.WmiResult wmiResult = llii0LAnD.I1O1I1LaNd(li0i01LanD2);
            for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
                lIliIILAnd lIliIILAnd2 = new lIliIILAnd(ii00llanD.I1O1I1LaNd(wmiResult, lii1IlAND.lli0OiIlAND, i2), String.format("%s %s", ii00llanD.I1O1I1LaNd(wmiResult, lii1IlAND.lI00OlAND, i2), ii00llanD.I1O1I1LaNd(wmiResult, lii1IlAND.OOOIilanD, i2)).trim(), lOilLanD.lli0OiIlAND(ii00llanD.I1O1I1LaNd(wmiResult, lii1IlAND.li0iOILAND, i2)), ii00llanD.li0iOILAND(wmiResult, lii1IlAND.O1il1llOLANd, i2));
                String string = Integer.toString(ii00llanD.O1il1llOLANd(wmiResult, lii1IlAND.I1O1I1LaNd, i2));
                lIliIILAnd2.li0iOILAND = l0l1lLaNd.I1O1I1LaNd(l0l1lLaNd2).getOrDefault(string, 0L);
                lIliIILAnd2.O1il1llOLANd = l0l1lLaNd.OOOIilanD(l0l1lLaNd2).getOrDefault(string, 0L);
                lIliIILAnd2.Oill1LAnD = l0l1lLaNd.lI00OlAND(l0l1lLaNd2).getOrDefault(string, 0L);
                lIliIILAnd2.lIOILand = l0l1lLaNd.lli0OiIlAND(l0l1lLaNd2).getOrDefault(string, 0L);
                lIliIILAnd2.lil0liLand = l0l1lLaNd.li0iOILAND(l0l1lLaNd2).getOrDefault(string, 0L);
                lIliIILAnd2.iilIi1laND = l0l1lLaNd.O1il1llOLANd(l0l1lLaNd2).getOrDefault(string, 0L);
                lIliIILAnd2.lli011lLANd = l0l1lLaNd.Oill1LAnD(l0l1lLaNd2);
                ArrayList arrayList2 = new ArrayList();
                List list = (List)iI0OilLANd.I1O1I1LaNd(iI0OilLANd2).get(lIliIILAnd2.I1O1I1LaNd());
                if (list != null && !list.isEmpty()) {
                    for (String string2 : list) {
                        if (!iI0OilLANd.OOOIilanD(iI0OilLANd2).containsKey(string2)) continue;
                        arrayList2.addAll((Collection)iI0OilLANd.OOOIilanD(iI0OilLANd2).get(string2));
                    }
                }
                lIliIILAnd2.l0illAND = Collections.unmodifiableList(arrayList2.stream().sorted(Comparator.comparing(i0Oi1LANd::OOOIilanD)).collect(Collectors.toList()));
                arrayList.add(lIliIILAnd2);
            }
            ArrayList<lIliIILAnd> arrayList3 = arrayList;
            return arrayList3;
        }
        catch (COMException cOMException) {
            I1O1I1LaNd.warn("COM exception: {}", (Object)cOMException.getMessage());
            List list = Collections.emptyList();
            return list;
        }
        finally {
            if (bl) {
                li0i01LanD2.lI00OlAND();
            }
        }
    }

    private static l0l1lLaNd I1O1I1LaNd(String string) {
        l0l1lLaNd l0l1lLaNd2 = new l0l1lLaNd(null);
        O1IiIiI1LAND o1IiIiI1LAND = O1l1OILAnD.I1O1I1LaNd();
        List list = (List)o1IiIiI1LAND.I1O1I1LaNd();
        Map map = (Map)o1IiIiI1LAND.OOOIilanD();
        l0l1lLaNd.I1O1I1LaNd(l0l1lLaNd2, System.currentTimeMillis());
        List list2 = (List)map.get(i1OilAnD.OOOIilanD);
        List list3 = (List)map.get(i1OilAnD.lI00OlAND);
        List list4 = (List)map.get(i1OilAnD.lli0OiIlAND);
        List list5 = (List)map.get(i1OilAnD.li0iOILAND);
        List list6 = (List)map.get(i1OilAnD.O1il1llOLANd);
        List list7 = (List)map.get(i1OilAnD.Oill1LAnD);
        if (list.isEmpty() || list2 == null || list3 == null || list4 == null || list5 == null || list6 == null || list7 == null) {
            return l0l1lLaNd2;
        }
        for (int i2 = 0; i2 < list.size(); ++i2) {
            String string2 = lIliIILAnd.OOOIilanD((String)list.get(i2));
            if (string != null && !string.equals(string2)) continue;
            l0l1lLaNd.I1O1I1LaNd(l0l1lLaNd2).put(string2, list2.get(i2));
            l0l1lLaNd.OOOIilanD(l0l1lLaNd2).put(string2, list3.get(i2));
            l0l1lLaNd.lI00OlAND(l0l1lLaNd2).put(string2, list4.get(i2));
            l0l1lLaNd.lli0OiIlAND(l0l1lLaNd2).put(string2, list5.get(i2));
            l0l1lLaNd.li0iOILAND(l0l1lLaNd2).put(string2, list6.get(i2));
            l0l1lLaNd.O1il1llOLANd(l0l1lLaNd2).put(string2, (Long)list7.get(i2) / 10000L);
        }
        return l0l1lLaNd2;
    }

    private static iI0OilLANd I1O1I1LaNd(li0i01LanD li0i01LanD2) {
        ArrayList<O1IiIiI1LAND> arrayList;
        Matcher matcher;
        Matcher matcher2;
        iI0OilLANd iI0OilLANd2 = new iI0OilLANd(null);
        WbemcliUtil.WmiResult wmiResult = iIllllOlANd.I1O1I1LaNd(li0i01LanD2);
        for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
            matcher2 = lI00OlAND.matcher(ii00llanD.lli0OiIlAND(wmiResult, lOililLAnd.I1O1I1LaNd, i2));
            matcher = lI00OlAND.matcher(ii00llanD.lli0OiIlAND(wmiResult, lOililLAnd.OOOIilanD, i2));
            if (!matcher2.matches() || !matcher.matches()) continue;
            iI0OilLANd.I1O1I1LaNd(iI0OilLANd2).computeIfAbsent(matcher2.group(1).replace("\\\\", "\\"), string -> new ArrayList()).add(matcher.group(1));
        }
        WbemcliUtil.WmiResult wmiResult2 = I0Oi1O0OlaND.I1O1I1LaNd(li0i01LanD2);
        for (int i3 = 0; i3 < wmiResult2.getResultCount(); ++i3) {
            matcher2 = lI00OlAND.matcher(ii00llanD.lli0OiIlAND(wmiResult2, lOOliLANd.I1O1I1LaNd, i3));
            matcher = lI00OlAND.matcher(ii00llanD.lli0OiIlAND(wmiResult2, lOOliLANd.OOOIilanD, i3));
            long l2 = ii00llanD.li0iOILAND(wmiResult2, lOOliLANd.lI00OlAND, i3) - ii00llanD.li0iOILAND(wmiResult2, lOOliLANd.lli0OiIlAND, i3) + 1L;
            if (!matcher2.matches() || !matcher.matches()) continue;
            if (iI0OilLANd.lI00OlAND(iI0OilLANd2).containsKey(matcher2.group(1))) {
                ((List)iI0OilLANd.lI00OlAND(iI0OilLANd2).get(matcher2.group(1))).add(new O1IiIiI1LAND(matcher.group(1) + "\\", l2));
                continue;
            }
            arrayList = new ArrayList<O1IiIiI1LAND>();
            arrayList.add(new O1IiIiI1LAND(matcher.group(1) + "\\", l2));
            iI0OilLANd.lI00OlAND(iI0OilLANd2).put(matcher2.group(1), arrayList);
        }
        WbemcliUtil.WmiResult wmiResult3 = lI0OIILand.I1O1I1LaNd(li0i01LanD2);
        for (int i4 = 0; i4 < wmiResult3.getResultCount(); ++i4) {
            String string2 = ii00llanD.I1O1I1LaNd(wmiResult3, lOi1OOI1lanD.lI00OlAND, i4);
            arrayList = (ArrayList<O1IiIiI1LAND>)iI0OilLANd.lI00OlAND(iI0OilLANd2).get(string2);
            if (arrayList == null) continue;
            for (int i5 = 0; i5 < arrayList.size(); ++i5) {
                O1IiIiI1LAND o1IiIiI1LAND = (O1IiIiI1LAND)arrayList.get(i5);
                if (o1IiIiI1LAND == null || ((String)o1IiIiI1LAND.I1O1I1LaNd()).isEmpty()) continue;
                char[] arrc = new char[100];
                Kernel32.INSTANCE.GetVolumeNameForVolumeMountPoint((String)o1IiIiI1LAND.I1O1I1LaNd(), arrc, 100);
                String string3 = lOilLanD.I1O1I1LaNd(new String(arrc).trim(), "");
                i0Oi1LANd i0Oi1LANd2 = new i0Oi1LANd(ii00llanD.I1O1I1LaNd(wmiResult3, lOi1OOI1lanD.li0iOILAND, i4), ii00llanD.I1O1I1LaNd(wmiResult3, lOi1OOI1lanD.Oill1LAnD, i4), ii00llanD.I1O1I1LaNd(wmiResult3, lOi1OOI1lanD.OOOIilanD, i4), string3, (Long)o1IiIiI1LAND.OOOIilanD(), ii00llanD.O1il1llOLANd(wmiResult3, lOi1OOI1lanD.lli0OiIlAND, i4), ii00llanD.O1il1llOLANd(wmiResult3, lOi1OOI1lanD.I1O1I1LaNd, i4), (String)o1IiIiI1LAND.I1O1I1LaNd());
                if (iI0OilLANd.OOOIilanD(iI0OilLANd2).containsKey(string2)) {
                    ((List)iI0OilLANd.OOOIilanD(iI0OilLANd2).get(string2)).add(i0Oi1LANd2);
                    continue;
                }
                ArrayList<i0Oi1LANd> arrayList2 = new ArrayList<i0Oi1LANd>();
                arrayList2.add(i0Oi1LANd2);
                iI0OilLANd.OOOIilanD(iI0OilLANd2).put(string2, arrayList2);
            }
        }
        return iI0OilLANd2;
    }

    private static String OOOIilanD(String string) {
        if (string.isEmpty()) {
            return string;
        }
        return string.split("\\s")[0];
    }
}

