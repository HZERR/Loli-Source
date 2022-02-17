/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

public enum l01O01Iiland {
    I1O1I1LaNd,
    OOOIilanD,
    lI00OlAND,
    lli0OiIlAND;


    public static l01O01Iiland I1O1I1LaNd(String string) {
        for (l01O01Iiland l01O01Iiland2 : l01O01Iiland.values()) {
            if (!string.contains(l01O01Iiland2.toString())) continue;
            return l01O01Iiland2;
        }
        return null;
    }
}

