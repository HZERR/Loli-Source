/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.IO00LaND;
import loliland.launcher.client.O1IiIiI1LAND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class lOilLanD {
    private static final Logger O1il1llOLANd = LoggerFactory.getLogger(lOilLanD.class);
    private static final String Oill1LAnD = "{} didn't parse. Returning default. {}";
    private static final Pattern lIOILand = Pattern.compile("(\\d+(.\\d+)?) ?([kMGT]?Hz).*");
    private static final Pattern lil0liLand = Pattern.compile("(\\d+) ?([kMGT]?B).*");
    private static final Pattern iilIi1laND = Pattern.compile("(\\d+(.\\d+)?)[\\s]?([kKMGT])?");
    private static final Pattern lli011lLANd = Pattern.compile("[0-9a-fA-F]+");
    private static final Pattern l0illAND = Pattern.compile("(?:(\\d+)-)?(?:(\\d+):)??(?:(\\d+):)?(\\d+)(?:\\.(\\d+))?");
    private static final Pattern IO11O0LANd = Pattern.compile(".*([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}).*");
    private static final Pattern l11lLANd = Pattern.compile(".*(?:VID|VEN)_(\\p{XDigit}{4})&(?:PID|DEV)_(\\p{XDigit}{4})(.*)\\\\(.*)");
    private static final Pattern lO110l1LANd = Pattern.compile("(.+)\\s\\[(.*?)\\]");
    private static final Pattern l0iIlIO1laNd = Pattern.compile(".+\\s\\[size=(\\d+)([kKMGT])\\]");
    private static final String iOIl0LAnD = "Hz";
    private static final String iIiO00OLaNd = "kHz";
    private static final String ii1li00Land = "MHz";
    private static final String IOI1LaNd = "GHz";
    private static final String lI00ilAND = "THz";
    private static final String l0l00lAND = "PHz";
    private static final Map iOl10IlLAnd;
    private static final long lIiIii1LAnD = 11644473600000L;
    private static final int II1Iland;
    public static final Pattern I1O1I1LaNd;
    public static final Pattern OOOIilanD;
    public static final Pattern lI00OlAND;
    public static final Pattern lli0OiIlAND;
    public static final Pattern li0iOILAND;
    private static final long[] l0IO0LAnd;
    private static final DateTimeFormatter liOIOOLANd;

    private lOilLanD() {
    }

    public static long I1O1I1LaNd(String string) {
        double d2;
        Matcher matcher = lIOILand.matcher(string.trim());
        if (matcher.find() && matcher.groupCount() == 3 && (d2 = Double.valueOf(matcher.group(1)) * (double)iOl10IlLAnd.getOrDefault(matcher.group(3), -1L).longValue()) >= 0.0) {
            return (long)d2;
        }
        return -1L;
    }

    public static int I1O1I1LaNd(String string, int n2) {
        try {
            String string2 = lOilLanD.OOOIilanD(string);
            if (string2.toLowerCase().startsWith("0x")) {
                return Integer.decode(string2);
            }
            return Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            return n2;
        }
    }

    public static long I1O1I1LaNd(String string, long l2) {
        try {
            String string2 = lOilLanD.OOOIilanD(string);
            if (string2.toLowerCase().startsWith("0x")) {
                return Long.decode(string2);
            }
            return Long.parseLong(string2);
        }
        catch (NumberFormatException numberFormatException) {
            O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            return l2;
        }
    }

    public static double I1O1I1LaNd(String string, double d2) {
        try {
            return Double.parseDouble(lOilLanD.OOOIilanD(string));
        }
        catch (NumberFormatException numberFormatException) {
            O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            return d2;
        }
    }

    public static String OOOIilanD(String string) {
        String[] arrstring = OOOIilanD.split(string);
        return arrstring[arrstring.length - 1];
    }

    public static String I1O1I1LaNd(byte[] arrby) {
        StringBuilder stringBuilder = new StringBuilder(arrby.length * 2);
        for (byte by : arrby) {
            stringBuilder.append(Character.forDigit((by & 0xF0) >>> 4, 16));
            stringBuilder.append(Character.forDigit(by & 0xF, 16));
        }
        return stringBuilder.toString().toUpperCase();
    }

    public static byte[] lI00OlAND(String string) {
        int n2 = string.length();
        if (!lli011lLANd.matcher(string).matches() || (n2 & 1) != 0) {
            O1il1llOLANd.warn("Invalid hexadecimal string: {}", (Object)string);
            return new byte[0];
        }
        byte[] arrby = new byte[n2 / 2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            arrby[i2 / 2] = (byte)(Character.digit(string.charAt(i2), 16) << 4 | Character.digit(string.charAt(i2 + 1), 16));
        }
        return arrby;
    }

    public static byte[] OOOIilanD(String string, int n2) {
        return Arrays.copyOf(string.getBytes(StandardCharsets.US_ASCII), n2);
    }

    public static byte[] I1O1I1LaNd(long l2, int n2, int n3) {
        long l3 = l2;
        byte[] arrby = new byte[8];
        for (int i2 = 7; i2 >= 0 && l3 != 0L; l3 >>>= 8, --i2) {
            arrby[i2] = (byte)l3;
        }
        return Arrays.copyOfRange(arrby, 8 - n2, 8 + n3 - n2);
    }

    public static long lI00OlAND(String string, int n2) {
        return lOilLanD.I1O1I1LaNd(string.getBytes(StandardCharsets.US_ASCII), n2);
    }

    public static long I1O1I1LaNd(byte[] arrby, int n2) {
        return lOilLanD.I1O1I1LaNd(arrby, n2, true);
    }

    public static long I1O1I1LaNd(byte[] arrby, int n2, boolean bl) {
        if (n2 > 8) {
            throw new IllegalArgumentException("Can't convert more than 8 bytes.");
        }
        if (n2 > arrby.length) {
            throw new IllegalArgumentException("Size can't be larger than array length.");
        }
        long l2 = 0L;
        for (int i2 = 0; i2 < n2; ++i2) {
            l2 = bl ? l2 << 8 | (long)(arrby[i2] & 0xFF) : l2 << 8 | (long)(arrby[n2 - i2 - 1] & 0xFF);
        }
        return l2;
    }

    public static float I1O1I1LaNd(byte[] arrby, int n2, int n3) {
        return (float)lOilLanD.I1O1I1LaNd(arrby, n2) / (float)(1 << n3);
    }

    public static long I1O1I1LaNd(int n2) {
        long l2 = n2;
        return l2 & 0xFFFFFFFFL;
    }

    public static long I1O1I1LaNd(long l2) {
        return l2 & Long.MAX_VALUE;
    }

    public static String lli0OiIlAND(String string) {
        if (string.length() % 2 > 0) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            for (int i2 = 0; i2 < string.length(); i2 += 2) {
                int n2 = Integer.parseInt(string.substring(i2, i2 + 2), 16);
                if (n2 < 32 || n2 > 127) {
                    return string;
                }
                stringBuilder.append((char)n2);
            }
        }
        catch (NumberFormatException numberFormatException) {
            O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            return string;
        }
        return stringBuilder.toString();
    }

    public static int lli0OiIlAND(String string, int n2) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException) {
            O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            return n2;
        }
    }

    public static long OOOIilanD(String string, long l2) {
        try {
            return Long.parseLong(string);
        }
        catch (NumberFormatException numberFormatException) {
            O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            return l2;
        }
    }

    public static long lI00OlAND(String string, long l2) {
        try {
            return new BigInteger(string).longValue();
        }
        catch (NumberFormatException numberFormatException) {
            O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            return l2;
        }
    }

    public static double OOOIilanD(String string, double d2) {
        try {
            return Double.parseDouble(string);
        }
        catch (NumberFormatException numberFormatException) {
            O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            return d2;
        }
    }

    public static long lli0OiIlAND(String string, long l2) {
        Matcher matcher = l0illAND.matcher(string);
        if (matcher.matches()) {
            long l3 = 0L;
            if (matcher.group(1) != null) {
                l3 += lOilLanD.OOOIilanD(matcher.group(1), 0L) * 86400000L;
            }
            if (matcher.group(2) != null) {
                l3 += lOilLanD.OOOIilanD(matcher.group(2), 0L) * 3600000L;
            }
            if (matcher.group(3) != null) {
                l3 += lOilLanD.OOOIilanD(matcher.group(3), 0L) * 60000L;
            }
            l3 += lOilLanD.OOOIilanD(matcher.group(4), 0L) * 1000L;
            return l3 += (long)(1000.0 * lOilLanD.OOOIilanD("0." + matcher.group(5), 0.0));
        }
        return l2;
    }

    public static String I1O1I1LaNd(String string, String string2) {
        Matcher matcher = IO11O0LANd.matcher(string.toLowerCase());
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return string2;
    }

    public static String li0iOILAND(String string) {
        return lOilLanD.I1O1I1LaNd(string, '\'');
    }

    public static String O1il1llOLANd(String string) {
        return lOilLanD.I1O1I1LaNd(string, '\"');
    }

    public static String I1O1I1LaNd(String string, char c2) {
        int n2 = string.indexOf(c2);
        if (n2 < 0) {
            return "";
        }
        return string.substring(n2 + 1, string.lastIndexOf(c2)).trim();
    }

    public static int Oill1LAnD(String string) {
        return lOilLanD.li0iOILAND(string, 1);
    }

    public static int li0iOILAND(String string, int n2) {
        String[] arrstring = lI00OlAND.split(lli0OiIlAND.matcher(string).replaceFirst(""));
        if (arrstring.length >= n2) {
            return lOilLanD.lli0OiIlAND(arrstring[n2 - 1], 0);
        }
        return 0;
    }

    public static String OOOIilanD(String string, String string2) {
        if (string == null || string.isEmpty() || string2 == null || string2.isEmpty()) {
            return string;
        }
        int n2 = string.indexOf(string2, 0);
        if (n2 == -1) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder(string.length() - string2.length());
        int n3 = 0;
        do {
            stringBuilder.append(string.substring(n3, n2));
        } while ((n2 = string.indexOf(string2, n3 = n2 + string2.length())) != -1);
        stringBuilder.append(string.substring(n3));
        return stringBuilder.toString();
    }

    public static long[] I1O1I1LaNd(String string, int[] arrn, int n2, char c2) {
        long[] arrl = new long[arrn.length];
        int n3 = string.length();
        int n4 = arrn.length - 1;
        int n5 = n2 - 1;
        int n6 = 0;
        boolean bl = false;
        boolean bl2 = true;
        boolean bl3 = false;
        boolean bl4 = false;
        while (--n3 > 0 && n4 >= 0) {
            char c3 = string.charAt(n3);
            if (c3 == c2) {
                if (!bl3 && bl2) {
                    bl3 = true;
                }
                if (bl) continue;
                if (bl3 && arrn[n4] == n5--) {
                    --n4;
                }
                bl = true;
                n6 = 0;
                bl4 = false;
                bl2 = true;
                continue;
            }
            if (arrn[n4] != n5 || c3 == '+' || !bl2) {
                bl = false;
                continue;
            }
            if (c3 >= '0' && c3 <= '9' && !bl4) {
                if (n6 > 18 || n6 == 17 && c3 == '9' && arrl[n4] > 223372036854775807L) {
                    arrl[n4] = Long.MAX_VALUE;
                } else {
                    int n7 = n4;
                    arrl[n7] = arrl[n7] + (long)(c3 - 48) * l0IO0LAnd[n6++];
                }
                bl = false;
                continue;
            }
            if (c3 == '-') {
                int n8 = n4;
                arrl[n8] = arrl[n8] * -1L;
                bl = false;
                bl4 = true;
                continue;
            }
            if (bl3) {
                if (!lOilLanD.iIiO00OLaNd(string)) {
                    O1il1llOLANd.error("Illegal character parsing string '{}' to long array: {}", (Object)string, (Object)Character.valueOf(string.charAt(n3)));
                }
                return new long[arrn.length];
            }
            arrl[n4] = 0L;
            bl2 = false;
        }
        if (n4 > 0) {
            if (!lOilLanD.iIiO00OLaNd(string)) {
                O1il1llOLANd.error("Not enough fields in string '{}' parsing to long array: {}", (Object)string, (Object)(arrn.length - n4));
            }
            return new long[arrn.length];
        }
        return arrl;
    }

    private static boolean iIiO00OLaNd(String string) {
        return string.startsWith("NOLOG: ");
    }

    public static int OOOIilanD(String string, char c2) {
        int n2 = string.length();
        int n3 = 0;
        boolean bl = false;
        boolean bl2 = true;
        boolean bl3 = false;
        while (--n2 > 0) {
            char c3 = string.charAt(n2);
            if (c3 == c2) {
                if (bl) continue;
                if (bl2) {
                    ++n3;
                }
                bl = true;
                bl3 = false;
                bl2 = true;
                continue;
            }
            if (c3 == '+' || !bl2) {
                bl = false;
                continue;
            }
            if (c3 >= '0' && c3 <= '9' && !bl3) {
                bl = false;
                continue;
            }
            if (c3 == '-') {
                bl = false;
                bl3 = true;
                continue;
            }
            if (n3 > 0) {
                return n3;
            }
            bl2 = false;
        }
        return n3 + 1;
    }

    public static String I1O1I1LaNd(String string, String string2, String string3) {
        String string4 = "";
        if (string.indexOf(string2) >= 0 && string.indexOf(string3) >= 0) {
            string4 = string.substring(string.indexOf(string2) + string2.length(), string.length());
            string4 = string4.substring(0, string4.indexOf(string3));
        }
        return string4;
    }

    public static long I1O1I1LaNd(long l2, boolean bl) {
        return l2 / 10000L - 11644473600000L - (bl ? (long)II1Iland : 0L);
    }

    public static String lIOILand(String string) {
        try {
            return String.format("%s-%s-%s", string.substring(6, 10), string.substring(0, 2), string.substring(3, 5));
        }
        catch (StringIndexOutOfBoundsException stringIndexOutOfBoundsException) {
            return string;
        }
    }

    public static OffsetDateTime lil0liLand(String string) {
        try {
            int n2 = Integer.parseInt(string.substring(22));
            LocalTime localTime = LocalTime.MIDNIGHT.plusMinutes(n2);
            return OffsetDateTime.parse(string.substring(0, 22) + localTime.format(DateTimeFormatter.ISO_LOCAL_TIME), liOIOOLANd);
        }
        catch (IndexOutOfBoundsException | NumberFormatException | DateTimeParseException runtimeException) {
            O1il1llOLANd.trace("Unable to parse {} to CIM DateTime.", (Object)string);
            return IO00LaND.lI00OlAND;
        }
    }

    public static boolean I1O1I1LaNd(List list, String string) {
        for (String string2 : list) {
            if (!string.equals(string2) && !string.startsWith(string2 + "/")) continue;
            return true;
        }
        return false;
    }

    public static long iilIi1laND(String string) {
        Matcher matcher = iilIi1laND.matcher(string.trim());
        String[] arrstring = matcher.find() && matcher.groupCount() == 3 ? new String[]{matcher.group(1), matcher.group(3)} : new String[]{string};
        double d2 = lOilLanD.OOOIilanD(arrstring[0], 0.0);
        if (arrstring.length == 2 && arrstring[1] != null && arrstring[1].length() >= 1) {
            switch (arrstring[1].charAt(0)) {
                case 'T': {
                    d2 *= 1.0E12;
                    break;
                }
                case 'G': {
                    d2 *= 1.0E9;
                    break;
                }
                case 'M': {
                    d2 *= 1000000.0;
                    break;
                }
                case 'K': 
                case 'k': {
                    d2 *= 1000.0;
                    break;
                }
            }
        }
        return (long)d2;
    }

    public static long lli011lLANd(String string) {
        Matcher matcher;
        String[] arrstring = OOOIilanD.split(string);
        if (arrstring.length < 2 && (matcher = lil0liLand.matcher(string.trim())).find() && matcher.groupCount() == 2) {
            arrstring = new String[]{matcher.group(1), matcher.group(2)};
        }
        long l2 = lOilLanD.OOOIilanD(arrstring[0], 0L);
        if (arrstring.length == 2 && arrstring[1].length() > 1) {
            switch (arrstring[1].charAt(0)) {
                case 'T': {
                    l2 <<= 40;
                    break;
                }
                case 'G': {
                    l2 <<= 30;
                    break;
                }
                case 'M': {
                    l2 <<= 20;
                    break;
                }
                case 'K': 
                case 'k': {
                    l2 <<= 10;
                    break;
                }
            }
        }
        return l2;
    }

    public static IIOOOlIiLanD l0illAND(String string) {
        Matcher matcher = l11lLANd.matcher(string);
        if (matcher.matches()) {
            String string2 = "0x" + matcher.group(1).toLowerCase();
            String string3 = "0x" + matcher.group(2).toLowerCase();
            String string4 = matcher.group(4);
            return new IIOOOlIiLanD(string2, string3, !matcher.group(3).isEmpty() || string4.contains("&") ? "" : string4);
        }
        return null;
    }

    public static long IO11O0LANd(String string) {
        String[] arrstring;
        long l2 = 0L;
        for (String string2 : arrstring = OOOIilanD.split(string)) {
            String[] arrstring2;
            if (!string2.startsWith("memory:") || (arrstring2 = string2.substring(7).split("-")).length != 2) continue;
            try {
                l2 += Long.parseLong(arrstring2[1], 16) - Long.parseLong(arrstring2[0], 16) + 1L;
            }
            catch (NumberFormatException numberFormatException) {
                O1il1llOLANd.trace(Oill1LAnD, (Object)string2, (Object)numberFormatException);
            }
        }
        return l2;
    }

    public static O1IiIiI1LAND l11lLANd(String string) {
        Matcher matcher = lO110l1LANd.matcher(string);
        if (matcher.matches()) {
            return new O1IiIiI1LAND(matcher.group(1), matcher.group(2));
        }
        return null;
    }

    public static long lO110l1LANd(String string) {
        Matcher matcher = l0iIlIO1laNd.matcher(string);
        if (matcher.matches()) {
            return lOilLanD.lli011lLANd(matcher.group(1) + " " + matcher.group(2) + "B");
        }
        return 0L;
    }

    public static List l0iIlIO1laNd(String string) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (String string2 : OOOIilanD.split(string)) {
            int n2;
            if (string2.contains("-")) {
                n2 = lOilLanD.Oill1LAnD(string2);
                int n3 = lOilLanD.li0iOILAND(string2, 2);
                for (int i2 = n2; i2 <= n3; ++i2) {
                    arrayList.add(i2);
                }
                continue;
            }
            n2 = lOilLanD.lli0OiIlAND(string2, -1);
            if (n2 < 0) continue;
            arrayList.add(n2);
        }
        return arrayList;
    }

    public static byte[] OOOIilanD(int n2) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(n2).array();
    }

    public static byte[] I1O1I1LaNd(int[] arrn) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16).order(ByteOrder.LITTLE_ENDIAN);
        for (int n2 : arrn) {
            byteBuffer.putInt(n2);
        }
        return byteBuffer.array();
    }

    public static int lI00OlAND(int n2) {
        return n2 >> 8 & 0xFF | n2 << 8 & 0xFF00;
    }

    public static String OOOIilanD(int[] arrn) {
        if (arrn.length != 4) {
            throw new IllegalArgumentException("ut_addr_v6 must have exactly 4 elements");
        }
        if (arrn[1] == 0 && arrn[2] == 0 && arrn[3] == 0) {
            if (arrn[0] == 0) {
                return "::";
            }
            byte[] arrby = ByteBuffer.allocate(4).putInt(arrn[0]).array();
            try {
                return InetAddress.getByAddress(arrby).getHostAddress();
            }
            catch (UnknownHostException unknownHostException) {
                return "unknown";
            }
        }
        byte[] arrby = ByteBuffer.allocate(16).putInt(arrn[0]).putInt(arrn[1]).putInt(arrn[2]).putInt(arrn[3]).array();
        try {
            return InetAddress.getByAddress(arrby).getHostAddress().replaceAll("((?:(?:^|:)0+\\b){2,}):?(?!\\S*\\b\\1:0+\\b)(\\S*)", "::$2");
        }
        catch (UnknownHostException unknownHostException) {
            return "unknown";
        }
    }

    public static int O1il1llOLANd(String string, int n2) {
        if (string != null) {
            try {
                if (string.startsWith("0x")) {
                    return new BigInteger(string.substring(2), 16).intValue();
                }
                return new BigInteger(string, 16).intValue();
            }
            catch (NumberFormatException numberFormatException) {
                O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            }
        }
        return n2;
    }

    public static long li0iOILAND(String string, long l2) {
        if (string != null) {
            try {
                if (string.startsWith("0x")) {
                    return new BigInteger(string.substring(2), 16).longValue();
                }
                return new BigInteger(string, 16).longValue();
            }
            catch (NumberFormatException numberFormatException) {
                O1il1llOLANd.trace(Oill1LAnD, (Object)string, (Object)numberFormatException);
            }
        }
        return l2;
    }

    public static String iOIl0LAnD(String string) {
        int n2;
        for (n2 = 0; n2 < string.length() && string.charAt(n2) == '.'; ++n2) {
        }
        return n2 < string.length() ? string.substring(n2) : "";
    }

    static {
        II1Iland = TimeZone.getDefault().getOffset(System.currentTimeMillis());
        I1O1I1LaNd = Pattern.compile("\\s+:\\s");
        OOOIilanD = Pattern.compile("\\s+");
        lI00OlAND = Pattern.compile("[^0-9]+");
        lli0OiIlAND = Pattern.compile("^[^0-9]*");
        li0iOILAND = Pattern.compile("\\/");
        iOl10IlLAnd = new HashMap();
        iOl10IlLAnd.put(iOIl0LAnD, 1L);
        iOl10IlLAnd.put(iIiO00OLaNd, 1000L);
        iOl10IlLAnd.put(ii1li00Land, 1000000L);
        iOl10IlLAnd.put(IOI1LaNd, 1000000000L);
        iOl10IlLAnd.put(lI00ilAND, 1000000000000L);
        iOl10IlLAnd.put(l0l00lAND, 1000000000000000L);
        l0IO0LAnd = new long[]{1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};
        liOIOOLANd = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSSSSZZZZZ", Locale.US);
    }
}

