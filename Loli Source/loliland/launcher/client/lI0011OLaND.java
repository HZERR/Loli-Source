/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.LinkedList;
import loliland.launcher.XLauncher;
import loliland.launcher.client.IO0lll1lanD;
import loliland.launcher.client.O0IlOiILAnD;
import loliland.launcher.client.OOOOllANd;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.lOO0ii0LAND;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.input.Keyboard;

public class lI0011OLaND
extends llIIi1lanD {
    private float O1il1llOLANd;
    private float Oill1LAnD;
    private float lIOILand;
    public static LinkedList li0iOILAND;

    @Override
    public void lli0OiIlAND() {
        boolean bl;
        boolean bl2;
        lI0il11LaND.I1O1I1LaNd(0.0, 0.0, 1027.0, 659.0, -1, this.lli0OiIlAND, "bg.png");
        if (Keyboard.isKeyDown(42) && Keyboard.isKeyDown(29)) {
            iO11lland.I1O1I1LaNd.OOOIilanD(XLauncher.getBit() + " bit, " + XLauncher.getSettingsManager().li0iOILAND() + " MBytes", 10.0, 624.0, 20, 1.0, -1);
        }
        boolean bl3 = bl2 = llIIi1lanD.I1O1I1LaNd() && O0IlOiILAnD.OOOIilanD() > 24 && O0IlOiILAnD.OOOIilanD() < 42 && O0IlOiILAnD.lI00OlAND() > 30 && O0IlOiILAnD.lI00OlAND() < 48;
        if (bl2 && lI0011OLaND.OOOIilanD()) {
            llIIi1lanD.I1O1I1LaNd(new lOO0ii0LAND());
        }
        this.Oill1LAnD = lI0011OLaND.I1O1I1LaNd(bl2, this.Oill1LAnD, 0.125f);
        lI0il11LaND.I1O1I1LaNd(24.0, 30.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "back.png");
        if (this.Oill1LAnD > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(24.0, 30.0, I1O1I1LaNd, -1, this.Oill1LAnD * this.lli0OiIlAND, "back_hover.png");
        }
        boolean bl4 = bl = llIIi1lanD.I1O1I1LaNd() && O0IlOiILAnD.OOOIilanD() > 985 && O0IlOiILAnD.OOOIilanD() < 1011 && O0IlOiILAnD.lI00OlAND() > 30 && O0IlOiILAnD.lI00OlAND() < 56;
        if (bl && lI0011OLaND.OOOIilanD()) {
            IO0lll1lanD.I1O1I1LaNd(true);
        }
        this.lIOILand = lI0011OLaND.I1O1I1LaNd(bl, this.lIOILand, 0.125f);
        lI0il11LaND.I1O1I1LaNd(985.0, 30.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "settings.png");
        if (this.lIOILand > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(985.0, 30.0, I1O1I1LaNd, -1, this.lIOILand * this.lli0OiIlAND, "settings_hover.png");
        }
        double d2 = 20.0;
        double d3 = 160.0;
        double d4 = 0.0;
        for (LinkedList linkedList : li0iOILAND) {
            double d5 = 966.0;
            double d6 = (d5 - (double)(linkedList.size() * 322)) / 2.0;
            for (OOOOllANd oOOOllANd : linkedList) {
                oOOOllANd.I1O1I1LaNd(this, d2 + d6, d3 + d4);
                d6 += 322.0;
            }
            d4 += 90.0;
        }
    }
}

