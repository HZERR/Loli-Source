/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.Oii011ILaND;
import loliland.launcher.client.OilILAnD;
import loliland.launcher.client.i00ilaNd;
import loliland.launcher.client.lOilLanD;

public abstract class lI01110LaNd
implements OilILAnD {
    @Override
    public List li0iOILAND() {
        ArrayList<Oii011ILaND> arrayList = new ArrayList<Oii011ILaND>();
        List list = Iill1lanD.I1O1I1LaNd("dmidecode --type 17");
        int n2 = 0;
        String string = "unknown";
        String string2 = "";
        long l2 = 0L;
        long l3 = 0L;
        String string3 = "unknown";
        String string4 = "unknown";
        for (String string5 : list) {
            String[] arrstring;
            if (string5.trim().contains("DMI type 17")) {
                if (n2++ <= 0) continue;
                if (l2 > 0L) {
                    arrayList.add(new Oii011ILaND(string + string2, l2, l3, string3, string4));
                }
                string = "unknown";
                string2 = "";
                l2 = 0L;
                l3 = 0L;
                continue;
            }
            if (n2 <= 0 || (arrstring = string5.trim().split(":")).length != 2) continue;
            switch (arrstring[0]) {
                case "Bank Locator": {
                    string = arrstring[1].trim();
                    break;
                }
                case "Locator": {
                    string2 = "/" + arrstring[1].trim();
                    break;
                }
                case "Size": {
                    l2 = lOilLanD.lli011lLANd(arrstring[1].trim());
                    break;
                }
                case "Type": {
                    string4 = arrstring[1].trim();
                    break;
                }
                case "Speed": {
                    l3 = lOilLanD.I1O1I1LaNd(arrstring[1]);
                    break;
                }
                case "Manufacturer": {
                    string3 = arrstring[1].trim();
                    break;
                }
            }
        }
        if (l2 > 0L) {
            arrayList.add(new Oii011ILaND(string + string2, l2, l3, string3, string4));
        }
        return arrayList;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Available: ");
        stringBuilder.append(i00ilaNd.I1O1I1LaNd(this.OOOIilanD()));
        stringBuilder.append("/");
        stringBuilder.append(i00ilaNd.I1O1I1LaNd(this.I1O1I1LaNd()));
        return stringBuilder.toString();
    }
}

