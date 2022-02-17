/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.LockableResource;
import com.sun.scenario.effect.impl.Renderer;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class FloatMap {
    private final int width;
    private final int height;
    private final FloatBuffer buf;
    private boolean cacheValid;
    private Map<FilterContext, Entry> cache;

    public FloatMap(int n2, int n3) {
        if (n2 <= 0 || n2 > 4096 || n3 <= 0 || n3 > 4096) {
            throw new IllegalArgumentException("Width and height must be in the range [1, 4096]");
        }
        this.width = n2;
        this.height = n3;
        int n4 = n2 * n3 * 4;
        this.buf = FloatBuffer.wrap(new float[n4]);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public float[] getData() {
        return this.buf.array();
    }

    public FloatBuffer getBuffer() {
        return this.buf;
    }

    public float getSample(int n2, int n3, int n4) {
        return this.buf.get((n2 + n3 * this.width) * 4 + n4);
    }

    public void setSample(int n2, int n3, int n4, float f2) {
        this.buf.put((n2 + n3 * this.width) * 4 + n4, f2);
        this.cacheValid = false;
    }

    public void setSamples(int n2, int n3, float f2) {
        int n4 = (n2 + n3 * this.width) * 4;
        this.buf.put(n4 + 0, f2);
        this.cacheValid = false;
    }

    public void setSamples(int n2, int n3, float f2, float f3) {
        int n4 = (n2 + n3 * this.width) * 4;
        this.buf.put(n4 + 0, f2);
        this.buf.put(n4 + 1, f3);
        this.cacheValid = false;
    }

    public void setSamples(int n2, int n3, float f2, float f3, float f4) {
        int n4 = (n2 + n3 * this.width) * 4;
        this.buf.put(n4 + 0, f2);
        this.buf.put(n4 + 1, f3);
        this.buf.put(n4 + 2, f4);
        this.cacheValid = false;
    }

    public void setSamples(int n2, int n3, float f2, float f3, float f4, float f5) {
        int n4 = (n2 + n3 * this.width) * 4;
        this.buf.put(n4 + 0, f2);
        this.buf.put(n4 + 1, f3);
        this.buf.put(n4 + 2, f4);
        this.buf.put(n4 + 3, f5);
        this.cacheValid = false;
    }

    public void put(float[] arrf) {
        this.buf.rewind();
        this.buf.put(arrf);
        this.buf.rewind();
        this.cacheValid = false;
    }

    public LockableResource getAccelData(FilterContext filterContext) {
        Entry entry2;
        if (this.cache == null) {
            this.cache = new HashMap<FilterContext, Entry>();
        } else if (!this.cacheValid) {
            for (Entry entry2 : this.cache.values()) {
                entry2.valid = false;
            }
            this.cacheValid = true;
        }
        Renderer renderer = Renderer.getRenderer(filterContext);
        entry2 = this.cache.get(filterContext);
        if (entry2 != null) {
            entry2.texture.lock();
            if (entry2.texture.isLost()) {
                entry2.texture.unlock();
                this.cache.remove(filterContext);
                entry2 = null;
            }
        }
        if (entry2 == null) {
            LockableResource lockableResource = renderer.createFloatTexture(this.width, this.height);
            entry2 = new Entry(lockableResource);
            this.cache.put(filterContext, entry2);
        }
        if (!entry2.valid) {
            renderer.updateFloatTexture(entry2.texture, this);
            entry2.valid = true;
        }
        return entry2.texture;
    }

    private static class Entry {
        LockableResource texture;
        boolean valid;

        Entry(LockableResource lockableResource) {
            this.texture = lockableResource;
        }
    }
}

