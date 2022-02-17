/*
 * Decompiled with CFR 0.150.
 */
package loliland.launcher.client;

import com.sun.jna.Native;
import com.sun.jna.ptr.PointerByReference;
import loliland.launcher.client.IiI1l1IllANd;
import loliland.launcher.client.Iil1Ol1laNd;
import loliland.launcher.client.Iill1lanD;
import loliland.launcher.client.l0liOI11laND;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class liiIlanD
extends IiI1l1IllANd {
    private static final Logger I1O1I1LaNd = LoggerFactory.getLogger(liiIlanD.class);
    private static final Iil1Ol1laNd OOOIilanD = Iil1Ol1laNd.INSTANCE;

    liiIlanD() {
    }

    @Override
    public String I1O1I1LaNd() {
        PointerByReference pointerByReference;
        l0liOI11laND l0liOI11laND2 = new l0liOI11laND();
        l0liOI11laND2.ai_flags = 2;
        String string = this.OOOIilanD();
        int n2 = OOOIilanD.getaddrinfo(string, null, l0liOI11laND2, pointerByReference = new PointerByReference());
        if (n2 > 0) {
            if (I1O1I1LaNd.isErrorEnabled()) {
                I1O1I1LaNd.warn("Failed getaddrinfo(): {}", (Object)OOOIilanD.gai_strerror(n2));
            }
            return "";
        }
        l0liOI11laND l0liOI11laND3 = new l0liOI11laND(pointerByReference.getValue());
        String string2 = l0liOI11laND3.ai_canonname.trim();
        OOOIilanD.freeaddrinfo(pointerByReference.getValue());
        return string2;
    }

    @Override
    public String OOOIilanD() {
        byte[] arrby = new byte[256];
        if (0 != OOOIilanD.gethostname(arrby, arrby.length)) {
            return super.OOOIilanD();
        }
        return Native.toString(arrby);
    }

    @Override
    public String lli0OiIlAND() {
        return liiIlanD.I1O1I1LaNd(Iill1lanD.I1O1I1LaNd("route -4 get default"));
    }

    @Override
    public String li0iOILAND() {
        return liiIlanD.I1O1I1LaNd(Iill1lanD.I1O1I1LaNd("route -6 get default"));
    }
}

