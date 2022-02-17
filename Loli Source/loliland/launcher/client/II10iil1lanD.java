/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import loliland.launcher.client.OilOlAND;

class II10iil1lanD
implements Closeable {
    final /* synthetic */ Statement I1O1I1LaNd;
    final /* synthetic */ OilOlAND OOOIilanD;

    II10iil1lanD(OilOlAND oilOlAND, Statement statement) {
        this.OOOIilanD = oilOlAND;
        this.I1O1I1LaNd = statement;
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

