/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.iIl0il1lAnD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lOilLanD;

final class lliiIlANd
extends iIl0il1lAnD {
    lliiIlANd(String string, String string2, String string3, String string4, long l2) {
        super(string, string2, string3, string4, l2);
    }

    public static List I1O1I1LaNd(Supplier supplier) {
        ArrayList<lliiIlANd> arrayList = new ArrayList<lliiIlANd>();
        boolean bl = false;
        String string = null;
        String string2 = null;
        ArrayList<String> arrayList2 = new ArrayList<String>();
        for (String string3 : (List)supplier.get()) {
            String string4 = string3.trim();
            if (string4.startsWith("Name:") && string4.contains("display")) {
                bl = true;
                continue;
            }
            if (bl && string4.toLowerCase().contains("graphics")) {
                string = string4;
                continue;
            }
            if (!bl || string == null) continue;
            if (string4.startsWith("Manufacture ID")) {
                string2 = lOilLanD.iOIl0LAnD(string4.substring(14));
                continue;
            }
            if (string4.contains("Level")) {
                arrayList2.add(string4.replaceAll("\\.\\.+", "="));
                continue;
            }
            if (!string4.startsWith("Hardware Location Code")) continue;
            arrayList.add(new lliiIlANd(string, "unknown", iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2, arrayList2.isEmpty() ? "unknown" : String.join((CharSequence)",", arrayList2), 0L));
            bl = false;
        }
        return arrayList;
    }
}

