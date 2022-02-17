/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

public final class WCRectangle {
    float x;
    float y;
    float w;
    float h;

    public WCRectangle(float f2, float f3, float f4, float f5) {
        this.x = f2;
        this.y = f3;
        this.w = f4;
        this.h = f5;
    }

    public WCRectangle(WCRectangle wCRectangle) {
        this.x = wCRectangle.x;
        this.y = wCRectangle.y;
        this.w = wCRectangle.w;
        this.h = wCRectangle.h;
    }

    public WCRectangle() {
    }

    public float getX() {
        return this.x;
    }

    public int getIntX() {
        return (int)this.x;
    }

    public float getY() {
        return this.y;
    }

    public int getIntY() {
        return (int)this.y;
    }

    public float getWidth() {
        return this.w;
    }

    public int getIntWidth() {
        return (int)this.w;
    }

    public float getHeight() {
        return this.h;
    }

    public int getIntHeight() {
        return (int)this.h;
    }

    public boolean contains(WCRectangle wCRectangle) {
        return this.x <= wCRectangle.x && this.x + this.w >= wCRectangle.x + wCRectangle.w && this.y <= wCRectangle.y && this.y + this.h >= wCRectangle.y + wCRectangle.h;
    }

    public WCRectangle intersection(WCRectangle wCRectangle) {
        float f2 = this.x;
        float f3 = this.y;
        float f4 = wCRectangle.x;
        float f5 = wCRectangle.y;
        float f6 = f2;
        f6 += this.w;
        float f7 = f3;
        f7 += this.h;
        float f8 = f4;
        f8 += wCRectangle.w;
        float f9 = f5;
        f9 += wCRectangle.h;
        if (f2 < f4) {
            f2 = f4;
        }
        if (f3 < f5) {
            f3 = f5;
        }
        if (f6 > f8) {
            f6 = f8;
        }
        if (f7 > f9) {
            f7 = f9;
        }
        f6 -= f2;
        f7 -= f3;
        if (f6 < Float.MIN_VALUE) {
            f6 = Float.MIN_VALUE;
        }
        if (f7 < Float.MIN_VALUE) {
            f7 = Float.MIN_VALUE;
        }
        return new WCRectangle(f2, f3, f6, f7);
    }

    public void translate(float f2, float f3) {
        float f4 = this.x;
        float f5 = f4 + f2;
        if (f2 < 0.0f) {
            if (f5 > f4) {
                if (this.w >= 0.0f) {
                    this.w += f5 - Float.MIN_VALUE;
                }
                f5 = Float.MIN_VALUE;
            }
        } else if (f5 < f4) {
            if (this.w >= 0.0f) {
                this.w += f5 - Float.MAX_VALUE;
                if (this.w < 0.0f) {
                    this.w = Float.MAX_VALUE;
                }
            }
            f5 = Float.MAX_VALUE;
        }
        this.x = f5;
        f4 = this.y;
        f5 = f4 + f3;
        if (f3 < 0.0f) {
            if (f5 > f4) {
                if (this.h >= 0.0f) {
                    this.h += f5 - Float.MIN_VALUE;
                }
                f5 = Float.MIN_VALUE;
            }
        } else if (f5 < f4) {
            if (this.h >= 0.0f) {
                this.h += f5 - Float.MAX_VALUE;
                if (this.h < 0.0f) {
                    this.h = Float.MAX_VALUE;
                }
            }
            f5 = Float.MAX_VALUE;
        }
        this.y = f5;
    }

    public WCRectangle createUnion(WCRectangle wCRectangle) {
        WCRectangle wCRectangle2 = new WCRectangle();
        WCRectangle.union(this, wCRectangle, wCRectangle2);
        return wCRectangle2;
    }

    public static void union(WCRectangle wCRectangle, WCRectangle wCRectangle2, WCRectangle wCRectangle3) {
        float f2 = Math.min(wCRectangle.getMinX(), wCRectangle2.getMinX());
        float f3 = Math.min(wCRectangle.getMinY(), wCRectangle2.getMinY());
        float f4 = Math.max(wCRectangle.getMaxX(), wCRectangle2.getMaxX());
        float f5 = Math.max(wCRectangle.getMaxY(), wCRectangle2.getMaxY());
        wCRectangle3.setFrameFromDiagonal(f2, f3, f4, f5);
    }

    public void setFrameFromDiagonal(float f2, float f3, float f4, float f5) {
        float f6;
        if (f4 < f2) {
            f6 = f2;
            f2 = f4;
            f4 = f6;
        }
        if (f5 < f3) {
            f6 = f3;
            f3 = f5;
            f5 = f6;
        }
        this.setFrame(f2, f3, f4 - f2, f5 - f3);
    }

    public void setFrame(float f2, float f3, float f4, float f5) {
        this.x = f2;
        this.y = f3;
        this.w = f4;
        this.h = f5;
    }

    public float getMinX() {
        return this.getX();
    }

    public float getMaxX() {
        return this.getX() + this.getWidth();
    }

    public float getMinY() {
        return this.getY();
    }

    public float getMaxY() {
        return this.getY() + this.getHeight();
    }

    public boolean isEmpty() {
        return this.w <= 0.0f || this.h <= 0.0f;
    }

    public boolean equals(Object object) {
        if (object instanceof WCRectangle) {
            WCRectangle wCRectangle = (WCRectangle)object;
            return this.x == wCRectangle.x && this.y == wCRectangle.y && this.w == wCRectangle.w && this.h == wCRectangle.h;
        }
        return super.equals(object);
    }

    public String toString() {
        return "WCRectangle{x:" + this.x + " y:" + this.y + " w:" + this.w + " h:" + this.h + "}";
    }
}

