/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.unix.solaris.LibKstat;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import loliland.launcher.client.II0101Iland;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OII01land;
import loliland.launcher.client.i0I0land;
import loliland.launcher.client.i0O00iOland;
import loliland.launcher.client.i100laND;
import loliland.launcher.client.iIiO00OLaNd;
import loliland.launcher.client.iOI10lAnd;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.iiiIOI0lAnd;
import loliland.launcher.client.il00iLAnD;
import loliland.launcher.client.l0IilaND;
import loliland.launcher.client.l0l0iIlaND;
import loliland.launcher.client.l111llanD;
import loliland.launcher.client.lIiIlOi0lanD;
import loliland.launcher.client.lIilLaNd;
import loliland.launcher.client.lO0li10llaND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lli1OiIland;

public class IOi0ii1OlanD
extends lIilLaNd {
    private static final String lI00OlAND = "s,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etime,time,comm,args";
    private static final String lli0OiIlAND = "ps -o s,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etime,time,comm,args -p ";
    private static final String li0iOILAND = "ps -eo s,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etime,time,comm,args";
    private static final long O1il1llOLANd = IOi0ii1OlanD.l0l00lAND();

    @Override
    public String OOOIilanD() {
        return "Oracle";
    }

    @Override
    public O1IiIiI1LAND li0iOILAND() {
        String[] arrstring = lOilLanD.OOOIilanD.split(Iill1lanD.OOOIilanD("uname -rv"));
        String string = arrstring[0];
        String string2 = null;
        if (arrstring.length > 1) {
            string2 = arrstring[1];
        }
        return new O1IiIiI1LAND("SunOS", new l111llanD(string, "Solaris", string2));
    }

    @Override
    protected int I1O1I1LaNd(int n2) {
        if (n2 == 64) {
            return 64;
        }
        return lOilLanD.lli0OiIlAND(Iill1lanD.OOOIilanD("isainfo -b"), 32);
    }

    @Override
    public l0l0iIlaND lIOILand() {
        return new OII01land();
    }

    @Override
    public lli1OiIland lil0liLand() {
        return new iiiIOI0lAnd();
    }

    @Override
    public List ii1li00Land() {
        return OOOIilanD ? super.ii1li00Land() : i100laND.I1O1I1LaNd();
    }

    @Override
    public iiO1LAnD lli0OiIlAND(int n2) {
        List list = IOi0ii1OlanD.I1O1I1LaNd(lli0OiIlAND, n2);
        if (list.isEmpty()) {
            return null;
        }
        return (iiO1LAnD)list.get(0);
    }

    @Override
    public List Oill1LAnD() {
        return IOi0ii1OlanD.IOI1LaNd();
    }

    @Override
    public List OOOIilanD(int n2) {
        List list = IOi0ii1OlanD.IOI1LaNd();
        Set set = IOi0ii1OlanD.I1O1I1LaNd(list, n2, false);
        return list.stream().filter(iiO1LAnD2 -> set.contains(iiO1LAnD2.I1O1I1LaNd())).collect(Collectors.toList());
    }

    @Override
    public List lI00OlAND(int n2) {
        List list = IOi0ii1OlanD.IOI1LaNd();
        Set set = IOi0ii1OlanD.I1O1I1LaNd(list, n2, true);
        return list.stream().filter(iiO1LAnD2 -> set.contains(iiO1LAnD2.I1O1I1LaNd())).collect(Collectors.toList());
    }

    private static List IOI1LaNd() {
        return IOi0ii1OlanD.I1O1I1LaNd(li0iOILAND, -1);
    }

    private static List I1O1I1LaNd(String string, int n2) {
        List list = n2 < 0 ? Iill1lanD.I1O1I1LaNd(string) : Iill1lanD.I1O1I1LaNd(string + n2);
        List list2 = n2 < 0 ? Iill1lanD.I1O1I1LaNd("prstat -v 1 1") : Iill1lanD.I1O1I1LaNd("prstat -v -p " + n2 + " 1 1");
        Map map = II0101Iland.I1O1I1LaNd(list, 1, 15, list2, false);
        return map.entrySet().stream().map(entry -> new II0101Iland((Integer)entry.getKey(), (String[])entry.getValue())).collect(Collectors.toList());
    }

    @Override
    public int lli011lLANd() {
        return il00iLAnD.INSTANCE.getpid();
    }

    @Override
    public int l0illAND() {
        return iIiO00OLaNd.I1O1I1LaNd().length;
    }

    @Override
    public int IO11O0LANd() {
        List list = Iill1lanD.I1O1I1LaNd("ps -eLo pid");
        if (!list.isEmpty()) {
            return list.size() - 1;
        }
        return this.l0illAND();
    }

    @Override
    public long l11lLANd() {
        return IOi0ii1OlanD.lI00ilAND();
    }

    private static long lI00ilAND() {
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            LibKstat.Kstat kstat = lIiIlOi0lanD.I1O1I1LaNd("unix", 0, "system_misc");
            if (kstat != null) {
                long l2 = kstat.ks_snaptime / 1000000000L;
                return l2;
            }
        }
        return 0L;
    }

    @Override
    public long lO110l1LANd() {
        return O1il1llOLANd;
    }

    private static long l0l00lAND() {
        try (lIiIlOi0lanD lIiIlOi0lanD2 = lO0li10llaND.I1O1I1LaNd();){
            LibKstat.Kstat kstat = lIiIlOi0lanD.I1O1I1LaNd("unix", 0, "system_misc");
            if (kstat != null && lIiIlOi0lanD.I1O1I1LaNd(kstat)) {
                long l2 = lO0li10llaND.OOOIilanD(kstat, "boot_time");
                return l2;
            }
        }
        return System.currentTimeMillis() / 1000L - IOi0ii1OlanD.lI00ilAND();
    }

    @Override
    public i0O00iOland iOIl0LAnD() {
        return new l0IilaND();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public iOI10lAnd[] iIiO00OLaNd() {
        File[] arrfile2;
        File[] arrfile;
        ArrayList<iOI10lAnd> arrayList = new ArrayList<iOI10lAnd>();
        ArrayList<String> arrayList2 = new ArrayList<String>();
        File file = new File("/etc/init.d");
        if (file.exists() && file.isDirectory() && (arrfile = file.listFiles()) != null) {
            void string;
            arrfile2 = arrfile;
            int n2 = arrfile2.length;
            boolean bl = false;
            while (++string < n2) {
                File n22 = arrfile2[string];
                arrayList2.add(n22.getName());
            }
        }
        arrfile2 = Iill1lanD.I1O1I1LaNd("svcs -p");
        block1: for (String string : arrfile2) {
            if (string.startsWith("online")) {
                int arrstring = string.lastIndexOf(":/");
                if (arrstring <= 0) continue;
                String string2 = string.substring(arrstring + 1);
                if (string2.endsWith(":default")) {
                    string2 = string2.substring(0, string2.length() - 8);
                }
                arrayList.add(new iOI10lAnd(string2, 0, i0I0land.OOOIilanD));
                continue;
            }
            if (string.startsWith(" ")) {
                String[] arrstring = lOilLanD.OOOIilanD.split(string.trim());
                if (arrstring.length != 3) continue;
                arrayList.add(new iOI10lAnd(arrstring[2], lOilLanD.lli0OiIlAND(arrstring[1], 0), i0I0land.I1O1I1LaNd));
                continue;
            }
            if (!string.startsWith("legacy_run")) continue;
            for (String string2 : arrayList2) {
                if (!string.endsWith(string2)) continue;
                arrayList.add(new iOI10lAnd(string2, 0, i0I0land.OOOIilanD));
                continue block1;
            }
        }
        return arrayList.toArray(new iOI10lAnd[0]);
    }
}

