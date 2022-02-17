/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.Iil1Ol1laNd;
import loliland.launcher.client.iOI1I1iLaNd;
import loliland.launcher.client.l0OI1iOlAnD;
import loliland.launcher.client.ll1ILAnd;

public final class II1II0laND {
    private static final Iil1Ol1laNd I1O1I1LaNd = Iil1Ol1laNd.INSTANCE;

    private II1II0laND() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static synchronized List I1O1I1LaNd() {
        ArrayList<iOI1I1iLaNd> arrayList = new ArrayList<iOI1I1iLaNd>();
        I1O1I1LaNd.setutxent();
        try {
            l0OI1iOlAnD l0OI1iOlAnD2;
            while ((l0OI1iOlAnD2 = I1O1I1LaNd.getutxent()) != null) {
                if (l0OI1iOlAnD2.ut_type != 7 && l0OI1iOlAnD2.ut_type != 6) continue;
                String string = Native.toString(l0OI1iOlAnD2.ut_user, StandardCharsets.US_ASCII);
                String string2 = Native.toString(l0OI1iOlAnD2.ut_line, StandardCharsets.US_ASCII);
                String string3 = Native.toString(l0OI1iOlAnD2.ut_host, StandardCharsets.US_ASCII);
                long l2 = l0OI1iOlAnD2.ut_tv.tv_sec * 1000L + l0OI1iOlAnD2.ut_tv.tv_usec / 1000L;
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

