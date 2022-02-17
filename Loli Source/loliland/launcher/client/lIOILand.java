/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import loliland.launcher.client.liOIOOlLAnD;

public final class lIOILand {
    private lIOILand() {
    }

    public static String I1O1I1LaNd() {
        String string = liOIOOlLAnD.li0iOILAND("/sys/firmware/devicetree/base/model");
        if (!string.isEmpty()) {
            return string.replace("Machine: ", "");
        }
        return null;
    }
}

