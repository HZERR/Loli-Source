/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.lIl1OlAND;

public abstract class il1iIIILaND
implements lIl1OlAND {
    @Override
    public String OOOIilanD() {
        return "unknown";
    }

    @Override
    public String lI00OlAND() {
        return "unknown";
    }

    @Override
    public String li0iOILAND() {
        return "unknown";
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("manufacturer=").append(this.I1O1I1LaNd()).append(", ");
        stringBuilder.append("name=").append(this.OOOIilanD()).append(", ");
        stringBuilder.append("description=").append(this.lI00OlAND()).append(", ");
        stringBuilder.append("version=").append(this.lli0OiIlAND()).append(", ");
        stringBuilder.append("release date=").append(this.li0iOILAND() == null ? "unknown" : this.li0iOILAND());
        return stringBuilder.toString();
    }
}

