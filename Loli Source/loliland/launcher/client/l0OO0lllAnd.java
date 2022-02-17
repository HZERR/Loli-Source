/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import loliland.launcher.XLauncher;
import loliland.launcher.client.O1lllAnD;
import loliland.launcher.client.OOOOllANd;
import loliland.launcher.client.ii0ii01LanD;
import loliland.launcher.client.l0il0l1iLaNd;
import loliland.launcher.client.l1lIOlAND;
import loliland.launcher.client.lI0011OLaND;
import loliland.launcher.client.lI10ilAnd;
import loliland.launcher.client.lIOlLaND;
import loliland.launcher.client.li01laND;
import loliland.launcher.client.llIIi1lanD;

public class l0OO0lllAnd {
    private OOOOllANd I1O1I1LaNd;
    private HashMap OOOIilanD;
    private HashMap lI00OlAND;
    private ConcurrentHashMap lli0OiIlAND;
    private HashMap li0iOILAND;
    private AtomicLong O1il1llOLANd;
    private AtomicLong Oill1LAnD;
    private AtomicBoolean lIOILand;
    private ExecutorService lil0liLand;

    public l0OO0lllAnd(OOOOllANd oOOOllANd, HashMap hashMap) {
        this.I1O1I1LaNd = oOOOllANd;
        this.OOOIilanD = hashMap;
        this.lli0OiIlAND = new ConcurrentHashMap(hashMap);
        this.lI00OlAND = new HashMap();
        this.li0iOILAND = new HashMap();
        this.O1il1llOLANd = new AtomicLong();
        this.Oill1LAnD = new AtomicLong();
        this.lIOILand = new AtomicBoolean(false);
    }

