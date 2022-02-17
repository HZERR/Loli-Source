/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;

public class Rectangle {
    public int x;
    public int y;
    public int width;
    public int height;

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public Rectangle(BaseBounds baseBounds) {
        this.setBounds(baseBounds);
    }

    public Rectangle(Rectangle rectangle) {
        this(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public Rectangle(int n2, int n3, int n4, int n5) {
        this.x = n2;
        this.y = n3;
        this.width = n4;
        this.height = n5;
    }

    public Rectangle(int n2, int n3) {
        this(0, 0, n2, n3);
    }

    public void setBounds(Rectangle rectangle) {
        this.setBounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public void setBounds(int n2, int n3, int n4, int n5) {
        this.reshape(n2, n3, n4, n5);
    }

    public void setBounds(BaseBounds baseBounds) {
        this.x = (int)Math.floor(baseBounds.getMinX());
        this.y = (int)Math.floor(baseBounds.getMinY());
        int n2 = (int)Math.ceil(baseBounds.getMaxX());
        int n3 = (int)Math.ceil(baseBounds.getMaxY());
        this.width = n2 - this.x;
        this.height = n3 - this.y;
    }

    public boolean contains(int n2, int n3) {
        int n4 = this.width;
        int n5 = this.height;
        if ((n4 | n5) < 0) {
            return false;
        }
        int n6 = this.x;
        int n7 = this.y;
        if (n2 < n6 || n3 < n7) {
            return false;
        }
        n5 += n7;
        return !((n4 += n6) >= n6 && n4 <= n2 || n5 >= n7 && n5 <= n3);
    }

    public boolean contains(Rectangle rectangle) {
        return this.contains(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
    }

    public boolean contains(int n2, int n3, int n4, int n5) {
        int n6 = this.width;
        int n7 = this.height;
        if ((n6 | n7 | n4 | n5) < 0) {
            return false;
        }
        int n8 = this.x;
        int n9 = this.y;
        if (n2 < n8 || n3 < n9) {
            return false;
        }
        n6 += n8;
        if ((n4 += n2) <= n2 ? n6 >= n8 || n4 > n6 : n6 >= n8 && n4 > n6) {
            return false;
        }
        n7 += n9;
        return !((n5 += n3) <= n3 ? n7 >= n9 || n5 > n7 : n7 >= n9 && n5 > n7);
    }

    public Rectangle intersection(Rectangle rectangle) {
        Rectangle rectangle2 = new Rectangle(this);
        rectangle2.intersectWith(rectangle);
        return rectangle2;
    }

    public void intersectWith(Rectangle rectangle) {
        if (rectangle == null) {
            return;
        }
        int n2 = this.x;
        int n3 = this.y;
        int n4 = rectangle.x;
        int n5 = rectangle.y;
        long l2 = n2;
        l2 += (long)this.width;
        long l3 = n3;
        l3 += (long)this.height;
        long l4 = n4;
        l4 += (long)rectangle.width;
        long l5 = n5;
        l5 += (long)rectangle.height;
        if (n2 < n4) {
            n2 = n4;
        }
        if (n3 < n5) {
            n3 = n5;
        }
        if (l2 > l4) {
            l2 = l4;
        }
        if (l3 > l5) {
            l3 = l5;
        }
        l3 -= (long)n3;
        if ((l2 -= (long)n2) < Integer.MIN_VALUE) {
            l2 = Integer.MIN_VALUE;
        }
        if (l3 < Integer.MIN_VALUE) {
            l3 = Integer.MIN_VALUE;
        }
        this.setBounds(n2, n3, (int)l2, (int)l3);
    }

    public void translate(int n2, int n3) {
        int n4 = this.x;
        int n5 = n4 + n2;
        if (n2 < 0) {
            if (n5 > n4) {
                if (this.width >= 0) {
                    this.width += n5 - Integer.MIN_VALUE;
                }
                n5 = Integer.MIN_VALUE;
            }
        } else if (n5 < n4) {
            if (this.width >= 0) {
                this.width += n5 - Integer.MAX_VALUE;
                if (this.width < 0) {
                    this.width = Integer.MAX_VALUE;
                }
            }
            n5 = Integer.MAX_VALUE;
        }
        this.x = n5;
        n4 = this.y;
        n5 = n4 + n3;
        if (n3 < 0) {
            if (n5 > n4) {
                if (this.height >= 0) {
                    this.height += n5 - Integer.MIN_VALUE;
                }
                n5 = Integer.MIN_VALUE;
            }
        } else if (n5 < n4) {
            if (this.height >= 0) {
                this.height += n5 - Integer.MAX_VALUE;
                if (this.height < 0) {
                    this.height = Integer.MAX_VALUE;
                }
            }
            n5 = Integer.MAX_VALUE;
        }
        this.y = n5;
    }

    public RectBounds toRectBounds() {
        return new RectBounds(this.x, this.y, this.x + this.width, this.y + this.height);
    }

    public void add(int n2, int n3) {
        if ((this.width | this.height) < 0) {
            this.x = n2;
            this.y = n3;
            this.height = 0;
            this.width = 0;
            return;
        }
        int n4 = this.x;
        int n5 = this.y;
        long l2 = this.width;
        long l3 = this.height;
        l2 += (long)n4;
        l3 += (long)n5;
        if (n4 > n2) {
            n4 = n2;
        }
        if (n5 > n3) {
            n5 = n3;
        }
        if (l2 < (long)n2) {
            l2 = n2;
        }
        if (l3 < (long)n3) {
            l3 = n3;
        }
        l3 -= (long)n5;
        if ((l2 -= (long)n4) > Integer.MAX_VALUE) {
            l2 = Integer.MAX_VALUE;
        }
        if (l3 > Integer.MAX_VALUE) {
            l3 = Integer.MAX_VALUE;
        }
        this.reshape(n4, n5, (int)l2, (int)l3);
    }

    public void add(Rectangle rectangle) {
        long l2;
        long l3;
        long l4 = this.width;
        long l5 = this.height;
        if ((l4 | l5) < 0L) {
            this.reshape(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        if (((l3 = (long)rectangle.width) | (l2 = (long)rectangle.height)) < 0L) {
            return;
        }
        int n2 = this.x;
        int n3 = this.y;
        l4 += (long)n2;
        l5 += (long)n3;
        int n4 = rectangle.x;
        int n5 = rectangle.y;
        l3 += (long)n4;
        l2 += (long)n5;
        if (n2 > n4) {
            n2 = n4;
        }
        if (n3 > n5) {
            n3 = n5;
        }
        if (l4 < l3) {
            l4 = l3;
        }
        if (l5 < l2) {
            l5 = l2;
        }
        l5 -= (long)n3;
        if ((l4 -= (long)n2) > Integer.MAX_VALUE) {
            l4 = Integer.MAX_VALUE;
        }
        if (l5 > Integer.MAX_VALUE) {
            l5 = Integer.MAX_VALUE;
        }
        this.reshape(n2, n3, (int)l4, (int)l5);
    }

    public void grow(int n2, int n3) {
        long l2 = this.x;
        long l3 = this.y;
        long l4 = this.width;
        long l5 = this.height;
        l4 += l2;
        l5 += l3;
        l3 -= (long)n3;
        l5 += (long)n3;
        if ((l4 += (long)n2) < (l2 -= (long)n2)) {
            if ((l4 -= l2) < Integer.MIN_VALUE) {
                l4 = Integer.MIN_VALUE;
            }
            if (l2 < Integer.MIN_VALUE) {
                l2 = Integer.MIN_VALUE;
            } else if (l2 > Integer.MAX_VALUE) {
                l2 = Integer.MAX_VALUE;
            }
        } else {
            if (l2 < Integer.MIN_VALUE) {
                l2 = Integer.MIN_VALUE;
            } else if (l2 > Integer.MAX_VALUE) {
                l2 = Integer.MAX_VALUE;
            }
            if ((l4 -= l2) < Integer.MIN_VALUE) {
                l4 = Integer.MIN_VALUE;
            } else if (l4 > Integer.MAX_VALUE) {
                l4 = Integer.MAX_VALUE;
            }
        }
        if (l5 < l3) {
            if ((l5 -= l3) < Integer.MIN_VALUE) {
                l5 = Integer.MIN_VALUE;
            }
            if (l3 < Integer.MIN_VALUE) {
                l3 = Integer.MIN_VALUE;
            } else if (l3 > Integer.MAX_VALUE) {
                l3 = Integer.MAX_VALUE;
            }
        } else {
            if (l3 < Integer.MIN_VALUE) {
                l3 = Integer.MIN_VALUE;
            } else if (l3 > Integer.MAX_VALUE) {
                l3 = Integer.MAX_VALUE;
            }
            if ((l5 -= l3) < Integer.MIN_VALUE) {
                l5 = Integer.MIN_VALUE;
            } else if (l5 > Integer.MAX_VALUE) {
                l5 = Integer.MAX_VALUE;
            }
        }
        this.reshape((int)l2, (int)l3, (int)l4, (int)l5);
    }

    private void reshape(int n2, int n3, int n4, int n5) {
        this.x = n2;
        this.y = n3;
        this.width = n4;
        this.height = n5;
    }

    public boolean isEmpty() {
        return this.width <= 0 || this.height <= 0;
    }

    public boolean equals(Object object) {
        if (object instanceof Rectangle) {
            Rectangle rectangle = (Rectangle)object;
            return this.x == rectangle.x && this.y == rectangle.y && this.width == rectangle.width && this.height == rectangle.height;
        }
        return super.equals(object);
    }

    public int hashCode() {
        int n2 = Float.floatToIntBits(this.x);
        n2 += Float.floatToIntBits(this.y) * 37;
        n2 += Float.floatToIntBits(this.width) * 43;
        return n2 += Float.floatToIntBits(this.height) * 47;
    }

    public String toString() {
        return this.getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
    }
}

