/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import org.lwjgl.opengl.GL11;

public class lll0O0LAnd {
    public static void I1O1I1LaNd(int n2, int n3, int n4) {
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GL11.glTranslated(n2, n3, 0.0);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        lll0O0LAnd.I1O1I1LaNd(4 * n4, 0 * n4, 8 * n4, 8 * n4, 0.125, 0.25, 0.25, 0.5);
        lll0O0LAnd.I1O1I1LaNd(0 * n4, 8 * n4, 4 * n4, 12 * n4, 0.625, 0.625, 0.6875, 1.0);
        lll0O0LAnd.I1O1I1LaNd(12 * n4, 8 * n4, 4 * n4, 12 * n4, 0.625, 0.625, 0.6875, 1.0);
        lll0O0LAnd.I1O1I1LaNd(4 * n4, 8 * n4, 8 * n4, 12 * n4, 0.3125, 0.625, 0.4375, 1.0);
        lll0O0LAnd.I1O1I1LaNd(4 * n4, 20 * n4, 4 * n4, 12 * n4, 0.0625, 0.625, 0.125, 1.0);
        lll0O0LAnd.I1O1I1LaNd(8 * n4, 20 * n4, 4 * n4, 12 * n4, 0.0625, 0.625, 0.125, 1.0);
        GL11.glPopMatrix();
    }

    public static void I1O1I1LaNd(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        GL11.glBegin(7);
        GL11.glTexCoord2d(d6, d9);
        GL11.glVertex3d(d2, d3 + d5, 0.0);
        GL11.glTexCoord2d(d8, d9);
        GL11.glVertex3d(d2 + d4, d3 + d5, 0.0);
        GL11.glTexCoord2d(d8, d7);
        GL11.glVertex3d(d2 + d4, d3, 0.0);
        GL11.glTexCoord2d(d6, d7);
        GL11.glVertex3d(d2, d3, 0.0);
        GL11.glEnd();
    }
}

