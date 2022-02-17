/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.ilI1l1IOLanD;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;

final class O0OO1LaNd
extends ilI1l1IOLanD {
    private static final String I1O1I1LaNd = "Apple Inc.";

    O0OO1LaNd(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public static List lli0OiIlAND() {
        ArrayList<O0OO1LaNd> arrayList = new ArrayList<O0OO1LaNd>();
        String string = I1O1I1LaNd;
        String string2 = "AppleHDAController";
        String string3 = "AppleHDACodec";
        boolean bl = false;
        String string4 = "<key>com.apple.driver.AppleHDAController</key>";
        for (String string5 : liOIOOlLAnD.I1O1I1LaNd("/System/Library/Extensions/AppleHDA.kext/Contents/Info.plist")) {
            if (string5.contains(string4)) {
                bl = true;
                continue;
            }
            if (!bl) continue;
            string2 = "AppleHDAController " + lOilLanD.I1O1I1LaNd(string5, "<string>", "</string>");
            bl = false;
        }
        arrayList.add(new O0OO1LaNd(string2, string, string3));
        return arrayList;
    }
}

