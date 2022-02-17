/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.XLauncher;
import loliland.launcher.client.O0IlOiILAnD;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class OOOiO0lANd {
    private long I1O1I1LaNd;
    private long OOOIilanD;
    private long lI00OlAND;
    private double lli0OiIlAND;
    private double li0iOILAND;
    private boolean O1il1llOLANd = false;

    public OOOiO0lANd(long l2, long l3) {
        this.OOOIilanD = l2;
        this.lI00OlAND = l3;
        this.I1O1I1LaNd = XLauncher.getSettingsManager().li0iOILAND();
        this.I1O1I1LaNd();
    }

    public void I1O1I1LaNd(boolean bl, float f2, double d2, double d3) {
        double d4;
        int n2;
        double d5 = 417.0;
        double d6 = 9.0;
        lI0il11LaND.I1O1I1LaNd(d2, d3, f2, "settings/progress.png");
        int n3 = (int)Math.round((double)this.lI00OlAND / 1024.0);
        if (n3 > 8) {
            n3 = 8;
        }
        iO11lland.I1O1I1LaNd.OOOIilanD("1 \u0433\u0431", d2, d3 + 15.0, 16, 1.0, -1, f2 * 0.8f);
        iO11lland.I1O1I1LaNd.OOOIilanD(Math.round((double)this.lI00OlAND / 1024.0) + " \u0433\u0431", d2 + d5, d3 + 15.0, 16, 1.0, -1, f2 * 0.8f);
        double d7 = d5 / (double)n3;
        for (n2 = 1; n2 < n3; ++n2) {
            lI0il11LaND.OOOIilanD(d2 + (double)n2 * d7 - 4.0, d3, 2.0, 9.0, 0xAF8FFF, f2 * 0.5f);
            d4 = (double)this.OOOIilanD + (double)(this.lI00OlAND - this.OOOIilanD) * ((double)n2 * d7) / d5;
            if (this.lI00OlAND < 14000L) {
                iO11lland.I1O1I1LaNd.OOOIilanD(String.format("%.2g%n", d4 / 1024.0) + " \u0433\u0431", d2 + (double)n2 * d7 - 2.0, d3 + 15.0, 16, 1.0, -1, f2 * 0.8f);
                continue;
            }
            iO11lland.I1O1I1LaNd.OOOIilanD(Math.round(d4 / 1024.0) + " \u0433\u0431", d2 + (double)n2 * d7 - 2.0, d3 + 15.0, 16, 1.0, -1, f2 * 0.8f);
        }
        GL11.glEnable(3089);
        lI0il11LaND.I1O1I1LaNd((int)d2, (int)d3, (int)this.li0iOILAND, (int)d6);
        lI0il11LaND.I1O1I1LaNd(d2, d3, f2, "settings/progress_full.png");
        GL11.glDisable(3089);
        n2 = bl && (double)O0IlOiILAnD.OOOIilanD() > d2 + this.li0iOILAND - 9.5 && (double)O0IlOiILAnD.OOOIilanD() < d2 + this.li0iOILAND - 9.5 + 19.0 && (double)O0IlOiILAnD.lI00OlAND() > d3 - 5.0 && (double)O0IlOiILAnD.lI00OlAND() < d3 - 5.0 + 19.0 ? 1 : 0;
        lI0il11LaND.I1O1I1LaNd(d2 + this.li0iOILAND - 9.5, d3 - 5.0, f2 * (this.O1il1llOLANd || n2 != 0 ? 0.7f : 1.0f), "settings/slider.png");
        if (n2 != 0 && llIIi1lanD.OOOIilanD()) {
            this.O1il1llOLANd = true;
        }
        if (!Mouse.isButtonDown(0)) {
            this.O1il1llOLANd = false;
        }
        if (this.O1il1llOLANd && ((d4 = (double)O0IlOiILAnD.OOOIilanD() - this.lli0OiIlAND) > 0.0 && (double)O0IlOiILAnD.OOOIilanD() > d2 || d4 < 0.0 && (double)O0IlOiILAnD.OOOIilanD() < d2 + d5)) {
            this.li0iOILAND += d4;
            this.li0iOILAND = Math.max(1.0, this.li0iOILAND);
            this.li0iOILAND = Math.min(d5, this.li0iOILAND);
            this.I1O1I1LaNd = this.OOOIilanD + (long)((double)(this.lI00OlAND - this.OOOIilanD) * this.li0iOILAND / d5);
            this.I1O1I1LaNd = Math.max(this.OOOIilanD, this.I1O1I1LaNd);
            this.I1O1I1LaNd = Math.min(this.lI00OlAND, this.I1O1I1LaNd);
            XLauncher.getSettingsManager().I1O1I1LaNd(this.I1O1I1LaNd);
        }
        this.lli0OiIlAND = O0IlOiILAnD.OOOIilanD();
    }

    public void I1O1I1LaNd() {
        double d2 = 417.0;
        this.li0iOILAND = (double)this.I1O1I1LaNd / (double)this.lI00OlAND * d2;
        this.li0iOILAND = Math.max(1.0, this.li0iOILAND);
        this.li0iOILAND = Math.min(d2, this.li0iOILAND);
    }

    public long OOOIilanD() {
        return this.I1O1I1LaNd;
    }

    public long lI00OlAND() {
        return this.OOOIilanD;
    }

    public long lli0OiIlAND() {
        return this.lI00OlAND;
    }

    public void I1O1I1LaNd(long l2) {
        this.I1O1I1LaNd = l2;
    }

    public void OOOIilanD(long l2) {
        this.OOOIilanD = l2;
    }

    public void lI00OlAND(long l2) {
        this.lI00OlAND = l2;
    }
}

