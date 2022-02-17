/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import loliland.launcher.client.i0iOLANd;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.l01l111lAnD;
import loliland.launcher.client.l0IlILANd;
import loliland.launcher.client.lI10lAnd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.li1O0l0LaNd;
import loliland.launcher.client.lilILAND;
import loliland.launcher.client.ll10lanD;

public class l1IlliI0lANd
extends lI10lAnd {
    private static final int li0iOILAND = 255;
    private static final int O1il1llOLANd = 1;
    private static final int Oill1LAnD = 1;
    private static final int lIOILand = 2;
    private static final int lil0liLand = 16;
    private static final int iilIi1laND = 0x20000000;
    private static final int lli011lLANd = 262144;
    private static final int l0illAND = 8;
    private static final int IO11O0LANd = 524288;
    private static final int l11lLANd = 0x100000;
    private static final int lO110l1LANd = 131072;
    private static final int l0iIlIO1laNd = 65536;
    private static final int iOIl0LAnD = 128;
    private static final int iIiO00OLaNd = 64;
    private static final int ii1li00Land = 0x200000;
    private static final int IOI1LaNd = 0x2000000;
    private static final int lI00ilAND = 4;
    private static final int l0l00lAND = 32768;
    private static final int iOl10IlLAnd = 32;
    private static final Map lIiIii1LAnD = new HashMap();
    private static final long II1Iland;

    public l1IlliI0lANd() {
        Kernel32.INSTANCE.SetErrorMode(1);
    }

    @Override
    public List I1O1I1LaNd(boolean bl) {
        ArrayList arrayList = l1IlliI0lANd.I1O1I1LaNd(null);
        HashMap<String, l01l111lAnD> hashMap = new HashMap<String, l01l111lAnD>();
        for (l01l111lAnD l01l111lAnD2 : arrayList) {
            hashMap.put(l01l111lAnD2.lli0OiIlAND(), l01l111lAnD2);
        }
        for (l01l111lAnD l01l111lAnD2 : l1IlliI0lANd.I1O1I1LaNd(null, bl)) {
            if (hashMap.containsKey(l01l111lAnD2.lli0OiIlAND())) {
                l01l111lAnD l01l111lAnD3 = (l01l111lAnD)hashMap.get(l01l111lAnD2.lli0OiIlAND());
                arrayList.remove(l01l111lAnD3);
                arrayList.add(new i0iOLANd(l01l111lAnD2.I1O1I1LaNd(), l01l111lAnD3.OOOIilanD(), l01l111lAnD3.lI00OlAND().isEmpty() ? l01l111lAnD2.lI00OlAND() : l01l111lAnD3.lI00OlAND(), l01l111lAnD3.lli0OiIlAND(), l01l111lAnD3.li0iOILAND(), l01l111lAnD3.O1il1llOLANd(), "", l01l111lAnD3.lIOILand(), l01l111lAnD3.lil0liLand(), l01l111lAnD3.iilIi1laND(), l01l111lAnD3.lli011lLANd(), l01l111lAnD3.l0illAND(), 0L, 0L));
                continue;
            }
            if (bl) continue;
            arrayList.add(l01l111lAnD2);
        }
        return arrayList;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static ArrayList I1O1I1LaNd(String string) {
        ArrayList<i0iOLANd> arrayList = new ArrayList<i0iOLANd>();
        char[] arrc = new char[255];
        WinNT.HANDLE hANDLE = Kernel32.INSTANCE.FindFirstVolume(arrc, 255);
        if (hANDLE == WinBase.INVALID_HANDLE_VALUE) {
            return arrayList;
        }
        try {
            do {
                char[] arrc2 = new char[16];
                char[] arrc3 = new char[255];
                char[] arrc4 = new char[255];
                IntByReference intByReference = new IntByReference();
                WinNT.LARGE_INTEGER lARGE_INTEGER = new WinNT.LARGE_INTEGER(0L);
                WinNT.LARGE_INTEGER lARGE_INTEGER2 = new WinNT.LARGE_INTEGER(0L);
                WinNT.LARGE_INTEGER lARGE_INTEGER3 = new WinNT.LARGE_INTEGER(0L);
                String string2 = Native.toString(arrc);
                Kernel32.INSTANCE.GetVolumeInformation(string2, arrc3, 255, null, null, intByReference, arrc2, 16);
                int n2 = intByReference.getValue();
                Kernel32.INSTANCE.GetVolumePathNamesForVolumeName(string2, arrc4, 255, null);
                String string3 = Native.toString(arrc4);
                if (string3.isEmpty() || string != null && !string.equals(string2)) continue;
                String string4 = Native.toString(arrc3);
                String string5 = Native.toString(arrc2);
                StringBuilder stringBuilder = new StringBuilder((0x80000 & n2) == 0 ? "rw" : "ro");
                String string6 = lIiIii1LAnD.entrySet().stream().filter(entry -> ((Integer)entry.getKey() & n2) > 0).map(Map.Entry::getValue).collect(Collectors.joining(","));
                if (!string6.isEmpty()) {
                    stringBuilder.append(',').append(string6);
                }
                Kernel32.INSTANCE.GetDiskFreeSpaceEx(string2, lARGE_INTEGER, lARGE_INTEGER2, lARGE_INTEGER3);
                String string7 = lOilLanD.I1O1I1LaNd(string2, "");
                arrayList.add(new i0iOLANd(String.format("%s (%s)", string4, string3), string2, string4, string3, stringBuilder.toString(), string7, "", l1IlliI0lANd.OOOIilanD(string3), string5, lARGE_INTEGER3.getValue(), lARGE_INTEGER.getValue(), lARGE_INTEGER2.getValue(), 0L, 0L));
            } while (Kernel32.INSTANCE.FindNextVolume(hANDLE, arrc, 255));
            ArrayList<i0iOLANd> arrayList2 = arrayList;
            return arrayList2;
        }
        finally {
            Kernel32.INSTANCE.FindVolumeClose(hANDLE);
        }
    }

    static List I1O1I1LaNd(String string, boolean bl) {
        ArrayList<i0iOLANd> arrayList = new ArrayList<i0iOLANd>();
        WbemcliUtil.WmiResult wmiResult = li1O0l0LaNd.I1O1I1LaNd(string, bl);
        for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
            String string2;
            Object[] arrobject;
            long l2 = ii00llanD.li0iOILAND(wmiResult, lilILAND.li0iOILAND, i2);
            long l3 = ii00llanD.li0iOILAND(wmiResult, lilILAND.lIOILand, i2);
            Object object = ii00llanD.I1O1I1LaNd(wmiResult, lilILAND.OOOIilanD, i2);
            String string3 = ii00llanD.I1O1I1LaNd(wmiResult, lilILAND.O1il1llOLANd, i2);
            String string4 = ii00llanD.I1O1I1LaNd(wmiResult, lilILAND.lil0liLand, i2);
            String string5 = ii00llanD.lil0liLand(wmiResult, lilILAND.I1O1I1LaNd, i2) == 1 ? "ro" : "rw";
            int n2 = ii00llanD.O1il1llOLANd(wmiResult, lilILAND.lI00OlAND, i2);
            if (n2 != 4) {
                arrobject = new char[255];
                Kernel32.INSTANCE.GetVolumeNameForVolumeMountPoint(string3 + "\\", (char[])arrobject, 255);
                string2 = Native.toString(arrobject);
            } else {
                string2 = ii00llanD.I1O1I1LaNd(wmiResult, lilILAND.Oill1LAnD, i2);
                arrobject = string2.split("\\\\");
                if (arrobject.length > 1 && arrobject[arrobject.length - 1].length() > 0) {
                    object = arrobject[arrobject.length - 1];
                }
            }
            arrayList.add(new i0iOLANd(String.format("%s (%s)", object, string3), string2, string4, string3 + "\\", string5, "", "", l1IlliI0lANd.OOOIilanD(string3), ii00llanD.I1O1I1LaNd(wmiResult, lilILAND.lli0OiIlAND, i2), l2, l2, l3, 0L, 0L));
        }
        return arrayList;
    }

    private static String OOOIilanD(String string) {
        switch (Kernel32.INSTANCE.GetDriveType(string)) {
            case 2: {
                return "Removable drive";
            }
            case 3: {
                return "Fixed drive";
            }
            case 4: {
                return "Network drive";
            }
            case 5: {
                return "CD-ROM";
            }
            case 6: {
                return "RAM drive";
            }
        }
        return "Unknown drive type";
    }

    @Override
    public long OOOIilanD() {
        Map map = (Map)ll10lanD.OOOIilanD().OOOIilanD();
        List list = (List)map.get(l0IlILANd.OOOIilanD);
        long l2 = 0L;
        if (list != null) {
            for (int i2 = 0; i2 < list.size(); ++i2) {
                l2 += ((Long)list.get(i2)).longValue();
            }
        }
        return l2;
    }

    @Override
    public long lI00OlAND() {
        return II1Iland;
    }

    static {
        lIiIii1LAnD.put(2, "casepn");
        lIiIii1LAnD.put(1, "casess");
        lIiIii1LAnD.put(16, "fcomp");
        lIiIii1LAnD.put(0x20000000, "dax");
        lIiIii1LAnD.put(262144, "streams");
        lIiIii1LAnD.put(8, "acls");
        lIiIii1LAnD.put(0x100000, "wronce");
        lIiIii1LAnD.put(131072, "efs");
        lIiIii1LAnD.put(65536, "oids");
        lIiIii1LAnD.put(128, "reparse");
        lIiIii1LAnD.put(64, "sparse");
        lIiIii1LAnD.put(0x200000, "trans");
        lIiIii1LAnD.put(0x2000000, "journaled");
        lIiIii1LAnD.put(4, "unicode");
        lIiIii1LAnD.put(32768, "vcomp");
        lIiIii1LAnD.put(32, "quota");
        II1Iland = System.getenv("ProgramFiles(x86)") == null ? 0xFF8000L : 0xFF0000L;
    }
}

