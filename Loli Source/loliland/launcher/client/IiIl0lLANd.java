/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.TimerTask;
import loliland.launcher.XLauncher;
import loliland.launcher.client.OIi01Ol1LaNd;
import loliland.launcher.client.OOOOllANd;
import loliland.launcher.client.liIlILAnD;
import loliland.launcher.client.llI0OlAND;

final class IiIl0lLANd
extends TimerTask {
    final /* synthetic */ int[] I1O1I1LaNd;

    IiIl0lLANd(int[] arrn) {
        this.I1O1I1LaNd = arrn;
    }

    @Override
    public void run() {
        if (this.I1O1I1LaNd[0] != 0 && !llI0OlAND.lli0OiIlAND()) {
            return;
        }
        for (OOOOllANd oOOOllANd : XLauncher.getClientManager().lI00OlAND().values()) {
            for (liIlILAnD liIlILAnD2 : oOOOllANd.l0iIlIO1laNd()) {
                OIi01Ol1LaNd oIi01Ol1LaNd = liIlILAnD2.lli0OiIlAND();
                OIi01Ol1LaNd.lli011lLANd().execute(() -> {
                    oIi01Ol1LaNd.I1O1I1LaNd();
                    if (oIi01Ol1LaNd.iilIi1laND()) {
                        try {
                            liIlILAnD2.I1O1I1LaNd(Integer.parseInt(oIi01Ol1LaNd.Oill1LAnD()));
                            liIlILAnD2.OOOIilanD(Integer.parseInt(oIi01Ol1LaNd.lIOILand()));
                        }
                        catch (Exception exception) {
                            liIlILAnD2.I1O1I1LaNd(-1);
                        }
                    } else {
                        liIlILAnD2.I1O1I1LaNd(-1);
                    }
                });
            }
        }
        this.I1O1I1LaNd[0] = this.I1O1I1LaNd[0] + 1;
    }
}

