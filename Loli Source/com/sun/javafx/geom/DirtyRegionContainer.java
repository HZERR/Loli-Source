/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.geom;

import com.sun.javafx.geom.RectBounds;
import java.util.Arrays;

public final class DirtyRegionContainer {
    public static final int DTR_OK = 1;
    public static final int DTR_CONTAINS_CLIP = 0;
    private RectBounds[] dirtyRegions;
    private int emptyIndex;
    private int[][] heap;
    private int heapSize;
    private long invalidMask;

    public DirtyRegionContainer(int n2) {
        this.initDirtyRegions(n2);
    }

    public boolean equals(Object object) {
        if (object instanceof DirtyRegionContainer) {
            DirtyRegionContainer dirtyRegionContainer = (DirtyRegionContainer)object;
            if (this.size() != dirtyRegionContainer.size()) {
                return false;
            }
            for (int i2 = 0; i2 < this.emptyIndex; ++i2) {
                if (this.getDirtyRegion(i2).equals(dirtyRegionContainer.getDirtyRegion(i2))) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int n2 = 5;
        n2 = 97 * n2 + Arrays.deepHashCode(this.dirtyRegions);
        n2 = 97 * n2 + this.emptyIndex;
        return n2;
    }

    public DirtyRegionContainer deriveWithNewRegion(RectBounds rectBounds) {
        if (rectBounds == null) {
            return this;
        }
        this.dirtyRegions[0].deriveWithNewBounds(rectBounds);
        this.emptyIndex = 1;
        return this;
    }

    public DirtyRegionContainer deriveWithNewRegions(RectBounds[] arrrectBounds) {
        if (arrrectBounds == null || arrrectBounds.length == 0) {
            return this;
        }
        if (arrrectBounds.length > this.maxSpace()) {
            this.initDirtyRegions(arrrectBounds.length);
        }
        this.regioncopy(arrrectBounds, 0, this.dirtyRegions, 0, arrrectBounds.length);
        this.emptyIndex = arrrectBounds.length;
        return this;
    }

    public DirtyRegionContainer deriveWithNewContainer(DirtyRegionContainer dirtyRegionContainer) {
        if (dirtyRegionContainer == null || dirtyRegionContainer.maxSpace() == 0) {
            return this;
        }
        if (dirtyRegionContainer.maxSpace() > this.maxSpace()) {
            this.initDirtyRegions(dirtyRegionContainer.maxSpace());
        }
        this.regioncopy(dirtyRegionContainer.dirtyRegions, 0, this.dirtyRegions, 0, dirtyRegionContainer.emptyIndex);
        this.emptyIndex = dirtyRegionContainer.emptyIndex;
        return this;
    }

    private void initDirtyRegions(int n2) {
        this.dirtyRegions = new RectBounds[n2];
        for (int i2 = 0; i2 < n2; ++i2) {
            this.dirtyRegions[i2] = new RectBounds();
        }
        this.emptyIndex = 0;
    }

    public DirtyRegionContainer copy() {
        DirtyRegionContainer dirtyRegionContainer = new DirtyRegionContainer(this.maxSpace());
        this.regioncopy(this.dirtyRegions, 0, dirtyRegionContainer.dirtyRegions, 0, this.emptyIndex);
        dirtyRegionContainer.emptyIndex = this.emptyIndex;
        return dirtyRegionContainer;
    }

    public int maxSpace() {
        return this.dirtyRegions.length;
    }

    public RectBounds getDirtyRegion(int n2) {
        return this.dirtyRegions[n2];
    }

    public void setDirtyRegion(int n2, RectBounds rectBounds) {
        this.dirtyRegions[n2] = rectBounds;
    }

    public void addDirtyRegion(RectBounds rectBounds) {
        RectBounds rectBounds2;
        if (rectBounds.isEmpty()) {
            return;
        }
        int n2 = 0;
        int n3 = this.emptyIndex;
        for (int i2 = 0; i2 < n3; ++i2) {
            rectBounds2 = this.dirtyRegions[n2];
            if (rectBounds.intersects(rectBounds2)) {
                rectBounds.unionWith(rectBounds2);
                RectBounds rectBounds3 = this.dirtyRegions[n2];
                this.dirtyRegions[n2] = this.dirtyRegions[this.emptyIndex - 1];
                this.dirtyRegions[this.emptyIndex - 1] = rectBounds3;
                --this.emptyIndex;
                continue;
            }
            ++n2;
        }
        if (this.hasSpace()) {
            rectBounds2 = this.dirtyRegions[this.emptyIndex];
            rectBounds2.deriveWithNewBounds(rectBounds);
            ++this.emptyIndex;
            return;
        }
        if (this.dirtyRegions.length == 1) {
            this.dirtyRegions[0].deriveWithUnion(rectBounds);
        } else {
            this.compress(rectBounds);
        }
    }

    public void merge(DirtyRegionContainer dirtyRegionContainer) {
        int n2 = dirtyRegionContainer.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.addDirtyRegion(dirtyRegionContainer.getDirtyRegion(i2));
        }
    }

    public int size() {
        return this.emptyIndex;
    }

    public void reset() {
        this.emptyIndex = 0;
    }

    private RectBounds compress(RectBounds rectBounds) {
        this.compress_heap();
        this.addDirtyRegion(rectBounds);
        return rectBounds;
    }

    private boolean hasSpace() {
        return this.emptyIndex < this.dirtyRegions.length;
    }

    private void regioncopy(RectBounds[] arrrectBounds, int n2, RectBounds[] arrrectBounds2, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            RectBounds rectBounds;
            if ((rectBounds = arrrectBounds[n2++]) == null) {
                arrrectBounds2[n3++].makeEmpty();
                continue;
            }
            arrrectBounds2[n3++].deriveWithNewBounds(rectBounds);
        }
    }

