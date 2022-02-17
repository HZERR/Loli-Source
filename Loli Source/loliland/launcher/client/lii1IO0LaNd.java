/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import loliland.launcher.client.I11Ii0LAND;
import loliland.launcher.client.l10lO11lanD;

public final class lii1IO0LaNd {
    private static final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(lii1IO0LaNd::OOOIilanD, TimeUnit.MINUTES.toNanos(1L));

    private lii1IO0LaNd() {
    }

    private static long OOOIilanD() {
        return TimeUnit.MILLISECONDS.toNanos(l10lO11lanD.I1O1I1LaNd("oshi.util.memoizer.expiration", 300));
    }

    public static long I1O1I1LaNd() {
        return (Long)I1O1I1LaNd.get();
    }

    public static Supplier I1O1I1LaNd(Supplier supplier, long l2) {
        return new I11Ii0LAND(supplier, l2);
    }

    public static Supplier I1O1I1LaNd(Supplier supplier) {
        return lii1IO0LaNd.I1O1I1LaNd(supplier, -1L);
    }
}

