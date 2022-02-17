/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public final class i00ilaNd {
    private static final long OOOIilanD = 1024L;
    private static final long lI00OlAND = 0x100000L;
    private static final long lli0OiIlAND = 0x40000000L;
    private static final long li0iOILAND = 0x10000000000L;
    private static final long O1il1llOLANd = 0x4000000000000L;
    private static final long Oill1LAnD = 0x1000000000000000L;
    private static final long lIOILand = 1000L;
    private static final long lil0liLand = 1000000L;
    private static final long iilIi1laND = 1000000000L;
    private static final long lli011lLANd = 1000000000000L;
    private static final long l0illAND = 1000000000000000L;
    private static final long IO11O0LANd = 1000000000000000000L;
    private static final BigInteger l11lLANd = BigInteger.ONE.shiftLeft(64);
    public static final String I1O1I1LaNd = "0x%08X";

    private i00ilaNd() {
    }

    public static String I1O1I1LaNd(long l2) {
        if (l2 == 1L) {
            return String.format("%d byte", l2);
        }
        if (l2 < 1024L) {
            return String.format("%d bytes", l2);
        }
        if (l2 < 0x100000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 1024L, "KiB");
        }
        if (l2 < 0x40000000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 0x100000L, "MiB");
        }
        if (l2 < 0x10000000000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 0x40000000L, "GiB");
        }
        if (l2 < 0x4000000000000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 0x10000000000L, "TiB");
        }
        if (l2 < 0x1000000000000000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 0x4000000000000L, "PiB");
        }
        return i00ilaNd.I1O1I1LaNd(l2, 0x1000000000000000L, "EiB");
    }

    private static String I1O1I1LaNd(long l2, long l3, String string) {
        if (l2 % l3 == 0L) {
            return String.format("%d %s", l2 / l3, string);
        }
        return String.format("%.1f %s", (double)l2 / (double)l3, string);
    }

    public static String OOOIilanD(long l2) {
        if (l2 == 1L) {
            return String.format("%d byte", l2);
        }
        if (l2 < 1000L) {
            return String.format("%d bytes", l2);
        }
        return i00ilaNd.I1O1I1LaNd(l2, "B");
    }

    public static String lI00OlAND(long l2) {
        return i00ilaNd.I1O1I1LaNd(l2, "Hz");
    }

    public static String I1O1I1LaNd(long l2, String string) {
        if (l2 < 1000L) {
            return String.format("%d %s", l2, string).trim();
        }
        if (l2 < 1000000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 1000L, "K" + string);
        }
        if (l2 < 1000000000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 1000000L, "M" + string);
        }
        if (l2 < 1000000000000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 1000000000L, "G" + string);
        }
        if (l2 < 1000000000000000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 1000000000000L, "T" + string);
        }
        if (l2 < 1000000000000000000L) {
            return i00ilaNd.I1O1I1LaNd(l2, 1000000000000000L, "P" + string);
        }
        return i00ilaNd.I1O1I1LaNd(l2, 1000000000000000000L, "E" + string);
    }

    public static String lli0OiIlAND(long l2) {
        long l3 = l2;
        long l4 = TimeUnit.SECONDS.toDays(l3);
        long l5 = TimeUnit.SECONDS.toHours(l3 -= TimeUnit.DAYS.toSeconds(l4));
        long l6 = TimeUnit.SECONDS.toMinutes(l3 -= TimeUnit.HOURS.toSeconds(l5));
        long l7 = l3 -= TimeUnit.MINUTES.toSeconds(l6);
        return String.format("%d days, %02d:%02d:%02d", l4, l5, l6, l7);
    }

    public static long I1O1I1LaNd(int n2) {
        return (long)n2 & 0xFFFFFFFFL;
    }

    public static String OOOIilanD(int n2) {
        if (n2 >= 0) {
            return Integer.toString(n2);
        }
        return Long.toString(i00ilaNd.I1O1I1LaNd(n2));
    }

    public static String li0iOILAND(long l2) {
        if (l2 >= 0L) {
            return Long.toString(l2);
        }
        return BigInteger.valueOf(l2).add(l11lLANd).toString();
    }

    public static String lI00OlAND(int n2) {
        return String.format(I1O1I1LaNd, n2);
    }

    public static int I1O1I1LaNd(double d2) {
        return (int)Math.round(d2);
    }
}

