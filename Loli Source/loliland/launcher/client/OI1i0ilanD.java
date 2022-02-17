/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.i00ilaNd;
import loliland.launcher.client.il10laNd;

public abstract class OI1i0ilanD
implements il10laNd {
    private final String I1O1I1LaNd;
    private final String OOOIilanD;
    private final String lI00OlAND;
    private final long lli0OiIlAND;

    protected OI1i0ilanD(String string, String string2, String string3, long l2) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
        this.lI00OlAND = string3;
        this.lli0OiIlAND = l2;
    }

    @Override
    public String I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    @Override
    public String OOOIilanD() {
        return this.OOOIilanD;
    }

    @Override
    public String lI00OlAND() {
        return this.lI00OlAND;
    }

    @Override
    public long lli0OiIlAND() {
        return this.lli0OiIlAND;
    }

    public String toString() {
        boolean bl = this.li0iOILAND() > 0L || this.Oill1LAnD() > 0L;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.I1O1I1LaNd()).append(": ");
        stringBuilder.append("(model: ").append(this.OOOIilanD());
        stringBuilder.append(" - S/N: ").append(this.lI00OlAND()).append(") ");
        stringBuilder.append("size: ").append(this.lli0OiIlAND() > 0L ? i00ilaNd.OOOIilanD(this.lli0OiIlAND()) : "?").append(", ");
        stringBuilder.append("reads: ").append(bl ? Long.valueOf(this.li0iOILAND()) : "?");
        stringBuilder.append(" (").append(bl ? i00ilaNd.I1O1I1LaNd(this.O1il1llOLANd()) : "?").append("), ");
        stringBuilder.append("writes: ").append(bl ? Long.valueOf(this.Oill1LAnD()) : "?");
        stringBuilder.append(" (").append(bl ? i00ilaNd.I1O1I1LaNd(this.lIOILand()) : "?").append("), ");
        stringBuilder.append("xfer: ").append(bl ? Long.valueOf(this.iilIi1laND()) : "?");
        return stringBuilder.toString();
    }
}

