/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import loliland.launcher.client.II1i1l0laND;
import loliland.launcher.client.OOOliOOllANd;
import loliland.launcher.client.i1i0lOliLaND;
import loliland.launcher.client.iOiiOLAnD;
import loliland.launcher.client.ll0OlllanD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class i0I0i10llaNd
extends i1i0lOliLaND {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(i0I0i10llaNd.class);
    private int OOOIilanD;
    private long lI00OlAND;
    private long lli0OiIlAND;
    private long li0iOILAND;
    private long O1il1llOLANd;
    private long Oill1LAnD;
    private long lIOILand;
    private long lil0liLand;
    private long iilIi1laND;
    private long lli011lLANd;
    private long l0illAND;

    public i0I0i10llaNd(NetworkInterface networkInterface, Map map) throws InstantiationException {
        super(networkInterface, i0I0i10llaNd.I1O1I1LaNd(networkInterface));
        this.I1O1I1LaNd(map);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static String I1O1I1LaNd(NetworkInterface networkInterface) {
        String string = networkInterface.getName();
        CoreFoundation.CFArrayRef cFArrayRef = iOiiOLAnD.INSTANCE.SCNetworkInterfaceCopyAll();
        if (cFArrayRef != null) {
            try {
                int n2 = cFArrayRef.getCount();
                for (int i2 = 0; i2 < n2; ++i2) {
                    Pointer pointer = cFArrayRef.getValueAtIndex(i2);
                    ll0OlllanD ll0OlllanD2 = new ll0OlllanD(pointer);
                    CoreFoundation.CFStringRef cFStringRef = iOiiOLAnD.INSTANCE.SCNetworkInterfaceGetBSDName(ll0OlllanD2);
                    if (cFStringRef == null || !string.equals(cFStringRef.stringValue())) continue;
                    CoreFoundation.CFStringRef cFStringRef2 = iOiiOLAnD.INSTANCE.SCNetworkInterfaceGetLocalizedDisplayName(ll0OlllanD2);
                    String string2 = cFStringRef2.stringValue();
                    return string2;
                }
            }
            finally {
                cFArrayRef.release();
            }
        }
        return string;
    }

    public static List OOOIilanD(boolean bl) {
        Map map = II1i1l0laND.I1O1I1LaNd(-1);
        ArrayList<i0I0i10llaNd> arrayList = new ArrayList<i0I0i10llaNd>();
        for (NetworkInterface networkInterface : i0I0i10llaNd.I1O1I1LaNd(bl)) {
            try {
                arrayList.add(new i0I0i10llaNd(networkInterface, map));
            }
            catch (InstantiationException instantiationException) {
                I1O1I1LaNd.debug("Network Interface Instantiation failed: {}", (Object)instantiationException.getMessage());
            }
        }
        return arrayList;
    }

    @Override
    public int IO11O0LANd() {
        return this.OOOIilanD;
    }

    @Override
    public long l0iIlIO1laNd() {
        return this.lI00OlAND;
    }

    @Override
    public long iOIl0LAnD() {
        return this.lli0OiIlAND;
    }

    @Override
    public long iIiO00OLaNd() {
        return this.li0iOILAND;
    }

    @Override
    public long ii1li00Land() {
        return this.O1il1llOLANd;
    }

    @Override
    public long IOI1LaNd() {
        return this.Oill1LAnD;
    }

    @Override
    public long lI00ilAND() {
        return this.lIOILand;
    }

    @Override
    public long l0l00lAND() {
        return this.lil0liLand;
    }

    @Override
    public long iOl10IlLAnd() {
        return this.iilIi1laND;
    }

    @Override
    public long lIiIii1LAnD() {
        return this.lli011lLANd;
    }

    @Override
    public long II1Iland() {
        return this.l0illAND;
    }

    @Override
    public boolean liOIOOLANd() {
        int n2 = this.I1O1I1LaNd().getIndex();
        return this.I1O1I1LaNd(II1i1l0laND.I1O1I1LaNd(n2));
    }

    private boolean I1O1I1LaNd(Map map) {
        int n2 = this.I1O1I1LaNd().getIndex();
        if (map.containsKey(n2)) {
            OOOliOOllANd oOOliOOllANd = (OOOliOOllANd)map.get(n2);
            this.OOOIilanD = oOOliOOllANd.I1O1I1LaNd();
            this.lli0OiIlAND = oOOliOOllANd.lli0OiIlAND();
            this.lI00OlAND = oOOliOOllANd.li0iOILAND();
            this.O1il1llOLANd = oOOliOOllANd.OOOIilanD();
            this.li0iOILAND = oOOliOOllANd.lI00OlAND();
            this.lIOILand = oOOliOOllANd.O1il1llOLANd();
            this.Oill1LAnD = oOOliOOllANd.Oill1LAnD();
            this.iilIi1laND = oOOliOOllANd.lIOILand();
            this.lil0liLand = oOOliOOllANd.lil0liLand();
            this.lli011lLANd = oOOliOOllANd.iilIi1laND();
            this.l0illAND = oOOliOOllANd.lli011lLANd();
            return true;
        }
        return false;
    }
}

