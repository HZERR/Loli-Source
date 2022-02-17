/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iIl0il1lAnD;

final class OI0Ii0i0lANd
extends iIl0il1lAnD {
    private static final String I1O1I1LaNd = "Class: 03 Display";
    private static final Pattern OOOIilanD = Pattern.compile(" \\d+:\\d+:\\d+: (.+)");

    OI0Ii0i0lANd(String string, String string2, String string3, String string4, long l2) {
        super(string, string2, string3, string4, l2);
    }

    public static List O1il1llOLANd() {
        ArrayList<OI0Ii0i0lANd> arrayList = new ArrayList<OI0Ii0i0lANd>();
        List list = Iill1lanD.I1O1I1LaNd("pcidump -v");
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        String string = "";
        String string2 = "";
        String string3 = "";
        boolean bl = false;
        String string4 = "";
        for (String string5 : list) {
            int n2;
            Matcher matcher = OOOIilanD.matcher(string5);
            if (matcher.matches()) {
                if (bl) {
                    arrayList.add(new OI0Ii0i0lANd(string.isEmpty() ? "unknown" : string, string3.isEmpty() ? "0x0000" : string3, string2.isEmpty() ? "0x0000" : string2, string4.isEmpty() ? "unknown" : string4, 0L));
                }
                string = matcher.group(1);
                string2 = "";
                string3 = "";
                bl = false;
                string4 = "";
                continue;
            }
            if (!bl) {
                n2 = string5.indexOf("Vendor ID: ");
                if (n2 >= 0 && string5.length() >= n2 + 15) {
                    string2 = "0x" + string5.substring(n2 + 11, n2 + 15);
                }
                if ((n2 = string5.indexOf("Product ID: ")) >= 0 && string5.length() >= n2 + 16) {
                    string3 = "0x" + string5.substring(n2 + 12, n2 + 16);
                }
                if (!string5.contains(I1O1I1LaNd)) continue;
                bl = true;
                continue;
            }
            if (!string4.isEmpty() || (n2 = string5.indexOf("Revision: ")) < 0) continue;
            string4 = string5.substring(n2);
        }
        if (bl) {
            arrayList.add(new OI0Ii0i0lANd(string.isEmpty() ? "unknown" : string, string3.isEmpty() ? "0x0000" : string3, string2.isEmpty() ? "0x0000" : string2, string4.isEmpty() ? "unknown" : string4, 0L));
        }
        return arrayList;
    }
}

