/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.iIlI0LAND;
import loliland.launcher.client.lIl1OlAND;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1OLAnd;

public abstract class IiiIOIlanD
implements iIlI0LAND {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(this::Oill1LAnD);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(this::lIOILand);

    @Override
    public lIl1OlAND li0iOILAND() {
        return (lIl1OlAND)this.I1O1I1LaNd.get();
    }

    protected abstract lIl1OlAND Oill1LAnD();

    @Override
    public ll1OLAnd O1il1llOLANd() {
        return (ll1OLAnd)this.OOOIilanD.get();
    }

    protected abstract ll1OLAnd lIOILand();

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("manufacturer=").append(this.I1O1I1LaNd()).append(", ");
        stringBuilder.append("model=").append(this.OOOIilanD()).append(", ");
        stringBuilder.append("serial number=").append(this.lI00OlAND()).append(", ");
        stringBuilder.append("uuid=").append(this.lli0OiIlAND());
        return stringBuilder.toString();
    }
}

