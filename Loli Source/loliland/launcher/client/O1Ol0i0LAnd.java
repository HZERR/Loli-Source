/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.LinkedList;
import java.util.List;
import loliland.launcher.client.OOOIilanD;
import loliland.launcher.client.l0O0IlaND;
import loliland.launcher.client.l0OI0lAnd;

public final class O1Ol0i0LAnd {
    public static List I1O1I1LaNd(OOOIilanD oOOIilanD) {
        LinkedList<String> linkedList = new LinkedList<String>();
        List list = oOOIilanD.lli0OiIlAND().lIOILand();
        if (!list.isEmpty()) {
            int n2 = 0;
            for (l0OI0lAnd l0OI0lAnd2 : list) {
                StringBuilder stringBuilder = new StringBuilder();
                byte[] arrby = l0OI0lAnd2.I1O1I1LaNd();
                byte[][] arrby2 = l0O0IlaND.iilIi1laND(arrby);
                String string = "Display " + n2;
                for (byte[] arrby3 : arrby2) {
                    if (l0O0IlaND.lli011lLANd(arrby3) != 252) continue;
                    string = l0O0IlaND.l11lLANd(arrby3);
                }
                stringBuilder.append(string).append(": ");
                int n3 = l0O0IlaND.lIOILand(arrby);
                int n4 = l0O0IlaND.lil0liLand(arrby);
                stringBuilder.append(String.format("%d x %d cm (%.1f x %.1f in)", n3, n4, (double)n3 / 2.54, (double)n4 / 2.54));
                linkedList.add(stringBuilder.toString());
            }
        }
        return linkedList;
    }
}

