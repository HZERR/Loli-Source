/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.l11IliilLANd;
import loliland.launcher.client.lOOO110laNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liIli0OlAND;
import loliland.launcher.client.ll1l10l0LaNd;

public final class IiiilAnD {
    private IiiilAnD() {
    }

    public static O1IiIiI1LAND I1O1I1LaNd() {
        long l2 = 0L;
        long l3 = 0L;
        List list = Iill1lanD.I1O1I1LaNd("netstat -n -p tcp");
        for (String string : list) {
            if (!string.endsWith("ESTABLISHED")) continue;
            if (string.startsWith("tcp4")) {
                ++l2;
                continue;
            }
            if (!string.startsWith("tcp6")) continue;
            ++l3;
        }
        return new O1IiIiI1LAND(l2, l3);
    }

    public static List OOOIilanD() {
        ArrayList<lOOO110laNd> arrayList = new ArrayList<lOOO110laNd>();
        List list = Iill1lanD.I1O1I1LaNd("netstat -n");
        for (String string : list) {
            String[] arrstring = null;
            if (!string.startsWith("tcp") && !string.startsWith("udp") || (arrstring = lOilLanD.OOOIilanD.split(string)).length < 5) continue;
            String string2 = arrstring.length == 6 ? arrstring[5] : null;
            String string3 = arrstring[0];
            O1IiIiI1LAND o1IiIiI1LAND = IiiilAnD.lI00OlAND(arrstring[3]);
            O1IiIiI1LAND o1IiIiI1LAND2 = IiiilAnD.lI00OlAND(arrstring[4]);
            arrayList.add(new lOOO110laNd(string3, (byte[])o1IiIiI1LAND.I1O1I1LaNd(), (Integer)o1IiIiI1LAND.OOOIilanD(), (byte[])o1IiIiI1LAND2.I1O1I1LaNd(), (Integer)o1IiIiI1LAND2.OOOIilanD(), string2 == null ? liIli0OlAND.IO11O0LANd : liIli0OlAND.valueOf(string2), lOilLanD.lli0OiIlAND(arrstring[2], 0), lOilLanD.lli0OiIlAND(arrstring[1], 0), -1));
        }
        return arrayList;
    }

    private static O1IiIiI1LAND lI00OlAND(String string) {
        int n2 = string.lastIndexOf(46);
        if (n2 > 0 && string.length() > n2) {
            int n3 = lOilLanD.lli0OiIlAND(string.substring(n2 + 1), 0);
            String string2 = string.substring(0, n2);
            try {
                return new O1IiIiI1LAND(InetAddress.getByName(string2).getAddress(), n3);
            }
            catch (UnknownHostException unknownHostException) {
                try {
                    string2 = string2.endsWith(":") && string2.contains("::") ? string2 + "0" : (string2.endsWith(":") || string2.contains("::") ? string2 + ":0" : string2 + "::0");
                    return new O1IiIiI1LAND(InetAddress.getByName(string2).getAddress(), n3);
                }
                catch (UnknownHostException unknownHostException2) {
                    return new O1IiIiI1LAND(new byte[0], n3);
                }
            }
        }
        return new O1IiIiI1LAND(new byte[0], 0);
    }

    public static ll1l10l0LaNd I1O1I1LaNd(String string) {
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
        List list = Iill1lanD.I1O1I1LaNd(string);
        block38: for (String string2 : list) {
            String[] arrstring = string2.trim().split(" ", 2);
            if (arrstring.length != 2) continue;
            switch (arrstring[1]) {
                case "connections established": 
                case "connection established (including accepts)": 
                case "connections established (including accepts)": {
                    l2 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
                case "active connection openings": {
                    l3 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
                case "passive connection openings": {
                    l4 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
                case "failed connection attempts": 
                case "bad connection attempts": {
                    l5 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
                case "connection resets received": 
                case "dropped due to RST": {
                    l6 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
                case "segments sent out": 
                case "packet sent": 
                case "packets sent": {
                    l7 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
                case "segments received": 
                case "packet received": 
                case "packets received": {
                    l8 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
                case "segments retransmitted": {
                    l9 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
                case "bad segments received": 
                case "discarded for bad checksum": 
                case "discarded for bad checksums": 
                case "discarded for bad header offset field": 
                case "discarded for bad header offset fields": 
                case "discarded because packet too short": 
                case "discarded for missing IPsec protection": {
                    l10 += lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
                case "resets sent": {
                    l11 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    continue block38;
                }
            }
            if (!arrstring[1].contains("retransmitted") || !arrstring[1].contains("data packet")) continue;
            l9 += lOilLanD.OOOIilanD(arrstring[0], 0L);
        }
        return new ll1l10l0LaNd(l2, l3, l4, l5, l6, l7, l8, l9, l10, l11);
    }

    public static l11IliilLANd OOOIilanD(String string) {
        long l2 = 0L;
        long l3 = 0L;
        long l4 = 0L;
        long l5 = 0L;
        List list = Iill1lanD.I1O1I1LaNd(string);
        for (String string2 : list) {
            String[] arrstring = string2.trim().split(" ", 2);
            if (arrstring.length != 2) continue;
            switch (arrstring[1]) {
                case "packets sent": 
                case "datagram output": 
                case "datagrams output": {
                    l2 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    break;
                }
                case "packets received": 
                case "datagram received": 
                case "datagrams received": {
                    l3 = lOilLanD.OOOIilanD(arrstring[0], 0L);
                    break;
                }
                case "packets to unknown port received": 
                case "dropped due to no socket": 
                case "broadcast/multicast datagram dropped due to no socket": 
                case "broadcast/multicast datagrams dropped due to no socket": {
                    l4 += lOilLanD.OOOIilanD(arrstring[0], 0L);
                    break;
                }
                case "packet receive errors": 
                case "with incomplete header": 
                case "with bad data length field": 
                case "with bad checksum": 
                case "woth no checksum": {
                    l5 += lOilLanD.OOOIilanD(arrstring[0], 0L);
                    break;
                }
            }
        }
        return new l11IliilLANd(l2, l3, l4, l5);
    }
}

