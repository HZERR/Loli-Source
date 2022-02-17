/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.OOIlLAnD;

public abstract class ilI1l1IOLanD
implements OOIlLAnD {
    private String I1O1I1LaNd;
    private String OOOIilanD;
    private String lI00OlAND;

    protected ilI1l1IOLanD(String string, String string2, String string3) {
        this.I1O1I1LaNd = string;
        this.OOOIilanD = string2;
        this.lI00OlAND = string3;
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

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SoundCard@");
        stringBuilder.append(Integer.toHexString(this.hashCode()));
        stringBuilder.append(" [name=");
        stringBuilder.append(this.OOOIilanD);
        stringBuilder.append(", kernelVersion=");
        stringBuilder.append(this.I1O1I1LaNd);
        stringBuilder.append(", codec=");
        stringBuilder.append(this.lI00OlAND);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }
}

