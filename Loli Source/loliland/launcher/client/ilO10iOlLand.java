/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.IOKitUtil;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import loliland.launcher.client.IiiIOIlanD;
import loliland.launcher.client.O11i1I1lAnd;
import loliland.launcher.client.iOIiIIOOlaNd;
import loliland.launcher.client.iiIIIlO1lANd;
import loliland.launcher.client.lIl1OlAND;
import loliland.launcher.client.lOO1ILAnd;
import loliland.launcher.client.lii1IO0LaNd;
import loliland.launcher.client.ll1OLAnd;

final class ilO10iOlLand
extends IiiIOIlanD {
    private final Supplier I1O1I1LaNd = lii1IO0LaNd.I1O1I1LaNd(ilO10iOlLand::lil0liLand);

    ilO10iOlLand() {
    }

    @Override
    public String I1O1I1LaNd() {
        return (String)((O11i1I1lAnd)this.I1O1I1LaNd.get()).I1O1I1LaNd();
    }

    @Override
    public String OOOIilanD() {
        return (String)((O11i1I1lAnd)this.I1O1I1LaNd.get()).OOOIilanD();
    }

    @Override
    public String lI00OlAND() {
        return (String)((O11i1I1lAnd)this.I1O1I1LaNd.get()).lI00OlAND();
    }

    @Override
    public String lli0OiIlAND() {
        return (String)((O11i1I1lAnd)this.I1O1I1LaNd.get()).lli0OiIlAND();
    }

    @Override
    public lIl1OlAND Oill1LAnD() {
        return new iOIiIIOOlaNd();
    }

    @Override
    public ll1OLAnd lIOILand() {
        return new lOO1ILAnd();
    }

    private static O11i1I1lAnd lil0liLand() {
        String string = null;
        String string2 = null;
        String string3 = null;
        String string4 = null;
        IOKit.IOService iOService = IOKitUtil.getMatchingService("IOPlatformExpertDevice");
        if (iOService != null) {
            byte[] arrby = iOService.getByteArrayProperty("manufacturer");
            if (arrby != null) {
                string = Native.toString(arrby, StandardCharsets.UTF_8);
            }
            if ((arrby = iOService.getByteArrayProperty("model")) != null) {
                string2 = Native.toString(arrby, StandardCharsets.UTF_8);
            }
            string3 = iOService.getStringProperty("IOPlatformSerialNumber");
            string4 = iOService.getStringProperty("IOPlatformUUID");
            iOService.release();
        }
        return new O11i1I1lAnd(iiIIIlO1lANd.I1O1I1LaNd(string) ? "Apple Inc." : string, iiIIIlO1lANd.I1O1I1LaNd(string2) ? "unknown" : string2, iiIIIlO1lANd.I1O1I1LaNd(string3) ? "unknown" : string3, iiIIIlO1lANd.I1O1I1LaNd(string4) ? "unknown" : string4);
    }
}

