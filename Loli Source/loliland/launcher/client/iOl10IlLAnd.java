/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lIiIii1LAnD;
import loliland.launcher.client.lOilLanD;

public final class iOl10IlLAnd {
    private static final Pattern I1O1I1LaNd = Pattern.compile("\\D+(\\d+).+(\\d+\\.\\d)\\s+(\\w)\\s+(\\d+)\\D+(\\d+:\\d{2}\\.\\d{2})\\s+(\\d+:\\d{2}\\.\\d{2}).+");

    private iOl10IlLAnd() {
    }

    public static List I1O1I1LaNd(int n2) {
        String string = " " + n2 + " ";
        ArrayList<lIiIii1LAnD> arrayList = new ArrayList<lIiIii1LAnD>();
        List list = Iill1lanD.I1O1I1LaNd("ps -awwxM").stream().filter(string2 -> string2.contains(string)).collect(Collectors.toList());
        int n3 = 0;
        for (String string3 : list) {
            Matcher matcher = I1O1I1LaNd.matcher(string3);
            if (!matcher.matches() || n2 != lOilLanD.lli0OiIlAND(matcher.group(1), -1)) continue;
            double d2 = lOilLanD.OOOIilanD(matcher.group(2), 0.0);
            char c2 = matcher.group(3).charAt(0);
            int n4 = lOilLanD.lli0OiIlAND(matcher.group(4), 0);
            long l2 = lOilLanD.lli0OiIlAND(matcher.group(5), 0L);
            long l3 = lOilLanD.lli0OiIlAND(matcher.group(6), 0L);
            arrayList.add(new lIiIii1LAnD(n3++, d2, c2, l2, l3, n4));
        }
        return arrayList;
    }
}

