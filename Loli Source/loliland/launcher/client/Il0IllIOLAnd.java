/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.VersionHelpers;
import java.util.Map;
import loliland.launcher.client.I1IiilLAnD;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.OiO1llAnd;
import loliland.launcher.client.Olli1LAnD;
import loliland.launcher.client.i10lIIOllANd;
import loliland.launcher.client.iiO101LanD;

public final class Il0IllIOLAnd {
    private static final String I1O1I1LaNd = "Processor";
    private static final String OOOIilanD = "Processor Information";
    private static final String lI00OlAND = "Win32_PerfRawData_Counters_ProcessorInformation WHERE NOT Name LIKE \"%_Total\"";
    private static final String lli0OiIlAND = "Win32_PerfRawData_PerfOS_Processor WHERE NOT Name=\"_Total\"";
    private static final String li0iOILAND = "Win32_PerfRawData_PerfOS_Processor WHERE Name=\"_Total\"";
    private static final boolean O1il1llOLANd = VersionHelpers.IsWindows7OrGreater();

    private Il0IllIOLAnd() {
    }

    public static O1IiIiI1LAND I1O1I1LaNd() {
        return O1il1llOLANd ? i10lIIOllANd.I1O1I1LaNd(OiO1llAnd.class, OOOIilanD, lI00OlAND) : i10lIIOllANd.I1O1I1LaNd(OiO1llAnd.class, I1O1I1LaNd, lli0OiIlAND);
    }

    public static Map OOOIilanD() {
        return iiO101LanD.I1O1I1LaNd(Olli1LAnD.class, I1O1I1LaNd, li0iOILAND);
    }

    public static O1IiIiI1LAND lI00OlAND() {
        return i10lIIOllANd.I1O1I1LaNd(I1IiilLAnD.class, OOOIilanD, lI00OlAND);
    }
}

