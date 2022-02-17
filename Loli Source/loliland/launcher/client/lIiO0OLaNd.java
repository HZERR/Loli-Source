/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import java.util.Map;
import java.util.function.Supplier;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.IOIIO1lanD;
import loliland.launcher.client.IOOOLaND;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.i0IO1II1laND;
import loliland.launcher.client.iI0IlIlLAnD;
import loliland.launcher.client.ilI110lAnd;
import loliland.launcher.client.l1O0i1IlanD;
import loliland.launcher.client.lii1IO0LaNd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class lIiO0OLaNd
extends IOIIO1lanD {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(lIiO0OLaNd.class);
    private final IOOOLaND OOOIilanD;
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(lIiO0OLaNd::Oill1LAnD, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(lIiO0OLaNd::lIOILand, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier li0iOILAND = lii1IO0LaNd.I1O1I1LaNd(lIiO0OLaNd::lil0liLand, lii1IO0LaNd.I1O1I1LaNd());

    lIiO0OLaNd(IOOOLaND iOOOLaND) {
        this.OOOIilanD = iOOOLaND;
    }

    @Override
    public long OOOIilanD() {
        return this.OOOIilanD.lI00OlAND() * (Long)this.lI00OlAND.get();
    }

    @Override
    public long I1O1I1LaNd() {
        return this.OOOIilanD.lI00OlAND() * (Long)((IIOOOlIiLanD)this.lli0OiIlAND.get()).I1O1I1LaNd();
    }

    @Override
    public long lI00OlAND() {
        return this.OOOIilanD.lI00OlAND() * (Long)((IIOOOlIiLanD)this.lli0OiIlAND.get()).OOOIilanD();
    }

    @Override
    public long lli0OiIlAND() {
        return this.OOOIilanD.lI00OlAND() * (Long)((IIOOOlIiLanD)this.lli0OiIlAND.get()).lI00OlAND();
    }

    @Override
    public long li0iOILAND() {
        return (Long)((O1IiIiI1LAND)this.li0iOILAND.get()).I1O1I1LaNd();
    }

    @Override
    public long O1il1llOLANd() {
        return (Long)((O1IiIiI1LAND)this.li0iOILAND.get()).OOOIilanD();
    }

    private static long Oill1LAnD() {
        return i0IO1II1laND.I1O1I1LaNd().getOrDefault(l1O0i1IlanD.I1O1I1LaNd, 0L);
    }

    private static IIOOOlIiLanD lIOILand() {
        Psapi.PERFORMANCE_INFORMATION pERFORMANCE_INFORMATION = new Psapi.PERFORMANCE_INFORMATION();
        if (!Psapi.INSTANCE.GetPerformanceInfo(pERFORMANCE_INFORMATION, pERFORMANCE_INFORMATION.size())) {
            I1O1I1LaNd.error("Failed to get Performance Info. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return new IIOOOlIiLanD(0L, 0L, 0L);
        }
        return new IIOOOlIiLanD(pERFORMANCE_INFORMATION.CommitLimit.longValue() - pERFORMANCE_INFORMATION.PhysicalTotal.longValue(), pERFORMANCE_INFORMATION.CommitLimit.longValue(), pERFORMANCE_INFORMATION.CommitTotal.longValue());
    }

    private static O1IiIiI1LAND lil0liLand() {
        Map map = iI0IlIlLAnD.I1O1I1LaNd();
        return new O1IiIiI1LAND(map.getOrDefault(ilI110lAnd.I1O1I1LaNd, 0L), map.getOrDefault(ilI110lAnd.OOOIilanD, 0L));
    }
}

