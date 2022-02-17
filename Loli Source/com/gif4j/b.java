/*
 * Decompiled with CFR 0.150.
 */
package com.gif4j;

import com.gif4j.GifFrame;
import com.gif4j.GifImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

class b {
    private GifImage e = null;
    int a = 0;
    boolean b = false;
    private BufferedImage f = null;
    private BufferedImage g = null;
    private Graphics2D h = null;
    private Graphics2D i = null;
    static final Color c = new Color(0xFFFFFF, true);
    static final int d = c.getRGB();

    b(GifImage gifImage) {
        this.e = gifImage;
    }

    BufferedImage a() {
        if (!this.b) {
            this.f = new BufferedImage(this.e.getScreenWidth(), this.e.getScreenHeight(), 2);
            this.h = this.f.createGraphics();
            this.h.setBackground(c);
            this.h.clearRect(0, 0, this.e.getScreenWidth(), this.e.getScreenHeight());
            this.b = true;
        }
        BufferedImage bufferedImage = null;
        GifFrame gifFrame = this.e.getFrame(this.a);
        Point point = gifFrame.b(this.e.getScreenWidth(), this.e.getScreenHeight());
        if (gifFrame.o == 0 || gifFrame.o == 1) {
            this.h.drawImage(gifFrame.getAsBufferedImage(), null, point.x, point.y);
            bufferedImage = this.f;
        } else if (gifFrame.o == 3) {
            if (this.g == null) {
                this.g = new BufferedImage(this.e.getScreenWidth(), this.e.getScreenHeight(), 2);
                this.i = this.g.createGraphics();
                this.i.setBackground(c);
                this.i.clearRect(0, 0, this.e.getScreenWidth(), this.e.getScreenHeight());
            }
            int[] arrn = ((DataBufferInt)this.f.getRaster().getDataBuffer()).getData();
            int[] arrn2 = ((DataBufferInt)this.g.getRaster().getDataBuffer()).getData();
            System.arraycopy(arrn, 0, arrn2, 0, arrn.length);
            this.i.drawImage(gifFrame.getAsBufferedImage(), null, point.x, point.y);
            bufferedImage = this.g;
        } else if (gifFrame.o == 2) {
            if (this.g == null) {
                this.g = new BufferedImage(this.e.getScreenWidth(), this.e.getScreenHeight(), 2);
                this.i = this.g.createGraphics();
            }
            int[] arrn = ((DataBufferInt)this.f.getRaster().getDataBuffer()).getData();
            int[] arrn3 = ((DataBufferInt)this.g.getRaster().getDataBuffer()).getData();
            System.arraycopy(arrn, 0, arrn3, 0, arrn.length);
            this.i.drawImage(gifFrame.getAsBufferedImage(), null, point.x, point.y);
            bufferedImage = this.g;
            this.h.setBackground(c);
            this.h.clearRect(point.x, point.y, gifFrame.d, gifFrame.e);
        }
        if (++this.a == this.e.getNumberOfFrames()) {
            this.b();
        }
        return bufferedImage;
    }

    void b() {
        if (this.b) {
            this.b = false;
            if (this.h != null) {
                this.h.dispose();
            }
            if (this.i != null) {
                this.i.dispose();
            }
        }
        this.g = null;
        this.i = null;
        this.f = null;
        this.h = null;
        this.e = null;
    }
}

