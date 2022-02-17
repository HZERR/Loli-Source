/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import loliland.launcher.client.O0IlOiILAnD;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.l0liiO0land;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class O0li11Land {
    private boolean I1O1I1LaNd;
    private float OOOIilanD = 0.0f;
    private double lI00OlAND = 0.0;
    private List lli0OiIlAND = new LinkedList();
    private AtomicReference li0iOILAND = new AtomicReference();
    private boolean O1il1llOLANd;
    private String Oill1LAnD;
    private String lIOILand;
    private int lil0liLand;
    private int iilIi1laND;

    public O0li11Land(String string, int n2, String string2) {
        this.lIOILand = string;
        this.lil0liLand = n2;
        this.Oill1LAnD = string2;
    }

    public String I1O1I1LaNd() {
        this.li0iOILAND.set("");
        this.lli0OiIlAND.forEach(c2 -> this.li0iOILAND.set(this.li0iOILAND + c2.toString()));
        return (String)this.li0iOILAND.get();
    }

    public void I1O1I1LaNd(l0liiO0land l0liiO0land2, double d2, double d3) {
        Object object;
        int n2;
        boolean bl = System.currentTimeMillis() % 1000L > 500L;
        boolean bl2 = (double)O0IlOiILAnD.OOOIilanD() > d2 && (double)O0IlOiILAnD.OOOIilanD() < d2 + 279.0 && (double)O0IlOiILAnD.lI00OlAND() > d3 && (double)O0IlOiILAnD.lI00OlAND() < d3 + 43.0;
        if (l0liiO0land.OOOIilanD()) {
            this.I1O1I1LaNd = bl2;
        }
        if (this.I1O1I1LaNd || !this.lli0OiIlAND.isEmpty()) {
            if (this.lI00OlAND < 280.0) {
                this.lI00OlAND += 2.0;
            }
        } else if (this.lI00OlAND > 2.0) {
            this.lI00OlAND -= 2.0;
        }
        if (this.I1O1I1LaNd || !this.lli0OiIlAND.isEmpty()) {
            if (this.OOOIilanD < 1.0f) {
                this.OOOIilanD += 0.015625f;
            }
        } else {
            if (this.OOOIilanD > 0.0f) {
                this.OOOIilanD -= 0.015625f;
            }
            if (this.OOOIilanD == 0.0f) {
                this.lI00OlAND = 0.0;
            }
        }
        lI0il11LaND.I1O1I1LaNd(d2, d3, llIIi1lanD.I1O1I1LaNd, -1, l0liiO0land2.lli0OiIlAND, "input.png");
        lI0il11LaND.I1O1I1LaNd(d2 + 13.0, d3 + 43.0, llIIi1lanD.I1O1I1LaNd, -1, l0liiO0land2.lli0OiIlAND, "input_line.png");
        lI0il11LaND.I1O1I1LaNd(d2 + 16.0, d3 + 8.0 + (double)this.lil0liLand, llIIi1lanD.I1O1I1LaNd, -1, l0liiO0land2.lli0OiIlAND, this.lIOILand + ".png");
        if (this.OOOIilanD > 0.0f) {
            GL11.glEnable(3089);
            lI0il11LaND.I1O1I1LaNd((int)d2, (int)d3, (int)this.lI00OlAND, (int)(80.0 * llIIi1lanD.I1O1I1LaNd));
            lI0il11LaND.I1O1I1LaNd(d2 + 13.0, d3 + 43.0, llIIi1lanD.I1O1I1LaNd, -1, this.OOOIilanD * l0liiO0land2.lli0OiIlAND, "input_line_hover.png");
            lI0il11LaND.I1O1I1LaNd(d2 + 16.0, d3 + 8.0 + (double)this.lil0liLand, llIIi1lanD.I1O1I1LaNd, -1, this.OOOIilanD * l0liiO0land2.lli0OiIlAND, this.lIOILand + "_hover.png");
            GL11.glDisable(3089);
        }
        int n3 = 17;
        double d4 = 1.5;
        double d5 = d2 + 44.0;
        double d6 = d3 + 8.0;
        GL11.glEnable(3089);
        lI0il11LaND.I1O1I1LaNd((int)d5, (int)d6, 220, (int)(80.0 * llIIi1lanD.I1O1I1LaNd));
        double d7 = 0.0;
        String string = "*";
        for (n2 = 0; n2 < this.lli0OiIlAND.size(); ++n2) {
            Character c2 = (Character)this.lli0OiIlAND.get(n2);
            if (n2 >= this.iilIi1laND) continue;
            object = this.O1il1llOLANd ? string : String.valueOf(c2);
            d7 += iO11lland.I1O1I1LaNd.I1O1I1LaNd((String)object, n3, d4);
        }
        if (d7 > 210.0) {
            d5 -= d7 - 210.0;
        }
        if (this.lli0OiIlAND.isEmpty()) {
            iO11lland.I1O1I1LaNd.I1O1I1LaNd(this.Oill1LAnD, d5, d6, n3, d4, -1, 0.25f * l0liiO0land2.lli0OiIlAND, false);
        }
        if (this.I1O1I1LaNd || !this.lli0OiIlAND.isEmpty()) {
            String string2;
            int n4;
            d7 = 0.0;
            n2 = 0;
            for (n4 = 0; n4 < this.lli0OiIlAND.size(); ++n4) {
                object = (Character)this.lli0OiIlAND.get(n4);
                string2 = this.O1il1llOLANd ? string : String.valueOf(object);
                iO11lland.I1O1I1LaNd.I1O1I1LaNd(string2, d5 + d7, d6, n3, d4, -1, l0liiO0land2.lli0OiIlAND);
                double d8 = iO11lland.I1O1I1LaNd.I1O1I1LaNd(string2, n3, d4);
                if (l0liiO0land.OOOIilanD() && (double)O0IlOiILAnD.OOOIilanD() > d5 + d7 && (double)O0IlOiILAnD.OOOIilanD() < d5 + d7 + d8 && (double)O0IlOiILAnD.lI00OlAND() >= d6 && (double)O0IlOiILAnD.lI00OlAND() <= d6 + 60.0) {
                    this.iilIi1laND = n4;
                    n2 = 1;
                }
                d7 += d8;
            }
            if (this.I1O1I1LaNd) {
                if (l0liiO0land.OOOIilanD() && n2 == 0) {
                    this.iilIi1laND = this.lli0OiIlAND.size();
                }
            }
            if (bl && this.I1O1I1LaNd) {
                d7 = 0.0;
                for (n4 = 0; n4 < this.lli0OiIlAND.size(); ++n4) {
                    object = (Character)this.lli0OiIlAND.get(n4);
                    if (n4 >= this.iilIi1laND) continue;
                    string2 = this.O1il1llOLANd ? string : String.valueOf(object);
                    d7 += iO11lland.I1O1I1LaNd.I1O1I1LaNd(string2, n3, d4);
                }
                lI0il11LaND.OOOIilanD(d5 + d7 + 3.0, d6, 2.0, 26.0, -1, 0.9f * l0liiO0land2.lli0OiIlAND);
                lI0il11LaND.OOOIilanD(d5 + d7 + 3.0 + 1.0, d6, 1.0, 26.0, Color.black.getRGB(), 0.7f * l0liiO0land2.lli0OiIlAND);
            }
        }
        GL11.glDisable(3089);
    }

    public void I1O1I1LaNd(char c2, int n2) {
        if (!this.I1O1I1LaNd) {
            return;
        }
        if (n2 == 14) {
            if (!this.lli0OiIlAND.isEmpty() && this.iilIi1laND - 1 >= 0 && this.iilIi1laND - 1 <= this.lli0OiIlAND.size() - 1) {
                this.lli0OiIlAND.remove(this.iilIi1laND - 1);
                --this.iilIi1laND;
            }
            return;
        }
        if (n2 == 211) {
            if (!this.lli0OiIlAND.isEmpty() && this.iilIi1laND <= this.lli0OiIlAND.size() - 1) {
                this.lli0OiIlAND.remove(this.iilIi1laND);
            }
            return;
        }
        if (n2 == 205) {
            ++this.iilIi1laND;
            if (this.iilIi1laND > this.lli0OiIlAND.size()) {
                this.iilIi1laND = this.lli0OiIlAND.size();
            }
            return;
        }
        if (n2 == 203) {
            --this.iilIi1laND;
            if (this.iilIi1laND < 0) {
                this.iilIi1laND = 0;
            }
            return;
        }
        if (this.lli0OiIlAND() && n2 == 47) {
            this.I1O1I1LaNd(O0li11Land.lI00OlAND());
            return;
        }
        if (c2 != '\u00a7' && c2 >= ' ' && c2 != '\u007f') {
            this.lli0OiIlAND.add(this.iilIi1laND, Character.valueOf(c2));
            ++this.iilIi1laND;
        }
    }

    private void I1O1I1LaNd(String string) {
        string.chars().forEach(n2 -> {
            this.lli0OiIlAND.add(this.iilIi1laND, Character.valueOf((char)n2));
            ++this.iilIi1laND;
        });
    }

    private static String lI00OlAND() {
        try {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String)transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return "";
    }

    private boolean lli0OiIlAND() {
        return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    public void I1O1I1LaNd(boolean bl) {
        this.O1il1llOLANd = bl;
    }

    public boolean OOOIilanD() {
        return this.O1il1llOLANd;
    }
}

