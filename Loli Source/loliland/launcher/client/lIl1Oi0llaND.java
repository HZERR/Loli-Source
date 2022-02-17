/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import loliland.launcher.XLauncher;
import loliland.launcher.client.OOOOllANd;
import loliland.launcher.client.ii0ii01LanD;
import loliland.launcher.client.l0il0l1iLaNd;
import loliland.launcher.client.lI0011OLaND;
import loliland.launcher.client.lI10ilAnd;
import loliland.launcher.client.lIOlLaND;
import loliland.launcher.client.liIlILAnD;
import loliland.launcher.client.llI1OlOlanD;

public class lIl1Oi0llaND {
    private boolean I1O1I1LaNd;
    private LinkedHashMap OOOIilanD;

    public void I1O1I1LaNd() {
        lI10ilAnd.OOOIilanD().submit(() -> {
            try {
                LinkedList<Object> linkedList2;
                ii0ii01LanD ii0ii01LanD2;
                lIOlLaND.I1O1I1LaNd("\u0417\u0430\u0433\u0440\u0443\u0437\u043a\u0430 \u0434\u0430\u043d\u043d\u044b\u0445...", "\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430, \u043f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435, \u043f\u043e\u043a\u0430 \u0437\u0430\u0433\u0440\u0443\u0437\u043a\u0430", "\u0434\u0430\u043d\u043d\u044b\u0445 \u043d\u0435 \u0431\u0443\u0434\u0435\u0442 \u0437\u0430\u043a\u043e\u043d\u0447\u0435\u043d\u0430");
                ii0ii01LanD ii0ii01LanD3 = l0il0l1iLaNd.I1O1I1LaNd();
                this.OOOIilanD = new LinkedHashMap();
                if (ii0ii01LanD3.lI00OlAND("clients")) {
                    ii0ii01LanD2 = ii0ii01LanD3.OOOIilanD("clients").O1il1llOLANd();
                    for (LinkedList<Object> linkedList2 : ii0ii01LanD2.OOOIilanD()) {
                        Iterator iterator = ii0ii01LanD2.OOOIilanD((String)((Object)linkedList2)).O1il1llOLANd();
                        this.OOOIilanD.put(linkedList2, new OOOOllANd((ii0ii01LanD)((Object)iterator)));
                    }
                }
                if ((ii0ii01LanD2 = l0il0l1iLaNd.OOOIilanD()).lI00OlAND("data")) {
                    ii0ii01LanD ii0ii01LanD4 = ii0ii01LanD2.OOOIilanD("data").O1il1llOLANd();
                    for (Iterator iterator : ii0ii01LanD4.OOOIilanD()) {
                        Object object = ii0ii01LanD4.OOOIilanD((String)((Object)iterator)).O1il1llOLANd();
                        OOOOllANd oOOOllANd = this.OOOIilanD.getOrDefault(iterator, null);
                        if (oOOOllANd == null) {
                            System.out.println("Client " + iterator + " not found");
                            continue;
                        }
                        for (String string : ((ii0ii01LanD)object).OOOIilanD()) {
                            ii0ii01LanD ii0ii01LanD5 = ((ii0ii01LanD)object).OOOIilanD(string).O1il1llOLANd();
                            oOOOllANd.l0iIlIO1laNd().add(new liIlILAnD(ii0ii01LanD5.OOOIilanD("address").Oill1LAnD(), ii0ii01LanD5.OOOIilanD("port").I1O1I1LaNd()));
                        }
                    }
                }
                llI1OlOlanD.I1O1I1LaNd();
                lI0011OLaND.li0iOILAND = new LinkedList();
                int n2 = 0;
                linkedList2 = new LinkedList<Object>();
                for (Object object : XLauncher.getClientManager().lI00OlAND().values()) {
                    linkedList2.add(object);
                    if (++n2 % 3 != 0) continue;
                    lI0011OLaND.li0iOILAND.add(new LinkedList(linkedList2));
                    linkedList2.clear();
                }
                lI0011OLaND.li0iOILAND.add(new LinkedList(linkedList2));
                this.I1O1I1LaNd = true;
                lIOlLaND.I1O1I1LaNd();
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
                System.exit(0);
            }
        });
    }

    public static HashMap I1O1I1LaNd(ii0ii01LanD ii0ii01LanD2) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        if (ii0ii01LanD2.lI00OlAND("hash")) {
            ii0ii01LanD ii0ii01LanD3 = ii0ii01LanD2.OOOIilanD("hash").O1il1llOLANd();
            for (String string : ii0ii01LanD3.OOOIilanD()) {
                hashMap.put(string, ii0ii01LanD3.OOOIilanD(string).Oill1LAnD());
            }
        }
        return hashMap;
    }

    public boolean OOOIilanD() {
        return this.I1O1I1LaNd;
    }

    public LinkedHashMap lI00OlAND() {
        return this.OOOIilanD;
    }
}

