/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.XLauncher;
import loliland.launcher.client.O0IlOiILAnD;
import loliland.launcher.client.OOOiO0lANd;
import loliland.launcher.client.i1iiOOlanD;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.lI10ilAnd;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.opengl.GL11;

public final class IO0lll1lanD {
    private static OOOiO0lANd I1O1I1LaNd = new OOOiO0lANd(1024L, i1iiOOlanD.lli0OiIlAND());
    private static boolean OOOIilanD;
    private static float lI00OlAND;
    private static float lli0OiIlAND;
    private static boolean li0iOILAND;
    private static float O1il1llOLANd;

    public static void I1O1I1LaNd() {
        boolean bl;
        IO0lll1lanD.OOOIilanD();
        lI0il11LaND.OOOIilanD(0.0, 0.0, 1027.0, 659.0, 0, lI00OlAND * 0.5f);
        GL11.glTranslated(630.0f - lli0OiIlAND, 0.0, 0.0);
        lI0il11LaND.I1O1I1LaNd(397.0, 0.0, lI00OlAND, "settings/bg.png");
        lI0il11LaND.I1O1I1LaNd(850.0, 21.0, lI00OlAND, "settings/logo.png");
        boolean bl2 = li0iOILAND = O0IlOiILAnD.OOOIilanD() > 414 && O0IlOiILAnD.OOOIilanD() < 449 && O0IlOiILAnD.lI00OlAND() > 14 && O0IlOiILAnD.lI00OlAND() < 49;
        if (li0iOILAND && llIIi1lanD.OOOIilanD()) {
            OOOIilanD = false;
            lI10ilAnd.OOOIilanD().submit(() -> XLauncher.getSettingsManager().OOOIilanD());
        }
        lI0il11LaND.I1O1I1LaNd(414.0, 14.0, lI00OlAND, "settings/back.png");
        if (O1il1llOLANd > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(414.0, 14.0, O1il1llOLANd * lI00OlAND, "settings/back_hover.png");
        }
        iO11lland.lli0OiIlAND.I1O1I1LaNd("\u041e\u043f\u0435\u0440\u0430\u0442\u0438\u0432\u043d\u0430\u044f \u043f\u0430\u043c\u044f\u0442\u044c", 477.0, 126.0, 17, 1.4, -1, lI00OlAND, false);
        iO11lland.I1O1I1LaNd.I1O1I1LaNd("\u0414\u043b\u044f \u0441\u0442\u0430\u0431\u0438\u043b\u044c\u043d\u043e\u0439 \u0440\u0430\u0431\u043e\u0442\u044b \u0438\u0433\u0440\u044b \u0440\u0435\u043a\u043e\u043c\u0435\u043d\u0434\u0443\u0435\u0442\u0441\u044f \u0432\u044b\u0434\u0435\u043b\u0438\u0442\u044c", 486.0, 151.0, 13, 1.4, -1, lI00OlAND * 0.75f, false);
        iO11lland.I1O1I1LaNd.I1O1I1LaNd("\u043d\u0435 \u043c\u0435\u043d\u0435\u0435 1 \u0433\u0431 \u043e\u043f\u0435\u0440\u0430\u0442\u0438\u0432\u043d\u043e\u0439 \u043f\u0430\u043c\u044f\u0442\u0438.", 486.0, 172.0, 13, 1.4, -1, lI00OlAND * 0.75f, false);
        boolean bl3 = bl = O0IlOiILAnD.OOOIilanD() > 477 && O0IlOiILAnD.OOOIilanD() < 650 && O0IlOiILAnD.lI00OlAND() > 207 && O0IlOiILAnD.lI00OlAND() < 235;
        if (bl && llIIi1lanD.OOOIilanD()) {
            i1iiOOlanD i1iiOOlanD2;
            i1iiOOlanD2.I1O1I1LaNd(!(i1iiOOlanD2 = XLauncher.getSettingsManager()).O1il1llOLANd());
            if (i1iiOOlanD2.O1il1llOLANd()) {
                i1iiOOlanD2.I1O1I1LaNd(i1iiOOlanD.lI00OlAND());
                I1O1I1LaNd.I1O1I1LaNd(i1iiOOlanD2.li0iOILAND());
                I1O1I1LaNd.I1O1I1LaNd();
            }
        }
        lI0il11LaND.I1O1I1LaNd(477.0, 207.0, lI00OlAND, "settings/automatic_memory_" + XLauncher.getSettingsManager().O1il1llOLANd() + ".png");
        iO11lland.lli0OiIlAND.OOOIilanD(I1O1I1LaNd.OOOIilanD() + " \u043c\u0431", 685.5, 237.0, 18, 1.0, -1, XLauncher.getSettingsManager().O1il1llOLANd() ? lI00OlAND * 0.5f : lI00OlAND);
        I1O1I1LaNd.I1O1I1LaNd(!XLauncher.getSettingsManager().O1il1llOLANd(), XLauncher.getSettingsManager().O1il1llOLANd() ? lI00OlAND * 0.5f : lI00OlAND, 476.0, 261.0);
        GL11.glTranslated(-(630.0f - lli0OiIlAND), 0.0, 0.0);
    }

    public static void OOOIilanD() {
        if (OOOIilanD) {
            if (lI00OlAND < 1.0f) {
                lI00OlAND += 0.0625f;
            }
            if (lli0OiIlAND < 630.0f) {
                lli0OiIlAND += 30.0f;
            }
        } else if (lI00OlAND > 0.0f) {
            lI00OlAND -= 0.0625f;
        }
        if (!OOOIilanD && lli0OiIlAND > 0.0f) {
            lli0OiIlAND -= 30.0f;
        }
        if (li0iOILAND) {
            if (O1il1llOLANd < 1.0f) {
                O1il1llOLANd += 0.0625f;
            }
        } else if (O1il1llOLANd > 0.0f) {
            O1il1llOLANd -= 0.0625f;
        }
    }

    private IO0lll1lanD() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean lI00OlAND() {
        return OOOIilanD;
    }

    public static void I1O1I1LaNd(boolean bl) {
        OOOIilanD = bl;
    }
}

