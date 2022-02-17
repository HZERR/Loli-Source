/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import loliland.launcher.XLauncher;
import loliland.launcher.client.I1IIll1LaND;
import loliland.launcher.client.OOOOllANd;
import loliland.launcher.client.i1i1OlLaNd;
import loliland.launcher.client.l000O1ILaND;
import loliland.launcher.client.l1lIOlAND;

public class lIIiO0laNd {
    public static void I1O1I1LaNd(OOOOllANd oOOOllANd, HashMap hashMap) {
        System.out.println(oOOOllANd.Oill1LAnD() + " start clients");
        l000O1ILaND l000O1ILaND2 = lIIiO0laNd.I1O1I1LaNd(oOOOllANd, null);
        if (l000O1ILaND2 == null) {
            System.out.println(oOOOllANd.Oill1LAnD() + " NOT STARTING!!!");
        }
        XLauncher.closeLauncher();
    }

    private static l000O1ILaND I1O1I1LaNd(OOOOllANd oOOOllANd, i1i1OlLaNd i1i1OlLaNd2) {
        File file = new File(XLauncher.getStorageClients(), "clients");
        File file2 = new File(file, oOOOllANd.O1il1llOLANd());
        File file3 = new File(file2, "natives");
        String string2 = System.getenv("APPDATA");
        File file4 = new File(new File(string2, ".loliland"), "java");
        if (!file4.exists()) {
            file4 = new File(XLauncher.getStorageLauncher(), "jre");
        }
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add(file4.getAbsolutePath() + File.separator + "bin" + File.separator + "javaw.exe");
        linkedList.add("-XX:HeapDumpPath=ThisTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
        linkedList.add("-Dminecraft.applet.TargetDirectory=" + file2.getAbsolutePath());
        linkedList.add("-Djava.net.preferIPv4Stack=true");
        linkedList.add("-Dfml.debugExit=true");
        linkedList.add("-Djava.library.path=" + file3.getAbsolutePath());
        linkedList.add("-Dorg.lwjgl.librarypath=" + file3.getAbsolutePath());
        linkedList.add("-Dnet.java.games.input.librarypath=" + file3.getAbsolutePath());
        linkedList.add("-Dloliland.client.name=" + oOOOllANd.Oill1LAnD());
        linkedList.add("-Dloliland.servers=" + (oOOOllANd.OOOIilanD().isEmpty() ? "135.125.4.19:20001" : oOOOllANd.OOOIilanD()));
        linkedList.add("-Dloliland.token=" + XLauncher.getAuthManager().li0iOILAND());
        if (I1IIll1LaND.Oill1LAnD()) {
            linkedList.add("-Dos.name=Windows 10");
            linkedList.add("-Dos.version=10.0");
        }
        if (I1IIll1LaND.li0iOILAND()) {
            linkedList.add("-Xdock:name=LoliLand: Minecraft " + oOOOllANd.lIOILand());
        }
        List list = oOOOllANd.l0illAND();
        list.removeIf(string -> string.contains("-XX:+DisableAttachMechanism"));
        linkedList.addAll(list);
        long l2 = XLauncher.getSettingsManager().li0iOILAND();
        linkedList.add("-Xmx" + l2 + "M");
        linkedList.add("-classpath");
        linkedList.add(lIIiO0laNd.OOOIilanD(oOOOllANd, file2));
        linkedList.add(oOOOllANd.l11lLANd());
        linkedList.addAll(lIIiO0laNd.I1O1I1LaNd(oOOOllANd, file2));
        System.out.println(linkedList);
        ProcessBuilder processBuilder = new ProcessBuilder(linkedList);
        processBuilder.directory(file2);
        processBuilder.inheritIO();
        try {
            return new l000O1ILaND(processBuilder.start(), i1i1OlLaNd2);
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
            return null;
        }
    }

    private static LinkedList I1O1I1LaNd(OOOOllANd oOOOllANd, File file) {
        String[] arrstring;
        LinkedList<String> linkedList = new LinkedList<String>();
        for (String string : arrstring = oOOOllANd.lO110l1LANd().split(" ")) {
            string = string.replace("${auth_player_name}", XLauncher.getAuthManager().lI00OlAND());
            string = string.replace("${auth_session}", "-");
            string = string.replace("${gameDir}", file.getAbsolutePath());
            string = string.replace("${assetsDir}", XLauncher.getStorageClients() + "assets" + File.separator + "asset" + oOOOllANd.lIOILand());
            string = string.replace("${version}", oOOOllANd.lIOILand());
            linkedList.add(string);
        }
        linkedList.add("--width");
        linkedList.add(String.valueOf(1110));
        linkedList.add("--height");
        linkedList.add(String.valueOf(636));
        return linkedList;
    }

    private static String OOOIilanD(OOOOllANd oOOOllANd, File file) {
        String string = File.pathSeparator;
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : oOOOllANd.IO11O0LANd()) {
            File file2 = new File(file, string2);
            if (!file2.exists()) continue;
            if (file2.isFile()) {
                stringBuilder.append(string).append(file2.getAbsolutePath());
                continue;
            }
            ArrayList arrayList = new ArrayList();
            l1lIOlAND.I1O1I1LaNd(file2, arrayList);
            for (File file3 : arrayList) {
                if (!file3.getName().endsWith(".jar")) continue;
                stringBuilder.append(string).append(file3.getAbsolutePath());
            }
        }
        stringBuilder = new StringBuilder(stringBuilder.toString().replaceFirst(string, ""));
        return stringBuilder.toString();
    }
}

