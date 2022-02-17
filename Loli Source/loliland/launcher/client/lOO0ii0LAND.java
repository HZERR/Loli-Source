/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.XLauncher;
import loliland.launcher.client.IO0lll1lanD;
import loliland.launcher.client.O0IlOiILAnD;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.l0liiO0land;
import loliland.launcher.client.l1lIOlAND;
import loliland.launcher.client.lI0011OLaND;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.lI10ilAnd;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

public class lOO0ii0LAND
extends llIIi1lanD {
    private float li0iOILAND;
    private float O1il1llOLANd;
    private float Oill1LAnD;
    private float lIOILand;
    private float lil0liLand;
    private float iilIi1laND;
    private float lli011lLANd;
    private float l0illAND;

    public lOO0ii0LAND() {
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void lli0OiIlAND() {
        boolean bl;
        boolean bl2;
        boolean bl3;
        boolean bl4;
        boolean bl5;
        boolean bl6;
        boolean bl7;
        lI0il11LaND.I1O1I1LaNd(0.0, 0.0, 1027.0, 659.0, -1, this.lli0OiIlAND, "bg_player.png");
        boolean bl8 = bl7 = llIIi1lanD.I1O1I1LaNd() && O0IlOiILAnD.OOOIilanD() > 24 && O0IlOiILAnD.OOOIilanD() < 42 && O0IlOiILAnD.lI00OlAND() > 30 && O0IlOiILAnD.lI00OlAND() < 48;
        if (bl7 && lOO0ii0LAND.OOOIilanD()) {
            this.Oill1LAnD();
        }
        this.O1il1llOLANd = lOO0ii0LAND.I1O1I1LaNd(bl7, this.O1il1llOLANd, 0.125f);
        lI0il11LaND.I1O1I1LaNd(24.0, 30.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "back.png");
        if (this.O1il1llOLANd > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(24.0, 30.0, I1O1I1LaNd, -1, this.O1il1llOLANd * this.lli0OiIlAND, "back_hover.png");
        }
        boolean bl9 = bl6 = llIIi1lanD.I1O1I1LaNd() && O0IlOiILAnD.OOOIilanD() > 985 && O0IlOiILAnD.OOOIilanD() < 1011 && O0IlOiILAnD.lI00OlAND() > 30 && O0IlOiILAnD.lI00OlAND() < 56;
        if (bl6 && lOO0ii0LAND.OOOIilanD()) {
            IO0lll1lanD.I1O1I1LaNd(true);
        }
        this.Oill1LAnD = lOO0ii0LAND.I1O1I1LaNd(bl6, this.Oill1LAnD, 0.125f);
        lI0il11LaND.I1O1I1LaNd(985.0, 30.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "settings.png");
        if (this.Oill1LAnD > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(985.0, 30.0, I1O1I1LaNd, -1, this.Oill1LAnD * this.lli0OiIlAND, "settings_hover.png");
        }
        lI0il11LaND.I1O1I1LaNd(66.0, 121.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "player/chest.png");
        boolean bl10 = bl5 = llIIi1lanD.I1O1I1LaNd() && O0IlOiILAnD.OOOIilanD() > 66 && O0IlOiILAnD.OOOIilanD() < 538 && O0IlOiILAnD.lI00OlAND() > 121 && O0IlOiILAnD.lI00OlAND() < 538 && XLauncher.getClientManager().OOOIilanD();
        if (bl5 && lOO0ii0LAND.OOOIilanD()) {
            Sys.openURL("https://loliland.ru/votes");
        }
        this.lil0liLand = lOO0ii0LAND.I1O1I1LaNd(bl5, this.lil0liLand, 0.125f);
        if (this.lil0liLand > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(66.0, 121.0, I1O1I1LaNd, -1, this.lil0liLand * this.lli0OiIlAND, "player/chest_hover.png");
        }
        lI0il11LaND.I1O1I1LaNd(55.0, 29.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "player/discord.png");
        boolean bl11 = bl4 = llIIi1lanD.I1O1I1LaNd() && O0IlOiILAnD.OOOIilanD() > 55 && O0IlOiILAnD.OOOIilanD() < 240 && O0IlOiILAnD.lI00OlAND() > 29 && O0IlOiILAnD.lI00OlAND() < 53 && XLauncher.getClientManager().OOOIilanD();
        if (bl4 && lOO0ii0LAND.OOOIilanD()) {
            Sys.openURL("https://discord.gg/hyUep5t");
        }
        this.lli011lLANd = lOO0ii0LAND.I1O1I1LaNd(bl4, this.lli011lLANd, 0.125f);
        if (this.lli011lLANd > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(55.0, 29.0, I1O1I1LaNd, -1, this.lli011lLANd * this.lli0OiIlAND, "player/discord_hover.png");
        }
        lI0il11LaND.I1O1I1LaNd(255.0, 34.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "player/vk.png");
        boolean bl12 = bl3 = llIIi1lanD.I1O1I1LaNd() && O0IlOiILAnD.OOOIilanD() > 255 && O0IlOiILAnD.OOOIilanD() < 431 && O0IlOiILAnD.lI00OlAND() > 34 && O0IlOiILAnD.lI00OlAND() < 54 && XLauncher.getClientManager().OOOIilanD();
        if (bl3 && lOO0ii0LAND.OOOIilanD()) {
            Sys.openURL("https://vk.com/loliland_mc");
        }
        this.l0illAND = lOO0ii0LAND.I1O1I1LaNd(bl3, this.l0illAND, 0.125f);
        if (this.l0illAND > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(255.0, 34.0, I1O1I1LaNd, -1, this.l0illAND * this.lli0OiIlAND, "player/vk_hover.png");
        }
        lI0il11LaND.I1O1I1LaNd(611.0, 522.0, I1O1I1LaNd, -1, this.lli0OiIlAND, "player/start.png");
        boolean bl13 = bl2 = llIIi1lanD.I1O1I1LaNd() && O0IlOiILAnD.OOOIilanD() > 611 && O0IlOiILAnD.OOOIilanD() < 1021 && O0IlOiILAnD.lI00OlAND() > 522 && O0IlOiILAnD.lI00OlAND() < 632 && XLauncher.getClientManager().OOOIilanD();
        if (bl2 && lOO0ii0LAND.OOOIilanD()) {
            lOO0ii0LAND.I1O1I1LaNd(new lI0011OLaND());
        }
        this.iilIi1laND = lOO0ii0LAND.I1O1I1LaNd(bl2, this.iilIi1laND, 0.125f);
        if (this.iilIi1laND > 0.0f) {
            lI0il11LaND.I1O1I1LaNd(611.0, 522.0, I1O1I1LaNd, -1, this.iilIi1laND * this.lli0OiIlAND, "player/start_hover.png");
        }
        String string = "https://api.loliland.ru/body/" + XLauncher.getAuthManager().lI00OlAND() + "";
        lI0il11LaND.I1O1I1LaNd(l1lIOlAND.I1O1I1LaNd(string));
        lI0il11LaND.I1O1I1LaNd(745.0, 155.0, 136.0, 273.0, -1, this.lli0OiIlAND);
        boolean bl14 = bl = llIIi1lanD.I1O1I1LaNd() && O0IlOiILAnD.OOOIilanD() > 745 && O0IlOiILAnD.OOOIilanD() < 881 && O0IlOiILAnD.lI00OlAND() > 155 && O0IlOiILAnD.lI00OlAND() < 428;
        if (!bl || lOO0ii0LAND.OOOIilanD()) {
            // empty if block
        }
        this.lIOILand = lOO0ii0LAND.I1O1I1LaNd(bl, this.lIOILand, 0.125f);
        if (this.lIOILand > 0.0f) {
            string = "https://api.loliland.ru/back/" + XLauncher.getAuthManager().lI00OlAND() + "";
            lI0il11LaND.I1O1I1LaNd(l1lIOlAND.I1O1I1LaNd(string));
            lI0il11LaND.I1O1I1LaNd(745.0, 155.0, 136.0, 273.0, -1, this.lIOILand * this.lli0OiIlAND);
        }
        iO11lland.I1O1I1LaNd.OOOIilanD("\u0414\u043e\u0431\u0440\u043e \u043f\u043e\u0436\u0430\u043b\u043e\u0432\u0430\u0442\u044c,", 813.0, 452.0, 17, 1.3, -1, 1.0f);
        iO11lland.lli0OiIlAND.OOOIilanD(XLauncher.getAuthManager().lI00OlAND(), 813.0, 480.0, 25, 1.3, -1, 1.0f);
        int n2 = XLauncher.getAuthManager().lli0OiIlAND();
        if (n2 > 0) {
            iO11lland.lli0OiIlAND.I1O1I1LaNd("\u0412 \u044d\u0442\u043e\u043c \u043c\u0435\u0441\u044f\u0446\u0435 \u0412\u044b \u043f\u0440\u043e\u0433\u043e\u043b\u043e\u0441\u043e\u0432\u0430\u043b\u0438 " + this.I1O1I1LaNd(XLauncher.getAuthManager().lli0OiIlAND(), "\u0440\u0430\u0437", "\u0440\u0430\u0437\u0430", "\u0440\u0430\u0437"), 22.0, 580.0, 84, 0.25, -1, 0.7f);
        } else {
            iO11lland.lli0OiIlAND.I1O1I1LaNd("\u041c\u044b \u0436\u0434\u0435\u043c \u0442\u0432\u043e\u0435\u0433\u043e \u0433\u043e\u043b\u043e\u0441\u0430 :3 ", 22.0, 580.0, 84, 0.25, -1, 0.7f);
        }
        iO11lland.OOOIilanD.I1O1I1LaNd("\u0412 \u043a\u043e\u043d\u0446\u0435 \u043c\u0435\u0441\u044f\u0446\u0430 \u0441\u0430\u043c\u044b\u043c \u0430\u043a\u0442\u0438\u0432\u043d\u044b\u043c \u0433\u043e\u043b\u043e\u0441\u0443\u044e\u0449\u0438\u043c \u0434\u043e\u0441\u0442\u0430\u043d\u0443\u0442\u0441\u044f \u043f\u0440\u0438\u0437\u044b!", 22.0, 605.0, 76, 0.25, -1, 0.7f);
    }

    String I1O1I1LaNd(int n2, String ... arrstring) {
        int[] arrn = new int[]{2, 0, 1, 1, 1, 2};
        return n2 + " " + arrstring[n2 % 100 > 4 && n2 % 100 < 20 ? 2 : arrn[Math.min(n2 % 10, 5)]];
    }

    private void Oill1LAnD() {
        llIIi1lanD.I1O1I1LaNd(new l0liiO0land());
        lI10ilAnd.OOOIilanD().submit(() -> XLauncher.getAuthManager().OOOIilanD());
    }
}

