/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.aix.Perfstat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1I01lLANd;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OIi0IlanD;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.ili0OiILand;
import loliland.launcher.client.l01l0OilANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;

public class OO1iiOlanD
extends O1I01lLANd {
    private Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(this::ll1ILAnd);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(l01l0OilANd::lI00OlAND, lii1IO0LaNd.I1O1I1LaNd());
    private String lI00OlAND;
    private String lli0OiIlAND = "";
    private String li0iOILAND;
    private String O1il1llOLANd;
    private String Oill1LAnD;
    private String lIOILand;
    private String lil0liLand;
    private OOOOO10iLAND iilIi1laND = OOOOO10iLAND.lIOILand;
    private int lli011lLANd;
    private int l0illAND;
    private int IO11O0LANd;
    private long l11lLANd;
    private long lO110l1LANd;
    private long l0iIlIO1laNd;
    private long iOIl0LAnD;
    private long iIiO00OLaNd;
    private long ii1li00Land;
    private long IOI1LaNd;
    private long lI00ilAND;
    private long l0l00lAND;
    private Supplier iOl10IlLAnd;

    public OO1iiOlanD(int n2, String[] arrstring, Map map, Supplier supplier) {
        super(n2);
        this.iOl10IlLAnd = supplier;
        this.I1O1I1LaNd(arrstring, map);
    }

    @Override
    public String lI00OlAND() {
        return this.lI00OlAND;
    }

    @Override
    public String lli0OiIlAND() {
        return this.lli0OiIlAND;
    }

    @Override
    public String li0iOILAND() {
        return this.li0iOILAND;
    }

    @Override
    public String O1il1llOLANd() {
        return ili0OiILand.OOOIilanD(this.I1O1I1LaNd());
    }

    @Override
    public String Oill1LAnD() {
        return this.O1il1llOLANd;
    }

    @Override
    public String lIOILand() {
        return this.Oill1LAnD;
    }

    @Override
    public String lil0liLand() {
        return this.lIOILand;
    }

    @Override
    public String iilIi1laND() {
        return this.lil0liLand;
    }

    @Override
    public OOOOO10iLAND lli011lLANd() {
        return this.iilIi1laND;
    }

    @Override
    public int l0illAND() {
        return this.lli011lLANd;
    }

    @Override
    public int IO11O0LANd() {
        return this.l0illAND;
    }

    @Override
    public int l11lLANd() {
        return this.IO11O0LANd;
    }

    @Override
    public long lO110l1LANd() {
        return this.l11lLANd;
    }

    @Override
    public long l0iIlIO1laNd() {
        return this.lO110l1LANd;
    }

    @Override
    public long iOIl0LAnD() {
        return this.l0iIlIO1laNd;
    }

    @Override
    public long iIiO00OLaNd() {
        return this.iOIl0LAnD;
    }

    @Override
    public long ii1li00Land() {
        return this.ii1li00Land;
    }

    @Override
    public long IOI1LaNd() {
        return this.iIiO00OLaNd;
    }

    @Override
    public long lI00ilAND() {
        return this.IOI1LaNd;
    }

    @Override
    public long l0l00lAND() {
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
        List list = Iill1lanD.I1O1I1LaNd("ps -m -o THREAD -p " + this.I1O1I1LaNd());
        if (list.size() > 2) {
            list.remove(0);
            list.remove(0);
            for (String string : list) {
                String[] arrstring = lOilLanD.OOOIilanD.split(string.trim());
                if (arrstring.length <= 13 || arrstring[4].charAt(0) == 'Z') continue;
                if (arrstring[11].charAt(0) == '-') {
                    return (Long)this.OOOIilanD.get();
                }
                int n2 = lOilLanD.lli0OiIlAND(arrstring[11], 0);
                l2 |= 1L << n2;
            }
        }
        return l2;
    }

    @Override
    public List liOIOOLANd() {
        List list = Iill1lanD.I1O1I1LaNd("ps -m -o THREAD -p " + this.I1O1I1LaNd());
        if (list.size() > 2) {
            ArrayList<OIi0IlanD> arrayList = new ArrayList<OIi0IlanD>();
            list.remove(0);
            list.remove(0);
            for (String string : list) {
                String[] arrstring = lOilLanD.OOOIilanD.split(string.trim());
                if (arrstring.length != 13) continue;
                String[] arrstring2 = new String[]{arrstring[3], arrstring[4], arrstring[6]};
                arrayList.add(new OIi0IlanD(this.I1O1I1LaNd(), arrstring2));
            }
            return arrayList;
        }
        return Collections.emptyList();
    }

    @Override
    public long OOOliOOllANd() {
        return this.l0l00lAND;
    }

    @Override
    public boolean l0IO0LAnd() {
        Perfstat.perfstat_process_t[] arrperfstat_process_t = (Perfstat.perfstat_process_t[])this.iOl10IlLAnd.get();
        List list = Iill1lanD.I1O1I1LaNd("ps -o s,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etime,time,comm,args -p " + this.I1O1I1LaNd());
        HashMap<Integer, O1IiIiI1LAND> hashMap = new HashMap<Integer, O1IiIiI1LAND>();
        Object[] arrobject = arrperfstat_process_t;
        int n2 = arrobject.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Perfstat.perfstat_process_t perfstat_process_t2 = arrobject[i2];
            hashMap.put((int)perfstat_process_t2.pid, new O1IiIiI1LAND((long)perfstat_process_t2.ucpu_time, (long)perfstat_process_t2.scpu_time));
        }
        if (list.size() > 1 && (arrobject = lOilLanD.OOOIilanD.split(((String)list.get(1)).trim(), 15)).length == 15) {
            return this.I1O1I1LaNd((String[])arrobject, hashMap);
        }
        this.iilIi1laND = OOOOO10iLAND.lIOILand;
        return false;
    }

    private boolean I1O1I1LaNd(String[] arrstring, Map map) {
        long l2 = System.currentTimeMillis();
        this.iilIi1laND = OO1iiOlanD.I1O1I1LaNd(arrstring[0].charAt(0));
        this.lli011lLANd = lOilLanD.lli0OiIlAND(arrstring[2], 0);
        this.O1il1llOLANd = arrstring[3];
        this.Oill1LAnD = arrstring[4];
        this.lIOILand = arrstring[5];
        this.lil0liLand = arrstring[6];
        this.l0illAND = lOilLanD.lli0OiIlAND(arrstring[7], 0);
        this.IO11O0LANd = lOilLanD.lli0OiIlAND(arrstring[8], 0);
        this.l11lLANd = lOilLanD.OOOIilanD(arrstring[9], 0L) << 10;
        this.lO110l1LANd = lOilLanD.OOOIilanD(arrstring[10], 0L) << 10;
        long l3 = lOilLanD.lli0OiIlAND(arrstring[11], 0L);
        if (map.containsKey(this.I1O1I1LaNd())) {
            O1IiIiI1LAND o1IiIiI1LAND = (O1IiIiI1LAND)map.get(this.I1O1I1LaNd());
            this.iOIl0LAnD = (Long)o1IiIiI1LAND.I1O1I1LaNd();
            this.l0iIlIO1laNd = (Long)o1IiIiI1LAND.OOOIilanD();
        } else {
            this.iOIl0LAnD = lOilLanD.lli0OiIlAND(arrstring[12], 0L);
            this.l0iIlIO1laNd = 0L;
        }
        long l4 = this.ii1li00Land = l3 < 1L ? 1L : l3;
        while (this.ii1li00Land < this.iOIl0LAnD + this.l0iIlIO1laNd) {
            this.ii1li00Land += 500L;
        }
        this.iIiO00OLaNd = l2 - this.ii1li00Land;
        this.lI00OlAND = arrstring[13];
        this.l0l00lAND = lOilLanD.OOOIilanD(arrstring[14], 0L);
        this.lli0OiIlAND = lOilLanD.OOOIilanD.split(arrstring[15])[0];
        this.li0iOILAND = arrstring[15];
        return true;
    }

    static OOOOO10iLAND I1O1I1LaNd(char c2) {
        OOOOO10iLAND oOOOO10iLAND;
        switch (c2) {
            case 'O': {
                oOOOO10iLAND = OOOOO10iLAND.lIOILand;
                break;
            }
            case 'A': 
            case 'R': {
                oOOOO10iLAND = OOOOO10iLAND.OOOIilanD;
                break;
            }
            case 'I': {
                oOOOO10iLAND = OOOOO10iLAND.lli0OiIlAND;
                break;
            }
            case 'S': 
            case 'W': {
                oOOOO10iLAND = OOOOO10iLAND.lI00OlAND;
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

