/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import loliland.launcher.client.Ii11I1lAnd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OIl11IOlanD;
import loliland.launcher.client.i0O00iOland;
import loliland.launcher.client.iOI10lAnd;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.l0l0iIlaND;
import loliland.launcher.client.l111llanD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lOl00LAND;
import loliland.launcher.client.lili0l0laNd;
import loliland.launcher.client.ll1ILAnd;
import loliland.launcher.client.lli1OiIland;

public interface liO0lAND {
    public String lI00OlAND();

    public String I1O1I1LaNd();

    public l111llanD lli0OiIlAND();

    public l0l0iIlaND lIOILand();

    public lli1OiIland lil0liLand();

    default public List iilIi1laND() {
        return this.I1O1I1LaNd(null, null, 0);
    }

    public List I1O1I1LaNd(Predicate var1, Comparator var2, int var3);

    @Deprecated
    default public List I1O1I1LaNd(int n2, lOl00LAND lOl00LAND2) {
        return this.I1O1I1LaNd(null, Ii11I1lAnd.I1O1I1LaNd(lOl00LAND2), n2);
    }

    default public List I1O1I1LaNd(Collection collection) {
        return collection.stream().map(this::lli0OiIlAND).filter(Objects::nonNull).filter(OIl11IOlanD.OOOIilanD).collect(Collectors.toList());
    }

    public iiO1LAnD lli0OiIlAND(int var1);

    @Deprecated
    default public List I1O1I1LaNd(int n2, int n3, lOl00LAND lOl00LAND2) {
        return this.I1O1I1LaNd(n2, null, Ii11I1lAnd.I1O1I1LaNd(lOl00LAND2), n3);
    }

    public List I1O1I1LaNd(int var1, Predicate var2, Comparator var3, int var4);

    public List OOOIilanD(int var1, Predicate var2, Comparator var3, int var4);

    public int lli011lLANd();

    public int l0illAND();

    public int IO11O0LANd();

    public int O1il1llOLANd();

    public long l11lLANd();

    public long lO110l1LANd();

    default public boolean l0iIlIO1laNd() {
        return 0 == lOilLanD.lli0OiIlAND(Iill1lanD.OOOIilanD("id -u"), -1);
    }

    public i0O00iOland iOIl0LAnD();

    default public iOI10lAnd[] iIiO00OLaNd() {
        return new iOI10lAnd[0];
    }

    default public List ii1li00Land() {
        return ll1ILAnd.I1O1I1LaNd();
    }

    default public List I1O1I1LaNd(boolean bl) {
        return lili0l0laNd.I1O1I1LaNd(bl);
    }
}

