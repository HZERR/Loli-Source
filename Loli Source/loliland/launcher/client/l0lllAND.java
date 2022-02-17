/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.Iill1lanD;

public final class l0lllAND {
    private static final String I1O1I1LaNd = "mount";
    private static final Pattern OOOIilanD = Pattern.compile("/dev/(\\S+p\\d+) on (\\S+) .*");

    private l0lllAND() {
    }

    public static Map I1O1I1LaNd() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String string : Iill1lanD.I1O1I1LaNd(I1O1I1LaNd)) {
            Matcher matcher = OOOIilanD.matcher(string);
            if (!matcher.matches()) continue;
            hashMap.put(matcher.group(1), matcher.group(2));
        }
        return hashMap;
    }
}

