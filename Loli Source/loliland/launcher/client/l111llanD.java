/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.iiIIIlO1lANd;

public class l111llanD {
    private final String I1O1I1LaNd;
    private final String OOOIilanD;
    private final String lI00OlAND;
    private final String lli0OiIlAND;

    public l111llanD(String string, String string2, String string3) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
        this.lI00OlAND = string3;
        StringBuilder stringBuilder = new StringBuilder(this.I1O1I1LaNd() != null ? this.I1O1I1LaNd() : "unknown");
        if (!iiIIIlO1lANd.I1O1I1LaNd(this.OOOIilanD())) {
            stringBuilder.append(" (").append(this.OOOIilanD()).append(')');
        }
        if (!iiIIIlO1lANd.I1O1I1LaNd(this.lI00OlAND())) {
            stringBuilder.append(" build ").append(this.lI00OlAND());
        }
        this.lli0OiIlAND = stringBuilder.toString();
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

    public String toString() {
        return this.lli0OiIlAND;
    }
}

