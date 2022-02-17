/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Logger;
import loliland.launcher.client.I010ii1Land;
import loliland.launcher.client.II10iil1lanD;
import loliland.launcher.client.IIiI0O11laND;
import loliland.launcher.client.ilO0lanD;
import loliland.launcher.client.l1illl01Land;
import loliland.launcher.client.lIiiil1laND;

public final class OilOlAND
implements Closeable {
    private static final Logger OOOIilanD = Logger.getLogger(OilOlAND.class.getCanonicalName());
    private static final IIiI0O11laND lI00OlAND = l1illl01Land.I1O1I1LaNd() ? l1illl01Land.I1O1I1LaNd : I010ii1Land.I1O1I1LaNd;
    final IIiI0O11laND I1O1I1LaNd;
    private final Deque lli0OiIlAND = new ArrayDeque(4);
    private Throwable li0iOILAND;

    public static OilOlAND I1O1I1LaNd() {
        return new OilOlAND(lI00OlAND);
    }

    OilOlAND(IIiI0O11laND iIiI0O11laND) {
        this.I1O1I1LaNd = Preconditions.checkNotNull(iIiI0O11laND);
    }

    public Closeable I1O1I1LaNd(Closeable closeable) {
        this.lli0OiIlAND.push(closeable);
        return closeable;
    }

    public Connection I1O1I1LaNd(Connection connection) {
        this.I1O1I1LaNd(new lIiiil1laND(this, connection));
        return connection;
    }

    public Statement I1O1I1LaNd(Statement statement) {
        this.I1O1I1LaNd(new II10iil1lanD(this, statement));
        return statement;
    }

    public ResultSet I1O1I1LaNd(ResultSet resultSet) {
        this.I1O1I1LaNd(new ilO0lanD(this, resultSet));
        return resultSet;
    }

    public RuntimeException I1O1I1LaNd(Throwable throwable) throws IOException {
        this.li0iOILAND = throwable;
        Throwables.propagateIfPossible(this.li0iOILAND, IOException.class);
        throw Throwables.propagate(throwable);
    }

    public RuntimeException I1O1I1LaNd(Throwable throwable, Class class_) throws IOException, Exception, Exception {
        this.li0iOILAND = throwable;
        Throwables.propagateIfPossible(this.li0iOILAND, IOException.class);
        Throwables.propagateIfPossible(throwable, class_);
        throw Throwables.propagate(throwable);
    }

    public RuntimeException I1O1I1LaNd(Throwable throwable, Class class_, Class class_2) throws IOException, Exception, Exception, Exception {
        this.li0iOILAND = throwable;
        Throwables.propagateIfPossible(this.li0iOILAND, IOException.class);
        Throwables.propagateIfPossible(throwable, class_, class_2);
        throw Throwables.propagate(throwable);
    }

    @Override
    public void close() throws IOException {
        Throwable throwable = this.li0iOILAND;
        while (!this.lli0OiIlAND.isEmpty()) {
            Closeable closeable = (Closeable)this.lli0OiIlAND.pop();
            try {
                closeable.close();
            }
            catch (Throwable throwable2) {
                if (throwable == null) {
                    throwable = throwable2;
                    continue;
                }
                this.I1O1I1LaNd.I1O1I1LaNd(closeable, throwable, throwable2);
            }
        }
        if (this.li0iOILAND == null && throwable != null) {
            Throwables.propagateIfPossible(throwable, IOException.class);
            throw new AssertionError((Object)throwable);
        }
    }

    public void OOOIilanD() {
        try {
            this.close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    static /* synthetic */ Logger lI00OlAND() {
        return OOOIilanD;
    }
}

