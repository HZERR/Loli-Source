/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.unix.aix.Perfstat;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OI1II1OIlanD;
import loliland.launcher.client.OO1Oi0LAnD;
import loliland.launcher.client.OO1iiOlanD;
import loliland.launcher.client.OO1lILANd;
import loliland.launcher.client.i0I0land;
import loliland.launcher.client.i0O00iOland;
import loliland.launcher.client.iO0IlAND;
import loliland.launcher.client.iOI10lAnd;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.iiIiOLAnd;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.illiLanD;
import loliland.launcher.client.l0l0iIlaND;
import loliland.launcher.client.l111llanD;
import loliland.launcher.client.lIilLaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lli1OiIland;
import loliland.launcher.client.lliOlOLanD;
import loliland.launcher.client.lliliilanD;

public class I0l0Land
extends lIilLaNd {
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(OO1Oi0LAnD::I1O1I1LaNd);
    Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(illiLanD::I1O1I1LaNd, lii1IO0LaNd.I1O1I1LaNd());
    private static final long li0iOILAND = I0l0Land.IOI1LaNd() / 1000L;

    @Override
    public String OOOIilanD() {
        return "IBM";
    }

    @Override
    public O1IiIiI1LAND li0iOILAND() {
        String string;
        Perfstat.perfstat_partition_config_t perfstat_partition_config_t2 = (Perfstat.perfstat_partition_config_t)this.lli0OiIlAND.get();
        String string2 = System.getProperty("os.name");
        String string3 = System.getProperty("os.arch");
        String string4 = System.getProperty("os.version");
        if (iiIIIlO1lANd.I1O1I1LaNd(string4)) {
            string4 = Iill1lanD.OOOIilanD("oslevel");
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string = Native.toString(perfstat_partition_config_t2.OSBuild))) {
            string = Iill1lanD.OOOIilanD("oslevel -s");
        } else {
            int n2 = string.lastIndexOf(32);
            if (n2 > 0 && n2 < string.length()) {
                string = string.substring(n2 + 1);
            }
        }
        return new O1IiIiI1LAND(string2, new l111llanD(string4, string3, string));
    }

    @Override
    protected int I1O1I1LaNd(int n2) {
        if (n2 == 64) {
            return 64;
        }
        return (((Perfstat.perfstat_partition_config_t)this.lli0OiIlAND.get()).conf & 0x800000) > 0 ? 64 : 32;
    }

    @Override
    public l0l0iIlaND lIOILand() {
        return new iO0IlAND();
    }

    @Override
    public lli1OiIland lil0liLand() {
        return new iiIiOLAnd();
    }

    @Override
    public List Oill1LAnD() {
        return this.I1O1I1LaNd("ps -A -o st,pid,ppid,user,uid,group,gid,thcount,pri,vsize,rssize,etime,time,comm,pagein,args", -1);
    }

    @Override
    public List OOOIilanD(int n2) {
        List list = this.Oill1LAnD();
        Set set = I0l0Land.I1O1I1LaNd(list, n2, false);
        return list.stream().filter(iiO1LAnD2 -> set.contains(iiO1LAnD2.I1O1I1LaNd())).collect(Collectors.toList());
    }

    @Override
    public List lI00OlAND(int n2) {
        List list = this.Oill1LAnD();
        Set set = I0l0Land.I1O1I1LaNd(list, n2, true);
        return list.stream().filter(iiO1LAnD2 -> set.contains(iiO1LAnD2.I1O1I1LaNd())).collect(Collectors.toList());
    }

    @Override
    public iiO1LAnD lli0OiIlAND(int n2) {
        List list = this.I1O1I1LaNd("ps -o st,pid,ppid,user,uid,group,gid,thcount,pri,vsize,rssize,etime,time,comm,pagein,args -p ", n2);
        if (list.isEmpty()) {
            return null;
        }
        return (iiO1LAnD)list.get(0);
    }

    private List I1O1I1LaNd(String string, int n2) {
        Perfstat.perfstat_process_t[] arrperfstat_process_t = (Perfstat.perfstat_process_t[])this.lI00OlAND.get();
        List list = Iill1lanD.I1O1I1LaNd(string + (n2 < 0 ? "" : Integer.valueOf(n2)));
        if (list.isEmpty() || list.size() < 2) {
            return Collections.emptyList();
        }
        HashMap<Integer, O1IiIiI1LAND> hashMap = new HashMap<Integer, O1IiIiI1LAND>();
        for (Perfstat.perfstat_process_t arrstring : arrperfstat_process_t) {
            hashMap.put((int)arrstring.pid, new O1IiIiI1LAND((long)arrstring.ucpu_time, (long)arrstring.scpu_time));
        }
        list.remove(0);
        ArrayList arrayList = new ArrayList();
        for (String string2 : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string2.trim(), 16);
            if (arrstring.length != 16) continue;
            arrayList.add(new OO1iiOlanD(n2 < 0 ? lOilLanD.lli0OiIlAND(arrstring[1], 0) : n2, arrstring, hashMap, this.lI00OlAND));
        }
        return arrayList;
    }

    @Override
    public int lli011lLANd() {
        return OO1lILANd.INSTANCE.getpid();
    }

    @Override
    public int l0illAND() {
        return ((Perfstat.perfstat_process_t[])this.lI00OlAND.get()).length;
    }

    @Override
    public int IO11O0LANd() {
        long l2 = 0L;
        for (Perfstat.perfstat_process_t perfstat_process_t2 : (Perfstat.perfstat_process_t[])this.lI00OlAND.get()) {
            l2 += perfstat_process_t2.num_threads;
        }
        return (int)l2;
    }

    @Override
    public long l11lLANd() {
        return System.currentTimeMillis() / 1000L - li0iOILAND;
    }

    @Override
    public long lO110l1LANd() {
        return li0iOILAND;
    }

    private static long IOI1LaNd() {
        long l2 = lliliilanD.I1O1I1LaNd();
        if (l2 >= 1000L) {
            return l2;
        }
        return System.currentTimeMillis() - OI1II1OIlanD.I1O1I1LaNd();
    }

    @Override
    public i0O00iOland iOIl0LAnD() {
        return new lliOlOLanD();
    }

    @Override
    public iOI10lAnd[] iIiO00OLaNd() {
        File[] arrfile;
        Object object;
        ArrayList<iOI10lAnd> arrayList = new ArrayList<iOI10lAnd>();
        List list = Iill1lanD.I1O1I1LaNd("lssrc -a");
        if (list.size() > 1) {
            list.remove(0);
            object = list.iterator();
            while (object.hasNext()) {
                arrfile = (File[])object.next();
                Object[] arrobject = lOilLanD.OOOIilanD.split(arrfile.trim());
                if (arrfile.contains("active")) {
                    if (arrobject.length == 4) {
                        arrayList.add(new iOI10lAnd((String)arrobject[0], lOilLanD.lli0OiIlAND((String)arrobject[2], 0), i0I0land.I1O1I1LaNd));
                        continue;
                    }
                    if (arrobject.length != 3) continue;
                    arrayList.add(new iOI10lAnd((String)arrobject[0], lOilLanD.lli0OiIlAND((String)arrobject[1], 0), i0I0land.I1O1I1LaNd));
                    continue;
                }
                if (!arrfile.contains("inoperative")) continue;
                arrayList.add(new iOI10lAnd((String)arrobject[0], 0, i0I0land.OOOIilanD));
            }
        }
        if (((File)(object = new File("/etc/rc.d/init.d"))).exists() && ((File)object).isDirectory() && (arrfile = ((File)object).listFiles()) != null) {
            for (File file : arrfile) {
                String string = Iill1lanD.OOOIilanD(file.getAbsolutePath() + " status");
                if (string.contains("running")) {
                    arrayList.add(new iOI10lAnd(file.getName(), lOilLanD.I1O1I1LaNd(string, 0), i0I0land.I1O1I1LaNd));
                    continue;
                }
                arrayList.add(new iOI10lAnd(file.getName(), 0, i0I0land.OOOIilanD));
            }
        }
        return arrayList.toArray(new iOI10lAnd[0]);
    }
}

