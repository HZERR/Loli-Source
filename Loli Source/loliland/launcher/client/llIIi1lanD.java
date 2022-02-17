/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.IO0lll1lanD;
import loliland.launcher.client.lIOlLaND;

public class llIIi1lanD {
    public static double I1O1I1LaNd = 1.0;
    public static llIIi1lanD OOOIilanD;
    public static llIIi1lanD lI00OlAND;
    public float lli0OiIlAND;
    private static boolean li0iOILAND;

    public static void I1O1I1LaNd(llIIi1lanD llIIi1lanD2) {
        OOOIilanD = lI00OlAND;
        lI00OlAND = llIIi1lanD2;
        llIIi1lanD.lI00OlAND();
    }

    public static boolean I1O1I1LaNd() {
        if (lIOlLaND.li0iOILAND() != null && lIOlLaND.li0iOILAND().get() != null && !((lIOlLaND)lIOlLaND.li0iOILAND().get()).O1il1llOLANd()) {
            return true;
        }
        return !IO0lll1lanD.lI00OlAND();
    }

    public static boolean OOOIilanD() {
        return li0iOILAND;
    }

    public static void lI00OlAND() {
        li0iOILAND = false;
    }

    public void lli0OiIlAND() {
    }

    public void I1O1I1LaNd(char c2, int n2) {
    }

    public void li0iOILAND() {
    }

    public void O1il1llOLANd() {
        li0iOILAND = true;
    }

    public static float I1O1I1LaNd(boolean bl, float f2, float f3) {
        if (bl) {
            if (f2 < 1.0f) {
                f2 += f3;
            }
        } else if (f2 > 0.0f) {
            f2 -= f3;
        }
        return f2;
    }
}

