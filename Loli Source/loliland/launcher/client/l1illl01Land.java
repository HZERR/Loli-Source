/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.Closeable;
import java.lang.reflect.Method;
import loliland.launcher.client.I010ii1Land;
import loliland.launcher.client.IIiI0O11laND;

final class l1illl01Land
implements IIiI0O11laND {
    static final l1illl01Land I1O1I1LaNd = new l1illl01Land();
    static final Method OOOIilanD = l1illl01Land.OOOIilanD();

    l1illl01Land() {
    }

    static boolean I1O1I1LaNd() {
        return OOOIilanD != null;
    }

    private static Method OOOIilanD() {
        try {
            return Throwable.class.getMethod("addSuppressed", Throwable.class);
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    @Override
    public void I1O1I1LaNd(Closeable closeable, Throwable throwable, Throwable throwable2) {
        if (throwable == throwable2) {
            return;
        }
        try {
            OOOIilanD.invoke(throwable, throwable2);
        }
        catch (Throwable throwable3) {
            I010ii1Land.I1O1I1LaNd.I1O1I1LaNd(closeable, throwable, throwable2);
        }
    }
}

