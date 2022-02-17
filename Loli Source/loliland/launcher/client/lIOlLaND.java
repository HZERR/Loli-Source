/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.concurrent.atomic.AtomicReference;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.llIIi1lanD;

public class lIOlLaND {
    private static AtomicReference I1O1I1LaNd;
    private long OOOIilanD = -1L;
    private String lI00OlAND;
    private String[] lli0OiIlAND;
    private float li0iOILAND;
    private boolean O1il1llOLANd;

    public static void I1O1I1LaNd(int n2, String string, String ... arrstring) {
        if (I1O1I1LaNd == null) {
            I1O1I1LaNd = new AtomicReference();
        }
        lIOlLaND lIOlLaND2 = new lIOlLaND();
        lIOlLaND2.lI00OlAND = string;
        lIOlLaND2.lli0OiIlAND = arrstring;
        lIOlLaND2.OOOIilanD = n2 == -1 ? (long)n2 : System.currentTimeMillis() + (long)n2;
        if (I1O1I1LaNd.get() != null) {
            lIOlLaND2.li0iOILAND = ((lIOlLaND)lIOlLaND.I1O1I1LaNd.get()).li0iOILAND;
        }
        I1O1I1LaNd.set(lIOlLaND2);
    }

    public static void I1O1I1LaNd(String string, String ... arrstring) {
        lIOlLaND.I1O1I1LaNd(-1, string, arrstring);
    }

    public static void I1O1I1LaNd() {
        if (I1O1I1LaNd == null) {
            I1O1I1LaNd = new AtomicReference();
        }
        if (I1O1I1LaNd.get() != null) {
            ((lIOlLaND)I1O1I1LaNd.get()).lli0OiIlAND();
        }
    }

    public static void OOOIilanD() {
        if (I1O1I1LaNd == null) {
            I1O1I1LaNd = new AtomicReference();
        }
        if (I1O1I1LaNd.get() != null) {
            I1O1I1LaNd.set(null);
        }
    }

    public void lI00OlAND() {
        if (this.OOOIilanD != -1L && System.currentTimeMillis() > this.OOOIilanD) {
            this.OOOIilanD = -1L;
            this.O1il1llOLANd = true;
        }
        if (this.O1il1llOLANd) {
            if (this.li0iOILAND > 0.0f) {
                this.li0iOILAND -= 0.0325f;
            } else {
                I1O1I1LaNd.set(null);
            }
        } else if (this.li0iOILAND < 1.0f) {
            this.li0iOILAND += 0.0625f;
        }
        int n2 = 495;
        int n3 = 177;
        double d2 = 513.5 - (double)n2 / 2.0;
        double d3 = 329.5 - (double)n3 / 2.0;
        lI0il11LaND.OOOIilanD(0.0, 0.0, 1027.0, 659.0, 0, this.li0iOILAND * 0.35f);
        lI0il11LaND.I1O1I1LaNd(d2, d3, llIIi1lanD.I1O1I1LaNd, -1, this.li0iOILAND, "modal_window.png");
        iO11lland.lli0OiIlAND.OOOIilanD(this.lI00OlAND, 506.5, d3 + 40.0, 17, 1.5, -1, this.li0iOILAND);
        int n4 = 0;
        for (String string : this.lli0OiIlAND) {
            iO11lland.I1O1I1LaNd.OOOIilanD(string, 506.5, d3 + (double)n4 + 77.0, 14, 1.5, -1, this.li0iOILAND);
            n4 += 22;
        }
    }

    public void lli0OiIlAND() {
        this.O1il1llOLANd = true;
    }

    public static AtomicReference li0iOILAND() {
        return I1O1I1LaNd;
    }

    public boolean O1il1llOLANd() {
        return this.O1il1llOLANd;
    }
}

