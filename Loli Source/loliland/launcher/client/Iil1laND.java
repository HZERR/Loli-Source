/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.IPHlpAPI;
import com.sun.jna.platform.win32.VersionHelpers;
import com.sun.jna.ptr.IntByReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import loliland.launcher.client.i0l10iiilaNd;
import loliland.launcher.client.l11IliilLANd;
import loliland.launcher.client.lOOO110laNd;
import loliland.launcher.client.lOilLanD;
import loliland.launcher.client.liIli0OlAND;
import loliland.launcher.client.ll1l10l0LaNd;

public class Iil1laND
extends i0l10iiilaNd {
    private static final IPHlpAPI I1O1I1LaNd = IPHlpAPI.INSTANCE;
    private static final boolean OOOIilanD = VersionHelpers.IsWindowsVistaOrGreater();

    @Override
    public ll1l10l0LaNd lli0OiIlAND() {
        IPHlpAPI.MIB_TCPSTATS mIB_TCPSTATS = new IPHlpAPI.MIB_TCPSTATS();
        I1O1I1LaNd.GetTcpStatisticsEx(mIB_TCPSTATS, 2);
        return new ll1l10l0LaNd(mIB_TCPSTATS.dwCurrEstab, mIB_TCPSTATS.dwActiveOpens, mIB_TCPSTATS.dwPassiveOpens, mIB_TCPSTATS.dwAttemptFails, mIB_TCPSTATS.dwEstabResets, mIB_TCPSTATS.dwOutSegs, mIB_TCPSTATS.dwInSegs, mIB_TCPSTATS.dwRetransSegs, mIB_TCPSTATS.dwInErrs, mIB_TCPSTATS.dwOutRsts);
    }

    @Override
    public ll1l10l0LaNd I1O1I1LaNd() {
        IPHlpAPI.MIB_TCPSTATS mIB_TCPSTATS = new IPHlpAPI.MIB_TCPSTATS();
        I1O1I1LaNd.GetTcpStatisticsEx(mIB_TCPSTATS, 23);
        return new ll1l10l0LaNd(mIB_TCPSTATS.dwCurrEstab, mIB_TCPSTATS.dwActiveOpens, mIB_TCPSTATS.dwPassiveOpens, mIB_TCPSTATS.dwAttemptFails, mIB_TCPSTATS.dwEstabResets, mIB_TCPSTATS.dwOutSegs, mIB_TCPSTATS.dwInSegs, mIB_TCPSTATS.dwRetransSegs, mIB_TCPSTATS.dwInErrs, mIB_TCPSTATS.dwOutRsts);
    }

    @Override
    public l11IliilLANd li0iOILAND() {
        IPHlpAPI.MIB_UDPSTATS mIB_UDPSTATS = new IPHlpAPI.MIB_UDPSTATS();
        I1O1I1LaNd.GetUdpStatisticsEx(mIB_UDPSTATS, 2);
        return new l11IliilLANd(mIB_UDPSTATS.dwOutDatagrams, mIB_UDPSTATS.dwInDatagrams, mIB_UDPSTATS.dwNoPorts, mIB_UDPSTATS.dwInErrors);
    }

    @Override
    public l11IliilLANd OOOIilanD() {
        IPHlpAPI.MIB_UDPSTATS mIB_UDPSTATS = new IPHlpAPI.MIB_UDPSTATS();
        I1O1I1LaNd.GetUdpStatisticsEx(mIB_UDPSTATS, 23);
        return new l11IliilLANd(mIB_UDPSTATS.dwOutDatagrams, mIB_UDPSTATS.dwInDatagrams, mIB_UDPSTATS.dwNoPorts, mIB_UDPSTATS.dwInErrors);
    }

    @Override
    public List lI00OlAND() {
        if (OOOIilanD) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Iil1laND.O1il1llOLANd());
            arrayList.addAll(Iil1laND.Oill1LAnD());
            arrayList.addAll(Iil1laND.lIOILand());
            arrayList.addAll(Iil1laND.lil0liLand());
            return arrayList;
        }
        return Collections.emptyList();
    }

    private static List O1il1llOLANd() {
        Memory memory;
        int n2;
        ArrayList<lOOO110laNd> arrayList = new ArrayList<lOOO110laNd>();
        IntByReference intByReference = new IntByReference();
        I1O1I1LaNd.GetExtendedTcpTable(null, intByReference, false, 2, 5, 0);
        do {
            n2 = intByReference.getValue();
            memory = new Memory(n2);
            I1O1I1LaNd.GetExtendedTcpTable(memory, intByReference, false, 2, 5, 0);
        } while (n2 < intByReference.getValue());
        IPHlpAPI.MIB_TCPTABLE_OWNER_PID mIB_TCPTABLE_OWNER_PID = new IPHlpAPI.MIB_TCPTABLE_OWNER_PID(memory);
        for (int i2 = 0; i2 < mIB_TCPTABLE_OWNER_PID.dwNumEntries; ++i2) {
            IPHlpAPI.MIB_TCPROW_OWNER_PID mIB_TCPROW_OWNER_PID = mIB_TCPTABLE_OWNER_PID.table[i2];
            arrayList.add(new lOOO110laNd("tcp4", lOilLanD.OOOIilanD(mIB_TCPROW_OWNER_PID.dwLocalAddr), lOilLanD.lI00OlAND(mIB_TCPROW_OWNER_PID.dwLocalPort), lOilLanD.OOOIilanD(mIB_TCPROW_OWNER_PID.dwRemoteAddr), lOilLanD.lI00OlAND(mIB_TCPROW_OWNER_PID.dwRemotePort), Iil1laND.I1O1I1LaNd(mIB_TCPROW_OWNER_PID.dwState), 0, 0, mIB_TCPROW_OWNER_PID.dwOwningPid));
        }
        return arrayList;
    }

    private static List Oill1LAnD() {
        Memory memory;
        int n2;
        ArrayList<lOOO110laNd> arrayList = new ArrayList<lOOO110laNd>();
        IntByReference intByReference = new IntByReference();
        I1O1I1LaNd.GetExtendedTcpTable(null, intByReference, false, 23, 5, 0);
        do {
            n2 = intByReference.getValue();
            memory = new Memory(n2);
            I1O1I1LaNd.GetExtendedTcpTable(memory, intByReference, false, 23, 5, 0);
        } while (n2 < intByReference.getValue());
        IPHlpAPI.MIB_TCP6TABLE_OWNER_PID mIB_TCP6TABLE_OWNER_PID = new IPHlpAPI.MIB_TCP6TABLE_OWNER_PID(memory);
        for (int i2 = 0; i2 < mIB_TCP6TABLE_OWNER_PID.dwNumEntries; ++i2) {
            IPHlpAPI.MIB_TCP6ROW_OWNER_PID mIB_TCP6ROW_OWNER_PID = mIB_TCP6TABLE_OWNER_PID.table[i2];
            arrayList.add(new lOOO110laNd("tcp6", mIB_TCP6ROW_OWNER_PID.LocalAddr, lOilLanD.lI00OlAND(mIB_TCP6ROW_OWNER_PID.dwLocalPort), mIB_TCP6ROW_OWNER_PID.RemoteAddr, lOilLanD.lI00OlAND(mIB_TCP6ROW_OWNER_PID.dwRemotePort), Iil1laND.I1O1I1LaNd(mIB_TCP6ROW_OWNER_PID.State), 0, 0, mIB_TCP6ROW_OWNER_PID.dwOwningPid));
        }
        return arrayList;
    }

    private static List lIOILand() {
        Memory memory;
        int n2;
        ArrayList<lOOO110laNd> arrayList = new ArrayList<lOOO110laNd>();
        IntByReference intByReference = new IntByReference();
        I1O1I1LaNd.GetExtendedUdpTable(null, intByReference, false, 2, 1, 0);
        do {
            n2 = intByReference.getValue();
            memory = new Memory(n2);
            I1O1I1LaNd.GetExtendedUdpTable(memory, intByReference, false, 2, 1, 0);
        } while (n2 < intByReference.getValue());
        IPHlpAPI.MIB_UDPTABLE_OWNER_PID mIB_UDPTABLE_OWNER_PID = new IPHlpAPI.MIB_UDPTABLE_OWNER_PID(memory);
        for (int i2 = 0; i2 < mIB_UDPTABLE_OWNER_PID.dwNumEntries; ++i2) {
            IPHlpAPI.MIB_UDPROW_OWNER_PID mIB_UDPROW_OWNER_PID = mIB_UDPTABLE_OWNER_PID.table[i2];
            arrayList.add(new lOOO110laNd("udp4", lOilLanD.OOOIilanD(mIB_UDPROW_OWNER_PID.dwLocalAddr), lOilLanD.lI00OlAND(mIB_UDPROW_OWNER_PID.dwLocalPort), new byte[0], 0, liIli0OlAND.IO11O0LANd, 0, 0, mIB_UDPROW_OWNER_PID.dwOwningPid));
        }
        return arrayList;
    }

    private static List lil0liLand() {
        Memory memory;
        int n2;
        ArrayList<lOOO110laNd> arrayList = new ArrayList<lOOO110laNd>();
        IntByReference intByReference = new IntByReference();
        I1O1I1LaNd.GetExtendedUdpTable(null, intByReference, false, 23, 1, 0);
        do {
            n2 = intByReference.getValue();
            memory = new Memory(n2);
            I1O1I1LaNd.GetExtendedUdpTable(memory, intByReference, false, 23, 1, 0);
        } while (n2 < intByReference.getValue());
        IPHlpAPI.MIB_UDP6TABLE_OWNER_PID mIB_UDP6TABLE_OWNER_PID = new IPHlpAPI.MIB_UDP6TABLE_OWNER_PID(memory);
        for (int i2 = 0; i2 < mIB_UDP6TABLE_OWNER_PID.dwNumEntries; ++i2) {
            IPHlpAPI.MIB_UDP6ROW_OWNER_PID mIB_UDP6ROW_OWNER_PID = mIB_UDP6TABLE_OWNER_PID.table[i2];
            arrayList.add(new lOOO110laNd("udp6", mIB_UDP6ROW_OWNER_PID.ucLocalAddr, lOilLanD.lI00OlAND(mIB_UDP6ROW_OWNER_PID.dwLocalPort), new byte[0], 0, liIli0OlAND.IO11O0LANd, 0, 0, mIB_UDP6ROW_OWNER_PID.dwOwningPid));
        }
        return arrayList;
    }

    private static liIli0OlAND I1O1I1LaNd(int n2) {
        switch (n2) {
            case 1: 
            case 12: {
                return liIli0OlAND.OOOIilanD;
            }
            case 2: {
                return liIli0OlAND.lI00OlAND;
            }
            case 3: {
                return liIli0OlAND.lli0OiIlAND;
            }
            case 4: {
                return liIli0OlAND.li0iOILAND;
            }
            case 5: {
                return liIli0OlAND.O1il1llOLANd;
            }
            case 6: {
                return liIli0OlAND.Oill1LAnD;
            }
            case 7: {
                return liIli0OlAND.lIOILand;
            }
            case 8: {
                return liIli0OlAND.lil0liLand;
            }
            case 9: {
                return liIli0OlAND.iilIi1laND;
            }
            case 10: {
                return liIli0OlAND.lli011lLANd;
            }
            case 11: {
                return liIli0OlAND.l0illAND;
            }
        }
        return liIli0OlAND.I1O1I1LaNd;
    }
}

