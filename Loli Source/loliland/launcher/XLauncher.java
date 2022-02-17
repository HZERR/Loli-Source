/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher;

import com.google.common.util.concurrent.Futures;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ThreadInfo;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import loliland.launcher.I1O1I1LaNd;
import loliland.launcher.client.IO0I1lilaNd;
import loliland.launcher.client.O0IlOiILAnD;
import loliland.launcher.client.O1liIliLand;
import loliland.launcher.client.OIOiO10LaND;
import loliland.launcher.client.i1iiOOlanD;
import loliland.launcher.client.iO11lland;
import loliland.launcher.client.ii0ii01LanD;
import loliland.launcher.client.l1lIOlAND;
import loliland.launcher.client.lI0il11LaND;
import loliland.launcher.client.lI10ilAnd;
import loliland.launcher.client.lIl1Oi0llaND;
import loliland.launcher.client.liiOlanD;
import loliland.launcher.client.llI0OlAND;
import loliland.launcher.client.llIIi1lanD;
import org.lwjgl.opengl.Display;

public class XLauncher {
    private static boolean launcherRun;
    private static liiOlanD authManager;
    private static lIl1Oi0llaND clientManager;
    private static i1iiOOlanD settingsManager;
    private static String storageLauncher;
    private static String storageClients;
    private static ii0ii01LanD storageHash;
    private static String bit;
    private static OIOiO10LaND systemData;

    public static void startSampler(String string, int n2) {
        O1liIliLand o1liIliLand = new O1liIliLand();
        o1liIliLand.I1O1I1LaNd(threadInfo -> true);
        o1liIliLand.I1O1I1LaNd(n2, TimeUnit.MINUTES);
        IO0I1lilaNd iO0I1lilaNd = o1liIliLand.lI00OlAND();
        System.out.println("Start CPU profiling!");
        Futures.addCallback(iO0I1lilaNd.I1O1I1LaNd(), new I1O1I1LaNd(string));
    }

    public static void main(String ... arrstring) {
        try {
            bit = XLauncher.setupBit();
            settingsManager = new i1iiOOlanD();
            settingsManager.I1O1I1LaNd();
            launcherRun = true;
            storageClients = storageLauncher + "updates" + File.separator;
            String string = "{}";
            try {
                File file = new File(storageClients + "hash.json");
                file.getParentFile().mkdirs();
                if (!file.exists()) {
                    file.createNewFile();
                } else {
                    string = new String(Files.readAllBytes(Paths.get(storageClients + "hash.json", new String[0])));
                }
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            if (string.isEmpty()) {
                string = "{}";
            }
            storageHash = ii0ii01LanD.I1O1I1LaNd(string);
            authManager = new liiOlanD();
            clientManager = new lIl1Oi0llaND();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                launcherRun = false;
            }));
            long l2 = System.currentTimeMillis();
            systemData = OIOiO10LaND.I1O1I1LaNd();
            long l3 = System.currentTimeMillis();
            System.out.println(systemData.OOOIilanD() + " loaded in " + (l3 - l2) + " ms.");
            iO11lland.values();
            iO11lland.lli0OiIlAND.OOOIilanD();
            lI0il11LaND.OOOIilanD("bg.png");
            lI0il11LaND.OOOIilanD("anime.png");
            System.out.println("Launcher is started");
            llI0OlAND.I1O1I1LaNd();
            authManager.I1O1I1LaNd();
            clientManager.I1O1I1LaNd();
            System.gc();
            lI10ilAnd.I1O1I1LaNd().I1O1I1LaNd();
            while (XLauncher.isLauncherRun() && Display.isCreated() && !Display.isCloseRequested()) {
                lI10ilAnd.I1O1I1LaNd().I1O1I1LaNd();
                try {
                    O0IlOiILAnD.I1O1I1LaNd();
                    llI0OlAND.OOOIilanD();
                    llIIi1lanD.lI00OlAND();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        System.exit(0);
    }

    public static String setupBit() {
        boolean bl = System.getProperty("os.name").contains("Windows") ? System.getenv("ProgramFiles(x86)") != null : System.getProperty("os.arch").contains("64");
        return bl ? "64" : "32";
    }

    private static void loadLib(String string) {
        try {
            InputStream inputStream = XLauncher.class.getResourceAsStream("/natives/" + string);
            File file = new File(storageLauncher + "natives/" + string);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            l1lIOlAND.I1O1I1LaNd(inputStream, fileOutputStream);
            inputStream.close();
            ((OutputStream)fileOutputStream).close();
            System.out.println("Load native library: " + string);
        }
        catch (Exception exception) {
            throw new RuntimeException("Failed to load required DLL", exception);
        }
    }

    public static void closeLauncher() {
        launcherRun = false;
        System.exit(0);
    }

    public static void saveStorageHash() {
        String string = XLauncher.getStorageHash().toString();
        String string2 = storageClients + "hash.json";
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

    public static boolean isLauncherRun() {
        return launcherRun;
    }

    public static void setLauncherRun(boolean bl) {
        launcherRun = bl;
    }

    public static liiOlanD getAuthManager() {
        return authManager;
    }

    public static lIl1Oi0llaND getClientManager() {
        return clientManager;
    }

    public static i1iiOOlanD getSettingsManager() {
        return settingsManager;
    }

    public static String getStorageLauncher() {
        return storageLauncher;
    }

    public static String getStorageClients() {
        return storageClients;
    }

    public static ii0ii01LanD getStorageHash() {
        return storageHash;
    }

    public static String getBit() {
        return bit;
    }

    public static OIOiO10LaND getSystemData() {
        return systemData;
    }

    private static /* synthetic */ boolean lambda$main$1(ThreadInfo threadInfo) {
        return true;
    }

    static {
        storageLauncher = System.getProperty("user.home") + "/loliland/";
        File file = new File(storageLauncher);
        file.mkdirs();
        try {
            CodeSource codeSource = XLauncher.class.getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                ZipEntry zipEntry;
                File file2 = new File(storageLauncher + "natives/");
                file2.mkdirs();
                System.setProperty("org.lwjgl.librarypath", file2.getAbsolutePath());
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                URL uRL = codeSource.getLocation();
                ZipInputStream zipInputStream = new ZipInputStream(uRL.openStream());
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    String string = zipEntry.getName();
                    if (!string.startsWith("natives/") || (string = string.replaceFirst("natives/", "")).isEmpty()) continue;
                    try {
                        XLauncher.loadLib(string);
                    }
                    catch (Exception exception) {
                        System.out.println("Failed to load native library: " + string);
                        exception.printStackTrace();
                    }
                }
            }
        }
        catch (Exception exception) {
            System.err.println("Native code library failed to load.\n" + exception);
            System.exit(1);
        }
    }
}

