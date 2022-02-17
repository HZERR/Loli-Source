/*
 * Decompiled with CFR 0.150.
 */
package com.sun.webkit.network;

final class ExtendedTime
implements Comparable<ExtendedTime> {
    private final long baseTime;
    private final int subtime;

    ExtendedTime(long l2, int n2) {
        this.baseTime = l2;
        this.subtime = n2;
    }

    static ExtendedTime currentTime() {
        return new ExtendedTime(System.currentTimeMillis(), 0);
    }

    long baseTime() {
        return this.baseTime;
    }

    int subtime() {
        return this.subtime;
    }

    ExtendedTime incrementSubtime() {
        return new ExtendedTime(this.baseTime, this.subtime + 1);
    }

    @Override
    public int compareTo(ExtendedTime extendedTime) {
        int n2 = (int)(this.baseTime - extendedTime.baseTime);
        if (n2 != 0) {
            return n2;
        }
        return this.subtime - extendedTime.subtime;
    }

    public String toString() {
        return "[baseTime=" + this.baseTime + ", subtime=" + this.subtime + "]";
    }
}

