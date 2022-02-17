/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.unix.LibCAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.I11llANd;
import loliland.launcher.client.Iil1Ol1laNd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O0iIl1ilaND;
import loliland.launcher.client.OOiO1lAnd;
import loliland.launcher.client.l0OllOOlland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.liiIlO01lAnD;
import loliland.launcher.client.lilOIlANd;
import loliland.launcher.client.lli10iliLaND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class lIIi1I1lAnd
extends l0OllOOlland {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lIIi1I1lAnd.class);
    private static final Pattern OOOIilanD = Pattern.compile(".*<cpu\\s.*mask=\"(?:0x)?(\\p{XDigit}+)\".*>.*</cpu>.*");

    lIIi1I1lAnd() {
    }

    @Override
    protected lilOIlANd IO11O0LANd() {
        Pattern pattern = Pattern.compile("Origin=\"([^\"]*)\".*Id=(\\S+).*Family=(\\S+).*Model=(\\S+).*Stepping=(\\S+).*");
        Pattern pattern2 = Pattern.compile("Features=(\\S+)<.*");
        String string = "";
        String string2 = lli10iliLaND.I1O1I1LaNd("hw.model", "");
        String string3 = "";
        String string4 = "";
        String string5 = "";
        long l2 = lli10iliLaND.I1O1I1LaNd("hw.clockrate", 0L) * 1000000L;
        long l3 = 0L;
        List list = liOIOOlLAnD.I1O1I1LaNd("/var/run/dmesg.boot");
        for (String string6 : list) {
            Matcher matcher;
            if ((string6 = string6.trim()).startsWith("CPU:") && string2.isEmpty()) {
                string2 = string6.replace("CPU:", "").trim();
                continue;
            }
            if (string6.startsWith("Origin=")) {
                matcher = pattern.matcher(string6);
                if (!matcher.matches()) continue;
                string = matcher.group(1);
                l3 |= Long.decode(matcher.group(2)).longValue();
                string3 = Integer.decode(matcher.group(3)).toString();
                string4 = Integer.decode(matcher.group(4)).toString();
                string5 = Integer.decode(matcher.group(5)).toString();
                continue;
            }
            if (!string6.startsWith("Features=")) continue;
            matcher = pattern2.matcher(string6);
            if (!matcher.matches()) break;
            l3 |= Long.decode(matcher.group(1)) << 32;
            break;
        }
        boolean bl = Iill1lanD.OOOIilanD("uname -m").trim().contains("64");
        String string7 = lIIi1I1lAnd.I1O1I1LaNd(l3);
        return new lilOIlANd(string, string2, string3, string4, string5, string7, bl, l2);
    }

    @Override
    protected List l0illAND() {
        List list = lIIi1I1lAnd.IOI1LaNd();
        if (list.isEmpty()) {
            list.add(new I11llANd(0, 0, 0));
        }
        return list;
    }

    private static List IOI1LaNd() {
        String[] arrstring = lli10iliLaND.I1O1I1LaNd("kern.sched.topology_spec", "").split("\\n|\\r");
        long l2 = 1L;
        ArrayList<Long> arrayList = new ArrayList<Long>();
        ArrayList<Long> arrayList2 = new ArrayList<Long>();
        int n2 = 0;
        block5: for (String string : arrstring) {
            Matcher matcher;
            if (string.contains("<group level=")) {
                ++n2;
                continue;
            }
            if (string.contains("</group>")) {
                --n2;
                continue;
            }
            if (!string.contains("<cpu") || !(matcher = OOOIilanD.matcher(string)).matches()) continue;
            switch (n2) {
                case 1: {
                    l2 = Long.parseLong(matcher.group(1), 16);
                    continue block5;
                }
                case 2: {
                    arrayList.add(Long.parseLong(matcher.group(1), 16));
                    continue block5;
                }
                case 3: {
                    arrayList2.add(Long.parseLong(matcher.group(1), 16));
                    continue block5;
                }
            }
        }
        return lIIi1I1lAnd.I1O1I1LaNd(l2, arrayList, arrayList2);
    }

    private static List I1O1I1LaNd(long l2, List list, List list2) {
        ArrayList<I11llANd> arrayList = new ArrayList<I11llANd>();
        int n2 = Long.numberOfTrailingZeros(l2);
        int n3 = 63 - Long.numberOfLeadingZeros(l2);
        for (int i2 = n2; i2 <= n3; ++i2) {
            if ((l2 & 1L << i2) <= 0L) continue;
            int n4 = 0;
            I11llANd i11llANd = new I11llANd(i2, lIIi1I1lAnd.I1O1I1LaNd(list2, i2), lIIi1I1lAnd.I1O1I1LaNd(list, i2), n4);
            arrayList.add(i11llANd);
        }
        return arrayList;
    }

    private static int I1O1I1LaNd(List list, int n2) {
        for (int i2 = 0; i2 < list.size(); ++i2) {
            if (((Long)list.get(i2) & 1L << n2) == 0L) continue;
            return i2;
        }
        return 0;
    }

    @Override
    public long[] iIiO00OLaNd() {
        long[] arrl = new long[OOiO1lAnd.values().length];
        liiIlO01lAnD liiIlO01lAnD2 = new liiIlO01lAnD();
        lli10iliLaND.I1O1I1LaNd("kern.cp_time", liiIlO01lAnD2);
        arrl[OOiO1lAnd.I1O1I1LaNd.I1O1I1LaNd()] = liiIlO01lAnD2.cpu_ticks[0];
        arrl[OOiO1lAnd.OOOIilanD.I1O1I1LaNd()] = liiIlO01lAnD2.cpu_ticks[1];
        arrl[OOiO1lAnd.lI00OlAND.I1O1I1LaNd()] = liiIlO01lAnD2.cpu_ticks[2];
        arrl[OOiO1lAnd.O1il1llOLANd.I1O1I1LaNd()] = liiIlO01lAnD2.cpu_ticks[3];
        arrl[OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] = liiIlO01lAnD2.cpu_ticks[4];
        return arrl;
    }

    @Override
    public long[] lO110l1LANd() {
        long[] arrl = new long[]{lli10iliLaND.I1O1I1LaNd("dev.cpu.0.freq", -1L)};
        arrl[0] = arrl[0] > 0L ? arrl[0] * 1000000L : lli10iliLaND.I1O1I1LaNd("machdep.tsc_freq", -1L);
        return arrl;
    }

    @Override
    public long l11lLANd() {
        long l2 = -1L;
        String string = lli10iliLaND.I1O1I1LaNd("dev.cpu.0.freq_levels", "");
        for (String string2 : lOilLanD.OOOIilanD.split(string)) {
            long l3 = lOilLanD.OOOIilanD(string2.split("/")[0], -1L);
            if (l2 >= l3) continue;
            l2 = l3;
        }
        l2 = l2 > 0L ? (l2 *= 1000000L) : lli10iliLaND.I1O1I1LaNd("machdep.tsc_freq", -1L);
        return l2;
    }

    @Override
    public double[] I1O1I1LaNd(int n2) {
        if (n2 < 1 || n2 > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        double[] arrd = new double[n2];
        int n3 = Iil1Ol1laNd.INSTANCE.getloadavg(arrd, n2);
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
        String string = "kern.cp_times";
        long l2 = new liiIlO01lAnD().size();
        long l3 = l2 * (long)this.Oill1LAnD();
        Memory memory = new Memory(l3);
        if (0 != Iil1Ol1laNd.INSTANCE.sysctlbyname(string, memory, new O0iIl1ilaND(new LibCAPI.size_t(l3)), null, LibCAPI.size_t.ZERO)) {
            I1O1I1LaNd.error("Failed syctl call: {}, Error code: {}", (Object)string, (Object)Native.getLastError());
            return arrl;
        }
        for (int i2 = 0; i2 < this.Oill1LAnD(); ++i2) {
            arrl[i2][OOiO1lAnd.I1O1I1LaNd.I1O1I1LaNd()] = ((Pointer)memory).getLong(l2 * (long)i2 + (long)(0 * Iil1Ol1laNd.UINT64_SIZE));
            arrl[i2][OOiO1lAnd.OOOIilanD.I1O1I1LaNd()] = ((Pointer)memory).getLong(l2 * (long)i2 + (long)(1 * Iil1Ol1laNd.UINT64_SIZE));
            arrl[i2][OOiO1lAnd.lI00OlAND.I1O1I1LaNd()] = ((Pointer)memory).getLong(l2 * (long)i2 + (long)(2 * Iil1Ol1laNd.UINT64_SIZE));
            arrl[i2][OOiO1lAnd.O1il1llOLANd.I1O1I1LaNd()] = ((Pointer)memory).getLong(l2 * (long)i2 + (long)(3 * Iil1Ol1laNd.UINT64_SIZE));
            arrl[i2][OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] = ((Pointer)memory).getLong(l2 * (long)i2 + (long)(4 * Iil1Ol1laNd.UINT64_SIZE));
        }
        return arrl;
    }

    private static String I1O1I1LaNd(long l2) {
        boolean bl = false;
        String string = "Processor Information";
        for (String string2 : Iill1lanD.I1O1I1LaNd("dmidecode -t system")) {
            if (!bl && string2.contains(string)) {
                string = "ID:";
                bl = true;
                continue;
            }
            if (!bl || !string2.contains(string)) continue;
            return string2.split(string)[1].trim();
        }
        return String.format("%016X", l2);
    }

    @Override
    public long l0iIlIO1laNd() {
        String string = "vm.stats.sys.v_swtch";
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND(new LibCAPI.size_t((long)Iil1Ol1laNd.INT_SIZE));
        Memory memory = new Memory(o0iIl1ilaND.getValue().longValue());
        if (0 != Iil1Ol1laNd.INSTANCE.sysctlbyname(string, memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            return 0L;
        }
        return lOilLanD.I1O1I1LaNd(((Pointer)memory).getInt(0L));
    }

    @Override
    public long iOIl0LAnD() {
        String string = "vm.stats.sys.v_intr";
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND(new LibCAPI.size_t((long)Iil1Ol1laNd.INT_SIZE));
        Memory memory = new Memory(o0iIl1ilaND.getValue().longValue());
        if (0 != Iil1Ol1laNd.INSTANCE.sysctlbyname(string, memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            return 0L;
        }
        return lOilLanD.I1O1I1LaNd(((Pointer)memory).getInt(0L));
    }
}

