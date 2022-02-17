/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.Closeable;
import java.util.logging.Level;
import loliland.launcher.client.IIiI0O11laND;
import loliland.launcher.client.OilOlAND;

final class I010ii1Land
implements IIiI0O11laND {
    static final I010ii1Land I1O1I1LaNd = new I010ii1Land();

    I010ii1Land() {
    }

    @Override
    public void I1O1I1LaNd(Closeable closeable, Throwable throwable, Throwable throwable2) {
        OilOlAND.lI00OlAND().log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, throwable2);
    }
}

