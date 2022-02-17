/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.IiiIOIlanD;
import loliland.launcher.client.l0IOlaND;
import loliland.launcher.client.lIl0iilland;
import loliland.launcher.client.lIl1OlAND;
import loliland.launcher.client.lOIiIlI0Land;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1OLAnd;

public class lIO11LAnD
extends IiiIOIlanD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(lIO11LAnD::lil0liLand);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(lIO11LAnD::iilIi1laND);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(lIO11LAnD::lli011lLANd);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(lIO11LAnD::l0illAND);

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

    @Override
    protected lIl1OlAND Oill1LAnD() {
        return new l0IOlaND();
    }

    @Override
    protected ll1OLAnd lIOILand() {
        return new lOIiIlI0Land((String)this.I1O1I1LaNd.get(), (String)this.OOOIilanD.get(), (String)this.lI00OlAND.get(), lIl0iilland.I1O1I1LaNd("hw.product", "unknown"));
    }

    private static String lil0liLand() {
        return lIl0iilland.I1O1I1LaNd("hw.vendor", "unknown");
    }

    private static String iilIi1laND() {
        return lIl0iilland.I1O1I1LaNd("hw.version", "unknown");
    }

    private static String lli011lLANd() {
        return lIl0iilland.I1O1I1LaNd("hw.serialno", "unknown");
    }

    private static String l0illAND() {
        return lIl0iilland.I1O1I1LaNd("hw.uuid", "unknown");
    }
}

