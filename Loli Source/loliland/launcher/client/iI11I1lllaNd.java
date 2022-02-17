/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import loliland.launcher.client.l10lO11lanD;
import loliland.launcher.client.ll10lIO1lANd;

public final class iI11I1lllaNd {
    public static final String I1O1I1LaNd = iI11I1lllaNd.I1O1I1LaNd();
    public static final String OOOIilanD = I1O1I1LaNd + "/asound/";
    public static final String lI00OlAND = I1O1I1LaNd + "/cpuinfo";
    public static final String lli0OiIlAND = I1O1I1LaNd + "/diskstats";
    public static final String li0iOILAND = I1O1I1LaNd + "/meminfo";
    public static final String O1il1llOLANd = I1O1I1LaNd + "/mounts";
    public static final String Oill1LAnD = I1O1I1LaNd + "/net";
    public static final String lIOILand = I1O1I1LaNd + "/%d/cmdline";
    public static final String lil0liLand = I1O1I1LaNd + "/%d/cwd";
    public static final String iilIi1laND = I1O1I1LaNd + "/%d/exe";
    public static final String lli011lLANd = I1O1I1LaNd + "/%d/fd";
    public static final String l0illAND = I1O1I1LaNd + "/%d/io";
    public static final String IO11O0LANd = I1O1I1LaNd + "/%d/stat";
    public static final String l11lLANd = I1O1I1LaNd + "/%d/statm";
    public static final String lO110l1LANd = I1O1I1LaNd + "/%d/status";
    public static final String l0iIlIO1laNd = I1O1I1LaNd + "/self/stat";
    public static final String iOIl0LAnD = I1O1I1LaNd + "/stat";
    public static final String iIiO00OLaNd = I1O1I1LaNd + "/sys/fs/file-nr";
    public static final String ii1li00Land = I1O1I1LaNd + "/%d/task";
    public static final String IOI1LaNd = ii1li00Land + "/%d/comm";
    public static final String lI00ilAND = ii1li00Land + "/%d/status";
    public static final String l0l00lAND = ii1li00Land + "/%d/stat";
    public static final String iOl10IlLAnd = I1O1I1LaNd + "/uptime";
    public static final String lIiIii1LAnD = I1O1I1LaNd + "/version";
    public static final String II1Iland = I1O1I1LaNd + "/vmstat";

    private iI11I1lllaNd() {
    }

    private static String I1O1I1LaNd() {
        String string = l10lO11lanD.I1O1I1LaNd("oshi.util.proc.path", "/proc");
        string = '/' + string.replaceAll("/$|^/", "");
        if (!new File(string).exists()) {
            throw new ll10lIO1lANd("oshi.util.proc.path", "The path does not exist");
        }
        return string;
    }
}

