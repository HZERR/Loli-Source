/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.i00ilaNd;

public class i0Oi1LANd {
    private final String I1O1I1LaNd;
    private final String OOOIilanD;
    private final String lI00OlAND;
    private final String lli0OiIlAND;
    private final long li0iOILAND;
    private final int O1il1llOLANd;
    private final int Oill1LAnD;
    private final String lIOILand;

    public i0Oi1LANd(String string, String string2, String string3, String string4, long l2, int n2, int n3, String string5) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
        this.lI00OlAND = string3;
        this.lli0OiIlAND = string4;
        this.li0iOILAND = l2;
        this.O1il1llOLANd = n2;
        this.Oill1LAnD = n3;
        this.lIOILand = string5;
    }

    public String I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    public String OOOIilanD() {
        return this.OOOIilanD;
    }

    public String lI00OlAND() {
        return this.lI00OlAND;
    }

    public String lli0OiIlAND() {
        return this.lli0OiIlAND;
    }

    public long li0iOILAND() {
        return this.li0iOILAND;
    }

    public int O1il1llOLANd() {
        return this.O1il1llOLANd;
    }

    public int Oill1LAnD() {
        return this.Oill1LAnD;
    }

    public String lIOILand() {
        return this.lIOILand;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.I1O1I1LaNd()).append(": ");
        stringBuilder.append(this.OOOIilanD()).append(" ");
        stringBuilder.append("(").append(this.lI00OlAND()).append(") ");
        stringBuilder.append("Maj:Min=").append(this.O1il1llOLANd()).append(":").append(this.Oill1LAnD()).append(", ");
        stringBuilder.append("size: ").append(i00ilaNd.OOOIilanD(this.li0iOILAND()));
        stringBuilder.append(this.lIOILand().isEmpty() ? "" : " @ " + this.lIOILand());
        return stringBuilder.toString();
    }
}