    public boolean checkAndClearRegion(int n2) {
        boolean bl = false;
        if (this.dirtyRegions[n2].isEmpty()) {
            System.arraycopy(this.dirtyRegions, n2 + 1, this.dirtyRegions, n2, this.emptyIndex - n2 - 1);
            --this.emptyIndex;
            bl = true;
        }
        return bl;
    }

    public void grow(int n2, int n3) {
        if (n2 != 0 || n3 != 0) {
            for (int i2 = 0; i2 < this.emptyIndex; ++i2) {
                this.getDirtyRegion(i2).grow(n2, n3);
            }
        }
    }

    public void roundOut() {
        for (int i2 = 0; i2 < this.emptyIndex; ++i2) {
            this.dirtyRegions[i2].roundOut();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i2 = 0; i2 < this.emptyIndex; ++i2) {
            stringBuilder.append(this.dirtyRegions[i2]);
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    private void heapCompress() {
        int n2;
        this.invalidMask = 0L;
        int[] arrn = new int[this.dirtyRegions.length];
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            arrn[i2] = i2;
        }
        for (int i3 = 0; i3 < this.dirtyRegions.length / 2; ++i3) {
            int n3;
            int[] arrn2 = this.takeMinWithMap(arrn);
            n2 = this.resolveMap(arrn, arrn2[1]);
            if (n2 == (n3 = this.resolveMap(arrn, arrn2[2]))) continue;
            this.dirtyRegions[n2].deriveWithUnion(this.dirtyRegions[n3]);
            arrn[n3] = n2;
            this.invalidMask |= (long)(1 << n2);
            this.invalidMask |= (long)(1 << n3);
        }
        for (n2 = 0; n2 < this.emptyIndex; ++n2) {
            if (arrn[n2] == n2) continue;
            while (arrn[this.emptyIndex - 1] != this.emptyIndex - 1) {
                --this.emptyIndex;
            }
            if (n2 >= this.emptyIndex - 1) continue;
            RectBounds rectBounds = this.dirtyRegions[this.emptyIndex - 1];
            this.dirtyRegions[this.emptyIndex - 1] = this.dirtyRegions[n2];
            this.dirtyRegions[n2] = rectBounds;
            arrn[n2] = n2;
            --this.emptyIndex;
        }
    }

    private void heapify() {
        for (int i2 = this.heapSize / 2; i2 >= 0; --i2) {
            this.siftDown(i2);
        }
    }

    private void siftDown(int n2) {
        int n3 = this.heapSize >> 1;
        while (n2 < n3) {
            int n4 = (n2 << 1) + 1;
            int[] arrn = this.heap[n4];
            if (n4 + 1 < this.heapSize && this.heap[n4 + 1][0] < arrn[0]) {
                ++n4;
            }
            if (this.heap[n4][0] >= this.heap[n2][0]) break;
            int[] arrn2 = this.heap[n4];
            this.heap[n4] = this.heap[n2];
            this.heap[n2] = arrn2;
            n2 = n4;
        }
    }

    private int[] takeMinWithMap(int[] arrn) {
        int[] arrn2 = this.heap[0];
        while (((long)(1 << arrn2[1] | 1 << arrn2[2]) & this.invalidMask) > 0L) {
            arrn2[0] = this.unifiedRegionArea(this.resolveMap(arrn, arrn2[1]), this.resolveMap(arrn, arrn2[2]));
            this.siftDown(0);
            if (this.heap[0] == arrn2) break;
            arrn2 = this.heap[0];
        }
        this.heap[this.heapSize - 1] = arrn2;
        this.siftDown(0);
        --this.heapSize;
        return arrn2;
    }

    private int[] takeMin() {
        int[] arrn = this.heap[0];
        this.heap[0] = this.heap[this.heapSize - 1];
        this.heap[this.heapSize - 1] = arrn;
        this.siftDown(0);
        --this.heapSize;
        return arrn;
    }

    private int resolveMap(int[] arrn, int n2) {
        while (arrn[n2] != n2) {
            n2 = arrn[n2];
        }
        return n2;
    }

    private int unifiedRegionArea(int n2, int n3) {
        RectBounds rectBounds = this.dirtyRegions[n2];
        RectBounds rectBounds2 = this.dirtyRegions[n3];
        float f2 = rectBounds.getMinX() < rectBounds2.getMinX() ? rectBounds.getMinX() : rectBounds2.getMinX();
        float f3 = rectBounds.getMinY() < rectBounds2.getMinY() ? rectBounds.getMinY() : rectBounds2.getMinY();
        float f4 = rectBounds.getMaxX() > rectBounds2.getMaxX() ? rectBounds.getMaxX() : rectBounds2.getMaxX();
        float f5 = rectBounds.getMaxY() > rectBounds2.getMaxY() ? rectBounds.getMaxY() : rectBounds2.getMaxY();
        return (int)((f4 - f2) * (f5 - f3));
    }

    private void compress_heap() {
        int n2;
        assert (this.dirtyRegions.length == this.emptyIndex);
        if (this.heap == null) {
            n2 = this.dirtyRegions.length;
            this.heap = new int[n2 * (n2 - 1) / 2][3];
        }
        this.heapSize = this.heap.length;
        n2 = 0;
        for (int i2 = 0; i2 < this.dirtyRegions.length - 1; ++i2) {
            int n3 = i2 + 1;
            while (n3 < this.dirtyRegions.length) {
                this.heap[n2][0] = this.unifiedRegionArea(i2, n3);
                this.heap[n2][1] = i2;
                this.heap[n2++][2] = n3++;
            }
        }
        this.heapify();
        this.heapCompress();
    }
}

