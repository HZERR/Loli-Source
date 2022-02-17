/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.io.File;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.IOI1LaNd;
import loliland.launcher.client.OOOOO10iLAND;
import loliland.launcher.client.iI11I1lllaNd;
import loliland.launcher.client.ii1li00Land;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liOIOOlLAnD;

public final class iIiO00OLaNd {
    private static final Pattern OOOIilanD = Pattern.compile("\\d+");
    private static final Pattern lI00OlAND = Pattern.compile("socket:\\[(\\d+)\\]");
    public static final int I1O1I1LaNd;

    private iIiO00OLaNd() {
    }

    public static IIOOOlIiLanD I1O1I1LaNd(int n2) {
        String string = liOIOOlLAnD.li0iOILAND(String.format(iI11I1lllaNd.IO11O0LANd, n2));
        if (string.isEmpty()) {
            return null;
        }
        int n3 = string.indexOf(40) + 1;
        int n4 = string.indexOf(41);
        String string2 = string.substring(n3, n4);
        Character c2 = Character.valueOf(string.charAt(n4 + 2));
        String[] arrstring = lOilLanD.OOOIilanD.split(string.substring(n4 + 4).trim());
        EnumMap<ii1li00Land, Long> enumMap = new EnumMap<ii1li00Land, Long>(ii1li00Land.class);
        ii1li00Land[] arrii1li00Land = (ii1li00Land[])ii1li00Land.class.getEnumConstants();
        for (int i2 = 3; i2 < arrii1li00Land.length && i2 - 3 < arrstring.length; ++i2) {
            enumMap.put(arrii1li00Land[i2], lOilLanD.OOOIilanD(arrstring[i2 - 3], 0L));
        }
        return new IIOOOlIiLanD(string2, c2, enumMap);
    }

    public static Map OOOIilanD(int n2) {
        String string = liOIOOlLAnD.li0iOILAND(String.format(iI11I1lllaNd.l11lLANd, n2));
        if (string.isEmpty()) {
            return null;
        }
        String[] arrstring = lOilLanD.OOOIilanD.split(string);
        EnumMap<IOI1LaNd, Long> enumMap = new EnumMap<IOI1LaNd, Long>(IOI1LaNd.class);
        IOI1LaNd[] arriOI1LaNd = (IOI1LaNd[])IOI1LaNd.class.getEnumConstants();
        for (int i2 = 0; i2 < arriOI1LaNd.length && i2 < arrstring.length; ++i2) {
            enumMap.put(arriOI1LaNd[i2], lOilLanD.OOOIilanD(arrstring[i2], 0L));
        }
        return enumMap;
    }

    public static File[] lI00OlAND(int n2) {
        return iIiO00OLaNd.I1O1I1LaNd(String.format(iI11I1lllaNd.lli011lLANd, n2));
    }

    public static File[] I1O1I1LaNd() {
        return iIiO00OLaNd.I1O1I1LaNd(iI11I1lllaNd.I1O1I1LaNd);
    }

    public static Map OOOIilanD() {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        for (File file : iIiO00OLaNd.I1O1I1LaNd()) {
            File[] arrfile;
            int n2 = lOilLanD.lli0OiIlAND(file.getName(), -1);
            for (File file2 : arrfile = iIiO00OLaNd.lI00OlAND(n2)) {
                Matcher matcher;
                String string = liOIOOlLAnD.I1O1I1LaNd(file2);
                if (string == null || !(matcher = lI00OlAND.matcher(string)).matches()) continue;
                hashMap.put(lOilLanD.lli0OiIlAND(matcher.group(1), -1), n2);
            }
        }
        return hashMap;
    }

    public static List lli0OiIlAND(int n2) {
        File[] arrfile = iIiO00OLaNd.I1O1I1LaNd(String.format(iI11I1lllaNd.ii1li00Land, n2));
        return Arrays.stream(arrfile).map(file -> lOilLanD.lli0OiIlAND(file.getName(), 0)).filter(n3 -> n3 != n2).collect(Collectors.toList());
    }

    private static File[] I1O1I1LaNd(String string) {
        File file2 = new File(string);
        File[] arrfile = file2.listFiles(file -> OOOIilanD.matcher(file.getName()).matches());
        return arrfile == null ? new File[0] : arrfile;
    }

    public static OOOOO10iLAND I1O1I1LaNd(char c2) {
        OOOOO10iLAND oOOOO10iLAND;
        switch (c2) {
            case 'R': {
                oOOOO10iLAND = OOOOO10iLAND.OOOIilanD;
                break;
            }
            case 'S': {
                oOOOO10iLAND = OOOOO10iLAND.lI00OlAND;
                break;
            }
            case 'D': {
                oOOOO10iLAND = OOOOO10iLAND.lli0OiIlAND;
                break;
            }
            case 'Z': {
                oOOOO10iLAND = OOOOO10iLAND.li0iOILAND;
                break;
            }
            case 'T': {
                oOOOO10iLAND = OOOOO10iLAND.O1il1llOLANd;
                break;
            }
            default: {
                oOOOO10iLAND = OOOOO10iLAND.Oill1LAnD;
            }
        }
        return oOOOO10iLAND;
    }

    static {
        String string = liOIOOlLAnD.li0iOILAND(iI11I1lllaNd.l0iIlIO1laNd);
        I1O1I1LaNd = !string.isEmpty() && string.contains(")") ? lOilLanD.OOOIilanD(string, ' ') + 3 : 52;
    }
}

