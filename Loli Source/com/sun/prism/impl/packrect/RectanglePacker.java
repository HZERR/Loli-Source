/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.packrect;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Texture;
import com.sun.prism.impl.packrect.Level;
import java.util.ArrayList;
import java.util.List;

public class RectanglePacker {
    private Texture backingStore;
    private List<Level> levels = new ArrayList<Level>(150);
    private static final int MIN_SIZE = 8;
    private static final int ROUND_UP = 4;
    private int recentUsedLevelIndex = 0;
    private int length;
    private int size;
    private int sizeOffset;
    private int x;
    private int y;
    private boolean vertical;

    public RectanglePacker(Texture texture, int n2, int n3, int n4, int n5, boolean bl) {
        this.backingStore = texture;
        if (bl) {
            this.length = n5;
            this.size = n4;
        } else {
            this.length = n4;
            this.size = n5;
        }
        this.x = n2;
        this.y = n3;
        this.vertical = bl;
    }

    public RectanglePacker(Texture texture, int n2, int n3) {
        this(texture, 0, 0, n2, n3, false);
    }

    public final Texture getBackingStore() {
        return this.backingStore;
    }

    public final boolean add(Rectangle rectangle) {
        int n2;
        int n3 = this.vertical ? rectangle.height : rectangle.width;
        int n4 = n2 = this.vertical ? rectangle.width : rectangle.height;
        if (n3 > this.length) {
            return false;
        }
        if (n2 > this.size) {
            return false;
        }
        int n5 = 8 > n2 ? 8 : n2;
        n5 = n5 + 4 - 1 - (n5 - 1) % 4;
        int n6 = this.recentUsedLevelIndex < this.levels.size() && this.levels.get((int)this.recentUsedLevelIndex).size != n5 ? RectanglePacker.binarySearch(this.levels, n5) : this.recentUsedLevelIndex;
        boolean bl = this.sizeOffset + n5 <= this.size;
        int n7 = this.levels.size();
        for (int i2 = n6; i2 < n7; ++i2) {
            Level level = this.levels.get(i2);
            if (level.size > n5 + 8 && bl) break;
            if (!level.add(rectangle, this.x, this.y, n3, n2, this.vertical)) continue;
            this.recentUsedLevelIndex = i2;
            return true;
        }
        if (!bl) {
            return false;
        }
        Level level = new Level(this.length, n5, this.sizeOffset);
        this.sizeOffset += n5;
        if (n6 < this.levels.size() && this.levels.get((int)n6).size <= n5) {
            this.levels.add(n6 + 1, level);
            this.recentUsedLevelIndex = n6 + 1;
        } else {
            this.levels.add(n6, level);
            this.recentUsedLevelIndex = n6;
        }
        return level.add(rectangle, this.x, this.y, n3, n2, this.vertical);
    }

    public void clear() {
        this.levels.clear();
        this.sizeOffset = 0;
        this.recentUsedLevelIndex = 0;
    }

    public void dispose() {
        if (this.backingStore != null) {
            this.backingStore.dispose();
        }
        this.backingStore = null;
        this.levels = null;
    }

    private static int binarySearch(List<Level> list, int n2) {
        int n3 = n2 + 1;
        int n4 = 0;
        int n5 = list.size() - 1;
        int n6 = 0;
        int n7 = 0;
        if (n5 < 0) {
            return 0;
        }
        while (n4 <= n5) {
            n6 = (n4 + n5) / 2;
            n7 = list.get((int)n6).size;
            if (n3 < n7) {
                n5 = n6 - 1;
                continue;
            }
            n4 = n6 + 1;
        }
        if (n7 < n2) {
            return n6 + 1;
        }
        if (n7 > n2) {
            return n6 > 0 ? n6 - 1 : 0;
        }
        return n6;
    }
}

