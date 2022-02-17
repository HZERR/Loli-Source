/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.l11OliLAnD;
import loliland.launcher.client.l1lIOlAND;
import loliland.launcher.client.lI1il011LAnD;
import org.lwjgl.opengl.GL11;

public class lI0il11LaND {
    private static int I1O1I1LaNd = -1;

    public static void I1O1I1LaNd(l11OliLAnD l11OliLAnD2) {
        int n2 = l11OliLAnD2.OOOIilanD();
        lI0il11LaND.I1O1I1LaNd(n2);
    }

    public static void I1O1I1LaNd(int n2, int n3, int n4, int n5) {
        int n6 = 1;
        int n7 = n4 * n6;
        int n8 = n5 * n6;
        int n9 = n2 * n6;
        int n10 = 659 - n8 - n3 * n6;
        GL11.glScissor(n9, n10, n7, n8);
    }

    public static void I1O1I1LaNd(int n2) {
        GL11.glBindTexture(3553, n2);
    }

    public static l11OliLAnD I1O1I1LaNd(String string) {
        String string2 = "https://api.loliland.ru/avatar/" + string + "";
        return l1lIOlAND.I1O1I1LaNd(string2);
    }

    public static l11OliLAnD I1O1I1LaNd(String string, String string2) {
        return l1lIOlAND.I1O1I1LaNd(string, string2);
    }

    private static l11OliLAnD OOOIilanD(String string, String string2) {
        return l1lIOlAND.OOOIilanD(string, string2);
    }

    private static l11OliLAnD lI00OlAND(String string) {
        if (string.contains(":")) {
            String[] arrstring = string.split(":");
            return lI0il11LaND.OOOIilanD(arrstring[0], arrstring[1]);
        }
        return lI0il11LaND.OOOIilanD("loliland/launcher", string);
    }

    public static void OOOIilanD(String string) {
        l11OliLAnD l11OliLAnD2 = lI0il11LaND.lI00OlAND(string);
    }

    public static void I1O1I1LaNd(double d2, double d3, double d4, double d5, int n2, float f2, String string) {
        l11OliLAnD l11OliLAnD2 = lI0il11LaND.lI00OlAND(string);
        lI0il11LaND.I1O1I1LaNd(l11OliLAnD2);
        lI0il11LaND.I1O1I1LaNd(d2, d3, d4, d5, n2, f2);
    }

    public static void I1O1I1LaNd(double d2, double d3, float f2, String string) {
        l11OliLAnD l11OliLAnD2 = lI0il11LaND.lI00OlAND(string);
        lI0il11LaND.I1O1I1LaNd(l11OliLAnD2);
        lI0il11LaND.I1O1I1LaNd(d2, d3, (double)l11OliLAnD2.O1il1llOLANd(), (double)l11OliLAnD2.li0iOILAND(), -1, f2);
    }

    public static void I1O1I1LaNd(double d2, double d3, double d4, int n2, float f2, String string) {
        l11OliLAnD l11OliLAnD2 = lI0il11LaND.lI00OlAND(string);
        lI0il11LaND.I1O1I1LaNd(l11OliLAnD2);
        lI0il11LaND.I1O1I1LaNd(d2, d3, (double)l11OliLAnD2.O1il1llOLANd() * d4, (double)l11OliLAnD2.li0iOILAND() * d4, n2, f2);
    }

    public static void I1O1I1LaNd(double d2, double d3, double d4, String string) {
        lI0il11LaND.I1O1I1LaNd(d2, d3, d4, -1, 1.0f, string);
    }

    public static void I1O1I1LaNd(double d2, double d3, double d4, double d5, int n2, float f2) {
        float f3 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n2 & 0xFF) / 255.0f;
        float f6 = 10.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3553);
        GL11.glEnable(2832);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3153, 4352);
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glTranslated(d2, d3, 0.0);
        GL11.glScalef(1.0f / f6, 1.0f / f6, 0.0f);
        d2 = 0.0;
        d3 = 0.0;
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3d(d2 + 0.0, d3 + (d5 *= (double)f6), 0.0);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3d(d2 + (d4 *= (double)f6), d3 + d5, 0.0);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3d(d2 + d4, d3 + 0.0, 0.0);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3d(d2, d3 + 0.0, 0.0);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glDisable(3553);
        GL11.glPopMatrix();
    }

    public static void I1O1I1LaNd(String string, double d2, double d3, double d4, double d5) {
        GL11.glPushMatrix();
        if (I1O1I1LaNd == -1) {
            I1O1I1LaNd = lI1il011LAnD.I1O1I1LaNd();
            BufferedImage bufferedImage = new BufferedImage(256, 256, 2);
            Graphics2D graphics2D = bufferedImage.createGraphics();
            graphics2D.setColor(Color.WHITE);
            graphics2D.fillOval(0, 0, 256, 256);
            lI1il011LAnD.I1O1I1LaNd(I1O1I1LaNd, bufferedImage);
        }
        GL11.glTranslated(d2, d3, 0.0);
        lI0il11LaND.I1O1I1LaNd(I1O1I1LaNd);
        GL11.glDepthMask(true);
        lI0il11LaND.I1O1I1LaNd(0.0, 0.0, d4, d5, -1, 0.7f);
        GL11.glDepthMask(false);
        GL11.glDepthFunc(514);
        lI0il11LaND.I1O1I1LaNd(lI0il11LaND.I1O1I1LaNd(string));
        lI0il11LaND.I1O1I1LaNd(0.0, 0.0, d4, d5, -1, 1.0f);
        GL11.glDepthFunc(515);
        GL11.glTranslatef(0.0f, 0.0f, 0.0f);
        GL11.glPopMatrix();
    }

    public static void I1O1I1LaNd(double d2, double d3, double d4, int n2, int n3, int n4) {
        GL11.glDisable(3553);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        float f2 = (float)(n3 >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(n3 >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(n3 & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, (float)n4 / 255.0f);
        GL11.glBegin(9);
        d4 -= (double)0.1f;
        for (int i2 = 0; i2 < n2; ++i2) {
            double d5 = Math.PI * -2 * (double)i2 / (double)n2;
            double d6 = Math.cos(d5);
            double d7 = Math.sin(d5);
            GL11.glVertex2d(d6 * d4 + d2 + 0.5, d7 * d4 + d3 + 0.5);
        }
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
    }

    public static void I1O1I1LaNd(double d2, double d3, double d4, double d5, float f2, float f3) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(f2);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, f3);
        GL11.glBegin(2);
        GL11.glVertex2d(d2, d3);
        GL11.glVertex2d(d4, d5);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void OOOIilanD(double d2, double d3, double d4, double d5, int n2, float f2) {
        GL11.glDisable(3553);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        float f3 = (float)(n2 >> 16 & 0xFF) / 255.0f;
        float f4 = (float)(n2 >> 8 & 0xFF) / 255.0f;
        float f5 = (float)(n2 & 0xFF) / 255.0f;
        GL11.glColor4f(f3, f4, f5, f2);
        GL11.glBegin(9);
        GL11.glVertex2d(d2 + d4, d3);
        GL11.glVertex2d(d2, d3);
        GL11.glVertex2d(d2, d3 + d5);
        GL11.glVertex2d(d2 + d4, d3 + d5);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
    }

    public static void I1O1I1LaNd() {
        l1lIOlAND.I1O1I1LaNd().forEach((string, l11OliLAnD2) -> l11OliLAnD2.lli0OiIlAND());
        l1lIOlAND.I1O1I1LaNd().clear();
        iO11lland.I1O1I1LaNd();
    }
}

