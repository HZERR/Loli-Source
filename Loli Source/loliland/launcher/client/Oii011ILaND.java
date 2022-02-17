/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.i00ilaNd;

public class Oii011ILaND {
    private final String I1O1I1LaNd;
    private final long OOOIilanD;
    private final long lI00OlAND;
    private final String lli0OiIlAND;
    private final String li0iOILAND;

    public Oii011ILaND(String string, long l2, long l3, String string2, String string3) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = l2;
        this.lI00OlAND = l3;
        this.lli0OiIlAND = string2;
        this.li0iOILAND = string3;
    }

    public String I1O1I1LaNd() {
        return this.I1O1I1LaNd;
    }

    public long OOOIilanD() {
        return this.OOOIilanD;
    }

    public long lI00OlAND() {
        return this.lI00OlAND;
    }

    public String lli0OiIlAND() {
        return this.lli0OiIlAND;
    }

    public String li0iOILAND() {
        return this.li0iOILAND;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bank label: " + this.I1O1I1LaNd());
        stringBuilder.append(", Capacity: " + i00ilaNd.I1O1I1LaNd(this.OOOIilanD()));
        stringBuilder.append(", Clock speed: " + i00ilaNd.lI00OlAND(this.lI00OlAND()));
        stringBuilder.append(", Manufacturer: " + this.lli0OiIlAND());
        stringBuilder.append(", Memory type: " + this.li0iOILAND());
        return stringBuilder.toString();
    }
}

