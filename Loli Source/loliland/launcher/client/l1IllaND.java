/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.solaris.LibKstat;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.i1i0lOliLaND;
import loliland.launcher.client.lIiIlOi0lanD;
import loliland.launcher.client.lO0li10llaND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class l1IllaND
extends i1i0lOliLaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(l1IllaND.class);
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

    public l1IllaND(NetworkInterface networkInterface) throws InstantiationException {
        super(networkInterface);
        this.liOIOOLANd();
    }

    public static List OOOIilanD(boolean bl) {
        ArrayList<l1IllaND> arrayList = new ArrayList<l1IllaND>();
        for (NetworkInterface networkInterface : l1IllaND.I1O1I1LaNd(bl)) {
            try {
                arrayList.add(new l1IllaND(networkInterface));
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
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            LibKstat.Kstat kstat = lIiIlOi0lanD.I1O1I1LaNd("link", -1, this.OOOIilanD());
            if (kstat == null) {
                kstat = lIiIlOi0lanD.I1O1I1LaNd(null, -1, this.OOOIilanD());
            }
            if (kstat != null && lIiIlOi0lanD.I1O1I1LaNd(kstat)) {
                this.lI00OlAND = lO0li10llaND.OOOIilanD(kstat, "obytes64");
                this.OOOIilanD = lO0li10llaND.OOOIilanD(kstat, "rbytes64");
                this.li0iOILAND = lO0li10llaND.OOOIilanD(kstat, "opackets64");
                this.lli0OiIlAND = lO0li10llaND.OOOIilanD(kstat, "ipackets64");
                this.Oill1LAnD = lO0li10llaND.OOOIilanD(kstat, "oerrors");
                this.O1il1llOLANd = lO0li10llaND.OOOIilanD(kstat, "ierrors");
                this.lil0liLand = lO0li10llaND.OOOIilanD(kstat, "collisions");
                this.lIOILand = lO0li10llaND.OOOIilanD(kstat, "dl_idrops");
                this.iilIi1laND = lO0li10llaND.OOOIilanD(kstat, "ifspeed");
                this.lli011lLANd = kstat.ks_snaptime / 1000000L;
                boolean bl = true;
                return bl;
            }
        }
        return false;
    }
}

