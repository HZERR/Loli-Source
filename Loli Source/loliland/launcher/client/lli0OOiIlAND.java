/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.ilI1l1IOLanD;
import loliland.launcher.client.liOIOOlLAnD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class lli0OOiIlAND
extends ilI1l1IOLanD {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lli0OOiIlAND.class);
    private static final String OOOIilanD = "card";
    private static final String lI00OlAND = "cards";
    private static final String lli0OiIlAND = "id";

    lli0OOiIlAND(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    private static List li0iOILAND() {
        File file = new File(iI11I1lllaNd.OOOIilanD);
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] arrfile = file.listFiles();
        if (arrfile != null) {
            for (File file2 : arrfile) {
                if (!file2.getName().startsWith(OOOIilanD) || !file2.isDirectory()) continue;
                arrayList.add(file2);
            }
        } else {
            I1O1I1LaNd.warn("No Audio Cards Found");
        }
        return arrayList;
    }

    private static String O1il1llOLANd() {
        String string = liOIOOlLAnD.li0iOILAND(iI11I1lllaNd.OOOIilanD + "version");
        return string.isEmpty() ? "not available" : string;
    }

    private static String I1O1I1LaNd(File file) {
        String string = "";
        File[] arrfile = file.listFiles();
        if (arrfile != null) {
            block0: for (File file2 : arrfile) {
                if (!file2.getName().startsWith("codec")) continue;
                if (!file2.isDirectory()) {
                    string = (String)liOIOOlLAnD.I1O1I1LaNd(file2.getPath(), ":").get("Codec");
                    continue;
                }
                File[] arrfile2 = file2.listFiles();
                if (arrfile2 == null) continue;
                for (File file3 : arrfile2) {
                    if (file3.isDirectory() || !file3.getName().contains("#")) continue;
                    string = file3.getName().substring(0, file3.getName().indexOf(35));
                    continue block0;
                }
            }
        }
        return string;
    }

    private static String OOOIilanD(File file) {
        String string = "Not Found..";
        Map map = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.OOOIilanD + "/" + lI00OlAND, ":");
        String string2 = liOIOOlLAnD.li0iOILAND(file.getPath() + "/" + lli0OiIlAND);
        for (Map.Entry entry : map.entrySet()) {
            if (!((String)entry.getKey()).contains(string2)) continue;
            string = (String)entry.getValue();
            return string;
        }
        return string;
    }

    public static List lli0OiIlAND() {
        ArrayList<lli0OOiIlAND> arrayList = new ArrayList<lli0OOiIlAND>();
        for (File file : lli0OOiIlAND.li0iOILAND()) {
            arrayList.add(new lli0OOiIlAND(lli0OOiIlAND.O1il1llOLANd(), lli0OOiIlAND.OOOIilanD(file), lli0OOiIlAND.I1O1I1LaNd(file)));
        }
        return arrayList;
    }
}

