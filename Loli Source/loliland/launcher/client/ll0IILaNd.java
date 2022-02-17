/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.VersionHelpers;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinReg;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.I11llANd;
import loliland.launcher.client.I1IiilLAnD;
import loliland.launcher.client.Il0IllIOLAnd;
import loliland.launcher.client.Il0O0LANd;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OOiO1lAnd;
import loliland.launcher.client.OiO1llAnd;
import loliland.launcher.client.Olli1LAnD;
import loliland.launcher.client.i11IllAnd;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.il1iILaNd;
import loliland.launcher.client.l0OllOOlland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lilO1llaND;
import loliland.launcher.client.lilOIlANd;
import loliland.launcher.client.ll0iI10IlAND;
import loliland.launcher.client.llO001i0laND;
import loliland.launcher.client.llOIOlAnd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ll0IILaNd
extends l0OllOOlland {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(ll0IILaNd.class);
    private Map OOOIilanD;

    ll0IILaNd() {
    }

    @Override
    protected lilOIlANd IO11O0LANd() {
        String string;
        WbemcliUtil.WmiResult wmiResult;
        Object object;
        String string2 = "";
        String string3 = "";
        String string4 = "";
        String string5 = "";
        String string6 = "";
        String string7 = "";
        long l2 = 0L;
        boolean bl = false;
        String string8 = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\";
        String[] arrstring = Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\");
        if (arrstring.length > 0) {
            object = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\" + arrstring[0];
            string2 = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, (String)object, "VendorIdentifier");
            string3 = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, (String)object, "ProcessorNameString");
            string4 = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, (String)object, "Identifier");
            try {
                l2 = (long)Advapi32Util.registryGetIntValue(WinReg.HKEY_LOCAL_MACHINE, (String)object, "~MHz") * 1000000L;
            }
            catch (Win32Exception win32Exception) {
                // empty catch block
            }
        }
        if (!string4.isEmpty()) {
            string5 = ll0IILaNd.I1O1I1LaNd(string4, "Family");
            string6 = ll0IILaNd.I1O1I1LaNd(string4, "Model");
            string7 = ll0IILaNd.I1O1I1LaNd(string4, "Stepping");
        }
        object = new WinBase.SYSTEM_INFO();
        Kernel32.INSTANCE.GetNativeSystemInfo((WinBase.SYSTEM_INFO)object);
        int n2 = object.processorArchitecture.pi.wProcessorArchitecture.intValue();
        if (n2 == 9 || n2 == 12 || n2 == 6) {
            bl = true;
        }
        if ((wmiResult = i11IllAnd.OOOIilanD()).getResultCount() > 0) {
            string = ii00llanD.I1O1I1LaNd(wmiResult, Il0O0LANd.I1O1I1LaNd, 0);
        } else {
            String[] arrstring2;
            if (bl) {
                String[] arrstring3 = new String[1];
                arrstring2 = arrstring3;
                arrstring3[0] = "ia64";
            } else {
                arrstring2 = new String[]{};
            }
            string = ll0IILaNd.I1O1I1LaNd(string7, string6, string5, arrstring2);
        }
        return new lilOIlANd(string2, string3, string5, string6, string7, string, bl, l2);
    }

    private static String I1O1I1LaNd(String string, String string2) {
        String[] arrstring = lOilLanD.OOOIilanD.split(string);
        boolean bl = false;
        for (String string3 : arrstring) {
            if (bl) {
                return string3;
            }
            bl = string3.equals(string2);
        }
        return "";
    }

    @Override
    protected List l0illAND() {
        if (VersionHelpers.IsWindows7OrGreater()) {
            List list = il1iILaNd.I1O1I1LaNd();
            int n2 = -1;
            int n3 = 0;
            int n4 = 0;
            this.OOOIilanD = new HashMap();
            for (I11llANd i11llANd : list) {
                int n5 = i11llANd.lli0OiIlAND();
                if (n5 != n2) {
                    n2 = n5;
                    n3 = 0;
                }
                this.OOOIilanD.put(String.format("%d,%d", i11llANd.lli0OiIlAND(), n3++), n4++);
            }
            return list;
        }
        return il1iILaNd.OOOIilanD();
    }

    @Override
    public long[] iIiO00OLaNd() {
        long[] arrl = new long[OOiO1lAnd.values().length];
        long[][] arrl2 = this.O1il1llOLANd();
        for (int i2 = 0; i2 < arrl.length; ++i2) {
            for (long[] arrl3 : arrl2) {
                int n2 = i2;
                arrl[n2] = arrl[n2] + arrl3[i2];
            }
        }
        return arrl;
    }

    @Override
    public long[] lO110l1LANd() {
        if (VersionHelpers.IsWindows7OrGreater()) {
            O1IiIiI1LAND o1IiIiI1LAND = Il0IllIOLAnd.lI00OlAND();
            List list = (List)o1IiIiI1LAND.I1O1I1LaNd();
            Map map = (Map)o1IiIiI1LAND.OOOIilanD();
            List list2 = (List)map.get(I1IiilLAnD.OOOIilanD);
            if (!list.isEmpty()) {
                long l2 = this.OOOIilanD();
                long[] arrl = new long[this.Oill1LAnD()];
                for (int i2 = 0; i2 < list.size(); ++i2) {
                    int n2;
                    int n3 = n2 = ((String)list.get(i2)).contains(",") ? this.OOOIilanD.getOrDefault(list.get(i2), 0) : lOilLanD.lli0OiIlAND((String)list.get(i2), 0);
                    if (n2 >= this.Oill1LAnD()) continue;
                    arrl[n2] = (Long)list2.get(n2) * l2 / 100L;
                }
                return arrl;
            }
        }
        return this.OOOIilanD(2);
    }

    @Override
    public long l11lLANd() {
        long[] arrl = this.OOOIilanD(1);
        return Arrays.stream(arrl).max().orElse(-1L);
    }

    private long[] OOOIilanD(int n2) {
        llO001i0laND llO001i0laND2 = new llO001i0laND();
        long[] arrl = new long[this.Oill1LAnD()];
        int n3 = llO001i0laND2.size() * arrl.length;
        Memory memory = new Memory(n3);
        if (0 != lilO1llaND.INSTANCE.CallNtPowerInformation(11, null, 0, memory, n3)) {
            I1O1I1LaNd.error("Unable to get Processor Information");
            Arrays.fill(arrl, -1L);
            return arrl;
        }
        for (int i2 = 0; i2 < arrl.length; ++i2) {
            llO001i0laND2 = new llO001i0laND(memory.share((long)i2 * (long)llO001i0laND2.size()));
            arrl[i2] = n2 == 1 ? (long)llO001i0laND2.maxMhz * 1000000L : (n2 == 2 ? (long)llO001i0laND2.currentMhz * 1000000L : -1L);
        }
        return arrl;
    }

    @Override
    public double[] I1O1I1LaNd(int n2) {
        if (n2 < 1 || n2 > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        double[] arrd = new double[n2];
        for (int i2 = 0; i2 < arrd.length; ++i2) {
            arrd[i2] = -1.0;
        }
        return arrd;
    }

    @Override
    public long[][] ii1li00Land() {
        O1IiIiI1LAND o1IiIiI1LAND = Il0IllIOLAnd.I1O1I1LaNd();
        List list = (List)o1IiIiI1LAND.I1O1I1LaNd();
        Map map = (Map)o1IiIiI1LAND.OOOIilanD();
        List list2 = (List)map.get(OiO1llAnd.lli0OiIlAND);
        List list3 = (List)map.get(OiO1llAnd.O1il1llOLANd);
        List list4 = (List)map.get(OiO1llAnd.lI00OlAND);
        List list5 = (List)map.get(OiO1llAnd.OOOIilanD);
        List list6 = (List)map.get(OiO1llAnd.li0iOILAND);
        long[][] arrl = new long[this.Oill1LAnD()][OOiO1lAnd.values().length];
        if (list.isEmpty() || list2 == null || list3 == null || list4 == null || list5 == null || list6 == null) {
            return arrl;
        }
        for (int i2 = 0; i2 < list.size(); ++i2) {
            int n2;
            int n3 = n2 = ((String)list.get(i2)).contains(",") ? this.OOOIilanD.getOrDefault(list.get(i2), 0) : lOilLanD.lli0OiIlAND((String)list.get(i2), 0);
            if (n2 >= this.Oill1LAnD()) continue;
            arrl[n2][OOiO1lAnd.lI00OlAND.I1O1I1LaNd()] = (Long)list2.get(n2);
            arrl[n2][OOiO1lAnd.I1O1I1LaNd.I1O1I1LaNd()] = (Long)list3.get(n2);
            arrl[n2][OOiO1lAnd.O1il1llOLANd.I1O1I1LaNd()] = (Long)list4.get(n2);
            arrl[n2][OOiO1lAnd.Oill1LAnD.I1O1I1LaNd()] = (Long)list5.get(n2);
            arrl[n2][OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] = (Long)list6.get(n2);
            long[] arrl2 = arrl[n2];
            int n4 = OOiO1lAnd.lI00OlAND.I1O1I1LaNd();
            arrl2[n4] = arrl2[n4] - (arrl[n2][OOiO1lAnd.O1il1llOLANd.I1O1I1LaNd()] + arrl[n2][OOiO1lAnd.Oill1LAnD.I1O1I1LaNd()]);
            long[] arrl3 = arrl[n2];
            int n5 = OOiO1lAnd.lI00OlAND.I1O1I1LaNd();
            arrl3[n5] = arrl3[n5] / 10000L;
            long[] arrl4 = arrl[n2];
            int n6 = OOiO1lAnd.I1O1I1LaNd.I1O1I1LaNd();
            arrl4[n6] = arrl4[n6] / 10000L;
            long[] arrl5 = arrl[n2];
            int n7 = OOiO1lAnd.O1il1llOLANd.I1O1I1LaNd();
            arrl5[n7] = arrl5[n7] / 10000L;
            long[] arrl6 = arrl[n2];
            int n8 = OOiO1lAnd.Oill1LAnD.I1O1I1LaNd();
            arrl6[n8] = arrl6[n8] / 10000L;
            long[] arrl7 = arrl[n2];
            int n9 = OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd();
            arrl7[n9] = arrl7[n9] / 10000L;
        }
        return arrl;
    }

    @Override
    public long l0iIlIO1laNd() {
        return llOIOlAnd.I1O1I1LaNd().getOrDefault(ll0iI10IlAND.I1O1I1LaNd, 0L);
    }

    @Override
    public long iOIl0LAnD() {
        return Il0IllIOLAnd.OOOIilanD().getOrDefault(Olli1LAnD.I1O1I1LaNd, 0L);
    }
}

