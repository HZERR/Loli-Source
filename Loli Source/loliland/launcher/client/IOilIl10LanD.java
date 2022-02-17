/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Netapi32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.iOI1I1iLaNd;

public final class IOilIl10LanD {
    private static final Netapi32 I1O1I1LaNd = Netapi32.INSTANCE;

    private IOilIl10LanD() {
    }

    public static List I1O1I1LaNd() {
        ArrayList<iOI1I1iLaNd> arrayList = new ArrayList<iOI1I1iLaNd>();
        PointerByReference pointerByReference = new PointerByReference();
        IntByReference intByReference = new IntByReference();
        IntByReference intByReference2 = new IntByReference();
        if (0 == I1O1I1LaNd.NetSessionEnum(null, null, null, 10, pointerByReference, -1, intByReference, intByReference2, null)) {
            Pointer pointer = pointerByReference.getValue();
            Netapi32.SESSION_INFO_10 sESSION_INFO_10 = new Netapi32.SESSION_INFO_10(pointer);
            if (intByReference.getValue() > 0) {
                Netapi32.SESSION_INFO_10[] arrsESSION_INFO_10;
                for (Netapi32.SESSION_INFO_10 sESSION_INFO_102 : arrsESSION_INFO_10 = (Netapi32.SESSION_INFO_10[])sESSION_INFO_10.toArray(intByReference.getValue())) {
                    long l2 = System.currentTimeMillis() - 1000L * (long)sESSION_INFO_102.sesi10_time;
                    arrayList.add(new iOI1I1iLaNd(sESSION_INFO_102.sesi10_username, "Network session", l2, sESSION_INFO_102.sesi10_cname));
                }
            }
            I1O1I1LaNd.NetApiBufferFree(pointer);
        }
        return arrayList;
    }
}

