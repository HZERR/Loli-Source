/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.linux.Udev;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import loliland.launcher.client.OI1i0ilanD;
import loliland.launcher.client.i0Oi1LANd;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.il10laNd;
import loliland.launcher.client.lIl0ILaND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;

public final class ii111Land
extends OI1i0ilanD {
    private static final String I1O1I1LaNd = "block";
    private static final String OOOIilanD = "disk";
    private static final String lI00OlAND = "partition";
    private static final String lli0OiIlAND = "stat";
    private static final String li0iOILAND = "size";
    private static final String O1il1llOLANd = "MINOR";
    private static final String Oill1LAnD = "MAJOR";
    private static final String lIOILand = "ID_FS_TYPE";
    private static final String lil0liLand = "ID_FS_UUID";
    private static final String iilIi1laND = "ID_MODEL";
    private static final String lli011lLANd = "ID_SERIAL_SHORT";
    private static final String l0illAND = "DM_UUID";
    private static final String IO11O0LANd = "DM_VG_NAME";
    private static final String l11lLANd = "DM_LV_NAME";
    private static final String lO110l1LANd = "Logical Volume Group";
    private static final String l0iIlIO1laNd = "/dev/";
    private static final String iOIl0LAnD = "/dev/mapper/";
    private static final int iIiO00OLaNd = 512;
    private static final int[] ii1li00Land = new int[lIl0ILaND.values().length];
    private static final int IOI1LaNd;
    private long lI00ilAND = 0L;
    private long l0l00lAND = 0L;
    private long iOl10IlLAnd = 0L;
    private long lIiIii1LAnD = 0L;
    private long II1Iland = 0L;
    private long l0IO0LAnd = 0L;
    private long liOIOOLANd = 0L;
    private List II1i1l0laND = new ArrayList();

    private ii111Land(String string, String string2, String string3, long l2) {
        super(string, string2, string3, l2);
    }

    @Override
    public long li0iOILAND() {
        return this.lI00ilAND;
    }

    @Override
    public long O1il1llOLANd() {
        return this.l0l00lAND;
    }

    @Override
    public long Oill1LAnD() {
        return this.iOl10IlLAnd;
    }

    @Override
    public long lIOILand() {
        return this.lIiIii1LAnD;
    }

    @Override
    public long lil0liLand() {
        return this.II1Iland;
    }

    @Override
    public long iilIi1laND() {
        return this.l0IO0LAnd;
    }

    @Override
    public long l0illAND() {
        return this.liOIOOLANd;
    }

    @Override
    public List lli011lLANd() {
        return this.II1i1l0laND;
    }

    public static List l11lLANd() {
        return ii111Land.I1O1I1LaNd(null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    private static List I1O1I1LaNd(ii111Land ii111Land2) {
        OI1i0ilanD oI1i0ilanD = null;
        ArrayList<OI1i0ilanD> arrayList = new ArrayList<OI1i0ilanD>();
        Map map = ii111Land.lO110l1LANd();
        Udev.UdevContext udevContext = Udev.INSTANCE.udev_new();
        try {
            Udev.UdevEnumerate udevEnumerate = udevContext.enumerateNew();
            try {
                void var6_7;
                udevEnumerate.addMatchSubsystem(I1O1I1LaNd);
                udevEnumerate.scanDevices();
                Udev.UdevListEntry object = udevEnumerate.getListEntry();
                while (var6_7 != null) {
                    block18: {
                        String string = var6_7.getName();
                        Udev.UdevDevice udevDevice = udevContext.deviceNewFromSyspath(string);
                        if (udevDevice != null) {
                            try {
                                String string2;
                                Object object2;
                                String string3 = udevDevice.getDevnode();
                                if (string3 == null || string3.startsWith("/dev/loop") || string3.startsWith("/dev/ram")) break block18;
                                if (OOOIilanD.equals(udevDevice.getDevtype())) {
                                    object2 = udevDevice.getPropertyValue(iilIi1laND);
                                    string2 = udevDevice.getPropertyValue(lli011lLANd);
                                    long l2 = lOilLanD.OOOIilanD(udevDevice.getSysattrValue(li0iOILAND), 0L) * 512L;
                                    if (string3.startsWith("/dev/dm")) {
                                        object2 = lO110l1LANd;
                                        string2 = udevDevice.getPropertyValue(l0illAND);
                                        oI1i0ilanD = new ii111Land(string3, (String)object2, string2 == null ? "unknown" : string2, l2);
                                        String string4 = udevDevice.getPropertyValue(IO11O0LANd);
                                        String string5 = udevDevice.getPropertyValue(l11lLANd);
                                        ((ii111Land)oI1i0ilanD).II1i1l0laND.add(new i0Oi1LANd(ii111Land.I1O1I1LaNd(string4, string5), udevDevice.getSysname(), udevDevice.getPropertyValue(lIOILand) == null ? lI00OlAND : udevDevice.getPropertyValue(lIOILand), udevDevice.getPropertyValue(lil0liLand) == null ? "" : udevDevice.getPropertyValue(lil0liLand), lOilLanD.OOOIilanD(udevDevice.getSysattrValue(li0iOILAND), 0L) * 512L, lOilLanD.lli0OiIlAND(udevDevice.getPropertyValue(Oill1LAnD), 0), lOilLanD.lli0OiIlAND(udevDevice.getPropertyValue(O1il1llOLANd), 0), ii111Land.OOOIilanD(string4, string5)));
                                    } else {
                                        oI1i0ilanD = new ii111Land(string3, (String)(object2 == null ? "unknown" : object2), string2 == null ? "unknown" : string2, l2);
                                    }
                                    if (ii111Land2 == null) {
                                        ii111Land.I1O1I1LaNd((ii111Land)oI1i0ilanD, udevDevice.getSysattrValue(lli0OiIlAND));
                                        arrayList.add(oI1i0ilanD);
                                        break block18;
                                    }
                                    if (!oI1i0ilanD.I1O1I1LaNd().equals(ii111Land2.I1O1I1LaNd()) || !oI1i0ilanD.OOOIilanD().equals(ii111Land2.OOOIilanD()) || !oI1i0ilanD.lI00OlAND().equals(ii111Land2.lI00OlAND()) || oI1i0ilanD.lli0OiIlAND() != ii111Land2.lli0OiIlAND()) break block18;
                                    ii111Land.I1O1I1LaNd(ii111Land2, udevDevice.getSysattrValue(lli0OiIlAND));
                                    arrayList.add(ii111Land2);
                                    break;
                                }
                                if (ii111Land2 == null && oI1i0ilanD != null && lI00OlAND.equals(udevDevice.getDevtype()) && (object2 = udevDevice.getParentWithSubsystemDevtype(I1O1I1LaNd, OOOIilanD)) != null && oI1i0ilanD.I1O1I1LaNd().equals(((Udev.UdevDevice)object2).getDevnode())) {
                                    string2 = udevDevice.getDevnode();
                                    ((ii111Land)oI1i0ilanD).II1i1l0laND.add(new i0Oi1LANd(string2, udevDevice.getSysname(), udevDevice.getPropertyValue(lIOILand) == null ? lI00OlAND : udevDevice.getPropertyValue(lIOILand), udevDevice.getPropertyValue(lil0liLand) == null ? "" : udevDevice.getPropertyValue(lil0liLand), lOilLanD.OOOIilanD(udevDevice.getSysattrValue(li0iOILAND), 0L) * 512L, lOilLanD.lli0OiIlAND(udevDevice.getPropertyValue(Oill1LAnD), 0), lOilLanD.lli0OiIlAND(udevDevice.getPropertyValue(O1il1llOLANd), 0), map.getOrDefault(string2, ii111Land.I1O1I1LaNd(udevDevice.getSysname()))));
                                }
                            }
                            finally {
                                udevDevice.unref();
                            }
                        }
                    }
                    Udev.UdevListEntry udevListEntry = var6_7.getNext();
                }
            }
            finally {
                udevEnumerate.unref();
            }
        }
        finally {
            udevContext.unref();
        }
        for (il10laNd il10laNd2 : arrayList) {
            ((ii111Land)il10laNd2).II1i1l0laND = Collections.unmodifiableList(il10laNd2.lli011lLANd().stream().sorted(Comparator.comparing(i0Oi1LANd::OOOIilanD)).collect(Collectors.toList()));
        }
        return arrayList;
    }

    @Override
    public boolean IO11O0LANd() {
        return !ii111Land.I1O1I1LaNd(this).isEmpty();
    }

    private static Map lO110l1LANd() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.O1il1llOLANd);
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string);
            if (arrstring.length < 2 || !arrstring[0].startsWith(l0iIlIO1laNd)) continue;
            hashMap.put(arrstring[0], arrstring[1]);
        }
        return hashMap;
    }

    private static void I1O1I1LaNd(ii111Land ii111Land2, String string) {
        long[] arrl = lOilLanD.I1O1I1LaNd(string, ii1li00Land, IOI1LaNd, ' ');
        ii111Land2.liOIOOLANd = System.currentTimeMillis();
        ii111Land2.lI00ilAND = arrl[lIl0ILaND.I1O1I1LaNd.ordinal()];
        ii111Land2.l0l00lAND = arrl[lIl0ILaND.OOOIilanD.ordinal()] * 512L;
        ii111Land2.iOl10IlLAnd = arrl[lIl0ILaND.lI00OlAND.ordinal()];
        ii111Land2.lIiIii1LAnD = arrl[lIl0ILaND.lli0OiIlAND.ordinal()] * 512L;
        ii111Land2.II1Iland = arrl[lIl0ILaND.li0iOILAND.ordinal()];
        ii111Land2.l0IO0LAnd = arrl[lIl0ILaND.O1il1llOLANd.ordinal()];
    }

    private static String I1O1I1LaNd(String string, String string2) {
        return l0iIlIO1laNd + string + '/' + string2;
    }

    private static String OOOIilanD(String string, String string2) {
        return iOIl0LAnD + string + '-' + string2;
    }

    private static String I1O1I1LaNd(String string) {
        File file = new File(string + "/holders");
        File[] arrfile = file.listFiles();
        if (arrfile != null) {
            return Arrays.stream(arrfile).map(File::getName).collect(Collectors.joining(" "));
        }
        return "";
    }

    static {
        for (lIl0ILaND lIl0ILaND2 : lIl0ILaND.values()) {
            ii111Land.ii1li00Land[lIl0ILaND2.ordinal()] = lIl0ILaND2.I1O1I1LaNd();
        }
        String string = liOIOOlLAnD.li0iOILAND(iI11I1lllaNd.lli0OiIlAND);
        int n2 = 11;
        if (!string.isEmpty()) {
            n2 = lOilLanD.OOOIilanD(string, ' ');
        }
        IOI1LaNd = n2;
    }
}

