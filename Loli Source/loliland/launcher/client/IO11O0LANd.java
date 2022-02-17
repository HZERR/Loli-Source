/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.O0lOlOOlaNd;
import loliland.launcher.client.iOI1I1iLaNd;
import loliland.launcher.client.l1IOlANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.ll1ILAnd;

public final class IO11O0LANd {
    private static final l1IOlANd I1O1I1LaNd = l1IOlANd.INSTANCE;

    private IO11O0LANd() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static synchronized List I1O1I1LaNd() {
        ArrayList<iOI1I1iLaNd> arrayList = new ArrayList<iOI1I1iLaNd>();
        I1O1I1LaNd.setutxent();
        try {
            O0lOlOOlaNd o0lOlOOlaNd;
            while ((o0lOlOOlaNd = I1O1I1LaNd.getutxent()) != null) {
                if (o0lOlOOlaNd.ut_type != 7 && o0lOlOOlaNd.ut_type != 6) continue;
                String string = Native.toString(o0lOlOOlaNd.ut_user, Charset.defaultCharset());
                String string2 = Native.toString(o0lOlOOlaNd.ut_line, Charset.defaultCharset());
                String string3 = lOilLanD.OOOIilanD(o0lOlOOlaNd.ut_addr_v6);
                long l2 = (long)o0lOlOOlaNd.ut_tv.tv_sec * 1000L + (long)o0lOlOOlaNd.ut_tv.tv_usec / 1000L;
                if (string.isEmpty() || string2.isEmpty() || l2 < 0L || l2 > System.currentTimeMillis()) {
                    List list = ll1ILAnd.I1O1I1LaNd();
                    return list;
                }
                arrayList.add(new iOI1I1iLaNd(string, string2, l2, string3));
            }
        }
        finally {
            I1O1I1LaNd.endutxent();
        }
        return arrayList;
    }
}

