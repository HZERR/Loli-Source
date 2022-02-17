/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.IiiilAnD;
import loliland.launcher.client.i0l10iiilaNd;
import loliland.launcher.client.l11IliilLANd;
import loliland.launcher.client.ll1l10l0LaNd;

public class lll1Il0LaND
extends i0l10iiilaNd {
    @Override
    public ll1l10l0LaNd lli0OiIlAND() {
        return IiiilAnD.I1O1I1LaNd("netstat -s -p tcp");
    }

    @Override
    public l11IliilLANd li0iOILAND() {
        return IiiilAnD.OOOIilanD("netstat -s -p udp");
    }
}

