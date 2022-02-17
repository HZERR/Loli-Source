/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.l110LAND;

public abstract class iIl0il1lAnD
implements l110LAND {
    private final String I1O1I1LaNd;
    private final String OOOIilanD;
    private final String lI00OlAND;
    private final String lli0OiIlAND;
    private long li0iOILAND;

    protected iIl0il1lAnD(String string, String string2, String string3, String string4, long l2) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
        this.lI00OlAND = string3;
        this.lli0OiIlAND = string4;
        this.li0iOILAND = l2;
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
    public String lli0OiIlAND() {
        return this.lli0OiIlAND;
    }

    @Override
    public long li0iOILAND() {
        return this.li0iOILAND;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("GraphicsCard@");
        stringBuilder.append(Integer.toHexString(this.hashCode()));
        stringBuilder.append(" [name=");
        stringBuilder.append(this.I1O1I1LaNd);
        stringBuilder.append(", deviceId=");
        stringBuilder.append(this.OOOIilanD);
        stringBuilder.append(", vendor=");
        stringBuilder.append(this.lI00OlAND);
        stringBuilder.append(", vRam=");
        stringBuilder.append(this.li0iOILAND);
        stringBuilder.append(", versionInfo=[");
        stringBuilder.append(this.lli0OiIlAND);
        stringBuilder.append("]]");
        return stringBuilder.toString();
    }
}

