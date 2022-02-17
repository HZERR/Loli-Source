/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.IPHlpAPI;
import com.sun.jna.platform.win32.VersionHelpers;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import loliland.launcher.client.O0I1il01lANd;
import loliland.launcher.client.i1i0lOliLaND;
import loliland.launcher.client.lOilLanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class liIiO1liLAnd
extends i1i0lOliLaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(liIiO1liLAnd.class);
    private static final boolean OOOIilanD = VersionHelpers.IsWindowsVistaOrGreater();
    private static final byte lI00OlAND = 4;
    private int lli0OiIlAND;
    private int li0iOILAND;
    private boolean O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;
    private long lil0liLand;
    private long iilIi1laND;
    private long lli011lLANd;
    private long l0illAND;
    private long IO11O0LANd;
    private long l11lLANd;
    private long lO110l1LANd;
    private long l0iIlIO1laNd;
    private String iOIl0LAnD;
    private O0I1il01lANd iIiO00OLaNd;

    public liIiO1liLAnd(NetworkInterface networkInterface) throws InstantiationException {
        super(networkInterface);
        this.liOIOOLANd();
    }

    public static List OOOIilanD(boolean bl) {
        ArrayList<liIiO1liLAnd> arrayList = new ArrayList<liIiO1liLAnd>();
        for (NetworkInterface networkInterface : liIiO1liLAnd.I1O1I1LaNd(bl)) {
            try {
                arrayList.add(new liIiO1liLAnd(networkInterface));
            }
            catch (InstantiationException instantiationException) {
                I1O1I1LaNd.debug("Network Interface Instantiation failed: {}", (Object)instantiationException.getMessage());
            }
        }
        return arrayList;
    }

    @Override
    public int IO11O0LANd() {
        return this.lli0OiIlAND;
    }

    @Override
    public int l11lLANd() {
        return this.li0iOILAND;
    }

    @Override
    public boolean lO110l1LANd() {
        return this.O1il1llOLANd;
    }

    @Override
    public long l0iIlIO1laNd() {
        return this.Oill1LAnD;
    }

    @Override
    public long iOIl0LAnD() {
        return this.lIOILand;
    }

    @Override
    public long iIiO00OLaNd() {
        return this.lil0liLand;
    }

    @Override
    public long ii1li00Land() {
        return this.iilIi1laND;
    }

    @Override
    public long IOI1LaNd() {
        return this.lli011lLANd;
    }

    @Override
    public long lI00ilAND() {
        return this.l0illAND;
    }

    @Override
    public long l0l00lAND() {
        return this.IO11O0LANd;
    }

    @Override
    public long iOl10IlLAnd() {
        return this.l11lLANd;
    }

    @Override
    public long lIiIii1LAnD() {
        return this.lO110l1LANd;
    }

    @Override
    public long II1Iland() {
        return this.l0iIlIO1laNd;
    }

    @Override
    public String li0iOILAND() {
        return this.iOIl0LAnD;
    }

    @Override
    public O0I1il01lANd O1il1llOLANd() {
        return this.iIiO00OLaNd;
    }

    @Override
    public boolean liOIOOLANd() {
        if (OOOIilanD) {
            IPHlpAPI.MIB_IF_ROW2 mIB_IF_ROW2 = new IPHlpAPI.MIB_IF_ROW2();
            mIB_IF_ROW2.InterfaceIndex = this.I1O1I1LaNd().getIndex();
            if (0 != IPHlpAPI.INSTANCE.GetIfEntry2(mIB_IF_ROW2)) {
                I1O1I1LaNd.error("Failed to retrieve data for interface {}, {}", (Object)this.I1O1I1LaNd().getIndex(), (Object)this.OOOIilanD());
                return false;
            }
            this.lli0OiIlAND = mIB_IF_ROW2.Type;
            this.li0iOILAND = mIB_IF_ROW2.PhysicalMediumType;
            this.O1il1llOLANd = (mIB_IF_ROW2.InterfaceAndOperStatusFlags & 4) > 0;
            this.lIOILand = mIB_IF_ROW2.OutOctets;
            this.Oill1LAnD = mIB_IF_ROW2.InOctets;
            this.iilIi1laND = mIB_IF_ROW2.OutUcastPkts;
            this.lil0liLand = mIB_IF_ROW2.InUcastPkts;
            this.l0illAND = mIB_IF_ROW2.OutErrors;
            this.lli011lLANd = mIB_IF_ROW2.InErrors;
            this.l11lLANd = mIB_IF_ROW2.OutDiscards;
            this.IO11O0LANd = mIB_IF_ROW2.InDiscards;
            this.lO110l1LANd = mIB_IF_ROW2.ReceiveLinkSpeed;
            this.iOIl0LAnD = Native.toString(mIB_IF_ROW2.Alias);
            this.iIiO00OLaNd = O0I1il01lANd.I1O1I1LaNd(mIB_IF_ROW2.OperStatus);
        } else {
            IPHlpAPI.MIB_IFROW mIB_IFROW = new IPHlpAPI.MIB_IFROW();
            mIB_IFROW.dwIndex = this.I1O1I1LaNd().getIndex();
            if (0 != IPHlpAPI.INSTANCE.GetIfEntry(mIB_IFROW)) {
                I1O1I1LaNd.error("Failed to retrieve data for interface {}, {}", (Object)this.I1O1I1LaNd().getIndex(), (Object)this.OOOIilanD());
                return false;
            }
            this.lli0OiIlAND = mIB_IFROW.dwType;
            this.lIOILand = lOilLanD.I1O1I1LaNd(mIB_IFROW.dwOutOctets);
            this.Oill1LAnD = lOilLanD.I1O1I1LaNd(mIB_IFROW.dwInOctets);
            this.iilIi1laND = lOilLanD.I1O1I1LaNd(mIB_IFROW.dwOutUcastPkts);
            this.lil0liLand = lOilLanD.I1O1I1LaNd(mIB_IFROW.dwInUcastPkts);
            this.l0illAND = lOilLanD.I1O1I1LaNd(mIB_IFROW.dwOutErrors);
            this.lli011lLANd = lOilLanD.I1O1I1LaNd(mIB_IFROW.dwInErrors);
            this.l11lLANd = lOilLanD.I1O1I1LaNd(mIB_IFROW.dwOutDiscards);
            this.IO11O0LANd = lOilLanD.I1O1I1LaNd(mIB_IFROW.dwInDiscards);
            this.lO110l1LANd = lOilLanD.I1O1I1LaNd(mIB_IFROW.dwSpeed);
            this.iOIl0LAnD = "";
            this.iIiO00OLaNd = O0I1il01lANd.lli0OiIlAND;
        }
        this.l0iIlIO1laNd = System.currentTimeMillis();
        return true;
    }
}

