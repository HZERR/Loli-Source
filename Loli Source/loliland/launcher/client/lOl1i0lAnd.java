/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Iterator;
import java.util.function.Supplier;
import loliland.launcher.client.IiiIOIlanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O0i1Ol00lanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lIl1OlAND;
import loliland.launcher.client.lOIiIlI0Land;
import loliland.launcher.client.lOO0lllAND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1OLAnd;

final class lOl1i0lAnd
extends IiiIOIlanD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(lOl1i0lAnd::lil0liLand);

    lOl1i0lAnd() {
    }

    @Override
    public String I1O1I1LaNd() {
        return O0i1Ol00lanD.I1O1I1LaNd((O0i1Ol00lanD)this.I1O1I1LaNd.get());
    }

    @Override
    public String OOOIilanD() {
        return O0i1Ol00lanD.OOOIilanD((O0i1Ol00lanD)this.I1O1I1LaNd.get());
    }

    @Override
    public String lI00OlAND() {
        return O0i1Ol00lanD.lI00OlAND((O0i1Ol00lanD)this.I1O1I1LaNd.get());
    }

    @Override
    public String lli0OiIlAND() {
        return O0i1Ol00lanD.lli0OiIlAND((O0i1Ol00lanD)this.I1O1I1LaNd.get());
    }

    @Override
    public lIl1OlAND Oill1LAnD() {
        return new lOO0lllAND(O0i1Ol00lanD.li0iOILAND((O0i1Ol00lanD)this.I1O1I1LaNd.get()), O0i1Ol00lanD.O1il1llOLANd((O0i1Ol00lanD)this.I1O1I1LaNd.get()), O0i1Ol00lanD.Oill1LAnD((O0i1Ol00lanD)this.I1O1I1LaNd.get()));
    }

    @Override
    public ll1OLAnd lIOILand() {
        return new lOIiIlI0Land(O0i1Ol00lanD.lIOILand((O0i1Ol00lanD)this.I1O1I1LaNd.get()), O0i1Ol00lanD.lil0liLand((O0i1Ol00lanD)this.I1O1I1LaNd.get()), O0i1Ol00lanD.iilIi1laND((O0i1Ol00lanD)this.I1O1I1LaNd.get()), O0i1Ol00lanD.lli011lLANd((O0i1Ol00lanD)this.I1O1I1LaNd.get()));
    }

    private static O0i1Ol00lanD lil0liLand() {
        String string;
        String string2 = null;
        String string3 = null;
        String string4 = null;
        String string5 = null;
        String string6 = null;
        String string7 = null;
        String string8 = null;
        String string9 = null;
        String string10 = null;
        String string11 = null;
        String string12 = null;
        String string13 = "Vendor:";
        String string14 = "Release Date:";
        String string15 = "VersionString:";
        String string16 = "Manufacturer:";
        String string17 = "Product:";
        String string18 = "Serial Number:";
        String string19 = "UUID:";
        String string20 = "Version:";
        int n2 = -1;
        Iterator iterator = Iill1lanD.I1O1I1LaNd("smbios").iterator();
        while (iterator.hasNext() && (!(string = (String)iterator.next()).contains("SMB_TYPE_") || (n2 = lOl1i0lAnd.I1O1I1LaNd(string)) != Integer.MAX_VALUE)) {
            switch (n2) {
                case 0: {
                    if (string.contains("Vendor:")) {
                        string2 = string.split("Vendor:")[1].trim();
                        break;
                    }
                    if (string.contains("VersionString:")) {
                        string3 = string.split("VersionString:")[1].trim();
                        break;
                    }
                    if (!string.contains("Release Date:")) break;
                    string4 = string.split("Release Date:")[1].trim();
                    break;
                }
                case 1: {
                    if (string.contains("Manufacturer:")) {
                        string5 = string.split("Manufacturer:")[1].trim();
                        break;
                    }
                    if (string.contains("Product:")) {
                        string6 = string.split("Product:")[1].trim();
                        break;
                    }
                    if (string.contains("Serial Number:")) {
                        string7 = string.split("Serial Number:")[1].trim();
                        break;
                    }
                    if (!string.contains("UUID:")) break;
                    string8 = string.split("UUID:")[1].trim();
                    break;
                }
                case 2: {
                    if (string.contains("Manufacturer:")) {
                        string9 = string.split("Manufacturer:")[1].trim();
                        break;
                    }
                    if (string.contains("Product:")) {
                        string10 = string.split("Product:")[1].trim();
                        break;
                    }
                    if (string.contains("Version:")) {
                        string11 = string.split("Version:")[1].trim();
                        break;
                    }
                    if (!string.contains("Serial Number:")) break;
                    string12 = string.split("Serial Number:")[1].trim();
                    break;
                }
            }
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string7)) {
            string7 = lOl1i0lAnd.iilIi1laND();
        }
        return new O0i1Ol00lanD(string2, string3, string4, string5, string6, string7, string8, string9, string10, string11, string12, null);
    }

    private static int I1O1I1LaNd(String string) {
        if (string.contains("SMB_TYPE_BIOS")) {
            return 0;
        }
        if (string.contains("SMB_TYPE_SYSTEM")) {
            return 1;
        }
        if (string.contains("SMB_TYPE_BASEBOARD")) {
            return 2;
        }
        return Integer.MAX_VALUE;
    }

    private static String iilIi1laND() {
        String string = Iill1lanD.OOOIilanD("sneep");
        if (string.isEmpty()) {
            String string2 = "chassis-sn:";
            for (String string3 : Iill1lanD.I1O1I1LaNd("prtconf -pv")) {
                if (!string3.contains(string2)) continue;
                string = lOilLanD.li0iOILAND(string3);
                break;
            }
        }
        return string;
    }
}

