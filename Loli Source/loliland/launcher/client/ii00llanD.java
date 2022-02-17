/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.time.OffsetDateTime;
import loliland.launcher.client.IO00LaND;
import loliland.launcher.client.lOilLanD;

public final class ii00llanD {
    public static final String I1O1I1LaNd = "ROOT\\OpenHardwareMonitor";
    private static final String OOOIilanD = "%s is not a %s type. CIM Type is %d and VT type is %d";

    private ii00llanD() {
    }

    public static String I1O1I1LaNd(WbemcliUtil.WmiQuery wmiQuery) {
        Enum[] arrenum = (Enum[])wmiQuery.getPropertyEnum().getEnumConstants();
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        stringBuilder.append(arrenum[0].name());
        for (int i2 = 1; i2 < arrenum.length; ++i2) {
            stringBuilder.append(',').append(arrenum[i2].name());
        }
        stringBuilder.append(" FROM ").append(wmiQuery.getWmiClassName());
        return stringBuilder.toString();
    }

    public static String I1O1I1LaNd(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        if (wmiResult.getCIMType(enum_) == 8) {
            return ii00llanD.lli011lLANd(wmiResult, enum_, n2);
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "String", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    public static String OOOIilanD(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        OffsetDateTime offsetDateTime = ii00llanD.lI00OlAND(wmiResult, enum_, n2);
        if (offsetDateTime.equals(IO00LaND.lI00OlAND)) {
            return "";
        }
        return offsetDateTime.toLocalDate().toString();
    }

    public static OffsetDateTime lI00OlAND(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        if (wmiResult.getCIMType(enum_) == 101) {
            return lOilLanD.lil0liLand(ii00llanD.lli011lLANd(wmiResult, enum_, n2));
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "DateTime", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    public static String lli0OiIlAND(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        if (wmiResult.getCIMType(enum_) == 102) {
            return ii00llanD.lli011lLANd(wmiResult, enum_, n2);
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "Reference", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    private static String lli011lLANd(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        Object object = wmiResult.getValue(enum_, n2);
        if (object == null) {
            return "";
        }
        if (wmiResult.getVtType(enum_) == 8) {
            return (String)object;
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "String-mapped", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    public static long li0iOILAND(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        Object object = wmiResult.getValue(enum_, n2);
        if (object == null) {
            return 0L;
        }
        if (wmiResult.getCIMType(enum_) == 21 && wmiResult.getVtType(enum_) == 8) {
            return lOilLanD.OOOIilanD((String)object, 0L);
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "UINT64", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    public static int O1il1llOLANd(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        if (wmiResult.getCIMType(enum_) == 19) {
            return ii00llanD.l0illAND(wmiResult, enum_, n2);
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "UINT32", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    public static long Oill1LAnD(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        if (wmiResult.getCIMType(enum_) == 19) {
            return (long)ii00llanD.l0illAND(wmiResult, enum_, n2) & 0xFFFFFFFFL;
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "UINT32", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    public static int lIOILand(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        if (wmiResult.getCIMType(enum_) == 3) {
            return ii00llanD.l0illAND(wmiResult, enum_, n2);
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "SINT32", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    public static int lil0liLand(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        if (wmiResult.getCIMType(enum_) == 18) {
            return ii00llanD.l0illAND(wmiResult, enum_, n2);
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "UINT16", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    private static int l0illAND(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        Object object = wmiResult.getValue(enum_, n2);
        if (object == null) {
            return 0;
        }
        if (wmiResult.getVtType(enum_) == 3) {
            return (Integer)object;
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "32-bit integer", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }

    public static float iilIi1laND(WbemcliUtil.WmiResult wmiResult, Enum enum_, int n2) {
        Object object = wmiResult.getValue(enum_, n2);
        if (object == null) {
            return 0.0f;
        }
        if (wmiResult.getCIMType(enum_) == 4 && wmiResult.getVtType(enum_) == 4) {
            return ((Float)object).floatValue();
        }
        throw new ClassCastException(String.format(OOOIilanD, enum_.name(), "Float", wmiResult.getCIMType(enum_), wmiResult.getVtType(enum_)));
    }
}

