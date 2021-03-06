/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl;

import com.sun.prism.impl.PrismSettings;
import java.util.HashMap;
import java.util.Map;

public class PrismTrace {
    private static final boolean enabled = PrismSettings.printAllocs;
    private static Map<Long, Long> texData;
    private static long texBytes;
    private static Map<Long, Long> rttData;
    private static long rttBytes;

    private static String summary(long l2, long l3, String string) {
        return String.format("%s=%d@%,dKB", string, l2, l3 >> 10);
    }

    private static String summary(SummaryType summaryType) {
        switch (summaryType) {
            case TYPE_TEX: {
                return PrismTrace.summary(texData.size(), texBytes, " tex");
            }
            case TYPE_RTT: {
                return PrismTrace.summary(rttData.size(), rttBytes, " rtt");
            }
            case TYPE_ALL: {
                return PrismTrace.summary(texData.size() + rttData.size(), texBytes + rttBytes, " combined");
            }
        }
        return "ERROR";
    }

    private static long computeSize(int n2, int n3, int n4) {
        long l2 = n2;
        l2 *= (long)n3;
        return l2 *= (long)n4;
    }

    public static void textureCreated(long l2, int n2, int n3, long l3) {
        if (!enabled) {
            return;
        }
        texData.put(l2, l3);
        texBytes += l3;
        System.out.println("Created Texture: id=" + l2 + ", " + n2 + "x" + n3 + " pixels, " + l3 + " bytes," + PrismTrace.summary(SummaryType.TYPE_TEX) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void textureCreated(long l2, int n2, int n3, int n4) {
        if (!enabled) {
            return;
        }
        long l3 = PrismTrace.computeSize(n2, n3, n4);
        texData.put(l2, l3);
        texBytes += l3;
        System.out.println("Created Texture: id=" + l2 + ", " + n2 + "x" + n3 + " pixels, " + l3 + " bytes," + PrismTrace.summary(SummaryType.TYPE_TEX) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void textureDisposed(long l2) {
        if (!enabled) {
            return;
        }
        Long l3 = texData.remove(l2);
        if (l3 == null) {
            throw new InternalError("Disposing unknown Texture " + l2);
        }
        texBytes -= l3.longValue();
        System.out.println("Disposed Texture: id=" + l2 + ", " + l3 + " bytes" + PrismTrace.summary(SummaryType.TYPE_TEX) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void rttCreated(long l2, int n2, int n3, long l3) {
        if (!enabled) {
            return;
        }
        rttData.put(l2, l3);
        rttBytes += l3;
        System.out.println("Created RTTexture: id=" + l2 + ", " + n2 + "x" + n3 + " pixels, " + l3 + " bytes," + PrismTrace.summary(SummaryType.TYPE_RTT) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void rttCreated(long l2, int n2, int n3, int n4) {
        if (!enabled) {
            return;
        }
        long l3 = PrismTrace.computeSize(n2, n3, n4);
        rttData.put(l2, l3);
        rttBytes += l3;
        System.out.println("Created RTTexture: id=" + l2 + ", " + n2 + "x" + n3 + " pixels, " + l3 + " bytes," + PrismTrace.summary(SummaryType.TYPE_RTT) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    public static void rttDisposed(long l2) {
        if (!enabled) {
            return;
        }
        Long l3 = rttData.remove(l2);
        if (l3 == null) {
            throw new InternalError("Disposing unknown RTTexture " + l2);
        }
        rttBytes -= l3.longValue();
        System.out.println("Disposed RTTexture: id=" + l2 + ", " + l3 + " bytes" + PrismTrace.summary(SummaryType.TYPE_RTT) + PrismTrace.summary(SummaryType.TYPE_ALL));
    }

    private PrismTrace() {
    }

    static {
        if (enabled) {
            texData = new HashMap<Long, Long>();
            rttData = new HashMap<Long, Long>();
            Runtime.getRuntime().addShutdownHook(new Thread("RTT printAlloc shutdown hook"){

                @Override
                public void run() {
                    System.out.println("Final Texture resources:" + PrismTrace.summary(SummaryType.TYPE_TEX) + PrismTrace.summary(SummaryType.TYPE_RTT) + PrismTrace.summary(SummaryType.TYPE_ALL));
                }
            });
        }
    }

    private static enum SummaryType {
        TYPE_TEX,
        TYPE_RTT,
        TYPE_ALL;

    }
}

