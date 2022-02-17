/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import loliland.launcher.client.OilOlAND;

class lIiiil1laND
implements Closeable {
    final /* synthetic */ Connection I1O1I1LaNd;
    final /* synthetic */ OilOlAND OOOIilanD;

    lIiiil1laND(OilOlAND oilOlAND, Connection connection) {
        this.OOOIilanD = oilOlAND;
        this.I1O1I1LaNd = connection;
    }

    @Override
    public void close() throws IOException {
        try {
            this.I1O1I1LaNd.close();
        }
        catch (SQLException sQLException) {
            throw new IOException("Failed to close", sQLException);
        }
    }
}

