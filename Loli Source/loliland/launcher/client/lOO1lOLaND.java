/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.IOlllOLAND;
import loliland.launcher.client.IiiIOIlanD;
import loliland.launcher.client.iIOiOLAND;
import loliland.launcher.client.iilIi1laND;
import loliland.launcher.client.l0illAND;
import loliland.launcher.client.l11lLANd;
import loliland.launcher.client.lIOILand;
import loliland.launcher.client.lIl1OlAND;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lil0liLand;
import loliland.launcher.client.ll1OLAnd;
import loliland.launcher.client.lli011lLANd;

final class lOO1lOLaND
extends IiiIOIlanD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(lOO1lOLaND::lil0liLand);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(lOO1lOLaND::iilIi1laND);
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(lOO1lOLaND::lli011lLANd);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(lOO1lOLaND::l0illAND);

    lOO1lOLaND() {
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

    @Override
    public lIl1OlAND Oill1LAnD() {
        return new IOlllOLAND();
    }

    @Override
    public ll1OLAnd lIOILand() {
        return new iIOiOLAND();
    }

    private static String lil0liLand() {
        String string = null;
        string = l0illAND.I1O1I1LaNd();
        if (string == null && (string = l11lLANd.I1O1I1LaNd()) == null) {
            return "unknown";
        }
        return string;
    }

    private static String iilIi1laND() {
        String string = null;
        string = l0illAND.OOOIilanD();
        if (string == null && (string = lIOILand.I1O1I1LaNd()) == null && (string = lli011lLANd.I1O1I1LaNd()) == null) {
            return "unknown";
        }
        return string;
    }

    private static String lli011lLANd() {
        String string = null;
        string = l0illAND.lI00OlAND();
        if (string == null && (string = lil0liLand.I1O1I1LaNd()) == null && (string = iilIi1laND.I1O1I1LaNd()) == null && (string = lli011lLANd.OOOIilanD()) == null) {
            return "unknown";
        }
        return string;
    }

    private static String l0illAND() {
        String string = null;
        string = l0illAND.lli0OiIlAND();
        if (string == null && (string = lil0liLand.OOOIilanD()) == null && (string = iilIi1laND.OOOIilanD()) == null && (string = lli011lLANd.lI00OlAND()) == null) {
            return "unknown";
        }
        return string;
    }
}

