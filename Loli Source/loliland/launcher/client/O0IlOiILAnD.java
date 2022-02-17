/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.IO0lll1lanD;
import loliland.launcher.client.lIOlLaND;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class O0IlOiILAnD {
    private static int I1O1I1LaNd;
    private static int OOOIilanD;

    public static void I1O1I1LaNd() {
        try {
            char c2;
            Thread.yield();
            GL11.glFlush();
            while (Mouse.next()) {
                if (llIIi1lanD.lI00OlAND != null) {
                    llIIi1lanD.lI00OlAND.li0iOILAND();
                }
                if (!Mouse.getEventButtonState() || llIIi1lanD.lI00OlAND == null) continue;
                llIIi1lanD.lI00OlAND.O1il1llOLANd();
            }
            while (Keyboard.next()) {
                c2 = Keyboard.getEventCharacter();
                if ((Keyboard.getEventKey() != 0 || c2 < ' ') && !Keyboard.getEventKeyState() || llIIi1lanD.lI00OlAND == null) continue;
                llIIi1lanD.lI00OlAND.I1O1I1LaNd(c2, Keyboard.getEventKey());
            }
            GL11.glClear(16384);
            c2 = (char)(llIIi1lanD.OOOIilanD() ? 1 : 0);
            llIIi1lanD.lI00OlAND();
            if (llIIi1lanD.OOOIilanD != null) {
                llIIi1lanD.OOOIilanD.lli0OiIlAND();
            }
            if (c2 != '\u0000' && llIIi1lanD.lI00OlAND != null) {
                llIIi1lanD.lI00OlAND.O1il1llOLANd();
            }
            if (llIIi1lanD.lI00OlAND == null) {
                return;
            }
            I1O1I1LaNd = Mouse.getX();
            OOOIilanD = 659 - Mouse.getY();
            if (llIIi1lanD.OOOIilanD == null) {
                llIIi1lanD.lI00OlAND.lli0OiIlAND = 1.0f;
            }
            if (llIIi1lanD.lI00OlAND.lli0OiIlAND < 1.0f) {
                llIIi1lanD.lI00OlAND.lli0OiIlAND += 0.0625f;
            }
            llIIi1lanD.lI00OlAND.lli0OiIlAND();
            if (lIOlLaND.li0iOILAND() != null && lIOlLaND.li0iOILAND().get() != null) {
                ((lIOlLaND)lIOlLaND.li0iOILAND().get()).lI00OlAND();
            }
            IO0lll1lanD.I1O1I1LaNd();
        }
        catch (Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    public static int OOOIilanD() {
        return I1O1I1LaNd;
    }

    public static int lI00OlAND() {
        return OOOIilanD;
    }
}

