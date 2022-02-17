/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.II0l0LaND;
import loliland.launcher.client.lOilLanD;

public class llOO1LANd
extends II0l0LaND {
    public llOO1LANd(String string, String string2, String string3, String string4, String string5, String string6, List list) {
        super(string, string2, string3, string4, string5, string6, list);
    }

    public static List I1O1I1LaNd(boolean bl, Supplier supplier) {
        ArrayList<llOO1LANd> arrayList = new ArrayList<llOO1LANd>();
        for (String string : (List)supplier.get()) {
            String[] arrstring;
            String string2 = string.trim();
            if (!string2.startsWith("usb") || (arrstring = lOilLanD.OOOIilanD.split(string2, 3)).length != 3) continue;
            arrayList.add(new llOO1LANd(arrstring[2], "unknown", "unknown", "unknown", "unknown", arrstring[0], Collections.emptyList()));
        }
        if (bl) {
            return Arrays.asList(new llOO1LANd("USB Controller", "", "0000", "0000", "", "", arrayList));
        }
        return arrayList;
    }
}

