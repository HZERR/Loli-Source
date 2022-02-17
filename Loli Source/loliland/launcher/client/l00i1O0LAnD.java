/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.l10lO11lanD;

public final class l00i1O0LAnD {
    private static final String I1O1I1LaNd = "glob:";
    private static final String OOOIilanD = "regex:";

    private l00i1O0LAnD() {
    }

    public static boolean I1O1I1LaNd(String string, String string2, List list, List list2, List list3, List list4) {
        Path path = Paths.get(string, new String[0]);
        Path path2 = Paths.get(string2, new String[0]);
        if (l00i1O0LAnD.I1O1I1LaNd(path, list) || l00i1O0LAnD.I1O1I1LaNd(path2, list3)) {
            return false;
        }
        return l00i1O0LAnD.I1O1I1LaNd(path, list2) || l00i1O0LAnD.I1O1I1LaNd(path2, list4);
    }

    public static List I1O1I1LaNd(String string) {
        String string2 = l10lO11lanD.I1O1I1LaNd(string, "");
        return l00i1O0LAnD.OOOIilanD(string2);
    }

    public static List OOOIilanD(String string) {
        FileSystem fileSystem = FileSystems.getDefault();
        ArrayList<PathMatcher> arrayList = new ArrayList<PathMatcher>();
        for (String string2 : string.split(",")) {
            if (string2.length() <= 0) continue;
            if (!string2.startsWith(I1O1I1LaNd) && !string2.startsWith(OOOIilanD)) {
                string2 = I1O1I1LaNd + string2;
            }
            arrayList.add(fileSystem.getPathMatcher(string2));
        }
        return arrayList;
    }

    public static boolean I1O1I1LaNd(Path path, List list) {
        for (PathMatcher pathMatcher : list) {
            if (!pathMatcher.matches(path)) continue;
            return true;
        }
        return false;
    }
}

