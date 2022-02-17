/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import java.util.function.Supplier;
import loliland.launcher.client.IiiilAnD;
import loliland.launcher.client.IilOi0lllaNd;
import loliland.launcher.client.O1IiIiI1LAND;
import loliland.launcher.client.i0l10iiilaNd;
import loliland.launcher.client.l11IliilLANd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1l10l0LaNd;
import loliland.launcher.client.llIIllanD;
import loliland.launcher.client.lli10iliLaND;

public class l1liland
extends i0l10iiilaNd {
    private Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(IiiilAnD::I1O1I1LaNd, lii1IO0LaNd.I1O1I1LaNd());
    private Supplier OOOIilanD = lii1IO0LaNd.I1O1I1LaNd(l1liland::O1il1llOLANd, lii1IO0LaNd.I1O1I1LaNd());
    private Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(l1liland::Oill1LAnD, lii1IO0LaNd.I1O1I1LaNd());

    @Override
    public ll1l10l0LaNd lli0OiIlAND() {
        llIIllanD llIIllanD2 = (llIIllanD)this.OOOIilanD.get();
        return new ll1l10l0LaNd((Long)((O1IiIiI1LAND)this.I1O1I1LaNd.get()).I1O1I1LaNd(), lOilLanD.I1O1I1LaNd(llIIllanD2.I1O1I1LaNd), lOilLanD.I1O1I1LaNd(llIIllanD2.OOOIilanD), lOilLanD.I1O1I1LaNd(llIIllanD2.lli0OiIlAND), lOilLanD.I1O1I1LaNd(llIIllanD2.lI00OlAND), lOilLanD.I1O1I1LaNd(llIIllanD2.li0iOILAND), lOilLanD.I1O1I1LaNd(llIIllanD2.Oill1LAnD), lOilLanD.I1O1I1LaNd(llIIllanD2.O1il1llOLANd), lOilLanD.I1O1I1LaNd(llIIllanD2.lIOILand + llIIllanD2.lil0liLand + llIIllanD2.iilIi1laND + llIIllanD2.lli011lLANd), 0L);
    }

    @Override
    public l11IliilLANd li0iOILAND() {
        IilOi0lllaNd iilOi0lllaNd = (IilOi0lllaNd)this.lI00OlAND.get();
        return new l11IliilLANd(lOilLanD.I1O1I1LaNd(iilOi0lllaNd.li0iOILAND), lOilLanD.I1O1I1LaNd(iilOi0lllaNd.I1O1I1LaNd), lOilLanD.I1O1I1LaNd(iilOi0lllaNd.O1il1llOLANd), lOilLanD.I1O1I1LaNd(iilOi0lllaNd.OOOIilanD + iilOi0lllaNd.lI00OlAND + iilOi0lllaNd.lli0OiIlAND));
    }

    @Override
    public l11IliilLANd OOOIilanD() {
        IilOi0lllaNd iilOi0lllaNd = (IilOi0lllaNd)this.lI00OlAND.get();
        return new l11IliilLANd(lOilLanD.I1O1I1LaNd(iilOi0lllaNd.lIOILand), lOilLanD.I1O1I1LaNd(iilOi0lllaNd.Oill1LAnD), 0L, 0L);
    }

    private static llIIllanD O1il1llOLANd() {
        llIIllanD llIIllanD2 = new llIIllanD();
        Memory memory = lli10iliLaND.I1O1I1LaNd("net.inet.tcp.stats");
        if (memory != null && memory.size() >= 128L) {
            llIIllanD2.I1O1I1LaNd = memory.getInt(0L);
            llIIllanD2.OOOIilanD = memory.getInt(4L);
            llIIllanD2.lI00OlAND = memory.getInt(12L);
            llIIllanD2.lli0OiIlAND = memory.getInt(16L);
            llIIllanD2.li0iOILAND = memory.getInt(64L);
            llIIllanD2.O1il1llOLANd = memory.getInt(72L);
            llIIllanD2.Oill1LAnD = memory.getInt(104L);
            llIIllanD2.lIOILand = memory.getInt(112L);
            llIIllanD2.lil0liLand = memory.getInt(116L);
            llIIllanD2.iilIi1laND = memory.getInt(120L);
            llIIllanD2.lli011lLANd = memory.getInt(124L);
        }
        return llIIllanD2;
    }

    private static IilOi0lllaNd Oill1LAnD() {
        IilOi0lllaNd iilOi0lllaNd = new IilOi0lllaNd();
        Memory memory = lli10iliLaND.I1O1I1LaNd("net.inet.udp.stats");
        if (memory != null && memory.size() >= 1644L) {
            iilOi0lllaNd.I1O1I1LaNd = memory.getInt(0L);
            iilOi0lllaNd.OOOIilanD = memory.getInt(4L);
            iilOi0lllaNd.lI00OlAND = memory.getInt(8L);
            iilOi0lllaNd.lli0OiIlAND = memory.getInt(12L);
            iilOi0lllaNd.li0iOILAND = memory.getInt(36L);
            iilOi0lllaNd.O1il1llOLANd = memory.getInt(48L);
            iilOi0lllaNd.Oill1LAnD = memory.getInt(64L);
            iilOi0lllaNd.lIOILand = memory.getInt(80L);
        }
        return iilOi0lllaNd;
    }
}

