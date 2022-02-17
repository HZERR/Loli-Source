/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.i0l10iiilaNd;
import loliland.launcher.client.l11IliilLANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.ll1l10l0LaNd;

public class iiiIOI0lAnd
extends i0l10iiilaNd {
    @Override
    public ll1l10l0LaNd lli0OiIlAND() {
        return iiiIOI0lAnd.O1il1llOLANd();
    }

    @Override
    public l11IliilLANd li0iOILAND() {
        return iiiIOI0lAnd.Oill1LAnD();
    }

    private static ll1l10l0LaNd O1il1llOLANd() {
        long l2 = 0L;
        long l3 = 0L;
        long l4 = 0L;
        long l5 = 0L;
        long l6 = 0L;
        long l7 = 0L;
        long l8 = 0L;
        long l9 = 0L;
        long l10 = 0L;
        long l11 = 0L;
        List list = Iill1lanD.I1O1I1LaNd("netstat -s -P tcp");
        list.addAll(Iill1lanD.I1O1I1LaNd("netstat -s -P ip"));
        for (String string : list) {
            String[] arrstring;
            block25: for (String string2 : arrstring = iiiIOI0lAnd.I1O1I1LaNd(string, "tcp")) {
                String[] arrstring2;
                if (string2 == null || (arrstring2 = string2.split("=")).length != 2) continue;
                switch (arrstring2[0].trim()) {
                    case "tcpCurrEstab": {
                        l2 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block25;
                    }
                    case "tcpActiveOpens": {
                        l3 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block25;
                    }
                    case "tcpPassiveOpens": {
                        l4 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block25;
                    }
                    case "tcpAttemptFails": {
                        l5 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block25;
                    }
                    case "tcpEstabResets": {
                        l6 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block25;
                    }
                    case "tcpOutSegs": {
                        l7 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block25;
                    }
                    case "tcpInSegs": {
                        l8 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block25;
                    }
                    case "tcpRetransSegs": {
                        l9 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block25;
                    }
                    case "tcpInErr": {
                        l10 = lOilLanD.Oill1LAnD(arrstring2[1].trim());
                        continue block25;
                    }
                    case "tcpOutRsts": {
                        l11 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block25;
                    }
                }
            }
        }
        return new ll1l10l0LaNd(l2, l3, l4, l5, l6, l7, l8, l9, l10, l11);
    }

    private static l11IliilLANd Oill1LAnD() {
        long l2 = 0L;
        long l3 = 0L;
        long l4 = 0L;
        long l5 = 0L;
        List list = Iill1lanD.I1O1I1LaNd("netstat -s -P udp");
        list.addAll(Iill1lanD.I1O1I1LaNd("netstat -s -P ip"));
        for (String string : list) {
            String[] arrstring;
            block13: for (String string2 : arrstring = iiiIOI0lAnd.I1O1I1LaNd(string, "udp")) {
                String[] arrstring2;
                if (string2 == null || (arrstring2 = string2.split("=")).length != 2) continue;
                switch (arrstring2[0].trim()) {
                    case "udpOutDatagrams": {
                        l2 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block13;
                    }
                    case "udpInDatagrams": {
                        l3 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block13;
                    }
                    case "udpNoPorts": {
                        l4 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block13;
                    }
                    case "udpInErrors": {
                        l5 = lOilLanD.OOOIilanD(arrstring2[1].trim(), 0L);
                        continue block13;
                    }
                }
            }
        }
        return new l11IliilLANd(l2, l3, l4, l5);
    }

    private static String[] I1O1I1LaNd(String string, String string2) {
        String[] arrstring = new String[2];
        int n2 = string.indexOf(string2);
        if (n2 >= 0) {
            int n3 = string.indexOf(string2, n2 + 1);
            if (n3 >= 0) {
                arrstring[0] = string.substring(n2, n3).trim();
                arrstring[1] = string.substring(n3).trim();
            } else {
                arrstring[0] = string.substring(n2).trim();
            }
        }
        return arrstring;
    }
}

