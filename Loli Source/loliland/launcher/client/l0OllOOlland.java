/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.I11llANd;
import loliland.launcher.client.OOiO1lAnd;
import loliland.launcher.client.Oli0liiLAnd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lilOIlANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class l0OllOOlland
implements Oli0liiLAnd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(l0OllOOlland.class);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(this::IO11O0LANd);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(this::l11lLANd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(this::lO110l1LANd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier li0iOILAND = lii1IO0LaNd.I1O1I1LaNd(this::l0iIlIO1laNd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier O1il1llOLANd = lii1IO0LaNd.I1O1I1LaNd(this::iOIl0LAnD, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier Oill1LAnD = lii1IO0LaNd.I1O1I1LaNd(this::iIiO00OLaNd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lIOILand = lii1IO0LaNd.I1O1I1LaNd(this::ii1li00Land, lii1IO0LaNd.I1O1I1LaNd());
    private final int lil0liLand;
    private final int iilIi1laND;
    private final int lli011lLANd;
    private final List l0illAND = Collections.unmodifiableList(this.l0illAND());

    protected l0OllOOlland() {
        HashSet<String> hashSet = new HashSet<String>();
        HashSet<Integer> hashSet2 = new HashSet<Integer>();
        for (I11llANd i11llANd : this.l0illAND) {
            int n2 = i11llANd.lI00OlAND();
            hashSet.add(i11llANd.OOOIilanD() + ":" + n2);
            hashSet2.add(n2);
        }
        this.lli011lLANd = this.l0illAND.size();
        this.iilIi1laND = hashSet.size();
        this.lil0liLand = hashSet2.size();
    }

    protected abstract List l0illAND();

    protected abstract lilOIlANd IO11O0LANd();

    @Override
    public lilOIlANd I1O1I1LaNd() {
        return (lilOIlANd)this.OOOIilanD.get();
    }

    @Override
    public long OOOIilanD() {
        return (Long)this.lI00OlAND.get();
    }

    protected abstract long l11lLANd();

    @Override
    public long[] lI00OlAND() {
        long[] arrl = (long[])this.lli0OiIlAND.get();
        if (arrl.length == this.Oill1LAnD()) {
            return arrl;
        }
        long[] arrl2 = new long[this.Oill1LAnD()];
        Arrays.fill(arrl2, arrl[0]);
        return arrl2;
    }

    protected abstract long[] lO110l1LANd();

    @Override
    public long iilIi1laND() {
        return (Long)this.li0iOILAND.get();
    }

    protected abstract long l0iIlIO1laNd();

    @Override
    public long lli011lLANd() {
        return (Long)this.O1il1llOLANd.get();
    }

    protected abstract long iOIl0LAnD();

    @Override
    public List lli0OiIlAND() {
        return this.l0illAND;
    }

    @Override
    public long[] li0iOILAND() {
        return (long[])this.Oill1LAnD.get();
    }

    protected abstract long[] iIiO00OLaNd();

    @Override
    public long[][] O1il1llOLANd() {
        return (long[][])this.lIOILand.get();
    }

    protected abstract long[][] ii1li00Land();

    @Override
    public double I1O1I1LaNd(long[] arrl) {
        if (arrl.length != OOiO1lAnd.values().length) {
            throw new IllegalArgumentException("Tick array " + arrl.length + " should have " + OOiO1lAnd.values().length + " elements");
        }
        long[] arrl2 = this.li0iOILAND();
        long l2 = 0L;
        for (int i2 = 0; i2 < arrl2.length; ++i2) {
            l2 += arrl2[i2] - arrl[i2];
        }
        long l3 = arrl2[OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] + arrl2[OOiO1lAnd.li0iOILAND.I1O1I1LaNd()] - arrl[OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] - arrl[OOiO1lAnd.li0iOILAND.I1O1I1LaNd()];
        I1O1I1LaNd.trace("Total ticks: {}  Idle ticks: {}", (Object)l2, (Object)l3);
        return l2 > 0L && l3 >= 0L ? (double)(l2 - l3) / (double)l2 : 0.0;
    }

    @Override
    public double[] I1O1I1LaNd(long[][] arrl) {
        if (arrl.length != this.lli011lLANd || arrl[0].length != OOiO1lAnd.values().length) {
            throw new IllegalArgumentException("Tick array " + arrl.length + " should have " + this.lli011lLANd + " arrays, each of which has " + OOiO1lAnd.values().length + " elements");
        }
        long[][] arrl2 = this.O1il1llOLANd();
        double[] arrd = new double[this.lli011lLANd];
        for (int i2 = 0; i2 < this.lli011lLANd; ++i2) {
            long l2 = 0L;
            for (int i3 = 0; i3 < arrl2[i2].length; ++i3) {
                l2 += arrl2[i2][i3] - arrl[i2][i3];
            }
            long l3 = arrl2[i2][OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] + arrl2[i2][OOiO1lAnd.li0iOILAND.I1O1I1LaNd()] - arrl[i2][OOiO1lAnd.lli0OiIlAND.I1O1I1LaNd()] - arrl[i2][OOiO1lAnd.li0iOILAND.I1O1I1LaNd()];
            I1O1I1LaNd.trace("CPU: {}  Total ticks: {}  Idle ticks: {}", i2, l2, l3);
            arrd[i2] = l2 > 0L && l3 >= 0L ? (double)(l2 - l3) / (double)l2 : 0.0;
        }
        return arrd;
    }

    @Override
    public int Oill1LAnD() {
        return this.lli011lLANd;
    }

    @Override
    public int lIOILand() {
        return this.iilIi1laND;
    }

    @Override
    public int lil0liLand() {
        return this.lil0liLand;
    }

    protected static String I1O1I1LaNd(String string, String string2, String string3, String[] arrstring) {
        long l2 = 0L;
        long l3 = lOilLanD.OOOIilanD(string, 0L);
        long l4 = lOilLanD.OOOIilanD(string2, 0L);
        long l5 = lOilLanD.OOOIilanD(string3, 0L);
        l2 |= l3 & 0xFL;
        l2 |= (l4 & 0xFL) << 4;
        l2 |= (l4 & 0xF0L) << 16;
        l2 |= (l5 & 0xFL) << 8;
        l2 |= (l5 & 0xF0L) << 20;
        String[] arrstring2 = arrstring;
        int n2 = arrstring2.length;
        block64: for (int i2 = 0; i2 < n2; ++i2) {
            String string4;
            switch (string4 = arrstring2[i2]) {
                case "fpu": {
                    l2 |= 0x100000000L;
                    continue block64;
                }
                case "vme": {
                    l2 |= 0x200000000L;
                    continue block64;
                }
                case "de": {
                    l2 |= 0x400000000L;
                    continue block64;
                }
                case "pse": {
                    l2 |= 0x800000000L;
                    continue block64;
                }
                case "tsc": {
                    l2 |= 0x1000000000L;
                    continue block64;
                }
                case "msr": {
                    l2 |= 0x2000000000L;
                    continue block64;
                }
                case "pae": {
                    l2 |= 0x4000000000L;
                    continue block64;
                }
                case "mce": {
                    l2 |= 0x8000000000L;
                    continue block64;
                }
                case "cx8": {
                    l2 |= 0x10000000000L;
                    continue block64;
                }
                case "apic": {
                    l2 |= 0x20000000000L;
                    continue block64;
                }
                case "sep": {
                    l2 |= 0x80000000000L;
                    continue block64;
                }
                case "mtrr": {
                    l2 |= 0x100000000000L;
                    continue block64;
                }
                case "pge": {
                    l2 |= 0x200000000000L;
                    continue block64;
                }
                case "mca": {
                    l2 |= 0x400000000000L;
                    continue block64;
                }
                case "cmov": {
                    l2 |= 0x800000000000L;
                    continue block64;
                }
                case "pat": {
                    l2 |= 0x1000000000000L;
                    continue block64;
                }
                case "pse-36": {
                    l2 |= 0x2000000000000L;
                    continue block64;
                }
                case "psn": {
                    l2 |= 0x4000000000000L;
                    continue block64;
                }
                case "clfsh": {
                    l2 |= 0x8000000000000L;
                    continue block64;
                }
                case "ds": {
                    l2 |= 0x20000000000000L;
                    continue block64;
                }
                case "acpi": {
                    l2 |= 0x40000000000000L;
                    continue block64;
                }
                case "mmx": {
                    l2 |= 0x80000000000000L;
                    continue block64;
                }
                case "fxsr": {
                    l2 |= 0x100000000000000L;
                    continue block64;
                }
                case "sse": {
                    l2 |= 0x200000000000000L;
                    continue block64;
                }
                case "sse2": {
                    l2 |= 0x400000000000000L;
                    continue block64;
                }
                case "ss": {
                    l2 |= 0x800000000000000L;
                    continue block64;
                }
                case "htt": {
                    l2 |= 0x1000000000000000L;
                    continue block64;
                }
                case "tm": {
                    l2 |= 0x2000000000000000L;
                    continue block64;
                }
                case "ia64": {
                    l2 |= 0x4000000000000000L;
                    continue block64;
                }
                case "pbe": {
                    l2 |= Long.MIN_VALUE;
                    continue block64;
                }
            }
        }
        return String.format("%016X", l2);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.I1O1I1LaNd().OOOIilanD());
        stringBuilder.append("\n ").append(this.lil0liLand()).append(" physical CPU package(s)");
        stringBuilder.append("\n ").append(this.lIOILand()).append(" physical CPU core(s)");
        stringBuilder.append("\n ").append(this.Oill1LAnD()).append(" logical CPU(s)");
        stringBuilder.append('\n').append("Identifier: ").append(this.I1O1I1LaNd().Oill1LAnD());
        stringBuilder.append('\n').append("ProcessorID: ").append(this.I1O1I1LaNd().O1il1llOLANd());
        stringBuilder.append('\n').append("Microarchitecture: ").append(this.I1O1I1LaNd().iilIi1laND());
        return stringBuilder.toString();
    }
}

