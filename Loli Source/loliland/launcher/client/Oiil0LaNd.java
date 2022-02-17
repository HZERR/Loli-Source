/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.linux.LibC;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.l00i1O0LAnD;
import loliland.launcher.client.lI10lAnd;
import loliland.launcher.client.lOiO0l0lAnd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Oiil0LaNd
extends lI10lAnd {
    private static final Logger lil0liLand = LoggerFactory.getLogger(Oiil0LaNd.class);
    public static final String li0iOILAND = "oshi.os.linux.filesystem.path.excludes";
    public static final String O1il1llOLANd = "oshi.os.linux.filesystem.path.includes";
    public static final String Oill1LAnD = "oshi.os.linux.filesystem.volume.excludes";
    public static final String lIOILand = "oshi.os.linux.filesystem.volume.includes";
    private static final List iilIi1laND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.linux.filesystem.path.excludes");
    private static final List lli011lLANd = l00i1O0LAnD.I1O1I1LaNd("oshi.os.linux.filesystem.path.includes");
    private static final List l0illAND = l00i1O0LAnD.I1O1I1LaNd("oshi.os.linux.filesystem.volume.excludes");
    private static final List IO11O0LANd = l00i1O0LAnD.I1O1I1LaNd("oshi.os.linux.filesystem.volume.includes");
    private static final String l11lLANd = "\\040";

    @Override
    public List I1O1I1LaNd(boolean bl) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        File file = new File("/dev/mapper");
        File[] arrfile = file.listFiles();
        if (arrfile != null) {
            for (File arrfile2 : arrfile) {
                try {
                    hashMap.put(arrfile2.getCanonicalPath(), arrfile2.getAbsolutePath());
                }
                catch (IOException iOException) {
                    lil0liLand.error("Couldn't get canonical path for {}. {}", (Object)arrfile2.getName(), (Object)iOException.getMessage());
                }
            }
        }
        HashMap hashMap2 = new HashMap();
        File file2 = new File("/dev/disk/by-uuid");
        File[] arrfile3 = file2.listFiles();
        if (arrfile3 != null) {
            for (File file3 : arrfile3) {
                try {
                    String iOException = file3.getCanonicalPath();
                    hashMap2.put(iOException, file3.getName().toLowerCase());
                    if (!hashMap.containsKey(iOException)) continue;
                    hashMap2.put(hashMap.get(iOException), file3.getName().toLowerCase());
                }
                catch (IOException iOException) {
                    lil0liLand.error("Couldn't get canonical path for {}. {}", (Object)file3.getName(), (Object)iOException.getMessage());
                }
            }
        }
        return Oiil0LaNd.I1O1I1LaNd(null, hashMap2, bl);
    }

    static List I1O1I1LaNd(String string, Map map) {
        return Oiil0LaNd.I1O1I1LaNd(string, map, false);
    }

    private static List I1O1I1LaNd(String string, Map map, boolean bl) {
        ArrayList<lOiO0l0lAnd> arrayList = new ArrayList<lOiO0l0lAnd>();
        Map map2 = Oiil0LaNd.lli0OiIlAND();
        List list = liOIOOlLAnD.I1O1I1LaNd(iI11I1lllaNd.O1il1llOLANd);
        for (String string2 : list) {
            Object object;
            String string3;
            String string4;
            String[] arrstring = string2.split(" ");
            if (arrstring.length < 6) continue;
            String string5 = string4 = arrstring[0].replace(l11lLANd, " ");
            String string6 = arrstring[1].replace(l11lLANd, " ");
            if (string6.equals("/")) {
                string5 = "/";
            }
            String string7 = arrstring[2];
            if (bl && lI00OlAND.contains(string7) || !string6.equals("/") && (lli0OiIlAND.contains(string7) || l00i1O0LAnD.I1O1I1LaNd(string6, string4, lli011lLANd, iilIi1laND, IO11O0LANd, l0illAND))) continue;
            String string8 = arrstring[3];
            if (string != null && !string.equals(string5)) continue;
            String string9 = string3 = map != null ? map.getOrDefault(arrstring[0], "") : "";
            String string10 = string4.startsWith("/dev") ? "Local Disk" : (string4.equals("tmpfs") ? "Ram Disk" : (lI00OlAND.contains(string7) ? "Network Disk" : "Mount Point"));
            String string11 = "";
            String string12 = "/dev/mapper/";
            Path path = Paths.get(string4, new String[0]);
            if (path.toFile().exists() && Files.isSymbolicLink(path)) {
                try {
                    Path path2 = Files.readSymbolicLink(path);
                    Path path3 = Paths.get(string12 + path2.toString(), new String[0]);
                    if (path3.toFile().exists()) {
                        string11 = path3.normalize().toString();
                    }
                }
                catch (IOException iOException) {
                    lil0liLand.warn("Couldn't access symbolic path  {}. {}", (Object)path, (Object)iOException.getMessage());
                }
            }
            long l2 = 0L;
            long l3 = 0L;
            long l4 = 0L;
            long l5 = 0L;
            long l6 = 0L;
            try {
                object = new LibC.Statvfs();
                if (0 == LibC.INSTANCE.statvfs(string6, (LibC.Statvfs)object)) {
                    l2 = ((LibC.Statvfs)object).f_files.longValue();
                    l3 = ((LibC.Statvfs)object).f_ffree.longValue();
                    l4 = ((LibC.Statvfs)object).f_blocks.longValue() * ((LibC.Statvfs)object).f_frsize.longValue();
                    l5 = ((LibC.Statvfs)object).f_bavail.longValue() * ((LibC.Statvfs)object).f_frsize.longValue();
                    l6 = ((LibC.Statvfs)object).f_bfree.longValue() * ((LibC.Statvfs)object).f_frsize.longValue();
                } else {
                    lil0liLand.warn("Failed to get information to use statvfs. path: {}, Error code: {}", (Object)string6, (Object)Native.getLastError());
                }
            }
            catch (NoClassDefFoundError | UnsatisfiedLinkError linkageError) {
                lil0liLand.error("Failed to get file counts from statvfs. {}", (Object)linkageError.getMessage());
            }
            if (l4 == 0L) {
                object = new File(string6);
                l4 = ((File)object).getTotalSpace();
                l5 = ((File)object).getUsableSpace();
                l6 = ((File)object).getFreeSpace();
            }
            arrayList.add(new lOiO0l0lAnd(string5, string4, map2.getOrDefault(string6, string5), string6, string8, string3, string11, string10, string7, l6, l5, l4, l3, l2));
        }
        return arrayList;
    }

    private static Map lli0OiIlAND() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (String string : Iill1lanD.I1O1I1LaNd("lsblk -o mountpoint,label")) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string, 2);
            if (arrstring.length != 2) continue;
            hashMap.put(arrstring[0], arrstring[1]);
        }
        return hashMap;
    }

    @Override
    public long OOOIilanD() {
        return Oiil0LaNd.I1O1I1LaNd(0);
    }

    @Override
    public long lI00OlAND() {
        return Oiil0LaNd.I1O1I1LaNd(2);
    }

    private static long I1O1I1LaNd(int n2) {
        String string = iI11I1lllaNd.iIiO00OLaNd;
        if (n2 < 0 || n2 > 2) {
            throw new IllegalArgumentException("Index must be between 0 and 2.");
        }
        List list = liOIOOlLAnD.I1O1I1LaNd(string);
        if (!list.isEmpty()) {
            String[] arrstring = ((String)list.get(0)).split("\\D+");
            return lOilLanD.OOOIilanD(arrstring[n2], 0L);
        }
        return 0L;
    }
}

