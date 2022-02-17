/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import loliland.launcher.XLauncher;
import loliland.launcher.client.OOOOllANd;
import loliland.launcher.client.l1lIOlAND;

public class li01laND {
    private OOOOllANd I1O1I1LaNd;
    private HashMap OOOIilanD;
    private HashSet lI00OlAND;

    public li01laND(OOOOllANd oOOOllANd, HashMap hashMap) {
        this.I1O1I1LaNd = oOOOllANd;
        this.OOOIilanD = hashMap;
        this.lI00OlAND = new HashSet();
        hashMap.forEach((string, string2) -> this.lI00OlAND.add(string2));
    }

    public List I1O1I1LaNd() {
        ArrayList<File> arrayList = new ArrayList<File>();
        for (String string : this.I1O1I1LaNd.lil0liLand()) {
            ArrayList arrayList2 = new ArrayList();
            File file = new File(XLauncher.getStorageClients() + "clients" + File.separator + this.I1O1I1LaNd.O1il1llOLANd() + File.separator + string);
            l1lIOlAND.I1O1I1LaNd(file, arrayList2);
            for (File file2 : arrayList2) {
                String string2 = l1lIOlAND.I1O1I1LaNd(file2);
                if (this.lI00OlAND.contains(string2) && !this.I1O1I1LaNd(string, file2)) continue;
                arrayList.add(file2);
            }
        }
        return arrayList;
    }

    private boolean I1O1I1LaNd(String string, File file) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        this.OOOIilanD.forEach((string2, string3) -> {
            if (string2.endsWith(file.getName()) && file.getPath().contains(string)) {
                atomicBoolean.set(true);
            }
        });
        return !atomicBoolean.get();
    }
}

