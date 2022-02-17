/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.unix.solaris.LibKstat;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.IlII0OLAnD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lO0li10llaND;

public final class lIiIlOi0lanD
implements AutoCloseable {
    private lIiIlOi0lanD() {
        lO0li10llaND.OOOIilanD().lock();
        lIiIlOi0lanD.I1O1I1LaNd();
    }

    public static boolean I1O1I1LaNd(LibKstat.Kstat kstat) {
        int n2 = 0;
        while (0 > lO0li10llaND.lli0OiIlAND().kstat_read(lO0li10llaND.lI00OlAND(), kstat, null)) {
            if (11 != Native.getLastError() || 5 <= ++n2) {
                if (lO0li10llaND.li0iOILAND().isDebugEnabled()) {
                    lO0li10llaND.li0iOILAND().debug("Failed to read kstat {}:{}:{}", Native.toString(kstat.ks_module, StandardCharsets.US_ASCII), kstat.ks_instance, Native.toString(kstat.ks_name, StandardCharsets.US_ASCII));
                }
                return false;
            }
            iiIIIlO1lANd.I1O1I1LaNd(8 << n2);
        }
        return true;
    }

    public static LibKstat.Kstat I1O1I1LaNd(String string, int n2, String string2) {
        return lO0li10llaND.lli0OiIlAND().kstat_lookup(lO0li10llaND.lI00OlAND(), string, n2, string2);
    }

    public static List OOOIilanD(String string, int n2, String string2) {
        ArrayList<LibKstat.Kstat> arrayList = new ArrayList<LibKstat.Kstat>();
        for (LibKstat.Kstat kstat = lO0li10llaND.lli0OiIlAND().kstat_lookup(lO0li10llaND.lI00OlAND(), string, n2, string2); kstat != null; kstat = kstat.next()) {
            if (string != null && !string.equals(Native.toString(kstat.ks_module, StandardCharsets.US_ASCII)) || n2 >= 0 && n2 != kstat.ks_instance || string2 != null && !string2.equals(Native.toString(kstat.ks_name, StandardCharsets.US_ASCII))) continue;
            arrayList.add(kstat);
        }
        return arrayList;
    }

    public static int I1O1I1LaNd() {
        return lO0li10llaND.lli0OiIlAND().kstat_chain_update(lO0li10llaND.lI00OlAND());
    }

    @Override
    public void close() {
        lO0li10llaND.OOOIilanD().unlock();
    }

    /* synthetic */ lIiIlOi0lanD(IlII0OLAnD ilII0OLAnD) {
        this();
    }
}

