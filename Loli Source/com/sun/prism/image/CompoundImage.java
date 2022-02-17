/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundCoords;
import com.sun.prism.image.Coords;

public abstract class CompoundImage {
    public static final int BORDER_SIZE_DEFAULT = 1;
    protected final int[] uSubdivision;
    protected final int[] u0;
    protected final int[] u1;
    protected final int[] vSubdivision;
    protected final int[] v0;
    protected final int[] v1;
    protected final int uSections;
    protected final int vSections;
    protected final int uBorderSize;
    protected final int vBorderSize;
    protected Image[] tiles;

    public CompoundImage(Image image, int n2) {
        this(image, n2, 1);
    }

    public CompoundImage(Image image, int n2, int n3) {
        int n4;
        if (4 * n3 >= n2) {
            n3 = n2 / 4;
        }
        int n5 = image.getWidth();
        int n6 = image.getHeight();
        this.uBorderSize = n5 <= n2 ? 0 : n3;
        this.vBorderSize = n6 <= n2 ? 0 : n3;
        this.uSubdivision = CompoundImage.subdivideUVs(n5, n2, this.uBorderSize);
        this.vSubdivision = CompoundImage.subdivideUVs(n6, n2, this.vBorderSize);
        this.uSections = this.uSubdivision.length - 1;
        this.vSections = this.vSubdivision.length - 1;
        this.u0 = new int[this.uSections];
        this.u1 = new int[this.uSections];
        this.v0 = new int[this.vSections];
        this.v1 = new int[this.vSections];
        this.tiles = new Image[this.uSections * this.vSections];
        for (n4 = 0; n4 != this.vSections; ++n4) {
            this.v0[n4] = this.vSubdivision[n4] - this.uBorder(n4);
            this.v1[n4] = this.vSubdivision[n4 + 1] + this.dBorder(n4);
        }
        for (n4 = 0; n4 != this.uSections; ++n4) {
            this.u0[n4] = this.uSubdivision[n4] - this.lBorder(n4);
            this.u1[n4] = this.uSubdivision[n4 + 1] + this.rBorder(n4);
        }
        for (n4 = 0; n4 != this.vSections; ++n4) {
            for (int i2 = 0; i2 != this.uSections; ++i2) {
                this.tiles[n4 * this.uSections + i2] = image.createSubImage(this.u0[i2], this.v0[n4], this.u1[i2] - this.u0[i2], this.v1[n4] - this.v0[n4]);
            }
        }
    }

    private int lBorder(int n2) {
        return n2 > 0 ? this.uBorderSize : 0;
    }

    private int rBorder(int n2) {
        return n2 < this.uSections - 1 ? this.uBorderSize : 0;
    }

    private int uBorder(int n2) {
        return n2 > 0 ? this.vBorderSize : 0;
    }

    private int dBorder(int n2) {
        return n2 < this.vSections - 1 ? this.vBorderSize : 0;
    }

    private static int[] subdivideUVs(int n2, int n3, int n4) {
        int n5 = n3 - n4 * 2;
        int n6 = (n2 - n4 * 2 + n5 - 1) / n5;
        int[] arrn = new int[n6 + 1];
        arrn[0] = 0;
        arrn[n6] = n2;
        for (int i2 = 1; i2 < n6; ++i2) {
            arrn[i2] = n4 + n5 * i2;
        }
        return arrn;
    }

    protected abstract Texture getTile(int var1, int var2, ResourceFactory var3);

    public void drawLazy(Graphics graphics, Coords coords, float f2, float f3) {
        new CompoundCoords(this, coords).draw(graphics, this, f2, f3);
    }
}

