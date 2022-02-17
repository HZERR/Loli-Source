/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.solaris.LibKstat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import loliland.launcher.client.I0Oiland;
import loliland.launcher.client.lIiIlOi0lanD;
import loliland.launcher.client.lO0li10llaND;
import loliland.launcher.client.llOIlIilAND;

public final class lOI01II1lAnD
extends I0Oiland {
    private static final String[] I1O1I1LaNd = new String[]{null, "battery", "acpi_drv"};
    private static final int OOOIilanD;

    public lOI01II1lAnD(String string, String string2, double d2, double d3, double d4, double d5, double d6, double d7, boolean bl, boolean bl2, boolean bl3, llOIlIilAND llOIlIilAND2, int n2, int n3, int n4, int n5, String string3, LocalDate localDate, String string4, String string5, double d8) {
        super(string, string2, d2, d3, d4, d5, d6, d7, bl, bl2, bl3, llOIlIilAND2, n2, n3, n4, n5, string3, localDate, string4, string5, d8);
    }

    public static List iOl10IlLAnd() {
        return Arrays.asList(lOI01II1lAnD.I1O1I1LaNd("BAT0"));
    }

    private static lOI01II1lAnD I1O1I1LaNd(String string) {
        String string2 = string;
        String string3 = "unknown";
        double d2 = 1.0;
        double d3 = -1.0;
        double d4 = 0.0;
        double d5 = 0.0;
        double d6 = -1.0;
        double d7 = 0.0;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        llOIlIilAND llOIlIilAND2 = llOIlIilAND.lI00OlAND;
        int n2 = 0;
        int n3 = 1;
        int n4 = 1;
        int n5 = -1;
        String string4 = "unknown";
        LocalDate localDate = null;
        String string5 = "unknown";
        String string6 = "unknown";
        double d8 = 0.0;
        if (OOOIilanD > 0) {
            try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
                long l2;
                long l3;
                LibKstat.Kstat kstat = lIiIlOi0lanD.I1O1I1LaNd(I1O1I1LaNd[OOOIilanD], 0, "battery BIF0");
                if (kstat != null) {
                    l3 = lO0li10llaND.OOOIilanD(kstat, "bif_last_cap");
                    if (l3 == -1L || l3 <= 0L) {
                        l3 = lO0li10llaND.OOOIilanD(kstat, "bif_design_cap");
                    }
                    if (l3 != -1L && l3 > 0L) {
                        n3 = (int)l3;
                    }
                    if ((l2 = lO0li10llaND.OOOIilanD(kstat, "bif_unit")) == 0L) {
                        llOIlIilAND2 = llOIlIilAND.I1O1I1LaNd;
                    } else if (l2 == 1L) {
                        llOIlIilAND2 = llOIlIilAND.OOOIilanD;
                    }
                    string3 = lO0li10llaND.I1O1I1LaNd(kstat, "bif_model");
                    string6 = lO0li10llaND.I1O1I1LaNd(kstat, "bif_serial");
                    string4 = lO0li10llaND.I1O1I1LaNd(kstat, "bif_type");
                    string5 = lO0li10llaND.I1O1I1LaNd(kstat, "bif_oem_info");
                }
                if ((kstat = lIiIlOi0lanD.I1O1I1LaNd(I1O1I1LaNd[OOOIilanD], 0, "battery BST0")) != null) {
                    long l4;
                    boolean bl4;
                    l3 = lO0li10llaND.OOOIilanD(kstat, "bst_rem_cap");
                    if (l3 >= 0L) {
                        n2 = (int)l3;
                    }
                    if ((l2 = lO0li10llaND.OOOIilanD(kstat, "bst_rate")) == -1L) {
                        l2 = 0L;
                    }
                    boolean bl5 = bl4 = (lO0li10llaND.OOOIilanD(kstat, "bst_state") & 0x10L) > 0L;
                    if (!bl4) {
                        double d9 = d3 = l2 > 0L ? 3600.0 * (double)l3 / (double)l2 : -1.0;
                    }
                    if ((l4 = lO0li10llaND.OOOIilanD(kstat, "bst_voltage")) > 0L) {
                        d6 = (double)l4 / 1000.0;
                        d7 = d5 * 1000.0 / (double)l4;
                    }
                }
            }
        }
        return new lOI01II1lAnD(string2, string3, d2, d3, d4, d5, d6, d7, bl, bl2, bl3, llOIlIilAND2, n2, n3, n4, n5, string4, localDate, string5, string6, d8);
    }

    static {
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            OOOIilanD = lIiIlOi0lanD.I1O1I1LaNd(I1O1I1LaNd[1], 0, null) != null ? 1 : (lIiIlOi0lanD.I1O1I1LaNd(I1O1I1LaNd[2], 0, null) != null ? 2 : 0);
        }
    }
}

