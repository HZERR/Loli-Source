/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lO1OIlI1laNd;

final class OO0iLaNd
extends lO1OIlI1laNd {
    private static final String I1O1I1LaNd = "diskutil cs list";
    private static final String OOOIilanD = "Logical Volume Group";
    private static final String lI00OlAND = "Physical Volume";
    private static final String lli0OiIlAND = "Logical Volume";

    OO0iLaNd(String string, Map map, Set set) {
        super(string, map, set);
    }

    static List lli0OiIlAND() {
        HashMap<String, Map> hashMap = new HashMap<String, Map>();
        HashMap<String, Set> hashMap2 = new HashMap<String, Set>();
        String string2 = null;
        boolean bl = false;
        boolean bl2 = false;
        for (String string3 : Iill1lanD.I1O1I1LaNd(I1O1I1LaNd)) {
            int n2;
            if (string3.contains(OOOIilanD)) {
                bl = true;
                continue;
            }
            if (bl) {
                n2 = string3.indexOf("Name:");
                if (n2 < 0) continue;
                string2 = string3.substring(n2 + 5).trim();
                bl = false;
                continue;
            }
            if (string3.contains(lI00OlAND)) {
                bl2 = true;
                continue;
            }
            if (string3.contains(lli0OiIlAND)) {
                bl2 = false;
                continue;
            }
            n2 = string3.indexOf("Disk:");
            if (n2 < 0) continue;
            if (bl2) {
                hashMap2.computeIfAbsent(string2, string -> new HashSet()).add(string3.substring(n2 + 5).trim());
                continue;
            }
            hashMap.computeIfAbsent(string2, string -> new HashMap()).put(string3.substring(n2 + 5).trim(), Collections.emptySet());
        }
        return hashMap.entrySet().stream().map(entry -> new OO0iLaNd((String)entry.getKey(), (Map)entry.getValue(), (Set)hashMap2.get(entry.getKey()))).collect(Collectors.toList());
    }
}

