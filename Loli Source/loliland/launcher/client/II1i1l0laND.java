/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.SystemB;
import com.sun.jna.ptr.IntByReference;
import java.util.HashMap;
import java.util.Map;
import loliland.launcher.client.OOOliOOllANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class II1i1l0laND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(II1i1l0laND.class);
    private static final int OOOIilanD = 4;
    private static final int lI00OlAND = 17;
    private static final int lli0OiIlAND = 6;
    private static final int li0iOILAND = 18;

    private II1i1l0laND() {
    }

    public static Map I1O1I1LaNd(int n2) {
        SystemB.IFmsgHdr iFmsgHdr;
        HashMap<Integer, OOOliOOllANd> hashMap = new HashMap<Integer, OOOliOOllANd>();
        int[] arrn = new int[]{4, 17, 0, 0, 6, 0};
        IntByReference intByReference = new IntByReference();
        if (0 != SystemB.INSTANCE.sysctl(arrn, 6, null, intByReference, null, 0)) {
            I1O1I1LaNd.error("Didn't get buffer length for IFLIST2");
            return hashMap;
        }
        Memory memory = new Memory(intByReference.getValue());
        if (0 != SystemB.INSTANCE.sysctl(arrn, 6, memory, intByReference, null, 0)) {
            I1O1I1LaNd.error("Didn't get buffer for IFLIST2");
            return hashMap;
        }
        long l2 = System.currentTimeMillis();
        int n3 = (int)(memory.size() - (long)new SystemB.IFmsgHdr().size());
        for (int i2 = 0; i2 < n3; i2 += iFmsgHdr.ifm_msglen) {
            Pointer pointer = memory.share(i2);
            iFmsgHdr = new SystemB.IFmsgHdr(pointer);
            iFmsgHdr.read();
            if (iFmsgHdr.ifm_type != 18) continue;
            SystemB.IFmsgHdr2 iFmsgHdr2 = new SystemB.IFmsgHdr2(pointer);
            iFmsgHdr2.read();
            if (n2 >= 0 && n2 != iFmsgHdr2.ifm_index) continue;
            hashMap.put(Integer.valueOf(iFmsgHdr2.ifm_index), new OOOliOOllANd(0xFF & iFmsgHdr2.ifm_data.ifi_type, iFmsgHdr2.ifm_data.ifi_opackets, iFmsgHdr2.ifm_data.ifi_ipackets, iFmsgHdr2.ifm_data.ifi_obytes, iFmsgHdr2.ifm_data.ifi_ibytes, iFmsgHdr2.ifm_data.ifi_oerrors, iFmsgHdr2.ifm_data.ifi_ierrors, iFmsgHdr2.ifm_data.ifi_collisions, iFmsgHdr2.ifm_data.ifi_iqdrops, iFmsgHdr2.ifm_data.ifi_baudrate, l2));
            if (n2 < 0) continue;
            return hashMap;
        }
        return hashMap;
    }
}

