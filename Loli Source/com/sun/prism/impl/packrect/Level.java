/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.packrect;

import com.sun.javafx.geom.Rectangle;

class Level {
    int length;
    int size;
    private int sizeOffset;
    private int lengthOffset;

    Level(int n2, int n3, int n4) {
        this.length = n2;
        this.size = n3;
        this.sizeOffset = n4;
    }

    boolean add(Rectangle rectangle, int n2, int n3, int n4, int n5, boolean bl) {
        if (this.lengthOffset + n4 <= this.length && n5 <= this.size) {
            if (bl) {
                rectangle.x = this.sizeOffset;
                rectangle.y = this.lengthOffset;
            } else {
                rectangle.x = this.lengthOffset;
                rectangle.y = this.sizeOffset;
            }
            this.lengthOffset += n4;
            rectangle.x += n2;
            rectangle.y += n3;
            return true;
        }
        return false;
    }
}

