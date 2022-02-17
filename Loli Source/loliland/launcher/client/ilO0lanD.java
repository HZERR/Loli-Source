/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.Closeable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import loliland.launcher.client.OilOlAND;

class ilO0lanD
implements Closeable {
    final /* synthetic */ ResultSet I1O1I1LaNd;
    final /* synthetic */ OilOlAND OOOIilanD;

    ilO0lanD(OilOlAND oilOlAND, ResultSet resultSet) {
        this.OOOIilanD = oilOlAND;
        this.I1O1I1LaNd = resultSet;
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

