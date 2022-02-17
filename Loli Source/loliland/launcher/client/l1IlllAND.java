/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.Advapi32;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.SetupApi;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.IOll1OIlaND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class l1IlllAND
extends IOll1OIlaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(l1IlllAND.class);
    private static final SetupApi OOOIilanD = SetupApi.INSTANCE;
    private static final Advapi32 lI00OlAND = Advapi32.INSTANCE;
    private static final Guid.GUID lli0OiIlAND = new Guid.GUID("E6F07B5F-EE97-4a90-B076-33F57BF4EAA7");

    l1IlllAND(byte[] arrby) {
        super(arrby);
        I1O1I1LaNd.debug("Initialized WindowsDisplay");
    }

    public static List OOOIilanD() {
        ArrayList<l1IlllAND> arrayList = new ArrayList<l1IlllAND>();
        WinNT.HANDLE hANDLE = OOOIilanD.SetupDiGetClassDevs(lli0OiIlAND, null, null, 18);
        if (!hANDLE.equals(WinBase.INVALID_HANDLE_VALUE)) {
            SetupApi.SP_DEVICE_INTERFACE_DATA sP_DEVICE_INTERFACE_DATA = new SetupApi.SP_DEVICE_INTERFACE_DATA();
            sP_DEVICE_INTERFACE_DATA.cbSize = sP_DEVICE_INTERFACE_DATA.size();
            SetupApi.SP_DEVINFO_DATA sP_DEVINFO_DATA = new SetupApi.SP_DEVINFO_DATA();
            int n2 = 0;
            while (OOOIilanD.SetupDiEnumDeviceInfo(hANDLE, n2, sP_DEVINFO_DATA)) {
                IntByReference intByReference;
                byte[] arrby;
                IntByReference intByReference2;
                WinReg.HKEY hKEY = OOOIilanD.SetupDiOpenDevRegKey(hANDLE, sP_DEVINFO_DATA, 1, 0, 1, 1);
                if (lI00OlAND.RegQueryValueEx(hKEY, "EDID", 0, intByReference2 = new IntByReference(), arrby = new byte[1], intByReference = new IntByReference()) == 234 && lI00OlAND.RegQueryValueEx(hKEY, "EDID", 0, intByReference2, arrby = new byte[intByReference.getValue()], intByReference) == 0) {
                    l1IlllAND l1IlllAND2 = new l1IlllAND(arrby);
                    arrayList.add(l1IlllAND2);
                }
                Advapi32.INSTANCE.RegCloseKey(hKEY);
                ++n2;
            }
            OOOIilanD.SetupDiDestroyDeviceInfoList(hANDLE);
        }
        return arrayList;
    }
}

