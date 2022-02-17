/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import loliland.launcher.client.il1OOilAnD;
import loliland.launcher.client.l11OliLAnD;
import loliland.launcher.client.lI10ilAnd;
import loliland.launcher.client.lI1il011LAnD;
import loliland.launcher.client.liililO0LAnD;

public class i1O1laNd {
    public static String I1O1I1LaNd = " 0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ%";
    private Font OOOIilanD;
    private liililO0LAnD lI00OlAND;
    private HashMap lli0OiIlAND = new HashMap();

    public i1O1laNd(Font font, liililO0LAnD liililO0LAnD2) {
        this.OOOIilanD = font;
        this.lI00OlAND = liililO0LAnD2;
    }

    public il1OOilAnD I1O1I1LaNd(char c2) {
        if (this.lli0OiIlAND.containsKey(Character.valueOf(c2))) {
            return (il1OOilAnD)this.lli0OiIlAND.get(Character.valueOf(c2));
        }
        il1OOilAnD il1OOilAnD2 = new il1OOilAnD();
        l11OliLAnD.I1O1I1LaNd.submit(() -> {
            BufferedImage bufferedImage = new BufferedImage(64, 64, 2);
            Graphics graphics = bufferedImage.getGraphics();
            Font font = this.OOOIilanD.deriveFont(60.0f);
            graphics.setFont(font);
            HashMap<TextAttribute, Object> hashMap = new HashMap<TextAttribute, Object>();
            hashMap.put(TextAttribute.FAMILY, this.OOOIilanD.getFamily());
            hashMap.put(TextAttribute.SIZE, 60);
            font = Font.getFont(hashMap);
            if (this.lI00OlAND == liililO0LAnD.OOOIilanD) {
                font = font.deriveFont(1);
            } else if (this.lI00OlAND == liililO0LAnD.lli0OiIlAND) {
                font = font.deriveFont(3);
            } else if (this.lI00OlAND == liililO0LAnD.lI00OlAND) {
                font = font.deriveFont(2);
            }
            graphics.setFont(font);
            Rectangle2D rectangle2D = graphics.getFontMetrics().getStringBounds(String.valueOf(c2), graphics);
            graphics.drawString(String.valueOf(c2), 3, 53);
            lI10ilAnd.I1O1I1LaNd().I1O1I1LaNd(() -> {
                int n2 = lI1il011LAnD.I1O1I1LaNd();
                lI1il011LAnD.I1O1I1LaNd(n2, bufferedImage);
                il1OOilAnD2.I1O1I1LaNd = n2;
                il1OOilAnD2.OOOIilanD = rectangle2D.getWidth();
            });
        });
        this.lli0OiIlAND.put(Character.valueOf(c2), il1OOilAnD2);
        return il1OOilAnD2;
    }

    public void I1O1I1LaNd() {
        this.lli0OiIlAND.clear();
    }
}

