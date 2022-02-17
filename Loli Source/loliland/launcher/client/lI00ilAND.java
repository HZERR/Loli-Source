/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.liOIOOlLAnD;

public final class lI00ilAND {
    private lI00ilAND() {
    }

    public static double I1O1I1LaNd() {
        String string = liOIOOlLAnD.li0iOILAND(iI11I1lllaNd.iOl10IlLAnd);
        int n2 = string.indexOf(32);
        try {
            if (n2 < 0) {
                return 0.0;
            }
            return Double.parseDouble(string.substring(0, n2));
        }
        catch (NumberFormatException numberFormatException) {
            return 0.0;
        }
    }
}

