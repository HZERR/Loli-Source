/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.linux.LibC;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.IO11O0LANd;
import loliland.launcher.client.Ii11I1lAnd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.IliOOllOlanD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OIIII0IlAND;
import loliland.launcher.client.OIl11IOlanD;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.Oiil0LaNd;
import loliland.launcher.client.i0I0land;
import loliland.launcher.client.i0O00iOland;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.iIiO00OLaNd;
import loliland.launcher.client.iO1llaND;
import loliland.launcher.client.iOI10lAnd;
import loliland.launcher.client.iiO1LAnD;
import loliland.launcher.client.l0l0iIlaND;
import loliland.launcher.client.l111llanD;
import loliland.launcher.client.l1IOlANd;
import loliland.launcher.client.lI00ilAND;
import loliland.launcher.client.lIilLaNd;
import loliland.launcher.client.lO110l1LANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import loliland.launcher.client.lli1OiIland;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class l11IlanD
extends lIilLaNd {
    private static final Logger lli0OiIlAND = LoggerFactory.getLogger(l11IlanD.class);
    private static final String li0iOILAND = "os-release: {}";
    private static final String O1il1llOLANd = "lsb_release -a: {}";
    private static final String Oill1LAnD = "lsb-release: {}";
    private static final String lIOILand = " release ";
    private static final String lil0liLand = "(?:^\")|(?:\"$)";
    private static final String iilIi1laND = "oshi.linux.filename.properties";
    private static final long lli011lLANd = lOilLanD.OOOIilanD(Iill1lanD.OOOIilanD("getconf CLK_TCK"), 100L);
    static final long lI00OlAND;
    private static final int[] l0illAND;

    @Override
    public String OOOIilanD() {
        return "GNU/Linux";
    }

    @Override
    public O1IiIiI1LAND li0iOILAND() {
        Object object;
        IIOOOlIiLanD iIOOOlIiLanD = l11IlanD.l0l00lAND();
        String string = null;
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.lIiIii1LAnD);
        if (!list.isEmpty()) {
            for (String string2 : object = lOilLanD.OOOIilanD.split((CharSequence)list.get(0))) {
                if ("Linux".equals(string2) || "version".equals(string2)) continue;
                string = string2;
                break;
            }
        }
        object = new l111llanD((String)iIOOOlIiLanD.OOOIilanD(), (String)iIOOOlIiLanD.lI00OlAND(), string);
        return new O1IiIiI1LAND(iIOOOlIiLanD.I1O1I1LaNd(), object);
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
        return new Oiil0LaNd();
    }

    @Override
    public lli1OiIland lil0liLand() {
        return new iO1llaND();
    }

    @Override
    public List ii1li00Land() {
        return OOOIilanD ? super.ii1li00Land() : IO11O0LANd.I1O1I1LaNd();
    }

    @Override
    public iiO1LAnD lli0OiIlAND(int n2) {
        OIIII0IlAND oIIII0IlAND = new OIIII0IlAND(n2);
        if (!oIIII0IlAND.lli011lLANd().equals((Object)OOOOO10iLAND.lIOILand)) {
            return oIIII0IlAND;
        }
        return null;
    }

    @Override
    public List Oill1LAnD() {
        return this.OOOIilanD(-1);
    }

    @Override
    public List OOOIilanD(int n2) {
        File[] arrfile = iIiO00OLaNd.I1O1I1LaNd();
        if (n2 >= 0) {
            return l11IlanD.I1O1I1LaNd(l11IlanD.I1O1I1LaNd(l11IlanD.I1O1I1LaNd(arrfile), n2, false));
        }
        HashSet<Integer> hashSet = new HashSet<Integer>();
        for (File file : arrfile) {
            int n3 = lOilLanD.lli0OiIlAND(file.getName(), -2);
            if (n3 == -2) continue;
            hashSet.add(n3);
        }
        return l11IlanD.I1O1I1LaNd(hashSet);
    }

    @Override
    public List lI00OlAND(int n2) {
        File[] arrfile = iIiO00OLaNd.I1O1I1LaNd();
        return l11IlanD.I1O1I1LaNd(l11IlanD.I1O1I1LaNd(l11IlanD.I1O1I1LaNd(arrfile), n2, true));
    }

    private static List I1O1I1LaNd(Set set) {
        ArrayList<OIIII0IlAND> arrayList = new ArrayList<OIIII0IlAND>();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            int n2 = (Integer)iterator.next();
            OIIII0IlAND oIIII0IlAND = new OIIII0IlAND(n2);
            if (oIIII0IlAND.lli011lLANd().equals((Object)OOOOO10iLAND.lIOILand)) continue;
            arrayList.add(oIIII0IlAND);
        }
        return arrayList;
    }

    private static Map I1O1I1LaNd(File[] arrfile) {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (File file : arrfile) {
            int n2 = lOilLanD.lli0OiIlAND(file.getName(), 0);
            hashMap.put(n2, l11IlanD.li0iOILAND(n2));
        }
        return hashMap;
    }

    private static int li0iOILAND(int n2) {
        String string = liOIOOlLAnD.li0iOILAND(String.format("/proc/%d/stat", n2));
        if (string.isEmpty()) {
            return 0;
        }
        long[] arrl = lOilLanD.I1O1I1LaNd(string, l0illAND, iIiO00OLaNd.I1O1I1LaNd, ' ');
        return (int)arrl[0];
    }

    @Override
    public int lli011lLANd() {
        return l1IOlANd.INSTANCE.getpid();
    }

    @Override
    public int l0illAND() {
        return iIiO00OLaNd.I1O1I1LaNd().length;
    }

    @Override
    public int IO11O0LANd() {
        try {
            LibC.Sysinfo sysinfo = new LibC.Sysinfo();
            if (0 != LibC.INSTANCE.sysinfo(sysinfo)) {
                lli0OiIlAND.error("Failed to get process thread count. Error code: {}", (Object)Native.getLastError());
                return 0;
            }
            return sysinfo.procs;
        }
        catch (NoClassDefFoundError | UnsatisfiedLinkError linkageError) {
            lli0OiIlAND.error("Failed to get procs from sysinfo. {}", (Object)linkageError.getMessage());
            return 0;
        }
    }

    @Override
    public long l11lLANd() {
        return (long)lI00ilAND.I1O1I1LaNd();
    }

    @Override
    public long lO110l1LANd() {
        return lI00OlAND;
    }

    @Override
    public i0O00iOland iOIl0LAnD() {
        return new IliOOllOlanD();
    }

    private static IIOOOlIiLanD l0l00lAND() {
        IIOOOlIiLanD iIOOOlIiLanD = l11IlanD.I1O1I1LaNd("/etc/system-release");
        if (iIOOOlIiLanD != null) {
            return iIOOOlIiLanD;
        }
        iIOOOlIiLanD = l11IlanD.iOl10IlLAnd();
        if (iIOOOlIiLanD != null) {
            return iIOOOlIiLanD;
        }
        iIOOOlIiLanD = l11IlanD.lIiIii1LAnD();
        if (iIOOOlIiLanD != null) {
            return iIOOOlIiLanD;
        }
        iIOOOlIiLanD = l11IlanD.II1Iland();
        if (iIOOOlIiLanD != null) {
            return iIOOOlIiLanD;
        }
        String string = l11IlanD.IOI1LaNd();
        iIOOOlIiLanD = l11IlanD.I1O1I1LaNd(string);
        if (iIOOOlIiLanD != null) {
            return iIOOOlIiLanD;
        }
        String string2 = l11IlanD.OOOIilanD(string.replace("/etc/", "").replace("release", "").replace("version", "").replace("-", "").replace("_", ""));
        return new IIOOOlIiLanD(string2, "unknown", "unknown");
    }

    private static IIOOOlIiLanD iOl10IlLAnd() {
        String string = null;
        String string2 = "unknown";
        String string3 = "unknown";
        List list = liOIOOlLAnD.I1O1I1LaNd("/etc/os-release");
        for (String string4 : list) {
            if (string4.startsWith("VERSION=")) {
                lli0OiIlAND.debug(li0iOILAND, (Object)string4);
                string4 = string4.replace("VERSION=", "").replaceAll(lil0liLand, "").trim();
                String[] arrstring = string4.split("[()]");
                if (arrstring.length <= 1) {
                    arrstring = string4.split(", ");
                }
                if (arrstring.length > 0) {
                    string2 = arrstring[0].trim();
                }
                if (arrstring.length <= 1) continue;
                string3 = arrstring[1].trim();
                continue;
            }
            if (string4.startsWith("NAME=") && string == null) {
                lli0OiIlAND.debug(li0iOILAND, (Object)string4);
                string = string4.replace("NAME=", "").replaceAll(lil0liLand, "").trim();
                continue;
            }
            if (!string4.startsWith("VERSION_ID=") || !string2.equals("unknown")) continue;
            lli0OiIlAND.debug(li0iOILAND, (Object)string4);
            string2 = string4.replace("VERSION_ID=", "").replaceAll(lil0liLand, "").trim();
        }
        return string == null ? null : new IIOOOlIiLanD(string, string2, string3);
    }

    private static IIOOOlIiLanD lIiIii1LAnD() {
        String string = null;
        String string2 = "unknown";
        String string3 = "unknown";
        for (String string4 : Iill1lanD.I1O1I1LaNd("lsb_release -a")) {
            if (string4.startsWith("Description:")) {
                lli0OiIlAND.debug(O1il1llOLANd, (Object)string4);
                if (!(string4 = string4.replace("Description:", "").trim()).contains(lIOILand)) continue;
                IIOOOlIiLanD iIOOOlIiLanD = l11IlanD.I1O1I1LaNd(string4, lIOILand);
                string = (String)iIOOOlIiLanD.I1O1I1LaNd();
                if (string2.equals("unknown")) {
                    string2 = (String)iIOOOlIiLanD.OOOIilanD();
                }
                if (!string3.equals("unknown")) continue;
                string3 = (String)iIOOOlIiLanD.lI00OlAND();
                continue;
            }
            if (string4.startsWith("Distributor ID:") && string == null) {
                lli0OiIlAND.debug(O1il1llOLANd, (Object)string4);
                string = string4.replace("Distributor ID:", "").trim();
                continue;
            }
            if (string4.startsWith("Release:") && string2.equals("unknown")) {
                lli0OiIlAND.debug(O1il1llOLANd, (Object)string4);
                string2 = string4.replace("Release:", "").trim();
                continue;
            }
            if (!string4.startsWith("Codename:") || !string3.equals("unknown")) continue;
            lli0OiIlAND.debug(O1il1llOLANd, (Object)string4);
            string3 = string4.replace("Codename:", "").trim();
        }
        return string == null ? null : new IIOOOlIiLanD(string, string2, string3);
    }

    private static IIOOOlIiLanD II1Iland() {
        String string = null;
        String string2 = "unknown";
        String string3 = "unknown";
        List list = liOIOOlLAnD.I1O1I1LaNd("/etc/lsb-release");
        for (String string4 : list) {
            if (string4.startsWith("DISTRIB_DESCRIPTION=")) {
                lli0OiIlAND.debug(Oill1LAnD, (Object)string4);
                if (!(string4 = string4.replace("DISTRIB_DESCRIPTION=", "").replaceAll(lil0liLand, "").trim()).contains(lIOILand)) continue;
                IIOOOlIiLanD iIOOOlIiLanD = l11IlanD.I1O1I1LaNd(string4, lIOILand);
                string = (String)iIOOOlIiLanD.I1O1I1LaNd();
                if (string2.equals("unknown")) {
                    string2 = (String)iIOOOlIiLanD.OOOIilanD();
                }
                if (!string3.equals("unknown")) continue;
                string3 = (String)iIOOOlIiLanD.lI00OlAND();
                continue;
            }
            if (string4.startsWith("DISTRIB_ID=") && string == null) {
                lli0OiIlAND.debug(Oill1LAnD, (Object)string4);
                string = string4.replace("DISTRIB_ID=", "").replaceAll(lil0liLand, "").trim();
                continue;
            }
            if (string4.startsWith("DISTRIB_RELEASE=") && string2.equals("unknown")) {
                lli0OiIlAND.debug(Oill1LAnD, (Object)string4);
                string2 = string4.replace("DISTRIB_RELEASE=", "").replaceAll(lil0liLand, "").trim();
                continue;
            }
            if (!string4.startsWith("DISTRIB_CODENAME=") || !string3.equals("unknown")) continue;
            lli0OiIlAND.debug(Oill1LAnD, (Object)string4);
            string3 = string4.replace("DISTRIB_CODENAME=", "").replaceAll(lil0liLand, "").trim();
        }
        return string == null ? null : new IIOOOlIiLanD(string, string2, string3);
    }

    private static IIOOOlIiLanD I1O1I1LaNd(String string) {
        if (new File(string).exists()) {
            List list = liOIOOlLAnD.I1O1I1LaNd(string);
            for (String string2 : list) {
                lli0OiIlAND.debug("{}: {}", (Object)string, (Object)string2);
                if (string2.contains(lIOILand)) {
                    return l11IlanD.I1O1I1LaNd(string2, lIOILand);
                }
                if (!string2.contains(" VERSION ")) continue;
                return l11IlanD.I1O1I1LaNd(string2, " VERSION ");
            }
        }
        return null;
    }

    private static IIOOOlIiLanD I1O1I1LaNd(String string, String string2) {
        String[] arrstring = string.split(string2);
        String string3 = arrstring[0].trim();
        String string4 = "unknown";
        String string5 = "unknown";
        if (arrstring.length > 1) {
            if ((arrstring = arrstring[1].split("[()]")).length > 0) {
                string4 = arrstring[0].trim();
            }
            if (arrstring.length > 1) {
                string5 = arrstring[1].trim();
            }
        }
        return new IIOOOlIiLanD(string3, string4, string5);
    }

    protected static String IOI1LaNd() {
        File file2 = new File("/etc");
        File[] arrfile = file2.listFiles(file -> (file.getName().endsWith("-release") || file.getName().endsWith("-version") || file.getName().endsWith("_release") || file.getName().endsWith("_version")) && !file.getName().endsWith("os-release") && !file.getName().endsWith("lsb-release") && !file.getName().endsWith("system-release"));
        if (arrfile != null && arrfile.length > 0) {
            return arrfile[0].getPath();
        }
        if (new File("/etc/release").exists()) {
            return "/etc/release";
        }
        return "/etc/issue";
    }

    private static String OOOIilanD(String string) {
        if (string.isEmpty()) {
            return "Solaris";
        }
        if ("issue".equalsIgnoreCase(string)) {
            return "Unknown";
        }
        Properties properties = liOIOOlLAnD.O1il1llOLANd(iilIi1laND);
        String string2 = properties.getProperty(string.toLowerCase());
        return string2 != null ? string2 : string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    @Override
    public iOI10lAnd[] iIiO00OLaNd() {
        String string2;
        Object object;
        Object object22;
        ArrayList<Object> arrayList = new ArrayList<Object>();
        HashSet<String> hashSet = new HashSet<String>();
        for (Object object22 : this.I1O1I1LaNd(1, OIl11IOlanD.I1O1I1LaNd, Ii11I1lAnd.O1il1llOLANd, 0)) {
            object = new iOI10lAnd(object22.lI00OlAND(), object22.I1O1I1LaNd(), i0I0land.I1O1I1LaNd);
            arrayList.add(object);
            hashSet.add(object22.lI00OlAND());
        }
        boolean bl = false;
        object22 = Iill1lanD.I1O1I1LaNd("systemctl list-unit-files");
        object = object22.iterator();
        while (object.hasNext()) {
            File[] arrfile = (File[])object.next();
            String[] arrstring = lOilLanD.OOOIilanD.split((CharSequence)arrfile);
            if (arrstring.length < 2 || !arrstring[0].endsWith(".service") || !"enabled".equals(arrstring[1])) continue;
            String string3 = arrstring[0].substring(0, arrstring[0].length() - 8);
            int n2 = string3.lastIndexOf(46);
            String string4 = string2 = n2 < 0 || n2 > string3.length() - 2 ? string3 : string3.substring(n2 + 1);
            if (hashSet.contains(string3) || hashSet.contains(string2)) continue;
            iOI10lAnd iOI10lAnd2 = new iOI10lAnd(string3, 0, i0I0land.OOOIilanD);
            arrayList.add(iOI10lAnd2);
            bl = true;
        }
        if (!bl) {
            object = new File("/etc/init");
            if (((File)object).exists() && ((File)object).isDirectory()) {
                for (File file2 : ((File)object).listFiles((file, string) -> string.toLowerCase().endsWith(".conf"))) {
                    String string5;
                    string2 = file2.getName().substring(0, file2.getName().length() - 5);
                    int n3 = string2.lastIndexOf(46);
                    String string6 = string5 = n3 < 0 || n3 > string2.length() - 2 ? string2 : string2.substring(n3 + 1);
                    if (hashSet.contains(string2) || hashSet.contains(string5)) continue;
                    iOI10lAnd iOI10lAnd3 = new iOI10lAnd(string2, 0, i0I0land.OOOIilanD);
                    arrayList.add(iOI10lAnd3);
                }
            } else {
                lli0OiIlAND.error("Directory: /etc/init does not exist");
            }
        }
        return arrayList.toArray(new iOI10lAnd[0]);
    }

    public static long lI00ilAND() {
        return lli011lLANd;
    }

    static {
        long l2 = lO110l1LANd.lli0OiIlAND();
        if (l2 == 0L) {
            l2 = System.currentTimeMillis() / 1000L - (long)lI00ilAND.I1O1I1LaNd();
        }
        lI00OlAND = l2;
        l0illAND = new int[]{3};
    }
}

