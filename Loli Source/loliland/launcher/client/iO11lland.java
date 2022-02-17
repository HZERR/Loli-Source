/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import loliland.launcher.client.i1O1laNd;
import loliland.launcher.client.il1OOilAnD;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.liililO0LAnD;
import org.lwjgl.opengl.GL11;

public enum iO11lland {
    I1O1I1LaNd("/assets/fonts/Ubuntu.ttf", liililO0LAnD.I1O1I1LaNd),
    OOOIilanD("/assets/fonts/Ubuntu-Italic.ttf", liililO0LAnD.lI00OlAND),
    lI00OlAND("/assets/fonts/Ubuntu-BoldItalic.ttf", liililO0LAnD.lli0OiIlAND),
    lli0OiIlAND("/assets/fonts/Ubuntu-Bold.ttf", liililO0LAnD.OOOIilanD);

    private Font li0iOILAND;
    private i1O1laNd O1il1llOLANd;

    /*
     * WARNING - void declaration
     */
    private iO11lland() {
        void var4_2;
        void var3_1;
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        boolean bl = false;
        boolean bl2 = false;
        try {
            this.li0iOILAND = Font.createFont(0, ((Object)((Object)this)).getClass().getResourceAsStream((String)var3_1));
            bl2 = true;
        }
        catch (FontFormatException fontFormatException) {
            bl = true;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        if (bl) {
            try {
                this.li0iOILAND = Font.createFont(1, ((Object)((Object)this)).getClass().getResourceAsStream((String)var3_1));
                bl2 = true;
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (bl2) {
            boolean bl3 = graphicsEnvironment.registerFont(this.li0iOILAND);
            if (bl3) {
                System.out.println("Registered the external font: " + this.li0iOILAND.getFontName() + (bl ? " (Type1)" : " (TrueType)"));
            } else {
                System.out.println("Already exists in the system: " + this.li0iOILAND.getFontName() + (bl ? " (Type1)" : " (TrueType)"));
            }
        } else {
            System.out.println("Skipped " + (String)var3_1 + ". Could not recognize it as a font file.");
        }
        this.O1il1llOLANd = new i1O1laNd(this.li0iOILAND, (liililO0LAnD)var4_2);
    }

    public static void I1O1I1LaNd() {
        for (iO11lland iO11lland2 : iO11lland.values()) {
            iO11lland2.O1il1llOLANd.I1O1I1LaNd();
        }
    }

    public void OOOIilanD() {
        char[] arrc = i1O1laNd.I1O1I1LaNd.toCharArray();
        int n2 = arrc.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Character c2 = Character.valueOf(arrc[i2]);
            this.O1il1llOLANd.I1O1I1LaNd(c2.charValue());
        }
    }

    private void I1O1I1LaNd(String string, int n2, float f2, boolean bl) {
        double d2 = 0.0;
        for (char c2 : string.toCharArray()) {
            il1OOilAnD il1OOilAnD2 = this.O1il1llOLANd.I1O1I1LaNd(c2);
            if (il1OOilAnD2.I1O1I1LaNd == -1) continue;
            lI0il11LaND.I1O1I1LaNd(il1OOilAnD2.I1O1I1LaNd);
            if (bl) {
                lI0il11LaND.I1O1I1LaNd(d2 + 4.0, 4.0, 64.0, 64.0, new Color(n2).darker().darker().darker().darker().darker().darker().getRGB(), f2 * 0.3f);
            }
            lI0il11LaND.I1O1I1LaNd(d2, 0.0, 64.0, 64.0, n2, f2);
            d2 += il1OOilAnD2.OOOIilanD;
        }
    }

    public void I1O1I1LaNd(String string, double d2, double d3, int n2, double d4, int n3) {
        this.OOOIilanD(string, d2, d3, n2, d4, n3, 1.0f);
    }

    public void OOOIilanD(String string, double d2, double d3, int n2, double d4, int n3) {
        this.I1O1I1LaNd(string, d2, d3, n2, d4, n3, 1.0f);
    }

    public void I1O1I1LaNd(String string, double d2, double d3, int n2, double d4, int n3, float f2, boolean bl) {
        GL11.glPushMatrix();
        GL11.glTranslated(d2, d3, 0.0);
        GL11.glScaled((float)n2 / 60.0f, (float)n2 / 60.0f, 1.0);
        GL11.glScaled(d4, d4, 1.0);
        this.I1O1I1LaNd(string, n3, f2, bl);
        GL11.glPopMatrix();
    }

    public void I1O1I1LaNd(String string, double d2, double d3, int n2, double d4, int n3, float f2) {
        this.I1O1I1LaNd(string, d2, d3, n2, d4, n3, f2, false);
    }

    public void OOOIilanD(String string, double d2, double d3, int n2, double d4, int n3, float f2) {
        GL11.glPushMatrix();
        GL11.glTranslated(d2 - this.I1O1I1LaNd(string) / 2.0 * d4 * (double)n2 / 60.0, d3, 0.0);
        GL11.glScaled((float)n2 / 60.0f, (float)n2 / 60.0f, 1.0);
        GL11.glScaled(d4, d4, 1.0);
        this.I1O1I1LaNd(string, n3, f2, false);
        GL11.glPopMatrix();
    }

    public double I1O1I1LaNd(String string, int n2, double d2) {
        return this.I1O1I1LaNd(string) * (double)((float)n2 / 60.0f) * d2;
    }

    private double I1O1I1LaNd(String string) {
        double d2 = 0.0;
        for (char c2 : string.toCharArray()) {
            d2 += this.I1O1I1LaNd(c2);
        }
        return d2;
    }

    private double I1O1I1LaNd(char c2) {
        double d2 = 0.0;
        il1OOilAnD il1OOilAnD2 = this.O1il1llOLANd.I1O1I1LaNd(c2);
        if (il1OOilAnD2.I1O1I1LaNd != -1) {
            d2 += il1OOilAnD2.OOOIilanD;
        }
        return d2;
    }
}

