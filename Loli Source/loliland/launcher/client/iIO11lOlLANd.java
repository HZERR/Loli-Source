/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.IPHlpAPI;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.ptr.IntByReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.IiI1l1IllANd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.lOilLanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class iIO11lOlLANd
extends IiI1l1IllANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(iIO11lOlLANd.class);
    private static final int OOOIilanD = 3;

    iIO11lOlLANd() {
    }

    @Override
    public String I1O1I1LaNd() {
        char[] arrc = new char[256];
        IntByReference intByReference = new IntByReference(arrc.length);
        if (!Kernel32.INSTANCE.GetComputerNameEx(3, arrc, intByReference)) {
            I1O1I1LaNd.error("Failed to get dns domain name. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return "";
        }
        return Native.toString(arrc);
    }

    @Override
    public String[] lI00OlAND() {
        IntByReference intByReference = new IntByReference();
        int n2 = IPHlpAPI.INSTANCE.GetNetworkParams(null, intByReference);
        if (n2 != 111) {
            I1O1I1LaNd.error("Failed to get network parameters buffer size. Error code: {}", (Object)n2);
            return new String[0];
        }
        Memory memory = new Memory(intByReference.getValue());
        n2 = IPHlpAPI.INSTANCE.GetNetworkParams(memory, intByReference);
        if (n2 != 0) {
            I1O1I1LaNd.error("Failed to get network parameters. Error code: {}", (Object)n2);
            return new String[0];
        }
        IPHlpAPI.FIXED_INFO fIXED_INFO = new IPHlpAPI.FIXED_INFO(memory);
        ArrayList<String> arrayList = new ArrayList<String>();
        IPHlpAPI.IP_ADDR_STRING iP_ADDR_STRING = fIXED_INFO.DnsServerList;
        while (iP_ADDR_STRING != null) {
            String string = Native.toString(iP_ADDR_STRING.IpAddress.String, StandardCharsets.US_ASCII);
            int n3 = string.indexOf(0);
            if (n3 != -1) {
                string = string.substring(0, n3);
            }
            arrayList.add(string);
            iP_ADDR_STRING = iP_ADDR_STRING.Next;
        }
        return arrayList.toArray(new String[0]);
    }

    @Override
    public String OOOIilanD() {
        return Kernel32Util.getComputerName();
    }

    @Override
    public String lli0OiIlAND() {
        return iIO11lOlLANd.O1il1llOLANd();
    }

    @Override
    public String li0iOILAND() {
        return iIO11lOlLANd.Oill1LAnD();
    }

    private static String O1il1llOLANd() {
        List list = Iill1lanD.I1O1I1LaNd("route print -4 0.0.0.0");
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string.trim());
            if (arrstring.length <= 2 || !"0.0.0.0".equals(arrstring[0])) continue;
            return arrstring[2];
        }
        return "";
    }

    private static String Oill1LAnD() {
        List list = Iill1lanD.I1O1I1LaNd("route print -6 ::/0");
        for (String string : list) {
            String[] arrstring = lOilLanD.OOOIilanD.split(string.trim());
            if (arrstring.length <= 3 || !"::/0".equals(arrstring[2])) continue;
            return arrstring[3];
        }
        return "";
    }
}