    public void I1O1I1LaNd() {
        try {
            ii0ii01LanD ii0ii01LanD2 = XLauncher.getStorageHash();
            System.out.println("updateVerify: " + this.I1O1I1LaNd.lil0liLand());
            System.out.println("updateVersion: " + this.I1O1I1LaNd.iilIi1laND());
            System.out.println("updateVersion local hashes: " + this.I1O1I1LaNd.lli011lLANd());
            li01laND li01laND2 = new li01laND(this.I1O1I1LaNd, this.OOOIilanD);
            List list = li01laND2.I1O1I1LaNd();
            System.out.println("toRemove: " + list);
            for (Object object : list) {
                ((File)object).delete();
            }
            this.lil0liLand = Executors.newFixedThreadPool(3);
            new HashMap<String, String>(this.lli0OiIlAND).forEach((string, string2) -> {
                File file = new File(XLauncher.getStorageClients(), (String)string);
                if (file.exists()) {
                    this.lil0liLand.execute(() -> {
                        String string5 = l1lIOlAND.I1O1I1LaNd(file);
                        if (string2.equals(string5)) {
                            this.lli0OiIlAND.remove(string);
                            return;
                        }
                        String string6 = string.replaceFirst("clients/" + this.I1O1I1LaNd.O1il1llOLANd() + "/", "");
                        this.I1O1I1LaNd.iilIi1laND().forEach((string3, string4) -> {
                            String string5;
                            String string6;
                            String string7 = string6 = string3.endsWith("/*") ? string3.substring(0, string3.length() - 2) : string3;
                            if (string6.startsWith(string6) && (string5 = this.I1O1I1LaNd.lli011lLANd().getOrDefault(string3, "")).equals(string4)) {
                                this.lli0OiIlAND.remove(string);
                            }
                        });
                    });
                }
            });
            this.lil0liLand.shutdown();
            this.I1O1I1LaNd(this.lil0liLand);
            this.lil0liLand = null;
            for (Object object : ii0ii01LanD2.OOOIilanD()) {
                File file;
                String string3;
                if (!this.lli0OiIlAND.containsValue(object) || (string3 = ii0ii01LanD2.OOOIilanD((String)object).Oill1LAnD()).startsWith("/asset") || !(file = new File(XLauncher.getStorageClients(), string3)).exists()) continue;
                try {
                    String string4 = l1lIOlAND.I1O1I1LaNd(file);
                    if (!((String)object).equals(string4)) continue;
                    ArrayList arrayList = new ArrayList();
                    this.lli0OiIlAND.forEach((arg_0, arg_1) -> this.I1O1I1LaNd(string3, (String)object, arrayList, arg_0, arg_1));
                    for (String string5 : arrayList) {
                        this.lli0OiIlAND.remove(string5);
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            this.lli0OiIlAND.forEach((string, string2) -> {
                int n2 = Integer.parseInt(string2.split(":")[1]);
                this.O1il1llOLANd.set(this.O1il1llOLANd.get() + (long)n2);
            });
            System.out.println("toCopy: " + this.lI00OlAND);
            System.out.println("toDownload: " + this.lli0OiIlAND);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("stopped!");
            System.exit(0);
        }
    }

    public boolean OOOIilanD() {
        return !this.lli0OiIlAND.isEmpty();
    }

    public boolean lI00OlAND() {
        this.lil0liLand = Executors.newFixedThreadPool(10);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        this.lli0OiIlAND.forEach((string, string2) -> {
            File file = new File(XLauncher.getStorageClients(), (String)string);
            this.lil0liLand.submit(() -> {
                try {
                    O1lllAnD o1lllAnD = l1lIOlAND.I1O1I1LaNd(this, file, l0il0l1iLaNd.I1O1I1LaNd(this.I1O1I1LaNd.O1il1llOLANd(), string, string2));
                    if (o1lllAnD == O1lllAnD.I1O1I1LaNd) {
                        String string5 = string.replaceFirst("clients/" + this.I1O1I1LaNd.O1il1llOLANd() + "/", "");
                        this.I1O1I1LaNd.iilIi1laND().forEach((string2, string3) -> {
                            String string4;
                            if (this.li0iOILAND.containsKey(string2)) {
                                return;
                            }
                            String string5 = string4 = string2.endsWith("/*") ? string2.substring(0, string2.length() - 2) : string2;
                            if (string5.startsWith(string4)) {
                                this.li0iOILAND.put(string2, string3);
                            }
                        });
                        XLauncher.getStorageHash().I1O1I1LaNd((String)string2, string);
                    } else if (o1lllAnD == O1lllAnD.OOOIilanD) {
                        atomicBoolean.set(true);
                    }
                }
                catch (Throwable throwable) {
                    atomicBoolean.set(true);
                    throwable.printStackTrace();
                }
            });
        });
        this.lil0liLand.shutdown();
        this.I1O1I1LaNd(this.lil0liLand);
        if (atomicBoolean.get()) {
            this.lli011lLANd();
            return false;
        }
        XLauncher.saveStorageHash();
        return true;
    }

    public boolean lli0OiIlAND() {
        return !this.lI00OlAND.isEmpty();
    }

    public void li0iOILAND() {
        this.lI00OlAND.forEach((string2, arrayList) -> {
            File file = new File((String)string2);
            arrayList.forEach(string -> {
                File file2 = new File((String)string);
                l1lIOlAND.I1O1I1LaNd(this, file, file2);
            });
        });
    }

    private void I1O1I1LaNd(ExecutorService executorService) {
        while (!executorService.isTerminated()) {
        }
    }

    private void lli011lLANd() {
        llIIi1lanD.I1O1I1LaNd(new lI0011OLaND());
        lIOlLaND.I1O1I1LaNd("\u041f\u0440\u043e\u0438\u0437\u043e\u0448\u043b\u0430 \u043e\u0448\u0438\u0431\u043a\u0430!", "\u0421\u043a\u0430\u0447\u0438\u0432\u0430\u043d\u0438\u0435 \u0444\u0430\u0439\u043b\u043e\u0432", "\u043e\u0442\u043c\u0435\u043d\u0435\u043d\u043e");
        try {
            Thread.sleep(1500L);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        lIOlLaND.I1O1I1LaNd();
    }

    public void I1O1I1LaNd(int n2) {
        this.Oill1LAnD.set(this.Oill1LAnD.get() + (long)n2);
    }

    public void O1il1llOLANd() {
        lIOlLaND.I1O1I1LaNd("\u041e\u0442\u043c\u0435\u043d\u0430 \u0441\u043a\u0430\u0447\u0438\u0432\u0430\u043d\u0438\u044f...", "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u043f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435,", "\u0437\u0430\u043f\u0440\u043e\u0441 \u043e\u0431\u0440\u0430\u0431\u0430\u0442\u044b\u0432\u0430\u0435\u0442\u0441\u044f");
        lI10ilAnd.OOOIilanD().submit(() -> {
            this.lIOILand.set(true);
            this.lil0liLand.shutdownNow();
            this.I1O1I1LaNd(this.lil0liLand);
            lIOlLaND.I1O1I1LaNd();
        });
    }

    public HashMap Oill1LAnD() {
        return this.li0iOILAND;
    }

    public AtomicLong lIOILand() {
        return this.O1il1llOLANd;
    }

    public AtomicLong lil0liLand() {
        return this.Oill1LAnD;
    }

    public AtomicBoolean iilIi1laND() {
        return this.lIOILand;
    }

    private /* synthetic */ void I1O1I1LaNd(String string, String string4, ArrayList arrayList, String string5, String string6) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        String string7 = string5.replaceFirst("clients/" + this.I1O1I1LaNd.O1il1llOLANd() + "/", "");
        this.I1O1I1LaNd.iilIi1laND().forEach((string2, string3) -> {
            String string4;
            String string5;
            String string6 = string5 = string2.endsWith("/*") ? string2.substring(0, string2.length() - 2) : string2;
            if (string7.startsWith(string5) && !(string4 = this.I1O1I1LaNd.lli011lLANd().getOrDefault(string2, "")).equals(string3)) {
                atomicBoolean.set(true);
            }
        });
        if (atomicBoolean.get()) {
            return;
        }
        if (!string.equals(string5) && string6.equals(string4)) {
            String string8 = XLauncher.getStorageClients() + string;
            if (!this.lI00OlAND.containsKey(string8)) {
                this.lI00OlAND.put(string8, new ArrayList());
            }
            ((ArrayList)this.lI00OlAND.get(string8)).add(XLauncher.getStorageClients() + string5);
            int n2 = Integer.parseInt(string6.split(":")[1]);
            this.O1il1llOLANd.set(this.O1il1llOLANd.get() + (long)n2);
            arrayList.add(string5);
        }
    }
}

