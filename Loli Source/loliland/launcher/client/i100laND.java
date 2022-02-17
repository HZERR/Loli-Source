/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.IIOOlanD;
import loliland.launcher.client.iOI1I1iLaNd;
import loliland.launcher.client.il00iLAnD;
import loliland.launcher.client.ll1ILAnd;

public final class i100laND {
    private static final il00iLAnD I1O1I1LaNd = il00iLAnD.INSTANCE;

    private i100laND() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static synchronized List I1O1I1LaNd() {
        ArrayList<iOI1I1iLaNd> arrayList = new ArrayList<iOI1I1iLaNd>();
        I1O1I1LaNd.setutxent();
        try {
            IIOOlanD iIOOlanD;
            while ((iIOOlanD = I1O1I1LaNd.getutxent()) != null) {
                String string;
                if (iIOOlanD.ut_type != 7 && iIOOlanD.ut_type != 6 || "LOGIN".equals(string = Native.toString(iIOOlanD.ut_user, StandardCharsets.US_ASCII))) continue;
                String string2 = Native.toString(iIOOlanD.ut_line, StandardCharsets.US_ASCII);
                String string3 = Native.toString(iIOOlanD.ut_host, StandardCharsets.US_ASCII);
                long l2 = iIOOlanD.ut_tv.tv_sec.longValue() * 1000L + iIOOlanD.ut_tv.tv_usec.longValue() / 1000L;
                if (string.isEmpty() || string2.isEmpty() || l2 < 0L || l2 > System.currentTimeMillis()) {
                    List list = ll1ILAnd.I1O1I1LaNd();
                    return list;
                }
                arrayList.add(new iOI1I1iLaNd(string, string2, l2, string3));
            }
            return arrayList;
        }
        finally {
            I1O1I1LaNd.endutxent();
        }
    }
}

