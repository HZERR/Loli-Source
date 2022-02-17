/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.I1IllaND;
import loliland.launcher.client.IOIIO1lanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll0i0I1ILAnD;

final class l0I0OLaNd
extends IOIIO1lanD {
    private static final Pattern I1O1I1LaNd = Pattern.compile(".+\\s(\\d+)K\\s+(\\d+)K$");
    private final ll0i0I1ILAnD OOOIilanD;
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(I1IllaND::I1O1I1LaNd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(l0I0OLaNd::lil0liLand, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier li0iOILAND = lii1IO0LaNd.I1O1I1LaNd(l0I0OLaNd::Oill1LAnD, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier O1il1llOLANd = lii1IO0LaNd.I1O1I1LaNd(l0I0OLaNd::lIOILand, lii1IO0LaNd.I1O1I1LaNd());

    l0I0OLaNd(ll0i0I1ILAnD ll0i0I1ILAnD2) {
        this.OOOIilanD = ll0i0I1ILAnD2;
    }

    @Override
    public long OOOIilanD() {
        return (Long)((O1IiIiI1LAND)this.lli0OiIlAND.get()).I1O1I1LaNd();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)((O1IiIiI1LAND)this.lli0OiIlAND.get()).OOOIilanD();
    }

    @Override
    public long lI00OlAND() {
        return this.OOOIilanD.lI00OlAND() * (Long)((O1IiIiI1LAND)this.lI00OlAND.get()).OOOIilanD() + this.I1O1I1LaNd();
    }

    @Override
    public long lli0OiIlAND() {
        return this.OOOIilanD.lI00OlAND() * ((Long)((O1IiIiI1LAND)this.lI00OlAND.get()).OOOIilanD() - (Long)((O1IiIiI1LAND)this.lI00OlAND.get()).I1O1I1LaNd()) + this.OOOIilanD();
    }

    @Override
    public long li0iOILAND() {
        return (Long)this.li0iOILAND.get();
    }

    @Override
    public long O1il1llOLANd() {
        return (Long)this.O1il1llOLANd.get();
    }

    private static long Oill1LAnD() {
        long l2 = 0L;
        for (String string : Iill1lanD.I1O1I1LaNd("kstat -p cpu_stat:::pgswapin")) {
            l2 += lOilLanD.I1O1I1LaNd(string, 0L);
        }
        return l2;
    }

    private static long lIOILand() {
        long l2 = 0L;
        for (String string : Iill1lanD.I1O1I1LaNd("kstat -p cpu_stat:::pgswapout")) {
            l2 += lOilLanD.I1O1I1LaNd(string, 0L);
        }
        return l2;
    }

    private static O1IiIiI1LAND lil0liLand() {
        long l2 = 0L;
        long l3 = 0L;
        String string = Iill1lanD.I1O1I1LaNd("swap -lk", 1);
        Matcher matcher = I1O1I1LaNd.matcher(string);
        if (matcher.matches()) {
            l2 = lOilLanD.OOOIilanD(matcher.group(1), 0L) << 10;
            l3 = l2 - (lOilLanD.OOOIilanD(matcher.group(2), 0L) << 10);
        }
        return new O1IiIiI1LAND(l3, l2);
    }
}

