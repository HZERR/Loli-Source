/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import loliland.launcher.client.I11llANd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OO1Oi0LAnD;
import loliland.launcher.client.OOiO1lAnd;
import loliland.launcher.client.l01l0OilANd;
import loliland.launcher.client.l0OllOOlland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liO1Oli1lanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lilOIlANd;

final class ii01iLAnd
extends l0OllOOlland {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(l01l0OilANd::I1O1I1LaNd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(l01l0OilANd::OOOIilanD, lii1IO0LaNd.I1O1I1LaNd());
    private static final int lI00OlAND = ii01iLAnd.IOI1LaNd();
    private Perfstat.perfstat_partition_config_t lli0OiIlAND;
    private static final long li0iOILAND = lOilLanD.OOOIilanD(Iill1lanD.OOOIilanD("getconf CLK_TCK"), 100L);

    ii01iLAnd() {
    }

    @Override
    protected lilOIlANd IO11O0LANd() {
        String string8;
        String string2 = "unknown";
        String string3 = "";
        String string4 = "";
        boolean bl = false;
        String string5 = "Processor Type:";
        String string6 = "Processor Version:";
        String string7 = "CPU Type:";
        for (String string8 : Iill1lanD.I1O1I1LaNd("prtconf")) {
            if (string8.startsWith("Processor Type:")) {
                string3 = string8.split("Processor Type:")[1].trim();
                if (string3.startsWith("P")) {
                    string2 = "IBM";
                    continue;
                }
                if (!string3.startsWith("I")) continue;
                string2 = "Intel";
                continue;
            }
            if (string8.startsWith("Processor Version:")) {
                string4 = string8.split("Processor Version:")[1].trim();
                continue;
            }
            if (!string8.startsWith("CPU Type:")) continue;
            bl = string8.split("CPU Type:")[1].contains("64");
        }
        Object object = "";
        string8 = "";
        String string9 = Native.toString(this.lli0OiIlAND.machineID);
        if (string9.isEmpty()) {
            string9 = Iill1lanD.OOOIilanD("uname -m");
        }
        if (string9.length() > 10) {
            int n2 = string9.length() - 4;
            int n3 = string9.length() - 2;
            object = string9.substring(n2, n3);
            string8 = string9.substring(n3);
        }
        return new lilOIlANd(string2, string3, string4, (String)object, string8, string9, bl, (long)(this.lli0OiIlAND.processorMHz * 1000000.0));
    }

    @Override
    protected List l0illAND() {
        int n2;
        this.lli0OiIlAND = OO1Oi0LAnD.I1O1I1LaNd();
        int n3 = (int)this.lli0OiIlAND.numProcessors.max;
        if (n3 < 1) {
            n3 = 1;
        }
        if ((n2 = this.lli0OiIlAND.lcpus) < 1) {
            n2 = 1;
        }
        Map map = liO1Oli1lanD.I1O1I1LaNd();
        ArrayList<I11llANd> arrayList = new ArrayList<I11llANd>();
        for (int i2 = 0; i2 < n2; ++i2) {
            O1IiIiI1LAND o1IiIiI1LAND = (O1IiIiI1LAND)map.get(i2);
            arrayList.add(new I11llANd(i2, i2 / n3, o1IiIiI1LAND == null ? 0 : (Integer)o1IiIiI1LAND.OOOIilanD(), o1IiIiI1LAND == null ? 0 : (Integer)o1IiIiI1LAND.I1O1I1LaNd()));
        }
        return arrayList;
    }

    @Override
    public long[] iIiO00OLaNd() {
        Perfstat.perfstat_cpu_total_t perfstat_cpu_total_t2 = (Perfstat.perfstat_cpu_total_t)this.I1O1I1LaNd.get();
        long[] arrl = new long[OOiO1lAnd.values().length];
        arrl[OOiO1lAnd.I1O1I1LaNd.ordinal()] = perfstat_cpu_total_t2.user * 1000L / li0iOILAND;
        arrl[OOiO1lAnd.lI00OlAND.ordinal()] = perfstat_cpu_total_t2.sys * 1000L / li0iOILAND;
        arrl[OOiO1lAnd.lli0OiIlAND.ordinal()] = perfstat_cpu_total_t2.idle * 1000L / li0iOILAND;
        arrl[OOiO1lAnd.li0iOILAND.ordinal()] = perfstat_cpu_total_t2.wait * 1000L / li0iOILAND;
        arrl[OOiO1lAnd.O1il1llOLANd.ordinal()] = perfstat_cpu_total_t2.devintrs * 1000L / li0iOILAND;
        arrl[OOiO1lAnd.Oill1LAnD.ordinal()] = perfstat_cpu_total_t2.softintrs * 1000L / li0iOILAND;
        arrl[OOiO1lAnd.lIOILand.ordinal()] = (perfstat_cpu_total_t2.idle_stolen_purr + perfstat_cpu_total_t2.busy_stolen_purr) * 1000L / li0iOILAND;
        return arrl;
    }

    @Override
    public long[] lO110l1LANd() {
        long[] arrl = new long[this.Oill1LAnD()];
        Arrays.fill(arrl, -1L);
        String string = "runs at";
        int n2 = 0;
        for (String string2 : Iill1lanD.I1O1I1LaNd("pmcycles -m")) {
            if (!string2.contains(string)) continue;
            arrl[n2++] = lOilLanD.I1O1I1LaNd(string2.split(string)[1].trim());
            if (n2 < arrl.length) continue;
            break;
        }
        return arrl;
    }

    @Override
    protected long l11lLANd() {
        Perfstat.perfstat_cpu_total_t perfstat_cpu_total_t2 = (Perfstat.perfstat_cpu_total_t)this.I1O1I1LaNd.get();
        return perfstat_cpu_total_t2.processorHZ;
    }

    @Override
    public double[] I1O1I1LaNd(int n2) {
        if (n2 < 1 || n2 > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        double[] arrd = new double[n2];
        long[] arrl = ((Perfstat.perfstat_cpu_total_t)this.I1O1I1LaNd.get()).loadavg;
        for (int i2 = 0; i2 < n2; ++i2) {
            arrd[i2] = (double)arrl[i2] / (double)(1L << lI00OlAND);
        }
        return arrd;
    }

    @Override
    public long[][] ii1li00Land() {
        Perfstat.perfstat_cpu_t[] arrperfstat_cpu_t = (Perfstat.perfstat_cpu_t[])this.OOOIilanD.get();
        long[][] arrl = new long[arrperfstat_cpu_t.length][OOiO1lAnd.values().length];
        for (int i2 = 0; i2 < arrperfstat_cpu_t.length; ++i2) {
            arrl[i2] = new long[OOiO1lAnd.values().length];
            arrl[i2][OOiO1lAnd.I1O1I1LaNd.ordinal()] = arrperfstat_cpu_t[i2].user * 1000L / li0iOILAND;
            arrl[i2][OOiO1lAnd.lI00OlAND.ordinal()] = arrperfstat_cpu_t[i2].sys * 1000L / li0iOILAND;
            arrl[i2][OOiO1lAnd.lli0OiIlAND.ordinal()] = arrperfstat_cpu_t[i2].idle * 1000L / li0iOILAND;
            arrl[i2][OOiO1lAnd.li0iOILAND.ordinal()] = arrperfstat_cpu_t[i2].wait * 1000L / li0iOILAND;
            arrl[i2][OOiO1lAnd.O1il1llOLANd.ordinal()] = arrperfstat_cpu_t[i2].devintrs * 1000L / li0iOILAND;
            arrl[i2][OOiO1lAnd.Oill1LAnD.ordinal()] = arrperfstat_cpu_t[i2].softintrs * 1000L / li0iOILAND;
            arrl[i2][OOiO1lAnd.lIOILand.ordinal()] = (arrperfstat_cpu_t[i2].idle_stolen_purr + arrperfstat_cpu_t[i2].busy_stolen_purr) * 1000L / li0iOILAND;
        }
        return arrl;
    }

    @Override
    public long l0iIlIO1laNd() {
        return ((Perfstat.perfstat_cpu_total_t)this.I1O1I1LaNd.get()).pswitch;
    }

    @Override
    public long iOIl0LAnD() {
        Perfstat.perfstat_cpu_total_t perfstat_cpu_total_t2 = (Perfstat.perfstat_cpu_total_t)this.I1O1I1LaNd.get();
        return perfstat_cpu_total_t2.devintrs + perfstat_cpu_total_t2.softintrs;
    }

    private static int IOI1LaNd() {
        for (String string : liOIOOlLAnD.I1O1I1LaNd("/usr/include/sys/proc.h")) {
            if (!string.contains("SBITS") || !string.contains("#define")) continue;
            return lOilLanD.I1O1I1LaNd(string, 16);
        }
        return 16;
    }
}

