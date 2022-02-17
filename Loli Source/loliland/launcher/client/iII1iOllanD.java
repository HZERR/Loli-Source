/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.mac.SystemB;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import loliland.launcher.client.I0OIOlaNd;
import loliland.launcher.client.II1Iland;
import loliland.launcher.client.IIl111lAnD;
import loliland.launcher.client.Ii11I1lAnd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OIl11IOlanD;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.i0I0land;
import loliland.launcher.client.i0O00iOland;
import loliland.launcher.client.i1iilAND;
import loliland.launcher.client.iOI10lAnd;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.l0IO0LAnd;
import loliland.launcher.client.l0l0iIlaND;
import loliland.launcher.client.l111llanD;
import loliland.launcher.client.lIilLaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.liiIILaND;
import loliland.launcher.client.lli1OiIland;
import loliland.launcher.client.lliI01iland;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class iII1iOllanD
extends lIilLaNd {
    private static final Logger lli0OiIlAND = LoggerFactory.getLogger(iII1iOllanD.class);
    public static final String lI00OlAND = "oshi.macos.versions.properties";
    private static final String li0iOILAND = "/System/Library/LaunchAgents";
    private static final String O1il1llOLANd = "/System/Library/LaunchDaemons";
    private int Oill1LAnD = 1024;
    private final String lIOILand;
    private final int lil0liLand;
    private final int iilIi1laND;
    private static final long lli011lLANd;

    public iII1iOllanD() {
        String string = System.getProperty("os.version");
        int n2 = lOilLanD.Oill1LAnD(string);
        int n3 = lOilLanD.li0iOILAND(string, 2);
        if (n2 == 10 && n3 > 15) {
            String string2 = Iill1lanD.OOOIilanD("sw_vers -productVersion");
            if (!string2.isEmpty()) {
                string = string2;
            }
            n2 = lOilLanD.Oill1LAnD(string);
            n3 = lOilLanD.li0iOILAND(string, 2);
        }
        this.lIOILand = string;
        this.lil0liLand = n2;
        this.iilIi1laND = n3;
        this.Oill1LAnD = liiIILaND.I1O1I1LaNd("kern.maxproc", 4096);
    }

    @Override
    public String OOOIilanD() {
        return "Apple";
    }

    @Override
    public O1IiIiI1LAND li0iOILAND() {
        String string = this.lil0liLand > 10 || this.lil0liLand == 10 && this.iilIi1laND >= 12 ? "macOS" : System.getProperty("os.name");
        String string2 = this.IOI1LaNd();
        String string3 = liiIILaND.I1O1I1LaNd("kern.osversion", "");
        return new O1IiIiI1LAND(string, new l111llanD(this.lIOILand, string2, string3));
    }

    private String IOI1LaNd() {
        Properties properties = liOIOOlLAnD.O1il1llOLANd(lI00OlAND);
        String string = null;
        if (this.lil0liLand > 10) {
            string = properties.getProperty(Integer.toString(this.lil0liLand));
        } else if (this.lil0liLand == 10) {
            string = properties.getProperty(this.lil0liLand + "." + this.iilIi1laND);
        }
        if (iiIIIlO1lANd.I1O1I1LaNd(string)) {
            lli0OiIlAND.warn("Unable to parse version {}.{} to a codename.", (Object)this.lil0liLand, (Object)this.iilIi1laND);
        }
        return string;
    }

    @Override
    protected int I1O1I1LaNd(int n2) {
        if (n2 == 64 || this.lil0liLand == 10 && this.iilIi1laND > 6) {
            return 64;
        }
        return lOilLanD.lli0OiIlAND(Iill1lanD.OOOIilanD("getconf LONG_BIT"), 32);
    }

    @Override
    public l0l0iIlaND lIOILand() {
        return new i1iilAND();
    }

    @Override
    public lli1OiIland lil0liLand() {
        return new lliI01iland(this.l0iIlIO1laNd());
    }

    @Override
    public List ii1li00Land() {
        return OOOIilanD ? super.ii1li00Land() : II1Iland.I1O1I1LaNd();
    }

    @Override
    public List Oill1LAnD() {
        ArrayList<iiO1LAnD> arrayList = new ArrayList<iiO1LAnD>();
        int[] arrn = new int[this.Oill1LAnD];
        int n2 = SystemB.INSTANCE.proc_listpids(1, 0, arrn, arrn.length * SystemB.INT_SIZE) / SystemB.INT_SIZE;
        for (int i2 = 0; i2 < n2; ++i2) {
            iiO1LAnD iiO1LAnD2;
            if (arrn[i2] == 0 || (iiO1LAnD2 = this.lli0OiIlAND(arrn[i2])) == null) continue;
            arrayList.add(iiO1LAnD2);
        }
        return arrayList;
    }

    @Override
    public iiO1LAnD lli0OiIlAND(int n2) {
        IIl111lAnD iIl111lAnD = new IIl111lAnD(n2, this.iilIi1laND);
        return iIl111lAnD.lli011lLANd().equals((Object)OOOOO10iLAND.lIOILand) ? null : iIl111lAnD;
    }

    @Override
    public List OOOIilanD(int n2) {
        List list = this.Oill1LAnD();
        Set set = iII1iOllanD.I1O1I1LaNd(list, n2, false);
        return list.stream().filter(iiO1LAnD2 -> set.contains(iiO1LAnD2.I1O1I1LaNd())).collect(Collectors.toList());
    }

    @Override
    public List lI00OlAND(int n2) {
        List list = this.Oill1LAnD();
        Set set = iII1iOllanD.I1O1I1LaNd(list, n2, true);
        return list.stream().filter(iiO1LAnD2 -> set.contains(iiO1LAnD2.I1O1I1LaNd())).collect(Collectors.toList());
    }

    @Override
    public int lli011lLANd() {
        return SystemB.INSTANCE.getpid();
    }

    @Override
    public int l0illAND() {
        return SystemB.INSTANCE.proc_listpids(1, 0, null, 0) / SystemB.INT_SIZE;
    }

    @Override
    public int IO11O0LANd() {
        int[] arrn = new int[this.l0illAND() + 10];
        int n2 = SystemB.INSTANCE.proc_listpids(1, 0, arrn, arrn.length) / SystemB.INT_SIZE;
        int n3 = 0;
        SystemB.ProcTaskInfo procTaskInfo = new SystemB.ProcTaskInfo();
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = SystemB.INSTANCE.proc_pidinfo(arrn[i2], 4, 0L, procTaskInfo, procTaskInfo.size());
            if (n4 == -1) continue;
            n3 += procTaskInfo.pti_threadnum;
        }
        return n3;
    }

    @Override
    public long l11lLANd() {
        return System.currentTimeMillis() / 1000L - lli011lLANd;
    }

    @Override
    public long lO110l1LANd() {
        return lli011lLANd;
    }

    @Override
    public i0O00iOland iOIl0LAnD() {
        return new I0OIOlaNd();
    }

    @Override
    public iOI10lAnd[] iIiO00OLaNd() {
        Object object;
        Object object22;
        ArrayList<iOI10lAnd> arrayList = new ArrayList<iOI10lAnd>();
        HashSet<String> hashSet = new HashSet<String>();
        for (Object object22 : this.I1O1I1LaNd(1, OIl11IOlanD.I1O1I1LaNd, Ii11I1lAnd.O1il1llOLANd, 0)) {
            object = new iOI10lAnd(object22.lI00OlAND(), object22.I1O1I1LaNd(), i0I0land.I1O1I1LaNd);
            arrayList.add((iOI10lAnd)object);
            hashSet.add(object22.lI00OlAND());
        }
        ArrayList arrayList2 = new ArrayList();
        object22 = new File(li0iOILAND);
        if (((File)object22).exists() && ((File)object22).isDirectory()) {
            arrayList2.addAll(Arrays.asList(((File)object22).listFiles((file, string) -> string.toLowerCase().endsWith(".plist"))));
        } else {
            lli0OiIlAND.error("Directory: /System/Library/LaunchAgents does not exist");
        }
        object22 = new File(O1il1llOLANd);
        if (((File)object22).exists() && ((File)object22).isDirectory()) {
            arrayList2.addAll(Arrays.asList(((File)object22).listFiles((file, string) -> string.toLowerCase().endsWith(".plist"))));
        } else {
            lli0OiIlAND.error("Directory: /System/Library/LaunchDaemons does not exist");
        }
        object = arrayList2.iterator();
        while (object.hasNext()) {
            String string2;
            File file2 = (File)object.next();
            String string3 = file2.getName().substring(0, file2.getName().length() - 6);
            int n2 = string3.lastIndexOf(46);
            String string4 = string2 = n2 < 0 || n2 > string3.length() - 2 ? string3 : string3.substring(n2 + 1);
            if (hashSet.contains(string3) || hashSet.contains(string2)) continue;
            iOI10lAnd iOI10lAnd2 = new iOI10lAnd(string3, 0, i0I0land.OOOIilanD);
            arrayList.add(iOI10lAnd2);
        }
        return arrayList.toArray(new iOI10lAnd[0]);
    }

    @Override
    public List I1O1I1LaNd(boolean bl) {
        return l0IO0LAnd.I1O1I1LaNd(bl);
    }

    static {
        SystemB.Timeval timeval = new SystemB.Timeval();
        lli011lLANd = !liiIILaND.I1O1I1LaNd("kern.boottime", timeval) || timeval.tv_sec.longValue() == 0L ? lOilLanD.OOOIilanD(Iill1lanD.OOOIilanD("sysctl -n kern.boottime").split(",")[0].replaceAll("\\D", ""), System.currentTimeMillis() / 1000L) : timeval.tv_sec.longValue();
    }
}

