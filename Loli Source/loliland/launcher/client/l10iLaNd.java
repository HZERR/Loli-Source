/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.l0Oil0IILAnd;
import loliland.launcher.client.ll1O0iiland;

final class l10iLaNd
extends ll1O0iiland {
    private static final String I1O1I1LaNd = "IBM";
    private final String OOOIilanD;
    private final String lI00OlAND;
    private final String lli0OiIlAND;

    l10iLaNd(Supplier supplier) {
        IIOOOlIiLanD iIOOOlIiLanD = l0Oil0IILAnd.I1O1I1LaNd((List)supplier.get());
        this.OOOIilanD = iiIIIlO1lANd.I1O1I1LaNd((String)iIOOOlIiLanD.I1O1I1LaNd()) ? "unknown" : (String)iIOOOlIiLanD.I1O1I1LaNd();
        this.lI00OlAND = iiIIIlO1lANd.I1O1I1LaNd((String)iIOOOlIiLanD.OOOIilanD()) ? "unknown" : (String)iIOOOlIiLanD.OOOIilanD();
        this.lli0OiIlAND = iiIIIlO1lANd.I1O1I1LaNd((String)iIOOOlIiLanD.lI00OlAND()) ? "unknown" : (String)iIOOOlIiLanD.lI00OlAND();
    }

    @Override
    public String I1O1I1LaNd() {
        return I1O1I1LaNd;
    }

    @Override
    public String OOOIilanD() {
        return this.OOOIilanD;
    }

    @Override
    public String lli0OiIlAND() {
        return this.lI00OlAND;
    }

    @Override
    public String lI00OlAND() {
        return this.lli0OiIlAND;
    }
}

