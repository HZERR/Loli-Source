/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.I11llANd;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OOiO1lAnd;
import loliland.launcher.client.l0OllOOlland;
import loliland.launcher.client.lIl0iilland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lilOIlANd;
import loliland.launcher.client.lllilIiiLANd;

public class lO01liiLaND
extends l0OllOOlland {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(lO01liiLaND::IOI1LaNd, lii1IO0LaNd.I1O1I1LaNd());
    private static final Pattern OOOIilanD = Pattern.compile("cpu(\\d+): smt (\\d+), core (\\d+), package (\\d+)");

    @Override
    protected lilOIlANd IO11O0LANd() {
        String string = lIl0iilland.I1O1I1LaNd("machdep.cpuvendor", "");
        int[] arrn = new int[]{6, 2};
        String string2 = lIl0iilland.I1O1I1LaNd(arrn, "");
        int n2 = lOilLanD.O1il1llOLANd(lIl0iilland.I1O1I1LaNd("machdep.cpuid", ""), 0);
        int n3 = lOilLanD.O1il1llOLANd(lIl0iilland.I1O1I1LaNd("machdep.cpufeature", ""), 0);
        IIOOOlIiLanD iIOOOlIiLanD = lO01liiLaND.OOOIilanD(n2);
        String string3 = ((Integer)iIOOOlIiLanD.I1O1I1LaNd()).toString();
        String string4 = ((Integer)iIOOOlIiLanD.OOOIilanD()).toString();
        String string5 = ((Integer)iIOOOlIiLanD.lI00OlAND()).toString();
        long l2 = lOilLanD.I1O1I1LaNd(string2);
        if (l2 < 0L) {
            l2 = this.l11lLANd();
        }
        arrn[1] = 1;
        String string6 = lIl0iilland.I1O1I1LaNd(arrn, "");
        boolean bl = string6 != null && string6.contains("64") || Iill1lanD.OOOIilanD("uname -m").trim().contains("64");
        String string7 = String.format("%08x%08x", n3, n2);
        return new lilOIlANd(string, string2, string3, string4, string5, string7, bl, l2);
    }

    private static IIOOOlIiLanD OOOIilanD(int n2) {
        int n3 = n2 >> 16 & 0xFF0 | n2 >> 8 & 0xF;
        int n4 = n2 >> 12 & 0xF0 | n2 >> 4 & 0xF;
        int n5 = n2 & 0xF;
        return new IIOOOlIiLanD(n3, n4, n5);
    }

    @Override
    protected long l11lLANd() {
        return this.lO110l1LANd()[0];
    }

    @Override
    protected long[] lO110l1LANd() {
        long[] arrl = new long[1];
        int[] arrn = new int[]{6, 12};
        arrl[0] = lIl0iilland.I1O1I1LaNd(arrn, 0L) * 1000000L;
        return arrl;
    }

    @Override
    protected List l0illAND() {
        Object object2;
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> hashMap2 = new HashMap<Integer, Integer>();
        for (Object object2 : Iill1lanD.I1O1I1LaNd("dmesg")) {
            Matcher matcher = OOOIilanD.matcher((CharSequence)object2);
            if (!matcher.matches()) continue;
            int n2 = lOilLanD.lli0OiIlAND(matcher.group(1), 0);
            hashMap.put(n2, lOilLanD.lli0OiIlAND(matcher.group(3), 0));
            hashMap2.put(n2, lOilLanD.lli0OiIlAND(matcher.group(4), 0));
        }
        int n3 = lIl0iilland.I1O1I1LaNd("hw.ncpuonline", 1);
        if (n3 < hashMap.keySet().size()) {
            n3 = hashMap.keySet().size();
        }
        object2 = new ArrayList(n3);
        for (int i2 = 0; i2 < n3; ++i2) {
            object2.add(new I11llANd(i2, hashMap.getOrDefault(i2, 0), hashMap2.getOrDefault(i2, 0)));
        }
        return object2;
    }

    @Override
    protected long l0iIlIO1laNd() {
        return (Long)((O1IiIiI1LAND)this.I1O1I1LaNd.get()).I1O1I1LaNd();
    }

    @Override
    protected long iOIl0LAnD() {
        return (Long)((O1IiIiI1LAND)this.I1O1I1LaNd.get()).OOOIilanD();
    }

    private static O1IiIiI1LAND IOI1LaNd() {
        long l2 = 0L;
        long l3 = 0L;
        List list = Iill1lanD.I1O1I1LaNd("vmstat -s");
        for (String string : list) {
            if (string.endsWith("cpu context switches")) {
                l2 = lOilLanD.Oill1LAnD(string);
                continue;
            }
            if (!string.endsWith("interrupts")) continue;
            l3 = lOilLanD.Oill1LAnD(string);
        }
        return new O1IiIiI1LAND(l2, l3);
    }

    @Override
    protected long[] iIiO00OLaNd() {
        long[] arrl = new long[OOiO1lAnd.values().length];
        int[] arrn = new int[]{1, 40};
        Memory memory = lIl0iilland.I1O1I1LaNd(arrn);
        long[] arrl2 = lO01liiLaND.I1O1I1LaNd(memory, false);
        if (arrl2.length >= 5) {
            arrl[OOiO1lAnd.I1O1I1LaNd.I1O1I1LaNd()] = arrl2[0];
            arrl[OOiO1lAnd.OOOIilanD.I1O1I1LaNd()] = arrl2[1];
            arrl[OOiO1lAnd.lI00OlAND.I1O1I1LaNd()] = arrl2[2];
            int n2 = arrl2.length > 5 ? 1 : 0;
            arrl[OOiO1lAnd.O1il1llOLANd.I1O1I1LaNd()] = arrl2[3 + n2];
            arrl[OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] = arrl2[4 + n2];
        }
        return arrl;
    }

    @Override
    protected long[][] ii1li00Land() {
        long[][] arrl = new long[this.Oill1LAnD()][OOiO1lAnd.values().length];
        int[] arrn = new int[3];
        arrn[0] = 1;
        arrn[1] = 71;
        for (int i2 = 0; i2 < this.Oill1LAnD(); ++i2) {
            arrn[2] = i2;
            Memory memory = lIl0iilland.I1O1I1LaNd(arrn);
            long[] arrl2 = lO01liiLaND.I1O1I1LaNd(memory, true);
            if (arrl2.length < 5) continue;
            arrl[i2][OOiO1lAnd.I1O1I1LaNd.I1O1I1LaNd()] = arrl2[0];
            arrl[i2][OOiO1lAnd.OOOIilanD.I1O1I1LaNd()] = arrl2[1];
            arrl[i2][OOiO1lAnd.lI00OlAND.I1O1I1LaNd()] = arrl2[2];
            int n2 = arrl2.length > 5 ? 1 : 0;
            arrl[i2][OOiO1lAnd.O1il1llOLANd.I1O1I1LaNd()] = arrl2[3 + n2];
            arrl[i2][OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] = arrl2[4 + n2];
        }
        return arrl;
    }

    private static long[] I1O1I1LaNd(Memory memory, boolean bl) {
        int n2;
        long l2 = bl ? 8L : (long)Native.LONG_SIZE;
        int n3 = n2 = memory == null ? 0 : (int)(memory.size() / l2);
        if (bl && memory != null) {
            return memory.getLongArray(0L, n2);
        }
        long[] arrl = new long[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            arrl[i2] = memory.getNativeLong((long)i2 * l2).longValue();
        }
        return arrl;
    }

    @Override
    public double[] I1O1I1LaNd(int n2) {
        if (n2 < 1 || n2 > 3) {
            throw new IllegalArgumentException("Must include from one to three elements.");
        }
        double[] arrd = new double[n2];
        int n3 = lllilIiiLANd.INSTANCE.getloadavg(arrd, n2);
        if (n3 < n2) {
            Arrays.fill(arrd, -1.0);
        }
        return arrd;
    }
}

