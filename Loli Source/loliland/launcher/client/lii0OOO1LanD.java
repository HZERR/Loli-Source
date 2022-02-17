/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.platform.unix.LibCAPI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.Iil1Ol1laNd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O0iIl1ilaND;
import loliland.launcher.client.O1I01lLANd;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.lIllOlI0lAND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.li0I0iland;
import loliland.launcher.client.lii1IO0LaNd;

public class lii0OOO1LanD
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
    private long l0l00lAND;
    private long iOl10IlLAnd;

    public lii0OOO1LanD(int n2, String[] arrstring) {
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
        return lIllOlI0lAND.OOOIilanD(this.I1O1I1LaNd());
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
    public long iOl10IlLAnd() {
        return lIllOlI0lAND.lI00OlAND(this.I1O1I1LaNd());
    }

    @Override
    public int lIiIii1LAnD() {
        return (Integer)this.I1O1I1LaNd.get();
    }

    @Override
    public long II1Iland() {
        long l2 = 0L;
        String string = Iill1lanD.OOOIilanD("cpuset -gp " + this.I1O1I1LaNd());
        String[] arrstring = string.split(":");
        if (arrstring.length > 1) {
            String[] arrstring2;
            for (String string2 : arrstring2 = arrstring[1].split(",")) {
                int n2 = lOilLanD.lli0OiIlAND(string2.trim(), -1);
                if (n2 < 0) continue;
                l2 |= 1L << n2;
            }
        }
        return l2;
    }

    private int ll1ILAnd() {
        int[] arrn = new int[]{1, 14, 9, this.I1O1I1LaNd()};
        Memory memory = new Memory(32L);
        O0iIl1ilaND o0iIl1ilaND = new O0iIl1ilaND(new LibCAPI.size_t(32L));
        if (0 == Iil1Ol1laNd.INSTANCE.sysctl(arrn, arrn.length, memory, o0iIl1ilaND, null, LibCAPI.size_t.ZERO)) {
            String string = memory.getString(0L);
            if (string.contains("ELF32")) {
                return 32;
            }
            if (string.contains("ELF64")) {
                return 64;
            }
        }
        return 0;
    }

    @Override
    public List liOIOOLANd() {
        List list;
        ArrayList<li0I0iland> arrayList = new ArrayList<li0I0iland>();
        String string = "ps -awwxo tdname,lwp,state,etimes,systime,time,tdaddr,nivcsw,nvcsw,majflt,minflt,pri -H";
        if (this.I1O1I1LaNd() >= 0) {
            string = string + " -p " + this.I1O1I1LaNd();
        }
        if ((list = Iill1lanD.I1O1I1LaNd(string)).isEmpty() || list.size() < 2) {
            return arrayList;
        }
        list.remove(0);
        for (String string2 : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string2.trim(), 12);
            if (arrstring.length != 10) continue;
            arrayList.add(new li0I0iland(this.I1O1I1LaNd(), arrstring));
        }
        return arrayList;
    }

    @Override
    public long II1i1l0laND() {
        return this.lI00ilAND;
    }

    @Override
    public long OOOliOOllANd() {
        return this.l0l00lAND;
    }

    @Override
    public long IiiilAnD() {
        return this.iOl10IlLAnd;
    }

    @Override
    public boolean l0IO0LAnd() {
        String[] arrstring;
        String string = "ps -awwxo state,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etimes,systime,time,comm,majflt,minflt,args -p " + this.I1O1I1LaNd();
        List list = Iill1lanD.I1O1I1LaNd(string);
        if (list.size() > 1 && (arrstring = lOilLanD.OOOIilanD.split(((String)list.get(1)).trim(), 18)).length == 18) {
            return this.I1O1I1LaNd(arrstring);
        }
        this.lil0liLand = OOOOO10iLAND.lIOILand;
        return false;
    }

    private boolean I1O1I1LaNd(String[] arrstring) {
        long l2 = System.currentTimeMillis();
        switch (arrstring[0].charAt(0)) {
            case 'R': {
                this.lil0liLand = OOOOO10iLAND.OOOIilanD;
                break;
            }
            case 'I': 
            case 'S': {
                this.lil0liLand = OOOOO10iLAND.lI00OlAND;
                break;
            }
            case 'D': 
            case 'L': 
            case 'U': {
                this.lil0liLand = OOOOO10iLAND.lli0OiIlAND;
                break;
            }
            case 'Z': {
                this.lil0liLand = OOOOO10iLAND.li0iOILAND;
                break;
            }
            case 'T': {
                this.lil0liLand = OOOOO10iLAND.O1il1llOLANd;
                break;
            }
            default: {
                this.lil0liLand = OOOOO10iLAND.Oill1LAnD;
            }
        }
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
        this.lO110l1LANd = lOilLanD.lli0OiIlAND(arrstring[12], 0L);
        this.l0iIlIO1laNd = lOilLanD.lli0OiIlAND(arrstring[13], 0L) - this.lO110l1LANd;
        this.lI00OlAND = arrstring[14];
        this.OOOIilanD = this.lI00OlAND.substring(this.lI00OlAND.lastIndexOf(47) + 1);
        this.lI00ilAND = lOilLanD.OOOIilanD(arrstring[15], 0L);
        this.l0l00lAND = lOilLanD.OOOIilanD(arrstring[16], 0L);
        long l4 = lOilLanD.OOOIilanD(arrstring[17], 0L);
        long l5 = lOilLanD.OOOIilanD(arrstring[18], 0L);
        this.iOl10IlLAnd = l5 + l4;
        this.lli0OiIlAND = arrstring[19];
        return true;
    }
}

