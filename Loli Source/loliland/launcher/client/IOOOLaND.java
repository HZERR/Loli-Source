/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.COM.WbemcliUtil;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Psapi;
import com.sun.jna.platform.win32.VersionHelpers;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import loliland.launcher.client.I111i0OILaNd;
import loliland.launcher.client.IIOOOlIiLanD;
import loliland.launcher.client.OIi00l0LaND;
import loliland.launcher.client.Oii011ILaND;
import loliland.launcher.client.ii00llanD;
import loliland.launcher.client.lI01110LaNd;
import loliland.launcher.client.lIOi0LANd;
import loliland.launcher.client.lIiO0OLaNd;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.lilO00LANd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class IOOOLaND
extends lI01110LaNd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(IOOOLaND.class);
    private static final boolean OOOIilanD = VersionHelpers.IsWindows10OrGreater();
    private final Supplier lI00OlAND = lii1IO0LaNd.I1O1I1LaNd(IOOOLaND::Oill1LAnD, lii1IO0LaNd.I1O1I1LaNd());
    private final Supplier lli0OiIlAND = lii1IO0LaNd.I1O1I1LaNd(this::O1il1llOLANd);

    IOOOLaND() {
    }

    @Override
    public long OOOIilanD() {
        return (Long)((IIOOOlIiLanD)this.lI00OlAND.get()).I1O1I1LaNd();
    }

    @Override
    public long I1O1I1LaNd() {
        return (Long)((IIOOOlIiLanD)this.lI00OlAND.get()).OOOIilanD();
    }

    @Override
    public long lI00OlAND() {
        return (Long)((IIOOOlIiLanD)this.lI00OlAND.get()).lI00OlAND();
    }

    @Override
    public lilO00LANd lli0OiIlAND() {
        return (lilO00LANd)this.lli0OiIlAND.get();
    }

    private lilO00LANd O1il1llOLANd() {
        return new lIiO0OLaNd(this);
    }

    @Override
    public List li0iOILAND() {
        ArrayList<Oii011ILaND> arrayList = new ArrayList<Oii011ILaND>();
        if (OOOIilanD) {
            WbemcliUtil.WmiResult wmiResult = OIi00l0LaND.I1O1I1LaNd();
            for (int i2 = 0; i2 < wmiResult.getResultCount(); ++i2) {
                String string = ii00llanD.I1O1I1LaNd(wmiResult, I111i0OILaNd.I1O1I1LaNd, i2);
                long l2 = ii00llanD.li0iOILAND(wmiResult, I111i0OILaNd.OOOIilanD, i2);
                long l3 = (long)ii00llanD.O1il1llOLANd(wmiResult, I111i0OILaNd.lI00OlAND, i2) * 1000000L;
                String string2 = ii00llanD.I1O1I1LaNd(wmiResult, I111i0OILaNd.lli0OiIlAND, i2);
                String string3 = IOOOLaND.OOOIilanD(ii00llanD.O1il1llOLANd(wmiResult, I111i0OILaNd.li0iOILAND, i2));
                arrayList.add(new Oii011ILaND(string, l2, l3, string2, string3));
            }
        } else {
            WbemcliUtil.WmiResult wmiResult = OIi00l0LaND.OOOIilanD();
            for (int i3 = 0; i3 < wmiResult.getResultCount(); ++i3) {
                String string = ii00llanD.I1O1I1LaNd(wmiResult, lIOi0LANd.I1O1I1LaNd, i3);
                long l4 = ii00llanD.li0iOILAND(wmiResult, lIOi0LANd.OOOIilanD, i3);
                long l5 = (long)ii00llanD.O1il1llOLANd(wmiResult, lIOi0LANd.lI00OlAND, i3) * 1000000L;
                String string4 = ii00llanD.I1O1I1LaNd(wmiResult, lIOi0LANd.lli0OiIlAND, i3);
                String string5 = IOOOLaND.I1O1I1LaNd(ii00llanD.lil0liLand(wmiResult, lIOi0LANd.li0iOILAND, i3));
                arrayList.add(new Oii011ILaND(string, l4, l5, string4, string5));
            }
        }
        return arrayList;
    }

    private static String I1O1I1LaNd(int n2) {
        switch (n2) {
            case 1: {
                return "Other";
            }
            case 2: {
                return "DRAM";
            }
            case 3: {
                return "Synchronous DRAM";
            }
            case 4: {
                return "Cache DRAM";
            }
            case 5: {
                return "EDO";
            }
            case 6: {
                return "EDRAM";
            }
            case 7: {
                return "VRAM";
            }
            case 8: {
                return "SRAM";
            }
            case 9: {
                return "RAM";
            }
            case 10: {
                return "ROM";
            }
            case 11: {
                return "Flash";
            }
            case 12: {
                return "EEPROM";
            }
            case 13: {
                return "FEPROM";
            }
            case 14: {
                return "EPROM";
            }
            case 15: {
                return "CDRAM";
            }
            case 16: {
                return "3DRAM";
            }
            case 17: {
                return "SDRAM";
            }
            case 18: {
                return "SGRAM";
            }
            case 19: {
                return "RDRAM";
            }
            case 20: {
                return "DDR";
            }
            case 21: {
                return "DDR2";
            }
            case 22: {
                return "DDR2-FB-DIMM";
            }
            case 24: {
                return "DDR3";
            }
            case 25: {
                return "FBD2";
            }
        }
        return "Unknown";
    }

    private static String OOOIilanD(int n2) {
        switch (n2) {
            case 1: {
                return "Other";
            }
            case 3: {
                return "DRAM";
            }
            case 4: {
                return "EDRAM";
            }
            case 5: {
                return "VRAM";
            }
            case 6: {
                return "SRAM";
            }
            case 7: {
                return "RAM";
            }
            case 8: {
                return "ROM";
            }
            case 9: {
                return "FLASH";
            }
            case 10: {
                return "EEPROM";
            }
            case 11: {
                return "FEPROM";
            }
            case 12: {
                return "EPROM";
            }
            case 13: {
                return "CDRAM";
            }
            case 14: {
                return "3DRAM";
            }
            case 15: {
                return "SDRAM";
            }
            case 16: {
                return "SGRAM";
            }
            case 17: {
                return "RDRAM";
            }
            case 18: {
                return "DDR";
            }
            case 19: {
                return "DDR2";
            }
            case 20: {
                return "DDR2 FB-DIMM";
            }
            case 24: {
                return "DDR3";
            }
            case 25: {
                return "FBD2";
            }
            case 26: {
                return "DDR4";
            }
            case 27: {
                return "LPDDR";
            }
            case 28: {
                return "LPDDR2";
            }
            case 29: {
                return "LPDDR3";
            }
            case 30: {
                return "LPDDR4";
            }
            case 31: {
                return "Logical non-volatile device";
            }
        }
        return "Unknown";
    }

    private static IIOOOlIiLanD Oill1LAnD() {
        Psapi.PERFORMANCE_INFORMATION pERFORMANCE_INFORMATION = new Psapi.PERFORMANCE_INFORMATION();
        if (!Psapi.INSTANCE.GetPerformanceInfo(pERFORMANCE_INFORMATION, pERFORMANCE_INFORMATION.size())) {
            I1O1I1LaNd.error("Failed to get Performance Info. Error code: {}", (Object)Kernel32.INSTANCE.GetLastError());
            return new IIOOOlIiLanD(0L, 0L, 4098L);
        }
        long l2 = pERFORMANCE_INFORMATION.PageSize.longValue();
        long l3 = l2 * pERFORMANCE_INFORMATION.PhysicalAvailable.longValue();
        long l4 = l2 * pERFORMANCE_INFORMATION.PhysicalTotal.longValue();
        return new IIOOOlIiLanD(l3, l4, l2);
    }
}

