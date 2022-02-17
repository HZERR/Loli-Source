/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Objects;
import loliland.launcher.client.i1i1OlLaNd;
import loliland.launcher.client.l000O1ILaND;

class l0liLaND
extends Thread {
    private l000O1ILaND I1O1I1LaNd;
    private i1i1OlLaNd OOOIilanD;

    public l0liLaND(l000O1ILaND l000O1ILaND2, i1i1OlLaNd i1i1OlLaNd2) {
        this.I1O1I1LaNd = l000O1ILaND2;
        this.OOOIilanD = i1i1OlLaNd2;
    }

    @Override
    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (this.I1O1I1LaNd.I1O1I1LaNd()) {
                this.I1O1I1LaNd.OOOIilanD();
            }
        }));
        if (Objects.nonNull(this.OOOIilanD)) {
            this.OOOIilanD.I1O1I1LaNd(this.I1O1I1LaNd);
        }
        while (this.I1O1I1LaNd.I1O1I1LaNd()) {
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }
        if (Objects.nonNull(this.OOOIilanD)) {
            this.OOOIilanD.OOOIilanD(this.I1O1I1LaNd);
        }
    }
}

