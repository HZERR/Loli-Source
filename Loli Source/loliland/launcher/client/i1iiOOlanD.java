/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import loliland.launcher.XLauncher;
import loliland.launcher.client.ii0ii01LanD;

public class i1iiOOlanD {
    private static long I1O1I1LaNd;
    private long OOOIilanD;
    private boolean lI00OlAND;

    public void I1O1I1LaNd() {
        Object object;
        I1O1I1LaNd = ((OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize() / 1024L / 1024L;
        String string = XLauncher.getStorageLauncher() + "settings.json";
        String string2 = "";
        try {
            object = new File(string);
            if (!((File)object).exists()) {
                ((File)object).createNewFile();
            } else {
                string2 = new String(Files.readAllBytes(Paths.get(string, new String[0])));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        if (string2.isEmpty()) {
            string2 = "{}";
        }
        this.OOOIilanD = ((ii0ii01LanD)(object = ii0ii01LanD.I1O1I1LaNd(string2))).lI00OlAND("memorySize") ? ((ii0ii01LanD)object).OOOIilanD("memorySize").lI00OlAND() : i1iiOOlanD.lI00OlAND();
        this.lI00OlAND = ((ii0ii01LanD)object).lI00OlAND("automaticMemory") ? ((ii0ii01LanD)object).OOOIilanD("automaticMemory").li0iOILAND() : true;
        if (this.lI00OlAND || this.OOOIilanD < 300L || this.OOOIilanD > I1O1I1LaNd + 1024L) {
            this.OOOIilanD = i1iiOOlanD.lI00OlAND();
        }
    }

    public void OOOIilanD() {
        ii0ii01LanD ii0ii01LanD2 = ii0ii01LanD.I1O1I1LaNd().I1O1I1LaNd("memorySize", this.OOOIilanD).I1O1I1LaNd("automaticMemory", this.lI00OlAND);
        String string = ii0ii01LanD2.toString();
        String string2 = XLauncher.getStorageLauncher() + "settings.json";
        try {
            File file = new File(string2);
            if (!file.exists()) {
                file.createNewFile();
            }
            Files.write(Paths.get(string2, new String[0]), string.getBytes(), new OpenOption[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static long lI00OlAND() {
        long l2 = I1O1I1LaNd;
        if (l2 > 9000L) {
            l2 = 8192L;
        } else if (l2 > 7000L) {
            l2 = 6144L;
        } else if (l2 > 6000L) {
            l2 = 5120L;
        } else if (l2 > 5000L) {
            l2 = 4096L;
        } else if (l2 > 4000L) {
            l2 = 4096L;
        } else if (l2 > 3000L) {
            l2 = 3072L;
        } else if (l2 > 2000L) {
            l2 = 2048L;
        } else if (l2 > 1000L) {
            l2 = 1024L;
        }
        return l2;
    }

    public static long lli0OiIlAND() {
        return I1O1I1LaNd;
    }

    public long li0iOILAND() {
        return this.OOOIilanD;
    }

    public void I1O1I1LaNd(long l2) {
        this.OOOIilanD = l2;
    }

    public boolean O1il1llOLANd() {
        return this.lI00OlAND;
    }

    public void I1O1I1LaNd(boolean bl) {
        this.lI00OlAND = bl;
    }
}

