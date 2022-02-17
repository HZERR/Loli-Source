/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import loliland.launcher.client.lOilLanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class l0O0IlaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(l0O0IlaND.class);

    private l0O0IlaND() {
    }

    public static String I1O1I1LaNd(byte[] arrby) {
        String string = String.format("%8s%8s", Integer.toBinaryString(arrby[8] & 0xFF), Integer.toBinaryString(arrby[9] & 0xFF)).replace(' ', '0');
        I1O1I1LaNd.debug("Manufacurer ID: {}", (Object)string);
        return String.format("%s%s%s", Character.valueOf((char)(64 + Integer.parseInt(string.substring(1, 6), 2))), Character.valueOf((char)(64 + Integer.parseInt(string.substring(7, 11), 2))), Character.valueOf((char)(64 + Integer.parseInt(string.substring(12, 16), 2)))).replace("@", "");
    }

    public static String OOOIilanD(byte[] arrby) {
        return Integer.toHexString(ByteBuffer.wrap(Arrays.copyOfRange(arrby, 10, 12)).order(ByteOrder.LITTLE_ENDIAN).getShort() & 0xFFFF);
    }

    public static String lI00OlAND(byte[] arrby) {
        if (I1O1I1LaNd.isDebugEnabled()) {
            I1O1I1LaNd.debug("Serial number: {}", (Object)Arrays.toString(Arrays.copyOfRange(arrby, 12, 16)));
        }
        return String.format("%s%s%s%s", l0O0IlaND.I1O1I1LaNd(arrby[15]), l0O0IlaND.I1O1I1LaNd(arrby[14]), l0O0IlaND.I1O1I1LaNd(arrby[13]), l0O0IlaND.I1O1I1LaNd(arrby[12]));
    }

    private static String I1O1I1LaNd(byte by) {
        return Character.isLetterOrDigit((char)by) ? String.format("%s", Character.valueOf((char)by)) : String.format("%02X", by);
    }

    public static byte lli0OiIlAND(byte[] arrby) {
        return arrby[16];
    }

    public static int li0iOILAND(byte[] arrby) {
        byte by = arrby[17];
        I1O1I1LaNd.debug("Year-1990: {}", (Object)by);
        return by + 1990;
    }

    public static String O1il1llOLANd(byte[] arrby) {
        return arrby[18] + "." + arrby[19];
    }

    public static boolean Oill1LAnD(byte[] arrby) {
        return 1 == (arrby[20] & 0xFF) >> 7;
    }

    public static int lIOILand(byte[] arrby) {
        return arrby[21];
    }

    public static int lil0liLand(byte[] arrby) {
        return arrby[22];
    }

    public static byte[][] iilIi1laND(byte[] arrby) {
        byte[][] arrby2 = new byte[4][18];
        for (int i2 = 0; i2 < arrby2.length; ++i2) {
            System.arraycopy(arrby, 54 + 18 * i2, arrby2[i2], 0, 18);
        }
        return arrby2;
    }

    public static int lli011lLANd(byte[] arrby) {
        return ByteBuffer.wrap(Arrays.copyOfRange(arrby, 0, 4)).getInt();
    }

    public static String l0illAND(byte[] arrby) {
        int n2 = ByteBuffer.wrap(Arrays.copyOfRange(arrby, 0, 2)).order(ByteOrder.LITTLE_ENDIAN).getShort() / 100;
        int n3 = (arrby[2] & 0xFF) + ((arrby[4] & 0xF0) << 4);
        int n4 = (arrby[5] & 0xFF) + ((arrby[7] & 0xF0) << 4);
        return String.format("Clock %dMHz, Active Pixels %dx%d ", n2, n3, n4);
    }

    public static String IO11O0LANd(byte[] arrby) {
        return String.format("Field Rate %d-%d Hz vertical, %d-%d Hz horizontal, Max clock: %d MHz", arrby[5], arrby[6], arrby[7], arrby[8], arrby[9] * 10);
    }

    public static String l11lLANd(byte[] arrby) {
        return new String(Arrays.copyOfRange(arrby, 4, 18), StandardCharsets.US_ASCII).trim();
    }

    public static String lO110l1LANd(byte[] arrby) {
        byte[][] arrby2;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  Manuf. ID=").append(l0O0IlaND.I1O1I1LaNd(arrby));
        stringBuilder.append(", Product ID=").append(l0O0IlaND.OOOIilanD(arrby));
        stringBuilder.append(", ").append(l0O0IlaND.Oill1LAnD(arrby) ? "Digital" : "Analog");
        stringBuilder.append(", Serial=").append(l0O0IlaND.lI00OlAND(arrby));
        stringBuilder.append(", ManufDate=").append(l0O0IlaND.lli0OiIlAND(arrby) * 12 / 52 + 1).append('/').append(l0O0IlaND.li0iOILAND(arrby));
        stringBuilder.append(", EDID v").append(l0O0IlaND.O1il1llOLANd(arrby));
        int n2 = l0O0IlaND.lIOILand(arrby);
        int n3 = l0O0IlaND.lil0liLand(arrby);
        stringBuilder.append(String.format("%n  %d x %d cm (%.1f x %.1f in)", n2, n3, (double)n2 / 2.54, (double)n3 / 2.54));
        block8: for (byte[] arrby3 : arrby2 = l0O0IlaND.iilIi1laND(arrby)) {
            switch (l0O0IlaND.lli011lLANd(arrby3)) {
                case 255: {
                    stringBuilder.append("\n  Serial Number: ").append(l0O0IlaND.l11lLANd(arrby3));
                    continue block8;
                }
                case 254: {
                    stringBuilder.append("\n  Unspecified Text: ").append(l0O0IlaND.l11lLANd(arrby3));
                    continue block8;
                }
                case 253: {
                    stringBuilder.append("\n  Range Limits: ").append(l0O0IlaND.IO11O0LANd(arrby3));
                    continue block8;
                }
                case 252: {
                    stringBuilder.append("\n  Monitor Name: ").append(l0O0IlaND.l11lLANd(arrby3));
                    continue block8;
                }
                case 251: {
                    stringBuilder.append("\n  White Point Data: ").append(lOilLanD.I1O1I1LaNd(arrby3));
                    continue block8;
                }
                case 250: {
                    stringBuilder.append("\n  Standard Timing ID: ").append(lOilLanD.I1O1I1LaNd(arrby3));
                    continue block8;
                }
                default: {
                    if (l0O0IlaND.lli011lLANd(arrby3) <= 15 && l0O0IlaND.lli011lLANd(arrby3) >= 0) {
                        stringBuilder.append("\n  Manufacturer Data: ").append(lOilLanD.I1O1I1LaNd(arrby3));
                        continue block8;
                    }
                    stringBuilder.append("\n  Preferred Timing: ").append(l0O0IlaND.l0illAND(arrby3));
                }
            }
        }
        return stringBuilder.toString();
    }
}

