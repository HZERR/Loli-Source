/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.ilI1l1IOLanD;

final class liO01iO1lAND
extends ilI1l1IOLanD {
    private static final Pattern I1O1I1LaNd = Pattern.compile("audio\\d+ at (.+)");
    private static final Pattern OOOIilanD = Pattern.compile("(.+) at pci\\d+ dev \\d+ function \\d+ \"(.*)\" (rev .+):.*");

    liO01iO1lAND(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public static List lli0OiIlAND() {
        Object object;
        Object object222;
        List list = Iill1lanD.I1O1I1LaNd("dmesg");
        HashSet<String> hashSet = new HashSet<String>();
        for (Object object222 : list) {
            object = I1O1I1LaNd.matcher((CharSequence)object222);
            if (!((Matcher)object).matches()) continue;
            hashSet.add(((Matcher)object).group(1));
        }
        HashMap hashMap = new HashMap();
        object222 = new HashMap();
        object = new HashMap();
        String string = "";
        for (Object object3 : list) {
            Matcher object4 = OOOIilanD.matcher((CharSequence)object3);
            if (object4.matches() && hashSet.contains(object4.group(1))) {
                string = object4.group(1);
                hashMap.put(string, object4.group(2));
                object.put(string, object4.group(3));
                continue;
            }
            if (string.isEmpty()) continue;
            int n2 = ((String)object3).indexOf("codec");
            if (n2 >= 0) {
                n2 = ((String)object3).indexOf(58);
                object222.put(string, ((String)object3).substring(n2 + 1).trim());
            }
            string = "";
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry entry : hashMap.entrySet()) {
            arrayList.add(new liO01iO1lAND((String)object.get(entry.getKey()), (String)entry.getValue(), (String)object222.get(entry.getKey())));
        }
        return arrayList;
    }
}

