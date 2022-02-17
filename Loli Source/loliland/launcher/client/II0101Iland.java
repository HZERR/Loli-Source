/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O0IllLAnD;
import loliland.launcher.client.O1I01lLANd;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.ili0OiILand;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;

public class II0101Iland
extends O1I01lLANd {
    private Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(this::ll1ILAnd);
    private String OOOIilanD;
    private String lI00OlAND = "";
    private String lli0OiIlAND;
    private String li0iOILAND;
    private String O1il1llOLANd;
    private String Oill1LAnD;
    private String lIOILand;
    private OOOOO10iLAND lil0liLand = OOOOO10iLAND.lIOILand;
    private int iilIi1laND;
    private int lli011lLANd;
    private int l0illAND;
    private long IO11O0LANd;
    private long l11lLANd;
    private long lO110l1LANd;
    private long l0iIlIO1laNd;
    private long iOIl0LAnD;
    private long iIiO00OLaNd;
    private long ii1li00Land;
    private long IOI1LaNd;
    private long lI00ilAND;

    public II0101Iland(int n2, String[] arrstring) {
        super(n2);
        this.I1O1I1LaNd(arrstring);
    }

    @Override
    public String lI00OlAND() {
        return this.OOOIilanD;
    }

    @Override
    public String lli0OiIlAND() {
        return this.lI00OlAND;
    }

    @Override
    public String li0iOILAND() {
        return this.lli0OiIlAND;
    }

    @Override
    public String O1il1llOLANd() {
        return ili0OiILand.OOOIilanD(this.I1O1I1LaNd());
    }

    @Override
    public String Oill1LAnD() {
        return this.li0iOILAND;
    }

    @Override
    public String lIOILand() {
        return this.O1il1llOLANd;
    }

    @Override
    public String lil0liLand() {
        return this.Oill1LAnD;
    }

    @Override
    public String iilIi1laND() {
        return this.lIOILand;
    }

    @Override
    public OOOOO10iLAND lli011lLANd() {
        return this.lil0liLand;
    }

    @Override
    public int l0illAND() {
        return this.iilIi1laND;
    }

    @Override
    public int IO11O0LANd() {
        return this.lli011lLANd;
    }

    @Override
    public int l11lLANd() {
        return this.l0illAND;
    }

    @Override
    public long lO110l1LANd() {
        return this.IO11O0LANd;
    }

    @Override
    public long l0iIlIO1laNd() {
        return this.l11lLANd;
    }

    @Override
    public long iOIl0LAnD() {
        return this.lO110l1LANd;
    }

    @Override
    public long iIiO00OLaNd() {
        return this.l0iIlIO1laNd;
    }

    @Override
    public long ii1li00Land() {
        return this.iIiO00OLaNd;
    }

    @Override
    public long IOI1LaNd() {
        return this.iOIl0LAnD;
    }

    @Override
    public long lI00ilAND() {
        return this.ii1li00Land;
    }

    @Override
    public long l0l00lAND() {
        return this.IOI1LaNd;
    }

    @Override
    public long IiiilAnD() {
        return this.lI00ilAND;
    }

    @Override
    public long iOl10IlLAnd() {
        return ili0OiILand.lI00OlAND(this.I1O1I1LaNd());
    }

    @Override
    public int lIiIii1LAnD() {
        return (Integer)this.I1O1I1LaNd.get();
    }

    private int ll1ILAnd() {
        List list = Iill1lanD.I1O1I1LaNd("pflags " + this.I1O1I1LaNd());
        for (String string : list) {
            if (!string.contains("data model")) continue;
            if (string.contains("LP32")) {
                return 32;
            }
            if (!string.contains("LP64")) continue;
            return 64;
        }
        return 0;
    }

    @Override
    public long II1Iland() {
        long l2 = 0L;
        String string = Iill1lanD.OOOIilanD("pbind -q " + this.I1O1I1LaNd());
        if (string.isEmpty()) {
            List list = Iill1lanD.I1O1I1LaNd("psrinfo");
            for (String string2 : list) {
                String[] arrstring = lOilLanD.OOOIilanD.split(string2);
                int n2 = lOilLanD.lli0OiIlAND(arrstring[0], -1);
                if (n2 < 0) continue;
                l2 |= 1L << n2;
            }
            return l2;
        }
        if (string.endsWith(".") && string.contains("strongly bound to processor(s)")) {
            int n3;
            String string3 = string.substring(0, string.length() - 1);
            String[] arrstring = lOilLanD.OOOIilanD.split(string3);
            for (int i2 = arrstring.length - 1; i2 >= 0 && (n3 = lOilLanD.lli0OiIlAND(arrstring[i2], -1)) >= 0; --i2) {
                l2 |= 1L << n3;
            }
        }
        return l2;
    }

    @Override
    public List liOIOOLANd() {
        List list;
        List list2 = Iill1lanD.I1O1I1LaNd("ps -o lwp,s,etime,stime,time,addr,pri -p " + this.I1O1I1LaNd());
        Map map = II0101Iland.I1O1I1LaNd(list2, 0, 7, list = Iill1lanD.I1O1I1LaNd("prstat -L -v -p " + this.I1O1I1LaNd() + " 1 1"), true);
        if (map.keySet().size() > 1) {
            return map.entrySet().stream().map(entry -> new O0IllLAnD(this.I1O1I1LaNd(), (String[])entry.getValue())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean l0IO0LAnd() {
        List list;
        int n2 = this.I1O1I1LaNd();
        List list2 = Iill1lanD.I1O1I1LaNd("ps -o s,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etime,time,comm,args -p " + n2);
        Map map = II0101Iland.I1O1I1LaNd(list2, 1, 15, list = Iill1lanD.I1O1I1LaNd("prstat -v -p " + n2 + " 1 1"), false);
        if (map.containsKey(n2)) {
            return this.I1O1I1LaNd((String[])map.get(this.I1O1I1LaNd()));
        }
        this.lil0liLand = OOOOO10iLAND.lIOILand;
        return false;
    }

    private boolean I1O1I1LaNd(String[] arrstring) {
        long l2 = System.currentTimeMillis();
        this.lil0liLand = II0101Iland.I1O1I1LaNd(arrstring[0].charAt(0));
        this.iilIi1laND = lOilLanD.lli0OiIlAND(arrstring[2], 0);
        this.li0iOILAND = arrstring[3];
        this.O1il1llOLANd = arrstring[4];
        this.Oill1LAnD = arrstring[5];
        this.lIOILand = arrstring[6];
        this.lli011lLANd = lOilLanD.lli0OiIlAND(arrstring[7], 0);
        this.l0illAND = lOilLanD.lli0OiIlAND(arrstring[8], 0);
        this.IO11O0LANd = lOilLanD.OOOIilanD(arrstring[9], 0L) * 1024L;
        this.l11lLANd = lOilLanD.OOOIilanD(arrstring[10], 0L) * 1024L;
        long l3 = lOilLanD.lli0OiIlAND(arrstring[11], 0L);
        this.iIiO00OLaNd = l3 < 1L ? 1L : l3;
        this.iOIl0LAnD = l2 - this.iIiO00OLaNd;
        this.lO110l1LANd = 0L;
        this.l0iIlIO1laNd = lOilLanD.lli0OiIlAND(arrstring[12], 0L);
        this.lI00OlAND = arrstring[13];
        this.OOOIilanD = this.lI00OlAND.substring(this.lI00OlAND.lastIndexOf(47) + 1);
        this.lli0OiIlAND = arrstring[14];
        long l4 = lOilLanD.OOOIilanD(arrstring[15], 0L);
        long l5 = lOilLanD.OOOIilanD(arrstring[16], 0L);
        this.lI00ilAND = l5 + l4;
        return true;
    }

    static Map I1O1I1LaNd(List list, int n2, int n3, List list2, boolean bl) {
        HashMap hashMap = new HashMap();
        if (list.size() > 1) {
            list.stream().skip(1L).forEach(string -> {
                String[] arrstring = lOilLanD.OOOIilanD.split(string.trim(), n3);
                String[] arrstring2 = new String[n3 + 2];
                if (arrstring.length == n3) {
                    for (int i2 = 0; i2 < n3; ++i2) {
                        if (i2 == n2) {
                            hashMap.put(lOilLanD.lli0OiIlAND(arrstring[i2], 0), arrstring2);
                        }
                        arrstring2[i2] = arrstring[i2];
                    }
                }
            });
            if (list2.size() > 1) {
                list2.stream().skip(1L).forEach(string -> {
                    String[] arrstring = lOilLanD.OOOIilanD.split(string.trim());
                    if (arrstring.length == 15) {
                        String[] arrstring2;
                        int n3;
                        String string2 = arrstring[0];
                        if (bl && (n3 = arrstring[14].lastIndexOf(47) + 1) > 0 && n3 < arrstring[14].length()) {
                            string2 = arrstring[14].substring(n3);
                        }
                        if ((arrstring2 = (String[])hashMap.get(Integer.parseInt(string2))) != null) {
                            arrstring2[n2] = arrstring[10];
                            arrstring2[n2 + 1] = arrstring[11];
                        }
                    }
                });
            }
        }
        return hashMap;
    }

    static OOOOO10iLAND I1O1I1LaNd(char c2) {
        OOOOO10iLAND oOOOO10iLAND;
        switch (c2) {
            case 'O': {
                oOOOO10iLAND = OOOOO10iLAND.OOOIilanD;
                break;
            }
            case 'S': {
                oOOOO10iLAND = OOOOO10iLAND.lI00OlAND;
                break;
            }
            case 'R': 
            case 'W': {
                oOOOO10iLAND = OOOOO10iLAND.lli0OiIlAND;
                break;
            }
            case 'Z': {
                oOOOO10iLAND = OOOOO10iLAND.li0iOILAND;
                break;
            }
            case 'T': {
                oOOOO10iLAND = OOOOO10iLAND.O1il1llOLANd;
                break;
            }
            default: {
                oOOOO10iLAND = OOOOO10iLAND.Oill1LAnD;
            }
        }
        return oOOOO10iLAND;
    }
}

