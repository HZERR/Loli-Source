/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class iiiO00OOLAnD {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(iiiO00OOLAnD.class);
    private WinBase.SYSTEM_INFO OOOIilanD = null;

    public iiiO00OOLAnD() {
        this.OOOIilanD();
    }

    public iiiO00OOLAnD(WinBase.SYSTEM_INFO sYSTEM_INFO) {
        this.OOOIilanD = sYSTEM_INFO;
    }

    private void OOOIilanD() {
        WinBase.SYSTEM_INFO sYSTEM_INFO = new WinBase.SYSTEM_INFO();
        Kernel32.INSTANCE.GetSystemInfo(sYSTEM_INFO);
        try {
            IntByReference intByReference = new IntByReference();
            WinNT.HANDLE hANDLE = Kernel32.INSTANCE.GetCurrentProcess();
            if (Kernel32.INSTANCE.IsWow64Process(hANDLE, intByReference) && intByReference.getValue() > 0) {
                Kernel32.INSTANCE.GetNativeSystemInfo(sYSTEM_INFO);
            }
        }
        catch (UnsatisfiedLinkError unsatisfiedLinkError) {
            I1O1I1LaNd.trace("No WOW64 support: {}", (Object)unsatisfiedLinkError.getMessage());
        }
        this.OOOIilanD = sYSTEM_INFO;
        I1O1I1LaNd.debug("Initialized OSNativeSystemInfo");
    }

    public int I1O1I1LaNd() {
        return this.OOOIilanD.dwNumberOfProcessors.intValue();
    }
}

