/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Platform;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import loliland.launcher.client.Ii11I1lAnd;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OIl11IOlanD;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.l10lO11lanD;
import loliland.launcher.client.l111llanD;
import loliland.launcher.client.liO0lAND;
import loliland.launcher.client.lii1IO0LaNd;

public abstract class lIilLaNd
implements liO0lAND {
    public static final String I1O1I1LaNd = "oshi.os.unix.whoCommand";
    protected static final boolean OOOIilanD = l10lO11lanD.I1O1I1LaNd("oshi.os.unix.whoCommand", false);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(this::OOOIilanD);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(this::li0iOILAND);
    private final Supplier li0iOILAND = lii1IO0LaNd.I1O1I1LaNd(this::IOI1LaNd);

    @Override
    public String I1O1I1LaNd() {
        return (String)this.lI00OlAND.get();
    }

    protected abstract String OOOIilanD();

    @Override
    public String lI00OlAND() {
        return (String)((O1IiIiI1LAND)this.lli0OiIlAND.get()).I1O1I1LaNd();
    }

    @Override
    public l111llanD lli0OiIlAND() {
        return (l111llanD)((O1IiIiI1LAND)this.lli0OiIlAND.get()).OOOIilanD();
    }

    protected abstract O1IiIiI1LAND li0iOILAND();

    @Override
    public int O1il1llOLANd() {
        return (Integer)this.li0iOILAND.get();
    }

    private int IOI1LaNd() {
        if (Platform.is64Bit()) {
            return 64;
        }
        int n2 = System.getProperty("os.arch").indexOf("64") != -1 ? 64 : 32;
        return this.I1O1I1LaNd(n2);
    }

    protected abstract int I1O1I1LaNd(int var1);

    @Override
    public List I1O1I1LaNd(Predicate predicate, Comparator comparator, int n2) {
        return this.Oill1LAnD().stream().filter(predicate == null ? OIl11IOlanD.I1O1I1LaNd : predicate).sorted(comparator == null ? Ii11I1lAnd.I1O1I1LaNd : comparator).limit(n2 > 0 ? (long)n2 : Long.MAX_VALUE).collect(Collectors.toList());
    }

    protected abstract List Oill1LAnD();

    @Override
    public List I1O1I1LaNd(int n2, Predicate predicate, Comparator comparator, int n3) {
        List list = this.OOOIilanD(n2);
        iiO1LAnD iiO1LAnD3 = list.stream().filter(iiO1LAnD2 -> iiO1LAnD2.l0illAND() == n2).findAny().orElse(null);
        long l2 = iiO1LAnD3 == null ? 0L : iiO1LAnD3.IOI1LaNd();
        return this.OOOIilanD(n2).stream().filter(predicate == null ? OIl11IOlanD.I1O1I1LaNd : predicate).filter(iiO1LAnD2 -> iiO1LAnD2.I1O1I1LaNd() != n2 && iiO1LAnD2.IOI1LaNd() >= l2).sorted(comparator == null ? Ii11I1lAnd.I1O1I1LaNd : comparator).limit(n3 > 0 ? (long)n3 : Long.MAX_VALUE).collect(Collectors.toList());
    }

    protected abstract List OOOIilanD(int var1);

    @Override
    public List OOOIilanD(int n2, Predicate predicate, Comparator comparator, int n3) {
        List list = this.lI00OlAND(n2);
        iiO1LAnD iiO1LAnD3 = list.stream().filter(iiO1LAnD2 -> iiO1LAnD2.l0illAND() == n2).findAny().orElse(null);
        long l2 = iiO1LAnD3 == null ? 0L : iiO1LAnD3.IOI1LaNd();
        return this.lI00OlAND(n2).stream().filter(predicate == null ? OIl11IOlanD.I1O1I1LaNd : predicate).filter(iiO1LAnD2 -> iiO1LAnD2.I1O1I1LaNd() != n2 && iiO1LAnD2.IOI1LaNd() >= l2).sorted(comparator == null ? Ii11I1lAnd.I1O1I1LaNd : comparator).limit(n3 > 0 ? (long)n3 : Long.MAX_VALUE).collect(Collectors.toList());
    }

    protected abstract List lI00OlAND(int var1);

    protected static Set I1O1I1LaNd(Collection collection, int n2, boolean bl) {
        Map<Integer, Integer> map = collection.stream().collect(Collectors.toMap(iiO1LAnD::I1O1I1LaNd, iiO1LAnD::l0illAND));
        return lIilLaNd.I1O1I1LaNd(map, n2, bl);
    }

    protected static Set I1O1I1LaNd(Map map, int n2, boolean bl) {
        HashSet<Integer> hashSet = new HashSet<Integer>();
        hashSet.add(n2);
        ArrayDeque<Integer> arrayDeque = new ArrayDeque<Integer>();
        arrayDeque.add(n2);
        do {
            Iterator iterator = lIilLaNd.I1O1I1LaNd(map, (Integer)arrayDeque.poll()).iterator();
            while (iterator.hasNext()) {
                int n3 = (Integer)iterator.next();
                if (hashSet.contains(n3)) continue;
                hashSet.add(n3);
                arrayDeque.add(n3);
            }
        } while (bl && !arrayDeque.isEmpty());
        return hashSet;
    }

    private static Set I1O1I1LaNd(Map map, int n2) {
        return map.entrySet().stream().filter(entry -> ((Integer)entry.getValue()).equals(n2) && !((Integer)entry.getKey()).equals(n2)).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.I1O1I1LaNd()).append(' ').append(this.lI00OlAND()).append(' ').append(this.lli0OiIlAND());
        return stringBuilder.toString();
    }
}

