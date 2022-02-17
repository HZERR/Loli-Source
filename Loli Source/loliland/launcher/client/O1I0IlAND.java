/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.mac.SystemB;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.Oii011ILaND;
import loliland.launcher.client.lI01110LaNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lOlI1OILaND;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.liiIILaND;
import loliland.launcher.client.lilO00LANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class O1I0IlAND
extends lI01110LaNd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(O1I0IlAND.class);
    private final Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(this::O1il1llOLANd, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(O1I0IlAND::Oill1LAnD);
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(O1I0IlAND::lIOILand);
    private final Supplier li0iOILAND = lii1IO0LaNd.I1O1I1LaNd(this::lil0liLand);

    O1I0IlAND() {
    }

    @Override
    public long OOOIilanD() {
        return (Long)this.OOOIilanD.get();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)this.lI00OlAND.get();
    }

    @Override
    public long lI00OlAND() {
        return (Long)this.lli0OiIlAND.get();
    }

    @Override
    public lilO00LANd lli0OiIlAND() {
        return (lilO00LANd)this.li0iOILAND.get();
    }

    @Override
    public List li0iOILAND() {
        ArrayList<Oii011ILaND> arrayList = new ArrayList<Oii011ILaND>();
        List list = Iill1lanD.I1O1I1LaNd("system_profiler SPMemoryDataType");
        int n2 = 0;
        String string = "unknown";
        long l2 = 0L;
        long l3 = 0L;
        String string2 = "unknown";
        String string3 = "unknown";
        for (String string4 : list) {
            String[] arrstring;
            if (string4.trim().startsWith("BANK")) {
                int n3;
                if (n2++ > 0) {
                    arrayList.add(new Oii011ILaND(string, l2, l3, string2, string3));
                }
                if ((n3 = (string = string4.trim()).lastIndexOf(58)) <= 0) continue;
                string = string.substring(0, n3 - 1);
                continue;
            }
            if (n2 <= 0 || (arrstring = string4.trim().split(":")).length != 2) continue;
            switch (arrstring[0]) {
                case "Size": {
                    l2 = lOilLanD.lli011lLANd(arrstring[1].trim());
                    break;
                }
                case "Type": {
                    string3 = arrstring[1].trim();
                    break;
                }
                case "Speed": {
                    l3 = lOilLanD.I1O1I1LaNd(arrstring[1]);
                    break;
                }
                case "Manufacturer": {
                    string2 = arrstring[1].trim();
                    break;
                }
            }
        }
        arrayList.add(new Oii011ILaND(string, l2, l3, string2, string3));
        return arrayList;
    }

    private long O1il1llOLANd() {
        SystemB.VMStatistics vMStatistics = new SystemB.VMStatistics();
        if (0 != SystemB.INSTANCE.host_statistics(SystemB.INSTANCE.mach_host_self(), 2, vMStatistics, new IntByReference(vMStatistics.size() / SystemB.INT_SIZE))) {
            I1O1I1LaNd.error("Failed to get host VM info. Error code: {}", (Object)Native.getLastError());
            return 0L;
        }
        return (long)(vMStatistics.free_count + vMStatistics.inactive_count) * this.lI00OlAND();
    }

    private static long Oill1LAnD() {
        return liiIILaND.I1O1I1LaNd("hw.memsize", 0L);
    }

    private static long lIOILand() {
        LongByReference longByReference = new LongByReference();
        if (0 == SystemB.INSTANCE.host_page_size(SystemB.INSTANCE.mach_host_self(), longByReference)) {
            return longByReference.getValue();
        }
        I1O1I1LaNd.error("Failed to get host page size. Error code: {}", (Object)Native.getLastError());
        return 4098L;
    }

    private lilO00LANd lil0liLand() {
        return new lOlI1OILaND(this);
    }
}

