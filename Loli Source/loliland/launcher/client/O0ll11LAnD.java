/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.i1i0lOliLaND;
import loliland.launcher.client.lOilLanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class O0ll11LAnD
extends i1i0lOliLaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(O0ll11LAnD.class);
    private long OOOIilanD;
    private long lI00OlAND;
    private long lli0OiIlAND;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;
    private long lil0liLand;
    private long iilIi1laND;

    public O0ll11LAnD(NetworkInterface networkInterface) throws InstantiationException {
        super(networkInterface);
        this.liOIOOLANd();
    }

    public static List OOOIilanD(boolean bl) {
        ArrayList<O0ll11LAnD> arrayList = new ArrayList<O0ll11LAnD>();
        for (NetworkInterface networkInterface : O0ll11LAnD.I1O1I1LaNd(bl)) {
            try {
                arrayList.add(new O0ll11LAnD(networkInterface));
            }
            catch (InstantiationException instantiationException) {
                I1O1I1LaNd.debug("Network Interface Instantiation failed: {}", (Object)instantiationException.getMessage());
            }
        }
        return arrayList;
    }

    @Override
    public long l0iIlIO1laNd() {
        return this.OOOIilanD;
    }

    @Override
    public long iOIl0LAnD() {
        return this.lI00OlAND;
    }

    @Override
    public long iIiO00OLaNd() {
        return this.lli0OiIlAND;
    }

    @Override
    public long ii1li00Land() {
        return this.li0iOILAND;
    }

    @Override
    public long IOI1LaNd() {
        return this.O1il1llOLANd;
    }

    @Override
    public long lI00ilAND() {
        return this.Oill1LAnD;
    }

    @Override
    public long l0l00lAND() {
        return this.lIOILand;
    }

    @Override
    public long iOl10IlLAnd() {
        return this.lil0liLand;
    }

    @Override
    public long lIiIii1LAnD() {
        return 0L;
    }

    @Override
    public long II1Iland() {
        return this.iilIi1laND;
    }

    @Override
    public boolean liOIOOLANd() {
        String string = Iill1lanD.I1O1I1LaNd("netstat -bI " + this.OOOIilanD(), 1);
        this.iilIi1laND = System.currentTimeMillis();
        String[] arrstring = lOilLanD.OOOIilanD.split(string);
        if (arrstring.length < 12) {
            return false;
        }
        this.lI00OlAND = lOilLanD.lI00OlAND(arrstring[10], 0L);
        this.OOOIilanD = lOilLanD.lI00OlAND(arrstring[7], 0L);
        this.li0iOILAND = lOilLanD.lI00OlAND(arrstring[8], 0L);
        this.lli0OiIlAND = lOilLanD.lI00OlAND(arrstring[4], 0L);
        this.Oill1LAnD = lOilLanD.lI00OlAND(arrstring[9], 0L);
        this.O1il1llOLANd = lOilLanD.lI00OlAND(arrstring[5], 0L);
        this.lil0liLand = lOilLanD.lI00OlAND(arrstring[11], 0L);
        this.lIOILand = lOilLanD.lI00OlAND(arrstring[6], 0L);
        return true;
    }
}

