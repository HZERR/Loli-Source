/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.XLauncher;
import loliland.launcher.client.O0IlOiILAnD;
import loliland.launcher.client.O0li11Land;
import loliland.launcher.client.ii0ii01LanD;
import loliland.launcher.client.l0il0l1iLaNd;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.lI10ilAnd;
import loliland.launcher.client.lIOlLaND;
import loliland.launcher.client.lOO0ii0LAND;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

public class l0liiO0land
extends llIIi1lanD {
    private float li0iOILAND;
    private float O1il1llOLANd;
    private float Oill1LAnD;
    private float lIOILand;
    private O0li11Land lil0liLand;
    private O0li11Land iilIi1laND;
    private boolean lli011lLANd;

    public l0liiO0land() {
        Keyboard.enableRepeatEvents(true);
        this.lil0liLand = new O0li11Land("auth/user_icon", 0, "\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043b\u043e\u0433\u0438\u043d");
        this.iilIi1laND = new O0li11Land("auth/pass_icon", 8, "\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043f\u0430\u0440\u043e\u043b\u044c");
        this.iilIi1laND.I1O1I1LaNd(true);
    }

    @Override
    public void lli0OiIlAND() {
        boolean bl;
        boolean bl2;
        lI0il11LaND.I1O1I1LaNd(0.0, 0.0, 1027.0, 659.0, -1, this.lli0OiIlAND, "bg.png");
        lI0il11LaND.I1O1I1LaNd((513.5 - (double)O0IlOiILAnD.OOOIilanD()) / 60.0, 208.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "anime.png");
        lI0il11LaND.I1O1I1LaNd(308.5, 360.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "auth/auth.png");
        boolean bl3 = bl2 = (double)O0IlOiILAnD.OOOIilanD() > 308.5 && (double)O0IlOiILAnD.OOOIilanD() < 718.5 && O0IlOiILAnD.lI00OlAND() > 360 && O0IlOiILAnD.lI00OlAND() < 470 && XLauncher.getClientManager().OOOIilanD();
        if (bl2 && l0liiO0land.OOOIilanD()) {
            this.Oill1LAnD();
        }
        this.O1il1llOLANd = l0liiO0land.I1O1I1LaNd(bl2, this.O1il1llOLANd, 0.125f);
        if (this.O1il1llOLANd > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(308.5, 360.0, I1O1I1LaNd, -1, this.O1il1llOLANd * this.lli0OiIlAND, "auth/auth_hover.png");
        }
        lI0il11LaND.I1O1I1LaNd(440.0, 565.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "auth/register.png");
        boolean bl4 = (double)O0IlOiILAnD.OOOIilanD() > 440.0 && (double)O0IlOiILAnD.OOOIilanD() < 587.0 && O0IlOiILAnD.lI00OlAND() > 565 && O0IlOiILAnD.lI00OlAND() < 584;
        this.Oill1LAnD = l0liiO0land.I1O1I1LaNd(bl4, this.Oill1LAnD, 0.125f);
        if (this.Oill1LAnD > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(440.0, 565.0, I1O1I1LaNd, -1, this.Oill1LAnD * this.lli0OiIlAND, "auth/register_hover.png");
        }
        lI0il11LaND.I1O1I1LaNd(451.0, 592.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "auth/reset.png");
        boolean bl5 = bl = (double)O0IlOiILAnD.OOOIilanD() > 451.0 && (double)O0IlOiILAnD.OOOIilanD() < 576.0 && O0IlOiILAnD.lI00OlAND() > 592 && O0IlOiILAnD.lI00OlAND() < 611;
        if (bl && l0liiO0land.OOOIilanD()) {
            Sys.openURL("https://loliland.ru/reset");
        }
        this.lIOILand = l0liiO0land.I1O1I1LaNd(bl, this.lIOILand, 0.125f);
        if (this.lIOILand > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(451.0, 592.0, I1O1I1LaNd, -1, this.lIOILand * this.lli0OiIlAND, "auth/reset_hover.png");
        }
        this.lil0liLand.I1O1I1LaNd(this, 376.0, 247.0);
        this.iilIi1laND.I1O1I1LaNd(this, 376.0, 297.0);
        boolean bl6 = O0IlOiILAnD.OOOIilanD() > 661 && O0IlOiILAnD.OOOIilanD() < 699 && O0IlOiILAnD.lI00OlAND() > 300 && O0IlOiILAnD.lI00OlAND() < 337;
        lI0il11LaND.I1O1I1LaNd(661.0, 300.0, I1O1I1LaNd, -1, (bl6 ? 0.8f : 1.0f) * this.lli0OiIlAND, this.iilIi1laND.OOOIilanD() ? "auth/visible.png" : "auth/unvisible.png");
        if (bl6 && l0liiO0land.OOOIilanD()) {
            this.iilIi1laND.I1O1I1LaNd(!this.iilIi1laND.OOOIilanD());
        }
    }

    @Override
    public void I1O1I1LaNd(char c2, int n2) {
        this.lil0liLand.I1O1I1LaNd(c2, n2);
        this.iilIi1laND.I1O1I1LaNd(c2, n2);
    }

    private void Oill1LAnD() {
        if (this.lli011lLANd) {
            return;
        }
        this.lli011lLANd = true;
        lIOlLaND.I1O1I1LaNd("\u041f\u0440\u043e\u0432\u0435\u0440\u043a\u0430 \u0434\u0430\u043d\u043d\u044b\u0445...", "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u043f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435, \u043f\u043e\u043a\u0430 \u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0430", "\u0434\u0430\u043d\u043d\u044b\u0445 \u043d\u0435 \u0431\u0443\u0434\u0435\u0442 \u0437\u0430\u043a\u043e\u043d\u0447\u0435\u043d\u0430");
        lI10ilAnd.OOOIilanD().submit(() -> {
            ii0ii01LanD ii0ii01LanD2 = l0il0l1iLaNd.I1O1I1LaNd(this.lil0liLand.I1O1I1LaNd(), this.iilIi1laND.I1O1I1LaNd());
            System.out.println(ii0ii01LanD2);
            if (!ii0ii01LanD2.lI00OlAND("type")) {
                lIOlLaND.I1O1I1LaNd(1500, "\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430!", "\u041d\u0435\u0442 \u043f\u043e\u0434\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u044f \u043a", "api.loliland.ru");
                this.lli011lLANd = false;
                return;
            }
            String string = ii0ii01LanD2.OOOIilanD("type").Oill1LAnD();
            if (string.equalsIgnoreCase("error")) {
                int n2 = ii0ii01LanD2.OOOIilanD("error").I1O1I1LaNd();
                switch (n2) {
                    case 0: {
                        lIOlLaND.I1O1I1LaNd(1500, "\u041e\u0448\u0438\u0431\u043a\u0430 \u0430\u0432\u0442\u043e\u0440\u0438\u0437\u0430\u0446\u0438\u0438!", "\u041b\u043e\u0433\u0438\u043d \u0438\u043b\u0438 \u043f\u0430\u0440\u043e\u043b\u044c", "\u0432\u0432\u0435\u0434\u0435\u043d \u043d\u0435 \u0432\u0435\u0440\u043d\u043e");
                        break;
                    }
                    case 1: {
                        lIOlLaND.I1O1I1LaNd(1500, "\u0412\u044b \u0437\u0430\u0431\u0430\u043d\u0435\u043d\u044b!", "LoliLand \u0431\u0443\u0434\u0435\u0442", "\u0441\u043a\u0443\u0447\u0430\u0442\u044c :C");
                    }
                }
                this.lli011lLANd = false;
            } else {
                lIOlLaND.I1O1I1LaNd();
                XLauncher.getAuthManager().I1O1I1LaNd(ii0ii01LanD2.OOOIilanD("data").O1il1llOLANd().OOOIilanD("votes").I1O1I1LaNd());
                XLauncher.getAuthManager().I1O1I1LaNd(this.lil0liLand.I1O1I1LaNd(), this.iilIi1laND.I1O1I1LaNd());
                this.lli011lLANd = false;
                lI10ilAnd.I1O1I1LaNd().I1O1I1LaNd(() -> llIIi1lanD.I1O1I1LaNd(new lOO0ii0LAND()));
            }
        });
    }
}

