/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.util.Collection;
import java.util.Set;
import loliland.launcher.client.iOO1IiI1lAnD;
import loliland.launcher.client.iiO0ilaNd;
import loliland.launcher.client.li0i01LanD;

public final class O1iOOlllANd {
    private static final String I1O1I1LaNd = "Win32_Process";

    private O1iOOlllANd() {
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd(Set set) {
        StringBuilder stringBuilder = new StringBuilder(I1O1I1LaNd);
        if (set != null) {
            boolean bl = true;
            for (Integer n2 : set) {
                if (bl) {
                    stringBuilder.append(" WHERE ProcessID=");
                    bl = false;
                } else {
                    stringBuilder.append(" OR ProcessID=");
                }
                stringBuilder.append(n2);
            }
        }
        WbemcliUtil.WmiQuery<iOO1IiI1lAnD> wmiQuery = new WbemcliUtil.WmiQuery<iOO1IiI1lAnD>(stringBuilder.toString(), iOO1IiI1lAnD.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }

    public static WbemcliUtil.WmiResult I1O1I1LaNd(Collection collection) {
        StringBuilder stringBuilder = new StringBuilder(I1O1I1LaNd);
        if (collection != null) {
            boolean bl = true;
            for (Integer n2 : collection) {
                if (bl) {
                    stringBuilder.append(" WHERE ProcessID=");
                    bl = false;
                } else {
                    stringBuilder.append(" OR ProcessID=");
                }
                stringBuilder.append(n2);
            }
        }
        WbemcliUtil.WmiQuery<iiO0ilaNd> wmiQuery = new WbemcliUtil.WmiQuery<iiO0ilaNd>(stringBuilder.toString(), iiO0ilaNd.class);
        return li0i01LanD.I1O1I1LaNd().I1O1I1LaNd(wmiQuery);
    }
}

