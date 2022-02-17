/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import loliland.launcher.client.IiI1l1IllANd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.il00iLAnD;

final class l0IilaND
extends IiI1l1IllANd {
    private static final il00iLAnD I1O1I1LaNd = il00iLAnD.INSTANCE;

    l0IilaND() {
    }

    @Override
    public String OOOIilanD() {
        byte[] arrby = new byte[256];
        if (0 != I1O1I1LaNd.gethostname(arrby, arrby.length)) {
            return super.OOOIilanD();
        }
        return Native.toString(arrby);
    }

    @Override
    public String lli0OiIlAND() {
        return l0IilaND.I1O1I1LaNd(Iill1lanD.I1O1I1LaNd("route get -inet default"));
    }

    @Override
    public String li0iOILAND() {
        return l0IilaND.I1O1I1LaNd(Iill1lanD.I1O1I1LaNd("route get -inet6 default"));
    }
}

