/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.image;

import com.sun.prism.Graphics;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.image.CompoundImage;
import com.sun.prism.image.Coords;

public class CompoundCoords {
    private int xImg0;
    private int xImg1;
    private int yImg0;
    private int yImg1;
    private Coords[] tileCoords;

    public CompoundCoords(CompoundImage compoundImage, Coords coords) {
        int n2;
        int n3 = CompoundCoords.find1(CompoundCoords.fastFloor(coords.u0), compoundImage.uSubdivision);
        int n4 = CompoundCoords.find2(CompoundCoords.fastCeil(coords.u1), compoundImage.uSubdivision);
        int n5 = CompoundCoords.find1(CompoundCoords.fastFloor(coords.v0), compoundImage.vSubdivision);
        int n6 = CompoundCoords.find2(CompoundCoords.fastCeil(coords.v1), compoundImage.vSubdivision);
        if (n3 < 0 || n4 < 0 || n5 < 0 || n6 < 0) {
            return;
        }
        this.xImg0 = n3;
        this.xImg1 = n4;
        this.yImg0 = n5;
        this.yImg1 = n6;
        this.tileCoords = new Coords[(n4 - n3 + 1) * (n6 - n5 + 1)];
        float[] arrf = new float[n4 - n3];
        float[] arrf2 = new float[n6 - n5];
        for (n2 = n3; n2 < n4; ++n2) {
            arrf[n2 - n3] = coords.getX(compoundImage.uSubdivision[n2 + 1]);
        }
        for (n2 = n5; n2 < n6; ++n2) {
            arrf2[n2 - n5] = coords.getY(compoundImage.vSubdivision[n2 + 1]);
        }
        n2 = 0;
        for (int i2 = n5; i2 <= n6; ++i2) {
            float f2 = (i2 == n5 ? coords.v0 : (float)compoundImage.vSubdivision[i2]) - (float)compoundImage.v0[i2];
            float f3 = (i2 == n6 ? coords.v1 : (float)compoundImage.vSubdivision[i2 + 1]) - (float)compoundImage.v0[i2];
            float f4 = i2 == n5 ? coords.y0 : arrf2[i2 - n5 - 1];
            float f5 = i2 == n6 ? coords.y1 : arrf2[i2 - n5];
            for (int i3 = n3; i3 <= n4; ++i3) {
                Coords coords2 = new Coords();
                coords2.v0 = f2;
                coords2.v1 = f3;
                coords2.y0 = f4;
                coords2.y1 = f5;
                coords2.u0 = (i3 == n3 ? coords.u0 : (float)compoundImage.uSubdivision[i3]) - (float)compoundImage.u0[i3];
                coords2.u1 = (i3 == n4 ? coords.u1 : (float)compoundImage.uSubdivision[i3 + 1]) - (float)compoundImage.u0[i3];
                coords2.x0 = i3 == n3 ? coords.x0 : arrf[i3 - n3 - 1];
                coords2.x1 = i3 == n4 ? coords.x1 : arrf[i3 - n3];
                this.tileCoords[n2++] = coords2;
            }
        }
    }

    public void draw(Graphics graphics, CompoundImage compoundImage, float f2, float f3) {
        if (this.tileCoords == null) {
            return;
        }
        ResourceFactory resourceFactory = graphics.getResourceFactory();
        int n2 = 0;
        for (int i2 = this.yImg0; i2 <= this.yImg1; ++i2) {
            for (int i3 = this.xImg0; i3 <= this.xImg1; ++i3) {
                Texture texture = compoundImage.getTile(i3, i2, resourceFactory);
                this.tileCoords[n2++].draw(texture, graphics, f2, f3);
                texture.unlock();
            }
        }
    }

    private static int find1(int n2, int[] arrn) {
        for (int i2 = 0; i2 < arrn.length - 1; ++i2) {
            if (arrn[i2] > n2 || n2 >= arrn[i2 + 1]) continue;
            return i2;
        }
        return -1;
    }

    private static int find2(int n2, int[] arrn) {
        for (int i2 = 0; i2 < arrn.length - 1; ++i2) {
            if (arrn[i2] >= n2 || n2 > arrn[i2 + 1]) continue;
            return i2;
        }
        return -1;
    }

    private static int fastFloor(float f2) {
        int n2 = (int)f2;
        return (float)n2 <= f2 ? n2 : n2 - 1;
    }

    private static int fastCeil(float f2) {
        int n2 = (int)f2;
        return (float)n2 >= f2 ? n2 : n2 + 1;
    }
}

