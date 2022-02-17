/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.mac.SystemB;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class liOIOOLANd {
    private liOIOOLANd() {
    }

    public static Map I1O1I1LaNd() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        int n2 = SystemB.INSTANCE.getfsstat64(null, 0, 0);
        SystemB.Statfs[] arrstatfs = new SystemB.Statfs[n2];
        SystemB.INSTANCE.getfsstat64(arrstatfs, n2 * new SystemB.Statfs().size(), 16);
        for (SystemB.Statfs statfs : arrstatfs) {
            String string = Native.toString(statfs.f_mntfromname, StandardCharsets.UTF_8);
            hashMap.put(string.replace("/dev/", ""), Native.toString(statfs.f_mntonname, StandardCharsets.UTF_8));
        }
        return hashMap;
    }
}

