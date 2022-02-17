/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.solaris.LibKstat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.I11llANd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OOiO1lAnd;
import loliland.launcher.client.il00iLAnD;
import loliland.launcher.client.l0OllOOlland;
import loliland.launcher.client.lIiIlOi0lanD;
import loliland.launcher.client.lO0li10llaND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lilOIlANd;

final class iIOlIland
extends l0OllOOlland {
    private static final String I1O1I1LaNd = "cpu_info";

    iIOlIland() {
    }

    @Override
    protected lilOIlANd IO11O0LANd() {
        String string = "";
        String string2 = "";
        String string3 = "";
        String string4 = "";
        String string5 = "";
        long l2 = 0L;
        lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();
        Object object = null;
        try {
            LibKstat.Kstat kstat = lIiIlOi0lanD.I1O1I1LaNd(I1O1I1LaNd, -1, null);
            if (kstat != null && lIiIlOi0lanD.I1O1I1LaNd(kstat)) {
                string = lO0li10llaND.I1O1I1LaNd(kstat, "vendor_id");
                string2 = lO0li10llaND.I1O1I1LaNd(kstat, "brand");
                string3 = lO0li10llaND.I1O1I1LaNd(kstat, "family");
                string4 = lO0li10llaND.I1O1I1LaNd(kstat, "model");
                string5 = lO0li10llaND.I1O1I1LaNd(kstat, "stepping");
                l2 = lO0li10llaND.OOOIilanD(kstat, "clock_MHz") * 1000000L;
            }
        }
        catch (Throwable throwable) {
            object = throwable;
            throw throwable;
        }
        finally {
            if (lIiIlOi0lanD2 != null) {
                if (object != null) {
                    try {
                        lIiIlOi0lanD2.close();
                    }
                    catch (Throwable throwable) {
                        ((Throwable)object).addSuppressed(throwable);
                    }
                } else {
                    lIiIlOi0lanD2.close();
                }
            }
        }
        boolean bl = "64".equals(Iill1lanD.OOOIilanD("isainfo -b").trim());
        object = iIOlIland.I1O1I1LaNd(string5, string4, string3);
        return new lilOIlANd(string, string2, string3, string4, string5, (String)object, bl, l2);
    }

    @Override
    protected List l0illAND() {
        Map map = iIOlIland.IOI1LaNd();
        ArrayList<I11llANd> arrayList = new ArrayList<I11llANd>();
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            List list = lIiIlOi0lanD.OOOIilanD(I1O1I1LaNd, -1, null);
            for (LibKstat.Kstat kstat : list) {
                if (kstat == null || !lIiIlOi0lanD.I1O1I1LaNd(kstat)) continue;
                int n2 = arrayList.size();
                String string = lO0li10llaND.I1O1I1LaNd(kstat, "chip_id");
                String string2 = lO0li10llaND.I1O1I1LaNd(kstat, "core_id");
                I11llANd i11llANd = new I11llANd(n2, lOilLanD.lli0OiIlAND(string2, 0), lOilLanD.lli0OiIlAND(string, 0), map.getOrDefault(n2, 0));
                arrayList.add(i11llANd);
            }
        }
        if (arrayList.isEmpty()) {
            arrayList.add(new I11llANd(0, 0, 0));
        }
        return arrayList;
    }

    private static Map IOI1LaNd() {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        int n2 = 0;
        for (String string : Iill1lanD.I1O1I1LaNd("lgrpinfo -c leaves")) {
            if (string.startsWith("lgroup")) {
                n2 = lOilLanD.Oill1LAnD(string);
                continue;
            }
            if (!string.contains("CPUs:") && !string.contains("CPU:")) continue;
            for (Integer n3 : lOilLanD.l0iIlIO1laNd(string.split(":")[1])) {
                hashMap.put(n3, n2);
            }
        }
        return hashMap;
    }

    @Override
    public long[] iIiO00OLaNd() {
        long[] arrl = new long[OOiO1lAnd.values().length];
        long[][] arrl2 = this.O1il1llOLANd();
        int n2 = 0;
        while (n2 < arrl.length) {
            for (long[] arrl3 : arrl2) {
                int n3 = n2;
                arrl[n3] = arrl[n3] + arrl3[n2];
            }
            int n4 = n2++;
            arrl[n4] = arrl[n4] / (long)arrl2.length;
        }
        return arrl;
    }

    @Override
    public long[] lO110l1LANd() {
        long[] arrl = new long[this.Oill1LAnD()];
        Arrays.fill(arrl, -1L);
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            for (int i2 = 0; i2 < arrl.length; ++i2) {
                for (LibKstat.Kstat kstat : lIiIlOi0lanD.OOOIilanD(I1O1I1LaNd, i2, null)) {
                    if (!lIiIlOi0lanD.I1O1I1LaNd(kstat)) continue;
                    arrl[i2] = lO0li10llaND.OOOIilanD(kstat, "current_clock_Hz");
                }
            }
        }
        return arrl;
    }

    @Override
    public long l11lLANd() {
        long l2 = -1L;
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            for (LibKstat.Kstat kstat : lIiIlOi0lanD.OOOIilanD(I1O1I1LaNd, 0, null)) {
                String string;
                if (!lIiIlOi0lanD.I1O1I1LaNd(kstat) || (string = lO0li10llaND.I1O1I1LaNd(kstat, "supported_frequencies_Hz")).isEmpty()) continue;
                for (String string2 : string.split(":")) {
                    long l3 = lOilLanD.OOOIilanD(string2, -1L);
                    if (l2 >= l3) continue;
                    l2 = l3;
                }
            }
        }
        return l2;
    }

    @Override
    public double[] I1O1I1LaNd(int n2) {
        if (n2 < 1 || n2 > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        double[] arrd = new double[n2];
        int n3 = il00iLAnD.INSTANCE.getloadavg(arrd, n2);
        if (n3 < n2) {
            for (int i2 = Math.max(n3, 0); i2 < arrd.length; ++i2) {
                arrd[i2] = -1.0;
            }
        }
        return arrd;
    }

    @Override
    public long[][] ii1li00Land() {
        long[][] arrl = new long[this.Oill1LAnD()][OOiO1lAnd.values().length];
        int n2 = -1;
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            for (LibKstat.Kstat kstat : lIiIlOi0lanD.OOOIilanD("cpu", -1, "sys")) {
                if (++n2 >= arrl.length) {
                    break;
                }
                if (!lIiIlOi0lanD.I1O1I1LaNd(kstat)) continue;
                arrl[n2][OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] = lO0li10llaND.OOOIilanD(kstat, "cpu_ticks_idle");
                arrl[n2][OOiO1lAnd.lI00OlAND.I1O1I1LaNd()] = lO0li10llaND.OOOIilanD(kstat, "cpu_ticks_kernel");
                arrl[n2][OOiO1lAnd.I1O1I1LaNd.I1O1I1LaNd()] = lO0li10llaND.OOOIilanD(kstat, "cpu_ticks_user");
            }
        }
        return arrl;
    }

    private static String I1O1I1LaNd(String string, String string2, String string3) {
        String string4;
        List list = Iill1lanD.I1O1I1LaNd("isainfo -v");
        StringBuilder stringBuilder = new StringBuilder();
        Iterator iterator = list.iterator();
        while (iterator.hasNext() && !(string4 = (String)iterator.next()).startsWith("32-bit")) {
            if (string4.startsWith("64-bit")) continue;
            stringBuilder.append(' ').append(string4.trim());
        }
        return iIOlIland.I1O1I1LaNd(string, string2, string3, lOilLanD.OOOIilanD.split(stringBuilder.toString().toLowerCase()));
    }

    @Override
    public long l0iIlIO1laNd() {
        long l2 = 0L;
        List list = Iill1lanD.I1O1I1LaNd("kstat -p cpu_stat:::/pswitch\\\\|inv_swtch/");
        for (String string : list) {
            l2 += lOilLanD.I1O1I1LaNd(string, 0L);
        }
        return l2;
    }

    @Override
    public long iOIl0LAnD() {
        long l2 = 0L;
        List list = Iill1lanD.I1O1I1LaNd("kstat -p cpu_stat:::/intr/");
        for (String string : list) {
            l2 += lOilLanD.I1O1I1LaNd(string, 0L);
        }
        return l2;
    }
}

