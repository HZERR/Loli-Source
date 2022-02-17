/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import loliland.launcher.client.Ii11I1lAnd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.IlOi10i1lANd;
import loliland.launcher.client.O0Ol0IlAnD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OIl11IOlanD;
import loliland.launcher.client.i0I0land;
import loliland.launcher.client.i0O00iOland;
import loliland.launcher.client.iOI10lAnd;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.l0l0iIlaND;
import loliland.launcher.client.l111llanD;
import loliland.launcher.client.lIilLaNd;
import loliland.launcher.client.lIl0iilland;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lOliOILaND;
import loliland.launcher.client.lli1OiIland;
import loliland.launcher.client.lll1Il0LaND;
import loliland.launcher.client.lllilIiiLANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class lO0lOOlAnD
extends lIilLaNd {
    private static final Logger lI00OlAND = LoggerFactory.getLogger(lO0lOOlAnD.class);
    private static final long lli0OiIlAND = lO0lOOlAnD.IOI1LaNd();

    @Override
    public String OOOIilanD() {
        return "Unix/BSD";
    }

    @Override
    public O1IiIiI1LAND li0iOILAND() {
        int[] arrn = new int[]{1, 1};
        String string = lIl0iilland.I1O1I1LaNd(arrn, "OpenBSD");
        arrn[1] = 2;
        String string2 = lIl0iilland.I1O1I1LaNd(arrn, "");
        arrn[1] = 4;
        String string3 = lIl0iilland.I1O1I1LaNd(arrn, "");
        String string4 = string3.split(":")[0].replace(string, "").replace(string2, "").trim();
        return new O1IiIiI1LAND(string, new l111llanD(string2, null, string4));
    }

    @Override
    protected int I1O1I1LaNd(int n2) {
        if (n2 < 64 && Iill1lanD.OOOIilanD("uname -m").indexOf("64") == -1) {
            return n2;
        }
        return 64;
    }

    @Override
    public l0l0iIlaND lIOILand() {
        return new lOliOILaND();
    }

    @Override
    public lli1OiIland lil0liLand() {
        return new lll1Il0LaND();
    }

    @Override
    public List Oill1LAnD() {
        return lO0lOOlAnD.li0iOILAND(-1);
    }

    @Override
    public List OOOIilanD(int n2) {
        List list = this.Oill1LAnD();
        Set set = lO0lOOlAnD.I1O1I1LaNd(list, n2, false);
        return list.stream().filter(iiO1LAnD2 -> set.contains(iiO1LAnD2.I1O1I1LaNd())).collect(Collectors.toList());
    }

    @Override
    public List lI00OlAND(int n2) {
        List list = this.Oill1LAnD();
        Set set = lO0lOOlAnD.I1O1I1LaNd(list, n2, true);
        return list.stream().filter(iiO1LAnD2 -> set.contains(iiO1LAnD2.I1O1I1LaNd())).collect(Collectors.toList());
    }

    @Override
    public iiO1LAnD lli0OiIlAND(int n2) {
        List list = lO0lOOlAnD.li0iOILAND(n2);
        if (list.isEmpty()) {
            return null;
        }
        return (iiO1LAnD)list.get(0);
    }

    private static List li0iOILAND(int n2) {
        List list;
        ArrayList<O0Ol0IlAnD> arrayList = new ArrayList<O0Ol0IlAnD>();
        String string = "ps -awwxo state,pid,ppid,user,uid,group,gid,pri,vsz,rss,etime,cputime,comm,majflt,minflt,nvscw,nivscw,args";
        if (n2 >= 0) {
            string = string + " -p " + n2;
        }
        if ((list = Iill1lanD.I1O1I1LaNd(string)).isEmpty() || list.size() < 2) {
            return arrayList;
        }
        list.remove(0);
        for (String string2 : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string2.trim(), 18);
            if (arrstring.length != 18) continue;
            arrayList.add(new O0Ol0IlAnD(n2 < 0 ? lOilLanD.lli0OiIlAND(arrstring[1], 0) : n2, arrstring));
        }
        return arrayList;
    }

    @Override
    public int lli011lLANd() {
        return lllilIiiLANd.INSTANCE.getpid();
    }

    @Override
    public int l0illAND() {
        List list = Iill1lanD.I1O1I1LaNd("ps -axo pid");
        if (!list.isEmpty()) {
            return list.size() - 1;
        }
        return 0;
    }

    @Override
    public int IO11O0LANd() {
        List list = Iill1lanD.I1O1I1LaNd("ps -axHo tid");
        if (!list.isEmpty()) {
            return list.size() - 1;
        }
        return 0;
    }

    @Override
    public long l11lLANd() {
        return System.currentTimeMillis() / 1000L - lli0OiIlAND;
    }

    @Override
    public long lO110l1LANd() {
        return lli0OiIlAND;
    }

    private static long IOI1LaNd() {
        return lOilLanD.OOOIilanD(Iill1lanD.OOOIilanD("sysctl -n kern.boottime").split(",")[0].replaceAll("\\D", ""), System.currentTimeMillis() / 1000L);
    }

    @Override
    public i0O00iOland iOIl0LAnD() {
        return new IlOi10i1lANd();
    }

    @Override
    public iOI10lAnd[] iIiO00OLaNd() {
        File[] arrfile2;
        ArrayList<Object> arrayList = new ArrayList<Object>();
        HashSet<String> hashSet = new HashSet<String>();
        for (File[] arrfile2 : this.I1O1I1LaNd(1, OIl11IOlanD.I1O1I1LaNd, Ii11I1lAnd.O1il1llOLANd, 0)) {
            File[] arrfile3 = new iOI10lAnd(arrfile2.lI00OlAND(), arrfile2.I1O1I1LaNd(), i0I0land.I1O1I1LaNd);
            arrayList.add(arrfile3);
            hashSet.add(arrfile2.lI00OlAND());
        }
        File file = new File("/etc/rc.d");
        if (file.exists() && file.isDirectory() && (arrfile2 = file.listFiles()) != null) {
            for (File file2 : arrfile2) {
                String string = file2.getName();
                if (hashSet.contains(string)) continue;
                iOI10lAnd iOI10lAnd2 = new iOI10lAnd(string, 0, i0I0land.OOOIilanD);
                arrayList.add(iOI10lAnd2);
            }
        } else {
            lI00OlAND.error("Directory: /etc/rc.d does not exist");
        }
        return arrayList.toArray(new iOI10lAnd[0]);
    }
}

