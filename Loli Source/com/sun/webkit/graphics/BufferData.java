/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.graphics;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

final class BufferData {
    private final AtomicInteger idCount = new AtomicInteger(0);
    private final HashMap<Integer, String> strMap = new HashMap();
    private final HashMap<Integer, int[]> intArrMap = new HashMap();
    private final HashMap<Integer, float[]> floatArrMap = new HashMap();
    private ByteBuffer buffer;

    BufferData() {
    }

    private int createID() {
        return this.idCount.incrementAndGet();
    }

    int addIntArray(int[] arrn) {
        int n2 = this.createID();
        this.intArrMap.put(n2, arrn);
        return n2;
    }

    int[] getIntArray(int n2) {
        return this.intArrMap.get(n2);
    }

    int addFloatArray(float[] arrf) {
        int n2 = this.createID();
        this.floatArrMap.put(n2, arrf);
        return n2;
    }

    float[] getFloatArray(int n2) {
        return this.floatArrMap.get(n2);
    }

    int addString(String string) {
        int n2 = this.createID();
        this.strMap.put(n2, string);
        return n2;
    }

    String getString(int n2) {
        return this.strMap.get(n2);
    }

    ByteBuffer getBuffer() {
        return this.buffer;
    }

    void setBuffer(ByteBuffer byteBuffer) {
        this.buffer = byteBuffer;
    }
}

