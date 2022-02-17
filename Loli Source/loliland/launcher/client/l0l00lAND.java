/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lii1IO0LaNd;

public final class l0l00lAND {
    private static final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(l0l00lAND::I1O1I1LaNd, TimeUnit.MINUTES.toNanos(1L));
    private static final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(l0l00lAND::OOOIilanD, TimeUnit.MINUTES.toNanos(1L));

    private l0l00lAND() {
    }

    public static String I1O1I1LaNd(String string) {
        return ((Map)I1O1I1LaNd.get()).getOrDefault(string, "unknown");
    }

    public static String OOOIilanD(String string) {
        return ((Map)OOOIilanD.get()).getOrDefault(string, "unknown");
    }

    private static Map I1O1I1LaNd() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        List list = Iill1lanD.I1O1I1LaNd("getent passwd");
        for (String string : list) {
            String[] arrstring = string.split(":");
            if (arrstring.length <= 2) continue;
            String string2 = arrstring[0];
            String string3 = arrstring[2];
            hashMap.putIfAbsent(string3, string2);
        }
        return hashMap;
    }

    private static Map OOOIilanD() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        List list = Iill1lanD.I1O1I1LaNd("getent group");
        for (String string : list) {
            String[] arrstring = string.split(":");
            if (arrstring.length <= 2) continue;
            String string2 = arrstring[0];
            String string3 = arrstring[2];
            hashMap.putIfAbsent(string3, string2);
        }
        return hashMap;
    }
}

