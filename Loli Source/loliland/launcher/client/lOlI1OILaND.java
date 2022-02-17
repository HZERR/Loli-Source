/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.mac.SystemB;
import com.sun.jna.ptr.IntByReference;
import java.util.function.Supplier;
import loliland.launcher.client.IOIIO1lanD;
import loliland.launcher.client.O1I0IlAND;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.liiIILaND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class lOlI1OILaND
extends IOIIO1lanD {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lOlI1OILaND.class);
    private final O1I0IlAND OOOIilanD;
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(lOlI1OILaND::Oill1LAnD, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(lOlI1OILaND::lIOILand, lii1IO0LaNd.I1O1I1LaNd());

    lOlI1OILaND(O1I0IlAND o1I0IlAND) {
        this.OOOIilanD = o1I0IlAND;
    }

    @Override
    public long OOOIilanD() {
        return (Long)((O1IiIiI1LAND)this.lI00OlAND.get()).I1O1I1LaNd();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)((O1IiIiI1LAND)this.lI00OlAND.get()).OOOIilanD();
    }

    @Override
    public long lI00OlAND() {
        return this.OOOIilanD.I1O1I1LaNd() + this.I1O1I1LaNd();
    }

    @Override
    public long lli0OiIlAND() {
        return this.OOOIilanD.I1O1I1LaNd() - this.OOOIilanD.OOOIilanD() + this.OOOIilanD();
    }

    @Override
    public long li0iOILAND() {
        return (Long)((O1IiIiI1LAND)this.lli0OiIlAND.get()).I1O1I1LaNd();
    }

    @Override
    public long O1il1llOLANd() {
        return (Long)((O1IiIiI1LAND)this.lli0OiIlAND.get()).OOOIilanD();
    }

    private static O1IiIiI1LAND Oill1LAnD() {
        long l2 = 0L;
        long l3 = 0L;
        SystemB.XswUsage xswUsage = new SystemB.XswUsage();
        if (liiIILaND.I1O1I1LaNd("vm.swapusage", xswUsage)) {
            l2 = xswUsage.xsu_used;
            l3 = xswUsage.xsu_total;
        }
        return new O1IiIiI1LAND(l2, l3);
    }

    private static O1IiIiI1LAND lIOILand() {
        long l2 = 0L;
        long l3 = 0L;
        SystemB.VMStatistics vMStatistics = new SystemB.VMStatistics();
        if (0 == SystemB.INSTANCE.host_statistics(SystemB.INSTANCE.mach_host_self(), 2, vMStatistics, new IntByReference(vMStatistics.size() / SystemB.INT_SIZE))) {
            l2 = lOilLanD.I1O1I1LaNd(vMStatistics.pageins);
            l3 = lOilLanD.I1O1I1LaNd(vMStatistics.pageouts);
        } else {
            I1O1I1LaNd.error("Failed to get host VM info. Error code: {}", (Object)Native.getLastError());
        }
        return new O1IiIiI1LAND(l2, l3);
    }
}

