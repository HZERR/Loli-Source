/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.IiiIOIlanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.OI1l11LaNd;
import loliland.launcher.client.OOOO1OLAND;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lIl1OlAND;
import loliland.launcher.client.lOIiIlI0Land;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1OLAnd;
import loliland.launcher.client.lli10iliLaND;

final class OlIlIlaND
extends IiiIOIlanD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(OlIlIlaND::lil0liLand);

    OlIlIlaND() {
    }

    @Override
    public String I1O1I1LaNd() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).I1O1I1LaNd();
    }

    @Override
    public String OOOIilanD() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).OOOIilanD();
    }

    @Override
    public String lI00OlAND() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).lI00OlAND();
    }

    @Override
    public String lli0OiIlAND() {
        return (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).lli0OiIlAND();
    }

    @Override
    public lIl1OlAND Oill1LAnD() {
        return new OOOO1OLAND();
    }

    @Override
    public ll1OLAnd lIOILand() {
        return new lOIiIlI0Land((String)((OI1l11LaNd)this.I1O1I1LaNd.get()).I1O1I1LaNd(), (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).OOOIilanD(), (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).lI00OlAND(), (String)((OI1l11LaNd)this.I1O1I1LaNd.get()).li0iOILAND());
    }

    private static OI1l11LaNd lil0liLand() {
        String string = null;
        String string2 = null;
        String string3 = null;
        String string4 = null;
        String string5 = null;
        String string6 = "Manufacturer:";
        String string7 = "Product Name:";
        String string8 = "Serial Number:";
        String string9 = "UUID:";
        String string10 = "Version:";
        for (String string11 : Iill1lanD.I1O1I1LaNd("dmidecode -t system")) {
            if (string11.contains("Manufacturer:")) {
                string = string11.split("Manufacturer:")[1].trim();
                continue;
            }
            if (string11.contains("Product Name:")) {
                string2 = string11.split("Product Name:")[1].trim();
                continue;
            }
            if (string11.contains("Serial Number:")) {
                string3 = string11.split("Serial Number:")[1].trim();
                continue;
            }
            if (string11.contains("UUID:")) {
                string4 = string11.split("UUID:")[1].trim();
                continue;
            }
            if (!string11.contains("Version:")) continue;
            string5 = string11.split("Version:")[1].trim();
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string3)) {
            string3 = OlIlIlaND.iilIi1laND();
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string4)) {
            string4 = lli10iliLaND.I1O1I1LaNd("kern.hostuuid", "unknown");
        }
        return new OI1l11LaNd(iiIIIlO1lANd.I1O1I1LaNd(string) ? "unknown" : string, iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2, iiIIIlO1lANd.I1O1I1LaNd(string3) ? "unknown" : string3, iiIIIlO1lANd.I1O1I1LaNd(string4) ? "unknown" : string4, iiIIIlO1lANd.I1O1I1LaNd(string5) ? "unknown" : string5);
    }

    private static String iilIi1laND() {
        String string = "system.hardware.serial =";
        for (String string2 : Iill1lanD.I1O1I1LaNd("lshal")) {
            if (!string2.contains(string)) continue;
            return lOilLanD.li0iOILAND(string2);
        }
        return "unknown";
    }
}

