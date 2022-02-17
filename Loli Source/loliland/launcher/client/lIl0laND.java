/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.ilI1l1IOLanD;
import loliland.launcher.client.lOilLanD;

final class lIl0laND
extends ilI1l1IOLanD {
    lIl0laND(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public static List I1O1I1LaNd(Supplier supplier) {
        ArrayList<lIl0laND> arrayList = new ArrayList<lIl0laND>();
        for (String string : (List)supplier.get()) {
            String[] arrstring;
            String string2 = string.trim();
            if (!string2.startsWith("paud") || (arrstring = lOilLanD.OOOIilanD.split(string2, 3)).length != 3) continue;
            arrayList.add(new lIl0laND("unknown", arrstring[2], "unknown"));
        }
        return arrayList;
    }
}

