/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;

public final class OI1II1OIlanD {
    private static final long I1O1I1LaNd = 60000L;
    private static final long OOOIilanD = 3600000L;
    private static final long lI00OlAND = 86400000L;
    private static final Pattern lli0OiIlAND = Pattern.compile(".*\\sup\\s+((\\d+)\\s+days?,?\\s+)?\\b((\\d+):)?(\\d+)(\\s+min(utes?)?)?,\\s+\\d+\\s+user.+");

    private OI1II1OIlanD() {
    }

    public static long I1O1I1LaNd() {
        long l2 = 0L;
        String string = Iill1lanD.OOOIilanD("/usr/bin/uptime");
        Matcher matcher = lli0OiIlAND.matcher(string);
        if (matcher.matches()) {
            if (matcher.group(2) != null) {
                l2 += lOilLanD.OOOIilanD(matcher.group(2), 0L) * 86400000L;
            }
            if (matcher.group(4) != null) {
                l2 += lOilLanD.OOOIilanD(matcher.group(4), 0L) * 3600000L;
            }
            l2 += lOilLanD.OOOIilanD(matcher.group(5), 0L) * 60000L;
        }
        return l2;
    }
}

