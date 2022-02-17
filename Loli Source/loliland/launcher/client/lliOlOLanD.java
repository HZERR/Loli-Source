/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import loliland.launcher.client.IiI1l1IllANd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OO1lILANd;
import loliland.launcher.client.lOilLanD;

final class lliOlOLanD
extends IiI1l1IllANd {
    private static final OO1lILANd I1O1I1LaNd = OO1lILANd.INSTANCE;

    lliOlOLanD() {
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
        return lliOlOLanD.I1O1I1LaNd("netstat -rnf inet");
    }

    @Override
    public String li0iOILAND() {
        return lliOlOLanD.I1O1I1LaNd("netstat -rnf inet6");
    }

    private static String I1O1I1LaNd(String string) {
        for (String string2 : Iill1lanD.I1O1I1LaNd(string)) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string2);
            if (arrstring.length <= 7 || !"default".equals(arrstring[0])) continue;
            return arrstring[1];
        }
        return "unknown";
    }
}

