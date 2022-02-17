/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.IiiIOIlanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OIIOIIOlAnD;
import loliland.launcher.client.l10iLaNd;
import loliland.launcher.client.lIl1OlAND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1OI1iOLANd;
import loliland.launcher.client.ll1OLAnd;

final class l1lO1iI1LaND
extends IiiIOIlanD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(l1lO1iI1LaND::lil0liLand);
    private final Supplier OOOIilanD;

    l1lO1iI1LaND(Supplier supplier) {
        this.OOOIilanD = supplier;
    }

    @Override
    public String I1O1I1LaNd() {
        return OIIOIIOlAnD.I1O1I1LaNd((OIIOIIOlAnD)this.I1O1I1LaNd.get());
    }

    @Override
    public String OOOIilanD() {
        return OIIOIIOlAnD.OOOIilanD((OIIOIIOlAnD)this.I1O1I1LaNd.get());
    }

    @Override
    public String lI00OlAND() {
        return OIIOIIOlAnD.lI00OlAND((OIIOIIOlAnD)this.I1O1I1LaNd.get());
    }

    @Override
    public String lli0OiIlAND() {
        return OIIOIIOlAnD.lli0OiIlAND((OIIOIIOlAnD)this.I1O1I1LaNd.get());
    }

    @Override
    public lIl1OlAND Oill1LAnD() {
        return new ll1OI1iOLANd(OIIOIIOlAnD.li0iOILAND((OIIOIIOlAnD)this.I1O1I1LaNd.get()), OIIOIIOlAnD.O1il1llOLANd((OIIOIIOlAnD)this.I1O1I1LaNd.get()), OIIOIIOlAnD.Oill1LAnD((OIIOIIOlAnD)this.I1O1I1LaNd.get()));
    }

    @Override
    public ll1OLAnd lIOILand() {
        return new l10iLaNd(this.OOOIilanD);
    }

    private static OIIOIIOlAnD lil0liLand() {
        String string = "IBM";
        String string2 = null;
        String string3 = null;
        String string4 = string;
        String string5 = null;
        String string6 = null;
        String string7 = null;
        String string8 = "fwversion";
        String string9 = "modelname";
        String string10 = "systemid";
        String string11 = "os_uuid";
        String string12 = "Platform Firmware level is";
        for (String string13 : Iill1lanD.I1O1I1LaNd("lsattr -El sys0")) {
            int n2;
            if (string13.startsWith("fwversion")) {
                string2 = string13.split("fwversion")[1].trim();
                n2 = string2.indexOf(44);
                if (n2 > 0 && string2.length() > n2) {
                    string = string2.substring(0, n2);
                    string2 = string2.substring(n2 + 1);
                }
                string2 = lOilLanD.OOOIilanD.split(string2)[0];
                continue;
            }
            if (string13.startsWith("modelname")) {
                string5 = string13.split("modelname")[1].trim();
                n2 = string5.indexOf(44);
                if (n2 > 0 && string5.length() > n2) {
                    string4 = string5.substring(0, n2);
                    string5 = string5.substring(n2 + 1);
                }
                string5 = lOilLanD.OOOIilanD.split(string5)[0];
                continue;
            }
            if (string13.startsWith("systemid")) {
                string6 = string13.split("systemid")[1].trim();
                string6 = lOilLanD.OOOIilanD.split(string6)[0];
                continue;
            }
            if (!string13.startsWith("os_uuid")) continue;
            string7 = string13.split("os_uuid")[1].trim();
            string7 = lOilLanD.OOOIilanD.split(string7)[0];
        }
        for (String string13 : Iill1lanD.I1O1I1LaNd("lsmcode -c")) {
            if (!string13.startsWith("Platform Firmware level is")) continue;
            string3 = string13.split("Platform Firmware level is")[1].trim();
            break;
        }
        return new OIIOIIOlAnD(string, string3, string2, string4, string5, string6, string7, null);
    }
}

