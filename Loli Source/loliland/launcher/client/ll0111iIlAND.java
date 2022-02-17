/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.O0IlOiILAnD;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.l0OO0lllAnd;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.opengl.GL11;

public class ll0111iIlAND
extends llIIi1lanD {
    private float li0iOILAND;
    private float O1il1llOLANd;
    private l0OO0lllAnd Oill1LAnD;

    public ll0111iIlAND(l0OO0lllAnd l0OO0lllAnd2) {
        this.Oill1LAnD = l0OO0lllAnd2;
    }

    @Override
    public void lli0OiIlAND() {
        boolean bl;
        long l2;
        lI0il11LaND.I1O1I1LaNd(0.0, 0.0, 1027.0, 659.0, -1, this.lli0OiIlAND, "bg.png");
        long l3 = this.Oill1LAnD == null ? 10L : this.Oill1LAnD.lil0liLand().get();
        long l4 = l2 = this.Oill1LAnD == null ? 40L : this.Oill1LAnD.lIOILand().get();
        if (l3 > l2) {
            l3 = l2;
        }
        int n2 = (int)Math.max(0.0, Math.min(100.0, Math.ceil((double)(l3 * 100L) / (double)l2)));
        iO11lland.I1O1I1LaNd.OOOIilanD("\u0417\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0444\u0430\u0439\u043b\u043e\u0432 \u0438\u0433\u0440\u044b", 513.5, 260.0, 20, 1.3, -1, 1.0f);
        iO11lland.lli0OiIlAND.OOOIilanD(n2 + "%", 513.5, 338.0, 22, 1.3, -1, 1.0f);
        lI0il11LaND.I1O1I1LaNd(330.0, 310.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "load/progress.png");
        int n3 = 368;
        double d2 = (int)Math.max(1.0, Math.min((double)n3, Math.ceil((double)(l3 * (long)n3) / (double)l2)));
        GL11.glEnable(3089);
        lI0il11LaND.I1O1I1LaNd(330, 310, (int)d2, 10);
        lI0il11LaND.I1O1I1LaNd(330.0, 310.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "load/progress_full.png");
        GL11.glDisable(3089);
        lI0il11LaND.I1O1I1LaNd(455.0, 555.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "load/stop.png");
        boolean bl2 = bl = O0IlOiILAnD.OOOIilanD() > 455 && O0IlOiILAnD.OOOIilanD() < 573 && O0IlOiILAnD.lI00OlAND() > 555 && O0IlOiILAnD.lI00OlAND() < 586;
        if (bl && ll0111iIlAND.OOOIilanD() && this.Oill1LAnD != null) {
            this.Oill1LAnD.O1il1llOLANd();
        }
        this.O1il1llOLANd = ll0111iIlAND.I1O1I1LaNd(bl, this.O1il1llOLANd, 0.125f);
        if (this.O1il1llOLANd > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(455.0, 555.0, I1O1I1LaNd, -1, this.O1il1llOLANd * this.lli0OiIlAND, "load/stop_hover.png");
        }
    }
}

