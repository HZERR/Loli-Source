/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.VersionHelpers;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.Wtsapi32;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import loliland.launcher.client.iOI1I1iLaNd;
import loliland.launcher.client.lOilLanD;

public final class lii1LAND {
    private static final int I1O1I1LaNd = 0;
    private static final int OOOIilanD = 14;
    private static final int lI00OlAND = 24;
    private static final int lli0OiIlAND = 16;
    private static final boolean li0iOILAND = VersionHelpers.IsWindowsVistaOrGreater();
    private static final Wtsapi32 O1il1llOLANd = Wtsapi32.INSTANCE;

    private lii1LAND() {
    }

    public static List I1O1I1LaNd() {
        IntByReference intByReference;
        PointerByReference pointerByReference;
        ArrayList<iOI1I1iLaNd> arrayList = new ArrayList<iOI1I1iLaNd>();
        if (li0iOILAND && O1il1llOLANd.WTSEnumerateSessions(Wtsapi32.WTS_CURRENT_SERVER_HANDLE, 0, 1, pointerByReference = new PointerByReference(), intByReference = new IntByReference())) {
            Pointer pointer = pointerByReference.getValue();
            if (intByReference.getValue() > 0) {
                Wtsapi32.WTS_SESSION_INFO[] arrwTS_SESSION_INFO;
                Wtsapi32.WTS_SESSION_INFO wTS_SESSION_INFO = new Wtsapi32.WTS_SESSION_INFO(pointer);
                for (Wtsapi32.WTS_SESSION_INFO wTS_SESSION_INFO2 : arrwTS_SESSION_INFO = (Wtsapi32.WTS_SESSION_INFO[])wTS_SESSION_INFO.toArray(intByReference.getValue())) {
                    if (wTS_SESSION_INFO2.State != 0) continue;
                    PointerByReference pointerByReference2 = new PointerByReference();
                    IntByReference intByReference2 = new IntByReference();
                    O1il1llOLANd.WTSQuerySessionInformation(Wtsapi32.WTS_CURRENT_SERVER_HANDLE, wTS_SESSION_INFO2.SessionId, 16, pointerByReference2, intByReference2);
                    Pointer pointer2 = pointerByReference2.getValue();
                    short s2 = pointer2.getShort(0L);
                    O1il1llOLANd.WTSFreeMemory(pointer2);
                    if (s2 <= 0) continue;
                    String string = wTS_SESSION_INFO2.pWinStationName;
                    O1il1llOLANd.WTSQuerySessionInformation(Wtsapi32.WTS_CURRENT_SERVER_HANDLE, wTS_SESSION_INFO2.SessionId, 24, pointerByReference2, intByReference2);
                    pointer2 = pointerByReference2.getValue();
                    Wtsapi32.WTSINFO wTSINFO = new Wtsapi32.WTSINFO(pointer2);
                    long l2 = new WinBase.FILETIME(new WinNT.LARGE_INTEGER(wTSINFO.LogonTime.getValue())).toTime();
                    String string2 = wTSINFO.getUserName();
                    O1il1llOLANd.WTSFreeMemory(pointer2);
                    O1il1llOLANd.WTSQuerySessionInformation(Wtsapi32.WTS_CURRENT_SERVER_HANDLE, wTS_SESSION_INFO2.SessionId, 14, pointerByReference2, intByReference2);
                    pointer2 = pointerByReference2.getValue();
                    Wtsapi32.WTS_CLIENT_ADDRESS wTS_CLIENT_ADDRESS = new Wtsapi32.WTS_CLIENT_ADDRESS(pointer2);
                    O1il1llOLANd.WTSFreeMemory(pointer2);
                    String string3 = "::";
                    if (wTS_CLIENT_ADDRESS.AddressFamily == 2) {
                        try {
                            string3 = InetAddress.getByAddress(Arrays.copyOfRange(wTS_CLIENT_ADDRESS.Address, 2, 6)).getHostAddress();
                        }
                        catch (UnknownHostException unknownHostException) {
                            string3 = "Illegal length IP Array";
                        }
                    } else if (wTS_CLIENT_ADDRESS.AddressFamily == 23) {
                        int[] arrn = lii1LAND.I1O1I1LaNd(wTS_CLIENT_ADDRESS.Address);
                        string3 = lOilLanD.OOOIilanD(arrn);
                    }
                    arrayList.add(new iOI1I1iLaNd(string2, string, l2, string3));
                }
            }
            O1il1llOLANd.WTSFreeMemory(pointer);
        }
        return arrayList;
    }

    private static int[] I1O1I1LaNd(byte[] arrby) {
        IntBuffer intBuffer = ByteBuffer.wrap(Arrays.copyOfRange(arrby, 2, 18)).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        int[] arrn = new int[intBuffer.remaining()];
        intBuffer.get(arrn);
        return arrn;
    }
}

