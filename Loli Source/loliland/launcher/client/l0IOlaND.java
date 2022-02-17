/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.il1iIIILaND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;

public class l0IOlaND
extends il1iIIILaND {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(l0IOlaND::O1il1llOLANd);

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
        List list = Iill1lanD.I1O1I1LaNd("dmesg");
        for (String string4 : list) {
            if (!string4.startsWith("bios0: vendor")) continue;
            string = lOilLanD.I1O1I1LaNd(string4, '\"');
            string3 = lOilLanD.lIOILand(lOilLanD.OOOIilanD(string4));
            string2 = string4.split("vendor")[1].trim();
        }
        return new IIOOOlIiLanD(iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2, iiIIIlO1lANd.I1O1I1LaNd(string) ? "unknown" : string, iiIIIlO1lANd.I1O1I1LaNd(string3) ? "unknown" : string3);
    }
}

