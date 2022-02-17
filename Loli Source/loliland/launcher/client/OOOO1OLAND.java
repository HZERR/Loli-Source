/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.function.Supplier;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.il1iIIILaND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;

final class OOOO1OLAND
extends il1iIIILaND {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(OOOO1OLAND::O1il1llOLANd);

    OOOO1OLAND() {
    }

    @Override
    public String I1O1I1LaNd() {
        return (String)((IIOOOlIiLanD)this.I1O1I1LaNd.get()).I1O1I1LaNd();
    }

    @Override
    public String lli0OiIlAND() {
        return (String)((IIOOOlIiLanD)this.I1O1I1LaNd.get()).OOOIilanD();
    }

    @Override
    public String li0iOILAND() {
        return (String)((IIOOOlIiLanD)this.I1O1I1LaNd.get()).lI00OlAND();
    }

    private static IIOOOlIiLanD O1il1llOLANd() {
        String string = null;
        String string2 = null;
        String string3 = "";
        String string4 = "Vendor:";
        String string5 = "Version:";
        String string6 = "Release Date:";
        for (String string7 : Iill1lanD.I1O1I1LaNd("dmidecode -t bios")) {
            if (string7.contains("Vendor:")) {
                string = string7.split("Vendor:")[1].trim();
                continue;
            }
            if (string7.contains("Version:")) {
                string2 = string7.split("Version:")[1].trim();
                continue;
            }
            if (!string7.contains("Release Date:")) continue;
            string3 = string7.split("Release Date:")[1].trim();
        }
        string3 = lOilLanD.lIOILand(string3);
        return new IIOOOlIiLanD(iiIIIlO1lANd.I1O1I1LaNd(string) ? "unknown" : string, iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2, iiIIIlO1lANd.I1O1I1LaNd(string3) ? "unknown" : string3);
    }
}

