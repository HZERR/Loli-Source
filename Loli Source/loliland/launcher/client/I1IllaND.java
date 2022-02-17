/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.solaris.LibKstat;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.lIiIlOi0lanD;
import loliland.launcher.client.lO0li10llaND;

public final class I1IllaND {
    private I1IllaND() {
    }

    public static O1IiIiI1LAND I1O1I1LaNd() {
        long l2 = 0L;
        long l3 = 0L;
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            LibKstat.Kstat kstat = lIiIlOi0lanD.I1O1I1LaNd(null, -1, "system_pages");
            if (kstat != null && lIiIlOi0lanD.I1O1I1LaNd(kstat)) {
                l2 = lO0li10llaND.OOOIilanD(kstat, "availrmem");
                l3 = lO0li10llaND.OOOIilanD(kstat, "physmem");
            }
        }
        return new O1IiIiI1LAND(l2, l3);
    }
}

