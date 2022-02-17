/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.O11i1I1lAnd;
import loliland.launcher.client.l0illAND;
import loliland.launcher.client.l11lLANd;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1O0iiland;

final class iIOiOLAND
extends ll1O0iiland {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(this::li0iOILAND);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(this::O1il1llOLANd);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(this::Oill1LAnD);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(this::lIOILand);
    private final Supplier li0iOILAND = lii1IO0LaNd.I1O1I1LaNd(l11lLANd::OOOIilanD);

    iIOiOLAND() {
    }

    @Override
    public String I1O1I1LaNd() {
        return (String)this.I1O1I1LaNd.get();
    }

    @Override
    public String OOOIilanD() {
        return (String)this.OOOIilanD.get();
    }

    @Override
    public String lI00OlAND() {
        return (String)this.lI00OlAND.get();
    }

    @Override
    public String lli0OiIlAND() {
        return (String)this.lli0OiIlAND.get();
    }

    private String li0iOILAND() {
        String string = null;
        string = l0illAND.li0iOILAND();
        if (string == null && (string = (String)((O11i1I1lAnd)this.li0iOILAND.get()).I1O1I1LaNd()) == null) {
            return "unknown";
        }
        return string;
    }

    private String O1il1llOLANd() {
        String string = null;
        string = l0illAND.O1il1llOLANd();
        if (string == null && (string = (String)((O11i1I1lAnd)this.li0iOILAND.get()).OOOIilanD()) == null) {
            return "unknown";
        }
        return string;
    }

    private String Oill1LAnD() {
        String string = null;
        string = l0illAND.Oill1LAnD();
        if (string == null && (string = (String)((O11i1I1lAnd)this.li0iOILAND.get()).lI00OlAND()) == null) {
            return "unknown";
        }
        return string;
    }

    private String lIOILand() {
        String string = null;
        string = l0illAND.lIOILand();
        if (string == null && (string = (String)((O11i1I1lAnd)this.li0iOILAND.get()).lli0OiIlAND()) == null) {
            return "unknown";
        }
        return string;
    }
}

