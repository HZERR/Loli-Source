/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import loliland.launcher.client.OOOIilanD;

public class I1IIll1LaND {
    private static final int I1O1I1LaNd = I1IIll1LaND.l0illAND();
    private static final int OOOIilanD = I1IIll1LaND.O1il1llOLANd() ? (System.getenv("ProgramFiles(x86)") == null ? 32 : 64) : (System.getProperty("os.arch").contains("64") ? 64 : 32);

    private static int l0illAND() {
        String string = System.getProperty("os.name").toLowerCase();
        if (string.contains("mac")) {
            return 2;
        }
        if (string.contains("win")) {
            return 1;
        }
        if (string.contains("linux") || string.contains("unix")) {
            return 0;
        }
        return -1;
    }

    public static String I1O1I1LaNd() {
        switch (I1O1I1LaNd) {
            case 0: {
                return I1IIll1LaND.lli0OiIlAND() ? "linux-x64" : "linux-i586";
            }
            case 1: {
                return I1IIll1LaND.lli0OiIlAND() ? "windows-x64" : "windows-i586";
            }
            case 2: {
                return I1IIll1LaND.lil0liLand().contains("M1") ? "macos-aarch64" : "macos-x64";
            }
        }
        return I1IIll1LaND.lli0OiIlAND() ? "unknown-x64" : "unknown-i586";
    }

    public static int OOOIilanD() {
        return OOOIilanD;
    }

    public static boolean lI00OlAND() {
        return OOOIilanD != 64;
    }

    public static boolean lli0OiIlAND() {
        return OOOIilanD == 64;
    }

    public static boolean li0iOILAND() {
        return I1O1I1LaNd == 2;
    }

    public static boolean O1il1llOLANd() {
        return I1O1I1LaNd == 1;
    }

    public static boolean Oill1LAnD() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        return operatingSystemMXBean.getName().equalsIgnoreCase("windows 10") && operatingSystemMXBean.getVersion().startsWith("10.");
    }

    public static boolean lIOILand() {
        return I1O1I1LaNd == 0;
    }

    public static void I1O1I1LaNd(String string) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(string), null);
    }

    public static void OOOIilanD(String string) {
        if (I1IIll1LaND.lIOILand()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().browse(new URL(string).toURI());
                }
                catch (IOException | URISyntaxException exception) {
                    exception.printStackTrace();
                }
            }).start();
        } else {
            try {
                Desktop.getDesktop().browse(new URL(string).toURI());
            }
            catch (IOException | URISyntaxException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void I1O1I1LaNd(Path path) {
        I1IIll1LaND.I1O1I1LaNd(path.toFile());
    }

    public static void I1O1I1LaNd(File file) {
        if (I1IIll1LaND.lIOILand()) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(file);
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }).start();
            return;
        }
        try {
            Desktop.getDesktop().open(file);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public static String lil0liLand() {
        return new OOOIilanD().lli0OiIlAND().OOOIilanD().I1O1I1LaNd().OOOIilanD();
    }

    public static void iilIi1laND() {
        if (I1IIll1LaND.li0iOILAND()) {
            try {
                Class<?> class_ = Class.forName("com.apple.eawt.Application");
                Method method = class_.getMethod("getApplication", new Class[0]);
                Object object = method.invoke(class_, new Object[0]);
                Method method2 = class_.getMethod("setDockIconImage", Image.class);
            }
            catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static int lli011lLANd() {
        OOOIilanD oOOIilanD = new OOOIilanD();
        return Math.toIntExact(oOOIilanD.lli0OiIlAND().lI00OlAND().OOOIilanD() / 1024L) / 1024;
    }
}

