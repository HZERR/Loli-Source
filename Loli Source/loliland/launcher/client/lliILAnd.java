/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.i1i0lOliLaND;
import loliland.launcher.client.iOIOOilanD;
import loliland.launcher.client.lii1IO0LaNd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class lliILAnd
extends i1i0lOliLaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lliILAnd.class);
    private long OOOIilanD;
    private long lI00OlAND;
    private long lli0OiIlAND;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;
    private long lil0liLand;
    private long iilIi1laND;
    private long lli011lLANd;
    private Supplier l0illAND;

    public lliILAnd(NetworkInterface networkInterface, Supplier supplier) throws InstantiationException {
        super(networkInterface);
        this.l0illAND = supplier;
        this.liOIOOLANd();
    }

    public static List OOOIilanD(boolean bl) {
        Supplier supplier = lii1IO0LaNd.I1O1I1LaNd(iOIOOilanD::I1O1I1LaNd, lii1IO0LaNd.I1O1I1LaNd());
        ArrayList<lliILAnd> arrayList = new ArrayList<lliILAnd>();
        for (NetworkInterface networkInterface : lliILAnd.I1O1I1LaNd(bl)) {
            try {
                arrayList.add(new lliILAnd(networkInterface, supplier));
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
        return this.iilIi1laND;
    }

    @Override
    public long II1Iland() {
        return this.lli011lLANd;
    }

    @Override
    public boolean liOIOOLANd() {
        Perfstat.perfstat_netinterface_t[] arrperfstat_netinterface_t = (Perfstat.perfstat_netinterface_t[])this.l0illAND.get();
        long l2 = System.currentTimeMillis();
        for (Perfstat.perfstat_netinterface_t perfstat_netinterface_t2 : arrperfstat_netinterface_t) {
            String string = Native.toString(perfstat_netinterface_t2.name);
            if (!string.equals(this.OOOIilanD())) continue;
            this.lI00OlAND = perfstat_netinterface_t2.obytes;
            this.OOOIilanD = perfstat_netinterface_t2.ibytes;
            this.li0iOILAND = perfstat_netinterface_t2.opackets;
            this.lli0OiIlAND = perfstat_netinterface_t2.ipackets;
            this.Oill1LAnD = perfstat_netinterface_t2.oerrors;
            this.O1il1llOLANd = perfstat_netinterface_t2.ierrors;
            this.lil0liLand = perfstat_netinterface_t2.collisions;
            this.lIOILand = perfstat_netinterface_t2.if_iqdrops;
            this.iilIi1laND = perfstat_netinterface_t2.bitrate;
            this.lli011lLANd = l2;
            return true;
        }
        return false;
    }
}

