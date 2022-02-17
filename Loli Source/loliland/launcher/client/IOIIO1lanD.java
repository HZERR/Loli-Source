/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.i00ilaNd;
import loliland.launcher.client.lilO00LANd;

public abstract class IOIIO1lanD
implements lilO00LANd {
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Swap Used/Avail: ");
        stringBuilder.append(i00ilaNd.I1O1I1LaNd(this.OOOIilanD()));
        stringBuilder.append("/");
        stringBuilder.append(i00ilaNd.I1O1I1LaNd(this.I1O1I1LaNd()));
        stringBuilder.append(", Virtual Memory In Use/Max=");
        stringBuilder.append(i00ilaNd.I1O1I1LaNd(this.lli0OiIlAND()));
        stringBuilder.append("/");
        stringBuilder.append(i00ilaNd.I1O1I1LaNd(this.lI00OlAND()));
        return stringBuilder.toString();
    }
}

