/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.platform.win32.WinNT;
import java.util.HashMap;
import java.util.Map;
import loliland.launcher.client.O1O00OllAND;
import loliland.launcher.client.i00ilaNd;
import loliland.launcher.client.l1Oili01LaND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class il000IOOlaNd
implements AutoCloseable {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(il000IOOlaNd.class);
    private Map OOOIilanD = new HashMap();
    private WinNT.HANDLEByReference lI00OlAND = null;

    public boolean I1O1I1LaNd(l1Oili01LaND l1Oili01LaND2) {
        if (this.lI00OlAND == null) {
            this.lI00OlAND = new WinNT.HANDLEByReference();
            if (!O1O00OllAND.OOOIilanD(this.lI00OlAND)) {
                I1O1I1LaNd.warn("Failed to open a query for PDH counter: {}", (Object)l1Oili01LaND2.lli0OiIlAND());
                this.lI00OlAND = null;
                return false;
            }
        }
        WinNT.HANDLEByReference hANDLEByReference = new WinNT.HANDLEByReference();
        if (!O1O00OllAND.I1O1I1LaNd(this.lI00OlAND, l1Oili01LaND2.lli0OiIlAND(), hANDLEByReference)) {
            I1O1I1LaNd.warn("Failed to add counter for PDH counter: {}", (Object)l1Oili01LaND2.lli0OiIlAND());
            return false;
        }
        this.OOOIilanD.put(l1Oili01LaND2, hANDLEByReference);
        return true;
    }

    public boolean OOOIilanD(l1Oili01LaND l1Oili01LaND2) {
        boolean bl = false;
        WinNT.HANDLEByReference hANDLEByReference = (WinNT.HANDLEByReference)this.OOOIilanD.remove(l1Oili01LaND2);
        if (hANDLEByReference != null) {
            bl = O1O00OllAND.li0iOILAND(hANDLEByReference);
        }
        if (this.OOOIilanD.isEmpty()) {
            O1O00OllAND.lI00OlAND(this.lI00OlAND);
            this.lI00OlAND = null;
        }
        return bl;
    }

    public void I1O1I1LaNd() {
        for (WinNT.HANDLEByReference hANDLEByReference : this.OOOIilanD.values()) {
            O1O00OllAND.li0iOILAND(hANDLEByReference);
        }
        this.OOOIilanD.clear();
        if (this.lI00OlAND != null) {
            O1O00OllAND.lI00OlAND(this.lI00OlAND);
        }
        this.lI00OlAND = null;
    }

    public long OOOIilanD() {
        if (this.lI00OlAND == null) {
            I1O1I1LaNd.warn("Query does not exist to update.");
            return 0L;
        }
        return O1O00OllAND.I1O1I1LaNd(this.lI00OlAND);
    }

    public long lI00OlAND(l1Oili01LaND l1Oili01LaND2) {
        if (!this.OOOIilanD.containsKey(l1Oili01LaND2)) {
            if (I1O1I1LaNd.isWarnEnabled()) {
                I1O1I1LaNd.warn("Counter {} does not exist to query.", (Object)l1Oili01LaND2.lli0OiIlAND());
            }
            return 0L;
        }
        long l2 = O1O00OllAND.lli0OiIlAND((WinNT.HANDLEByReference)this.OOOIilanD.get(l1Oili01LaND2));
        if (l2 < 0L) {
            if (I1O1I1LaNd.isWarnEnabled()) {
                I1O1I1LaNd.warn("Error querying counter {}: {}", (Object)l1Oili01LaND2.lli0OiIlAND(), (Object)String.format(i00ilaNd.lI00OlAND((int)l2), new Object[0]));
            }
            return 0L;
        }
        return l2;
    }

    @Override
    public void close() {
        this.I1O1I1LaNd();
    }
}

