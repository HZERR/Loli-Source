/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j.quantizer;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

class o {
    o() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static final int[] a(BufferedImage bufferedImage) {
        int[] arrn;
        int n2 = bufferedImage.getType();
        if (bufferedImage.getRaster().getParent() == null && (n2 == 1 || n2 == 2 || n2 == 3)) {
            int[] arrn2 = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
            if (bufferedImage.getProperty("GIF4J") != null && bufferedImage.getProperty("GIF4J") != Image.UndefinedProperty) {
                arrn = arrn2;
            } else {
                arrn = new int[arrn2.length];
                System.arraycopy(arrn2, 0, arrn, 0, arrn2.length);
            }
        } else {
            int n3 = 1;
            if (bufferedImage.getColorModel().hasAlpha()) {
                n3 = 2;
            }
            BufferedImage bufferedImage2 = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), n3);
            Graphics2D graphics2D = null;
            boolean bl = false;
            try {
                graphics2D = bufferedImage2.createGraphics();
                graphics2D.drawImage(bufferedImage, null, 0, 0);
                bl = true;
            }
            catch (Error error) {
            }
            finally {
                if (graphics2D != null) {
                    graphics2D.dispose();
                }
            }
            arrn = bl ? ((DataBufferInt)bufferedImage2.getRaster().getDataBuffer()).getData() : bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        }
        return arrn;
    }

    static final int a(int n2) {
        int n3 = 0;
        int n4 = n2;
        while ((n2 >>>= 1) > 0) {
            ++n3;
        }
        if (1 << n3 < n4) {
            ++n3;
        }
        return n3;
    }
}

