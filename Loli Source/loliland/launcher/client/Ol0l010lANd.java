/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.OilILAnD;
import loliland.launcher.client.Oli0liiLAnd;
import loliland.launcher.client.iIlI0LAND;
import loliland.launcher.client.lOllOiLANd;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.llII0lOiLAnD;

public abstract class Ol0l010lANd
implements lOllOiLANd {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(this::l0illAND);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(this::IO11O0LANd);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(this::l11lLANd);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(this::lO110l1LANd);

    @Override
    public iIlI0LAND I1O1I1LaNd() {
        return (iIlI0LAND)this.I1O1I1LaNd.get();
    }

    protected abstract iIlI0LAND l0illAND();

    @Override
    public Oli0liiLAnd OOOIilanD() {
        return (Oli0liiLAnd)this.OOOIilanD.get();
    }

    protected abstract Oli0liiLAnd IO11O0LANd();

    @Override
    public OilILAnD lI00OlAND() {
        return (OilILAnD)this.lI00OlAND.get();
    }

    protected abstract OilILAnD l11lLANd();

    @Override
    public llII0lOiLAnD lil0liLand() {
        return (llII0lOiLAnD)this.lli0OiIlAND.get();
    }

    protected abstract llII0lOiLAnD lO110l1LANd();

    @Override
    public List Oill1LAnD() {
        return this.I1O1I1LaNd(false);
    }
}

